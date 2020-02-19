/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.beanutils.BeanUtils;
import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
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
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
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
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.CandidateEducationResource;
import com.drishika.gradzcircle.web.rest.TestUtil;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * @author abhinav
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateEducationServiceIntTest {
	
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
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;
	
	@Mock
	private ElasticsearchTemplate elasticSearchTemplate;
	
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
	private  CandidateProjectRepository candidateProjectRepository;
	
	@Autowired
	private  QualificationRepository qualififcationRepository;

	@Autowired
	private  CandidateEducationSearchRepository candidateEducationSearchRepository;
	
	@Mock
	private  ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private  ProfileScoreCalculator profileScoreCalculator;
	
	@Autowired
	private  DTOConverters converter;

	@Autowired @Qualifier("CandidateEducationMatcher")
	private  Matcher<Long> matcher;
	
	@Autowired 
	CandidateProjectSearchRepository candidateProjectSearchRepository;
	
	@Autowired
	UniversitySearchRepository universitySearchRepository;
	
	
	CandidateEducationService candidateEducationService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		candidateEducationService = new CandidateEducationService(
				candidateEducationRepository,candidateEducationSearchRepository,candidateProjectRepository,candidateProjectSearchRepository,
				collegeRepository,qualificationRepository,courseRepository,universityRepository,matcher,profileScoreCalculator,universitySearchRepository,
				elasticsearchTemplate,candidateRepository,converter);
		
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
	public void shouldSetHighestEducationByToDateNullWhenCreatingSecondEducation()
			throws Exception {
		
		University university = new University().universityName("Delhi").value("Delhi").display("Delhi");
		College college = new College().collegeName("Miranda").display("Miranda").value("Miranda")
				.university(university);
		Course course = new Course().course("Computer").value("Computer").display("Computer");
		Qualification qualification = new Qualification().value("Master").qualification("Master").value("Master").display("Master");
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
		System.out.println("==================="+candidateEducation);
		CandidateEducation newEducation = new CandidateEducation();
		BeanUtils.copyProperties(newEducation, candidateEducation);
		newEducation.setId(null);
		newEducation.setEducationToDate(null);
		newEducation.setHighestQualification(null);
		newEducation.setProjects(new HashSet<>());
		// Create the CandidateEducation
		candidateEducationService.createCandidateEducation(newEducation);

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
		candidateEducationService.createCandidateEducation(candidateEducation);
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
		//assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Bachelor Of Something");
		//assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Bachelor Of Something");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(3l);
		// Validate the CandidateEducation in Elasticsearch
	//	CandidateEducation candidateEducationEs = candidateEducationSearchRepository
	//			.findById(testCandidateEducation.getId()).get();
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
		candidateEducationService.createCandidateEducation(candidateEducation);
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
		//assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("A.B.C");
		//assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(-1l);
		// Validate the CandidateEducation in Elasticsearch
	//	CandidateEducation candidateEducationEs = candidateEducationSearchRepository
	//		.findById(testCandidateEducation.getId()).get();
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
		candidateEducationService.createCandidateEducation(candidateEducation);
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
	//	assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Pharma");
	//	assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Pharma");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
	//	CandidateEducation candidateEducationEs = candidateEducationSearchRepository
	//			.findById(testCandidateEducation.getId()).get();
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);
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
		candidateEducationService.createCandidateEducation(candidateEducation);
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
		//assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("IIT");
		//assertThat(testCandidateEducation.getCollege().getValue()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCollege().getUniversity().getValue()).isEqualTo("Delhi");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
		//CandidateEducation candidateEducationEs = candidateEducationSearchRepository
		//		.findById(testCandidateEducation.getId()).get();
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}
	
	/*@Test
	@Transactional
	@Ignore
	public void searchCandidateEducation() throws Exception {
		// Initialize the database
		universityRepository.saveAndFlush(university);
		collegeRepository.saveAndFlush(college);
		qualificationRepository.saveAndFlush(qualification);
		courseRepository.saveAndFlush(course);
		candidateEducationRepository.saveAndFlush(candidateEducation);
		//candidateEducationSearchRepository.save(candidateEducation);

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
	}*/

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
		candidateEducationService.createCandidateEducation(candidateEducation);

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
	//	assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("A.B.C");
	//	assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("A.B.C");
		assertThat(testCandidateEducation.getQualification().getWeightage()).isEqualTo(-1l);
		// Validate the CandidateEducation in Elasticsearch
	//	CandidateEducation candidateEducationEs = candidateEducationSearchRepository
	//			.findById(testCandidateEducation.getId()).get();
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
		candidateEducationService.createCandidateEducation(candidateEducation);

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
		//assertThat(testCandidateEducation.getCollege().getDisplay()).isEqualTo("IIT");
		assertThat(testCandidateEducation.getCollege().getCollegeName()).isEqualTo("IIT");
		//assertThat(testCandidateEducation.getCollege().getUniversity().getDisplay()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCollege().getUniversity().getUniversityName()).isEqualTo("Mumbai");
		assertThat(testCandidateEducation.getCourse().getDisplay()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getCourse().getValue()).isEqualTo("Computer");
		assertThat(testCandidateEducation.getQualification().getValue()).isEqualTo("Master");
		assertThat(testCandidateEducation.getQualification().getDisplay()).isEqualTo("Master");
		// Validate the CandidateEducation in Elasticsearch
	//	CandidateEducation candidateEducationEs = candidateEducationSearchRepository
	//			.findById(testCandidateEducation.getId()).get();
		// assertThat(candidateEducationEs).isEqualToComparingFieldByField(testCandidateEducation);

	}
}
