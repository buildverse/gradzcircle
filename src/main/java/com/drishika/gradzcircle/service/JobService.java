package com.drishika.gradzcircle.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.exceptions.BeanCopyException;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.repository.search.JobFilterSearchRepository;
import com.drishika.gradzcircle.repository.search.JobHistorySearchRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.util.JobsUtil;

@Service
@Transactional
public class JobService {

	private final Logger log = LoggerFactory.getLogger(JobService.class);

	private final JobRepository jobRepository;

	private final JobSearchRepository jobSearchRepository;

	private final JobFilterRepository jobFilterRepository;

	private final JobFilterHistoryRepository jobFilterHistoryRepository;

	private final JobHistoryRepository jobHistoryRepository;

	private final JobHistorySearchRepository jobHistorySearchRepository;

	private final CacheManager cacheManager;

	private final CorporateRepository corporateRepository;

	private final CorporateSearchRepository corporateSearchRepository;

	public JobService(JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository,
			CorporateRepository corporateRepository, CacheManager cacheManager,
			CorporateSearchRepository corporateSearchRepository, JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository,
			JobFilterHistoryRepository jobFilterHistoryRepository) {
		this.jobRepository = jobRepository;
		this.jobSearchRepository = jobSearchRepository;
		this.corporateRepository = corporateRepository;
		this.cacheManager = cacheManager;
		this.corporateSearchRepository = corporateSearchRepository;
		this.jobHistoryRepository = jobHistoryRepository;
		this.jobHistorySearchRepository = jobHistorySearchRepository;
		this.jobFilterRepository = jobFilterRepository;
		this.jobFilterHistoryRepository = jobFilterHistoryRepository;

	}

	public Job createJob(Job job) throws BeanCopyException {
		log.debug("In Job Service to create Job with JobFilter : {} , {}", job, job.getJobFilters());
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		job.setCorporate(corporateRepository.findOne(job.getCorporate().getId()));
		job.setCreateDate(dateTime);
		job.setCanEdit(Boolean.TRUE);
		Job savedJob = jobRepository.save(job);
		jobSearchRepository.save(savedJob);
		saveJobFilters(job, savedJob);
		return savedJob;
	}

	public Job updateJob(Job job) throws BeanCopyException {
		log.debug("In Job Service to update Job with JobFilter : {} , {}", job, job.getJobFilters());
		JobHistory jobHistory = new JobHistory();
		Job prevJob = getJob(job.getId());
		log.debug("The rpev job is ++++++++++++++++{},{},{},{}", prevJob, prevJob.getEmploymentType(),
				prevJob.getJobType(), prevJob.equals(job));
		saveJobFilters(job, prevJob);
		job.setCreateDate(prevJob.getCreateDate());
		if(job.getJobStatus()==1 && !prevJob.isEverActive())
			job.setEverActive(Boolean.TRUE);
		if (!prevJob.isHasBeenEdited() && prevJob.isEverActive() && job.getJobStatus()==1) {
			job.setHasBeenEdited(Boolean.TRUE);
		}
		if(prevJob.isHasBeenEdited() && job.getJobStatus()==1)
			job.setCanEdit(Boolean.FALSE);
		JobsUtil.populateHistories(jobHistory, prevJob);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		jobHistory.job(prevJob);
		jobHistory.createDate(dateTime);
		job.setUpdateDate(dateTime);
		if (job.getJobFilters() != null) {
			job.getJobFilters().forEach(jobFilter -> {
				jobFilter.job(job);
			});
		}

		Job updatedJob = jobRepository.save(job);
		jobHistorySearchRepository.save(jobHistoryRepository.save(jobHistory));
		jobSearchRepository.save(updatedJob);
		if (job.getCorporate() != null && job.getCorporate().getEscrowAmount() != null) {
			Corporate corporate = corporateRepository.getOne(job.getCorporate().getId());
			corporate.setEscrowAmount(job.getCorporate().getEscrowAmount());
			corporateSearchRepository.save(corporateRepository.save(updatedJob.getCorporate()));
		}
		return updatedJob;
	}

	private void saveJobFilters(Job job, Job savedJob) throws BeanCopyException {
		log.debug("In save for job filters {} and saved job {} and its filters are {}", job.getJobFilters(), savedJob,
				savedJob.getJobFilters());
		Set<JobFilterHistory> jobFilterHistories= new HashSet<JobFilterHistory>();
		if (job.getJobFilters() != null && !job.getJobFilters().isEmpty()) {
			for (JobFilter jobFilter : job.getJobFilters()) {
				if (jobFilter.getId() != null) {
					JobFilter prevJobFilter = getJobFilter(jobFilter.getId());
					log.debug("Previous job filter is {}",prevJobFilter );
					JobFilterHistory jobFilterHistory = new JobFilterHistory();
					JobsUtil.populateHistories(jobFilterHistory, prevJobFilter);
					
					jobFilterHistory.jobFilter(prevJobFilter);
					jobFilterHistories.add(jobFilterHistory);
					jobFilter.job(savedJob);
				} else {
					jobFilter.job(savedJob);
				}
			}
		}
		jobFilterRepository.save(job.getJobFilters());
		log.debug("JSaving what ======= {}",jobFilterHistories );
		jobFilterHistoryRepository.save(jobFilterHistories);
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
	
	public void deActivateJob (Long id) {
		log.info("Deactivating job {}",id);
		Job job = jobRepository.getOne(id);
		job.setJobStatus(ApplicationConstants.JOB_DEACTIVATE);
		jobSearchRepository.save(jobRepository.save(job));
		log.info("Deactivated job {}",id);
	}

}
