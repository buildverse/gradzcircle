package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.CountryEntityBuilder;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CountrySearchRepository;
import com.drishika.gradzcircle.service.CountryService;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

	private final Logger log = LoggerFactory.getLogger(CountryResource.class);

	private static final String ENTITY_NAME = "country";

	private final CountryRepository countryRepository;
	
	private final CountryService countryService;

	public CountryResource(CountryRepository countryRepository, CountryService countryService) {
		this.countryRepository = countryRepository;
		this.countryService = countryService;
	}

	/**
	 * POST /countries : Create a new country.
	 *
	 * @param country
	 *            the country to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         country, or with status 400 (Bad Request) if the country has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/countries")
	@Timed
	public ResponseEntity<Country> createCountry(@RequestBody Country country) throws URISyntaxException {
		log.debug("REST request to save Country : {}", country);
		if (country.getId() != null) {
			throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Country result = countryService.createCountry(country);
		return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /countries : Updates an existing country.
	 *
	 * @param country
	 *            the country to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         country, or with status 400 (Bad Request) if the country is not
	 *         valid, or with status 500 (Internal Server Error) if the country
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/countries")
	@Timed
	public ResponseEntity<Country> updateCountry(@RequestBody Country country) throws URISyntaxException {
		log.debug("REST request to update Country : {}", country);
		if (country.getId() == null) {
			throw new BadRequestAlertException("A new profileCategory cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Country result = countryService.updateCountry(country);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, country.getId().toString()))
				.body(result);
	}

	/**
	 * GET /countries : get all the countries.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of countries in
	 *         body
	 */
	@GetMapping("/countries")
	@Timed
	public List<Country> getAllCountries() {
		log.debug("REST request to get all Countries");
		return countryRepository.findAll();
	}

	/**
	 * GET /countries : get all the countries.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of countries in
	 *         body
	 */
	@GetMapping("/countriesMetaData")
	@Timed
	public List<Country> getCountries() {
		log.debug("REST request to get all Countries");
		List<Country> countries = countryRepository.findCountries();
		return countries;
	}

	/**
	 * GET /countries/:id : get the "id" country.
	 *
	 * @param id
	 *            the id of the country to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the country, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/countries/{id}")
	@Timed
	public ResponseEntity<Country> getCountry(@PathVariable Long id) {
		log.debug("REST request to get Country : {}", id);
		Optional<Country> country = countryRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(country);
	}

	/**
	 * DELETE /countries/:id : delete the "id" country.
	 *
	 * @param id
	 *            the id of the country to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/countries/{id}")
	@Timed
	public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
		log.debug("REST request to delete Country : {}", id);
		countryService.deleteCountry(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 *
	 * Get Enabled countries : Countires where we will be live
	 *
	 */

	@GetMapping("/countries/enabled")
	@Timed
	public List<Country> getEnabledCountries() {
		log.debug("Call to get enabled countries");
		List<Country> countries =null;
		try {
			countries = countryService.getEnabledCountries();
		} catch (Exception e) {
			log.error("Unable to get enabled Countries {}",e);
		}
		return countries;
	}

	/**
	 * SEARCH /_search/countries?query=:query : search for the country corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the country search
	 * @return the result of the search
	 */
	@GetMapping("/_search/countries")
	@Timed
	public List<Country> searchCountries(@RequestParam String query) {
		log.debug("REST request to search Countries for query {}", query);
		return countryService.searchCountries(query);
		
	}
	
	 /**
	 * SEARCH /_search/jobCategories?query=:query : search for the college corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the college search
	 * @return the result of the search
	 */
	@GetMapping("/_search/countryBySuggest")
	@Timed
	public String searchCountryBySuggest(@RequestParam String query) {
		log.debug("REST request to search Country for query {}", query);
		return countryService.searchCountryBySuggest(query);
		
	}

}
