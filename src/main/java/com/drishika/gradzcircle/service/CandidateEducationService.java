package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
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
	private final CandidateEducationSearchRepository candidateEducationSearchRepository;

	@Qualifier("CandidateEducationMatcher")
	private final Matcher<CandidateEducation> jobMatcher;

	public CandidateEducationService(CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository, CollegeRepository collegeRepository,
			QualificationRepository qualififcationRepository, CourseRepository courseRepository,
			UniversityRepository universityRepository, Matcher<CandidateEducation> jobMatcher) {
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.collegeRepository = collegeRepository;
		this.universityRepository = universityRepository;
		this.courseRepository = courseRepository;
		this.qualififcationRepository = qualififcationRepository;
		this.jobMatcher = jobMatcher;

	}

	private void setGrade(CandidateEducation candidateEducation) {
		String gradeMajorUnit = candidateEducation.getRoundOfGrade().toString();
		String gradeMinorUnit = candidateEducation.getGradeDecimal().toString();
		candidateEducation.setGrade(new Double(gradeMajorUnit + "." + gradeMinorUnit));
	}

	public CandidateEducation createCandidateEducation(CandidateEducation candidateEducation) {
		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		if(candidateEducation.getCapturedCollege() != null)
			massageCollegeInformation(candidateEducation);
		massageCourseInformation(candidateEducation);
		massageQualificationInformation(candidateEducation);
		log.debug("Creating education for candidate {}", candidateEducation.getCandidate());
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		candidateEducationSearchRepository.save(result);
		// Replace with future
		jobMatcher.match(result);
		// updateEducationDependentMetaForDisplay(result);
		return result;
	}

	private void massageCollegeInformation(CandidateEducation candidateEducation) {
		log.debug("Incoming Education is  {} , and college is {}  ", candidateEducation, candidateEducation.getCollege());
		String capturedCollegeName = candidateEducation.getCapturedCollege();
		String capturedUniversityName = candidateEducation.getCapturedUniversity();
		College capturedCollege = collegeRepository.findByCollegeName(capturedCollegeName);
		University capturedUniversity = universityRepository.findByUniversityName(capturedUniversityName);
		Boolean capturedCollegeExist = false;
		Boolean capturedUninversityExist = false;
		if(capturedCollege != null)
		 capturedCollegeExist = true;
		if(capturedUniversity != null)
			capturedUninversityExist = true;
		
		if(capturedCollegeExist && capturedUninversityExist) {
			if(capturedCollege.getUniversity().equals(capturedUniversity)) {
				candidateEducation.setCollege(capturedCollege);
			} else {
				
			}
		}
		College college = collegeRepository.findByCollegeName(candidateEducation.getCollege().getCollegeName());
		if (candidateEducation.getCollege().getCollegeName().equalsIgnoreCase(Constants.OTHER)) {
		
			if(capturedCollegeExist && capturedUninversityExist) {
				candidateEducation.setCollege(college);
			}
				
			else if (! capturedCollegeExist && ! capturedUninversityExist) {
				College newCollege = new College();
				Set<College> colleges = new HashSet<College>();
				newCollege.setCollegeName(candidateEducation.getCapturedCollege());
				colleges.add(newCollege);
				University newUniversity = new University();
				newUniversity.setUniversityName(candidateEducation.getCapturedUniversity());
				newCollege.setUniversity(newUniversity);
				newUniversity.colleges(colleges);
				universityRepository.save(newUniversity);
				log.info("Saved new College {}  and University {} ", newCollege.getCollegeName(),
						newUniversity.getUniversityName());
			} 

		}
		log.debug("Looked up college details {}", college);
		
	}



	private void massageQualificationInformation(CandidateEducation candidateEducation) {
		Qualification qualification = qualififcationRepository
				.findByQualification(candidateEducation.getQualification().getQualification());
		if (candidateEducation.getQualification().getQualification().equals(Constants.OTHER)) {
			Qualification newQualification = new Qualification();
			newQualification.setQualification(candidateEducation.getCapturedQualification());
			qualififcationRepository.save(newQualification);
			log.info("Saved new Qualifcation {} ", qualification.getQualification());
		}
		log.debug("Looked up qualification details {}", qualification);
		candidateEducation.setQualification(qualification);
	}

	private void massageCourseInformation(CandidateEducation candidateEducation) {
		Course course = courseRepository.findByCourse(candidateEducation.getCourse().getCourse());
		if (candidateEducation.getCourse().getCourse().equals(Constants.OTHER)) {
			Course newCourse = new Course();
			newCourse.setCourse(candidateEducation.getCapturedCourse());
			courseRepository.save(newCourse);
			log.info("Saved new Course {} ", course.getCourse());
		}
		log.debug("Looked up course details {}", course);
		candidateEducation.setCourse(course);
	}

	public CandidateEducation updateCandidateEductaion(CandidateEducation candidateEducation) {

		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		candidateEducation.setProjects(null);
		massageCollegeInformation(candidateEducation);
		massageCourseInformation(candidateEducation);
		massageQualificationInformation(candidateEducation);
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

}
