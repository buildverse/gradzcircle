/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.entitybuilders.SkillsEntityBuilder;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CandidateSkillsRepository;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.search.CandidateSkillsSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateSkillsDTO;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

import io.jsonwebtoken.lang.Arrays;

/**
 * @author abhinav
 *
 */
@Service
@Transactional
public class CandidateSkillsService {

	private final Logger log = LoggerFactory.getLogger(CandidateSkillsService.class);

	private CandidateSkillsRepository candidateSkillsRepository;
	private CandidateSkillsSearchRepository candidateSkillsSearchRepository;
	private ProfileScoreCalculator profileScoreCalculator;
	private SkillsRepository skillsRepository;
	private CandidateRepository candidateRepository;
	private DTOConverters converter ;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final Matcher<Candidate> matcher;

	public CandidateSkillsService(CandidateSkillsRepository candidateSkillsRepository,
			CandidateSkillsSearchRepository candidateSkillsSearchRepository, ProfileScoreCalculator profileScoreCalculator,
			SkillsRepository skillsRepository,CandidateRepository candidateRepository, DTOConverters converter,ElasticsearchTemplate elasticsearchTemplate,
			@Qualifier("CandidateSkillMatcher")Matcher<Candidate> matcher) {
		this.candidateSkillsRepository = candidateSkillsRepository;
		this.candidateSkillsSearchRepository = candidateSkillsSearchRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.candidateRepository = candidateRepository;
		this.skillsRepository = skillsRepository;
		this.converter = converter;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.matcher = matcher;
	}

