/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.web.websocket.dto.MatchActivityDTO;

/**
 * @author abhinav
 *
 */
@Component
@Qualifier("CandidateEducationMatcher")
@Transactional
public class CandidateEducationMatcher implements Matcher<Candidate> {
	
	private final Logger log = LoggerFactory.getLogger(CandidateEducationMatcher.class);

	private final JobRepository jobRepository;
	private final MatchUtils matchUtils;
	private final CandidateRepository candidateRepository;
	private final CandidateEducationRepository candidateEducationRepository;
	
	public CandidateEducationMatcher(JobRepository jobRepository, MatchUtils matchUtils, CandidateRepository candidateRepository,
			CandidateEducationRepository candidateEducationRepository) {
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
		this.candidateEducationRepository = candidateEducationRepository;
	}
	
	/* (non-Javadoc)
	 * @see com.drishika.gradzcircle.service.matching.Matcher#match(java.lang.Object)
	 */
	@Override
	public void match(Candidate candidate) {
		Stream<Job> activeJobs = jobRepository.findAllActiveJobsForMatchingAsStream();
		Set<CandidateJob> candidateJobs = new HashSet<>();
		matchUtils.populateJobFilterWeightMap();
		if(candidate.getEducations()!=null && candidate.getEducations().size()>0)
			candidateJobs = activeJobs.parallel().map(job -> beginMatching(job, candidate)).filter(candidateJob -> candidateJob!=null).collect(Collectors.toSet());
		log.debug("Got CandidateJobs post Match as {} and is empty {}",candidateJobs,candidateJobs.isEmpty());
		if(candidateJobs.isEmpty()) {
			candidate.getCandidateJobs().clear();
		}
		else {
			candidateJobs.forEach(candidateJob -> {
				if(candidate.getCandidateJobs().contains(candidateJob)) {
					candidate.getCandidateJobs().remove(candidateJob);
					candidate.getCandidateJobs().add(candidateJob);
					log.debug("In If");
				} else {
					candidate.getCandidateJobs().add(candidateJob);
					log.debug("In else");
				}
			});
		}
		log.debug("Status of education in candidate before save {}",candidate.getEducations());
		candidateRepository.save(candidate);
	
	}
	
	private CandidateJob beginMatching(Job job, Candidate candidate) {	
		log.debug("Matching on {}",job.getId());
		
		CandidateJob incomingCandidateJob = new CandidateJob(candidate,job);
		JobFilterObject jobFilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		CandidateJob candidateJob = null;
	//	log.debug("Candidate Educaiton to match is {}",candidate.getEducations().stream().filter(education->education.isHighestQualification()).findFirst());
		CandidateEducation candidateEducation = candidate.getEducations().stream().filter(education->education.getHighestQualification()!=null).filter(education->education.isHighestQualification()).findAny().orElse(null);
	
		if(candidateEducation != null && isCandidateEligibleByGraduationDate(candidateEducation, jobFilterObject)) {
			if(candidate.getCandidateJobs().stream().filter(incomingCandidateJob :: equals).findAny().isPresent()) {
				CandidateJob cJob = candidate.getCandidateJobs().stream().filter(incomingCandidateJob :: equals).findAny().get();
				if(candidate.getCandidateLanguageProficiencies().size()>0 && cJob.getLanguageMatchScore() == null 
						&& candidate.getGender()!=null && cJob.getGenderMatchScore() ==null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true);
				else if (candidate.getCandidateLanguageProficiencies().size()>0 && cJob.getLanguageMatchScore() != null 
						&& candidate.getGender()!=null && cJob.getGenderMatchScore() ==null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true);
				else if (candidate.getCandidateLanguageProficiencies().size()>0 && cJob.getLanguageMatchScore() == null 
						&& candidate.getGender()!=null && cJob.getGenderMatchScore() !=null )
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false);
				else 
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false);
			} else {
				if(candidate.getCandidateLanguageProficiencies().size()>0 && candidate.getGender()!=null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true);
				else if (candidate.getCandidateLanguageProficiencies().size()>0 && candidate.getGender()==null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false);
				else if (candidate.getCandidateLanguageProficiencies().size()==0 && candidate.getGender()!=null  )
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true);
				else 
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false);
			}
			
		} else {
			candidate.getCandidateJobs().remove(incomingCandidateJob);
		}
		return candidateJob;
	}
	
	private Boolean isCandidateEligibleByGraduationDate(CandidateEducation education, JobFilterObject jobFilterObject) {
		log.debug("Incoming education date is {} and filter object is {}",education.getEducationToDate(), jobFilterObject);
		if(jobFilterObject.getGraduationDateType()==null)
			return true;
		if(jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_GREATER)) {
			if(education.getEducationToDate().isAfter(jobFilterObject.getGraduationDate().getGraduationDate()) ||
					education.getEducationToDate().equals(jobFilterObject.getGraduationDate().getGraduationDate()))
				return true;
		} else if (jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_LESS)) {
			if(education.getEducationToDate().isBefore(jobFilterObject.getGraduationDate().getGraduationDate()) || 
					education.getEducationToDate().equals(jobFilterObject.getGraduationDate().getGraduationDate()))
				return true;
		} else if (jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_BETWEEN)) {
			if((education.getEducationToDate().isAfter(jobFilterObject.getGraduationFromDate().getGraduationDate()) || 
					education.getEducationToDate().equals(jobFilterObject.getGraduationFromDate().getGraduationDate())) && 
					(education.getEducationToDate().isBefore(jobFilterObject.getGraduationToDate().getGraduationDate()) ||  
							education.getEducationToDate().equals(jobFilterObject.getGraduationToDate().getGraduationDate())))
				return true;
		}
		return false;
	}

}
