package com.drishika.gradzcircle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.service.CandidateLanguageService;
import com.drishika.gradzcircle.service.CandidateService;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CandidateLanguageProficiencyResource REST controller.
 *
 * @see CandidateLanguageProficiencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateLanguageProficiencyResourceIntTest {

	private static final String DEFAULT_PROFICIENCY = "AAAAAAAAAA";
	private static final String UPDATED_PROFICIENCY = "BBBBBBBBBB";

	@Autowired
	private CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	@Autowired
	private CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	@Autowired
	private CandidateLanguageService candidateLanguageService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateLanguageProficiencyMockMvc;

	private CandidateLanguageProficiency candidateLanguageProficiency;

	private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH;

	private Language hindiLanguage, englishLanguage, marathiLanguage;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private ProfileScoreCalculator profileScoreCalculator;

	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;
	
	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;
	
	private Candidate candidate;


	@Autowired
	private LanguageRepository languageRepository;

	private static final String JOB_A = "JOB_A";
	private static final String JOB_B = "JOB_B";
	private static final String JOB_C = "JOB_C";
	private static final String JOB_D = "JOB_D";
	private static final String JOB_E = "JOB_E";
	private static final String JOB_F = "JOB_F";
	private static final String JOB_G = "JOB_G";
	private static final String JOB_H = "JOB_H";
	private final static String HINDI = "Hindi";
	private final static String ENGLISH = "English";
	private final static String MARATHI = "Marathi";
	private final static String MALE = "MALE";
	private final static String FEMALE = "FEMALE";
	private Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
			universityFilter, qualificationFilter;
	private Gender maleGender, femaleGender;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateLanguageProficiencyResource candidateLanguageProficiencyResource = new CandidateLanguageProficiencyResource(
				candidateLanguageService);
		this.restCandidateLanguageProficiencyMockMvc = MockMvcBuilders
				.standaloneSetup(candidateLanguageProficiencyResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	public static Job createJobA(EntityManager em) {
		Job jobA = new Job().jobTitle(JOB_A).jobStatus(1);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\":[{\"value\":\"MIRANDA HOUSE\",\"display\":\"MIRANDA HOUSE\"}],\"universities\":[{\"value\":\"UNIVERSITY OF DELHI\",\"display\":\"UNIVERSITY OF DELHI\"}],\"premium\": true,\"courses\":[{\"value\":\"COMPUTER\",\"display\":\"COMPUTER\"}],\"qualifications\":[{\"value\":\"MASTERS\",\"display\":\"MASTERS\"}],\"scoreType\": \"percent\",\"percentage\": \"80\",\"addOn\": true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\": 2017,\"month\": 7,\"day\": 11},\"languages\":[{\"value\":\"Hindi\",\"display\":\"Hindi\"},{\"value\":\"English\",\"display\":\"English\"},{\"value\":\"Punjabi\",\"display\":\"Punjabi\"}]}";
		jobFilter.filterDescription(filterDescription).job(jobA);
		jobFilters.add(jobFilter);
		jobA.setJobFilters(jobFilters);
		return jobA;
	}

	public static Job createJobB(EntityManager em) {
		Job jobB = new Job().jobTitle(JOB_B).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"XYZ\",\"display\": \"XYZ\"}],\"universities\": [{\"value\": \"UNIVERSITY OF MUMBAI\",\"display\": \"UNIVERSITY OF MUMBAI\"}],\"premium\": true,\"courses\": [{\"value\":\"PHARMA\",\"display\": \"PHARMA\"}],\"qualifications\": [{\"value\": \"Diploma\",\"display\": \"Diploma\"}],\"scoreType\":\"percent\",\"percentage\": \"75\",\"addOn\": true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\":2017,\"month\": 2,\"day\": 24},\"languages\": [{\"value\":\"Hindi\",\"display\": \"Hindi\"},{\"value\": \"Marathi\",\"display\":\"Marathi\"}]}";
		jobFilter.filterDescription(filterDescription).job(jobB);
		jobFilters.add(jobFilter);
		jobB.setJobFilters(jobFilters);
		return jobB;
	}

	public static Job createJobC(EntityManager em) {
		Job jobC = new Job().jobTitle(JOB_C).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\":2017,\"month\": 7,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobC);
		jobFilters.add(jobFilter);
		jobC.setJobFilters(jobFilters);
		return jobC;

	}

	public static Job createJobF(EntityManager em) {
		Job jobF = new Job().jobTitle(JOB_F).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"less\",\"graduationDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobF);
		jobFilters.add(jobFilter);
		jobF.setJobFilters(jobFilters);
		return jobF;
	}

	public static Job createJobG(EntityManager em) {
		Job jobG = new Job().jobTitle(JOB_G).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"between\",\"graduationFromDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"graduationToDate\": {\"year\":2018,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobG);
		jobFilters.add(jobFilter);
		jobG.setJobFilters(jobFilters);
		return jobG;
	}

	public static Gender createMaleGender(EntityManager em) {
		return new Gender().gender(MALE);
	}

	public static Gender createFemaleGender(EntityManager em) {
		return new Gender().gender(FEMALE);
	}

	public static Language createHindiLanguae(EntityManager em) {
		return new Language().language(HINDI);
	}

	public static Language createMarathiLanguage(EntityManager em) {
		return new Language().language(MARATHI);
	}

	public static Language createEnglishLanguage(EntityManager em) {
		return new Language().language(ENGLISH);
	}

	public static Filter createGradDateFilter(EntityManager em) {
		return new Filter().filterName("gradDate").matchWeight(0L);
	}

	public static Filter createScoreFilter(EntityManager em) {
		return new Filter().filterName("score").matchWeight(8l);
	}

	public static Filter createCourseFilter(EntityManager em) {
		return new Filter().filterName("course").matchWeight(10l);
	}

	public static Filter createQualificationFilter(EntityManager em) {
		return new Filter().filterName("qualification").matchWeight(9L);
	}

	public static Filter createUniversityFilter(EntityManager em) {
		return new Filter().filterName("universities").matchWeight(7l);
	}

	public static Filter createCollegeFilter(EntityManager em) {
		return new Filter().filterName("colleges").matchWeight(6L);
	}

	public static Filter createLanguagefilter(EntityManager em) {
		return new Filter().filterName("languages").matchWeight(5L);
	}

	public static Filter createGenderFilter(EntityManager em) {
		return new Filter().filterName("gender").matchWeight(4L);
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
	
	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateLanguageProficiency createEntity(EntityManager em) {
		CandidateLanguageProficiency candidateLanguageProficiency = new CandidateLanguageProficiency()
				.proficiency(DEFAULT_PROFICIENCY);
		return candidateLanguageProficiency;
	}

	@Before
	public void initTest() {
		candidateLanguageProficiencyRepository.deleteAll();
		candidateLanguageProficiency = createEntity(em);
		maleGender = createMaleGender(em);
		femaleGender = createFemaleGender(em);
		jobA = createJobA(em);
		jobB = createJobB(em);
		jobC = createJobC(em);
		jobF = createJobF(em);
		jobG = createJobG(em);
		hindiLanguage = createHindiLanguae(em);
		englishLanguage = createEnglishLanguage(em);
		marathiLanguage = createMarathiLanguage(em);
		qualificationFilter = createQualificationFilter(em);
		courseFilter = createCourseFilter(em);
		collegeFilter = createCollegeFilter(em);
		universityFilter = createUniversityFilter(em);
		gradDateFilter = createGradDateFilter(em);
		scoreFilter = createScoreFilter(em);
		languageFilter = createLanguagefilter(em);
		genderFilter = createGenderFilter(em);
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
		
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		genderRepository.saveAndFlush(femaleGender);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		
	}

	@Test
	@Transactional
	public void createFirstCandidateLanguageProficiencyAndNoEducationSavedShouldAddToLanguageProfile() throws Exception {
		int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
	//	languageRepository.saveAndFlush(hindiLanguage);
		candidateLanguageProficiency.language(hindiLanguage);
		candidateLanguageProficiency.candidate(candidate);
		// Create the CandidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isCreated());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();

		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList
				.get(candidateLanguageProficiencyList.size() - 1);
		Candidate testCandidate = testCandidateLanguageProficiency.getCandidate();
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(1);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);

		// Validate the CandidateLanguageProficiency in Elasticsearch
		// CandidateLanguageProficiency candidateLanguageProficiencyEs =
		// candidateLanguageProficiencySearchRepository.findOne(testCandidateLanguageProficiency.getId());
		// assertThat(candidateLanguageProficiencyEs).isEqualToComparingFieldByField(testCandidateLanguageProficiency);
	}
	
	@Test
	@Transactional
	public void testCreateLanguageCreatAnotherOneDeleteOneUpdateRemainingDeleteAllScoreFrom10to5() throws Exception {
		
		int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
		candidateLanguageProficiency.language(hindiLanguage);
		candidateLanguageProficiency.candidate(candidate);
		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
		scoreBasic.setScore(5d);
		candidate.addCandidateProfileScore(scoreBasic);
		candidate.setProfileScore(5d);;
		candidateRepository.saveAndFlush(candidate);
		// Create the CandidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isCreated());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();

		assertThat(candidateLanguageProficiencyList).hasSize(1);
		CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList
				.get(candidateLanguageProficiencyList.size() - 1);
		Candidate testCandidate = testCandidateLanguageProficiency.getCandidate();
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(10d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		System.out.println("======================" + testCandidate.getProfileScores());
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		
		restCandidateLanguageProficiencyMockMvc
		.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
		.andExpect(status().isCreated());
		
		 candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();

		assertThat(candidateLanguageProficiencyList).hasSize(2);
		testCandidate = candidateRepository.findAll().get(0);
		testCandidateLanguageProficiency = candidateLanguageProficiencyList
				.get(candidateLanguageProficiencyList.size() - 1);
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(10d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		
		restCandidateLanguageProficiencyMockMvc
		.perform(delete("/api/candidate-language-proficiencies/{id}", testCandidateLanguageProficiency.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
		 candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
					.findAll();

			assertThat(candidateLanguageProficiencyList).hasSize(1);
			testCandidate = candidateRepository.findAll().get(0);
			testCandidateLanguageProficiency = candidateLanguageProficiencyList
					.get(candidateLanguageProficiencyList.size() - 1);
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(10d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		
		
        CandidateLanguageProficiency updatedCandidateLanguageProficiency = candidateLanguageProficiencyRepository
				.findOne(testCandidateLanguageProficiency.getId());
      //  System.out.println("=========== to update is "+updatedCandidateLanguageProficiency);
		restCandidateLanguageProficiencyMockMvc
		.perform(put("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidateLanguageProficiency.proficiency(UPDATED_PROFICIENCY))))
		.andExpect(status().isOk());
		
		 candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
					.findAll();

			assertThat(candidateLanguageProficiencyList).hasSize(1);
			testCandidate = candidateRepository.findAll().get(0);
			testCandidateLanguageProficiency = candidateLanguageProficiencyList
					.get(candidateLanguageProficiencyList.size() - 1);
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(UPDATED_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(10d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		
		restCandidateLanguageProficiencyMockMvc
		.perform(delete("/api/candidate-language-proficiencies/{id}", testCandidateLanguageProficiency.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());
		
		 candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
					.findAll();

			assertThat(candidateLanguageProficiencyList).hasSize(0);
			testCandidate = candidateRepository.findAll().get(0);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		
		
	}

	@Test
	@Transactional
	public void createFirstCandidateLanguageProficiencyWithZeroScoreForLangProfileAndNoEducationSavedShouldAddToLanguageProfile() throws Exception {
		int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,lang);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(55D);
		//languageRepository.saveAndFlush(hindiLanguage);
		candidateLanguageProficiency.language(hindiLanguage);
		candidateLanguageProficiency.candidate(candidate);
		
		// Create the CandidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isCreated());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();

		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeCreate + 1);
		CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList
				.get(candidateLanguageProficiencyList.size() - 1);
		Candidate testCandidate = testCandidateLanguageProficiency.getCandidate();
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(DEFAULT_PROFICIENCY);
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScore()).isEqualTo(60D);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		// Validate the CandidateLanguageProficiency in Elasticsearch
		// CandidateLanguageProficiency candidateLanguageProficiencyEs =
		// candidateLanguageProficiencySearchRepository.findOne(testCandidateLanguageProficiency.getId());
		// assertThat(candidateLanguageProficiencyEs).isEqualToComparingFieldByField(testCandidateLanguageProficiency);
	}

	
	@Test
	@Transactional
	public void createAdditionalCandidateLanguageProficiencyForCandidateWithHighestEducationAndHaveCandidateJobDataSetShouldNotUpdateLanguageScoreAndLanguageProfileScore()
			throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		CandidateLanguageProficiency profHindi = new CandidateLanguageProficiency().language(hindiLanguage);
		CandidateLanguageProficiency profMarathi = new CandidateLanguageProficiency().language(marathiLanguage);
		candidate.addCandidateLanguageProficiency(profMarathi).addCandidateLanguageProficiency(profHindi);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,lang);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		candidateJob1.setLanguageMatchScore(2.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setLanguageMatchScore(2.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setLanguageMatchScore(0.0);
		candidateJob3.setGenderMatchScore(4.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(45.0);
		candidateJob4.setLanguageMatchScore(0.0);
		candidateJob4.setGenderMatchScore(4.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(45.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateLanguageProficiency.candidate(candidate).language(englishLanguage);
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isCreated());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		
		Set<CandidateLanguageProficiency> testCandidateLanguageProficiencies = testCandidates.get(0)
				.getCandidateLanguageProficiencies();
		assertThat(testCandidateLanguageProficiencies).hasSize(3);
		
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getProfileScores().size()).isEqualTo(3);
		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCandidate.getProfileScore()).isEqualTo(60D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		assertThat(testCandidate.getCandidateJobs()).hasSize(4);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 82.0, 30.0, 4.0, 3.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 87.0, 30.0, 4.0, 5.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_G, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteCandidateLanguageProficiencyForCandidateWithHighestEducationAndHaveCandidateJobDataSet()
			throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);

		Set<CandidateJob> candidateJobs = new HashSet<>();
		CandidateLanguageProficiency profHindi = new CandidateLanguageProficiency().language(hindiLanguage);
		CandidateLanguageProficiency profMarathi = new CandidateLanguageProficiency().language(marathiLanguage);
		candidateLanguageProficiency.language(englishLanguage);
		candidate.addCandidateLanguageProficiency(profMarathi).addCandidateLanguageProficiency(profHindi)
				.addCandidateLanguageProficiency(candidateLanguageProficiency);
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setLanguageMatchScore(0.0);
		candidateJob3.setGenderMatchScore(4.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(44.0);
		candidateJob4.setLanguageMatchScore(0.0);
		candidateJob4.setGenderMatchScore(4.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(44.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		//

		restCandidateLanguageProficiencyMockMvc
				.perform(delete("/api/candidate-language-proficiencies/{id}", candidateLanguageProficiency.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		List<CandidateLanguageProficiency> testLanguages = candidateLanguageProficiencyRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateJobs()).hasSize(4);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 80.0, 30.0, 4.0, 2.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 87.0, 30.0, 4.0, 5.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_G, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"));
		assertThat(testCandidate.getCandidateLanguageProficiencies()).hasSize(2);
		assertThat(testCandidate.getCandidateLanguageProficiencies()).extracting("language.language").contains(HINDI)
				.contains(MARATHI);
		assertThat(testLanguages).hasSize(2);
		assertThat(testLanguages).extracting("language.language", "candidate.firstName")
				.contains(tuple(HINDI, "Abhinav")).contains(tuple(MARATHI, "Abhinav"));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteTheOnlyCandidateLanguageProficiencyForCandidateWithHighestEducationAndHaveCandidateJobDataSet()
			throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);

		Set<CandidateJob> candidateJobs = new HashSet<>();
		// CandidateLanguageProficiency profHindi = new
		// CandidateLanguageProficiency().language(hindiLanguage).candidate(candidate);
		// CandidateLanguageProficiency profMarathi = new
		// CandidateLanguageProficiency().language(marathiLanguage).candidate(candidate);
		candidateLanguageProficiency.language(englishLanguage);
		candidate.addCandidateLanguageProficiency(candidateLanguageProficiency);
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,lang);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setLanguageMatchScore(0.0);
		candidateJob3.setGenderMatchScore(4.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(44.0);
		candidateJob4.setLanguageMatchScore(0.0);
		candidateJob4.setGenderMatchScore(4.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(44.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		//

		restCandidateLanguageProficiencyMockMvc
				.perform(delete("/api/candidate-language-proficiencies/{id}", candidateLanguageProficiency.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		List<CandidateLanguageProficiency> testLanuages = candidateLanguageProficiencyRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateJobs()).hasSize(4);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 76.0, 30.0, 4.0, 0.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 76.0, 30.0, 4.0, 0.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_G, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"));
		assertThat(testCandidate.getCandidateLanguageProficiencies()).hasSize(0);
		assertThat(testLanuages).hasSize(0);
		// assertThat(testCandidate.getCandidateLanguageProficiencies()).extracting("language.language").contains(HINDI).contains(MARATHI);

	}

	@Test
	@Transactional
	public void createCandidateLanguageProficiencyWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateLanguageProficiencyRepository.findAll().size();

		// Create the CandidateLanguageProficiency with an existing ID
		candidateLanguageProficiency.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();
		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateLanguageProficiencies() throws Exception {
		// Initialize the database
		
		candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);

		// Get all the candidateLanguageProficiencyList
		restCandidateLanguageProficiencyMockMvc.perform(get("/api/candidate-language-proficiencies?sort=id,desc"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateLanguageProficiency.getId().intValue())))
				.andExpect(jsonPath("$.[*].proficiency").value(hasItem(DEFAULT_PROFICIENCY.toString())));
	}

	@Test
	@Transactional
	public void getCandidateLanguageProficiency() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		languageRepository.saveAndFlush(hindiLanguage);
		candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency.language(hindiLanguage).candidate(candidate));

		// Get the candidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(get("/api/candidate-language-proficiencies/{id}", candidateLanguageProficiency.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateLanguageProficiency.getId().intValue()))
				.andExpect(jsonPath("$.proficiency").value(DEFAULT_PROFICIENCY.toString()))
				.andExpect(jsonPath("$.candidate.profileScore").value(25D));
	}
	
	@Test
	@Transactional
	@Ignore
	public void getCandidateLaguageProfByCandidateWithProfileScoreEmptyLangProfList() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		//candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateLanguageProficiencyMockMvc.perform(get("/api/language-proficiencies-by-candidate/{id}",candidate.getId())).andDo(MockMvcResultHandlers.print())
				
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateLanguageProficiency() throws Exception {
		// Get the candidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(get("/api/candidate-language-proficiencies/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateLanguageProficiencyAndNoEducationSaved() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
		candidateLanguageProficiency.candidate(candidate);
		candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
		candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);
		int databaseSizeBeforeUpdate = candidateLanguageProficiencyRepository.findAll().size();

		// Update the candidateLanguageProficiency
		CandidateLanguageProficiency updatedCandidateLanguageProficiency = candidateLanguageProficiencyRepository
				.findOne(candidateLanguageProficiency.getId());
		updatedCandidateLanguageProficiency.proficiency(UPDATED_PROFICIENCY);

		restCandidateLanguageProficiencyMockMvc
				.perform(put("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateLanguageProficiency)))
				.andExpect(status().isOk());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();
		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeUpdate);
		CandidateLanguageProficiency testCandidateLanguageProficiency = candidateLanguageProficiencyList
				.get(candidateLanguageProficiencyList.size() - 1);
		assertThat(testCandidateLanguageProficiency.getProficiency()).isEqualTo(UPDATED_PROFICIENCY);
		assertThat(testCandidateLanguageProficiency.getCandidate().getCandidateJobs()).hasSize(0);
		// Validate the CandidateLanguageProficiency in Elasticsearch
		// CandidateLanguageProficiency candidateLanguageProficiencyEs =
		// candidateLanguageProficiencySearchRepository.findOne(testCandidateLanguageProficiency.getId());
		// assertThat(candidateLanguageProficiencyEs).isEqualToComparingFieldByField(testCandidateLanguageProficiency);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidateLanguageProficiency() throws Exception {
		int databaseSizeBeforeUpdate = candidateLanguageProficiencyRepository.findAll().size();
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
		candidateLanguageProficiency.language(hindiLanguage);
		candidateLanguageProficiency.candidate(candidate);

		// Create the CandidateLanguageProficiency

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateLanguageProficiencyMockMvc
				.perform(put("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateLanguageProficiency)))
				.andExpect(status().isCreated());

		// Validate the CandidateLanguageProficiency in the database
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();
		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteTheOnlyCandidateLanguageProficiencyShouldRemoveLanguageProfileScore() throws Exception {
		// Initialize the database
		//languageRepository.saveAndFlush(hindiLanguage);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(candidate);
		CandidateLanguageProficiency profHindi = new CandidateLanguageProficiency().language(hindiLanguage);
		candidateLanguageProficiencyRepository.saveAndFlush(profHindi);
		candidate.addCandidateLanguageProficiency(profHindi.language(hindiLanguage));
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,lang);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		// candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
		// candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);
		int databaseSizeBeforeDelete = candidateLanguageProficiencyRepository.findAll().size();

		// Get the candidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(delete("/api/candidate-language-proficiencies/{id}", profHindi.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		// boolean candidateLanguageProficiencyExistsInEs =
		// candidateLanguageProficiencySearchRepository.exists(candidateLanguageProficiency.getId());
		// assertThat(candidateLanguageProficiencyExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();
		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateLanguageProficiencyList).hasSize(0);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().size()).isEqualTo(3);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(55D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteTheOneOfManyCandidateLanguageProficiencyShouldNotChangeLanguageProfileScore() throws Exception {
		// Initialize the database
	//	languageRepository.saveAndFlush(hindiLanguage);
	//	languageRepository.saveAndFlush(englishLanguage);
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateLanguageProficiency profHindi = new CandidateLanguageProficiency().language(hindiLanguage);
		CandidateLanguageProficiency profEng = new CandidateLanguageProficiency().language(englishLanguage);
		candidateLanguageProficiencyRepository.saveAndFlush(profHindi);
		candidateLanguageProficiencyRepository.saveAndFlush(profEng);
		candidate.addCandidateLanguageProficiency(profHindi.language(hindiLanguage));
		candidate.addCandidateLanguageProficiency(profEng.language(englishLanguage));
		candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,lang);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		candidateRepository.saveAndFlush(candidate);
		// candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
		// candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);
		int databaseSizeBeforeDelete = candidateLanguageProficiencyRepository.findAll().size();

		// Get the candidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(delete("/api/candidate-language-proficiencies/{id}", profHindi.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		// boolean candidateLanguageProficiencyExistsInEs =
		// candidateLanguageProficiencySearchRepository.exists(candidateLanguageProficiency.getId());
		// assertThat(candidateLanguageProficiencyExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateLanguageProficiency> candidateLanguageProficiencyList = candidateLanguageProficiencyRepository
				.findAll();
		assertThat(candidateLanguageProficiencyList).hasSize(databaseSizeBeforeDelete - 1);
		assertThat(candidateLanguageProficiencyList).hasSize(1);
		List<Candidate> testCanidates = candidateRepository.findAll();
		assertThat(testCanidates.get(0).getProfileScores().size()).isEqualTo(3);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isEqualTo(5d);
		assertThat(testCanidates.get(0).getProfileScore()).isEqualTo(60D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCanidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

		
	}

	@Test
	@Transactional
	@Ignore
	public void searchCandidateLanguageProficiency() throws Exception {
		// Initialize the database
		candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
		candidateLanguageProficiencySearchRepository.save(candidateLanguageProficiency);

		// Search the candidateLanguageProficiency
		restCandidateLanguageProficiencyMockMvc
				.perform(get("/api/_search/candidate-language-proficiencies?query=id:"
						+ candidateLanguageProficiency.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateLanguageProficiency.getId().intValue())))
				.andExpect(jsonPath("$.[*].proficiency").value(hasItem(DEFAULT_PROFICIENCY.toString())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateLanguageProficiency.class);
		CandidateLanguageProficiency candidateLanguageProficiency1 = new CandidateLanguageProficiency();
		candidateLanguageProficiency1.setId(1L);
		CandidateLanguageProficiency candidateLanguageProficiency2 = new CandidateLanguageProficiency();
		candidateLanguageProficiency2.setId(candidateLanguageProficiency1.getId());
		assertThat(candidateLanguageProficiency1).isEqualTo(candidateLanguageProficiency2);
		candidateLanguageProficiency2.setId(2L);
		assertThat(candidateLanguageProficiency1).isNotEqualTo(candidateLanguageProficiency2);
		candidateLanguageProficiency1.setId(null);
		assertThat(candidateLanguageProficiency1).isNotEqualTo(candidateLanguageProficiency2);
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndEducationThenDeleteLanguage() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);

		Set<CandidateJob> candidateJobs = new HashSet<>();

		CandidateLanguageProficiency profHindi = new CandidateLanguageProficiency().language(hindiLanguage);
		CandidateLanguageProficiency profMarathi = new CandidateLanguageProficiency().language(marathiLanguage);
		candidate.addCandidateLanguageProficiency(profMarathi).addCandidateLanguageProficiency(profHindi);
		candidate.setGender(femaleGender);
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		candidateJob1.setLanguageMatchScore(5.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setLanguageMatchScore(2.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setLanguageMatchScore(2.0);
		candidateJob3.setGenderMatchScore(4.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(45.0);
		candidateJob4.setLanguageMatchScore(2.0);
		candidateJob4.setGenderMatchScore(4.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(45.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		//

		restCandidateLanguageProficiencyMockMvc.perform(delete("/api/candidate-language-proficiencies/{id}",
				candidate.getCandidateLanguageProficiencies().stream()
						.filter(lang -> lang.getLanguage().getLanguage().equals(HINDI)).findFirst().get().getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateLanguageProficiencies()).hasSize(1);
		assertThat(testCandidate.getCandidateLanguageProficiencies()).extracting("language.language").contains(MARATHI);
		assertThat(testCandidate.getCandidateJobs()).hasSize(4);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 76.0, 30.0, 4.0, 0.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 82.0, 30.0, 4.0, 3.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_G, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 77.0, 30.0, 4.0, null, 44.0, "Abhinav"));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createGenderAndEducationThenLanguage() throws Exception {

		Candidate candidate = new Candidate().firstName("Abhinav").gender(femaleGender);
		CandidateEducation education = new CandidateEducation().educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).highestQualification(true);
		candidate.addEducation(education);
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobB);
		candidateJob1.setEducationMatchScore(0d);
		candidateJob1.setGenderMatchScore(null);
		candidateJob1.setLanguageMatchScore(null);
		candidateJob1.setTotalEligibleScore(45d);
		candidateJob1.setMatchScore(0d);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobF);
		candidateJob2.setEducationMatchScore(0d);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setLanguageMatchScore(null);
		candidateJob2.setTotalEligibleScore(44d);
		candidateJob2.setMatchScore(9d);
		candidate.addCandidateJob(candidateJob2).addCandidateJob(candidateJob1);
		candidateRepository.saveAndFlush(candidate);
		Thread.sleep(10000);
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(
								candidateLanguageProficiency.language(hindiLanguage).candidate(candidate))))
				.andExpect(status().isCreated());

		List<CandidateLanguageProficiency> languaaes = candidateLanguageProficiencyRepository.findAll();
		assertThat(languaaes).hasSize(1);
		assertThat(languaaes).extracting("language.language").contains(HINDI);
		assertThat(languaaes).extracting("candidate.firstName").contains("Abhinav");
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0)).extracting("firstName").contains("Abhinav");
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidates.get(0).getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 7.0, 0.0, null, 3.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_F, 9.0, 0.0, 4.0, null, 44.0, "Abhinav"));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createEducationThenLanguage() throws Exception {

		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation education = new CandidateEducation().educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).highestQualification(true);
		candidate.addEducation(education);
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobB);
		candidateJob1.setEducationMatchScore(0d);
		candidateJob1.setGenderMatchScore(null);
		candidateJob1.setLanguageMatchScore(null);
		candidateJob1.setTotalEligibleScore(45d);
		candidateJob1.setMatchScore(0d);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobF);
		candidateJob2.setEducationMatchScore(0d);
		candidateJob2.setGenderMatchScore(0.0);
		candidateJob2.setLanguageMatchScore(null);
		candidateJob2.setTotalEligibleScore(44d);
		candidateJob2.setMatchScore(0d);
		candidate.addCandidateJob(candidateJob2).addCandidateJob(candidateJob1);
		candidateRepository.saveAndFlush(candidate);
		restCandidateLanguageProficiencyMockMvc
				.perform(post("/api/candidate-language-proficiencies").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(
								candidateLanguageProficiency.language(marathiLanguage).candidate(candidate))))
				.andExpect(status().isCreated());

		List<CandidateLanguageProficiency> languaaes = candidateLanguageProficiencyRepository.findAll();
		assertThat(languaaes).hasSize(1);
		assertThat(languaaes).extracting("language.language").contains(MARATHI);
		assertThat(languaaes).extracting("candidate.firstName").contains("Abhinav");
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0)).extracting("firstName").contains("Abhinav");
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidates.get(0).getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 7.0, 0.0, null, 3.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_F, 0.0, 0.0, 0.0, null, 44.0, "Abhinav"));

	}
}
