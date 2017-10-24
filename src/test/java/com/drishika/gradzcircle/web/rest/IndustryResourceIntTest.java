package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Industry;
import com.drishika.gradzcircle.repository.IndustryRepository;
import com.drishika.gradzcircle.repository.search.IndustrySearchRepository;
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
 * Test class for the IndustryResource REST controller.
 *
 * @see IndustryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class IndustryResourceIntTest {

    private static final String DEFAULT_INDUSTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY_NAME = "BBBBBBBBBB";

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private IndustrySearchRepository industrySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndustryMockMvc;

    private Industry industry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndustryResource industryResource = new IndustryResource(industryRepository, industrySearchRepository);
        this.restIndustryMockMvc = MockMvcBuilders.standaloneSetup(industryResource)
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
    public static Industry createEntity(EntityManager em) {
        Industry industry = new Industry()
            .industryName(DEFAULT_INDUSTRY_NAME);
        return industry;
    }

    @Before
    public void initTest() {
        industrySearchRepository.deleteAll();
        industry = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndustry() throws Exception {
        int databaseSizeBeforeCreate = industryRepository.findAll().size();

        // Create the Industry
        restIndustryMockMvc.perform(post("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industry)))
            .andExpect(status().isCreated());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeCreate + 1);
        Industry testIndustry = industryList.get(industryList.size() - 1);
        assertThat(testIndustry.getIndustryName()).isEqualTo(DEFAULT_INDUSTRY_NAME);

        // Validate the Industry in Elasticsearch
        Industry industryEs = industrySearchRepository.findOne(testIndustry.getId());
        assertThat(industryEs).isEqualToComparingFieldByField(testIndustry);
    }

    @Test
    @Transactional
    public void createIndustryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = industryRepository.findAll().size();

        // Create the Industry with an existing ID
        industry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndustryMockMvc.perform(post("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industry)))
            .andExpect(status().isBadRequest());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIndustries() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);

        // Get all the industryList
        restIndustryMockMvc.perform(get("/api/industries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industry.getId().intValue())))
            .andExpect(jsonPath("$.[*].industryName").value(hasItem(DEFAULT_INDUSTRY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);

        // Get the industry
        restIndustryMockMvc.perform(get("/api/industries/{id}", industry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(industry.getId().intValue()))
            .andExpect(jsonPath("$.industryName").value(DEFAULT_INDUSTRY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIndustry() throws Exception {
        // Get the industry
        restIndustryMockMvc.perform(get("/api/industries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);
        industrySearchRepository.save(industry);
        int databaseSizeBeforeUpdate = industryRepository.findAll().size();

        // Update the industry
        Industry updatedIndustry = industryRepository.findOne(industry.getId());
        updatedIndustry
            .industryName(UPDATED_INDUSTRY_NAME);

        restIndustryMockMvc.perform(put("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndustry)))
            .andExpect(status().isOk());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeUpdate);
        Industry testIndustry = industryList.get(industryList.size() - 1);
        assertThat(testIndustry.getIndustryName()).isEqualTo(UPDATED_INDUSTRY_NAME);

        // Validate the Industry in Elasticsearch
        Industry industryEs = industrySearchRepository.findOne(testIndustry.getId());
        assertThat(industryEs).isEqualToComparingFieldByField(testIndustry);
    }

    @Test
    @Transactional
    public void updateNonExistingIndustry() throws Exception {
        int databaseSizeBeforeUpdate = industryRepository.findAll().size();

        // Create the Industry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndustryMockMvc.perform(put("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industry)))
            .andExpect(status().isCreated());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);
        industrySearchRepository.save(industry);
        int databaseSizeBeforeDelete = industryRepository.findAll().size();

        // Get the industry
        restIndustryMockMvc.perform(delete("/api/industries/{id}", industry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean industryExistsInEs = industrySearchRepository.exists(industry.getId());
        assertThat(industryExistsInEs).isFalse();

        // Validate the database is empty
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);
        industrySearchRepository.save(industry);

        // Search the industry
        restIndustryMockMvc.perform(get("/api/_search/industries?query=id:" + industry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industry.getId().intValue())))
            .andExpect(jsonPath("$.[*].industryName").value(hasItem(DEFAULT_INDUSTRY_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Industry.class);
        Industry industry1 = new Industry();
        industry1.setId(1L);
        Industry industry2 = new Industry();
        industry2.setId(industry1.getId());
        assertThat(industry1).isEqualTo(industry2);
        industry2.setId(2L);
        assertThat(industry1).isNotEqualTo(industry2);
        industry1.setId(null);
        assertThat(industry1).isNotEqualTo(industry2);
    }
}
