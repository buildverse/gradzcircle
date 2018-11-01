package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.AppConfig;
import com.drishika.gradzcircle.repository.AppConfigRepository;
import com.drishika.gradzcircle.repository.search.AppConfigSearchRepository;
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
 * Test class for the AppConfigResource REST controller.
 *
 * @see AppConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class AppConfigResourceIntTest {

    private static final String DEFAULT_CONFIG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONFIG_VALUE = false;
    private static final Boolean UPDATED_CONFIG_VALUE = true;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private AppConfigSearchRepository appConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppConfigMockMvc;

    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppConfigResource appConfigResource = new AppConfigResource(appConfigRepository, appConfigSearchRepository);
        this.restAppConfigMockMvc = MockMvcBuilders.standaloneSetup(appConfigResource)
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
    public static AppConfig createEntity(EntityManager em) {
        AppConfig appConfig = new AppConfig()
            .configName(DEFAULT_CONFIG_NAME)
            .configValue(DEFAULT_CONFIG_VALUE);
        return appConfig;
    }

    @Before
    public void initTest() {
        appConfigSearchRepository.deleteAll();
        appConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppConfig() throws Exception {
        int databaseSizeBeforeCreate = appConfigRepository.findAll().size();

        // Create the AppConfig
        restAppConfigMockMvc.perform(post("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfig)))
            .andExpect(status().isCreated());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeCreate + 1);
        AppConfig testAppConfig = appConfigList.get(appConfigList.size() - 1);
        assertThat(testAppConfig.getConfigName()).isEqualTo(DEFAULT_CONFIG_NAME);
        assertThat(testAppConfig.isConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);

        // Validate the AppConfig in Elasticsearch
        AppConfig appConfigEs = appConfigSearchRepository.findOne(testAppConfig.getId());
        assertThat(appConfigEs).isEqualToIgnoringGivenFields(testAppConfig);
    }

    @Test
    @Transactional
    public void createAppConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appConfigRepository.findAll().size();

        // Create the AppConfig with an existing ID
        appConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppConfigMockMvc.perform(post("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfig)))
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppConfigs() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);

        // Get all the appConfigList
        restAppConfigMockMvc.perform(get("/api/app-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);

        // Get the appConfig
        restAppConfigMockMvc.perform(get("/api/app-configs/{id}", appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appConfig.getId().intValue()))
            .andExpect(jsonPath("$.configName").value(DEFAULT_CONFIG_NAME.toString()))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAppConfig() throws Exception {
        // Get the appConfig
        restAppConfigMockMvc.perform(get("/api/app-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);
        int databaseSizeBeforeUpdate = appConfigRepository.findAll().size();

        // Update the appConfig
        AppConfig updatedAppConfig = appConfigRepository.findOne(appConfig.getId());
        // Disconnect from session so that the updates on updatedAppConfig are not directly saved in db
        em.detach(updatedAppConfig);
        updatedAppConfig
            .configName(UPDATED_CONFIG_NAME)
            .configValue(UPDATED_CONFIG_VALUE);

        restAppConfigMockMvc.perform(put("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppConfig)))
            .andExpect(status().isOk());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeUpdate);
        AppConfig testAppConfig = appConfigList.get(appConfigList.size() - 1);
        assertThat(testAppConfig.getConfigName()).isEqualTo(UPDATED_CONFIG_NAME);
        assertThat(testAppConfig.isConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);

        // Validate the AppConfig in Elasticsearch
        AppConfig appConfigEs = appConfigSearchRepository.findOne(testAppConfig.getId());
        assertThat(appConfigEs).isEqualToIgnoringGivenFields(testAppConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingAppConfig() throws Exception {
        int databaseSizeBeforeUpdate = appConfigRepository.findAll().size();

        // Create the AppConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppConfigMockMvc.perform(put("/api/app-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appConfig)))
            .andExpect(status().isCreated());

        // Validate the AppConfig in the database
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);
        int databaseSizeBeforeDelete = appConfigRepository.findAll().size();

        // Get the appConfig
        restAppConfigMockMvc.perform(delete("/api/app-configs/{id}", appConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appConfigExistsInEs = appConfigSearchRepository.exists(appConfig.getId());
        assertThat(appConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<AppConfig> appConfigList = appConfigRepository.findAll();
        assertThat(appConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppConfig() throws Exception {
        // Initialize the database
        appConfigRepository.saveAndFlush(appConfig);
        appConfigSearchRepository.save(appConfig);

        // Search the appConfig
        restAppConfigMockMvc.perform(get("/api/_search/app-configs?query=id:" + appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppConfig.class);
        AppConfig appConfig1 = new AppConfig();
        appConfig1.setId(1L);
        AppConfig appConfig2 = new AppConfig();
        appConfig2.setId(appConfig1.getId());
        assertThat(appConfig1).isEqualTo(appConfig2);
        appConfig2.setId(2L);
        assertThat(appConfig1).isNotEqualTo(appConfig2);
        appConfig1.setId(null);
        assertThat(appConfig1).isNotEqualTo(appConfig2);
    }
}
