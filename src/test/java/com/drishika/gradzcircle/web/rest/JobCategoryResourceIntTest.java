package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.JobCategory;
import com.drishika.gradzcircle.repository.JobCategoryRepository;
import com.drishika.gradzcircle.repository.search.JobCategorySearchRepository;
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

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobCategoryResource REST controller.
 *
 * @see JobCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobCategoryResourceIntTest {

    private static final String DEFAULT_JOB_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_JOB_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private JobCategorySearchRepository jobCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobCategoryMockMvc;

    private JobCategory jobCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobCategoryResource jobCategoryResource = new JobCategoryResource(jobCategoryRepository, jobCategorySearchRepository);
        this.restJobCategoryMockMvc = MockMvcBuilders.standaloneSetup(jobCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobCategory createEntity(EntityManager em) {
        JobCategory jobCategory = new JobCategory()
            .jobCategory(DEFAULT_JOB_CATEGORY);
        return jobCategory;
    }

    @Before
    public void initTest() {
        jobCategorySearchRepository.deleteAll();
        jobCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobCategory() throws Exception {
        int databaseSizeBeforeCreate = jobCategoryRepository.findAll().size();

        // Create the JobCategory
        restJobCategoryMockMvc.perform(post("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isCreated());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobCategory testJobCategory = jobCategoryList.get(jobCategoryList.size() - 1);
        assertThat(testJobCategory.getJobCategory()).isEqualTo(DEFAULT_JOB_CATEGORY);

        // Validate the JobCategory in Elasticsearch
        JobCategory jobCategoryEs = jobCategorySearchRepository.findOne(testJobCategory.getId());
        assertThat(jobCategoryEs).isEqualToIgnoringGivenFields(testJobCategory);
    }

    @Test
    @Transactional
    public void createJobCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobCategoryRepository.findAll().size();

        // Create the JobCategory with an existing ID
        jobCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobCategoryMockMvc.perform(post("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isBadRequest());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobCategories() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList
        restJobCategoryMockMvc.perform(get("/api/job-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCategory").value(hasItem(DEFAULT_JOB_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getJobCategory() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get the jobCategory
        restJobCategoryMockMvc.perform(get("/api/job-categories/{id}", jobCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobCategory.getId().intValue()))
            .andExpect(jsonPath("$.jobCategory").value(DEFAULT_JOB_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobCategory() throws Exception {
        // Get the jobCategory
        restJobCategoryMockMvc.perform(get("/api/job-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobCategory() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);
        jobCategorySearchRepository.save(jobCategory);
        int databaseSizeBeforeUpdate = jobCategoryRepository.findAll().size();

        // Update the jobCategory
        JobCategory updatedJobCategory = jobCategoryRepository.findOne(jobCategory.getId());
        // Disconnect from session so that the updates on updatedJobCategory are not directly saved in db
        em.detach(updatedJobCategory);
        updatedJobCategory
            .jobCategory(UPDATED_JOB_CATEGORY);

        restJobCategoryMockMvc.perform(put("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobCategory)))
            .andExpect(status().isOk());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeUpdate);
        JobCategory testJobCategory = jobCategoryList.get(jobCategoryList.size() - 1);
        assertThat(testJobCategory.getJobCategory()).isEqualTo(UPDATED_JOB_CATEGORY);

        // Validate the JobCategory in Elasticsearch
        JobCategory jobCategoryEs = jobCategorySearchRepository.findOne(testJobCategory.getId());
        assertThat(jobCategoryEs).isEqualToIgnoringGivenFields(testJobCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingJobCategory() throws Exception {
        int databaseSizeBeforeUpdate = jobCategoryRepository.findAll().size();

        // Create the JobCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobCategoryMockMvc.perform(put("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isCreated());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobCategory() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);
        jobCategorySearchRepository.save(jobCategory);
        int databaseSizeBeforeDelete = jobCategoryRepository.findAll().size();

        // Get the jobCategory
        restJobCategoryMockMvc.perform(delete("/api/job-categories/{id}", jobCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobCategoryExistsInEs = jobCategorySearchRepository.exists(jobCategory.getId());
        assertThat(jobCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobCategory() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);
        jobCategorySearchRepository.save(jobCategory);

        // Search the jobCategory
        restJobCategoryMockMvc.perform(get("/api/_search/job-categories?query=id:" + jobCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCategory").value(hasItem(DEFAULT_JOB_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobCategory.class);
        JobCategory jobCategory1 = new JobCategory();
        jobCategory1.setId(1L);
        JobCategory jobCategory2 = new JobCategory();
        jobCategory2.setId(jobCategory1.getId());
        assertThat(jobCategory1).isEqualTo(jobCategory2);
        jobCategory2.setId(2L);
        assertThat(jobCategory1).isNotEqualTo(jobCategory2);
        jobCategory1.setId(null);
        assertThat(jobCategory1).isNotEqualTo(jobCategory2);
    }
}
