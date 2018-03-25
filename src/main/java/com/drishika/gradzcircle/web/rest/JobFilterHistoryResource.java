package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.JobFilterHistory;

import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.search.JobFilterHistorySearchRepository;
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
 * REST controller for managing JobFilterHistory.
 */
@RestController
@RequestMapping("/api")
public class JobFilterHistoryResource {

    private final Logger log = LoggerFactory.getLogger(JobFilterHistoryResource.class);

    private static final String ENTITY_NAME = "jobFilterHistory";

    private final JobFilterHistoryRepository jobFilterHistoryRepository;

    private final JobFilterHistorySearchRepository jobFilterHistorySearchRepository;

    public JobFilterHistoryResource(JobFilterHistoryRepository jobFilterHistoryRepository, JobFilterHistorySearchRepository jobFilterHistorySearchRepository) {
        this.jobFilterHistoryRepository = jobFilterHistoryRepository;
        this.jobFilterHistorySearchRepository = jobFilterHistorySearchRepository;
    }

    /**
     * POST  /job-filter-histories : Create a new jobFilterHistory.
     *
     * @param jobFilterHistory the jobFilterHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobFilterHistory, or with status 400 (Bad Request) if the jobFilterHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-filter-histories")
    @Timed
    public ResponseEntity<JobFilterHistory> createJobFilterHistory(@Valid @RequestBody JobFilterHistory jobFilterHistory) throws URISyntaxException {
        log.debug("REST request to save JobFilterHistory : {}", jobFilterHistory);
        if (jobFilterHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobFilterHistory cannot already have an ID")).body(null);
        }
        JobFilterHistory result = jobFilterHistoryRepository.save(jobFilterHistory);
        jobFilterHistorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-filter-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-filter-histories : Updates an existing jobFilterHistory.
     *
     * @param jobFilterHistory the jobFilterHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobFilterHistory,
     * or with status 400 (Bad Request) if the jobFilterHistory is not valid,
     * or with status 500 (Internal Server Error) if the jobFilterHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-filter-histories")
    @Timed
    public ResponseEntity<JobFilterHistory> updateJobFilterHistory(@Valid @RequestBody JobFilterHistory jobFilterHistory) throws URISyntaxException {
        log.debug("REST request to update JobFilterHistory : {}", jobFilterHistory);
        if (jobFilterHistory.getId() == null) {
            return createJobFilterHistory(jobFilterHistory);
        }
        JobFilterHistory result = jobFilterHistoryRepository.save(jobFilterHistory);
        jobFilterHistorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobFilterHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-filter-histories : get all the jobFilterHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobFilterHistories in body
     */
    @GetMapping("/job-filter-histories")
    @Timed
    public List<JobFilterHistory> getAllJobFilterHistories() {
        log.debug("REST request to get all JobFilterHistories");
        return jobFilterHistoryRepository.findAll();
        }

    /**
     * GET  /job-filter-histories/:id : get the "id" jobFilterHistory.
     *
     * @param id the id of the jobFilterHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobFilterHistory, or with status 404 (Not Found)
     */
    @GetMapping("/job-filter-histories/{id}")
    @Timed
    public ResponseEntity<JobFilterHistory> getJobFilterHistory(@PathVariable Long id) {
        log.debug("REST request to get JobFilterHistory : {}", id);
        JobFilterHistory jobFilterHistory = jobFilterHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobFilterHistory));
    }

    /**
     * DELETE  /job-filter-histories/:id : delete the "id" jobFilterHistory.
     *
     * @param id the id of the jobFilterHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-filter-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobFilterHistory(@PathVariable Long id) {
        log.debug("REST request to delete JobFilterHistory : {}", id);
        jobFilterHistoryRepository.delete(id);
        jobFilterHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-filter-histories?query=:query : search for the jobFilterHistory corresponding
     * to the query.
     *
     * @param query the query of the jobFilterHistory search
     * @return the result of the search
     */
    @GetMapping("/_search/job-filter-histories")
    @Timed
    public List<JobFilterHistory> searchJobFilterHistories(@RequestParam String query) {
        log.debug("REST request to search JobFilterHistories for query {}", query);
        return StreamSupport
            .stream(jobFilterHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
