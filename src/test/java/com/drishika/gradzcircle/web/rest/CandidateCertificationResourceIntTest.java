package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CandidateCertificationResource REST controller.
 *
 * @see CandidateCertificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateCertificationResourceIntTest {

	private static final String DEFAULT_CERTIFICATION_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_CERTIFICATION_TITLE = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_CERTIFICATION_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_CERTIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_CERTIFICATION_DETAILS = "AAAAAAAAAA";
	private static final String UPDATED_CERTIFICATION_DETAILS = "BBBBBBBBBB";

	@Autowired
	private CandidateCertificationRepository candidateCertificationRepository;

	@Autowired
	private CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Autowired
	private ProfileScoreCalculator profileScoreCalculator;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	
	@Autowired
	private DTOConverters converter;


	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;
	
	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;
	
	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;

	private MockMvc restCandidateCertificationMockMvc;

	private CandidateCertification candidateCertification, candidateCertification2;
	
	private Candidate candidate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateCertificationResource candidateCertificationResource = new CandidateCertificationResource(
				candidateCertificationRepository, candidateCertificationSearchRepository, profileScoreCalculator, candidateRepository,converter);
		this.restCandidateCertificationMockMvc = MockMvcBuilders.standaloneSetup(candidateCertificationResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateCertification createEntity(EntityManager em) {
		CandidateCertification candidateCertification = new CandidateCertification()
				.certificationTitle(DEFAULT_CERTIFICATION_TITLE).certificationDate(DEFAULT_CERTIFICATION_DATE)
				.certificationDetails(DEFAULT_CERTIFICATION_DETAILS);
		return candidateCertification;
	}
	
	public static CandidateCertification createEntity2(EntityManager em) {
		CandidateCertification candidateCertification2 = new CandidateCertification()
				.certificationTitle(DEFAULT_CERTIFICATION_TITLE).certificationDate(DEFAULT_CERTIFICATION_DATE)
				.certificationDetails(DEFAULT_CERTIFICATION_DETAILS);
		return candidateCertification2;
	}
	
	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Candidate createCandidateEntity(EntityManager em) {
		Candidate candidate = new Candidate().firstName("Abhinav");
				
		return candidate;
	}

	public static ProfileCategory createBasicProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_BASIC_PROFILE).weightage(5);
	}
	
	public static ProfileCategory createCertProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_CERTIFICATION_PROFILE).weightage(5);
	}
	
	public static ProfileCategory createEduProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_EDUCATION_PROFILE).weightage(50);
	}
	
	public static ProfileCategory createExpProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_EXPERIENCE_PROFILE).weightage(15);
	}
	
	public static ProfileCategory createLangProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_LANGUAGE_PROFILE).weightage(5);
	}
	
	public static ProfileCategory createNonAcadProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_NON_ACADEMIC_PROFILE).weightage(5);
	}
	
	public static ProfileCategory createPersonalProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE).weightage(15);
	}
	
	
	@Before
	public void initTest() {
		candidateCertificationSearchRepository.deleteAll();
		candidateCertification = createEntity(em);
		candidateCertification2 = createEntity2(em);
		candidate = createCandidateEntity(em);
		basic = createBasicProfile(em);
		personal = createPersonalProfile(em);
		cert=createCertProfile(em);
		exp = createExpProfile(em);
		nonAcad = createNonAcadProfile(em);
		edu = createEduProfile(em);
		lang = createLangProfile(em);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
	}

	@Test
	@Transactional
	public void createFirstCandidateCertificationShouldCreateCertProfileScore() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		candidateCertification.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);
		assertThat(testCandidateCertification.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		// Validate the CandidateCertification in Elasticsearch
	//	CandidateCertification candidateCertificationEs = candidateCertificationSearchRepository
		//		.findOne(testCandidateCertification.getId());
		//assertThat(candidateCertificationEs).isEqualToComparingFieldByField(testCandidateCertification);
	}
	
	
	
	@Test
	@Transactional
	public void createFirstCandidateCertificationShouldCreateCertProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateCertification.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);
		assertThat(testCandidateCertification.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

	}
	
	@Test
	@Transactional
	public void createCertificationAndAnotherCertificationUpdateOneRemoveOneAndRemoveAllAndAddBackScoreShouldMoveFrom20To25To20To25() throws Exception {
		
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,personal);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(15d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(20D);
		candidateRepository.saveAndFlush(candidate);
		candidateCertification.setCandidate(candidate);
		candidateCertification2.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);
		assertThat(testCandidateCertification.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(25D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		
		restCandidateCertificationMockMvc
		.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateCertification2)))
		.andExpect(status().isCreated());

		candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(2);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(2);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(25D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		
		CandidateCertification updatedCandidateCertification = candidateCertificationRepository
				.findById(testCandidateCertification.getId()).get();
		updatedCandidateCertification.certificationTitle(UPDATED_CERTIFICATION_TITLE)
				.certificationDate(UPDATED_CERTIFICATION_DATE).certificationDetails(UPDATED_CERTIFICATION_DETAILS).candidate(candidate);
		
		restCandidateCertificationMockMvc
		.perform(put("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidateCertification)))
		.andExpect(status().isOk());
		
		candidateCertificationList = candidateCertificationRepository.findAll();
		testCandidateCertification = candidateCertificationList.get(0);
		assertThat(candidateCertificationList).hasSize(2);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(2);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(25D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		
		restCandidateCertificationMockMvc
		.perform(delete("/api/candidate-certifications/{id}", testCandidateCertification.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
		candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(1);
		testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(25D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);

		
		restCandidateCertificationMockMvc
		.perform(delete("/api/candidate-certifications/{id}", testCandidateCertification.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
		candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(0);
		candidate = candidateRepository.findAll().get(0);
		assertThat(candidate.getCertifications().size()).isEqualTo(0);
		assertThat(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(candidate.getProfileScore()).isEqualTo(20D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		
		restCandidateCertificationMockMvc
		.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
		.andExpect(status().isCreated());

		candidateCertificationList = candidateCertificationRepository.findAll();
		testCandidateCertification = candidateCertificationList.get(0);
		assertThat(candidateCertificationList).hasSize(1);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(25D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		
		
	}
	
	@Test
	@Transactional
	public void createSecondCandidateCertificationShouldCreateCertProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();
		candidate.addCertification(new CandidateCertification().certificationTitle("AAA"));
		candidateRepository.saveAndFlush(candidate);
		
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		candidateCertification.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 2);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);
		assertThat(testCandidateCertification.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(2);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}

	@Test
	@Transactional
	public void createFirstCandidateCertificationWithExisitngCerticationZeroScoreShouldUpdateCertProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateCertification.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(DEFAULT_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(DEFAULT_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(DEFAULT_CERTIFICATION_DETAILS);
		assertThat(testCandidateCertification.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateCertification.getCandidate().getCertifications().size()).isEqualTo(1);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateCertification.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateCertification.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}

	
	
	@Test
	@Transactional
	public void createCandidateCertificationWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateCertificationRepository.findAll().size();

		// Create the CandidateCertification with an existing ID
		candidateCertification.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateCertificationMockMvc
				.perform(post("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateCertifications() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);

		// Get all the candidateCertificationList
		restCandidateCertificationMockMvc.perform(get("/api/candidate-certifications?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateCertification.getId().intValue())))
				.andExpect(jsonPath("$.[*].certificationTitle").value(hasItem(DEFAULT_CERTIFICATION_TITLE.toString())))
				.andExpect(jsonPath("$.[*].certificationDate").value(hasItem(DEFAULT_CERTIFICATION_DATE.toString())))
				.andExpect(jsonPath("$.[*].certificationDetails")
						.value(hasItem(DEFAULT_CERTIFICATION_DETAILS.toString())));
	}

	@Test
	@Transactional
	public void getCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/candidate-certifications/{id}", candidateCertification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateCertification.getId().intValue()))
				.andExpect(jsonPath("$.certificationTitle").value(DEFAULT_CERTIFICATION_TITLE.toString()))
				.andExpect(jsonPath("$.certificationDate").value(DEFAULT_CERTIFICATION_DATE.toString()))
				.andExpect(jsonPath("$.certificationDetails").value(DEFAULT_CERTIFICATION_DETAILS.toString()));
	}
	
	@Test
	@Transactional
	public void getCandidateCertificationByCandidateWithProfileScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate.profileScore(25D));
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		candidate.addCertification(candidateCertification);
		candidateRepository.saveAndFlush(candidate);

		

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/candidate-cert-by-id/{id}", candidate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateCertification.getId().intValue()))
				.andExpect(jsonPath("$[0].certificationTitle").value(DEFAULT_CERTIFICATION_TITLE.toString()))
				.andExpect(jsonPath("$[0].certificationDate").value(DEFAULT_CERTIFICATION_DATE.toString()))
				.andExpect(jsonPath("$[0].certificationDetails").value(DEFAULT_CERTIFICATION_DETAILS.toString()))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}
	
	@Test
	@Transactional
	public void getCandidateCertificationByCandidateWithoutProfileScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		candidateCertificationRepository.saveAndFlush(candidateCertification.candidate(candidate));

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/candidate-cert-by-id/{id}", candidate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateCertification.getId().intValue()))
				.andExpect(jsonPath("$[0].certificationTitle").value(DEFAULT_CERTIFICATION_TITLE.toString()))
				.andExpect(jsonPath("$[0].certificationDate").value(DEFAULT_CERTIFICATION_DATE.toString()))
				.andExpect(jsonPath("$[0].certificationDetails").value(DEFAULT_CERTIFICATION_DETAILS.toString()))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(0d));
	}
	
	@Test
	@Transactional
	@Ignore
	public void getCandidateCertificationByCandidateWithProfileScoreEmptyCertificationList() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		//candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateCertificationMockMvc.perform(get("/api/candidate-cert-by-id/{id}",candidate.getId())).andDo(MockMvcResultHandlers.print())
				
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateCertification() throws Exception {
		// Get the candidateCertification
		restCandidateCertificationMockMvc.perform(get("/api/candidate-certifications/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		//candidateCertificationSearchRepository.save(candidateCertification);
		int databaseSizeBeforeUpdate = candidateCertificationRepository.findAll().size();

		// Update the candidateCertification
		CandidateCertification updatedCandidateCertification = candidateCertificationRepository
				.findById(candidateCertification.getId()).get();
		updatedCandidateCertification.certificationTitle(UPDATED_CERTIFICATION_TITLE)
				.certificationDate(UPDATED_CERTIFICATION_DATE).certificationDetails(UPDATED_CERTIFICATION_DETAILS);

		restCandidateCertificationMockMvc
				.perform(put("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateCertification)))
				.andExpect(status().isOk());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeUpdate);
		CandidateCertification testCandidateCertification = candidateCertificationList
				.get(candidateCertificationList.size() - 1);
		assertThat(testCandidateCertification.getCertificationTitle()).isEqualTo(UPDATED_CERTIFICATION_TITLE);
		assertThat(testCandidateCertification.getCertificationDate()).isEqualTo(UPDATED_CERTIFICATION_DATE);
		assertThat(testCandidateCertification.getCertificationDetails()).isEqualTo(UPDATED_CERTIFICATION_DETAILS);

		// Validate the CandidateCertification in Elasticsearch
	//	CandidateCertification candidateCertificationEs = candidateCertificationSearchRepository
	//			.findOne(testCandidateCertification.getId());
	//	assertThat(candidateCertificationEs).isEqualToComparingFieldByField(testCandidateCertification);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateCertification() throws Exception {
		int databaseSizeBeforeUpdate = candidateCertificationRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		// Create the CandidateCertification
		candidateCertification.setCandidate(candidate);

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateCertificationMockMvc
				.perform(put("/api/candidate-certifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateCertification)))
				.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTheOnlyCandidateCertificationShouldRemoveCertificationProfileScore() throws Exception {
		// Initialize the database	
		candidateRepository.saveAndFlush(candidate);
		candidate.addCertification(candidateCertification);
		//candidateCertificationRepository.saveAndFlush(candidateCertification);
		//candidateCertificationSearchRepository.save(candidateCertification);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateCertificationRepository.findAll().size();

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(delete("/api/candidate-certifications/{id}", candidate.getCertifications().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateCertificationExistsInEs = candidateCertificationSearchRepository
		//		.exists(candidateCertification.getId());
		//assertThat(candidateCertificationExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateCertificationList).hasSize(0);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(55D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}
	
	@Test
	@Transactional
	public void deleteOneOutOfTwoCandidateCertificationShouldNotRemoveProfileScoreForCerts() throws Exception {
		// Initialize the database	
		candidateRepository.saveAndFlush(candidate);
		candidate.addCertification(candidateCertification);
		candidate.addCertification(new CandidateCertification().certificationTitle("Abhinav"));
		//candidateCertificationRepository.saveAndFlush(candidateCertification);
		//candidateCertificationSearchRepository.save(candidateCertification);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateCertificationRepository.findAll().size();

		// Get the candidateCertification
		restCandidateCertificationMockMvc
				.perform(delete("/api/candidate-certifications/{id}", candidate.getCertifications().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateCertificationExistsInEs = candidateCertificationSearchRepository
		//		.exists(candidateCertification.getId());
		//assertThat(candidateCertificationExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateCertification> candidateCertificationList = candidateCertificationRepository.findAll();
		assertThat(candidateCertificationList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateCertificationList).hasSize(1);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(60D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}

	@Test
	@Transactional
	@Ignore // not selecting in elastic searhc
	public void searchCandidateCertification() throws Exception {
		// Initialize the database
		candidateCertificationRepository.saveAndFlush(candidateCertification);
		candidateCertificationSearchRepository.save(candidateCertification);

		// Search the candidateCertification
		restCandidateCertificationMockMvc
				.perform(get("/api/_search/candidate-certifications?query=id:" + candidateCertification.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateCertification.getId().intValue())))
				.andExpect(jsonPath("$.[*].certificationTitle").value(hasItem(DEFAULT_CERTIFICATION_TITLE.toString())))
				.andExpect(jsonPath("$.[*].certificationDate").value(hasItem(DEFAULT_CERTIFICATION_DATE.toString())))
				.andExpect(jsonPath("$.[*].certificationDetails")
						.value(hasItem(DEFAULT_CERTIFICATION_DETAILS.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateCertification.class);
		CandidateCertification candidateCertification1 = new CandidateCertification();
		candidateCertification1.setId(1L);
		CandidateCertification candidateCertification2 = new CandidateCertification();
		candidateCertification2.setId(candidateCertification1.getId());
		assertThat(candidateCertification1).isEqualTo(candidateCertification2);
		candidateCertification2.setId(2L);
		assertThat(candidateCertification1).isNotEqualTo(candidateCertification2);
		candidateCertification1.setId(null);
		assertThat(candidateCertification1).isNotEqualTo(candidateCertification2);
	}
}

