package com.drishika.gradzcircle.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Industry;
import com.drishika.gradzcircle.domain.JobCategory;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.MaritalStatus;
import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CountryEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.GenderEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.IndustryEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.JobCategoryEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.LanguageEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.NationalityEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.SkillsEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.AuditRepository;
import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateNonAcademicWorkRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CaptureCollegeRepository;
import com.drishika.gradzcircle.repository.CaptureCourseRepository;
import com.drishika.gradzcircle.repository.CaptureQualificationRepository;
import com.drishika.gradzcircle.repository.CaptureUniversityRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.EmployabilityRepository;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.ErrorMessagesRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.IndustryRepository;
import com.drishika.gradzcircle.repository.JobCategoryRepository;
import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.MaritalStatusRepository;
import com.drishika.gradzcircle.repository.NationalityRepository;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.UserRepository;
import com.drishika.gradzcircle.repository.VisaTypeRepository;
import com.drishika.gradzcircle.repository.search.AddressSearchRepository;
import com.drishika.gradzcircle.repository.search.AuditSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
import com.drishika.gradzcircle.repository.search.CaptureCollegeSearchRepository;
import com.drishika.gradzcircle.repository.search.CaptureCourseSearchRepository;
import com.drishika.gradzcircle.repository.search.CaptureQualificationSearchRepository;
import com.drishika.gradzcircle.repository.search.CaptureUniversitySearchRepository;
import com.drishika.gradzcircle.repository.search.CollegeSearchRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.repository.search.CountrySearchRepository;
import com.drishika.gradzcircle.repository.search.CourseSearchRepository;
import com.drishika.gradzcircle.repository.search.EmployabilitySearchRepository;
import com.drishika.gradzcircle.repository.search.EmploymentTypeSearchRepository;
import com.drishika.gradzcircle.repository.search.ErrorMessagesSearchRepository;
import com.drishika.gradzcircle.repository.search.GenderSearchRepository;
import com.drishika.gradzcircle.repository.search.IndustrySearchRepository;
import com.drishika.gradzcircle.repository.search.JobCategorySearchRepository;
import com.drishika.gradzcircle.repository.search.JobTypeSearchRepository;
import com.drishika.gradzcircle.repository.search.LanguageSearchRepository;
import com.drishika.gradzcircle.repository.search.MaritalStatusSearchRepository;
import com.drishika.gradzcircle.repository.search.NationalitySearchRepository;
import com.drishika.gradzcircle.repository.search.ProfileCategorySearchRepository;
import com.drishika.gradzcircle.repository.search.QualificationSearchRepository;
import com.drishika.gradzcircle.repository.search.SkillsSearchRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.repository.search.UserSearchRepository;
import com.drishika.gradzcircle.repository.search.VisaTypeSearchRepository;

@Service
public class ElasticsearchIndexService {

	private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

	private final AddressRepository addressRepository;

	private final AddressSearchRepository addressSearchRepository;

	private final AuditRepository auditRepository;

	private final AuditSearchRepository auditSearchRepository;

	private final CandidateRepository candidateRepository;

	private final CandidateSearchRepository candidateSearchRepository;

	private final CandidateCertificationRepository candidateCertificationRepository;

	private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	private final CandidateEducationRepository candidateEducationRepository;

	private final CandidateEducationSearchRepository candidateEducationSearchRepository;

	private final CandidateEmploymentRepository candidateEmploymentRepository;

	private final CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	private final CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository;

	private final CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;

	private final CandidateProjectRepository candidateProjectRepository;

	private final CandidateProjectSearchRepository candidateProjectSearchRepository;

	private final CaptureCollegeRepository captureCollegeRepository;

	private final CaptureCollegeSearchRepository captureCollegeSearchRepository;

	private final CaptureCourseRepository captureCourseRepository;

	private final CaptureCourseSearchRepository captureCourseSearchRepository;

	private final CaptureQualificationRepository captureQualificationRepository;

	private final CaptureQualificationSearchRepository captureQualificationSearchRepository;

	private final CaptureUniversityRepository captureUniversityRepository;

