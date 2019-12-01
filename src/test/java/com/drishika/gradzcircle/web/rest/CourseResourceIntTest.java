package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.search.CourseSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CourseResource REST controller.
 *
 * @see CourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CourseResourceIntTest {

	private static final String DEFAULT_COURSE = "AAAAAAAAAA";
	private static final String UPDATED_COURSE = "BBBBBBBBBB";

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseSearchRepository mockCourseSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Mock
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private EntityManager em;

	private MockMvc restCourseMockMvc;

	private Course course;

	private com.drishika.gradzcircle.domain.elastic.Course elasticCourse;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CourseResource courseResource = new CourseResource(courseRepository, mockCourseSearchRepository,
				elasticsearchTemplate);
		this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Course createEntity(EntityManager em) {
		Course course = new Course().course(DEFAULT_COURSE);
		return course;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CourseEntityBuilder createEntityBuilder(Course course) {
		CourseEntityBuilder entityBuilder = new CourseEntityBuilder(course.getId());
		entityBuilder.name(course.getCourse());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.Course createElasticInstance(Course course) {
		com.drishika.gradzcircle.domain.elastic.Course elasticCourse = new com.drishika.gradzcircle.domain.elastic.Course();
		elasticCourse.setId(course.getId());
		elasticCourse.course(course.getCourse());
		return elasticCourse;
	}

	@Before
	public void initTest() {
		course = createEntity(em);
		elasticCourse = createElasticInstance(course);
	}

	@Test
	@Transactional
	public void createCourse() throws Exception {
		int databaseSizeBeforeCreate = courseRepository.findAll().size();

		// Create the Course
		restCourseMockMvc.perform(post("/api/courses").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(course))).andExpect(status().isCreated());

		// Validate the Course in the database
		List<Course> courseList = courseRepository.findAll();
		assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
		Course testCourse = courseList.get(courseList.size() - 1);
		assertThat(testCourse.getCourse()).isEqualTo(DEFAULT_COURSE);

		// Validate the Course in Elasticsearch
		verify(elasticsearchTemplate,times(1)).index(any());
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}

	@Test
	@Transactional
	public void createCourseWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = courseRepository.findAll().size();

		// Create the Course with an existing ID
		course.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCourseMockMvc.perform(post("/api/courses").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(course))).andExpect(status().isBadRequest());

		// Validate the Course in the database
		List<Course> courseList = courseRepository.findAll();
		assertThat(courseList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCourses() throws Exception {
		// Initialize the database
		courseRepository.saveAndFlush(course);

		// Get all the courseList
		restCourseMockMvc.perform(get("/api/courses?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
				.andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())));
	}

	@Test
	@Transactional
	public void getCourse() throws Exception {
		// Initialize the database
		courseRepository.saveAndFlush(course);

		// Get the course
		restCourseMockMvc.perform(get("/api/courses/{id}", course.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(course.getId().intValue()))
				.andExpect(jsonPath("$.course").value(DEFAULT_COURSE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingCourse() throws Exception {
		// Get the course
		restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCourse() throws Exception {
		// Initialize the database
		courseRepository.saveAndFlush(course);
		elasticsearchTemplate.index(createEntityBuilder(course)
				.suggest(new String[] { createElasticInstance(course).getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		int databaseSizeBeforeUpdate = courseRepository.findAll().size();

		// Update the course
		Course updatedCourse = courseRepository.findById(course.getId()).get();
		updatedCourse.course(UPDATED_COURSE);

		restCourseMockMvc.perform(put("/api/courses").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCourse))).andExpect(status().isOk());

		// Validate the Course in the database
		List<Course> courseList = courseRepository.findAll();
		assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
		Course testCourse = courseList.get(courseList.size() - 1);
		assertThat(testCourse.getCourse()).isEqualTo(UPDATED_COURSE);

		// Validate the Course in Elasticsearch
		verify(elasticsearchTemplate,times(2)).index(any());
		verify(elasticsearchTemplate,times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}

	@Test
	@Transactional
	public void updateNonExistingCourse() throws Exception {
		int databaseSizeBeforeUpdate = courseRepository.findAll().size();

		// Create the Course

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCourseMockMvc.perform(put("/api/courses")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(course)))
	            .andExpect(status().isBadRequest());

		// Validate the Course in the database
		List<Course> courseList = courseRepository.findAll();
		assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
		verify(elasticsearchTemplate,times(0)).index(any());
		verify(elasticsearchTemplate,times(0)).refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}

	@Test
	@Transactional
	public void deleteCourse() throws Exception {
		// Initialize the database
		courseRepository.saveAndFlush(course);
		elasticsearchTemplate.index(createEntityBuilder(course)
				.suggest(new String[] { createElasticInstance(course).getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		int databaseSizeBeforeDelete = courseRepository.findAll().size();

		// Get the course
		restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		verify(elasticsearchTemplate,times(1)).index(any());
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Course.class);

		// Validate the database is empty
		List<Course> courseList = courseRepository.findAll();
		assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCourse() throws Exception {
		// Initialize the database
		courseRepository.saveAndFlush(course);
		elasticsearchTemplate.index(createEntityBuilder(course)
				.suggest(new String[] { createElasticInstance(course).getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);

		elasticCourse.setId(course.getId());
		when(mockCourseSearchRepository.search(queryStringQuery("id:" + course.getId())))
	        .thenReturn(Collections.singletonList(elasticCourse));

		// Search the course
		restCourseMockMvc.perform(get("/api/_search/courses?query=id:" + course.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
				.andExpect(jsonPath("$.[*].course").value(hasItem(DEFAULT_COURSE.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Course.class);
		Course course1 = new Course();
		course1.setId(1L);
		Course course2 = new Course();
		course2.setId(course1.getId());
		assertThat(course1).isEqualTo(course2);
		course2.setId(2L);
		assertThat(course1).isNotEqualTo(course2);
		course1.setId(null);
		assertThat(course1).isNotEqualTo(course2);
	}
}
