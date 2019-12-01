package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

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

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.search.JobTypeSearchRepository;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the JobTypeResource REST controller.
 *
 * @see JobTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobTypeResourceIntTest {

    private static final String DEFAULT_JOB_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_JOB_TYPE_COST = 1D;
    private static final Double UPDATED_JOB_TYPE_COST = 2D;

    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Autowired
    private JobTypeSearchRepository mockJobTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
    @Autowired 
    GradzcircleCacheManager< String, Map<String,JobType>> cacheManager;

    @Autowired
    private EntityManager em;

    private MockMvc restJobTypeMockMvc;

    private JobType jobType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobTypeResource jobTypeResource = new JobTypeResource(jobTypeRepository, mockJobTypeSearchRepository,cacheManager);
        this.restJobTypeMockMvc = MockMvcBuilders.standaloneSetup(jobTypeResource)
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
    public static JobType createEntity(EntityManager em) {
        JobType jobType = new JobType()
            .jobType(DEFAULT_JOB_TYPE)
            .jobTypeCost(DEFAULT_JOB_TYPE_COST);
        return jobType;
    }

    @Before
    public void initTest() {
       // jobTypeSearchRepository.deleteAll();
        jobType = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobType() throws Exception {
        int databaseSizeBeforeCreate = jobTypeRepository.findAll().size();

        // Create the JobType
        restJobTypeMockMvc.perform(post("/api/job-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobType)))
            .andExpect(status().isCreated());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeCreate + 1);
        JobType testJobType = jobTypeList.get(jobTypeList.size() - 1);
        assertThat(testJobType.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testJobType.getJobTypeCost()).isEqualTo(DEFAULT_JOB_TYPE_COST);

        // Validate the JobType in Elasticsearch
        verify(mockJobTypeSearchRepository,times(1)).save(testJobType);
    }

    @Test
    @Transactional
    public void createJobTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTypeRepository.findAll().size();

        // Create the JobType with an existing ID
        jobType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTypeMockMvc.perform(post("/api/job-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobType)))
            .andExpect(status().isBadRequest());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobTypes() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        // Get all the jobTypeList
        restJobTypeMockMvc.perform(get("/api/job-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobType.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobTypeCost").value(hasItem(DEFAULT_JOB_TYPE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void getJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);

        // Get the jobType
        restJobTypeMockMvc.perform(get("/api/job-types/{id}", jobType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobType.getId().intValue()))
            .andExpect(jsonPath("$.jobType").value(DEFAULT_JOB_TYPE.toString()))
            .andExpect(jsonPath("$.jobTypeCost").value(DEFAULT_JOB_TYPE_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJobType() throws Exception {
        // Get the jobType
        restJobTypeMockMvc.perform(get("/api/job-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);
       // jobTypeSearchRepository.save(jobType);
        int databaseSizeBeforeUpdate = jobTypeRepository.findAll().size();

        // Update the jobType
        JobType updatedJobType = jobTypeRepository.findById(jobType.getId()).get();
        // Disconnect from session so that the updates on updatedJobType are not directly saved in db
        em.detach(updatedJobType);
        updatedJobType
            .jobType(UPDATED_JOB_TYPE)
            .jobTypeCost(UPDATED_JOB_TYPE_COST);

        restJobTypeMockMvc.perform(put("/api/job-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobType)))
            .andExpect(status().isOk());

        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeUpdate);
        JobType testJobType = jobTypeList.get(jobTypeList.size() - 1);
        assertThat(testJobType.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testJobType.getJobTypeCost()).isEqualTo(UPDATED_JOB_TYPE_COST);

        // Validate the JobType in Elasticsearch
        verify(mockJobTypeSearchRepository,times(1)).save(testJobType);
    }

    @Test
    @Transactional
    public void updateNonExistingJobType() throws Exception {
        int databaseSizeBeforeUpdate = jobTypeRepository.findAll().size();

        // Create the JobType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobTypeMockMvc.perform(put("/api/job-types")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(jobType)))
	            .andExpect(status().isBadRequest());
        // Validate the JobType in the database
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);
        //jobTypeSearchRepository.save(jobType);
        int databaseSizeBeforeDelete = jobTypeRepository.findAll().size();

        // Get the jobType
        restJobTypeMockMvc.perform(delete("/api/job-types/{id}", jobType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        verify(mockJobTypeSearchRepository,times(1)).deleteById(jobType.getId());

        // Validate the database is empty
        List<JobType> jobTypeList = jobTypeRepository.findAll();
        assertThat(jobTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobType() throws Exception {
        // Initialize the database
        jobTypeRepository.saveAndFlush(jobType);
       // jobTypeSearchRepository.save(jobType);
        when(mockJobTypeSearchRepository.search(queryStringQuery("id:" + jobType.getId())))
        .thenReturn(Collections.singletonList(jobType));
        // Search the jobType
        restJobTypeMockMvc.perform(get("/api/_search/job-types?query=id:" + jobType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobType.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobTypeCost").value(hasItem(DEFAULT_JOB_TYPE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobType.class);
        JobType jobType1 = new JobType();
        jobType1.setId(1L);
        JobType jobType2 = new JobType();
        jobType2.setId(jobType1.getId());
        assertThat(jobType1).isEqualTo(jobType2);
        jobType2.setId(2L);
        assertThat(jobType1).isNotEqualTo(jobType2);
        jobType1.setId(null);
        assertThat(jobType1).isNotEqualTo(jobType2);
    }
}
