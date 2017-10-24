package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Corporate;

import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
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
 * REST controller for managing Corporate.
 */
@RestController
@RequestMapping("/api")
public class CorporateResource {

    private final Logger log = LoggerFactory.getLogger(CorporateResource.class);

    private static final String ENTITY_NAME = "corporate";

    private final CorporateRepository corporateRepository;

    private final CorporateSearchRepository corporateSearchRepository;

    public CorporateResource(CorporateRepository corporateRepository, CorporateSearchRepository corporateSearchRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateSearchRepository = corporateSearchRepository;
    }

    /**
     * POST  /corporates : Create a new corporate.
     *
     * @param corporate the corporate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corporate, or with status 400 (Bad Request) if the corporate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/corporates")
    @Timed
    public ResponseEntity<Corporate> createCorporate(@RequestBody Corporate corporate) throws URISyntaxException {
        log.debug("REST request to save Corporate : {}", corporate);
        if (corporate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new corporate cannot already have an ID")).body(null);
        }
        Corporate result = corporateRepository.save(corporate);
        corporateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/corporates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /corporates : Updates an existing corporate.
     *
     * @param corporate the corporate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corporate,
     * or with status 400 (Bad Request) if the corporate is not valid,
     * or with status 500 (Internal Server Error) if the corporate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/corporates")
    @Timed
    public ResponseEntity<Corporate> updateCorporate(@RequestBody Corporate corporate) throws URISyntaxException {
        log.debug("REST request to update Corporate : {}", corporate);
        if (corporate.getId() == null) {
            return createCorporate(corporate);
        }
        Corporate result = corporateRepository.save(corporate);
        corporateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, corporate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /corporates : get all the corporates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of corporates in body
     */
    @GetMapping("/corporates")
    @Timed
    public List<Corporate> getAllCorporates() {
        log.debug("REST request to get all Corporates");
        return corporateRepository.findAll();
        }

    /**
     * GET  /corporates/:id : get the "id" corporate.
     *
     * @param id the id of the corporate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corporate, or with status 404 (Not Found)
     */
    @GetMapping("/corporates/{id}")
    @Timed
    public ResponseEntity<Corporate> getCorporate(@PathVariable Long id) {
        log.debug("REST request to get Corporate : {}", id);
        Corporate corporate = corporateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(corporate));
    }

     /**
     * GET  /corporates/:id : get the "id" Login.
     *
     * @param id the id of the corporate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corporate, or with status 404 (Not Found)
     */
    @GetMapping("/corporateByLoginId/{id}")
    @Timed
    public ResponseEntity<Corporate> getCorporateByLoginID(@PathVariable Long id) {
        log.debug("REST request to get Corporate : {}", id);
        Corporate corporate = corporateRepository.findByLoginId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(corporate));
    }

    /**
     * DELETE  /corporates/:id : delete the "id" corporate.
     *
     * @param id the id of the corporate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/corporates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorporate(@PathVariable Long id) {
        log.debug("REST request to delete Corporate : {}", id);
        corporateRepository.delete(id);
        corporateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/corporates?query=:query : search for the corporate corresponding
     * to the query.
     *
     * @param query the query of the corporate search
     * @return the result of the search
     */
    @GetMapping("/_search/corporates")
    @Timed
    public List<Corporate> searchCorporates(@RequestParam String query) {
        log.debug("REST request to search Corporates for query {}", query);
        return StreamSupport
            .stream(corporateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
