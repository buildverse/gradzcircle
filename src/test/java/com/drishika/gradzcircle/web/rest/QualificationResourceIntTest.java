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
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class QualificationResourceIntTest {

	private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
	private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

	@Autowired
	private QualificationRepository qualificationRepository;

	@Autowired
	private QualificationSearchRepository qualificationSearchRepository;

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

	private MockMvc restQualificationMockMvc;

	private Qualification qualification;

	private com.drishika.gradzcircle.domain.elastic.Qualification elasticQualification;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final QualificationResource qualificationResource = new QualificationResource(qualificationRepository,
				qualificationSearchRepository, elasticsearchTemplate);
		this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Qualification createEntity(EntityManager em) {
		Qualification qualification = new Qualification().qualification(DEFAULT_QUALIFICATION);
		return qualification;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static QualificationEntityBuilder createEntityBuilder(Qualification qualification) {
		QualificationEntityBuilder entityBuilder = new QualificationEntityBuilder(qualification.getId());
		entityBuilder.name(qualification.getQualification());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.Qualification createElasticInstance(
			Qualification qualification) {
		com.drishika.gradzcircle.domain.elastic.Qualification elasticQualification = new com.drishika.gradzcircle.domain.elastic.Qualification();
		elasticQualification.qualification(qualification.getQualification());
		return elasticQualification;
	}

	@Before
	public void initTest() {
		qualificationSearchRepository.deleteAll();
		qualification = createEntity(em);
	}

	@Test
	@Transactional
	public void createQualification() throws Exception {
		int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

		// Create the Qualification
		restQualificationMockMvc.perform(post("/api/qualifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(qualification))).andExpect(status().isCreated());

		// Validate the Qualification in the database
		List<Qualification> qualificationList = qualificationRepository.findAll();
		assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
		Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
		assertThat(testQualification.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);

		// Validate the Qualification in Elasticsearch
		Qualification qualificationEs = qualificationSearchRepository.findOne(testQualification.getId());
		assertThat(qualificationEs.getId()).isEqualTo(testQualification.getId());
		assertThat(qualificationEs.getQualification()).isEqualTo(testQualification.getQualification());
	}

	@Test
	@Transactional
	public void createQualificationWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

		// Create the Qualification with an existing ID
		qualification.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restQualificationMockMvc.perform(post("/api/qualifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(qualification))).andExpect(status().isBadRequest());

		// Validate the Qualification in the database
		List<Qualification> qualificationList = qualificationRepository.findAll();
		assertThat(qualificationList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllQualifications() throws Exception {
		// Initialize the database
		qualificationRepository.saveAndFlush(qualification);

		// Get all the qualificationList
		restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
				.andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())));
	}

	@Test
	@Transactional
	public void getQualification() throws Exception {
		// Initialize the database
		qualificationRepository.saveAndFlush(qualification);

		// Get the qualification
		restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
				.andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingQualification() throws Exception {
		// Get the qualification
		restQualificationMockMvc.perform(get("/api/qualifications/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateQualification() throws Exception {
		// Initialize the database
		qualificationRepository.saveAndFlush(qualification);
		elasticsearchTemplate.index(createEntityBuilder(qualification)
				.suggest(new String[] { createElasticInstance(qualification).getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
		int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

		// Update the qualification
		Qualification updatedQualification = qualificationRepository.findOne(qualification.getId());
		updatedQualification.qualification(UPDATED_QUALIFICATION);

		restQualificationMockMvc.perform(put("/api/qualifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedQualification))).andExpect(status().isOk());

		// Validate the Qualification in the database
		List<Qualification> qualificationList = qualificationRepository.findAll();
		assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
		Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
		assertThat(testQualification.getQualification()).isEqualTo(UPDATED_QUALIFICATION);

		// Validate the Qualification in Elasticsearch
		Qualification qualificationEs = qualificationSearchRepository.findOne(testQualification.getId());
		assertThat(qualificationEs.getId()).isEqualTo(testQualification.getId());
		assertThat(qualificationEs.getQualification()).isEqualTo(testQualification.getQualification());
	}

	@Test
	@Transactional
	public void updateNonExistingQualification() throws Exception {
		int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

		// Create the Qualification

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restQualificationMockMvc.perform(put("/api/qualifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(qualification))).andExpect(status().isCreated());

		// Validate the Qualification in the database
		List<Qualification> qualificationList = qualificationRepository.findAll();
		assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteQualification() throws Exception {
		// Initialize the database
		qualificationRepository.saveAndFlush(qualification);
		elasticsearchTemplate.index(createEntityBuilder(qualification)
				.suggest(new String[] { createElasticInstance(qualification).getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
		int databaseSizeBeforeDelete = qualificationRepository.findAll().size();

		// Get the qualification
		restQualificationMockMvc.perform(
				delete("/api/qualifications/{id}", qualification.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean qualificationExistsInEs = qualificationSearchRepository.exists(qualification.getId());
		assertThat(qualificationExistsInEs).isFalse();

		// Validate the database is empty
		List<Qualification> qualificationList = qualificationRepository.findAll();
		assertThat(qualificationList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchQualification() throws Exception {
		// Initialize the database
		qualificationRepository.saveAndFlush(qualification);
		elasticsearchTemplate.index(createEntityBuilder(qualification)
				.suggest(new String[] { createElasticInstance(qualification).getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);

		// Search the qualification
		restQualificationMockMvc.perform(get("/api/_search/qualifications?query=id:" + qualification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
				.andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Qualification.class);
		Qualification qualification1 = new Qualification();
		qualification1.setId(1L);
		Qualification qualification2 = new Qualification();
		qualification2.setId(qualification1.getId());
		assertThat(qualification1).isEqualTo(qualification2);
		qualification2.setId(2L);
		assertThat(qualification1).isNotEqualTo(qualification2);
		qualification1.setId(null);
		assertThat(qualification1).isNotEqualTo(qualification2);
	}
}
