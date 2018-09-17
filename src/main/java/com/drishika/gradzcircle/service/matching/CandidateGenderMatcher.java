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
	
	public CandidateGenderMatcher(JobRepository jobRepository, MatchUtils matchUtils, CandidateRepository candidateRepository,
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
		CandidateEducation candidateEducation = candidateEducationRepository.findByCandidateAndHighestQualification(candidate, true);
		if(candidateEducation != null) {
			Stream<Job> activeJobs = jobRepository.findAllActiveJobsForMatchingAsStream();
			Set<CandidateJob> candidateJobs = null;
			matchUtils.populateJobFilterWeightMap();
			candidateJobs = activeJobs.parallel().map(job -> beginMatching(job, candidate)).filter(candidateJob -> candidateJob!=null).collect(Collectors.toSet());
			candidateJobs.forEach(candidateJob -> {
				if(candidate.getCandidateJobs().contains(candidateJob)) {
					candidate.getCandidateJobs().remove(candidateJob);
					candidate.getCandidateJobs().add(candidateJob);
				} else {
					candidate.getCandidateJobs().add(candidateJob);
				}
			});
			
		}
		else {
			log.debug("Abort Matching as no Education saved");
		}
		candidateRepository.save(candidate);
	}
	
	private CandidateJob beginMatching(Job job, Candidate candidate) {	
		log.debug("Matching on {}",job.getId());
		JobFilterObject jobFilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		CandidateJob candidateJob = null;
		candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, false, false, true);
		return candidateJob;
	}

}
