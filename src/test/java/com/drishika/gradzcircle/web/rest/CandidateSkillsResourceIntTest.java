package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
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
import com.drishika.gradzcircle.service.CandidateSkillsService;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;


/**
 * Test class for the CandidateSkillsResource REST controller.
 *
 * @see CandidateSkillsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateSkillsResourceIntTest {

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
    
    @Autowired
    private CandidateSkillsService candidateSkillsService;
    
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


    @Autowired
    private CandidateSkillsSearchRepository candidateSkillsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
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

    private MockMvc restCandidateSkillsMockMvc;

    private CandidateSkills candidateSkills;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CandidateSkillsResource candidateSkillsResource = new CandidateSkillsResource(candidateSkillsService);
        this.restCandidateSkillsMockMvc = MockMvcBuilders.standaloneSetup(candidateSkillsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
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
    public void createCandidateSkills() throws Exception {
        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();
        List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(msExcel);
        candidateSkills.setSkillsList(candidateSkill);
        // Create the CandidateSkills
        restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.candidate(candidate))))
            .andExpect(status().isCreated());

        // Validate the CandidateSkills in the database
        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeCreate + 1);
        CandidateSkills testCandidateSkills = candidateSkillsList.get(candidateSkillsList.size() - 1);
        assertThat(testCandidateSkills.getSkills().getSkill()).isEqualTo(msExcel.getSkill());

        // Validate the CandidateSkills in Elasticsearch
       // CandidateSkills candidateSkillsEs = candidateSkillsSearchRepository.findOne(testCandidateSkills.getId());
       // assertThat(candidateSkillsEs).isEqualToIgnoringGivenFields(testCandidateSkills);
    }

    @Test
    @Transactional
    public void createCandidateSkillsAndReCreateSameSkillViaOtherRoute() throws Exception {
        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();
        List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(msExcel);
        candidateSkills.setSkillsList(candidateSkill);
        // Create the CandidateSkills
        restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.candidate(candidate))))
            .andExpect(status().isCreated());

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
        restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(candidateSkills1.capturedSkills("Data Science, DataScience,Datascience"))))
	            .andExpect(status().isCreated());
        List<CandidateSkills> candidateSkillsListAgain = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsListAgain).hasSize(2);
        CandidateSkills testCandidateSkillsAgain = candidateSkillsListAgain.get(candidateSkillsListAgain.size() - 1);
      //  assertThat(testCandidateSkillsAgain.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
        assertThat(skillsRepository.findAll()).hasSize(6);
       
    }

    
    @Test
    @Transactional
    public void createCandidateSkillsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = candidateSkillsRepository.findAll().size();

        // Create the CandidateSkills with an existing ID
        candidateSkills.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
            .andExpect(status().isBadRequest());

        // Validate the CandidateSkills in the database
        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCandidateSkills() throws Exception {
        // Initialize the database
        candidateSkillsRepository.saveAndFlush(candidateSkills);

        // Get all the candidateSkillsList
        restCandidateSkillsMockMvc.perform(get("/api/candidate-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateSkills.getId().intValue())));
           
    }
    
    @Test
    @Transactional
    public void getSkillsForCandidate() throws Exception {
        // Initialize the database
    		CandidateSkills wordSkill = new CandidateSkills();
    		wordSkill.setSkills(msWord);
    		CandidateSkills excelSkill = new CandidateSkills();
    		excelSkill.setSkills(msExcel);
    		candidate.addCandidateSkill(wordSkill).addCandidateSkill(excelSkill);
        candidateSkillsRepository.saveAndFlush(wordSkill.candidate(candidate));
        candidateSkillsRepository.saveAndFlush(excelSkill.candidate(candidate));
        
        // Get all the candidateSkillsList
        restCandidateSkillsMockMvc.perform(get("/api/skills-for-candidate/{id}",candidate.getId()))
            .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(Matchers.containsInAnyOrder(wordSkill.getId().intValue(),excelSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillName").value(Matchers.containsInAnyOrder(msWord.getSkill(),msExcel.getSkill())));
            
    }

    @Test
    @Transactional
    public void getCandidateSkills() throws Exception {
        // Initialize the database
        candidateSkillsRepository.saveAndFlush(candidateSkills.skills(msExcel));

        // Get the candidateSkills
        restCandidateSkillsMockMvc.perform(get("/api/candidate-skills/{id}", candidateSkills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(candidateSkills.getId().intValue()));
            
    }

    @Test
    @Transactional
    public void getNonExistingCandidateSkills() throws Exception {
        // Get the candidateSkills
        restCandidateSkillsMockMvc.perform(get("/api/candidate-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidateSkills() throws Exception {
        // Initialize the database
        candidateSkillsRepository.saveAndFlush(candidateSkills.skills(msExcel));
        candidateSkillsSearchRepository.save(candidateSkills);
        int databaseSizeBeforeUpdate = candidateSkillsRepository.findAll().size();

        // Update the candidateSkills
        CandidateSkills updatedCandidateSkills = candidateSkillsRepository.findOne(candidateSkills.getId());
        // Disconnect from session so that the updates on updatedCandidateSkills are not directly saved in db
        em.detach(updatedCandidateSkills);
        updatedCandidateSkills.skills(msWord);
        restCandidateSkillsMockMvc.perform(put("/api/candidate-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCandidateSkills)))
            .andExpect(status().isOk());

        // Validate the CandidateSkills in the database
        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeUpdate);
        CandidateSkills testCandidateSkills = candidateSkillsList.get(candidateSkillsList.size() - 1);
        assertThat(testCandidateSkills.getSkills().getSkill()).isEqualTo(msWord.getSkill());

        // Validate the CandidateSkills in Elasticsearch
      //  CandidateSkills candidateSkillsEs = candidateSkillsSearchRepository.findOne(testCandidateSkills.getId());
      //  assertThat(candidateSkillsEs).isEqualToIgnoringGivenFields(testCandidateSkills);
    }

    @Test
    @Transactional
    @Ignore
    public void updateNonExistingCandidateSkills() throws Exception {
        int databaseSizeBeforeUpdate = candidateSkillsRepository.findAll().size();

        // Create the CandidateSkills
        List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(msExcel);
        candidateSkills.setSkillsList(candidateSkill);
        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCandidateSkillsMockMvc.perform(put("/api/candidate-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.candidate(candidate))))
            .andExpect(status().isCreated());

        // Validate the CandidateSkills in the database
        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    @Ignore
    public void deleteCandidateSkills() throws Exception {
        // Initialize the database
        candidateSkillsRepository.saveAndFlush(candidateSkills);
        candidateSkillsSearchRepository.save(candidateSkills);
        int databaseSizeBeforeDelete = candidateSkillsRepository.findAll().size();

        // Get the candidateSkills
        restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", candidateSkills.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean candidateSkillsExistsInEs = candidateSkillsSearchRepository.exists(candidateSkills.getId());
        assertThat(candidateSkillsExistsInEs).isFalse();

        // Validate the database is empty
        List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.findAll();
        assertThat(candidateSkillsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCandidateSkills() throws Exception {
        // Initialize the database
        candidateSkillsRepository.saveAndFlush(candidateSkills);
        candidateSkillsSearchRepository.save(candidateSkills);

        // Search the candidateSkills
        restCandidateSkillsMockMvc.perform(get("/api/_search/candidate-skills?query=id:" + candidateSkills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidateSkills.getId().intValue())));
           // .andExpect(jsonPath("$.[*].skill").value(hasItem(DEFAULT_SKILL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateSkills.class);
        CandidateSkills candidateSkills1 = new CandidateSkills();
        candidateSkills1.setId(1L);
        CandidateSkills candidateSkills2 = new CandidateSkills();
        candidateSkills2.setId(candidateSkills1.getId());
        assertThat(candidateSkills1).isEqualTo(candidateSkills2);
        candidateSkills2.setId(2L);
        assertThat(candidateSkills1).isNotEqualTo(candidateSkills2);
        candidateSkills1.setId(null);
        assertThat(candidateSkills1).isNotEqualTo(candidateSkills2);
    }
    
    @Test
    @Transactional
    public void whenCreateSkillsWithoutEducationWithExistingEmploymentAndBasicProfileMustUpdateProfileScoreTo55() throws Exception {
    		Candidate candidate = new Candidate().firstName("Abhinav");
    		candidateRepository.saveAndFlush(candidate);
    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
    		scoreBasic.setScore(5d);
    		scorePersonal.setScore(15d);
    		scoreEmployment.setScore(15d);
    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal);
    		candidate.setProfileScore(35d);
    		candidateSkills.candidate(candidate);
    		List<Skills> candidateSkill = new ArrayList<>();
            candidateSkill.add(msExcel);
            candidateSkills.setSkillsList(candidateSkill);
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
    		            .andExpect(status().isCreated());
    		
    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
    				.findAll();

    		assertThat(candidateSkillList).hasSize(1);
    		CandidateSkills testCandidateSkill = candidateSkillList
    				.get(candidateSkillList.size() - 1);
    		Candidate testCandidate = testCandidateSkill.getCandidate();
    		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
    		assertThat(testCandidate).isEqualTo(candidate);
    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);

    		 
    }
    
    @Test
    @Transactional
    public void whenCreateAddtionalSkillsWithoutEducationWithExistingSkillEmploymentAndBasicProfileMustMaintainProfileScoreAs55() throws Exception {
    		Candidate candidate = new Candidate().firstName("Abhinav");
    		candidateRepository.saveAndFlush(candidate);
    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
    		scoreBasic.setScore(5d);
    		scorePersonal.setScore(15d);
    		scoreEmployment.setScore(15d);
    		scoreSkill.setScore(20d);
    		candidate.addCandidateProfileScore(scoreBasic).addCandidateProfileScore(scoreEmployment).addCandidateProfileScore(scorePersonal).addCandidateProfileScore(scoreSkill);
    		candidate.setProfileScore(55d);
    		candidateSkills.candidate(candidate);
    		List<Skills> candidateSkill = new ArrayList<>();
            candidateSkill.add(msExcel);
            candidateSkills.setSkillsList(candidateSkill);
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
    		            .andExpect(status().isCreated());
    		
    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
    				.findAll();

    		assertThat(candidateSkillList).hasSize(1);
    		CandidateSkills testCandidateSkill = candidateSkillList
    				.get(candidateSkillList.size() - 1);
    		Candidate testCandidate = testCandidateSkill.getCandidate();
    		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
    		assertThat(testCandidate).isEqualTo(candidate);
    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);

    		 
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
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.capturedSkills("Yoyo,honey,  diljit"))))
    		            .andExpect(status().isCreated());
    		
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
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.capturedSkills("Yoyo,honey,  diljit"))))
    		            .andExpect(status().isCreated());
    		
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
    		candidateSkills.candidate(candidate);
    		candidateSkills.setSkillsList(cSkills);
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.capturedSkills("Yoyo,honey,  diljit"))))
    		            .andExpect(status().isCreated());
    		
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
    
    		restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
    		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
    		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills.capturedSkills("Yoyo,honey,  diljit"))))
    		            .andExpect(status().isCreated());
    		
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
    }
    
    
    
    @Test
    @Transactional
    public void whenDeleteSkillsWithoutEducationWithExistingMultipleSkillAndBasicAndEmploymentProfileMustMaintainProfileScoreAs55() throws Exception {
    		Candidate candidate = new Candidate().firstName("Abhinav");
    		CandidateSkills skill= new CandidateSkills();
    		skill.skills(msExcel);
    		skill.candidate(candidate);
    		candidate.addCandidateSkill(skill);
    		candidate.addCandidateSkill(candidateSkills);
    		candidateSkills.candidate(candidate);
    		candidateRepository.saveAndFlush(candidate);
    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
    		
    		scoreBasic.setScore(5d);
    		scorePersonal.setScore(15d);
    		scoreEmployment.setScore(15d);
    		scoreSkill.setScore(20d);
    		candidate.addCandidateProfileScore(scoreBasic)
    					.addCandidateProfileScore(scoreEmployment)
    						.addCandidateProfileScore(scorePersonal)
    							.addCandidateProfileScore(scoreSkill);
    		candidate.setProfileScore(55d);
    	
    		candidateRepository.saveAndFlush(candidate);
    		 restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", candidateSkills.getId())
    		            .accept(TestUtil.APPLICATION_JSON_UTF8))
    		            .andExpect(status().isOk());
    		
    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
    				.findAll();

    		assertThat(candidateSkillList).hasSize(1);
    		CandidateSkills testCandidateSkill = candidateSkillList
    				.get(candidateSkillList.size() - 1);
    		Candidate testCandidate = testCandidateSkill.getCandidate();
    		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
    		assertThat(testCandidate).isEqualTo(candidate);
    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
    		assertThat(testCandidate.getProfileScore()).isEqualTo(55d);
    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(20d);

    		 
    }
    
    @Test
    @Transactional
    public void whenDeleteAllSkillsWithoutEducationWithExistingMultipleSkillAndBasicAndEmploymentProfileMustReduceProfileScoreTo35() throws Exception {
    		Candidate candidate = new Candidate().firstName("Abhinav");
    		CandidateSkills skill= new CandidateSkills();
    		skill.skills(msExcel);
    		skill.candidate(candidate);
    		candidate.addCandidateSkill(skill);
    		candidate.addCandidateSkill(candidateSkills);
    		candidateSkills.candidate(candidate);
    		candidateRepository.saveAndFlush(candidate);
    		CandidateProfileScore scoreBasic = new CandidateProfileScore(candidate, basic);
    		CandidateProfileScore scorePersonal = new CandidateProfileScore(candidate, personal);
    		CandidateProfileScore scoreEmployment = new CandidateProfileScore(candidate, exp);
    		CandidateProfileScore scoreSkill = new CandidateProfileScore(candidate, skills);
    		
    		scoreBasic.setScore(5d);
    		scorePersonal.setScore(15d);
    		scoreEmployment.setScore(15d);
    		scoreSkill.setScore(20d);
    		candidate.addCandidateProfileScore(scoreBasic)
    					.addCandidateProfileScore(scoreEmployment)
    						.addCandidateProfileScore(scorePersonal)
    							.addCandidateProfileScore(scoreSkill);
    		candidate.setProfileScore(55d);
    	
    		candidateRepository.saveAndFlush(candidate);
    		 restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", candidateSkills.getId())
    		            .accept(TestUtil.APPLICATION_JSON_UTF8))
    		            .andExpect(status().isOk());
    		 restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", skill.getId())
 		            .accept(TestUtil.APPLICATION_JSON_UTF8))
 		            .andExpect(status().isOk());
    		
    		List<CandidateSkills> candidateSkillList = candidateSkillsRepository
    				.findAll();
    		Candidate testCandidate = candidateRepository.findOne(candidate.getId());
    		assertThat(candidateSkillList).hasSize(0);
    		assertThat(testCandidate).isEqualTo(candidate);
    		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
    		assertThat(testCandidate.getProfileScore()).isEqualTo(35d);
    		assertThat(testCandidate.getProfileScores().size()).isEqualTo(4);
    		assertThat(testCandidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_SKILL_PROFILE)).findFirst().get().getScore()).isEqualTo(0d);

    		 
    }
    
    @Test
    @Transactional
    public void whenCandidateHasNoEducationAndAddingSkillMustNotCreateAMatchSet() throws Exception {
    	Candidate candidate = new Candidate().firstName("Abhinav");
    	candidateRepository.saveAndFlush(candidate);
    	
    	candidateSkills.candidate(candidate);
		List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(msExcel);
        candidateSkills.setSkillsList(candidateSkill);
    	
    	restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
	            .andExpect(status().isCreated());
    	
    	List<CandidateSkills> candidateSkillList = candidateSkillsRepository
				.findAll();

		assertThat(candidateSkillList).hasSize(1);
		CandidateSkills testCandidateSkill = candidateSkillList
				.get(candidateSkillList.size() - 1);
		Candidate testCandidate = testCandidateSkill.getCandidate();
		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(0);
		
    }
    
    
    @Test
    @Transactional
    public void whenCandidateEducationAndAddingSkillMustCreateAMatchSetWithMatchScoreZeroIfNoneOfSkillsMatchWithJob() throws Exception {
    	Candidate candidate = new Candidate().firstName("Abhinav");
    	CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);
    	candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
    	CandidateJob cJob = new CandidateJob(candidate,jobF);
    	cJob.setMatchScore(2.0);
    	cJob.setEducationMatchScore(20.0);
    	candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
    	candidateSkills.candidate(candidate);
		List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(yoyoSkill);
        candidateSkills.setSkillsList(candidateSkill);
        
     	restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
	            .andExpect(status().isCreated());
     	
     	List<CandidateSkills> candidateSkillList = candidateSkillsRepository
				.findAll();
		assertThat(candidateSkillList).hasSize(1);
		CandidateSkills testCandidateSkill = candidateSkillList
				.get(candidateSkillList.size() - 1);
		Candidate testCandidate = testCandidateSkill.getCandidate();
		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(yoyoSkill.getSkill());
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(1);
		assertThat(testCandidate.getCandidateJobs())
		.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
				"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
		.contains(tuple(JOB_F, 34.0, 20.0, null, null, 59.0, "Abhinav",0.0));
		
    	
    }
    
    

    @Test
    @Transactional
    public void whenCandidateEducationAndAddingSkillMustCreateAMatchSetWithMatchScoreIfSomeOfSkillsMatchWithJob() throws Exception {
     	Candidate candidate = new Candidate().firstName("Abhinav");
	    	CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
					.candidate(candidate);
	    	candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
	    	CandidateJob cJob = new CandidateJob(candidate,jobF);
	    	cJob.setMatchScore(2.0);
	    	cJob.setEducationMatchScore(20.0);
	    	candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
	    	candidateSkills.candidate(candidate);
			List<Skills> candidateSkill = new ArrayList<>();
	        candidateSkill.add(msExcel);
	        candidateSkills.setSkillsList(candidateSkill);
	        
	     	restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
		            .andExpect(status().isCreated());
	     	
	     	List<CandidateSkills> candidateSkillList = candidateSkillsRepository
					.findAll();
			assertThat(candidateSkillList).hasSize(1);
			CandidateSkills testCandidateSkill = candidateSkillList
					.get(candidateSkillList.size() - 1);
			Candidate testCandidate = testCandidateSkill.getCandidate();
			assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
			assertThat(testCandidate).isEqualTo(candidate);
			assertThat(testCandidate.getCandidateJobs()).hasSize(1);
			assertThat(testCandidate.getCandidateJobs())
			.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
					"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
			.contains(tuple(JOB_F, 42.0, 20.0, null, null, 59.0, "Abhinav",5.0));
    }
    
    @Test
    @Transactional
    public void whenCandidateEducationAndAddingSkillMustCreateAMatchSetWithMatchScoreIfSomeOfSkillsMatchWithJobAddAnotherAndMatchSetMustBeUpdated() throws Exception {
     	Candidate candidate = new Candidate().firstName("Abhinav");
	    	CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
					.candidate(candidate);
	    	candidateRepository.saveAndFlush(candidate.addEducation(candidateEducation));
	    	CandidateJob cJob = new CandidateJob(candidate,jobF);
	    	cJob.setMatchScore(2.0);
	    	cJob.setEducationMatchScore(20.0);
	    	candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
	    	
	    	candidateSkills.candidate(candidate);
			List<Skills> candidateSkill = new ArrayList<>();
	        candidateSkill.add(msExcel);
	        candidateSkills.setSkillsList(candidateSkill);
	        
	     	restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
		            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
		            .andExpect(status().isCreated());
	     	
	     	List<CandidateSkills> candidateSkillList = candidateSkillsRepository
					.findAll();
			assertThat(candidateSkillList).hasSize(1);
			CandidateSkills testCandidateSkill = candidateSkillList
					.get(candidateSkillList.size() - 1);
			Candidate testCandidate = testCandidateSkill.getCandidate();
			assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
			assertThat(testCandidate).isEqualTo(candidate);
			assertThat(testCandidate.getCandidateJobs()).hasSize(1);
			assertThat(testCandidate.getCandidateJobs())
			.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
					"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
			.contains(tuple(JOB_F, 42.0, 20.0, null, null, 59.0, "Abhinav",5.0));
		
			CandidateSkills additonaCandidateSkill = new CandidateSkills();
			List<Skills> additionalSkill = new ArrayList<>();
			additionalSkill.add(msWord);
			additonaCandidateSkill.setSkillsList(additionalSkill);
			additonaCandidateSkill.candidate(candidate);
			restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
		            .contentType(TestUtil.APPLICATION_JSON_UTF8)
		            .content(TestUtil.convertObjectToJsonBytes(additonaCandidateSkill)))
		            .andExpect(status().isCreated());

			
			List<CandidateSkills> candidateSkillListNew = candidateSkillsRepository
					.findAll();
			assertThat(candidateSkillListNew).hasSize(2);
			assertThat(candidateSkillListNew).extracting("skills.skill").contains("MSWORD","MSEXCEL");
			Candidate testCandidateAgain = candidateSkillListNew.get(0).getCandidate();
			assertThat(testCandidateAgain.getCandidateJobs()).hasSize(1);
			assertThat(testCandidateAgain.getCandidateJobs())
			.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
					"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
			.contains(tuple(JOB_F, 51.0, 20.0, null, null, 59.0, "Abhinav",10.0));
    }
    
    
    @Test
	@Transactional
	public void whenCandidateEducationAndLanguageAndGenderAddingSkillMustUpdateAMatchSetWithMatchScoreForSkillsMustNotChnageOtherScores()
			throws Exception {
		Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);
		CandidateLanguageProficiency langProf = new CandidateLanguageProficiency();
		langProf.language(englishLanguage);
		langProf.candidate(candidate);
		candidate.gender(femaleGender);
		candidate.addCandidateLanguageProficiency(langProf);
		candidate.addEducation(candidateEducation);
		candidateRepository.saveAndFlush(candidate);
	 	CandidateJob cJob = new CandidateJob(candidate,jobF);
	 	cJob.setMatchScore(2.0);
	 	cJob.setEducationMatchScore(20.0);
	 	cJob.setLanguageMatchScore(5d);
	 	cJob.setGenderMatchScore(1d);
		candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
	 	candidateSkills.candidate(candidate);
		List<Skills> candidateSkill = new ArrayList<>();
        candidateSkill.add(msExcel);
        candidateSkills.setSkillsList(candidateSkill);
        
     	restCandidateSkillsMockMvc.perform(post("/api/candidate-skills")
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
	            .content(TestUtil.convertObjectToJsonBytes(candidateSkills)))
	            .andExpect(status().isCreated());
     	List<CandidateSkills> candidateSkillList = candidateSkillsRepository
				.findAll();
		assertThat(candidateSkillList).hasSize(1);
		CandidateSkills testCandidateSkill = candidateSkillList
				.get(candidateSkillList.size() - 1);
		Candidate testCandidate = testCandidateSkill.getCandidate();
		assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msExcel.getSkill());
		assertThat(testCandidate).isEqualTo(candidate);
		assertThat(testCandidate.getCandidateJobs()).hasSize(1);
		assertThat(testCandidate.getCandidateJobs())
		.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
				"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
		.contains(tuple(JOB_F, 53.0, 20.0, 1.0, 5.0, 59.0, "Abhinav",5.0));
     	
     	
	}
    
    @Test
    @Transactional
    public void whenCandidateEducationAndLanguageAndGenderRemovingAllSkillMustUpdateAMatchSetWithMatchScoreZeroForSkillsMustNotChnageOtherScores() throws Exception {
    	Candidate candidate = new Candidate().firstName("Abhinav");
		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
				.candidate(candidate);
		CandidateLanguageProficiency langProf = new CandidateLanguageProficiency();
		langProf.language(englishLanguage);
		langProf.candidate(candidate);
		candidate.gender(femaleGender);
		candidate.addCandidateLanguageProficiency(langProf);
		candidate.addEducation(candidateEducation);
		candidateSkills.candidate(candidate);
        candidateSkills.setSkills(msExcel);
		candidateRepository.saveAndFlush(candidate.addCandidateSkill(candidateSkills));
		
	 	CandidateJob cJob = new CandidateJob(candidate,jobF);
	 	cJob.setMatchScore(2.0);
	 	cJob.setEducationMatchScore(20.0);
	 	cJob.setLanguageMatchScore(5d);
	 	cJob.setGenderMatchScore(1d);
	 	cJob.setSkillMatchScore(5d);
		candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
		  restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", candidateSkills.getId())
		            .accept(TestUtil.APPLICATION_JSON_UTF8))
		            .andExpect(status().isOk());
			List<CandidateSkills> candidateSkillList = candidateSkillsRepository
					.findAll();
			assertThat(candidateSkillList).hasSize(0);
			Candidate testCandidate = candidateRepository.findOne(candidate.getId());
			assertThat(testCandidate).isEqualTo(candidate);
			assertThat(testCandidate.getCandidateJobs()).hasSize(1);
			assertThat(testCandidate.getCandidateJobs())
			.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
					"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
			.contains(tuple(JOB_F, 44.0, 20.0, 1.0, 5.0, 59.0, "Abhinav",0.0));
    }
    
    @Test
    @Transactional
    public void whenCandidateEducationAndLanguageAndGenderRemovingOneOutOfManySkillMustUpdateAMatchSetWithMatchScoreForSkillsMustNotChangeOtherScores() throws Exception {
      	Candidate candidate = new Candidate().firstName("Abhinav");
    		CandidateEducation candidateEducation = new CandidateEducation().highestQualification(true).grade(9.8)
    				.candidate(candidate);
    		CandidateLanguageProficiency langProf = new CandidateLanguageProficiency();
    		langProf.language(englishLanguage);
    		langProf.candidate(candidate);
    		candidate.gender(femaleGender);
    		candidate.addCandidateLanguageProficiency(langProf);
    		candidate.addEducation(candidateEducation);
    		
    		candidateSkills.candidate(candidate);
    		
        candidateSkills.setSkills(msExcel);
         CandidateSkills skill2 = new CandidateSkills();
         skill2.candidate(candidate);
    		
    		skill2.setSkills(msWord);
    		
    		
    		candidateRepository.saveAndFlush(candidate.addCandidateSkill(candidateSkills).addCandidateSkill(skill2));
    		
    	 	CandidateJob cJob = new CandidateJob(candidate,jobF);
    	 	cJob.setMatchScore(2.0);
    	 	cJob.setEducationMatchScore(20.0);
    	 	cJob.setLanguageMatchScore(5d);
    	 	cJob.setGenderMatchScore(1d);
    	 	cJob.setSkillMatchScore(10d);
    		candidateRepository.saveAndFlush(candidate.addCandidateJob(cJob));
    		  restCandidateSkillsMockMvc.perform(delete("/api/candidate-skills/{id}", candidateSkills.getId())
    		            .accept(TestUtil.APPLICATION_JSON_UTF8))
    		            .andExpect(status().isOk());
    			List<CandidateSkills> candidateSkillList = candidateSkillsRepository
    					.findAll();
    			assertThat(candidateSkillList).hasSize(1);
    			Candidate testCandidate = candidateRepository.findOne(candidate.getId());
    			CandidateSkills testCandidateSkill = candidateSkillList
    					.get(candidateSkillList.size() - 1);
    			assertThat(testCandidateSkill.getSkills().getSkill()).isEqualTo(msWord.getSkill());
    			assertThat(testCandidate).isEqualTo(candidate);
    			assertThat(testCandidate.getCandidateJobs()).hasSize(1);
    			assertThat(testCandidate.getCandidateJobs())
    			.extracting("job.jobTitle", "matchScore", "educationMatchScore", "genderMatchScore",
    					"languageMatchScore", "totalEligibleScore", "candidate.firstName","skillMatchScore")
    			.contains(tuple(JOB_F, 53.0, 20.0, 1.0, 5.0, 59.0, "Abhinav",5.0));
    }
}
