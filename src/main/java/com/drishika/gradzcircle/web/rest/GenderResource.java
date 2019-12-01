package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.BeanUtils;
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
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.GenderEntityBuilder;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.search.GenderSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

	private final Logger log = LoggerFactory.getLogger(GenderResource.class);

	private static final String ENTITY_NAME = "gender";

	private final GenderRepository genderRepository;

	private final GenderSearchRepository genderSearchRepository;

	private final ElasticsearchTemplate elasticSearchTemplate;

	public GenderResource(GenderRepository genderRepository, GenderSearchRepository genderSearchRepository,
			ElasticsearchTemplate elasticSearchTemplate) {
		this.genderRepository = genderRepository;
		this.genderSearchRepository = genderSearchRepository;
		this.elasticSearchTemplate = elasticSearchTemplate;
	}

	/**
	 * POST /genders : Create a new gender.
	 *
	 * @param gender
	 *            the gender to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         gender, or with status 400 (Bad Request) if the gender has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/genders")
	@Timed
	public ResponseEntity<Gender> createGender(@RequestBody Gender gender) throws URISyntaxException {
		log.debug("REST request to save Gender : {}", gender);
		 if (gender.getId() != null) {
	            throw new BadRequestAlertException("A new gender cannot already have an ID", ENTITY_NAME, "idexists");
	        }
		Gender result = genderRepository.save(gender);
		elasticSearchTemplate.index(new GenderEntityBuilder(result.getId()).name(result.getGender())
				.suggest(new String[] { result.getGender() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);
		// genderSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /genders : Updates an existing gender.
	 *
	 * @param gender
	 *            the gender to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         gender, or with status 400 (Bad Request) if the gender is not valid,
	 *         or with status 500 (Internal Server Error) if the gender couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/genders")
	@Timed
	public ResponseEntity<Gender> updateGender(@RequestBody Gender gender) throws URISyntaxException {
		log.debug("REST request to update Gender : {}", gender);
		if (gender.getId() == null) {
			 throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Gender result = genderRepository.save(gender);
		// genderSearchRepository.save(result);
		com.drishika.gradzcircle.domain.elastic.Gender genderElasticInstance = new com.drishika.gradzcircle.domain.elastic.Gender();
		try {
			BeanUtils.copyProperties(genderElasticInstance, gender);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticSearchTemplate.index(new GenderEntityBuilder(result.getId()).name(result.getGender())
				.suggest(new String[] { result.getGender() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gender.getId().toString()))
				.body(result);
	}

	/**
	 * GET /genders : get all the genders.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of genders in
	 *         body
	 */
	@GetMapping("/genders")
	@Timed
	public List<Gender> getAllGenders() {
		log.debug("REST request to get all Genders");
		return genderRepository.findAll();
	}

	/**
	 * GET /genders/:id : get the "id" gender.
	 *
	 * @param id
	 *            the id of the gender to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gender, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/genders/{id}")
	@Timed
	public ResponseEntity<Gender> getGender(@PathVariable Long id) {
		log.debug("REST request to get Gender : {}", id);
		Optional<Gender> gender = genderRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(gender);
	}

	/**
	 * DELETE /genders/:id : delete the "id" gender.
	 *
	 * @param id
	 *            the id of the gender to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/genders/{id}")
	@Timed
	public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
		log.debug("REST request to delete Gender : {}", id);
		genderRepository.deleteById(id);
		genderSearchRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/genders?query=:query : search for the gender corresponding to
	 * the query.
	 *
	 * @param query
	 *            the query of the gender search
	 * @return the result of the search
	 */
	@GetMapping("/_search/genders")
	@Timed
	public List<Gender> searchGenders(@RequestParam String query) {
		log.debug("REST request to search Genders for query {}", query);
		return StreamSupport.stream(genderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

	/**
	 * SEARCH /_search/colleges?query=:query : search for the college corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the college search
	 * @return the result of the search
	 */
	@GetMapping("/_search/genderBySuggest")
	@Timed
	public String searchGenderBySuggest(@RequestParam String query) {
		log.debug("REST request to search Gender for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("suggest").text(query).prefix(query);
		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("suggest", completionSuggestionBuilder);
		SearchResponse searchResponse = elasticSearchTemplate.suggest(suggestion, com.drishika.gradzcircle.domain.elastic.Gender.class);
		CompletionSuggestion completionSuggestion = searchResponse.getSuggest().getSuggestion("suggest");
		
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> genders = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			genders.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(genders);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
