package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Employability;

import com.drishika.gradzcircle.repository.EmployabilityRepository;
import com.drishika.gradzcircle.repository.search.EmployabilitySearchRepository;
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
 * REST controller for managing Employability.
 */
@RestController
@RequestMapping("/api")
public class EmployabilityResource {

	private final Logger log = LoggerFactory.getLogger(EmployabilityResource.class);

	private static final String ENTITY_NAME = "employability";

	private final EmployabilityRepository employabilityRepository;

	private final EmployabilitySearchRepository employabilitySearchRepository;

	public EmployabilityResource(EmployabilityRepository employabilityRepository,
			EmployabilitySearchRepository employabilitySearchRepository) {
		this.employabilityRepository = employabilityRepository;
		this.employabilitySearchRepository = employabilitySearchRepository;
	}

	/**
	 * POST /employabilities : Create a new employability.
	 *
	 * @param employability
	 *            the employability to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         employability, or with status 400 (Bad Request) if the employability
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/employabilities")
	@Timed
	public ResponseEntity<Employability> createEmployability(@RequestBody Employability employability)
			throws URISyntaxException {
		log.debug("REST request to save Employability : {}", employability);
		if (employability.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new employability cannot already have an ID")).body(null);
		}
		Employability result = employabilityRepository.save(employability);
		employabilitySearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/employabilities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /employabilities : Updates an existing employability.
	 *
	 * @param employability
	 *            the employability to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         employability, or with status 400 (Bad Request) if the employability
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         employability couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/employabilities")
	@Timed
	public ResponseEntity<Employability> updateEmployability(@RequestBody Employability employability)
			throws URISyntaxException {
		log.debug("REST request to update Employability : {}", employability);
		if (employability.getId() == null) {
			return createEmployability(employability);
		}
		Employability result = employabilityRepository.save(employability);
		employabilitySearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employability.getId().toString()))
				.body(result);
	}

	/**
	 * GET /employabilities : get all the employabilities.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         employabilities in body
	 */
	@GetMapping("/employabilities")
	@Timed
	public List<Employability> getAllEmployabilities() {
		log.debug("REST request to get all Employabilities");
		return employabilityRepository.findAll();
	}

	/**
	 * GET /employabilities/:id : get the "id" employability.
	 *
	 * @param id
	 *            the id of the employability to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         employability, or with status 404 (Not Found)
	 */
	@GetMapping("/employabilities/{id}")
	@Timed
	public ResponseEntity<Employability> getEmployability(@PathVariable Long id) {
		log.debug("REST request to get Employability : {}", id);
		Employability employability = employabilityRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employability));
	}

	/**
	 * DELETE /employabilities/:id : delete the "id" employability.
	 *
	 * @param id
	 *            the id of the employability to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/employabilities/{id}")
	@Timed
	public ResponseEntity<Void> deleteEmployability(@PathVariable Long id) {
		log.debug("REST request to delete Employability : {}", id);
		employabilityRepository.delete(id);
		employabilitySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/employabilities?query=:query : search for the employability
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the employability search
	 * @return the result of the search
	 */
	@GetMapping("/_search/employabilities")
	@Timed
	public List<Employability> searchEmployabilities(@RequestParam String query) {
		log.debug("REST request to search Employabilities for query {}", query);
		return StreamSupport.stream(employabilitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
