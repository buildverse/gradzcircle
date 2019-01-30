/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.ProfileCategory;
import com.drishika.gradzcircle.repository.ProfileCategoryRepository;

/**
 * @author abhinav
 *
 */
@Service
//@Depends need to be commented while running tests
@DependsOn("liquibase")
@Transactional
public class ProfileScoreCalculator {
	
	private final Logger log = LoggerFactory.getLogger(ProfileScoreCalculator.class);

	private ProfileCategoryRepository profileCategoryRepository;
	
	private  final Map<String, Long> profileCategoryWeightMap = new HashMap<>();
	
	private  final Map<String, ProfileCategory> profileCategoryMap = new HashMap<>();
	
	public ProfileScoreCalculator(ProfileCategoryRepository profileCategoryRepository) {
		this.profileCategoryRepository = profileCategoryRepository;
	}
	
	//@PsotConstruct needs to be commented while running tests
	@PostConstruct
	public Map<String, Long> init() {
		populateProfileCategoryWeightMap();
		return profileCategoryWeightMap;

	}

	public void populateProfileCategoryWeightMap() {
		List<ProfileCategory> profileCategories = profileCategoryRepository.findAll();
		log.info("Categories list is {}",profileCategories);
		Long totalCategoryWeight = 0L;
		Iterator<ProfileCategory> categories = profileCategories.iterator();
		while(categories.hasNext()) {
			ProfileCategory category = categories.next();
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_BASIC_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_BASIC_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_BASIC_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_CERTIFICATION_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_CERTIFICATION_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_CERTIFICATION_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_EDUCATION_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_EDUCATION_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_EDUCATION_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_EXPERIENCE_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_EXPERIENCE_PROFILE,category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_EXPERIENCE_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_NON_ACADEMIC_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_NON_ACADEMIC_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
			if (category.getCategoryName().equalsIgnoreCase(Constants.CANDIDATE_LANGUAGE_PROFILE)) {
				profileCategoryWeightMap.put(Constants.CANDIDATE_LANGUAGE_PROFILE, category.getWeightage().longValue());
				profileCategoryMap.put(Constants.CANDIDATE_LANGUAGE_PROFILE, category);
				totalCategoryWeight += category.getWeightage().longValue();
			}
		}
		profileCategoryWeightMap.put(Constants.CANDIDATE_PROFILE_TOTAL_WEIGHT, totalCategoryWeight);
		log.info("Profile Category map has been populated {}", profileCategoryMap);
	}
	
	private CandidateProfileScore setInitialProfileScore(Candidate candidate, String categoryName) {
		CandidateProfileScore profileScore =null;
		if(candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(categoryName)).findFirst().isPresent()) {
			profileScore = candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(categoryName)).findFirst().get();	
		} else 
			profileScore = new CandidateProfileScore(candidate,profileCategoryMap.get(categoryName));
		return profileScore;
	}
	
	public void updateProfileScore(Candidate candidate,String categoryName, Boolean remove) {
		if(profileCategoryWeightMap.isEmpty())
			init();
		Double totalScore = candidate.getProfileScore()!=null?candidate.getProfileScore():0D;
		CandidateProfileScore profileScore =null;
		if (Constants.CANDIDATE_CERTIFICATION_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				if(!candidate.getProfileScores().stream().filter(cert->cert.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().isPresent() ||
						candidate.getProfileScores().stream().filter(cert->cert.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()==0) {
					profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_CERTIFICATION_PROFILE).doubleValue());
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_CERTIFICATION_PROFILE);
				}
			} else {
				if(candidate.getProfileScores().stream().filter(cert->cert.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_CERTIFICATION_PROFILE)).findFirst().get().getScore()>0) {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_CERTIFICATION_PROFILE);
				}
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_EDUCATION_PROFILE.equalsIgnoreCase(categoryName)) {
			
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				if(!candidate.getProfileScores().stream().filter(edu->edu.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().isPresent()||
						candidate.getProfileScores().stream().filter(edu->edu.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()==0) {
					profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_EDUCATION_PROFILE).doubleValue());
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_EDUCATION_PROFILE);
				}					
			} else {
				if(candidate.getProfileScores().stream().filter(edu->edu.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EDUCATION_PROFILE)).findFirst().get().getScore()>0) {
					profileScore.setScore(0d);
					totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_EDUCATION_PROFILE);
				}
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_EXPERIENCE_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				if(!candidate.getProfileScores().stream().filter(exp->exp.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().isPresent() || 
						candidate.getProfileScores().stream().filter(exp->exp.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore() == 0 ) {
					profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_EXPERIENCE_PROFILE).doubleValue());
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_EXPERIENCE_PROFILE);
				}
			} else {
				if(candidate.getProfileScores().stream().filter(exp->exp.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_EXPERIENCE_PROFILE)).findFirst().get().getScore() > 0 ) {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_EXPERIENCE_PROFILE);
				}
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_LANGUAGE_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				if(!candidate.getProfileScores().stream().filter(lang->lang.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().isPresent() ||
						candidate.getProfileScores().stream().filter(lang->lang.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()==0) {
					profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_LANGUAGE_PROFILE).doubleValue());
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_LANGUAGE_PROFILE);
				}
			} else {
				if(candidate.getProfileScores().stream().filter(lang->lang.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_LANGUAGE_PROFILE)).findFirst().get().getScore()>0) {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_LANGUAGE_PROFILE);
				}
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_NON_ACADEMIC_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				if(!candidate.getProfileScores().stream().filter(nonAcad->nonAcad.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().isPresent() ||
						candidate.getProfileScores().stream().filter(nonAcad->nonAcad.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()==0) {
					profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_NON_ACADEMIC_PROFILE).doubleValue());
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_NON_ACADEMIC_PROFILE);
				}
			} else {
				if(candidate.getProfileScores().stream().filter(nonAcad->nonAcad.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_NON_ACADEMIC_PROFILE)).findFirst().get().getScore()>0) {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_NON_ACADEMIC_PROFILE);
				}
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE).doubleValue());
				if(!candidate.getProfileScores().stream().filter(personal->personal.getProfileCategory().getCategoryName().equals(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE)).findFirst().isPresent())
					totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE);
			} else {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE);
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else if (Constants.CANDIDATE_BASIC_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE).doubleValue());
				totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE);
			} else {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE);
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		}
		log.info("Final is {}",candidate.getProfileScores());
	}
	
	private void setCandidateProfileOnCandidate(Candidate candidate, CandidateProfileScore profileScore,Double totalScore) {
		candidate.setProfileScore(totalScore);
		Set<CandidateProfileScore> scores = new HashSet<>();
		scores.add(profileScore);
		candidate.getProfileScores().forEach(score -> scores.add(score));
		candidate.getProfileScores().addAll(scores);
	}
	
}
