package com.drishika.gradzcircle.web.rest;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;

import com.drishika.gradzcircle.service.CandidateNonAcademicService;
import com.drishika.gradzcircle.service.dto.CandidateNonAcademicWorkDTO;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CandidateNonAcademicWork.
 */
@RestController
@RequestMapping("/api")
public class CandidateNonAcademicWorkResource {

	private final Logger log = LoggerFactory.getLogger(CandidateNonAcademicWorkResource.class);

	private static final String ENTITY_NAME = "candidateNonAcademicWork";
	
	private final CandidateNonAcademicService candidateNonAcademiceService;

	public CandidateNonAcademicWorkResource(CandidateNonAcademicService candidateNonAcademiceService) {

		this.candidateNonAcademiceService = candidateNonAcademiceService;
		
	}

	/**
	 * POST /candidate-non-academic-works : Create a new candidateNonAcademicWork.
	 *
	 * @param candidateNonAcademicWork
	 *            the candidateNonAcademicWork to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         candidateNonAcademicWork, or with status 400 (Bad Request) if the
	 *         candidateNonAcademicWork has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/candidate-non-academic-works")
	@Timed
	public ResponseEntity<CandidateNonAcademicWork> createCandidateNonAcademicWork(
			@RequestBody CandidateNonAcademicWork candidateNonAcademicWork) throws URISyntaxException {
		log.debug("REST request to save CandidateNonAcademicWork : {}", candidateNonAcademicWork);
		if (candidateNonAcademicWork.getId() != null) {
			throw new BadRequestAlertException("A new candidateNonAcademicWork cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		CandidateNonAcademicWork result = candidateNonAcademiceService.createCandidateNonAcademicWork(candidateNonAcademicWork);
		return ResponseEntity.created(new URI("/api/candidate-non-academic-works/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /candidate-non-academic-works : Updates an existing
	 * candidateNonAcademicWork.
	 *
	 * @param candidateNonAcademicWork
	 *            the candidateNonAcademicWork to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         candidateNonAcademicWork, or with status 400 (Bad Request) if the
	 *         candidateNonAcademicWork is not valid, or with status 500 (Internal
	 *         Server Error) if the candidateNonAcademicWork couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/candidate-non-academic-works")
	@Timed
	public ResponseEntity<CandidateNonAcademicWork> updateCandidateNonAcademicWork(
			@RequestBody CandidateNonAcademicWork candidateNonAcademicWork) throws URISyntaxException {
		log.debug("REST request to update CandidateNonAcademicWork : {}", candidateNonAcademicWork);
		if (candidateNonAcademicWork.getId() == null) {
			return createCandidateNonAcademicWork(candidateNonAcademicWork);
		}
		CandidateNonAcademicWork result = candidateNonAcademiceService.updateCandidateNonAcademicWork(candidateNonAcademicWork);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateNonAcademicWork.getId().toString()))
				.body(result);
	}

	/**
	 * GET /candidate-non-academic-works : get all the candidateNonAcademicWorks.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateNonAcademicWorks in body
	 */
	@GetMapping("/candidate-non-academic-works")
	@Timed
	public List<CandidateNonAcademicWork> getAllCandidateNonAcademicWorks() {
		log.debug("REST request to get all CandidateNonAcademicWorks");
		return candidateNonAcademiceService.getAllCandidateNonAcademicWorks();
	}

	/**
	 * GET /candidate-non-academic-works/:id : get the "id"
	 * candidateNonAcademicWork.
	 *
	 * @param id
	 *            the id of the candidateNonAcademicWork to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         candidateNonAcademicWork, or with status 404 (Not Found)
	 */
	@GetMapping("/candidate-non-academic-works/{id}")
	@Timed
	public ResponseEntity<CandidateNonAcademicWork> getCandidateNonAcademicWork(@PathVariable Long id) {
		log.debug("REST request to get CandidateNonAcademicWork : {}", id);
		return ResponseUtil.wrapOrNotFound(candidateNonAcademiceService.getCandidateNonAcademicWork(id));
	}

	/**
	 * DELETE /candidate-non-academic-works/:id : delete the "id"
	 * candidateNonAcademicWork.
	 *
	 * @param id
	 *            the id of the candidateNonAcademicWork to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/candidate-non-academic-works/{id}")
	@Timed
	public ResponseEntity<Void> deleteCandidateNonAcademicWork(@PathVariable Long id) {
		log.debug("REST request to delete CandidateNonAcademicWork : {}", id);
		candidateNonAcademiceService.deleteCandidateNonAcademicWork(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * GET /candidate-certifications/:id : get certifications by candidate id
	 *
	 * @param id
	 *            the id of the candidateCertification to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         candidateCertification, or with status 404 (Not Found)
	 */
	@GetMapping("/candidate-non-academic-work-by-id/{id}")
	@Timed
	public List<CandidateNonAcademicWorkDTO> getNonAcadsByCandidate(@PathVariable Long id) {
		log.debug("REST request to get CandidateCertifications by Candidate Id : {}", id);
		return candidateNonAcademiceService.getNonAcadsByCandidate(id);
		
	}

	/**
	 * SEARCH /_search/candidate-non-academic-works?query=:query : search for the
	 * candidateNonAcademicWork corresponding to the query.
	 *
	 * @param query
	 *            the query of the candidateNonAcademicWork search
	 * @return the result of the search
	 */
	@GetMapping("/_search/candidate-non-academic-works")
	@Timed
	public List<CandidateNonAcademicWork> searchCandidateNonAcademicWorks(@RequestParam String query) {
		log.debug("REST request to search CandidateNonAcademicWorks for query {}", query);
		return candidateNonAcademiceService.searchCandidateNonAcademicWorks(query);
	}
	
}
