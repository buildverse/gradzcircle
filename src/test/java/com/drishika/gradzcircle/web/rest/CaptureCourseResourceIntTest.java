package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;
import static org.mockito.Mockito.*;

import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.CaptureCourse;
import com.drishika.gradzcircle.repository.CaptureCourseRepository;
import com.drishika.gradzcircle.repository.search.CaptureCourseSearchRepository;
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
 * Test class for the CaptureCourseResource REST controller.
 *
 * @see CaptureCourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CaptureCourseResourceIntTest {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    @Autowired
    private CaptureCourseRepository captureCourseRepository;

    @Autowired
    private CaptureCourseSearchRepository mockCaptureCourseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCaptureCourseMockMvc;

    private CaptureCourse captureCourse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CaptureCourseResource captureCourseResource = new CaptureCourseResource(captureCourseRepository, mockCaptureCourseSearchRepository);
        this.restCaptureCourseMockMvc = MockMvcBuilders.standaloneSetup(captureCourseResource)
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
    public static CaptureCourse createEntity(EntityManager em) {
        CaptureCourse captureCourse = new CaptureCourse()
            .courseName(DEFAULT_COURSE_NAME);
        return captureCourse;
    }

    @Before
    public void initTest() {
        //captureCourseSearchRepository.deleteAll();
        captureCourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaptureCourse() throws Exception {
        int databaseSizeBeforeCreate = captureCourseRepository.findAll().size();

        // Create the CaptureCourse
        restCaptureCourseMockMvc.perform(post("/api/capture-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureCourse)))
            .andExpect(status().isCreated());

        // Validate the CaptureCourse in the database
        List<CaptureCourse> captureCourseList = captureCourseRepository.findAll();
        assertThat(captureCourseList).hasSize(databaseSizeBeforeCreate + 1);
        CaptureCourse testCaptureCourse = captureCourseList.get(captureCourseList.size() - 1);
        assertThat(testCaptureCourse.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);

        // Validate the CaptureCourse in Elasticsearch
        verify(mockCaptureCourseSearchRepository, times(1)).save(testCaptureCourse);
    }

    @Test
    @Transactional
    public void createCaptureCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = captureCourseRepository.findAll().size();

        // Create the CaptureCourse with an existing ID
        captureCourse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaptureCourseMockMvc.perform(post("/api/capture-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(captureCourse)))
            .andExpect(status().isBadRequest());

        // Validate the CaptureCourse in the database
        List<CaptureCourse> captureCourseList = captureCourseRepository.findAll();
        assertThat(captureCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCaptureCourses() throws Exception {
        // Initialize the database
        captureCourseRepository.saveAndFlush(captureCourse);

        // Get all the captureCourseList
        restCaptureCourseMockMvc.perform(get("/api/capture-courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCaptureCourse() throws Exception {
        // Initialize the database
        captureCourseRepository.saveAndFlush(captureCourse);

        // Get the captureCourse
        restCaptureCourseMockMvc.perform(get("/api/capture-courses/{id}", captureCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(captureCourse.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaptureCourse() throws Exception {
        // Get the captureCourse
        restCaptureCourseMockMvc.perform(get("/api/capture-courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaptureCourse() throws Exception {
        // Initialize the database
        captureCourseRepository.saveAndFlush(captureCourse);
       // captureCourseSearchRepository.save(captureCourse);
        int databaseSizeBeforeUpdate = captureCourseRepository.findAll().size();

        // Update the captureCourse
        CaptureCourse updatedCaptureCourse = captureCourseRepository.findById(captureCourse.getId()).get();
        // Disconnect from session so that the updates on updatedCaptureCourse are not directly saved in db
        em.detach(updatedCaptureCourse);
        updatedCaptureCourse
            .courseName(UPDATED_COURSE_NAME);

        restCaptureCourseMockMvc.perform(put("/api/capture-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaptureCourse)))
            .andExpect(status().isOk());

        // Validate the CaptureCourse in the database
        List<CaptureCourse> captureCourseList = captureCourseRepository.findAll();
        assertThat(captureCourseList).hasSize(databaseSizeBeforeUpdate);
        CaptureCourse testCaptureCourse = captureCourseList.get(captureCourseList.size() - 1);
        assertThat(testCaptureCourse.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);

        // Validate the CaptureCourse in Elasticsearch
        verify(mockCaptureCourseSearchRepository, times(1)).save(testCaptureCourse);
    }

    @Test
    @Transactional
    public void updateNonExistingCaptureCourse() throws Exception {
        int databaseSizeBeforeUpdate = captureCourseRepository.findAll().size();

        // Create the CaptureCourse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        
        restCaptureCourseMockMvc.perform(put("/api/capture-courses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(captureCourse)))
                .andExpect(status().isBadRequest());

        // Validate the CaptureCourse in the database
        List<CaptureCourse> captureCourseList = captureCourseRepository.findAll();
        assertThat(captureCourseList).hasSize(databaseSizeBeforeUpdate);
        verify(mockCaptureCourseSearchRepository, times(0)).save(captureCourse);
    }

    @Test
    @Transactional
    public void deleteCaptureCourse() throws Exception {
        // Initialize the database
        captureCourseRepository.saveAndFlush(captureCourse);
       // captureCourseSearchRepository.save(captureCourse);
        int databaseSizeBeforeDelete = captureCourseRepository.findAll().size();

        // Get the captureCourse
        restCaptureCourseMockMvc.perform(delete("/api/capture-courses/{id}", captureCourse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
       // boolean captureCourseExistsInEs = captureCourseSearchRepository.existsById(captureCourse.getId());
      //  assertThat(captureCourseExistsInEs).isFalse();
        verify(mockCaptureCourseSearchRepository, times(1)).deleteById(captureCourse.getId());
        // Validate the database is empty
        List<CaptureCourse> captureCourseList = captureCourseRepository.findAll();
        assertThat(captureCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCaptureCourse() throws Exception {
        // Initialize the database
        captureCourseRepository.saveAndFlush(captureCourse);
     //   captureCourseSearchRepository.save(captureCourse);
        when(mockCaptureCourseSearchRepository.search(queryStringQuery("id:" + captureCourse.getId())))
        .thenReturn(Collections.singletonList(captureCourse));
        // Search the captureCourse
        restCaptureCourseMockMvc.perform(get("/api/_search/capture-courses?query=id:" + captureCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captureCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaptureCourse.class);
        CaptureCourse captureCourse1 = new CaptureCourse();
        captureCourse1.setId(1L);
        CaptureCourse captureCourse2 = new CaptureCourse();
        captureCourse2.setId(captureCourse1.getId());
        assertThat(captureCourse1).isEqualTo(captureCourse2);
        captureCourse2.setId(2L);
        assertThat(captureCourse1).isNotEqualTo(captureCourse2);
        captureCourse1.setId(null);
        assertThat(captureCourse1).isNotEqualTo(captureCourse2);
    }
}
