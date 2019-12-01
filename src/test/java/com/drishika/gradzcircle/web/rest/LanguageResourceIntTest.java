package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.entitybuilders.LanguageEntityBuilder;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.search.LanguageSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the LanguageResource REST controller.
 *
 * @see LanguageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class LanguageResourceIntTest {

	private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
	private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private LanguageSearchRepository mockLanguageSearchRepository;

	@Mock
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restLanguageMockMvc;

	private Language language;

	private com.drishika.gradzcircle.domain.elastic.Language elasticLanguage;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final LanguageResource languageResource = new LanguageResource(languageRepository, mockLanguageSearchRepository,
				elasticsearchTemplate);
		this.restLanguageMockMvc = MockMvcBuilders.standaloneSetup(languageResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Language createEntity(EntityManager em) {
		Language language = new Language().language(DEFAULT_LANGUAGE);
		return language;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static LanguageEntityBuilder createEntityBuilder(Language language) {
		LanguageEntityBuilder entityBuilder = new LanguageEntityBuilder(language.getId());
		entityBuilder.name(language.getLanguage());
		return entityBuilder;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static com.drishika.gradzcircle.domain.elastic.Language createElasticInstance(Language language) {
		com.drishika.gradzcircle.domain.elastic.Language elasticLanguage = new com.drishika.gradzcircle.domain.elastic.Language();
		elasticLanguage.setId(language.getId());
		elasticLanguage.language(language.getLanguage());
		return elasticLanguage;
	}

	@Before
	public void initTest() {
		//languageSearchRepository.deleteAll();
		language = createEntity(em);
		elasticLanguage = createElasticInstance(language);
	}

	@Test
	@Transactional
	public void createLanguage() throws Exception {
		int databaseSizeBeforeCreate = languageRepository.findAll().size();

		// Create the Language
		restLanguageMockMvc.perform(post("/api/languages").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(language))).andExpect(status().isCreated());

		// Validate the Language in the database
		List<Language> languageList = languageRepository.findAll();
		assertThat(languageList).hasSize(databaseSizeBeforeCreate + 1);
		Language testLanguage = languageList.get(languageList.size() - 1);
		assertThat(testLanguage.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);

		// Validate the Language in Elasticsearch
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		verify(elasticsearchTemplate,times(1)).index(any());
		
	}

	@Test
	@Transactional
	public void createLanguageWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = languageRepository.findAll().size();

		// Create the Language with an existing ID
		language.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restLanguageMockMvc.perform(post("/api/languages").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(language))).andExpect(status().isBadRequest());

		// Validate the Language in the database
		List<Language> languageList = languageRepository.findAll();
		assertThat(languageList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllLanguages() throws Exception {
		// Initialize the database
		languageRepository.saveAndFlush(language);

		// Get all the languageList
		restLanguageMockMvc.perform(get("/api/languages?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
				.andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
	}

	@Test
	@Transactional
	public void getLanguage() throws Exception {
		// Initialize the database
		languageRepository.saveAndFlush(language);

		// Get the language
		restLanguageMockMvc.perform(get("/api/languages/{id}", language.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(language.getId().intValue()))
				.andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingLanguage() throws Exception {
		// Get the language
		restLanguageMockMvc.perform(get("/api/languages/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateLanguage() throws Exception {
		// Initialize the database
		languageRepository.saveAndFlush(language);
		elasticsearchTemplate.index(createEntityBuilder(language)
				.suggest(new String[] { createElasticInstance(language).getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		int databaseSizeBeforeUpdate = languageRepository.findAll().size();

		// Update the language
		Language updatedLanguage = languageRepository.findById(language.getId()).get();
		updatedLanguage.language(UPDATED_LANGUAGE);

		restLanguageMockMvc.perform(put("/api/languages").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedLanguage))).andExpect(status().isOk());

		// Validate the Language in the database
		List<Language> languageList = languageRepository.findAll();
		assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
		Language testLanguage = languageList.get(languageList.size() - 1);
		assertThat(testLanguage.getLanguage()).isEqualTo(UPDATED_LANGUAGE);

		// Validate the Language in Elasticsearch
		//NEED TO ADD
	}

	@Test
	@Transactional
	public void updateNonExistingLanguage() throws Exception {
		int databaseSizeBeforeUpdate = languageRepository.findAll().size();

		// Create the Language

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated

		restLanguageMockMvc.perform(put("/api/languages")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(language)))
	            .andExpect(status().isBadRequest());
		// Validate the Language in the database
		List<Language> languageList = languageRepository.findAll();
		assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
		
		verify(elasticsearchTemplate,times(0)).refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		verify(elasticsearchTemplate,times(0)).index(any());
		
	}

	@Test
	@Transactional
	public void deleteLanguage() throws Exception {
		// Initialize the database
		languageRepository.saveAndFlush(language);
		elasticsearchTemplate.index(createEntityBuilder(language)
				.suggest(new String[] { createElasticInstance(language).getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		int databaseSizeBeforeDelete = languageRepository.findAll().size();

		// Get the language
		restLanguageMockMvc
				.perform(delete("/api/languages/{id}", language.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		verify(elasticsearchTemplate,times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		verify(elasticsearchTemplate,times(1)).index(any());
		
		// Validate the database is empty
		List<Language> languageList = languageRepository.findAll();
		assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchLanguage() throws Exception {
		// Initialize the database
		languageRepository.saveAndFlush(language);
		elasticsearchTemplate.index(createEntityBuilder(language)
				.suggest(new String[] { createElasticInstance(language).getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);

		elasticLanguage.setId(language.getId());
		when(mockLanguageSearchRepository.search(queryStringQuery("id:" + language.getId())))
	        .thenReturn(Collections.singletonList(elasticLanguage));
		// Search the language
		restLanguageMockMvc.perform(get("/api/_search/languages?query=id:" + language.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
				.andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Language.class);
		Language language1 = new Language();
		language1.setId(1L);
		Language language2 = new Language();
		language2.setId(language1.getId());
		assertThat(language1).isEqualTo(language2);
		language2.setId(2L);
		assertThat(language1).isNotEqualTo(language2);
		language1.setId(null);
		assertThat(language1).isNotEqualTo(language2);
	}
}
