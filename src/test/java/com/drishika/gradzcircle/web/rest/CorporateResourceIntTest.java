package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
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
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.service.CorporateService;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CorporateResource REST controller.
 *
 * @see CorporateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CorporateResourceIntTest {

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
	private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

	private static final String DEFAULT_CITY = "AAAAAAAAAA";
	private static final String UPDATED_CITY = "Bbbbbbbbbb";

	private static final LocalDate DEFAULT_ESTABLISHED_SINCE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_ESTABLISHED_SINCE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
	private static final String UPDATED_EMAIL = "BBBBBBBBBB";

	private static final String DEFAULT_OVERVIEW = "AAAAAAAAAA";
	private static final String UPDATED_OVERVIEW = "BBBBBBBBBB";

	private static final String DEFAULT_BENEFITS = "AAAAAAAAAA";
	private static final String UPDATED_BENEFITS = "BBBBBBBBBB";

	private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
	private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

	private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
	private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

	private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
	private static final String UPDATED_TWITTER = "BBBBBBBBBB";

	private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
	private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

	private static final String DEFAULT_LINKED_IN = "AAAAAAAAAA";
	private static final String UPDATED_LINKED_IN = "BBBBBBBBBB";

	private static final String DEFAULT_CULTURE = "AAAAAAAAAA";
	private static final String UPDATED_CULTURE = "BBBBBBBBBB";

	private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
	private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

	private static final String DEFAULT_PHONE = "AAAAAAAAAA";
	private static final String UPDATED_PHONE = "BBBBBBBBBB";

	private static final String DEFAULT_PHONE_CODE = "AAAAAAAAAA";
	private static final String UPDATED_PHONE_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_PERSON_DESIGNATION = "AAAAAAAAAA";
	private static final String UPDATED_PERSON_DESIGNATION = "BBBBBBBBBB";

	private static final String DEFAULT_TAG_LINE = "AAAAAAAAAA";
	private static final String UPDATED_TAG_LINE = "BBBBBBBBBB";

	private static final Double DEFAULT_ESCROW_AMOUNT = 1D;
	private static final Double UPDATED_ESCROW_AMOUNT = 2D;

	@Autowired
	private CorporateRepository corporateRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CorporateSearchRepository mockCorporateSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private CorporateService corporateService;
	

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private EntityManager em;

	private MockMvc restCorporateMockMvc;

	private Corporate corporate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CorporateResource corporateResource = new CorporateResource(corporateRepository,
				mockCorporateSearchRepository, corporateService);
		this.restCorporateMockMvc = MockMvcBuilders.standaloneSetup(corporateResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Corporate createEntity(EntityManager em) {
		Corporate corporate = new Corporate().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).city(DEFAULT_CITY)
				.establishedSince(DEFAULT_ESTABLISHED_SINCE).email(DEFAULT_EMAIL).overview(DEFAULT_OVERVIEW)
				.benefits(DEFAULT_BENEFITS).website(DEFAULT_WEBSITE).facebook(DEFAULT_FACEBOOK).twitter(DEFAULT_TWITTER)
				.instagram(DEFAULT_INSTAGRAM).linkedIn(DEFAULT_LINKED_IN).culture(DEFAULT_CULTURE)
				.contactPerson(DEFAULT_CONTACT_PERSON).phone(DEFAULT_PHONE).phoneCode(DEFAULT_PHONE_CODE)
				.personDesignation(DEFAULT_PERSON_DESIGNATION).tagLine(DEFAULT_TAG_LINE)
				.escrowAmount(DEFAULT_ESCROW_AMOUNT);
		return corporate;
	}

	@Before
	public void initTest() {
		//corporateSearchRepository.deleteAll();
		corporate = createEntity(em);
	}

	@Test
	@Transactional
	public void createCorporate() throws Exception {
		int databaseSizeBeforeCreate = corporateRepository.findAll().size();
		
		// Create the Corporate
		restCorporateMockMvc.perform(post("/api/corporates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(corporate))).andExpect(status().isCreated());

		// Validate the Corporate in the database
		List<Corporate> corporateList = corporateRepository.findAll();
		assertThat(corporateList).hasSize(databaseSizeBeforeCreate + 1);
		Corporate testCorporate = corporateList.get(corporateList.size() - 1);
		assertThat(testCorporate.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testCorporate.getAddress()).isEqualTo(DEFAULT_ADDRESS);
		assertThat(testCorporate.getCity()).isEqualTo(DEFAULT_CITY);
		assertThat(testCorporate.getEstablishedSince()).isEqualTo(DEFAULT_ESTABLISHED_SINCE);
		assertThat(testCorporate.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(testCorporate.getOverview()).isEqualTo(DEFAULT_OVERVIEW);
		assertThat(testCorporate.getBenefits()).isEqualTo(DEFAULT_BENEFITS);
		assertThat(testCorporate.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
		assertThat(testCorporate.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
		assertThat(testCorporate.getTwitter()).isEqualTo(DEFAULT_TWITTER);
		assertThat(testCorporate.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
		assertThat(testCorporate.getLinkedIn()).isEqualTo(DEFAULT_LINKED_IN);
		assertThat(testCorporate.getCulture()).isEqualTo(DEFAULT_CULTURE);
		assertThat(testCorporate.getContactPerson()).isEqualTo(DEFAULT_CONTACT_PERSON);
		assertThat(testCorporate.getPhone()).isEqualTo(DEFAULT_PHONE);
		assertThat(testCorporate.getPhoneCode()).isEqualTo(DEFAULT_PHONE_CODE);
		assertThat(testCorporate.getPersonDesignation()).isEqualTo(DEFAULT_PERSON_DESIGNATION);
		assertThat(testCorporate.getTagLine()).isEqualTo(DEFAULT_TAG_LINE);
		assertThat(testCorporate.getEscrowAmount()).isEqualTo(DEFAULT_ESCROW_AMOUNT);

		// Validate the Corporate in Elasticsearch
		//no need as am not saving in elastic
	}

	@Test
	@Transactional
	public void createCorporateWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = corporateRepository.findAll().size();

		// Create the Corporate with an existing ID
		corporate.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCorporateMockMvc.perform(post("/api/corporates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(corporate))).andExpect(status().isBadRequest());

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
		restCorporateMockMvc.perform(get("/api/corporates?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
				.andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
				.andExpect(jsonPath("$.[*].establishedSince").value(hasItem(DEFAULT_ESTABLISHED_SINCE.toString())))
				.andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
				.andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())))
				.andExpect(jsonPath("$.[*].benefits").value(hasItem(DEFAULT_BENEFITS.toString())))
				.andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
				.andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
				.andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
				.andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM.toString())))
				.andExpect(jsonPath("$.[*].linkedIn").value(hasItem(DEFAULT_LINKED_IN.toString())))
				.andExpect(jsonPath("$.[*].culture").value(hasItem(DEFAULT_CULTURE.toString())))
				.andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON.toString())))
				.andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
				.andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE.toString())))
				.andExpect(jsonPath("$.[*].personDesignation").value(hasItem(DEFAULT_PERSON_DESIGNATION.toString())))
				.andExpect(jsonPath("$.[*].tagLine").value(hasItem(DEFAULT_TAG_LINE.toString())))
				.andExpect(jsonPath("$.[*].escrowAmount").value(hasItem(DEFAULT_ESCROW_AMOUNT.doubleValue())));
	}

	@Test
	@Transactional
	public void getCorporate() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);

		// Get the corporate
		restCorporateMockMvc.perform(get("/api/corporates/{id}", corporate.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(corporate.getId().intValue()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
				.andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
				.andExpect(jsonPath("$.establishedSince").value(DEFAULT_ESTABLISHED_SINCE.toString()))
				.andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
				.andExpect(jsonPath("$.overview").value(DEFAULT_OVERVIEW.toString()))
				.andExpect(jsonPath("$.benefits").value(DEFAULT_BENEFITS.toString()))
				.andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
				.andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
				.andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER.toString()))
				.andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM.toString()))
				.andExpect(jsonPath("$.linkedIn").value(DEFAULT_LINKED_IN.toString()))
				.andExpect(jsonPath("$.culture").value(DEFAULT_CULTURE.toString()))
				.andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON.toString()))
				.andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
				.andExpect(jsonPath("$.phoneCode").value(DEFAULT_PHONE_CODE.toString()))
				.andExpect(jsonPath("$.personDesignation").value(DEFAULT_PERSON_DESIGNATION.toString()))
				.andExpect(jsonPath("$.tagLine").value(DEFAULT_TAG_LINE.toString()))
				.andExpect(jsonPath("$.escrowAmount").value(DEFAULT_ESCROW_AMOUNT.doubleValue()));
	}

	@Test
	@Transactional
	public void getNonExistingCorporate() throws Exception {
		// Get the corporate
		restCorporateMockMvc.perform(get("/api/corporates/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCorporate() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		//corporateSearchRepository.save(corporate);
		Country country = new Country().countryName("INDIA");
		country.setDisplay("India");
		country.setValue("India");
		countryRepository.saveAndFlush(country);
		int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

		// Update the corporate
		Corporate updatedCorporate = corporateRepository.findById(corporate.getId()).get();
		updatedCorporate.name(UPDATED_NAME).address(UPDATED_ADDRESS).city(UPDATED_CITY)
				.establishedSince(UPDATED_ESTABLISHED_SINCE).email(UPDATED_EMAIL).overview(UPDATED_OVERVIEW)
				.benefits(UPDATED_BENEFITS).website(UPDATED_WEBSITE).facebook(UPDATED_FACEBOOK).twitter(UPDATED_TWITTER)
				.instagram(UPDATED_INSTAGRAM).linkedIn(UPDATED_LINKED_IN).culture(UPDATED_CULTURE)
				.contactPerson(UPDATED_CONTACT_PERSON).phone(UPDATED_PHONE).phoneCode(UPDATED_PHONE_CODE)
				.personDesignation(UPDATED_PERSON_DESIGNATION).tagLine(UPDATED_TAG_LINE)
				.escrowAmount(UPDATED_ESCROW_AMOUNT);

		restCorporateMockMvc.perform(put("/api/corporates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCorporate.country(country)))).andExpect(status().isOk());

		// Validate the Corporate in the database
		List<Corporate> corporateList = corporateRepository.findAll();
		assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
		Corporate testCorporate = corporateList.get(corporateList.size() - 1);
		assertThat(testCorporate.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testCorporate.getAddress()).isEqualTo(UPDATED_ADDRESS);
		assertThat(testCorporate.getCity()).isEqualTo(UPDATED_CITY);
		assertThat(testCorporate.getEstablishedSince()).isEqualTo(UPDATED_ESTABLISHED_SINCE);
		assertThat(testCorporate.getEmail()).isEqualTo(UPDATED_EMAIL);
		assertThat(testCorporate.getOverview()).isEqualTo(UPDATED_OVERVIEW);
		assertThat(testCorporate.getBenefits()).isEqualTo(UPDATED_BENEFITS);
		assertThat(testCorporate.getWebsite()).isEqualTo(UPDATED_WEBSITE);
		assertThat(testCorporate.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
		assertThat(testCorporate.getTwitter()).isEqualTo(UPDATED_TWITTER);
		assertThat(testCorporate.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
		assertThat(testCorporate.getLinkedIn()).isEqualTo(UPDATED_LINKED_IN);
		assertThat(testCorporate.getCulture()).isEqualTo(UPDATED_CULTURE);
		assertThat(testCorporate.getContactPerson()).isEqualTo(UPDATED_CONTACT_PERSON);
		assertThat(testCorporate.getPhone()).isEqualTo(UPDATED_PHONE);
		assertThat(testCorporate.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
		assertThat(testCorporate.getPersonDesignation()).isEqualTo(UPDATED_PERSON_DESIGNATION);
		assertThat(testCorporate.getTagLine()).isEqualTo(UPDATED_TAG_LINE);
		assertThat(testCorporate.getEscrowAmount()).isEqualTo(UPDATED_ESCROW_AMOUNT);

		// Validate the Corporate in Elasticsearch
		//no need as am not saving in elastic
	}

	@Test
	@Transactional
	public void updateNonExistingCorporate() throws Exception {
		int databaseSizeBeforeUpdate = corporateRepository.findAll().size();

		// Create the Corporate

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
	
		restCorporateMockMvc.perform(put("/api/corporates")
		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
		            .content(TestUtil.convertObjectToJsonBytes(corporate)))
		            .andExpect(status().isBadRequest());
		// Validate the Corporate in the database
		List<Corporate> corporateList = corporateRepository.findAll();
		assertThat(corporateList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteCorporate() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		//corporateSearchRepository.save(corporate);
		int databaseSizeBeforeDelete = corporateRepository.findAll().size();

		// Get the corporate
		restCorporateMockMvc
				.perform(delete("/api/corporates/{id}", corporate.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		verify(mockCorporateSearchRepository,timeout(1)).deleteById(corporate.getId());

		// Validate the database is empty
		List<Corporate> corporateList = corporateRepository.findAll();
		assertThat(corporateList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCorporate() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		 when(mockCorporateSearchRepository.search(queryStringQuery("id:" + corporate.getId())))
         .thenReturn(Collections.singletonList(corporate));
		 
		 
		// Search the corporate
		restCorporateMockMvc.perform(get("/api/_search/corporates?query=id:" + corporate.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(corporate.getId().intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
				.andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
				.andExpect(jsonPath("$.[*].establishedSince").value(hasItem(DEFAULT_ESTABLISHED_SINCE.toString())))
				.andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
				.andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())))
				.andExpect(jsonPath("$.[*].benefits").value(hasItem(DEFAULT_BENEFITS.toString())))
				.andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
				.andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
				.andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
				.andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM.toString())))
				.andExpect(jsonPath("$.[*].linkedIn").value(hasItem(DEFAULT_LINKED_IN.toString())))
				.andExpect(jsonPath("$.[*].culture").value(hasItem(DEFAULT_CULTURE.toString())))
				.andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON.toString())))
				.andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
				.andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE.toString())))
				.andExpect(jsonPath("$.[*].personDesignation").value(hasItem(DEFAULT_PERSON_DESIGNATION.toString())))
				.andExpect(jsonPath("$.[*].tagLine").value(hasItem(DEFAULT_TAG_LINE.toString())))
				.andExpect(jsonPath("$.[*].escrowAmount").value(hasItem(DEFAULT_ESCROW_AMOUNT.doubleValue())));
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
	
	@Test
	@Transactional
	public void findAllLinkedCandidatesForCorporte() throws Exception{
		
		Candidate c1 = new Candidate().firstName("abhinav");
		Candidate c2 = new Candidate().firstName("Bunny");
		Candidate c3 = new Candidate().firstName("Tom");
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		candidateRepository.saveAndFlush(c3);
		corporateRepository.saveAndFlush(corporate);
		CorporateCandidate cc1 = new CorporateCandidate(corporate,c1,-1L);
		CorporateCandidate cc2 = new CorporateCandidate(corporate,c2,-1L);
		corporate.addCorporateCandidate(cc1);
		corporate.addCorporateCandidate(cc2);
		corporateRepository.saveAndFlush(corporate);
		restCorporateMockMvc.perform(get("/api/totalLinkedCandidates/{id}", corporate.getId())).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$").value(2));
		
		
	}
	
	@Test
	@Transactional
	public void findLinkedCandidatesForCorporte() throws Exception{
		Corporate corp = new Corporate();
		Candidate c1 = new Candidate().firstName("abhinav");
		Candidate c2 = new Candidate().firstName("Bunny");
		Candidate c3 = new Candidate().firstName("Tom");
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		candidateRepository.saveAndFlush(c3);
		corporateRepository.saveAndFlush(corporate);
		corporateRepository.saveAndFlush(corp);
		CorporateCandidate cc1 = new CorporateCandidate(corporate,c1,-1L);
		CorporateCandidate cc2 = new CorporateCandidate(corporate,c2,-1L);
		CorporateCandidate cc3 = new CorporateCandidate(corp,c2,-1L);
		corporate.addCorporateCandidate(cc1);
		corporate.addCorporateCandidate(cc2);
		corporate.addCorporateCandidate(cc3);
		corporateRepository.saveAndFlush(corporate);
		restCorporateMockMvc.perform(get("/api/linkedCandidatesForCorporate/{id}", corporate.getId())).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$", hasSize(2)));
		
		
	}
}
