/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.entitybuilders.CountryEntityBuilder;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CountrySearchRepository;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * @author abhinav
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CountryServiceIntTest {
	
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
	private CountrySearchRepository mockCountrySearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restCountryMockMvc;

	private Country country,country2;
	
	com.drishika.gradzcircle.domain.elastic.Country elasticCountry;
	
	@Autowired
	private GradzcircleCacheManager <String, List<Country>> manager;
	
	CountryService countryService;
	
    @Mock
    private ElasticsearchTemplate elasticsearchTemplate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		countryService = new CountryService(manager, countryRepository, mockCountrySearchRepository, elasticsearchTemplate);
		
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
	
	public static Country createEntity2(EntityManager em) {
		Country country2 = new Country().countryName(DEFAULT_COUNTRY_NAME).shortCode(DEFAULT_SHORT_CODE)
				.shortCodeThreeChar(DEFAULT_SHORT_CODE_THREE_CHAR).countryNiceName(DEFAULT_COUNTRY_NICE_NAME)
				.numCode(DEFAULT_NUM_CODE).phoneCode(DEFAULT_PHONE_CODE).enabled(UPDATED_ENABLED);
		return country2;
	}
	
	 /**
		 * Create an entity for this test.
		 *
		 * This is a static method, as tests for other entities might also need it, if
		 * they test an entity which requires the current entity.
		 */
		public static CountryEntityBuilder createEntityBuilder(Country country) {
			CountryEntityBuilder entityBuilder = new CountryEntityBuilder(country.getId());
			entityBuilder.name(country.getCountryNiceName());
			return entityBuilder;
		}

		/**
		 * Create an entity for this test.
		 *
		 * This is a static method, as tests for other entities might also need it, if
		 * they test an entity which requires the current entity.
		 */
		public static com.drishika.gradzcircle.domain.elastic.Country createElasticInstance(
				Country country) {
			com.drishika.gradzcircle.domain.elastic.Country elasticCountry = new com.drishika.gradzcircle.domain.elastic.Country();
			elasticCountry.countryNiceName(country.getCountryNiceName());
			return elasticCountry;
		}

	@Before
	public void initTest() {
		//countrySearchRepository.deleteAll();
		country = createEntity(em);
		country2 = createEntity2(em);
		elasticCountry = createElasticInstance(country);
		
	}

	@Test
	@Transactional
	public void createCountry() throws Exception {
		int databaseSizeBeforeCreate = countryRepository.findAll().size();

		// Create the Country
		countryService.createCountry(country);

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
		verify(elasticsearchTemplate,times(1)).index(any());
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
	}
	
	@Test
	@Transactional
	public void updateCountry() throws Exception {
		// Initialize the database
		countryRepository.saveAndFlush(country);
		elasticsearchTemplate.index(createEntityBuilder(country)
				.suggest(new String[] { createElasticInstance(country).getCountryNiceName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
		int databaseSizeBeforeUpdate = countryRepository.findAll().size();

		// Update the country
		Country updatedCountry = countryRepository.findById(country.getId()).get();
		updatedCountry.countryName(UPDATED_COUNTRY_NAME).shortCode(UPDATED_SHORT_CODE)
				.shortCodeThreeChar(UPDATED_SHORT_CODE_THREE_CHAR).countryNiceName(UPDATED_COUNTRY_NICE_NAME)
				.numCode(UPDATED_NUM_CODE).phoneCode(UPDATED_PHONE_CODE).enabled(UPDATED_ENABLED);

		countryService.updateCountry(updatedCountry);

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
		
		//Get country to check if cache is refreshed
		List<Country> testCountriesFromRepo = countryService.getEnabledCountries();
		assertThat(testCountriesFromRepo.get(0).isEnabled()).isEqualTo(Boolean.TRUE);
		

		// Validate the Country in Elasticsearch
		verify(elasticsearchTemplate,times(2)).index(any());
		verify(elasticsearchTemplate,times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
	}
	
	@Test
	@Transactional
	@Ignore // NEED TO FIX THIS TEST AS UNABLE TO RETURN VALID SEARCH RESPONSE OBJECT FROM MOCK
	public void searchCountryBySuggest() throws Exception {
		int databaseSizeBeforeCreate = countryRepository.findAll().size();
		countryService.createCountry(country);
		
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
		verify(elasticsearchTemplate,times(1)).index(any());
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("suggest").text(testCountry.getCountryNiceName()).prefix(testCountry.getCountryNiceName());
		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("suggest", completionSuggestionBuilder);
		countryService.searchCountryBySuggest(country.getCountryNiceName());
		verify(elasticsearchTemplate,times(1)).suggest(any());
		when(elasticsearchTemplate.suggest(suggestion, com.drishika.gradzcircle.domain.elastic.Country.class))
        .thenReturn(new SearchResponse());
		
	}

}
