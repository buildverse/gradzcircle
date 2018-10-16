/**
 * 
 */
package com.drishika.gradzcircle.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.repository.CandidateJobRepository;
import com.drishika.gradzcircle.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;;

/**
 * @author abhinav REST controller for managing CandidateResource.
 */
@RestController
@RequestMapping("/api")
public class CandidateJobResource {

	private final Logger log = LoggerFactory.getLogger(CandidateJobResource.class);

	private static final String ENTITY_NAME = "candidateJob";

	private final CandidateJobRepository candidateJobRepository;

	// private final CandidateJobSearchRepository candidateJobSearchRepository;

	public CandidateJobResource(CandidateJobRepository candidateJobRepository) {
		this.candidateJobRepository = candidateJobRepository;
		// this.candidateJobSearchRepository = candidateJobSearchRepository;
	}

	/**
	 * GET /candidate-jobs : get all the candidateJobs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/candidate-jobs")
	@Timed
	public ResponseEntity<List<CandidateJob>> getAllCandidateJobs(@ApiParam Pageable pageable) {
		log.debug("REST request to get all CandidateJobs");
		Page<CandidateJob> candidateJobPage = candidateJobRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(candidateJobPage, "/api/candidate-jobs");
		return new ResponseEntity<>(candidateJobPage.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /candidate-jobs : get all the candidateJobs by candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/candidateJobsByCandidate/{candidateId}")
	@Timed
	public ResponseEntity<List<CandidateJob>> getCandidateJobsForCandidate(@ApiParam Pageable pageable,
			@PathVariable Long candidateId) {
		log.debug("REST request to get CandidateJobs for Candidate");
		Page<CandidateJob> candidateJobPage = candidateJobRepository.findByCandidateId(candidateId, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(candidateJobPage,
				"/api/candidateJobsByCandidate");
		return new ResponseEntity<>(candidateJobPage.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /candidate-jobs : get all the candidateJobs by Job
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/candidateJobsByJob/{jobId}")
	@Timed
	public ResponseEntity<List<CandidateJob>> getCandidateJobsForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId) {
		log.debug("REST request to get CandidateJobs for Corporate");
		Page<CandidateJob> candidateJobPage = candidateJobRepository.findByJobId(jobId, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(candidateJobPage, "/api/candidateJobsByJob");
		return new ResponseEntity<>(candidateJobPage.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /candidate-jobs : get all the candidateJobs for reviewed candidate
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/reviewedCandidatesByJob/{jobId}")
	@Timed
	public ResponseEntity<List<CandidateJob>> getReviewedCandidateForJob(@ApiParam Pageable pageable,
			@PathVariable Long jobId) {
		log.debug("REST request to get CandidateJobs for Corporate");
		Page<CandidateJob> candidateJobPage = candidateJobRepository.findCandidateReviewedForJob(jobId, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(candidateJobPage,
				"/api/reviewedCandidatesByJob");
		return new ResponseEntity<>(candidateJobPage.getContent(), headers, HttpStatus.OK);
	}

}
