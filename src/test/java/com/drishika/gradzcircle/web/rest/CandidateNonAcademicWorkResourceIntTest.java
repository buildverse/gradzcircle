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
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.CandidateNonAcademicWorkRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CandidateNonAcademicWorkResource REST controller.
 *
 * @see CandidateNonAcademicWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateNonAcademicWorkResourceIntTest {

	private static final String DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_NON_ACADEMIC_INITIATIVE_TITLE = "BBBBBBBBBB";

	private static final String DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_NON_ACADEMIC_INITIATIVE_DESCRIPTION = "BBBBBBBBBB";

	private static final Integer DEFAULT_DURATION = 1;
	private static final Integer UPDATED_DURATION = 2;

	private static final Boolean DEFAULT_IS_CURRENT_ACTIVITY = false;
	private static final Boolean UPDATED_IS_CURRENT_ACTIVITY = true;

	private static final String DEFAULT_ROLE_IN_INITIATIVE = "AAAAAAAAAA";
	private static final String UPDATED_ROLE_IN_INITIATIVE = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_NON_ACADEMIC_WORK_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_NON_ACADEMIC_WORK_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_NON_ACADEMIC_WORK_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_NON_ACADEMIC_WORK_END_DATE = LocalDate.now(ZoneId.systemDefault());

	@Autowired
	private CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository;

	@Autowired
	private CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;
	
	@Autowired
	private ProfileScoreCalculator profileScoreCalculator;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private DTOConverters converter;

	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;
	
	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;
	
	private Candidate candidate;

	private MockMvc restCandidateNonAcademicWorkMockMvc;

	private CandidateNonAcademicWork candidateNonAcademicWork;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateNonAcademicWorkResource candidateNonAcademicWorkResource = new CandidateNonAcademicWorkResource(
				candidateNonAcademicWorkRepository, candidateNonAcademicWorkSearchRepository,profileScoreCalculator,candidateRepository,converter);
		this.restCandidateNonAcademicWorkMockMvc = MockMvcBuilders.standaloneSetup(candidateNonAcademicWorkResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateNonAcademicWork createEntity(EntityManager em) {
		CandidateNonAcademicWork candidateNonAcademicWork = new CandidateNonAcademicWork()
				.nonAcademicInitiativeTitle(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE)
				.nonAcademicInitiativeDescription(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION)
				.duration(DEFAULT_DURATION).isCurrentActivity(DEFAULT_IS_CURRENT_ACTIVITY)
				.roleInInitiative(DEFAULT_ROLE_IN_INITIATIVE)
				.nonAcademicWorkStartDate(DEFAULT_NON_ACADEMIC_WORK_START_DATE)
				.nonAcademicWorkEndDate(DEFAULT_NON_ACADEMIC_WORK_END_DATE);
		return candidateNonAcademicWork;
	}
	
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
		candidateNonAcademicWorkSearchRepository.deleteAll();
		candidateRepository.deleteAll();
		candidateNonAcademicWork = createEntity(em);
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
	public void createFirstCandidateNonAcademicShouldCreateCertProfileScore() throws Exception {
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();

		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// Create the CandidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeTitle())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeDescription())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION);
		assertThat(testCandidateNonAcademicWork.getDuration()).isEqualTo(DEFAULT_DURATION);
		assertThat(testCandidateNonAcademicWork.isIsCurrentActivity()).isEqualTo(DEFAULT_IS_CURRENT_ACTIVITY);
		assertThat(testCandidateNonAcademicWork.getRoleInInitiative()).isEqualTo(DEFAULT_ROLE_IN_INITIATIVE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkStartDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_START_DATE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkEndDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_END_DATE);
		assertThat(testCandidateNonAcademicWork.getCandidate().getNonAcademics().size()).isEqualTo(1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScore()).isEqualTo(5d);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().size()).isEqualTo(1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);

		// Validate the CandidateNonAcademicWork in Elasticsearch
		//CandidateNonAcademicWork candidateNonAcademicWorkEs = candidateNonAcademicWorkSearchRepository
		//		.findOne(testCandidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkEs).isEqualToComparingFieldByField(testCandidateNonAcademicWork);
	}
	
	@Test
	@Transactional
	public void createFirstCandidateCertificationShouldCreateNonAcadProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();

		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// Create the CandidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeTitle())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeDescription())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION);
		assertThat(testCandidateNonAcademicWork.getDuration()).isEqualTo(DEFAULT_DURATION);
		assertThat(testCandidateNonAcademicWork.isIsCurrentActivity()).isEqualTo(DEFAULT_IS_CURRENT_ACTIVITY);
		assertThat(testCandidateNonAcademicWork.getRoleInInitiative()).isEqualTo(DEFAULT_ROLE_IN_INITIATIVE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkStartDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_START_DATE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkEndDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_END_DATE);
		assertThat(testCandidateNonAcademicWork.getCandidate().getNonAcademics().size()).isEqualTo(1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

		// Validate the CandidateNonAcademicWork in Elasticsearch
		//CandidateNonAcademicWork candidateNonAcademicWorkEs = candidateNonAcademicWorkSearchRepository
		//		.findOne(testCandidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkEs).isEqualToComparingFieldByField(testCandidateNonAcademicWork);
	}
	
	@Test
	@Transactional
	public void testAddingNonAcadThenUpdatingThenAdingANotherThenDeletingAllThenAddingBackScoreFrom55to60to55() throws Exception {
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();

		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// Create the CandidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getNonAcademics().size()).isEqualTo(1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		CandidateNonAcademicWork updatedCandidateNonAcademicWork = candidateNonAcademicWorkRepository
				.findOne(testCandidateNonAcademicWork.getId());
		updatedCandidateNonAcademicWork.nonAcademicInitiativeTitle(UPDATED_NON_ACADEMIC_INITIATIVE_TITLE)
				.nonAcademicInitiativeDescription(UPDATED_NON_ACADEMIC_INITIATIVE_DESCRIPTION)
				.duration(UPDATED_DURATION).isCurrentActivity(UPDATED_IS_CURRENT_ACTIVITY)
				.roleInInitiative(UPDATED_ROLE_IN_INITIATIVE)
				.nonAcademicWorkStartDate(UPDATED_NON_ACADEMIC_WORK_START_DATE)
				.nonAcademicWorkEndDate(UPDATED_NON_ACADEMIC_WORK_END_DATE);

		restCandidateNonAcademicWorkMockMvc
				.perform(put("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateNonAcademicWork)))
				.andExpect(status().isOk());
		
		candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		candidate = candidateRepository.findAll().get(0);
		assertThat(candidate.getNonAcademics().size()).isEqualTo(1);
		assertThat(candidate.getProfileScores().size()).isEqualTo(3);
		assertThat(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(candidate.getProfileScore()).isEqualTo(60D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		restCandidateNonAcademicWorkMockMvc
		.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
		.andExpect(status().isCreated());
		
		
		candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		candidate = candidateRepository.findAll().get(0);
		assertThat(candidate.getNonAcademics().size()).isEqualTo(2);
		assertThat(candidate.getProfileScores().size()).isEqualTo(3);
		assertThat(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(candidate.getProfileScore()).isEqualTo(60D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		

		restCandidateNonAcademicWorkMockMvc
		.perform(delete("/api/candidate-non-academic-works/{id}", candidateNonAcademicWorkList.get(0).getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
		restCandidateNonAcademicWorkMockMvc
		.perform(delete("/api/candidate-non-academic-works/{id}", candidateNonAcademicWorkList.get(1).getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());

		
		candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		candidate = candidateRepository.findAll().get(0);
		assertThat(candidate.getNonAcademics().size()).isEqualTo(0);
		assertThat(candidate.getProfileScores().size()).isEqualTo(3);
		assertThat(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(candidate.getProfileScore()).isEqualTo(55D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		
		restCandidateNonAcademicWorkMockMvc
		.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
		.andExpect(status().isCreated());
		
		candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		candidate = candidateRepository.findAll().get(0);
		assertThat(candidate.getNonAcademics().size()).isEqualTo(1);
		assertThat(candidate.getProfileScores().size()).isEqualTo(3);
		assertThat(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(candidate.getProfileScore()).isEqualTo(60D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(candidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		
	}
	
	
	@Test
	@Transactional
	public void createSecondCandidateNonAcadShouldCreateNonAcadProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		

		candidate.addNonAcademic(new CandidateNonAcademicWork().nonAcademicInitiativeTitle("AAA"));
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,nonAcad);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// Create the CandidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateNonAcademicWorkList).hasSize(2);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeTitle())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeDescription())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION);
		assertThat(testCandidateNonAcademicWork.getDuration()).isEqualTo(DEFAULT_DURATION);
		assertThat(testCandidateNonAcademicWork.isIsCurrentActivity()).isEqualTo(DEFAULT_IS_CURRENT_ACTIVITY);
		assertThat(testCandidateNonAcademicWork.getRoleInInitiative()).isEqualTo(DEFAULT_ROLE_IN_INITIATIVE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkStartDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_START_DATE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkEndDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_END_DATE);
		assertThat(testCandidateNonAcademicWork.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateNonAcademicWork.getCandidate().getNonAcademics().size()).isEqualTo(2);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		// Validate the CandidateNonAcademicWork in Elasticsearch
		//CandidateNonAcademicWork candidateNonAcademicWorkEs = candidateNonAcademicWorkSearchRepository
		//		.findOne(testCandidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkEs).isEqualToComparingFieldByField(testCandidateNonAcademicWork);
	}
	
	@Test
	@Transactional
	public void createFirstCandidateNonAcadWithExistingProfileScoreAsZeroShouldUpdateNonAcadProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,nonAcad);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// Create the CandidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateNonAcademicWorkList).hasSize(1);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeTitle())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeDescription())
				.isEqualTo(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION);
		assertThat(testCandidateNonAcademicWork.getDuration()).isEqualTo(DEFAULT_DURATION);
		assertThat(testCandidateNonAcademicWork.isIsCurrentActivity()).isEqualTo(DEFAULT_IS_CURRENT_ACTIVITY);
		assertThat(testCandidateNonAcademicWork.getRoleInInitiative()).isEqualTo(DEFAULT_ROLE_IN_INITIATIVE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkStartDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_START_DATE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkEndDate())
				.isEqualTo(DEFAULT_NON_ACADEMIC_WORK_END_DATE);
		assertThat(testCandidateNonAcademicWork.getCandidate()).isEqualTo(candidate);
		assertThat(testCandidateNonAcademicWork.getCandidate().getNonAcademics().size()).isEqualTo(1);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScore()).isEqualTo(60D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateNonAcademicWork.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		// Validate the CandidateNonAcademicWork in Elasticsearch
		//CandidateNonAcademicWork candidateNonAcademicWorkEs = candidateNonAcademicWorkSearchRepository
		//		.findOne(testCandidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkEs).isEqualToComparingFieldByField(testCandidateNonAcademicWork);
	}
	

	@Test
	@Transactional
	public void createCandidateNonAcademicWorkWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateNonAcademicWorkRepository.findAll().size();

		// Create the CandidateNonAcademicWork with an existing ID
		candidateNonAcademicWork.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateNonAcademicWorkMockMvc
				.perform(post("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateNonAcademicWorks() throws Exception {
		// Initialize the database
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork);

		// Get all the candidateNonAcademicWorkList
		restCandidateNonAcademicWorkMockMvc.perform(get("/api/candidate-non-academic-works?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateNonAcademicWork.getId().intValue())))
				.andExpect(jsonPath("$.[*].nonAcademicInitiativeTitle")
						.value(hasItem(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicInitiativeDescription")
						.value(hasItem(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
				.andExpect(
						jsonPath("$.[*].isCurrentActivity").value(hasItem(DEFAULT_IS_CURRENT_ACTIVITY.booleanValue())))
				.andExpect(jsonPath("$.[*].roleInInitiative").value(hasItem(DEFAULT_ROLE_IN_INITIATIVE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicWorkStartDate")
						.value(hasItem(DEFAULT_NON_ACADEMIC_WORK_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicWorkEndDate")
						.value(hasItem(DEFAULT_NON_ACADEMIC_WORK_END_DATE.toString())));
	}

	@Test
	@Transactional
	public void getCandidateNonAcademicWork() throws Exception {
		// Initialize the database
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork);

		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(get("/api/candidate-non-academic-works/{id}", candidateNonAcademicWork.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateNonAcademicWork.getId().intValue()))
				.andExpect(jsonPath("$.nonAcademicInitiativeTitle")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE.toString()))
				.andExpect(jsonPath("$.nonAcademicInitiativeDescription")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION.toString()))
				.andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
				.andExpect(jsonPath("$.isCurrentActivity").value(DEFAULT_IS_CURRENT_ACTIVITY.booleanValue()))
				.andExpect(jsonPath("$.roleInInitiative").value(DEFAULT_ROLE_IN_INITIATIVE.toString()))
				.andExpect(
						jsonPath("$.nonAcademicWorkStartDate").value(DEFAULT_NON_ACADEMIC_WORK_START_DATE.toString()))
				.andExpect(jsonPath("$.nonAcademicWorkEndDate").value(DEFAULT_NON_ACADEMIC_WORK_END_DATE.toString()));
	}
	
	@Test
	@Transactional
	public void getCandidateNonAcademicWorkByCandidateWithProfileScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate.profileScore(25D));
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork);
		candidate.addNonAcademic(candidateNonAcademicWork);
		candidateRepository.saveAndFlush(candidate);

		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(get("/api/candidate-non-academic-work-by-id/{id}", candidate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateNonAcademicWork.getId().intValue()))
				.andExpect(jsonPath("$[0].nonAcademicInitiativeTitle")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE.toString()))
				.andExpect(jsonPath("$[0].nonAcademicInitiativeDescription")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION.toString()))
				.andExpect(jsonPath("$[0].duration").value(DEFAULT_DURATION))
				.andExpect(jsonPath("$[0].isCurrentActivity").value(DEFAULT_IS_CURRENT_ACTIVITY.booleanValue()))
				.andExpect(jsonPath("$[0].roleInInitiative").value(DEFAULT_ROLE_IN_INITIATIVE.toString()))
				.andExpect(
						jsonPath("$[0].nonAcademicWorkStartDate").value(DEFAULT_NON_ACADEMIC_WORK_START_DATE.toString()))
				.andExpect(jsonPath("$[0].nonAcademicWorkEndDate").value(DEFAULT_NON_ACADEMIC_WORK_END_DATE.toString()))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}
	
	@Test
	@Transactional
	public void getCandidateNonAcademicWorkByCandidateWithoutProfileScore() throws Exception {
		// Initialize the database
	//	Candidate candidate = new Candidate();
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork.candidate(candidate));

		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(get("/api/candidate-non-academic-work-by-id/{id}", candidate.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateNonAcademicWork.getId().intValue()))
				.andExpect(jsonPath("$[0].nonAcademicInitiativeTitle")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE.toString()))
				.andExpect(jsonPath("$[0].nonAcademicInitiativeDescription")
						.value(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION.toString()))
				.andExpect(jsonPath("$[0].duration").value(DEFAULT_DURATION))
				.andExpect(jsonPath("$[0].isCurrentActivity").value(DEFAULT_IS_CURRENT_ACTIVITY.booleanValue()))
				.andExpect(jsonPath("$[0].roleInInitiative").value(DEFAULT_ROLE_IN_INITIATIVE.toString()))
				.andExpect(
						jsonPath("$[0].nonAcademicWorkStartDate").value(DEFAULT_NON_ACADEMIC_WORK_START_DATE.toString()))
				.andExpect(jsonPath("$[0].nonAcademicWorkEndDate").value(DEFAULT_NON_ACADEMIC_WORK_END_DATE.toString()))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(0d));
	}


	@Test
	@Transactional
	@Ignore
	public void getCandidateNonAcademicByCandidateWithProfileScoreEmptyNonAcademicList() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		//candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateNonAcademicWorkMockMvc.perform(get("/api/candidate-non-academic-work-by-id//{id}",candidate.getId())).andDo(MockMvcResultHandlers.print())
				
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}
	
	@Test
	@Transactional
	public void getNonExistingCandidateNonAcademicWork() throws Exception {
		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc.perform(get("/api/candidate-non-academic-works/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateNonAcademicWork() throws Exception {
		// Initialize the database
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork);
		candidateNonAcademicWorkSearchRepository.save(candidateNonAcademicWork);
		int databaseSizeBeforeUpdate = candidateNonAcademicWorkRepository.findAll().size();

		// Update the candidateNonAcademicWork
		CandidateNonAcademicWork updatedCandidateNonAcademicWork = candidateNonAcademicWorkRepository
				.findOne(candidateNonAcademicWork.getId());
		updatedCandidateNonAcademicWork.nonAcademicInitiativeTitle(UPDATED_NON_ACADEMIC_INITIATIVE_TITLE)
				.nonAcademicInitiativeDescription(UPDATED_NON_ACADEMIC_INITIATIVE_DESCRIPTION)
				.duration(UPDATED_DURATION).isCurrentActivity(UPDATED_IS_CURRENT_ACTIVITY)
				.roleInInitiative(UPDATED_ROLE_IN_INITIATIVE)
				.nonAcademicWorkStartDate(UPDATED_NON_ACADEMIC_WORK_START_DATE)
				.nonAcademicWorkEndDate(UPDATED_NON_ACADEMIC_WORK_END_DATE);

		restCandidateNonAcademicWorkMockMvc
				.perform(put("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateNonAcademicWork)))
				.andExpect(status().isOk());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeUpdate);
		CandidateNonAcademicWork testCandidateNonAcademicWork = candidateNonAcademicWorkList
				.get(candidateNonAcademicWorkList.size() - 1);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeTitle())
				.isEqualTo(UPDATED_NON_ACADEMIC_INITIATIVE_TITLE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicInitiativeDescription())
				.isEqualTo(UPDATED_NON_ACADEMIC_INITIATIVE_DESCRIPTION);
		assertThat(testCandidateNonAcademicWork.getDuration()).isEqualTo(UPDATED_DURATION);
		assertThat(testCandidateNonAcademicWork.isIsCurrentActivity()).isEqualTo(UPDATED_IS_CURRENT_ACTIVITY);
		assertThat(testCandidateNonAcademicWork.getRoleInInitiative()).isEqualTo(UPDATED_ROLE_IN_INITIATIVE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkStartDate())
				.isEqualTo(UPDATED_NON_ACADEMIC_WORK_START_DATE);
		assertThat(testCandidateNonAcademicWork.getNonAcademicWorkEndDate())
				.isEqualTo(UPDATED_NON_ACADEMIC_WORK_END_DATE);

		// Validate the CandidateNonAcademicWork in Elasticsearch
	//	CandidateNonAcademicWork candidateNonAcademicWorkEs = candidateNonAcademicWorkSearchRepository
	//			.findOne(testCandidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkEs).isEqualToComparingFieldByField(testCandidateNonAcademicWork);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateNonAcademicWork() throws Exception {
		int databaseSizeBeforeUpdate = candidateNonAcademicWorkRepository.findAll().size();

		// Create the CandidateNonAcademicWork
		candidateRepository.saveAndFlush(candidate);
		candidateNonAcademicWork.setCandidate(candidate);
		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateNonAcademicWorkMockMvc
				.perform(put("/api/candidate-non-academic-works").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateNonAcademicWork)))
				.andExpect(status().isCreated());

		// Validate the CandidateNonAcademicWork in the database
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTheOnlyCandidateNonAcadShouldRemoveCertificationProfileScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		candidate.addNonAcademic(candidateNonAcademicWork);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,nonAcad);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateNonAcademicWorkRepository.findAll().size();

		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(delete("/api/candidate-non-academic-works/{id}", candidate.getNonAcademics().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateNonAcademicWorkExistsInEs = candidateNonAcademicWorkSearchRepository
	//			.exists(candidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateNonAcademicWorkList).hasSize(0);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(55D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}

	@Test
	@Transactional
	public void deleteOneOutOfTwoCandidateNonAcadShouldRemoveCertificationProfileScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		candidate.addNonAcademic(candidateNonAcademicWork);
		candidate.addNonAcademic(new CandidateNonAcademicWork().nonAcademicInitiativeTitle("Abhinav"));
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,nonAcad);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateNonAcademicWorkRepository.findAll().size();

		// Get the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(delete("/api/candidate-non-academic-works/{id}", candidate.getNonAcademics().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateNonAcademicWorkExistsInEs = candidateNonAcademicWorkSearchRepository
	//			.exists(candidateNonAcademicWork.getId());
	//	assertThat(candidateNonAcademicWorkExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateNonAcademicWork> candidateNonAcademicWorkList = candidateNonAcademicWorkRepository.findAll();
		assertThat(candidateNonAcademicWorkList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateNonAcademicWorkList).hasSize(1);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(60D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}
	
	
	@Test
	@Transactional
	public void searchCandidateNonAcademicWork() throws Exception {
		// Initialize the database
		candidateNonAcademicWorkRepository.saveAndFlush(candidateNonAcademicWork);
		candidateNonAcademicWorkSearchRepository.save(candidateNonAcademicWork);

		// Search the candidateNonAcademicWork
		restCandidateNonAcademicWorkMockMvc
				.perform(get("/api/_search/candidate-non-academic-works?query=id:" + candidateNonAcademicWork.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateNonAcademicWork.getId().intValue())))
				.andExpect(jsonPath("$.[*].nonAcademicInitiativeTitle")
						.value(hasItem(DEFAULT_NON_ACADEMIC_INITIATIVE_TITLE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicInitiativeDescription")
						.value(hasItem(DEFAULT_NON_ACADEMIC_INITIATIVE_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
				.andExpect(
						jsonPath("$.[*].isCurrentActivity").value(hasItem(DEFAULT_IS_CURRENT_ACTIVITY.booleanValue())))
				.andExpect(jsonPath("$.[*].roleInInitiative").value(hasItem(DEFAULT_ROLE_IN_INITIATIVE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicWorkStartDate")
						.value(hasItem(DEFAULT_NON_ACADEMIC_WORK_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].nonAcademicWorkEndDate")
						.value(hasItem(DEFAULT_NON_ACADEMIC_WORK_END_DATE.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateNonAcademicWork.class);
		CandidateNonAcademicWork candidateNonAcademicWork1 = new CandidateNonAcademicWork();
		candidateNonAcademicWork1.setId(1L);
		CandidateNonAcademicWork candidateNonAcademicWork2 = new CandidateNonAcademicWork();
		candidateNonAcademicWork2.setId(candidateNonAcademicWork1.getId());
		assertThat(candidateNonAcademicWork1).isEqualTo(candidateNonAcademicWork2);
		candidateNonAcademicWork2.setId(2L);
		assertThat(candidateNonAcademicWork1).isNotEqualTo(candidateNonAcademicWork2);
		candidateNonAcademicWork1.setId(null);
		assertThat(candidateNonAcademicWork1).isNotEqualTo(candidateNonAcademicWork2);
	}
}
