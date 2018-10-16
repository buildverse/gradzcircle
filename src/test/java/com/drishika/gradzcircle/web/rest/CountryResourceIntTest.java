package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CountrySearchRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CountryResourceIntTest {

	private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
	private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_SHORT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_SHORT_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_SHORT_CODE_THREE_CHAR = "AAAAAAAAAA";
	private static final String UPDATED_SHORT_CODE_THREE_CHAR = "BBBBBBBBBB";

	private static final String DEFAULT_COUNTRY_NICE_NAME = "AAAAAAAAAA";
	private static final String UPDATED_COUNTRY_NICE_NAME = "BBBBBBBBBB";

	private static final Integer DEFAULT_NUM_CODE = 1;
	private static final Integer UPDATED_NUM_CODE = 2;

	private static final Integer DEFAULT_PHONE_CODE = 1;
	private static final Integer UPDATED_PHONE_CODE = 2;

	private static final Boolean DEFAULT_ENABLED = false;
	private static final Boolean UPDATED_ENABLED = true;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CountrySearchRepository countrySearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCountryMockMvc;

	private Country country;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CountryResource countryResource = new CountryResource(countryRepository, countrySearchRepository);
		this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Country createEntity(EntityManager em) {
		Country country = new Country().countryName(DEFAULT_COUNTRY_NAME).shortCode(DEFAULT_SHORT_CODE)
				.shortCodeThreeChar(DEFAULT_SHORT_CODE_THREE_CHAR).countryNiceName(DEFAULT_COUNTRY_NICE_NAME)
				.numCode(DEFAULT_NUM_CODE).phoneCode(DEFAULT_PHONE_CODE).enabled(DEFAULT_ENABLED);
		return country;
	}

	@Before
	public void initTest() {
		countrySearchRepository.deleteAll();
		country = createEntity(em);
	}

	@Test
	@Transactional
	public void createCountry() throws Exception {
		int databaseSizeBeforeCreate = countryRepository.findAll().size();

		// Create the Country
		restCountryMockMvc.perform(post("/api/countries").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(country))).andExpect(status().isCreated());

		// Validate the Country in the database
		List<Country> countryList = countryRepository.findAll();
		assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
		Country testCountry = countryList.get(countryList.size() - 1);
		assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
		assertThat(testCountry.getShortCode()).isEqualTo(DEFAULT_SHORT_CODE);
		assertThat(testCountry.getShortCodeThreeChar()).isEqualTo(DEFAULT_SHORT_CODE_THREE_CHAR);
		assertThat(testCountry.getCountryNiceName()).isEqualTo(DEFAULT_COUNTRY_NICE_NAME);
		assertThat(testCountry.getNumCode()).isEqualTo(DEFAULT_NUM_CODE);
		assertThat(testCountry.getPhoneCode()).isEqualTo(DEFAULT_PHONE_CODE);
		assertThat(testCountry.isEnabled()).isEqualTo(DEFAULT_ENABLED);

		// Validate the Country in Elasticsearch
		Country countryEs = countrySearchRepository.findOne(testCountry.getId());
		assertThat(countryEs).isEqualToComparingFieldByField(testCountry);
	}

	@Test
	@Transactional
	public void createCountryWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = countryRepository.findAll().size();

		// Create the Country with an existing ID
		country.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCountryMockMvc.perform(post("/api/countries").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(country))).andExpect(status().isBadRequest());

		// Validate the Country in the database
		List<Country> countryList = countryRepository.findAll();
		assertThat(countryList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCountries() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);

		// Get all the countryList
		restCountryMockMvc.perform(get("/api/countries?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
				.andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())))
				.andExpect(jsonPath("$.[*].shortCode").value(hasItem(DEFAULT_SHORT_CODE.toString())))
				.andExpect(
						jsonPath("$.[*].shortCodeThreeChar").value(hasItem(DEFAULT_SHORT_CODE_THREE_CHAR.toString())))
				.andExpect(jsonPath("$.[*].countryNiceName").value(hasItem(DEFAULT_COUNTRY_NICE_NAME.toString())))
				.andExpect(jsonPath("$.[*].numCode").value(hasItem(DEFAULT_NUM_CODE)))
				.andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE)))
				.andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
	}

	@Test
	@Transactional
	public void getCountry() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);

		// Get the country
		restCountryMockMvc.perform(get("/api/countries/{id}", country.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(country.getId().intValue()))
				.andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME.toString()))
				.andExpect(jsonPath("$.shortCode").value(DEFAULT_SHORT_CODE.toString()))
				.andExpect(jsonPath("$.shortCodeThreeChar").value(DEFAULT_SHORT_CODE_THREE_CHAR.toString()))
				.andExpect(jsonPath("$.countryNiceName").value(DEFAULT_COUNTRY_NICE_NAME.toString()))
				.andExpect(jsonPath("$.numCode").value(DEFAULT_NUM_CODE))
				.andExpect(jsonPath("$.phoneCode").value(DEFAULT_PHONE_CODE))
				.andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
	}

	@Test
	@Transactional
	public void getNonExistingCountry() throws Exception {
		// Get the country
		restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCountry() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);
		countrySearchRepository.save(country);
		int databaseSizeBeforeUpdate = countryRepository.findAll().size();

		// Update the country
		Country updatedCountry = countryRepository.findOne(country.getId());
		updatedCountry.countryName(UPDATED_COUNTRY_NAME).shortCode(UPDATED_SHORT_CODE)
				.shortCodeThreeChar(UPDATED_SHORT_CODE_THREE_CHAR).countryNiceName(UPDATED_COUNTRY_NICE_NAME)
				.numCode(UPDATED_NUM_CODE).phoneCode(UPDATED_PHONE_CODE).enabled(UPDATED_ENABLED);

		restCountryMockMvc.perform(put("/api/countries").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCountry))).andExpect(status().isOk());

		// Validate the Country in the database
		List<Country> countryList = countryRepository.findAll();
		assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
		Country testCountry = countryList.get(countryList.size() - 1);
		assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
		assertThat(testCountry.getShortCode()).isEqualTo(UPDATED_SHORT_CODE);
		assertThat(testCountry.getShortCodeThreeChar()).isEqualTo(UPDATED_SHORT_CODE_THREE_CHAR);
		assertThat(testCountry.getCountryNiceName()).isEqualTo(UPDATED_COUNTRY_NICE_NAME);
		assertThat(testCountry.getNumCode()).isEqualTo(UPDATED_NUM_CODE);
		assertThat(testCountry.getPhoneCode()).isEqualTo(UPDATED_PHONE_CODE);
		assertThat(testCountry.isEnabled()).isEqualTo(UPDATED_ENABLED);

		// Validate the Country in Elasticsearch
		Country countryEs = countrySearchRepository.findOne(testCountry.getId());
		assertThat(countryEs).isEqualToComparingFieldByField(testCountry);
	}

	@Test
	@Transactional
	public void updateNonExistingCountry() throws Exception {
		int databaseSizeBeforeUpdate = countryRepository.findAll().size();

		// Create the Country

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCountryMockMvc.perform(put("/api/countries").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(country))).andExpect(status().isCreated());

		// Validate the Country in the database
		List<Country> countryList = countryRepository.findAll();
		assertThat(countryList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteCountry() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);
		countrySearchRepository.save(country);
		int databaseSizeBeforeDelete = countryRepository.findAll().size();

		// Get the country
		restCountryMockMvc
				.perform(delete("/api/countries/{id}", country.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean countryExistsInEs = countrySearchRepository.exists(country.getId());
		assertThat(countryExistsInEs).isFalse();

		// Validate the database is empty
		List<Country> countryList = countryRepository.findAll();
		assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCountry() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);
		countrySearchRepository.save(country);

		// Search the country
		restCountryMockMvc.perform(get("/api/_search/countries?query=id:" + country.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
				.andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())))
				.andExpect(jsonPath("$.[*].shortCode").value(hasItem(DEFAULT_SHORT_CODE.toString())))
				.andExpect(
						jsonPath("$.[*].shortCodeThreeChar").value(hasItem(DEFAULT_SHORT_CODE_THREE_CHAR.toString())))
				.andExpect(jsonPath("$.[*].countryNiceName").value(hasItem(DEFAULT_COUNTRY_NICE_NAME.toString())))
				.andExpect(jsonPath("$.[*].numCode").value(hasItem(DEFAULT_NUM_CODE)))
				.andExpect(jsonPath("$.[*].phoneCode").value(hasItem(DEFAULT_PHONE_CODE)))
				.andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Country.class);
		Country country1 = new Country();
		country1.setId(1L);
		Country country2 = new Country();
		country2.setId(country1.getId());
		assertThat(country1).isEqualTo(country2);
		country2.setId(2L);
		assertThat(country1).isNotEqualTo(country2);
		country1.setId(null);
		assertThat(country1).isNotEqualTo(country2);
	}
}
