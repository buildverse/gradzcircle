package com.drishika.gradzcircle.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
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
		// this.cacheManager = cacheManager;
		this.corporateSearchRepository = corporateSearchRepository;
		this.jobHistoryRepository = jobHistoryRepository;
		this.jobHistorySearchRepository = jobHistorySearchRepository;
		this.jobFilterRepository = jobFilterRepository;
		this.jobFilterHistoryRepository = jobFilterHistoryRepository;

	}

	public Job createJob(Job job) throws BeanCopyException {
		log.info("In create job {}", job);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		job.setCorporate(corporateRepository.findOne(job.getCorporate().getId()));
		job.setCreateDate(dateTime);
		job.setCanEdit(Boolean.TRUE);
		Job savedJob = jobRepository.save(job);
		jobSearchRepository.save(savedJob);
		/*
		 * if(job.getJobFilters() !=null) saveJobFilters(job, savedJob);
		 */
		return savedJob;
	}

	/*public Job updateJob(Job job) throws BeanCopyException {
		log.info("In updating job {}", job);
		JobHistory jobHistory = new JobHistory();
		Job prevJob = getJob(job.getId());
		if (job.getJobStatus() == 1 && !prevJob.isEverActive())
			job.setEverActive(Boolean.TRUE);
		if (!prevJob.isHasBeenEdited() && prevJob.isEverActive() && job.getJobStatus() == 1) {
			job.setHasBeenEdited(Boolean.TRUE);
		}
		if (prevJob.isHasBeenEdited() && job.getJobStatus() == 1)
			job.setCanEdit(Boolean.FALSE);
		saveJobFilters(job, prevJob);
		if (job.equals(prevJob)) {
			log.info("Jobs were same .. aborting update");
			return prevJob;
		}
		job.setCreateDate(prevJob.getCreateDate());

		JobsUtil.populateHistories(jobHistory, prevJob);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		jobHistory.job(prevJob);
		jobHistory.createDate(dateTime);
		job.setUpdateDate(dateTime);
		if (job.getJobFilters() != null) {
			job.getJobFilters().forEach(jobFilter -> {
				jobFilter.job(job);
			});
		}

		Job updatedJob = jobRepository.save(job);
		jobSearchRepository.save(updatedJob);
		jobHistorySearchRepository.save(jobHistoryRepository.save(jobHistory));

		if (job.getCorporate() != null && job.getCorporate().getEscrowAmount() != null) {
			Corporate corporate = corporateRepository.getOne(job.getCorporate().getId());
			corporate.setEscrowAmount(job.getCorporate().getEscrowAmount());
			corporateSearchRepository.save(corporateRepository.save(updatedJob.getCorporate()));
		}
		log.info("Job updated");
		return updatedJob;
	}
*/
	public Job updateJob(Job job) throws BeanCopyException, JobEditException {
		log.info("In updating job {}", job);
		if(!job.getCanEdit())
			throw new JobEditException("Cannot Edit job anymmore");
		Job prevJob = getJob(job.getId());
		if(!( job.equals(prevJob) && jobFiltersSame(prevJob, job) ) ) {
			updateJobMetaActions(job, prevJob);
			setJob(job,prevJob);
			setJobFilters(job,prevJob);
			log.info("Updating job");
		}
		jobSearchRepository.save(jobRepository.save(job));
		if (job.getCorporate() != null && job.getCorporate().getEscrowAmount() != null) {
			Corporate corporate = corporateRepository.getOne(job.getCorporate().getId());
			corporate.setEscrowAmount(job.getCorporate().getEscrowAmount());
			corporateSearchRepository.save(corporateRepository.save(corporate));
		}
		log.info("Job updated");
		return job;
	}
	
	private void setJob(Job job, Job prevJob) throws BeanCopyException {
		job.setCreateDate(prevJob.getCreateDate());
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		job.setUpdateDate(dateTime);
		setJobHistory(job, prevJob);
		
	}
	
	private void setJobHistory(Job job, Job prevJob) throws BeanCopyException{
		JobHistory jobHistory = new JobHistory();
		JobsUtil.populateHistories(jobHistory, prevJob);
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
		jobHistory.job(prevJob);
		jobHistory.createDate(dateTime);
		//Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		//jobHistories.add(jobHistory);
		//job.setHistories(jobHistories);
		job.addHistory(jobHistory);
		
		
	}
	private Boolean jobFiltersSame(Job job, Job prevJob) {
		log.info(" Current filter is {}",job.getJobFilters());
		log.info(" Prev filter is {}",prevJob.getJobFilters());
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

	private void setJobFilters(Job job,Job prevJob) throws BeanCopyException {
		if(jobFiltersSame(job, prevJob))
			return;
		log.info("In saving Job filter  {}", job.getJobFilters());
	//	Set<JobFilterHistory> jobFilterHistories = new HashSet<JobFilterHistory>();
		if (job.getJobFilters() != null && !job.getJobFilters().isEmpty()) {
			for (JobFilter jobFilter : job.getJobFilters()) {
				if (jobFilter.getId() != null) {
					JobFilter prevJobFilter = getJobFilter(jobFilter.getId());
					JobFilterHistory jobFilterHistory = new JobFilterHistory();
					JobsUtil.populateHistories(jobFilterHistory, prevJobFilter);
					jobFilterHistory.jobFilter(prevJobFilter);
	//				jobFilterHistories.add(jobFilterHistory);
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

}