/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.service.MailService;

/**
 * @author abhinav
 *
 */
@Service
@Qualifier("CandidateSkillMatcher")
@Transactional
public class CandidateSkillMatcher implements Matcher<Long> {
	
	private final Logger log = LoggerFactory.getLogger(CandidateSkillMatcher.class);

	private final JobRepository jobRepository;
	private final MatchUtils matchUtils;
	private final CandidateRepository candidateRepository;
	private final CandidateEducationRepository candidateEducationRepository;
	private final MailService mailService;
	
	public CandidateSkillMatcher (JobRepository jobRepository, MatchUtils matchUtils,
			CandidateRepository candidateRepository, CandidateEducationRepository candidateEducationRepository,MailService mailService) {
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
		this.candidateEducationRepository = candidateEducationRepository;
		this.mailService = mailService;
	}
	
	public void match(Long candidateId) {
		Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
		if(!candidateOptional.isPresent())
			return;
		final Candidate candidate = candidateOptional.get();
		CandidateEducation candidateEducation = candidateEducationRepository
				.findByCandidateAndHighestQualification(candidate, true);
		if (candidateEducation != null) {
			Stream<Job> activeJobs = jobRepository.findAllActiveJobsForMatchingAsStream();
			Set<CandidateJob> candidateJobs = new HashSet<>();
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
			log.debug("Skills before save are {}", candidate.getCandidateSkills());
			candidateRepository.save(candidate);
			mailService.sendMatchedJobEmailToCandidate(candidate.getLogin(), new Long(candidate.getCandidateJobs().size()),candidate.getFirstName());
		} else {
			log.info("Abort Matching as no Education saved");
		}
		
	}

	private CandidateJob beginMatching(Job job, Candidate candidate) {
		log.debug("Matching on {}", job.getId());
		JobFilterObject jobFilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		CandidateJob candidateJob = null;
		CandidateJob matchedCandidateJob = new CandidateJob(candidate, job);
		if (candidate.getCandidateJobs().contains(matchedCandidateJob))
			candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, false, false, false,true);
		return candidateJob;
	}

}
