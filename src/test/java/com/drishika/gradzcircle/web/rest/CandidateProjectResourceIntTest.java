package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
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

import com.drishika.gradzcircle.domain.enumeration.ProjectType;

/**
 * Test class for the CandidateProjectResource REST controller.
 *
 * @see CandidateProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateProjectResourceIntTest {

	private static final String DEFAULT_PROJECT_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_PROJECT_TITLE = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_PROJECT_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_PROJECT_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_PROJECT_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_PROJECT_END_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_PROJECT_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_PROJECT_DESCRIPTION = "BBBBBBBBBB";

	private static final Integer DEFAULT_PROJECT_DURATION = 1;
	private static final Integer UPDATED_PROJECT_DURATION = 2;

	private static final String DEFAULT_PROJECT_PERIOD = "AAAAAAAAAA";
	private static final String UPDATED_PROJECT_PERIOD = "BBBBBBBBBB";

	private static final String DEFAULT_CONTRIBUTION_IN_PROJECT = "AAAAAAAAAA";
	private static final String UPDATED_CONTRIBUTION_IN_PROJECT = "BBBBBBBBBB";

	private static final Boolean DEFAULT_IS_CURRENT_PROJECT = false;
	private static final Boolean UPDATED_IS_CURRENT_PROJECT = true;

	private static final ProjectType DEFAULT_PROJECT_TYPE = ProjectType.ACADEMIC;
	private static final ProjectType UPDATED_PROJECT_TYPE = ProjectType.SELF_INTEREST;

	@Autowired
	private CandidateProjectRepository candidateProjectRepository;

	@Autowired
	private CandidateProjectSearchRepository candidateProjectSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateProjectMockMvc;

	private CandidateProject candidateProject;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateProjectResource candidateProjectResource = new CandidateProjectResource(
				candidateProjectRepository, candidateProjectSearchRepository);
		this.restCandidateProjectMockMvc = MockMvcBuilders.standaloneSetup(candidateProjectResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateProject createEntity(EntityManager em) {
		CandidateProject candidateProject = new CandidateProject().projectTitle(DEFAULT_PROJECT_TITLE)
				.projectStartDate(DEFAULT_PROJECT_START_DATE).projectEndDate(DEFAULT_PROJECT_END_DATE)
				.projectDescription(DEFAULT_PROJECT_DESCRIPTION).projectDuration(DEFAULT_PROJECT_DURATION)

				.contributionInProject(DEFAULT_CONTRIBUTION_IN_PROJECT).isCurrentProject(DEFAULT_IS_CURRENT_PROJECT)
				.projectType(DEFAULT_PROJECT_TYPE);
		return candidateProject;
	}

	@Before
	public void initTest() {
		candidateProjectSearchRepository.deleteAll();
		candidateProject = createEntity(em);
	}

	@Test
	@Transactional
	public void createCandidateProject() throws Exception {
		int databaseSizeBeforeCreate = candidateProjectRepository.findAll().size();

		// Create the CandidateProject
		restCandidateProjectMockMvc.perform(post("/api/candidate-projects").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateProject))).andExpect(status().isCreated());

		// Validate the CandidateProject in the database
		List<CandidateProject> candidateProjectList = candidateProjectRepository.findAll();
		assertThat(candidateProjectList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateProject testCandidateProject = candidateProjectList.get(candidateProjectList.size() - 1);
		assertThat(testCandidateProject.getProjectTitle()).isEqualTo(DEFAULT_PROJECT_TITLE);
		assertThat(testCandidateProject.getProjectStartDate()).isEqualTo(DEFAULT_PROJECT_START_DATE);
		assertThat(testCandidateProject.getProjectEndDate()).isEqualTo(DEFAULT_PROJECT_END_DATE);
		assertThat(testCandidateProject.getProjectDescription()).isEqualTo(DEFAULT_PROJECT_DESCRIPTION);
		assertThat(testCandidateProject.getProjectDuration()).isEqualTo(DEFAULT_PROJECT_DURATION);
		// assertThat(testCandidateProject.getProjectPeriod()).isEqualTo(DEFAULT_PROJECT_PERIOD);
		assertThat(testCandidateProject.getContributionInProject()).isEqualTo(DEFAULT_CONTRIBUTION_IN_PROJECT);
		assertThat(testCandidateProject.isIsCurrentProject()).isEqualTo(DEFAULT_IS_CURRENT_PROJECT);
		assertThat(testCandidateProject.getProjectType()).isEqualTo(DEFAULT_PROJECT_TYPE);

		// Validate the CandidateProject in Elasticsearch
		CandidateProject candidateProjectEs = candidateProjectSearchRepository.findOne(testCandidateProject.getId());
		assertThat(candidateProjectEs).isEqualToComparingFieldByField(testCandidateProject);
	}

	@Test
	@Transactional
	public void createCandidateProjectWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateProjectRepository.findAll().size();

		// Create the CandidateProject with an existing ID
		candidateProject.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateProjectMockMvc
				.perform(post("/api/candidate-projects").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateProject)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateProject in the database
		List<CandidateProject> candidateProjectList = candidateProjectRepository.findAll();
		assertThat(candidateProjectList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateProjects() throws Exception {
		// Initialize the database
		candidateProjectRepository.saveAndFlush(candidateProject);

		// Get all the candidateProjectList
		restCandidateProjectMockMvc.perform(get("/api/candidate-projects?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateProject.getId().intValue())))
				.andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE.toString())))
				.andExpect(jsonPath("$.[*].projectStartDate").value(hasItem(DEFAULT_PROJECT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].projectEndDate").value(hasItem(DEFAULT_PROJECT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].projectDuration").value(hasItem(DEFAULT_PROJECT_DURATION)))
				.andExpect(jsonPath("$.[*].contributionInProject")
						.value(hasItem(DEFAULT_CONTRIBUTION_IN_PROJECT.toString())))
				.andExpect(jsonPath("$.[*].isCurrentProject").value(hasItem(DEFAULT_IS_CURRENT_PROJECT.booleanValue())))
				.andExpect(jsonPath("$.[*].projectType").value(hasItem(DEFAULT_PROJECT_TYPE.toString())));
	}

	@Test
	@Transactional
	public void getCandidateProject() throws Exception {
		// Initialize the database
		candidateProjectRepository.saveAndFlush(candidateProject);

		// Get the candidateProject
		restCandidateProjectMockMvc.perform(get("/api/candidate-projects/{id}", candidateProject.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateProject.getId().intValue()))
				.andExpect(jsonPath("$.projectTitle").value(DEFAULT_PROJECT_TITLE.toString()))
				.andExpect(jsonPath("$.projectStartDate").value(DEFAULT_PROJECT_START_DATE.toString()))
				.andExpect(jsonPath("$.projectEndDate").value(DEFAULT_PROJECT_END_DATE.toString()))
				.andExpect(jsonPath("$.projectDescription").value(DEFAULT_PROJECT_DESCRIPTION.toString()))
				.andExpect(jsonPath("$.projectDuration").value(DEFAULT_PROJECT_DURATION))
				.andExpect(jsonPath("$.contributionInProject").value(DEFAULT_CONTRIBUTION_IN_PROJECT.toString()))
				.andExpect(jsonPath("$.isCurrentProject").value(DEFAULT_IS_CURRENT_PROJECT.booleanValue()))
				.andExpect(jsonPath("$.projectType").value(DEFAULT_PROJECT_TYPE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateProject() throws Exception {
		// Get the candidateProject
		restCandidateProjectMockMvc.perform(get("/api/candidate-projects/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateProject() throws Exception {
		// Initialize the database
		candidateProjectRepository.saveAndFlush(candidateProject);
		candidateProjectSearchRepository.save(candidateProject);
		int databaseSizeBeforeUpdate = candidateProjectRepository.findAll().size();

		// Update the candidateProject
		CandidateProject updatedCandidateProject = candidateProjectRepository.findOne(candidateProject.getId());
		updatedCandidateProject.projectTitle(UPDATED_PROJECT_TITLE).projectStartDate(UPDATED_PROJECT_START_DATE)
				.projectEndDate(UPDATED_PROJECT_END_DATE).projectDescription(UPDATED_PROJECT_DESCRIPTION)
				.projectDuration(UPDATED_PROJECT_DURATION)

				.contributionInProject(UPDATED_CONTRIBUTION_IN_PROJECT).isCurrentProject(UPDATED_IS_CURRENT_PROJECT)
				.projectType(UPDATED_PROJECT_TYPE);

		restCandidateProjectMockMvc
				.perform(put("/api/candidate-projects").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateProject)))
				.andExpect(status().isOk());

		// Validate the CandidateProject in the database
		List<CandidateProject> candidateProjectList = candidateProjectRepository.findAll();
		assertThat(candidateProjectList).hasSize(databaseSizeBeforeUpdate);
		CandidateProject testCandidateProject = candidateProjectList.get(candidateProjectList.size() - 1);
		assertThat(testCandidateProject.getProjectTitle()).isEqualTo(UPDATED_PROJECT_TITLE);
		assertThat(testCandidateProject.getProjectStartDate()).isEqualTo(UPDATED_PROJECT_START_DATE);
		assertThat(testCandidateProject.getProjectEndDate()).isEqualTo(UPDATED_PROJECT_END_DATE);
		assertThat(testCandidateProject.getProjectDescription()).isEqualTo(UPDATED_PROJECT_DESCRIPTION);
		assertThat(testCandidateProject.getProjectDuration()).isEqualTo(UPDATED_PROJECT_DURATION);
		// assertThat(testCandidateProject.getProjectPeriod()).isEqualTo(UPDATED_PROJECT_PERIOD);
		assertThat(testCandidateProject.getContributionInProject()).isEqualTo(UPDATED_CONTRIBUTION_IN_PROJECT);
		assertThat(testCandidateProject.isIsCurrentProject()).isEqualTo(UPDATED_IS_CURRENT_PROJECT);
		assertThat(testCandidateProject.getProjectType()).isEqualTo(UPDATED_PROJECT_TYPE);

		// Validate the CandidateProject in Elasticsearch
		CandidateProject candidateProjectEs = candidateProjectSearchRepository.findOne(testCandidateProject.getId());
		assertThat(candidateProjectEs).isEqualToComparingFieldByField(testCandidateProject);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateProject() throws Exception {
		int databaseSizeBeforeUpdate = candidateProjectRepository.findAll().size();

		// Create the CandidateProject

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateProjectMockMvc.perform(put("/api/candidate-projects").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateProject))).andExpect(status().isCreated());

		// Validate the CandidateProject in the database
		List<CandidateProject> candidateProjectList = candidateProjectRepository.findAll();
		assertThat(candidateProjectList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCandidateProject() throws Exception {
		// Initialize the database
		candidateProjectRepository.saveAndFlush(candidateProject);
		candidateProjectSearchRepository.save(candidateProject);
		int databaseSizeBeforeDelete = candidateProjectRepository.findAll().size();

		// Get the candidateProject
		restCandidateProjectMockMvc.perform(
				delete("/api/candidate-projects/{id}", candidateProject.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean candidateProjectExistsInEs = candidateProjectSearchRepository.exists(candidateProject.getId());
		assertThat(candidateProjectExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateProject> candidateProjectList = candidateProjectRepository.findAll();
		assertThat(candidateProjectList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCandidateProject() throws Exception {
		// Initialize the database
		candidateProjectRepository.saveAndFlush(candidateProject);
		candidateProjectSearchRepository.save(candidateProject);

		// Search the candidateProject
		restCandidateProjectMockMvc.perform(get("/api/_search/candidate-projects?query=id:" + candidateProject.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateProject.getId().intValue())))
				.andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE.toString())))
				.andExpect(jsonPath("$.[*].projectStartDate").value(hasItem(DEFAULT_PROJECT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].projectEndDate").value(hasItem(DEFAULT_PROJECT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].projectDuration").value(hasItem(DEFAULT_PROJECT_DURATION)))
				.andExpect(jsonPath("$.[*].contributionInProject")
						.value(hasItem(DEFAULT_CONTRIBUTION_IN_PROJECT.toString())))
				.andExpect(jsonPath("$.[*].isCurrentProject").value(hasItem(DEFAULT_IS_CURRENT_PROJECT.booleanValue())))
				.andExpect(jsonPath("$.[*].projectType").value(hasItem(DEFAULT_PROJECT_TYPE.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateProject.class);
		CandidateProject candidateProject1 = new CandidateProject();
		candidateProject1.setId(1L);
		CandidateProject candidateProject2 = new CandidateProject();
		candidateProject2.setId(candidateProject1.getId());
		assertThat(candidateProject1).isEqualTo(candidateProject2);
		candidateProject2.setId(2L);
		assertThat(candidateProject1).isNotEqualTo(candidateProject2);
		candidateProject1.setId(null);
		assertThat(candidateProject1).isNotEqualTo(candidateProject2);
	}
}
