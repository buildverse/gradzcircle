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
import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.repository.NationalityRepository;
import com.drishika.gradzcircle.repository.search.NationalitySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Nationality.
 */
@RestController
@RequestMapping("/api")
public class NationalityResource {

	private final Logger log = LoggerFactory.getLogger(NationalityResource.class);

	private static final String ENTITY_NAME = "nationality";

	private final NationalityRepository nationalityRepository;

	private final NationalitySearchRepository nationalitySearchRepository;

	public NationalityResource(NationalityRepository nationalityRepository,
			NationalitySearchRepository nationalitySearchRepository) {
		this.nationalityRepository = nationalityRepository;
		this.nationalitySearchRepository = nationalitySearchRepository;
	}

	/**
	 * POST /nationalities : Create a new nationality.
	 *
	 * @param nationality
	 *            the nationality to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         nationality, or with status 400 (Bad Request) if the nationality has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/nationalities")
	@Timed
	public ResponseEntity<Nationality> createNationality(@RequestBody Nationality nationality)
			throws URISyntaxException {
		log.debug("REST request to save Nationality : {}", nationality);
		if (nationality.getId() != null) {
            throw new BadRequestAlertException("A new nationality cannot already have an ID", ENTITY_NAME, "idexists");
        }
		Nationality result = nationalityRepository.save(nationality);
		nationalitySearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/nationalities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /nationalities : Updates an existing nationality.
	 *
	 * @param nationality
	 *            the nationality to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         nationality, or with status 400 (Bad Request) if the nationality is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         nationality couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/nationalities")
	@Timed
	public ResponseEntity<Nationality> updateNationality(@RequestBody Nationality nationality)
			throws URISyntaxException {
		log.debug("REST request to update Nationality : {}", nationality);
		if (nationality.getId() == null) {
			return createNationality(nationality);
		}
		Nationality result = nationalityRepository.save(nationality);
		nationalitySearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nationality.getId().toString())).body(result);
	}

	/**
	 * GET /nationalities : get all the nationalities.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of nationalities
	 *         in body
	 */
	@GetMapping("/nationalities")
	@Timed
	public List<Nationality> getAllNationalities() {
		log.debug("REST request to get all Nationalities");
		return nationalityRepository.findAll();
	}

	/**
	 * GET /nationalities/:id : get the "id" nationality.
	 *
	 * @param id
	 *            the id of the nationality to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         nationality, or with status 404 (Not Found)
	 */
	@GetMapping("/nationalities/{id}")
	@Timed
	public ResponseEntity<Nationality> getNationality(@PathVariable Long id) {
		log.debug("REST request to get Nationality : {}", id);
		Nationality nationality = nationalityRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nationality));
	}

	/**
	 * DELETE /nationalities/:id : delete the "id" nationality.
	 *
	 * @param id
	 *            the id of the nationality to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/nationalities/{id}")
	@Timed
	public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
		log.debug("REST request to delete Nationality : {}", id);
		nationalityRepository.delete(id);
		nationalitySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/nationalities?query=:query : search for the nationality
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the nationality search
	 * @return the result of the search
	 */
	@GetMapping("/_search/nationalities")
	@Timed
	public List<Nationality> searchNationalities(@RequestParam String query) {
		log.debug("REST request to search Nationalities for query {}", query);
		return StreamSupport.stream(nationalitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
