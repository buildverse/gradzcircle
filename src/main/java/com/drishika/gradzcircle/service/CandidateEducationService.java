package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.service.matching.Matcher;

@Service
@Transactional
public class CandidateEducationService {

	private final Logger log = LoggerFactory.getLogger(CandidateEducationService.class);

	private final CandidateEducationRepository candidateEducationRepository;
	private final CandidateRepository candidateRepository;
	private final CandidateProjectRepository candidateProjectRepository;
	private final CollegeRepository collegeRepository;
	private final QualificationRepository qualififcationRepository;
	private final CourseRepository courseRepository;
	private final UniversityRepository universityRepository;
	private final CandidateEducationSearchRepository candidateEducationSearchRepository;
	private final ElasticsearchTemplate elasticsearchTemplate;

	//@Qualifier("CandidateEducationMatcher")
	private final Matcher<Candidate> matcher;

	public CandidateEducationService(CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository, CollegeRepository collegeRepository,
			QualificationRepository qualififcationRepository, CourseRepository courseRepository,
			UniversityRepository universityRepository, @Qualifier("CandidateEducationMatcher")Matcher<Candidate> matcher,
		
			UniversitySearchRepository universitySearchRepository, ElasticsearchTemplate elasticsearchTemplate,CandidateRepository candidateRepository) {
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.collegeRepository = collegeRepository;
		this.universityRepository = universityRepository;
		this.courseRepository = courseRepository;
		this.qualififcationRepository = qualififcationRepository;
		this.matcher = matcher;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.candidateRepository = candidateRepository;
	}

	private void setGrade(CandidateEducation candidateEducation) {
		String gradeMajorUnit = candidateEducation.getRoundOfGrade().toString();
		String gradeMinorUnit = null;
		if(candidateEducation.getGradeDecimal()!=null)
			gradeMinorUnit = candidateEducation.getGradeDecimal().toString();
		else {
			gradeMinorUnit = "0";
		}
		log.debug("Grade Decimla is {}",gradeMinorUnit);
		candidateEducation.setGrade(new Double(gradeMajorUnit + "." + gradeMinorUnit));
	}

	public CandidateEducation createCandidateEducation(CandidateEducation candidateEducation) {
		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		injestCollegeInformation(candidateEducation);
		injestCourseInformation(candidateEducation);
		injestQualificationInformation(candidateEducation);
		log.info("Creating education for candidate, course,qualification {},{},{}", candidateEducation.getCandidate(),
				candidateEducation.getCourse(), candidateEducation.getQualification());
		setHighestEducation(candidateEducation,false);
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		log.debug("CandidateJobs post save candidateEducation is {}",result.getCandidate().getCandidateJobs());
		log.debug("Highest Qulaification {}",result.getHighestQualification());
		Candidate candidate = candidateRepository.findOne(candidateEducation.getCandidate().getId());
		if(result.getHighestQualification())
			matcher.match(candidate.addEducation(result));
		//updateEducationDependentMetaForDisplay(result);
		return result;
	}
	
