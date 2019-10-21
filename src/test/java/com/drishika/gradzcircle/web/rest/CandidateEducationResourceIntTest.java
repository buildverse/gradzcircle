
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
import org.junit.After;
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
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateJobRepository;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.SkillsRepository;
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
//(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) 
public class CandidateEducationResourceIntTest {

	private static final Double DEFAULT_GRADE = 1.1D;
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

	private static final String DEFAULT_SCORE_TYPE = "gpa";
	private static final String UPDATED_SCORE_TYPE = "gpa";

	private static final Integer DEFAULT_EDUCATION_DURATION = 1;
	private static final Integer UPDATED_EDUCATION_DURATION = 2;

	private static final String CANDIDATE_A = "Candidate A";
	private static final String CANDIDATE_B = "Candidate B";
	private static final String JOB_A = "JOB_A";
	private static final String JOB_B = "JOB_B";
	private static final String JOB_C = "JOB_C";
	private static final String JOB_D = "JOB_D";
	private static final String JOB_E = "JOB_E";
	private static final String JOB_F = "JOB_F";
	private static final String JOB_G = "JOB_G";
	private static final String JOB_H = "JOB_H";
	private static final String JOB_I = "JOB_I";
	private static final String JOB_J = "JOB_J";
	private static final String JOB_K = "JOB_K";
	private final static String ISC = "ISC";
	private final static String ICSE = "ICSE";
	private final static String DIPLOMA = "Diploma";
	private final static String MASTERS = "MASTERS";
	private final static String PG_DIPLOMA = "PG. Diploma";
	private final static String MBA = "MBA";
	private final static String MGMT = "MGMT";
	private final static String BACHELOR = "BACHELORS";
	private final static String StTHOMAS = "St. THOMAS";
	private static final String DOON = "Doon";
	private final static String BUSINESS_ADM ="HR";
	private final static String IIM = "IIM";
	private final static String MEDICAL = "MEDICAL";
	private final static String HINDI = "Hindi";
	private final static String ENGLISH = "English";
	private final static String MARATHI = "Marathi";
	private final static String MALE = "MALE";
	private final static String FEMALE = "FEMALE";
	private final static String MUMBAI_UNIVERSITY = "UNIVERSITY OF MUMBAI";
	private final static String DEFAULT_QUAL_CATEGORY="DEFAULT";
	private final static String PHARMA="PHARMA";
	private final static String ENGG="ENGG";
	private final static String ENGINEERING ="ENGINEERING";
	private final static String ARTS = "ARTS";
	private final static String BA ="BA";
	private final static String MA = "MA";
	private final static String AA = "AA";
	private final static String BTECH ="BTECH";
	private final static String MTECH = "MTECH";
	private final static String UK = "UK";

	private University uniIIM, uniDoon;
	private Course courseBUSINESS_ADM, courseICSE, courseISC, courseMedical,coursePHARMA,courseENGG;
	private Qualification qualPG_DIPLOMA, qualISC, qualICSE, qualDIPLOMA, qualBachelor, qualMaster, qualBE,qualBA,qualME,qualMA,qualAA,qualPG_MBA,qualUK;;
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
	private SkillsRepository skillsRepository;

	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private ProfileCategoryRepository profileCategoryRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private EntityManager em;

	private MockMvc restCandidateEducationMockMvc;

	private CandidateEducation candidateEducation;

	private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad;
	
	private Skills msWord,msExcel,otherSkill;
	
	private Course course;
	private Qualification qualification;
	private College college;
	private University university, mumbaiUni;
	private Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
			universityFilter, qualificationFilter, skillFilter;

	private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH,jobI,jobJ,jobK;

	@Autowired
	private CandidateEducationService candidateEducationService;

	@Autowired
	private CandidateLanguageProficiencyRepository candidateLanguageRepository;

	
	@Autowired
	private CandidateJobRepository candidateJobRepository;
	

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

	 public static Skills createMsWordSkill(EntityManager em) {
	    	return new Skills().skill("MSWord");
	    }
	    
	    public static Skills createMsExcelSkill(EntityManager em) {
	    	return new Skills().skill("MSExcel");
	    }
	    
	    public static Skills createOtherSkill(EntityManager em) {
	    	return new Skills().skill("Other");
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
		return new Course().course(BUSINESS_ADM).display(BUSINESS_ADM).value(BUSINESS_ADM);
	}

	public static Course createMedicalCourse(EntityManager em) {
		return new Course().course(MEDICAL).display(MEDICAL).value(MEDICAL);
	}
	
	public static Course createEnggCourse(EntityManager em) {
		return new Course().course(ENGG).display(ENGG).value(ENGG);
	}
	
	public static Course createPharmaCourse(EntityManager em) {
		return new Course().course(PHARMA).display(PHARMA).value(PHARMA);
	}

	public static Course createISCCourse(EntityManager em) {
		return new Course().course(ISC).value(ISC).display(ISC);
	}

	public static Course createICSECourse(EntityManager em) {
		return new Course().course(ICSE).value(ICSE).display(ICSE);
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
	
	public static Filter createSkillfilter(EntityManager em) {
		return new Filter().filterName("skill").matchWeight(10L);
	}

	public static Filter createGenderFilter(EntityManager em) {
		return new Filter().filterName("gender").matchWeight(4L);
	}

	@Before
	public void initTest() {
		/*candidateJobRepository.deleteAll();
		candidateProfileScoreRepository.deleteAll();
		candidateRepository.deleteAll();
		profileCategoryRepository.deleteAll();
		em.clear();
		em.close();
		System.out.println("==========candidate Profile b4========="+candidateProfileScoreRepository.findAll());

		System.out.println("==========candidate=========="+candidateRepository.findAll());
		System.out.println("==========candidate==JOB========"+candidateJobRepository.findAll());
		System.out.println("==========Profile Category after========="+profileCategoryRepository.findAll());*/
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
		qualPG_MBA = createMBAQualification(em);
		qualDIPLOMA = createDiplomaQualification(em);
		qualBachelor = createBacehlorQualification(em);
		qualMaster = createMasterQualification(em);
		qualBA  =  createBAQualification(em);
		qualAA =createAAQualification(em);
		qualMA = createMAQualification(em);
		qualBE = createBEQualification(em);
		qualME = createMEQualification(em);
		qualUK = createUnknownQualification(em);
		courseICSE = createICSECourse(em);
		courseISC = createISCCourse(em);
		courseBUSINESS_ADM = createBusAdmCourse(em);
		courseMedical = createMedicalCourse(em);
		coursePHARMA  = createPharmaCourse(em);
		courseENGG = createEnggCourse(em);
		qualificationFilter = createQualificationFilter(em);
		courseFilter = createCourseFilter(em);
		collegeFilter = createCollegeFilter(em);
		skillFilter = createSkillfilter(em);
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
		basic = createBasicProfile(em);
		personal = createPersonalProfile(em);
		cert=createCertProfile(em);
		exp = createExpProfile(em);
		nonAcad = createNonAcadProfile(em);
		edu = createEduProfile(em);
		lang = createLangProfile(em);
		jobA = createJobA(em);
		jobB = createJobB(em);
		jobC = createJobC(em);
		// jobD = createJobD(em);
		// jobE = createJobE(em);
		jobF = createJobF(em);
		jobG = createJobG(em);
		jobH = createJobH(em);
		jobI = createJobI(em);
		jobJ = createJobJ(em);
		jobK= createJobK(em);
		msExcel = createMsExcelSkill(em);
		msWord = createMsWordSkill(em);

	}
	
	@After
	public void destory() {
	/*	System.out.println("==========candidate Profile b4========="+candidateProfileScoreRepository.findAll());

		System.out.println("==========candidate=========="+candidateRepository.findAll());
		System.out.println("==========candidate==JOB========"+candidateJobRepository.findAll());
		System.out.println("==========Profile Category after========="+profileCategoryRepository.findAll());
		*/
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
		Job jobE = new Job().jobTitle(JOB_E).jobStatus(1);
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
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"less\",\"graduationDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"skills\":[{\"value\":\"MSExcel\",\"display\":\"MSExcel\"},{\"value\":\"MSWord\",\"display\":\"MSWord\"}],\"languages\":[{\"value\":\"English\",\"display\":\"English\"}],\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
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
	
	public static Job createJobH(EntityManager em) {
		Job jobH = new Job().jobTitle(JOB_H).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BTECH\",\"display\": \"BTECH\"},{\"value\":\"MTECH\",\"display\": \"MTECH\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"between\",\"graduationFromDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"graduationToDate\": {\"year\":2018,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobH);
		jobFilters.add(jobFilter);
		jobH.setJobFilters(jobFilters);
		return jobH;
	}
	
	public static Job createJobI(EntityManager em) {
		Job jobI = new Job().jobTitle(JOB_I).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BA\",\"display\": \"BA\"},{\"value\":\"MA\",\"display\": \"MA\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"between\",\"graduationFromDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"graduationToDate\": {\"year\":2018,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobI);
		jobFilters.add(jobFilter);
		jobI.setJobFilters(jobFilters);
		return jobI;
	}
	
	public static Job createJobJ(EntityManager em) {
		Job jobJ = new Job().jobTitle(JOB_J).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"HR\",\"display\": \"HR\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"MBA\",\"display\": \"MBA\"},{\"value\":\"PG. Diploma\",\"display\": \"PG. Diploma\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"between\",\"graduationFromDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"graduationToDate\": {\"year\":2020,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobJ);
		jobFilters.add(jobFilter);
		jobJ.setJobFilters(jobFilters);
		return jobJ;
	}
	
	public static Job createJobK(EntityManager em) {
		Job jobK = new Job().jobTitle(JOB_K).jobStatus(1);
		;
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"HR\",\"display\": \"HR\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BTECH\",\"display\": \"BTECH\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\"}}";
		jobFilter.filterDescription(filterDescription).job(jobK);
		jobFilters.add(jobFilter);
		jobK.setJobFilters(jobFilters);
		return jobK;
	}

	public static Qualification createPGDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(PG_DIPLOMA).weightage(4L).category(DEFAULT_QUAL_CATEGORY);
	}
	
	public static Qualification createMBAQualification(EntityManager em) {
		return new Qualification().qualification(MBA).weightage(4L).category(MGMT);
	}

	public static Qualification createISCQualification(EntityManager em) {
		return new Qualification().qualification(ISC).weightage(0L).category(DEFAULT_QUAL_CATEGORY);
	}

	public static Qualification createICSEaQualification(EntityManager em) {
		return new Qualification().qualification(ICSE).weightage(0L).category(DEFAULT_QUAL_CATEGORY);
	}

	public static Qualification createDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(DIPLOMA).weightage(1L).category(DEFAULT_QUAL_CATEGORY);
	}

