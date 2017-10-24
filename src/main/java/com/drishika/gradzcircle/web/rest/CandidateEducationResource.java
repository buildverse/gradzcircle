package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Set;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CandidateEducation.
 */
@RestController
@RequestMapping("/api")
public class CandidateEducationResource {

    private final Logger log = LoggerFactory.getLogger(CandidateEducationResource.class);

    private static final String ENTITY_NAME = "candidateEducation";

    private final CandidateEducationRepository candidateEducationRepository;
    private final CandidateProjectRepository candidateProjectRepository;

    private final CandidateEducationSearchRepository candidateEducationSearchRepository;
    private final CandidateProjectSearchRepository candidateProjectSearchRepository;

    public CandidateEducationResource(CandidateEducationRepository candidateEducationRepository,
            CandidateEducationSearchRepository candidateEducationSearchRepository,
            CandidateProjectRepository candidateProjectRepository,
            CandidateProjectSearchRepository candidateProjectSearchRepository) {
        this.candidateEducationRepository = candidateEducationRepository;
        this.candidateEducationSearchRepository = candidateEducationSearchRepository;
        this.candidateProjectRepository = candidateProjectRepository;
        this.candidateProjectSearchRepository = candidateProjectSearchRepository;

    }

    /**
     * POST  /candidate-educations : Create a new candidateEducation.
     *
     * @param candidateEducation the candidateEducation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidateEducation, or with status 400 (Bad Request) if the candidateEducation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/candidate-educations")
    @Timed
    public ResponseEntity<CandidateEducation> createCandidateEducation(
            @RequestBody CandidateEducation candidateEducation) throws URISyntaxException {
        log.debug("REST request to save CandidateEducation : {}", candidateEducation);
        if (candidateEducation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
                    "A new candidateEducation cannot already have an ID")).body(null);
        }
        if("gpa".equals(candidateEducation.getScoreType()))
            setGrade(candidateEducation);
        CandidateEducation result = candidateEducationRepository.save(candidateEducation);
        candidateEducationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/candidate-educations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    private void setGrade(CandidateEducation candidateEducation){
            String gradeMajorUnit = candidateEducation.getRoundOfGrade().toString();
            String gradeMinorUnit = candidateEducation.getGradeDecimal().toString();
            candidateEducation.setGrade(new Double(gradeMajorUnit+"."+gradeMinorUnit));
    }

    /**
     * PUT  /candidate-educations : Updates an existing candidateEducation.
     *
     * @param candidateEducation the candidateEducation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidateEducation,
     * or with status 400 (Bad Request) if the candidateEducation is not valid,
     * or with status 500 (Internal Server Error) if the candidateEducation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/candidate-educations")
    @Timed
    public ResponseEntity<CandidateEducation> updateCandidateEducation(
            @RequestBody CandidateEducation candidateEducation) throws URISyntaxException {
        log.debug("REST request to update CandidateEducation : {}", candidateEducation);
        if (candidateEducation.getId() == null) {
            return createCandidateEducation(candidateEducation);
        }
        if("gpa".equals(candidateEducation.getScoreType()))
            setGrade(candidateEducation);
        candidateEducation.setProjects(null);
        CandidateEducation result = candidateEducationRepository.save(candidateEducation);
        candidateEducationSearchRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateEducation.getId().toString()))
                .body(result);
    }

    /**
     * GET  /candidate-educations : get all the candidateEducations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidateEducations in body
     */
    @GetMapping("/candidate-educations")
    @Timed
    public List<CandidateEducation> getAllCandidateEducations() {
        log.debug("REST request to get all CandidateEducations");
        return candidateEducationRepository.findAll();
        }

    /**
     * GET  /candidate-educations/:id : get the "id" candidateEducation.
     *
     * @param id the id of the candidateEducation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidateEducation, or with status 404 (Not Found)
     */
    @GetMapping("/candidate-educations/{id}")
    @Timed
    public ResponseEntity<CandidateEducation> getCandidateEducation(@PathVariable Long id) {
        log.debug("REST request to get CandidateEducation : {}", id);
        CandidateEducation candidateEducation = candidateEducationRepository.findOne(id);
        if (candidateEducation != null) {
            Set<CandidateProject> candidateProjects = candidateProjectRepository.findByEducation(candidateEducation);
            candidateEducation.setProjects(candidateProjects);
        }

        log.debug("Candidate Education reutrned is: {}", candidateEducation);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateEducation));
    }

    /**
    * GET  /candidate-educations/:id : get the "id" candidateEducation.
    *
    * @param id the id of the candidateEducation to retrieve
    * @return the ResponseEntity with status 200 (OK) and with body the candidateEducation, or with status 404 (Not Found)
    */
    @GetMapping("/education-by-candidate/{id}")
    @Timed

    public List<CandidateEducation> getEducationByCandidateId(@PathVariable Long id) {
        log.debug("REST request to get CandidateEducation : {}", id);
        List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(id);
        if (candidateEducations != null) {
            candidateEducations.forEach(candidateEducation -> {
                Set<CandidateProject> candidateProjects = candidateProjectRepository
                        .findByEducation(candidateEducation);
                candidateEducation.setProjects(candidateProjects);
                candidateEducation.setCandidate(null);
                candidateEducation.getCollege().getUniversity().setCountry(null);
            });
        }
        log.debug("Candidate Education returned is: {}", candidateEducations);
        return candidateEducations;
    }

    /**
     * DELETE  /candidate-educations/:id : delete the "id" candidateEducation.
     *
     * @param id the id of the candidateEducation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidate-educations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidateEducation(@PathVariable Long id) {
        log.debug("REST request to delete CandidateEducation : {}", id);
        candidateEducationRepository.delete(id);
        candidateEducationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/candidate-educations?query=:query : search for the candidateEducation corresponding
     * to the query.
     *
     * @param query the query of the candidateEducation search
     * @return the result of the search
     */
    @GetMapping("/_search/candidate-educations")
    @Timed
    public List<CandidateEducation> searchCandidateEducations(@RequestParam String query) {
        log.debug("REST request to search CandidateEducations for query {}", query);
        return StreamSupport
                .stream(candidateEducationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());

    }

     /**
     * SEARCH  /_search/candidate-educations?query=:query : search for the candidateEducation corresponding
     * to the query.
     *
     * @param query the query of the candidateEducation search
     * @return the result of the search
     */
    @GetMapping("/_search/candidate-educations-ordered-by-to-date")
    @Timed
    public List<CandidateEducation> searchCandidateEducationsOrderedByToDate(@RequestParam String query) {
        log.debug("REST request to search CandidateEducations for query {}", query);
        return candidateEducationSearchRepository.findByCandidateIdOrderByEducationToDateDesc(query);

    }

}