	private final CaptureUniversitySearchRepository captureUniversitySearchRepository;

	private final CollegeRepository collegeRepository;

	private final CollegeSearchRepository collegeSearchRepository;

	private final CorporateRepository corporateRepository;

	private final CorporateSearchRepository corporateSearchRepository;

	private final CountryRepository countryRepository;

	private final CountrySearchRepository countrySearchRepository;

	private final CourseRepository courseRepository;

	private final CourseSearchRepository courseSearchRepository;

	private final EmployabilityRepository employabilityRepository;

	private final EmployabilitySearchRepository employabilitySearchRepository;

	private final EmploymentTypeRepository employmentTypeRepository;

	private final EmploymentTypeSearchRepository employmentTypeSearchRepository;

	private final ErrorMessagesRepository errorMessagesRepository;

	private final ErrorMessagesSearchRepository errorMessagesSearchRepository;

	private final GenderRepository genderRepository;

	private final GenderSearchRepository genderSearchRepository;

	private final IndustryRepository industryRepository;

	private final IndustrySearchRepository industrySearchRepository;

	private final JobCategoryRepository jobCategoryRepository;

	private final JobCategorySearchRepository jobCategorySearchRepository;

	private final JobTypeRepository jobTypeRepository;

	private final JobTypeSearchRepository jobTypeSearchRepository;

	private final LanguageRepository languageRepository;

	private final LanguageSearchRepository languageSearchRepository;

	private final MaritalStatusRepository maritalStatusRepository;

	private final MaritalStatusSearchRepository maritalStatusSearchRepository;

	private final NationalityRepository nationalityRepository;

	private final NationalitySearchRepository nationalitySearchRepository;

	private final QualificationRepository qualificationRepository;

	private final QualificationSearchRepository qualificationSearchRepository;

	private final SkillsRepository skillsRepository;

	private final SkillsSearchRepository skillsSearchRepository;

	private final UniversityRepository universityRepository;

	private final UniversitySearchRepository universitySearchRepository;

	private final VisaTypeRepository visaTypeRepository;

	private final VisaTypeSearchRepository visaTypeSearchRepository;

	private final UserRepository userRepository;

	private final UserSearchRepository userSearchRepository;
	
	private ProfileCategoryRepository profileCategoryRepository;
	
	private ProfileCategorySearchRepository profileCategorySearchRepository;

	private final ElasticsearchTemplate elasticsearchTemplate;

