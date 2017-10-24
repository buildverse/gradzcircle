package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.University;

import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
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
 * REST controller for managing University.
 */
@RestController
@RequestMapping("/api")
public class UniversityResource {

    private final Logger log = LoggerFactory.getLogger(UniversityResource.class);

    private static final String ENTITY_NAME = "university";

    private final UniversityRepository universityRepository;

    private final UniversitySearchRepository universitySearchRepository;

    public UniversityResource(UniversityRepository universityRepository, UniversitySearchRepository universitySearchRepository) {
        this.universityRepository = universityRepository;
        this.universitySearchRepository = universitySearchRepository;
    }

    /**
     * POST  /universities : Create a new university.
     *
     * @param university the university to create
     * @return the ResponseEntity with status 201 (Created) and with body the new university, or with status 400 (Bad Request) if the university has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/universities")
    @Timed
    public ResponseEntity<University> createUniversity(@RequestBody University university) throws URISyntaxException {
        log.debug("REST request to save University : {}", university);
        if (university.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new university cannot already have an ID")).body(null);
        }
        University result = universityRepository.save(university);
        universitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/universities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /universities : Updates an existing university.
     *
     * @param university the university to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated university,
     * or with status 400 (Bad Request) if the university is not valid,
     * or with status 500 (Internal Server Error) if the university couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/universities")
    @Timed
    public ResponseEntity<University> updateUniversity(@RequestBody University university) throws URISyntaxException {
        log.debug("REST request to update University : {}", university);
        if (university.getId() == null) {
            return createUniversity(university);
        }
        University result = universityRepository.save(university);
        universitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, university.getId().toString()))
            .body(result);
    }

    /**
     * GET  /universities : get all the universities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of universities in body
     */
    @GetMapping("/universities")
    @Timed
    public List<University> getAllUniversities() {
        log.debug("REST request to get all Universities");
        return universityRepository.findAll();
        }

    /**
     * GET  /universities/:id : get the "id" university.
     *
     * @param id the id of the university to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the university, or with status 404 (Not Found)
     */
    @GetMapping("/universities/{id}")
    @Timed
    public ResponseEntity<University> getUniversity(@PathVariable Long id) {
        log.debug("REST request to get University : {}", id);
        University university = universityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(university));
    }

    /**
     * DELETE  /universities/:id : delete the "id" university.
     *
     * @param id the id of the university to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/universities/{id}")
    @Timed
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        log.debug("REST request to delete University : {}", id);
        universityRepository.delete(id);
        universitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/universities?query=:query : search for the university corresponding
     * to the query.
     *
     * @param query the query of the university search
     * @return the result of the search
     */
    @GetMapping("/_search/universities")
    @Timed
    public List<University> searchUniversities(@RequestParam String query) {
        log.debug("REST request to search Universities for query {}", query);
        return StreamSupport
            .stream(universitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
