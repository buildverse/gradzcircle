package com.drishika.gradzcircle.service;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.*;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.GenderEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.LanguageEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.*;
import com.drishika.gradzcircle.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

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
			VisaTypeSearchRepository visaTypeSearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
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
		reindexForClass(Country.class, countryRepository, countrySearchRepository);
		// reindexForClass(Course.class, courseRepository, courseSearchRepository);
		// reindexForClass(Employability.class, employabilityRepository,
		// employabilitySearchRepository);
		reindexForClass(EmploymentType.class, employmentTypeRepository, employmentTypeSearchRepository);
		// reindexForClass(ErrorMessages.class, errorMessagesRepository,
		// errorMessagesSearchRepository);
		// reindexForClass(Gender.class, genderRepository, genderSearchRepository);
		reindexForClass(Industry.class, industryRepository, industrySearchRepository);
		reindexForClass(JobCategory.class, jobCategoryRepository, jobCategorySearchRepository);
		reindexForClass(JobType.class, jobTypeRepository, jobTypeSearchRepository);
		// reindexForClass(Language.class, languageRepository,
		// languageSearchRepository);
		// reindexForClass(MaritalStatus.class, maritalStatusRepository,
		// maritalStatusSearchRepository);
		reindexForClass(Nationality.class, nationalityRepository, nationalitySearchRepository);
		// reindexForClass(Qualification.class, qualificationRepository,
		// qualificationSearchRepository);
		reindexForClass(Skills.class, skillsRepository, skillsSearchRepository);
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
		log.info("Elasticsearch: Successfully performed reindexing");
	}

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private void reindexForCollege(CollegeRepository collegeRepository) {
		List<College> colleges = collegeRepository.findAll();
		colleges.forEach(college -> {
			elasticsearchTemplate.index(new CollegeEntityBuilder(college.getId()).name(college.getCollegeName())
					.suggest(new String[] { college.getCollegeName() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
		});
	}

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private void reindexForLanguage(LanguageRepository languageRepository) {
		List<Language> languages = languageRepository.findAll();
		languages.forEach(language -> {
			elasticsearchTemplate.index(new LanguageEntityBuilder(language.getId()).name(language.getLanguage())
					.suggest(new String[] { language.getLanguage() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
		});
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private void reindexForCourse(CourseRepository courseRepository) {
		List<Course> courses = courseRepository.findAll();
		courses.forEach(course -> {
			elasticsearchTemplate.index(new CourseEntityBuilder(course.getId()).name(course.getCourse())
					.suggest(new String[] { course.getCourse() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
		});
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	private void reindexForGender(GenderRepository genderRepository) {
		List<Gender> genders = genderRepository.findAll();
		genders.forEach(gender -> {
			elasticsearchTemplate.index(new GenderEntityBuilder(gender.getId()).name(gender.getGender())
					.suggest(new String[] { gender.getGender() }).buildIndex());
			elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Gender.class);
		});
	}
}
