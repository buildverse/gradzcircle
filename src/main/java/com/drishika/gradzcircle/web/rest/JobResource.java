package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.JobService;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.util.JobsUtil;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.errors.CustomParameterizedException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.drishika.gradzcircle.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

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

	// private final JobFilterRepository jobFilterRepository;

	private final JobService jobService;

	public JobResource(JobRepository jobRepository, JobSearchRepository jobSearchRepository, JobService jobService) {
		this.jobRepository = jobRepository;
		this.jobSearchRepository = jobSearchRepository;
		// this.jobFilterRepository = jobFilterRepository;
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
	            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
		 }
		Job result;
		try {
			result = jobService.createJob(job);
		} catch (BeanCopyException e) {
			result = new Job();
			result.jobDescription(e.getMessage());
			log.error("Error creating job {} , {}", e.getMessage(), e.getCause());

		}
		// JobsUtil.trimJobFromFilter(result);
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
		log.debug("REST request to update Job with JobFilter : {} , {}, {} ,{}", job, job.getEmploymentType(),
				job.getJobType(), job.getJobFilters());
		if (job.getId() == null) {
			return createJob(job);
		}
		Job result;
		Job updatedJob = new Job();

		try {
			result = jobService.updateJob(job);
			// BeanUtils.copyProperties(updatedJob, result);
			log.info("Updated job is {},{}", updatedJob, updatedJob.getJobFilters());
		} catch (BeanCopyException e) {
			result = job;
			result.setJobDescription(e.getMessage());
			log.error("Error updating job {} , {}", e.getMessage(), e.getCause());
		} catch (JobEditException e) {
			result = job;
			result.setJobDescription(e.getMessage());
			log.error("Error updating job {} , {}", e.getMessage(), e.getCause());
			throw new CustomParameterizedException(e.getMessage());
		} catch (Exception ex) {
			result = job;
			result.setJobDescription(ex.getMessage());
			log.error("Error updating job {} , {}", ex.getMessage(), ex.getCause());
			throw new CustomParameterizedException(ex.getMessage());
		}

		// JobsUtil.trimJobFromFilter(updatedJob);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/jobs")
	@Timed
	public ResponseEntity<List<Job>> getAllJobs(@ApiParam Pageable pageable) {
		log.debug("REST request to get all Jobs");
		Page<Job> jobs = jobRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(jobs, "/api/jobs");
		return new ResponseEntity<>(jobs.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /activeJobs for Corporates: get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsForCorporate/{corporateId}")
	@Timed
	public ResponseEntity<List<CorporateJobDTO>> getActiveJobsListForCorporates(@ApiParam Pageable pageable,
			@PathVariable Long corporateId) {
		final Page<CorporateJobDTO> page = jobService.getActiveJobsListForCorporates(pageable, corporateId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsForCorporate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /activeJobs for Candidates : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/newActiveJobsForCandidate/{candidateId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getNewActiveJobsListForCandidates(@ApiParam Pageable pageable,
			@PathVariable Long candidateId) {
		final Page<CandidateJobDTO> page = jobService.getNewActiveJobsListForCandidates(pageable, candidateId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newActiveJobsForCandidate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /matchedCandiatesForJob : Matched candidates for Job
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/matchedCandiatesForJob/{jobId}")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getMatchedCandidatesForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId) {
		final Page<CandidateProfileListDTO> page = jobService.getMatchedCandidatesForJob(pageable, jobId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/matchedCandiatesForJob");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /appliedCandiatesForJob : Applied candidates for Job
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/appliedCandiatesForJob/{jobId}")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getAppliedCandidatesForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId) {
		final Page<CandidateProfileListDTO> page = jobService.getAppliedCandidatesForJob(pageable, jobId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appliedCandiatesForJob");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /appliedJobsByCandidate : Applied Jobs by Candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/appliedJobsByCandidate/{candidateId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getAppliedJobsByCandidate(@ApiParam Pageable pageable,
			@PathVariable Long candidateId) {
		final Page<CandidateJobDTO> page = jobService.getAppliedJobsListForCandidates(pageable, candidateId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appliedJobsByCandidate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * Apply for Job
	 */

	@GetMapping("/applyForJob/{jobId}/{loginId}")
	@Timed
	public ResponseEntity<Job> applyForJob(@PathVariable Long jobId, @PathVariable Long loginId)
			throws URISyntaxException {
		log.debug("REST request to ApplyForJob ");
		Job result = jobService.applyJobForCandidate(jobId, loginId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
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

		if (job != null) {
			JobFilter jobFilter = jobService.getJobFilter(job);
			if (jobFilter != null) {
				jobFilters = new HashSet<JobFilter>();
				jobFilters.add(jobFilter);
				job.setJobFilters(jobFilters);
				JobsUtil.trimJobFromFilter(job);
			}

			JobsUtil.trimCorporateFromJob(job);

			// log.debug("exiting for {} with filters {}", id, job.getJobFilters());
		}

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
	 * Remove /deActivatejob/:id : Deactivate the "id" job.
	 *
	 * @param id
	 *            the id of the job to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/deActivateJob/{id}")
	@Timed
	public ResponseEntity<Void> removeJob(@PathVariable Long id) {
		log.debug("REST request to remove Job : {}", id);
		try {
			jobService.deActivateJob(id);

		} catch (BeanCopyException e) {
			log.error("Error creating job {} , {}", e.getMessage(), e.getCause());
			return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
					.build();
		}

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
	public ResponseEntity<List<Job>> searchJobs(@RequestParam String query, @ApiParam Pageable pageable) {
		log.debug("REST request to search Jobs for query {}", query);
		Page<Job> page = jobSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

}
