package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.JobHistory;

import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.search.JobHistorySearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobHistory.
 */
@RestController
@RequestMapping("/api")
public class JobHistoryResource {

	private final Logger log = LoggerFactory.getLogger(JobHistoryResource.class);

	private static final String ENTITY_NAME = "jobHistory";

	private final JobHistoryRepository jobHistoryRepository;

	private final JobHistorySearchRepository jobHistorySearchRepository;

	public JobHistoryResource(JobHistoryRepository jobHistoryRepository,
			JobHistorySearchRepository jobHistorySearchRepository) {
		this.jobHistoryRepository = jobHistoryRepository;
		this.jobHistorySearchRepository = jobHistorySearchRepository;
	}

	/**
	 * POST /job-histories : Create a new jobHistory.
	 *
	 * @param jobHistory
	 *            the jobHistory to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         jobHistory, or with status 400 (Bad Request) if the jobHistory has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/job-histories")
	@Timed
	public ResponseEntity<JobHistory> createJobHistory(@Valid @RequestBody JobHistory jobHistory)
			throws URISyntaxException {
		log.debug("REST request to save JobHistory : {}", jobHistory);
		if (jobHistory.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new jobHistory cannot already have an ID")).body(null);
		}
		JobHistory result = jobHistoryRepository.save(jobHistory);
		jobHistorySearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/job-histories/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /job-histories : Updates an existing jobHistory.
	 *
	 * @param jobHistory
	 *            the jobHistory to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         jobHistory, or with status 400 (Bad Request) if the jobHistory is not
	 *         valid, or with status 500 (Internal Server Error) if the jobHistory
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/job-histories")
	@Timed
	public ResponseEntity<JobHistory> updateJobHistory(@Valid @RequestBody JobHistory jobHistory)
			throws URISyntaxException {
		log.debug("REST request to update JobHistory : {}", jobHistory);
		if (jobHistory.getId() == null) {
			return createJobHistory(jobHistory);
		}
		JobHistory result = jobHistoryRepository.save(jobHistory);
		jobHistorySearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobHistory.getId().toString())).body(result);
	}

	/**
	 * GET /job-histories : get all the jobHistories.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of jobHistories
	 *         in body
	 */
	@GetMapping("/job-histories")
	@Timed
	public List<JobHistory> getAllJobHistories() {
		log.debug("REST request to get all JobHistories");
		return jobHistoryRepository.findAll();
	}

	/**
	 * GET /job-histories/:id : get the "id" jobHistory.
	 *
	 * @param id
	 *            the id of the jobHistory to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the jobHistory,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/job-histories/{id}")
	@Timed
	public ResponseEntity<JobHistory> getJobHistory(@PathVariable Long id) {
		log.debug("REST request to get JobHistory : {}", id);
		JobHistory jobHistory = jobHistoryRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobHistory));
	}

	/**
	 * DELETE /job-histories/:id : delete the "id" jobHistory.
	 *
	 * @param id
	 *            the id of the jobHistory to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/job-histories/{id}")
	@Timed
	public ResponseEntity<Void> deleteJobHistory(@PathVariable Long id) {
		log.debug("REST request to delete JobHistory : {}", id);
		jobHistoryRepository.delete(id);
		jobHistorySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/job-histories?query=:query : search for the jobHistory
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the jobHistory search
	 * @return the result of the search
	 */
	@GetMapping("/_search/job-histories")
	@Timed
	public List<JobHistory> searchJobHistories(@RequestParam String query) {
		log.debug("REST request to search JobHistories for query {}", query);
		return StreamSupport.stream(jobHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
