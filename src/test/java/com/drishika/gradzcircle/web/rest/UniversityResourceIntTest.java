package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the UniversityResource REST controller.
 *
 * @see UniversityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class UniversityResourceIntTest {

	private static final String DEFAULT_UNIVERSITY_NAME = "AAAAAAAAAA";
	private static final String UPDATED_UNIVERSITY_NAME = "BBBBBBBBBB";

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private UniversitySearchRepository universitySearchRepository;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restUniversityMockMvc;

	private University university;

	private com.drishika.gradzcircle.domain.elastic.University elasticUniversity;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final UniversityResource universityResource = new UniversityResource(universityRepository,
				universitySearchRepository, elasticsearchTemplate);
		this.restUniversityMockMvc = MockMvcBuilders.standaloneSetup(universityResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static University createEntity(EntityManager em) {
		University university = new University().universityName(DEFAULT_UNIVERSITY_NAME);
		return university;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static UniversityEntityBuilder createEntityBuilder(University university) {
		UniversityEntityBuilder entityBuilder = new UniversityEntityBuilder(university.getId());
		entityBuilder.name(university.getUniversityName());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.University createElasticInstance(University university) {
		com.drishika.gradzcircle.domain.elastic.University elasticUniversity = new com.drishika.gradzcircle.domain.elastic.University();
		elasticUniversity.universityName(university.getUniversityName());
		return elasticUniversity;
	}

	@Before
	public void initTest() {
		universitySearchRepository.deleteAll();
		university = createEntity(em);
		elasticUniversity = createElasticInstance(university);
	}

	@Test
	@Transactional
	public void createUniversity() throws Exception {
		int databaseSizeBeforeCreate = universityRepository.findAll().size();

		// Create the University
		restUniversityMockMvc.perform(post("/api/universities").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(university))).andExpect(status().isCreated());

		// Validate the University in the database
		List<University> universityList = universityRepository.findAll();
		assertThat(universityList).hasSize(databaseSizeBeforeCreate + 1);
		University testUniversity = universityList.get(universityList.size() - 1);
		assertThat(testUniversity.getUniversityName()).isEqualTo(DEFAULT_UNIVERSITY_NAME);

		// Validate the University in Elasticsearch
		University universityEs = universitySearchRepository.findOne(testUniversity.getId());
		assertThat(universityEs.getId()).isEqualTo(testUniversity.getId());
		assertThat(universityEs.getUniversityName()).isEqualTo(testUniversity.getUniversityName());

	}

	@Test
	@Transactional
	public void createUniversityWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = universityRepository.findAll().size();

		// Create the University with an existing ID
		university.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restUniversityMockMvc.perform(post("/api/universities").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(university))).andExpect(status().isBadRequest());

		// Validate the University in the database
		List<University> universityList = universityRepository.findAll();
		assertThat(universityList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllUniversities() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);

		// Get all the universityList
		restUniversityMockMvc.perform(get("/api/universities?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(university.getId().intValue())))
				.andExpect(jsonPath("$.[*].universityName").value(hasItem(DEFAULT_UNIVERSITY_NAME.toString())));
	}

	@Test
	@Transactional
	public void getUniversity() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);

		// Get the university
		restUniversityMockMvc.perform(get("/api/universities/{id}", university.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(university.getId().intValue()))
				.andExpect(jsonPath("$.universityName").value(DEFAULT_UNIVERSITY_NAME.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingUniversity() throws Exception {
		// Get the university
		restUniversityMockMvc.perform(get("/api/universities/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateUniversity() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		elasticsearchTemplate.index(createEntityBuilder(university)
				.suggest(new String[] { createElasticInstance(university).getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
		int databaseSizeBeforeUpdate = universityRepository.findAll().size();

		// Update the university
		University updatedUniversity = universityRepository.findOne(university.getId());
		updatedUniversity.universityName(UPDATED_UNIVERSITY_NAME);

		restUniversityMockMvc.perform(put("/api/universities").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedUniversity))).andExpect(status().isOk());

		// Validate the University in the database
		List<University> universityList = universityRepository.findAll();
		assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
		University testUniversity = universityList.get(universityList.size() - 1);
		assertThat(testUniversity.getUniversityName()).isEqualTo(UPDATED_UNIVERSITY_NAME);

		// Validate the University in Elasticsearch
		University universityEs = universitySearchRepository.findOne(testUniversity.getId());
		assertThat(universityEs.getId()).isEqualTo(testUniversity.getId());
		assertThat(universityEs.getUniversityName()).isEqualTo(testUniversity.getUniversityName());
	}

	@Test
	@Transactional
	public void updateNonExistingUniversity() throws Exception {
		int databaseSizeBeforeUpdate = universityRepository.findAll().size();

		// Create the University

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restUniversityMockMvc.perform(put("/api/universities").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(university))).andExpect(status().isCreated());

		// Validate the University in the database
		List<University> universityList = universityRepository.findAll();
		assertThat(universityList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteUniversity() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		elasticsearchTemplate.index(createEntityBuilder(university)
				.suggest(new String[] { createElasticInstance(university).getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
		int databaseSizeBeforeDelete = universityRepository.findAll().size();

		// Get the university
		restUniversityMockMvc
				.perform(delete("/api/universities/{id}", university.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean universityExistsInEs = universitySearchRepository.exists(university.getId());
		assertThat(universityExistsInEs).isFalse();

		// Validate the database is empty
		List<University> universityList = universityRepository.findAll();
		assertThat(universityList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchUniversity() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		elasticsearchTemplate.index(createEntityBuilder(university)
				.suggest(new String[] { createElasticInstance(university).getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);

		// Search the university
		restUniversityMockMvc.perform(get("/api/_search/universities?query=id:" + university.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(university.getId().intValue())))
				.andExpect(jsonPath("$.[*].universityName").value(hasItem(DEFAULT_UNIVERSITY_NAME.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(University.class);
		University university1 = new University();
		university1.setId(1L);
		University university2 = new University();
		university2.setId(university1.getId());
		assertThat(university1).isEqualTo(university2);
		university2.setId(2L);
		assertThat(university1).isNotEqualTo(university2);
		university1.setId(null);
		assertThat(university1).isNotEqualTo(university2);
	}
}
