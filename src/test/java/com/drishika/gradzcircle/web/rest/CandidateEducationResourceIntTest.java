package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CandidateEducationResource REST controller.
 *
 * @see CandidateEducationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateEducationResourceIntTest {

    private static final Double DEFAULT_GRADE = 1D;
    private static final Double UPDATED_GRADE = 2D;

    private static final LocalDate DEFAULT_EDUCATION_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EDUCATION_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EDUCATION_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EDUCATION_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_PURSUING_EDUCATION = false;
    private static final Boolean UPDATED_IS_PURSUING_EDUCATION = true;

    private static final Integer DEFAULT_GRADE_SCALE = 1;
    private static final Integer UPDATED_GRADE_SCALE = 2;

    private static final Boolean DEFAULT_HIGHEST_QUALIFICATION = false;
    private static final Boolean UPDATED_HIGHEST_QUALIFICATION = true;

    private static final Integer DEFAULT_ROUND_OF_GRADE = 1;
    private static final Integer UPDATED_ROUND_OF_GRADE = 2;

    private static final Integer DEFAULT_GRADE_DECIMAL = 1;
    private static final Integer UPDATED_GRADE_DECIMAL = 2;

    private static final String DEFAULT_CAPTURED_COURSE = "AAAAAAAAAA";
    private static final String UPDATED_CAPTURED_COURSE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTURED_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTURED_QUALIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTURED_COLLEGE = "AAAAAAAAAA";
    private static final String UPDATED_CAPTURED_COLLEGE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTURED_UNIVERSITY = "AAAAAAAAAA";
    private static final String UPDATED_CAPTURED_UNIVERSITY = "BBBBBBBBBB";

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;

    private static final String DEFAULT_SCORE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SCORE_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDUCATION_DURATION = 1;
    private static final Integer UPDATED_EDUCATION_DURATION = 2;

    @Autowired
    private CandidateEducationRepository candidateEducationRepository;

    @Autowired
    private CandidateEducationSearchRepository candidateEducationSearchRepository;

    @Autowired
    private  CandidateProjectRepository candidateProjectRepository;

    @Autowired
    private CandidateProjectSearchRepository candidateProjectSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCandidateEducationMockMvc;

    private CandidateEducation candidateEducation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CandidateEducationResource candidateEducationResource = new CandidateEducationResource(candidateEducationRepository, 
            candidateEducationSearchRepository,candidateProjectRepository,candidateProjectSearchRepository);
        this.restCandidateEducationMockMvc = MockMvcBuilders.standaloneSetup(candidateEducationResource)
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
    public static CandidateEducation createEntity(EntityManager em) {
        CandidateEducation candidateEducation = new CandidateEducation()
            .grade(DEFAULT_GRADE)
            .educationFromDate(DEFAULT_EDUCATION_FROM_DATE)
            .educationToDate(DEFAULT_EDUCATION_TO_DATE)
            .isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION)
            .gradeScale(DEFAULT_GRADE_SCALE)
            .highestQualification(DEFAULT_HIGHEST_QUALIFICATION)
            .roundOfGrade(DEFAULT_ROUND_OF_GRADE)
            .gradeDecimal(DEFAULT_GRADE_DECIMAL)
            .capturedCourse(DEFAULT_CAPTURED_COURSE)
            .capturedQualification(DEFAULT_CAPTURED_QUALIFICATION)
            .capturedCollege(DEFAULT_CAPTURED_COLLEGE)
            .capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY)
            .percentage(DEFAULT_PERCENTAGE)
            .scoreType(DEFAULT_SCORE_TYPE)
            .educationDuration(DEFAULT_EDUCATION_DURATION);
        return candidateEducation;
    }

    @Before
    public void initTest() {
        candidateEducationSearchRepository.deleteAll();
        candidateEducation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCandidateEducation() throws Exception {
        int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

        // Create the CandidateEducation
        restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
            .andExpect(status().isCreated());

        // Validate the CandidateEducation in the database
        List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
        assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
        CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
        assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
        assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
        assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
        assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
        assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(DEFAULT_HIGHEST_QUALIFICATION);
        assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
        assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
        assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
        assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
        assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
        assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
        assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
        assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);

        // Validate the CandidateEducation in Elasticsearch
        CandidateEducation candidateEducationEs = candidateEducationSearchRepository.findOne(testCandidateEducation.getId());
        assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
    }

    @Test
    @Transactional
    public void createCandidateEducationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

        // Create the CandidateEducation with an existing ID
        candidateEducation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
            .andExpect(status().isBadRequest());

        // Validate the CandidateEducation in the database
        List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
        assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCandidateEducations() throws Exception {
        // Initialize the database
        candidateEducationRepository.saveAndFlush(candidateEducation);

        // Get all the candidateEducationList
        restCandidateEducationMockMvc.perform(get("/api/candidate-educations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateEducation.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())))
            .andExpect(jsonPath("$.[*].educationFromDate").value(hasItem(DEFAULT_EDUCATION_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].educationToDate").value(hasItem(DEFAULT_EDUCATION_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPursuingEducation").value(hasItem(DEFAULT_IS_PURSUING_EDUCATION.booleanValue())))
            .andExpect(jsonPath("$.[*].gradeScale").value(hasItem(DEFAULT_GRADE_SCALE)))
            .andExpect(jsonPath("$.[*].highestQualification").value(hasItem(DEFAULT_HIGHEST_QUALIFICATION.booleanValue())))
            .andExpect(jsonPath("$.[*].roundOfGrade").value(hasItem(DEFAULT_ROUND_OF_GRADE)))
            .andExpect(jsonPath("$.[*].gradeDecimal").value(hasItem(DEFAULT_GRADE_DECIMAL)))
            .andExpect(jsonPath("$.[*].capturedCourse").value(hasItem(DEFAULT_CAPTURED_COURSE.toString())))
            .andExpect(jsonPath("$.[*].capturedQualification").value(hasItem(DEFAULT_CAPTURED_QUALIFICATION.toString())))
            .andExpect(jsonPath("$.[*].capturedCollege").value(hasItem(DEFAULT_CAPTURED_COLLEGE.toString())))
            .andExpect(jsonPath("$.[*].capturedUniversity").value(hasItem(DEFAULT_CAPTURED_UNIVERSITY.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].scoreType").value(hasItem(DEFAULT_SCORE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].educationDuration").value(hasItem(DEFAULT_EDUCATION_DURATION)));
    }

    @Test
    @Transactional
    public void getCandidateEducation() throws Exception {
        // Initialize the database
        candidateEducationRepository.saveAndFlush(candidateEducation);

        // Get the candidateEducation
        restCandidateEducationMockMvc.perform(get("/api/candidate-educations/{id}", candidateEducation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(candidateEducation.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.doubleValue()))
            .andExpect(jsonPath("$.educationFromDate").value(DEFAULT_EDUCATION_FROM_DATE.toString()))
            .andExpect(jsonPath("$.educationToDate").value(DEFAULT_EDUCATION_TO_DATE.toString()))
            .andExpect(jsonPath("$.isPursuingEducation").value(DEFAULT_IS_PURSUING_EDUCATION.booleanValue()))
            .andExpect(jsonPath("$.gradeScale").value(DEFAULT_GRADE_SCALE))
            .andExpect(jsonPath("$.highestQualification").value(DEFAULT_HIGHEST_QUALIFICATION.booleanValue()))
            .andExpect(jsonPath("$.roundOfGrade").value(DEFAULT_ROUND_OF_GRADE))
            .andExpect(jsonPath("$.gradeDecimal").value(DEFAULT_GRADE_DECIMAL))
            .andExpect(jsonPath("$.capturedCourse").value(DEFAULT_CAPTURED_COURSE.toString()))
            .andExpect(jsonPath("$.capturedQualification").value(DEFAULT_CAPTURED_QUALIFICATION.toString()))
            .andExpect(jsonPath("$.capturedCollege").value(DEFAULT_CAPTURED_COLLEGE.toString()))
            .andExpect(jsonPath("$.capturedUniversity").value(DEFAULT_CAPTURED_UNIVERSITY.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.scoreType").value(DEFAULT_SCORE_TYPE.toString()))
            .andExpect(jsonPath("$.educationDuration").value(DEFAULT_EDUCATION_DURATION));
    }

    @Test
    @Transactional
    public void getNonExistingCandidateEducation() throws Exception {
        // Get the candidateEducation
        restCandidateEducationMockMvc.perform(get("/api/candidate-educations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidateEducation() throws Exception {
        // Initialize the database
        candidateEducationRepository.saveAndFlush(candidateEducation);
        candidateEducationSearchRepository.save(candidateEducation);
        int databaseSizeBeforeUpdate = candidateEducationRepository.findAll().size();

        // Update the candidateEducation
        CandidateEducation updatedCandidateEducation = candidateEducationRepository.findOne(candidateEducation.getId());
        updatedCandidateEducation
            .grade(UPDATED_GRADE)
            .educationFromDate(UPDATED_EDUCATION_FROM_DATE)
            .educationToDate(UPDATED_EDUCATION_TO_DATE)
            .isPursuingEducation(UPDATED_IS_PURSUING_EDUCATION)
            .gradeScale(UPDATED_GRADE_SCALE)
            .highestQualification(UPDATED_HIGHEST_QUALIFICATION)
            .roundOfGrade(UPDATED_ROUND_OF_GRADE)
            .gradeDecimal(UPDATED_GRADE_DECIMAL)
            .capturedCourse(UPDATED_CAPTURED_COURSE)
            .capturedQualification(UPDATED_CAPTURED_QUALIFICATION)
            .capturedCollege(UPDATED_CAPTURED_COLLEGE)
            .capturedUniversity(UPDATED_CAPTURED_UNIVERSITY)
            .percentage(UPDATED_PERCENTAGE)
            .scoreType(UPDATED_SCORE_TYPE)
            .educationDuration(UPDATED_EDUCATION_DURATION);

        restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCandidateEducation)))
            .andExpect(status().isOk());

        // Validate the CandidateEducation in the database
        List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
        assertThat(candidateEducationList).hasSize(databaseSizeBeforeUpdate);
        CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
        assertThat(testCandidateEducation.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
        assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_TO_DATE);
        assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(UPDATED_IS_PURSUING_EDUCATION);
        assertThat(testCandidateEducation.getGradeScale()).isEqualTo(UPDATED_GRADE_SCALE);
        assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
        assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(UPDATED_ROUND_OF_GRADE);
        assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(UPDATED_GRADE_DECIMAL);
        assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(UPDATED_CAPTURED_COURSE);
        assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(UPDATED_CAPTURED_QUALIFICATION);
        assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(UPDATED_CAPTURED_COLLEGE);
        assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(UPDATED_CAPTURED_UNIVERSITY);
        assertThat(testCandidateEducation.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testCandidateEducation.getScoreType()).isEqualTo(UPDATED_SCORE_TYPE);
        assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(UPDATED_EDUCATION_DURATION);

        // Validate the CandidateEducation in Elasticsearch
        CandidateEducation candidateEducationEs = candidateEducationSearchRepository.findOne(testCandidateEducation.getId());
        assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
    }

    @Test
    @Transactional
    public void updateNonExistingCandidateEducation() throws Exception {
        int databaseSizeBeforeUpdate = candidateEducationRepository.findAll().size();

        // Create the CandidateEducation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
            .andExpect(status().isCreated());

        // Validate the CandidateEducation in the database
        List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
        assertThat(candidateEducationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCandidateEducation() throws Exception {
        // Initialize the database
        candidateEducationRepository.saveAndFlush(candidateEducation);
        candidateEducationSearchRepository.save(candidateEducation);
        int databaseSizeBeforeDelete = candidateEducationRepository.findAll().size();

        // Get the candidateEducation
        restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", candidateEducation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean candidateEducationExistsInEs = candidateEducationSearchRepository.exists(candidateEducation.getId());
        assertThat(candidateEducationExistsInEs).isFalse();

        // Validate the database is empty
        List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
        assertThat(candidateEducationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCandidateEducation() throws Exception {
        // Initialize the database
        candidateEducationRepository.saveAndFlush(candidateEducation);
        candidateEducationSearchRepository.save(candidateEducation);

        // Search the candidateEducation
        restCandidateEducationMockMvc.perform(get("/api/_search/candidate-educations?query=id:" + candidateEducation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateEducation.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())))
            .andExpect(jsonPath("$.[*].educationFromDate").value(hasItem(DEFAULT_EDUCATION_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].educationToDate").value(hasItem(DEFAULT_EDUCATION_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPursuingEducation").value(hasItem(DEFAULT_IS_PURSUING_EDUCATION.booleanValue())))
            .andExpect(jsonPath("$.[*].gradeScale").value(hasItem(DEFAULT_GRADE_SCALE)))
            .andExpect(jsonPath("$.[*].highestQualification").value(hasItem(DEFAULT_HIGHEST_QUALIFICATION.booleanValue())))
            .andExpect(jsonPath("$.[*].roundOfGrade").value(hasItem(DEFAULT_ROUND_OF_GRADE)))
            .andExpect(jsonPath("$.[*].gradeDecimal").value(hasItem(DEFAULT_GRADE_DECIMAL)))
            .andExpect(jsonPath("$.[*].capturedCourse").value(hasItem(DEFAULT_CAPTURED_COURSE.toString())))
            .andExpect(jsonPath("$.[*].capturedQualification").value(hasItem(DEFAULT_CAPTURED_QUALIFICATION.toString())))
            .andExpect(jsonPath("$.[*].capturedCollege").value(hasItem(DEFAULT_CAPTURED_COLLEGE.toString())))
            .andExpect(jsonPath("$.[*].capturedUniversity").value(hasItem(DEFAULT_CAPTURED_UNIVERSITY.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].scoreType").value(hasItem(DEFAULT_SCORE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].educationDuration").value(hasItem(DEFAULT_EDUCATION_DURATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateEducation.class);
        CandidateEducation candidateEducation1 = new CandidateEducation();
        candidateEducation1.setId(1L);
        CandidateEducation candidateEducation2 = new CandidateEducation();
        candidateEducation2.setId(candidateEducation1.getId());
        assertThat(candidateEducation1).isEqualTo(candidateEducation2);
        candidateEducation2.setId(2L);
        assertThat(candidateEducation1).isNotEqualTo(candidateEducation2);
        candidateEducation1.setId(null);
        assertThat(candidateEducation1).isNotEqualTo(candidateEducation2);
    }
}
