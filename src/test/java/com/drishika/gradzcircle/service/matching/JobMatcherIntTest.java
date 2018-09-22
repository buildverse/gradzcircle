/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.repository.CandidateJobRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.CandidateEducationService;
import com.drishika.gradzcircle.service.CandidateLanguageService;
import com.drishika.gradzcircle.service.JobService;

/**
 * @author abhinav
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
@Transactional (isolation = Isolation.READ_UNCOMMITTED)
public class JobMatcherIntTest {

	private JobMatcher jobMatcher;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private CandidateEducationService candidateEducationService;
	@Autowired
	private CandidateLanguageService candidateLanguageService;
	@Autowired
	private JobFilterParser jobFilterParser;
	@Autowired
	private EntityManager em;
	@Autowired
	private CorporateRepository corporateRepository;
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private GenderRepository genderRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private CandidateJobRepository candidateJobRepository; 
	@Autowired
	MatchUtils matchUtils;

	@Autowired
	private FilterRepository filterRepository;

	@Autowired
	private QualificationRepository qualificationRepository;
	@Autowired
	private CollegeRepository collegeRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private UniversityRepository universityRepository;
	@Autowired 
	private JobRepository jobRepository;
	@Autowired 
	private JobSearchRepository jobSearchRepository;
	@Autowired
	private JobService jobService;

	private final static String BACHELORS = "BACHELORS";
	private final static String MASTERS = "MASTERS";
	private final static String DOCTRATE = "DOCTRATE";
	private final static String ISC = "ISC";
	private final static String ICSE = "ICSE";
	private final static String StTHOMAS = "St. THOMAS";
	private static final String DOON = "Doon";
	private final static String PG_DIPLOMA = "PG. Diploma";
	private final static String DIPLOMA = "Diploma";
	private final static String COMPUTER = "COMPUTER";
	private final static String BUSINESS_ADM = "Bus. Adm.";
	private final static String IIM = "IIM";
	private final static String PHARMA = "PHARMA";
	private static final String CANDIDATE_A = "Candidate A";
	private static final String CANDIDATE_B = "Candidate B";
	private static final String CANDIDATE_C = "Candidate C";
	private static final String CANDIDATE_D = "Candidate D";
	private static final String CANDIDATE_E = "Candidate E";
	private static final String CANDIDATE_F = "Candidate F";
	private static final String CANDIDATE_G = "Candidate G";
	private static final String CANDIDATE_H = "Candidate H";
	private static final String ENGG = "ENGG";
	private static final String MALE = "MALE";
	private static final String FEMALE = "FEMALE";
	private static final String FOOD_TECH = "Food Tech.";
	private static final String MEDICAL = "MEDICAL";
	private static final String UNIVERSITY_OF_MUMBAI = "UNIVERSITY OF MUMBAI";
	private static final String UNIVERSITY_OF_DELHI = "UNIVERSITY OF DELHI";
	private static final String MIRANDAHOUSE = "MIRANDA HOUSE";
	private static final String AMITY = "AMITY";
	private static final String ABC = "ABC";
	private static final String A = "A";
	private static final String B = "B";
	private static final String AA = "AA";
	private static final String BB = "BB";
	private static final String a = "a";
	private static final String b = "b";
	private static final String JOB_A = "JOB_A";
	private static final String JOB_B = "JOB_B";
	private static final String JOB_C = "JOB_C";
	private static final String JOB_D = "JOB_D";
	private static final String JOB_E = "JOB_E";
	private static final String JOB_F = "JOB_F";
	private static final String JOB_G = "JOB_G";
	private static final String JOB_H = "JOB_H";
	private static final String JOB_Z = "JOB_Z";
	private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH, jobZ;
	private Candidate candidateA, candidateB, candidateC, candidateD, candidateE, candidateF, candidateG, candidateH;
	private static Gender female, male;
	private static Language english, hindi, marathi, punjabi, sanskrit;
	//private static  College collegeIIM, collegeMIRANDAHOUSE, collegeAMITY, collegeA, collegeB, collegeABC, collegeStTHOMAS;
	private static  University uniIIM, uniUNIVERSITY_OF_DELHI, uniUNIVERSITY_OF_MUMBAI, uniA, uniB, uniStTHOMAS, uniDoon,
			uniAMITY, unia, unib;
	private static  Course courseFOOD_TECH, courseMEDICAL, coursePHARMA, courseBUSINESS_ADM, courseCOMPUTER, courseICSE,
			courseISC, courseENGG;
	private  static Qualification qualBACHELORS, qualMASTERS, qualDOCTRATE, qualPG_DIPLOMA, qualDIPLOMA, qualISC, qualICSE;
	private  static Set<Qualification> qualifications;
	private Corporate corporate;
	private  static Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
			universityFilter, qualificationFilter;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		jobMatcher = new JobMatcher(candidateEducationService, candidateLanguageService, jobFilterParser,
				filterRepository, courseRepository, qualificationRepository, collegeRepository, universityRepository,
				genderRepository, languageRepository,jobRepository,jobSearchRepository,matchUtils,candidateJobRepository,candidateRepository,applicationEventPublisher);	
		
		filterRepository.deleteAll();
		courseRepository.deleteAll();
		qualificationRepository.deleteAll();
		universityRepository.deleteAll();
		genderRepository.deleteAll();
		languageRepository.deleteAll();
		jobRepository.deleteAll();
		candidateRepository.deleteAll();
		female = genderRepository.saveAndFlush(createFemaleGender(em));
		male = genderRepository.saveAndFlush(createMaleGender(em));
		english = languageRepository.saveAndFlush(createEnglishLanguage(em));
		hindi = languageRepository.saveAndFlush(createHindiLanguage(em));
		punjabi = languageRepository.saveAndFlush(createPunjabiLanguage(em));
		marathi = languageRepository.saveAndFlush(createMarathiLanguage(em));
		sanskrit = languageRepository.saveAndFlush(createSanskritLanguage(em));
		uniIIM = universityRepository.saveAndFlush(createIIMUniversity(em));
		uniUNIVERSITY_OF_DELHI = universityRepository.saveAndFlush(createUniofDelhi(em));
		uniUNIVERSITY_OF_MUMBAI = universityRepository.saveAndFlush(createUniOfMumabi(em));
		uniA = universityRepository.saveAndFlush(createUniA(em));
		uniB = universityRepository.saveAndFlush(createUniB(em));
		uniDoon = universityRepository.saveAndFlush(createUniOfDoon(em));
		unia = universityRepository.saveAndFlush(createUnia(em));
		unib = universityRepository.saveAndFlush(createUnib(em));
		uniAMITY = universityRepository.saveAndFlush(createUniAmity(em));
		/*collegeIIM = collegeRepository.saveAndFlush(createABCCollege(em));
		collegeMIRANDAHOUSE = collegeRepository.saveAndFlush(createMirandaCollege(em));
		collegeAMITY = collegeRepository.saveAndFlush(createAmityCollege(em));
		collegeA = collegeRepository.saveAndFlush(createACollege(em));
		collegeB = collegeRepository.saveAndFlush(createBCollege(em));
		collegeABC = collegeRepository.saveAndFlush(createABCCollege(em));
		collegeStTHOMAS = collegeRepository.saveAndFlush(createStThomasCollege(em));*/
		qualBACHELORS = qualificationRepository.saveAndFlush(createBacheloraQualification(em));
		qualMASTERS = qualificationRepository.saveAndFlush(createMasterQualification(em));
		qualDOCTRATE = qualificationRepository.saveAndFlush(createDoctrateQualification(em));
		qualPG_DIPLOMA = qualificationRepository.saveAndFlush(createPGDiplomaQualification(em));
		qualDIPLOMA = qualificationRepository.saveAndFlush(createDiplomaQualification(em));
		qualISC = qualificationRepository.saveAndFlush(createISCQualification(em));
		qualICSE = qualificationRepository.saveAndFlush(createICSEaQualification(em));
		courseFOOD_TECH = courseRepository.saveAndFlush(createFoodTechCourse(em));
		courseMEDICAL = courseRepository.saveAndFlush(createMedicalCourse(em));
		coursePHARMA = courseRepository.saveAndFlush(createPharmaCourse(em));
		courseBUSINESS_ADM = courseRepository.saveAndFlush(createBusAdmCourse(em));
		courseCOMPUTER = courseRepository.saveAndFlush(createComputerCourse(em));
		courseICSE = courseRepository.saveAndFlush(createICSECourse(em));
		courseISC = courseRepository.saveAndFlush(createISCCourse(em));
		courseENGG = courseRepository.saveAndFlush(createEnggCourse(em));
		gradDateFilter = filterRepository.saveAndFlush(createGradDateFilter(em));
		scoreFilter = filterRepository.saveAndFlush(createScoreFilter(em));
		courseFilter = filterRepository.saveAndFlush(createCourseFilter(em));
		genderFilter = filterRepository.saveAndFlush(createGenderFilter(em));
		languageFilter = filterRepository.saveAndFlush(createLanguagefilter(em));
		collegeFilter = filterRepository.saveAndFlush(createCollegeFilter(em));
		universityFilter = filterRepository.saveAndFlush(createUniversityFilter(em));
		qualificationFilter = filterRepository.saveAndFlush(createQualificationFilter(em));
		jobA = createJobA(em);
		jobB = createJobB(em);
		jobC = createJobC(em);
		//jobD = createJobD(em);
		//jobE = createJobE(em);
		jobF = createJobF(em);
		jobG = createJobG(em);
		jobZ = createJobZ(em);
		candidateA = createCandidateAProfile(em);
		candidateB = createCandidateBProfile(em);
		candidateC = createCandidateCProfile(em);
		candidateD = createCandidateDProfile(em);
		candidateE = createCandidateEProfile(em);
		candidateF = createCandidateFProfile(em);
		candidateG = createCandidateGProfile(em);
		candidateH = createCandidateHProfile(em);
		// jobH = createJobH(em);
		corporate = new Corporate();
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

	public static Qualification createPGDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(PG_DIPLOMA).weightage(3L);
	}

	public static Qualification createBacheloraQualification(EntityManager em) {
		return new Qualification().qualification(BACHELORS).weightage(2L);
	}

	public static Qualification createMasterQualification(EntityManager em) {
		return new Qualification().qualification(MASTERS).weightage(4L);
	}

	public static Qualification createDoctrateQualification(EntityManager em) {
		return new Qualification().qualification(DOCTRATE).weightage(5L);
	}

	public static Qualification createDiplomaQualification(EntityManager em) {
		return new Qualification().qualification(DIPLOMA).weightage(1L);
	}

	public static Qualification createISCQualification(EntityManager em) {
		return new Qualification().qualification(ISC).weightage(0L);
	}

	public static Qualification createICSEaQualification(EntityManager em) {
		return new Qualification().qualification(ICSE).weightage(0L);
	}

	public static Course createComputerCourse(EntityManager em) {
		return new Course().course(COMPUTER);
	}

	public static Course createEnggCourse(EntityManager em) {
		return new Course().course(ENGG);
	}

	public static Course createBusAdmCourse(EntityManager em) {
		return new Course().course(BUSINESS_ADM);
	}

	public static Course createISCCourse(EntityManager em) {
		return new Course().course(ISC);
	}

	public static Course createICSECourse(EntityManager em) {
		return new Course().course(ICSE);
	}

	public static Course createPharmaCourse(EntityManager em) {
		return new Course().course(PHARMA);
	}

	public static Course createFoodTechCourse(EntityManager em) {
		return new Course().course(FOOD_TECH);
	}

	public static Course createMedicalCourse(EntityManager em) {
		return new Course().course(MEDICAL);
	}

	public static University createIIMUniversity(EntityManager em) {
		University university = new University().universityName(IIM);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(IIM).university(university));
		return university.colleges(set);
	}

	public static University createUniofDelhi(EntityManager em) {
		University university = new University().universityName(UNIVERSITY_OF_DELHI);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(MIRANDAHOUSE).university(university));
		return university.colleges(set);
	}

	public static University createUniOfMumabi(EntityManager em) {
		University university = new University().universityName(UNIVERSITY_OF_MUMBAI);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(ABC).university(university));
		return university.colleges(set);
	}

	public static University createUniA(EntityManager em) {
		University university = new University().universityName(A);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(AA).university(university));
		return university.colleges(set);
	}

	public static University createUniB(EntityManager em) {
		University university = new University().universityName(B);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(BB).university(university));
		return university.colleges(set);
	}

	public static University createUniOfDoon(EntityManager em) {
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(StTHOMAS));
		return new University().universityName(DOON).colleges(set);
	}

	public static University createUnia(EntityManager em) {
		University university = new University().universityName(a);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(A).university(university));
		return university.colleges(set);
	}

	public static University createUnib(EntityManager em) {
		University university = new University().universityName(b);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(B).university(university));
		return university.colleges(set);
	}

	public static University createUniAmity(EntityManager em) {
		University university = new University().universityName(AMITY);
		Set<College> set = new HashSet<>();
		set.add(new College().collegeName(AMITY).university(university));
		return university.colleges(set);
	}
	public static Gender createMaleGender(EntityManager em) {
		return new Gender().gender(MALE);
	}

	public static Gender createFemaleGender(EntityManager em) {
		return new Gender().gender(FEMALE);
	}

	public static Language createHindiLanguage(EntityManager em) {
		return new Language().language("Hindi");
	}

	public static Language createEnglishLanguage(EntityManager em) {
		return new Language().language("English");
	}

	public static Language createSanskritLanguage(EntityManager em) {
		return new Language().language("Sanskrit");
	}

	public static Language createMarathiLanguage(EntityManager em) {
		return new Language().language("Marathi");
	}

	public static Language createPunjabiLanguage(EntityManager em) {
		return new Language().language("Punjabi");
	}

	public static Candidate createCandidateAProfile(EntityManager em) {
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<CandidateLanguageProficiency>();
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateA = new Candidate().firstName(CANDIDATE_A).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualPG_DIPLOMA)
				.course(courseBUSINESS_ADM).percentage(80d).highestQualification(true)
				.educationFromDate(LocalDate.of(2017, 02, 25)).educationToDate(LocalDate.of(2018, 02, 24))
				.college(uniIIM.getColleges().iterator().next()).candidate(candidateA);

		CandidateLanguageProficiency languageProfA = new CandidateLanguageProficiency().language(hindi)
				.proficiency("Expert").candidate(candidateA);
		CandidateLanguageProficiency languageProfB = new CandidateLanguageProficiency().language(english)
				.proficiency("Expert").candidate(candidateA);
		CandidateLanguageProficiency languageProfC = new CandidateLanguageProficiency().language(sanskrit)
				.proficiency("Intermediate").candidate(candidateA);
		candidateLanguageProficiencies.add(languageProfA);
		candidateLanguageProficiencies.add(languageProfB);
		candidateLanguageProficiencies.add(languageProfC);
		CandidateEducation education2 = new CandidateEducation().qualification(qualBACHELORS).course(courseCOMPUTER)
				.grade(8.0).college(uniUNIVERSITY_OF_DELHI.getColleges().iterator().next()).candidate(candidateA);
		CandidateEducation education3 = new CandidateEducation().qualification(qualICSE).course(courseICSE)
				.percentage(89d).college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2001, 02, 25))
				.educationToDate(LocalDate.of(2005, 02, 24)).candidate(candidateA);
		CandidateEducation education4 = new CandidateEducation().qualification(qualISC).course(courseISC)
				.percentage(79d).college(uniDoon.getColleges().iterator().next()).educationFromDate(LocalDate.of(2010, 02, 25))
				.educationToDate(LocalDate.of(2007, 02, 24)).candidate(candidateA);
		candidateEducations.add(education1);
		candidateEducations.add(education2);
		candidateEducations.add(education3);
		candidateEducations.add(education4);
		candidateA.setCandidateLanguageProficiencies(candidateLanguageProficiencies);
		candidateA.setEducations(candidateEducations);
	
		return candidateA;
	}
	
	

	public static Candidate createCandidateBProfile(EntityManager em) {
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<CandidateLanguageProficiency>();
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateB = new Candidate().firstName(CANDIDATE_B).matchEligible(true);
		CandidateLanguageProficiency languageProfA = new CandidateLanguageProficiency().language(hindi)
				.proficiency("Expert").candidate(candidateB);
		CandidateLanguageProficiency languageProfB = new CandidateLanguageProficiency().language(english)
				.proficiency("Expert").candidate(candidateB);
		CandidateLanguageProficiency languageProfC = new CandidateLanguageProficiency().language(marathi)
				.proficiency("Intermediate").candidate(candidateB);
		candidateLanguageProficiencies.add(languageProfA);
		candidateLanguageProficiencies.add(languageProfB);
		candidateLanguageProficiencies.add(languageProfC);
		CandidateEducation education = new CandidateEducation().qualification(qualMASTERS).course(coursePHARMA)
				.percentage(79d).highestQualification(true).educationFromDate(LocalDate.of(2016, 02, 25))
				.educationToDate(LocalDate.of(2018, 02, 24)).college(uniUNIVERSITY_OF_MUMBAI.getColleges().iterator().next()).candidate(candidateB);
		candidateEducations.add(education);
		candidateB.setEducations(candidateEducations);
		candidateB.setCandidateLanguageProficiencies(candidateLanguageProficiencies);
		return candidateB;
	}

	public static Candidate createCandidateCProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateC = new Candidate().firstName(CANDIDATE_C).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualDOCTRATE).course(courseFOOD_TECH)
				.percentage(80d).highestQualification(true).educationFromDate(LocalDate.of(2017, 02, 25))
				.educationToDate(LocalDate.of(2018, 06, 24)).college(uniAMITY.getColleges().iterator().next()).candidate(candidateC);
		candidateEducations.add(education1);
		candidateC.setEducations(candidateEducations);
		return candidateC;
	}

	public static Candidate createCandidateDProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateD = new Candidate().firstName(CANDIDATE_D).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualMASTERS).course(courseFOOD_TECH)
				.percentage(78d).highestQualification(true).educationFromDate(LocalDate.of(2017, 02, 25))
				.educationToDate(LocalDate.of(2018, 02, 24)).college(uniAMITY.getColleges().iterator().next()).candidate(candidateD);
		candidateEducations.add(education1);
		candidateD.setEducations(candidateEducations);
		return candidateD;
	}

	public static Candidate createCandidateEProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateE = new Candidate().firstName(CANDIDATE_E).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualBACHELORS).course(coursePHARMA)
				.percentage(68d).educationFromDate(LocalDate.of(2016, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).college(uniAMITY.getColleges().iterator().next()).candidate(candidateE);
		CandidateEducation education2 = new CandidateEducation().qualification(qualDOCTRATE).course(courseMEDICAL)
				.percentage(70d).highestQualification(true).educationFromDate(LocalDate.of(2016, 02, 25))
				.educationToDate(LocalDate.of(2017, 02, 24)).college(uniAMITY.getColleges().iterator().next()).candidate(candidateE);
		candidateEducations.add(education1);
		candidateEducations.add(education2);
		candidateE.setEducations(candidateEducations);
		return candidateE;
	}

	public static Candidate createCandidateFProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateF = new Candidate().firstName(CANDIDATE_F).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualICSE).course(courseICSE)
				.percentage(50d).highestQualification(true).educationFromDate(LocalDate.of(2017, 02, 25))
				.educationToDate(LocalDate.of(2018, 02, 24)).college(uniDoon.getColleges().iterator().next()).candidate(candidateF);
		candidateEducations.add(education1);
		candidateF.setEducations(candidateEducations);
		return candidateF;
	}

	public static Candidate createCandidateGProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Candidate candidateG = new Candidate().firstName(CANDIDATE_G).matchEligible(true);
		CandidateEducation education1 = new CandidateEducation().qualification(qualMASTERS).course(courseENGG)
				.percentage(68d).highestQualification(true).educationFromDate(LocalDate.of(2017, 02, 25))
				.educationToDate(LocalDate.of(2018, 02, 24)).college(uniA.getColleges().iterator().next()).candidate(candidateG);
		candidateEducations.add(education1);
		candidateG.setEducations(candidateEducations);
		return candidateG;
	}

	public static Candidate createCandidateHProfile(EntityManager em) {
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>();
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<CandidateLanguageProficiency>();
		Candidate candidateH = new Candidate().firstName(CANDIDATE_H).matchEligible(true);
		CandidateLanguageProficiency languageProfA = new CandidateLanguageProficiency().language(hindi)
				.proficiency("Expert").candidate(candidateH);
		CandidateLanguageProficiency languageProfB = new CandidateLanguageProficiency().language(english)
				.proficiency("Expert").candidate(candidateH);
		CandidateLanguageProficiency languageProfC = new CandidateLanguageProficiency().language(punjabi)
				.proficiency("Intermediate").candidate(candidateH);
		candidateLanguageProficiencies.add(languageProfA);
		candidateLanguageProficiencies.add(languageProfB);
		candidateLanguageProficiencies.add(languageProfC);
		CandidateEducation education1 = new CandidateEducation().qualification(qualMASTERS).course(courseCOMPUTER)
				.percentage(80d).highestQualification(true).educationFromDate(LocalDate.of(2017, 02, 25))
				.educationToDate(LocalDate.of(2018, 02, 24)).college(uniUNIVERSITY_OF_DELHI.getColleges().iterator().next()).candidate(candidateH);
		candidateEducations.add(education1);
		candidateH.setEducations(candidateEducations);
		candidateH.setCandidateLanguageProficiencies(candidateLanguageProficiencies);
		return candidateH;
	}

	public static Job createJobA(EntityManager em) {
		Job jobA = new Job().jobTitle(JOB_A);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\":[{\"value\":\"MIRANDA HOUSE\",\"display\":\"MIRANDA HOUSE\"}],\"universities\":[{\"value\":\"UNIVERSITY OF DELHI\",\"display\":\"UNIVERSITY OF DELHI\"}],\"premium\": true,\"courses\":[{\"value\":\"COMPUTER\",\"display\":\"COMPUTER\"}],\"qualifications\":[{\"value\":\"MASTERS\",\"display\":\"MASTERS\"}],\"scoreType\": \"percent\",\"percentage\": \"80\",\"addOn\": true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\": 2017,\"month\": 7,\"day\": 11},\"languages\":[{\"value\":\"Hindi\",\"display\":\"Hindi\"},{\"value\":\"English\",\"display\":\"English\"},{\"value\":\"Punjabi\",\"display\":\"Punjabi\"}]}";
		jobFilter.filterDescription(filterDescription).job(jobA);
		jobFilters.add(jobFilter);
		jobA.setJobFilters(jobFilters);
		return jobA;
	}
	
	public static Job createJobZ(EntityManager em) {
		Job jobZ = new Job().jobTitle(JOB_Z);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\":[{\"value\":\"MIRANDA HOUSE\",\"display\":\"MIRANDA HOUSE\"}],\"universities\":[{\"value\":\"UNIVERSITY OF DELHI\",\"display\":\"UNIVERSITY OF DELHI\"}],\"premium\": true,\"courses\":[{\"value\":\"COMPUTER\",\"display\":\"COMPUTER\"}],\"qualifications\":[{\"value\":\"MASTERS\",\"display\":\"MASTERS\"}],\"scoreType\": \"percent\",\"percentage\": \"80\",\"addOn\": true,\"languages\":[{\"value\":\"Hindi\",\"display\":\"Hindi\"},{\"value\":\"English\",\"display\":\"English\"},{\"value\":\"Punjabi\",\"display\":\"Punjabi\"}]}";
		jobFilter.filterDescription(filterDescription).job(jobZ);
		jobFilters.add(jobFilter);
		jobZ.setJobFilters(jobFilters);
		return jobZ;
	}

	public static Job createJobB(EntityManager em) {
		Job jobB = new Job().jobTitle(JOB_B);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"XYZ\",\"display\": \"XYZ\"}],\"universities\": [{\"value\": \"UNIVERSITY OF MUMBAI\",\"display\": \"UNIVERSITY OF MUMBAI\"}],\"premium\": true,\"courses\": [{\"value\":\"PHARMA\",\"display\": \"PHARMA\"}],\"qualifications\": [{\"value\": \"Diploma\",\"display\": \"Diploma\"}],\"scoreType\":\"percent\",\"percentage\": \"75\",\"addOn\": true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\":2017,\"month\": 7,\"day\": 11},\"languages\": [{\"value\":\"Hindi\",\"display\": \"Hindi\"},{\"value\": \"Marathi\",\"display\":\"Marathi\"}]}";
		jobFilter.filterDescription(filterDescription).job(jobB);
		jobFilters.add(jobFilter);
		jobB.setJobFilters(jobFilters);
		return jobB;
	}


	public static Job createJobC(EntityManager em) {
		Job jobE = new Job().jobTitle(JOB_E);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"greater\",\"graduationDate\": {\"year\":2017,\"month\": 7,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobE);
		jobFilters.add(jobFilter);
		jobE.setJobFilters(jobFilters);
		return jobE;

	}

	public static Job createJobF(EntityManager em) {
		Job jobF = new Job().jobTitle(JOB_F);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"less\",\"graduationDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobF);
		jobFilters.add(jobFilter);
		jobF.setJobFilters(jobFilters);
		return jobF;
	}

	public static Job createJobG(EntityManager em) {
		Job jobG = new Job().jobTitle(JOB_G);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter();
		String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"between\",\"graduationFromDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"graduationToDate\": {\"year\":2018,\"month\": 3,\"day\": 11},\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
		jobFilter.filterDescription(filterDescription).job(jobG);
		jobFilters.add(jobFilter);
		jobG.setJobFilters(jobFilters);
		return jobG;
	}

	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testMatchingOnCreatingJobAWithCollegeCoursePercentageQualificationWithinCandidatesWithinGraduationDateRangeAndThreeLanguages() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobA.setCorporate(corporateRepository.save(corporate));	
		jobRepository.saveAndFlush(jobA);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
		CandidateJob candidateJob1 = new CandidateJob(candidateA, jobA);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob1);
		jobA.setCandidateJobs(candidateJobs);
		jobRepository.saveAndFlush(jobA);
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		jobMatcher.match(jobA);
		System.out.println("Returning from non wainting call");
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
	
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(7);
		assertThat(matchedJob.getCandidateJobs())
			.extracting("candidate.firstName","matchScore","educationMatchScore","languageMatchScore","genderMatchScore","totalEligibleScore")
			.contains(tuple(CANDIDATE_H,100.0,40.0,5.0,null,45.0),tuple(CANDIDATE_A,42.0,16.0,3.0,null,45.0),
				tuple(CANDIDATE_B,42.0,16.0,3.0,null,45.0),tuple(CANDIDATE_C,36.0,16.0,0.0,null,45.0),tuple(CANDIDATE_D,33.0,15.0,0.0,null,45.0),
				tuple(CANDIDATE_F,11.0,5.0,0.0,null,45.0),tuple(CANDIDATE_G,20.0,9.0,0.0,null,45.0));
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);

	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testMatchingOnCreatingJobZWithCollegeCoursePercentageQualificationWithinCandidatesWithinGraduationDateRangeAndThreeLanguagesJobWithoutDateFilter() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobZ.setCorporate(corporateRepository.save(corporate));	
		//jobRepository.saveAndFlush(jobZ);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
	/*	CandidateJob candidateJob1 = new CandidateJob(candidateA, jobZ);
		candidateJob1.setMatchScore(47.0);
		candidateJob1.setEducationMatchScore(40.0);
		candidateJob1.setLanguageMatchScore(3.0);
		candidateJob1.setGenderMatchScore(4.0);
		Set<CandidateJob> candidateJobs = new HashSet<>();
		candidateJobs.add(candidateJob1);
		jobZ.setCandidateJobs(candidateJobs);*/
		jobRepository.saveAndFlush(jobZ);
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		assertThat(candidateRepository.findAll().size()).isEqualTo(8);
		jobMatcher.match(jobZ);
		//System.out.println("Returning from non wainting call");
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
	
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(8);
		assertThat(matchedJob.getCandidateJobs())
			.extracting("candidate.firstName","matchScore","educationMatchScore","languageMatchScore","genderMatchScore","totalEligibleScore")
			.contains(tuple(CANDIDATE_H,100.0,40.0,5.0,null,45.0),tuple(CANDIDATE_A,42.0,16.0,3.0,null,45.0),
					tuple(CANDIDATE_B,42.0,16.0,3.0,null,45.0),tuple(CANDIDATE_C,36.0,16.0,0.0,null,45.0),tuple(CANDIDATE_D,33.0,15.0,0.0,null,45.0),
					tuple(CANDIDATE_F,11.0,5.0,0.0,null,45.0),tuple(CANDIDATE_G,20.0,9.0,0.0,null,45.0),tuple(CANDIDATE_E,18.0, 8.0,0.0,null,45.0));
		/*	.contains(tuple(CANDIDATE_H,100.0,40.0,5.0,0.0,45.0),tuple(CANDIDATE_A,42.0,16.0,3.0,0.0,45.0),
				tuple(CANDIDATE_B,42.0,16.0,3.0,0.0,45.0),tuple(CANDIDATE_C,36.0,16.0,0.0,0.0,45.0),tuple(CANDIDATE_D,33.0,15.0,0.0,0.0,45.0),
				tuple(CANDIDATE_F,11.0,5.0,0.0,0.0,45.0),tuple(CANDIDATE_G,20.0,9.0,0.0,0.0,45.0),tuple(CANDIDATE_E,20.0,9.0,0.0,0.0,45.0));*/
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);

	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testMatchingOnCreatingJobBWithCollegeCoursePercentageQualificationWithinCandidatesWithinGraduationDateRangeAndTwoLanguages() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobB.setCorporate(corporateRepository.save(corporate));	
		jobRepository.saveAndFlush(jobB);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		jobMatcher.match(jobB);
		
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(7);
		assertThat(matchedJob.getCandidateJobs()).extracting("candidate.firstName","matchScore").contains(tuple(CANDIDATE_H,38.0),tuple(CANDIDATE_A,40.0),
				tuple(CANDIDATE_B,80.0),tuple(CANDIDATE_C,29.0),tuple(CANDIDATE_D,31.0),tuple(CANDIDATE_F,18.0),tuple(CANDIDATE_G,13.0));
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);


	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testMatchingOnCreatingJobCWithMultipleCollegeCoursePercentageQualificationWithinCandidatesWithinGraduationDateRangeAndGender() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobB.setCorporate(corporateRepository.save(corporate));	
		jobRepository.saveAndFlush(jobC);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		jobMatcher.match(jobC);
		
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(7);
		assertThat(matchedJob.getCandidateJobs()).extracting("candidate.firstName","matchScore").contains(tuple(CANDIDATE_H,39.0),tuple(CANDIDATE_A,36.0),
				tuple(CANDIDATE_B,70.0),tuple(CANDIDATE_C,36.0),tuple(CANDIDATE_D,39.0),tuple(CANDIDATE_F,16.0),tuple(CANDIDATE_G,57.0));
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);


	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testFilterLessThanGraduationDate() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobB.setCorporate(corporateRepository.save(corporate));	
		jobRepository.saveAndFlush(jobF);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		jobMatcher.match(jobF);
		
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(1);
		assertThat(matchedJob.getCandidateJobs()).extracting("candidate.firstName").contains(CANDIDATE_E);
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);


	}
	
	@Test
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void testFilterWithinGraduationDateRange() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobService.getAllJobs().size();
		int candidateDatabaseSizeBeforeCreate = candidateRepository.findAll().size();
		jobB.setCorporate(corporateRepository.save(corporate));	
		jobRepository.saveAndFlush(jobG);
		candidateRepository.saveAndFlush(candidateA.gender(male));
		candidateRepository.saveAndFlush(candidateB.gender(female));
		candidateRepository.saveAndFlush(candidateC.gender(male));
		candidateRepository.saveAndFlush(candidateD.gender(male));
		candidateRepository.saveAndFlush(candidateE.gender(female));
		candidateRepository.saveAndFlush(candidateF.gender(male));
		candidateRepository.saveAndFlush(candidateG.gender(male));
		candidateRepository.saveAndFlush(candidateH.gender(male));
		
		int courseList = courseRepository.findAll().size();
		assertThat(courseList).isEqualTo(8);
		jobMatcher.match(jobG);
		
		List<Job> jobList = jobRepository.findAll();
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateEducation> candidateEducationList = candidateEducationService.getAllCandidateEducations();
		List<CandidateLanguageProficiency> candidateLanguageList = candidateLanguageService
				.getAllCandidateLanguageProficiencies();
		assertThat(candidateList).hasSize(8);
		assertThat(candidateEducationList).hasSize(12);
		assertThat(candidateLanguageList).hasSize(9);
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobList).hasSize(1);
		Job matchedJob = jobList.get(0);
		assertThat(matchedJob.getCandidateJobs()).hasSize(6);
		assertThat(matchedJob.getCandidateJobs()).extracting("candidate.firstName").contains(CANDIDATE_H,CANDIDATE_A,
				CANDIDATE_B,CANDIDATE_D,CANDIDATE_F,CANDIDATE_G);
		assertThat(candidateList).hasSize(candidateDatabaseSizeBeforeCreate + 8);


	}


}
