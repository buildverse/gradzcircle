package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CaptureQualification;

import com.drishika.gradzcircle.repository.CaptureQualificationRepository;
import com.drishika.gradzcircle.repository.search.CaptureQualificationSearchRepository;
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
 * REST controller for managing CaptureQualification.
 */
@RestController
@RequestMapping("/api")
public class CaptureQualificationResource {

    private final Logger log = LoggerFactory.getLogger(CaptureQualificationResource.class);

    private static final String ENTITY_NAME = "captureQualification";

    private final CaptureQualificationRepository captureQualificationRepository;

    private final CaptureQualificationSearchRepository captureQualificationSearchRepository;

    public CaptureQualificationResource(CaptureQualificationRepository captureQualificationRepository, CaptureQualificationSearchRepository captureQualificationSearchRepository) {
        this.captureQualificationRepository = captureQualificationRepository;
        this.captureQualificationSearchRepository = captureQualificationSearchRepository;
    }

    /**
     * POST  /capture-qualifications : Create a new captureQualification.
     *
     * @param captureQualification the captureQualification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new captureQualification, or with status 400 (Bad Request) if the captureQualification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/capture-qualifications")
    @Timed
    public ResponseEntity<CaptureQualification> createCaptureQualification(@RequestBody CaptureQualification captureQualification) throws URISyntaxException {
        log.debug("REST request to save CaptureQualification : {}", captureQualification);
        if (captureQualification.getId() != null) {
            throw new BadRequestAlertException("A new captureQualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CaptureQualification result = captureQualificationRepository.save(captureQualification);
        captureQualificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/capture-qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /capture-qualifications : Updates an existing captureQualification.
     *
     * @param captureQualification the captureQualification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated captureQualification,
     * or with status 400 (Bad Request) if the captureQualification is not valid,
     * or with status 500 (Internal Server Error) if the captureQualification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/capture-qualifications")
    @Timed
    public ResponseEntity<CaptureQualification> updateCaptureQualification(@RequestBody CaptureQualification captureQualification) throws URISyntaxException {
        log.debug("REST request to update CaptureQualification : {}", captureQualification);
        if (captureQualification.getId() == null) {
        		throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CaptureQualification result = captureQualificationRepository.save(captureQualification);
        captureQualificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, captureQualification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /capture-qualifications : get all the captureQualifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of captureQualifications in body
     */
    @GetMapping("/capture-qualifications")
    @Timed
    public List<CaptureQualification> getAllCaptureQualifications() {
        log.debug("REST request to get all CaptureQualifications");
        return captureQualificationRepository.findAll();
        }

    /**
     * GET  /capture-qualifications/:id : get the "id" captureQualification.
     *
     * @param id the id of the captureQualification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the captureQualification, or with status 404 (Not Found)
     */
    @GetMapping("/capture-qualifications/{id}")
    @Timed
    public ResponseEntity<CaptureQualification> getCaptureQualification(@PathVariable Long id) {
        log.debug("REST request to get CaptureQualification : {}", id);
        Optional<CaptureQualification> captureQualification = captureQualificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(captureQualification);
    }

    /**
     * DELETE  /capture-qualifications/:id : delete the "id" captureQualification.
     *
     * @param id the id of the captureQualification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/capture-qualifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaptureQualification(@PathVariable Long id) {
        log.debug("REST request to delete CaptureQualification : {}", id);
        captureQualificationRepository.deleteById(id);
        captureQualificationSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/capture-qualifications?query=:query : search for the captureQualification corresponding
     * to the query.
     *
     * @param query the query of the captureQualification search
     * @return the result of the search
     */
    @GetMapping("/_search/capture-qualifications")
    @Timed
    public List<CaptureQualification> searchCaptureQualifications(@RequestParam String query) {
        log.debug("REST request to search CaptureQualifications for query {}", query);
        return StreamSupport
            .stream(captureQualificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
