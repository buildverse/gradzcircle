package com.drishika.gradzcircle.service.matching;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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

/**
 * @author abhinav 
 * Matcher that will be called whenever a job's filter is
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
	
	public JobMatcher(CandidateEducationService candidateEducationService,
			CandidateLanguageService candidateLanguageService, JobFilterParser jobfilterParser,
			FilterRepository filterRepository, CourseRepository courseRepository,
			QualificationRepository qualificationRepository, CollegeRepository collegeRepository,
			UniversityRepository universityRepository, GenderRepository genderRepository,
			LanguageRepository languageRepository, JobRepository jobRepository,JobSearchRepository jobSearchRepository,
			MatchUtils matchUtils, CandidateJobRepository candidateJobRepository, CandidateRepository candidateRepository) {
		this.candidateEducationService = candidateEducationService;
		this.candidateLanguageService = candidateLanguageService;
		this.jobRepository = jobRepository;
		this.matchUtils = matchUtils;
		this.candidateRepository = candidateRepository;
	}

	@Override
	public void match(Job job) {
		long startTime = System.currentTimeMillis();
		matchUtils.populateJobFilterWeightMap();
		//Job jobFromRepo = jobRepository.findOne(job.getId());
		JobFilterObject jobfilterObject = matchUtils.retrieveJobFilterObjectFromJob(job);
		Stream<CandidateEducation> candidateEducationStream = filterCandidatesByEducationToDate(jobfilterObject).parallel();
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs = candidateEducationStream.map(candidateEducation -> beginMatchingOnEducation(job,jobfilterObject, candidateEducation)).filter(candidateJob -> candidateJob!=null).collect(Collectors.toSet());
		candidateJobs.forEach(matchedCandidateJob -> {
			if(job.getCandidateJobs().contains(matchedCandidateJob)) {
				job.getCandidateJobs().remove(matchedCandidateJob);
				job.getCandidateJobs().add(matchedCandidateJob);
			} else {
				job.getCandidateJobs().add(matchedCandidateJob);
			}
		});
		jobRepository.save(job);
		//BROADCAST NOW
		log.info("Job Matching completed in {} ms", (System.currentTimeMillis() - startTime));
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

	private CandidateJob beginMatchingOnEducation(Job job, JobFilterObject jobfilterObject, CandidateEducation candidateEducation) {
		log.debug("Begin Matching for job {} and education {}",jobfilterObject,candidateEducation);
		CandidateJob candidateJob = null;
		if(jobfilterObject == null || candidateEducation == null)
			return candidateJob;
		Candidate candidate = candidateRepository.findOne(candidateEducation.getCandidate().getId());
		log.debug("Candidate from reprositry while begin matching on education {} and {}",candidate);
		candidate.addEducation(candidateEducation);
		candidateJob = matchUtils.matchCandidateAndJob(jobfilterObject, candidate, job,true,true,true);
		return candidateJob;
	}
	
	
}
