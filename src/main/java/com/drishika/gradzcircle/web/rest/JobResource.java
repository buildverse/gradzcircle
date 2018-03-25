package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
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
import com.drishika.gradzcircle.service.JobService;
import com.drishika.gradzcircle.service.util.JobsUtil;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Job. IMPORTANT NOTE : WE AE NOT STROING THE JOB
 * FILTERS TO ELASTCI DUE TO INABILITY TO STORE JSON OBJECT AS IS ON FILTER
 * DESCRIPTION
 */
@RestController
@RequestMapping("/api")
public class JobResource {

	private final Logger log = LoggerFactory.getLogger(JobResource.class);

	private static final String ENTITY_NAME = "job";

	private final JobRepository jobRepository;

	private final JobSearchRepository jobSearchRepository;

	//private final JobFilterRepository jobFilterRepository;

	private final JobService jobService;

	public JobResource(JobRepository jobRepository, JobSearchRepository jobSearchRepository,
			JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository,
			CorporateRepository corporateRepository, CacheManager cacheManager,
			CorporateSearchRepository corporateSearchRepository, JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository,
			JobFilterHistoryRepository jobFilterHistoryRepository, JobService jobService) {
		this.jobRepository = jobRepository;
		this.jobSearchRepository = jobSearchRepository;
		//this.jobFilterRepository = jobFilterRepository;
		this.jobService = jobService;

	}

	/**
	 * POST /jobs : Create a new job.
	 *
	 * @param job
	 *            the job to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         job, or with status 400 (Bad Request) if the job has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/jobs")
	@Timed
	public ResponseEntity<Job> createJob(@RequestBody Job job) throws URISyntaxException {
		log.debug("REST request to save Job with JobFilter : {} , {}", job, job.getJobFilters());
		if (job.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new job cannot already have an ID"))
					.body(null);
		}
		Job result;
		try {
			result = jobService.createJob(job);
		} catch (BeanCopyException e) {
			result = new Job();
			result.jobDescription(e.getMessage());
			log.error("Error creating job {} , {}",e.getMessage(),e.getCause());
			
		}
		JobsUtil.trimJobFromFilter(result);
		return ResponseEntity.created(new URI("/api/jobs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /jobs : Updates an existing job.
	 *
	 * @param job
	 *            the job to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         job, or with status 400 (Bad Request) if the job is not valid, or
	 *         with status 500 (Internal Server Error) if the job couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/jobs")
	@Timed
	public ResponseEntity<Job> updateJob(@RequestBody Job job) throws URISyntaxException {
		log.debug("REST request to update Job with JobFilter : {} , {}, {} ,{}", job,job.getEmploymentType(),job.getJobType(), job.getJobFilters());
		if (job.getId() == null) {
			return createJob(job);
		}
		Job result;

		try {
			result = jobService.updateJob(job);
			
		} catch (BeanCopyException e) {
			result=job;
			result.jobDescription(e.getMessage());
			log.error("Error creating job {} , {}",e.getMessage(),e.getCause());
		}
		JobsUtil.trimJobFromFilter(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, job.getId().toString()))
				.body(result);
	}

	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/jobs")
	@Timed
	public List<Job> getAllJobs() {
		log.debug("REST request to get all Jobs");
		return jobRepository.findAll();
	}

	/**
	 * GET /jobs/:id : get the "id" job.
	 *
	 * @param id
	 *            the id of the job to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the job, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/jobs/{id}")
	@Timed
	public ResponseEntity<Job> getJob(@PathVariable Long id) {
		log.debug("REST request to get Job : {}", id);
		Set<JobFilter> jobFilters = null;
		Job job = jobRepository.findOne(id);
		
		//JobFilter jobFilter = jobFilterRepository.findByJob(job);
		JobFilter jobFilter = jobService.getJobFilter(job);
		if (jobFilter != null) {
			jobFilters = new HashSet<JobFilter>();
			jobFilters.add(jobFilter);
		}
		job.setJobFilters(jobFilters);
		JobsUtil.trimCorporateFromJob(job);
		JobsUtil.trimJobFromFilter(job);
		log.debug("exiting for {} with filters {}", id, job.getJobFilters());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(job));
	}

	/**
	 * DELETE /jobs/:id : delete the "id" job.
	 *
	 * @param id
	 *            the id of the job to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/jobs/{id}")
	@Timed
	public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
		log.debug("REST request to delete Job : {}", id);
		jobRepository.delete(id);
		jobSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/jobs?query=:query : search for the job corresponding to the
	 * query.
	 *
	 * @param query
	 *            the query of the job search
	 * @return the result of the search
	 */
	@GetMapping("/_search/jobs")
	@Timed
	public List<Job> searchJobs(@RequestParam String query) {
		log.debug("REST request to search Jobs for query {}", query);
		return StreamSupport.stream(jobSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
