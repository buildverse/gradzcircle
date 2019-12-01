package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

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
import com.drishika.gradzcircle.domain.CaptureUniversity;
import com.drishika.gradzcircle.repository.CaptureUniversityRepository;
import com.drishika.gradzcircle.repository.search.CaptureUniversitySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CaptureUniversityResource REST controller.
 *
 * @see CaptureUniversityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CaptureUniversityResourceIntTest {

    private static final String DEFAULT_UNIVERSITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSITY_NAME = "BBBBBBBBBB";

    @Autowired
    private CaptureUniversityRepository captureUniversityRepository;

    @Autowired
    private CaptureUniversitySearchRepository mockCaptureUniversitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCaptureUniversityMockMvc;

    private CaptureUniversity captureUniversity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CaptureUniversityResource captureUniversityResource = new CaptureUniversityResource(captureUniversityRepository, mockCaptureUniversitySearchRepository);
        this.restCaptureUniversityMockMvc = MockMvcBuilders.standaloneSetup(captureUniversityResource)
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
    public static CaptureUniversity createEntity(EntityManager em) {
        CaptureUniversity captureUniversity = new CaptureUniversity()
            .universityName(DEFAULT_UNIVERSITY_NAME);
        return captureUniversity;
    }

    @Before
    public void initTest() {
       // captureUniversitySearchRepository.deleteAll();
        captureUniversity = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaptureUniversity() throws Exception {
        int databaseSizeBeforeCreate = captureUniversityRepository.findAll().size();

        // Create the CaptureUniversity
        restCaptureUniversityMockMvc.perform(post("/api/capture-universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureUniversity)))
            .andExpect(status().isCreated());

        // Validate the CaptureUniversity in the database
        List<CaptureUniversity> captureUniversityList = captureUniversityRepository.findAll();
        assertThat(captureUniversityList).hasSize(databaseSizeBeforeCreate + 1);
        CaptureUniversity testCaptureUniversity = captureUniversityList.get(captureUniversityList.size() - 1);
        assertThat(testCaptureUniversity.getUniversityName()).isEqualTo(DEFAULT_UNIVERSITY_NAME);

        // Validate the CaptureUniversity in Elasticsearch
        verify(mockCaptureUniversitySearchRepository,times(1)).save(testCaptureUniversity);
        
    }

    @Test
    @Transactional
    public void createCaptureUniversityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = captureUniversityRepository.findAll().size();

        // Create the CaptureUniversity with an existing ID
        captureUniversity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaptureUniversityMockMvc.perform(post("/api/capture-universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureUniversity)))
            .andExpect(status().isBadRequest());

        // Validate the CaptureUniversity in the database
        List<CaptureUniversity> captureUniversityList = captureUniversityRepository.findAll();
        assertThat(captureUniversityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCaptureUniversities() throws Exception {
        // Initialize the database
        captureUniversityRepository.saveAndFlush(captureUniversity);

        // Get all the captureUniversityList
        restCaptureUniversityMockMvc.perform(get("/api/capture-universities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureUniversity.getId().intValue())))
            .andExpect(jsonPath("$.[*].universityName").value(hasItem(DEFAULT_UNIVERSITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCaptureUniversity() throws Exception {
        // Initialize the database
        captureUniversityRepository.saveAndFlush(captureUniversity);

        // Get the captureUniversity
        restCaptureUniversityMockMvc.perform(get("/api/capture-universities/{id}", captureUniversity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(captureUniversity.getId().intValue()))
            .andExpect(jsonPath("$.universityName").value(DEFAULT_UNIVERSITY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaptureUniversity() throws Exception {
        // Get the captureUniversity
        restCaptureUniversityMockMvc.perform(get("/api/capture-universities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaptureUniversity() throws Exception {
        // Initialize the database
        captureUniversityRepository.saveAndFlush(captureUniversity);
      //  captureUniversitySearchRepository.save(captureUniversity);
        int databaseSizeBeforeUpdate = captureUniversityRepository.findAll().size();

        // Update the captureUniversity
        CaptureUniversity updatedCaptureUniversity = captureUniversityRepository.findById(captureUniversity.getId()).get();
        // Disconnect from session so that the updates on updatedCaptureUniversity are not directly saved in db
        em.detach(updatedCaptureUniversity);
        updatedCaptureUniversity
            .universityName(UPDATED_UNIVERSITY_NAME);

        restCaptureUniversityMockMvc.perform(put("/api/capture-universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaptureUniversity)))
            .andExpect(status().isOk());

        // Validate the CaptureUniversity in the database
        List<CaptureUniversity> captureUniversityList = captureUniversityRepository.findAll();
        assertThat(captureUniversityList).hasSize(databaseSizeBeforeUpdate);
        CaptureUniversity testCaptureUniversity = captureUniversityList.get(captureUniversityList.size() - 1);
        assertThat(testCaptureUniversity.getUniversityName()).isEqualTo(UPDATED_UNIVERSITY_NAME);

        // Validate the CaptureUniversity in Elasticsearch
        verify(mockCaptureUniversitySearchRepository,times(1)).save(testCaptureUniversity);
    }

    @Test
    @Transactional
    public void updateNonExistingCaptureUniversity() throws Exception {
        int databaseSizeBeforeUpdate = captureUniversityRepository.findAll().size();

        // Create the CaptureUniversity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        
        restCaptureUniversityMockMvc.perform(put("/api/capture-universities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(captureUniversity)))
                .andExpect(status().isBadRequest());


        // Validate the CaptureUniversity in the database
        List<CaptureUniversity> captureUniversityList = captureUniversityRepository.findAll();
        assertThat(captureUniversityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCaptureUniversity() throws Exception {
        // Initialize the database
        captureUniversityRepository.saveAndFlush(captureUniversity);

        int databaseSizeBeforeDelete = captureUniversityRepository.findAll().size();

        // Get the captureUniversity
        restCaptureUniversityMockMvc.perform(delete("/api/capture-universities/{id}", captureUniversity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        verify(mockCaptureUniversitySearchRepository,times(1)).deleteById(captureUniversity.getId());

        // Validate the database is empty
        List<CaptureUniversity> captureUniversityList = captureUniversityRepository.findAll();
        assertThat(captureUniversityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCaptureUniversity() throws Exception {
        // Initialize the database
        captureUniversityRepository.saveAndFlush(captureUniversity);
 
        when(mockCaptureUniversitySearchRepository.search(queryStringQuery("id:" + captureUniversity.getId())))
        .thenReturn(Collections.singletonList(captureUniversity));
        
        // Search the captureUniversity
        restCaptureUniversityMockMvc.perform(get("/api/_search/capture-universities?query=id:" + captureUniversity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureUniversity.getId().intValue())))
            .andExpect(jsonPath("$.[*].universityName").value(hasItem(DEFAULT_UNIVERSITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaptureUniversity.class);
        CaptureUniversity captureUniversity1 = new CaptureUniversity();
        captureUniversity1.setId(1L);
        CaptureUniversity captureUniversity2 = new CaptureUniversity();
        captureUniversity2.setId(captureUniversity1.getId());
        assertThat(captureUniversity1).isEqualTo(captureUniversity2);
        captureUniversity2.setId(2L);
        assertThat(captureUniversity1).isNotEqualTo(captureUniversity2);
        captureUniversity1.setId(null);
        assertThat(captureUniversity1).isNotEqualTo(captureUniversity2);
    }
}
