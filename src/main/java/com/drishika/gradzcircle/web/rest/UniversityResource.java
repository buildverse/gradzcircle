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

import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.suggest.SuggestResponse;
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
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing University.
 */
@RestController
@RequestMapping("/api")
public class UniversityResource {

	private final Logger log = LoggerFactory.getLogger(UniversityResource.class);

	private static final String ENTITY_NAME = "university";

	private final UniversityRepository universityRepository;

	private final UniversitySearchRepository universitySearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public UniversityResource(UniversityRepository universityRepository,
			UniversitySearchRepository universitySearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
		this.universityRepository = universityRepository;
		this.universitySearchRepository = universitySearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /universities : Create a new university.
	 *
	 * @param university
	 *            the university to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         university, or with status 400 (Bad Request) if the university has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/universities")
	@Timed
	public ResponseEntity<University> createUniversity(@RequestBody University university) throws URISyntaxException {
		log.debug("REST request to save University : {}", university);
		if (university.getId() != null) {
            throw new BadRequestAlertException("A new university cannot already have an ID", ENTITY_NAME, "idexists");
        }
		University result = universityRepository.save(university);
		// universitySearchRepository.save(result);
		elasticsearchTemplate.index(new UniversityEntityBuilder(result.getId()).name(result.getUniversityName())
				.suggest(new String[] { result.getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
		return ResponseEntity.created(new URI("/api/universities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /universities : Updates an existing university.
	 *
	 * @param university
	 *            the university to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         university, or with status 400 (Bad Request) if the university is not
	 *         valid, or with status 500 (Internal Server Error) if the university
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/universities")
	@Timed
	public ResponseEntity<University> updateUniversity(@RequestBody University university) throws URISyntaxException {
		log.debug("REST request to update University : {}", university);
		if (university.getId() == null) {
			return createUniversity(university);
		}
		University result = universityRepository.save(university);
		// universitySearchRepository.save(result);
		com.drishika.gradzcircle.domain.elastic.University universityElasticInstance = new com.drishika.gradzcircle.domain.elastic.University();
		try {
			BeanUtils.copyProperties(universityElasticInstance, university);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new UniversityEntityBuilder(universityElasticInstance.getId())
				.name(universityElasticInstance.getUniversityName())
				.suggest(new String[] { universityElasticInstance.getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, university.getId().toString())).body(result);
	}

	/**
	 * GET /universities : get all the universities.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of universities
	 *         in body
	 */
	@GetMapping("/universities")
	@Timed
	public List<University> getAllUniversities() {
		log.debug("REST request to get all Universities");
		return universityRepository.findAll();
	}

	/**
	 * GET /universities/:id : get the "id" university.
	 *
	 * @param id
	 *            the id of the university to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the university,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/universities/{id}")
	@Timed
	public ResponseEntity<University> getUniversity(@PathVariable Long id) {
		log.debug("REST request to get University : {}", id);
		University university = universityRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(university));
	}

	/**
	 * DELETE /universities/:id : delete the "id" university.
	 *
	 * @param id
	 *            the id of the university to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/universities/{id}")
	@Timed
	public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
		log.debug("REST request to delete University : {}", id);
		universityRepository.delete(id);
		universitySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/universities?query=:query : search for the university
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the university search
	 * @return the result of the search
	 */
	@GetMapping("/_search/universities")
	@Timed
	public List<University> searchUniversities(@RequestParam String query) {
		log.debug("REST request to search Universities for query {}", query);
		return StreamSupport.stream(universitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
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
	@GetMapping("/_search/universitiesBySuggest")
	@Timed
	public String searchUniversityBySuggest(@RequestParam String query) {
		log.debug("REST request to search University for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("university-suggest").text(query).field("suggest");
		SuggestResponse suggestResponse = elasticsearchTemplate.suggest(completionSuggestionBuilder,
				com.drishika.gradzcircle.domain.elastic.University.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("university-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> universities = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			universities.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
		});
		try {
			suggest = objectMapper.writeValueAsString(universities);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
