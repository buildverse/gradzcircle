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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.search.CollegeSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CollegeResource REST controller.
 *
 * @see CollegeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CollegeResourceIntTest {

	private static final String DEFAULT_COLLEGE_NAME = "AAAAAAAAAA";
	private static final String UPDATED_COLLEGE_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_DOMAIN_NAME = "AAAAAAAAAA";
	private static final String UPDATED_DOMAIN_NAME = "BBBBBBBBBB";

	private static final Integer DEFAULT_STATUS = 1;
	private static final Integer UPDATED_STATUS = 2;

	@Autowired
	private CollegeRepository collegeRepository;

	@Autowired
	private CollegeSearchRepository collegeSearchRepository;

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

	private MockMvc restCollegeMockMvc;

	private College college;

	private com.drishika.gradzcircle.domain.elastic.College elasticCollege;

	private CollegeEntityBuilder collegeEntityBuilder;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CollegeResource collegeResource = new CollegeResource(collegeRepository, collegeSearchRepository,
				elasticsearchTemplate);
		this.restCollegeMockMvc = MockMvcBuilders.standaloneSetup(collegeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static College createEntity(EntityManager em) {
		College college = new College().collegeName(DEFAULT_COLLEGE_NAME).domainName(DEFAULT_DOMAIN_NAME)
				.status(DEFAULT_STATUS);
		return college;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CollegeEntityBuilder createEntityBuilder(College college) {
		CollegeEntityBuilder entityBuilder = new CollegeEntityBuilder(college.getId());
		entityBuilder.name(college.getCollegeName());
		//entityBuilder.domainName(college.getDomainName());
		//entityBuilder.status(college.getStatus());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.College createElasticInstance(College college) {
		com.drishika.gradzcircle.domain.elastic.College elasticCollege = new com.drishika.gradzcircle.domain.elastic.College();
		elasticCollege.setId(college.getId());
		elasticCollege.collegeName(college.getCollegeName());
		elasticCollege.domainName(college.getDomainName());
		elasticCollege.status(college.getStatus());
		return elasticCollege;
	}

	@Before
	public void initTest() {
		collegeSearchRepository.deleteAll();
		college = createEntity(em);
	}

	@Test
	@Transactional
	public void createCollege() throws Exception {
		int databaseSizeBeforeCreate = collegeRepository.findAll().size();

		// Create the College
		restCollegeMockMvc.perform(post("/api/colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(college))).andExpect(status().isCreated());

		// Validate the College in the database
		List<College> collegeList = collegeRepository.findAll();
		assertThat(collegeList).hasSize(databaseSizeBeforeCreate + 1);
		College testCollege = collegeList.get(collegeList.size() - 1);
		assertThat(testCollege.getCollegeName()).isEqualTo(DEFAULT_COLLEGE_NAME);
		assertThat(testCollege.getDomainName()).isEqualTo(DEFAULT_DOMAIN_NAME);
		assertThat(testCollege.getStatus()).isEqualTo(DEFAULT_STATUS);

		// Validate the College in Elasticsearch
		com.drishika.gradzcircle.domain.elastic.College collegeEs = collegeSearchRepository
				.findOne(testCollege.getId());
		// assertThat(collegeEs).isEqualToComparingFieldByField(testCollege);
		assertThat(collegeEs.getId()).isEqualTo(testCollege.getId());
		assertThat(collegeEs.getCollegeName()).isEqualTo(testCollege.getCollegeName());

	}

	@Test
	@Transactional
	public void createCollegeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = collegeRepository.findAll().size();

		// Create the College with an existing ID
		college.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCollegeMockMvc.perform(post("/api/colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(college))).andExpect(status().isBadRequest());

		// Validate the College in the database
		List<College> collegeList = collegeRepository.findAll();
		assertThat(collegeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllColleges() throws Exception {
		// Initialize the database
		collegeRepository.saveAndFlush(college);

		// Get all the collegeList
		restCollegeMockMvc.perform(get("/api/colleges?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(college.getId().intValue())))
				.andExpect(jsonPath("$.[*].collegeName").value(hasItem(DEFAULT_COLLEGE_NAME.toString())))
				.andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
	}

	@Test
	@Transactional
	public void getCollege() throws Exception {
		// Initialize the database
		collegeRepository.saveAndFlush(college);

		// Get the college
		restCollegeMockMvc.perform(get("/api/colleges/{id}", college.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(college.getId().intValue()))
				.andExpect(jsonPath("$.collegeName").value(DEFAULT_COLLEGE_NAME.toString()))
				.andExpect(jsonPath("$.domainName").value(DEFAULT_DOMAIN_NAME.toString()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
	}

	@Test
	@Transactional
	public void getNonExistingCollege() throws Exception {
		// Get the college
		restCollegeMockMvc.perform(get("/api/colleges/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	// TODO - fix elastic search template test. I have commented
	// collegeSearchRepository.save(college);- needs to be fixed.
	@Test
	@Transactional
	public void updateCollege() throws Exception {
		// Initialize the database
		collegeRepository.saveAndFlush(college);
		elasticsearchTemplate.index(createEntityBuilder(college)
				.suggest(new String[] { createElasticInstance(college).getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

		// Update the college
		College updatedCollege = collegeRepository.findOne(college.getId());
		updatedCollege.collegeName(UPDATED_COLLEGE_NAME).domainName(UPDATED_DOMAIN_NAME).status(UPDATED_STATUS);

		restCollegeMockMvc.perform(put("/api/colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCollege))).andExpect(status().isOk());

		// Validate the College in the database
		List<College> collegeList = collegeRepository.findAll();
		assertThat(collegeList).hasSize(databaseSizeBeforeUpdate);
		College testCollege = collegeList.get(collegeList.size() - 1);
		assertThat(testCollege.getCollegeName()).isEqualTo(UPDATED_COLLEGE_NAME);
		assertThat(testCollege.getDomainName()).isEqualTo(UPDATED_DOMAIN_NAME);
		assertThat(testCollege.getStatus()).isEqualTo(UPDATED_STATUS);

		// Validate the College in Elasticsearch
		com.drishika.gradzcircle.domain.elastic.College collegeEs = collegeSearchRepository
				.findOne(testCollege.getId());
		// assertThat(collegeEs).isEqualToComparingFieldByField(testCollege);
		assertThat(collegeEs.getId()).isEqualTo(testCollege.getId());
		assertThat(collegeEs.getCollegeName()).isEqualTo(testCollege.getCollegeName());
	}

	@Test
	@Transactional
	public void updateNonExistingCollege() throws Exception {
		int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

		// Create the College

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCollegeMockMvc.perform(put("/api/colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(college))).andExpect(status().isCreated());

		// Validate the College in the database
		List<College> collegeList = collegeRepository.findAll();
		assertThat(collegeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	// TODO - fix elastic search template test. I have commented
	// collegeSearchRepository.save(college);- needs to be fixed.
	@Test
	@Transactional
	public void deleteCollege() throws Exception {
		// Initialize the database
		collegeRepository.saveAndFlush(college);
		elasticsearchTemplate.index(createEntityBuilder(college)
				.suggest(new String[] { createElasticInstance(college).getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		int databaseSizeBeforeDelete = collegeRepository.findAll().size();

		// Get the college
		restCollegeMockMvc.perform(delete("/api/colleges/{id}", college.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean collegeExistsInEs = collegeSearchRepository.exists(college.getId());
		assertThat(collegeExistsInEs).isFalse();

		// Validate the database is empty
		List<College> collegeList = collegeRepository.findAll();
		assertThat(collegeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	// TODO - fix elastic search template test. I have commented
	// collegeSearchRepository.save(college);;- needs to be fixed.
	@Test
	@Transactional
	public void searchCollege() throws Exception {
		// Initialize the database
		collegeRepository.saveAndFlush(college);
		elasticsearchTemplate.index(createEntityBuilder(college)
				.suggest(new String[] { createElasticInstance(college).getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		// Search the college
		restCollegeMockMvc.perform(get("/api/_search/colleges?query=id:" + college.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(college.getId().intValue())))
				.andExpect(jsonPath("$.[*].collegeName").value(hasItem(DEFAULT_COLLEGE_NAME.toString())))
				.andExpect(jsonPath("$.[*].domainName").value(hasItem(DEFAULT_DOMAIN_NAME.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(College.class);
		College college1 = new College();
		college1.setId(1L);
		College college2 = new College();
		college2.setId(college1.getId());
		assertThat(college1).isEqualTo(college2);
		college2.setId(2L);
		assertThat(college1).isNotEqualTo(college2);
		college1.setId(null);
		assertThat(college1).isNotEqualTo(college2);
	}
}
