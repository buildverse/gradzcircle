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
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.search.CourseSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

	private final Logger log = LoggerFactory.getLogger(CourseResource.class);

	private static final String ENTITY_NAME = "course";

	private final CourseRepository courseRepository;

	private final CourseSearchRepository courseSearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public CourseResource(CourseRepository courseRepository, CourseSearchRepository courseSearchRepository,
			ElasticsearchTemplate elasticsearchTemplate) {
		this.courseRepository = courseRepository;
		this.courseSearchRepository = courseSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	/**
	 * POST /courses : Create a new course.
	 *
	 * @param course
	 *            the course to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         course, or with status 400 (Bad Request) if the course has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/courses")
	@Timed
	public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
		log.debug("REST request to save Course : {}", course);
		 if (course.getId() != null) {
	            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
	        }
		Course result = courseRepository.save(course);
		// courseSearchRepository.save(result);
		elasticsearchTemplate.index(new CourseEntityBuilder(result.getId()).name(result.getCourse())
				.suggest(new String[] { result.getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /courses : Updates an existing course.
	 *
	 * @param course
	 *            the course to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         course, or with status 400 (Bad Request) if the course is not valid,
	 *         or with status 500 (Internal Server Error) if the course couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/courses")
	@Timed
	public ResponseEntity<Course> updateCourse(@RequestBody Course course) throws URISyntaxException {
		log.debug("REST request to update Course : {}", course);
		if (course.getId() == null) {
			throw new BadRequestAlertException("A new profileCategory cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Course result = courseRepository.save(course);
		// courseSearchRepository.save(result);
		com.drishika.gradzcircle.domain.elastic.Course courseElasticInstance = new com.drishika.gradzcircle.domain.elastic.Course();
		try {
			BeanUtils.copyProperties(courseElasticInstance, course);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			throw new URISyntaxException(e.getMessage(), e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new CourseEntityBuilder(result.getId()).name(result.getCourse())
				.suggest(new String[] { result.getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, course.getId().toString()))
				.body(result);
	}

	/**
	 * GET /courses : get all the courses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of courses in
	 *         body
	 */
	@GetMapping("/courses")
	@Timed
	public List<Course> getAllCourses() {
		log.debug("REST request to get all Courses");
		return courseRepository.findAll();
	}

	/**
	 * GET /courses/:id : get the "id" course.
	 *
	 * @param id
	 *            the id of the course to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the course, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/courses/{id}")
	@Timed
	public ResponseEntity<Course> getCourse(@PathVariable Long id) {
		log.debug("REST request to get Course : {}", id);
		Optional<Course> course = courseRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(course);
	}

	/**
	 * DELETE /courses/:id : delete the "id" course.
	 *
	 * @param id
	 *            the id of the course to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/courses/{id}")
	@Timed
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		log.debug("REST request to delete Course : {}", id);
		courseRepository.deleteById(id);
		courseSearchRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/courses?query=:query : search for the course corresponding to
	 * the query.
	 *
	 * @param query
	 *            the query of the course search
	 * @return the result of the search
	 */
	@GetMapping("/_search/courses")
	@Timed
	public List<Course> searchCourses(@RequestParam String query) {
		log.debug("REST request to search Courses for query {}", query);
		return StreamSupport.stream(courseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
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
	@GetMapping("/_search/coursesBySuggest")
	@Timed
	public String searchCoursesBySuggest(@RequestParam String query) {
		log.debug("REST request to search Courses for query {}", query);
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("suggest").text(query).prefix(query);
		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("suggest", completionSuggestionBuilder);
		SearchResponse searchResponse = elasticsearchTemplate.suggest(suggestion, com.drishika.gradzcircle.domain.elastic.Course.class);
		CompletionSuggestion completionSuggestion = searchResponse.getSuggest().getSuggestion("suggest");
		
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> courses = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			courses.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(courses);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
