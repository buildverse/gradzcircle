package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.JobType;

import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.search.JobTypeSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobType.
 */
@RestController
@RequestMapping("/api")
public class JobTypeResource {

    private final Logger log = LoggerFactory.getLogger(JobTypeResource.class);

    private static final String ENTITY_NAME = "jobType";

    private final JobTypeRepository jobTypeRepository;

    private final JobTypeSearchRepository jobTypeSearchRepository;

    public JobTypeResource(JobTypeRepository jobTypeRepository, JobTypeSearchRepository jobTypeSearchRepository) {
        this.jobTypeRepository = jobTypeRepository;
        this.jobTypeSearchRepository = jobTypeSearchRepository;
    }

    /**
     * POST  /job-types : Create a new jobType.
     *
     * @param jobType the jobType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobType, or with status 400 (Bad Request) if the jobType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-types")
    @Timed
    public ResponseEntity<JobType> createJobType(@RequestBody JobType jobType) throws URISyntaxException {
        log.debug("REST request to save JobType : {}", jobType);
        if (jobType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobType cannot already have an ID")).body(null);
        }
        JobType result = jobTypeRepository.save(jobType);
        jobTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-types : Updates an existing jobType.
     *
     * @param jobType the jobType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobType,
     * or with status 400 (Bad Request) if the jobType is not valid,
     * or with status 500 (Internal Server Error) if the jobType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-types")
    @Timed
    public ResponseEntity<JobType> updateJobType(@RequestBody JobType jobType) throws URISyntaxException {
        log.debug("REST request to update JobType : {}", jobType);
        if (jobType.getId() == null) {
            return createJobType(jobType);
        }
        JobType result = jobTypeRepository.save(jobType);
        jobTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-types : get all the jobTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobTypes in body
     */
    @GetMapping("/job-types")
    @Timed
    public List<JobType> getAllJobTypes() {
        log.debug("REST request to get all JobTypes");
        return jobTypeRepository.findAll();
        }

    /**
     * GET  /job-types/:id : get the "id" jobType.
     *
     * @param id the id of the jobType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobType, or with status 404 (Not Found)
     */
    @GetMapping("/job-types/{id}")
    @Timed
    public ResponseEntity<JobType> getJobType(@PathVariable Long id) {
        log.debug("REST request to get JobType : {}", id);
        JobType jobType = jobTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobType));
    }

    /**
     * DELETE  /job-types/:id : delete the "id" jobType.
     *
     * @param id the id of the jobType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobType(@PathVariable Long id) {
        log.debug("REST request to delete JobType : {}", id);
        jobTypeRepository.delete(id);
        jobTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-types?query=:query : search for the jobType corresponding
     * to the query.
     *
     * @param query the query of the jobType search
     * @return the result of the search
     */
    @GetMapping("/_search/job-types")
    @Timed
    public List<JobType> searchJobTypes(@RequestParam String query) {
        log.debug("REST request to search JobTypes for query {}", query);
        return StreamSupport
            .stream(jobTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
