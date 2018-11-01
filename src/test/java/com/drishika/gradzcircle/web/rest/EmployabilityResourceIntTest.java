package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Employability;
import com.drishika.gradzcircle.repository.EmployabilityRepository;
import com.drishika.gradzcircle.repository.search.EmployabilitySearchRepository;
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
 * Test class for the EmployabilityResource REST controller.
 *
 * @see EmployabilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class EmployabilityResourceIntTest {

    private static final String DEFAULT_EMPLOYABLE_SKILL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYABLE_SKILL_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EMPLOYABILITY_SCORE = 1;
    private static final Integer UPDATED_EMPLOYABILITY_SCORE = 2;

    private static final Integer DEFAULT_EMPLOYABILITY_PERCENTILE = 1;
    private static final Integer UPDATED_EMPLOYABILITY_PERCENTILE = 2;

    @Autowired
    private EmployabilityRepository employabilityRepository;

    @Autowired
    private EmployabilitySearchRepository employabilitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployabilityMockMvc;

    private Employability employability;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployabilityResource employabilityResource = new EmployabilityResource(employabilityRepository, employabilitySearchRepository);
        this.restEmployabilityMockMvc = MockMvcBuilders.standaloneSetup(employabilityResource)
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
    public static Employability createEntity(EntityManager em) {
        Employability employability = new Employability()
            .employableSkillName(DEFAULT_EMPLOYABLE_SKILL_NAME)
            .employabilityScore(DEFAULT_EMPLOYABILITY_SCORE)
            .employabilityPercentile(DEFAULT_EMPLOYABILITY_PERCENTILE);
        return employability;
    }

    @Before
    public void initTest() {
        employabilitySearchRepository.deleteAll();
        employability = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployability() throws Exception {
        int databaseSizeBeforeCreate = employabilityRepository.findAll().size();

        // Create the Employability
        restEmployabilityMockMvc.perform(post("/api/employabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employability)))
            .andExpect(status().isCreated());

        // Validate the Employability in the database
        List<Employability> employabilityList = employabilityRepository.findAll();
        assertThat(employabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Employability testEmployability = employabilityList.get(employabilityList.size() - 1);
        assertThat(testEmployability.getEmployableSkillName()).isEqualTo(DEFAULT_EMPLOYABLE_SKILL_NAME);
        assertThat(testEmployability.getEmployabilityScore()).isEqualTo(DEFAULT_EMPLOYABILITY_SCORE);
        assertThat(testEmployability.getEmployabilityPercentile()).isEqualTo(DEFAULT_EMPLOYABILITY_PERCENTILE);

        // Validate the Employability in Elasticsearch
        Employability employabilityEs = employabilitySearchRepository.findOne(testEmployability.getId());
        assertThat(employabilityEs).isEqualToIgnoringGivenFields(testEmployability);
    }

    @Test
    @Transactional
    public void createEmployabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employabilityRepository.findAll().size();

        // Create the Employability with an existing ID
        employability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployabilityMockMvc.perform(post("/api/employabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employability)))
            .andExpect(status().isBadRequest());

        // Validate the Employability in the database
        List<Employability> employabilityList = employabilityRepository.findAll();
        assertThat(employabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmployabilities() throws Exception {
        // Initialize the database
        employabilityRepository.saveAndFlush(employability);

        // Get all the employabilityList
        restEmployabilityMockMvc.perform(get("/api/employabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employability.getId().intValue())))
            .andExpect(jsonPath("$.[*].employableSkillName").value(hasItem(DEFAULT_EMPLOYABLE_SKILL_NAME.toString())))
            .andExpect(jsonPath("$.[*].employabilityScore").value(hasItem(DEFAULT_EMPLOYABILITY_SCORE)))
            .andExpect(jsonPath("$.[*].employabilityPercentile").value(hasItem(DEFAULT_EMPLOYABILITY_PERCENTILE)));
    }

    @Test
    @Transactional
    public void getEmployability() throws Exception {
        // Initialize the database
        employabilityRepository.saveAndFlush(employability);

        // Get the employability
        restEmployabilityMockMvc.perform(get("/api/employabilities/{id}", employability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employability.getId().intValue()))
            .andExpect(jsonPath("$.employableSkillName").value(DEFAULT_EMPLOYABLE_SKILL_NAME.toString()))
            .andExpect(jsonPath("$.employabilityScore").value(DEFAULT_EMPLOYABILITY_SCORE))
            .andExpect(jsonPath("$.employabilityPercentile").value(DEFAULT_EMPLOYABILITY_PERCENTILE));
    }

    @Test
    @Transactional
    public void getNonExistingEmployability() throws Exception {
        // Get the employability
        restEmployabilityMockMvc.perform(get("/api/employabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployability() throws Exception {
        // Initialize the database
        employabilityRepository.saveAndFlush(employability);
        employabilitySearchRepository.save(employability);
        int databaseSizeBeforeUpdate = employabilityRepository.findAll().size();

        // Update the employability
        Employability updatedEmployability = employabilityRepository.findOne(employability.getId());
        // Disconnect from session so that the updates on updatedEmployability are not directly saved in db
        em.detach(updatedEmployability);
        updatedEmployability
            .employableSkillName(UPDATED_EMPLOYABLE_SKILL_NAME)
            .employabilityScore(UPDATED_EMPLOYABILITY_SCORE)
            .employabilityPercentile(UPDATED_EMPLOYABILITY_PERCENTILE);

        restEmployabilityMockMvc.perform(put("/api/employabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployability)))
            .andExpect(status().isOk());

        // Validate the Employability in the database
        List<Employability> employabilityList = employabilityRepository.findAll();
        assertThat(employabilityList).hasSize(databaseSizeBeforeUpdate);
        Employability testEmployability = employabilityList.get(employabilityList.size() - 1);
        assertThat(testEmployability.getEmployableSkillName()).isEqualTo(UPDATED_EMPLOYABLE_SKILL_NAME);
        assertThat(testEmployability.getEmployabilityScore()).isEqualTo(UPDATED_EMPLOYABILITY_SCORE);
        assertThat(testEmployability.getEmployabilityPercentile()).isEqualTo(UPDATED_EMPLOYABILITY_PERCENTILE);

        // Validate the Employability in Elasticsearch
        Employability employabilityEs = employabilitySearchRepository.findOne(testEmployability.getId());
        assertThat(employabilityEs).isEqualToIgnoringGivenFields(testEmployability);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployability() throws Exception {
        int databaseSizeBeforeUpdate = employabilityRepository.findAll().size();

        // Create the Employability

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployabilityMockMvc.perform(put("/api/employabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employability)))
            .andExpect(status().isCreated());

        // Validate the Employability in the database
        List<Employability> employabilityList = employabilityRepository.findAll();
        assertThat(employabilityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmployability() throws Exception {
        // Initialize the database
        employabilityRepository.saveAndFlush(employability);
        employabilitySearchRepository.save(employability);
        int databaseSizeBeforeDelete = employabilityRepository.findAll().size();

        // Get the employability
        restEmployabilityMockMvc.perform(delete("/api/employabilities/{id}", employability.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employabilityExistsInEs = employabilitySearchRepository.exists(employability.getId());
        assertThat(employabilityExistsInEs).isFalse();

        // Validate the database is empty
        List<Employability> employabilityList = employabilityRepository.findAll();
        assertThat(employabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmployability() throws Exception {
        // Initialize the database
        employabilityRepository.saveAndFlush(employability);
        employabilitySearchRepository.save(employability);

        // Search the employability
        restEmployabilityMockMvc.perform(get("/api/_search/employabilities?query=id:" + employability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employability.getId().intValue())))
            .andExpect(jsonPath("$.[*].employableSkillName").value(hasItem(DEFAULT_EMPLOYABLE_SKILL_NAME.toString())))
            .andExpect(jsonPath("$.[*].employabilityScore").value(hasItem(DEFAULT_EMPLOYABILITY_SCORE)))
            .andExpect(jsonPath("$.[*].employabilityPercentile").value(hasItem(DEFAULT_EMPLOYABILITY_PERCENTILE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employability.class);
        Employability employability1 = new Employability();
        employability1.setId(1L);
        Employability employability2 = new Employability();
        employability2.setId(employability1.getId());
        assertThat(employability1).isEqualTo(employability2);
        employability2.setId(2L);
        assertThat(employability1).isNotEqualTo(employability2);
        employability1.setId(null);
        assertThat(employability1).isNotEqualTo(employability2);
    }
}
