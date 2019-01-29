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
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CandidateEmploymentResource REST controller.
 *
 * @see CandidateEmploymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateEmploymentResourceIntTest {

	private static final Integer DEFAULT_LOCATION = 1;
	private static final Integer UPDATED_LOCATION = 2;

	private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

	private static final String DEFAULT_EMPLOYER_NAME = "AAAAAAAAAA";
	private static final String UPDATED_EMPLOYER_NAME = "BBBBBBBBBB";

	private static final LocalDate DEFAULT_EMPLOYMENT_START_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EMPLOYMENT_START_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_EMPLOYMENT_END_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EMPLOYMENT_END_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final Integer DEFAULT_EMPLOYMENT_DURATION = 1;
	private static final Integer UPDATED_EMPLOYMENT_DURATION = 2;

	private static final String DEFAULT_EMPLOYMENT_PERIOD = "AAAAAAAAAA";
	private static final String UPDATED_EMPLOYMENT_PERIOD = "BBBBBBBBBB";

	private static final Boolean DEFAULT_IS_CURRENT_EMPLOYMENT = false;
	private static final Boolean UPDATED_IS_CURRENT_EMPLOYMENT = true;

	private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

	private static final String DEFAULT_ROLE_AND_RESPONSIBILITIES = "AAAAAAAAAA";
	private static final String UPDATED_ROLE_AND_RESPONSIBILITIES = "BBBBBBBBBB";

	@Autowired
	private CandidateEmploymentRepository candidateEmploymentRepository;

	@Autowired
	private CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

	@Autowired
	private CandidateProjectRepository candidateProjectRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;
	
	@Autowired
	private EmploymentTypeRepository employmentpository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private ProfileScoreCalculator profileScoreCalculator;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;
	
	@Autowired
	private DTOConverters converter;

	private MockMvc restCandidateEmploymentMockMvc;

	private CandidateEmployment candidateEmployment;
	
	private Candidate candidate;
	
	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;
	
	private EmploymentType empType;
	
	private JobType jobType;
	
	public static Candidate createCandidateEntity(EntityManager em) {
		Candidate candidate = new Candidate().firstName("Abhinav");
				
		return candidate;
	}

	public static ProfileCategory createBasicProfile(EntityManager em) {
		return new ProfileCategory().categoryName(Constants.CANDIDATE_BASIC_PROFILE).weightage(5);
	}
	
	public static JobType createJobType(EntityManager em) {
		return new JobType().jobType("JobType");
	}
	
	public static EmploymentType createEmploymentType(EntityManager em) {
		return new EmploymentType().employmentType("EmploymentType");
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
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateEmploymentResource candidateEmploymentResource = new CandidateEmploymentResource(
				candidateEmploymentRepository, candidateEmploymentSearchRepository, candidateProjectRepository,profileScoreCalculator,candidateRepository,converter);
		this.restCandidateEmploymentMockMvc = MockMvcBuilders.standaloneSetup(candidateEmploymentResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateEmployment createEntity(EntityManager em) {
		CandidateEmployment candidateEmployment = new CandidateEmployment().location(DEFAULT_LOCATION)
				.jobTitle(DEFAULT_JOB_TITLE).employerName(DEFAULT_EMPLOYER_NAME)
				.employmentStartDate(DEFAULT_EMPLOYMENT_START_DATE).employmentEndDate(DEFAULT_EMPLOYMENT_END_DATE)
				.employmentDuration(DEFAULT_EMPLOYMENT_DURATION)

				.isCurrentEmployment(DEFAULT_IS_CURRENT_EMPLOYMENT).jobDescription(DEFAULT_JOB_DESCRIPTION);

		return candidateEmployment;
	}

	@Before
	public void initTest() {
		candidateEmploymentSearchRepository.deleteAll();
		candidateEmployment = createEntity(em);
		candidate = createCandidateEntity(em);
		basic = createBasicProfile(em);
		personal = createPersonalProfile(em);
		cert=createCertProfile(em);
		exp = createExpProfile(em);
		nonAcad = createNonAcadProfile(em);
		edu = createEduProfile(em);
		lang = createLangProfile(em);
		jobType =createJobType(em);
		empType = createEmploymentType(em);
		jobTypeRepository.saveAndFlush(jobType);
		employmentpository.saveAndFlush(empType);
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
	public void createFirstCandidateEmploymentShouldCreateExpProfileScore() throws Exception {
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		candidateEmployment.setCandidate(candidate);
		

		// Create the CandidateEmployment
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(DEFAULT_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(DEFAULT_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(DEFAULT_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(DEFAULT_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(DEFAULT_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(DEFAULT_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(DEFAULT_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testCandidateEmployment.getCandidate().getEmployments().size()).isEqualTo(1);
		assertThat(testCandidateEmployment.getCandidate().getCertifications().size()).isEqualTo(0);
		assertThat(testCandidateEmployment.getCandidate().getProfileScore()).isEqualTo(15d);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(15d);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(DEFAULT_ROLE_AND_RESPONSIBILITIES);

		// Validate the CandidateEmployment in Elasticsearch
	//	CandidateEmployment candidateEmploymentEs = candidateEmploymentSearchRepository
	//			.findOne(testCandidateEmployment.getId());
		//assertThat(candidateEmploymentEs).isEqualToComparingFieldByField(testCandidateEmployment);
	}
	
	@Test
	@Transactional
	public void createSecondCandidateEmploymentShouldNotChangeExpProfileScore() throws Exception {
		
		candidate.addEmployment(new CandidateEmployment().jobTitle("AAA"));
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,exp);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(15d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(70D);
		candidateRepository.saveAndFlush(candidate);
		candidateEmployment.setCandidate(candidate);
		// Create the CandidateEmployment
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEmploymentList).hasSize(2);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(DEFAULT_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(DEFAULT_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(DEFAULT_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(DEFAULT_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(DEFAULT_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(DEFAULT_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(DEFAULT_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testCandidateEmployment.getCandidate().getEmployments().size()).isEqualTo(2);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(15d);
		assertThat(testCandidateEmployment.getCandidate().getProfileScore()).isEqualTo(70D);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(DEFAULT_ROLE_AND_RESPONSIBILITIES);

		// Validate the CandidateEmployment in Elasticsearch
	//	CandidateEmployment candidateEmploymentEs = candidateEmploymentSearchRepository
	//			.findOne(testCandidateEmployment.getId());
		//assertThat(candidateEmploymentEs).isEqualToComparingFieldByField(testCandidateEmployment);
	}
	

	
	@Test
	@Transactional
	public void createFirstCandidateEmploymentShouldCreateExpProfileScoreAndMaintainAlreadyExistingScore() throws Exception {
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateEmployment.setCandidate(candidate);
		// Create the CandidateCertification
		restCandidateEmploymentMockMvc
		.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
		.andExpect(status().isCreated());

		// Validate the CandidateCertification in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(DEFAULT_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(DEFAULT_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(DEFAULT_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(DEFAULT_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(DEFAULT_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(DEFAULT_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(DEFAULT_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testCandidateEmployment.getCandidate().getEmployments().size()).isEqualTo(1);
		assertThat(testCandidateEmployment.getCandidate().getCertifications().size()).isEqualTo(0);
		assertThat(testCandidateEmployment.getCandidate().getProfileScore()).isEqualTo(70d);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(15d);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

	}
	
	@Test
	@Transactional
	public void createCandidateEmploymentForAlreadyCreatedwithZeroScoreShouldShouldUpDateScore() throws Exception {
		//candidate.addEmployment(new CandidateEmployment().jobTitle("AAA"));
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,exp);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(55D);
		candidateRepository.saveAndFlush(candidate);
		candidateEmployment.setCandidate(candidate);
		// Create the CandidateEmployment
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEmploymentList).hasSize(1);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(DEFAULT_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(DEFAULT_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(DEFAULT_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(DEFAULT_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(DEFAULT_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(DEFAULT_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(DEFAULT_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testCandidateEmployment.getCandidate().getEmployments().size()).isEqualTo(1);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(15d);
		assertThat(testCandidateEmployment.getCandidate().getProfileScore()).isEqualTo(70D);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidateEmployment.getCandidate().getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(DEFAULT_ROLE_AND_RESPONSIBILITIES);
	}

	@Test
	@Transactional
	public void createCandidateEmploymentWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateEmploymentRepository.findAll().size();

		// Create the CandidateEmployment with an existing ID
		candidateEmployment.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateEmploymentMockMvc
				.perform(post("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateEmployments() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);

		// Get all the candidateEmploymentList
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEmployment.getId().intValue())))
			//	.andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME.toString())))
				.andExpect(
						jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentEndDate").value(hasItem(DEFAULT_EMPLOYMENT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentDuration").value(hasItem(DEFAULT_EMPLOYMENT_DURATION)))
				.andExpect(jsonPath("$.[*].isCurrentEmployment")
						.value(hasItem(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())));

	}

	@Test
	@Transactional
	public void getCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments/{id}", candidateEmployment.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateEmployment.getId().intValue()))
				.andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
				.andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$.employerName").value(DEFAULT_EMPLOYER_NAME.toString()))
				.andExpect(jsonPath("$.employmentStartDate").value(DEFAULT_EMPLOYMENT_START_DATE.toString()))
				//.andExpect(jsonPath("$.employmentEndDate").value(DEFAULT_EMPLOYMENT_END_DATE.toString()))
				.andExpect(jsonPath("$.employmentDuration").value(DEFAULT_EMPLOYMENT_DURATION))
				.andExpect(jsonPath("$.isCurrentEmployment").value(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue()))
				.andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()));

	}
	
	@Test
	@Transactional
	public void getCandidateEmploymentWithProjectsWithProfileScore() throws Exception {
		// Initialize the database
		CandidateProject project = new CandidateProject().projectTitle("Hello");
		candidateEmploymentRepository.saveAndFlush(candidateEmployment.addProjects(project));
		candidateRepository.saveAndFlush(candidate.addEmployment(candidateEmployment).profileScore(25d));
		candidateEmployment.setEmploymentType(empType);
		candidateEmployment.setJobType(jobType);

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/employment-by-candidate/{id}", candidate.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateEmployment.getId().intValue()))
			//	.andExpect(jsonPath("$[0].location").value(DEFAULT_LOCATION))
				.andExpect(jsonPath("$[0].jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$[0].employerName").value(DEFAULT_EMPLOYER_NAME.toString()))
				.andExpect(jsonPath("$[0].employmentStartDate").value(DEFAULT_EMPLOYMENT_START_DATE.toString()))
				.andExpect(jsonPath("$[0].employmentEndDate").value(DEFAULT_EMPLOYMENT_END_DATE.toString()))
				//.andExpect(jsonPath("$[0].employmentDuration").value(DEFAULT_EMPLOYMENT_DURATION))
				//.andExpect(jsonPath("$[0].isCurrentEmployment").value(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue()))
				.andExpect(jsonPath("$[0].jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
				.andExpect(jsonPath("$[0].employmentType").value("EmploymentType"))
				.andExpect(jsonPath("$[0].jobType").value("JobType"))
				.andExpect(jsonPath("$[0].projects[0].projectTitle").value("Hello"))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));

	}
	
	@Test
	@Transactional
	public void getCandidateEmploymentWithProjectsWithoutProfileScore() throws Exception {
		// Initialize the database
		CandidateProject project = new CandidateProject().projectTitle("Hello");
		candidateEmploymentRepository.saveAndFlush(candidateEmployment.addProjects(project));
		candidateRepository.saveAndFlush(candidate.addEmployment(candidateEmployment));
		candidateEmployment.setEmploymentType(empType);
		candidateEmployment.setJobType(jobType);

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/employment-by-candidate/{id}", candidate.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateEmployment.getId().intValue()))
			//	.andExpect(jsonPath("$[0].location").value(DEFAULT_LOCATION))
				.andExpect(jsonPath("$[0].jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$[0].employerName").value(DEFAULT_EMPLOYER_NAME.toString()))
				.andExpect(jsonPath("$[0].employmentStartDate").value(DEFAULT_EMPLOYMENT_START_DATE.toString()))
				.andExpect(jsonPath("$[0].employmentEndDate").value(DEFAULT_EMPLOYMENT_END_DATE.toString()))
			//	.andExpect(jsonPath("$[0].employmentDuration").value(DEFAULT_EMPLOYMENT_DURATION))
				//.andExpect(jsonPath("$[0].isCurrentEmployment").value(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue()))
				.andExpect(jsonPath("$[0].jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
				.andExpect(jsonPath("$[0].employmentType").value("EmploymentType"))
				.andExpect(jsonPath("$[0].jobType").value("JobType"))
				.andExpect(jsonPath("$[0].projects[0].projectTitle").value("Hello"))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(0d));

	}
	
	@Test
	@Transactional
	public void getCandidateEmploymentByCandidateWithProfileScoreEmptyEmploymentnList() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		//candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateEmploymentMockMvc.perform(get("/api/employment-by-candidate/{id}",candidate.getId())).andDo(MockMvcResultHandlers.print())
				
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateEmployment() throws Exception {
		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(get("/api/candidate-employments/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		candidateEmploymentSearchRepository.save(candidateEmployment);
		int databaseSizeBeforeUpdate = candidateEmploymentRepository.findAll().size();

		// Update the candidateEmployment
		CandidateEmployment updatedCandidateEmployment = candidateEmploymentRepository
				.findOne(candidateEmployment.getId());
		updatedCandidateEmployment.location(UPDATED_LOCATION).jobTitle(UPDATED_JOB_TITLE)
				.employerName(UPDATED_EMPLOYER_NAME).employmentStartDate(UPDATED_EMPLOYMENT_START_DATE)
				.employmentEndDate(UPDATED_EMPLOYMENT_END_DATE).employmentDuration(UPDATED_EMPLOYMENT_DURATION)
				// .employmentPeriod(UPDATED_EMPLOYMENT_PERIOD)
				.isCurrentEmployment(UPDATED_IS_CURRENT_EMPLOYMENT).jobDescription(UPDATED_JOB_DESCRIPTION);
		// .roleAndResponsibilities(UPDATED_ROLE_AND_RESPONSIBILITIES);

		restCandidateEmploymentMockMvc
				.perform(put("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateEmployment)))
				.andExpect(status().isOk());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeUpdate);
		CandidateEmployment testCandidateEmployment = candidateEmploymentList.get(candidateEmploymentList.size() - 1);
		assertThat(testCandidateEmployment.getLocation()).isEqualTo(UPDATED_LOCATION);
		assertThat(testCandidateEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testCandidateEmployment.getEmployerName()).isEqualTo(UPDATED_EMPLOYER_NAME);
		assertThat(testCandidateEmployment.getEmploymentStartDate()).isEqualTo(UPDATED_EMPLOYMENT_START_DATE);
		assertThat(testCandidateEmployment.getEmploymentEndDate()).isEqualTo(UPDATED_EMPLOYMENT_END_DATE);
		assertThat(testCandidateEmployment.getEmploymentDuration()).isEqualTo(UPDATED_EMPLOYMENT_DURATION);
		// assertThat(testCandidateEmployment.getEmploymentPeriod()).isEqualTo(UPDATED_EMPLOYMENT_PERIOD);
		assertThat(testCandidateEmployment.isIsCurrentEmployment()).isEqualTo(UPDATED_IS_CURRENT_EMPLOYMENT);
		assertThat(testCandidateEmployment.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		// assertThat(testCandidateEmployment.getRoleAndResponsibilities()).isEqualTo(UPDATED_ROLE_AND_RESPONSIBILITIES);

		// Validate the CandidateEmployment in Elasticsearch
	//	CandidateEmployment candidateEmploymentEs = candidateEmploymentSearchRepository
	//			.findOne(testCandidateEmployment.getId());
	//	assertThat(candidateEmploymentEs).isEqualToComparingFieldByField(testCandidateEmployment);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateEmployment() throws Exception {
		int databaseSizeBeforeUpdate = candidateEmploymentRepository.findAll().size();
		candidateRepository.saveAndFlush(candidate);
		candidateEmployment.setCandidate(candidate);

		// Create the CandidateEmployment

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateEmploymentMockMvc
				.perform(put("/api/candidate-employments").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEmployment)))
				.andExpect(status().isCreated());

		// Validate the CandidateEmployment in the database
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTheOnlyCandidateEmploymentShouldRemoveCandidateEmploymentScoreFromProfile() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		candidate.addEmployment(candidateEmployment);
		//candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		//candidateEmploymentSearchRepository.save(candidateEmployment);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,exp);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(15d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(70D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateEmploymentRepository.findAll().size();

		// Get the candidateEmployment
		restCandidateEmploymentMockMvc.perform(delete("/api/candidate-employments/{id}", candidate.getEmployments().iterator().next().getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateEmploymentExistsInEs = candidateEmploymentSearchRepository.exists(candidateEmployment.getId());
	//	assertThat(candidateEmploymentExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		System.out.println("----------------------"+candidateEmploymentList);
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateEmploymentList).hasSize(0);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(55D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}
	
	@Test
	@Transactional
	public void deleteOneOutOfTwoCandidateEmployementsShouldNotRemoveProfileScoreForCerts() throws Exception {
		// Initialize the database	
		candidateRepository.saveAndFlush(candidate);
		candidate.addEmployment(candidateEmployment);
		candidate.addEmployment(new CandidateEmployment().jobTitle("Abhinav"));
		//candidateCertificationRepository.saveAndFlush(candidateCertification);
		//candidateCertificationSearchRepository.save(candidateCertification);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,exp);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(15d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(70D);
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeDelete = candidateEmploymentRepository.findAll().size();

		// Get the candidateCertification
		restCandidateEmploymentMockMvc.perform(delete("/api/candidate-employments/{id}", candidate.getEmployments().iterator().next().getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate Elasticsearch is empty
		//boolean candidateCertificationExistsInEs = candidateCertificationSearchRepository
		//		.exists(candidateCertification.getId());
		//assertThat(candidateCertificationExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateEmployment> candidateEmploymentList = candidateEmploymentRepository.findAll();
		assertThat(candidateEmploymentList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateEmploymentList).hasSize(1);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isEqualTo(15d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(70D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
	}


	@Test
	@Transactional
	public void searchCandidateEmployment() throws Exception {
		// Initialize the database
		candidateEmploymentRepository.saveAndFlush(candidateEmployment);
		candidateEmploymentSearchRepository.save(candidateEmployment);

		// Search the candidateEmployment
		restCandidateEmploymentMockMvc
				.perform(get("/api/_search/candidate-employments?query=id:" + candidateEmployment.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEmployment.getId().intValue())))
				.andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME.toString())))
				.andExpect(
						jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentEndDate").value(hasItem(DEFAULT_EMPLOYMENT_END_DATE.toString())))
				.andExpect(jsonPath("$.[*].employmentDuration").value(hasItem(DEFAULT_EMPLOYMENT_DURATION)))
				.andExpect(jsonPath("$.[*].isCurrentEmployment")
						.value(hasItem(DEFAULT_IS_CURRENT_EMPLOYMENT.booleanValue())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())));

	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateEmployment.class);
		CandidateEmployment candidateEmployment1 = new CandidateEmployment();
		candidateEmployment1.setId(1L);
		CandidateEmployment candidateEmployment2 = new CandidateEmployment();
		candidateEmployment2.setId(candidateEmployment1.getId());
		assertThat(candidateEmployment1).isEqualTo(candidateEmployment2);
		candidateEmployment2.setId(2L);
		assertThat(candidateEmployment1).isNotEqualTo(candidateEmployment2);
		candidateEmployment1.setId(null);
		assertThat(candidateEmployment1).isNotEqualTo(candidateEmployment2);
	}
}
