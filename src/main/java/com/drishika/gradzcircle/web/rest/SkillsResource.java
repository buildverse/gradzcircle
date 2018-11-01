package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Skills;

import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.search.SkillsSearchRepository;
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
 * REST controller for managing Skills.
 */
@RestController
@RequestMapping("/api")
public class SkillsResource {

    private final Logger log = LoggerFactory.getLogger(SkillsResource.class);

    private static final String ENTITY_NAME = "skills";

    private final SkillsRepository skillsRepository;

    private final SkillsSearchRepository skillsSearchRepository;

    public SkillsResource(SkillsRepository skillsRepository, SkillsSearchRepository skillsSearchRepository) {
        this.skillsRepository = skillsRepository;
        this.skillsSearchRepository = skillsSearchRepository;
    }

    /**
     * POST  /skills : Create a new skills.
     *
     * @param skills the skills to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skills, or with status 400 (Bad Request) if the skills has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skills")
    @Timed
    public ResponseEntity<Skills> createSkills(@RequestBody Skills skills) throws URISyntaxException {
        log.debug("REST request to save Skills : {}", skills);
        if (skills.getId() != null) {
            throw new BadRequestAlertException("A new skills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Skills result = skillsRepository.save(skills);
        skillsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skills : Updates an existing skills.
     *
     * @param skills the skills to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skills,
     * or with status 400 (Bad Request) if the skills is not valid,
     * or with status 500 (Internal Server Error) if the skills couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skills")
    @Timed
    public ResponseEntity<Skills> updateSkills(@RequestBody Skills skills) throws URISyntaxException {
        log.debug("REST request to update Skills : {}", skills);
        if (skills.getId() == null) {
            return createSkills(skills);
        }
        Skills result = skillsRepository.save(skills);
        skillsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skills.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skills : get all the skills.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of skills in body
     */
    @GetMapping("/skills")
    @Timed
    public List<Skills> getAllSkills() {
        log.debug("REST request to get all Skills");
        return skillsRepository.findAll();
        }

    /**
     * GET  /skills/:id : get the "id" skills.
     *
     * @param id the id of the skills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skills, or with status 404 (Not Found)
     */
    @GetMapping("/skills/{id}")
    @Timed
    public ResponseEntity<Skills> getSkills(@PathVariable Long id) {
        log.debug("REST request to get Skills : {}", id);
        Skills skills = skillsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skills));
    }

    /**
     * DELETE  /skills/:id : delete the "id" skills.
     *
     * @param id the id of the skills to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkills(@PathVariable Long id) {
        log.debug("REST request to delete Skills : {}", id);
        skillsRepository.delete(id);
        skillsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/skills?query=:query : search for the skills corresponding
     * to the query.
     *
     * @param query the query of the skills search
     * @return the result of the search
     */
    @GetMapping("/_search/skills")
    @Timed
    public List<Skills> searchSkills(@RequestParam String query) {
        log.debug("REST request to search Skills for query {}", query);
        return StreamSupport
            .stream(skillsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
