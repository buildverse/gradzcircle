/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.service.MailService;
import com.drishika.gradzcircle.web.websocket.dto.MatchActivityDTO;

/**
 * @author abhinav
 *
 */
@Component
@Qualifier("CandidateGenderMatcher")
@Transactional
public class CandidateGenderMatcher implements Matcher<Candidate> {
	private final Logger log = LoggerFactory.getLogger(CandidateGenderMatcher.class);
	private final JobRepository jobRepository;
	private final MatchUtils matchUtils;
	private final CandidateRepository candidateRepository;
	private final CandidateEducationRepository candidateEducationRepository;
	private final MailService mailService;
	
	public CandidateGenderMatcher(JobRepository jobRepository, MatchUtils matchUtils,
			CandidateRepository candidateRepository, CandidateEducationRepository candidateEducationRepository, MailService mailService) {
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
		this.candidateEducationRepository = candidateEducationRepository;
		this.mailService = mailService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.drishika.gradzcircle.service.matching.Matcher#match(java.lang.Object)
	 */
	@Override
	//I REMOVED PARALLEL STREAM TO ENABLE TESTS
	public void match(Candidate candidate) {
		CandidateEducation candidateEducation = candidateEducationRepository
				.findByCandidateAndHighestQualification(candidate, true);
		if (candidateEducation != null) {
			Stream<Job> activeJobs = jobRepository.findAllActiveJobsForMatchingAsStream();
			Set<CandidateJob> candidateJobs = null;
			matchUtils.populateJobFilterWeightMap();
			candidateJobs = activeJobs.map(job -> beginMatching(job, candidate))
					.filter(candidateJob -> candidateJob != null).collect(Collectors.toSet());
			candidateJobs.forEach(candidateJob -> {
				if (candidate.getCandidateJobs().contains(candidateJob)) {
					candidate.getCandidateJobs().remove(candidateJob);
					candidate.getCandidateJobs().add(candidateJob);
				} else {
					candidate.getCandidateJobs().add(candidateJob);
				}
			});
			candidateRepository.save(candidate);
			mailService.sendMatchedJobEmailToCandidate(candidate.getLogin(), new Long(candidate.getCandidateJobs().size()));

		} else {
			log.debug("Abort Matching as no Education saved");
		}
		
   
	}

	private CandidateJob beginMatching(Job job, Candidate candidate) {
		log.debug("Matching on {}", job.getId());
		JobFilterObject jobFilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		CandidateJob candidateJob = null;
		candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, false, false, true,false);
		return candidateJob;
	}

}
