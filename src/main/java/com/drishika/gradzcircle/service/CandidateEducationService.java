package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.entitybuilders.CollegeEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.CourseEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.QualificationEntityBuilder;
import com.drishika.gradzcircle.entitybuilders.UniversityEntityBuilder;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.UniversitySearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateEducationDTO;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

@Service
@Transactional
public class CandidateEducationService {

	private final Logger log = LoggerFactory.getLogger(CandidateEducationService.class);

	private final CandidateEducationRepository candidateEducationRepository;
	private final CandidateRepository candidateRepository;
	private final CandidateProjectRepository candidateProjectRepository;
	private final CollegeRepository collegeRepository;
	private final QualificationRepository qualififcationRepository;
	private final CourseRepository courseRepository;
	private final UniversityRepository universityRepository;
	private final CandidateEducationSearchRepository candidateEducationSearchRepository;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ProfileScoreCalculator profileScoreCalculator;
	private final DTOConverters converter;

	private final Matcher<Candidate> matcher;

	public CandidateEducationService(CandidateEducationRepository candidateEducationRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository, CollegeRepository collegeRepository,
			QualificationRepository qualififcationRepository, CourseRepository courseRepository,
			UniversityRepository universityRepository,
			@Qualifier("CandidateEducationMatcher") Matcher<Candidate> matcher,
			ProfileScoreCalculator profileScoreCalculator,
			UniversitySearchRepository universitySearchRepository, ElasticsearchTemplate elasticsearchTemplate,
			CandidateRepository candidateRepository,DTOConverters converter) {
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.collegeRepository = collegeRepository;
		this.universityRepository = universityRepository;
		this.courseRepository = courseRepository;
		this.qualififcationRepository = qualififcationRepository;
		this.matcher = matcher;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.candidateRepository = candidateRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.converter = converter;
	}

	private void setGrade(CandidateEducation candidateEducation) {
		String gradeMajorUnit = candidateEducation.getRoundOfGrade().toString();
		String gradeMinorUnit = null;
		if (candidateEducation.getGradeDecimal() != null)
			gradeMinorUnit = candidateEducation.getGradeDecimal().toString();
		else {
			gradeMinorUnit = "0";
		}
		log.debug("Grade Decimla is {}", gradeMinorUnit);
		candidateEducation.setGrade(new Double(gradeMajorUnit + "." + gradeMinorUnit));
	}

