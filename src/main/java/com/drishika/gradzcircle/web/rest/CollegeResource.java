package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.College;

import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.search.CollegeSearchRepository;
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
 * REST controller for managing College.
 */
@RestController
@RequestMapping("/api")
public class CollegeResource {

    private final Logger log = LoggerFactory.getLogger(CollegeResource.class);

    private static final String ENTITY_NAME = "college";

    private final CollegeRepository collegeRepository;

    private final CollegeSearchRepository collegeSearchRepository;

    public CollegeResource(CollegeRepository collegeRepository, CollegeSearchRepository collegeSearchRepository) {
        this.collegeRepository = collegeRepository;
        this.collegeSearchRepository = collegeSearchRepository;
    }

    /**
     * POST  /colleges : Create a new college.
     *
     * @param college the college to create
     * @return the ResponseEntity with status 201 (Created) and with body the new college, or with status 400 (Bad Request) if the college has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/colleges")
    @Timed
    public ResponseEntity<College> createCollege(@RequestBody College college) throws URISyntaxException {
        log.debug("REST request to save College : {}", college);
        if (college.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new college cannot already have an ID")).body(null);
        }
        College result = collegeRepository.save(college);
        collegeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/colleges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /colleges : Updates an existing college.
     *
     * @param college the college to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated college,
     * or with status 400 (Bad Request) if the college is not valid,
     * or with status 500 (Internal Server Error) if the college couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/colleges")
    @Timed
    public ResponseEntity<College> updateCollege(@RequestBody College college) throws URISyntaxException {
        log.debug("REST request to update College : {}", college);
        if (college.getId() == null) {
            return createCollege(college);
        }
        College result = collegeRepository.save(college);
        collegeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, college.getId().toString()))
            .body(result);
    }

    /**
     * GET  /colleges : get all the colleges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
     */
    @GetMapping("/colleges")
    @Timed
    public List<College> getAllColleges() {
        log.debug("REST request to get all Colleges");
        return collegeRepository.findAll();
        }

    /**
     * GET  /colleges/:id : get the "id" college.
     *
     * @param id the id of the college to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the college, or with status 404 (Not Found)
     */
    @GetMapping("/colleges/{id}")
    @Timed
    public ResponseEntity<College> getCollege(@PathVariable Long id) {
        log.debug("REST request to get College : {}", id);
        College college = collegeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(college));
    }

    /**
     * DELETE  /colleges/:id : delete the "id" college.
     *
     * @param id the id of the college to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/colleges/{id}")
    @Timed
    public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
        log.debug("REST request to delete College : {}", id);
        collegeRepository.delete(id);
        collegeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/colleges?query=:query : search for the college corresponding
     * to the query.
     *
     * @param query the query of the college search
     * @return the result of the search
     */
    @GetMapping("/_search/colleges")
    @Timed
    public List<College> searchColleges(@RequestParam String query) {
        log.debug("REST request to search Colleges for query {}", query);
        return StreamSupport
            .stream(collegeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
