package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.repository.NationalityRepository;
import com.drishika.gradzcircle.repository.search.NationalitySearchRepository;
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
 * Test class for the NationalityResource REST controller.
 *
 * @see NationalityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class NationalityResourceIntTest {

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    @Autowired
    private NationalityRepository nationalityRepository;

    @Autowired
    private NationalitySearchRepository nationalitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNationalityMockMvc;

    private Nationality nationality;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NationalityResource nationalityResource = new NationalityResource(nationalityRepository, nationalitySearchRepository);
        this.restNationalityMockMvc = MockMvcBuilders.standaloneSetup(nationalityResource)
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
    public static Nationality createEntity(EntityManager em) {
        Nationality nationality = new Nationality()
            .nationality(DEFAULT_NATIONALITY);
        return nationality;
    }

    @Before
    public void initTest() {
        nationalitySearchRepository.deleteAll();
        nationality = createEntity(em);
    }

    @Test
    @Transactional
    public void createNationality() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

        // Create the Nationality
        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate + 1);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationality()).isEqualTo(DEFAULT_NATIONALITY);

        // Validate the Nationality in Elasticsearch
        Nationality nationalityEs = nationalitySearchRepository.findOne(testNationality.getId());
        assertThat(nationalityEs).isEqualToComparingFieldByField(testNationality);
    }

    @Test
    @Transactional
    public void createNationalityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

        // Create the Nationality with an existing ID
        nationality.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalityMockMvc.perform(post("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNationalities() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get all the nationalityList
        restNationalityMockMvc.perform(get("/api/nationalities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())));
    }

    @Test
    @Transactional
    public void getNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get the nationality
        restNationalityMockMvc.perform(get("/api/nationalities/{id}", nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nationality.getId().intValue()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNationality() throws Exception {
        // Get the nationality
        restNationalityMockMvc.perform(get("/api/nationalities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);
        nationalitySearchRepository.save(nationality);
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality
        Nationality updatedNationality = nationalityRepository.findOne(nationality.getId());
        updatedNationality
            .nationality(UPDATED_NATIONALITY);

        restNationalityMockMvc.perform(put("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNationality)))
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationality()).isEqualTo(UPDATED_NATIONALITY);

        // Validate the Nationality in Elasticsearch
        Nationality nationalityEs = nationalitySearchRepository.findOne(testNationality.getId());
        assertThat(nationalityEs).isEqualToComparingFieldByField(testNationality);
    }

    @Test
    @Transactional
    public void updateNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Create the Nationality

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNationalityMockMvc.perform(put("/api/nationalities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);
        nationalitySearchRepository.save(nationality);
        int databaseSizeBeforeDelete = nationalityRepository.findAll().size();

        // Get the nationality
        restNationalityMockMvc.perform(delete("/api/nationalities/{id}", nationality.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean nationalityExistsInEs = nationalitySearchRepository.exists(nationality.getId());
        assertThat(nationalityExistsInEs).isFalse();

        // Validate the database is empty
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);
        nationalitySearchRepository.save(nationality);

        // Search the nationality
        restNationalityMockMvc.perform(get("/api/_search/nationalities?query=id:" + nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nationality.class);
        Nationality nationality1 = new Nationality();
        nationality1.setId(1L);
        Nationality nationality2 = new Nationality();
        nationality2.setId(nationality1.getId());
        assertThat(nationality1).isEqualTo(nationality2);
        nationality2.setId(2L);
        assertThat(nationality1).isNotEqualTo(nationality2);
        nationality1.setId(null);
        assertThat(nationality1).isNotEqualTo(nationality2);
    }
}
