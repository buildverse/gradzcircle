package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Qualification;

import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
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
 * REST controller for managing Qualification.
 */
@RestController
@RequestMapping("/api")
public class QualificationResource {

    private final Logger log = LoggerFactory.getLogger(QualificationResource.class);

    private static final String ENTITY_NAME = "qualification";

    private final QualificationRepository qualificationRepository;

    private final QualificationSearchRepository qualificationSearchRepository;

    public QualificationResource(QualificationRepository qualificationRepository, QualificationSearchRepository qualificationSearchRepository) {
        this.qualificationRepository = qualificationRepository;
        this.qualificationSearchRepository = qualificationSearchRepository;
    }

    /**
     * POST  /qualifications : Create a new qualification.
     *
     * @param qualification the qualification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new qualification, or with status 400 (Bad Request) if the qualification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/qualifications")
    @Timed
    public ResponseEntity<Qualification> createQualification(@RequestBody Qualification qualification) throws URISyntaxException {
        log.debug("REST request to save Qualification : {}", qualification);
        if (qualification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qualification cannot already have an ID")).body(null);
        }
        Qualification result = qualificationRepository.save(qualification);
        qualificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /qualifications : Updates an existing qualification.
     *
     * @param qualification the qualification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated qualification,
     * or with status 400 (Bad Request) if the qualification is not valid,
     * or with status 500 (Internal Server Error) if the qualification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/qualifications")
    @Timed
    public ResponseEntity<Qualification> updateQualification(@RequestBody Qualification qualification) throws URISyntaxException {
        log.debug("REST request to update Qualification : {}", qualification);
        if (qualification.getId() == null) {
            return createQualification(qualification);
        }
        Qualification result = qualificationRepository.save(qualification);
        qualificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /qualifications : get all the qualifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of qualifications in body
     */
    @GetMapping("/qualifications")
    @Timed
    public List<Qualification> getAllQualifications() {
        log.debug("REST request to get all Qualifications");
        return qualificationRepository.findAll();
        }

    /**
     * GET  /qualifications/:id : get the "id" qualification.
     *
     * @param id the id of the qualification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the qualification, or with status 404 (Not Found)
     */
    @GetMapping("/qualifications/{id}")
    @Timed
    public ResponseEntity<Qualification> getQualification(@PathVariable Long id) {
        log.debug("REST request to get Qualification : {}", id);
        Qualification qualification = qualificationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualification));
    }

    /**
     * DELETE  /qualifications/:id : delete the "id" qualification.
     *
     * @param id the id of the qualification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/qualifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
        log.debug("REST request to delete Qualification : {}", id);
        qualificationRepository.delete(id);
        qualificationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/qualifications?query=:query : search for the qualification corresponding
     * to the query.
     *
     * @param query the query of the qualification search
     * @return the result of the search
     */
    @GetMapping("/_search/qualifications")
    @Timed
    public List<Qualification> searchQualifications(@RequestParam String query) {
        log.debug("REST request to search Qualifications for query {}", query);
        return StreamSupport
            .stream(qualificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
