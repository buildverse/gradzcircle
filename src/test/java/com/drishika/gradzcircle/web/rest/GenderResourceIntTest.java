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
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.entitybuilders.GenderEntityBuilder;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.search.GenderSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the GenderResource REST controller.
 *
 * @see GenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class GenderResourceIntTest {

	private static final String DEFAULT_GENDER = "AAAAAAAAAA";
	private static final String UPDATED_GENDER = "BBBBBBBBBB";

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private GenderSearchRepository genderSearchRepository;

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

	private MockMvc restGenderMockMvc;

	private Gender gender;

	private com.drishika.gradzcircle.domain.elastic.Gender elasticGender;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final GenderResource genderResource = new GenderResource(genderRepository, genderSearchRepository,
				elasticsearchTemplate);
		this.restGenderMockMvc = MockMvcBuilders.standaloneSetup(genderResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Gender createEntity(EntityManager em) {
		Gender gender = new Gender().gender(DEFAULT_GENDER);
		return gender;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.Gender createElasticInstance(Gender gender) {
		com.drishika.gradzcircle.domain.elastic.Gender elasticGender = new com.drishika.gradzcircle.domain.elastic.Gender();
		elasticGender.setId(gender.getId());
		elasticGender.gender(gender.getGender());
		return elasticGender;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static GenderEntityBuilder createEntityBuilder(Gender gender) {
		GenderEntityBuilder entityBuilder = new GenderEntityBuilder(gender.getId());
		entityBuilder.name(gender.getGender());
		return entityBuilder;
	}

	@Before
	public void initTest() {
		genderSearchRepository.deleteAll();
		gender = createEntity(em);
	}

	@Test
	@Transactional
	public void createGender() throws Exception {
		int databaseSizeBeforeCreate = genderRepository.findAll().size();

		// Create the Gender
		restGenderMockMvc.perform(post("/api/genders").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gender))).andExpect(status().isCreated());

		// Validate the Gender in the database
		List<Gender> genderList = genderRepository.findAll();
		assertThat(genderList).hasSize(databaseSizeBeforeCreate + 1);
		Gender testGender = genderList.get(genderList.size() - 1);
		assertThat(testGender.getGender()).isEqualTo(DEFAULT_GENDER);

		// Validate the Gender in Elasticsearch
		Gender genderEs = genderSearchRepository.findOne(testGender.getId());
		assertThat(genderEs.getId()).isEqualTo(testGender.getId());
		assertThat(genderEs.getGender()).isEqualTo(testGender.getGender());
	}

	@Test
	@Transactional
	public void createGenderWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = genderRepository.findAll().size();

		// Create the Gender with an existing ID
		gender.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restGenderMockMvc.perform(post("/api/genders").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gender))).andExpect(status().isBadRequest());

		// Validate the Gender in the database
		List<Gender> genderList = genderRepository.findAll();
		assertThat(genderList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllGenders() throws Exception {
		// Initialize the database
		genderRepository.saveAndFlush(gender);

		// Get all the genderList
		restGenderMockMvc.perform(get("/api/genders?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
				.andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
	}

	@Test
	@Transactional
	public void getGender() throws Exception {
		// Initialize the database
		genderRepository.saveAndFlush(gender);

		// Get the gender
		restGenderMockMvc.perform(get("/api/genders/{id}", gender.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(gender.getId().intValue()))
				.andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingGender() throws Exception {
		// Get the gender
		restGenderMockMvc.perform(get("/api/genders/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateGender() throws Exception {
		// Initialize the database
		genderRepository.saveAndFlush(gender);
		elasticsearchTemplate.index(createEntityBuilder(gender)
				.suggest(new String[] { createElasticInstance(gender).getGender() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);

		int databaseSizeBeforeUpdate = genderRepository.findAll().size();

		// Update the gender
		Gender updatedGender = genderRepository.findOne(gender.getId());
		updatedGender.gender(UPDATED_GENDER);

		restGenderMockMvc.perform(put("/api/genders").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedGender))).andExpect(status().isOk());

		// Validate the Gender in the database
		List<Gender> genderList = genderRepository.findAll();
		assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
		Gender testGender = genderList.get(genderList.size() - 1);
		assertThat(testGender.getGender()).isEqualTo(UPDATED_GENDER);

		// Validate the Gender in Elasticsearch
		Gender genderEs = genderSearchRepository.findOne(testGender.getId());
		assertThat(genderEs.getId()).isEqualTo(testGender.getId());
		assertThat(genderEs.getGender()).isEqualTo(testGender.getGender());
	}

	@Test
	@Transactional
	public void updateNonExistingGender() throws Exception {
		int databaseSizeBeforeUpdate = genderRepository.findAll().size();

		// Create the Gender

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restGenderMockMvc.perform(put("/api/genders").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gender))).andExpect(status().isCreated());

		// Validate the Gender in the database
		List<Gender> genderList = genderRepository.findAll();
		assertThat(genderList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteGender() throws Exception {
		// Initialize the database
		genderRepository.saveAndFlush(gender);
		elasticsearchTemplate.index(createEntityBuilder(gender)
				.suggest(new String[] { createElasticInstance(gender).getGender() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);
		int databaseSizeBeforeDelete = genderRepository.findAll().size();

		// Get the gender
		restGenderMockMvc.perform(delete("/api/genders/{id}", gender.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean genderExistsInEs = genderSearchRepository.exists(gender.getId());
		assertThat(genderExistsInEs).isFalse();

		// Validate the database is empty
		List<Gender> genderList = genderRepository.findAll();
		assertThat(genderList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchGender() throws Exception {
		// Initialize the database
		genderRepository.saveAndFlush(gender);
		elasticsearchTemplate.index(createEntityBuilder(gender)
				.suggest(new String[] { createElasticInstance(gender).getGender() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);

		// Search the gender
		restGenderMockMvc.perform(get("/api/_search/genders?query=id:" + gender.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
				.andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Gender.class);
		Gender gender1 = new Gender();
		gender1.setId(1L);
		Gender gender2 = new Gender();
		gender2.setId(gender1.getId());
		assertThat(gender1).isEqualTo(gender2);
		gender2.setId(2L);
		assertThat(gender1).isNotEqualTo(gender2);
		gender1.setId(null);
		assertThat(gender1).isNotEqualTo(gender2);
	}
}
