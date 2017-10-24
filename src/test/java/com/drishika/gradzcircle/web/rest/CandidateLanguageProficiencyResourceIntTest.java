package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
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
 * Test class for the CandidateLanguageProficiencyResource REST controller.
 *
 * @see CandidateLanguageProficiencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateLanguageProficiencyResourceIntTest {

    private static final String DEFAULT_PROFICIENCY = "AAAAAAAAAA";
    private static final String UPDATED_PROFICIENCY = "BBBBBBBBBB";

    @Autowired
    private CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

    @Autowired
    private CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCandidateLanguageProficiencyMockMvc;

    private CandidateLanguageProficiency candidateLanguageProficiency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CandidateLanguageProficiencyResource candidateLanguageProficiencyResource = new CandidateLanguageProficiencyResource(candidateLanguageProficiencyRepository, candidateLanguageProficiencySearchRepository);
        this.restCandidateLanguageProficiencyMockMvc = MockMvcBuilders.standaloneSetup(candidateLanguageProficiencyResource)
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
    public static CandidateLanguageProficiency createEntity(EntityManager em) {
        CandidateLanguageProficiency candidateLanguageProficiency = new CandidateLanguageProficiency()
            .proficiency(DEFAULT_PROFICIENCY);
        return candidateLanguageProficiency;
    }

    @Before
    public void initTest() {
        candidateLanguageProficiencySearchRepository.deleteAll();
        candidateLanguageProficiency = createEntity(em);
    }

    @Test
    @Transactional
    public void createCandidateLanguageProficiency() throws Exception {
        int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();

        // Create the CandidateLanguageProficiency
        restCandidateLanguageProficiencyMockMvc.perform(post("/api/candidate-language-proficiencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
            .andExpect(status().isCreated());

        // Validate the CandidateLanguageProficiency in the database
        List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository.findAll();
        assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeCreate + 1);
        CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList.get(candidateLanguageProficiencyList.size() - 1);
        assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);

        // Validate the CandidateLanguageProficiency in Elasticsearch
        CandidateLanguageProficiency candidateLanguageProficiencyEs = candidateLanguageProficiencySearchRepository.findOne(testCandidateLanguageProficiency.getId());
        assertThat(candidateLanguageProficiencyEs).isEqualToComparingFieldByField(testCandidateLanguageProficiency);
    }

    @Test
    @Transactional
    public void createCandidateLanguageProficiencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();

        // Create the CandidateLanguageProficiency with an existing ID
        candidateLanguageProficiency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateLanguageProficiencyMockMvc.perform(post("/api/candidate-language-proficiencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
            .andExpect(status().isBadRequest());

        // Validate the CandidateLanguageProficiency in the database
        List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository.findAll();
        assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCandidateLanguageProficiencies() throws Exception {
        // Initialize the database
        candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);

        // Get all the candidateLanguageProficiencyList
        restCandidateLanguageProficiencyMockMvc.perform(get("/api/candidate-language-proficiencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateLanguageProficiency.getId().intValue())))
            .andExpect(jsonPath("$.[*].proficiency").value(hasItem(DEFAULT_PROFICIENCY.toString())));
    }

    @Test
    @Transactional
    public void getCandidateLanguageProficiency() throws Exception {
        // Initialize the database
        candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);

        // Get the candidateLanguageProficiency
        restCandidateLanguageProficiencyMockMvc.perform(get("/api/candidate-language-proficiencies/{id}", candidateLanguageProficiency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(candidateLanguageProficiency.getId().intValue()))
            .andExpect(jsonPath("$.proficiency").value(DEFAULT_PROFICIENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCandidateLanguageProficiency() throws Exception {
        // Get the candidateLanguageProficiency
        restCandidateLanguageProficiencyMockMvc.perform(get("/api/candidate-language-proficiencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidateLanguageProficiency() throws Exception {
        // Initialize the database
        candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
        candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);
        int databaseSizeBeforeUpdate = candidateLanguageProficiencyRepository.findAll().size();

        // Update the candidateLanguageProficiency
        CandidateLanguageProficiency updatedCandidateLanguageProficiency = candidateLanguageProficiencyRepository.findOne(candidateLanguageProficiency.getId());
        updatedCandidateLanguageProficiency
            .proficiency(UPDATED_PROFICIENCY);

        restCandidateLanguageProficiencyMockMvc.perform(put("/api/candidate-language-proficiencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCandidateLanguageProficiency)))
            .andExpect(status().isOk());

        // Validate the CandidateLanguageProficiency in the database
        List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository.findAll();
        assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeUpdate);
        CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList.get(candidateLanguageProficiencyList.size() - 1);
        assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(UPDATED_PROFICIENCY);

        // Validate the CandidateLanguageProficiency in Elasticsearch
        CandidateLanguageProficiency candidateLanguageProficiencyEs = candidateLanguageProficiencySearchRepository.findOne(testCandidateLanguageProficiency.getId());
        assertThat(candidateLanguageProficiencyEs).isEqualToComparingFieldByField(testCandidateLanguageProficiency);
    }

    @Test
    @Transactional
    public void updateNonExistingCandidateLanguageProficiency() throws Exception {
        int databaseSizeBeforeUpdate = candidateLanguageProficiencyRepository.findAll().size();

        // Create the CandidateLanguageProficiency

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCandidateLanguageProficiencyMockMvc.perform(put("/api/candidate-language-proficiencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
            .andExpect(status().isCreated());

        // Validate the CandidateLanguageProficiency in the database
        List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository.findAll();
        assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCandidateLanguageProficiency() throws Exception {
        // Initialize the database
        candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
        candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);
        int databaseSizeBeforeDelete = candidateLanguageProficiencyRepository.findAll().size();

        // Get the candidateLanguageProficiency
        restCandidateLanguageProficiencyMockMvc.perform(delete("/api/candidate-language-proficiencies/{id}", candidateLanguageProficiency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean candidateLanguageProficiencyExistsInEs = candidateLanguageProficiencySearchRepository.exists(candidateLanguageProficiency.getId());
        assertThat(candidateLanguageProficiencyExistsInEs).isFalse();

        // Validate the database is empty
        List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository.findAll();
        assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCandidateLanguageProficiency() throws Exception {
        // Initialize the database
        candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
        candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);

        // Search the candidateLanguageProficiency
        restCandidateLanguageProficiencyMockMvc.perform(get("/api/_search/candidate-language-proficiencies?query=id:" + candidateLanguageProficiency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateLanguageProficiency.getId().intValue())))
            .andExpect(jsonPath("$.[*].proficiency").value(hasItem(DEFAULT_PROFICIENCY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateLanguageProficiency.class);
        CandidateLanguageProficiency candidateLanguageProficiency1 = new CandidateLanguageProficiency();
        candidateLanguageProficiency1.setId(1L);
        CandidateLanguageProficiency candidateLanguageProficiency2 = new CandidateLanguageProficiency();
        candidateLanguageProficiency2.setId(candidateLanguageProficiency1.getId());
        assertThat(candidateLanguageProficiency1).isEqualTo(candidateLanguageProficiency2);
        candidateLanguageProficiency2.setId(2L);
        assertThat(candidateLanguageProficiency1).isNotEqualTo(candidateLanguageProficiency2);
        candidateLanguageProficiency1.setId(null);
        assertThat(candidateLanguageProficiency1).isNotEqualTo(candidateLanguageProficiency2);
    }
}
