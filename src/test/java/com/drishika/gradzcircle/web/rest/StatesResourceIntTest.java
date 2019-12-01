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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.States;
import com.drishika.gradzcircle.repository.StatesRepository;
import com.drishika.gradzcircle.repository.search.StatesSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the StatesResource REST controller.
 *
 * @see StatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class StatesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private StatesRepository statesRepository;

    @Autowired
    private StatesSearchRepository mockStatesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStatesMockMvc;

    private States states;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StatesResource statesResource = new StatesResource(statesRepository, mockStatesSearchRepository);
        this.restStatesMockMvc = MockMvcBuilders.standaloneSetup(statesResource)
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
    public static States createEntity(EntityManager em) {
        States states = new States()
            .name(DEFAULT_NAME);
        return states;
    }

    @Before
    public void initTest() {
       // statesSearchRepository.deleteAll();
        states = createEntity(em);
    }

    @Test
    @Transactional
    public void createStates() throws Exception {
        int databaseSizeBeforeCreate = statesRepository.findAll().size();

        // Create the States
        restStatesMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(states)))
            .andExpect(status().isCreated());

        // Validate the States in the database
        List<States> statesList = statesRepository.findAll();
        assertThat(statesList).hasSize(databaseSizeBeforeCreate + 1);
        States testStates = statesList.get(statesList.size() - 1);
        assertThat(testStates.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the States in Elasticsearch
        verify(mockStatesSearchRepository,times(1)).save(testStates);
    }

    @Test
    @Transactional
    public void createStatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statesRepository.findAll().size();

        // Create the States with an existing ID
        states.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatesMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(states)))
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        List<States> statesList = statesRepository.findAll();
        assertThat(statesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStates() throws Exception {
        // Initialize the database
        statesRepository.saveAndFlush(states);

        // Get all the statesList
        restStatesMockMvc.perform(get("/api/states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(states.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStates() throws Exception {
        // Initialize the database
        statesRepository.saveAndFlush(states);

        // Get the states
        restStatesMockMvc.perform(get("/api/states/{id}", states.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(states.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStates() throws Exception {
        // Get the states
        restStatesMockMvc.perform(get("/api/states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStates() throws Exception {
        // Initialize the database
        statesRepository.saveAndFlush(states);
    //    statesSearchRepository.save(states);
        int databaseSizeBeforeUpdate = statesRepository.findAll().size();

        // Update the states
        States updatedStates = statesRepository.findById(states.getId()).get();
        // Disconnect from session so that the updates on updatedStates are not directly saved in db
        em.detach(updatedStates);
        updatedStates
            .name(UPDATED_NAME);

        restStatesMockMvc.perform(put("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStates)))
            .andExpect(status().isOk());

        // Validate the States in the database
        List<States> statesList = statesRepository.findAll();
        assertThat(statesList).hasSize(databaseSizeBeforeUpdate);
        States testStates = statesList.get(statesList.size() - 1);
        assertThat(testStates.getName()).isEqualTo(UPDATED_NAME);

        // Validate the States in Elasticsearch
        verify(mockStatesSearchRepository,times(1)).save(updatedStates);
    }

    @Test
    @Transactional
    public void updateNonExistingStates() throws Exception {
        int databaseSizeBeforeUpdate = statesRepository.findAll().size();

        // Create the States

        // If the entity doesn't have an ID, it will be created instead of just being updated
        
        restStatesMockMvc.perform(put("/api/states")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(states)))
                .andExpect(status().isBadRequest());
        // Validate the States in the database
        List<States> statesList = statesRepository.findAll();
        assertThat(statesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStates() throws Exception {
        // Initialize the database
        statesRepository.saveAndFlush(states);
       // statesSearchRepository.save(states);
        int databaseSizeBeforeDelete = statesRepository.findAll().size();

        // Get the states
        restStatesMockMvc.perform(delete("/api/states/{id}", states.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

       verify(mockStatesSearchRepository,times(1)).deleteById(states.getId());

        // Validate the database is empty
        List<States> statesList = statesRepository.findAll();
        assertThat(statesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStates() throws Exception {
        // Initialize the database
        statesRepository.saveAndFlush(states);
     //  statesSearchRepository.save(states);
        
        when(mockStatesSearchRepository.search(queryStringQuery("id:" + states.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(states), PageRequest.of(0, 1), 1));

        // Search the states
        restStatesMockMvc.perform(get("/api/_search/states?query=id:" + states.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(states.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(States.class);
        States states1 = new States();
        states1.setId(1L);
        States states2 = new States();
        states2.setId(states1.getId());
        assertThat(states1).isEqualTo(states2);
        states2.setId(2L);
        assertThat(states1).isNotEqualTo(states2);
        states1.setId(null);
        assertThat(states1).isNotEqualTo(states2);
    }
}
