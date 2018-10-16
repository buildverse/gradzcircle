package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.CaptureUniversity;

import com.drishika.gradzcircle.repository.CaptureUniversityRepository;
import com.drishika.gradzcircle.repository.search.CaptureUniversitySearchRepository;
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
 * REST controller for managing CaptureUniversity.
 */
@RestController
@RequestMapping("/api")
public class CaptureUniversityResource {

	private final Logger log = LoggerFactory.getLogger(CaptureUniversityResource.class);

	private static final String ENTITY_NAME = "captureUniversity";

	private final CaptureUniversityRepository captureUniversityRepository;

	private final CaptureUniversitySearchRepository captureUniversitySearchRepository;

	public CaptureUniversityResource(CaptureUniversityRepository captureUniversityRepository,
			CaptureUniversitySearchRepository captureUniversitySearchRepository) {
		this.captureUniversityRepository = captureUniversityRepository;
		this.captureUniversitySearchRepository = captureUniversitySearchRepository;
	}

	/**
	 * POST /capture-universities : Create a new captureUniversity.
	 *
	 * @param captureUniversity
	 *            the captureUniversity to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         captureUniversity, or with status 400 (Bad Request) if the
	 *         captureUniversity has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/capture-universities")
	@Timed
	public ResponseEntity<CaptureUniversity> createCaptureUniversity(@RequestBody CaptureUniversity captureUniversity)
			throws URISyntaxException {
		log.debug("REST request to save CaptureUniversity : {}", captureUniversity);
		if (captureUniversity.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new captureUniversity cannot already have an ID")).body(null);
		}
		CaptureUniversity result = captureUniversityRepository.save(captureUniversity);
		captureUniversitySearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/capture-universities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /capture-universities : Updates an existing captureUniversity.
	 *
	 * @param captureUniversity
	 *            the captureUniversity to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         captureUniversity, or with status 400 (Bad Request) if the
	 *         captureUniversity is not valid, or with status 500 (Internal Server
	 *         Error) if the captureUniversity couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/capture-universities")
	@Timed
	public ResponseEntity<CaptureUniversity> updateCaptureUniversity(@RequestBody CaptureUniversity captureUniversity)
			throws URISyntaxException {
		log.debug("REST request to update CaptureUniversity : {}", captureUniversity);
		if (captureUniversity.getId() == null) {
			return createCaptureUniversity(captureUniversity);
		}
		CaptureUniversity result = captureUniversityRepository.save(captureUniversity);
		captureUniversitySearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, captureUniversity.getId().toString()))
				.body(result);
	}

	/**
	 * GET /capture-universities : get all the captureUniversities.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         captureUniversities in body
	 */
	@GetMapping("/capture-universities")
	@Timed
	public List<CaptureUniversity> getAllCaptureUniversities() {
		log.debug("REST request to get all CaptureUniversities");
		return captureUniversityRepository.findAll();
	}

	/**
	 * GET /capture-universities/:id : get the "id" captureUniversity.
	 *
	 * @param id
	 *            the id of the captureUniversity to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         captureUniversity, or with status 404 (Not Found)
	 */
	@GetMapping("/capture-universities/{id}")
	@Timed
	public ResponseEntity<CaptureUniversity> getCaptureUniversity(@PathVariable Long id) {
		log.debug("REST request to get CaptureUniversity : {}", id);
		CaptureUniversity captureUniversity = captureUniversityRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(captureUniversity));
	}

	/**
	 * DELETE /capture-universities/:id : delete the "id" captureUniversity.
	 *
	 * @param id
	 *            the id of the captureUniversity to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/capture-universities/{id}")
	@Timed
	public ResponseEntity<Void> deleteCaptureUniversity(@PathVariable Long id) {
		log.debug("REST request to delete CaptureUniversity : {}", id);
		captureUniversityRepository.delete(id);
		captureUniversitySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/capture-universities?query=:query : search for the
	 * captureUniversity corresponding to the query.
	 *
	 * @param query
	 *            the query of the captureUniversity search
	 * @return the result of the search
	 */
	@GetMapping("/_search/capture-universities")
	@Timed
	public List<CaptureUniversity> searchCaptureUniversities(@RequestParam String query) {
		log.debug("REST request to search CaptureUniversities for query {}", query);
		return StreamSupport
				.stream(captureUniversitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
