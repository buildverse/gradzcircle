package com.drishika.gradzcircle.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
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
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
import com.drishika.gradzcircle.exception.NoPreviousJobException;
import com.drishika.gradzcircle.repository.CandidateAppliedJobsRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateCandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.repository.search.JobFilterSearchRepository;
import com.drishika.gradzcircle.repository.search.JobHistorySearchRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.dto.JobEconomicsDTO;
import com.drishika.gradzcircle.service.dto.JobListDTO;
import com.drishika.gradzcircle.service.dto.JobStatistics;
import com.drishika.gradzcircle.service.matching.JobFilterObject;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.drishika.gradzcircle.service.util.JobsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class JobService {

	private final Logger log = LoggerFactory.getLogger(JobService.class);

	private final JobRepository jobRepository;

	private final JobSearchRepository jobSearchRepository;

	private final JobFilterRepository jobFilterRepository;
	
	private FilterRepository filterRepository;

	@Qualifier("JobMatcher")
	private final Matcher<Long> matcher;

	private final JobHistoryRepository jobHistoryRepository;

	private final JobHistorySearchRepository jobHistorySearchRepository;

	private final CorporateRepository corporateRepository;

	private final CandidateRepository candidateRepository;

	private final CorporateService corporateService;
	
	private final CandidateEducationService candidateEducationService;
	
	private final CandidateAppliedJobsRepository candidateAppliedJobsRepository;

	private final DTOConverters converter;
	
	private final GradzcircleCacheManager <String,List<JobStatistics>> jobStatsCacheManager;
	
	private final GradzcircleCacheManager <String,Long> jobCountCacheManager;
	
	private final GradzcircleCacheManager<String, Map<String,JobType>> jobTypeCacheManager;
	
	private final GradzcircleCacheManager<String, Map<String,EmploymentType>> employmentTypeCacheManager;
	
	private final GradzcircleCacheManager<String, Map<String,Double>> filtersCacheManager;
	
	private final CorporateCandidateRepository corporateCandidateRepository;
	
	private final EmploymentTypeRepository employmentTypeRepository;
	
	private final JobTypeRepository jobTypeRepository;
	
	private final CacheManager cacheManager;

	public JobService(JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository,
			CorporateRepository corporateRepository, CacheManager cacheManager,
			CorporateSearchRepository corporateSearchRepository, JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository,
			JobFilterHistoryRepository jobFilterHistoryRepository, @Qualifier("JobMatcher")Matcher<Long> matcher,
			CandidateRepository candidateRepository, DTOConverters converter, CandidateAppliedJobsRepository candidateAppliedJobsRepository,
			CorporateService corporateService, GradzcircleCacheManager <String,List<JobStatistics>> jobStatsCacheManager,
			EmploymentTypeRepository employmentTypeRepository,JobTypeRepository jobTypeRepository,GradzcircleCacheManager <String,Long> jobCountCacheManager,
			GradzcircleCacheManager<String, Map<String,JobType>> jobTypeCacheManager, GradzcircleCacheManager<String, Map<String,EmploymentType>> employmentTypeCacheManager, GradzcircleCacheManager<String, Map<String,Double>> filtersCacheManager,
			FilterRepository filterRepository, CorporateCandidateRepository corporateCandidateRepository, CandidateEducationService candidateEducationService
			) {
		this.jobRepository = jobRepository;
		this.jobSearchRepository = jobSearchRepository;
		this.corporateRepository = corporateRepository;
		this.cacheManager = cacheManager;
		this.corporateService = corporateService;
		this.jobHistoryRepository = jobHistoryRepository;
		this.jobHistorySearchRepository = jobHistorySearchRepository;
		this.jobFilterRepository = jobFilterRepository;
		this.matcher = matcher;
		this.candidateRepository = candidateRepository;
		this.converter = converter;
		this.candidateAppliedJobsRepository = candidateAppliedJobsRepository;
		this.jobStatsCacheManager = jobStatsCacheManager;
		this.employmentTypeRepository = employmentTypeRepository;
		this.jobTypeRepository = jobTypeRepository;
		this.jobCountCacheManager = jobCountCacheManager;
		this.jobTypeCacheManager = jobTypeCacheManager;
		this.employmentTypeCacheManager = employmentTypeCacheManager;
		this.filtersCacheManager = filtersCacheManager;
		this.filterRepository = filterRepository;
		this.corporateCandidateRepository = corporateCandidateRepository;
		this.candidateEducationService = candidateEducationService;
	}

	public Job createJob(Job job) throws BeanCopyException {
		log.info("In create job {}", job);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		Corporate corporate = null;
		Optional<Corporate> corporateOptional = corporateRepository.findById(job.getCorporate().getId());
		if(corporateOptional.isPresent())
			corporate = corporateOptional.get();
		else
			return null;
		job.setCorporate(corporateRepository.findById(job.getCorporate().getId()).get());
		job.setCreateDate(dateTime);
		job.setCanEdit(Boolean.TRUE);
		Job savedJob = jobRepository.save(job);
		// matcher.match(savedJob);
		return savedJob;
	}

	public Job updateJob(Job job) throws BeanCopyException, JobEditException {
		log.info("In updating job {} and escrow {}", job,job.getCorporate().getEscrowAmount());
		if (!job.getCanEdit())
			throw new JobEditException("Cannot Edit job anymmore");
		Optional<Job> prevJobOptional = getJob(job.getId());
		Job prevJob= null;
		if(prevJobOptional.isPresent())
			prevJob = prevJobOptional.get();
		else 
			return null;
		if (!job.getNoOfApplicants().equals(prevJob.getNoOfApplicants()) && prevJob.getEverActive())
			throw new JobEditException("Cannot Change number of candidates once Jobs is active");
		log.info("Previous job object is {}",prevJob);
		if (! (job.getJobDescription().equals(prevJob.getJobDescription())
				&& job.getSalary().equals(prevJob.getSalary()) && job.getJobTitle().equals(prevJob.getJobTitle())
						&& job.getJobStatus().equals(prevJob.getJobStatus()) && jobFiltersSame(prevJob, job))) {
			updateJobMetaActions(job, prevJob);
			log.debug("after updating sctions escrow {}", job.getCorporate().getEscrowAmount());
			setJob(job, prevJob);
			log.debug("after updating job and escrow {}", job.getCorporate().getEscrowAmount());
			setJobFilters(job, prevJob);
			log.debug("After updating filters and escrow {}", job.getCorporate().getEscrowAmount());
			log.info("Updating job");
		}
		// jobSearchRepository.save(jobRepository.save(job));
		Integer prevJobStatus = prevJob.getJobStatus();
		log.info("Candidate Jobs before saving this job is {} and escrow is {}",job.getCandidateJobs(),job.getCorporate().getEscrowAmount());
		Double escrowAmount = job.getCorporate().getEscrowAmount();
		job = jobRepository.saveAndFlush(job);
		//FIXME make cache refresh aysnc post update
		if(!job.getJobStatus().equals(prevJobStatus))
			jobCountCacheManager.removeFromCache(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		if(!(job.getEmploymentType().equals(prevJob.getEmploymentType())) || ! (job.getJobType().equals(prevJob.getJobType())) || !job.getJobStatus().equals(prevJobStatus))
			jobStatsCacheManager.clearCache();
		
		
		log.info("Job updated {} ,{}", job, job.getJobFilters());
		if (job.getCorporate() != null && escrowAmount != null) {
			Corporate corporate = corporateRepository.getOne(job.getCorporate().getId());
			log.debug("Escrow amount for coprateto be updated is {}",escrowAmount);
			corporate.setEscrowAmount(escrowAmount);
			// corporateSearchRepository.save(corporateRepository.save(corporate));
			corporateRepository.save(corporate);
		}
		
		if (job.getJobFilters() != null && job.getJobFilters().size() > 0)
			matcher.match(job.getId());
		log.info("TRIGGER MATHCING ASYNCH");
		return job;
	}

	public void beginMatchingAllJobsAndCandidates() {
		 // TODO implement util methods for matching via admin panel
	}
	
	private void setJob(Job job, Job prevJob) throws BeanCopyException {
		job.setCreateDate(prevJob.getCreateDate());
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		job.setUpdateDate(dateTime);
		setJobHistory(job, prevJob);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		log.debug("No of matched candidates from prev job is {}",prevJob.getCandidateJobs().size());
		log.debug("content of matched candidates from prev job is {}",prevJob.getCandidateJobs());
		log.debug("No of matched candidates from job is {}",job.getCandidateJobs());
		log.debug("content of matched candidates from job is {}",job.getCandidateJobs());
		candidateJobs.addAll(prevJob.getCandidateJobs());
		job.setCandidateJobs(candidateJobs);
		//log.info("No of matched candidates after setting is {}",job.getCandidateJobs().size());
		//log.info("content of matched candidates after setting is {}",job.getCandidateJobs());
		log.debug("no of applicants left incoming are {} and from prev job are {}",job.getNoOfApplicantLeft(),prevJob.getNoOfApplicantLeft(),prevJob.getNoOfApplicants());
		//job.setNoOfApplicantLeft(prevJob.getNoOfApplicants().longValue());

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
		if(!job.getNoOfApplicants().equals(prevJob.getNoOfApplicants()))
			return false;
		Boolean filtersSame = false;
		Set<JobFilter> jobFilters = job.getJobFilters();
		Set<JobFilter> prevJobFilters = prevJob.getJobFilters();
		Iterator<JobFilter> jobFilterIterator = jobFilters.iterator();
		Iterator<JobFilter> prevJobFilterIterator = prevJobFilters.iterator();
		while(jobFilterIterator.hasNext()) {
			JobFilter filter = jobFilterIterator.next();
			while(prevJobFilterIterator.hasNext()) {
				if(filter!=null && filter.getFilterDescription() != null &&
						filter.getFilterDescription().equals(prevJobFilterIterator.next().getFilterDescription()))
					filtersSame = true;
			}
		}
		return filtersSame;
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
					Optional<JobFilter> filterOption = getJobFilter(jobFilter.getId());
					if(!filterOption.isPresent())
						return;
					JobFilter prevJobFilter = filterOption.get();
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

	public Optional<JobFilter> getJobFilter(Long id) {
		return jobFilterRepository.findById(id);
	}

	public Optional<Job> getJob(Long id) {
		return jobRepository.findById(id);
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

	public void deActivateJob(Long id) throws BeanCopyException {
		log.info("Deactivating job {}", id);
		Optional<Job> prevJobOptional = getJob(id); 
		if(!prevJobOptional.isPresent())
			return;
		Job prevJob = prevJobOptional.get();
		JobHistory jobHistory = new JobHistory();
		JobsUtil.populateHistories(jobHistory, prevJob);
		prevJob.setJobStatus(ApplicationConstants.JOB_DEACTIVATE);
		jobHistorySearchRepository.save(jobHistoryRepository.save(jobHistory));
		jobStatsCacheManager.clearCache();
		jobCountCacheManager.removeFromCache(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		Job job = jobRepository.save(prevJob);
		jobSearchRepository.save(job);
		log.info("Deactivated job {}", id);
	}
	
	public List<JobListDTO> getShortlistedJobListForCorporateByCandidate(Long corporateId, Long candidateId) {
		log.debug("Corproate is {}",corporateRepository.getOne(corporateId));
		List<CorporateCandidate> linkedCandidates = corporateCandidateRepository.findJobThatCorporateShortListedCandidateFor(corporateId, candidateId);
		log.debug("Linked date set is {}",linkedCandidates);
		return linkedCandidates.stream().map(linkedCandidate -> convertToJobListDTO(linkedCandidate)).collect(Collectors.toList());
		
	}
	
	private JobListDTO convertToJobListDTO(CorporateCandidate linkedCandidate) {
		JobListDTO jobDto = null;
		Set<CandidateJob> candidateJobs = linkedCandidate.getCandidate().getCandidateJobs();
		Job job = null;
		Optional<Job> jobOptional = jobRepository.findById(linkedCandidate.getId().getJobId());
		if(!jobOptional.isPresent())
			job = new Job();
		else 
			job = jobOptional.get();
		CandidateJob candidateJob = new CandidateJob(linkedCandidate.getCandidate(),job);
		CandidateJob filteredCandidateJob = candidateJobs.stream().filter(cJ -> cJ.equals(candidateJob)).findAny().orElse(null);
		if(filteredCandidateJob != null)
			jobDto = new JobListDTO(filteredCandidateJob.getJob().getJobTitle(),filteredCandidateJob.getMatchScore());
		return jobDto;
	}

	public Page<CorporateJobDTO> getActiveJobsListForCorporates(Pageable pageable, Long corporateId) {
		Page<Job> jobPage = jobRepository.findByActiveJobAndCorporateId(corporateId, pageable);
		Long totalNumberOfJobs = getTotalJobsByCorporate(corporateId);
		Long jobsPostedLastMonth = getTotalJobsPostedSinceLastMonth(corporateId);
		Long applicantsToJobs = getAppliedCandidatesForAllJobsByCorporate(corporateId);
		Long totalLinkedCandidates = corporateService.getLinkedCandidatesCount(corporateId); 
		final Page<CorporateJobDTO> page = jobPage.map(job -> converter.convertToJobListingForCorporate(job,
				totalLinkedCandidates,totalNumberOfJobs,applicantsToJobs,jobsPostedLastMonth,getTotalCandidatedShorListedByCorporateForJob(corporateId,job.getId())));
		return page;
	}
	
	public Page<CandidateJobDTO> getShortListedJobsListForCandidate(Pageable pageable, Long candidateId) {
		Page<Job> jobPage = jobRepository.getJobListShortListedForCandidate(candidateId, pageable);
	//	List<JobStatistics> jobStats = jobRepository.findStatisticsByShortlistedJobsForCandidates(candidateId);
		final Page<CandidateJobDTO> page = jobPage.map(job -> converter.convertToJobListingForCandidate(job,candidateId,false));
		return page;
	}

	public Page<CandidateJobDTO> getNewActiveJobsListForCandidates(Pageable pageable, Long candidateId, Double matchScoreFrom, Double matchScoreTo) {
		long startTime = System.currentTimeMillis();
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.get().getEducations();
		Page<Job> jobPage = null;
		final Page<CandidateJobDTO> page;
		if(candidateEducations.isEmpty() ) {
			//get all jobs set score to 0
			jobPage = jobRepository.findAllActiveJobsForCandidatesThatCandidateHasNotAppliedFor(pageable,candidateId);
			log.info("Time taken to query new active Jobs That candidate with no education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidateWithNoEducation(job, candidateId));;
		} else {
			if(matchScoreFrom >-1 && matchScoreTo>-1)
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidateWithMatchScore(candidateId,matchScoreFrom,matchScoreTo,pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidate(candidateId,pageable);
			log.info("Time taken to query new active Jobs That candidate with education not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		}
		return page;
	}
	
	
	public Page<CandidateJobDTO> findAllActiveJobsOnPortal(Integer active, Pageable pageable) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		jobPage = jobRepository.findByJobStatus(active, pageable);
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);//jobRepository.countByJobStatus(ApplicationConstants.JOB_ACTIVE);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentType = getJobStatisticsEmploymentType(
				ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_WITHOUT_ANY_SELECTION);
		final List<JobStatistics> jobStatisticsJobType = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_WITHOUT_ANY_SELECTION);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		return jobPage.map(job -> converter.convertToJobListingForPortalAnonymous(job, totalActiveJobs,
				jobStatisticsEmploymentType, jobStatisticsJobType));
	}

	public Page<CandidateJobDTO> findActiveJobsByEmploymentType(Integer active, String employmentTypeName,
			Pageable pageable, Double matchScoreFrom, Double matchScoreTo, Long candidateId) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected;
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		//EmploymentType employmentType = employmentTypeRepository.findByEmploymentType(employmentTypeName);
		EmploymentType employmentType = getEmploymentType(employmentTypeName);
		if (candidateId > 0 && (candidate.get().getEducations()!=null && !candidate.get().getEducations().isEmpty())) {
			if(matchScoreFrom >-1 && matchScoreTo >-1) {
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilterByEmploymentTypeForCandidateWithMatchScore(candidateId,
					employmentType.getId(), matchScoreFrom, matchScoreTo, pageable);
			} else {
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilterByEmploymentTypeForCandidate(candidateId,
						employmentType.getId(),  pageable);
			}
				
		}
		else {
			jobPage = jobRepository.findByJobStatusAndEmploymentType(active, employmentType, pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all {} actiev jobs is  {} secs", totalActiveJobs, (System.currentTimeMillis() - startTime) / 1000);
		if (matchScoreFrom < 0 && matchScoreTo < 0) {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(
					ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_EMPLOYMENT_TYPE + employmentTypeName,
					employmentType);
			jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
					ApplicationConstants.STATS_FOR_JOB_TYPE_BY_EMPLOYMENT_TYPE + employmentTypeName, employmentType);
			log.info("Time taken get stats on all active jobs is {} secs",
					(System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		log.debug("Page is {} Page number is {} size is {} and content numbers are {}",jobPage,jobPage.getNumber(), jobPage.getSize(), jobPage.getTotalElements());
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	
	}

	public Page<CandidateJobDTO> findActiveJobsByOneJobType(Integer active, String jobTypeName, Long candidateId, Double matchScoreFrom,
			Double matchScoreTo, Pageable pageable)
			throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		//JobType jobType = jobTypeRepository.findByJobType(jobTypeName);
		JobType jobType = getJobType(jobTypeName);
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		if(candidateId <0 || (candidate.get().getEducations()!=null && candidate.get().getEducations().isEmpty()))
			jobPage = jobRepository.findByJobStatusAndJobType(active, jobType, pageable);
		else {
			if(matchScoreFrom>-1 && matchScoreTo >-1)
				jobPage = jobRepository.findByJobStatusAndJobTypeForCandidateWithMatchScore(candidateId, jobType.getId(), matchScoreFrom, matchScoreTo, pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndJobTypeForCandidate(candidateId, jobType.getId(), pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected;
		if(matchScoreFrom < 0 && matchScoreTo < 0) {
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(
				ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_ONE_JOB_TYPE + jobTypeName, jobType, Boolean.FALSE);
		jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_BY_ONE_JOB_TYPE + jobTypeName, jobType, Boolean.FALSE);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected= null;
		}
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}

	public Page<CandidateJobDTO> findActiveJobsByTwoJobType(Integer active, String jobTypeName1, String jobTypeName2,
			Long candidateId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		/*JobType jobType1 = jobTypeRepository.findByJobType(jobTypeName1);
		JobType jobType2 = jobTypeRepository.findByJobType(jobTypeName2);*/
		JobType jobType1 = getJobType(jobTypeName1);
		JobType jobType2 = getJobType(jobTypeName2);
		Candidate candidate = candidateRepository.getOne(candidateId);
		if(candidateId<0 || (candidate.getEducations()!=null && candidate.getEducations().isEmpty()))
			jobPage = jobRepository.findByJobStatusAndTwoJobTypes(active, jobType1.getId(), jobType2.getId(), pageable);
		else {
			if(matchScoreFrom >-1 && matchScoreTo > -1)
				jobPage = jobRepository.findByJobStatusAndTwoJobTypesForCandidateWithMatchScore(candidateId, jobType1.getId(), jobType2.getId(), 
					matchScoreFrom,matchScoreTo,pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndTwoJobTypesForCandidate(candidateId, jobType1.getId(), jobType2.getId(), 
						pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected;
		
		if(matchScoreFrom <0 && matchScoreTo<0) {
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(
				ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_TWO_JOB_TYPE + jobTypeName1 + jobTypeName2, jobType1,
				jobType2);
		jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_BY_TWO_JOB_TYPE + jobTypeName1 + jobTypeName2, jobType1,
				jobType2);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}
	
	private JobType filterJobType(String jobTypeName1, String jobTypeName2, String jobTypeName3) throws Exception{
		Set<String> filterJobs = new HashSet<String>();
		filterJobs.add(jobTypeName3);
		filterJobs.add(jobTypeName1);
		filterJobs.add(jobTypeName2);
		Map<String,JobType> jobTypeMap = getJobTypeMap();
		log.debug("The main map is {}",jobTypeMap);
		Map<String,JobType> cloneJobTypeMap = jobTypeMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
		log.debug("The clone map is {}",cloneJobTypeMap);
		cloneJobTypeMap.keySet().removeAll(filterJobs);
		log.debug("The remaining job Type is {}",cloneJobTypeMap);
		return cloneJobTypeMap.get(cloneJobTypeMap.keySet().iterator().next());
		
	}

	public Page<CandidateJobDTO> findActiveJobsByThreeJobType(Integer active, String jobTypeName1, String jobTypeName2,
			String jobTypeName3, Long candidateId, Double matchScoreFrom, Double matchScoreTo,Pageable pageable) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		Candidate candidate = null;
		//JobType jobType = jobTypeRepository.findRemainingJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		JobType jobType = filterJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		if(candidateRepository.findById(candidateId).isPresent())
			candidate = candidateRepository.findById(candidateId).get();
		if(candidateId<0 || (candidate.getEducations()!=null && candidate.getEducations().isEmpty()))
			jobPage = jobRepository.findByJobStatusAndJobTypeNot(active, jobType, pageable);
		else {
			if(matchScoreFrom >-1 && matchScoreTo>-1)
				jobPage = jobRepository.findByJobStatusAndJobTypeNotForCandidateWithMatchScore(candidateId, jobType.getId(), matchScoreFrom,matchScoreTo,pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndJobTypeNotForCandidate(candidateId, jobType.getId(),pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected;
		if(matchScoreFrom<0 && matchScoreTo <0) {
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(
				ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_THREE_JOB_TYPE + jobTypeName1 + jobTypeName2
						+ jobTypeName3,
				jobType, Boolean.TRUE);
		 jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_BY_THREE_JOB_TYPE + jobTypeName1 + jobTypeName2 + jobTypeName3,
				jobType, Boolean.TRUE);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}
	
	private Map<String, JobType>getJobTypeMap() throws Exception {
		Map<String,JobType> jobTypeMap = jobTypeCacheManager.getValue(ApplicationConstants.JOB_TYPE, new Callable<Map<String,JobType>>() {
			public Map<String,JobType> call() throws Exception {
				log.debug("From repo {}",jobTypeRepository.findAll());
				return jobTypeRepository.findAll().stream().collect(Collectors.toMap(JobType::getJobType, jobType->jobType));
			}
		});
		return jobTypeMap;
	}
	
	private Map<String,EmploymentType> getEmploymentTypeMap() throws Exception {
		Map<String,EmploymentType> employmentTypeMap = employmentTypeCacheManager.getValue(ApplicationConstants.EMPLOYMENT_TYPE, new Callable<Map<String,EmploymentType>>() {
			public Map<String,EmploymentType> call() throws Exception {
				return employmentTypeRepository.findAll().stream().collect(Collectors.toMap(EmploymentType::getEmploymentType, employmenType->employmenType));
			}
		} );
		return employmentTypeMap;
	}
	
	private JobType getJobType(String jobTypeName) throws Exception {
		Map<String,JobType> jobTypeMap = jobTypeCacheManager.getValue(ApplicationConstants.JOB_TYPE, new Callable<Map<String,JobType>>() {
			public Map<String,JobType> call() throws Exception {
				return jobTypeRepository.findAll().stream().collect(Collectors.toMap(JobType::getJobType, jobType->jobType));
			}
		} );
		log.debug("The jobType type map is {}",jobTypeMap.get(jobTypeName));
		return jobTypeMap.get(jobTypeName);

	}
	
	private EmploymentType getEmploymentType(String employmentTypeName) throws Exception {
		Map<String,EmploymentType> employmentTypeMap = employmentTypeCacheManager.getValue(ApplicationConstants.EMPLOYMENT_TYPE, new Callable<Map<String,EmploymentType>>() {
			public Map<String,EmploymentType> call() throws Exception {
				return employmentTypeRepository.findAll().stream().collect(Collectors.toMap(EmploymentType::getEmploymentType, employmenType->employmenType));
			}
		} );
		log.debug("The employment type map is {}",employmentTypeMap);
		return employmentTypeMap.get(employmentTypeName);

	}

	public Page<CandidateJobDTO> findActiveJobsByOneEmploymentTypeAndOneJobType(Integer active,
			String employmentTypeName, String jobTypeName, Pageable pageable, Long candidateId, Double matchScoreFrom,
			Double matchScoreTo) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		//JobType jobType = jobTypeRepository.findByJobType(jobTypeName);
		JobType jobType = getJobType(jobTypeName);
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		EmploymentType employmentType = getEmploymentType(employmentTypeName);//employmentTypeRepository.findByEmploymentType(employmentTypeName);
		log.debug("Job Type and Employment Type are {} {}",jobType,employmentType);
		if(candidateId > 0 && (candidate.get().getEducations()!=null && !candidate.get().getEducations().isEmpty())) {
			if(matchScoreFrom > -1 && matchScoreTo > -1)
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndJobTypeForCandidateWithMatchScore(candidateId,
						employmentType.getId(), jobType.getId(), matchScoreFrom, matchScoreTo, pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndJobTypeForCandidate(candidateId,
						employmentType.getId(), jobType.getId(), pageable);
		}
		else 
			jobPage = jobRepository.findByJobStatusAndJobTypeAndEmploymentType(active, jobType, employmentType, pageable);
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected;
		
		if(matchScoreFrom <0 && matchScoreTo <0 ) {
			log.debug("Job Type and Employment Type are {} {}",jobType,employmentType);
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_ONE_JOB_TYPE
						+ employmentTypeName + jobTypeName, employmentType, jobType, Boolean.FALSE);
		jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType( ApplicationConstants.STATS_FOR_JOB_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_ONE_JOB_TYPE + employmentTypeName
						+ jobTypeName, employmentType, jobType, Boolean.FALSE);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}

	public Page<CandidateJobDTO> findActiveJobsByOneEmploymentTypeAndTwoJobType(Integer active,
			String employmentTypeName, String jobTypeName1, String jobTypeName2, Long candidateId, Double matchScoreFrom , Double matchScoreTo,
				Pageable pageable) throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		JobType jobType1 = getJobType(jobTypeName1);
		JobType jobType2 = getJobType(jobTypeName2);
		EmploymentType employmentType = getEmploymentType(employmentTypeName);
		Candidate candidate = candidateRepository.getOne(candidateId);
		if(candidateId<0 || (candidate.getEducations()!=null && candidate.getEducations().isEmpty()))
			jobPage = jobRepository.findByJobStatusAndOneEmploymentTypeAndTwoJobTypes(active, employmentType.getId(),
				jobType1.getId(), jobType2.getId(), pageable);
		else {
			if(matchScoreFrom >-1 && matchScoreTo > -1)
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndTwoJobTypeForCandidateWithMatchScore(candidateId, employmentType.getId(),
						jobType1.getId(), jobType2.getId(), matchScoreFrom, matchScoreTo, pageable);
			else 
				jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndTwoJobTypeForCandidate(candidateId, employmentType.getId(),
						jobType1.getId(), jobType2.getId(), pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected ;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected ;
		if(matchScoreFrom < 0 && matchScoreTo < 0) {
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_TWO_JOB_TYPE
						+ employmentTypeName + jobTypeName1 + jobTypeName2,employmentType, jobType1, jobType2);
		jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_TWO_JOB_TYPE + employmentTypeName+ jobTypeName1 + jobTypeName2,employmentType, jobType1, jobType2);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}

	public Page<CandidateJobDTO> findActiveJobsByOneEmploymentTypeAndThreeJobType(Integer active,
			String employmentTypeName, String jobTypeName1, String jobTypeName2, String jobTypeName3, Long candidateId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable)
			throws Exception {
		long startTime = System.currentTimeMillis();
		Page<Job> jobPage = null;
		//JobType jobType = jobTypeRepository.findRemainingJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		JobType jobType = filterJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		//EmploymentType employmentType = employmentTypeRepository.findByEmploymentType(employmentTypeName);
		EmploymentType employmentType = getEmploymentType(employmentTypeName);
		Candidate candidate = candidateRepository.getOne(candidateId);
		
		if(candidateId <0 || (candidate.getEducations()!=null && candidate.getEducations().isEmpty()))
			jobPage = jobRepository.findByJobStatusAndEmploymentTypeAndJobTypeNot(active, employmentType, jobType,
					pageable);
		else {
			if(matchScoreFrom>-1 && matchScoreTo > -1)
				jobPage = jobRepository.findByJobStatusAndEmploymentTypeAndJobTypeNotForCandidateWithMatchScore(candidateId, employmentType.getId(), jobType.getId(),matchScoreFrom,matchScoreTo,
					pageable);
			else
				jobPage = jobRepository.findByJobStatusAndEmploymentTypeAndJobTypeNotForCandidate(candidateId, employmentType.getId(), jobType.getId(),
						pageable);
		}
		Long totalActiveJobs = getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		log.info("Time taken to get all actiev jobs is  {} secs", (System.currentTimeMillis() - startTime) / 1000);
		final List<JobStatistics> jobStatisticsEmploymentTypeForEmploymentTypeSelected ;
		final List<JobStatistics> jobStatisticsJobTypeForEmploymentTypeSelected ;
		if(matchScoreFrom<0 && matchScoreTo<0) {
		jobStatisticsEmploymentTypeForEmploymentTypeSelected = getJobStatisticsEmploymentType(
				ApplicationConstants.STATS_FOR_EMPLOYMENT_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_THREE_JOB_TYPE
						+ employmentTypeName + jobTypeName1 + jobTypeName2 + jobTypeName3,
				employmentType, jobType, Boolean.TRUE);
		jobStatisticsJobTypeForEmploymentTypeSelected = getJobStatisticsJobType(
				ApplicationConstants.STATS_FOR_JOB_TYPE_BY_ONE_EMPLOYMENT_TYPE_AND_THREE_JOB_TYPE + employmentTypeName
						+ jobTypeName1 + jobTypeName2 + jobTypeName3,
				employmentType, jobType, Boolean.TRUE);
		log.info("Time taken get stats on all active jobs is {} secs", (System.currentTimeMillis() - startTime) / 1000);
		} else {
			jobStatisticsEmploymentTypeForEmploymentTypeSelected = null;
			jobStatisticsJobTypeForEmploymentTypeSelected = null;
		}
		
		return jobPage.map(job -> converter.convertToJobListingForCandidateOrGuest(job, totalActiveJobs,
				jobStatisticsEmploymentTypeForEmploymentTypeSelected, jobStatisticsJobTypeForEmploymentTypeSelected,
				candidateId));
	}
	
	public Long getCountOfAcitveJobs() throws Exception{
		return getTotalActiveJobCount(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
	}

	private Long getTotalActiveJobCount(String jobCount) throws Exception {
		return jobCountCacheManager.getValue(jobCount, new Callable<Long>() {
			public Long call() throws Exception {
				return jobRepository.countByJobStatus(ApplicationConstants.JOB_ACTIVE);
			}
		});
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType) throws Exception {
		return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByEmploymentType();
			}
		});
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType) throws Exception {
		return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByJobType();
			}
		});
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType,
			EmploymentType employmentType) throws Exception {
		return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository
						.findStatisticsCountByEmploymentTypeWhenEmploymentTypeSelected(employmentType.getId());
			}
		});
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType, EmploymentType employmentType)
			throws Exception {
		return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByJobTypeWhenEmployementTypeSelected(employmentType.getId());
			}
		});
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType, JobType jobType,
			Boolean exclude) throws Exception {
		if (exclude) {
			return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByEmploymentTypeWhenThreeJobTypeSelected(jobType.getId());
				}
			});
		} else {
			return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByEmploymentTypeWhenOneJobTypeSelected(jobType.getId());
				}
			});
		}
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType, JobType jobType, Boolean exclude)
			throws Exception {
		if (exclude) {
			return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByJobTypeWhenThreeJobTypeSelected(jobType.getId());
				}
			});
		} else {
			return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByJobTypeWhenOneJobTypeSelected(jobType.getId());
				}
			});
		}
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType, JobType jobType1,
			JobType jobType2) throws Exception {
		return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByEmploymentTypeWhenTwoJobTypeSelected(jobType1.getId(),
						jobType2.getId());
			}
		});
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType, JobType jobType1, JobType jobType2)
			throws Exception {
		return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByJobTypeWhenTwoJobTypeSelected(jobType1.getId(),
						jobType2.getId());
			}
		});
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType,
			EmploymentType employmentType, JobType jobType, Boolean exclude) throws Exception {
		if (exclude) {
			return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository
							.findStatisticsCountByEmploymentTypeWhenOneEmployementTypeAndThreeJobTypeSelected(
									employmentType.getId(), jobType.getId());
				}
			});
		} else {
			return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					log.debug("Fetching stats for {}, {}", jobType, employmentType);
					return jobRepository.findStatisticsCountByEmploymentTypeWhenOneEmploymentTypeAndOneJobTypeSelected(
							jobType.getId(), employmentType.getId());
				}
			});
		}
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType, EmploymentType employmentType,
			JobType jobType, Boolean exclude) throws Exception {
		if (exclude) {
			return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByJobTypeWhenOneEmployementTypeAndThreeJobTypeSelected(
							employmentType.getId(), jobType.getId());
				}
			});
		} else {
			return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
				public List<JobStatistics> call() throws Exception {
					return jobRepository.findStatisticsCountByJobTypeWhenOneEmploymentTypeAndOneJobTypeSelected(
							jobType.getId(), employmentType.getId());
				}
			});
		}
	}

	private List<JobStatistics> getJobStatisticsEmploymentType(String statsByEmploymentType,
			EmploymentType employmentType, JobType jobType1, JobType jobType2) throws Exception {
		return jobStatsCacheManager.getValue(statsByEmploymentType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByEmploymentTypeWhenOneEmploymentTypeAndTwoJobTypeSelected(
						employmentType.getId(), jobType1.getId(), jobType2.getId());
			}
		});
	}

	private List<JobStatistics> getJobStatisticsJobType(String statsByJobType, EmploymentType employmentType,
			JobType jobType1, JobType jobType2) throws Exception {
		return jobStatsCacheManager.getValue(statsByJobType, new Callable<List<JobStatistics>>() {
			public List<JobStatistics> call() throws Exception {
				return jobRepository.findStatisticsCountByJobTypeWhenOneEmploymentTypeAndTwoJobTypeSelected(
						employmentType.getId(), jobType1.getId(), jobType2.getId());
			}
		});
	}
	
	public Page<CandidateJobDTO> getNewActiveJobsListForCandidatesByEmploymentType(Pageable pageable, Long candidateId, Long employmentTypeId) {
		long startTime = System.currentTimeMillis();
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.get().getEducations();
		Page<Job> jobPage = null;
		final Page<CandidateJobDTO> page;
		if(candidateEducations.isEmpty()) {
			//get all jobs set score to 0
			jobPage = jobRepository.findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByEmploymentType(pageable,candidateId,employmentTypeId);
			log.info("Time taken to query new active Jobs by employemnt Type That candidate with no education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidateWithNoEducation(job, candidateId));;
		} else {
			jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidateByEmploymentType(candidateId, pageable,employmentTypeId);
			log.info("Time taken to query new active Jobs by employemnt Type That candidate with education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		}
		return page;
	}
	
	public Page<CandidateJobDTO> getNewActiveJobsListForCandidatesByJobType(Pageable pageable, Long candidateId,Long jobTypeId) {
		long startTime = System.currentTimeMillis();
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.get().getEducations();
		Page<Job> jobPage = null;
		final Page<CandidateJobDTO> page;
		if(candidateEducations.isEmpty()) {
			//get all jobs set score to 0
			jobPage = jobRepository.findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByJobType(pageable,candidateId,jobTypeId);
			log.info("Time taken to query new active Jobs by Job Type That candidate with no education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidateWithNoEducation(job, candidateId));;
		} else {
			jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidateByJobType(candidateId, pageable,jobTypeId);
			log.info("Time taken to query new active Jobs by Job  Type That candidate with education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		}
		return page;
	}
	
	public Page<CandidateJobDTO> getNewActiveJobsListForCandidatesByEmploymentTypeAndJobType(Pageable pageable, Long candidateId,Long employmentTypeId,Long jobTypeId) {
		long startTime = System.currentTimeMillis();
		Optional<Candidate> candidate = candidateRepository.findById(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.get().getEducations();
		Page<Job> jobPage = null;
		final Page<CandidateJobDTO> page;
		if(candidateEducations.isEmpty()) {
			//get all jobs set score to 0
			jobPage = jobRepository.findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByEmploymentAndJobType(pageable,candidateId,employmentTypeId,jobTypeId);
			log.info("Time taken to query new active Jobs by Employment Type and Job Type That candidate with no education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidateWithNoEducation(job, candidateId));;
		} else {
			jobPage = jobRepository.findByJobStatusAndMatchAndNotAppliedForCandidateByEmploymentAndJobType(candidateId, pageable,employmentTypeId,jobTypeId);
			log.info("Time taken to query new active Jobs by Employment Type and Job  Type That candidate with education has not applied yet is {} secs",(System.currentTimeMillis() - startTime)/1000);
			page = jobPage
					.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		}
		return page;
	}

	public Page<CandidateJobDTO> getAppliedJobsListForCandidates(Pageable pageable, Long candidateId) {
		Page<Job> jobPage = jobRepository.findAppliedJobByCandidate(candidateId, pageable);
		final Page<CandidateJobDTO> page = jobPage
				.map(job -> converter.convertToJobListingForCandidate(job, candidateId, false));
		return page;
	}

	public Page<CandidateProfileListDTO> getAppliedCandidatesForJob(Pageable pageable, Long jobId,Double fromScore, Double toScore) {
		Page<CandidateAppliedJobs> candidatePage;
		if(fromScore<0 && toScore <0) 
			 candidatePage = jobRepository.findByAppliedCandidates(jobId, pageable);
		else 
			candidatePage = jobRepository.findByAppliedCandidates(jobId,fromScore,toScore, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage
				.map(candidateAppliedJob -> converter.convertToCandidateProfileListingDTO(candidateAppliedJob,
						candidateRepository.findById(candidateAppliedJob.getId().getCandidateId()).get(),jobRepository.findById(jobId).get()));
		return page;
	}

	public Page<CandidateProfileListDTO> getMatchedCandidatesForJob(Pageable pageable, Long jobId, Double fromScore, Double toScore, Boolean reviewed) {
		Page<CandidateJob> candidatePage = null;
		if(fromScore == -1 && toScore == -1) {
			candidatePage = jobRepository.findMatchedCandidatesForJob( jobId,reviewed,pageable);
		} else {
			candidatePage = jobRepository.findMatchedCandidatesForJobWithMatchScoreFilter(jobId, fromScore, toScore,reviewed, pageable);
		}
		final Page<CandidateProfileListDTO> page = candidatePage.map(candidateJob -> converter
				.convertToCandidateProfileListingDTO(candidateJob.getCandidate(), candidateJob,candidateJob.getJob().getCorporate().getShortlistedCandidates()));
		return page;
	}

	/*public Job addCandidatesToExisitingJob(Long jobId, Long numberOfCandidates, Double jobCost,Double amountPaid) {
		Job job = jobRepository.findOne(jobId);
		Corporate corporate = job.getCorporate();
	}*/
	
	public Job applyJobForCandidate(Long jobId, Long loginId) {
		Optional<Job> jobOptional = jobRepository.findById(jobId);
		Candidate candidate = candidateRepository.findByLoginId(loginId);
		Job job = jobOptional.get();
		job.addAppliedCandidate(candidate);
		return jobRepository.save(job);
	}
	
	public Long getAppliedCandidatesForAllJobsByCorporate(Long corporateId) {
		return candidateAppliedJobsRepository.findAppliedCandidatesForAllJobsByCorporate(corporateId);
	}
	
	public Long getTotalJobsByCorporate(Long corporateId) {
		return jobRepository.countByCorporate(corporateId);
	}
	
	public Long getTotalCandidatedShorListedByCorporateForJob(Long corporateId,Long jobId) {
		return corporateRepository.findCountOfCandidatesShortlistedByJob(corporateId,jobId);
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
				.convertToCandidateProfileListingDTO(corporateCandidate.getCandidate(), corporateCandidate,jobId));
		return page;
	}

	public JobEconomicsDTO getJobForAddingCandidates(Long id) {
		Double  filterCost =0d ;
		Optional<Job> jobOptional = jobRepository.findById(id);
		Job job = jobOptional.get();
		Iterator<JobFilter> iterator = job.getJobFilters().iterator();
		while(iterator.hasNext()) {
			try {
				filterCost = extractJobFilterCost(iterator.next(),job);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return converter.convertToJobEconomicsDTO(job,filterCost);
	}
	
	public CandidateJobDTO getJobForCandidateView(Long jobId,Long candidateId) {
		Optional<Job> job = jobRepository.findById(jobId);
		Boolean hasEducation = candidateEducationService.doesCandidateHaveEducation(candidateId);
		return converter.convertJobViewForCandidate(job.get(), candidateId,hasEducation);		
	}
	
	public CandidateJobDTO getJobForGuest(Long jobId) {
		Optional<Job> job = jobRepository.findById(jobId);
		return converter.convertJobViewForCandidate(job.get(),-1L,false);		
	}
	
	private Double extractJobFilterCost(JobFilter jobFilter,Job job) throws Exception{
		Double filterCost = 0d; 
		Map<String,Double> filterMap = getFiltersMap();
		log.debug("Filter Map is {}",filterMap);
		ObjectMapper mapper = new ObjectMapper();
		JobFilterObject jobFilterObject = mapper.readValue(jobFilter.getFilterDescription(),JobFilterObject.class);
		if(jobFilterObject.getColleges()!=null && jobFilterObject.getColleges().size()>0)
			filterCost += filterMap.get(ApplicationConstants.COLLEGES);
		if(jobFilterObject.getUniversities()!=null && jobFilterObject.getUniversities().size()>0)
			filterCost += filterMap.get(ApplicationConstants.UNIVERSITIES);
		if(jobFilterObject.getGpa()!=null || jobFilterObject.getPercentage()!=null)
			filterCost += filterMap.get(ApplicationConstants.SCORES);
		if(jobFilterObject.getQualifications()!=null && jobFilterObject.getQualifications().size()>0)
			filterCost += filterMap.get(ApplicationConstants.QUALIFICATIONS);
		if(jobFilterObject.getCourses()!=null && jobFilterObject.getCourses().size()>0)
			filterCost += filterMap.get(ApplicationConstants.COURSES);
		if(jobFilterObject.getLanguages()!=null && jobFilterObject.getLanguages().size()>0)
			filterCost += filterMap.get(ApplicationConstants.LANGUAGES);
		if(jobFilterObject.getGender()!=null )
			filterCost += filterMap.get(ApplicationConstants.GENDER);
		if(jobFilterObject.getGraduationDate()!=null || jobFilterObject.getGraduationFromDate() !=null|| jobFilterObject.getGraduationToDate()!=null )
			filterCost += filterMap.get(ApplicationConstants.GRADUATION_DATE);
		filterCost += job.getEmploymentType().getEmploymentTypeCost();
		filterCost += job.getJobType().getJobTypeCost();
		return filterCost;
		
	}
	
	private Map<String,Double> getFiltersMap() throws Exception {
		Map<String,Double> filterMap = filtersCacheManager.getValue(ApplicationConstants.FILTER, new Callable<Map<String,Double>>() {
			public Map<String,Double> call() throws Exception {
				return filterRepository.findAll().stream().collect(Collectors.toMap(Filter::getFilterName, Filter::getFilterCost));
			}
		} );
		return filterMap;
	}
	
	public Job addCandidatesToJob(Job job) throws BeanCopyException,NoPreviousJobException {
		Job prevJob = null;
		Double amountPaid = job.getAmountPaid();
		Double jobCost = job.getJobCost();
		Integer noOfApplicants = job.getNoOfApplicants();
		Double escrowAmountUsed = job.getEscrowAmountUsed();
		Double corporateEscrowAmount = job.getCorporate().getEscrowAmount();
		Optional<Job> prevJobOptional = getJob(job.getId());
		if(prevJobOptional.isPresent())
			prevJob = prevJobOptional.get();
		else
			throw new NoPreviousJobException("No Previous job");
		log.debug("The number of applicants lefts are {}",noOfApplicants);
		log.debug("Incoming escrow is  {}",job.getCorporate().getEscrowAmount());
		try {
			BeanUtils.copyProperties(job, prevJob);
			ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
			job.setUpdateDate(dateTime);
			setJobHistory(job, prevJob);
			Set<CandidateJob> candidateJobs = new HashSet<>();
			candidateJobs.addAll(prevJob.getCandidateJobs());
			job.setCandidateJobs(candidateJobs);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("No of andidate jobs on prev job  is {} ",prevJob.getCandidateJobs().size());
		Corporate corporateFromPrevJob = prevJob.getCorporate();
		log.debug("Earlier escorw was  {} ",corporateFromPrevJob.getEscrowAmount());
		job.setNoOfApplicantLeft(noOfApplicants.longValue());
		job.setAmountPaid(amountPaid);
		job.setJobCost(jobCost);
		job.setNoOfApplicants(prevJob.getNoOfApplicants()+noOfApplicants);
		job.setEscrowAmountUsed(escrowAmountUsed);
		log.info("Updating job");
		job = jobRepository.save(job);
		log.info("Job updated {} ", job);
		if (job.getCorporate() != null && job.getCorporate().getEscrowAmount() != null) {
			corporateFromPrevJob.setEscrowAmount(corporateEscrowAmount);
			log.debug("Setting escrow as {}",corporateEscrowAmount);
			corporateRepository.save(corporateFromPrevJob);
		}
		return job;
	}
	
}
