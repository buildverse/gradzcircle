package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.ErrorMessages;

import com.drishika.gradzcircle.repository.ErrorMessagesRepository;
import com.drishika.gradzcircle.repository.search.ErrorMessagesSearchRepository;
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
 * REST controller for managing ErrorMessages.
 */
@RestController
@RequestMapping("/api")
public class ErrorMessagesResource {

	private final Logger log = LoggerFactory.getLogger(ErrorMessagesResource.class);

	private static final String ENTITY_NAME = "errorMessages";

	private final ErrorMessagesRepository errorMessagesRepository;

	private final ErrorMessagesSearchRepository errorMessagesSearchRepository;

	public ErrorMessagesResource(ErrorMessagesRepository errorMessagesRepository,
			ErrorMessagesSearchRepository errorMessagesSearchRepository) {
		this.errorMessagesRepository = errorMessagesRepository;
		this.errorMessagesSearchRepository = errorMessagesSearchRepository;
	}

	/**
	 * POST /error-messages : Create a new errorMessages.
	 *
	 * @param errorMessages
	 *            the errorMessages to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         errorMessages, or with status 400 (Bad Request) if the errorMessages
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/error-messages")
	@Timed
	public ResponseEntity<ErrorMessages> createErrorMessages(@RequestBody ErrorMessages errorMessages)
			throws URISyntaxException {
		log.debug("REST request to save ErrorMessages : {}", errorMessages);
		if (errorMessages.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new errorMessages cannot already have an ID")).body(null);
		}
		ErrorMessages result = errorMessagesRepository.save(errorMessages);
		errorMessagesSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/error-messages/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /error-messages : Updates an existing errorMessages.
	 *
	 * @param errorMessages
	 *            the errorMessages to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         errorMessages, or with status 400 (Bad Request) if the errorMessages
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         errorMessages couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/error-messages")
	@Timed
	public ResponseEntity<ErrorMessages> updateErrorMessages(@RequestBody ErrorMessages errorMessages)
			throws URISyntaxException {
		log.debug("REST request to update ErrorMessages : {}", errorMessages);
		if (errorMessages.getId() == null) {
			return createErrorMessages(errorMessages);
		}
		ErrorMessages result = errorMessagesRepository.save(errorMessages);
		errorMessagesSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, errorMessages.getId().toString()))
				.body(result);
	}

	/**
	 * GET /error-messages : get all the errorMessages.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of errorMessages
	 *         in body
	 */
	@GetMapping("/error-messages")
	@Timed
	public List<ErrorMessages> getAllErrorMessages() {
		log.debug("REST request to get all ErrorMessages");
		return errorMessagesRepository.findAll();
	}

	/**
	 * GET /error-messages/:id : get the "id" errorMessages.
	 *
	 * @param id
	 *            the id of the errorMessages to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         errorMessages, or with status 404 (Not Found)
	 */
	@GetMapping("/error-messages/{id}")
	@Timed
	public ResponseEntity<ErrorMessages> getErrorMessages(@PathVariable Long id) {
		log.debug("REST request to get ErrorMessages : {}", id);
		ErrorMessages errorMessages = errorMessagesRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(errorMessages));
	}

	/**
	 * DELETE /error-messages/:id : delete the "id" errorMessages.
	 *
	 * @param id
	 *            the id of the errorMessages to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/error-messages/{id}")
	@Timed
	public ResponseEntity<Void> deleteErrorMessages(@PathVariable Long id) {
		log.debug("REST request to delete ErrorMessages : {}", id);
		errorMessagesRepository.delete(id);
		errorMessagesSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/error-messages?query=:query : search for the errorMessages
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the errorMessages search
	 * @return the result of the search
	 */
	@GetMapping("/_search/error-messages")
	@Timed
	public List<ErrorMessages> searchErrorMessages(@RequestParam String query) {
		log.debug("REST request to search ErrorMessages for query {}", query);
		return StreamSupport.stream(errorMessagesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
