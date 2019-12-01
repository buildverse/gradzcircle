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
import com.drishika.gradzcircle.domain.Industry;
import com.drishika.gradzcircle.entitybuilders.IndustryEntityBuilder;
import com.drishika.gradzcircle.repository.IndustryRepository;
import com.drishika.gradzcircle.repository.search.IndustrySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Industry.
 */
@RestController
@RequestMapping("/api")
public class IndustryResource {

	private final Logger log = LoggerFactory.getLogger(IndustryResource.class);

	private static final String ENTITY_NAME = "industry";

	private final IndustryRepository industryRepository;

	private final IndustrySearchRepository industrySearchRepository;
	
	private final ElasticsearchTemplate elasticsearchTemplate;

	public IndustryResource(IndustryRepository industryRepository, IndustrySearchRepository industrySearchRepository,ElasticsearchTemplate elasticsearchTemplate) {
		this.industryRepository = industryRepository;
		this.industrySearchRepository = industrySearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /industries : Create a new industry.
	 *
	 * @param industry
	 *            the industry to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         industry, or with status 400 (Bad Request) if the industry has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/industries")
	@Timed
	public ResponseEntity<Industry> createIndustry(@RequestBody Industry industry) throws URISyntaxException {
		log.debug("REST request to save Industry : {}", industry);
		 if (industry.getId() != null) {
	            throw new BadRequestAlertException("A new industry cannot already have an ID", ENTITY_NAME, "idexists");
	    }
		Industry result = industryRepository.save(industry);
		elasticsearchTemplate.index(new IndustryEntityBuilder(result.getId()).name(result.getIndustryName())
				.suggest(new String[] { result.getIndustryName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Industry.class);
		return ResponseEntity.created(new URI("/api/industries/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /industries : Updates an existing industry.
	 *
	 * @param industry
	 *            the industry to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         industry, or with status 400 (Bad Request) if the industry is not
	 *         valid, or with status 500 (Internal Server Error) if the industry
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/industries")
	@Timed
	public ResponseEntity<Industry> updateIndustry(@RequestBody Industry industry) throws URISyntaxException {
		log.debug("REST request to update Industry : {}", industry);
		if (industry.getId() == null) {
			 throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Industry result = industryRepository.save(industry);
		elasticsearchTemplate.index(new IndustryEntityBuilder(result.getId()).name(result.getIndustryName())
				.suggest(new String[] { result.getIndustryName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Industry.class);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, industry.getId().toString()))
				.body(result);
	}

	/**
	 * GET /industries : get all the industries.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of industries in
	 *         body
	 */
	@GetMapping("/industries")
	@Timed
	public List<Industry> getAllIndustries() {
		log.debug("REST request to get all Industries");
		return industryRepository.findAll();
	}

	/**
	 * GET /industries/:id : get the "id" industry.
	 *
	 * @param id
	 *            the id of the industry to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the industry,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/industries/{id}")
	@Timed
	public ResponseEntity<Industry> getIndustry(@PathVariable Long id) {
		log.debug("REST request to get Industry : {}", id);
		Optional<Industry> industry = industryRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(industry);
	}

	/**
	 * DELETE /industries/:id : delete the "id" industry.
	 *
	 * @param id
	 *            the id of the industry to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/industries/{id}")
	@Timed
	public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
		log.debug("REST request to delete Industry : {}", id);
		industryRepository.deleteById(id);
		industrySearchRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/industries?query=:query : search for the industry
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the industry search
	 * @return the result of the search
	 */
	@GetMapping("/_search/industries")
	@Timed
	public List<Industry> searchIndustries(@RequestParam String query) {
		log.debug("REST request to search Industries for query {}", query);
		return StreamSupport.stream(industrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
