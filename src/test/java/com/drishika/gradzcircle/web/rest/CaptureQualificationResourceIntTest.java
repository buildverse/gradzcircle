package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CaptureQualification;
import com.drishika.gradzcircle.repository.CaptureQualificationRepository;
import com.drishika.gradzcircle.repository.search.CaptureQualificationSearchRepository;
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
 * Test class for the CaptureQualificationResource REST controller.
 *
 * @see CaptureQualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CaptureQualificationResourceIntTest {

    private static final String DEFAULT_QUALIFICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION_NAME = "BBBBBBBBBB";

    @Autowired
    private CaptureQualificationRepository captureQualificationRepository;

    @Autowired
    private CaptureQualificationSearchRepository captureQualificationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCaptureQualificationMockMvc;

    private CaptureQualification captureQualification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CaptureQualificationResource captureQualificationResource = new CaptureQualificationResource(captureQualificationRepository, captureQualificationSearchRepository);
        this.restCaptureQualificationMockMvc = MockMvcBuilders.standaloneSetup(captureQualificationResource)
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
    public static CaptureQualification createEntity(EntityManager em) {
        CaptureQualification captureQualification = new CaptureQualification()
            .qualificationName(DEFAULT_QUALIFICATION_NAME);
        return captureQualification;
    }

    @Before
    public void initTest() {
        captureQualificationSearchRepository.deleteAll();
        captureQualification = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaptureQualification() throws Exception {
        int databaseSizeBeforeCreate = captureQualificationRepository.findAll().size();

        // Create the CaptureQualification
        restCaptureQualificationMockMvc.perform(post("/api/capture-qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureQualification)))
            .andExpect(status().isCreated());

        // Validate the CaptureQualification in the database
        List<CaptureQualification> captureQualificationList = captureQualificationRepository.findAll();
        assertThat(captureQualificationList).hasSize(databaseSizeBeforeCreate + 1);
        CaptureQualification testCaptureQualification = captureQualificationList.get(captureQualificationList.size() - 1);
        assertThat(testCaptureQualification.getQualificationName()).isEqualTo(DEFAULT_QUALIFICATION_NAME);

        // Validate the CaptureQualification in Elasticsearch
        CaptureQualification captureQualificationEs = captureQualificationSearchRepository.findOne(testCaptureQualification.getId());
        assertThat(captureQualificationEs).isEqualToComparingFieldByField(testCaptureQualification);
    }

    @Test
    @Transactional
    public void createCaptureQualificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = captureQualificationRepository.findAll().size();

        // Create the CaptureQualification with an existing ID
        captureQualification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaptureQualificationMockMvc.perform(post("/api/capture-qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureQualification)))
            .andExpect(status().isBadRequest());

        // Validate the CaptureQualification in the database
        List<CaptureQualification> captureQualificationList = captureQualificationRepository.findAll();
        assertThat(captureQualificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCaptureQualifications() throws Exception {
        // Initialize the database
        captureQualificationRepository.saveAndFlush(captureQualification);

        // Get all the captureQualificationList
        restCaptureQualificationMockMvc.perform(get("/api/capture-qualifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].qualificationName").value(hasItem(DEFAULT_QUALIFICATION_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCaptureQualification() throws Exception {
        // Initialize the database
        captureQualificationRepository.saveAndFlush(captureQualification);

        // Get the captureQualification
        restCaptureQualificationMockMvc.perform(get("/api/capture-qualifications/{id}", captureQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(captureQualification.getId().intValue()))
            .andExpect(jsonPath("$.qualificationName").value(DEFAULT_QUALIFICATION_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaptureQualification() throws Exception {
        // Get the captureQualification
        restCaptureQualificationMockMvc.perform(get("/api/capture-qualifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaptureQualification() throws Exception {
        // Initialize the database
        captureQualificationRepository.saveAndFlush(captureQualification);
        captureQualificationSearchRepository.save(captureQualification);
        int databaseSizeBeforeUpdate = captureQualificationRepository.findAll().size();

        // Update the captureQualification
        CaptureQualification updatedCaptureQualification = captureQualificationRepository.findOne(captureQualification.getId());
        updatedCaptureQualification
            .qualificationName(UPDATED_QUALIFICATION_NAME);

        restCaptureQualificationMockMvc.perform(put("/api/capture-qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaptureQualification)))
            .andExpect(status().isOk());

        // Validate the CaptureQualification in the database
        List<CaptureQualification> captureQualificationList = captureQualificationRepository.findAll();
        assertThat(captureQualificationList).hasSize(databaseSizeBeforeUpdate);
        CaptureQualification testCaptureQualification = captureQualificationList.get(captureQualificationList.size() - 1);
        assertThat(testCaptureQualification.getQualificationName()).isEqualTo(UPDATED_QUALIFICATION_NAME);

        // Validate the CaptureQualification in Elasticsearch
        CaptureQualification captureQualificationEs = captureQualificationSearchRepository.findOne(testCaptureQualification.getId());
        assertThat(captureQualificationEs).isEqualToComparingFieldByField(testCaptureQualification);
    }

    @Test
    @Transactional
    public void updateNonExistingCaptureQualification() throws Exception {
        int databaseSizeBeforeUpdate = captureQualificationRepository.findAll().size();

        // Create the CaptureQualification

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCaptureQualificationMockMvc.perform(put("/api/capture-qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureQualification)))
            .andExpect(status().isCreated());

        // Validate the CaptureQualification in the database
        List<CaptureQualification> captureQualificationList = captureQualificationRepository.findAll();
        assertThat(captureQualificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCaptureQualification() throws Exception {
        // Initialize the database
        captureQualificationRepository.saveAndFlush(captureQualification);
        captureQualificationSearchRepository.save(captureQualification);
        int databaseSizeBeforeDelete = captureQualificationRepository.findAll().size();

        // Get the captureQualification
        restCaptureQualificationMockMvc.perform(delete("/api/capture-qualifications/{id}", captureQualification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean captureQualificationExistsInEs = captureQualificationSearchRepository.exists(captureQualification.getId());
        assertThat(captureQualificationExistsInEs).isFalse();

        // Validate the database is empty
        List<CaptureQualification> captureQualificationList = captureQualificationRepository.findAll();
        assertThat(captureQualificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCaptureQualification() throws Exception {
        // Initialize the database
        captureQualificationRepository.saveAndFlush(captureQualification);
        captureQualificationSearchRepository.save(captureQualification);

        // Search the captureQualification
        restCaptureQualificationMockMvc.perform(get("/api/_search/capture-qualifications?query=id:" + captureQualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureQualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].qualificationName").value(hasItem(DEFAULT_QUALIFICATION_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaptureQualification.class);
        CaptureQualification captureQualification1 = new CaptureQualification();
        captureQualification1.setId(1L);
        CaptureQualification captureQualification2 = new CaptureQualification();
        captureQualification2.setId(captureQualification1.getId());
        assertThat(captureQualification1).isEqualTo(captureQualification2);
        captureQualification2.setId(2L);
        assertThat(captureQualification1).isNotEqualTo(captureQualification2);
        captureQualification1.setId(null);
        assertThat(captureQualification1).isNotEqualTo(captureQualification2);
    }
}
