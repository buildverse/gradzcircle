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
			profileCategoryWeightMap.put(category.getCategoryName(), category.getWeightage().longValue());
			profileCategoryMap.put(category.getCategoryName(), category);
			totalCategoryWeight += category.getWeightage().longValue();
		}
		profileCategoryWeightMap.put(Constants.CANDIDATE_PROFILE_TOTAL_WEIGHT, totalCategoryWeight);
		log.info("Profile Category map has been populated {}", profileCategoryMap);
	}
	
	private CandidateProfileScore setInitialProfileScore(Candidate candidate, String categoryName) {
		CandidateProfileScore profileScore =null;
		profileScore = candidate.getProfileScores().stream().filter(profile->profile.getProfileCategory().getCategoryName().equals(categoryName)).findFirst()
				.orElse(new CandidateProfileScore(candidate,profileCategoryMap.get(categoryName)));
		log.debug("Initial Profile Score is {}",profileScore);
		return profileScore;
	}
	
	public void updateProfileScore(Candidate candidate,String categoryName, Boolean remove) {
		//Comment If while running local tests
		if(profileCategoryWeightMap.isEmpty())
			init();
		Double totalScore = candidate.getProfileScore()!=null?candidate.getProfileScore():0D;
		CandidateProfileScore profileScore =null;
		if (Constants.CANDIDATE_BASIC_PROFILE.equalsIgnoreCase(categoryName)) {
			profileScore = setInitialProfileScore(candidate, categoryName);
			if(!remove) {
				profileScore.setScore(profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE).doubleValue());
				totalScore += profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE);
			} else {
				profileScore.setScore(0d);
				totalScore -= profileCategoryWeightMap.get(Constants.CANDIDATE_BASIC_PROFILE);
			}
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		} else {
			profileScore = setInitialProfileScore(candidate, categoryName);
			totalScore = setTotalScoreForProfile(remove, candidate, categoryName, profileScore, totalScore);
			setCandidateProfileOnCandidate(candidate, profileScore,totalScore);
		}
		log.info("Final is {}",candidate.getProfileScores());
	}
	
	private void setCandidateProfileOnCandidate(Candidate candidate, CandidateProfileScore profileScore,Double totalScore) {
		candidate.setProfileScore(totalScore);
		candidate.getProfileScores().add(profileScore);
		log.debug("Setting candidate sscore now {}",candidate.getProfileScore());
	}
	
	private Double setTotalScoreForProfile(Boolean remove, Candidate candidate, String categoryName,CandidateProfileScore profileScore, Double totalScore ) {
		log.debug("Setting score for category {}",categoryName);
		CandidateProfileScore score =  candidate.getProfileScores().stream().filter(category->category.getProfileCategory().getCategoryName().equals(categoryName)).findFirst().orElse(null);
		if(!remove) {
			if(score == null || (score != null &&  new Double(0).equals(score.getScore()))) {
				log.debug("Adding score {} for categrory {}",profileCategoryWeightMap.get(categoryName).doubleValue(), categoryName);
				profileScore.setScore(profileCategoryWeightMap.get(categoryName).doubleValue());
				totalScore += profileCategoryWeightMap.get(categoryName);
			}
		} else {
			if(score!=null && score.getScore() > 0 ) {
			profileScore.setScore(0d);
			totalScore -= profileCategoryWeightMap.get(categoryName);
			}
		}
		log.debug("total score is {}", totalScore);
		return totalScore;
	}
	
}
