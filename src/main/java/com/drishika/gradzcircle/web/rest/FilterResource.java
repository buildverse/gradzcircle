package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Filter;

import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.search.FilterSearchRepository;
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
 * REST controller for managing Filter.
 */
@RestController
@RequestMapping("/api")
public class FilterResource {

    private final Logger log = LoggerFactory.getLogger(FilterResource.class);

    private static final String ENTITY_NAME = "filter";

    private final FilterRepository filterRepository;

    private final FilterSearchRepository filterSearchRepository;

    public FilterResource(FilterRepository filterRepository, FilterSearchRepository filterSearchRepository) {
        this.filterRepository = filterRepository;
        this.filterSearchRepository = filterSearchRepository;
    }

    /**
     * POST  /filters : Create a new filter.
     *
     * @param filter the filter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new filter, or with status 400 (Bad Request) if the filter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/filters")
    @Timed
    public ResponseEntity<Filter> createFilter(@RequestBody Filter filter) throws URISyntaxException {
        log.debug("REST request to save Filter : {}", filter);
        if (filter.getId() != null) {
            throw new BadRequestAlertException("A new filter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Filter result = filterRepository.save(filter);
        filterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /filters : Updates an existing filter.
     *
     * @param filter the filter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated filter,
     * or with status 400 (Bad Request) if the filter is not valid,
     * or with status 500 (Internal Server Error) if the filter couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/filters")
    @Timed
    public ResponseEntity<Filter> updateFilter(@RequestBody Filter filter) throws URISyntaxException {
        log.debug("REST request to update Filter : {}", filter);
        if (filter.getId() == null) {
        	throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Filter result = filterRepository.save(filter);
        filterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, filter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /filters : get all the filters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of filters in body
     */
    @GetMapping("/filters")
    @Timed
    public List<Filter> getAllFilters() {
        log.debug("REST request to get all Filters");
        return filterRepository.findAll();
        }

    /**
     * GET  /filters/:id : get the "id" filter.
     *
     * @param id the id of the filter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the filter, or with status 404 (Not Found)
     */
    @GetMapping("/filters/{id}")
    @Timed
    public ResponseEntity<Filter> getFilter(@PathVariable Long id) {
        log.debug("REST request to get Filter : {}", id);
        Optional<Filter> filter = filterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filter);
    }

    /**
     * DELETE  /filters/:id : delete the "id" filter.
     *
     * @param id the id of the filter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/filters/{id}")
    @Timed
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        log.debug("REST request to delete Filter : {}", id);
        filterRepository.deleteById(id);
        filterSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/filters?query=:query : search for the filter corresponding
     * to the query.
     *
     * @param query the query of the filter search
     * @return the result of the search
     */
    @GetMapping("/_search/filters")
    @Timed
    public List<Filter> searchFilters(@RequestParam String query) {
        log.debug("REST request to search Filters for query {}", query);
        return StreamSupport
            .stream(filterSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
