package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
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
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.search.EmploymentTypeSearchRepository;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the EmploymentTypeResource REST controller.
 *
 * @see EmploymentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class EmploymentTypeResourceIntTest {

    private static final String DEFAULT_EMPLOYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_EMPLOYMENT_TYPE_COST = 1D;
    private static final Double UPDATED_EMPLOYMENT_TYPE_COST = 2D;

    @Autowired
    private EmploymentTypeRepository employmentTypeRepository;

    @Autowired
    private EmploymentTypeSearchRepository employmentTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
    @Autowired
    private GradzcircleCacheManager<String, Map<String,EmploymentType>> cacheManager;

    @Autowired
    private EntityManager em;

    private MockMvc restEmploymentTypeMockMvc;

    private EmploymentType employmentType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmploymentTypeResource employmentTypeResource = new EmploymentTypeResource(employmentTypeRepository, employmentTypeSearchRepository,cacheManager);
        this.restEmploymentTypeMockMvc = MockMvcBuilders.standaloneSetup(employmentTypeResource)
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
    public static EmploymentType createEntity(EntityManager em) {
        EmploymentType employmentType = new EmploymentType()
            .employmentType(DEFAULT_EMPLOYMENT_TYPE)
            .employmentTypeCost(DEFAULT_EMPLOYMENT_TYPE_COST);
        return employmentType;
    }

    @Before
    public void initTest() {
        employmentTypeSearchRepository.deleteAll();
        employmentType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmploymentType() throws Exception {
        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();

        // Create the EmploymentType
        restEmploymentTypeMockMvc.perform(post("/api/employment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employmentType)))
            .andExpect(status().isCreated());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testEmploymentType.getEmploymentTypeCost()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE_COST);

        // Validate the EmploymentType in Elasticsearch
        EmploymentType employmentTypeEs = employmentTypeSearchRepository.findOne(testEmploymentType.getId());
        assertThat(employmentTypeEs).isEqualToIgnoringGivenFields(testEmploymentType);
    }

    @Test
    @Transactional
    public void createEmploymentTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();

        // Create the EmploymentType with an existing ID
        employmentType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentTypeMockMvc.perform(post("/api/employment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employmentType)))
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmploymentTypes() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList
        restEmploymentTypeMockMvc.perform(get("/api/employment-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employmentTypeCost").value(hasItem(DEFAULT_EMPLOYMENT_TYPE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void getEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get the employmentType
        restEmploymentTypeMockMvc.perform(get("/api/employment-types/{id}", employmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employmentType.getId().intValue()))
            .andExpect(jsonPath("$.employmentType").value(DEFAULT_EMPLOYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.employmentTypeCost").value(DEFAULT_EMPLOYMENT_TYPE_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmploymentType() throws Exception {
        // Get the employmentType
        restEmploymentTypeMockMvc.perform(get("/api/employment-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);
        employmentTypeSearchRepository.save(employmentType);
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType
        EmploymentType updatedEmploymentType = employmentTypeRepository.findOne(employmentType.getId());
        // Disconnect from session so that the updates on updatedEmploymentType are not directly saved in db
        em.detach(updatedEmploymentType);
        updatedEmploymentType
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .employmentTypeCost(UPDATED_EMPLOYMENT_TYPE_COST);

        restEmploymentTypeMockMvc.perform(put("/api/employment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmploymentType)))
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testEmploymentType.getEmploymentTypeCost()).isEqualTo(UPDATED_EMPLOYMENT_TYPE_COST);

        // Validate the EmploymentType in Elasticsearch
        EmploymentType employmentTypeEs = employmentTypeSearchRepository.findOne(testEmploymentType.getId());
        assertThat(employmentTypeEs).isEqualToIgnoringGivenFields(testEmploymentType);
    }

    @Test
    @Transactional
    public void updateNonExistingEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Create the EmploymentType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmploymentTypeMockMvc.perform(put("/api/employment-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employmentType)))
            .andExpect(status().isCreated());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);
        employmentTypeSearchRepository.save(employmentType);
        int databaseSizeBeforeDelete = employmentTypeRepository.findAll().size();

        // Get the employmentType
        restEmploymentTypeMockMvc.perform(delete("/api/employment-types/{id}", employmentType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employmentTypeExistsInEs = employmentTypeSearchRepository.exists(employmentType.getId());
        assertThat(employmentTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);
        employmentTypeSearchRepository.save(employmentType);

        // Search the employmentType
        restEmploymentTypeMockMvc.perform(get("/api/_search/employment-types?query=id:" + employmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employmentTypeCost").value(hasItem(DEFAULT_EMPLOYMENT_TYPE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentType.class);
        EmploymentType employmentType1 = new EmploymentType();
        employmentType1.setId(1L);
        EmploymentType employmentType2 = new EmploymentType();
        employmentType2.setId(employmentType1.getId());
        assertThat(employmentType1).isEqualTo(employmentType2);
        employmentType2.setId(2L);
        assertThat(employmentType1).isNotEqualTo(employmentType2);
        employmentType1.setId(null);
        assertThat(employmentType1).isNotEqualTo(employmentType2);
    }
}
