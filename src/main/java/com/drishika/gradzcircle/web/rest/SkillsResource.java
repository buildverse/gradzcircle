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
import org.elasticsearch.search.suggest.SuggestionBuilder;
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
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.SkillsEntityBuilder;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.search.SkillsSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Skills.
 */
@RestController
@RequestMapping("/api")
public class SkillsResource {

    private final Logger log = LoggerFactory.getLogger(SkillsResource.class);

    private static final String ENTITY_NAME = "skills";

    private final SkillsRepository skillsRepository;

    private final SkillsSearchRepository skillsSearchRepository;
    
    private final ElasticsearchTemplate elasticsearchTemplate;

    public SkillsResource(SkillsRepository skillsRepository, SkillsSearchRepository skillsSearchRepository,ElasticsearchTemplate elasticsearchTemplate) {
        this.skillsRepository = skillsRepository;
        this.skillsSearchRepository = skillsSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * POST  /skills : Create a new skills.
     *
     * @param skills the skills to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skills, or with status 400 (Bad Request) if the skills has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skills")
    @Timed
    public ResponseEntity<Skills> createSkills(@RequestBody Skills skills) throws URISyntaxException {
        log.debug("REST request to save Skills : {}", skills);
        if (skills.getId() != null) {
            throw new BadRequestAlertException("A new skills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Skills result = skillsRepository.save(skills);
        elasticsearchTemplate.index(new SkillsEntityBuilder(result.getId()).name(result.getSkill())
				.suggest(new String[] { result.getSkill() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
        return ResponseEntity.created(new URI("/api/skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skills : Updates an existing skills.
     *
     * @param skills the skills to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skills,
     * or with status 400 (Bad Request) if the skills is not valid,
     * or with status 500 (Internal Server Error) if the skills couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skills")
    @Timed
    public ResponseEntity<Skills> updateSkills(@RequestBody Skills skills) throws URISyntaxException {
        log.debug("REST request to update Skills : {}", skills);
        if (skills.getId() == null) {
        		throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Skills result = skillsRepository.save(skills);
        elasticsearchTemplate.index(new SkillsEntityBuilder(result.getId()).name(result.getSkill())
				.suggest(new String[] { result.getSkill() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skills.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skills : get all the skills.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of skills in body
     */
    @GetMapping("/skills")
    @Timed
    public List<Skills> getAllSkills() {
        log.debug("REST request to get all Skills");
        return skillsRepository.findAll();
        }

    /**
     * GET  /skills/:id : get the "id" skills.
     *
     * @param id the id of the skills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skills, or with status 404 (Not Found)
     */
    @GetMapping("/skills/{id}")
    @Timed
    public ResponseEntity<Skills> getSkills(@PathVariable Long id) {
        log.debug("REST request to get Skills : {}", id);
        Optional<Skills> skills = skillsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(skills);
    }
    
    
    /**
     * GET  /skills/:name : get the "name" skills.
     *
     * @param id the id of the skills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skills, or with status 404 (Not Found)
     */
    @GetMapping("/skillsByName/{name}")
    @Timed
    public ResponseEntity<Skills> getSkills(@PathVariable String name) {
        log.debug("REST request to get Skills : {}", name);
        Skills skills = skillsRepository.findBySkillIgnoreCase(name);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(skills));
    }

    /**
     * DELETE  /skills/:id : delete the "id" skills.
     *
     * @param id the id of the skills to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkills(@PathVariable Long id) {
        log.debug("REST request to delete Skills : {}", id);
        skillsRepository.deleteById(id);
        skillsSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/skills?query=:query : search for the skills corresponding
     * to the query.
     *
     * @param query the query of the skills search
     * @return the result of the search
     */
    @GetMapping("/_search/skills")
    @Timed
    public List<Skills> searchSkills(@RequestParam String query) {
        log.debug("REST request to search Skills for query {}", query);
        return StreamSupport
            .stream(skillsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    
    @GetMapping("/_search/skillBySuggest")
	@Timed
	public String searchSkillBySuggest(@RequestParam String query) {
		log.debug("REST request to search skill for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("suggest").text(query).prefix(query);
		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("suggest", completionSuggestionBuilder);
		SearchResponse searchResponse = elasticsearchTemplate.suggest(suggestion, com.drishika.gradzcircle.domain.elastic.Skills.class);
 		CompletionSuggestion completionSuggestion = searchResponse.getSuggest().getSuggestion("suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> skills = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			skills.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(skills);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}
}
