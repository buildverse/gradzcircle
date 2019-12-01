package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import static org.mockito.Mockito.*;
import com.drishika.gradzcircle.domain.VisaType;
import com.drishika.gradzcircle.repository.VisaTypeRepository;
import com.drishika.gradzcircle.repository.search.VisaTypeSearchRepository;
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

import java.util.Collections;
import java.util.List;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VisaTypeResource REST controller.
 *
 * @see VisaTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class VisaTypeResourceIntTest {

    private static final String DEFAULT_VISA = "AAAAAAAAAA";
    private static final String UPDATED_VISA = "BBBBBBBBBB";

    @Autowired
    private VisaTypeRepository visaTypeRepository;

    @Autowired
    private VisaTypeSearchRepository mockVisaTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVisaTypeMockMvc;

    private VisaType visaType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VisaTypeResource visaTypeResource = new VisaTypeResource(visaTypeRepository, mockVisaTypeSearchRepository);
        this.restVisaTypeMockMvc = MockMvcBuilders.standaloneSetup(visaTypeResource)
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
    public static VisaType createEntity(EntityManager em) {
        VisaType visaType = new VisaType()
            .visa(DEFAULT_VISA);
        return visaType;
    }

    @Before
    public void initTest() {
        //visaTypeSearchRepository.deleteAll();
        visaType = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisaType() throws Exception {
        int databaseSizeBeforeCreate = visaTypeRepository.findAll().size();

        // Create the VisaType
        restVisaTypeMockMvc.perform(post("/api/visa-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visaType)))
            .andExpect(status().isCreated());

        // Validate the VisaType in the database
        List<VisaType> visaTypeList = visaTypeRepository.findAll();
        assertThat(visaTypeList).hasSize(databaseSizeBeforeCreate + 1);
        VisaType testVisaType = visaTypeList.get(visaTypeList.size() - 1);
        assertThat(testVisaType.getVisa()).isEqualTo(DEFAULT_VISA);

        // Validate the VisaType in Elasticsearch
        verify(mockVisaTypeSearchRepository, times(1)).save(testVisaType);
    }

    @Test
    @Transactional
    public void createVisaTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visaTypeRepository.findAll().size();

        // Create the VisaType with an existing ID
        visaType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisaTypeMockMvc.perform(post("/api/visa-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visaType)))
            .andExpect(status().isBadRequest());

        // Validate the VisaType in the database
        List<VisaType> visaTypeList = visaTypeRepository.findAll();
        assertThat(visaTypeList).hasSize(databaseSizeBeforeCreate);
        
        verify(mockVisaTypeSearchRepository, times(0)).save(visaType);
    }

    @Test
    @Transactional
    public void getAllVisaTypes() throws Exception {
        // Initialize the database
        visaTypeRepository.saveAndFlush(visaType);

        // Get all the visaTypeList
        restVisaTypeMockMvc.perform(get("/api/visa-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visaType.getId().intValue())))
            .andExpect(jsonPath("$.[*].visa").value(hasItem(DEFAULT_VISA.toString())));
    }

    @Test
    @Transactional
    public void getVisaType() throws Exception {
        // Initialize the database
        visaTypeRepository.saveAndFlush(visaType);

        // Get the visaType
        restVisaTypeMockMvc.perform(get("/api/visa-types/{id}", visaType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(visaType.getId().intValue()))
            .andExpect(jsonPath("$.visa").value(DEFAULT_VISA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVisaType() throws Exception {
        // Get the visaType
        restVisaTypeMockMvc.perform(get("/api/visa-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisaType() throws Exception {
        // Initialize the database
        visaTypeRepository.saveAndFlush(visaType);
     //   visaTypeSearchRepository.save(visaType);
        int databaseSizeBeforeUpdate = visaTypeRepository.findAll().size();

        // Update the visaType
        VisaType updatedVisaType = visaTypeRepository.findById(visaType.getId()).get();
        // Disconnect from session so that the updates on updatedVisaType are not directly saved in db
        em.detach(updatedVisaType);
        updatedVisaType
            .visa(UPDATED_VISA);

        restVisaTypeMockMvc.perform(put("/api/visa-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVisaType)))
            .andExpect(status().isOk());

        // Validate the VisaType in the database
        List<VisaType> visaTypeList = visaTypeRepository.findAll();
        assertThat(visaTypeList).hasSize(databaseSizeBeforeUpdate);
        VisaType testVisaType = visaTypeList.get(visaTypeList.size() - 1);
        assertThat(testVisaType.getVisa()).isEqualTo(UPDATED_VISA);

        // Validate the VisaType in Elasticsearch
        verify(mockVisaTypeSearchRepository, times(1)).save(testVisaType);
    }

    @Test
    @Transactional
    public void updateNonExistingVisaType() throws Exception {
        int databaseSizeBeforeUpdate = visaTypeRepository.findAll().size();

        // Create the VisaType

        // If the entity doesn't have an ID, it will be created instead of just being updated
       
        restVisaTypeMockMvc.perform(put("/api/visa-types")
        		 .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(visaType)))
                .andExpect(status().isBadRequest());
        // Validate the VisaType in the database
        List<VisaType> visaTypeList = visaTypeRepository.findAll();
        assertThat(visaTypeList).hasSize(databaseSizeBeforeUpdate);
        verify(mockVisaTypeSearchRepository, times(0)).save(visaType);
    }

    @Test
    @Transactional
    public void deleteVisaType() throws Exception {
        // Initialize the database
        visaTypeRepository.saveAndFlush(visaType);

        int databaseSizeBeforeDelete = visaTypeRepository.findAll().size();

        // Get the visaType
        restVisaTypeMockMvc.perform(delete("/api/visa-types/{id}", visaType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
     
        verify(mockVisaTypeSearchRepository, times(1)).deleteById(visaType.getId());
        // Validate the database is empty
        List<VisaType> visaTypeList = visaTypeRepository.findAll();
        assertThat(visaTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVisaType() throws Exception {
        // Initialize the database
        visaTypeRepository.saveAndFlush(visaType);
     //   visaTypeSearchRepository.save(visaType);
        when(mockVisaTypeSearchRepository.search(queryStringQuery("id:" + visaType.getId())))
        .thenReturn(Collections.singletonList(visaType));
        // Search the visaType
        restVisaTypeMockMvc.perform(get("/api/_search/visa-types?query=id:" + visaType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visaType.getId().intValue())))
            .andExpect(jsonPath("$.[*].visa").value(hasItem(DEFAULT_VISA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisaType.class);
        VisaType visaType1 = new VisaType();
        visaType1.setId(1L);
        VisaType visaType2 = new VisaType();
        visaType2.setId(visaType1.getId());
        assertThat(visaType1).isEqualTo(visaType2);
        visaType2.setId(2L);
        assertThat(visaType1).isNotEqualTo(visaType2);
        visaType1.setId(null);
        assertThat(visaType1).isNotEqualTo(visaType2);
    }
}
