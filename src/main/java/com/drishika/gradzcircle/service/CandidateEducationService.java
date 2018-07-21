package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.CollegeSearchRepository;
import com.drishika.gradzcircle.repository.search.CourseSearchRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.service.matching.Matcher;

@Service
public class CandidateEducationService {

	private final Logger log = LoggerFactory.getLogger(CandidateEducationService.class);

	private final CandidateEducationRepository candidateEducationRepository;
	private final CandidateProjectRepository candidateProjectRepository;
	private final CollegeRepository collegeRepository;
	private final QualificationRepository qualififcationRepository;
	private final CourseRepository courseRepository;
	private final UniversityRepository universityRepository;
	private final UniversitySearchRepository universitySearchRepository;
	private final CollegeSearchRepository collegeSearchRepository;
	private final QualificationSearchRepository qualificationSearchRepository;
	private final CourseSearchRepository courseSearchRepository;
	private final CandidateEducationSearchRepository candidateEducationSearchRepository;
	private final ElasticsearchTemplate elasticsearchTemplate;

	@Qualifier("CandidateEducationMatcher")
	private final Matcher<CandidateEducation> jobMatcher;

	public CandidateEducationService(CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository, CollegeRepository collegeRepository,
			QualificationRepository qualififcationRepository, CourseRepository courseRepository,
			UniversityRepository universityRepository, Matcher<CandidateEducation> jobMatcher,
			CollegeSearchRepository collegeSearchRepository,
			QualificationSearchRepository qualificationSearchRepository, CourseSearchRepository courseSearchRepository,
			UniversitySearchRepository universitySearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.collegeRepository = collegeRepository;
		this.universityRepository = universityRepository;
		this.courseRepository = courseRepository;
		this.qualififcationRepository = qualififcationRepository;
		this.jobMatcher = jobMatcher;
		this.collegeSearchRepository = collegeSearchRepository;
		this.universitySearchRepository = universitySearchRepository;
		this.courseSearchRepository = courseSearchRepository;
		this.qualificationSearchRepository = qualificationSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	private void setGrade(CandidateEducation candidateEducation) {
		String gradeMajorUnit = candidateEducation.getRoundOfGrade().toString();
		String gradeMinorUnit = candidateEducation.getGradeDecimal().toString();
		candidateEducation.setGrade(new Double(gradeMajorUnit + "." + gradeMinorUnit));
	}

	public CandidateEducation createCandidateEducation(CandidateEducation candidateEducation) {
		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		injestCollegeInformation(candidateEducation);
		injestCourseInformation(candidateEducation);
		injestQualificationInformation(candidateEducation);
		log.debug("Creating education for candidate, course,qualification {},{},{}", candidateEducation.getCandidate(),
				candidateEducation.getCourse(), candidateEducation.getQualification());
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		candidateEducationSearchRepository.save(result);
		// Replace with future
		jobMatcher.match(result);
		updateEducationDependentMetaForDisplay(result);
		return result;
	}

	private void injestCollegeInformation(CandidateEducation candidateEducation) {
		log.debug("Incoming Education is  {} , and college is {}  ", candidateEducation,
				candidateEducation.getCollege());
		if (candidateEducation.getCollege().getCollegeName().equals(Constants.OTHER)) {
			College college = collegeRepository.findByCollegeName(candidateEducation.getCapturedCollege());
			University university = universityRepository
					.findByUniversityName(candidateEducation.getCapturedUniversity());
			University newUniversity = null;
			if (university == null) {
				newUniversity = new University().universityName(candidateEducation.getCapturedUniversity());
				updateUniversityIndex(universityRepository.save(newUniversity));
				log.info("Created new University {}", newUniversity);
			}
			if (college == null) {
				College newCollege = new College().collegeName(candidateEducation.getCapturedCollege());
				if (university == null) {
					newCollege.university(newUniversity);
				} else {
					newCollege.university(university);
				}
				updateCollegeIndex(collegeRepository.save(newCollege));
				log.info("Created new College {}", newCollege);
				candidateEducation.setCollege(newCollege);

			} else {
				candidateEducation.setCollege(college);
			}

		} else {
			candidateEducation
					.setCollege(collegeRepository.findByCollegeName(candidateEducation.getCollege().getCollegeName()));
		}

	}

	private void injestQualificationInformation(CandidateEducation candidateEducation) {

		if (candidateEducation.getQualification().getQualification().equals(Constants.OTHER)) {
			Qualification qualification = qualififcationRepository
					.findByQualification(candidateEducation.getCapturedQualification());
			if (qualification == null) {
				Qualification newQualification = new Qualification();
				newQualification.setQualification(candidateEducation.getCapturedQualification());
				updateQualificationIndex(qualififcationRepository.save(newQualification));
				log.info("Saved new Qualifcation {} ", newQualification.getQualification());
				candidateEducation.setQualification(newQualification);
			} else {
				candidateEducation.setQualification(qualification);
			}
		} else {
			candidateEducation.setQualification(qualififcationRepository
					.findByQualification(candidateEducation.getQualification().getQualification()));
		}
	}

	private void injestCourseInformation(CandidateEducation candidateEducation) {
		if (candidateEducation.getCourse().getCourse().equals(Constants.OTHER)) {
			Course course = courseRepository.findByCourse(candidateEducation.getCapturedCourse());
			if (course == null) {
				Course newCourse = new Course();
				newCourse.setCourse(candidateEducation.getCapturedCourse());
				updateCourseIndex(courseRepository.save(newCourse));
				log.info("Saved new Course {} ", newCourse.getCourse());
				candidateEducation.setCourse(newCourse);
			} else {
				candidateEducation.setCourse(course);
			}
		} else {
			candidateEducation.setCourse(courseRepository.findByCourse(candidateEducation.getCourse().getCourse()));
		}

	}

	public CandidateEducation updateCandidateEductaion(CandidateEducation candidateEducation) {

		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		candidateEducation.setProjects(null);
		injestCollegeInformation(candidateEducation);
		injestCourseInformation(candidateEducation);
		injestQualificationInformation(candidateEducation);
		/* SHOULD WE ALLOW MULTIPLE HIGHEST QUALIFICATIONS ? - RUCHI SAYS YES */
		/*
		 * CandidateEducation educationWithHighestQualification =
		 * candidateEducationRepository.findByCandidateAndHighestQualification(
		 * candidateEducation.getCandidate(),Boolean.TRUE);
		 * if(educationWithHighestQualification!=null &&
		 * !educationWithHighestQualification.getId().equals(candidateEducation.getId())
		 * && candidateEducation.isHighestQualification()){ throw new
		 * CustomParameterizedException("You cannot have two highest qualifications"); }
		 * 
		 * log.debug("This candidate has a highest education already {}",
		 * educationWithHighestQualification);
		 */
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		candidateEducationSearchRepository.save(result);
		// Replace with future
		jobMatcher.match(result);
		updateEducationDependentMetaForDisplay(result);
		return result;
	}

	public List<CandidateEducation> getAllCandidateEducations() {
		log.debug("REST request to get all CandidateEducations");
		return candidateEducationRepository.findAll();
	}

	public CandidateEducation getCandidateEducation(Long id) {
		log.debug("Getting candidate Education for {}", id);
		CandidateEducation candidateEducation = candidateEducationRepository.findOne(id);
		if (candidateEducation != null) {
			Set<CandidateProject> candidateProjects = candidateProjectRepository.findByEducation(candidateEducation);
			candidateEducation.setProjects(candidateProjects);
			updateEducationDependentMetaForDisplay(candidateEducation);
			log.debug("College data is {}", candidateEducation.getCollege());
		}

		return candidateEducation;
	}

	public List<CandidateEducation> getEducationByCandidateId(Long id) {

		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(id);
		if (candidateEducations != null) {
			candidateEducations.forEach(candidateEducation -> {
				Set<CandidateProject> candidateProjects = candidateProjectRepository
						.findByEducation(candidateEducation);
				candidateProjects.forEach(candidateProject -> {
					candidateProject.setEducation(null);
				});
				candidateEducation.setProjects(candidateProjects);
				candidateEducation.setCandidate(null);
				if (candidateEducation.getCollege().getUniversity() != null) {
					candidateEducation.getCollege().getUniversity().setCountry(null);
				}
				updateEducationDependentMetaForDisplay(candidateEducation);
				log.debug("College data is {}", candidateEducation.getCollege());
			});
		}
		return candidateEducations;
	}

	public void deleteCandidateEducation(Long id) {
		candidateEducationRepository.delete(id);
		candidateEducationSearchRepository.delete(id);
	}

	public List<CandidateEducation> searchCandidateEducations(String query) {
		List<CandidateEducation> candidateEducations = StreamSupport
				.stream(candidateEducationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		return candidateEducations;
	}

	public List<CandidateEducation> searchCandidateEducationsOrderedByToDate(String query) {
		return candidateEducationSearchRepository.findByCandidateIdOrderByEducationToDateDesc(query);
	}

	private void updateEducationDependentMetaForDisplay(CandidateEducation candidateEducation) {
		candidateEducation.getCollege().setDisplay(candidateEducation.getCollege().getCollegeName());
		candidateEducation.getCollege().setValue(candidateEducation.getCollege().getCollegeName());
		candidateEducation.getQualification().setDisplay(candidateEducation.getQualification().getQualification());
		candidateEducation.getQualification().setValue(candidateEducation.getQualification().getQualification());
		candidateEducation.getCollege().getUniversity()
				.setDisplay(candidateEducation.getCollege().getUniversity().getUniversityName());
		candidateEducation.getCollege().getUniversity()
				.setValue(candidateEducation.getCollege().getUniversity().getUniversityName());
		candidateEducation.getCourse().setDisplay(candidateEducation.getCourse().getCourse());
		candidateEducation.getCourse().setValue(candidateEducation.getCourse().getCourse());
	}

	private void updateUniversityIndex(University university) {
		com.drishika.gradzcircle.domain.elastic.University universityElasticInstance = new com.drishika.gradzcircle.domain.elastic.University();
		try {
			BeanUtils.copyProperties(universityElasticInstance, university);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
			// @TODO - SEND EMAIL Alert
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new UniversityEntityBuilder(universityElasticInstance.getId())
				.name(universityElasticInstance.getUniversityName())
				.suggest(new String[] { universityElasticInstance.getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
	}

	private void updateCollegeIndex(College college) {
		com.drishika.gradzcircle.domain.elastic.College collegeElasticInstance = new com.drishika.gradzcircle.domain.elastic.College();
		try {
			BeanUtils.copyProperties(collegeElasticInstance, college);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(
				new CollegeEntityBuilder(collegeElasticInstance.getId()).name(collegeElasticInstance.getCollegeName())
						.suggest(new String[] { collegeElasticInstance.getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
	}

	private void updateQualificationIndex(Qualification qualification) {
		com.drishika.gradzcircle.domain.elastic.Qualification qualificationElasticInstance = new com.drishika.gradzcircle.domain.elastic.Qualification();
		try {
			BeanUtils.copyProperties(qualificationElasticInstance, qualification);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new QualificationEntityBuilder(qualificationElasticInstance.getId())
				.name(qualificationElasticInstance.getQualification())
				.suggest(new String[] { qualificationElasticInstance.getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
	}

	private void updateCourseIndex(Course course) {
		com.drishika.gradzcircle.domain.elastic.Course courseElasticInstance = new com.drishika.gradzcircle.domain.elastic.Course();
		try {
			BeanUtils.copyProperties(courseElasticInstance, course);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate
				.index(new CourseEntityBuilder(courseElasticInstance.getId()).name(courseElasticInstance.getCourse())
						.suggest(new String[] { courseElasticInstance.getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}
}
