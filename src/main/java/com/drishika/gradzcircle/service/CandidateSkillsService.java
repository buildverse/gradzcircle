/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

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

	public CandidateSkillsService(CandidateSkillsRepository candidateSkillsRepository,
			CandidateSkillsSearchRepository candidateSkillsSearchRepository, ProfileScoreCalculator profileScoreCalculator,
			SkillsRepository skillsRepository,CandidateRepository candidateRepository, DTOConverters converter,ElasticsearchTemplate elasticsearchTemplate) {
		this.candidateSkillsRepository = candidateSkillsRepository;
		this.candidateSkillsSearchRepository = candidateSkillsSearchRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.candidateRepository = candidateRepository;
		this.skillsRepository = skillsRepository;
		this.converter = converter;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public Set<CandidateSkills> createCandidateSkills(CandidateSkills candidateSkillObject) {
		log.debug("Service request to save CandidateSkills : {}", candidateSkillObject.getCapturedSkills());
		
		Candidate candidate = candidateRepository.findOne(candidateSkillObject.getCandidate().getId());
		
		if(candidate.getCandidateSkills().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_SKILL_PROFILE, false);
		}
		injestSkillsInformation(candidateSkillObject.candidate(candidate));
		candidate = candidateRepository.save(candidate);
		log.debug("The canddiate Skills post save in service is {}",candidate.getCandidateSkills());
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
		/*candidateSkillsRepository.delete(id);
		candidateSkillsSearchRepository.delete(id);*/
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
	
	private void injestSkillsInformation(CandidateSkills candidateSkillObject) {
		List<CandidateSkills> candidateSkills=  new ArrayList<>();
		Set<CandidateSkills> previousSkills = candidateSkillObject.getCandidate().getCandidateSkills();
		if(previousSkills!=null && !previousSkills.isEmpty())
			candidateSkills.addAll(previousSkills);
		if(candidateSkillObject.getSkillsList() != null) {
			log.debug("candidate Skill Object is {}",candidateSkillObject);
			Skills otherSkill = candidateSkillObject.getSkillsList().stream()
									.filter(candidateSkill->candidateSkill.getSkill()
											.equals(Constants.OTHER)).findAny().orElse(null);
			log.debug("Other Skill is {}",otherSkill);
			if(otherSkill!=null) {
				String[] skills = candidateSkillObject.getCapturedSkills().split(",");
				List<Skills> skillToAdd = new ArrayList<>();
				for(int i = 0; i< skills.length; i++) {
					CandidateSkills cSkill = new CandidateSkills();
					Skills skill = skillsRepository
							.findBySkill(skills[i]);
					if(skill == null) {
						Skills newSkill = new Skills();
						newSkill.setSkill(converter.convertToCamelCase(skills[i].trim()));
						skillToAdd.add(newSkill);
						cSkill.skills(newSkill);
						candidateSkills.add(cSkill);
					} else {
						cSkill.skills(skill);
						candidateSkills.add(cSkill);
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
								.findBySkill(cSkill.getSkill());
					 candidateSkill.skills(skill);
					 candidateSkill.candidate(candidateSkillObject.getCandidate());
					 candidateSkills.add(candidateSkill);
				 }
			
			 }
			 
		 });
		 candidateSkillObject.getCandidate().getCandidateSkills().addAll(candidateSkills);
		 }	
		
		log.debug("CandidateSkills post injestion is {} and candidate set has {}",candidateSkills,
				candidateSkillObject.getCandidate().getCandidateSkills());
	}
	
	
	private void updateSkillIndex(Skills skill) {
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
