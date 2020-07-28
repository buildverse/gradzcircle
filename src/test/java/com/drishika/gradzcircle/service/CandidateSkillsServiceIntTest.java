/**
 * 
 */
package com.drishika.gradzcircle.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.elasticsearch.common.lucene.search.Queries;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CandidateSkillsRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.search.CandidateSkillsSearchRepository;
import com.drishika.gradzcircle.repository.search.SkillsSearchRepository;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

/**
 * @author abhinav
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateSkillsServiceIntTest {
	
	
	 private static final String DEFAULT_SKILL = "AAAAAAAAAA";
	    private static final String UPDATED_SKILL = "BBBBBBBBBB";
	    private static final String JOB_A = "JOB_A";
		private static final String JOB_B = "JOB_B";
		private static final String JOB_C = "JOB_C";
		private static final String JOB_F = "JOB_F";
		private static final String JOB_G = "JOB_G";
		private static final String MALE = "MALE";
		private static final String FEMALE = "FEMALE";
		private final static String HINDI = "Hindi";
		private final static String ENGLISH = "English";
		private final static String MARATHI = "Marathi";

	    @Autowired
	    private CandidateSkillsRepository candidateSkillsRepository;
	   
	    CandidateSkillsService candidateSkillsService;
	    
		@Autowired
		private LanguageRepository languageRepository;
		
		@Autowired
		private GenderRepository genderRepository;
		
		@Autowired 
		private SkillsRepository skillsRepository;
		
		@Autowired
		private FilterRepository filterRepository;
		

		@Autowired
		private JobRepository jobRepository;

		
		@Mock
		ElasticsearchTemplate elasticSearchTemplate;
		
		CandidateSkills candidateSkills;


	    @Autowired
	    private CandidateSkillsSearchRepository candidateSkillsSearchRepository;
	    
	    @Autowired
		private ProfileCategoryRepository profileCategoryRepository;
	    
		private ProfileCategory basic, personal, edu, exp, lang, cert, nonAcad,skills;
		
		private Skills msWord,msExcel,otherSkill,yoyoSkill,dataScience1,dataScience2;
		
		private Filter gradDateFilter, scoreFilter, courseFilter, genderFilter, languageFilter, collegeFilter,
		universityFilter, qualificationFilter, skillFilter;
		
		private Job jobA, jobB, jobC, jobD, jobE, jobF, jobG, jobH;
		private Gender maleGender, femaleGender;
		private Language hindiLanguage, englishLanguage, marathiLanguage;
		private Candidate candidate;

	    @Autowired
	    private EntityManager em;
	    
	    
	    @Autowired
	    private CandidateRepository candidateRepository;

	    @Autowired
	    private ProfileScoreCalculator profileScoreCalculator;
	  
		@Autowired @Qualifier("CandidateSkillMatcher")Matcher<Long> matcher;
		
		@Autowired
		private DTOConverters converter;
		
	    @Before
	    public void setup() {
	        MockitoAnnotations.initMocks(this);
	        candidateSkillsService = new CandidateSkillsService(candidateSkillsRepository, candidateSkillsSearchRepository, 
	        		profileScoreCalculator, skillsRepository, candidateRepository, converter, elasticSearchTemplate, matcher);
	    }

	    /**
	     * Create an entity for this test.
	     *
	     * This is a static method, as tests for other entities might also need it,
	     * if they test an entity which requires the current entity.
	     */
	    public static CandidateSkills createEntity(EntityManager em) {
	        CandidateSkills candidateSkills = new CandidateSkills();
	        return candidateSkills;
	    }
	    
	    public static Candidate createCandidate(EntityManager em) {
	        Candidate candidate = new Candidate()
	            .firstName("Abhinav");
	        return candidate;
	    }
	    
	    public static Skills createMsWordSkill(EntityManager em) {
	    	return new Skills().skill("MSWORD");
	    }
	    
	    public static Skills createDataScienceSkill(EntityManager em) {
	    	return new Skills().skill("Datascience");
	    }
	    
	    public static Skills createMsExcelSkill(EntityManager em) {
	    	return new Skills().skill("MSEXCEL");
	    }
	    
	    public static Skills createOtherSkill(EntityManager em) {
	    	return new Skills().skill("Other");
	    }
	    
	    public static Skills createYoYoSkill(EntityManager em) {
	    	return new Skills().skill("YoYo");
	    }
	    
	    public static Skills createYoYoMeriJaanSkill(EntityManager em) {
	    	return new Skills().skill("YoYo Meri Jaan");
	    }
	    
	    
		public static Gender createMaleGender(EntityManager em) {
			return new Gender().gender(MALE);
		}

		public static Gender createFemaleGender(EntityManager em) {
			return new Gender().gender(FEMALE);
		}
		
		public static Filter createSkillfilter(EntityManager em) {
			return new Filter().filterName("skill").matchWeight(10L);
		}
		
	    public static ProfileCategory createBasicProfile(EntityManager em) {
			return new ProfileCategory().categoryName(Constants.CANDIDATE_BASIC_PROFILE).weightage(5);
		}
		
		public static ProfileCategory createCertProfile(EntityManager em) {
			return new ProfileCategory().categoryName(Constants.CANDIDATE_CERTIFICATION_PROFILE).weightage(5);
		}
		
		public static ProfileCategory createEduProfile(EntityManager em) {
			return new ProfileCategory().categoryName(Constants.CANDIDATE_EDUCATION_PROFILE).weightage(30);
		}
		
		public static ProfileCategory createSkillProfile(EntityManager em) {
			return new ProfileCategory().categoryName(Constants.CANDIDATE_SKILL_PROFILE).weightage(20);
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
		
		public static Language createHindiLanguae(EntityManager em) {
			return new Language().language(HINDI);
		}

		public static Language createMarathiLanguage(EntityManager em) {
			return new Language().language(MARATHI);
		}

		public static Language createEnglishLanguage(EntityManager em) {
			return new Language().language(ENGLISH);
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
			String filterDescription = "{\"basic\": true,\"colleges\": [{\"value\": \"A\",\"display\": \"A\"},{\"value\": \"B\",\"display\": \"B\"}],\"universities\": [{\"value\":\"a\",\"display\": \"a\"},{\"value\":\"b\",\"display\": \"b\"}],\"premium\": true,\"courses\": [{\"value\": \"PHARMA\",\"display\": \"PHARMA\"},{\"value\":\"MEDICAL\",\"display\": \"MEDICAL\"},{\"value\":\"ENGG\",\"display\": \"ENGG\"}],\"qualifications\": [{\"value\":\"BACHELORS\",\"display\": \"BACHELORS\"},{\"value\":\"MASTERS\",\"display\": \"MASTERS\"}],\"scoreType\":\"gpa\",\"gpa\": \"7.0\",\"addOn\":true,\"graduationDateType\": \"less\",\"graduationDate\": {\"year\":2017,\"month\": 3,\"day\": 11},\"skills\":[{\"value\":\"MSEXCEL\",\"display\":\"MSEXCEL\"},{\"value\":\"MSWORD\",\"display\":\"MSWORD\"}],\"languages\":[{\"value\":\"English\",\"display\":\"English\"}],\"gender\":{\"id\":2,\"gender\":\"FEMALE\"}}";
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
		
		public static Filter createSkillFilter(EntityManager em) {
			return new Filter().filterName("skill").matchWeight(10L);
		}

	    @Before
	    public void initTest() {
	        candidateSkillsSearchRepository.deleteAll();
	        candidateSkills = createEntity(em);
	        msExcel = createMsExcelSkill(em);
	        msWord = createMsWordSkill(em);
	        otherSkill = createOtherSkill(em);
	        yoyoSkill = createYoYoSkill(em);
	        dataScience1 = createDataScienceSkill(em);
	        jobA = createJobA(em);
			jobB = createJobB(em);
			jobC = createJobC(em);
			jobF = createJobF(em);
			jobG = createJobG(em);
			candidate = createCandidate(em);
	        basic = createBasicProfile(em);
			personal = createPersonalProfile(em);
			cert=createCertProfile(em);
			exp = createExpProfile(em);
			nonAcad = createNonAcadProfile(em);
			edu = createEduProfile(em);
			lang = createLangProfile(em);
			skills = createSkillProfile(em);
			qualificationFilter = createQualificationFilter(em);
			courseFilter = createCourseFilter(em);
			collegeFilter = createCollegeFilter(em);
			universityFilter = createUniversityFilter(em);
			gradDateFilter = createGradDateFilter(em);
			scoreFilter = createScoreFilter(em);
			languageFilter = createLanguagefilter(em);
			genderFilter = createGenderFilter(em);
			skillFilter = createSkillFilter(em);
			hindiLanguage = createHindiLanguae(em);
			englishLanguage = createEnglishLanguage(em);
			marathiLanguage = createMarathiLanguage(em);
			maleGender = createMaleGender(em);
			femaleGender = createFemaleGender(em);
			qualificationFilter = createQualificationFilter(em);
			profileCategoryRepository.saveAndFlush(basic);
			profileCategoryRepository.saveAndFlush(personal);
			profileCategoryRepository.saveAndFlush(cert);
			profileCategoryRepository.saveAndFlush(exp);
			profileCategoryRepository.saveAndFlush(edu);
			profileCategoryRepository.saveAndFlush(nonAcad);
			profileCategoryRepository.saveAndFlush(lang);
			profileCategoryRepository.saveAndFlush(skills);
			genderRepository.saveAndFlush(maleGender);
			genderRepository.saveAndFlush(femaleGender);
			candidateRepository.saveAndFlush(candidate);
			languageRepository.saveAndFlush(hindiLanguage);
			languageRepository.saveAndFlush(englishLanguage);
			languageRepository.saveAndFlush(marathiLanguage);
			skillsRepository.saveAndFlush(msExcel);
			skillsRepository.saveAndFlush(msWord);
			skillsRepository.saveAndFlush(yoyoSkill);
			skillsRepository.saveAndFlush(otherSkill);
			skillsRepository.saveAndFlush(dataScience1);
			filterRepository.saveAndFlush(qualificationFilter);
			filterRepository.saveAndFlush(courseFilter);
			filterRepository.saveAndFlush(gradDateFilter);
			filterRepository.saveAndFlush(genderFilter);
			filterRepository.saveAndFlush(collegeFilter);
			filterRepository.saveAndFlush(universityFilter);
			filterRepository.saveAndFlush(scoreFilter);
			filterRepository.saveAndFlush(languageFilter);
			filterRepository.saveAndFlush(skillFilter);
			jobRepository.saveAndFlush(jobA);
			jobRepository.saveAndFlush(jobB);
			jobRepository.saveAndFlush(jobF);
			jobRepository.saveAndFlush(jobG);

	    }
	    
	    @Test
	    @Transactional
	    public void whenCreateAddtionalOtherAndExisintgSkillsWithoutEducationWithExistingSkillEmploymentAndBasicProfileMustMaintainProfileScoreAs55() throws Exception {
	    		Candidate candidate = new Candidate().firstName("Abhinav");
	    		candidateRepository.saveAndFlush(candidate);
	    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
	    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
	    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
	    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
	    		List<Skills> cSkills = new ArrayList<>();
	    		cSkills.add(otherSkill);
	    		Skills skill = new Skills();
	    		skill.setSkill("MSEXCEL");
	    		cSkills.add(skill);
	    		scoreBasic.setScore(5d);
	    		scorePersonal.setScore(15d);
	    		scoreEmployment.setScore(15d);
	    		scoreSkill.setScore(20d);
	    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal).addCandidateProfileScore(scoreSkill);
	    		candidate.setProfileScore(55d);
	    		candidateRepository.saveAndFlush(candidate);
	    		candidateSkills.candidate(candidate);
	    		
	    		candidateSkills.setSkillsList(cSkills);
	    		candidateSkillsService.createCandidateSkills(candidateSkills.capturedSkills("Yoyo,honey,  diljit"));
	    		
	    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
	    				.findAll();
	    		List<Skills> testSkills = skillsRepository.findAll();
	    		assertThat(candidateSkillList).hasSize(4);
	    		assertThat(testSkills).hasSize(7);
	    		assertThat(testSkills).extracting("skill").contains("YoYo","Honey","Diljit","MSEXCEL");
	    		Candidate testCandidate = candidateSkillList.get(0).getCandidate();
	    		assertThat(candidateSkillList).extracting("skills.skill").contains("YoYo","Honey","Diljit","MSEXCEL");
	    		assertThat(testCandidate).isEqualTo(candidate);
	    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
	    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
	    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
	    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);
	    		verify(elasticSearchTemplate, times(2)).index(any(IndexQuery.class));
	    		verify(elasticSearchTemplate, times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    @Test
	    @Transactional
	    public void whenCreateAddtionalExisintgSkillsWithoutEducationWithExistingSkillEmploymentAndBasicProfileMustMaintainProfileScoreAs55AndMustNotAddTheSkillAgain() throws Exception {
	    		Candidate candidate = new Candidate().firstName("Abhinav");
	    		
	    		CandidateSkills setUpSKill = new CandidateSkills();
	    		setUpSKill.candidate(candidate);
	    		setUpSKill.skills(msExcel);
	    		Set<CandidateSkills> setUpSkills = new HashSet<>();
	    		setUpSkills.add(setUpSKill);
	    		candidate.setCandidateSkills(setUpSkills);
	    		candidateRepository.saveAndFlush(candidate);
	    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
	    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
	    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
	    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
	    		List<Skills> cSkills = new ArrayList<>();
	    		cSkills.add(otherSkill);
	    		Skills skill = new Skills();
	    		skill.setSkill("MSEXCEL");
	    		cSkills.add(skill);
	    		scoreBasic.setScore(5d);
	    		scorePersonal.setScore(15d);
	    		scoreEmployment.setScore(15d);
	    		scoreSkill.setScore(20d);
	    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal).addCandidateProfileScore(scoreSkill);
	    		candidate.setProfileScore(55d);
	    		candidateSkills.candidate(candidate);
	    		candidateSkills.setSkillsList(cSkills);
	    		candidateSkillsService.createCandidateSkills(candidateSkills.capturedSkills("Yoyo,honey,  diljit"));
	    		
	    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
	    				.findAll();
	    		List<Skills> testSkills = skillsRepository.findAll();
	    		assertThat(candidateSkillList).hasSize(4);
	    		assertThat(testSkills).hasSize(7);
	    		assertThat(testSkills).extracting("skill").contains("YoYo","Honey","Diljit","MSEXCEL");
	    		Candidate testCandidate = candidateSkillList.get(0).getCandidate();
	    		assertThat(candidateSkillList).extracting("skills.skill").contains("YoYo","Honey","Diljit","MSEXCEL");
	    		assertThat(testCandidate).isEqualTo(candidate);
	    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
	    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
	    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
	    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);
	    		verify(elasticSearchTemplate, times(2)).index(any(IndexQuery.class));
	    		verify(elasticSearchTemplate, times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    @Test
	    @Transactional
	    public void whenCreateAddtionalOtherAlongWithNonOtherSkillsWithoutEducationWithExistingSkillEmploymentAndBasicProfileMustMaintainProfileScoreAs55() throws Exception {
	    		Candidate candidate = new Candidate().firstName("Abhinav");
	    		candidateRepository.saveAndFlush(candidate);
	    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
	    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
	    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
	    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
	    		List<Skills> cSkills = new ArrayList<>();
	    		cSkills.add(otherSkill);
	    		cSkills.add(msExcel);
	    		cSkills.add(msWord);
	    		scoreBasic.setScore(5d);
	    		scorePersonal.setScore(15d);
	    		scoreEmployment.setScore(15d);
	    		scoreSkill.setScore(20d);
	    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal).addCandidateProfileScore(scoreSkill);
	    		candidate.setProfileScore(55d);
	    		candidateSkills.candidate(candidate);
	    		candidateSkills.setSkillsList(cSkills);
	    		candidateSkillsService.createCandidateSkills(candidateSkills.capturedSkills("Yoyo,honey,  diljit"));
	    		
	    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
	    				.findAll();
	    		List<Skills> testSkills = skillsRepository.findAll();
	    		assertThat(candidateSkillList).hasSize(5);
	    		assertThat(testSkills).hasSize(7);
	    		assertThat(testSkills).extracting("skill").contains("YoYo","Honey","Diljit","MSEXCEL","MSWORD");
	    		Candidate testCandidate = candidateSkillList.get(0).getCandidate();
	    		assertThat(candidateSkillList).extracting("skills.skill").contains("YoYo","Honey","Diljit","MSEXCEL","MSWORD");
	    		assertThat(testCandidate.getCandidateSkills()).extracting("skills.skill").contains("YoYo","Honey","Diljit","MSEXCEL","MSWORD");
	    		assertThat(testCandidate).isEqualTo(candidate);
	    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
	    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
	    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
	    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);
	    		verify(elasticSearchTemplate, times(2)).index(any(IndexQuery.class));
	    		verify(elasticSearchTemplate, times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    
	    @Test
	    @Transactional
	    public void createCandidateSkillsAndReCreateSameSkillViaOtherRoute() throws Exception {
	        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();
	        List<Skills> candidateSkill = new ArrayList<>();
	        candidateSkill.add(msExcel);
	        candidateSkills.setSkillsList(candidateSkill);
	        
	        // Create the CandidateSkills
	        
	        candidateSkillsService.createCandidateSkills(candidateSkills.candidate(candidate));
	        // Validate the CandidateSkills in the database
	        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeCreate + 1);
	        CandidateSkills testCandidateSkills = candidateSkillsList.get(candidateSkillsList.size() - 1);
	        assertThat(testCandidateSkills.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
	        System.out.println("All skills in repo are "+skillsRepository.findAll());
	        List<Skills> cSkills = new ArrayList<>();
			cSkills.add(otherSkill);
			CandidateSkills candidateSkills1 = new CandidateSkills();
			candidateSkills1.candidate(candidate);
			candidateSkills1.setSkillsList(cSkills);
			candidateSkillsService.createCandidateSkills(candidateSkills1.capturedSkills("Data Science, DataScience,Datascience"));
	      
	        List<CandidateSkills> candidateSkillsListAgain = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsListAgain).hasSize(2);
	        CandidateSkills testCandidateSkillsAgain = candidateSkillsListAgain.get(candidateSkillsListAgain.size() - 1);
	      //  assertThat(testCandidateSkillsAgain.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
	        assertThat(skillsRepository.findAll()).hasSize(5);
	        System.out.println("-----------"+skillsRepository.findAll());
	        verify(elasticSearchTemplate, times(0)).index(any(IndexQuery.class));
    		verify(elasticSearchTemplate, times(0)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    @Test
	    @Transactional
	    public void createCandidateSkillsAndReCreateSameSkillDifferentDataCombinationViaOtherRoute() throws Exception {
	        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();
	        List<Skills> candidateSkill = new ArrayList<>();
	        candidateSkill.add(msExcel);
	        candidateSkills.setSkillsList(candidateSkill);
	        
	        Skills skill = new Skills();
	        skill.setSkill("Machine Learning");
	        skillsRepository.saveAndFlush(skill);
	        
	        // Create the CandidateSkills
	        
	        candidateSkillsService.createCandidateSkills(candidateSkills.candidate(candidate));
	        // Validate the CandidateSkills in the database
	        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeCreate + 1);
	        CandidateSkills testCandidateSkills = candidateSkillsList.get(candidateSkillsList.size() - 1);
	        assertThat(testCandidateSkills.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
	        System.out.println("All skills in repo are "+skillsRepository.findAll());
	        List<Skills> cSkills = new ArrayList<>();
			cSkills.add(otherSkill);
			CandidateSkills candidateSkills1 = new CandidateSkills();
			candidateSkills1.candidate(candidate);
			candidateSkills1.setSkillsList(cSkills);
			candidateSkillsService.createCandidateSkills(candidateSkills1.capturedSkills("MachineLearning, machinelearning, machine learning"));
	      
	        List<CandidateSkills> candidateSkillsListAgain = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsListAgain).hasSize(2);
	        CandidateSkills testCandidateSkillsAgain = candidateSkillsListAgain.get(candidateSkillsListAgain.size() - 1);
	      //  assertThat(testCandidateSkillsAgain.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
	        assertThat(candidateSkillsListAgain).extracting("skills.skill").contains("Machine Learning", "MSEXCEL");
	        assertThat(skillsRepository.findAll()).hasSize(6);
	        System.out.println("-----------"+skillsRepository.findAll());
	        verify(elasticSearchTemplate, times(0)).index(any(IndexQuery.class));
    		verify(elasticSearchTemplate, times(0)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    @Test
	    @Transactional
	    public void createCandidateSkillsBullhorAndWORDSkillViaOtherRoute() throws Exception {
	        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();
	        List<Skills> candidateSkill = new ArrayList<>();
	        candidateSkill.add(msExcel);
	        candidateSkills.setSkillsList(candidateSkill);
	        
	        // Create the CandidateSkills
	        
	        candidateSkillsService.createCandidateSkills(candidateSkills.candidate(candidate));
	        // Validate the CandidateSkills in the database
	        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeCreate + 1);
	        CandidateSkills testCandidateSkills = candidateSkillsList.get(candidateSkillsList.size() - 1);
	        assertThat(testCandidateSkills.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
	        
	        List<Skills> cSkills = new ArrayList<>();
			cSkills.add(otherSkill);
			CandidateSkills candidateSkills1 = new CandidateSkills();
			candidateSkills1.candidate(candidate);
			candidateSkills1.setSkillsList(cSkills);
			candidateSkillsService.createCandidateSkills(candidateSkills1.capturedSkills("something,something,something,something, somethingnew, "));
	        List<CandidateSkills> candidateSkillsListAgain = candidateSkillsRepository.findAll();
	        assertThat(candidateSkillsListAgain).hasSize(3);
	        List<Skills> skillList = skillsRepository.findAll();
	        CandidateSkills testCandidateSkillsAgain = candidateSkillsListAgain.get(candidateSkillsListAgain.size() - 1);
	        assertThat(skillsRepository.findAll()).hasSize(7);
	        verify(elasticSearchTemplate, times(2)).index(any(IndexQuery.class));
    		verify(elasticSearchTemplate, times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);

	    }

	    
	    @Test
	    @Transactional
	    public void whenCreateAddtionalOtherSkillsWithoutEducationWithExistingSkillEmploymentAndBasicProfileMustMaintainProfileScoreAs55() throws Exception {
	    		Candidate candidate = new Candidate().firstName("Abhinav");
	    		candidateRepository.saveAndFlush(candidate);
	    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
	    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
	    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
	    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
	    		List<Skills> cSkills = new ArrayList<>();
	    		cSkills.add(otherSkill);
	    		scoreBasic.setScore(5d);
	    		scorePersonal.setScore(15d);
	    		scoreEmployment.setScore(15d);
	    		scoreSkill.setScore(20d);
	    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal).addCandidateProfileScore(scoreSkill);
	    		candidate.setProfileScore(55d);
	    		candidateSkills.candidate(candidate);
	    		candidateSkills.setSkillsList(cSkills);
	    		candidateSkillsService.createCandidateSkills(candidateSkills.capturedSkills("Yoyo,honey,  diljit"));
	    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
	    				.findAll();
	    		List<Skills> testSkills = skillsRepository.findAll();
	    		assertThat(candidateSkillList).hasSize(3);
	    		assertThat(testSkills).hasSize(7);
	    		assertThat(testSkills).extracting("skill").contains("YoYo","Honey","Diljit");
	    		Candidate testCandidate = candidateSkillList.get(0).getCandidate();
	    		assertThat(candidateSkillList).extracting("skills.skill").contains("YoYo","Honey","Diljit");
	    		assertThat(testCandidate).isEqualTo(candidate);
	    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
	    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
	    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
	    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);
	    		verify(elasticSearchTemplate, times(2)).index(any(IndexQuery.class));
	    		verify(elasticSearchTemplate, times(2)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }
	    
	    @Test
	    @Transactional
	    public void whenTryToReCreateASkillUsingOtherThatIsAlreadyPresentInSkillDBSystemMustNotReAdd() throws Exception {
	    		Candidate candidate = new Candidate().firstName("Abhinav");
	    		candidateRepository.saveAndFlush(candidate);
	    		List<Skills> cSkills = new ArrayList<>();
	    		cSkills.add(otherSkill);
	    		candidateSkills.candidate(candidate);
	    		candidateSkills.setSkillsList(cSkills);
	    		candidateSkillsService.createCandidateSkills(candidateSkills.capturedSkills("YoYo meri Jaan,YoYo meri Jaan"));
	    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
	    				.findAll();
	    		List<Skills> testSkills = skillsRepository.findAll();
	    		assertThat(candidateSkillList).hasSize(1);
	    		assertThat(testSkills).hasSize(6);
	    		assertThat(testSkills).extracting("skill").contains("Yoyo Meri Jaan");
	    		Candidate testCandidate = candidateSkillList.get(0).getCandidate();
	    		assertThat(candidateSkillList).extracting("skills.skill").contains("Yoyo Meri Jaan");
	    		assertThat(testCandidate).isEqualTo(candidate);
	    		verify(elasticSearchTemplate, times(1)).index(any(IndexQuery.class));
	    		verify(elasticSearchTemplate, times(1)).refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	    }

}
