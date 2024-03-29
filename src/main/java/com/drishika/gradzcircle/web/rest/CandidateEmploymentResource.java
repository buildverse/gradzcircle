package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.service.CandidateEmploymentService;
import com.drishika.gradzcircle.service.dto.CandidateEmploymentDTO;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CandidateEmployment.
 */
@RestController
@RequestMapping("/api")
public class CandidateEmploymentResource {

	private final Logger log = LoggerFactory.getLogger(CandidateEmploymentResource.class);

	private static final String ENTITY_NAME = "candidateEmployment";

	private final CandidateEmploymentService candidateEmploymentService;

	public CandidateEmploymentResource(CandidateEmploymentService candidateEmploymentService) {
		this.candidateEmploymentService = candidateEmploymentService;

	}

	/**
	 * POST /candidate-employments : Create a new candidateEmployment.
	 *
	 * @param candidateEmployment
	 *            the candidateEmployment to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         candidateEmployment, or with status 400 (Bad Request) if the
	 *         candidateEmployment has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/candidate-employments")
	@Timed
	public ResponseEntity<CandidateEmployment> createCandidateEmployment(
			@RequestBody CandidateEmployment candidateEmployment) throws URISyntaxException {
		log.debug("REST request to save CandidateEmployment : {}", candidateEmployment);
		if (candidateEmployment.getId() != null) {
            throw new BadRequestAlertException("A new candidateEmployment cannot already have an ID", ENTITY_NAME, "idexists");
        }
		CandidateEmployment result = candidateEmploymentService.createCandidateEmployment(candidateEmployment);
		return ResponseEntity.created(new URI("/api/candidate-employments/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /candidate-employments : Updates an existing candidateEmployment.
	 *
	 * @param candidateEmployment
	 *            the candidateEmployment to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         candidateEmployment, or with status 400 (Bad Request) if the
	 *         candidateEmployment is not valid, or with status 500 (Internal Server
	 *         Error) if the candidateEmployment couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/candidate-employments")
	@Timed
	public ResponseEntity<CandidateEmployment> updateCandidateEmployment(
			@RequestBody CandidateEmployment candidateEmployment) throws URISyntaxException {
		log.debug("REST request to update CandidateEmployment : {}", candidateEmployment);
		if (candidateEmployment.getId() == null) {
			return createCandidateEmployment(candidateEmployment);
		}
		CandidateEmployment result = candidateEmploymentService.updateCandidateEmployment(candidateEmployment); 
				
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateEmployment.getId().toString()))
				.body(result);
	}

	/**
	 * GET /candidate-employments : get all the candidateEmployments.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/candidate-employments")
	@Timed
	public List<CandidateEmployment> getAllCandidateEmployments() {
		log.debug("REST request to get all CandidateEmployments");
		return candidateEmploymentService.findAll();
	}

	/**
	 * GET /candidate-employments : get all the candidateEmployments.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateEmployments in body
	 */
	@GetMapping("/employment-by-candidate/{id}")
	@Timed
	public List<CandidateEmploymentDTO> getEmploymentsForCandidate(@PathVariable Long id) {
		log.debug("REST request to get employemnt for Candidate: {}", id);
		
		return candidateEmploymentService.getEmploymentsForCandidate(id);
	}

	/**
	 * GET /candidate-employments/:id : get the "id" candidateEmployment.
	 *
	 * @param id
	 *            the id of the candidateEmployment to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         candidateEmployment, or with status 404 (Not Found)
	 */
	@GetMapping("/candidate-employments/{id}")
	@Timed
	public ResponseEntity<CandidateEmployment> getCandidateEmployment(@PathVariable Long id) {
		log.debug("REST request to get CandidateEmployment : {}", id);
		return ResponseUtil.wrapOrNotFound(candidateEmploymentService.getCandidateEmployment(id));
	}

	/**
	 * DELETE /candidate-employments/:id : delete the "id" candidateEmployment.
	 *
	 * @param id
	 *            the id of the candidateEmployment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/candidate-employments/{id}")
	@Timed
	public ResponseEntity<Void> deleteCandidateEmployment(@PathVariable Long id) {
		candidateEmploymentService.deleteCandidateEmployment(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/candidate-employments?query=:query : search for the
	 * candidateEmployment corresponding to the query.
	 *
	 * @param query
	 *            the query of the candidateEmployment search
	 * @return the result of the search
	 */
	@GetMapping("/_search/candidate-employments")
	@Timed
	public List<CandidateEmployment> searchCandidateEmployments(@RequestParam String query) {
		log.debug("REST request to search CandidateEmployments for query {}", query);
		return candidateEmploymentService.searchCandidateEmployments(query);
	}

}
