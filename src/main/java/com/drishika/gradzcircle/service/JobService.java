package com.drishika.gradzcircle.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
import com.drishika.gradzcircle.repository.CandidateAppliedJobsRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.repository.search.JobFilterSearchRepository;
import com.drishika.gradzcircle.repository.search.JobHistorySearchRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.JobsUtil;

@Service
@Transactional
public class JobService {

	private final Logger log = LoggerFactory.getLogger(JobService.class);

	private final JobRepository jobRepository;

	private final JobSearchRepository jobSearchRepository;

	private final JobFilterRepository jobFilterRepository;

	@Qualifier("JobMatcher")
	private final Matcher<Job> matcher;

	private final JobHistoryRepository jobHistoryRepository;

	private final JobHistorySearchRepository jobHistorySearchRepository;

	private final CorporateRepository corporateRepository;

	private final CandidateRepository candidateRepository;

	private final CorporateService corporateService;
	
	private final CandidateAppliedJobsRepository candidateAppliedJobsRepository;

	private final DTOConverters converter;

	public JobService(JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository,
			CorporateRepository corporateRepository, CacheManager cacheManager,
			CorporateSearchRepository corporateSearchRepository, JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository,
			JobFilterHistoryRepository jobFilterHistoryRepository, Matcher<Job> matcher,
			CandidateRepository candidateRepository, DTOConverters converter, CandidateAppliedJobsRepository candidateAppliedJobsRepository,
			CorporateService corporateService) {
		this.jobRepository = jobRepository;
		this.jobSearchRepository = jobSearchRepository;
		this.corporateRepository = corporateRepository;
		// this.cacheManager = cacheManager;
		this.corporateService = corporateService;
		this.jobHistoryRepository = jobHistoryRepository;
		this.jobHistorySearchRepository = jobHistorySearchRepository;
		this.jobFilterRepository = jobFilterRepository;
		this.matcher = matcher;
		this.candidateRepository = candidateRepository;
		this.converter = converter;
		this.candidateAppliedJobsRepository = candidateAppliedJobsRepository;
	}

	public Job createJob(Job job) throws BeanCopyException {
		log.info("In create job {}", job);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		job.setCorporate(corporateRepository.findOne(job.getCorporate().getId()));
		job.setCreateDate(dateTime);
		job.setCanEdit(Boolean.TRUE);
		Job savedJob = jobRepository.save(job);
		// matcher.match(savedJob);
		return savedJob;
	}

	public Job updateJob(Job job) throws BeanCopyException, JobEditException {
		log.info("In updating job {}", job);
		if (!job.getCanEdit())
			throw new JobEditException("Cannot Edit job anymmore");
		Job prevJob = getJob(job.getId());
		if (!(job.equals(prevJob) && (job.getJobDescription().equals(prevJob.getJobDescription())
				&& (job.getSalary().equals(prevJob.getSalary()) && (job.getJobTitle().equals(prevJob.getJobTitle()))
						&& (job.getJobStatus().equals(prevJob.getJobStatus()) && jobFiltersSame(prevJob, job)))))) {
			updateJobMetaActions(job, prevJob);
			setJob(job, prevJob);
			setJobFilters(job, prevJob);

			log.info("Updating job");
		}
		// jobSearchRepository.save(jobRepository.save(job));

		job = jobRepository.save(job);
		log.info("Job updated {} ,{}", job, job.getJobFilters());
		if (job.getCorporate() != null && job.getCorporate().getEscrowAmount() != null) {
			Corporate corporate = corporateRepository.getOne(job.getCorporate().getId());
			corporate.setEscrowAmount(job.getCorporate().getEscrowAmount());
			// corporateSearchRepository.save(corporateRepository.save(corporate));
			corporateRepository.save(corporate);
		}
		if (job.getJobFilters() != null && job.getJobFilters().size() > 0)
			matcher.match(job);
		log.info("TRIGGER MATHCING ASYNCH");
		return job;
	}

	private void setJob(Job job, Job prevJob) throws BeanCopyException {
		job.setCreateDate(prevJob.getCreateDate());
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		job.setUpdateDate(dateTime);
		setJobHistory(job, prevJob);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.addAll(prevJob.getCandidateJobs());
		job.setCandidateJobs(candidateJobs);

	}