	public CandidateEducation createCandidateEducation(CandidateEducation candidateEducation) {
		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		injestCollegeInformation(candidateEducation);
		injestCourseInformation(candidateEducation);
		injestQualificationInformation(candidateEducation);
		Candidate candidate = null;
		log.info("Creating education for candidate, course,qualification {},{},{}", candidateEducation.getCandidate(),
				candidateEducation.getCourse(), candidateEducation.getQualification());
		setHighestEducation(candidateEducation, false);
		Optional<Candidate> optional = candidateRepository.findById(candidateEducation.getCandidate().getId());
		if(optional.isPresent()) 
			candidate = optional.get();
		else 
			return null;
		//Candidate candidate = candidateRepository.findById(candidateEducation.getCandidate().getId());
		candidateEducation = candidateEducationRepository.save(candidateEducation);
		candidate = candidate.addEducation(candidateEducation);
		profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_EDUCATION_PROFILE, false);
		candidate = candidateRepository.save(candidate);
		log.debug("CandidateEducation post save  is {}", candidate.getEducations());
		//Candidate candidate = candidateRepository.findOne(candidateEducation.getCandidate().getId());
		if (candidateEducation.getHighestQualification())
			matcher.match(candidate);
		// updateEducationDependentMetaForDisplay(result);
		return candidateEducation;
	}

	
	private List<CandidateEducation> setHighestEducation(CandidateEducation candidateEducation, Boolean isDelete) {
		List<CandidateEducation> candidateEducations = candidateEducationRepository
				.findByCandidateId(candidateEducation.getCandidate().getId());
		if (isDelete) {
			candidateEducations.remove(candidateEducation);
			if (candidateEducations.size() > 0) {
			Comparator<CandidateEducation> comparator = Comparator.comparing(CandidateEducation::getEducationToDate,Comparator.nullsLast(Comparator.naturalOrder()));
				candidateEducations.sort(comparator);
				if(candidateEducations.stream().filter(education->education.getHighestQualification()!=null).filter(education->education.isHighestQualification()).count()<=0)
					candidateEducations.get(candidateEducations.size() - 1).setHighestQualification(true);
				log.debug("Soretd education by dates {}", candidateEducations);
			}
		} else {
			if (candidateEducations.size() > 0) {
				log.debug("Candidate Edcuaitons are {} ",candidateEducations);
				CandidateEducation highestEducationInCollection = candidateEducations.stream().filter(education -> education.isHighestQualification()!=null && education.isHighestQualification())
										.findAny().orElse(null);
				if(highestEducationInCollection == null) {
					Comparator<CandidateEducation> comparator = Comparator.comparing(CandidateEducation::getEducationToDate,Comparator.nullsLast(Comparator.naturalOrder()));
					candidateEducations.sort(comparator);
					candidateEducations.get(candidateEducations.size() - 1).setHighestQualification(true);
				} else {
					if(candidateEducation.getHighestQualification()!=null && candidateEducation.getHighestQualification()) {
						candidateEducations.forEach(education->{
							if(!education.equals(candidateEducation))
								education.setHighestQualification(false);
						});
					} else {
						Comparator<CandidateEducation> comparator = Comparator.comparing(CandidateEducation::getEducationToDate,Comparator.nullsLast(Comparator.naturalOrder()));
						candidateEducations.sort(comparator);
						CandidateEducation lastEducation = candidateEducations.get(candidateEducations.size()-1);
						if(lastEducation.getEducationToDate()==null && candidateEducation.getEducationToDate()==null) {
							lastEducation.setHighestQualification(false);
							candidateEducation.setHighestQualification(true);
						} else if (lastEducation.getEducationToDate()!=null && candidateEducation.getEducationToDate() == null) {
							lastEducation.setHighestQualification(false);
							candidateEducation.setHighestQualification(true);
						} else if (lastEducation.getEducationToDate()!=null && candidateEducation.getEducationToDate() != null && 
								lastEducation.getEducationToDate().isAfter(candidateEducation.getEducationToDate())) {
							lastEducation.setHighestQualification(true);
							candidateEducation.setHighestQualification(false);
						} else if (lastEducation.getEducationToDate()!=null && candidateEducation.getEducationToDate() != null && 
								candidateEducation.getEducationToDate().isAfter(lastEducation.getEducationToDate())) {
							lastEducation.setHighestQualification(false);
							candidateEducation.setHighestQualification(true);
						} else if (lastEducation.getEducationToDate()==null && candidateEducation.getEducationToDate()!=null) {
							lastEducation.setHighestQualification(true);
							candidateEducation.setHighestQualification(false);
						} else if (lastEducation.getEducationToDate()!=null && candidateEducation.getEducationToDate() != null && 
								candidateEducation.getEducationToDate().isEqual(lastEducation.getEducationToDate())) {
							lastEducation.setHighestQualification(false);
							candidateEducation.setHighestQualification(true);
						}
							
					}
				}
			} else {
				candidateEducation.setHighestQualification(true);
			}
		}
		return candidateEducations;
	}

	private void injestCollegeInformation(CandidateEducation candidateEducation) {
		log.info("Incoming Education is  {} , and college is {}  ", candidateEducation,
				candidateEducation.getCollege());
		if (candidateEducation.getCollege()!=null && candidateEducation.getCollege().getCollegeName().equals(Constants.OTHER)) {
			College college = collegeRepository.findByCollegeName(candidateEducation.getCapturedCollege());
			University university = universityRepository
					.findByUniversityName(candidateEducation.getCapturedUniversity());
			University newUniversity = null;
			if (university == null) {
				newUniversity = new University().universityName(candidateEducation.getCapturedUniversity());
				updateUniversityIndex(universityRepository.save(newUniversity));
				log.info("Created new University {}", newUniversity);
			}
			if (college == null) {
				College newCollege = new College().collegeName(candidateEducation.getCapturedCollege());
				if (university == null) {
					newCollege.university(newUniversity);
				} else {
					newCollege.university(university);
				}
				updateCollegeIndex(collegeRepository.save(newCollege));
				log.info("Created new College {}", newCollege);
				candidateEducation.setCollege(newCollege);

			} else {
				candidateEducation.setCollege(college);
			}

		} else {
			candidateEducation
					.setCollege(collegeRepository.findByCollegeName(candidateEducation.getCollege().getCollegeName()));
		}

	}

	private void injestQualificationInformation(CandidateEducation candidateEducation) {

		if (candidateEducation.getQualification().getQualification().equals(Constants.OTHER)) {
			Qualification qualification = qualififcationRepository
					.findByQualification(candidateEducation.getCapturedQualification());
			if (qualification == null) {
				Qualification newQualification = new Qualification();
				newQualification.setQualification(convertToTitleCase(candidateEducation.getCapturedQualification()));
				newQualification.setCategory(Constants.OTHER);
				newQualification.setWeightage(setQualificationWeightage(newQualification.getQualification()));
				updateQualificationIndex(qualififcationRepository.save(newQualification));
				log.info("Saved new Qualifcation {} with weight {}", newQualification.getQualification(),newQualification.getWeightage());
				candidateEducation.setQualification(newQualification);
			} else {
				candidateEducation.setQualification(qualification);
			}
		} else {
			candidateEducation.setQualification(qualififcationRepository
					.findByQualification(candidateEducation.getQualification().getQualification()));
		}
	}

	private String convertToThreeLetterDegree(String name[]) {	
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < name.length; i++) {
			buffer.append(name[i].toUpperCase());
			if (name.length - i != 1)
				buffer.append(".");
		}
		return buffer.toString();
	}
	
	private String convertToThreeLetterDegree(String name) {	
		StringBuffer buffer = new StringBuffer(name);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < buffer.length(); i++) {
			builder.append(Character.toUpperCase(buffer.charAt(i)));
			if (buffer.length() - i != 1)
				builder.append(".");
		}
		return builder.toString();
	}
	
	private String convertToTitleCase(String qualificationName) {
		log.debug("The incomeing qualification is {}",qualificationName.length());
		if (StringUtils.isBlank(qualificationName)) {
			return "";
		}
		String[] split = qualificationName.split("\\.");
		log.debug("The split is {}",split.length);
		if(split.length>1) {
			return convertToThreeLetterDegree(split);	
		} 
		if(qualificationName.length()==3)
			return convertToThreeLetterDegree(qualificationName);
		
		StringBuffer resultPlaceHolder = new StringBuffer(qualificationName.length());
		
		Stream.of(qualificationName.split(" ")).forEach(stringPart -> {
			if (stringPart.length() > 1)
				resultPlaceHolder.append(stringPart.substring(0, 1).toUpperCase())
						.append(stringPart.substring(1).toLowerCase());
			else
				resultPlaceHolder.append(stringPart.toUpperCase());

			resultPlaceHolder.append(" ");
		});
		return StringUtils.trim(resultPlaceHolder.toString());

	}
	
	private Long setQualificationWeightage(String qualificationName) {
		Long weightage  = 0l;
		if(qualificationName == null)
			weightage  = 0l;
		if(qualificationName!=null && qualificationName.indexOf("Bach")>-1) 
			weightage  = 3l;
		else if( qualificationName!=null && (qualificationName.indexOf("Mast")>-1  || qualificationName.indexOf("Post")>-1))
			weightage  = 4l;
		else
			weightage  = -1l;
		return weightage;	
	}

	private void injestCourseInformation(CandidateEducation candidateEducation) {
		if (candidateEducation.getCourse().getCourse().equals(Constants.OTHER)) {
			Course course = courseRepository.findByCourse(candidateEducation.getCapturedCourse());
			if (course == null) {
				Course newCourse = new Course();
				newCourse.setCourse(candidateEducation.getCapturedCourse());
				updateCourseIndex(courseRepository.save(newCourse));
				log.info("Saved new Course {} ", newCourse.getCourse());
				candidateEducation.setCourse(newCourse);
			} else {
				candidateEducation.setCourse(course);
			}
		} else {
			candidateEducation.setCourse(courseRepository.findByCourse(candidateEducation.getCourse().getCourse()));
		}

	}

	public CandidateEducation updateCandidateEductaion(CandidateEducation candidateEducation) {

		if ("gpa".equals(candidateEducation.getScoreType()))
			setGrade(candidateEducation);
		Candidate candidate = null;
		candidateEducation.setProjects(null);
		injestCollegeInformation(candidateEducation);
		injestCourseInformation(candidateEducation);
		injestQualificationInformation(candidateEducation);
		candidateEducationRepository.saveAll(setHighestEducation(candidateEducation, false));
		CandidateEducation result = candidateEducationRepository.save(candidateEducation);
		// candidateEducationSearchRepository.save(result);
		Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateEducation.getCandidate().getId());
		if(optionalCandidate.isPresent())
			candidate = optionalCandidate.get();
		else 
			return null; 
		candidate.addEducation(result);
		// Replace with future
		if (result.getHighestQualification())
			matcher.match(candidate);
		// updateEducationDependentMetaForDisplay(result);
		return result;
	}

	public List<CandidateEducation> getAllCandidateEducations() {
		log.debug("REST request to get all CandidateEducations");
		return candidateEducationRepository.findAll();
	}

	public CandidateEducation getCandidateEducation(Long id) {
		log.debug("Getting candidate Education for {}", id);
		Optional<CandidateEducation> optionalCandidateEducation = candidateEducationRepository.findById(id);
		CandidateEducation candidateEducation = null;
		if(optionalCandidateEducation.isPresent())
			candidateEducation = optionalCandidateEducation.get();
		if (candidateEducation != null) {
			Set<CandidateProject> candidateProjects = candidateProjectRepository.findByEducation(candidateEducation);
			candidateEducation.setProjects(candidateProjects);
			// updateEducationDependentMetaForDisplay(candidateEducation);
			log.debug("College data is {}", candidateEducation.getCollege());
		}

		return candidateEducation;
	}

	public List<CandidateEducationDTO> getEducationByCandidateId(Long id) {
		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(id);
		if(candidateEducations==null || (candidateEducations != null && candidateEducations.isEmpty()))
			return new ArrayList<CandidateEducationDTO>();
		else
			sortEducationsByHighest(candidateEducations);
		Candidate candidate = null;
		Optional<Candidate> candidateOptional = candidateRepository.findById(id);
		if(candidateOptional.isPresent())
			candidate = candidateOptional.get();
		return converter.convertCandidateEducations(candidateEducations, true,candidate);
	}
	
	private void sortEducationsByHighest(List<CandidateEducation> candidateEducations) {
		long countOfNullToDates = candidateEducations.stream().filter(education->education.getEducationToDate()==null).count();
		if(countOfNullToDates>1) {
			Comparator<CandidateEducation> comparator = new Comparator<CandidateEducation>() {
				public int compare(final CandidateEducation e1, CandidateEducation e2) {
					if(e1.getHighestQualification() && !e2.getHighestQualification()) {
						return 1;
					} else if(!e1.getHighestQualification() && e2.getHighestQualification()) {
						return -1;
					}
					else return 0;
				}
			};
			Collections.sort(candidateEducations,comparator);
		}
	}

	public void deleteCandidateEducation(Long id) {
		Optional<CandidateEducation> optionalEducation = candidateEducationRepository.findById(id);
		CandidateEducation education = null;
		if(optionalEducation.isPresent())
			education = optionalEducation.get();
		else return;
		List<CandidateEducation> candidateEducations= setHighestEducation(education, true);
		Candidate candidate = education.getCandidate();
		candidate.getEducations().clear();
		//candidate.getEducations().addAll((setHighestEducation(education, true)));
		candidate.getEducations().addAll(candidateEducations);
		log.debug("Educaiton set post removing education is {}", candidate.getEducations());
		if(candidate.getEducations().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_EDUCATION_PROFILE, true);
		matcher.match(candidate);
		// candidateEducationSearchRepository.delete(id);
	}

	public List<CandidateEducation> searchCandidateEducations(String query) {
		List<CandidateEducation> candidateEducations = StreamSupport
				.stream(candidateEducationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		return candidateEducations;
	}

	public List<CandidateEducation> searchCandidateEducationsOrderedByToDate(String query) {
		return candidateEducationSearchRepository.findByCandidateIdOrderByEducationToDateDesc(query);
	}

	private void updateUniversityIndex(University university) {
		com.drishika.gradzcircle.domain.elastic.University universityElasticInstance = new com.drishika.gradzcircle.domain.elastic.University();
		try {
			BeanUtils.copyProperties(universityElasticInstance, university);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for university elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
			// @TODO - SEND EMAIL Alert
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for university elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new UniversityEntityBuilder(universityElasticInstance.getId())
				.name(universityElasticInstance.getUniversityName())
				.suggest(new String[] { universityElasticInstance.getUniversityName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.University.class);
	}

	private void updateCollegeIndex(College college) {
		com.drishika.gradzcircle.domain.elastic.College collegeElasticInstance = new com.drishika.gradzcircle.domain.elastic.College();
		try {
			BeanUtils.copyProperties(collegeElasticInstance, college);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for college elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for college elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(
				new CollegeEntityBuilder(collegeElasticInstance.getId()).name(collegeElasticInstance.getCollegeName())
						.suggest(new String[] { collegeElasticInstance.getCollegeName() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.College.class);
	}

	private void updateQualificationIndex(Qualification qualification) {
		com.drishika.gradzcircle.domain.elastic.Qualification qualificationElasticInstance = new com.drishika.gradzcircle.domain.elastic.Qualification();
		try {
			BeanUtils.copyProperties(qualificationElasticInstance, qualification);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for qualification elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for qualification elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new QualificationEntityBuilder(qualificationElasticInstance.getId())
				.name(qualificationElasticInstance.getQualification())
				.suggest(new String[] { qualificationElasticInstance.getQualification() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Qualification.class);
	}

	private void updateCourseIndex(Course course) {
		com.drishika.gradzcircle.domain.elastic.Course courseElasticInstance = new com.drishika.gradzcircle.domain.elastic.Course();
		try {
			BeanUtils.copyProperties(courseElasticInstance, course);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for course elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for course elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate
				.index(new CourseEntityBuilder(courseElasticInstance.getId()).name(courseElasticInstance.getCourse())
						.suggest(new String[] { courseElasticInstance.getCourse() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Course.class);
	}

	// If lnaguage is Other and saving new Language. Currently not suppoterd. And
	// should be in Candidate LanguageService calss.
	private void updateLanguageIndex(Language language) {
		com.drishika.gradzcircle.domain.elastic.Language languageElasticInstance = new com.drishika.gradzcircle.domain.elastic.Language();
		try {
			BeanUtils.copyProperties(languageElasticInstance, language);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for language elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for language elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(
				new CourseEntityBuilder(languageElasticInstance.getId()).name(languageElasticInstance.getLanguage())
						.suggest(new String[] { languageElasticInstance.getLanguage() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Language.class);
	}

	public Stream<CandidateEducation> getCandidateEducationBeforeSuppliedDate(LocalDate date) {
		return candidateEducationRepository.findByEducationToDateBeforeAndHighestQualification(date);
	}

	public Stream<CandidateEducation> getCandidateEducationAfterSuppliedDate(LocalDate date) {
		return candidateEducationRepository.findByEducationToDateAfterAndHighestQualification(date);
	}

	public Stream<CandidateEducation> getCandidateEducationBetweenSuppliedDates(LocalDate fromDate, LocalDate toDate) {
		LocalDate today = LocalDate.now();
		if(toDate.isAfter(today)) {
			log.debug("To date is in future, getting candidates with that complted education beyond {} ",fromDate);
			return candidateEducationRepository.findByEducationToDateFutureAndHighestQualification(fromDate);
		} else {
			return candidateEducationRepository.findByEducationToDateBetweenAndHighestQualification(fromDate, toDate);
		}
	}

	public Stream<CandidateEducation> getEducationForMatchEligibleCandidate() {
		return candidateEducationRepository.findAllHighestCandidateEducationForMatchEligilbeCandidates();
	}
	
	public Boolean doesCandidateHaveEducation(Long candidateId) {
		if(candidateEducationRepository.findByCandidateId(candidateId).isEmpty())
			return false;
		else
			return true;
	}
}
