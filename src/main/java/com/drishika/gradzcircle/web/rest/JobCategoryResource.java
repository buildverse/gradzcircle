package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
import com.drishika.gradzcircle.domain.JobCategory;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.JobCategoryEntityBuilder;
import com.drishika.gradzcircle.repository.JobCategoryRepository;
import com.drishika.gradzcircle.repository.search.JobCategorySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing JobCategory.
 */
@RestController
@RequestMapping("/api")
public class JobCategoryResource {

    private final Logger log = LoggerFactory.getLogger(JobCategoryResource.class);

    private static final String ENTITY_NAME = "jobCategory";

    private final JobCategoryRepository jobCategoryRepository;

    private final JobCategorySearchRepository jobCategorySearchRepository;
    
    private final ElasticsearchTemplate elasticSearchTemplate;

    public JobCategoryResource(JobCategoryRepository jobCategoryRepository, JobCategorySearchRepository jobCategorySearchRepository,ElasticsearchTemplate elasticSearchTemplate) {
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobCategorySearchRepository = jobCategorySearchRepository;
        this.elasticSearchTemplate = elasticSearchTemplate;
    }

    /**
     * POST  /job-categories : Create a new jobCategory.
     *
     * @param jobCategory the jobCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobCategory, or with status 400 (Bad Request) if the jobCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to save JobCategory : {}", jobCategory);
        if (jobCategory.getId() != null) {
            throw new BadRequestAlertException("A new jobCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobCategory result = jobCategoryRepository.save(jobCategory);
        elasticSearchTemplate.index(new JobCategoryEntityBuilder(result.getId()).name(result.getJobCategory())
				.suggest(new String[] { result.getJobCategory() }).buildIndex());
        elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.JobCategory.class);
        return ResponseEntity.created(new URI("/api/job-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-categories : Updates an existing jobCategory.
     *
     * @param jobCategory the jobCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobCategory,
     * or with status 400 (Bad Request) if the jobCategory is not valid,
     * or with status 500 (Internal Server Error) if the jobCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> updateJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to update JobCategory : {}", jobCategory);
        if (jobCategory.getId() == null) {
        		throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobCategory result = jobCategoryRepository.save(jobCategory);
        elasticSearchTemplate.index(new JobCategoryEntityBuilder(result.getId()).name(result.getJobCategory())
				.suggest(new String[] { result.getJobCategory() }).buildIndex());
        elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.JobCategory.class);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-categories : get all the jobCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobCategories in body
     */
    @GetMapping("/job-categories")
    @Timed
    public List<JobCategory> getAllJobCategories() {
        log.debug("REST request to get all JobCategories");
        return jobCategoryRepository.findAll();
        }

    /**
     * GET  /job-categories/:id : get the "id" jobCategory.
     *
     * @param id the id of the jobCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobCategory, or with status 404 (Not Found)
     */
    @GetMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<JobCategory> getJobCategory(@PathVariable Long id) {
        log.debug("REST request to get JobCategory : {}", id);
        Optional<JobCategory> jobCategory = jobCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobCategory);
    }

    /**
     * DELETE  /job-categories/:id : delete the "id" jobCategory.
     *
     * @param id the id of the jobCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Long id) {
        log.debug("REST request to delete JobCategory : {}", id);
        jobCategoryRepository.deleteById(id);
        jobCategorySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/job-categories?query=:query : search for the jobCategory corresponding
     * to the query.
     *
     * @param query the query of the jobCategory search
     * @return the result of the search
     */
    @GetMapping("/_search/job-categories")
    @Timed
    public List<JobCategory> searchJobCategories(@RequestParam String query) {
        log.debug("REST request to search JobCategories for query {}", query);
        return StreamSupport
            .stream(jobCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
	 * SEARCH /_search/jobCategories?query=:query : search for the college corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the college search
	 * @return the result of the search
	 */
	@GetMapping("/_search/jobCategoriesBySuggest")
	@Timed
	public String searchJobCategoriesBySuggest(@RequestParam String query) {
		log.debug("REST request to search job categories for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("suggest").text(query).prefix(query);
		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("suggest", completionSuggestionBuilder);
		SearchResponse searchResponse = elasticSearchTemplate.suggest(suggestion, com.drishika.gradzcircle.domain.elastic.JobCategory.class);
		CompletionSuggestion completionSuggestion = searchResponse.getSuggest().getSuggestion("suggest");
		
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> jobCategories = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			jobCategories.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(jobCategories);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
