package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.LanguageEntityBuilder;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.search.LanguageSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

	private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

	private static final String ENTITY_NAME = "language";

	private final LanguageRepository languageRepository;

	private final LanguageSearchRepository languageSearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public LanguageResource(LanguageRepository languageRepository, LanguageSearchRepository languageSearchRepository,
			ElasticsearchTemplate elasticsearchTemplate) {
		this.languageRepository = languageRepository;
		this.languageSearchRepository = languageSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /languages : Create a new language.
	 *
	 * @param language
	 *            the language to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         language, or with status 400 (Bad Request) if the language has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/languages")
	@Timed
	public ResponseEntity<Language> createLanguage(@RequestBody Language language) throws URISyntaxException {
		log.debug("REST request to save Language : {}", language);
		if (language.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new language cannot already have an ID"))
					.body(null);
		}
		Language result = languageRepository.save(language);
		// languageSearchRepository.save(result);
		elasticsearchTemplate.index(new LanguageEntityBuilder(result.getId()).name(result.getLanguage())
				.suggest(new String[] { language.getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /languages : Updates an existing language.
	 *
	 * @param language
	 *            the language to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         language, or with status 400 (Bad Request) if the language is not
	 *         valid, or with status 500 (Internal Server Error) if the language
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */

	@PutMapping("/languages")
	@Timed
	public ResponseEntity<Language> updateLanguage(@RequestBody Language language) throws URISyntaxException {
		log.debug("REST request to update Language : {}", language);
		if (language.getId() == null) {
			return createLanguage(language);
		}
		Language result = languageRepository.save(language);
		// languageSearchRepository.save(result);
		com.drishika.gradzcircle.domain.elastic.Language languageElasticInstance = new com.drishika.gradzcircle.domain.elastic.Language();
		try {
			BeanUtils.copyProperties(languageElasticInstance, language);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new LanguageEntityBuilder(result.getId()).name(result.getLanguage())
				.suggest(new String[] { language.getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, language.getId().toString()))
				.body(result);
	}

	/**
	 * GET /languages : get all the languages.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of languages in
	 *         body
	 */
	@GetMapping("/languages")
	@Timed
	public List<Language> getAllLanguages() {
		log.debug("REST request to get all Languages");
		return languageRepository.findAll();
	}

	/**
	 * GET /languages/:id : get the "id" language.
	 *
	 * @param id
	 *            the id of the language to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the language,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/languages/{id}")
	@Timed
	public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
		log.debug("REST request to get Language : {}", id);
		Language language = languageRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(language));
	}

	/**
	 * DELETE /languages/:id : delete the "id" language.
	 *
	 * @param id
	 *            the id of the language to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/languages/{id}")
	@Timed
	public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
		log.debug("REST request to delete Language : {}", id);
		languageRepository.delete(id);
		languageSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/languages?query=:query : search for the language
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the language search
	 * @return the result of the search
	 */
	@GetMapping("/_search/languages")
	@Timed
	public List<Language> searchLanguages(@RequestParam String query) {
		log.debug("REST request to search Languages for query {}", query);
		return StreamSupport.stream(languageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
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
	@GetMapping("/_search/languagesBySuggest")
	@Timed
	public String searchLanguagesBySuggest(@RequestParam String query) {
		log.debug("REST request to search languages for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("language-suggest").text(query).field("suggest");
		SuggestResponse suggestResponse = elasticsearchTemplate.suggest(completionSuggestionBuilder,
				com.drishika.gradzcircle.domain.elastic.Language.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("language-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> languages = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			languages.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(languages);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
