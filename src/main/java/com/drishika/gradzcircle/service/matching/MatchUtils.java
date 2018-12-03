/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.mutable.MutableDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.drishika.gradzcircle.config.Constants;
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
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;

/**
 * @author abhinav
 *
 */
@Component
public class MatchUtils {

	private final Logger log = LoggerFactory.getLogger(MatchUtils.class);

	private final JobFilterParser jobFilterParser;

	private final FilterRepository filterRepository;

	private final CourseRepository courseRepository;

	private final QualificationRepository qualificationRepository;

	private final CollegeRepository collegeRepository;

	private final UniversityRepository universityRepository;

	private final GenderRepository genderRepository;

	private final LanguageRepository languageRepository;

	private final CandidateRepository candidateRepository;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private  final Map<String, Long> jobFilterWeightMap = new HashMap<>();
	
	

	public MatchUtils(JobFilterParser jobFilterParser, FilterRepository filterRepository,
			CourseRepository courseRepository, QualificationRepository qualificationRepository,
			CollegeRepository collegeRepository, UniversityRepository universityRepository,
			GenderRepository genderRepository, LanguageRepository languageRepository,
			CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateRepository candidateRepository) {
		this.jobFilterParser = jobFilterParser;
		this.filterRepository = filterRepository;
		this.collegeRepository = collegeRepository;
		this.courseRepository = courseRepository;
		this.qualificationRepository = qualificationRepository;
		this.genderRepository = genderRepository;
		this.languageRepository = languageRepository;
		this.universityRepository = universityRepository;
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateRepository = candidateRepository;
	}

	public JobFilterObject retrieveJobFilterObjectFromJob(Job job) {
		JobFilterObject jobFilter = null;
		Set<JobFilter> jobFilters = job.getJobFilters();
		if (jobFilters != null && jobFilters.size() != 0) {
			String filterDescription = jobFilters.stream().findFirst().get().getFilterDescription();
			jobFilter = jobFilterParser.getFilterObjectFromJson(filterDescription);
		}
		log.info("Parsed Filter Object is {}", jobFilter);
		return jobFilter;
	}

	@PostConstruct
	public Map<String, Long> init() {
		//if (jobFilterWeightMap == null && jobFilterWeightMap.size() == 0)
		populateJobFilterWeightMap();
		return jobFilterWeightMap;

	}

