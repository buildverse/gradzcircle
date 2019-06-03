package com.drishika.gradzcircle.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
import com.drishika.gradzcircle.repository.CandidateAppliedJobsRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
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
import com.drishika.gradzcircle.service.dto.JobStatistics;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
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
	
	private final GradzcircleCacheManager <String,List<JobStatistics>> jobStatsCacheManager;
	
	private final GradzcircleCacheManager <String,Long> jobCountCacheManager;
	
	private final GradzcircleCacheManager<String, Map<String,JobType>> jobTypeCacheManager;
	
	private final GradzcircleCacheManager<String, Map<String,EmploymentType>> employmentTypeCacheManager;
	private final EmploymentTypeRepository employmentTypeRepository;
	
	private final JobTypeRepository jobTypeRepository;
	
	private final CacheManager cacheManager;

	public JobService(JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository,
			CorporateRepository corporateRepository, CacheManager cacheManager,
			CorporateSearchRepository corporateSearchRepository, JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository,
			JobFilterHistoryRepository jobFilterHistoryRepository, Matcher<Job> matcher,
			CandidateRepository candidateRepository, DTOConverters converter, CandidateAppliedJobsRepository candidateAppliedJobsRepository,
			CorporateService corporateService, GradzcircleCacheManager <String,List<JobStatistics>> jobStatsCacheManager,
			EmploymentTypeRepository employmentTypeRepository,JobTypeRepository jobTypeRepository,GradzcircleCacheManager <String,Long> jobCountCacheManager,
			GradzcircleCacheManager<String, Map<String,JobType>> jobTypeCacheManager, GradzcircleCacheManager<String, Map<String,EmploymentType>> employmentTypeCacheManager
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
		Integer prevJobStatus = prevJob.getJobStatus();
		job = jobRepository.save(job);
		//FIXME make cache refresh aysnc post update
		if(!job.getJobStatus().equals(prevJobStatus))
			jobCountCacheManager.removeFromCache(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
		if(!(job.getEmploymentType().equals(prevJob.getEmploymentType())) || ! (job.getJobType().equals(prevJob.getJobType())) || !job.getJobStatus().equals(prevJobStatus))
			jobStatsCacheManager.clearCache();
		
		
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
		jobStatsCacheManager.clearCache();
		jobCountCacheManager.removeFromCache(ApplicationConstants.COUNT_OF_ACTIVE_JOBS);
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
	//	List<JobStatistics> jobStats = jobRepository.findStatisticsByShortlistedJobsForCandidates(candidateId);
		final Page<CandidateJobDTO> page = jobPage.map(job -> converter.convertToJobListingForCandidate(job,candidateId,false));
		return page;
	}

	public Page<CandidateJobDTO> getNewActiveJobsListForCandidates(Pageable pageable, Long candidateId, Double matchScoreFrom, Double matchScoreTo) {
		long startTime = System.currentTimeMillis();
		Candidate candidate = candidateRepository.findOne(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.getEducations();
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
		//EmploymentType employmentType = employmentTypeRepository.findByEmploymentType(employmentTypeName);
		EmploymentType employmentType = getEmploymentType(employmentTypeName);
		if (candidateId > 0) {
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
		if(candidateId <0)
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
		if(candidateId<0)
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
		//JobType jobType = jobTypeRepository.findRemainingJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		JobType jobType = filterJobType(jobTypeName1, jobTypeName2, jobTypeName3);
		if(candidateId<0)
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
		
		EmploymentType employmentType = getEmploymentType(employmentTypeName);//employmentTypeRepository.findByEmploymentType(employmentTypeName);
		log.debug("Job Type and Employment Type are {} {}",jobType,employmentType);
		if(candidateId > 0) {
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
		if(candidateId<0)
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
		if(candidateId <0 )
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
		Candidate candidate = candidateRepository.findOne(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.getEducations();
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
		Candidate candidate = candidateRepository.findOne(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.getEducations();
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
		Candidate candidate = candidateRepository.findOne(candidateId);
		Set<CandidateEducation> candidateEducations = candidate.getEducations();
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

	public Page<CandidateProfileListDTO> getAppliedCandidatesForJob(Pageable pageable, Long jobId) {
		Page<CandidateAppliedJobs> candidatePage = jobRepository.findByAppliedCandidates(jobId, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage
				.map(candidateAppliedJob -> converter.convertToCandidateProfileListingDTO(candidateAppliedJob,
						candidateRepository.findOne(candidateAppliedJob.getId().getCandidateId()),jobRepository.findOne(jobId)));
		return page;
	}

	public Page<CandidateProfileListDTO> getMatchedCandidatesForJob(Pageable pageable, Long jobId, Double fromScore, Double toScore) {
		Page<CandidateJob> candidatePage = null;
		if(fromScore == -1 && toScore == -1) {
			candidatePage = jobRepository.findMatchedCandidatesForJob(jobId, pageable);
		} else {
			candidatePage = jobRepository.findMatchedCandidatesForJobWithMatchScoreFilter(jobId, fromScore, toScore, pageable);
		}
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
	
	/*public List<JobStatistics> getJobStatsByEmploymentType(Long employmentType) {
		List<JobStatistics> jobStatistics = null;
		if(employmentType!=null) {
			jobStatistics = jobRepository.findStatisticsCountByEmploymentTypeSelection(employmentType);
		} 
		return jobStatistics;
	}
	
	public List<JobStatistics> getJobStatsByJobType(Long jobType) {
		List<JobStatistics> jobStatistics = null;
		if(jobType!=null) {
			jobStatistics = jobRepository.findStatisticsCountByJobTypeSelection(jobType);
		} 
		return jobStatistics;
	}*/
	
}
