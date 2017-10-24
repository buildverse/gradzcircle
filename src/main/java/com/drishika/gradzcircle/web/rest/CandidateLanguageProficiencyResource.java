package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;

import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.CustomParameterizedException;
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

import javax.validation.ConstraintViolationException;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CandidateLanguageProficiency.
 */
@RestController
@RequestMapping("/api")
public class CandidateLanguageProficiencyResource {

    private final Logger log = LoggerFactory.getLogger(CandidateLanguageProficiencyResource.class);

    private static final String ENTITY_NAME = "candidateLanguageProficiency";

    private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

    private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

    public CandidateLanguageProficiencyResource(CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository, CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository) {
        this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
        this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
    }

    /**
     * POST  /candidate-language-proficiencies : Create a new candidateLanguageProficiency.
     *
     * @param candidateLanguageProficiency the candidateLanguageProficiency to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidateLanguageProficiency, or with status 400 (Bad Request) if the candidateLanguageProficiency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/candidate-language-proficiencies")
    @Timed
    public ResponseEntity<CandidateLanguageProficiency> createCandidateLanguageProficiency(@RequestBody CandidateLanguageProficiency candidateLanguageProficiency) throws URISyntaxException {
        log.debug("REST request to save CandidateLanguageProficiency : {}", candidateLanguageProficiency);
        if (candidateLanguageProficiency.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new candidateLanguageProficiency cannot already have an ID")).body(null);
        }
        CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency);
        log.debug("The result post save language proficicency{}",result);
        candidateLanguageProficiencySearchRepository.save(result);

        return ResponseEntity.created(new URI("/api/candidate-language-proficiencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /candidate-language-proficiencies : Updates an existing candidateLanguageProficiency.
     *
     * @param candidateLanguageProficiency the candidateLanguageProficiency to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidateLanguageProficiency,
     * or with status 400 (Bad Request) if the candidateLanguageProficiency is not valid,
     * or with status 500 (Internal Server Error) if the candidateLanguageProficiency couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/candidate-language-proficiencies")
    @Timed
    public ResponseEntity<CandidateLanguageProficiency> updateCandidateLanguageProficiency(@RequestBody CandidateLanguageProficiency candidateLanguageProficiency) throws URISyntaxException {
        log.debug("REST request to update CandidateLanguageProficiency : {}", candidateLanguageProficiency);
        if (candidateLanguageProficiency.getId() == null) {
            return createCandidateLanguageProficiency(candidateLanguageProficiency);
        }
        CandidateLanguageProficiency result =null;
        try{
        result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency);
        log.debug("The result post update language proficicency{}",result);
        candidateLanguageProficiencySearchRepository.save(result);

        } catch(org.springframework.dao.DataIntegrityViolationException dataIntegrityViolationException){

            throw new CustomParameterizedException(candidateLanguageProficiency.getLanguage().getLanguage()+" already exists");
        } catch (Exception ex){
            throw new CustomParameterizedException("Something Unexpected happened. Please try again later");
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateLanguageProficiency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /candidate-language-proficiencies : get all the candidateLanguageProficiencies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidateLanguageProficiencies in body
     */
    @GetMapping("/candidate-language-proficiencies")
    @Timed
    public List<CandidateLanguageProficiency> getAllCandidateLanguageProficiencies() {
        log.debug("REST request to get all CandidateLanguageProficiencies");
        List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository.findAll();
        //candidateLanguageProficiencies.forEach(candidateLanguageProficiency->candidateLanguageProficiency.setCandidate(null));
        return candidateLanguageProficiencies;
    }

    /**
     * GET  /candidate-language-proficiencies/:id : get the "id" candidateLanguageProficiency.
     *
     * @param id the id of the candidateLanguageProficiency to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidateLanguageProficiency, or with status 404 (Not Found)
     */
    @GetMapping("/candidate-language-proficiencies/{id}")
    @Timed
    public ResponseEntity<CandidateLanguageProficiency> getCandidateLanguageProficiency(@PathVariable Long id) {
        log.debug("REST request to get CandidateLanguageProficiency : {}", id);
        CandidateLanguageProficiency candidateLanguageProficiency = candidateLanguageProficiencyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateLanguageProficiency));
    }

     /**
     * GET  /language-proficiencies-by-candidate/:id : get the "id" candidate Id.
     *
     * @param id the id of the candidate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidateLanguageProficiency, or with status 404 (Not Found)
     */
    @GetMapping("/language-proficiencies-by-candidate/{id}")
    @Timed
    public List<CandidateLanguageProficiency> getCandidateLanguageProficiencyByCandidate(@PathVariable Long id) {
        log.debug("REST request to get CandidateLanguageProficiency : {}", id);
        List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository.findCandidateLanguageProficienciesByCandidateId(id);
        candidateLanguageProficiencies.forEach(candidateLanguageProficiency->candidateLanguageProficiency.setCandidate(null));
        return candidateLanguageProficiencies;
    }

    /**
     * DELETE  /candidate-language-proficiencies/:id : delete the "id" candidateLanguageProficiency.
     *
     * @param id the id of the candidateLanguageProficiency to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidate-language-proficiencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidateLanguageProficiency(@PathVariable Long id) {
        log.debug("REST request to delete CandidateLanguageProficiency : {}", id);
        candidateLanguageProficiencyRepository.delete(id);
        candidateLanguageProficiencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/candidate-language-proficiencies?query=:query : search for the candidateLanguageProficiency corresponding
     * to the query.
     *
     * @param query the query of the candidateLanguageProficiency search
     * @return the result of the search
     */
    @GetMapping("/_search/candidate-language-proficiencies")
    @Timed
    public List<CandidateLanguageProficiency> searchCandidateLanguageProficiencies(@RequestParam String query) {
        log.debug("REST request to search CandidateLanguageProficiencies for query {}", query);
         List<CandidateLanguageProficiency> candidateLanguageProficiencies = StreamSupport
            .stream(candidateLanguageProficiencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
        candidateLanguageProficiencies.forEach(candidateLanguageProficiency->candidateLanguageProficiency.setCandidate(null));
        return candidateLanguageProficiencies;
    }

}
