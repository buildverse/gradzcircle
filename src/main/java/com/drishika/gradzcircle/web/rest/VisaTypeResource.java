package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.VisaType;

import com.drishika.gradzcircle.repository.VisaTypeRepository;
import com.drishika.gradzcircle.repository.search.VisaTypeSearchRepository;
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
 * REST controller for managing VisaType.
 */
@RestController
@RequestMapping("/api")
public class VisaTypeResource {

    private final Logger log = LoggerFactory.getLogger(VisaTypeResource.class);

    private static final String ENTITY_NAME = "visaType";

    private final VisaTypeRepository visaTypeRepository;

    private final VisaTypeSearchRepository visaTypeSearchRepository;

    public VisaTypeResource(VisaTypeRepository visaTypeRepository, VisaTypeSearchRepository visaTypeSearchRepository) {
        this.visaTypeRepository = visaTypeRepository;
        this.visaTypeSearchRepository = visaTypeSearchRepository;
    }

    /**
     * POST  /visa-types : Create a new visaType.
     *
     * @param visaType the visaType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visaType, or with status 400 (Bad Request) if the visaType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visa-types")
    @Timed
    public ResponseEntity<VisaType> createVisaType(@RequestBody VisaType visaType) throws URISyntaxException {
        log.debug("REST request to save VisaType : {}", visaType);
        if (visaType.getId() != null) {
            throw new BadRequestAlertException("A new visaType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisaType result = visaTypeRepository.save(visaType);
        visaTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/visa-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /visa-types : Updates an existing visaType.
     *
     * @param visaType the visaType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visaType,
     * or with status 400 (Bad Request) if the visaType is not valid,
     * or with status 500 (Internal Server Error) if the visaType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visa-types")
    @Timed
    public ResponseEntity<VisaType> updateVisaType(@RequestBody VisaType visaType) throws URISyntaxException {
        log.debug("REST request to update VisaType : {}", visaType);
        if (visaType.getId() == null) {
            return createVisaType(visaType);
        }
        VisaType result = visaTypeRepository.save(visaType);
        visaTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visaType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visa-types : get all the visaTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of visaTypes in body
     */
    @GetMapping("/visa-types")
    @Timed
    public List<VisaType> getAllVisaTypes() {
        log.debug("REST request to get all VisaTypes");
        return visaTypeRepository.findAll();
        }

    /**
     * GET  /visa-types/:id : get the "id" visaType.
     *
     * @param id the id of the visaType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visaType, or with status 404 (Not Found)
     */
    @GetMapping("/visa-types/{id}")
    @Timed
    public ResponseEntity<VisaType> getVisaType(@PathVariable Long id) {
        log.debug("REST request to get VisaType : {}", id);
        VisaType visaType = visaTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visaType));
    }

    /**
     * DELETE  /visa-types/:id : delete the "id" visaType.
     *
     * @param id the id of the visaType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visa-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteVisaType(@PathVariable Long id) {
        log.debug("REST request to delete VisaType : {}", id);
        visaTypeRepository.delete(id);
        visaTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/visa-types?query=:query : search for the visaType corresponding
     * to the query.
     *
     * @param query the query of the visaType search
     * @return the result of the search
     */
    @GetMapping("/_search/visa-types")
    @Timed
    public List<VisaType> searchVisaTypes(@RequestParam String query) {
        log.debug("REST request to search VisaTypes for query {}", query);
        return StreamSupport
            .stream(visaTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
