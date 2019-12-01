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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.exception.BeanCopyException;
import com.drishika.gradzcircle.exception.JobEditException;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.security.AuthoritiesConstants;
import com.drishika.gradzcircle.service.JobService;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.dto.JobEconomicsDTO;
import com.drishika.gradzcircle.service.dto.JobListDTO;
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
		log.info("REST request to save Job with JobFilter : {} , {}", job, job.getJobFilters());
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
		log.info("REST request to update Job with JobFilter and escrow : {} , {}, {} ,{}, {}", job, job.getEmploymentType(),
				job.getJobType(), job.getJobFilters(),job.getCorporate().getEscrowAmount());
		if (job.getId() == null) {
			return createJob(job);
		}
		Job result;
		Job updatedJob = new Job();

		try {
			result = jobService.updateJob(job);
			// BeanUtils.copyProperties(updatedJob, result);
			log.info("Updated job is {},{}", result, result.getJobFilters());
		} catch (BeanCopyException e) {
		
			result = job;
			result.setJobDescription(e.getMessage());
			log.error("Error updating job {} , {}", e.getMessage(), e);
		} catch (JobEditException e) {
			
			result = job;
			result.setJobDescription(e.getMessage());
			log.error("Error updating job {} , {}", e.getMessage(), e);
			throw new CustomParameterizedException(e.getMessage());
		} catch (Exception ex) {
		
			result = job;
			result.setJobDescription(ex.getMessage());
			log.error("Error updating job {} , {}", ex.getMessage(), ex);
			throw new CustomParameterizedException(ex.getMessage());
		}

		// JobsUtil.trimJobFromFilter(updatedJob);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}
	
	@PutMapping("/addCandidatesToJob")
	@Timed
	public ResponseEntity<Job> addCandidatesToAnExistingJob(@RequestBody Job job) throws URISyntaxException {
		log.info("REST request to add candidates to job {}", job);
		if (job.getId() == null) {
			log.error("Error updating job {} ", "Job Id is null");
			throw new CustomParameterizedException("Job Id is null");
		}
		Job updatedJob;
		try {
			updatedJob = jobService.addCandidatesToJob(job);
			// BeanUtils.copyProperties(updatedJob, result);
			log.info("Updated job is {}", updatedJob);
		} catch (BeanCopyException e) {
		
			updatedJob = job;
			updatedJob.setJobDescription(e.getMessage());
			log.error("Error updating job {} , {}", e.getMessage(), e);
		} catch (Exception ex) {
		
			updatedJob = job;
			updatedJob.setJobDescription(ex.getMessage());
			log.error("Error updating job {} , {}", ex.getMessage(), ex);
			throw new CustomParameterizedException(ex.getMessage());
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedJob.getId().toString()))
				.body(updatedJob);
	}
	
	@RequestMapping(value = "/matchAllJobsToCandidates", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
	public ResponseEntity<Void> matchAllJobsAndCandidates() throws URISyntaxException {
		jobService.beginMatchingAllJobsAndCandidates();

		return ResponseEntity.accepted().headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
				.build();				
	}

	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/countOfActiveJobs")
	@Timed
	public ResponseEntity<Long> getCountOfActiveJobs() {
		log.info("REST request to get count of active Jobs");
		Long countofJobs;
		try {
			countofJobs = jobService.getCountOfAcitveJobs();
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}", e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, countofJobs.toString()))
		.body(countofJobs);
	}
	
	/**
	 * GET /activeJobCount : get count of all active jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/jobs")
	@Timed
	public ResponseEntity<List<Job>> getAllJobs(@ApiParam Pageable pageable) {
		log.info("REST request to get all Jobs");
		Page<Job> jobs = jobRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(jobs, "/api/jobs");
		return new ResponseEntity<>(jobs.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobs")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getAllActiveJobs(@ApiParam Pageable pageable) {
		log.info("REST request to get all Jobs with Stats");
		Page<CandidateJobDTO> page =null;
		try {
			page = jobService.findAllActiveJobsOnPortal(ApplicationConstants.JOB_ACTIVE, pageable);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByEmploymentType/{employmentType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByEmploymentType(@PathVariable String employmentType,
			@ApiParam Pageable pageable, @PathVariable Double matchScoreFrom, @PathVariable Double matchScoreTo,
			@PathVariable Long candidateId) {
		log.info("REST request to get Jobs and stats by employment Type for candidate  {} , {} with macth score from {} to {} ",
				employmentType,candidateId, matchScoreFrom, matchScoreTo);
		Page<CandidateJobDTO> page = null;
		try {
			page = jobService.findActiveJobsByEmploymentType(ApplicationConstants.JOB_ACTIVE, employmentType, pageable,
					matchScoreFrom, matchScoreTo,candidateId);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}", e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByEmploymentType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByOneJobType/{jobType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByOneJobType(@PathVariable String jobType, @PathVariable Long candidateId,
			@PathVariable Double matchScoreFrom,@PathVariable Double matchScoreTo, @ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by one job Type --> {} for candidate {} matchScore from {} to {}",
				jobType,candidateId,matchScoreFrom,matchScoreTo);
		Page<CandidateJobDTO> page =null;
		try {
			page = jobService.findActiveJobsByOneJobType(ApplicationConstants.JOB_ACTIVE, jobType, candidateId,matchScoreFrom,matchScoreTo,pageable);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByOneJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByTwoJobTypes/{jobType1}/{jobType2}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByTwoJobTypes(@PathVariable String jobType1,@PathVariable String jobType2, 
			@PathVariable Long candidateId, @PathVariable Double matchScoreFrom , @PathVariable Double matchScoreTo, @ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by two job Types --> {}, {} for candidate {} with scores from {} to {}",
					jobType1,jobType2,candidateId,matchScoreFrom,matchScoreTo);
		Page<CandidateJobDTO> page =null;
		try {
			page = jobService.findActiveJobsByTwoJobType(ApplicationConstants.JOB_ACTIVE, jobType1, jobType2, candidateId,
					matchScoreFrom,matchScoreTo,pageable);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByTwoJobTypes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByThreeJobTypes/{jobType1}/{jobType2}/{jobType3}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByThreeJobTypes(@PathVariable String jobType1, 
			@PathVariable String jobType2,@PathVariable String jobType3, @PathVariable Long candidateId, @PathVariable Double matchScoreFrom,
			@PathVariable Double matchScoreTo, @ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by three job Types --> {}, {}, {} for candidate {} for match score from {} to {} "
				,jobType1,jobType2,jobType3,candidateId,matchScoreFrom,matchScoreTo);
		Page<CandidateJobDTO> page =null;
		try {
			page = jobService.findActiveJobsByThreeJobType(ApplicationConstants.JOB_ACTIVE, jobType1, jobType2, 
					jobType3, candidateId, matchScoreFrom,matchScoreTo, pageable);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByThreeJobTypes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByOneEmploymentTypeAndOneJobType/{employmentType}/{jobType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByOneEmploymentTypeAndOneJobType(
			@PathVariable String employmentType, @PathVariable String jobType,
			@PathVariable Long candidateId, @PathVariable Double matchScoreFrom, @PathVariable Double matchScoreTo, @ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by one employment and job Type {}, {} for Canidate {} with MatcScore from {} to {} ", employmentType, 
				jobType,candidateId,matchScoreFrom,matchScoreTo);
		log.debug("Length on job type is {}",jobType.length());
		log.debug("Length on trimmed job type is {}",jobType.trim().length());
		jobType = jobType.trim();
		Page<CandidateJobDTO> page = null;
		try {
			page = jobService.findActiveJobsByOneEmploymentTypeAndOneJobType(ApplicationConstants.JOB_ACTIVE,
					employmentType, jobType, pageable,candidateId, matchScoreFrom,matchScoreTo);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}", e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/activeJobsByOneEmploymentTypeAndOneJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByOneEmploymentTypeAndTwoJobType/{employmentType}/{jobType1}/{jobType2}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByOneEmploymentTypeAndTwoJobType(@PathVariable String employmentType,@PathVariable String jobType1,
			@PathVariable String jobType2, @PathVariable Long candidateId,@PathVariable Double matchScoreFrom, @PathVariable Double matchScoreTo, @ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by one Employent and two job Type -> {}, {}, {} for candidate {} with Match scores from {} to {}",
					employmentType,jobType1,jobType2,candidateId,matchScoreFrom,matchScoreTo);
		Page<CandidateJobDTO> page =null;
		jobType1 = jobType1.trim();
		jobType2 = jobType2.trim();
		try {
			page = jobService.findActiveJobsByOneEmploymentTypeAndTwoJobType(ApplicationConstants.JOB_ACTIVE, employmentType, jobType1, jobType2, candidateId,
					matchScoreFrom,matchScoreTo, pageable);
			
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByOneEmploymentTypeAndTwoJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /jobs : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/activeJobsByOneEmploymentTypeAndThreeJobType/{employmentType}/{jobType1}/{jobType2}/{jobType3}/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getActiveJobsByOneEmploymentTypeAndThreeJobType(@PathVariable String employmentType,@PathVariable String jobType1, 
			@PathVariable String jobType2,@PathVariable String jobType3, @PathVariable Long candidateId, @PathVariable Double matchScoreFrom, @PathVariable Double matchScoreTo, 
				@ApiParam Pageable pageable) {
		log.info("REST request to get Jobs and stats by One EmploymentType and three job Type ---> {}, {}, {}, {} for candidate {} with matchScore From {} matchScoreTo {}",
				employmentType,jobType1,jobType2,jobType3,candidateId,matchScoreFrom,matchScoreTo);
		Page<CandidateJobDTO> page =null;
		jobType3 = jobType3.trim();
		try {
			page = jobService.findActiveJobsByOneEmploymentTypeAndThreeJobType(ApplicationConstants.JOB_ACTIVE, employmentType, jobType1, jobType2, jobType3,
					candidateId,matchScoreFrom,matchScoreTo ,pageable);
		} catch (Exception e) {
			log.error("Exception occured while gtting Job data {}",e);
			throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activeJobsByOneEmploymentTypeAndThreeJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
	 * GET /shortListedJob for Candidate: get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/shortListedJobsForCandidate/{candidateId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getShortListedJobsListForCcandidate(@ApiParam Pageable pageable,
			@PathVariable Long candidateId) {
		log.info("Request to get jobs that candidate {} got shortlisted for ",candidateId);
		final Page<CandidateJobDTO> page = jobService.getShortListedJobsListForCandidate(pageable, candidateId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shortListedJobsForCandidate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /activeJobs for Candidates : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/newActiveJobsForCandidate/{candidateId}/{matchScoreFrom}/{matchScoreTo}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getNewActiveJobsListForCandidates(@ApiParam Pageable pageable,
			@PathVariable Long candidateId, @PathVariable Double matchScoreFrom, @PathVariable Double matchScoreTo) {
		final Page<CandidateJobDTO> page = jobService.getNewActiveJobsListForCandidates(pageable, candidateId,matchScoreFrom,matchScoreTo);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newActiveJobsForCandidate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /activeJobs for Candidates by Employment Type: get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/newActiveJobsForCandidateByEmploymentType/{candidateId}/{employmentTypeId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getNewActiveJobsListForCandidatesByEmploymentType(@ApiParam Pageable pageable,
			@PathVariable Long candidateId,@PathVariable Long employmentTypeId) {
		final Page<CandidateJobDTO> page = jobService.getNewActiveJobsListForCandidatesByEmploymentType(pageable, candidateId,employmentTypeId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newActiveJobsForCandidateByEmploymentType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /activeJobs for Candidates By Job Type : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/newActiveJobsForCandidateByJobType/{candidateId}/{jobTypeId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getNewActiveJobsListForCandidatesByJobType(@ApiParam Pageable pageable,
			@PathVariable Long candidateId,@PathVariable Long jobTypeId) {
		final Page<CandidateJobDTO> page = jobService.getNewActiveJobsListForCandidatesByJobType(pageable, candidateId,jobTypeId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newActiveJobsForCandidateByJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /activeJobs for Candidates By Job and Employment Type : get all the jobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/newActiveJobsForCandidateByEmploymentAndJobType/{candidateId}/{employmentTypeId}/{jobTypeId}")
	@Timed
	public ResponseEntity<List<CandidateJobDTO>> getNewActiveJobsListForCandidatesByJobAndEmploymentType(@ApiParam Pageable pageable,
			@PathVariable Long candidateId,@PathVariable Long employmentTypeId,@PathVariable Long jobTypeId) {
		final Page<CandidateJobDTO> page = jobService.getNewActiveJobsListForCandidatesByEmploymentTypeAndJobType(pageable, candidateId,employmentTypeId,jobTypeId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newActiveJobsForCandidateByEmploymentAndJobType");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /matchedCandiatesForJob : Matched candidates for Job
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/matchedCandiatesForJob/{jobId}/{fromScore}/{toScore}/{reviewed}")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getMatchedCandidatesForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId, @PathVariable Double fromScore,@PathVariable Double toScore, @PathVariable Boolean reviewed) {
		log.info("Getting matched candidates for job {} with match score from {} to {} and reviwe status {}",jobId,fromScore,toScore,reviewed);
		final Page<CandidateProfileListDTO> page = jobService.getMatchedCandidatesForJob(pageable, jobId,fromScore,toScore,reviewed);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/matchedCandiatesForJob");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * Get Jobs Candidate ShortListed For
	 * @param pageable
	 * @param CorproateId
	 * @param CandidateId
	 * @return
	 */
	@GetMapping("/jobListForCandidateShortlistedByCorporate/{candidateId}/{corporateId}")
	@Timed
	public List<JobListDTO> getJobListCandidatesShortListedFor(@ApiParam Pageable pageable,
			@PathVariable Long candidateId, @PathVariable Long corporateId) {
		log.info("Getting List of jobs for that corporate {} shortlisted candidate {} for",corporateId,candidateId);
		return jobService.getShortlistedJobListForCorporateByCandidate(corporateId,candidateId);
	}

	
	/**
	 * GET /appliedCandiatesForJob : Applied candidates for Job
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/appliedCandiatesForJob/{jobId}/{fromScore}/{toScore}")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getAppliedCandidatesForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId, @PathVariable Double fromScore, @PathVariable Double toScore) {
		log.info("Getting applied candidates for job {}",jobId);
		final Page<CandidateProfileListDTO> page = jobService.getAppliedCandidatesForJob(pageable, jobId,fromScore, toScore);
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
		log.info("Request to get applied jobs by candidate {}", candidateId);
		final Page<CandidateJobDTO> page = jobService.getAppliedJobsListForCandidates(pageable, candidateId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appliedJobsByCandidate");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	
	/**
	 * GET /shortListedCandidatesForJob : Applied Jobs by Candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/shortListedCandidatesForJob/{jobId}")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getShortListedCandidatesByJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId) {
		final Page<CandidateProfileListDTO> page = jobService.getShortListedCandidatesForJob(pageable, jobId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shortListedCandidatesForJob");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /candidatesAppliedForAllJbsByCorporate : Applied Jobs by Candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/appliedCandidatesForJobsByCorporate/{corporateId}")
	@Timed
	public ResponseEntity<Long> getAppliedCandidatesForJobsByCorporate(@PathVariable Long corporateId) {
		Long numberOfCandidates = jobService.getAppliedCandidatesForAllJobsByCorporate(corporateId);
		return  ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, numberOfCandidates.toString()))
				.body(numberOfCandidates);
	}
	
	/**
	 * GET /totalJobsByCorporate : Applied Jobs by Candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
	 */
	@GetMapping("/totalJobsByCorporate/{corporateId}")
	@Timed
	public ResponseEntity<Long> getTotalJobsByCorporate(@PathVariable Long corporateId) {
		Long numberOfCandidates = jobService.getTotalJobsByCorporate(corporateId);
		return  ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, numberOfCandidates.toString()))
				.body(numberOfCandidates);
	}

	/**
	 * Apply for Job
	 */

	@GetMapping("/applyForJob/{jobId}/{loginId}")
	@Timed
	public ResponseEntity<Job> applyForJob(@PathVariable Long jobId, @PathVariable Long loginId)
			throws URISyntaxException {
		log.info("REST request to ApplyForJob {} by login {}",jobId,loginId);
		Job result = jobService.applyJobForCandidate(jobId, loginId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	
	
	@GetMapping("/totalJobsPostedLastMonthByCorporate/{corporateId}")
	@Timed
	public ResponseEntity <Long> jobsPostedLastMonth(@PathVariable Long corporateId)
			throws URISyntaxException {
		Long totalNumberOfJobsLAstMonth = jobService.getTotalJobsPostedSinceLastMonth(corporateId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, totalNumberOfJobsLAstMonth.toString()))
				.body(totalNumberOfJobsLAstMonth);
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
		log.info("REST request to get Job : {}", id);
		Set<JobFilter> jobFilters = null;
		Optional<Job> optionalJob = jobRepository.findById(id);
		Job job = optionalJob.get();
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

		return ResponseUtil.wrapOrNotFound(Optional.of(job));
	}
	
	/**
	 * GET /viewJobForCandidate/:id : get the "id" job.
	 *
	 * @param id
	 *            the id of the job to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the job, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/viewJobForCandidate/{jobId}/{candidateId}")
	@Timed
	public ResponseEntity<CandidateJobDTO> getJobForCandidate(@PathVariable Long jobId,@PathVariable Long candidateId) {
		log.info("REST request to get Job {} for Candidate View : {}", jobId,candidateId);
		if(candidateId <=0) {
			return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobService.getJobForGuest(jobId)));
		} else {
			return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobService.getJobForCandidateView(jobId,candidateId)));
		}
	}
	
	/**
	 * GET /jobs/:id : get the "id" job.
	 *
	 * @param id
	 *            the id of the job to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the job, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/jobForAddingCandidates/{id}")
	@Timed
	public ResponseEntity<JobEconomicsDTO> getJobForAddingCandidate(@PathVariable Long id) {
		log.info("REST request to get Job for Adding Candidates is : {}", id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobService.getJobForAddingCandidates(id)));
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
		log.info("REST request to delete Job : {}", id);
		jobRepository.deleteById(id);
		jobSearchRepository.deleteById(id);
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
		log.info("REST request to remove Job : {}", id);
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
		log.info("REST request to search Jobs for query {}", query);
		Page<Job> page = jobSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}
	

}
