package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.JobFilter;

import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.search.JobFilterSearchRepository;
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
 * REST controller for managing JobFilter.
 */
@RestController
@RequestMapping("/api")
public class JobFilterResource {

    private final Logger log = LoggerFactory.getLogger(JobFilterResource.class);

    private static final String ENTITY_NAME = "jobFilter";

    private final JobFilterRepository jobFilterRepository;

    private final JobFilterSearchRepository jobFilterSearchRepository;

    public JobFilterResource(JobFilterRepository jobFilterRepository, JobFilterSearchRepository jobFilterSearchRepository) {
        this.jobFilterRepository = jobFilterRepository;
        this.jobFilterSearchRepository = jobFilterSearchRepository;
    }

    /**
     * POST  /job-filters : Create a new jobFilter.
     *
     * @param jobFilter the jobFilter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobFilter, or with status 400 (Bad Request) if the jobFilter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-filters")
    @Timed
    public ResponseEntity<JobFilter> createJobFilter(@Valid @RequestBody JobFilter jobFilter) throws URISyntaxException {
        log.debug("REST request to save JobFilter : {}", jobFilter);
        if (jobFilter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobFilter cannot already have an ID")).body(null);
        }
        JobFilter result = jobFilterRepository.save(jobFilter);
        jobFilterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-filters : Updates an existing jobFilter.
     *
     * @param jobFilter the jobFilter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobFilter,
     * or with status 400 (Bad Request) if the jobFilter is not valid,
     * or with status 500 (Internal Server Error) if the jobFilter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-filters")
    @Timed
    public ResponseEntity<JobFilter> updateJobFilter(@Valid @RequestBody JobFilter jobFilter) throws URISyntaxException {
        log.debug("REST request to update JobFilter : {}", jobFilter);
        if (jobFilter.getId() == null) {
            return createJobFilter(jobFilter);
        }
        JobFilter result = jobFilterRepository.save(jobFilter);
        jobFilterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobFilter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-filters : get all the jobFilters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobFilters in body
     */
    @GetMapping("/job-filters")
    @Timed
    public List<JobFilter> getAllJobFilters() {
        log.debug("REST request to get all JobFilters");
        return jobFilterRepository.findAll();
        }

    /**
     * GET  /job-filters/:id : get the "id" jobFilter.
     *
     * @param id the id of the jobFilter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobFilter, or with status 404 (Not Found)
     */
    @GetMapping("/job-filters/{id}")
    @Timed
    public ResponseEntity<JobFilter> getJobFilter(@PathVariable Long id) {
        log.debug("REST request to get JobFilter : {}", id);
        JobFilter jobFilter = jobFilterRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobFilter));
    }

    /**
     * DELETE  /job-filters/:id : delete the "id" jobFilter.
     *
     * @param id the id of the jobFilter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-filters/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobFilter(@PathVariable Long id) {
        log.debug("REST request to delete JobFilter : {}", id);
        jobFilterRepository.delete(id);
        jobFilterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-filters?query=:query : search for the jobFilter corresponding
     * to the query.
     *
     * @param query the query of the jobFilter search
     * @return the result of the search
     */
    @GetMapping("/_search/job-filters")
    @Timed
    public List<JobFilter> searchJobFilters(@RequestParam String query) {
        log.debug("REST request to search JobFilters for query {}", query);
        return StreamSupport
            .stream(jobFilterSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
