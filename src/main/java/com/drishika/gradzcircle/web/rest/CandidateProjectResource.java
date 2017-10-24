package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CandidateProject;

import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.joda.time.Duration;
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
import java.time.LocalDate;
import java.time.Period;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CandidateProject.
 */
@RestController
@RequestMapping("/api")
public class CandidateProjectResource {

    private final Logger log = LoggerFactory.getLogger(CandidateProjectResource.class);

    private static final String ENTITY_NAME = "candidateProject";

    private final CandidateProjectRepository candidateProjectRepository;

    private final CandidateProjectSearchRepository candidateProjectSearchRepository;

    public CandidateProjectResource(CandidateProjectRepository candidateProjectRepository, CandidateProjectSearchRepository candidateProjectSearchRepository) {
        this.candidateProjectRepository = candidateProjectRepository;
        this.candidateProjectSearchRepository = candidateProjectSearchRepository;
    }

    /**
     * POST  /candidate-projects : Create a new candidateProject.
     *
     * @param candidateProject the candidateProject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidateProject, or with status 400 (Bad Request) if the candidateProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/candidate-projects")
    @Timed
    public ResponseEntity<CandidateProject> createCandidateProject(@RequestBody CandidateProject candidateProject) throws URISyntaxException {
        log.debug("REST request to save CandidateProject : {}", candidateProject);
        if (candidateProject.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new candidateProject cannot already have an ID")).body(null);
        }
        CandidateProject result = candidateProjectRepository.save(candidateProject);
        candidateProjectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/candidate-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // private void calculateProjectDuration(CandidateProject candidateProject){
    //     if(candidateProject.isIsCurrentProject()!=null){
    //         Period period = Period.between(candidateProject.getProjectStartDate(), candidateProject.getProjectEndDate());
    //         candidateProject.setDuration(period.getYears()+" years "+ period.getMonths()+" months "+period.getDays());
    //     }
    // }

    /**
     * PUT  /candidate-projects : Updates an existing candidateProject.
     *
     * @param candidateProject the candidateProject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidateProject,
     * or with status 400 (Bad Request) if the candidateProject is not valid,
     * or with status 500 (Internal Server Error) if the candidateProject couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/candidate-projects")
    @Timed
    public ResponseEntity<CandidateProject> updateCandidateProject(@RequestBody CandidateProject candidateProject) throws URISyntaxException {
        log.debug("REST request to update CandidateProject : {}", candidateProject);
        if (candidateProject.getId() == null) {
            return createCandidateProject(candidateProject);
        }
        CandidateProject result = candidateProjectRepository.save(candidateProject);
        candidateProjectSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateProject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /candidate-projects : get all the candidateProjects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidateProjects in body
     */
    @GetMapping("/candidate-projects")
    @Timed
    public List<CandidateProject> getAllCandidateProjects() {
        log.debug("REST request to get all CandidateProjects");
        List<CandidateProject> candidateProjects = candidateProjectRepository.findAll();
        // candidateProjects.forEach(candidateProject->{
        //     calculateProjectDuration(candidateProject);
        // });
        return candidateProjects;
    }

    /**
     * GET  /candidate-projects/:id : get the "id" candidateProject.
     *
     * @param id the id of the candidateProject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidateProject, or with status 404 (Not Found)
     */
    @GetMapping("/candidate-projects/{id}")
    @Timed
    public ResponseEntity<CandidateProject> getCandidateProject(@PathVariable Long id) {
        log.debug("REST request to get CandidateProject : {}", id);
        CandidateProject candidateProject = candidateProjectRepository.findOne(id);
        //calculateProjectDuration(candidateProject);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateProject));
    }

    /**
     * DELETE  /candidate-projects/:id : delete the "id" candidateProject.
     *
     * @param id the id of the candidateProject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidate-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidateProject(@PathVariable Long id) {
        log.debug("REST request to delete CandidateProject : {}", id);
        candidateProjectRepository.delete(id);
        candidateProjectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/candidate-projects?query=:query : search for the candidateProject corresponding
     * to the query.
     *
     * @param query the query of the candidateProject search
     * @return the result of the search
     */
    @GetMapping("/_search/candidate-projects")
    @Timed
    public List<CandidateProject> searchCandidateProjects(@RequestParam String query) {
        log.debug("REST request to search CandidateProjects for query {}", query);
        return StreamSupport
            .stream(candidateProjectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