	public ElasticsearchIndexService(UserRepository userRepository, UserSearchRepository userSearchRepository,
			AddressRepository addressRepository, AddressSearchRepository addressSearchRepository,
			AuditRepository auditRepository, AuditSearchRepository auditSearchRepository,
			CandidateRepository candidateRepository, CandidateSearchRepository candidateSearchRepository,
			CandidateCertificationRepository candidateCertificationRepository,
			CandidateCertificationSearchRepository candidateCertificationSearchRepository,
			CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateEmploymentRepository candidateEmploymentRepository,
			CandidateEmploymentSearchRepository candidateEmploymentSearchRepository,
			CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository,
			CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository,
			CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository,
			CaptureCollegeRepository captureCollegeRepository,
			CaptureCollegeSearchRepository captureCollegeSearchRepository,
			CaptureCourseRepository captureCourseRepository,
			CaptureCourseSearchRepository captureCourseSearchRepository,
			CaptureQualificationRepository captureQualificationRepository,
			CaptureQualificationSearchRepository captureQualificationSearchRepository,
			CaptureUniversityRepository captureUniversityRepository,
			CaptureUniversitySearchRepository captureUniversitySearchRepository, CollegeRepository collegeRepository,
			CollegeSearchRepository collegeSearchRepository, CorporateRepository corporateRepository,
			CorporateSearchRepository corporateSearchRepository, CountryRepository countryRepository,
			CountrySearchRepository countrySearchRepository, CourseRepository courseRepository,
			CourseSearchRepository courseSearchRepository, EmployabilityRepository employabilityRepository,
			EmployabilitySearchRepository employabilitySearchRepository,
			EmploymentTypeRepository employmentTypeRepository,
			EmploymentTypeSearchRepository employmentTypeSearchRepository,
			ErrorMessagesRepository errorMessagesRepository,
			ErrorMessagesSearchRepository errorMessagesSearchRepository, GenderRepository genderRepository,
			GenderSearchRepository genderSearchRepository, IndustryRepository industryRepository,
			IndustrySearchRepository industrySearchRepository, JobCategoryRepository jobCategoryRepository,
			JobCategorySearchRepository jobCategorySearchRepository, JobTypeRepository jobTypeRepository,
			JobTypeSearchRepository jobTypeSearchRepository, LanguageRepository languageRepository,
			LanguageSearchRepository languageSearchRepository, MaritalStatusRepository maritalStatusRepository,
			MaritalStatusSearchRepository maritalStatusSearchRepository, NationalityRepository nationalityRepository,
			NationalitySearchRepository nationalitySearchRepository, QualificationRepository qualificationRepository,
			QualificationSearchRepository qualificationSearchRepository, SkillsRepository skillsRepository,
			SkillsSearchRepository skillsSearchRepository, UniversityRepository universityRepository,
			UniversitySearchRepository universitySearchRepository, VisaTypeRepository visaTypeRepository,
			VisaTypeSearchRepository visaTypeSearchRepository, ElasticsearchTemplate elasticsearchTemplate,
			ProfileCategoryRepository profileCategoryRepository, ProfileCategorySearchRepository profileCategorySearchRepository) {
		this.userRepository = userRepository;
		this.userSearchRepository = userSearchRepository;
		this.addressRepository = addressRepository;
		this.addressSearchRepository = addressSearchRepository;
		this.auditRepository = auditRepository;
		this.auditSearchRepository = auditSearchRepository;
		this.candidateRepository = candidateRepository;
		this.candidateSearchRepository = candidateSearchRepository;
		this.candidateCertificationRepository = candidateCertificationRepository;
		this.candidateCertificationSearchRepository = candidateCertificationSearchRepository;
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateEmploymentRepository = candidateEmploymentRepository;
		this.candidateEmploymentSearchRepository = candidateEmploymentSearchRepository;
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		this.candidateNonAcademicWorkRepository = candidateNonAcademicWorkRepository;
		this.candidateNonAcademicWorkSearchRepository = candidateNonAcademicWorkSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.candidateProjectSearchRepository = candidateProjectSearchRepository;
		this.captureCollegeRepository = captureCollegeRepository;
		this.captureCollegeSearchRepository = captureCollegeSearchRepository;
		this.captureCourseRepository = captureCourseRepository;
		this.captureCourseSearchRepository = captureCourseSearchRepository;
		this.captureQualificationRepository = captureQualificationRepository;
		this.captureQualificationSearchRepository = captureQualificationSearchRepository;
		this.captureUniversityRepository = captureUniversityRepository;
		this.captureUniversitySearchRepository = captureUniversitySearchRepository;
		this.collegeRepository = collegeRepository;
		this.collegeSearchRepository = collegeSearchRepository;
		this.corporateRepository = corporateRepository;
		this.corporateSearchRepository = corporateSearchRepository;
		this.countryRepository = countryRepository;
		this.countrySearchRepository = countrySearchRepository;
		this.courseRepository = courseRepository;
		this.courseSearchRepository = courseSearchRepository;
		this.employabilityRepository = employabilityRepository;
		this.employabilitySearchRepository = employabilitySearchRepository;
		this.employmentTypeRepository = employmentTypeRepository;
		this.employmentTypeSearchRepository = employmentTypeSearchRepository;
		this.errorMessagesRepository = errorMessagesRepository;
		this.errorMessagesSearchRepository = errorMessagesSearchRepository;
		this.genderRepository = genderRepository;
		this.genderSearchRepository = genderSearchRepository;
		this.industryRepository = industryRepository;
		this.industrySearchRepository = industrySearchRepository;
		this.jobCategoryRepository = jobCategoryRepository;
		this.jobCategorySearchRepository = jobCategorySearchRepository;
		this.jobTypeRepository = jobTypeRepository;
		this.jobTypeSearchRepository = jobTypeSearchRepository;
		this.languageRepository = languageRepository;
		this.languageSearchRepository = languageSearchRepository;
		this.maritalStatusRepository = maritalStatusRepository;
		this.maritalStatusSearchRepository = maritalStatusSearchRepository;
		this.nationalityRepository = nationalityRepository;
		this.nationalitySearchRepository = nationalitySearchRepository;
		this.qualificationRepository = qualificationRepository;
		this.qualificationSearchRepository = qualificationSearchRepository;
		this.skillsRepository = skillsRepository;
		this.skillsSearchRepository = skillsSearchRepository;
		this.universityRepository = universityRepository;
		this.universitySearchRepository = universitySearchRepository;
		this.visaTypeRepository = visaTypeRepository;
		this.visaTypeSearchRepository = visaTypeSearchRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.profileCategoryRepository = profileCategoryRepository;
		this.profileCategorySearchRepository  = profileCategorySearchRepository;
	}

