package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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
import com.drishika.gradzcircle.domain.MaritalStatus;
import com.drishika.gradzcircle.entitybuilders.MaritalStatusEntityBuilder;
import com.drishika.gradzcircle.repository.MaritalStatusRepository;
import com.drishika.gradzcircle.repository.search.MaritalStatusSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing MaritalStatus.
 */
@RestController
@RequestMapping("/api")
public class MaritalStatusResource {

    private final Logger log = LoggerFactory.getLogger(MaritalStatusResource.class);

    private static final String ENTITY_NAME = "maritalStatus";

    private final MaritalStatusRepository maritalStatusRepository;

    private final MaritalStatusSearchRepository maritalStatusSearchRepository;
    
    private final ElasticsearchTemplate elasticSearchTemplate;

    public MaritalStatusResource(MaritalStatusRepository maritalStatusRepository, MaritalStatusSearchRepository maritalStatusSearchRepository,ElasticsearchTemplate elasticSearchTemplate) {
        this.maritalStatusRepository = maritalStatusRepository;
        this.maritalStatusSearchRepository = maritalStatusSearchRepository;
        this.elasticSearchTemplate = elasticSearchTemplate;
    }

    /**
     * POST  /marital-statuses : Create a new maritalStatus.
     *
     * @param maritalStatus the maritalStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new maritalStatus, or with status 400 (Bad Request) if the maritalStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/marital-statuses")
    @Timed
    public ResponseEntity<MaritalStatus> createMaritalStatus(@RequestBody MaritalStatus maritalStatus) throws URISyntaxException {
        log.debug("REST request to save MaritalStatus : {}", maritalStatus);
        if (maritalStatus.getId() != null) {
            throw new BadRequestAlertException("A new maritalStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaritalStatus result = maritalStatusRepository.save(maritalStatus);
        elasticSearchTemplate.index(new MaritalStatusEntityBuilder(result.getId()).name(result.getStatus())
				.suggest(new String[] { result.getStatus() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
        return ResponseEntity.created(new URI("/api/marital-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /marital-statuses : Updates an existing maritalStatus.
     *
     * @param maritalStatus the maritalStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated maritalStatus,
     * or with status 400 (Bad Request) if the maritalStatus is not valid,
     * or with status 500 (Internal Server Error) if the maritalStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/marital-statuses")
    @Timed
    public ResponseEntity<MaritalStatus> updateMaritalStatus(@RequestBody MaritalStatus maritalStatus) throws URISyntaxException {
        log.debug("REST request to update MaritalStatus : {}", maritalStatus);
        if (maritalStatus.getId() == null) {
            return createMaritalStatus(maritalStatus);
        }
        MaritalStatus result = maritalStatusRepository.save(maritalStatus);
        elasticSearchTemplate.index(new MaritalStatusEntityBuilder(result.getId()).name(result.getStatus())
				.suggest(new String[] { result.getStatus() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maritalStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /marital-statuses : get all the maritalStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of maritalStatuses in body
     */
    @GetMapping("/marital-statuses")
    @Timed
    public List<MaritalStatus> getAllMaritalStatuses() {
        log.debug("REST request to get all MaritalStatuses");
        return maritalStatusRepository.findAll();
        }

    /**
     * GET  /marital-statuses/:id : get the "id" maritalStatus.
     *
     * @param id the id of the maritalStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the maritalStatus, or with status 404 (Not Found)
     */
    @GetMapping("/marital-statuses/{id}")
    @Timed
    public ResponseEntity<MaritalStatus> getMaritalStatus(@PathVariable Long id) {
        log.debug("REST request to get MaritalStatus : {}", id);
        MaritalStatus maritalStatus = maritalStatusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maritalStatus));
    }

    /**
     * DELETE  /marital-statuses/:id : delete the "id" maritalStatus.
     *
     * @param id the id of the maritalStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/marital-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteMaritalStatus(@PathVariable Long id) {
        log.debug("REST request to delete MaritalStatus : {}", id);
        maritalStatusRepository.delete(id);
        maritalStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/marital-statuses?query=:query : search for the maritalStatus corresponding
     * to the query.
     *
     * @param query the query of the maritalStatus search
     * @return the result of the search
     */
    @GetMapping("/_search/marital-statuses")
    @Timed
    public List<MaritalStatus> searchMaritalStatuses(@RequestParam String query) {
        log.debug("REST request to search MaritalStatuses for query {}", query);
        return StreamSupport
            .stream(maritalStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
