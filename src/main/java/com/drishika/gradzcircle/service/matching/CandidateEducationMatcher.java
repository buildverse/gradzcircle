/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.ArrayList;
import java.util.HashSet;
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
	private final MailService mailService;

	public CandidateEducationMatcher(JobRepository jobRepository, MatchUtils matchUtils,
			CandidateRepository candidateRepository, CandidateEducationRepository candidateEducationRepository,
			MailService mailService) {
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
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
		CandidateEducation candidateEducation = candidate.getEducations().stream()
				.filter(education -> education.getHighestQualification() != null)
				.filter(education -> education.isHighestQualification()).findAny().orElse(null);

		// Match what is not there on adding highest education
		//Boolean matchEducaton, Boolean matchLanguages, Boolean matchGender, Boolean matchSkills
		if (candidateEducation != null && isCandidateEligibleByGraduationDate(candidateEducation, jobFilterObject)) {
			CandidateJob cJob = candidate.getCandidateJobs().stream().filter(incomingCandidateJob::equals).findAny().orElse(null);
			
			if (cJob != null) {
				/* If have Language, gender and skills and Adding/updating education match all - As */
				if (!candidate.getCandidateLanguageProficiencies().isEmpty() && 
						(cJob.getLanguageMatchScore() == null || cJob.getLanguageMatchScore()==0)
						&& candidate.getGender() != null && (cJob.getGenderMatchScore() == null || cJob.getGenderMatchScore() == 0) 
						&& !candidate.getCandidateSkills().isEmpty() && (cJob.getSkillMatchScore() ==null || cJob.getSkillMatchScore() == 0)) {
					log.debug("Calling within match set as true, true, true, true");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true,true);
				}
				/*I have Language saved , No skill and gender saved */
				else if (!candidate.getCandidateLanguageProficiencies().isEmpty() &&
							(cJob.getLanguageMatchScore() == null || cJob.getLanguageMatchScore() == 0)
							&& candidate.getGender() == null && candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have Language with score, skill with score but gender without score - Match Language with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true,false,false);
					
				}
				/*I have Skill saved , No Languages and gender saved */
				else if (candidate.getCandidateLanguageProficiencies().isEmpty()  
						&&	candidate.getGender() == null && !candidate.getCandidateSkills().isEmpty() && 
							(cJob.getSkillMatchScore()==null||cJob.getSkillMatchScore() == 0)) {
					log.debug("I have Skill saved , No Languages and gender saved - Match skills with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false,true);
					
				}
				/*I have Gender saved , No Languages and skill saved */
				else if (candidate.getCandidateLanguageProficiencies().isEmpty()  
						&& candidate.getGender() != null && (cJob.getGenderMatchScore() == null || cJob.getGenderMatchScore() == 0) 
						&& candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have gender saved , No Languages and skill saved - Match gender with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true,false);
					
				}
				/* I have language and Skills but no gender*/
				else if (!candidate.getCandidateLanguageProficiencies().isEmpty() &&
						(cJob.getLanguageMatchScore()==null || cJob.getLanguageMatchScore() ==0) && candidate.getGender()==null && 
							(cJob.getSkillMatchScore() == null || cJob.getSkillMatchScore() == 0)
						&& !candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have language and Skills but no gender- Match language and skills with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false,true);
					
					/*I have Language and Gender but no skills*/
				} else if (!candidate.getCandidateLanguageProficiencies().isEmpty() && 
							(cJob.getLanguageMatchScore() == null || cJob.getLanguageMatchScore()==0) && candidate.getGender()!=null && 
								(cJob.getGenderMatchScore()==null || cJob.getGenderMatchScore() == 0)
							&& candidate.getCandidateSkills().isEmpty()) {
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true,false);
					log.debug("I have language and gender but no skills- Match language,gender no skills with Education");
					/*I have Skill and Gender but no Language*/
				} else if (candidate.getCandidateLanguageProficiencies().isEmpty() && candidate.getGender()!=null 
							&& (cJob.getGenderMatchScore() == null || cJob.getGenderMatchScore() == 0)
						&& !candidate.getCandidateSkills().isEmpty() && (cJob.getSkillMatchScore()==null || cJob.getSkillMatchScore() == 0 )) {
					log.debug("I have  Skill and Gender but no Language - Match skill,gender no Language with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true,true);
					
				}
				/*If I have only educaiton */
				else {
					log.debug("Calling within match set as true, false, false, false");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false,false);
					
				}
			} else {
				/* If have Language, gender and skills and Adding/updating education match all - As */
				if (!candidate.getCandidateLanguageProficiencies().isEmpty() 
						&& candidate.getGender() != null 
						&& !candidate.getCandidateSkills().isEmpty()) {
					log.debug("Calling  as true, true, true, true");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true,true);
					
				}
				/*I have Language saved , No skill and gender saved */
				else if (!candidate.getCandidateLanguageProficiencies().isEmpty() 
							&& candidate.getGender() == null && candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have Language with score, skill with score but gender without score - Match Language with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true,false,false);
					
				}
				/*I have Skill saved , No Languages and gender saved */
				else if (candidate.getCandidateLanguageProficiencies().isEmpty()  
						&&	candidate.getGender() == null && !candidate.getCandidateSkills().isEmpty() ) {
					log.debug("I have Skill saved , No Languages and gender saved - Match skills with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false,true);
					
				}
				/*I have Gender saved , No Languages and skill saved */
				else if (candidate.getCandidateLanguageProficiencies().isEmpty()  
						&& candidate.getGender() != null
						&& candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have gender saved , No Languages and skill saved - Match gender with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true,false);
					
				}
				/* I have language and Skills but no gender*/
				else if (!candidate.getCandidateLanguageProficiencies().isEmpty() 
						 && candidate.getGender()==null 
						&& !candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have language and Skills but no gender- Match language and skills with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, false,true);
					
					/*I have Language and Gender but no skills*/
				} else if (!candidate.getCandidateLanguageProficiencies().isEmpty() && 
							 candidate.getGender()!=null && candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have language and gender but no skills- Match language,gender no skills with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, true, true,false);
					
					/*I have Skill and Gender but no Language*/
				} else if (candidate.getCandidateLanguageProficiencies().isEmpty() && candidate.getGender()!=null 
						&& !candidate.getCandidateSkills().isEmpty()) {
					log.debug("I have  Skill and Gender but no Language - Match skill,gender no Language with Education");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, true,true);
					
				}
				/*If I have only educaiton */
				else {
					log.debug("Calling  as true, false, false, false");
					candidateJob = matchUtils.matchCandidateAndJob(jobFilterObject, candidate, job, true, false, false,false);
					
				}
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
