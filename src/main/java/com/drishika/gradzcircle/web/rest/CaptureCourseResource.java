package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CaptureCourse;

import com.drishika.gradzcircle.repository.CaptureCourseRepository;
import com.drishika.gradzcircle.repository.search.CaptureCourseSearchRepository;
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
 * REST controller for managing CaptureCourse.
 */
@RestController
@RequestMapping("/api")
public class CaptureCourseResource {

    private final Logger log = LoggerFactory.getLogger(CaptureCourseResource.class);

    private static final String ENTITY_NAME = "captureCourse";

    private final CaptureCourseRepository captureCourseRepository;

    private final CaptureCourseSearchRepository captureCourseSearchRepository;

    public CaptureCourseResource(CaptureCourseRepository captureCourseRepository, CaptureCourseSearchRepository captureCourseSearchRepository) {
        this.captureCourseRepository = captureCourseRepository;
        this.captureCourseSearchRepository = captureCourseSearchRepository;
    }

    /**
     * POST  /capture-courses : Create a new captureCourse.
     *
     * @param captureCourse the captureCourse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new captureCourse, or with status 400 (Bad Request) if the captureCourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/capture-courses")
    @Timed
    public ResponseEntity<CaptureCourse> createCaptureCourse(@RequestBody CaptureCourse captureCourse) throws URISyntaxException {
        log.debug("REST request to save CaptureCourse : {}", captureCourse);
        if (captureCourse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new captureCourse cannot already have an ID")).body(null);
        }
        CaptureCourse result = captureCourseRepository.save(captureCourse);
        captureCourseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/capture-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /capture-courses : Updates an existing captureCourse.
     *
     * @param captureCourse the captureCourse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated captureCourse,
     * or with status 400 (Bad Request) if the captureCourse is not valid,
     * or with status 500 (Internal Server Error) if the captureCourse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/capture-courses")
    @Timed
    public ResponseEntity<CaptureCourse> updateCaptureCourse(@RequestBody CaptureCourse captureCourse) throws URISyntaxException {
        log.debug("REST request to update CaptureCourse : {}", captureCourse);
        if (captureCourse.getId() == null) {
            return createCaptureCourse(captureCourse);
        }
        CaptureCourse result = captureCourseRepository.save(captureCourse);
        captureCourseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, captureCourse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /capture-courses : get all the captureCourses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of captureCourses in body
     */
    @GetMapping("/capture-courses")
    @Timed
    public List<CaptureCourse> getAllCaptureCourses() {
        log.debug("REST request to get all CaptureCourses");
        return captureCourseRepository.findAll();
        }

    /**
     * GET  /capture-courses/:id : get the "id" captureCourse.
     *
     * @param id the id of the captureCourse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the captureCourse, or with status 404 (Not Found)
     */
    @GetMapping("/capture-courses/{id}")
    @Timed
    public ResponseEntity<CaptureCourse> getCaptureCourse(@PathVariable Long id) {
        log.debug("REST request to get CaptureCourse : {}", id);
        CaptureCourse captureCourse = captureCourseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(captureCourse));
    }

    /**
     * DELETE  /capture-courses/:id : delete the "id" captureCourse.
     *
     * @param id the id of the captureCourse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/capture-courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaptureCourse(@PathVariable Long id) {
        log.debug("REST request to delete CaptureCourse : {}", id);
        captureCourseRepository.delete(id);
        captureCourseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/capture-courses?query=:query : search for the captureCourse corresponding
     * to the query.
     *
     * @param query the query of the captureCourse search
     * @return the result of the search
     */
    @GetMapping("/_search/capture-courses")
    @Timed
    public List<CaptureCourse> searchCaptureCourses(@RequestParam String query) {
        log.debug("REST request to search CaptureCourses for query {}", query);
        return StreamSupport
            .stream(captureCourseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
