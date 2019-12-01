package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CaptureCollege;

import com.drishika.gradzcircle.repository.CaptureCollegeRepository;
import com.drishika.gradzcircle.repository.search.CaptureCollegeSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing CaptureCollege.
 */
@RestController
@RequestMapping("/api")
public class CaptureCollegeResource {

    private final Logger log = LoggerFactory.getLogger(CaptureCollegeResource.class);

    private static final String ENTITY_NAME = "captureCollege";

    private final CaptureCollegeRepository captureCollegeRepository;

    private final CaptureCollegeSearchRepository captureCollegeSearchRepository;

    public CaptureCollegeResource(CaptureCollegeRepository captureCollegeRepository, CaptureCollegeSearchRepository captureCollegeSearchRepository) {
        this.captureCollegeRepository = captureCollegeRepository;
        this.captureCollegeSearchRepository = captureCollegeSearchRepository;
    }

    /**
     * POST  /capture-colleges : Create a new captureCollege.
     *
     * @param captureCollege the captureCollege to create
     * @return the ResponseEntity with status 201 (Created) and with body the new captureCollege, or with status 400 (Bad Request) if the captureCollege has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/capture-colleges")
    @Timed
    public ResponseEntity<CaptureCollege> createCaptureCollege(@RequestBody CaptureCollege captureCollege) throws URISyntaxException {
        log.debug("REST request to save CaptureCollege : {}", captureCollege);
        if (captureCollege.getId() != null) {
            throw new BadRequestAlertException("A new captureCollege cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CaptureCollege result = captureCollegeRepository.save(captureCollege);
        captureCollegeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/capture-colleges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /capture-colleges : Updates an existing captureCollege.
     *
     * @param captureCollege the captureCollege to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated captureCollege,
     * or with status 400 (Bad Request) if the captureCollege is not valid,
     * or with status 500 (Internal Server Error) if the captureCollege couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/capture-colleges")
    @Timed
    public ResponseEntity<CaptureCollege> updateCaptureCollege(@RequestBody CaptureCollege captureCollege) throws URISyntaxException {
        log.debug("REST request to update CaptureCollege : {}", captureCollege);
        if (captureCollege.getId() == null) {
        		throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CaptureCollege result = captureCollegeRepository.save(captureCollege);
        captureCollegeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, captureCollege.getId().toString()))
            .body(result);
    }

    /**
     * GET  /capture-colleges : get all the captureColleges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of captureColleges in body
     */
    @GetMapping("/capture-colleges")
    @Timed
    public List<CaptureCollege> getAllCaptureColleges() {
        log.debug("REST request to get all CaptureColleges");
        return captureCollegeRepository.findAll();
        }

    /**
     * GET  /capture-colleges/:id : get the "id" captureCollege.
     *
     * @param id the id of the captureCollege to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the captureCollege, or with status 404 (Not Found)
     */
    @GetMapping("/capture-colleges/{id}")
    @Timed
    public ResponseEntity<CaptureCollege> getCaptureCollege(@PathVariable Long id) {
        log.debug("REST request to get CaptureCollege : {}", id);
        Optional<CaptureCollege> captureCollege = captureCollegeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(captureCollege);
    }

    /**
     * DELETE  /capture-colleges/:id : delete the "id" captureCollege.
     *
     * @param id the id of the captureCollege to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/capture-colleges/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaptureCollege(@PathVariable Long id) {
        log.debug("REST request to delete CaptureCollege : {}", id);
        captureCollegeRepository.deleteById(id);
        captureCollegeSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/capture-colleges?query=:query : search for the captureCollege corresponding
     * to the query.
     *
     * @param query the query of the captureCollege search
     * @return the result of the search
     */
    @GetMapping("/_search/capture-colleges")
    @Timed
    public List<CaptureCollege> searchCaptureColleges(@RequestParam String query) {
        log.debug("REST request to search CaptureColleges for query {}", query);
        return StreamSupport
            .stream(captureCollegeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
