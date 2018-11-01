package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Audit;

import com.drishika.gradzcircle.repository.AuditRepository;
import com.drishika.gradzcircle.repository.search.AuditSearchRepository;
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
 * REST controller for managing Audit.
 */
@RestController
@RequestMapping("/api")
public class AuditResource {

    private final Logger log = LoggerFactory.getLogger(AuditResource.class);

    private static final String ENTITY_NAME = "audit";

    private final AuditRepository auditRepository;

    private final AuditSearchRepository auditSearchRepository;

    public AuditResource(AuditRepository auditRepository, AuditSearchRepository auditSearchRepository) {
        this.auditRepository = auditRepository;
        this.auditSearchRepository = auditSearchRepository;
    }

    /**
     * POST  /audits : Create a new audit.
     *
     * @param audit the audit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new audit, or with status 400 (Bad Request) if the audit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/audits")
    @Timed
    public ResponseEntity<Audit> createAudit(@RequestBody Audit audit) throws URISyntaxException {
        log.debug("REST request to save Audit : {}", audit);
        if (audit.getId() != null) {
            throw new BadRequestAlertException("A new audit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Audit result = auditRepository.save(audit);
        auditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /audits : Updates an existing audit.
     *
     * @param audit the audit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated audit,
     * or with status 400 (Bad Request) if the audit is not valid,
     * or with status 500 (Internal Server Error) if the audit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/audits")
    @Timed
    public ResponseEntity<Audit> updateAudit(@RequestBody Audit audit) throws URISyntaxException {
        log.debug("REST request to update Audit : {}", audit);
        if (audit.getId() == null) {
            return createAudit(audit);
        }
        Audit result = auditRepository.save(audit);
        auditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, audit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /audits : get all the audits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of audits in body
     */
    @GetMapping("/audits")
    @Timed
    public List<Audit> getAllAudits() {
        log.debug("REST request to get all Audits");
        return auditRepository.findAll();
        }

    /**
     * GET  /audits/:id : get the "id" audit.
     *
     * @param id the id of the audit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the audit, or with status 404 (Not Found)
     */
    @GetMapping("/audits/{id}")
    @Timed
    public ResponseEntity<Audit> getAudit(@PathVariable Long id) {
        log.debug("REST request to get Audit : {}", id);
        Audit audit = auditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(audit));
    }

    /**
     * DELETE  /audits/:id : delete the "id" audit.
     *
     * @param id the id of the audit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteAudit(@PathVariable Long id) {
        log.debug("REST request to delete Audit : {}", id);
        auditRepository.delete(id);
        auditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/audits?query=:query : search for the audit corresponding
     * to the query.
     *
     * @param query the query of the audit search
     * @return the result of the search
     */
    @GetMapping("/_search/audits")
    @Timed
    public List<Audit> searchAudits(@RequestParam String query) {
        log.debug("REST request to search Audits for query {}", query);
        return StreamSupport
            .stream(auditSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
