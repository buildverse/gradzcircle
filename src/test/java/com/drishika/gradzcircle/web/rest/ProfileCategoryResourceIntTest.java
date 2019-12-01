package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
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
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
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
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.ProfileCategorySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ProfileCategoryResource REST controller.
 *
 * @see ProfileCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class ProfileCategoryResourceIntTest {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHTAGE = 1;
    private static final Integer UPDATED_WEIGHTAGE = 2;

    @Autowired
    private ProfileCategoryRepository profileCategoryRepository;

    @Autowired
    private ProfileCategorySearchRepository mockProfileCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfileCategoryMockMvc;

    private ProfileCategory profileCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileCategoryResource profileCategoryResource = new ProfileCategoryResource(profileCategoryRepository, mockProfileCategorySearchRepository);
        this.restProfileCategoryMockMvc = MockMvcBuilders.standaloneSetup(profileCategoryResource)
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
    public static ProfileCategory createEntity(EntityManager em) {
        ProfileCategory profileCategory = new ProfileCategory()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .weightage(DEFAULT_WEIGHTAGE);
        return profileCategory;
    }

    @Before
    public void initTest() {
      //  profileCategorySearchRepository.deleteAll();
        profileCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfileCategory() throws Exception {
        int databaseSizeBeforeCreate = profileCategoryRepository.findAll().size();

        // Create the ProfileCategory
        restProfileCategoryMockMvc.perform(post("/api/profile-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileCategory)))
            .andExpect(status().isCreated());

        // Validate the ProfileCategory in the database
        List<ProfileCategory> profileCategoryList = profileCategoryRepository.findAll();
        assertThat(profileCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProfileCategory testProfileCategory = profileCategoryList.get(profileCategoryList.size() - 1);
        assertThat(testProfileCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testProfileCategory.getWeightage()).isEqualTo(DEFAULT_WEIGHTAGE);

        // Validate the ProfileCategory in Elasticsearch
       verify(mockProfileCategorySearchRepository,times(1)).save(testProfileCategory);
    }

    @Test
    @Transactional
    public void createProfileCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileCategoryRepository.findAll().size();

        // Create the ProfileCategory with an existing ID
        profileCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileCategoryMockMvc.perform(post("/api/profile-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileCategory in the database
        List<ProfileCategory> profileCategoryList = profileCategoryRepository.findAll();
        assertThat(profileCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProfileCategories() throws Exception {
        // Initialize the database
        profileCategoryRepository.saveAndFlush(profileCategory);

        // Get all the profileCategoryList
        restProfileCategoryMockMvc.perform(get("/api/profile-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].weightage").value(hasItem(DEFAULT_WEIGHTAGE)));
    }

    @Test
    @Transactional
    public void getProfileCategory() throws Exception {
        // Initialize the database
        profileCategoryRepository.saveAndFlush(profileCategory);

        // Get the profileCategory
        restProfileCategoryMockMvc.perform(get("/api/profile-categories/{id}", profileCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profileCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.weightage").value(DEFAULT_WEIGHTAGE));
    }

    @Test
    @Transactional
    public void getNonExistingProfileCategory() throws Exception {
        // Get the profileCategory
        restProfileCategoryMockMvc.perform(get("/api/profile-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfileCategory() throws Exception {
        // Initialize the database
        profileCategoryRepository.saveAndFlush(profileCategory);
      //  profileCategorySearchRepository.save(profileCategory);
        int databaseSizeBeforeUpdate = profileCategoryRepository.findAll().size();

        // Update the profileCategory
        ProfileCategory updatedProfileCategory = profileCategoryRepository.findById(profileCategory.getId()).get();
        // Disconnect from session so that the updates on updatedProfileCategory are not directly saved in db
        em.detach(updatedProfileCategory);
        updatedProfileCategory
            .categoryName(UPDATED_CATEGORY_NAME)
            .weightage(UPDATED_WEIGHTAGE);

        restProfileCategoryMockMvc.perform(put("/api/profile-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfileCategory)))
            .andExpect(status().isOk());

        // Validate the ProfileCategory in the database
        List<ProfileCategory> profileCategoryList = profileCategoryRepository.findAll();
        assertThat(profileCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProfileCategory testProfileCategory = profileCategoryList.get(profileCategoryList.size() - 1);
        assertThat(testProfileCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProfileCategory.getWeightage()).isEqualTo(UPDATED_WEIGHTAGE);

        // Validate the ProfileCategory in Elasticsearch
        verify(mockProfileCategorySearchRepository,times(1)).save(testProfileCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingProfileCategory() throws Exception {
        int databaseSizeBeforeUpdate = profileCategoryRepository.findAll().size();

        // Create the ProfileCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated

        
        restProfileCategoryMockMvc.perform(put("/api/profile-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profileCategory)))
                .andExpect(status().isBadRequest());

        // Validate the ProfileCategory in the database
        List<ProfileCategory> profileCategoryList = profileCategoryRepository.findAll();
        assertThat(profileCategoryList).hasSize(databaseSizeBeforeUpdate);
        verify(mockProfileCategorySearchRepository,times(0)).deleteById(profileCategory.getId());
    }

    @Test
    @Transactional
    public void deleteProfileCategory() throws Exception {
        // Initialize the database
        profileCategoryRepository.saveAndFlush(profileCategory);
       // profileCategorySearchRepository.save(profileCategory);
        int databaseSizeBeforeDelete = profileCategoryRepository.findAll().size();

        // Get the profileCategory
        restProfileCategoryMockMvc.perform(delete("/api/profile-categories/{id}", profileCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        
        verify(mockProfileCategorySearchRepository,times(1)).deleteById(profileCategory.getId());
        // Validate the database is empty
        List<ProfileCategory> profileCategoryList = profileCategoryRepository.findAll();
        assertThat(profileCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfileCategory() throws Exception {
        // Initialize the database
        profileCategoryRepository.saveAndFlush(profileCategory);
        when(mockProfileCategorySearchRepository.search(queryStringQuery("id:" + profileCategory.getId())))
        .thenReturn((Collections.singletonList(profileCategory)));

        // Search the profileCategory
        restProfileCategoryMockMvc.perform(get("/api/_search/profile-categories?query=id:" + profileCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].weightage").value(hasItem(DEFAULT_WEIGHTAGE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileCategory.class);
        ProfileCategory profileCategory1 = new ProfileCategory();
        profileCategory1.setId(1L);
        ProfileCategory profileCategory2 = new ProfileCategory();
        profileCategory2.setId(profileCategory1.getId());
        assertThat(profileCategory1).isEqualTo(profileCategory2);
        profileCategory2.setId(2L);
        assertThat(profileCategory1).isNotEqualTo(profileCategory2);
        profileCategory1.setId(null);
        assertThat(profileCategory1).isNotEqualTo(profileCategory2);
    }
}
