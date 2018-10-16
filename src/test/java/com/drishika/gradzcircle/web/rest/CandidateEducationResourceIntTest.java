
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

import org.apache.commons.beanutils.BeanUtils;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
import com.drishika.gradzcircle.service.CandidateEducationService;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CandidateEducationResource REST controller.
 *
 * @see CandidateEducationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateEducationResourceIntTest {

	private static final Double DEFAULT_GRADE = 1D;
	private static final Double UPDATED_GRADE = 2D;

	private static final LocalDate DEFAULT_EDUCATION_FROM_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EDUCATION_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final LocalDate DEFAULT_EDUCATION_TO_DATE = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_EDUCATION_TO_DATE = LocalDate.now(ZoneId.systemDefault());

	private static final Boolean DEFAULT_IS_PURSUING_EDUCATION = false;
	private static final Boolean UPDATED_IS_PURSUING_EDUCATION = true;

	private static final Integer DEFAULT_GRADE_SCALE = 1;
	private static final Integer UPDATED_GRADE_SCALE = 2;

	private static final Boolean DEFAULT_HIGHEST_QUALIFICATION = false;
	private static final Boolean UPDATED_HIGHEST_QUALIFICATION = true;

	private static final Integer DEFAULT_ROUND_OF_GRADE = 1;
	private static final Integer UPDATED_ROUND_OF_GRADE = 2;

	private static final Integer DEFAULT_GRADE_DECIMAL = 1;
	private static final Integer UPDATED_GRADE_DECIMAL = 2;

	private static final String DEFAULT_CAPTURED_COURSE = "AAAAAAAAAA";
	private static final String UPDATED_CAPTURED_COURSE = "BBBBBBBBBB";

	private static final String DEFAULT_CAPTURED_QUALIFICATION = "AAAAAAAAAA";
	private static final String UPDATED_CAPTURED_QUALIFICATION = "BBBBBBBBBB";

	private static final String DEFAULT_CAPTURED_COLLEGE = "AAAAAAAAAA";
	private static final String UPDATED_CAPTURED_COLLEGE = "BBBBBBBBBB";

	private static final String DEFAULT_CAPTURED_UNIVERSITY = "AAAAAAAAAA";
	private static final String UPDATED_CAPTURED_UNIVERSITY = "BBBBBBBBBB";

	private static final Double DEFAULT_PERCENTAGE = 1D;
	private static final Double UPDATED_PERCENTAGE = 2D;

	private static final String DEFAULT_SCORE_TYPE = "AAAAAAAAAA";
	private static final String UPDATED_SCORE_TYPE = "gpa";

	private static final Integer DEFAULT_EDUCATION_DURATION = 1;
	private static final Integer UPDATED_EDUCATION_DURATION = 2;

	private static final String CANDIDATE_A = "Candidate A";
	private static final String JOB_A = "JOB_A";
	private static final String JOB_B = "JOB_B";
	private static final String JOB_C = "JOB_C";
	private static final String JOB_D = "JOB_D";
	private static final String JOB_E = "JOB_E";
	private static final String JOB_F = "JOB_F";
	private static final String JOB_G = "JOB_G";
	private static final String JOB_H = "JOB_H";
	private final static String ISC = "ISC";
	private final static String ICSE = "ICSE";
	private final static String DIPLOMA = "Diploma";
	private final static String MASTERS = "MASTERS";
	private final static String PG_DIPLOMA = "PG. Diploma";
	private final static String BACHELOR = "BACHELORS";
	private final static String StTHOMAS = "St. THOMAS";
	private static final String DOON = "Doon";
	private final static String BUSINESS_ADM = "Bus. Adm.";
	private final static String IIM = "IIM";
	private final static String MEDICAL = "MEDICAL";
	private final static String HINDI = "Hindi";
	private final static String ENGLISH = "English";
	private final static String MARATHI = "Marathi";
	private final static String MALE = "MALE";
	private final static String FEMALE = "FEMALE";
	private final static String MUMBAI_UNIVERSITY = "UNIVERSITY OF MUMBAI";

	// private static College collegeIIM, collegeMIRANDAHOUSE, collegeAMITY,
	// collegeA, collegeB, collegeABC, collegeStTHOMAS;
	private University uniIIM, uniDoon;
	private Course courseBUSINESS_ADM, courseICSE, courseISC, courseMedical;
	private Qualification qualPG_DIPLOMA, qualISC, qualICSE, qualDIPLOMA, qualBachelor, qualMaster;
	private Language hindiLanguage, englishLanguage, marathiLanguage;
	private Gender maleGender, femaleGender;

	@Autowired
	private CandidateEducationRepository candidateEducationRepository;

	@Autowired
	private CandidateEducationSearchRepository candidateEducationSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateEducationMockMvc;

	private CandidateEducation candidateEducation;

	private Course course;
	private Qualification qualification;
	private College college;
	private University university, mumbaiUni;
	private Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
			universityFilter, qualificationFilter;

	private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH;

	@Autowired
	private CandidateEducationService candidateEducationService;

	@Autowired
	private CandidateLanguageProficiencyRepository candidateLanguageRepository;

	@Autowired
	private CollegeRepository collegeRepository;

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private QualificationRepository qualificationRepository;

	@Autowired
	private QualificationSearchRepository qualificationSeacrhRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CandidateEducationResource candidateEducationResource = new CandidateEducationResource(
				candidateEducationService);
		this.restCandidateEducationMockMvc = MockMvcBuilders.standaloneSetup(candidateEducationResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static CandidateEducation createEntity(EntityManager em) {

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);
		return candidateEducation;
	}

	public static Course createCourse(EntityManager em) {
		Course course = new Course().course("Computer").display("Computer").value("Computer");
		return course;
	}

	public static Qualification createQualification(EntityManager em) {
		Qualification qualification = new Qualification().qualification("Master").display("Master").value("Master");
		return qualification;
	}

	public static College createCollege(EntityManager em) {
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda");
		return college;
	}

	public static University createUniversity(EntityManager em) {
		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		return university;
	}

	public static Course createBusAdmCourse(EntityManager em) {
		return new Course().course(BUSINESS_ADM);
	}

	public static Course createMedicalCourse(EntityManager em) {
		return new Course().course(MEDICAL);
	}

	public static Course createISCCourse(EntityManager em) {
		return new Course().course(ISC);
	}

	public static Course createICSECourse(EntityManager em) {
		return new Course().course(ICSE);
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

	public static University createIIMUniversity(EntityManager em) {
		University university = new University().universityName(IIM);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(IIM).university(university));
		return university.colleges(set);
	}

	public static University createMumbaiUniversity(EntityManager em) {
		University mumbaiUni = new University().universityName(MUMBAI_UNIVERSITY);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName("XYZ").university(mumbaiUni));
		return mumbaiUni.colleges(set);
	}

	public static University createUniOfDoon(EntityManager em) {
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(StTHOMAS));
		return new University().universityName(DOON).colleges(set);
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

	@Before
	public void initTest() {
		candidateEducationRepository.deleteAll();
		/*
		 * universityRepository.deleteAll(); qualificationRepository.deleteAll();
		 * courseRepository.deleteAll(); jobRepository.deleteAll();
		 * filterRepository.deleteAll(); genderRepository.deleteAll();
		 */
		university = createUniversity(em);
		college = createCollege(em);
		college.setUniversity(university);
		qualification = createQualification(em);
		course = createCourse(em);
		candidateEducation = createEntity(em);
		candidateEducation.setCourse(course);
		candidateEducation.setCollege(college);
		candidateEducation.setQualification(qualification);
		uniDoon = createUniOfDoon(em);
		mumbaiUni = createMumbaiUniversity(em);
		uniIIM = createIIMUniversity(em);
		qualICSE = createICSEaQualification(em);
		qualISC = createISCQualification(em);
		qualPG_DIPLOMA = createPGDiplomaQualification(em);
		qualDIPLOMA = createDiplomaQualification(em);
		qualBachelor = createBacehlorQualification(em);
		qualMaster = createMasterQualification(em);
		courseICSE = createICSECourse(em);
		courseISC = createISCCourse(em);
		courseBUSINESS_ADM = createBusAdmCourse(em);
		courseMedical = createMedicalCourse(em);
		qualificationFilter = createQualificationFilter(em);
		courseFilter = createCourseFilter(em);
		collegeFilter = createCollegeFilter(em);
		universityFilter = createUniversityFilter(em);
		gradDateFilter = createGradDateFilter(em);
		scoreFilter = createScoreFilter(em);
		languageFilter = createLanguagefilter(em);
		genderFilter = createGenderFilter(em);
		hindiLanguage = createHindiLanguae(em);
		englishLanguage = createEnglishLanguage(em);
		marathiLanguage = createMarathiLanguage(em);
		maleGender = createMaleGender(em);
		femaleGender = createFemaleGender(em);

		jobA = createJobA(em);
		jobB = createJobB(em);
		jobC = createJobC(em);
		// jobD = createJobD(em);
		// jobE = createJobE(em);
		jobF = createJobF(em);
		jobG = createJobG(em);

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
		Job jobE = new Job().jobTitle(JOB_C).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\":2017,\"month\": 7,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobE);
		jobFilters.add(jobFilter);
		jobE.setJobFilters(jobFilters);
		return jobE;

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

	public static Qualification createPGDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(PG_DIPLOMA).weightage(4L);
	}

	public static Qualification createISCQualification(EntityManager em) {
		return new Qualification().qualification(ISC).weightage(0L);
	}

	public static Qualification createICSEaQualification(EntityManager em) {
		return new Qualification().qualification(ICSE).weightage(0L);
	}

	public static Qualification createDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(DIPLOMA).weightage(1L);
	}

	public static Qualification createBacehlorQualification(EntityManager em) {
		return new Qualification().qualification(BACHELOR).weightage(2L);
	}

	public static Qualification createMasterQualification(EntityManager em) {
		return new Qualification().qualification(MASTERS).weightage(3L);
	}

	@Test
	@Transactional
	public void getCollegeByName() throws Exception {
		College college = new College();
		// college.setId(11L);
		college.setCollegeName("AAAAAA");
		collegeRepository.saveAndFlush(college);
		assertThat("AAAAAA").isEqualTo(collegeRepository.findByCollegeName("AAAAAA").getCollegeName());
		// assertThat(11L).isEqualTo(collegeRepository.findByCollegeName("AAAAAA").getId());

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createCandidateAEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
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
		// Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).candidate(candidateA);

		// candidateEducationService.createCandidateEducation(education);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education)))
				.andExpect(status().isCreated());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations.get(0).getHighestQualification()).isEqualTo(true);
		assertThat(testCandidateEducations.get(0).getCandidate()).isEqualTo(candidateA);
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidates.get(0).getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 36.0, 16.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 34.0, 15.0, null, null, 44.0, CANDIDATE_A));
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createAddtionalCandidateAEducationWhenOneAlreadySavedAndHasLanaguageAndGenderScore_TheMatchAndEducationScoreShudUpdateWithLanguageAndGenderScoreCarriedOver()
			throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
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
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24));
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobB);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		CandidateJob candidateJob2 = new CandidateJob(candidateA, jobF);
		candidateJob2.setMatchScore(47.0);
		candidateJob2.setEducationMatchScore(38.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setTotalEligibleScore(100.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateA.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidateA);
		candidateEducationRepository.saveAndFlush(education1.candidate(candidateA));
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		CandidateEducation education3 = new CandidateEducation().qualification(qualPG_DIPLOMA)
				.course(courseBUSINESS_ADM).percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2018, 03, 24))
				.candidate(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isCreated());
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, DIPLOMA, CANDIDATE_A)).contains(tuple(false, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 53.0, 17.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 36.0, 16.0, null, null, 44.0, CANDIDATE_A));
		// .contains(tuple(JOB_F,47.0,38.0,4.0,5.0,100.0,CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void updateAnyFieldApartFromToDateUpdatingCandidateAEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
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
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobB);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setTotalEligibleScore(45.0);
		CandidateJob candidateJob2 = new CandidateJob(candidateA, jobF);
		candidateJob2.setMatchScore(47.0);
		candidateJob2.setEducationMatchScore(38.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setTotalEligibleScore(100.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateA.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education2);
		CandidateEducation updatedEducation2 = new CandidateEducation();
		BeanUtils.copyProperties(updatedEducation2, education2);
		updatedEducation2.setCourse(courseMedical);

		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedEducation2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, DIPLOMA, CANDIDATE_A)).contains(tuple(false, ISC, CANDIDATE_A));
		assertThat(testCandidates.get(0).getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 53.0, 17.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 59.0, 26.0, null, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void updateCandidateAEducationSuchThatMatchScoreGoesToLeast() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
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
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education2);
		CandidateEducation updatedEducation2 = new CandidateEducation();
		BeanUtils.copyProperties(updatedEducation2, education2);
		updatedEducation2.setCourse(course);
		updatedEducation2.setPercentage(10.0);
		updatedEducation2.setQualification(qualICSE);
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobB);
		candidateJob1.setMatchScore(100D);
		candidateJob1.setEducationMatchScore(30d);
		candidateJob1.setLanguageMatchScore(null);
		candidateJob1.setGenderMatchScore(null);
		CandidateJob candidateJob2 = new CandidateJob(candidateA, jobG);
		candidateJob2.setMatchScore(19D);
		candidateJob2.setEducationMatchScore(20d);
		candidateJob2.setLanguageMatchScore(2d);
		candidateJob2.setGenderMatchScore(3d);
		candidateA.addCandidateJob(candidateJob1).addCandidateJob(candidateJob2);

		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedEducation2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, ICSE, CANDIDATE_A)).contains(tuple(false, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 18.0, 8.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 27.0, 7.0, 3.0, 2.0, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	@Ignore
	public void updateToDateToforceChangeInHighestEducationPostCreaingAddtionalCandidateAEducationWithLanguageAndGender()
			throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
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
		genderRepository.saveAndFlush(maleGender);
		genderRepository.saveAndFlush(femaleGender);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);

		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA).highestQualification(true);
		candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA).highestQualification(null);
		candidateEducationRepository.saveAndFlush(education2);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateLanguageRepository.saveAndFlush(languageProficiency1);
		candidateLanguageRepository.saveAndFlush(languageProficiency2);
		CandidateEducation updatedEducation2 = new CandidateEducation();
		BeanUtils.copyProperties(updatedEducation2, education2);
		updatedEducation2.setCourse(courseMedical);
		updatedEducation2.setEducationToDate(LocalDate.of(2018, 03, 11));

		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedEducation2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		// assertThat(testCandidateEducations).extracting("course.course","highestQualification").contains(tuple(MEDICAL,true)).contains(tuple(ISC,false)).contains(tuple(BUSINESS_ADM,null));
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, DIPLOMA, CANDIDATE_A)).contains(tuple(false, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 44.0, 17.0, 0.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 70.0, 27.0, 4.0, 0.0, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteHighestEducationPostCreatingAddtionalCandidateAEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(course);
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
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobB);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		CandidateJob candidateJob2 = new CandidateJob(candidateA, jobF);
		candidateJob2.setMatchScore(47.0);
		candidateJob2.setEducationMatchScore(38.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setTotalEligibleScore(100.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateA.getCandidateJobs().addAll(candidateJobs);
		candidateRepository.saveAndFlush(candidateA);
		Thread.sleep(10000);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(68d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.highestQualification(null);
		// candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.highestQualification(null);
		// candidateEducationRepository.saveAndFlush(education2);
		CandidateEducation education3 = new CandidateEducation().qualification(qualMaster).course(course)
				.percentage(68d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2018, 02, 24))
				.highestQualification(true);
		candidateA.addEducation(education3).addEducation(education2).addEducation(education1);
		// candidateEducationRepository.saveAndFlush(education3);
		candidateRepository.saveAndFlush(candidateA);

		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}",
				candidateA.getEducations().stream().filter(education -> education.isHighestQualification() != null)
						.filter(education -> education.isHighestQualification()).findAny().get().getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(null, DIPLOMA, CANDIDATE_A)).contains(tuple(true, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 33.0, 8.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 30.0, 13.0, null, null, 44.0, CANDIDATE_A));
		boolean candidateEducationExistsInEs = candidateEducationSearchRepository.exists(education3.getId());
		assertThat(candidateEducationExistsInEs).isFalse();

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteTheOnlyEducationRecordWithAlreadySavedLanguageAndGender() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
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
		// Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobB);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		candidateJob1.setTotalEligibleScore(45.0);
		CandidateJob candidateJob2 = new CandidateJob(candidateA, jobG);
		candidateJob2.setMatchScore(47.0);
		candidateJob2.setEducationMatchScore(38.0);
		candidateJob2.setLanguageMatchScore(5.0);
		candidateJob2.setGenderMatchScore(4.0);
		candidateJob2.setTotalEligibleScore(100.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob2);
		candidateJobs.add(candidateJob1);
		candidateA.getCandidateJobs().addAll(candidateJobs);

		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA.addEducation(education));
		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", candidateA.getEducations().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducationsAgain = candidateEducationRepository.findAll();
		assertThat(testCandidateEducationsAgain).hasSize(0);
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(0);
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void deleteTheOnlyEducationRecordWithNoSavedLanguageAndGender() throws Exception {

	}

	@Test
	@Transactional
	@Ignore
	public void updateCandidateAEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
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
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.save(candidateA);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2007, 02, 24)).candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education);

		CandidateEducation newCandidateEducation = new CandidateEducation().qualification(qualPG_DIPLOMA)
				.course(courseBUSINESS_ADM).percentage(80d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2007, 03, 24))
				.candidate(candidateA);

		candidateEducationService.updateCandidateEductaion(newCandidateEducation);

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations)
				.extracting("qualification.Qualification", "highestQualification", "candidate.firstName")
				.contains(tuple(ISC, false, CANDIDATE_A)).contains(tuple(PG_DIPLOMA, true, CANDIDATE_A));
		// assertThat(testCandidateEducations.get(0).getCandidate()).isEqualTo(candidateA);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(5);

	}

	@Test
	@Transactional
	public void createCandidateEducation() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

		university = universityRepository.saveAndFlush(university);
		college = collegeRepository.saveAndFlush(college);
		qualification = qualificationRepository.saveAndFlush(qualification);
		course = courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");

		candidateEducation.college(college).course(course).qualification(qualification).candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createCandidateEducationWithPreviouslySavedEducationMarkingCurrentAsHighestBasedOnEducationToDate()
			throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(UPDATED_EDUCATION_TO_DATE);
		// Create the CandidateEducation
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(newEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(2);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation prevEducation = candidateEducationList.get(0);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		assertThat(prevEducation.getHighestQualification()).isEqualTo(false);
		// Validate the CandidateEducation in Elasticsearch
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createCandidateEducationWithPreviouslySavedEducationMarkingCurrentAsHighestBasedOnIsPursuing()
			throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).isPursuingEducation(UPDATED_IS_PURSUING_EDUCATION)
				.gradeScale(DEFAULT_GRADE_SCALE).highestQualification(UPDATED_HIGHEST_QUALIFICATION)
				.roundOfGrade(DEFAULT_ROUND_OF_GRADE).gradeDecimal(DEFAULT_GRADE_DECIMAL)
				.capturedCourse(DEFAULT_CAPTURED_COURSE).capturedQualification(DEFAULT_CAPTURED_QUALIFICATION)
				.capturedCollege(DEFAULT_CAPTURED_COLLEGE).capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY)
				.percentage(DEFAULT_PERCENTAGE).scoreType(DEFAULT_SCORE_TYPE)
				.educationDuration(DEFAULT_EDUCATION_DURATION);

		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(null);
		newEducation.setIsPursuingEducation(true);
		// Create the CandidateEducation
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(newEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository
				.findByOrderByEducationToDateDesc();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(2);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation prevEducation = candidateEducationList.get(0);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(prevEducation.getHighestQualification()).isEqualTo(DEFAULT_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		// assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(null);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(UPDATED_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		assertThat(prevEducation.getHighestQualification()).isEqualTo(false);
		// Validate the CandidateEducation in Elasticsearch
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createEducationWithOtherQualificationOnlyWithExistingQualification() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Other").qualification("Other").value("Other");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedQualification("Master").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}

	@Test
	@Transactional
	public void createEducationWithOtherQualificationOnlyWithNonExistingQualification() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Other").qualification("Other").value("Other");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedQualification("Bachelor").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(2);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("Bachelor");
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Bachelor");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Bachelor");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Bachelor");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createEducationWithOtherCourseOnlyWithExistingCourse() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate();
		candidateRepository.saveAndFlush(candidate);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Other").value("Other").display("Other");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCourse("Computer").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createEducationWithOtherCourseOnlyWithNonExistingCourse() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Other").value("Other").display("Other");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCourse("Pharma").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(2);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo("Pharma");
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Pharma");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Pharma");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Pharma");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	@Ignore
	// IGNORING AS IF WE HAVE A COLLEGE IT WOULD ALWAYS HAVE A UNIVERSITY ASSOCIATED
	// AS UNIVERSITY IS PARENT

	public void createEducationWithOtherCollegeOnlyWithExistingCollegeAndNotExistingUniversity() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Other").value("Other").display("Other");
		College college = new College().collegeName("Other").display("Other").value("Other").university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCollege("Miranda").capturedUniversity("Mumbai");

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(2);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);

		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(DEFAULT_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void createEducationWithOtherCollegeOnlyWithExistingUniversityAndNotExistingCollege() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Other").value("Other").display("Other");
		College college = new College().collegeName("Other").display("Other").value("Other").university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCollege("IIT").capturedUniversity("Delhi").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(2);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}

	@Test
	@Transactional
	public void createEducationWithOtherCollegeOnlyWithNotExistingUniversityAndNotExistingCollege() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Other").value("Other").display("Other");
		College college = new College().collegeName("Other").display("Other").value("Other").university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCollege("IIT").capturedUniversity("Mumbai").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(2);
		assertThat(universities).hasSize(2);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}

	@Test
	@Transactional
	public void createEducationWithOtherCollegeOnlyWithExistingUniversityAndExistingCollege() throws Exception {

		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);

		University university = new University().universityName("Other").value("Other").display("Other");
		College college = new College().collegeName("Other").display("Other").value("Other").university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master");
		candidateEducation = candidateEducation.college(college).course(course).qualification(qualification)
				.capturedCollege("Miranda").capturedUniversity("Delhi").candidate(candidate);

		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(candidateEducationList).hasSize(1);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(DEFAULT_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Master");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo("Miranda");
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}

	@Test
	@Transactional
	public void createCandidateEducationWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

		// Create the CandidateEducation with an existing ID
		candidateEducation.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isBadRequest());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllCandidateEducations() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);

		// Get all the candidateEducationList
		restCandidateEducationMockMvc.perform(get("/api/candidate-educations?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEducation.getId().intValue())))
				.andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())))
				.andExpect(jsonPath("$.[*].educationFromDate").value(hasItem(DEFAULT_EDUCATION_FROM_DATE.toString())))
				.andExpect(jsonPath("$.[*].educationToDate").value(hasItem(DEFAULT_EDUCATION_TO_DATE.toString())))
				.andExpect(jsonPath("$.[*].isPursuingEducation")
						.value(hasItem(DEFAULT_IS_PURSUING_EDUCATION.booleanValue())))
				.andExpect(jsonPath("$.[*].gradeScale").value(hasItem(DEFAULT_GRADE_SCALE)))
				.andExpect(jsonPath("$.[*].highestQualification")
						.value(hasItem(DEFAULT_HIGHEST_QUALIFICATION.booleanValue())))
				.andExpect(jsonPath("$.[*].roundOfGrade").value(hasItem(DEFAULT_ROUND_OF_GRADE)))
				.andExpect(jsonPath("$.[*].gradeDecimal").value(hasItem(DEFAULT_GRADE_DECIMAL)))
				.andExpect(jsonPath("$.[*].capturedCourse").value(hasItem(DEFAULT_CAPTURED_COURSE.toString())))
				.andExpect(jsonPath("$.[*].capturedQualification")
						.value(hasItem(DEFAULT_CAPTURED_QUALIFICATION.toString())))
				.andExpect(jsonPath("$.[*].capturedCollege").value(hasItem(DEFAULT_CAPTURED_COLLEGE.toString())))
				.andExpect(jsonPath("$.[*].capturedUniversity").value(hasItem(DEFAULT_CAPTURED_UNIVERSITY.toString())))
				.andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
				.andExpect(jsonPath("$.[*].scoreType").value(hasItem(DEFAULT_SCORE_TYPE.toString())))
				.andExpect(jsonPath("$.[*].educationDuration").value(hasItem(DEFAULT_EDUCATION_DURATION)));
	}

	@Test
	@Transactional
	public void getCandidateEducation() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		System.out.println("Id is " + candidateEducation.getId());

		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(get("/api/candidate-educations/{id}", candidateEducation.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(candidateEducation.getId().intValue()))
				.andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.doubleValue()))
				.andExpect(jsonPath("$.educationFromDate").value(DEFAULT_EDUCATION_FROM_DATE.toString()))
				.andExpect(jsonPath("$.educationToDate").value(DEFAULT_EDUCATION_TO_DATE.toString()))
				.andExpect(jsonPath("$.isPursuingEducation").value(DEFAULT_IS_PURSUING_EDUCATION.booleanValue()))
				.andExpect(jsonPath("$.gradeScale").value(DEFAULT_GRADE_SCALE))
				.andExpect(jsonPath("$.highestQualification").value(DEFAULT_HIGHEST_QUALIFICATION.booleanValue()))
				.andExpect(jsonPath("$.roundOfGrade").value(DEFAULT_ROUND_OF_GRADE))
				.andExpect(jsonPath("$.gradeDecimal").value(DEFAULT_GRADE_DECIMAL))
				.andExpect(jsonPath("$.capturedCourse").value(DEFAULT_CAPTURED_COURSE.toString()))
				.andExpect(jsonPath("$.capturedQualification").value(DEFAULT_CAPTURED_QUALIFICATION.toString()))
				.andExpect(jsonPath("$.capturedCollege").value(DEFAULT_CAPTURED_COLLEGE.toString()))
				.andExpect(jsonPath("$.capturedUniversity").value(DEFAULT_CAPTURED_UNIVERSITY.toString()))
				.andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
				.andExpect(jsonPath("$.scoreType").value(DEFAULT_SCORE_TYPE.toString()))
				.andExpect(jsonPath("$.educationDuration").value(DEFAULT_EDUCATION_DURATION));
	}

	@Test
	@Transactional
	public void getNonExistingCandidateEducation() throws Exception {
		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(get("/api/candidate-educations/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCandidateEducation() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate();
		candidateRepository.saveAndFlush(candidate);
		candidateEducationRepository.saveAndFlush(candidateEducation.candidate(candidate));
		// candidateEducationSearchRepository.save(candidateEducation);

		int databaseSizeBeforeUpdate = candidateEducationRepository.findAll().size();

		// Update the candidateEducation
		CandidateEducation updatedCandidateEducation = candidateEducationRepository.findOne(candidateEducation.getId());
		updatedCandidateEducation.grade(UPDATED_GRADE).educationFromDate(UPDATED_EDUCATION_FROM_DATE)
				.educationToDate(UPDATED_EDUCATION_TO_DATE).isPursuingEducation(UPDATED_IS_PURSUING_EDUCATION)
				.gradeScale(UPDATED_GRADE_SCALE).highestQualification(UPDATED_HIGHEST_QUALIFICATION)
				.roundOfGrade(UPDATED_ROUND_OF_GRADE).gradeDecimal(UPDATED_GRADE_DECIMAL)
				.capturedCourse(UPDATED_CAPTURED_COURSE).capturedQualification(UPDATED_CAPTURED_QUALIFICATION)
				.capturedCollege(UPDATED_CAPTURED_COLLEGE).capturedUniversity(UPDATED_CAPTURED_UNIVERSITY)
				.percentage(UPDATED_PERCENTAGE).scoreType(UPDATED_SCORE_TYPE)
				.educationDuration(UPDATED_EDUCATION_DURATION).candidate(candidate);

		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateEducation)))
				.andExpect(status().isOk());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeUpdate);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(2.2);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(UPDATED_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(UPDATED_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(UPDATED_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(UPDATED_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(UPDATED_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(UPDATED_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(UPDATED_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(UPDATED_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(UPDATED_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(UPDATED_EDUCATION_DURATION);

		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional
	public void updateCandidateEducationWithUserOverride() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate();
		candidateRepository.saveAndFlush(candidate);
		candidateEducationRepository
				.saveAndFlush(candidateEducation.candidate(candidate).educationToDate(UPDATED_EDUCATION_TO_DATE));
		// candidateEducationSearchRepository.save(candidateEducation);

		int databaseSizeBeforeUpdate = candidateEducationRepository.findAll().size();

		// Update the candidateEducation
		CandidateEducation updatedCandidateEducation = candidateEducationRepository.findOne(candidateEducation.getId());
		updatedCandidateEducation.grade(UPDATED_GRADE).educationFromDate(UPDATED_EDUCATION_FROM_DATE)
				.educationToDate(DEFAULT_EDUCATION_TO_DATE).isPursuingEducation(UPDATED_IS_PURSUING_EDUCATION)
				.gradeScale(UPDATED_GRADE_SCALE).highestQualification(UPDATED_HIGHEST_QUALIFICATION)
				.roundOfGrade(UPDATED_ROUND_OF_GRADE).gradeDecimal(UPDATED_GRADE_DECIMAL)
				.capturedCourse(UPDATED_CAPTURED_COURSE).capturedQualification(UPDATED_CAPTURED_QUALIFICATION)
				.capturedCollege(UPDATED_CAPTURED_COLLEGE).capturedUniversity(UPDATED_CAPTURED_UNIVERSITY)
				.percentage(UPDATED_PERCENTAGE).scoreType(UPDATED_SCORE_TYPE)
				.educationDuration(UPDATED_EDUCATION_DURATION).candidate(candidate);

		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updatedCandidateEducation)))
				.andExpect(status().isOk());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeUpdate);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(2.2);
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(UPDATED_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(UPDATED_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(UPDATED_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(UPDATED_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(UPDATED_CAPTURED_COURSE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo(UPDATED_CAPTURED_QUALIFICATION);
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(UPDATED_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(UPDATED_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(UPDATED_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(UPDATED_EDUCATION_DURATION);

	}

	@Test
	@Transactional
	public void updateNonExistingCandidateEducation() throws Exception {
		int databaseSizeBeforeUpdate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		Candidate candidate = new Candidate();
		candidateRepository.saveAndFlush(candidate);
		candidateEducation.candidate(candidate);

		// Create the CandidateEducation

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restCandidateEducationMockMvc
				.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	@Ignore
	public void deleteCandidateEducation() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		candidateEducationSearchRepository.save(candidateEducation);
		int databaseSizeBeforeDelete = candidateEducationRepository.findAll().size();

		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", candidateEducation.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate Elasticsearch is empty
		boolean candidateEducationExistsInEs = candidateEducationSearchRepository.exists(candidateEducation.getId());
		assertThat(candidateEducationExistsInEs).isFalse();

		// Validate the database is empty
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchCandidateEducation() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		candidateEducationSearchRepository.save(candidateEducation);

		// Search the candidateEducation
		restCandidateEducationMockMvc
				.perform(get("/api/_search/candidate-educations?query=id:" + candidateEducation.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(candidateEducation.getId().intValue())))
				.andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())))
				.andExpect(jsonPath("$.[*].educationFromDate").value(hasItem(DEFAULT_EDUCATION_FROM_DATE.toString())))
				.andExpect(jsonPath("$.[*].educationToDate").value(hasItem(DEFAULT_EDUCATION_TO_DATE.toString())))
				.andExpect(jsonPath("$.[*].isPursuingEducation")
						.value(hasItem(DEFAULT_IS_PURSUING_EDUCATION.booleanValue())))
				.andExpect(jsonPath("$.[*].gradeScale").value(hasItem(DEFAULT_GRADE_SCALE)))
				.andExpect(jsonPath("$.[*].highestQualification")
						.value(hasItem(DEFAULT_HIGHEST_QUALIFICATION.booleanValue())))
				.andExpect(jsonPath("$.[*].roundOfGrade").value(hasItem(DEFAULT_ROUND_OF_GRADE)))
				.andExpect(jsonPath("$.[*].gradeDecimal").value(hasItem(DEFAULT_GRADE_DECIMAL)))
				.andExpect(jsonPath("$.[*].capturedCourse").value(hasItem(DEFAULT_CAPTURED_COURSE.toString())))
				.andExpect(jsonPath("$.[*].capturedQualification")
						.value(hasItem(DEFAULT_CAPTURED_QUALIFICATION.toString())))
				.andExpect(jsonPath("$.[*].capturedCollege").value(hasItem(DEFAULT_CAPTURED_COLLEGE.toString())))
				.andExpect(jsonPath("$.[*].capturedUniversity").value(hasItem(DEFAULT_CAPTURED_UNIVERSITY.toString())))
				.andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
				.andExpect(jsonPath("$.[*].scoreType").value(hasItem(DEFAULT_SCORE_TYPE.toString())))
				.andExpect(jsonPath("$.[*].educationDuration").value(hasItem(DEFAULT_EDUCATION_DURATION)));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CandidateEducation.class);
		CandidateEducation candidateEducation1 = new CandidateEducation();
		candidateEducation1.setId(1L);
		CandidateEducation candidateEducation2 = new CandidateEducation();
		candidateEducation2.setId(candidateEducation1.getId());
		assertThat(candidateEducation1).isEqualTo(candidateEducation2);
		candidateEducation2.setId(2L);
		assertThat(candidateEducation1).isNotEqualTo(candidateEducation2);
		candidateEducation1.setId(null);
		assertThat(candidateEducation1).isNotEqualTo(candidateEducation2);
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createOnlyLanguageThenNewEducationAndNoPreviousMatchSet() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
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
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);

		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 42.0, 16.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 34.0, 15.0, null, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createOnlyGenderThenNewEducationAndNoPreviousMatchSet() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);

		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);

		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 36.0, 16.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 43.0, 15.0, 4.0, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createGenderAndLanguagesThenNewEducationAndNoPreviousMatchSet() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);

		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 42.0, 16.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 43.0, 15.0, 4.0, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithGenderAndEducationOnlyThenUpdateEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);

		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		education2.setId(candidateA.getEducations().iterator().next().getId());
		candidateA.getEducations().clear();

		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, MASTERS, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 62.0, 28.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 70.0, 27.0, 4.0, null, 44.0, CANDIDATE_A));
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndEducationOnlyThenUpdateEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		education2.setId(candidateA.getEducations().iterator().next().getId());
		candidateA.getEducations().clear();

		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, MASTERS, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 67.0, 28.0, null, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 70.0, 27.0, 4.0, null, 44.0, CANDIDATE_A));
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndEducationThenUpdateEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		education2.setId(candidateA.getEducations().iterator().next().getId());
		candidateA.getEducations().clear();

		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, MASTERS, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 76.0, 28.0, 4.0, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 75.0, 27.0, 4.0, 2.0, 44.0, CANDIDATE_A));
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndEducationThenDeleteEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", candidateA.getEducations().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(0);

		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(0);
		/*
		 * assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
		 * .extracting("job.jobTitle", "matchScore", "educationMatchScore",
		 * "genderMatchScore", "languageMatchScore","totalEligibleScore",
		 * "candidate.firstName") .contains(tuple(JOB_B, 76.0, 28.0, 4.0, 2.0,45.0,
		 * CANDIDATE_A)) .contains(tuple(JOB_F, 75.0, 27.0, 4.0, 2.0,
		 * 44.0,CANDIDATE_A));
		 */
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndMultipleEducationThenDeleteHighestEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA).highestQualification(true);
		candidateA.addEducation(education1).addEducation(education2);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(28.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(76.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(27.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(75.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);
		assertThat(candidateA.getEducations()).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS)).contains(tuple(null, ISC));

		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}",
				candidateA.getEducations().stream().filter(education -> education.isHighestQualification() != null)
						.filter(education -> education.isHighestQualification()).findFirst().get().getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, ISC));
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 49.0, 16.0, 4.0, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 48.0, 15.0, 4.0, 2.0, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndMultipleEducationThenDeleteNonHighestEducation()
			throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA).highestQualification(true);
		candidateA.addEducation(education1).addEducation(education2);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(28.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(76.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(27.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(75.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);
		assertThat(candidateA.getEducations()).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS)).contains(tuple(null, ISC));

		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", candidateA.getEducations()
				.stream().filter(education -> education.isHighestQualification() == null).findFirst().get().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS));
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 76.0, 28.0, 4.0, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 75.0, 27.0, 4.0, 2.0, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createMatchSetWithLanguageAndGenderAndEducationThenUpdateEducationDateToPickUpNewJobFittingCriteria()
			throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		genderRepository.saveAndFlush(femaleGender);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		Thread.sleep(10000);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA).highestQualification(true);
		candidateA.addEducation(education1).addEducation(education2);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(28.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(76.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(27.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(75.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education3 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 07, 24))
				.candidate(candidateA);
		education3.setId(candidateA.getEducations().stream().filter(education -> education.getPercentage().equals(89.0))
				.findFirst().get().getId());
		candidateA.getEducations().remove(education3);

		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education3)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		assertThat(testCandidateEducations).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS));
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(4);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "candidate.firstName").contains(tuple(JOB_B, CANDIDATE_A))
				.contains(tuple(JOB_A, CANDIDATE_A)).contains(tuple(JOB_G, CANDIDATE_A))
				.contains(tuple(JOB_C, CANDIDATE_A));

	}
}
