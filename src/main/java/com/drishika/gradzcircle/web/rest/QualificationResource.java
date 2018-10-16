package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Qualification.
 */
@RestController
@RequestMapping("/api")
public class QualificationResource {

	private final Logger log = LoggerFactory.getLogger(QualificationResource.class);

	private static final String ENTITY_NAME = "qualification";

	private final QualificationRepository qualificationRepository;

	private final QualificationSearchRepository qualificationSearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public QualificationResource(QualificationRepository qualificationRepository,
			QualificationSearchRepository qualificationSearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
		this.qualificationRepository = qualificationRepository;
		this.qualificationSearchRepository = qualificationSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /qualifications : Create a new qualification.
	 *
	 * @param qualification
	 *            the qualification to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         qualification, or with status 400 (Bad Request) if the qualification
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/qualifications")
	@Timed
	public ResponseEntity<Qualification> createQualification(@RequestBody Qualification qualification)
			throws URISyntaxException {
		log.debug("REST request to save Qualification : {}", qualification);
		if (qualification.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new qualification cannot already have an ID")).body(null);
		}
		Qualification result = qualificationRepository.save(qualification);
		// qualificationSearchRepository.save(result);
		elasticsearchTemplate.index(new QualificationEntityBuilder(result.getId()).name(result.getQualification())
				.suggest(new String[] { result.getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
		return ResponseEntity.created(new URI("/api/qualifications/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /qualifications : Updates an existing qualification.
	 *
	 * @param qualification
	 *            the qualification to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         qualification, or with status 400 (Bad Request) if the qualification
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         qualification couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/qualifications")
	@Timed
	public ResponseEntity<Qualification> updateQualification(@RequestBody Qualification qualification)
			throws URISyntaxException {
		log.debug("REST request to update Qualification : {}", qualification);
		if (qualification.getId() == null) {
			return createQualification(qualification);
		}
		Qualification result = qualificationRepository.save(qualification);
		// qualificationSearchRepository.save(result);
		com.drishika.gradzcircle.domain.elastic.Qualification qualificationElasticInstance = new com.drishika.gradzcircle.domain.elastic.Qualification();
		try {
			BeanUtils.copyProperties(qualificationElasticInstance, qualification);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new QualificationEntityBuilder(result.getId()).name(result.getQualification())
				.suggest(new String[] { result.getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualification.getId().toString()))
				.body(result);
	}

	/**
	 * GET /qualifications : get all the qualifications.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         qualifications in body
	 */
	@GetMapping("/qualifications")
	@Timed
	public List<Qualification> getAllQualifications() {
		log.debug("REST request to get all Qualifications");
		return qualificationRepository.findAll();
	}

	/**
	 * GET /qualifications/:id : get the "id" qualification.
	 *
	 * @param id
	 *            the id of the qualification to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         qualification, or with status 404 (Not Found)
	 */
	@GetMapping("/qualifications/{id}")
	@Timed
	public ResponseEntity<Qualification> getQualification(@PathVariable Long id) {
		log.debug("REST request to get Qualification : {}", id);
		Qualification qualification = qualificationRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualification));
	}

	/**
	 * DELETE /qualifications/:id : delete the "id" qualification.
	 *
	 * @param id
	 *            the id of the qualification to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/qualifications/{id}")
	@Timed
	public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
		log.debug("REST request to delete Qualification : {}", id);
		qualificationRepository.delete(id);
		qualificationSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/qualifications?query=:query : search for the qualification
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the qualification search
	 * @return the result of the search
	 */
	@GetMapping("/_search/qualifications")
	@Timed
	public List<Qualification> searchQualifications(@RequestParam String query) {
		log.debug("REST request to search Qualifications for query {}", query);
		return StreamSupport.stream(qualificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
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
	@GetMapping("/_search/qualificationsBySuggest")
	@Timed
	public String searchQualificationsBySuggest(@RequestParam String query) {
		log.debug("REST request to search qualifications for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("qualification-suggest").text(query).field("suggest");
		SuggestResponse suggestResponse = elasticsearchTemplate.suggest(completionSuggestionBuilder,
				com.drishika.gradzcircle.domain.elastic.Qualification.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("qualification-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> qualifications = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			qualifications.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(qualifications);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
