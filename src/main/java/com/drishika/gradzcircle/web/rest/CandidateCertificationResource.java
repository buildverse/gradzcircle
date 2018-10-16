package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;

import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
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
 * REST controller for managing CandidateCertification.
 */
@RestController
@RequestMapping("/api")
public class CandidateCertificationResource {

	private final Logger log = LoggerFactory.getLogger(CandidateCertificationResource.class);

	private static final String ENTITY_NAME = "candidateCertification";

	private final CandidateCertificationRepository candidateCertificationRepository;

	private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	public CandidateCertificationResource(CandidateCertificationRepository candidateCertificationRepository,
			CandidateCertificationSearchRepository candidateCertificationSearchRepository) {
		this.candidateCertificationRepository = candidateCertificationRepository;
		this.candidateCertificationSearchRepository = candidateCertificationSearchRepository;
	}

	/**
	 * POST /candidate-certifications : Create a new candidateCertification.
	 *
	 * @param candidateCertification
	 *            the candidateCertification to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         candidateCertification, or with status 400 (Bad Request) if the
	 *         candidateCertification has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/candidate-certifications")
	@Timed
	public ResponseEntity<CandidateCertification> createCandidateCertification(
			@RequestBody CandidateCertification candidateCertification) throws URISyntaxException {
		log.debug("REST request to save CandidateCertification : {}", candidateCertification);
		if (candidateCertification.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new candidateCertification cannot already have an ID")).body(null);
		}
		CandidateCertification result = candidateCertificationRepository.save(candidateCertification);
		candidateCertificationSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/candidate-certifications/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /candidate-certifications : Updates an existing candidateCertification.
	 *
	 * @param candidateCertification
	 *            the candidateCertification to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         candidateCertification, or with status 400 (Bad Request) if the
	 *         candidateCertification is not valid, or with status 500 (Internal
	 *         Server Error) if the candidateCertification couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/candidate-certifications")
	@Timed
	public ResponseEntity<CandidateCertification> updateCandidateCertification(
			@RequestBody CandidateCertification candidateCertification) throws URISyntaxException {
		log.debug("REST request to update CandidateCertification : {}", candidateCertification);
		if (candidateCertification.getId() == null) {
			return createCandidateCertification(candidateCertification);
		}
		CandidateCertification result = candidateCertificationRepository.save(candidateCertification);
		candidateCertificationSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidateCertification.getId().toString()))
				.body(result);
	}

	/**
	 * GET /candidate-certifications : get all the candidateCertifications.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         candidateCertifications in body
	 */
	@GetMapping("/candidate-certifications")
	@Timed
	public List<CandidateCertification> getAllCandidateCertifications() {
		log.debug("REST request to get all CandidateCertifications");
		List<CandidateCertification> candidateCertifications = candidateCertificationRepository.findAll();
		candidateCertifications.forEach(candidateCertification -> candidateCertification.setCandidate(null));
		return candidateCertifications;
	}

	/**
	 * GET /candidate-certifications/:id : get the "id" candidateCertification.
	 *
	 * @param id
	 *            the id of the candidateCertification to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         candidateCertification, or with status 404 (Not Found)
	 */
	@GetMapping("/candidate-certifications/{id}")
	@Timed
	public ResponseEntity<CandidateCertification> getCandidateCertification(@PathVariable Long id) {
		log.debug("REST request to get CandidateCertification : {}", id);
		CandidateCertification candidateCertification = candidateCertificationRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateCertification));
	}

	/**
	 * GET /candidate-certifications/:id : get certifications by candidate id
	 *
	 * @param id
	 *            the id of the candidateCertification to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         candidateCertification, or with status 404 (Not Found)
	 */
	@GetMapping("/candidate-cert-by-id/{id}")
	@Timed
	public List<CandidateCertification> getCertificationByCandidate(@PathVariable Long id) {
		log.debug("REST request to get CandidateCertifications by Candidate Id : {}", id);
		List<CandidateCertification> candidateCertifications = candidateCertificationRepository
				.findCertificationsByCandidateId(id);
		candidateCertifications.forEach(candidateCertification -> candidateCertification.setCandidate(null));
		return candidateCertifications;
	}

	/**
	 * DELETE /candidate-certifications/:id : delete the "id"
	 * candidateCertification.
	 *
	 * @param id
	 *            the id of the candidateCertification to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/candidate-certifications/{id}")
	@Timed
	public ResponseEntity<Void> deleteCandidateCertification(@PathVariable Long id) {
		log.debug("REST request to delete CandidateCertification : {}", id);
		candidateCertificationRepository.delete(id);
		candidateCertificationSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/candidate-certifications?query=:query : search for the
	 * candidateCertification corresponding to the query.
	 *
	 * @param query
	 *            the query of the candidateCertification search
	 * @return the result of the search
	 */
	@GetMapping("/_search/candidate-certifications")
	@Timed
	public List<CandidateCertification> searchCandidateCertifications(@RequestParam String query) {
		log.debug("REST request to search CandidateCertifications for query {}", query);
		List<CandidateCertification> candidateCertifications = StreamSupport
				.stream(candidateCertificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		candidateCertifications.forEach(candidateCertification -> candidateCertification.setCandidate(null));
		return candidateCertifications;
	}

	/**
	 * SEARCH /_search/candidate-certifications?query=:query : search for the
	 * candidateCertification corresponding to the query.
	 *
	 * @param query
	 *            the query of the candidateCertification search
	 * @return the result of the search
	 */
	@GetMapping("/_search/candidate-certifications-admin")
	@Timed
	public List<CandidateCertification> searchCandidateCertificationsAdmin(@RequestParam String query) {
		log.debug("REST request to search CandidateCertifications for query {}", query);
		return StreamSupport
				.stream(candidateCertificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}
}
