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
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Filter;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.domain.University;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CollegeRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.FilterRepository;
import com.drishika.gradzcircle.repository.GenderRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.UniversityRepository;

/**
 * @author abhinav
 *
 */
@Component
@DependsOn("liquibase")
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
	
	private final SkillsRepository skillsRepository;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private  final Map<String, Long> jobFilterWeightMap = new HashMap<>();
	
	

	public MatchUtils(JobFilterParser jobFilterParser, FilterRepository filterRepository,
			CourseRepository courseRepository, QualificationRepository qualificationRepository,
			CollegeRepository collegeRepository, UniversityRepository universityRepository,
			GenderRepository genderRepository, LanguageRepository languageRepository,
			CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateRepository candidateRepository,SkillsRepository skillsRepository) {
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
		this.skillsRepository = skillsRepository;
	}

	public JobFilterObject retrieveJobFilterObjectFromJob(Job job) {
		JobFilterObject jobFilter = null;
		String filterDescription = null;
		Set<JobFilter> jobFilters = job.getJobFilters();
		if (jobFilters != null && jobFilters.size() != 0) {
			JobFilter filter = jobFilters.stream().findFirst().orElse(null);
			if(filter != null) {
				filterDescription = filter.getFilterDescription();
			}			
			jobFilter = jobFilterParser.getFilterObjectFromJson(filterDescription);
		}
		log.info("Parsed Filter Object is {}", jobFilter);
		return jobFilter;
	}

	@PostConstruct
	public Map<String, Long> init() {
		
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
				if (filter.getFilterName().equalsIgnoreCase(Constants.SKILL))
					jobFilterWeightMap.put(filter.getFilterName(), filter.getMatchWeight());
			});
		
		log.info("Job filter Weight has been populated {}", jobFilterWeightMap);
	}

	public CandidateJob matchCandidateAndJob(JobFilterObject jobfilterObject, Candidate candidate, Job job,
			Boolean matchEducaton, Boolean matchLanguages, Boolean matchGender, Boolean matchSkills) {
		Double genderScore = null;
		Double languageScore = null;
		Double educationScore = null;
		Double skillScore = null;
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
			if(educationScore == null) {
				job.getCandidateJobs().remove(candidateJob);
				return null;
			}
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
		
		if (matchSkills) {
			skillScore = matchSkillAndJob(jobfilterObject, candidate);
		} else {
			if (candidateJob != null) {
				skillScore = candidateJob.getSkillMatchScore();
			}
		}

		Double matchEligibleScore = getMatchScoreEligible(jobfilterObject);
		log.info("gender score , languageScore and educaitonScore are {},{},{}", genderScore, languageScore,
				educationScore);
		if (genderScore != null)
			totalScore += genderScore;
		if (languageScore != null)
			totalScore += languageScore;
		if (educationScore != null)
			totalScore += educationScore;
		if (skillScore != null)
			totalScore += skillScore;
		candidateJobMatched.setMatchScore(calculateMatchScore(totalScore, matchEligibleScore));
		log.info("seting the language score as {}", languageScore);
		candidateJobMatched.setLanguageMatchScore(languageScore);
		log.info("seting the gender score as {}", genderScore);
		candidateJobMatched.setGenderMatchScore(genderScore);
		candidateJobMatched.setEducationMatchScore(educationScore);
		candidateJobMatched.setSkillMatchScore(skillScore);
		candidateJobMatched.setTotalEligibleScore(matchEligibleScore);
		return candidateJobMatched;
	}

	private Double matchCandidateEducationToJob(JobFilterObject jobfilterObject, CandidateEducation education) {
		Double educationScore = 0.0;
		log.debug("Filter weight Mpa before matching starts is {}",jobFilterWeightMap);
		log.debug("JobFilterObject is {}",jobfilterObject);
		educationScore = matchCourse(jobfilterObject.getCourses(), education);
		log.debug("EducationScore after curse matching is {}",educationScore);
		if(educationScore==0.0)
			return null;
		Double qualificationScore = matchQualification(jobfilterObject.getQualifications(), education);
		if(qualificationScore == 0.0)
			return null;
		else		
			educationScore += qualificationScore;
		
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
	
	private Double matchSkillAndJob(JobFilterObject jobfilterObject, Candidate candidate) {
		return matchSkill(jobfilterObject, candidate);

	}

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
					log.debug("The filter course is {}",filterCourse.getValue());
					log.debug("The education course is {}",education.getCourse().getId());
					log.debug("Course Repo has {}",courseRepository.findAll());
					Course course = courseRepository.findByCourse(filterCourse.getValue());
					log.debug("The filter course  is {}",course);
					if (course != null && course.equals(education.getCourse())) {
						// matchScoreGained.add(jobFilterWeightMap.get(Constants.COURSE));
						courseScore = new Double(jobFilterWeightMap.get(Constants.COURSE));
						log.info("Matching on Course");
						
					}
				}
			return courseScore;
		}
	}

	private Double matchQualification(List<Qualification> jobFilterQualification, CandidateEducation education) {
		log.debug("Entering mathcing qulaification with {}",jobFilterQualification);
		double previousScoreMatchedValue = 0;
		double qualificationScore = 0;
		if (jobFilterQualification == null || jobFilterQualification.size() == 0)
			return qualificationScore;
		else {
			if (jobFilterQualification.size() > 0)
				for (Qualification filterQualification : jobFilterQualification) {
					Qualification qualification = qualificationRepository
							.findByQualification(filterQualification.getValue());
					log.debug("Job Qulaiifcation is {} ",jobFilterQualification);
					log.debug("Qulaification from filter is {}", qualification);
					log.debug("Qulaification from education is {}", education.getQualification());
					if (qualification != null && qualification.getCategory().equalsIgnoreCase(education.getQualification().getCategory())) {
						if (qualification.equals(education.getQualification())) {
							qualificationScore = jobFilterWeightMap.get(Constants.QUALIFICATION);
							log.info("Perfect matching on Quaification");
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
			log.info("final qualificaiton Score  {}", qualificationScore);
			
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
						log.info("Matching on College");
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
						log.info("Matching on University");
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
				log.info("Matching on grade");
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
				log.info("condititonla Matching on grade");
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
				log.info("Matching on percent");
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
			log.debug("Gender from repo is {}",gender);
			if (gender != null && gender.equals(candidate.getGender())) {
				// matchScoreGained.add(jobFilterWeightMap.get(Constants.GENDER));
				genderScore = new Double((jobFilterWeightMap.get(Constants.GENDER)));
				log.info("Matching on gender");
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
			log.debug("proficienceis are {} ", proficiencies);
			for (Language filterLanguage : jobFilterLanguages) {
				log.debug("Looking for  {}", filterLanguage.getValue());
				Language language = languageRepository.findByLanguage((filterLanguage.getValue()));
				log.debug("Language from repo are {}", language);
				for (CandidateLanguageProficiency proficiency : proficiencies) {
					if (proficiency.getLanguage().equals(language)) {
						numberOfMatchedLanguage++;
						log.info("Matching on language");
					}
				}
			}
			double matchRate = numberOfMatchedLanguage / jobFilterLanguages.size();
			// matchScoreGained.add(jobFilterWeightMap.get(Constants.LANGUAGE) * matchRate);
			languageScore = (double) Math.round(jobFilterWeightMap.get(Constants.LANGUAGE) * matchRate);
			return languageScore;
		}
	}
	
	private Double matchSkill(JobFilterObject jobfilterObject, Candidate candidate) {
		List<Skills> jobFilterSkills = jobfilterObject.getSkills();
		Double skillScore = null;
		if (jobFilterSkills == null || jobFilterSkills.size() <= 0)
			return skillScore;
		else {
			double numberOfMatchedSkill = 0;
			Set<CandidateSkills> candidateSkills = candidate.getCandidateSkills();
			log.debug("candidate skills are {} ", candidateSkills);
			log.debug("Job Filter skills are {} ", jobFilterSkills);
			for (Skills skillFilter : jobFilterSkills) {
				log.debug("Looking for  {}", skillFilter.getValue());
				Skills skill = skillsRepository.findBySkillIgnoreCase((skillFilter.getValue()));
				log.debug("Skill from repo are {}", skill.getSkill());
				for (CandidateSkills candidateSkill : candidateSkills) {
					if (candidateSkill.getSkills().getSkill().equals(skill.getSkill())) {
						numberOfMatchedSkill++;
						log.info("Matching on Skill {}",skill.getSkill());
					}
				}
			}
			double matchRate = numberOfMatchedSkill / jobFilterSkills.size();
			log.debug("Match rate is {}",matchRate);
			// matchScoreGained.add(jobFilterWeightMap.get(Constants.LANGUAGE) * matchRate);
			skillScore = (double) Math.round(jobFilterWeightMap.get(Constants.SKILL) * matchRate);
			return skillScore;
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
		if (jobfilterObject.getSkills() != null) {
			matchScoreEligible.add(jobFilterWeightMap.get(Constants.SKILL));
		}
		return matchScoreEligible.toDouble();
	}

}
