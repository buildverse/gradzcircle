package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
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
 * Test class for the CandidateResource REST controller.
 *
 * @see CandidateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_LINKED_IN = "AAAAAAAAAA";
    private static final String UPDATED_LINKED_IN = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT_ME = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT_ME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DIFFERENTLY_ABLED = false;
    private static final Boolean UPDATED_DIFFERENTLY_ABLED = true;

    private static final Boolean DEFAULT_AVAILABLE_FOR_HIRING = false;
    private static final Boolean UPDATED_AVAILABLE_FOR_HIRING = true;

    private static final Boolean DEFAULT_OPEN_TO_RELOCATE = false;
    private static final Boolean UPDATED_OPEN_TO_RELOCATE = true;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateSearchRepository candidateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCandidateMockMvc;

    private Candidate candidate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CandidateResource candidateResource = new CandidateResource(candidateRepository, candidateSearchRepository);
        this.restCandidateMockMvc = MockMvcBuilders.standaloneSetup(candidateResource)
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
    public static Candidate createEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .facebook(DEFAULT_FACEBOOK)
            .linkedIn(DEFAULT_LINKED_IN)
            .twitter(DEFAULT_TWITTER)
            .aboutMe(DEFAULT_ABOUT_ME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .phoneCode(DEFAULT_PHONE_CODE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .differentlyAbled(DEFAULT_DIFFERENTLY_ABLED)
            .availableForHiring(DEFAULT_AVAILABLE_FOR_HIRING)
            .openToRelocate(DEFAULT_OPEN_TO_RELOCATE);
        return candidate;
    }

    @Before
    public void initTest() {
        candidateSearchRepository.deleteAll();
        candidate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCandidate() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().size();

        // Create the Candidate
        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isCreated());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate + 1);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCandidate.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testCandidate.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testCandidate.getLinkedIn()).isEqualTo(DEFAULT_LINKED_IN);
        assertThat(testCandidate.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testCandidate.getAboutMe()).isEqualTo(DEFAULT_ABOUT_ME);
        assertThat(testCandidate.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testCandidate.getPhoneCode()).isEqualTo(DEFAULT_PHONE_CODE);
        assertThat(testCandidate.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCandidate.isDifferentlyAbled()).isEqualTo(DEFAULT_DIFFERENTLY_ABLED);
        assertThat(testCandidate.isAvailableForHiring()).isEqualTo(DEFAULT_AVAILABLE_FOR_HIRING);
        assertThat(testCandidate.isOpenToRelocate()).isEqualTo(DEFAULT_OPEN_TO_RELOCATE);

        // Validate the Candidate in Elasticsearch
        Candidate candidateEs = candidateSearchRepository.findOne(testCandidate.getId());
        assertThat(candidateEs).isEqualToComparingFieldByField(testCandidate);
    }

    @Test
    @Transactional
    public void createCandidateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().size();

        // Create the Candidate with an existing ID
        candidate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateMockMvc.perform(post("/api/candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isBadRequest());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCandidates() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get all the candidateList
        restCandidateMockMvc.perform(get("/api/candidates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].linkedIn").value(hasItem(DEFAULT_LINKED_IN.toString())))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].differentlyAbled").value(hasItem(DEFAULT_DIFFERENTLY_ABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].availableForHiring").value(hasItem(DEFAULT_AVAILABLE_FOR_HIRING.booleanValue())))
            .andExpect(jsonPath("$.[*].openToRelocate").value(hasItem(DEFAULT_OPEN_TO_RELOCATE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);

        // Get the candidate
        restCandidateMockMvc.perform(get("/api/candidates/{id}", candidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.linkedIn").value(DEFAULT_LINKED_IN.toString()))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER.toString()))
            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.phoneCode").value(DEFAULT_PHONE_CODE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.differentlyAbled").value(DEFAULT_DIFFERENTLY_ABLED.booleanValue()))
            .andExpect(jsonPath("$.availableForHiring").value(DEFAULT_AVAILABLE_FOR_HIRING.booleanValue()))
            .andExpect(jsonPath("$.openToRelocate").value(DEFAULT_OPEN_TO_RELOCATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCandidate() throws Exception {
        // Get the candidate
        restCandidateMockMvc.perform(get("/api/candidates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);
        candidateSearchRepository.save(candidate);
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Update the candidate
        Candidate updatedCandidate = candidateRepository.findOne(candidate.getId());
        updatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .facebook(UPDATED_FACEBOOK)
            .linkedIn(UPDATED_LINKED_IN)
            .twitter(UPDATED_TWITTER)
            .aboutMe(UPDATED_ABOUT_ME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .phoneCode(UPDATED_PHONE_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .differentlyAbled(UPDATED_DIFFERENTLY_ABLED)
            .availableForHiring(UPDATED_AVAILABLE_FOR_HIRING)
            .openToRelocate(UPDATED_OPEN_TO_RELOCATE);

        restCandidateMockMvc.perform(put("/api/candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCandidate)))
            .andExpect(status().isOk());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCandidate.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testCandidate.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testCandidate.getLinkedIn()).isEqualTo(UPDATED_LINKED_IN);
        assertThat(testCandidate.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testCandidate.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
        assertThat(testCandidate.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testCandidate.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
        assertThat(testCandidate.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCandidate.isDifferentlyAbled()).isEqualTo(UPDATED_DIFFERENTLY_ABLED);
        assertThat(testCandidate.isAvailableForHiring()).isEqualTo(UPDATED_AVAILABLE_FOR_HIRING);
        assertThat(testCandidate.isOpenToRelocate()).isEqualTo(UPDATED_OPEN_TO_RELOCATE);

        // Validate the Candidate in Elasticsearch
        Candidate candidateEs = candidateSearchRepository.findOne(testCandidate.getId());
        assertThat(candidateEs).isEqualToComparingFieldByField(testCandidate);
    }

    @Test
    @Transactional
    public void updateNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

        // Create the Candidate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCandidateMockMvc.perform(put("/api/candidates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidate)))
            .andExpect(status().isCreated());

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);
        candidateSearchRepository.save(candidate);
        int databaseSizeBeforeDelete = candidateRepository.findAll().size();

        // Get the candidate
        restCandidateMockMvc.perform(delete("/api/candidates/{id}", candidate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean candidateExistsInEs = candidateSearchRepository.exists(candidate.getId());
        assertThat(candidateExistsInEs).isFalse();

        // Validate the database is empty
        List<Candidate> candidateList = candidateRepository.findAll();
        assertThat(candidateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCandidate() throws Exception {
        // Initialize the database
        candidateRepository.saveAndFlush(candidate);
        candidateSearchRepository.save(candidate);

        // Search the candidate
        restCandidateMockMvc.perform(get("/api/_search/candidates?query=id:" + candidate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidate.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].linkedIn").value(hasItem(DEFAULT_LINKED_IN.toString())))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
            .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].differentlyAbled").value(hasItem(DEFAULT_DIFFERENTLY_ABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].availableForHiring").value(hasItem(DEFAULT_AVAILABLE_FOR_HIRING.booleanValue())))
            .andExpect(jsonPath("$.[*].openToRelocate").value(hasItem(DEFAULT_OPEN_TO_RELOCATE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidate.class);
        Candidate candidate1 = new Candidate();
        candidate1.setId(1L);
        Candidate candidate2 = new Candidate();
        candidate2.setId(candidate1.getId());
        assertThat(candidate1).isEqualTo(candidate2);
        candidate2.setId(2L);
        assertThat(candidate1).isNotEqualTo(candidate2);
        candidate1.setId(null);
        assertThat(candidate1).isNotEqualTo(candidate2);
    }
}
