package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
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
 * Test class for the CorporateResource REST controller.
 *
 * @see CorporateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CorporateResourceIntTest {

    private static final String DEFAULT_CORPORATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_CITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESTABLISHED_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTABLISHED_SINCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CORPORATE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_OVERVIEW = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_OVERVIEW = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_BENEFITS = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_BENEFITS = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_LINKED_IN = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_LINKED_IN = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_CULTURE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_CULTURE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_PHONE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_PHONE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_TAG_LINE = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_TAG_LINE = "BBBBBBBBBB";

    @Autowired
    private CorporateRepository corporateRepository;

    @Autowired
    private CorporateSearchRepository corporateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorporateMockMvc;

    private Corporate corporate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorporateResource corporateResource = new CorporateResource(corporateRepository, corporateSearchRepository);
        this.restCorporateMockMvc = MockMvcBuilders.standaloneSetup(corporateResource)
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
    public static Corporate createEntity(EntityManager em) {
        Corporate corporate = new Corporate()
            .corporateName(DEFAULT_CORPORATE_NAME)
            .corporateAddress(DEFAULT_CORPORATE_ADDRESS)
            .corporateCity(DEFAULT_CORPORATE_CITY)
            .establishedSince(DEFAULT_ESTABLISHED_SINCE)
            .corporateEmail(DEFAULT_CORPORATE_EMAIL)
            .corporateOverview(DEFAULT_CORPORATE_OVERVIEW)
            .corporateBenefits(DEFAULT_CORPORATE_BENEFITS)
            .corporateWebsite(DEFAULT_CORPORATE_WEBSITE)
            .corporateFacebook(DEFAULT_CORPORATE_FACEBOOK)
            .corporateTwitter(DEFAULT_CORPORATE_TWITTER)
            .corporateInstagram(DEFAULT_CORPORATE_INSTAGRAM)
            .corporateLinkedIn(DEFAULT_CORPORATE_LINKED_IN)
            .corporateCulture(DEFAULT_CORPORATE_CULTURE)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .corporatePhone(DEFAULT_CORPORATE_PHONE)
            .corporatePhoneCode(DEFAULT_CORPORATE_PHONE_CODE)
            .contactPersonDesignation(DEFAULT_CONTACT_PERSON_DESIGNATION)
            .corporateTagLine(DEFAULT_CORPORATE_TAG_LINE);
        return corporate;
    }

    @Before
    public void initTest() {
        corporateSearchRepository.deleteAll();
        corporate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorporate() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // Create the Corporate
        restCorporateMockMvc.perform(post("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isCreated());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate + 1);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCorporateName()).isEqualTo(DEFAULT_CORPORATE_NAME);
        assertThat(testCorporate.getCorporateAddress()).isEqualTo(DEFAULT_CORPORATE_ADDRESS);
        assertThat(testCorporate.getCorporateCity()).isEqualTo(DEFAULT_CORPORATE_CITY);
        assertThat(testCorporate.getEstablishedSince()).isEqualTo(DEFAULT_ESTABLISHED_SINCE);
        assertThat(testCorporate.getCorporateEmail()).isEqualTo(DEFAULT_CORPORATE_EMAIL);
        assertThat(testCorporate.getCorporateOverview()).isEqualTo(DEFAULT_CORPORATE_OVERVIEW);
        assertThat(testCorporate.getCorporateBenefits()).isEqualTo(DEFAULT_CORPORATE_BENEFITS);
        assertThat(testCorporate.getCorporateWebsite()).isEqualTo(DEFAULT_CORPORATE_WEBSITE);
        assertThat(testCorporate.getCorporateFacebook()).isEqualTo(DEFAULT_CORPORATE_FACEBOOK);
        assertThat(testCorporate.getCorporateTwitter()).isEqualTo(DEFAULT_CORPORATE_TWITTER);
        assertThat(testCorporate.getCorporateInstagram()).isEqualTo(DEFAULT_CORPORATE_INSTAGRAM);
        assertThat(testCorporate.getCorporateLinkedIn()).isEqualTo(DEFAULT_CORPORATE_LINKED_IN);
        assertThat(testCorporate.getCorporateCulture()).isEqualTo(DEFAULT_CORPORATE_CULTURE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
        assertThat(testCorporate.getCorporatePhone()).isEqualTo(DEFAULT_CORPORATE_PHONE);
        assertThat(testCorporate.getCorporatePhoneCode()).isEqualTo(DEFAULT_CORPORATE_PHONE_CODE);
        assertThat(testCorporate.getContactPersonDesignation()).isEqualTo(DEFAULT_CONTACT_PERSON_DESIGNATION);
        assertThat(testCorporate.getCorporateTagLine()).isEqualTo(DEFAULT_CORPORATE_TAG_LINE);

        // Validate the Corporate in Elasticsearch
        Corporate corporateEs = corporateSearchRepository.findOne(testCorporate.getId());
        assertThat(corporateEs).isEqualToComparingFieldByField(testCorporate);
    }

    @Test
    @Transactional
    public void createCorporateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corporateRepository.findAll().size();

        // Create the Corporate with an existing ID
        corporate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporateMockMvc.perform(post("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isBadRequest());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorporates() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get all the corporateList
        restCorporateMockMvc.perform(get("/api/corporates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
            .andExpect(jsonPath("$.[*].corporateName").value(hasItem(DEFAULT_CORPORATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].corporateAddress").value(hasItem(DEFAULT_CORPORATE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].corporateCity").value(hasItem(DEFAULT_CORPORATE_CITY.toString())))
            .andExpect(jsonPath("$.[*].establishedSince").value(hasItem(DEFAULT_ESTABLISHED_SINCE.toString())))
            .andExpect(jsonPath("$.[*].corporateEmail").value(hasItem(DEFAULT_CORPORATE_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].corporateOverview").value(hasItem(DEFAULT_CORPORATE_OVERVIEW.toString())))
            .andExpect(jsonPath("$.[*].corporateBenefits").value(hasItem(DEFAULT_CORPORATE_BENEFITS.toString())))
            .andExpect(jsonPath("$.[*].corporateWebsite").value(hasItem(DEFAULT_CORPORATE_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].corporateFacebook").value(hasItem(DEFAULT_CORPORATE_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].corporateTwitter").value(hasItem(DEFAULT_CORPORATE_TWITTER.toString())))
            .andExpect(jsonPath("$.[*].corporateInstagram").value(hasItem(DEFAULT_CORPORATE_INSTAGRAM.toString())))
            .andExpect(jsonPath("$.[*].corporateLinkedIn").value(hasItem(DEFAULT_CORPORATE_LINKED_IN.toString())))
            .andExpect(jsonPath("$.[*].corporateCulture").value(hasItem(DEFAULT_CORPORATE_CULTURE.toString())))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON.toString())))
            .andExpect(jsonPath("$.[*].corporatePhone").value(hasItem(DEFAULT_CORPORATE_PHONE.toString())))
            .andExpect(jsonPath("$.[*].corporatePhoneCode").value(hasItem(DEFAULT_CORPORATE_PHONE_CODE.toString())))
            .andExpect(jsonPath("$.[*].contactPersonDesignation").value(hasItem(DEFAULT_CONTACT_PERSON_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].corporateTagLine").value(hasItem(DEFAULT_CORPORATE_TAG_LINE.toString())));
    }

    @Test
    @Transactional
    public void getCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);

        // Get the corporate
        restCorporateMockMvc.perform(get("/api/corporates/{id}", corporate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corporate.getId().intValue()))
            .andExpect(jsonPath("$.corporateName").value(DEFAULT_CORPORATE_NAME.toString()))
            .andExpect(jsonPath("$.corporateAddress").value(DEFAULT_CORPORATE_ADDRESS.toString()))
            .andExpect(jsonPath("$.corporateCity").value(DEFAULT_CORPORATE_CITY.toString()))
            .andExpect(jsonPath("$.establishedSince").value(DEFAULT_ESTABLISHED_SINCE.toString()))
            .andExpect(jsonPath("$.corporateEmail").value(DEFAULT_CORPORATE_EMAIL.toString()))
            .andExpect(jsonPath("$.corporateOverview").value(DEFAULT_CORPORATE_OVERVIEW.toString()))
            .andExpect(jsonPath("$.corporateBenefits").value(DEFAULT_CORPORATE_BENEFITS.toString()))
            .andExpect(jsonPath("$.corporateWebsite").value(DEFAULT_CORPORATE_WEBSITE.toString()))
            .andExpect(jsonPath("$.corporateFacebook").value(DEFAULT_CORPORATE_FACEBOOK.toString()))
            .andExpect(jsonPath("$.corporateTwitter").value(DEFAULT_CORPORATE_TWITTER.toString()))
            .andExpect(jsonPath("$.corporateInstagram").value(DEFAULT_CORPORATE_INSTAGRAM.toString()))
            .andExpect(jsonPath("$.corporateLinkedIn").value(DEFAULT_CORPORATE_LINKED_IN.toString()))
            .andExpect(jsonPath("$.corporateCulture").value(DEFAULT_CORPORATE_CULTURE.toString()))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON.toString()))
            .andExpect(jsonPath("$.corporatePhone").value(DEFAULT_CORPORATE_PHONE.toString()))
            .andExpect(jsonPath("$.corporatePhoneCode").value(DEFAULT_CORPORATE_PHONE_CODE.toString()))
            .andExpect(jsonPath("$.contactPersonDesignation").value(DEFAULT_CONTACT_PERSON_DESIGNATION.toString()))
            .andExpect(jsonPath("$.corporateTagLine").value(DEFAULT_CORPORATE_TAG_LINE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorporate() throws Exception {
        // Get the corporate
        restCorporateMockMvc.perform(get("/api/corporates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);
        corporateSearchRepository.save(corporate);
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Update the corporate
        Corporate updatedCorporate = corporateRepository.findOne(corporate.getId());
        updatedCorporate
            .corporateName(UPDATED_CORPORATE_NAME)
            .corporateAddress(UPDATED_CORPORATE_ADDRESS)
            .corporateCity(UPDATED_CORPORATE_CITY)
            .establishedSince(UPDATED_ESTABLISHED_SINCE)
            .corporateEmail(UPDATED_CORPORATE_EMAIL)
            .corporateOverview(UPDATED_CORPORATE_OVERVIEW)
            .corporateBenefits(UPDATED_CORPORATE_BENEFITS)
            .corporateWebsite(UPDATED_CORPORATE_WEBSITE)
            .corporateFacebook(UPDATED_CORPORATE_FACEBOOK)
            .corporateTwitter(UPDATED_CORPORATE_TWITTER)
            .corporateInstagram(UPDATED_CORPORATE_INSTAGRAM)
            .corporateLinkedIn(UPDATED_CORPORATE_LINKED_IN)
            .corporateCulture(UPDATED_CORPORATE_CULTURE)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .corporatePhone(UPDATED_CORPORATE_PHONE)
            .corporatePhoneCode(UPDATED_CORPORATE_PHONE_CODE)
            .contactPersonDesignation(UPDATED_CONTACT_PERSON_DESIGNATION)
            .corporateTagLine(UPDATED_CORPORATE_TAG_LINE);

        restCorporateMockMvc.perform(put("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCorporate)))
            .andExpect(status().isOk());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
        Corporate testCorporate = corporateList.get(corporateList.size() - 1);
        assertThat(testCorporate.getCorporateName()).isEqualTo(UPDATED_CORPORATE_NAME);
        assertThat(testCorporate.getCorporateAddress()).isEqualTo(UPDATED_CORPORATE_ADDRESS);
        assertThat(testCorporate.getCorporateCity()).isEqualTo(UPDATED_CORPORATE_CITY);
        assertThat(testCorporate.getEstablishedSince()).isEqualTo(UPDATED_ESTABLISHED_SINCE);
        assertThat(testCorporate.getCorporateEmail()).isEqualTo(UPDATED_CORPORATE_EMAIL);
        assertThat(testCorporate.getCorporateOverview()).isEqualTo(UPDATED_CORPORATE_OVERVIEW);
        assertThat(testCorporate.getCorporateBenefits()).isEqualTo(UPDATED_CORPORATE_BENEFITS);
        assertThat(testCorporate.getCorporateWebsite()).isEqualTo(UPDATED_CORPORATE_WEBSITE);
        assertThat(testCorporate.getCorporateFacebook()).isEqualTo(UPDATED_CORPORATE_FACEBOOK);
        assertThat(testCorporate.getCorporateTwitter()).isEqualTo(UPDATED_CORPORATE_TWITTER);
        assertThat(testCorporate.getCorporateInstagram()).isEqualTo(UPDATED_CORPORATE_INSTAGRAM);
        assertThat(testCorporate.getCorporateLinkedIn()).isEqualTo(UPDATED_CORPORATE_LINKED_IN);
        assertThat(testCorporate.getCorporateCulture()).isEqualTo(UPDATED_CORPORATE_CULTURE);
        assertThat(testCorporate.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
        assertThat(testCorporate.getCorporatePhone()).isEqualTo(UPDATED_CORPORATE_PHONE);
        assertThat(testCorporate.getCorporatePhoneCode()).isEqualTo(UPDATED_CORPORATE_PHONE_CODE);
        assertThat(testCorporate.getContactPersonDesignation()).isEqualTo(UPDATED_CONTACT_PERSON_DESIGNATION);
        assertThat(testCorporate.getCorporateTagLine()).isEqualTo(UPDATED_CORPORATE_TAG_LINE);

        // Validate the Corporate in Elasticsearch
        Corporate corporateEs = corporateSearchRepository.findOne(testCorporate.getId());
        assertThat(corporateEs).isEqualToComparingFieldByField(testCorporate);
    }

    @Test
    @Transactional
    public void updateNonExistingCorporate() throws Exception {
        int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

        // Create the Corporate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorporateMockMvc.perform(put("/api/corporates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporate)))
            .andExpect(status().isCreated());

        // Validate the Corporate in the database
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);
        corporateSearchRepository.save(corporate);
        int databaseSizeBeforeDelete = corporateRepository.findAll().size();

        // Get the corporate
        restCorporateMockMvc.perform(delete("/api/corporates/{id}", corporate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean corporateExistsInEs = corporateSearchRepository.exists(corporate.getId());
        assertThat(corporateExistsInEs).isFalse();

        // Validate the database is empty
        List<Corporate> corporateList = corporateRepository.findAll();
        assertThat(corporateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorporate() throws Exception {
        // Initialize the database
        corporateRepository.saveAndFlush(corporate);
        corporateSearchRepository.save(corporate);

        // Search the corporate
        restCorporateMockMvc.perform(get("/api/_search/corporates?query=id:" + corporate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
            .andExpect(jsonPath("$.[*].corporateName").value(hasItem(DEFAULT_CORPORATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].corporateAddress").value(hasItem(DEFAULT_CORPORATE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].corporateCity").value(hasItem(DEFAULT_CORPORATE_CITY.toString())))
            .andExpect(jsonPath("$.[*].establishedSince").value(hasItem(DEFAULT_ESTABLISHED_SINCE.toString())))
            .andExpect(jsonPath("$.[*].corporateEmail").value(hasItem(DEFAULT_CORPORATE_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].corporateOverview").value(hasItem(DEFAULT_CORPORATE_OVERVIEW.toString())))
            .andExpect(jsonPath("$.[*].corporateBenefits").value(hasItem(DEFAULT_CORPORATE_BENEFITS.toString())))
            .andExpect(jsonPath("$.[*].corporateWebsite").value(hasItem(DEFAULT_CORPORATE_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].corporateFacebook").value(hasItem(DEFAULT_CORPORATE_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].corporateTwitter").value(hasItem(DEFAULT_CORPORATE_TWITTER.toString())))
            .andExpect(jsonPath("$.[*].corporateInstagram").value(hasItem(DEFAULT_CORPORATE_INSTAGRAM.toString())))
            .andExpect(jsonPath("$.[*].corporateLinkedIn").value(hasItem(DEFAULT_CORPORATE_LINKED_IN.toString())))
            .andExpect(jsonPath("$.[*].corporateCulture").value(hasItem(DEFAULT_CORPORATE_CULTURE.toString())))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON.toString())))
            .andExpect(jsonPath("$.[*].corporatePhone").value(hasItem(DEFAULT_CORPORATE_PHONE.toString())))
            .andExpect(jsonPath("$.[*].corporatePhoneCode").value(hasItem(DEFAULT_CORPORATE_PHONE_CODE.toString())))
            .andExpect(jsonPath("$.[*].contactPersonDesignation").value(hasItem(DEFAULT_CONTACT_PERSON_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].corporateTagLine").value(hasItem(DEFAULT_CORPORATE_TAG_LINE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corporate.class);
        Corporate corporate1 = new Corporate();
        corporate1.setId(1L);
        Corporate corporate2 = new Corporate();
        corporate2.setId(corporate1.getId());
        assertThat(corporate1).isEqualTo(corporate2);
        corporate2.setId(2L);
        assertThat(corporate1).isNotEqualTo(corporate2);
        corporate1.setId(null);
        assertThat(corporate1).isNotEqualTo(corporate2);
    }
}
