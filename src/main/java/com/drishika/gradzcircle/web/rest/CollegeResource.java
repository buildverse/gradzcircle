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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.search.CollegeSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.drishika.gradzcircle.web.rest.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing College.
 */
@RestController
@RequestMapping("/api")
public class CollegeResource {

	private final Logger log = LoggerFactory.getLogger(CollegeResource.class);

	private static final String ENTITY_NAME = "college";

	private final CollegeRepository collegeRepository;

	private final CollegeSearchRepository collegeSearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public CollegeResource(CollegeRepository collegeRepository, CollegeSearchRepository collegeSearchRepository,
			ElasticsearchTemplate elasticsearchTemplate) {
		this.collegeRepository = collegeRepository;
		this.collegeSearchRepository = collegeSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /colleges : Create a new college.
	 *
	 * @param college
	 *            the college to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         college, or with status 400 (Bad Request) if the college has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/colleges")
	@Timed
	public ResponseEntity<College> createCollege(@RequestBody College college) throws URISyntaxException {
		log.debug("REST request to save College : {}", college);
		if (college.getId() != null) {
            throw new BadRequestAlertException("A new college cannot already have an ID", ENTITY_NAME, "idexists");
        }
		College result = collegeRepository.save(college);
		// collegeSearchRepository.save(result);

		elasticsearchTemplate.index(new CollegeEntityBuilder(result.getId()).name(result.getCollegeName())
				.suggest(new String[] { result.getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		return ResponseEntity.created(new URI("/api/colleges/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /colleges : Updates an existing college.
	 * 
	 * @param college
	 *            the college to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         college, or with status 400 (Bad Request) if the college is not
	 *         valid, or with status 500 (Internal Server Error) if the college
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */

	@PutMapping("/colleges")
	@Timed
	public ResponseEntity<College> updateCollege(@RequestBody College college) throws URISyntaxException {
		log.debug("REST request to update College : {}", college);
		if (college.getId() == null) {
			return createCollege(college);
		}
		College result = collegeRepository.save(college);
		com.drishika.gradzcircle.domain.elastic.College collegeElasticInstance = new com.drishika.gradzcircle.domain.elastic.College();
		try {
			BeanUtils.copyProperties(collegeElasticInstance, result);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(
				new CollegeEntityBuilder(collegeElasticInstance.getId()).name(collegeElasticInstance.getCollegeName())
						.suggest(new String[] { collegeElasticInstance.getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, college.getId().toString()))
				.body(result);
	}

	/**
	 * GET /colleges : get all the colleges.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of colleges in
	 *         body
	 */
	@GetMapping("/colleges")
	@Timed
	public ResponseEntity<List<College>> getAllColleges(@ApiParam Pageable pageable) {
		log.debug("REST request to get all Colleges");
		Page<College> college =  collegeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(college, "/api/colleges");
		return new ResponseEntity<>(college.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /colleges/:id : get the "id" college.
	 *
	 * @param id
	 *            the id of the college to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the college, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/colleges/{id}")
	@Timed
	public ResponseEntity<College> getCollege(@PathVariable Long id) {
		log.debug("REST request to get College : {}", id);
		College college = collegeRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(college));
	}

	/**
	 * DELETE /colleges/:id : delete the "id" college.
	 *
	 * @param id
	 *            the id of the college to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/colleges/{id}")
	@Timed
	public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
		log.debug("REST request to delete College : {}", id);
		collegeRepository.delete(id);
		collegeSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/colleges?query=:query : search for the college corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the college search
	 * @return the result of the search
	 */
	@GetMapping("/_search/collegesBySuggest")
	@Timed
	public String searchCollegesBySuggest(@RequestParam String query) {
		log.debug("REST request to search Colleges for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("college-suggest").text(query).field("suggest");
		SuggestResponse suggestResponse = elasticsearchTemplate.suggest(completionSuggestionBuilder,
				com.drishika.gradzcircle.domain.elastic.College.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("college-suggest");
		log.info("Suggestion is {}",completionSuggestion.getEntries());
		List<CompletionSuggestion.Entry.Option> options = null;
		if(completionSuggestion.getEntries() != null)
			options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> colleges = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		if(options != null) {
			options.forEach(option -> {
				colleges.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
				// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
			});
		} else {
			colleges.add(new GenericElasticSuggest("Other", "Other"));
		}
		try {
			suggest = objectMapper.writeValueAsString(colleges);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

	/**
	 * SEARCH /_search/colleges?query=:query : search for the college corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the college search
	 * @return the result of the search
	 */
	@GetMapping("/_search/colleges")
	@Timed
	public List<com.drishika.gradzcircle.domain.elastic.College> searchColleges(@RequestParam String query) {
		log.debug("REST request to search Colleges for query {}", query);
		return StreamSupport.stream(collegeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
