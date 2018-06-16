
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
import org.junit.Ignore;
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
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.search.JobFilterSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the JobFilterResource REST controller.
 *
 * @see JobFilterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
@Ignore
public class JobFilterResourceIntTest {

    private static final String DEFAULT_FILTER_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private JobFilterRepository jobFilterRepository;

    @Autowired
    private JobFilterSearchRepository jobFilterSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobFilterMockMvc;

    private JobFilter jobFilter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobFilterResource jobFilterResource = new JobFilterResource(jobFilterRepository, jobFilterSearchRepository);
        this.restJobFilterMockMvc = MockMvcBuilders.standaloneSetup(jobFilterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobFilter createEntity(EntityManager em) {
        JobFilter jobFilter = new JobFilter()
            .filterDescription(DEFAULT_FILTER_DESCRIPTION);
        return jobFilter;
    }

    @Before
    public void initTest() {
        jobFilterSearchRepository.deleteAll();
        jobFilter = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobFilter() throws Exception {
        int databaseSizeBeforeCreate = jobFilterRepository.findAll().size();

        // Create the JobFilter
        restJobFilterMockMvc.perform(post("/api/job-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobFilter)))
            .andExpect(status().isCreated());

        // Validate the JobFilter in the database
        List<JobFilter> jobFilterList = jobFilterRepository.findAll();
        assertThat(jobFilterList).hasSize(databaseSizeBeforeCreate + 1);
        JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
        assertThat(testJobFilter.getFilterDescription()).isEqualTo(DEFAULT_FILTER_DESCRIPTION);

        // Validate the JobFilter in Elasticsearch
        JobFilter jobFilterEs = jobFilterSearchRepository.findOne(testJobFilter.getId());
        assertThat(jobFilterEs).isEqualToComparingFieldByField(testJobFilter);
    }

    @Test
    @Transactional
    public void createJobFilterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobFilterRepository.findAll().size();

        // Create the JobFilter with an existing ID
        jobFilter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobFilterMockMvc.perform(post("/api/job-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobFilter)))
            .andExpect(status().isBadRequest());

        // Validate the JobFilter in the database
        List<JobFilter> jobFilterList = jobFilterRepository.findAll();
        assertThat(jobFilterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobFilters() throws Exception {
        // Initialize the database
        jobFilterRepository.saveAndFlush(jobFilter);

        // Get all the jobFilterList
        restJobFilterMockMvc.perform(get("/api/job-filters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobFilter.getId().intValue())))
            .andExpect(jsonPath("$.[*].filterDescription").value(hasItem(DEFAULT_FILTER_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobFilter() throws Exception {
        // Initialize the database
        jobFilterRepository.saveAndFlush(jobFilter);

        // Get the jobFilter
        restJobFilterMockMvc.perform(get("/api/job-filters/{id}", jobFilter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobFilter.getId().intValue()))
            .andExpect(jsonPath("$.filterDescription").value(DEFAULT_FILTER_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobFilter() throws Exception {
        // Get the jobFilter
        restJobFilterMockMvc.perform(get("/api/job-filters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobFilter() throws Exception {
        // Initialize the database
        jobFilterRepository.saveAndFlush(jobFilter);
        jobFilterSearchRepository.save(jobFilter);
        int databaseSizeBeforeUpdate = jobFilterRepository.findAll().size();

        // Update the jobFilter
        JobFilter updatedJobFilter = jobFilterRepository.findOne(jobFilter.getId());
        updatedJobFilter
            .filterDescription(UPDATED_FILTER_DESCRIPTION);

        restJobFilterMockMvc.perform(put("/api/job-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobFilter)))
            .andExpect(status().isOk());

        // Validate the JobFilter in the database
        List<JobFilter> jobFilterList = jobFilterRepository.findAll();
        assertThat(jobFilterList).hasSize(databaseSizeBeforeUpdate);
        JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
        assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER_DESCRIPTION);

        // Validate the JobFilter in Elasticsearch
        JobFilter jobFilterEs = jobFilterSearchRepository.findOne(testJobFilter.getId());
        assertThat(jobFilterEs).isEqualToComparingFieldByField(testJobFilter);
    }

    @Test
    @Transactional
    public void updateNonExistingJobFilter() throws Exception {
        int databaseSizeBeforeUpdate = jobFilterRepository.findAll().size();

        // Create the JobFilter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobFilterMockMvc.perform(put("/api/job-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobFilter)))
            .andExpect(status().isCreated());

        // Validate the JobFilter in the database
        List<JobFilter> jobFilterList = jobFilterRepository.findAll();
        assertThat(jobFilterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobFilter() throws Exception {
        // Initialize the database
        jobFilterRepository.saveAndFlush(jobFilter);
        jobFilterSearchRepository.save(jobFilter);
        int databaseSizeBeforeDelete = jobFilterRepository.findAll().size();

        // Get the jobFilter
        restJobFilterMockMvc.perform(delete("/api/job-filters/{id}", jobFilter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobFilterExistsInEs = jobFilterSearchRepository.exists(jobFilter.getId());
        assertThat(jobFilterExistsInEs).isFalse();

        // Validate the database is empty
        List<JobFilter> jobFilterList = jobFilterRepository.findAll();
        assertThat(jobFilterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobFilter() throws Exception {
        // Initialize the database
        jobFilterRepository.saveAndFlush(jobFilter);
        jobFilterSearchRepository.save(jobFilter);

        // Search the jobFilter
        restJobFilterMockMvc.perform(get("/api/_search/job-filters?query=id:" + jobFilter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobFilter.getId().intValue())))
            .andExpect(jsonPath("$.[*].filterDescription").value(hasItem(DEFAULT_FILTER_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobFilter.class);
        JobFilter jobFilter1 = new JobFilter();
        jobFilter1.setId(1L);
        JobFilter jobFilter2 = new JobFilter();
        jobFilter2.setId(jobFilter1.getId());
        assertThat(jobFilter1).isEqualTo(jobFilter2);
        jobFilter2.setId(2L);
        assertThat(jobFilter1).isNotEqualTo(jobFilter2);
        jobFilter1.setId(null);
        assertThat(jobFilter1).isNotEqualTo(jobFilter2);
    }
}