	private void setJobHistory(Job job, Job prevJob) throws BeanCopyException {
		JobHistory jobHistory = new JobHistory();
		JobsUtil.populateHistories(jobHistory, prevJob);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		jobHistory.job(prevJob);
		jobHistory.createDate(dateTime);
		job.addHistory(jobHistory);

	}

	private Boolean jobFiltersSame(Job job, Job prevJob) {
		log.info(" Current filter is {}", job.getJobFilters());
		log.info(" Prev filter is {}", prevJob.getJobFilters());
		if (job.getJobFilters().equals(prevJob.getJobFilters()))
			return true;
		else
			return false;
	}

	private void updateJobMetaActions(Job job, Job prevJob) {
		if (job.getJobStatus() == 1 && !prevJob.isEverActive())
			job.setEverActive(Boolean.TRUE);
		if (!prevJob.isHasBeenEdited() && prevJob.isEverActive() && job.getJobStatus() == 1) {
			job.setHasBeenEdited(Boolean.TRUE);
		}
		if (prevJob.isHasBeenEdited() && job.getJobStatus() == 1)
			job.setCanEdit(Boolean.FALSE);
	}

	private void setJobFilters(Job job, Job prevJob) throws BeanCopyException {
		if (jobFiltersSame(job, prevJob))
			return;
		log.info("In saving Job filter  {}", job.getJobFilters());
		if (job.getJobFilters() != null && !job.getJobFilters().isEmpty()) {
			for (JobFilter jobFilter : job.getJobFilters()) {
				if (jobFilter.getId() != null) {
					JobFilter prevJobFilter = getJobFilter(jobFilter.getId());
					JobFilterHistory jobFilterHistory = new JobFilterHistory();
					JobsUtil.populateHistories(jobFilterHistory, prevJobFilter);
					jobFilterHistory.jobFilter(prevJobFilter);
					jobFilter.job(job);
					jobFilter.addHistory(jobFilterHistory);
				} else {
					jobFilter.job(job);
				}
			}

		}
	}

	public JobFilter getJobFilter(Job job) {
		return jobFilterRepository.findByJob(job);
	}

	public JobFilter getJobFilter(Long id) {
		return jobFilterRepository.findOne(id);
	}

	public Job getJob(Long id) {
		return jobRepository.findOne(id);
	}

	public List<Job> getAllActiveJobs() {
		return jobRepository.findAllActiveJobs();
	}

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	public void saveJobOnly(Job job) {
		jobSearchRepository.save(jobRepository.save(job));
	}

	public Job deActivateJob(Long id) throws BeanCopyException {
		log.info("Deactivating job {}", id);
		Job prevJob = getJob(id);
		JobHistory jobHistory = new JobHistory();
		JobsUtil.populateHistories(jobHistory, prevJob);
		prevJob.setJobStatus(ApplicationConstants.JOB_DEACTIVATE);
		jobHistorySearchRepository.save(jobHistoryRepository.save(jobHistory));
		Job job = jobRepository.save(prevJob);
		jobSearchRepository.save(job);
		log.info("Deactivated job {}", id);
		return job;
	}

	public Page<CorporateJobDTO> getActiveJobsListForCorporates(Pageable pageable, Long corporateId) {
		Page<Job> jobPage = jobRepository.findByActiveJobAndCorporateId(corporateId, pageable);
		Long totalNumberOfJobs = getTotalJobsByCorporate(corporateId);
		Long jobsPostedLastMonth = getTotalJobsPostedSinceLastMonth(corporateId);
		Long applicantsToJobs = getAppliedCandidatesForAllJobsByCorporate(corporateId);
		Long totalLinkedCandidates = corporateService.getLinkedCandidatesCount(corporateId); 
		final Page<CorporateJobDTO> page = jobPage.map(job -> converter.convertToJobListingForCorporate(job,
				totalLinkedCandidates,totalNumberOfJobs,applicantsToJobs,jobsPostedLastMonth,getTotalCandidatedShorListedByCorporateForJob(job.getId())));
		return page;
	}
	
	public Page<CandidateJobDTO> getShortListedJobsListForCandidate(Pageable pageable, Long candidateId) {
		Page<Job> jobPage = jobRepository.getJobListShortListedForCandidate(candidateId, pageable);
		final Page<CandidateJobDTO> page = jobPage.map(job -> converter.convertToJobListingForCandidate(job,candidateId,false));
		return page;
	}

