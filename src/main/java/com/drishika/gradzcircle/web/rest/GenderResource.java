package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Gender;

import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.search.GenderSearchRepository;
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
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    private static final String ENTITY_NAME = "gender";

    private final GenderRepository genderRepository;

    private final GenderSearchRepository genderSearchRepository;

    public GenderResource(GenderRepository genderRepository, GenderSearchRepository genderSearchRepository) {
        this.genderRepository = genderRepository;
        this.genderSearchRepository = genderSearchRepository;
    }

    /**
     * POST  /genders : Create a new gender.
     *
     * @param gender the gender to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gender, or with status 400 (Bad Request) if the gender has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genders")
    @Timed
    public ResponseEntity<Gender> createGender(@RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to save Gender : {}", gender);
        if (gender.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gender cannot already have an ID")).body(null);
        }
        Gender result = genderRepository.save(gender);
        genderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genders : Updates an existing gender.
     *
     * @param gender the gender to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gender,
     * or with status 400 (Bad Request) if the gender is not valid,
     * or with status 500 (Internal Server Error) if the gender couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genders")
    @Timed
    public ResponseEntity<Gender> updateGender(@RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to update Gender : {}", gender);
        if (gender.getId() == null) {
            return createGender(gender);
        }
        Gender result = genderRepository.save(gender);
        genderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genders : get all the genders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of genders in body
     */
    @GetMapping("/genders")
    @Timed
    public List<Gender> getAllGenders() {
        log.debug("REST request to get all Genders");
        return genderRepository.findAll();
        }

    /**
     * GET  /genders/:id : get the "id" gender.
     *
     * @param id the id of the gender to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gender, or with status 404 (Not Found)
     */
    @GetMapping("/genders/{id}")
    @Timed
    public ResponseEntity<Gender> getGender(@PathVariable Long id) {
        log.debug("REST request to get Gender : {}", id);
        Gender gender = genderRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gender));
    }

    /**
     * DELETE  /genders/:id : delete the "id" gender.
     *
     * @param id the id of the gender to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genders/{id}")
    @Timed
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        log.debug("REST request to delete Gender : {}", id);
        genderRepository.delete(id);
        genderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/genders?query=:query : search for the gender corresponding
     * to the query.
     *
     * @param query the query of the gender search
     * @return the result of the search
     */
    @GetMapping("/_search/genders")
    @Timed
    public List<Gender> searchGenders(@RequestParam String query) {
        log.debug("REST request to search Genders for query {}", query);
        return StreamSupport
            .stream(genderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
