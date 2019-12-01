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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.MaritalStatus;
import com.drishika.gradzcircle.entitybuilders.MaritalStatusEntityBuilder;
import com.drishika.gradzcircle.repository.MaritalStatusRepository;
import com.drishika.gradzcircle.repository.search.MaritalStatusSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the MaritalStatusResource REST controller.
 *
 * @see MaritalStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class MaritalStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private MaritalStatusRepository maritalStatusRepository;

    @Autowired
    private MaritalStatusSearchRepository mockMaritalStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaritalStatusMockMvc;

    private MaritalStatus maritalStatus;
    
    private com.drishika.gradzcircle.domain.elastic.MaritalStatus elasticMaritalStatus;
    
    @Mock
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaritalStatusResource maritalStatusResource = new MaritalStatusResource(maritalStatusRepository, mockMaritalStatusSearchRepository,elasticsearchTemplate);
        this.restMaritalStatusMockMvc = MockMvcBuilders.standaloneSetup(maritalStatusResource)
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
    public static MaritalStatus createEntity(EntityManager em) {
        MaritalStatus maritalStatus = new MaritalStatus()
            .status(DEFAULT_STATUS);
        return maritalStatus;
    }
    
    /**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static MaritalStatusEntityBuilder createEntityBuilder(MaritalStatus maritalStatus) {
		MaritalStatusEntityBuilder entityBuilder = new MaritalStatusEntityBuilder(maritalStatus.getId());
		entityBuilder.name(maritalStatus.getStatus());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.MaritalStatus createElasticInstance(
			MaritalStatus maritalStatus) {
		com.drishika.gradzcircle.domain.elastic.MaritalStatus elasticMaritalStatus = new com.drishika.gradzcircle.domain.elastic.MaritalStatus();
		elasticMaritalStatus.status(maritalStatus.getStatus());
		return elasticMaritalStatus;
	}

    @Before
    public void initTest() {
        //maritalStatusSearchRepository.deleteAll();
        maritalStatus = createEntity(em);
        elasticMaritalStatus = createElasticInstance(maritalStatus);
    }

    @Test
    @Transactional
    public void createMaritalStatus() throws Exception {
        int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus
        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isCreated());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate + 1);
        MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
        assertThat(testMaritalStatus.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MaritalStatus in Elasticsearch
        verify(elasticsearchTemplate,times(1)).index(any());
        verify(elasticsearchTemplate,times(1)).refresh(	com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
    }

    @Test
    @Transactional
    public void createMaritalStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus with an existing ID
        maritalStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaritalStatusMockMvc.perform(post("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
            .andExpect(status().isBadRequest());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMaritalStatuses() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);

        // Get all the maritalStatusList
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maritalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);

        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses/{id}", maritalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maritalStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaritalStatus() throws Exception {
        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/marital-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);
        elasticsearchTemplate.index(createEntityBuilder(maritalStatus)
				.suggest(new String[] { createElasticInstance(maritalStatus).getStatus() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
        int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

        // Update the maritalStatus
        MaritalStatus updatedMaritalStatus = maritalStatusRepository.findById(maritalStatus.getId()).get();
        // Disconnect from session so that the updates on updatedMaritalStatus are not directly saved in db
        em.detach(updatedMaritalStatus);
        updatedMaritalStatus
            .status(UPDATED_STATUS);

        restMaritalStatusMockMvc.perform(put("/api/marital-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaritalStatus)))
            .andExpect(status().isOk());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate);
        MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
        assertThat(testMaritalStatus.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MaritalStatus in Elasticsearch
       // FIX on Elastic template like others
    }

    @Test
    @Transactional
    public void updateNonExistingMaritalStatus() throws Exception {
        int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

        // Create the MaritalStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        
        restMaritalStatusMockMvc.perform(put("/api/marital-statuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(maritalStatus)))
                .andExpect(status().isBadRequest());

        // Validate the MaritalStatus in the database
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);
        elasticsearchTemplate.index(createEntityBuilder(maritalStatus)
				.suggest(new String[] { createElasticInstance(maritalStatus).getStatus() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
        int databaseSizeBeforeDelete = maritalStatusRepository.findAll().size();

        // Get the maritalStatus
        restMaritalStatusMockMvc.perform(delete("/api/marital-statuses/{id}", maritalStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        verify(elasticsearchTemplate,times(1)).index(any());
        verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
        // Validate the database is empty
        List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
        assertThat(maritalStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMaritalStatus() throws Exception {
        // Initialize the database
        maritalStatusRepository.saveAndFlush(maritalStatus);
        elasticsearchTemplate.index(createEntityBuilder(maritalStatus)
				.suggest(new String[] { createElasticInstance(maritalStatus).getStatus()}).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
		
		elasticMaritalStatus.setId(maritalStatus.getId());
		when(mockMaritalStatusSearchRepository.search(queryStringQuery("id:" + maritalStatus.getId())))
	        .thenReturn(Collections.singletonList(elasticMaritalStatus));

        // Search the maritalStatus
        restMaritalStatusMockMvc.perform(get("/api/_search/marital-statuses?query=id:" + maritalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maritalStatus.getId().intValue())));
            //.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaritalStatus.class);
        MaritalStatus maritalStatus1 = new MaritalStatus();
        maritalStatus1.setId(1L);
        MaritalStatus maritalStatus2 = new MaritalStatus();
        maritalStatus2.setId(maritalStatus1.getId());
        assertThat(maritalStatus1).isEqualTo(maritalStatus2);
        maritalStatus2.setId(2L);
        assertThat(maritalStatus1).isNotEqualTo(maritalStatus2);
        maritalStatus1.setId(null);
        assertThat(maritalStatus1).isNotEqualTo(maritalStatus2);
    }
}
