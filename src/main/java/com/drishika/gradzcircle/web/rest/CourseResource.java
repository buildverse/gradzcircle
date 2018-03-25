package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.search.CourseSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
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
     * POST  /courses : Create a new course.
     *
     * @param course the course to create
     * @return the ResponseEntity with status 201 (Created) and with body the new course, or with status 400 (Bad Request) if the course has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courses")
    @Timed
    public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new course cannot already have an ID"))
                    .body(null);
        }
        Course result = courseRepository.save(course);
        //courseSearchRepository.save(result);
        elasticsearchTemplate.index(new CourseEntityBuilder(course.getId()).name(course.getCourse())
                .suggest(new String[] { course.getCourse() }).buildIndex());
        elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /courses : Updates an existing course.
     *
     * @param course the course to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated course,
     * or with status 400 (Bad Request) if the course is not valid,
     * or with status 500 (Internal Server Error) if the course couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courses")
    @Timed
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getId() == null) {
            return createCourse(course);
        }
        Course result = courseRepository.save(course);
        //  courseSearchRepository.save(result);
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
        elasticsearchTemplate.index(new CourseEntityBuilder(course.getId()).name(course.getCourse())
                .suggest(new String[] { course.getCourse() }).buildIndex());
        elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, course.getId().toString()))
                .body(result);
    }

    /**
     * GET  /courses : get all the courses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of courses in body
     */
    @GetMapping("/courses")
    @Timed
    public List<Course> getAllCourses() {
        log.debug("REST request to get all Courses");
        return courseRepository.findAll();
    }

    /**
     * GET  /courses/:id : get the "id" course.
     *
     * @param id the id of the course to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the course, or with status 404 (Not Found)
     */
    @GetMapping("/courses/{id}")
    @Timed
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Course course = courseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(course));
    }

    /**
     * DELETE  /courses/:id : delete the "id" course.
     *
     * @param id the id of the course to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseRepository.delete(id);
        courseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/courses?query=:query : search for the course corresponding
     * to the query.
     *
     * @param query the query of the course search
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
    * SEARCH  /_search/colleges?query=:query : search for the college corresponding
    * to the query.
    *
    * @param query the query of the college search
    * @return the result of the search
    */
    @GetMapping("/_search/coursesBySuggest")
    @Timed
    public String searchCoursesBySuggest(@RequestParam String query) {
        log.debug("REST request to search Courses for query {}", query);
        String suggest = null;
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
                .completionSuggestion("course-suggest").text(query).field("suggest");
        SuggestResponse suggestResponse = elasticsearchTemplate.suggest(completionSuggestionBuilder,
                com.drishika.gradzcircle.domain.elastic.Course.class);
        CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("course-suggest");
        List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
        List<GenericElasticSuggest> courses = new ArrayList<GenericElasticSuggest>();
        ObjectMapper objectMapper = new ObjectMapper();
        options.forEach(option -> {
            courses.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
            //colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
        });
        try {
            suggest = objectMapper.writeValueAsString(courses);
        } catch (JsonProcessingException e) {
            log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
        }
        return suggest;
    }

}
