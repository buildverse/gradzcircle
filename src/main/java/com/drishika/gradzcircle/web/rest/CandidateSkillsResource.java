package com.drishika.gradzcircle.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.service.CandidateSkillsService;
import com.drishika.gradzcircle.service.dto.CandidateSkillsDTO;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CandidateSkills.
 */
@RestController
@RequestMapping("/api")
public class CandidateSkillsResource {

    private final Logger log = LoggerFactory.getLogger(CandidateSkillsResource.class);

    private static final String ENTITY_NAME = "candidateSkills";

   private CandidateSkillsService candidateSkillsService;

    public CandidateSkillsResource(CandidateSkillsService candidateSkillsService) {
        this.candidateSkillsService = candidateSkillsService;
    }

    /**
     * POST  /candidate-skills : Create a new candidateSkills.
     *
     * @param candidateSkills the candidateSkills to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidateSkills, or with status 400 (Bad Request) if the candidateSkills has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/candidate-skills")
    @Timed
    public ResponseEntity<Set<CandidateSkills>> createCandidateSkills(@RequestBody CandidateSkills candidateSkills) throws URISyntaxException {
        log.debug("REST request to save CandidateSkills : {}", candidateSkills);
        if (candidateSkills.getId() != null) {
            throw new BadRequestAlertException("A new candidateSkills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Set<CandidateSkills> result = candidateSkillsService.createCandidateSkills(candidateSkills); 
        log.debug("Post create Skill is {}",result);
        if(result == null)
        	throw new BadRequestAlertException("A new candidateSkills cannot be created as no candidate available", ENTITY_NAME, "idexists");
        return ResponseEntity.created(new URI("/api/candidate-skills/" ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,"CREATED"))
            .body(result);
    }

    /**
     * PUT  /candidate-skills : Updates an existing candidateSkills.
     *
     * @param candidateSkills the candidateSkills to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidateSkills,
     * or with status 400 (Bad Request) if the candidateSkills is not valid,
     * or with status 500 (Internal Server Error) if the candidateSkills couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/candidate-skills")
    @Timed
    public ResponseEntity<CandidateSkills> updateCandidateSkills(@RequestBody CandidateSkills candidateSkills) throws URISyntaxException {
        log.debug("REST request to update CandidateSkills : {}", candidateSkills);
       /* if (candidateSkills.getId() == null) {
            return createCandidateSkills(candidateSkills);
        }*/
        CandidateSkills result = candidateSkillsService.updateCandidateSkills(candidateSkills);
       // candidateSkillsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateSkills.getId().toString()))
            .body(result);
    }

    /**
     * GET  /candidate-skills : get all the candidateSkills.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidateSkills in body
     */
    @GetMapping("/candidate-skills")
    @Timed
    public List<CandidateSkills> getAllCandidateSkills() {
        log.debug("REST request to get all CandidateSkills");
        return candidateSkillsService.getAllCandidateSkills();
        }

    /**
     * GET  /candidate-skills/:id : get the "id" candidateSkills.
     *
     * @param id the id of the candidateSkills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidateSkills, or with status 404 (Not Found)
     */
    @GetMapping("/candidate-skills/{id}")
    @Timed
    public ResponseEntity<CandidateSkillsDTO> getCandidateSkill(@PathVariable Long id) {
        log.debug("REST request to get CandidateSkills : {}", id);
        CandidateSkillsDTO candidateSkills = candidateSkillsService.getCandidateSkill(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateSkills));
    }

    /**
     * DELETE  /candidate-skills/:id : delete the "id" candidateSkills.
     *
     * @param id the id of the candidateSkills to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidate-skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidateSkills(@PathVariable Long id) {
        log.debug("REST request to delete CandidateSkills : {}", id);
        candidateSkillsService.deleteCandidateSkills(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/candidate-skills?query=:query : search for the candidateSkills corresponding
     * to the query.
     *
     * @param query the query of the candidateSkills search
     * @return the result of the search
     */
    @GetMapping("/_search/candidate-skills")
    @Timed
    public List<CandidateSkills> searchCandidateSkills(@RequestParam String query) {
        log.debug("REST request to search CandidateSkills for query {}", query);
        return candidateSkillsService.searchCandidateSkills(query);
    }
    
    @GetMapping("/skills-for-candidate/{id}")
    @Timed
    public List<CandidateSkillsDTO> getSkillsForCandidate(@PathVariable Long id) {
    	log.debug("Request to get skills for Candidate {}",id);
    		return candidateSkillsService.getSkillsForCandidate(id);
    }

}
