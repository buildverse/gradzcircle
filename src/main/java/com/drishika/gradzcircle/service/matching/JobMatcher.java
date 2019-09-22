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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.repository.CandidateJobRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.CandidateEducationService;
import com.drishika.gradzcircle.service.CandidateLanguageService;
import com.drishika.gradzcircle.service.MailService;

/**
 * @author abhinav Matcher that will be called whenever a job's filter is
 *         amended or new job is created.
 */
@Service
@Qualifier("JobMatcher")
public class JobMatcher implements Matcher<Job> {

	private final Logger log = LoggerFactory.getLogger(JobMatcher.class);

	private final CandidateEducationService candidateEducationService;
	private final CandidateLanguageService candidateLanguageService;
	private final JobRepository jobRepository;
	private final CandidateRepository candidateRepository;
	private final MatchUtils matchUtils;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final MailService mailService;

	public JobMatcher(CandidateEducationService candidateEducationService,
			CandidateLanguageService candidateLanguageService, JobFilterParser jobfilterParser,
			FilterRepository filterRepository, CourseRepository courseRepository,
			QualificationRepository qualificationRepository, CollegeRepository collegeRepository,
			UniversityRepository universityRepository, GenderRepository genderRepository,
			LanguageRepository languageRepository, JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			MatchUtils matchUtils, CandidateJobRepository candidateJobRepository,
			CandidateRepository candidateRepository, ApplicationEventPublisher applicationEventPublisher,
			MailService mailService) {
		this.candidateEducationService = candidateEducationService;
		this.candidateLanguageService = candidateLanguageService;
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
		this.applicationEventPublisher = applicationEventPublisher;
		this.mailService = mailService;
	}

	@Override
	public void match(Job job) {
		long startTime = System.currentTimeMillis();
		matchUtils.populateJobFilterWeightMap();
		Long numberOfNewCandidates=0L;
		JobFilterObject jobfilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		Stream<CandidateEducation> candidateEducationStream = filterCandidatesByEducationToDate(jobfilterObject);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		List<Candidate> candidates = new ArrayList<Candidate>();
		Long initialMatches = new Long(job.getCandidateJobs().size());
		candidateJobs = candidateEducationStream
				.map(candidateEducation -> beginMatchingOnEducation(job, jobfilterObject, candidateEducation))
				.filter(candidateJob -> candidateJob != null).collect(Collectors.toSet());
		Long newMatches = new Long(candidateJobs.size());
		numberOfNewCandidates = Math.abs(initialMatches-newMatches);
		job.getCandidateJobs().clear();
		job.getCandidateJobs().addAll(candidateJobs);
		job.getCandidateJobs().forEach(cJ->{
			log.debug("Candidate Jobs are {}",cJ);
			Candidate candidate = cJ.getCandidate();
			if(candidate.getCandidateJobs().contains(cJ) ) {
				candidate.getCandidateJobs().remove(cJ);
				candidate.getCandidateJobs().add(cJ);
				log.debug("Replacing Candidate Job in Job Object{}",candidate);
				
			} else {
				candidate.getCandidateJobs().add(cJ);
			}
			candidates.add(candidate);
		});
		candidateRepository.save(candidates);
		jobRepository.save(job);
		log.info("Job Matching completed in {} ms and number of new candidates matched are {}", (System.currentTimeMillis() - startTime),numberOfNewCandidates);
		mailService.sendMatchedCandidateEmailToCorporate(job.getCorporate().getLogin(), job.getJobTitle(), numberOfNewCandidates);

	}

	private Stream<CandidateLanguageProficiency> getAllLanguageProfienciesForActiveCandidates() {
		return candidateLanguageService.getAllLanguageProfienciesForActiveCandidates();
	}

	/**
	 * First try to filter the candidates by graduation date and prepare data set to
	 * match
	 * 
	 * @param jobFilter
	 */
	private Stream<CandidateEducation> filterCandidatesByEducationToDate(JobFilterObject jobFilter) {
		Stream<CandidateEducation> candidateEducationStream = null;
		if (jobFilter.getGraduationDateType() == null) {
			candidateEducationStream = candidateEducationService.getEducationForMatchEligibleCandidate();
			log.info("CandidateEducation for eMatch eligible Canidates {}");
		} else if (jobFilter.getGraduationDateType()
				.equalsIgnoreCase(ApplicationConstants.GRADUATION_DATE_TYPE_BETWEEN)) {
			candidateEducationStream = candidateEducationService.getCandidateEducationBetweenSuppliedDates(
					jobFilter.getGraduationFromDate().getGraduationDate(),
					jobFilter.getGraduationToDate().getGraduationDate());
			log.info("CandidateEducation filtered between dates {} and {} ",
					jobFilter.getGraduationFromDate().getGraduationDate(),
					jobFilter.getGraduationToDate().getGraduationDate());
		} else if (jobFilter.getGraduationDateType().equalsIgnoreCase(ApplicationConstants.GRADUATION_DATE_TYPE_LESS)) {
			candidateEducationStream = candidateEducationService
					.getCandidateEducationBeforeSuppliedDate(jobFilter.getGraduationDate().getGraduationDate());
			log.info("CandidateEducation filtered graduated before {} ",
					jobFilter.getGraduationDate().getGraduationDate());
		} else if (jobFilter.getGraduationDateType()
				.equalsIgnoreCase(ApplicationConstants.GRADUATION_DATE_TYPE_GREATER)) {
			candidateEducationStream = candidateEducationService
					.getCandidateEducationAfterSuppliedDate(jobFilter.getGraduationDate().getGraduationDate());
			log.info("CandidateEducation filtered graudated after {} ",
					jobFilter.getGraduationDate().getGraduationDate());
		}
		return candidateEducationStream;

	}

	private CandidateJob beginMatchingOnEducation(Job job, JobFilterObject jobfilterObject,
			CandidateEducation candidateEducation) {
		log.debug("Begin Matching for job {} and education {}", jobfilterObject, candidateEducation);
		CandidateJob candidateJob = null;
		if (jobfilterObject == null || candidateEducation == null)
			return candidateJob;
		log.debug("Candidate Id from educaiton is {}",candidateEducation.getCandidate());
		Candidate candidate = candidateRepository.findOne(candidateEducation.getCandidate().getId());
		log.debug("Candidate from reprositry while begin matching on education {} and {}", candidateEducation,candidate);
		candidate.addEducation(candidateEducation);
		candidateJob = matchUtils.matchCandidateAndJob(jobfilterObject, candidate, job, true, true, true);
		return candidateJob;
	}

}
