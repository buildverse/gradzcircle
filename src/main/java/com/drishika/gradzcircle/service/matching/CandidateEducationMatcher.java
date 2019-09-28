/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.drishika.gradzcircle.service.MailService;

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
	private final MailService mailService;

	public CandidateEducationMatcher(JobRepository jobRepository, MatchUtils matchUtils,
			CandidateRepository candidateRepository, CandidateEducationRepository candidateEducationRepository,
			MailService mailService) {
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
	//REMOVED PARALLELE STREAM TO ENABLE TESTS
	public void match(Candidate candidate) {
		Stream<Job> activeJobs = jobRepository.findAllActiveJobsForMatchingAsStream();
		Set<CandidateJob> candidateJobs = new HashSet<>();
		//List<Job> jobs = new ArrayList<>();
		matchUtils.populateJobFilterWeightMap();
		if (candidate.getEducations() != null)
			candidateJobs = activeJobs.map(job -> beginMatching(job, candidate))
					.filter(candidateJob -> candidateJob != null).collect(Collectors.toSet());
		log.info("Got CandidateJobs post Match as {} and is empty {}", candidateJobs, candidateJobs.isEmpty());
		List<Job> jobs = new ArrayList<Job>();
		candidate.getCandidateJobs().clear();
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidate.getCandidateJobs().forEach(cJ-> {
			log.debug("Candidate Jobs are {}",cJ);
			Job job = cJ.getJob();
			if(job.getCandidateJobs().contains(cJ) ) {
				job.getCandidateJobs().remove(cJ);
				job.getCandidateJobs().add(cJ);
				log.debug("Replacing Candidate Job in Job Object{}",job);
				
			} else {
				job.getCandidateJobs().add(cJ);
			}
			jobs.add(job);
		});
		log.info("Status of education and Matched Set in candidate before save {},{}", candidate.getEducations(),candidate.getCandidateJobs());
		jobRepository.save(jobs);
		candidateRepository.save(candidate);
		
		mailService.sendMatchedJobEmailToCandidate(candidate.getLogin(), new Long(candidate.getCandidateJobs().size()));
	}

	private CandidateJob beginMatching(Job job, Candidate candidate) {
		log.debug("Matching on {}, {}", job.getId(),job.getJobTitle());

		CandidateJob incomingCandidateJob = new CandidateJob(candidate, job);
		JobFilterObject jobFilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		CandidateJob candidateJob = null;
		// log.debug("Candidate Educaiton to match is
		// {}",candidate.getEducations().stream().filter(education->education.isHighestQualification()).findFirst());
		CandidateEducation candidateEducation = candidate.getEducations().stream()
				.filter(education -> education.getHighestQualification() != null)
				.filter(education -> education.isHighestQualification()).findAny().orElse(null);

		if (candidateEducation != null && isCandidateEligibleByGraduationDate(candidateEducation, jobFilterObject)) {
			if (candidate.getCandidateJobs().stream().filter(incomingCandidateJob::equals).findAny().isPresent()) {
				CandidateJob cJob = candidate.getCandidateJobs().stream().filter(incomingCandidateJob::equals).findAny()
						.get();
				if (candidate.getCandidateLanguageProficiencies().size() > 0 && cJob.getLanguageMatchScore() == null
						&& candidate.getGender() != null && cJob.getGenderMatchScore() == null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true);
				else if (candidate.getCandidateLanguageProficiencies().size() > 0
						&& cJob.getLanguageMatchScore() != null && candidate.getGender() != null
						&& cJob.getGenderMatchScore() == null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true);
				else if (candidate.getCandidateLanguageProficiencies().size() > 0
						&& cJob.getLanguageMatchScore() == null && candidate.getGender() != null
						&& cJob.getGenderMatchScore() != null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false);
				else
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false);
			} else {
				if (candidate.getCandidateLanguageProficiencies().size() > 0 && candidate.getGender() != null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true);
				else if (candidate.getCandidateLanguageProficiencies().size() > 0 && candidate.getGender() == null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false);
				else if (candidate.getCandidateLanguageProficiencies().size() == 0 && candidate.getGender() != null)
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true);
				else
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false);
			}

		} else {
			List<Job> jobs = new ArrayList<>();
			candidate.getCandidateJobs().stream().forEach(cJ->{
				if(cJ.equals(incomingCandidateJob)) {
					Job jb = cJ.getJob();
					jb.getCandidateJobs().remove(incomingCandidateJob);
					jobs.add(jb);
				}
			});
			jobRepository.save(jobs);
			candidate.getCandidateJobs().remove(incomingCandidateJob);
			
		}
		return candidateJob;
	}

	private Boolean isCandidateEligibleByGraduationDate(CandidateEducation education, JobFilterObject jobFilterObject) {
		log.debug("Incoming education date is {} and filter object is {}", education.getEducationToDate(),
				jobFilterObject);
		if (jobFilterObject.getGraduationDateType() == null)
			return true;
		//IF CANIDATE IS CURRENLTY STUDYING SHOULD I CONSIDER OR SKIP THE CANDIDATE FOR MATCH
		
		if(education.getEducationToDate() == null && education.isIsPursuingEducation())
			return true;
		
		if (jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_GREATER)) {		
			if (education.getEducationToDate().isAfter(jobFilterObject.getGraduationDate().getGraduationDate())
					|| education.getEducationToDate().equals(jobFilterObject.getGraduationDate().getGraduationDate()))
				return true;
		} else if (jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_LESS)) {
			if (education.getEducationToDate().isBefore(jobFilterObject.getGraduationDate().getGraduationDate())
					|| education.getEducationToDate().equals(jobFilterObject.getGraduationDate().getGraduationDate()))
				return true;
		} else if (jobFilterObject.getGraduationDateType().equalsIgnoreCase(Constants.GRADUATION_DATE_BETWEEN)) {
			if ((education.getEducationToDate().isAfter(jobFilterObject.getGraduationFromDate().getGraduationDate())
					|| education.getEducationToDate()
							.equals(jobFilterObject.getGraduationFromDate().getGraduationDate()))
					&& (education.getEducationToDate()
							.isBefore(jobFilterObject.getGraduationToDate().getGraduationDate())
							|| education.getEducationToDate()
									.equals(jobFilterObject.getGraduationToDate().getGraduationDate())))
				return true;
		}
		return false;
	}

}