	/*private List<CandidateEducation> setHighestEducation(CandidateEducation candidateEducation) {
		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(candidateEducation.getCandidate().getId());
		if(candidateEducations.size()>0)
			candidateEducations.forEach(education->{
				if(candidateEducation.getEducationToDate()!=null) {
					if((education.getEducationToDate().isAfter(candidateEducation.getEducationToDate())) && !(candidateEducation.isHighestQualification())) {
						education.setHighestQualification(true);
						candidateEducation.setHighestQualification(false);
					}				
					else {
						candidateEducation.setHighestQualification(true);
						education.setHighestQualification(false);
					}
				} else if(candidateEducation.getIsPursuingEducation()) {
					candidateEducation.setHighestQualification(true);
					education.setHighestQualification(false);
				}
				
					
			});
		else 
			candidateEducation.setHighestQualification(true);
		return candidateEducations;
	}
	*/
	private Set<CandidateEducation> setHighestEducation(CandidateEducation candidateEducation, Boolean isDelete) {
		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(candidateEducation.getCandidate().getId());
		if(isDelete) {
			candidateEducations.remove(candidateEducation);
			if(candidateEducations.size()>0) {
				candidateEducations.sort((education1, education2)-> education1.getEducationToDate().compareTo(education2.getEducationToDate()));
				candidateEducations.get(candidateEducations.size()-1).setHighestQualification(true);
				log.debug("Soretd education by dates {}",candidateEducations);
			}
		}
		else {
			if(candidateEducations.size()>0)
				candidateEducations.forEach(education->{
					if(candidateEducation.getEducationToDate()!=null) {
						if((education.getEducationToDate().isAfter(candidateEducation.getEducationToDate())) && !(candidateEducation.isHighestQualification())) {
							education.setHighestQualification(true);
							candidateEducation.setHighestQualification(false);
						}				
						else {
							candidateEducation.setHighestQualification(true);
							education.setHighestQualification(false);
						}
					} else if(candidateEducation.getIsPursuingEducation()) {
						candidateEducation.setHighestQualification(true);
						education.setHighestQualification(false);
					}	
				});
			else 
				candidateEducation.setHighestQualification(true);
		}
		return new HashSet<>(candidateEducations);
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
		candidateEducationRepository.save(setHighestEducation(candidateEducation,false));
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		//candidateEducationSearchRepository.save(result);
		Candidate candidate = candidateRepository.findOne(candidateEducation.getCandidate().getId());
		candidate.addEducation(result);
		// Replace with future
		if(result.getHighestQualification())
			matcher.match(candidate);
		//updateEducationDependentMetaForDisplay(result);
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
		//	updateEducationDependentMetaForDisplay(candidateEducation);
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
				candidateEducation.setProjects(candidateProjects);
			});
		}
		return candidateEducations;
	}

	public void deleteCandidateEducation(Long id) {
		CandidateEducation education =  candidateEducationRepository.findOne(id);
		Candidate candidate = education.getCandidate();	
		log.debug("Canddtae from educaiton is {} ",candidate.getEducations());
		candidate.getEducations().remove(education);
		log.debug("Educaiton set post removing education is {}",candidate.getEducations());
		candidate.getEducations().addAll((setHighestEducation(education,true)));
		matcher.match(candidate);
		//candidateEducationSearchRepository.delete(id);
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

	

	private void updateUniversityIndex(University university) {
		com.drishika.gradzcircle.domain.elastic.University universityElasticInstance = new com.drishika.gradzcircle.domain.elastic.University();
		try {
			BeanUtils.copyProperties(universityElasticInstance, university);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for university elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
			// @TODO - SEND EMAIL Alert
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for university elastic instance", e);
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
			log.error("Error copying bean for qualification elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for qualification elastic instance", e);
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
			log.error("Error copying bean for course elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for course elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate
				.index(new CourseEntityBuilder(courseElasticInstance.getId()).name(courseElasticInstance.getCourse())
						.suggest(new String[] { courseElasticInstance.getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}
	
	//If lnaguage is Other and saving new Language. Currently not suppoterd. And should be in Candidate LanguageService calss.
	private void updateLanguageIndex(Language language) {
		com.drishika.gradzcircle.domain.elastic.Language languageElasticInstance = new com.drishika.gradzcircle.domain.elastic.Language();
		try {
			BeanUtils.copyProperties(languageElasticInstance, language);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for language elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for language elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate
				.index(new CourseEntityBuilder(languageElasticInstance.getId()).name(languageElasticInstance.getLanguage())
						.suggest(new String[] { languageElasticInstance.getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
	}
	
	public Stream<CandidateEducation> getCandidateEducationBeforeSuppliedDate(LocalDate date) {
		return candidateEducationRepository.findByEducationToDateBeforeAndHighestQualification(date,true);
	}
	
	public Stream<CandidateEducation> getCandidateEducationAfterSuppliedDate(LocalDate date) {
		return candidateEducationRepository.findByEducationToDateAfterAndHighestQualification(date,true);
	}
	
	public Stream<CandidateEducation> getCandidateEducationBetweenSuppliedDates(LocalDate fromDate,LocalDate toDate) {
		return candidateEducationRepository.findByEducationToDateBetweenAndHighestQualification(fromDate, toDate,true);
	}
	
	public Stream<CandidateEducation> getEducationForMatchEligibleCandidate(){
		return candidateEducationRepository.findAllHighestCandidateEducationForMatchEligilbeCandidates();
	}
}