	public Page<CandidateJobDTO> getNewActiveJobsListForCandidates(Pageable pageable, Long candidateId) {
		Candidate candidate = candidateRepository.findOne(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.getEducations();
		Page<Job> jobPage = null;
		final Page<CandidateJobDTO> page;
		if(candidateEducations.isEmpty()) {
			//get all jobs set score to 0
			jobPage = jobRepository.findAllActiveJobsForCandidatesThatCandidateHasNotAppliedFor(pageable,candidateId);
			log.debug("Did i get any result {}", jobPage.getContent());
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidateWithNoEducation(job, candidateId));;
		} else {
			jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidate(candidateId, pageable);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		}
		//Page<Job> jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidate(candidateId, pageable);
		/*if(jobPage.getSize()==0) {
			jobPage = jobRepository.findAllActiveJobsForCandidates(pageable,candidateId);
		}*/
		return page;
	}

	public Page<CandidateJobDTO> getAppliedJobsListForCandidates(Pageable pageable, Long candidateId) {
		Page<Job> jobPage = jobRepository.findAppliedJobByCandidate(candidateId, pageable);
		final Page<CandidateJobDTO> page = jobPage
				.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		return page;
	}

	public Page<CandidateProfileListDTO> getAppliedCandidatesForJob(Pageable pageable, Long jobId) {
		Page<CandidateAppliedJobs> candidatePage = jobRepository.findByAppliedCandidates(jobId, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage
				.map(candidateAppliedJob -> converter.convertToCandidateProfileListingDTO(candidateAppliedJob,
						candidateRepository.findOne(candidateAppliedJob.getId().getCandidateId())));
		return page;
	}

	public Page<CandidateProfileListDTO> getMatchedCandidatesForJob(Pageable pageable, Long jobId) {
		Page<CandidateJob> candidatePage = jobRepository.findMatchedCandidatesForJob(jobId, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage.map(candidateJob -> converter
				.convertToCandidateProfileListingDTO(candidateJob.getCandidate(), candidateJob));
		return page;
	}

	public Job applyJobForCandidate(Long jobId, Long loginId) {
		Job job = jobRepository.findOne(jobId);
		Candidate candidate = candidateRepository.findByLoginId(loginId);
		job.addAppliedCandidate(candidate);
		return jobRepository.save(job);
	}
	
	public Long getAppliedCandidatesForAllJobsByCorporate(Long corporateId) {
		return candidateAppliedJobsRepository.findAppliedCandidatesForAllJobsByCorporate(corporateId);
	}
	
	public Long getTotalJobsByCorporate(Long corporateId) {
		return jobRepository.countByCorporate(corporateId);
	}
	
	public Long getTotalCandidatedShorListedByCorporateForJob(Long jobId) {
		return corporateRepository.findCountOfCandidatesShortlistedByJob(jobId);
	}
	
	public Long getTotalJobsPostedSinceLastMonth(Long corporateId) {
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		//ZonedDateTime toDateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		//ZonedDateTime toDateTime = LocalDate.now().atStartOfDay(zoneId);
		//ZonedDateTime fromDateTime = ZonedDateTime.of(toDateTime.getYear(),toDateTime.getMonthValue()-1,toDateTime.getDayOfMonth()+1,00,00,00,00,zoneId);
		ZonedDateTime toDateTime = LocalDate.now().atStartOfDay(zoneId);
		ZonedDateTime fromDateTime = toDateTime.minusMonths(1).withDayOfMonth(1);
		return jobRepository.numberOfJobsPostedAcrossDates(corporateId,fromDateTime,toDateTime);
	}
	
	public Page<CandidateProfileListDTO> getShortListedCandidatesForJob(Pageable pageable,Long jobId) {
		Page<CorporateCandidate> candidateCorporatePage = corporateRepository.findLinkedCandidatesByJob(jobId, pageable);
		final Page<CandidateProfileListDTO> page = candidateCorporatePage.map(corporateCandidate -> converter
				.convertToCandidateProfileListingDTO(corporateCandidate.getCandidate(), corporateCandidate));
		return page;
	}
}
