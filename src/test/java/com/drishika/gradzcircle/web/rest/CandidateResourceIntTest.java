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
import java.time.ZoneId;
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
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.NationalityRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.UserRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
import com.drishika.gradzcircle.service.CandidateService;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

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

	private final static String MALE = "MALE";
	private final static String FEMALE = "FEMALE";

	private Gender maleGender, femaleGender;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CorporateRepository corporateRepository;

	@Autowired
	private CandidateSearchRepository candidateSearchRepository;
	
	@Autowired
	private LanguageRepository languageRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	NationalityRepository nationalityRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private GenderRepository genderRepository;
	
	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateMockMvc;

	private Candidate candidate;
	
	private Corporate corporate;
	
	private User user;
	
	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;

	private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH;
	
	private Language english, hindi;

	private static final String JOB_A = "JOB_A";
	private static final String JOB_B = "JOB_B";
	private static final String JOB_C = "JOB_C";
	private static final String JOB_D = "JOB_D";
	private static final String JOB_E = "JOB_E";
	private static final String JOB_F = "JOB_F";
	private static final String JOB_G = "JOB_G";
	private static final String JOB_H = "JOB_H";

	private Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
			universityFilter, qualificationFilter;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateResource candidateResource = new CandidateResource(candidateService);
		this.restCandidateMockMvc = MockMvcBuilders.standaloneSetup(candidateResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	
	public static User createUser(EntityManager em) {
	
		User user = new User();
		user.setActivated(true);
		user.setLogin("abhi");
		user.setPassword("passjohndoepassjohndoepassjohndoepassjohndoepassjohndoepassj");
		user.setEmail("johndoe@localhost");
		return user;
		
	}
	
	public static Language createEnglish(EntityManager em) {
		return new Language().language("English");
	}
	
	public static Language createHindi(EntityManager em) {
		return new Language().language("Hindi");
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
	public static Candidate createEntity(EntityManager em) {
		Candidate candidate = new Candidate().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME)
				.middleName(DEFAULT_MIDDLE_NAME).facebook(DEFAULT_FACEBOOK).linkedIn(DEFAULT_LINKED_IN)
				.twitter(DEFAULT_TWITTER).aboutMe(DEFAULT_ABOUT_ME).dateOfBirth(DEFAULT_DATE_OF_BIRTH)
				.phoneCode(DEFAULT_PHONE_CODE).phoneNumber(DEFAULT_PHONE_NUMBER)
				.differentlyAbled(DEFAULT_DIFFERENTLY_ABLED).availableForHiring(DEFAULT_AVAILABLE_FOR_HIRING)
				.openToRelocate(DEFAULT_OPEN_TO_RELOCATE);
		
		return candidate;
	}
	
	public static Corporate createCorporate(EntityManager em) {
		Corporate corporate = new Corporate().name("Drishika");
		
		return corporate;
	}

	@Before
	public void initTest() {
		candidateSearchRepository.deleteAll();
		candidate = createEntity(em);
		maleGender = createMaleGender(em);
		femaleGender = createFemaleGender(em);
		corporate = createCorporate(em);
		user = createUser(em);
		jobA = createJobA(em);
		jobB = createJobB(em);
		jobC = createJobC(em);
		basic = createBasicProfile(em);
		personal = createPersonalProfile(em);
		cert=createCertProfile(em);
		exp = createExpProfile(em);
		nonAcad = createNonAcadProfile(em);
		edu = createEduProfile(em);
		lang = createLangProfile(em);
		jobF = createJobF(em);
		jobG = createJobG(em);
		qualificationFilter = createQualificationFilter(em);
		courseFilter = createCourseFilter(em);
		collegeFilter = createCollegeFilter(em);
		universityFilter = createUniversityFilter(em);
		gradDateFilter = createGradDateFilter(em);
		scoreFilter = createScoreFilter(em);
		languageFilter = createLanguagefilter(em);
		genderFilter = createGenderFilter(em);
		corporateRepository.saveAndFlush(corporate);
		userRepository.saveAndFlush(user);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		genderRepository.saveAndFlush(maleGender);
		genderRepository.saveAndFlush(femaleGender);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		languageRepository.saveAndFlush(createEnglish(em));
		languageRepository.saveAndFlush(createHindi(em));
	}

	@Test
	@Transactional
	public void createCandidateWithGenderWithoutEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateRepository.findAll().size();
	
		candidate.setGender(maleGender);
		// Create the Candidate
		restCandidateMockMvc.perform(post("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidate))).andExpect(status().isCreated());

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
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		// Validate the Candidate in Elasticsearch
		// Candidate candidateEs =
		// candidateSearchRepository.findOne(testCandidate.getId());
		// assertThat(candidateEs).isEqualToComparingFieldByField(testCandidate);

	}

	@Test
	@Transactional
	public void updateCandidateGenderMaleJobRequiresFemaleWithEducationWithMatchedData() throws Exception {

		Set<CandidateJob> candidateJobs = new HashSet<>();
	
		genderRepository.saveAndFlush(femaleGender);
		Candidate candidate = new Candidate().firstName("Abhinav").gender(femaleGender);
		candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		candidate.addEducation(candidateEducation);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		CandidateJob candidateJob5 = new CandidateJob(candidate, jobC);
		candidateJob1.setMatchScore(71.0);
		candidateJob1.setLanguageMatchScore(2.0);
		candidateJob1.setGenderMatchScore(0.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setMatchScore(71.0);
		candidateJob2.setLanguageMatchScore(2.0);
		candidateJob2.setGenderMatchScore(0.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setMatchScore(71.0);
		candidateJob3.setLanguageMatchScore(2.0);
		candidateJob3.setGenderMatchScore(0.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(45.0);
		candidateJob4.setMatchScore(71.0);
		candidateJob4.setLanguageMatchScore(2.0);
		candidateJob4.setGenderMatchScore(0.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(45.0);
		candidateJob5.setMatchScore(71.0);
		candidateJob5.setLanguageMatchScore(2.0);
		candidateJob5.setGenderMatchScore(0.0);
		candidateJob5.setEducationMatchScore(30.0);
		candidateJob5.setTotalEligibleScore(45.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateJobs.add(candidateJob5);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidate);

		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateJobs()).hasSize(5);
		assertThat(testCandidate.getEducations()).hasSize(1);
		assertThat(testCandidate.getCandidateJobs())

				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 71.0, 30.0, null, 2.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 71.0, 30.0, null, 2.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_C, 73.0, 30.0, null, 2.0, 44.0, "Abhinav"))
				.contains(tuple(JOB_G, 73.0, 30.0, null, 2.0, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 73.0, 30.0, null, 2.0, 44.0, "Abhinav"));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguagesSavedwithGenderSetChnagingGenderMustNotImpactLanguages() throws Exception {
		candidate.gender(femaleGender);
		
		CandidateLanguageProficiency englishProf = new CandidateLanguageProficiency().language(english);
		CandidateLanguageProficiency hindiProf = new CandidateLanguageProficiency().language(hindi);
		candidate.addCandidateLanguageProficiency(hindiProf).addCandidateLanguageProficiency(englishProf);
		candidateRepository.saveAndFlush(candidate);
		
		
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getCandidateLanguageProficiencies()).hasSize(2);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasEducationSavedwithGenderSetChnagingGenderMustNotImpactEducation() throws Exception {
		candidate.gender(femaleGender);
		CandidateEducation education1 = new CandidateEducation().highestQualification(true);
		CandidateEducation education2 = new CandidateEducation().highestQualification(false);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		candidateRepository.saveAndFlush(candidate);
		candidate.addEducation(education2).addEducation(education1);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getEducations()).hasSize(2);
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(5);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasNoEducationSavedwithGenderSetChnagingGenderMustNotImpactEducationAnNoCadidateJobs() throws Exception {
		candidate.gender(femaleGender);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		candidateRepository.saveAndFlush(candidate);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getEducations()).hasSize(0);
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(0);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasEmploymentsSavedwithGenderSetChnagingGenderMustNotImpactEmployments() throws Exception {
		candidate.gender(femaleGender);
		
		CandidateEmployment employment1 = new CandidateEmployment();
		CandidateEmployment employment2 = new CandidateEmployment();
		candidate.addEmployment(employment2).addEmployment(employment1);
		candidateRepository.saveAndFlush(candidate);
		
		
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getEmployments()).hasSize(2);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasNonAcademicvedwithGenderSetChnagingGenderMustNotImpactNonAcademic() throws Exception {
		candidate.gender(femaleGender);
		
		CandidateNonAcademicWork nonAcademic1 = new CandidateNonAcademicWork();
		CandidateNonAcademicWork nonAcademic2 = new CandidateNonAcademicWork();
		candidate.addNonAcademic(nonAcademic2).addNonAcademic(nonAcademic1);
		candidateRepository.saveAndFlush(candidate);
		
		
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getNonAcademics()).hasSize(2);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasCertificationSavedwithGenderSetChnagingGenderMustNotImpactCertifications() throws Exception {
		candidate.gender(femaleGender);
		
		CandidateCertification cert1 = new CandidateCertification();
		CandidateCertification cert2 = new CandidateCertification();
		candidate.addCertification(cert2).addCertification(cert1);
		candidateRepository.saveAndFlush(candidate);
		
		
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(maleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getCertifications()).hasSize(2);
		assertThat(testCandidates.get(0).getGender().getGender()).isEqualTo("MALE");
		
	}

	@Test
	@Transactional
	public void updateCandidateGenderFemaleFromMaleJobRequiresFemaleWithEducationWithMatchedData() throws Exception {

		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);

		Set<CandidateJob> candidateJobs = new HashSet<>();
		
		candidateRepository.saveAndFlush(candidate);
		CandidateJob candidateJob1 = new CandidateJob(candidate, jobA);
		CandidateJob candidateJob2 = new CandidateJob(candidate, jobB);
		CandidateJob candidateJob3 = new CandidateJob(candidate, jobF);
		CandidateJob candidateJob4 = new CandidateJob(candidate, jobG);
		CandidateJob candidateJob5 = new CandidateJob(candidate, jobC);
		candidateJob1.setMatchScore(71.0);
		candidateJob1.setLanguageMatchScore(2.0);
		candidateJob1.setGenderMatchScore(0.0);
		candidateJob1.setEducationMatchScore(30.0);
		candidateJob1.setTotalEligibleScore(45.0);
		candidateJob2.setMatchScore(71.0);
		candidateJob2.setLanguageMatchScore(2.0);
		candidateJob2.setGenderMatchScore(0.0);
		candidateJob2.setEducationMatchScore(30.0);
		candidateJob2.setTotalEligibleScore(45.0);
		candidateJob3.setMatchScore(71.0);
		candidateJob3.setLanguageMatchScore(2.0);
		candidateJob3.setGenderMatchScore(0.0);
		candidateJob3.setEducationMatchScore(30.0);
		candidateJob3.setTotalEligibleScore(45.0);
		candidateJob4.setMatchScore(71.0);
		candidateJob4.setLanguageMatchScore(2.0);
		candidateJob4.setGenderMatchScore(0.0);
		candidateJob4.setEducationMatchScore(30.0);
		candidateJob4.setTotalEligibleScore(45.0);
		candidateJob5.setMatchScore(71.0);
		candidateJob5.setLanguageMatchScore(2.0);
		candidateJob5.setGenderMatchScore(0.0);
		candidateJob5.setEducationMatchScore(30.0);
		candidateJob5.setTotalEligibleScore(45.0);
		candidateJobs.add(candidateJob4);
		candidateJobs.add(candidateJob3);
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateJobs.add(candidateJob5);
		candidate.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));

		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(femaleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateJobs()).hasSize(5);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 71.0, 30.0, null, 2.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 71.0, 30.0, null, 2.0, 45.0, "Abhinav"))
				.contains(tuple(JOB_C, 82.0, 30.0, 4.0, 2.0, 44.0, "Abhinav"))
				.contains(tuple(JOB_G, 82.0, 30.0, 4.0, 2.0, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 82.0, 30.0, 4.0, 2.0, 44.0, "Abhinav"));
	}

	@Test
	@Transactional
	public void updateCandidateGenderWithEducationWithoutAnyMatchedData() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);

		
		// candidateRepository.saveAndFlush(candidate);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));

		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setGender(femaleGender);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getCandidateJobs()).hasSize(5);
		assertThat(testCandidate.getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_A, 0.0, null, null, null, 45.0, "Abhinav"))
				.contains(tuple(JOB_B, 0.0, null, null, null, 45.0, "Abhinav"))
				.contains(tuple(JOB_C, 9.0, null, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_G, 9.0, null, 4.0, null, 44.0, "Abhinav"))
				.contains(tuple(JOB_F, 9.0, null, 4.0, null, 44.0, "Abhinav"));
	}

	@Test
	@Transactional
	public void applyFirstJob() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);

		// candidateRepository.saveAndFlush(candidate);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));

		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.addAppliedJob(jobA);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidate-apply").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getAppliedJobs()).hasSize(1);
		assertThat(testCandidate.getAppliedJobs()).extracting("jobTitle").contains(JOB_A);

	}

	@Test
	@Transactional
	public void applySubsequentJobs() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);

		
		candidate.addAppliedJob(jobA);
		candidate.addAppliedJob(jobB);
		candidate.addAppliedJob(jobC);
		// candidateRepository.saveAndFlush(candidate);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));

		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		updatedCandidate.setAppliedJobs(candidate.getAppliedJobs());
		updatedCandidate.addAppliedJob(jobF);

		// updatedCandidate.setAppliedJobs(appliedJobUpdated);
		// update the Candidate
		restCandidateMockMvc.perform(put("/api/candidate-apply").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getAppliedJobs()).hasSize(4);
		assertThat(testCandidate.getAppliedJobs()).extracting("jobTitle").contains(JOB_A).contains(JOB_B)
				.contains(JOB_C).contains(JOB_F);

	}

	@Test
	@Transactional
	public void shortListCandidateForJobByCorporate() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		Corporate corporate = new Corporate();
		corporate.setName("Drishika");
		jobA.setNoOfApplicants(30);
		
		// candidateRepository.saveAndFlush(candidate);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		corporate.addJob(jobA);
		corporate.addJob(jobB);
		corporateRepository.saveAndFlush(corporate);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		// updatedCandidate.setJob(jobA);
		// updatedCandidate.setCorporate(corporate);
		// update the Candidate
		restCandidateMockMvc.perform(get("/api/candidate-corporate-link/{id1}/{id2}/{id3}", updatedCandidate.getId(),
				jobA.getId(), corporate.getId())).andExpect(status().isOk());

		// restCandidateMockMvc.perform(get("/api/candidates/{id}",
		// candidate.getId())).andExpect(status().isOk())
		List<Candidate> testCandidates = candidateRepository.findAll();
		Job testJob = jobRepository.findById(jobA.getId()).get();
		assertThat(testJob.getNoOfApplicantLeft()).isEqualTo(29);
		assertThat(testJob.getNoOfApplicants()).isEqualTo(30);
		assertThat(testJob.getNoOfApplicantsBought()).isEqualTo(1);
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getShortlistedByCorporates()).hasSize(1);
		assertThat(testCandidate.getShortlistedByCorporates())
				.extracting("id.jobId", "corporate.name", "candidate.firstName")
				.contains(tuple(jobA.getId(), "Drishika", "Abhinav"));

	}
	
	@Test
	@Transactional
	public void shortListCandidateForJobByCorporateWithAlreadyShortlistCandidateShouldIncrementAccordingly() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		Corporate corporate = new Corporate();
		corporate.setName("Drishika");
		jobA.setNoOfApplicants(30);
		jobA.setNoOfApplicantsBought(4);
		jobA.setNoOfApplicantLeft(26l);
	
		// candidateRepository.saveAndFlush(candidate);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		corporate.addJob(jobA);
		corporate.addJob(jobB);
		corporateRepository.saveAndFlush(corporate);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		// updatedCandidate.setJob(jobA);
		// updatedCandidate.setCorporate(corporate);
		// update the Candidate
		restCandidateMockMvc.perform(get("/api/candidate-corporate-link/{id1}/{id2}/{id3}", updatedCandidate.getId(),
				jobA.getId(), corporate.getId())).andExpect(status().isOk());

		// restCandidateMockMvc.perform(get("/api/candidates/{id}",
		// candidate.getId())).andExpect(status().isOk())
		List<Candidate> testCandidates = candidateRepository.findAll();
		Job testJob = jobRepository.findById(jobA.getId()).get();
		assertThat(testJob.getNoOfApplicantLeft()).isEqualTo(25);
		assertThat(testJob.getNoOfApplicants()).isEqualTo(30);
		assertThat(testJob.getNoOfApplicantsBought()).isEqualTo(5);
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getShortlistedByCorporates()).hasSize(1);
		assertThat(testCandidate.getShortlistedByCorporates())
				.extracting("id.jobId", "corporate.name", "candidate.firstName")
				.contains(tuple(jobA.getId(), "Drishika", "Abhinav"));

	}

	@Test
	@Transactional
	public void shortListCandidateForJobByCorporateWithAlreadyShortListedAvailableWithDifferentJobs() throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		Corporate corporate = new Corporate();
		corporate.setName("Drishika");
		jobRepository.saveAndFlush(jobA.noOfApplicants(30));
		jobRepository.saveAndFlush(jobB.noOfApplicants(30));
		jobRepository.saveAndFlush(jobC.noOfApplicants(30));
		jobRepository.saveAndFlush(jobF.noOfApplicants(30));
		jobRepository.saveAndFlush(jobG.noOfApplicants(30));
		
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		corporate.addJob(jobA);
		corporate.addJob(jobB);
		corporateRepository.saveAndFlush(corporate);
		CorporateCandidate corpCand1 = new CorporateCandidate(corporate, candidate, jobA.getId());
		CorporateCandidate corpCand2 = new CorporateCandidate(corporate, candidate, jobB.getId());
		candidate.addCorporateCandidate(corpCand2);
		candidate.addCorporateCandidate(corpCand1);
		candidateRepository.saveAndFlush(candidate);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate.getId());
		updatedCandidate.setFirstName(candidate.getFirstName());
		// updatedCandidate.setJob(jobC);
		// updatedCandidate.setCorporate(corporate);
		// update the Candidate
		restCandidateMockMvc.perform(get("/api/candidate-corporate-link/{id1}/{id2}/{id3}", updatedCandidate.getId(),
				jobC.getId(), corporate.getId())).andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Candidate testCandidate = testCandidates.get(0);
		assertThat(testCandidate.getShortlistedByCorporates()).hasSize(3);
		assertThat(testCandidate.getShortlistedByCorporates())
				.extracting("id.jobId", "corporate.name", "candidate.firstName")
				.contains(tuple(jobA.getId(), "Drishika", "Abhinav"))
				.contains(tuple(jobB.getId(), "Drishika", "Abhinav"))
				.contains(tuple(jobC.getId(), "Drishika", "Abhinav"));

	}

	@Test
	@Transactional
	public void shortListCandidateForJobByCorporateWithAlreadyShortListedAvailableWithSameJobDifferentCanddiates()
			throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav").gender(maleGender);
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8);
		Corporate corporate = new Corporate();
		corporate.setName("Drishika");
		jobRepository.saveAndFlush(jobA.noOfApplicants(30));
		jobRepository.saveAndFlush(jobB.noOfApplicants(30));
		jobRepository.saveAndFlush(jobC.noOfApplicants(30));
		jobRepository.saveAndFlush(jobF.noOfApplicants(30));
		jobRepository.saveAndFlush(jobG.noOfApplicants(30));
		
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		corporate.addJob(jobA);
		corporate.addJob(jobB);
		corporateRepository.saveAndFlush(corporate);
		CorporateCandidate corpCand1 = new CorporateCandidate(corporate, candidate, jobA.getId());
		CorporateCandidate corpCand2 = new CorporateCandidate(corporate, candidate, jobB.getId());
		candidate.addCorporateCandidate(corpCand2);
		candidate.addCorporateCandidate(corpCand1);
		candidateRepository.saveAndFlush(candidate);
		Candidate candidate2 = new Candidate().firstName("Aveer");
		candidateRepository.saveAndFlush(candidate2);
		Candidate updatedCandidate = new Candidate();
		updatedCandidate.setId(candidate2.getId());
		updatedCandidate.setFirstName(candidate2.getFirstName());
		// updatedCandidate.setJob(jobA);
		// updatedCandidate.setCorporate(corporate);
		// update the Candidate
		// restCandidateMockMvc.perform(put("/api/candidate-shortlist").contentType(TestUtil.APPLICATION_JSON_UTF8)
		// .content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

		restCandidateMockMvc.perform(get("/api/candidate-corporate-link/{id1}/{id2}/{id3}", updatedCandidate.getId(),
				jobA.getId(), corporate.getId())).andExpect(status().isOk());
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(2);

		/* WORST THING EVER.. NEED TO LEARN HOW TO WRITE THIS EXPRESSION */
		Candidate c1 = testCandidates.get(0);
		Candidate c2 = testCandidates.get(1);
		if (c1.getFirstName().equals("Aveer")) {
			assertThat(c1.getShortlistedByCorporates()).hasSize(1);
			assertThat(c1.getShortlistedByCorporates()).extracting("id.jobId", "corporate.name", "candidate.firstName")
					.contains(tuple(jobA.getId(), "Drishika", "Aveer"));

		} else if (c2.getFirstName().equals("Abhinav")) {
			assertThat(c2.getShortlistedByCorporates()).hasSize(2);
			assertThat(c2.getShortlistedByCorporates()).extracting("id.jobId", "corporate.name", "candidate.firstName")
					.contains(tuple(jobA.getId(), "Drishika", "Abhinav"))
					.contains(tuple(jobB.getId(), "Drishika", "Abhinav"));
		} else if (c2.getFirstName().equals("Aveer")) {
			assertThat(c2.getShortlistedByCorporates()).hasSize(1);
			assertThat(c2.getShortlistedByCorporates()).extracting("id.jobId", "corporate.name", "candidate.firstName")
					.contains(tuple(jobA.getId(), "Drishika", "Aveer"));

		} else if (c1.getFirstName().equals("Abhinav")) {
			assertThat(c1.getShortlistedByCorporates()).hasSize(2);
			assertThat(c1.getShortlistedByCorporates()).extracting("id.jobId", "corporate.name", "candidate.firstName")
					.contains(tuple(jobA.getId(), "Drishika", "Abhinav"))
					.contains(tuple(jobB.getId(), "Drishika", "Abhinav"));

		}

	}
	
	@Test
	@Transactional
	public void createCandidate() throws Exception {
		
		int databaseSizeBeforeCreate = candidateRepository.findAll().size();
		int profileSizeBeforeCreate = profileCategoryRepository.findAll().size();
		
		
	

		// Create the Candidate
		restCandidateMockMvc.perform(post("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidate))).andExpect(status().isCreated());

		// Validate the Candidate in the database
		List<Candidate> candidateList = candidateRepository.findAll();
		List<ProfileCategory> categoriesList = profileCategoryRepository.findAll();
		assertThat(categoriesList).hasSize(profileSizeBeforeCreate);
		assertThat(candidateList).hasSize(databaseSizeBeforeCreate + 1);
		Candidate testCandidate = candidateList.get(candidateList.size() - 1);
		assertThat(testCandidate.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
		assertThat(testCandidate.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
		assertThat(testCandidate.getProfileScores()).hasSize(1);
		assertThat(testCandidate.getProfileScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		// Validate the Filter in Elasticsearch
	}

	@Test
	@Transactional
	public void createCandidateWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateRepository.findAll().size();

		// Create the Candidate with an existing ID
		candidate.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateMockMvc.perform(post("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidate))).andExpect(status().isBadRequest());

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
		restCandidateMockMvc.perform(get("/api/candidates?sort=id,desc")).andExpect(status().isOk())
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
				.andExpect(jsonPath("$.[*].availableForHiring")
						.value(hasItem(DEFAULT_AVAILABLE_FOR_HIRING.booleanValue())))
				.andExpect(jsonPath("$.[*].openToRelocate").value(hasItem(DEFAULT_OPEN_TO_RELOCATE.booleanValue())));
	}

	@Test
	@Transactional
	public void getCandidate() throws Exception {
		// Initialize the database
		Address address = new Address();
		address.addressLineOne("AAAAA");
		address.addressLineTwo("BBBBB");
		address.city("AAAAA");
		Nationality nationality = new Nationality();
		nationalityRepository.saveAndFlush(nationality);
		Country country = new Country();
		country.visas(new HashSet<>());
		country.nationality(nationality);
		country.addAddress(address);
		countryRepository.saveAndFlush(country);
		candidate.addAddress(address);
		candidateRepository.saveAndFlush(candidate);
		
		
		// Get the candidate
		restCandidateMockMvc.perform(get("/api/candidates/{id}", candidate.getId())).andExpect(status().isOk())
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
	
	/*@Test
	@Transactional
	public void getCandidateByLoginIdWithEducationScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate.login(user));
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
		// Get the candidate
		restCandidateMockMvc.perform(get("/api/candidateByLogin/{id}", candidate.getLogin().getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
				.andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
				.andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
				.andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
				.andExpect(jsonPath("$.hasEducationScore").value(Boolean.TRUE))
				.andExpect(jsonPath("$.login.id").value(user.getId().intValue()));
				
	}
	*/
	@Test
	@Transactional
	public void getCandidateByLoginIdWithoutEducationScore() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate.login(user));
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,exp);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidateProfileScore3.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.addCandidateProfileScore(candidateProfileScore3);
		candidate.setProfileScore(60D);
		// Get the candidate
		restCandidateMockMvc.perform(get("/api/candidateByLogin/{id}", candidate.getLogin().getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
				.andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
				.andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
				.andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
				.andExpect(jsonPath("$.hasEducation").value(Boolean.FALSE))
				.andExpect(jsonPath("$.login.id").value(user.getId()));
				
	}
	
	@Test
	@Transactional
	public void getCandidatePublicProfileWhenShortlistedForJobsMustShowIndicatorShorListedOrNot() throws Exception {
		candidateRepository.saveAndFlush(candidate.login(user));
	/*	University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").display("Master");
		CandidateEducation candidateEducation = new CandidateEducation ();
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.candidate(candidate);
		CandidateProject project = new CandidateProject();
		project.contributionInProject("Hello");
		project.education(candidateEducation);
		CandidateEmployment candidateEmployment = new CandidateEmployment();
		candidateEmployment.employerName("AAAA").candidate(candidate);
		project.employment(candidateEmployment);
		Country country = new Country();
		country.countryName("Hello");
		Address address = new Address();
		address.addressLineOne("Hello");
		address.candidate(candidate);
		address.country(country);
		Nationality nationality = new Nationality();
		nationality.nationality("Hello");
		candidate.nationality(nationality);
		Set<CandidateEducation> educations = new HashSet<>();
		educations.add(candidateEducation);
		candidate.educations(educations);
		Set<CandidateEmployment> employments = new HashSet<>();
		employments.add(candidateEmployment);
		candidate.employments(employments);
		CandidateCertification cert = new CandidateCertification();
		cert.certificationTitle("Hello");
		Set<CandidateCertification> candidateCertifications = new HashSet<>();
		candidateCertifications.add(cert);
		candidate.certifications(candidateCertifications);*/
		corporate.addJob(jobA).addJob(jobB).addJob(jobC).addJob(jobF).addJob(jobG);
		CorporateCandidate cc1 = new CorporateCandidate(corporate,candidate,jobA.getId());
		CorporateCandidate cc2 = new CorporateCandidate(corporate,candidate,jobB.getId());
		CorporateCandidate cc3 = new CorporateCandidate(corporate,candidate,jobC.getId());
		CandidateJob cJ1 = new CandidateJob(candidate,jobA);
		CandidateJob cJ2= new CandidateJob(candidate,jobB);
		CandidateJob cJ3 = new CandidateJob(candidate,jobC);
		CandidateJob cJ4 = new CandidateJob(candidate,jobF);
		CandidateJob cJ5 = new CandidateJob(candidate,jobG);
		candidate.addCandidateJob(cJ5).addCandidateJob(cJ4).addCandidateJob(cJ3).addCandidateJob(cJ2).addCandidateJob(cJ1);
		corporate.addCorporateCandidate(cc1).addCorporateCandidate(cc2).addCorporateCandidate(cc3);
		candidate.addCorporateCandidate(cc3).addCorporateCandidate(cc2).addCorporateCandidate(cc1);
		//corporateRepository.saveAndFlush(corporate);
		//candidateRepository.saveAndFlush(candidate);
		restCandidateMockMvc.perform(get("/api/candidates/candidatePublicProfile/{candidateId}/{jobId}/{corporateId}", 
				candidate.getId(),jobF.getId(),corporate.getId())).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.candidateDetails.id").value(candidate.getId().intValue()))
		.andExpect(jsonPath("$.candidateDetails.firstName").value(DEFAULT_FIRST_NAME.toString()))
		.andExpect(jsonPath("$.candidateDetails.lastName").value(DEFAULT_LAST_NAME.toString()))
		.andExpect(jsonPath("$.candidateDetails.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
		.andExpect(jsonPath("$.isShortListed").value(Boolean.TRUE));
		
		
	}

	@Test
	@Transactional
	public void getNonExistingCandidate() throws Exception {
		// Get the candidate
		restCandidateMockMvc.perform(get("/api/candidates/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidatePersonalDetailsFromBasic() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
		CandidateProfileScore candidateProfileScore = new CandidateProfileScore(candidate,basic);
		candidateProfileScore.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore);
		candidate.setProfileScore(5D);
		candidateRepository.saveAndFlush(candidate);
		// Update the candidate
		Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).get();
		updatedCandidate.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).middleName(UPDATED_MIDDLE_NAME)
				.facebook(UPDATED_FACEBOOK).linkedIn(UPDATED_LINKED_IN).twitter(UPDATED_TWITTER)
				.aboutMe(UPDATED_ABOUT_ME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneCode(UPDATED_PHONE_CODE)
				.phoneNumber(UPDATED_PHONE_NUMBER).differentlyAbled(UPDATED_DIFFERENTLY_ABLED)
				.availableForHiring(UPDATED_AVAILABLE_FOR_HIRING).openToRelocate(UPDATED_OPEN_TO_RELOCATE);

		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

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
		assertThat(testCandidate.getProfileScore()).isEqualTo(20D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);

		// Validate the Candidate in Elasticsearch
		// Candidate candidateEs =
		// candidateSearchRepository.findOne(testCandidate.getId());
		// assertThat(candidateEs).isEqualToComparingFieldByField(testCandidate);
	}

	@Test
	@Transactional
	public void updateCandidateWithEducationAndCertProfileScores() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		int databaseSizeBeforeUpdate = candidateRepository.findAll().size();
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

		// Update the candidate
		Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).get();
		updatedCandidate.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).middleName(UPDATED_MIDDLE_NAME)
				.facebook(UPDATED_FACEBOOK).linkedIn(UPDATED_LINKED_IN).twitter(UPDATED_TWITTER)
				.aboutMe(UPDATED_ABOUT_ME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneCode(UPDATED_PHONE_CODE)
				.phoneNumber(UPDATED_PHONE_NUMBER).differentlyAbled(UPDATED_DIFFERENTLY_ABLED)
				.availableForHiring(UPDATED_AVAILABLE_FOR_HIRING).openToRelocate(UPDATED_OPEN_TO_RELOCATE);

		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());

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
		assertThat(testCandidate.getProfileScore()).isEqualTo(75D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);

		// Validate the Candidate in Elasticsearch
		// Candidate candidateEs =
		// candidateSearchRepository.findOne(testCandidate.getId());
		// assertThat(candidateEs).isEqualToComparingFieldByField(testCandidate);
	}

	@Test
	@Transactional
	public void updateNonExistingCandidate() throws Exception {
		int databaseSizeBeforeUpdate = candidateRepository.findAll().size();

		// Create the Candidate

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidate))).andExpect(status().isCreated());

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
		restCandidateMockMvc
				.perform(delete("/api/candidates/{id}", candidate.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		// boolean candidateExistsInEs =
		// candidateSearchRepository.exists(candidate.getId());
		// assertThat(candidateExistsInEs).isFalse();

		// Validate the database is empty
		List<Candidate> candidateList = candidateRepository.findAll();
		assertThat(candidateList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	@Ignore
	public void searchCandidate() throws Exception {
		// Initialize the database
		candidateRepository.saveAndFlush(candidate);
		candidateSearchRepository.save(candidate);

		// Search the candidate
		restCandidateMockMvc.perform(get("/api/_search/candidates?query=id:" + candidate.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
				.andExpect(jsonPath("$.[*].availableForHiring")
						.value(hasItem(DEFAULT_AVAILABLE_FOR_HIRING.booleanValue())))
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
	
	@Test
	@Transactional
	public void registerCandidateThenUpdateCandidateThenGetDetails() throws Exception {
		
		userRepository.saveAndFlush(user);
		candidateRepository.saveAndFlush(candidate.login(user));
		CandidateProfileScore score = new CandidateProfileScore(candidate,basic);
		candidate.addCandidateProfileScore(score);
		candidateRepository.saveAndFlush(candidate);
		Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).get();
		updatedCandidate.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).middleName(UPDATED_MIDDLE_NAME)
				.facebook(UPDATED_FACEBOOK).linkedIn(UPDATED_LINKED_IN).twitter(UPDATED_TWITTER)
				.aboutMe(UPDATED_ABOUT_ME).dateOfBirth(UPDATED_DATE_OF_BIRTH).phoneCode(UPDATED_PHONE_CODE)
				.phoneNumber(UPDATED_PHONE_NUMBER).differentlyAbled(UPDATED_DIFFERENTLY_ABLED)
				.availableForHiring(UPDATED_AVAILABLE_FOR_HIRING).openToRelocate(UPDATED_OPEN_TO_RELOCATE);
		System.out.println("---------------"+updatedCandidate.getProfileScores());;
		//updatedCandidate.addCandidateProfileScore(updatedCandidate.getProfileScores().iterator().next());

		restCandidateMockMvc.perform(put("/api/candidates").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedCandidate))).andExpect(status().isOk());
		
		restCandidateMockMvc.perform(get("/api/candidateById/{id}", candidate.getId())).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id").value(candidate.getId().intValue()))
		.andExpect(jsonPath("$.firstName").value(UPDATED_FIRST_NAME.toString()))
		.andExpect(jsonPath("$.lastName").value(UPDATED_LAST_NAME.toString()))
		.andExpect(jsonPath("$.middleName").value(UPDATED_MIDDLE_NAME.toString()))
		.andExpect(jsonPath("$.facebook").value(UPDATED_FACEBOOK.toString()))
		.andExpect(jsonPath("$.linkedIn").value(UPDATED_LINKED_IN.toString()))
		.andExpect(jsonPath("$.twitter").value(UPDATED_TWITTER.toString()))
		.andExpect(jsonPath("$.aboutMe").value(UPDATED_ABOUT_ME.toString()))
		.andExpect(jsonPath("$.dateOfBirth").value(UPDATED_DATE_OF_BIRTH.toString()))
		.andExpect(jsonPath("$.phoneCode").value(UPDATED_PHONE_CODE.toString()))
		.andExpect(jsonPath("$.phoneNumber").value(UPDATED_PHONE_NUMBER.toString()))
		.andExpect(jsonPath("$.differentlyAbled").value(UPDATED_DIFFERENTLY_ABLED.booleanValue()))
		.andExpect(jsonPath("$.availableForHiring").value(UPDATED_AVAILABLE_FOR_HIRING.booleanValue()))
		.andExpect(jsonPath("$.openToRelocate").value(UPDATED_OPEN_TO_RELOCATE.booleanValue()));
	//	.andExpect(jsonPath("$.hasEducation").value(Boolean.FALSE));
		
	}
	

}
