package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.JobCategory;

import com.drishika.gradzcircle.repository.JobCategoryRepository;
import com.drishika.gradzcircle.repository.search.JobCategorySearchRepository;
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
 * REST controller for managing JobCategory.
 */
@RestController
@RequestMapping("/api")
public class JobCategoryResource {

    private final Logger log = LoggerFactory.getLogger(JobCategoryResource.class);

    private static final String ENTITY_NAME = "jobCategory";

    private final JobCategoryRepository jobCategoryRepository;

    private final JobCategorySearchRepository jobCategorySearchRepository;

    public JobCategoryResource(JobCategoryRepository jobCategoryRepository, JobCategorySearchRepository jobCategorySearchRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobCategorySearchRepository = jobCategorySearchRepository;
    }

    /**
     * POST  /job-categories : Create a new jobCategory.
     *
     * @param jobCategory the jobCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobCategory, or with status 400 (Bad Request) if the jobCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to save JobCategory : {}", jobCategory);
        if (jobCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobCategory cannot already have an ID")).body(null);
        }
        JobCategory result = jobCategoryRepository.save(jobCategory);
        jobCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/job-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-categories : Updates an existing jobCategory.
     *
     * @param jobCategory the jobCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobCategory,
     * or with status 400 (Bad Request) if the jobCategory is not valid,
     * or with status 500 (Internal Server Error) if the jobCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> updateJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to update JobCategory : {}", jobCategory);
        if (jobCategory.getId() == null) {
            return createJobCategory(jobCategory);
        }
        JobCategory result = jobCategoryRepository.save(jobCategory);
        jobCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-categories : get all the jobCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobCategories in body
     */
    @GetMapping("/job-categories")
    @Timed
    public List<JobCategory> getAllJobCategories() {
        log.debug("REST request to get all JobCategories");
        return jobCategoryRepository.findAll();
        }

    /**
     * GET  /job-categories/:id : get the "id" jobCategory.
     *
     * @param id the id of the jobCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobCategory, or with status 404 (Not Found)
     */
    @GetMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<JobCategory> getJobCategory(@PathVariable Long id) {
        log.debug("REST request to get JobCategory : {}", id);
        JobCategory jobCategory = jobCategoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobCategory));
    }

    /**
     * DELETE  /job-categories/:id : delete the "id" jobCategory.
     *
     * @param id the id of the jobCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Long id) {
        log.debug("REST request to delete JobCategory : {}", id);
        jobCategoryRepository.delete(id);
        jobCategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-categories?query=:query : search for the jobCategory corresponding
     * to the query.
     *
     * @param query the query of the jobCategory search
     * @return the result of the search
     */
    @GetMapping("/_search/job-categories")
    @Timed
    public List<JobCategory> searchJobCategories(@RequestParam String query) {
        log.debug("REST request to search JobCategories for query {}", query);
        return StreamSupport
            .stream(jobCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