	@Async
	@Timed
	public void reindexAll() {
		// reindexForClass(Address.class, addressRepository, addressSearchRepository);
		// reindexForClass(Audit.class, auditRepository, auditSearchRepository);
		// reindexForClass(Candidate.class, candidateRepository,
		// candidateSearchRepository);
		// reindexForClass(CandidateCertification.class,
		// candidateCertificationRepository, candidateCertificationSearchRepository);
		// reindexForClass(CandidateEducation.class, candidateEducationRepository,
		// candidateEducationSearchRepository);
		// reindexForClass(CandidateEmployment.class, candidateEmploymentRepository,
		// candidateEmploymentSearchRepository);
		// reindexForClass(CandidateLanguageProficiency.class,
		// candidateLanguageProficiencyRepository,
		// candidateLanguageProficiencySearchRepository);
		// reindexForClass(CandidateNonAcademicWork.class,
		// candidateNonAcademicWorkRepository,
		// candidateNonAcademicWorkSearchRepository);
		// reindexForClass(CandidateProject.class, candidateProjectRepository,
		// candidateProjectSearchRepository);
		// reindexForClass(CaptureCollege.class, captureCollegeRepository,
		// captureCollegeSearchRepository);
		// reindexForClass(CaptureCourse.class, captureCourseRepository,
		// captureCourseSearchRepository);
		// reindexForClass(CaptureQualification.class, captureQualificationRepository,
		// captureQualificationSearchRepository);
		// reindexForClass(CaptureUniversity.class, captureUniversityRepository,
		// captureUniversitySearchRepository);
		// reindexForClass(Corporate.class, corporateRepository,
		// corporateSearchRepository);
		//reindexForClass(Country.class, countryRepository, countrySearchRepository);
		// reindexForClass(Course.class, courseRepository, courseSearchRepository);
		// reindexForClass(Employability.class, employabilityRepository,
		// employabilitySearchRepository);
		reindexForClass(EmploymentType.class, employmentTypeRepository, employmentTypeSearchRepository);
		// reindexForClass(ErrorMessages.class, errorMessagesRepository,
		// errorMessagesSearchRepository);
		// reindexForClass(Gender.class, genderRepository, genderSearchRepository);
		//reindexForClass(Industry.class, industryRepository, industrySearchRepository);
		//reindexForClass(JobCategory.class, jobCategoryRepository, jobCategorySearchRepository);
		reindexForClass(JobType.class, jobTypeRepository, jobTypeSearchRepository);
		reindexForClass(ProfileCategory.class, profileCategoryRepository, profileCategorySearchRepository);
		// reindexForClass(Language.class, languageRepository,
		// languageSearchRepository);
		// reindexForClass(MaritalStatus.class, maritalStatusRepository,
		// maritalStatusSearchRepository);
		//reindexForClass(Nationality.class, nationalityRepository, nationalitySearchRepository);
		// reindexForClass(Qualification.class, qualificationRepository,
		// qualificationSearchRepository);
		//reindexForClass(Skills.class, skillsRepository, skillsSearchRepository);
		// reindexForClass(University.class, universityRepository,
		// universitySearchRepository);
		// reindexForClass(VisaType.class, visaTypeRepository,
		// visaTypeSearchRepository);
		// reindexForClass(User.class, userRepository, userSearchRepository);
		reindexForUniversity(universityRepository);
		reindexForQualification(qualificationRepository);
		reindexForCourse(courseRepository);
		reindexForLanguage(languageRepository);
		reindexForGender(genderRepository);
		reindexForCollege(collegeRepository);
		reindexForCountry(countryRepository);
		reindexForNationality(nationalityRepository);
		reindexForIndustry(industryRepository);
		reindexForJobCategory(jobCategoryRepository);
		reindexForMaritalStatus(maritalStatusRepository);
		reindexForSkills(skillsRepository);
		log.info("Elasticsearch: Successfully performed reindexing");
	}
	
