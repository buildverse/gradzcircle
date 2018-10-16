package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CandidateCertificationResource REST controller.
 *
 * @see CandidateCertificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateCertificationResourceIntTest {

	private static final String DEFAULT_CERTIFICATION_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_CERTIFICATION_TITLE = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_CERTIFICATION_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CERTIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_CERTIFICATION_DETAILS = "AAAAAAAAAA";
	private static final String UPDATED_CERTIFICATION_DETAILS = "BBBBBBBBBB";

	@Autowired
	private CandidateCertificationRepository candidateCertificationRepository;

	@Autowired
	private CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateCertificationMockMvc;

	private CandidateCertification candidateCertification;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateCertificationResource candidateCertificationResource = new CandidateCertificationResource(
				candidateCertificationRepository, candidateCertificationSearchRepository);
		this.restCandidateCertificationMockMvc = MockMvcBuilders.standaloneSetup(candidateCertificationResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateCertification createEntity(EntityManager em) {
		CandidateCertification candidateCertification = new CandidateCertification()
				.certificationTitle(DEFAULT_CERTIFICATION_TITLE).certificationDate(DEFAULT_CERTIFICATION_DATE)
				.certificationDetails(DEFAULT_CERTIFICATION_DETAILS);
		return candidateCertification;
	}

	@Before
	public void initTest() {
		candidateCertificationSearchRepository.deleteAll();
		candidateCertification = createEntity(em);
	}

	@Test
	@Transactional
	public void createCandidateCertification() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();

		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);

		// Validate the CandidateCertification in Elasticsearch
		CandidateCertification candidateCertificationEs = candidateCertificationSearchRepository
				.findOne(testCandidateCertification.getId());
		assertThat(candidateCertificationEs).isEqualToComparingFieldByField(testCandidateCertification);
	}

	@Test
	@Transactional
	public void createCandidateCertificationWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();

		// Create the CandidateCertification with an existing ID
		candidateCertification.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateCertifications() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);

		// Get all the candidateCertificationList
		restCandidateCertificationMockMvc.perform(get("/api/candidate-certifications?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateCertification.getId().intValue())))
				.andExpect(jsonPath("$.[*].certificationTitle").value(hasItem(DEFAULT_CERTIFICATION_TITLE.toString())))
				.andExpect(jsonPath("$.[*].certificationDate").value(hasItem(DEFAULT_CERTIFICATION_DATE.toString())))
				.andExpect(jsonPath("$.[*].certificationDetails")
						.value(hasItem(DEFAULT_CERTIFICATION_DETAILS.toString())));
	}

	@Test
	@Transactional
	public void getCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/candidate-certifications/{id}", candidateCertification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateCertification.getId().intValue()))
				.andExpect(jsonPath("$.certificationTitle").value(DEFAULT_CERTIFICATION_TITLE.toString()))
				.andExpect(jsonPath("$.certificationDate").value(DEFAULT_CERTIFICATION_DATE.toString()))
				.andExpect(jsonPath("$.certificationDetails").value(DEFAULT_CERTIFICATION_DETAILS.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateCertification() throws Exception {
		// Get the candidateCertification
		restCandidateCertificationMockMvc.perform(get("/api/candidate-certifications/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		candidateCertificationSearchRepository.save(candidateCertification);
		int databaseSizeBeforeUpdate = candidateCertificationRepository.findAll().size();

		// Update the candidateCertification
		CandidateCertification updatedCandidateCertification = candidateCertificationRepository
				.findOne(candidateCertification.getId());
		updatedCandidateCertification.certificationTitle(UPDATED_CERTIFICATION_TITLE)
				.certificationDate(UPDATED_CERTIFICATION_DATE).certificationDetails(UPDATED_CERTIFICATION_DETAILS);

		restCandidateCertificationMockMvc
				.perform(put("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateCertification)))
				.andExpect(status().isOk());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeUpdate);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(UPDATED_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(UPDATED_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(UPDATED_CERTIFICATION_DETAILS);

		// Validate the CandidateCertification in Elasticsearch
		CandidateCertification candidateCertificationEs = candidateCertificationSearchRepository
				.findOne(testCandidateCertification.getId());
		assertThat(candidateCertificationEs).isEqualToComparingFieldByField(testCandidateCertification);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateCertification() throws Exception {
		int databaseSizeBeforeUpdate = candidateCertificationRepository.findAll().size();

		// Create the CandidateCertification

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateCertificationMockMvc
				.perform(put("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		candidateCertificationSearchRepository.save(candidateCertification);
		int databaseSizeBeforeDelete = candidateCertificationRepository.findAll().size();

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(delete("/api/candidate-certifications/{id}", candidateCertification.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean candidateCertificationExistsInEs = candidateCertificationSearchRepository
				.exists(candidateCertification.getId());
		assertThat(candidateCertificationExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		candidateCertificationSearchRepository.save(candidateCertification);

		// Search the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/_search/candidate-certifications?query=id:" + candidateCertification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateCertification.getId().intValue())))
				.andExpect(jsonPath("$.[*].certificationTitle").value(hasItem(DEFAULT_CERTIFICATION_TITLE.toString())))
				.andExpect(jsonPath("$.[*].certificationDate").value(hasItem(DEFAULT_CERTIFICATION_DATE.toString())))
				.andExpect(jsonPath("$.[*].certificationDetails")
						.value(hasItem(DEFAULT_CERTIFICATION_DETAILS.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateCertification.class);
		CandidateCertification candidateCertification1 = new CandidateCertification();
		candidateCertification1.setId(1L);
		CandidateCertification candidateCertification2 = new CandidateCertification();
		candidateCertification2.setId(candidateCertification1.getId());
		assertThat(candidateCertification1).isEqualTo(candidateCertification2);
		candidateCertification2.setId(2L);
		assertThat(candidateCertification1).isNotEqualTo(candidateCertification2);
		candidateCertification1.setId(null);
		assertThat(candidateCertification1).isNotEqualTo(candidateCertification2);
	}
}