	public Set<CandidateSkills> createCandidateSkills(CandidateSkills candidateSkillObject) {
		log.debug("Service request to save CandidateSkills : {}", candidateSkillObject);
		
		Candidate candidate = candidateRepository.findOne(candidateSkillObject.getCandidate().getId());
		log.debug("Candidate SKills already saved are {}",candidate.getCandidateSkills());
		if(candidate.getCandidateSkills().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_SKILL_PROFILE, false);
		}
		injestSkillsInformation(candidateSkillObject.candidate(candidate),candidate);
		candidate = candidateRepository.save(candidate);
		log.debug("The canddiate Skills post save in service is {}",candidate.getCandidateSkills());
		matcher.match(candidate);
		return candidate.getCandidateSkills();

	}

	public CandidateSkills updateCandidateSkills(CandidateSkills candidateSkills) {
		log.debug("Service request to save CandidateSkills : {}", candidateSkills);
		CandidateSkills result = candidateSkillsRepository.save(candidateSkills);
		// candidateSkillsSearchRepository.save(result);
		return result;
	}

	public List<CandidateSkills> getAllCandidateSkills() {
		log.debug("Service request to get all CandidateSkills");
		return candidateSkillsRepository.findAll();
	}

	public CandidateSkillsDTO getCandidateSkill(Long id) {
		log.debug("Service request to get CandidateSkills : {}", id);
		CandidateSkills candidateSkills = candidateSkillsRepository.findOne(id);
		return converter.convertToCandidateSkillDTO(candidateSkills,false);
	}

	public void deleteCandidateSkills(Long id) {
		log.debug("Service request to delete CandidateSkills : {}", id);
		CandidateSkills candidateSkill = candidateSkillsRepository.findOne(id);
		Candidate candidate = candidateSkill.getCandidate();
		candidate.getCandidateSkills().remove(candidateSkill);
		log.debug("Candidate Skills post remove is {}",candidate.getCandidateSkills());
		if(candidate.getCandidateSkills().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_SKILL_PROFILE, true);
		candidateRepository.save(candidate);
		matcher.match(candidate);
	}

	public List<CandidateSkills> searchCandidateSkills(String query) {
		log.debug("Service request to search CandidateSkills for query {}", query);
		return StreamSupport
				.stream(candidateSkillsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}
	
	 public List<CandidateSkillsDTO> getSkillsForCandidate(Long id) {
 		List<CandidateSkills> skills = candidateSkillsRepository.findSkillsForCandidate(id);
 		return converter.convertToCandidateSkillsDTO(skills,false);
	 }
	
	 private List<String> convertToCamelCaseAndEliminateDuplicates(String [] skills) {
		 List<String> skillList = new ArrayList<>();
		 for( int i =0 ; i < skills.length; i ++) {
			 skillList.add(skills[i].trim());
		 }
		 
		 return skillList.stream().map(skill->converter.convertToCamelCase(skill)).distinct().collect(Collectors.toList());
	 }
	 
	private void injestSkillsInformation(CandidateSkills candidateSkillObject, Candidate candidate) {
		List<CandidateSkills> candidateSkills=  new ArrayList<>();
		Set<CandidateSkills> previousSkills = candidate.getCandidateSkills();
		Set<String> uniqueSkillSet = new HashSet<>();
		if(previousSkills!=null && !previousSkills.isEmpty())
			candidateSkills.addAll(previousSkills);
		if(candidateSkillObject.getSkillsList() != null) {
			log.debug("candidate Skill Object is {}",candidateSkillObject);
			Skills otherSkill = candidateSkillObject.getSkillsList().stream()
									.filter(candidateSkill->candidateSkill.getSkill()
											.equals(Constants.OTHER)).findAny().orElse(null);
			log.debug("Other Skill is {}",otherSkill);
			if(otherSkill!=null) {
				String[] capturedSkills = candidateSkillObject.getCapturedSkills().split(",");
				List<String> skills = new ArrayList<>();
				for(int i=0; i < capturedSkills.length; i++) {
					if(uniqueSkillSet.contains(capturedSkills[i].replaceAll("\\s+", "").toLowerCase()))
						continue;
					else {
						uniqueSkillSet.add(capturedSkills[i].replaceAll("\\s+", "").toLowerCase());
						skills.add(capturedSkills[i]);
					}
				}
				log.debug("My final list is {}",skills);
			   // List<String> filteredAndConvertedSkillList = convertToCamelCaseAndEliminateDuplicates(skills);
				List<Skills> skillToAdd = new ArrayList<>();
				for(int i = 0; i< skills.size(); i++) {
					CandidateSkills cSkill = new CandidateSkills();
					Skills skill = skillsRepository
							.findBySkillIgnoreCase(skills.get(i));
					log.debug("DO i have {} in repo ",skill);
					if(skill == null) {
						Skills newSkill = new Skills();
						newSkill.setSkill(converter.convertToCamelCase(skills.get(i).trim()));
						skillToAdd.add(newSkill);
						cSkill.skills(newSkill);
						candidateSkills.add(cSkill);
					} else {
						if(!candidateSkills.stream().anyMatch(cS-> cS.getSkills().getSkill().equalsIgnoreCase(skill.getSkill()))) {
							cSkill.skills(skill);
							candidateSkills.add(cSkill);
						}
						
					}
					
					cSkill.candidate(candidateSkillObject.getCandidate());
				}
				if(skillToAdd.size()>0) {
					List<Skills> addedSkills = skillsRepository.save(skillToAdd);
					addedSkills.forEach(skill ->{
						updateSkillIndex(skill);
					});				
				}
				
			}
		 candidateSkillObject.getSkillsList().forEach(cSkill -> {
			 if(!cSkill.getSkill().equals(Constants.OTHER)) {
				 log.debug("Saving for non 'Other' Skill {}",cSkill.getSkill());
				 if(candidateSkillsRepository.findCandidateSkillPresent(candidateSkillObject.getCandidate().getId(), cSkill.getSkill())==null) {
					 CandidateSkills candidateSkill = new CandidateSkills();
					 Skills skill = skillsRepository
								.findBySkillIgnoreCase(cSkill.getSkill());
					 candidateSkill.skills(skill);
					 candidateSkill.candidate(candidateSkillObject.getCandidate());
					 candidateSkills.add(candidateSkill);
				 }
			
			 }
			 
		 });
		 log.debug("Candidate SKill lits has {}",candidateSkills);
		 candidateSkillObject.getCandidate().getCandidateSkills().addAll(candidateSkills);
		 }	
		
		log.debug("CandidateSkills post injestion is {} and candidate set has {}",candidateSkills,
				candidateSkillObject.getCandidate().getCandidateSkills());
	}
	
	
	private void updateSkillIndex(Skills skill) {
		log.debug("Indexing Skill -> {}",skill);
		com.drishika.gradzcircle.domain.elastic.Skills skillElasticInstance = new com.drishika.gradzcircle.domain.elastic.Skills();
		try {
			BeanUtils.copyProperties(skillElasticInstance, skill);
		} catch (IllegalAccessException e) {
			log.error("Error copying bean for skill elastic instance", e);
			// @TODO SEND EMAIL ALERT
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Error copying bean for skill elastic instance", e);
			// throw new URISyntaxException(e.getMessage(),e.getLocalizedMessage());

		}
		elasticsearchTemplate.index(new SkillsEntityBuilder(skillElasticInstance.getId())
				.name(skillElasticInstance.getSkill())
				.suggest(new String[] { skillElasticInstance.getSkill() }).buildIndex());
		elasticsearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Skills.class);
	}
}
