package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.search.EmploymentTypeSearchRepository;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing EmploymentType.
 */
@RestController
@RequestMapping("/api")
public class EmploymentTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentTypeResource.class);

    private static final String ENTITY_NAME = "employmentType";

    private final EmploymentTypeRepository employmentTypeRepository;

    private final EmploymentTypeSearchRepository employmentTypeSearchRepository;
    
    private final GradzcircleCacheManager<String, Map<String,EmploymentType>> cacheManager;

    public EmploymentTypeResource(EmploymentTypeRepository employmentTypeRepository, EmploymentTypeSearchRepository employmentTypeSearchRepository,
    		GradzcircleCacheManager<String, Map<String,EmploymentType>> cacheManager) {
        this.employmentTypeRepository = employmentTypeRepository;
        this.employmentTypeSearchRepository = employmentTypeSearchRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * POST  /employment-types : Create a new employmentType.
     *
     * @param employmentType the employmentType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employmentType, or with status 400 (Bad Request) if the employmentType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employment-types")
    @Timed
    public ResponseEntity<EmploymentType> createEmploymentType(@RequestBody EmploymentType employmentType) throws URISyntaxException {
        log.debug("REST request to save EmploymentType : {}", employmentType);
        if (employmentType.getId() != null) {
            throw new BadRequestAlertException("A new employmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmploymentType result = employmentTypeRepository.save(employmentType);
        cacheManager.removeFromCache(ApplicationConstants.EMPLOYMENT_TYPE);
        employmentTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/employment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employment-types : Updates an existing employmentType.
     *
     * @param employmentType the employmentType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employmentType,
     * or with status 400 (Bad Request) if the employmentType is not valid,
     * or with status 500 (Internal Server Error) if the employmentType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employment-types")
    @Timed
    public ResponseEntity<EmploymentType> updateEmploymentType(@RequestBody EmploymentType employmentType) throws URISyntaxException {
        log.debug("REST request to update EmploymentType : {}", employmentType);
        if (employmentType.getId() == null) {
            return createEmploymentType(employmentType);
        }
        EmploymentType result = employmentTypeRepository.save(employmentType);
        employmentTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employmentType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employment-types : get all the employmentTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employmentTypes in body
     */
    @GetMapping("/employment-types")
    @Timed
    public List<EmploymentType> getAllEmploymentTypes() {
        log.debug("REST request to get all EmploymentTypes");
        return employmentTypeRepository.findAll();
        }

    /**
     * GET  /employment-types/:id : get the "id" employmentType.
     *
     * @param id the id of the employmentType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employmentType, or with status 404 (Not Found)
     */
    @GetMapping("/employment-types/{id}")
    @Timed
    public ResponseEntity<EmploymentType> getEmploymentType(@PathVariable Long id) {
        log.debug("REST request to get EmploymentType : {}", id);
        EmploymentType employmentType = employmentTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employmentType));
    }

    /**
     * DELETE  /employment-types/:id : delete the "id" employmentType.
     *
     * @param id the id of the employmentType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employment-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmploymentType(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentType : {}", id);
        employmentTypeRepository.delete(id);
        employmentTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/employment-types?query=:query : search for the employmentType corresponding
     * to the query.
     *
     * @param query the query of the employmentType search
     * @return the result of the search
     */
    @GetMapping("/_search/employment-types")
    @Timed
    public List<EmploymentType> searchEmploymentTypes(@RequestParam String query) {
        log.debug("REST request to search EmploymentTypes for query {}", query);
        return StreamSupport
            .stream(employmentTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