	public static Qualification createBacehlorQualification(EntityManager em) {
		return new Qualification().qualification(BACHELOR).weightage(2L).category(DEFAULT_QUAL_CATEGORY);
	}

	public static Qualification createMasterQualification(EntityManager em) {
		return new Qualification().qualification(MASTERS).weightage(3L).category(DEFAULT_QUAL_CATEGORY);
	}
	
	public static Qualification createMEQualification(EntityManager em) {
		return new Qualification().qualification(MTECH).weightage(3L).category(ENGINEERING);
	}
	
	public static Qualification createBEQualification(EntityManager em) {
		return new Qualification().qualification(BTECH).weightage(2L).category(ENGINEERING);
	}
	
	public static Qualification createBAQualification(EntityManager em) {
		return new Qualification().qualification(BA).weightage(2L).category(ARTS);
	}
	
	public static Qualification createAAQualification(EntityManager em) {
		return new Qualification().qualification(AA).weightage(1L).category(ARTS);
	}
	
	public static Qualification createMAQualification(EntityManager em) {
		return new Qualification().qualification(MA).weightage(3L).category(ARTS);
	}
	
	public static Qualification createUnknownQualification(EntityManager em) {
		return new Qualification().qualification(UK).weightage(3L).category(UK);
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
	@Transactional
	
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(coursePHARMA).percentage(79d).highestQualification(true)
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
				.contains(tuple(JOB_B, 58.0, 26.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 42.0, 25.0, null, null, 59.0, CANDIDATE_A));
	}