	//TODO - enable catching exception from asynch method. 
	@Async
	@Timed
	public void reindexByEntityName(String entityName) throws EntityNotFoundException{
		if(Constants.ENTITY_COLLEGE.equalsIgnoreCase(entityName))
			reindexForCollege(collegeRepository);
		else if (Constants.ENTITY_COUNTRY.equalsIgnoreCase(entityName))
			reindexForCountry(countryRepository);
		else if (Constants.ENTITY_COURSE.equalsIgnoreCase(entityName))
			reindexForCourse(courseRepository);
		else if (Constants.ENTITY_GENDER.equalsIgnoreCase(entityName))
			reindexForGender(genderRepository);
		else if (Constants.ENTITY_INDUSTRY.equalsIgnoreCase(entityName))
			reindexForIndustry(industryRepository);
		else if (Constants.ENTITY_JOB_CATEGORY.equalsIgnoreCase(entityName))
			reindexForJobCategory(jobCategoryRepository);
		else if (Constants.ENTITY_LANGUAGE.equalsIgnoreCase(entityName))
			reindexForLanguage(languageRepository);
		else if (Constants.ENTITY_MARITAL_STATUS.equalsIgnoreCase(entityName))
			reindexForMaritalStatus(maritalStatusRepository);
		else if (Constants.ENTITY_NATIONALITY.equalsIgnoreCase(entityName))
			reindexForNationality(nationalityRepository);
		else if (Constants.ENTITY_QUALIFICATION.equalsIgnoreCase(entityName))
			reindexForQualification(qualificationRepository);
		else if (Constants.ENTITY_UNIVERSITY.equalsIgnoreCase(entityName))
			reindexForUniversity(universityRepository);
		else if (Constants.ENTITY_SKILL.equalsIgnoreCase(entityName))
			reindexForSkills(skillsRepository);
		else
			throw new EntityNotFoundException(entityName+" not found");
		
	
	}
	
	
	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void removeIndex (String entityName) {
		log.info("Removing index {} from server ",entityName);
		Boolean removed = elasticsearchTemplate.deleteIndex(entityName.toLowerCase());
		log.info("index {} from server has been removed, status {}",entityName,removed);
	}

	
	@SuppressWarnings("unchecked")
	private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
			ElasticsearchRepository<T, ID> elasticsearchRepository) {
		elasticsearchTemplate.deleteIndex(entityClass);
		try {
			elasticsearchTemplate.createIndex(entityClass);
		} catch (IndexAlreadyExistsException e) {
			// Do nothing. Index was already concurrently recreated by some other service.
		}
		elasticsearchTemplate.putMapping(entityClass);
		if (jpaRepository.count() > 0) {
			try {
				Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
				log.debug("The class name is {}", entityClass.getName());
				elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
			} catch (Exception e) {
				elasticsearchRepository.save(jpaRepository.findAll());
			}
		}
		log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForCollege(CollegeRepository collegeRepository) {
		List<College> colleges = collegeRepository.findAll();
		colleges.forEach(college -> {
			elasticsearchTemplate.index(new CollegeEntityBuilder(college.getId()).name(college.getCollegeName())
					.suggest(new String[] { college.getCollegeName() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		});
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForUniversity(UniversityRepository universityRepository) {
		List<University> universities = universityRepository.findAll();
		universities.forEach(university -> {
			elasticsearchTemplate
					.index(new UniversityEntityBuilder(university.getId()).name(university.getUniversityName())
							.suggest(new String[] { university.getUniversityName() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
		});
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForQualification(QualificationRepository qualificationRepository) {
		List<Qualification> qualifications = qualificationRepository.findAll();
		qualifications.forEach(qualification -> {
			elasticsearchTemplate
					.index(new QualificationEntityBuilder(qualification.getId()).name(qualification.getQualification())
							.suggest(new String[] { qualification.getQualification() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
		});
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForLanguage(LanguageRepository languageRepository) {
		List<Language> languages = languageRepository.findAll();
		languages.forEach(language -> {
			elasticsearchTemplate.index(new LanguageEntityBuilder(language.getId()).name(language.getLanguage())
					.suggest(new String[] { language.getLanguage() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		});
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForCourse(CourseRepository courseRepository) {
		List<Course> courses = courseRepository.findAll();
		courses.forEach(course -> {
			elasticsearchTemplate.index(new CourseEntityBuilder(course.getId()).name(course.getCourse())
					.suggest(new String[] { course.getCourse() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		});
	}

	
	@SuppressWarnings("unchecked")
	private void reindexForGender(GenderRepository genderRepository) {
		List<Gender> genders = genderRepository.findAll();
		genders.forEach(gender -> {
			elasticsearchTemplate.index(new GenderEntityBuilder(gender.getId()).name(gender.getGender())
					.suggest(new String[] { gender.getGender() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void reindexForCountry(CountryRepository countryRepository) {
		List<Country> countries = countryRepository.findAll();
		countries.forEach(country -> {
			elasticsearchTemplate.index(new CountryEntityBuilder(country.getId()).name(country.getCountryNiceName())
					.suggest(new String[] { country.getCountryNiceName() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void reindexForIndustry(IndustryRepository industryRepository) {
		List<Industry> industries = industryRepository.findAll();
		industries.forEach(industry -> {
			elasticsearchTemplate.index(new IndustryEntityBuilder(industry.getId()).name(industry.getIndustryName())
					.suggest(new String[] { industry.getIndustryName() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Industry.class);
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void reindexForJobCategory(JobCategoryRepository jobCategoryRepository) {
		List<JobCategory> jobCategories = jobCategoryRepository.findAll();
		jobCategories.forEach(jobCategory -> {
			elasticsearchTemplate.index(new JobCategoryEntityBuilder(jobCategory.getId()).name(jobCategory.getJobCategory())
					.suggest(new String[] { jobCategory.getJobCategory() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.JobCategory.class);
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void reindexForMaritalStatus(MaritalStatusRepository maritalStatusRepository) {
		List<MaritalStatus> statuses = maritalStatusRepository.findAll();
		statuses.forEach(status -> {
			elasticsearchTemplate.index(new CountryEntityBuilder(status.getId()).name(status.getStatus())
					.suggest(new String[] { status.getStatus() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.MaritalStatus.class);
		});
	}
	
	
	@SuppressWarnings("unchecked")
	private void reindexForNationality(NationalityRepository nationalityRepository) {
		List<Nationality> nationalities = nationalityRepository.findAll();
		nationalities.forEach(nationality -> {
			elasticsearchTemplate.index(new NationalityEntityBuilder(nationality.getId()).name(nationality.getNationality())
					.suggest(new String[] { nationality.getNationality() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Nationality.class);
		});
	}
	
	@SuppressWarnings("unchecked")
	private void reindexForSkills(SkillsRepository skillsRepository) {
		List<Skills> skills = skillsRepository.findAll();
		skills.forEach(skill -> {
			elasticsearchTemplate.index(new SkillsEntityBuilder(skill.getId()).name(skill.getSkill())
					.suggest(new String[] { skill.getSkill() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
		});
	}
}
