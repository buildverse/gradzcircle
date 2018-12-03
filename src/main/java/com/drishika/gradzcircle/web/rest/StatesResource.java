package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.States;

import com.drishika.gradzcircle.repository.StatesRepository;
import com.drishika.gradzcircle.repository.search.StatesSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.drishika.gradzcircle.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing States.
 */
@RestController
@RequestMapping("/api")
public class StatesResource {

    private final Logger log = LoggerFactory.getLogger(StatesResource.class);

    private static final String ENTITY_NAME = "states";

    private final StatesRepository statesRepository;

    private final StatesSearchRepository statesSearchRepository;

    public StatesResource(StatesRepository statesRepository, StatesSearchRepository statesSearchRepository) {
        this.statesRepository = statesRepository;
        this.statesSearchRepository = statesSearchRepository;
    }

    /**
     * POST  /states : Create a new states.
     *
     * @param states the states to create
     * @return the ResponseEntity with status 201 (Created) and with body the new states, or with status 400 (Bad Request) if the states has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/states")
    @Timed
    public ResponseEntity<States> createStates(@RequestBody States states) throws URISyntaxException {
        log.debug("REST request to save States : {}", states);
        if (states.getId() != null) {
            throw new BadRequestAlertException("A new states cannot already have an ID", ENTITY_NAME, "idexists");
        }
        States result = statesRepository.save(states);
        statesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /states : Updates an existing states.
     *
     * @param states the states to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated states,
     * or with status 400 (Bad Request) if the states is not valid,
     * or with status 500 (Internal Server Error) if the states couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/states")
    @Timed
    public ResponseEntity<States> updateStates(@RequestBody States states) throws URISyntaxException {
        log.debug("REST request to update States : {}", states);
        if (states.getId() == null) {
            return createStates(states);
        }
        States result = statesRepository.save(states);
        statesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, states.getId().toString()))
            .body(result);
    }

    /**
     * GET  /states : get all the states.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of states in body
     */
    @GetMapping("/states")
    @Timed
    public ResponseEntity<List<States>> getAllStates(Pageable pageable) {
        log.debug("REST request to get a page of States");
        Page<States> page = statesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /states/:id : get the "id" states.
     *
     * @param id the id of the states to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the states, or with status 404 (Not Found)
     */
    @GetMapping("/states/{id}")
    @Timed
    public ResponseEntity<States> getStates(@PathVariable Long id) {
        log.debug("REST request to get States : {}", id);
        States states = statesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(states));
    }

    /**
     * DELETE  /states/:id : delete the "id" states.
     *
     * @param id the id of the states to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/states/{id}")
    @Timed
    public ResponseEntity<Void> deleteStates(@PathVariable Long id) {
        log.debug("REST request to delete States : {}", id);
        statesRepository.delete(id);
        statesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/states?query=:query : search for the states corresponding
     * to the query.
     *
     * @param query the query of the states search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/states")
    @Timed
    public ResponseEntity<List<States>> searchStates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of States for query {}", query);
        Page<States> page = statesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
