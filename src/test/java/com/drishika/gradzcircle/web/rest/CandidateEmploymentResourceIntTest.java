package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
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
 * Test class for the CandidateEmploymentResource REST controller.
 *
 * @see CandidateEmploymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateEmploymentResourceIntTest {

	private static final Integer DEFAULT_LOCATION = 1;
	private static final Integer UPDATED_LOCATION = 2;

	private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

	private static final String DEFAULT_EMPLOYER_NAME = "AAAAAAAAAA";
	private static final String UPDATED_EMPLOYER_NAME = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_EMPLOYMENT_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EMPLOYMENT_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_EMPLOYMENT_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EMPLOYMENT_END_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final Integer DEFAULT_EMPLOYMENT_DURATION = 1;
	private static final Integer UPDATED_EMPLOYMENT_DURATION = 2;

	private static final String DEFAULT_EMPLOYMENT_PERIOD = "AAAAAAAAAA";
	private static final String UPDATED_EMPLOYMENT_PERIOD = "BBBBBBBBBB";

	private static final Boolean DEFAULT_IS_CURRENT_EMPLOYMENT = false;
	private static final Boolean UPDATED_IS_CURRENT_EMPLOYMENT = true;

	private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

	private static final String DEFAULT_ROLE_AND_RESPONSIBILITIES = "AAAAAAAAAA";
	private static final String UPDATED_ROLE_AND_RESPONSIBILITIES = "BBBBBBBBBB";

	@Autowired
	private CandidateEmploymentRepository candidateEmploymentRepository;

	@Autowired
	private CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

	@Autowired
	private CandidateProjectRepository candidateProjectRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateEmploymentMockMvc;

	private CandidateEmployment candidateEmployment;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateEmploymentResource candidateEmploymentResource = new CandidateEmploymentResource(
				candidateEmploymentRepository, candidateEmploymentSearchRepository, candidateProjectRepository);
		this.restCandidateEmploymentMockMvc = MockMvcBuilders.standaloneSetup(candidateEmploymentResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateEmployment createEntity(EntityManager em) {
		CandidateEmployment candidateEmployment = new CandidateEmployment().location(DEFAULT_LOCATION)
				.jobTitle(DEFAULT_JOB_TITLE).employerName(DEFAULT_EMPLOYER_NAME)
				.employmentStartDate(DEFAULT_EMPLOYMENT_START_DATE).employmentEndDate(DEFAULT_EMPLOYMENT_END_DATE)
				.employmentDuration(DEFAULT_EMPLOYMENT_DURATION)

				.isCurrentEmployment(DEFAULT_IS_CURRENT_EMPLOYMENT).jobDescription(DEFAULT_JOB_DESCRIPTION);

		return candidateEmployment;
	}

	@Before
	public void initTest() {
		candidateEmploymentSearchRepository.deleteAll();
		candidateEmployment = createEntity(em);
	}

	@Test
	@Transactional
	public void createCandidateEmployment() throws Exception {
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();

		// Create the CandidateEmployment
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(DEFAULT_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(DEFAULT_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(DEFAULT_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(DEFAULT_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(DEFAULT_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(DEFAULT_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(DEFAULT_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(DEFAULT_ROLE_AND_RESPONSIBILITIES);

		// Validate the CandidateEmployment in Elasticsearch
		CandidateEmployment candidateEmploymentEs = candidateEmploymentSearchRepository
				.findOne(testCandidateEmployment.getId());
		assertThat(candidateEmploymentEs).isEqualToComparingFieldByField(testCandidateEmployment);
	}

	@Test
	@Transactional
	public void createCandidateEmploymentWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();

		// Create the CandidateEmployment with an existing ID
		candidateEmployment.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateEmployments() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);

		// Get all the candidateEmploymentList
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEmployment.getId().intValue())))
				.andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME.toString())))
				.andExpect(
						jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentEndDate").value(hasItem(DEFAULT_EMPLOYMENT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentDuration").value(hasItem(DEFAULT_EMPLOYMENT_DURATION)))
				.andExpect(jsonPath("$.[*].isCurrentEmployment")
						.value(hasItem(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())));

	}

	@Test
	@Transactional
	public void getCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments/{id}", candidateEmployment.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateEmployment.getId().intValue()))
				.andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
				.andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$.employerName").value(DEFAULT_EMPLOYER_NAME.toString()))
				.andExpect(jsonPath("$.employmentStartDate").value(DEFAULT_EMPLOYMENT_START_DATE.toString()))
				.andExpect(jsonPath("$.employmentEndDate").value(DEFAULT_EMPLOYMENT_END_DATE.toString()))
				.andExpect(jsonPath("$.employmentDuration").value(DEFAULT_EMPLOYMENT_DURATION))
				.andExpect(jsonPath("$.isCurrentEmployment").value(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue()))
				.andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()));

	}

	@Test
	@Transactional
	public void getNonExistingCandidateEmployment() throws Exception {
		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		candidateEmploymentSearchRepository.save(candidateEmployment);
		int databaseSizeBeforeUpdate = candidateEmploymentRepository.findAll().size();

		// Update the candidateEmployment
		CandidateEmployment updatedCandidateEmployment = candidateEmploymentRepository
				.findOne(candidateEmployment.getId());
		updatedCandidateEmployment.location(UPDATED_LOCATION).jobTitle(UPDATED_JOB_TITLE)
				.employerName(UPDATED_EMPLOYER_NAME).employmentStartDate(UPDATED_EMPLOYMENT_START_DATE)
				.employmentEndDate(UPDATED_EMPLOYMENT_END_DATE).employmentDuration(UPDATED_EMPLOYMENT_DURATION)
				// .employmentPeriod(UPDATED_EMPLOYMENT_PERIOD)
				.isCurrentEmployment(UPDATED_IS_CURRENT_EMPLOYMENT).jobDescription(UPDATED_JOB_DESCRIPTION);
		// .roleAndResponsibilities(UPDATED_ROLE_AND_RESPONSIBILITIES);

		restCandidateEmploymentMockMvc
				.perform(put("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateEmployment)))
				.andExpect(status().isOk());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeUpdate);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(UPDATED_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(UPDATED_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(UPDATED_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(UPDATED_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(UPDATED_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(UPDATED_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(UPDATED_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(UPDATED_ROLE_AND_RESPONSIBILITIES);

		// Validate the CandidateEmployment in Elasticsearch
		CandidateEmployment candidateEmploymentEs = candidateEmploymentSearchRepository
				.findOne(testCandidateEmployment.getId());
		assertThat(candidateEmploymentEs).isEqualToComparingFieldByField(testCandidateEmployment);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateEmployment() throws Exception {
		int databaseSizeBeforeUpdate = candidateEmploymentRepository.findAll().size();

		// Create the CandidateEmployment

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateEmploymentMockMvc
				.perform(put("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		candidateEmploymentSearchRepository.save(candidateEmployment);
		int databaseSizeBeforeDelete = candidateEmploymentRepository.findAll().size();

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(delete("/api/candidate-employments/{id}", candidateEmployment.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean candidateEmploymentExistsInEs = candidateEmploymentSearchRepository.exists(candidateEmployment.getId());
		assertThat(candidateEmploymentExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		candidateEmploymentSearchRepository.save(candidateEmployment);

		// Search the candidateEmployment
		restCandidateEmploymentMockMvc
				.perform(get("/api/_search/candidate-employments?query=id:" + candidateEmployment.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEmployment.getId().intValue())))
				.andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME.toString())))
				.andExpect(
						jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentEndDate").value(hasItem(DEFAULT_EMPLOYMENT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentDuration").value(hasItem(DEFAULT_EMPLOYMENT_DURATION)))
				.andExpect(jsonPath("$.[*].isCurrentEmployment")
						.value(hasItem(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())));

	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateEmployment.class);
		CandidateEmployment candidateEmployment1 = new CandidateEmployment();
		candidateEmployment1.setId(1L);
		CandidateEmployment candidateEmployment2 = new CandidateEmployment();
		candidateEmployment2.setId(candidateEmployment1.getId());
		assertThat(candidateEmployment1).isEqualTo(candidateEmployment2);
		candidateEmployment2.setId(2L);
		assertThat(candidateEmployment1).isNotEqualTo(candidateEmployment2);
		candidateEmployment1.setId(null);
		assertThat(candidateEmployment1).isNotEqualTo(candidateEmployment2);
	}
}
