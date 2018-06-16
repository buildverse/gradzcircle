package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.service.matching.CandidateMatcher;
import com.drishika.gradzcircle.service.matching.Matcher;

@Service
@Transactional
public class CandidateEducationService {

	private final Logger log = LoggerFactory.getLogger(CandidateEducationService.class);

	private final CandidateEducationRepository candidateEducationRepository;
	private final CandidateProjectRepository candidateProjectRepository;

	private final CandidateEducationSearchRepository candidateEducationSearchRepository;
	
	@Qualifier("CandidateEducationMatcher")
	private final Matcher<CandidateEducation> jobMatcher;

	public CandidateEducationService(CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository,Matcher<CandidateEducation> jobMatcher) {
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
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
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		candidateEducationSearchRepository.save(result);
		//Replace with future
		jobMatcher.match(result);
		return result;
	}

	public CandidateEducation updateCandidateEductaion(CandidateEducation candidateEducation) {

		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		candidateEducation.setProjects(null);
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
		//Replace with future
		jobMatcher.match(result);
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
				candidateEducation.getCollege().getUniversity().setCountry(null);
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

}
