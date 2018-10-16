package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CaptureCollege;
import com.drishika.gradzcircle.repository.CaptureCollegeRepository;
import com.drishika.gradzcircle.repository.search.CaptureCollegeSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CaptureCollegeResource REST controller.
 *
 * @see CaptureCollegeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CaptureCollegeResourceIntTest {

	private static final String DEFAULT_COLLEGE_NAME = "AAAAAAAAAA";
	private static final String UPDATED_COLLEGE_NAME = "BBBBBBBBBB";

	@Autowired
	private CaptureCollegeRepository captureCollegeRepository;

	@Autowired
	private CaptureCollegeSearchRepository captureCollegeSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCaptureCollegeMockMvc;

	private CaptureCollege captureCollege;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CaptureCollegeResource captureCollegeResource = new CaptureCollegeResource(captureCollegeRepository,
				captureCollegeSearchRepository);
		this.restCaptureCollegeMockMvc = MockMvcBuilders.standaloneSetup(captureCollegeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CaptureCollege createEntity(EntityManager em) {
		CaptureCollege captureCollege = new CaptureCollege().collegeName(DEFAULT_COLLEGE_NAME);
		return captureCollege;
	}

	@Before
	public void initTest() {
		captureCollegeSearchRepository.deleteAll();
		captureCollege = createEntity(em);
	}

	@Test
	@Transactional
	public void createCaptureCollege() throws Exception {
		int databaseSizeBeforeCreate = captureCollegeRepository.findAll().size();

		// Create the CaptureCollege
		restCaptureCollegeMockMvc.perform(post("/api/capture-colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(captureCollege))).andExpect(status().isCreated());

		// Validate the CaptureCollege in the database
		List<CaptureCollege> captureCollegeList = captureCollegeRepository.findAll();
		assertThat(captureCollegeList).hasSize(databaseSizeBeforeCreate + 1);
		CaptureCollege testCaptureCollege = captureCollegeList.get(captureCollegeList.size() - 1);
		assertThat(testCaptureCollege.getCollegeName()).isEqualTo(DEFAULT_COLLEGE_NAME);

		// Validate the CaptureCollege in Elasticsearch
		CaptureCollege captureCollegeEs = captureCollegeSearchRepository.findOne(testCaptureCollege.getId());
		assertThat(captureCollegeEs).isEqualToComparingFieldByField(testCaptureCollege);
	}

	@Test
	@Transactional
	public void createCaptureCollegeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = captureCollegeRepository.findAll().size();

		// Create the CaptureCollege with an existing ID
		captureCollege.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCaptureCollegeMockMvc.perform(post("/api/capture-colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(captureCollege))).andExpect(status().isBadRequest());

		// Validate the CaptureCollege in the database
		List<CaptureCollege> captureCollegeList = captureCollegeRepository.findAll();
		assertThat(captureCollegeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCaptureColleges() throws Exception {
		// Initialize the database
		captureCollegeRepository.saveAndFlush(captureCollege);

		// Get all the captureCollegeList
		restCaptureCollegeMockMvc.perform(get("/api/capture-colleges?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(captureCollege.getId().intValue())))
				.andExpect(jsonPath("$.[*].collegeName").value(hasItem(DEFAULT_COLLEGE_NAME.toString())));
	}

	@Test
	@Transactional
	public void getCaptureCollege() throws Exception {
		// Initialize the database
		captureCollegeRepository.saveAndFlush(captureCollege);

		// Get the captureCollege
		restCaptureCollegeMockMvc.perform(get("/api/capture-colleges/{id}", captureCollege.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(captureCollege.getId().intValue()))
				.andExpect(jsonPath("$.collegeName").value(DEFAULT_COLLEGE_NAME.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingCaptureCollege() throws Exception {
		// Get the captureCollege
		restCaptureCollegeMockMvc.perform(get("/api/capture-colleges/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCaptureCollege() throws Exception {
		// Initialize the database
		captureCollegeRepository.saveAndFlush(captureCollege);
		captureCollegeSearchRepository.save(captureCollege);
		int databaseSizeBeforeUpdate = captureCollegeRepository.findAll().size();

		// Update the captureCollege
		CaptureCollege updatedCaptureCollege = captureCollegeRepository.findOne(captureCollege.getId());
		updatedCaptureCollege.collegeName(UPDATED_COLLEGE_NAME);

		restCaptureCollegeMockMvc.perform(put("/api/capture-colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCaptureCollege))).andExpect(status().isOk());

		// Validate the CaptureCollege in the database
		List<CaptureCollege> captureCollegeList = captureCollegeRepository.findAll();
		assertThat(captureCollegeList).hasSize(databaseSizeBeforeUpdate);
		CaptureCollege testCaptureCollege = captureCollegeList.get(captureCollegeList.size() - 1);
		assertThat(testCaptureCollege.getCollegeName()).isEqualTo(UPDATED_COLLEGE_NAME);

		// Validate the CaptureCollege in Elasticsearch
		CaptureCollege captureCollegeEs = captureCollegeSearchRepository.findOne(testCaptureCollege.getId());
		assertThat(captureCollegeEs).isEqualToComparingFieldByField(testCaptureCollege);
	}

	@Test
	@Transactional
	public void updateNonExistingCaptureCollege() throws Exception {
		int databaseSizeBeforeUpdate = captureCollegeRepository.findAll().size();

		// Create the CaptureCollege

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCaptureCollegeMockMvc.perform(put("/api/capture-colleges").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(captureCollege))).andExpect(status().isCreated());

		// Validate the CaptureCollege in the database
		List<CaptureCollege> captureCollegeList = captureCollegeRepository.findAll();
		assertThat(captureCollegeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCaptureCollege() throws Exception {
		// Initialize the database
		captureCollegeRepository.saveAndFlush(captureCollege);
		captureCollegeSearchRepository.save(captureCollege);
		int databaseSizeBeforeDelete = captureCollegeRepository.findAll().size();

		// Get the captureCollege
		restCaptureCollegeMockMvc.perform(
				delete("/api/capture-colleges/{id}", captureCollege.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean captureCollegeExistsInEs = captureCollegeSearchRepository.exists(captureCollege.getId());
		assertThat(captureCollegeExistsInEs).isFalse();

		// Validate the database is empty
		List<CaptureCollege> captureCollegeList = captureCollegeRepository.findAll();
		assertThat(captureCollegeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCaptureCollege() throws Exception {
		// Initialize the database
		captureCollegeRepository.saveAndFlush(captureCollege);
		captureCollegeSearchRepository.save(captureCollege);

		// Search the captureCollege
		restCaptureCollegeMockMvc.perform(get("/api/_search/capture-colleges?query=id:" + captureCollege.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(captureCollege.getId().intValue())))
				.andExpect(jsonPath("$.[*].collegeName").value(hasItem(DEFAULT_COLLEGE_NAME.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CaptureCollege.class);
		CaptureCollege captureCollege1 = new CaptureCollege();
		captureCollege1.setId(1L);
		CaptureCollege captureCollege2 = new CaptureCollege();
		captureCollege2.setId(captureCollege1.getId());
		assertThat(captureCollege1).isEqualTo(captureCollege2);
		captureCollege2.setId(2L);
		assertThat(captureCollege1).isNotEqualTo(captureCollege2);
		captureCollege1.setId(null);
		assertThat(captureCollege1).isNotEqualTo(captureCollege2);
	}
}
