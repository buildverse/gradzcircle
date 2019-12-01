package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.drishika.gradzcircle.domain.ErrorMessages;
import com.drishika.gradzcircle.repository.ErrorMessagesRepository;
import com.drishika.gradzcircle.repository.search.ErrorMessagesSearchRepository;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ErrorMessagesResource REST controller.
 *
 * @see ErrorMessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class ErrorMessagesResourceIntTest {

    private static final String DEFAULT_COMPONENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ErrorMessagesRepository errorMessagesRepository;

    @Autowired
    private ErrorMessagesSearchRepository mockErrorMessagesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restErrorMessagesMockMvc;

    private ErrorMessages errorMessages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ErrorMessagesResource errorMessagesResource = new ErrorMessagesResource(errorMessagesRepository, mockErrorMessagesSearchRepository);
        this.restErrorMessagesMockMvc = MockMvcBuilders.standaloneSetup(errorMessagesResource)
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
    public static ErrorMessages createEntity(EntityManager em) {
        ErrorMessages errorMessages = new ErrorMessages()
            .componentName(DEFAULT_COMPONENT_NAME)
            .errorKey(DEFAULT_ERROR_KEY)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
        return errorMessages;
    }

    @Before
    public void initTest() {
       // errorMessagesSearchRepository.deleteAll();
        errorMessages = createEntity(em);
    }

    @Test
    @Transactional
    public void createErrorMessages() throws Exception {
        int databaseSizeBeforeCreate = errorMessagesRepository.findAll().size();

        // Create the ErrorMessages
        restErrorMessagesMockMvc.perform(post("/api/error-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(errorMessages)))
            .andExpect(status().isCreated());

        // Validate the ErrorMessages in the database
        List<ErrorMessages> errorMessagesList = errorMessagesRepository.findAll();
        assertThat(errorMessagesList).hasSize(databaseSizeBeforeCreate + 1);
        ErrorMessages testErrorMessages = errorMessagesList.get(errorMessagesList.size() - 1);
        assertThat(testErrorMessages.getComponentName()).isEqualTo(DEFAULT_COMPONENT_NAME);
        assertThat(testErrorMessages.getErrorKey()).isEqualTo(DEFAULT_ERROR_KEY);
        assertThat(testErrorMessages.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);

        // Validate the ErrorMessages in Elasticsearch
       verify(mockErrorMessagesSearchRepository,times(1)).save(testErrorMessages);
    }

    @Test
    @Transactional
    public void createErrorMessagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = errorMessagesRepository.findAll().size();

        // Create the ErrorMessages with an existing ID
        errorMessages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restErrorMessagesMockMvc.perform(post("/api/error-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(errorMessages)))
            .andExpect(status().isBadRequest());

        // Validate the ErrorMessages in the database
        List<ErrorMessages> errorMessagesList = errorMessagesRepository.findAll();
        assertThat(errorMessagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllErrorMessages() throws Exception {
        // Initialize the database
        errorMessagesRepository.saveAndFlush(errorMessages);

        // Get all the errorMessagesList
        restErrorMessagesMockMvc.perform(get("/api/error-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(errorMessages.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentName").value(hasItem(DEFAULT_COMPONENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].errorKey").value(hasItem(DEFAULT_ERROR_KEY.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getErrorMessages() throws Exception {
        // Initialize the database
        errorMessagesRepository.saveAndFlush(errorMessages);

        // Get the errorMessages
        restErrorMessagesMockMvc.perform(get("/api/error-messages/{id}", errorMessages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(errorMessages.getId().intValue()))
            .andExpect(jsonPath("$.componentName").value(DEFAULT_COMPONENT_NAME.toString()))
            .andExpect(jsonPath("$.errorKey").value(DEFAULT_ERROR_KEY.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingErrorMessages() throws Exception {
        // Get the errorMessages
        restErrorMessagesMockMvc.perform(get("/api/error-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateErrorMessages() throws Exception {
        // Initialize the database
        errorMessagesRepository.saveAndFlush(errorMessages);
        int databaseSizeBeforeUpdate = errorMessagesRepository.findAll().size();

        // Update the errorMessages
        ErrorMessages updatedErrorMessages = errorMessagesRepository.findById(errorMessages.getId()).get();
        // Disconnect from session so that the updates on updatedErrorMessages are not directly saved in db
        em.detach(updatedErrorMessages);
        updatedErrorMessages
            .componentName(UPDATED_COMPONENT_NAME)
            .errorKey(UPDATED_ERROR_KEY)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restErrorMessagesMockMvc.perform(put("/api/error-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedErrorMessages)))
            .andExpect(status().isOk());

        // Validate the ErrorMessages in the database
        List<ErrorMessages> errorMessagesList = errorMessagesRepository.findAll();
        assertThat(errorMessagesList).hasSize(databaseSizeBeforeUpdate);
        ErrorMessages testErrorMessages = errorMessagesList.get(errorMessagesList.size() - 1);
        assertThat(testErrorMessages.getComponentName()).isEqualTo(UPDATED_COMPONENT_NAME);
        assertThat(testErrorMessages.getErrorKey()).isEqualTo(UPDATED_ERROR_KEY);
        assertThat(testErrorMessages.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);

        // Validate the ErrorMessages in Elasticsearch
        verify(mockErrorMessagesSearchRepository,times(1)).save(testErrorMessages);
    }

    @Test
    @Transactional
    public void updateNonExistingErrorMessages() throws Exception {
        int databaseSizeBeforeUpdate = errorMessagesRepository.findAll().size();

        // Create the ErrorMessages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restErrorMessagesMockMvc.perform(put("/api/error-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(errorMessages)))
                .andExpect(status().isBadRequest());
        // Validate the ErrorMessages in the database
        List<ErrorMessages> errorMessagesList = errorMessagesRepository.findAll();
        assertThat(errorMessagesList).hasSize(databaseSizeBeforeUpdate);
        verify(mockErrorMessagesSearchRepository,times(0)).save(errorMessages);
    }

    @Test
    @Transactional
    public void deleteErrorMessages() throws Exception {
        // Initialize the database
        errorMessagesRepository.saveAndFlush(errorMessages);
        int databaseSizeBeforeDelete = errorMessagesRepository.findAll().size();

        // Get the errorMessages
        restErrorMessagesMockMvc.perform(delete("/api/error-messages/{id}", errorMessages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        verify(mockErrorMessagesSearchRepository,times(1)).deleteById(errorMessages.getId());

        // Validate the database is empty
        List<ErrorMessages> errorMessagesList = errorMessagesRepository.findAll();
        assertThat(errorMessagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchErrorMessages() throws Exception {
        // Initialize the database
        errorMessagesRepository.saveAndFlush(errorMessages);
        when(mockErrorMessagesSearchRepository.search(queryStringQuery("id:" + errorMessages.getId())))
        .thenReturn(Collections.singletonList(errorMessages));

        // Search the errorMessages
        restErrorMessagesMockMvc.perform(get("/api/_search/error-messages?query=id:" + errorMessages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(errorMessages.getId().intValue())))
            .andExpect(jsonPath("$.[*].componentName").value(hasItem(DEFAULT_COMPONENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].errorKey").value(hasItem(DEFAULT_ERROR_KEY.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ErrorMessages.class);
        ErrorMessages errorMessages1 = new ErrorMessages();
        errorMessages1.setId(1L);
        ErrorMessages errorMessages2 = new ErrorMessages();
        errorMessages2.setId(errorMessages1.getId());
        assertThat(errorMessages1).isEqualTo(errorMessages2);
        errorMessages2.setId(2L);
        assertThat(errorMessages1).isNotEqualTo(errorMessages2);
        errorMessages1.setId(null);
        assertThat(errorMessages1).isNotEqualTo(errorMessages2);
    }
}
