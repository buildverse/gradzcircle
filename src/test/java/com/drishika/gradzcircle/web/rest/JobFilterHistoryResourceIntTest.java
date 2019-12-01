package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.search.JobFilterHistorySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import java.util.Collections;
import java.util.List;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobFilterHistoryResource REST controller.
 *
 * @see JobFilterHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobFilterHistoryResourceIntTest {

    private static final String DEFAULT_FILTER_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private JobFilterHistoryRepository jobFilterHistoryRepository;

    @Autowired
    private JobFilterHistorySearchRepository mockJobFilterHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobFilterHistoryMockMvc;

    private JobFilterHistory jobFilterHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobFilterHistoryResource jobFilterHistoryResource = new JobFilterHistoryResource(jobFilterHistoryRepository, mockJobFilterHistorySearchRepository);
        this.restJobFilterHistoryMockMvc = MockMvcBuilders.standaloneSetup(jobFilterHistoryResource)
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
    public static JobFilterHistory createEntity(EntityManager em) {
        JobFilterHistory jobFilterHistory = new JobFilterHistory()
            .filterDescription(DEFAULT_FILTER_DESCRIPTION);
        return jobFilterHistory;
    }

    @Before
    public void initTest() {
        //jobFilterHistorySearchRepository.deleteAll();
        jobFilterHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobFilterHistory() throws Exception {
        int databaseSizeBeforeCreate = jobFilterHistoryRepository.findAll().size();

        // Create the JobFilterHistory
        restJobFilterHistoryMockMvc.perform(post("/api/job-filter-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobFilterHistory)))
            .andExpect(status().isCreated());

        // Validate the JobFilterHistory in the database
        List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
        assertThat(jobFilterHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobFilterHistory testJobFilterHistory = jobFilterHistoryList.get(jobFilterHistoryList.size() - 1);
        assertThat(testJobFilterHistory.getFilterDescription()).isEqualTo(DEFAULT_FILTER_DESCRIPTION);

       verify(mockJobFilterHistorySearchRepository,times(1)).save(testJobFilterHistory);
    }

    @Test
    @Transactional
    public void createJobFilterHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobFilterHistoryRepository.findAll().size();

        // Create the JobFilterHistory with an existing ID
        jobFilterHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobFilterHistoryMockMvc.perform(post("/api/job-filter-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobFilterHistory)))
            .andExpect(status().isBadRequest());

        // Validate the JobFilterHistory in the database
        List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
        assertThat(jobFilterHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobFilterHistories() throws Exception {
        // Initialize the database
        jobFilterHistoryRepository.saveAndFlush(jobFilterHistory);

        // Get all the jobFilterHistoryList
        restJobFilterHistoryMockMvc.perform(get("/api/job-filter-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobFilterHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].filterDescription").value(hasItem(DEFAULT_FILTER_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobFilterHistory() throws Exception {
        // Initialize the database
        jobFilterHistoryRepository.saveAndFlush(jobFilterHistory);

        // Get the jobFilterHistory
        restJobFilterHistoryMockMvc.perform(get("/api/job-filter-histories/{id}", jobFilterHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobFilterHistory.getId().intValue()))
            .andExpect(jsonPath("$.filterDescription").value(DEFAULT_FILTER_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobFilterHistory() throws Exception {
        // Get the jobFilterHistory
        restJobFilterHistoryMockMvc.perform(get("/api/job-filter-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobFilterHistory() throws Exception {
        // Initialize the database
        jobFilterHistoryRepository.saveAndFlush(jobFilterHistory);
       // jobFilterHistorySearchRepository.save(jobFilterHistory);
        int databaseSizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();

        // Update the jobFilterHistory
        JobFilterHistory updatedJobFilterHistory = jobFilterHistoryRepository.findById(jobFilterHistory.getId()).get();
        // Disconnect from session so that the updates on updatedJobFilterHistory are not directly saved in db
        em.detach(updatedJobFilterHistory);
        updatedJobFilterHistory
            .filterDescription(UPDATED_FILTER_DESCRIPTION);

        restJobFilterHistoryMockMvc.perform(put("/api/job-filter-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobFilterHistory)))
            .andExpect(status().isOk());

        // Validate the JobFilterHistory in the database
        List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
        assertThat(jobFilterHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobFilterHistory testJobFilterHistory = jobFilterHistoryList.get(jobFilterHistoryList.size() - 1);
        assertThat(testJobFilterHistory.getFilterDescription()).isEqualTo(UPDATED_FILTER_DESCRIPTION);

        // Validate the JobFilterHistory in Elasticsearch
        verify(mockJobFilterHistorySearchRepository,times(1)).save(testJobFilterHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingJobFilterHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();

        // Create the JobFilterHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        
        restJobFilterHistoryMockMvc.perform(put("/api/job-filter-histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobFilterHistory)))
                .andExpect(status().isBadRequest());

        // Validate the JobFilterHistory in the database
        List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
        assertThat(jobFilterHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobFilterHistory() throws Exception {
        // Initialize the database
        jobFilterHistoryRepository.saveAndFlush(jobFilterHistory);
        //jobFilterHistorySearchRepository.save(jobFilterHistory);
        int databaseSizeBeforeDelete = jobFilterHistoryRepository.findAll().size();

        // Get the jobFilterHistory
        restJobFilterHistoryMockMvc.perform(delete("/api/job-filter-histories/{id}", jobFilterHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        verify(mockJobFilterHistorySearchRepository,times(1)).deleteById(jobFilterHistory.getId());

        // Validate the database is empty
        List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
        assertThat(jobFilterHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobFilterHistory() throws Exception {
        // Initialize the database
        jobFilterHistoryRepository.saveAndFlush(jobFilterHistory);
        when(mockJobFilterHistorySearchRepository.search(queryStringQuery("id:" + jobFilterHistory.getId())))
        .thenReturn(Collections.singletonList(jobFilterHistory));

        // Search the jobFilterHistory
        restJobFilterHistoryMockMvc.perform(get("/api/_search/job-filter-histories?query=id:" + jobFilterHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobFilterHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].filterDescription").value(hasItem(DEFAULT_FILTER_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobFilterHistory.class);
        JobFilterHistory jobFilterHistory1 = new JobFilterHistory();
        jobFilterHistory1.setId(1L);
        JobFilterHistory jobFilterHistory2 = new JobFilterHistory();
        jobFilterHistory2.setId(jobFilterHistory1.getId());
        assertThat(jobFilterHistory1).isEqualTo(jobFilterHistory2);
        jobFilterHistory2.setId(2L);
        assertThat(jobFilterHistory1).isNotEqualTo(jobFilterHistory2);
        jobFilterHistory1.setId(null);
        assertThat(jobFilterHistory1).isNotEqualTo(jobFilterHistory2);
    }
}