	@Test
	@Transactional
	
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC).highestQualification(true)
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
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(coursePHARMA)
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
				.contains(tuple(JOB_B, 76.0, 27.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 59.0, 26.0, null, null, 44.0, CANDIDATE_A));
		// .contains(tuple(JOB_F,47.0,38.0,4.0,5.0,100.0,CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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
				.percentage(79d).college(uniDoon.getColleges().iterator().next()).highestQualification(false)
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, DIPLOMA, CANDIDATE_A)).contains(tuple(false, ISC, CANDIDATE_A));
		assertThat(testCandidates.get(0).getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				//.contains(tuple(JOB_B, 53.0, 17.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 59.0, 26.0, null, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24)).highestQualification(false)
				.candidate(candidateA);
		candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
				/*.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 18.0, 8.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 27.0, 7.0, 3.0, 2.0, 44.0, CANDIDATE_A));*/

	}

	@Test
	@Transactional
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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
	@Transactional 
	public void deleteHighestEducationPostCreatingAddtionalCandidateAEducationShouldMatchWithNextHighestEducationAndEducationProfileScoreShouldContainEducation() throws Exception {
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidateA,edu);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidateA,basic);
		candidateProfileScore1.setScore(50d);
		candidateProfileScore2.setScore(5d);
		candidateA.addCandidateProfileScore(candidateProfileScore1);
		candidateA.addCandidateProfileScore(candidateProfileScore2);
		candidateA.setProfileScore(55D);
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

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(coursePHARMA)
				.percentage(68d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.highestQualification(null);
		 candidateEducationRepository.saveAndFlush(education1);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDIPLOMA).course(courseBUSINESS_ADM)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.highestQualification(null);
		 candidateEducationRepository.saveAndFlush(education2);
		CandidateEducation education3 = new CandidateEducation().qualification(qualMaster).course(course)
				.percentage(68d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2018, 02, 24))
				.highestQualification(true);
		candidateA.addEducation(education3).addEducation(education2).addEducation(education1);
		 candidateEducationRepository.saveAndFlush(education3);
		//candidateRepository.saveAndFlush(candidateA);

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
				.contains(tuple(JOB_B, 56.0, 18.0, 4.0, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 52.0, 23.0, null, null, 44.0, CANDIDATE_A));
		boolean candidateEducationExistsInEs = candidateEducationSearchRepository.exists(education3.getId());
		assertThat(candidateEducationExistsInEs).isFalse();
		assertThat(testCandidates.get(0).getProfileScore()).isEqualTo(55D);
		assertThat(testCandidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

	}

	@Test
	@Transactional 
	public void deleteTheOnlyEducationRecordWithAlreadySavedLanguageAndGenderShouldNotcontainEducationProfileScoreButMustContainOtherProfileScore() throws Exception {
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		// 
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24));
		candidateRepository.saveAndFlush(candidateA.addEducation(education));
		CandidateProfileScore profileScore1 = new CandidateProfileScore(candidateA,basic);
		CandidateProfileScore profileScore2 = new CandidateProfileScore(candidateA,personal);
		CandidateProfileScore profileScore3 = new CandidateProfileScore(candidateA,edu);
		profileScore1.setScore(5d);profileScore2.setScore(15d);profileScore3.setScore(50d);
		candidateA.addCandidateProfileScore(profileScore3).addCandidateProfileScore(profileScore2).addCandidateProfileScore(profileScore1);
		candidateA.setProfileScore(70D);
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
		jobB.addCandidateJob(candidateJob1);
		jobG.addCandidateJob(candidateJob2);
		assertThat(candidateA.getCandidateJobs()).hasSize(2);
		assertThat(jobG.getCandidateJobs()).hasSize(1);
		assertThat(jobB.getCandidateJobs()).hasSize(1);
		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", candidateA.getEducations().iterator().next().getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		
		List<CandidateEducation> testCandidateEducationsAgain = candidateEducationRepository.findAll();
		assertThat(testCandidateEducationsAgain).hasSize(0);
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(0);
		assertThat(testCandidates.get(0).getProfileScore()).isEqualTo(20D);
		assertThat(testCandidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidates.get(0).getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(0D);
		Job testJobB = jobRepository.findOne(jobB.getId());
		Job testJobG = jobRepository.findOne(jobG.getId());
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);

	}

	@Test
	@Transactional 
	public void createCandidateEducationOneThenAddAnotherThenUpdateOneRemoveEducationThenAddAgainThenRemoveAllTheScoreShouldMoveFrom20To70AndBackTo20() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		
		university = universityRepository.saveAndFlush(university);
		college = collegeRepository.saveAndFlush(college);
		qualification = qualificationRepository.saveAndFlush(qualification);
		course = courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,personal);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(15d);
		candidate.addCandidateProfileScore(candidateProfileScore1).addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(20D);
		candidateRepository.saveAndFlush(candidate);
		candidateProfileScore1.setCandidate(candidate);
		candidateProfileScore2.setCandidate(candidate);
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
		System.out.println("Profile cats are "+profileCategoryRepository.findAll());
		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(candidateEducationList.size()).isEqualTo(1);
		
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

		CandidateEducation candidateEducation2 = new CandidateEducation().grade(UPDATED_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION);
		
		candidateEducation2.college(college).course(course).qualification(qualification).candidate(candidate);
		
		restCandidateEducationMockMvc
		.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateEducation2)))
		.andExpect(status().isCreated());

		candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList.size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		CandidateEducation updatedCandidateEducation = candidateEducationRepository.findOne(testCandidateEducation.getId());
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
		
		candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList.size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		
		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", testCandidateEducation.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList.size()).isEqualTo(1);
		CandidateEducation testEducation2 = candidateEducationList.get(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		
		restCandidateEducationMockMvc
		.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
		.andExpect(status().isCreated());
		
		
		candidateEducationList = candidateEducationRepository.findAll();
		testCandidateEducation = candidateEducationList.get(0);
		testEducation2 = candidateEducationList.get(1);
		assertThat(candidateEducationList.size()).isEqualTo(2);
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		
		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", testCandidateEducation.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", testEducation2.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
		
		candidateEducationList = candidateEducationRepository.findAll();
		assertThat(candidateEducationList.size()).isEqualTo(0);
		assertThat(testCandidate.getProfileScore()).isEqualTo(20D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(0D);
		
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore = new CandidateProfileScore(candidate,basic);
		candidateProfileScore.setScore(5d);
		candidate.addCandidateProfileScore(candidateProfileScore);
		candidate.setProfileScore(5D);
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
		assertThat(testCandidate.getProfileScore()).isEqualTo(55D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}
	
	@Test
	@Transactional 
	public void createCandidateEducationWithAlreadyEducationCreatedProfileScoreShouldNotChange() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

		university = universityRepository.saveAndFlush(university);
		college = collegeRepository.saveAndFlush(college);
		qualification = qualificationRepository.saveAndFlush(qualification);
		course = courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(50d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(55D);
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
		assertThat(testCandidate.getProfileScore()).isEqualTo(55D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional 
	public void createCandidateEducationWithAlreadyEducationCreatedWithScoreZeroProfileScoreShouldChange() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();

		university = universityRepository.saveAndFlush(university);
		college = collegeRepository.saveAndFlush(college);
		qualification = qualificationRepository.saveAndFlush(qualification);
		course = courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,edu);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1);
		candidate.addCandidateProfileScore(candidateProfileScore2);
		candidate.setProfileScore(5D);
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
		assertThat(testCandidate.getProfileScore()).isEqualTo(55D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);

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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	public void haveMultipleSavedEducationsWithAnHighetsByNonNullEducaitonToDateShouldAllowANullEducationDateAsHighest()
	//TODO
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	public void haveMultipleSavedEducationsWithAnHighetsByNonNullEducaitonToDateShouldAllowANonNullEducationDateAsHighest()
	//TODO
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	public void haveMultipleSavedEducationsWithAnHighetsByNullEducaitonToDateShouldNotAllowANonNullEducationDateAsHighest()
	//TODO
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		assertThat(prevEducation.getHighestQualification()).isEqualTo(false);
		// Validate the CandidateEducation in Elasticsearch
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}


	@Test
	@Transactional 
	public void haveMultipleSavedEducationsWithAnHighetsByNullEducaitonToDateShouldAllowANullEducationDateAsHighest()
	//TODO
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	public void shouldSetHighestEducationWhenCreatingFirstEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION).
				college(college).course(course).qualification(qualification).candidate(candidate);

		
		// Create the CandidateEducation
		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
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
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}
	
	@Test
	@Transactional 
	public void shouldSetHighestEducationByUserPreferenceWhenCreatingFirstEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		
		// Create the CandidateEducation
		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());

		// Validate the CandidateEducation in the database
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		List<Qualification> qualifications = qualificationRepository.findAll();
		List<Course> courses = courseRepository.findAll();
		List<College> colleges = collegeRepository.findAll();
		List<University> universities = universityRepository.findAll();
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
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional 
	public void shouldSetHighestEducationByToDateHigherThanPreviousToDateWhenCreatingSecondEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(UPDATED_EDUCATION_TO_DATE);
		newEducation.setHighestQualification(null);
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
	public void shouldKeepHighestEducationByToDateNullWhenCreatingSecondEducationWithFiniteDate()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(null)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(UPDATED_EDUCATION_TO_DATE);
		newEducation.setHighestQualification(null);
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
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(false);
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
		assertThat(prevEducation.getHighestQualification()).isEqualTo(true);
		// Validate the CandidateEducation in Elasticsearch
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}
	
	
	@Test
	@Transactional 
	public void shouldSetHighestEducationByToDateWithFiniteDateWithUserOverrideWhenCreatingSecondEducationWithFirstEducationSavedWithNullEducationToDate()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(null)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(UPDATED_EDUCATION_TO_DATE);
		newEducation.setHighestQualification(true);
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
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(true);
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
	public void shouldSetHighestEducationByToDateWithFiniteDateWithUserOverrideWhenCreatingSecondEducationWithFirstEducationSavedWithHigherDateEducationToDate()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(UPDATED_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(DEFAULT_EDUCATION_TO_DATE);
		newEducation.setHighestQualification(true);
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
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(DEFAULT_EDUCATION_TO_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(true);
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
		assertThat(prevEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_TO_DATE);
		// Validate the CandidateEducation in Elasticsearch
		// CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		// .findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}
	
	
	@Test
	@Transactional 
	public void shouldSetHighestEducationByToDateNullWhenCreatingSecondEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(DEFAULT_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(null);
		newEducation.setHighestQualification(null);
		System.out.println("======================="+profileCategoryRepository.findAll());
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
		assertThat(testCandidateEducation.getEducationToDate()).isNull();
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
	public void shouldSetHighestEducationOnLatestWhenMultipleToDatesAsNullWhenCreatingSecondEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);

		CandidateEducation candidateEducation = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(null)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(null);
		newEducation.setHighestQualification(null);
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
		assertThat(testCandidateEducation.getEducationToDate()).isNull();
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
		assertThat(prevEducation.getEducationToDate()).isNull();
	}

	
	@Test
	@Transactional 
	public void shouldSetHighestEducationOnLatestWhenSameToDatesWhenCreatingEducation()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation1 = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(UPDATED_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(DEFAULT_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);
		
		CandidateEducation candidateEducation2 = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(UPDATED_EDUCATION_TO_DATE)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.highestQualification(UPDATED_HIGHEST_QUALIFICATION).roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation1);
		candidateEducationRepository.saveAndFlush(candidateEducation2);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation1);
		newEducation.setId(null);
		newEducation.setEducationToDate(UPDATED_EDUCATION_TO_DATE);
		newEducation.capturedQualification("Abhinav");
		newEducation.setHighestQualification(null);
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
		assertThat(candidateEducationList).hasSize(3);
		assertThat(qualifications).hasSize(1);
		assertThat(courses).hasSize(1);
		assertThat(colleges).hasSize(1);
		assertThat(universities).hasSize(1);
		CandidateEducation firstEducation = candidateEducationList.get(0);
		CandidateEducation secondEducation = candidateEducationList.get(1);
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidateEducation.getGrade()).isEqualTo(DEFAULT_GRADE);
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("Abhinav");
		assertThat(testCandidateEducation.getEducationFromDate()).isEqualTo(DEFAULT_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(UPDATED_HIGHEST_QUALIFICATION);
		assertThat(testCandidateEducation.getRoundOfGrade()).isEqualTo(DEFAULT_ROUND_OF_GRADE);
		assertThat(testCandidateEducation.getGradeDecimal()).isEqualTo(DEFAULT_GRADE_DECIMAL);
		assertThat(testCandidateEducation.getCapturedCourse()).isEqualTo(DEFAULT_CAPTURED_COURSE);

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
		assertThat(firstEducation.getHighestQualification()).isEqualTo(false);
		assertThat(firstEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
		assertThat(secondEducation.getHighestQualification()).isEqualTo(false);
		assertThat(secondEducation.getEducationToDate()).isEqualTo(UPDATED_EDUCATION_FROM_DATE);
	}

	
	@Test
	@Transactional 
	public void shouldOverrideUserPreferenceOnUpdateWhenMulitpleEducationsWithNullToDatesAreAlreadySaved()
			throws Exception {
		
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		CandidateEducation candidateEducation1 = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(null)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.roundOfGrade(DEFAULT_ROUND_OF_GRADE)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);
		
		CandidateEducation candidateEducation2 = new CandidateEducation().grade(DEFAULT_GRADE)
				.educationFromDate(DEFAULT_EDUCATION_FROM_DATE).educationToDate(null)
				.isPursuingEducation(DEFAULT_IS_PURSUING_EDUCATION).gradeScale(DEFAULT_GRADE_SCALE)
				.roundOfGrade(DEFAULT_ROUND_OF_GRADE).highestQualification(true)
				.gradeDecimal(DEFAULT_GRADE_DECIMAL).capturedCourse(DEFAULT_CAPTURED_COURSE)
				.capturedQualification(DEFAULT_CAPTURED_QUALIFICATION).capturedCollege(DEFAULT_CAPTURED_COLLEGE)
				.capturedUniversity(DEFAULT_CAPTURED_UNIVERSITY).percentage(DEFAULT_PERCENTAGE)
				.scoreType(DEFAULT_SCORE_TYPE).educationDuration(DEFAULT_EDUCATION_DURATION)
				.college(college).course(course).qualification(qualification).candidate(candidate);

		candidateEducationRepository.saveAndFlush(candidateEducation1);
		candidateEducationRepository.saveAndFlush(candidateEducation2);

		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation1);
		newEducation.setHighestQualification(true);
		// Create the CandidateEducation
		
		restCandidateEducationMockMvc
		.perform(put("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(newEducation)))
		.andExpect(status().isOk());
		

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
		assertThat(testCandidateEducation.getEducationToDate()).isNull();
		assertThat(testCandidateEducation.isIsPursuingEducation()).isEqualTo(DEFAULT_IS_PURSUING_EDUCATION);
		assertThat(testCandidateEducation.getGradeScale()).isEqualTo(DEFAULT_GRADE_SCALE);
		assertThat(testCandidateEducation.isHighestQualification()).isEqualTo(DEFAULT_HIGHEST_QUALIFICATION);
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
		assertThat(prevEducation.getHighestQualification()).isEqualTo(true);
		assertThat(prevEducation.getEducationToDate()).isNull();
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
				.capturedQualification("bacHelor of sOmeThing").candidate(candidate);

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
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("bacHelor of sOmeThing");
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("Bachelor Of Something");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Bachelor Of Something");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Bachelor Of Something");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(3l);
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}
	
	@Test
	@Transactional 
	public void createEducationWithOtherQualificationOnlyWithNonExistingThreeCharacterQualification() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
				.capturedQualification("AbC").candidate(candidate);

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
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("AbC");
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(-1l);
		// Validate the CandidateEducation in Elasticsearch
		CandidateEducation candidateEducationEs = candidateEducationSearchRepository
				.findOne(testCandidateEducation.getId());
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
	}

	@Test
	@Transactional 
	public void createEducationWithOtherQualificationOnlyWithNonExistingThreeCharacterWithDotsQualification() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		System.out.println("====================="+profileCategoryRepository.findAll());
		System.out.println("====================="+courseRepository.findAll());
		System.out.println("====================="+qualificationRepository.findAll());
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		System.out.println("====================="+profileCategoryRepository.findAll());
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
				.capturedQualification("a.b.C").candidate(candidate);

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
		assertThat(testCandidateEducation.getCapturedQualification()).isEqualTo("a.b.C");
		assertThat(testCandidateEducation.getCapturedCollege()).isEqualTo(DEFAULT_CAPTURED_COLLEGE);
		assertThat(testCandidateEducation.getCapturedUniversity()).isEqualTo(DEFAULT_CAPTURED_UNIVERSITY);
		assertThat(testCandidateEducation.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
		assertThat(testCandidateEducation.getScoreType()).isEqualTo(DEFAULT_SCORE_TYPE);
		assertThat(testCandidateEducation.getEducationDuration()).isEqualTo(DEFAULT_EDUCATION_DURATION);
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getCourse()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getQualification()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo(college.getCollegeName());
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(-1l);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	public void getCandidateEducationByCandidateWithProfileScore() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateRepository.saveAndFlush(candidate);
		candidateEducation.setCollege(college);
		candidateEducation.setQualification(qualification);
		candidateEducation.setCourse(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(get("/api/education-by-candidate/{id}", candidate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateEducation.getId().intValue()))
				.andExpect(jsonPath("$[0].grade").value(DEFAULT_GRADE.doubleValue()))
				.andExpect(jsonPath("$[0].educationFromDate").value(DEFAULT_EDUCATION_FROM_DATE.toString()))
				.andExpect(jsonPath("$[0].educationToDate").value(DEFAULT_EDUCATION_TO_DATE.toString()))
				.andExpect(jsonPath("$[0].isPursuingEducation").value(DEFAULT_IS_PURSUING_EDUCATION.booleanValue()))
			//	.andExpect(jsonPath("$[0].gradeScale").value(DEFAULT_GRADE_SCALE))
				.andExpect(jsonPath("$[0].highestQualification").value(DEFAULT_HIGHEST_QUALIFICATION.booleanValue()))
			//	.andExpect(jsonPath("$[0].roundOfGrade").value(DEFAULT_ROUND_OF_GRADE))
				//.andExpect(jsonPath("$[0].gradeDecimal").value(DEFAULT_GRADE_DECIMAL))
			//	.andExpect(jsonPath("$[0].capturedCourse").value(DEFAULT_CAPTURED_COURSE.toString()))
			//	.andExpect(jsonPath("$[0].capturedQualification").value(DEFAULT_CAPTURED_QUALIFICATION.toString()))
			//	.andExpect(jsonPath("$[0].capturedCollege").value(DEFAULT_CAPTURED_COLLEGE.toString()))
			//	.andExpect(jsonPath("$[0].capturedUniversity").value(DEFAULT_CAPTURED_UNIVERSITY.toString()))
				//.andExpect(jsonPath("$[0].percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
				.andExpect(jsonPath("$[0].scoreType").value(DEFAULT_SCORE_TYPE.toString()))
				.andExpect(jsonPath("$[0].college.collegeName").value("Miranda"))
				.andExpect(jsonPath("$[0].college.display").value("Miranda"))
				.andExpect(jsonPath("$[0].college.value").value("Miranda"))
				.andExpect(jsonPath("$[0].college.university.universityName").value("Delhi"))
				.andExpect(jsonPath("$[0].college.university.display").value("Delhi"))
				.andExpect(jsonPath("$[0].college.university.value").value("Delhi"))
				.andExpect(jsonPath("$[0].qualification.qualification").value("Master"))
				.andExpect(jsonPath("$[0].qualification.value").value("Master"))
				.andExpect(jsonPath("$[0].qualification.display").value("Master"))
				.andExpect(jsonPath("$[0].course.value").value("Computer"))
				.andExpect(jsonPath("$[0].course.display").value("Computer"))
				.andExpect(jsonPath("$[0].course.course").value("Computer"))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}
	
	@Test
	@Transactional
	@Ignore
	public void getCandidateEducationByCandidateWithProfileScoreEmptyEducationList() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate().profileScore(25d);
		candidateRepository.saveAndFlush(candidate);
		//candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(get("/api/education-by-candidate/{id}",candidate.getId())).andDo(MockMvcResultHandlers.print())
				
				.andExpect(jsonPath("$[0].candidate.profileScore").value(25d));
	}
	
	@Test
	@Transactional
	public void getCandidateEducationByCandidateWithoutProfileScore() throws Exception {
		// Initialize the database
		Candidate candidate = new Candidate();
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
		

		// Get the candidateEducation
		restCandidateEducationMockMvc.perform(get("/api/education-by-candidate/{id}", candidate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(candidateEducation.getId().intValue()))
				.andExpect(jsonPath("$[0].grade").value(DEFAULT_GRADE.doubleValue()))
				.andExpect(jsonPath("$[0].educationFromDate").value(DEFAULT_EDUCATION_FROM_DATE.toString()))
				.andExpect(jsonPath("$[0].educationToDate").value(DEFAULT_EDUCATION_TO_DATE.toString()))
				.andExpect(jsonPath("$[0].isPursuingEducation").value(DEFAULT_IS_PURSUING_EDUCATION.booleanValue()))
				//.andExpect(jsonPath("$[0].gradeScale").value(DEFAULT_GRADE_SCALE))
				.andExpect(jsonPath("$[0].highestQualification").value(DEFAULT_HIGHEST_QUALIFICATION.booleanValue()))
			//	.andExpect(jsonPath("$[0].roundOfGrade").value(DEFAULT_ROUND_OF_GRADE))
				//.andExpect(jsonPath("$[0].gradeDecimal").value(DEFAULT_GRADE_DECIMAL))
			//	.andExpect(jsonPath("$[0].capturedCourse").value(DEFAULT_CAPTURED_COURSE.toString()))
				////.andExpect(jsonPath("$[0].capturedQualification").value(DEFAULT_CAPTURED_QUALIFICATION.toString()))
				//.andExpect(jsonPath("$[0].capturedCollege").value(DEFAULT_CAPTURED_COLLEGE.toString()))
				//.andExpect(jsonPath("$[0].capturedUniversity").value(DEFAULT_CAPTURED_UNIVERSITY.toString()))
			//	.andExpect(jsonPath("$[0].percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
				.andExpect(jsonPath("$[0].scoreType").value(DEFAULT_SCORE_TYPE.toString()))
			//	.andExpect(jsonPath("$[0].educationDuration").value(DEFAULT_EDUCATION_DURATION))
				.andExpect(jsonPath("$[0].college.collegeName").value("Miranda"))
				.andExpect(jsonPath("$[0].college.display").value("Miranda"))
				.andExpect(jsonPath("$[0].college.value").value("Miranda"))
				.andExpect(jsonPath("$[0].college.university.universityName").value("Delhi"))
				.andExpect(jsonPath("$[0].college.university.display").value("Delhi"))
				.andExpect(jsonPath("$[0].college.university.value").value("Delhi"))
				.andExpect(jsonPath("$[0].qualification.qualification").value("Master"))
				.andExpect(jsonPath("$[0].qualification.value").value("Master"))
				.andExpect(jsonPath("$[0].qualification.display").value("Master"))
				.andExpect(jsonPath("$[0].course.value").value("Computer"))
				.andExpect(jsonPath("$[0].course.display").value("Computer"))
				.andExpect(jsonPath("$[0].candidate.profileScore").value(0d));
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
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
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		languageRepository.saveAndFlush(hindiLanguage);
		languageRepository.saveAndFlush(englishLanguage);
		languageRepository.saveAndFlush(marathiLanguage);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(coursePHARMA).highestQualification(true)
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
				.contains(tuple(JOB_B, 64.0, 26.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 51.0, 25.0, null, 5.0, 59.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);

		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(coursePHARMA)
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
				.contains(tuple(JOB_B, 58.0, 26.0, null, null, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 49.0, 25.0, 4.0, null, 59.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);

		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(coursePHARMA)
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
				.contains(tuple(JOB_B, 64.0, 26.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 58.0, 25.0, 4.0, 5.0, 59.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_F, 53.0, 27.0, 4.0, null, 59.0, CANDIDATE_A));
				
	}

	@Test
	@Transactional 
	public void createMatchSetWithGenderAndEducationOnlyThenUpdateEducationWithCurrentlyStudying() throws Exception {
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
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.flush();
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC)
					.course(courseISC).highestQualification(true)
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
		jobB.addCandidateJob(cJ1);
		jobF.addCandidateJob(cJ2);
		CandidateEducation education2 = new CandidateEducation().qualification(qualBachelor).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(null).isPursuingEducation(true)
				.candidate(candidateA);
		education2.setId(candidateA.getEducations().iterator().next().getId());
		candidateA.getEducations().clear();

		restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		Job testJobE = jobRepository.findOne(jobC.getId());
		Job testJobF = jobRepository.findOne(jobF.getId());
		Job testJobG = jobRepository.findOne(jobG.getId());
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName","course.course")
				.contains(tuple(true, BACHELOR, CANDIDATE_A,MEDICAL));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(3);
		assertThat(testJobE.getCandidateJobs()).hasSize(1);
		assertThat(testJobF.getCandidateJobs()).hasSize(1);
		assertThat(testJobG.getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_E, 70.0, 27.0, 4.0, null, 44.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 53.0, 27.0, 4.0, null, 59.0, CANDIDATE_A))
				.contains(tuple(JOB_G, 70.0, 27.0, 4.0, null, 44.0, CANDIDATE_A));
	}

	
	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				//.contains(tuple(JOB_B, 67.0, 28.0, null, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A));
	}

	@Test
	@Transactional 
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 26))
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
			//	.contains(tuple(JOB_B, 76.0, 28.0, 4.0, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 56.0, 27.0, 4.0, 2.0, 59.0, CANDIDATE_A));
	}

	@Test
	@Transactional 
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateProfileScore candidateProfileScore = new CandidateProfileScore(candidateA,edu);
		candidateProfileScore.setScore(50d);
		candidateA.addCandidateProfileScore(candidateProfileScore);
		candidateA.setProfileScore(50D);
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
	@Transactional 
	public void createMatchSetWithLanguageAndGenderAndMultipleEducationThenDeleteHighestEducation() throws Exception {
		universityRepository.deleteAll();
		qualificationRepository.deleteAll();
		courseRepository.deleteAll();
		jobRepository.deleteAll();
		languageRepository.deleteAll();
		filterRepository.deleteAll();
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualBachelor).course(coursePHARMA)
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
		cJ2.setMatchScore(72.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		jobB.addCandidateJob(cJ1);
		jobF.addCandidateJob(cJ2);
		assertThat(candidateA.getEducations()).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS)).contains(tuple(null, BACHELOR));

		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}",
				candidateA.getEducations().stream().filter(education -> education.isHighestQualification() != null)
						.filter(education -> education.isHighestQualification()).findFirst().get().getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		Job testJobA = jobRepository.findOne(jobA.getId());
		Job testJobB = jobRepository.findOne(jobB.getId());
		Job testJobC = jobRepository.findOne(jobC.getId());
		Job testJobF = jobRepository.findOne(jobF.getId());
		Job testJobG = jobRepository.findOne(jobG.getId());
		
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, BACHELOR));
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(1);
		assertThat(testJobC.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(1);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 71.0, 26.0,4.0, 2.0, 45.0, CANDIDATE_A))
				//.contains(tuple(JOB_E, 57.0, 25.0, null, null, 44.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 56.0, 27.0, 4.0, 2.0, 59.0, CANDIDATE_A));
			//	.contains(tuple(JOB_G, 57.0, 25.0, null, null, 44.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
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

		restCandidateEducationMockMvc.perform(delete("/api/candidate-educations/{id}", education1.getId())
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations).extracting("highestQualification", "qualification.Qualification")
				.contains(tuple(true, MASTERS));
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				//.contains(tuple(JOB_B, 76.0, 28.0, 4.0, 2.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 56.0, 27.0, 4.0, 2.0, 59.0, CANDIDATE_A));

	}

	@Test
	@Transactional 
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateLanguageProficiency languageProficiency1 = new CandidateLanguageProficiency().language(englishLanguage)
				.candidate(candidateA);
		CandidateLanguageProficiency languageProficiency2 = new CandidateLanguageProficiency().language(hindiLanguage)
				.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(languageProficiency2)
				.addCandidateLanguageProficiency(languageProficiency1);

		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next()).highestQualification(true)
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
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
		assertThat(testCandidates.get(0).getCandidateJobs()).hasSize(2);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "candidate.firstName").contains(tuple(JOB_E, CANDIDATE_A))
				.contains(tuple(JOB_G, CANDIDATE_A));
				

	}
	
	@Test
	@Transactional 
	public void deleteEducationWithSameEndDateAndOneHighest() throws Exception {
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		// 
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateProfileScore profileScore1 = new CandidateProfileScore(candidateA,basic);
		CandidateProfileScore profileScore2 = new CandidateProfileScore(candidateA,personal);
		CandidateProfileScore profileScore3 = new CandidateProfileScore(candidateA,edu);
		profileScore1.setScore(5d);profileScore2.setScore(15d);profileScore3.setScore(50d);
		candidateA.addCandidateProfileScore(profileScore3).addCandidateProfileScore(profileScore2).addCandidateProfileScore(profileScore1);
		candidateA.setProfileScore(70D);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(100d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).candidate(candidateA).highestQualification(false);
		CandidateEducation education2 = new CandidateEducation().qualification(qualISC).course(courseBUSINESS_ADM).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).candidate(candidateA).highestQualification(true);
		CandidateEducation education3 = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(78d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 23)).candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA.addEducation(education));
		candidateRepository.saveAndFlush(candidateA.addEducation(education2));
		candidateRepository.saveAndFlush(candidateA.addEducation(education3));
		Long id = candidateA.getEducations().stream().filter(educationFilter -> educationFilter.getPercentage().equals(78d)).findFirst().get().getId();
		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", id)
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getProfileScore()).isEqualTo(70d);
		List<CandidateEducation> testCandidateEducationsAgain = candidateEducationRepository.findAll();
		assertThat(testCandidateEducationsAgain).hasSize(2);
		assertThat(testCandidateEducationsAgain).extracting("course","highestQualification").contains(tuple(courseBUSINESS_ADM,true)).contains(tuple(courseISC,false));
	}
	
	@Test
	@Transactional 
	public void deleteEducationWithmultipleTillDateWhichMeansNullEndDateAndOneHighest() throws Exception {
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		// 
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateProfileScore profileScore1 = new CandidateProfileScore(candidateA,basic);
		CandidateProfileScore profileScore2 = new CandidateProfileScore(candidateA,personal);
		CandidateProfileScore profileScore3 = new CandidateProfileScore(candidateA,edu);
		profileScore1.setScore(5d);profileScore2.setScore(15d);profileScore3.setScore(50d);
		candidateA.addCandidateProfileScore(profileScore3).addCandidateProfileScore(profileScore2).addCandidateProfileScore(profileScore1);
		candidateA.setProfileScore(70D);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(100d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(null).candidate(candidateA).highestQualification(false).isPursuingEducation(false);
		CandidateEducation education2 = new CandidateEducation().qualification(qualISC).course(courseBUSINESS_ADM).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(null).candidate(candidateA).highestQualification(true).isPursuingEducation(true);
		CandidateEducation education3 = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(78d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 23)).candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA.addEducation(education));
		candidateRepository.saveAndFlush(candidateA.addEducation(education2));
		candidateRepository.saveAndFlush(candidateA.addEducation(education3));
		Long id = candidateA.getEducations().stream().filter(educationFilter -> educationFilter.getPercentage().equals(78d)).findFirst().get().getId();
		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", id)
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getProfileScore()).isEqualTo(70d);
		List<CandidateEducation> testCandidateEducationsAgain = candidateEducationRepository.findAll();
		assertThat(testCandidateEducationsAgain).hasSize(2);
		assertThat(testCandidateEducationsAgain).extracting("course","highestQualification").contains(tuple(courseBUSINESS_ADM,true)).contains(tuple(courseISC,false));
	}
	
	@Test
	@Transactional 
	public void deleteEducationWithOneTillDateAndOtherDatesInFuture() throws Exception {
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		// 
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateProfileScore profileScore1 = new CandidateProfileScore(candidateA,basic);
		CandidateProfileScore profileScore2 = new CandidateProfileScore(candidateA,personal);
		CandidateProfileScore profileScore3 = new CandidateProfileScore(candidateA,edu);
		profileScore1.setScore(5d);profileScore2.setScore(15d);profileScore3.setScore(50d);
		candidateA.addCandidateProfileScore(profileScore3).addCandidateProfileScore(profileScore2).addCandidateProfileScore(profileScore1);
		candidateA.setProfileScore(70D);
		CandidateEducation education = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(100d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(null).candidate(candidateA).highestQualification(true).isPursuingEducation(false);
		CandidateEducation education2 = new CandidateEducation().qualification(qualISC).course(courseBUSINESS_ADM).percentage(79d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2020, 02, 23)).candidate(candidateA).highestQualification(false).isPursuingEducation(true);
		CandidateEducation education3 = new CandidateEducation().qualification(qualISC).course(courseISC).percentage(78d)
				.college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25)).highestQualification(true)
				.educationToDate(LocalDate.of(2020, 03, 23)).candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA.addEducation(education));
		candidateRepository.saveAndFlush(candidateA.addEducation(education2));
		candidateRepository.saveAndFlush(candidateA.addEducation(education3));
		Long id = candidateA.getEducations().stream().filter(educationFilter -> educationFilter.getPercentage().equals(100d)).findFirst().get().getId();
		restCandidateEducationMockMvc
				.perform(delete("/api/candidate-educations/{id}", id)
						.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		assertThat(testCandidates.get(0).getProfileScore()).isEqualTo(70d);
		List<CandidateEducation> testCandidateEducationsAgain = candidateEducationRepository.findAll();
		assertThat(testCandidateEducationsAgain).hasSize(2);
		assertThat(testCandidateEducationsAgain).extracting("course","highestQualification").contains(tuple(courseBUSINESS_ADM,false)).contains(tuple(courseISC,true));
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndCandidateCourseAndQualificationDoNotMachThereShouldBeNoMatchCreated() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		
		System.out.println("==========Profile Category in test r========="+profileCategoryRepository.findAll());
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, ISC, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
		/*assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 64.0, 26.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 57.0, 25.0, null, null, 44.0, CANDIDATE_A));
		*/
		
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndCandidateCourseMatchesButQualificationDoNotMachThereShouldBeNoMatchCreated() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		qualificationRepository.saveAndFlush(qualUK);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualUK).course(coursePHARMA).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, UK, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
		/*assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 64.0, 26.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 57.0, 25.0, null, null, 44.0, CANDIDATE_A));
		*/
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndCandidateCourseDoesNotMatchButQualificationtMatchThereShouldBeNoMatchCreated() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualBA).course(courseISC).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, BA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
		/*assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_B, 64.0, 26.0, null, 3.0, 45.0, CANDIDATE_A))
				.contains(tuple(JOB_F, 57.0, 25.0, null, null, 44.0, CANDIDATE_A));
		*/
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndPrevoisMatchSetAndCandidateCourseDoesNotMatchButQualificationtMatchThenMatchShoudlRemove() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualBA).course(courseISC).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobA);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobB);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ3 = new CandidateJob(candidateA, jobC);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ4 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ5 = new CandidateJob(candidateA, jobG);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ6 = new CandidateJob(candidateA, jobH);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ7 = new CandidateJob(candidateA, jobI);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		
		candidateA.getCandidateJobs().add(cJ1);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ3);
		candidateA.getCandidateJobs().add(cJ4);
		candidateA.getCandidateJobs().add(cJ5);
		candidateA.getCandidateJobs().add(cJ6);
		candidateA.getCandidateJobs().add(cJ7);
		
		candidateRepository.saveAndFlush(candidateA);
		
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, BA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
		
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndPrevoisMatchSetAndCandidateCourseMatchButQualificationtDoesNotMatchThenMatchShoudlRemove() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		qualificationRepository.saveAndFlush(qualUK);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		jobRepository.saveAndFlush(jobK);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		candidateRepository.saveAndFlush(candidateA);
		CandidateEducation education1 = new CandidateEducation().qualification(qualBE).course(courseENGG).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		Job testJobK = jobRepository.getOne(jobK.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		assertThat(testJobK.getCandidateJobs()).hasSize(1);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, BTECH, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		CandidateEducation updateEducation = testCandidateEducations.get(0);
			updateEducation.qualification(qualBA).course(courseENGG).highestQualification(true)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
			restCandidateEducationMockMvc.perform(put("/api/candidate-educations")
					.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updateEducation)))
			.andExpect(status().isOk());
			
			 testJobA = jobRepository.getOne(jobA.getId());
			 testJobB = jobRepository.getOne(jobB.getId());
			 testJobE = jobRepository.getOne(jobC.getId());
			 testJobF = jobRepository.getOne(jobF.getId());
			 testJobG = jobRepository.getOne(jobG.getId());
			 testJobH = jobRepository.getOne(jobH.getId());
			 testJobI = jobRepository.getOne(jobI.getId());
			 testJobK = jobRepository.getOne(jobK.getId());
			assertThat(testJobA.getCandidateJobs()).hasSize(0);
			assertThat(testJobB.getCandidateJobs()).hasSize(0);
			assertThat(testJobE.getCandidateJobs()).hasSize(0);
			assertThat(testJobF.getCandidateJobs()).hasSize(0);
			assertThat(testJobG.getCandidateJobs()).hasSize(0);
			assertThat(testJobH.getCandidateJobs()).hasSize(0);
			assertThat(testJobI.getCandidateJobs()).hasSize(0);
			assertThat(testJobK.getCandidateJobs()).hasSize(0);
			testCandidates = candidateRepository.findAll();
			assertThat(testCandidates).hasSize(1);
			testCandidateEducations = candidateEducationRepository.findAll();
			assertThat(testCandidateEducations).hasSize(1);
			assertThat(testCandidateEducations)
					.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
					.contains(tuple(true, BA, CANDIDATE_A));
			assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(0);
		
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndCandidateCourseMatchAndQualificationtMatchByStreamThenCreateMatch() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualMA).course(courseENGG)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(1);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, MA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_I, 61.0, 27.0, null, null, 44.0, CANDIDATE_A));
		
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndMatchSetAndCandidateCourseMatchAndQualificationtMatchAndDifferentPercentageByStreamThenUpdateMatchSet() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualMA).course(courseENGG)
				.percentage(9d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobA);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobB);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ3 = new CandidateJob(candidateA, jobC);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ4 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ5 = new CandidateJob(candidateA, jobG);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ6 = new CandidateJob(candidateA, jobH);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ7 = new CandidateJob(candidateA, jobI);
		cJ1.setEducationMatchScore(27.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(44.0);
		cJ1.setMatchScore(61.0);
		
		candidateA.getCandidateJobs().add(cJ1);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ3);
		candidateA.getCandidateJobs().add(cJ4);
		candidateA.getCandidateJobs().add(cJ5);
		candidateA.getCandidateJobs().add(cJ6);
		candidateA.getCandidateJobs().add(cJ7);
		
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(1);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, MA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_I, 43.0, 19.0, null, null, 44.0, CANDIDATE_A));
	}
	
	@Test
	@Transactional 
	public void whenHaveMultipleJobsAndMatchSetAndCandidateCourseMatchAndQualificationtMatchByStreamAndPerfectQualificationMatchThenUpdateMatchSet() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		qualificationRepository.saveAndFlush(qualAA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateEducation education1 = new CandidateEducation().qualification(qualAA).course(courseENGG)
				.percentage(9d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		candidateRepository.saveAndFlush(candidateA);
		CandidateJob cJ1 = new CandidateJob(candidateA, jobA);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobB);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ3 = new CandidateJob(candidateA, jobC);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ4 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ5 = new CandidateJob(candidateA, jobG);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(4.0);
		cJ1.setLanguageMatchScore(2.0);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setMatchScore(49.0);
		CandidateJob cJ6 = new CandidateJob(candidateA, jobH);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(4.0);
		cJ2.setLanguageMatchScore(2.0);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setMatchScore(48.0);
		CandidateJob cJ7 = new CandidateJob(candidateA, jobI);
		cJ1.setEducationMatchScore(27.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(44.0);
		cJ1.setMatchScore(61.0);
		
		candidateA.getCandidateJobs().add(cJ1);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ3);
		candidateA.getCandidateJobs().add(cJ4);
		candidateA.getCandidateJobs().add(cJ5);
		candidateA.getCandidateJobs().add(cJ6);
		candidateA.getCandidateJobs().add(cJ7);
		
		candidateRepository.saveAndFlush(candidateA);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(1);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(1);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(1);
		assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, AA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_I, 41.0, 18.0, null, null, 44.0, CANDIDATE_A));
	}
	
	@Test
	@Transactional 
	public void whenHaveAJobForMBAOrPGDiplomaForHumarResourceAndCandidateOneIsPGDiplomaAndAnotherMBAInHumanResourceMustCreateMatchForBoth() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		qualificationRepository.saveAndFlush(qualBA);
		qualificationRepository.saveAndFlush(qualBE);
		qualificationRepository.saveAndFlush(qualMA);
		qualificationRepository.saveAndFlush(qualME);
		qualificationRepository.saveAndFlush(qualAA);
		qualificationRepository.saveAndFlush(qualPG_MBA);
		courseRepository.saveAndFlush(courseBUSINESS_ADM);
		courseRepository.saveAndFlush(courseICSE);
		courseRepository.saveAndFlush(courseISC);
		courseRepository.saveAndFlush(coursePHARMA);
		courseRepository.saveAndFlush(courseMedical);
		courseRepository.saveAndFlush(courseENGG);
		jobRepository.saveAndFlush(jobA);
		jobRepository.saveAndFlush(jobB);
		jobRepository.saveAndFlush(jobC);
		jobRepository.saveAndFlush(jobF);
		jobRepository.saveAndFlush(jobG);
		jobRepository.saveAndFlush(jobH);
		jobRepository.saveAndFlush(jobI);
		jobRepository.saveAndFlush(jobJ);
		filterRepository.saveAndFlush(qualificationFilter);
		filterRepository.saveAndFlush(courseFilter);
		filterRepository.saveAndFlush(gradDateFilter);
		filterRepository.saveAndFlush(genderFilter);
		filterRepository.saveAndFlush(collegeFilter);
		filterRepository.saveAndFlush(universityFilter);
		filterRepository.saveAndFlush(scoreFilter);
		filterRepository.saveAndFlush(languageFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		System.out.println("Course is "+courseRepository.findAll());
		System.out.println("Qual is "+qualificationRepository.findAll());
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		Candidate candidateB = new Candidate().firstName(CANDIDATE_B);
		CandidateEducation education1 = new CandidateEducation().qualification(qualPG_DIPLOMA).course(courseBUSINESS_ADM)
				.percentage(9d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateA);
		CandidateEducation education2 = new CandidateEducation().qualification(qualPG_MBA).course(courseBUSINESS_ADM)
				.percentage(9d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 03, 24))
				.candidate(candidateB);
		candidateRepository.saveAndFlush(candidateA);
		candidateRepository.saveAndFlush(candidateB);
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education1)))
				.andExpect(status().isCreated());
		restCandidateEducationMockMvc.perform(post("/api/candidate-educations")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(education2)))
				.andExpect(status().isCreated());
		Job testJobA = jobRepository.getOne(jobA.getId());
		Job testJobB = jobRepository.getOne(jobB.getId());
		Job testJobE = jobRepository.getOne(jobC.getId());
		Job testJobF = jobRepository.getOne(jobF.getId());
		Job testJobG = jobRepository.getOne(jobG.getId());
		Job testJobH = jobRepository.getOne(jobH.getId());
		Job testJobI = jobRepository.getOne(jobI.getId());
		Job testJobJ = jobRepository.getOne(jobJ.getId());
		assertThat(testJobA.getCandidateJobs()).hasSize(0);
		assertThat(testJobB.getCandidateJobs()).hasSize(0);
		assertThat(testJobE.getCandidateJobs()).hasSize(0);
		assertThat(testJobF.getCandidateJobs()).hasSize(0);
		assertThat(testJobG.getCandidateJobs()).hasSize(0);
		assertThat(testJobH.getCandidateJobs()).hasSize(0);
		assertThat(testJobI.getCandidateJobs()).hasSize(0);
		assertThat(testJobJ.getCandidateJobs()).hasSize(2);
		List<Candidate> testCandidates = candidateRepository.findAll();
		assertThat(testCandidates).hasSize(2);
		List<CandidateEducation> testCandidateEducations = candidateEducationRepository.findAll();
		assertThat(testCandidateEducations).hasSize(2);
		/*assertThat(testCandidateEducations)
				.extracting("highestQualification", "qualification.Qualification", "candidate.firstName")
				.contains(tuple(true, AA, CANDIDATE_A));
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName")
				.contains(tuple(JOB_I, 41.0, 18.0, null, null, 44.0, CANDIDATE_A));*/
	}
	
	@Test
	@Transactional 
	public void whenCandidateHasBasicProfileSetWithOverAllProfileScoreOf20AndEducationExpereinceLanguageScoreIsNullAndCertificationScore0AddingEducationMustUpdateEducationScoreTo50AndOverallScoreTo70() throws Exception {
		int databaseSizeBeforeCreate = candidateEducationRepository.findAll().size();
		
		university = universityRepository.saveAndFlush(university);
		college = collegeRepository.saveAndFlush(college);
		qualification = qualificationRepository.saveAndFlush(qualification);
		course = courseRepository.saveAndFlush(course);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		Candidate candidate = new Candidate().firstName("Abhinav");
		candidate = candidateRepository.saveAndFlush(candidate);
		CandidateProfileScore candidateProfileScore1 = new CandidateProfileScore(candidate,basic);
		CandidateProfileScore candidateProfileScore2 = new CandidateProfileScore(candidate,personal);
		CandidateProfileScore candidateProfileScore3 = new CandidateProfileScore(candidate,edu);
		CandidateProfileScore candidateProfileScore4 = new CandidateProfileScore(candidate,exp);
		CandidateProfileScore candidateProfileScore5 = new CandidateProfileScore(candidate,lang);
		CandidateProfileScore candidateProfileScore6 = new CandidateProfileScore(candidate,cert);
		candidateProfileScore1.setScore(5d);
		candidateProfileScore2.setScore(15d);
		candidateProfileScore3.setScore(null);
		candidateProfileScore4.setScore(null);
		candidateProfileScore5.setScore(null);
		candidateProfileScore6.setScore(0d);
		candidate.addCandidateProfileScore(candidateProfileScore1)
				 .addCandidateProfileScore(candidateProfileScore2)
				 .addCandidateProfileScore(candidateProfileScore3)
				 .addCandidateProfileScore(candidateProfileScore4)
				 .addCandidateProfileScore(candidateProfileScore5)
				 .addCandidateProfileScore(candidateProfileScore6);
		candidate.setProfileScore(20D);
		candidateRepository.saveAndFlush(candidate);
		candidateProfileScore1.setCandidate(candidate);
		candidateProfileScore2.setCandidate(candidate);
		candidateProfileScore3.setCandidate(candidate);
		candidateProfileScore4.setCandidate(candidate);
		candidateProfileScore5.setCandidate(candidate);
		candidateProfileScore6.setCandidate(candidate);
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
		System.out.println("Profile cats are "+profileCategoryRepository.findAll());
		// Create the CandidateEducation
		restCandidateEducationMockMvc
				.perform(post("/api/candidate-educations").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(candidateEducation)))
				.andExpect(status().isCreated());
		List<CandidateEducation> candidateEducationList = candidateEducationRepository.findAll();
		CandidateEducation testCandidateEducation = candidateEducationList.get(candidateEducationList.size() - 1);
		Candidate testCandidate = testCandidateEducation.getCandidate();
		assertThat(candidateEducationList.size()).isEqualTo(1);
		
		assertThat(testCandidate.getProfileScore()).isEqualTo(70D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_BASIC_PROFILE)).findFirst().get().getScore()).isEqualTo(5D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().get().getScore()).isEqualTo(15D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()).isEqualTo(50D);
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore()).isNull();
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()).isNull();
		assertThat(testCandidate.getProfileScores().stream().filter(score->score.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()).isEqualTo(0D);
		
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageSkillAndGenderWithScoreForGenderInMatchSetAndAddEducationMatchOnAll() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0.0);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 78.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageSkillAndGenderWithNoScoreInMatchSetAndAddEducationMatchOnAll() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 78.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageSkillAndGenderWithNoMatchSetAndAddEducationThenMatchAll() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 78.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasSkillsOnlyButNoLnaguageAndGenderWithMacthSetWithLanguageAndGenderScoreZeroAndAddEducationThenMatchOnSkillAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 63.0, 27.0, 0.0, 0.0, 59.0, CANDIDATE_A,10.0));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasSkillsOnlyButNoLnaguageAndGenderWithMacthSetWithLanguageAndGenderScoreNullAndAddEducationThenMatchOnSkillAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 63.0, 27.0, null, null, 59.0, CANDIDATE_A,10.0));
	}
	
	
	
	@Test
	@Transactional
	public void whenCandidateHasSkillsOnlyButNoLnaguageAndGenderNoMatchSetoAndAddEducationThenMatchOnSkillAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
	/*	CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 63.0, 27.0, null, null, 59.0, CANDIDATE_A,10.0));
	}
	
	
	
	@Test
	@Transactional
	public void whenCandidateHasGenderOnlyButNoLnaguageAndSkillNoMatchSetoAndAddEducationThenMatchOnGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
	/*	CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 53.0, 27.0, 4.0, null, 59.0, CANDIDATE_A,null));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasGenderOnlyButNoLnaguageAndSkillWithMatchSetAndHaveScoresAsNullInMatchSetAddEducationThenMatchOnGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 53.0, 27.0, 4.0, null, 59.0, CANDIDATE_A,null));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndSkillsButNoGenderAndSkilWithoMatchSetAndZeroScoreOnMacthSetAddEducationThenMatchOnLanguageAndSkillsAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0.0);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 71.0, 27.0, 0.0, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndSkillsButNoGenderAndSkilWithMatchSetAndNullScoreOnMacthSetAddEducationThenMatchOnLanguageAndSkillsAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 71.0, 27.0, null, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndSkillsButNoGenderAndSkilWithNoMatchSetAddEducationThenMatchOnLanguageAndSkillsAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
	/*	CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 71.0, 27.0, null, 5.0, 59.0, CANDIDATE_A,10.0));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndGenderButNoSkillsAndWithMatchSetAndNullScoreOnMacthSetAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,null));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndGenderButNoSkillsAndWithNoMatchSetAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
		/*CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,null));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndGenderButNoSkillsAndWithMatchSetAndZeroScoreOnMacthSetAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,0.0));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasLanguageAndGenderButNoSkillsNoMatchSetAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		/*CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		CandidateSkills skills2 = new CandidateSkills();
		skills2.candidate(candidateA);
		candidateA.addCandidateSkill(skills2);
		skills2.skills(msWord);*/
		CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
	/*	CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 5.0, 59.0, CANDIDATE_A,null));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasSillsAndGenderButNoLanguageNoMatchSetAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
		CandidateEducation education1 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next())
				.educationFromDate(LocalDate.of(2010, 02, 25)).educationToDate(LocalDate.of(2017, 02, 24))
				.candidate(candidateA);
		candidateA.addEducation(education1);
		candidateRepository.saveAndFlush(candidateA);
	/*	CandidateJob cJ1 = new CandidateJob(candidateA, jobB);
		cJ1.setEducationMatchScore(16.0);
		cJ1.setGenderMatchScore(null);
		cJ1.setLanguageMatchScore(null);
		cJ1.setTotalEligibleScore(45.0);
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);*/

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, null, 59.0, CANDIDATE_A,5.0));
	}
	
	
	@Test
	@Transactional
	public void whenCandidateHasSillsAndGenderButNoLanguageWithMatchSetAllScoreNullAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(null);
		cJ2.setLanguageMatchScore(null);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(null);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, null, 59.0, CANDIDATE_A,5.0));
	}
	
	@Test
	@Transactional
	public void whenCandidateHasSkillsAndGenderButNoLanguageWithMatchSetAllScoreZeroAddEducationThenMatchOnLanguageAndGenderAndEducation() throws Exception {
		universityRepository.saveAndFlush(uniIIM);
		universityRepository.saveAndFlush(uniDoon);
		universityRepository.saveAndFlush(mumbaiUni);
		qualificationRepository.saveAndFlush(qualPG_DIPLOMA);
		qualificationRepository.saveAndFlush(qualICSE);
		qualificationRepository.saveAndFlush(qualISC);
		qualificationRepository.saveAndFlush(qualMaster);
		qualificationRepository.saveAndFlush(qualBachelor);
		qualificationRepository.saveAndFlush(qualDIPLOMA);
		courseRepository.saveAndFlush(coursePHARMA);
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
		profileCategoryRepository.saveAndFlush(basic);
		profileCategoryRepository.saveAndFlush(personal);
		profileCategoryRepository.saveAndFlush(cert);
		profileCategoryRepository.saveAndFlush(exp);
		profileCategoryRepository.saveAndFlush(edu);
		profileCategoryRepository.saveAndFlush(nonAcad);
		profileCategoryRepository.saveAndFlush(lang);
		filterRepository.saveAndFlush(qualificationFilter.matchWeight(9l));
		filterRepository.saveAndFlush(courseFilter.matchWeight(10l));
		filterRepository.saveAndFlush(gradDateFilter.matchWeight(0l));
		filterRepository.saveAndFlush(genderFilter.matchWeight(4l));
		filterRepository.saveAndFlush(collegeFilter.matchWeight(6l));
		filterRepository.saveAndFlush(universityFilter.matchWeight(7l));
		filterRepository.saveAndFlush(scoreFilter.matchWeight(8l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(languageFilter.matchWeight(5l));
		filterRepository.saveAndFlush(skillFilter);
		skillsRepository.saveAndFlush(msExcel);
		skillsRepository.saveAndFlush(msWord);
		languageRepository.saveAndFlush(englishLanguage);
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).gender(femaleGender);
		CandidateSkills skills1 = new CandidateSkills();
		skills1.candidate(candidateA);
		candidateA.addCandidateSkill(skills1);
		skills1.skills(msExcel);
		
		/*CandidateLanguageProficiency prof1 = new CandidateLanguageProficiency();
		prof1.language(englishLanguage);
		prof1.candidate(candidateA);
		candidateA.addCandidateLanguageProficiency(prof1);*/
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
		cJ1.setSkillMatchScore(0d);
		cJ1.setMatchScore(36.0);
		CandidateJob cJ2 = new CandidateJob(candidateA, jobF);
		cJ2.setEducationMatchScore(15.0);
		cJ2.setGenderMatchScore(0d);
		cJ2.setLanguageMatchScore(0d);
		cJ2.setTotalEligibleScore(44.0);
		cJ2.setSkillMatchScore(0d);
		cJ2.setMatchScore(43.0);
		candidateA.getCandidateJobs().add(cJ2);
		candidateA.getCandidateJobs().add(cJ1);
		candidateRepository.saveAndFlush(candidateA);

		CandidateEducation education2 = new CandidateEducation().qualification(qualMaster).course(courseMedical)
				.percentage(89d).college(mumbaiUni.getColleges().iterator().next()).highestQualification(true)
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
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs()).hasSize(1);
		assertThat(testCandidateEducations.get(0).getCandidate().getCandidateJobs())
				.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
						"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
				.contains(tuple(JOB_F, 61.0, 27.0, 4.0, 0.0, 59.0, CANDIDATE_A,5.0));
	}
	
	
}