	public void populateJobFilterWeightMap() {
		
			List<Filter> filters = filterRepository.findAll();
			filters.forEach(filter -> {
				if (filter.getFilterName().equalsIgnoreCase(Constants.COURSE))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.QUALIFICATION))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.COLLEGE))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.GENDER))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.UNIVERSITY))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.SCORE))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.GRADUATION_DATE))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
				if (filter.getFilterName().equalsIgnoreCase(Constants.LANGUAGE))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
			});
		
		log.info("Job filter Weight has been populated {}", jobFilterWeightMap);
	}

	public CandidateJob matchCandidateAndJob(JobFilterObject jobfilterObject, Candidate candidate, Job job,
			Boolean matchEducaton, Boolean matchLanguages, Boolean matchGender) {
		Double genderScore = null;
		Double languageScore = null;
		Double educationScore = null;
		Double totalScore = 0.0;
		CandidateJob candidateJobMatched = new CandidateJob(candidate, job);
		CandidateJob candidateJob = candidate.getCandidateJobs().stream().filter(candidateJobMatched::equals).findAny()
				.orElse(null);
		log.debug("Candidate Job is {}", candidateJob);

		if (matchEducaton) {
			CandidateEducation candidateEducation = candidate.getEducations().stream()
					.filter(education -> education.getHighestQualification() != null)
					.filter(education -> education.isHighestQualification()).findAny().orElse(null);
			if (candidateEducation != null)
				educationScore = matchCandidateEducationToJob(jobfilterObject, candidateEducation);
		} else {
			if (candidateJob != null) {
				educationScore = candidateJob.getEducationMatchScore();
			}
		}

		if (matchGender)
			genderScore = matchCandidateGenderAndJob(jobfilterObject, candidate);
		else {
			if (candidateJob != null) {
				genderScore = candidateJob.getGenderMatchScore();
			}
		}

		if (matchLanguages) {
			languageScore = matchLanguagesAndJob(jobfilterObject, candidate);
		} else {
			if (candidateJob != null) {
				languageScore = candidateJob.getLanguageMatchScore();
			}
		}

		Double matchEligibleScore = getMatchScoreEligible(jobfilterObject);
		log.debug("gender score , languageScore and educaitonScore are {},{},{}", genderScore, languageScore,
				educationScore);
		if (genderScore != null)
			totalScore += genderScore;
		if (languageScore != null)
			totalScore += languageScore;
		if (educationScore != null)
			totalScore += educationScore;
		/*
		 * educationScore = matchCourse(jobfilterObject.getCourses(), education,
		 * matchScoreGained, jobFilterWeightMap); educationScore +=
		 * matchQualification(jobfilterObject.getQualifications(), education,
		 * matchScoreGained, jobFilterWeightMap); educationScore +=
		 * matchColleges(jobfilterObject.getColleges(), education, matchScoreGained,
		 * jobFilterWeightMap); educationScore +=
		 * matchUniversity(jobfilterObject.getUniversities(), education,
		 * matchScoreGained, jobFilterWeightMap); if (jobfilterObject.getPercentage() ==
		 * null) { if (education.getGrade() == null) educationScore +=
		 * matchGpaScore(jobfilterObject.getGpa(), education.getPercentage(),
		 * matchScoreGained, jobFilterWeightMap); else educationScore +=
		 * matchGpaScore(jobfilterObject.getGpa(), education.getGrade() * 10,
		 * matchScoreGained, jobFilterWeightMap); } else { if (education.getGrade() ==
		 * null) educationScore += matchPercentageScore(jobfilterObject.getPercentage(),
		 * education.getPercentage(), matchScoreGained, jobFilterWeightMap); else
		 * educationScore += matchPercentageScore(jobfilterObject.getPercentage(),
		 * education.getGrade() * 10, matchScoreGained, jobFilterWeightMap); }
		 * 
		 * if (jobfilterObject.getGender() != null && matchGender) genderScore =
		 * matchGender(jobfilterObject.getGender(), education.getCandidate(),
		 * matchScoreGained, jobFilterWeightMap); if (jobfilterObject.getLanguages() !=
		 * null && jobfilterObject.getLanguages().size() > 0 && matchLanguages)
		 * languageScore = matchLanguage(jobfilterObject.getLanguages(),
		 * education.getCandidate(), matchScoreGained, jobFilterWeightMap);
		 */
		// Candidate candidate = education.getCandidate();
		// log.debug("CandidateJob from Education is ,
		// {}",candidate.getCandidateJobs());
		// CandidateJob candidateJobMatched = new CandidateJob(candidate, job);
		/*
		 * CandidateJob candidateJob =
		 * candidate.getCandidateJobs().stream().filter(candidateJobMatched::equals).
		 * findAny().orElse(null); if(candidateJob != null) {
		 * 
		 * }
		 */
		candidateJobMatched.setMatchScore(calculateMatchScore(totalScore, matchEligibleScore));
		log.debug("seting the language score as {}", languageScore);
		candidateJobMatched.setLanguageMatchScore(languageScore);
		log.debug("seting the gender score as {}", genderScore);
		candidateJobMatched.setGenderMatchScore(genderScore);
		candidateJobMatched.setEducationMatchScore(educationScore);
		candidateJobMatched.setTotalEligibleScore(matchEligibleScore);
		return candidateJobMatched;
	}

	private Double matchCandidateEducationToJob(JobFilterObject jobfilterObject, CandidateEducation education) {
		Double educationScore = 0.0;
		log.debug("Filter weight Mpa before matching starts is {}",jobFilterWeightMap);
		educationScore = matchCourse(jobfilterObject.getCourses(), education);
		educationScore += matchQualification(jobfilterObject.getQualifications(), education);
		educationScore += matchColleges(jobfilterObject.getColleges(), education);
		educationScore += matchUniversity(jobfilterObject.getUniversities(), education);
		if (jobfilterObject.getPercentage() == null) {
			if (education.getGrade() == null)
				educationScore += matchGpaScore(jobfilterObject.getGpa(), education.getPercentage());
			else
				educationScore += matchGpaScore(jobfilterObject.getGpa(), education.getGrade() * 10);
		} else {
			if (education.getGrade() == null)
				educationScore += matchPercentageScore(jobfilterObject.getPercentage(), education.getPercentage());
			else
				educationScore += matchPercentageScore(jobfilterObject.getPercentage(), education.getGrade() * 10);
		}
		return educationScore;
	}

	private Double matchCandidateGenderAndJob(JobFilterObject jobfilterObject, Candidate candidate) {
		return matchGender(jobfilterObject, candidate);
	}

	private Double matchLanguagesAndJob(JobFilterObject jobfilterObject, Candidate candidate) {
		return matchLanguage(jobfilterObject, candidate);

	}

	/*
	 * public CandidateJob matchCandidateLanguageAndJob(MutableDouble
	 * matchScoreGained, MutableDouble matchScoreEligible,JobFilterObject
	 * jobfilterObject, CandidateLanguageProficiency
	 * candidateLanguageProficiency,Job job) { matchScoreEligible.setValue(0);
	 * matchScoreGained.setValue(0); CandidateJob candidateJobMatched = new
	 * CandidateJob(candidateLanguageProficiency.getCandidate(), job);
	 * log.debug("Creating Match set for Candidate {} and Job {}"
	 * ,candidateLanguageProficiency.getCandidate(),job); Candidate candidate =
	 * candidateRepository.findOne(candidateLanguageProficiency.getCandidate().getId
	 * ()); CandidateJob candidateJob = null;
	 * if(candidate.getCandidateJobs().stream().filter(candidateJobMatched::equals).
	 * findFirst().isPresent()) { candidateJob =
	 * candidate.getCandidateJobs().stream().filter(candidateJobMatched::equals).
	 * findFirst().get();
	 * log.debug("Found existing candidateJob object {}",candidateJob); } else
	 * candidateJob = candidateJobMatched; if(jobfilterObject.getLanguages()!=null
	 * && jobfilterObject.getLanguages().size()>0) { Double languageScore =
	 * matchLanguage(jobfilterObject.getLanguages(), candidateLanguageProficiency,
	 * matchScoreGained, jobFilterWeightMap, matchScoreEligible);
	 * candidateJob.setLanguageMatchScore(languageScore); } else {
	 * candidateJob.setLanguageMatchScore(0d); }
	 * candidateJob.setMatchScore(candidateJob.getEducationMatchScore()+(
	 * candidateJob.getGenderMatchScore()!=null?candidateJob.getGenderMatchScore():0
	 * .0) +(candidateJob.getLanguageMatchScore()!=null?candidateJob.
	 * getLanguageMatchScore():0.0)); return candidateJob;
	 * 
	 * }
	 */
	/*
	 * public CandidateJob matchCandidateGenderAndJob(MutableDouble
	 * matchScoreGained, MutableDouble matchScoreEligible,JobFilterObject
	 * jobfilterObject, Candidate candidate,Job job) {
	 * matchScoreEligible.setValue(0); matchScoreGained.setValue(0); CandidateJob
	 * candidateJobMatched = new CandidateJob(education.getCandidate(), job);
	 * log.debug("Creating Match set for Candidate {} and Job {}",education.
	 * getCandidate(),job); CandidateJob candidateJob =
	 * education.getCandidate().getCandidateJobs().stream().filter(cJob->cJob.equals
	 * (candidateJobMatched)).findFirst().get();
	 * log.debug("Found existing candidateJob object {}",candidateJob);
	 * if(jobfilterObject.getGender()!=null ) { Double genderScore =
	 * matchGender(jobfilterObject.getGender(), education, matchScoreGained,
	 * jobFilterWeightMap, matchScoreEligible);
	 * candidateJob.setGenderMatchScore(genderScore); } else {
	 * candidateJob.setGenderMatchScore(0d); } return candidateJob; }
	 */
	private Double calculateMatchScore(Double matchScoreGained, Double matchScoreEligible) {
		log.debug("Score gained to total is {},{}", matchScoreGained, matchScoreEligible);
		return (double) Math.round(matchScoreGained / matchScoreEligible * 100);
	}

	private Double matchCourse(List<Course> jobFilterCourses, CandidateEducation education) {
		Double courseScore = 0D;
		if (jobFilterCourses == null || jobFilterCourses.size() == 0)
			return courseScore;
		else {
			if (jobFilterCourses.size() > 0)
				// matchScoreEligible.add(jobFilterWeightMap.get(Constants.COURSE));
				for (Course filterCourse : jobFilterCourses) {
					Course course = courseRepository.findByCourse(filterCourse.getValue());
					if (course != null && course.equals(education.getCourse())) {
						// matchScoreGained.add(jobFilterWeightMap.get(Constants.COURSE));
						courseScore = new Double(jobFilterWeightMap.get(Constants.COURSE));
						log.debug("Matching on Course");
					}
				}
			return courseScore;
		}
	}

	private Double matchQualification(List<Qualification> jobFilterQualification, CandidateEducation education) {
		double previousScoreMatchedValue = 0;
		double qualificationScore = 0;
		if (jobFilterQualification == null || jobFilterQualification.size() == 0)
			return qualificationScore;
		else {
			if (jobFilterQualification.size() > 0)
				// matchScoreEligible.add(jobFilterWeightMap.get(Constants.QUALIFICATION));
				for (Qualification filterQualification : jobFilterQualification) {
					Qualification qualification = qualificationRepository
							.findByQualification(filterQualification.getValue());
					log.debug("Qulaification from filter is {}", qualification);
					if (qualification != null) {
						if (qualification.equals(education.getQualification())) {
							qualificationScore = jobFilterWeightMap.get(Constants.QUALIFICATION);
							log.debug("Perfect matching on Quaification");
							break;
						} else {
							qualificationScore = jobFilterWeightMap.get(Constants.QUALIFICATION) - Math
									.abs(qualification.getWeightage() - education.getQualification().getWeightage());
							if (previousScoreMatchedValue > qualificationScore)
								qualificationScore = previousScoreMatchedValue;
							else
								previousScoreMatchedValue = qualificationScore;
							log.debug("Matching on Quaification {}", qualification.getQualification());
						}
					}
				}
			log.debug("final qualificaiton Score  {}", qualificationScore);
			// matchScoreGained.add(qualificationScore);
			return qualificationScore;
		}
	}

	private Double matchColleges(List<College> jobFilterCollege, CandidateEducation education) {
		Double collegeScore = 0d;
		if (jobFilterCollege == null || jobFilterCollege.size() == 0)
			return collegeScore;
		else {
			if (jobFilterCollege.size() > 0)
				// matchScoreEligible.add(jobFilterWeightMap.get(Constants.COLLEGE));
				for (College filterCollege : jobFilterCollege) {
					College college = collegeRepository.findByCollegeName(filterCollege.getValue());
					if (college != null && college.equals(education.getCollege())) {
						// matchScoreGained.add(jobFilterWeightMap.get(Constants.COLLEGE));
						collegeScore = jobFilterWeightMap.get(Constants.COLLEGE).doubleValue();
						log.debug("Matching on College");
					}
				}
			return collegeScore;
		}
	}

	private Double matchUniversity(List<University> jobFilterUniversity, CandidateEducation education) {
		Double universityScore = 0d;
		if (jobFilterUniversity == null || jobFilterUniversity.size() == 0)
			return universityScore;
		else {
			if (jobFilterUniversity.size() > 0)
				// matchScoreEligible.add(jobFilterWeightMap.get(Constants.UNIVERSITY));
				for (University filterUniversity : jobFilterUniversity) {
					University university = universityRepository.findByUniversityName(filterUniversity.getValue());
					if (university != null && university.equals(education.getCollege().getUniversity())) {
						// matchScoreGained.add(jobFilterWeightMap.get(Constants.UNIVERSITY));
						universityScore = jobFilterWeightMap.get(Constants.UNIVERSITY).doubleValue();
						log.debug("Matching on University");
					}
				}
			return universityScore;
		}
	}

	private Double matchGpaScore(Double jobFilterGpa, double candidateEducaionScore) {
		Double gpaScore = 0d;
		if (jobFilterGpa == null)
			return gpaScore;
		else {
			// matchScoreEligible.add(jobFilterWeightMap.get(Constants.SCORE));
			Double gpaScoreForConversion = jobFilterGpa * 10;

			if (gpaScoreForConversion <= candidateEducaionScore) {
				log.debug("Matching on grade");
				// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE));
				gpaScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue();
			} else {
				Double scoreDifference = gpaScoreForConversion - candidateEducaionScore;
				if (scoreDifference == 2) {
					// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE) - 2);
					gpaScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue() - 2;
				} else if (scoreDifference == 1) {
					// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE) - 1);
					gpaScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue() - 1;
				}
				log.debug("condititonla Matching on grade");
			}

			return gpaScore;
		}
	}

	private Double matchPercentageScore(Double jobFilterPercent, double candidateEducationScore) {
		Double percentageScore = 0d;
		if (jobFilterPercent == null)
			return percentageScore;
		else {
			// matchScoreEligible.add(jobFilterWeightMap.get(Constants.SCORE));
			if (jobFilterPercent <= candidateEducationScore) {
				// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE));
				percentageScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue();
				log.debug("Matching on percent");
			} else {
				Double scoreDifference = jobFilterPercent - candidateEducationScore;
				if (scoreDifference == 2) {
					// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE) - 2);
					percentageScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue() - 2;
				} else if (scoreDifference == 1) {
					// matchScoreGained.add(jobFilterWeightMap.get(Constants.SCORE) - 1);
					percentageScore = jobFilterWeightMap.get(Constants.SCORE).doubleValue() - 1;
				}
				log.debug("Matching on conditional percent");
			}

			return percentageScore;
		}
	}

	private Double matchGender(JobFilterObject jobfilterObject, Candidate candidate) {
		Double genderScore = null;
		if (jobfilterObject.getGender() == null)
			return genderScore;
		else {
			// MutableDouble genderOverAllScore = new
			// MutableDouble(jobFilterWeightMap.get(Constants.GENDER));
			// matchScoreEligible.add(jobFilterWeightMap.get(Constants.GENDER));

			Gender gender = genderRepository.findByGender(jobfilterObject.getGender().getGender());
			if (gender != null && gender.equals(candidate.getGender())) {
				// matchScoreGained.add(jobFilterWeightMap.get(Constants.GENDER));
				genderScore = new Double((jobFilterWeightMap.get(Constants.GENDER)));
				log.debug("Matching on gender");
			}
			return genderScore;

		}
	}

	private Double matchLanguage(JobFilterObject jobfilterObject, Candidate candidate) {
		List<Language> jobFilterLanguages = jobfilterObject.getLanguages();
		Double languageScore = null;
		if (jobFilterLanguages == null || jobFilterLanguages.size() <= 0)
			return languageScore;
		else {
			double numberOfMatchedLanguage = 0;
			// if (jobFilterLanguages.size() > 0)
			// matchScoreEligible.add(jobFilterWeightMap.get(Constants.LANGUAGE));
			// List<CandidateLanguageProficiency> proficiencies =
			// candidateLanguageProficiencyRepository
			// .findCandidateLanguageProficienciesByCandidateId(candidate.getId());
			Set<CandidateLanguageProficiency> proficiencies = candidate.getCandidateLanguageProficiencies();
			log.debug("proficienceis are {}", proficiencies);
			for (Language filterLanguage : jobFilterLanguages) {
				Language language = languageRepository.findByLanguage((filterLanguage.getValue()));
				log.debug("Language from repo are {}", language);
				for (CandidateLanguageProficiency proficiency : proficiencies) {
					if (proficiency.getLanguage().equals(language)) {
						numberOfMatchedLanguage++;
						log.debug("Matching on language");
					}
				}
			}
			double matchRate = numberOfMatchedLanguage / jobFilterLanguages.size();
			// matchScoreGained.add(jobFilterWeightMap.get(Constants.LANGUAGE) * matchRate);
			languageScore = (double) Math.round(jobFilterWeightMap.get(Constants.LANGUAGE) * matchRate);
			return languageScore;
		}
	}

	private Double getMatchScoreEligible(JobFilterObject jobfilterObject) {
		//getJobFilterWeightMap();
		MutableDouble matchScoreEligible = new MutableDouble(0);
		if (jobfilterObject.getColleges() != null && jobfilterObject.getColleges().size() > 0) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.COLLEGE));
		}
		if (jobfilterObject.getUniversities() != null && jobfilterObject.getUniversities().size() > 0) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.UNIVERSITY));
		}
		if (jobfilterObject.getCourses() != null && jobfilterObject.getCourses().size() > 0) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.COURSE));
		}
		if (jobfilterObject.getQualifications() != null && jobfilterObject.getQualifications().size() > 0) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.QUALIFICATION));
		}
		if (jobfilterObject.getScoreType() != null) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.SCORE));
		}
		if (jobfilterObject.getLanguages() != null && jobfilterObject.getLanguages().size() > 0) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.LANGUAGE));
		}
		if (jobfilterObject.getGender() != null) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.GENDER));
		}
		return matchScoreEligible.toDouble();
	}

}
