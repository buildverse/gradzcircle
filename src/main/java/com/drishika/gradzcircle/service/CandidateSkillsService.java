/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
	private final Matcher<Long> matcher;

	public CandidateSkillsService(CandidateSkillsRepository candidateSkillsRepository,
			CandidateSkillsSearchRepository candidateSkillsSearchRepository, ProfileScoreCalculator profileScoreCalculator,
			SkillsRepository skillsRepository,CandidateRepository candidateRepository, DTOConverters converter,ElasticsearchTemplate elasticsearchTemplate,
			@Qualifier("CandidateSkillMatcher")Matcher<Long> matcher) {
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
		Candidate candidate = null;
		Optional<Candidate> candidateOptional = candidateRepository.findById(candidateSkillObject.getCandidate().getId());
		if(candidateOptional.isPresent())
			candidate = candidateOptional.get();
		else 
			return null;
		log.debug("Candidate SKills already saved are {}",candidate.getCandidateSkills());
		if(candidate.getCandidateSkills().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_SKILL_PROFILE, false);
		}
		injestAndUpdateCandidateSkillsInformation(candidateSkillObject.candidate(candidate),candidate);
		//injestSkillsInformation2(candidateSkillObject.candidate(candidate),candidate);
	
		candidate = candidateRepository.save(candidate);
		log.debug("The canddiate Skills post save in service is {}",candidate.getCandidateSkills());
		matcher.match(candidate.getId());
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
		Optional<CandidateSkills> candidateSkills = candidateSkillsRepository.findById(id);
		return converter.convertToCandidateSkillDTO(candidateSkills,false);
	}

	public void deleteCandidateSkills(Long id) {
		log.debug("Service request to delete CandidateSkills : {}", id);
		Optional<CandidateSkills> candidateSkill = candidateSkillsRepository.findById(id);
		if(!candidateSkill.isPresent())
			return;
		Candidate candidate = candidateSkill.get().getCandidate();
		candidate.getCandidateSkills().remove(candidateSkill.get());
		log.debug("Candidate Skills post remove is {}",candidate.getCandidateSkills());
		if(candidate.getCandidateSkills().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_SKILL_PROFILE, true);
		candidateRepository.save(candidate);
		matcher.match(candidate.getId());
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
	 
	 
	private void injestAndUpdateCandidateSkillsInformation(CandidateSkills candidateSkillObject,Candidate candidate) {
		if (candidateSkillObject.getCapturedSkills() != null
				&& !candidateSkillObject.getCapturedSkills().isEmpty()) {
			Set<String> uniqueCapturedSkills = createUniqueCapturedSkills(candidateSkillObject.getCapturedSkills());
			createListToAddToDBandElastic(uniqueCapturedSkills, candidate);
		
			
		}
		
		//Add already available in system
		if(candidateSkillObject.getSkillsList() != null)
			candidateSkillObject.getSkillsList().forEach(cSkill -> {
				 if(!cSkill.getSkill().equals(Constants.OTHER)) {
					 log.debug("Saving for non 'Other' Skill {}",cSkill.getSkill());
					 if(candidateSkillsRepository.findCandidateSkillPresent(candidateSkillObject.getCandidate().getId(), cSkill.getSkill())==null) {
						 CandidateSkills candidateSkill = new CandidateSkills();
						 Skills skill = skillsRepository
									.findBySkillIgnoreCase(cSkill.getSkill());
						 candidateSkill.skills(skill);
						 candidateSkill.candidate(candidate);
						 candidate.addCandidateSkill(candidateSkill);
						 
					 }
				
				 }
				 
			 });

	}
 
	 private Set<String> createUniqueCapturedSkills(String capturedSkills) {
		 Set<String> uniqueSkillSet = new HashSet<>();
		 String[] capturedSkillsSplit = capturedSkills.split(",");
		 for(int i = 0 ;i < capturedSkillsSplit.length; i ++) {
			 if(!capturedSkillsSplit[i].trim().isEmpty())
			 uniqueSkillSet.add(capturedSkillsSplit[i].trim());
		 }
		 log.debug("Unique skill set is {}",uniqueSkillSet);
		 return uniqueSkillSet;
	 }
	 
	 private void createListToAddToDBandElastic(Set<String> uniqueSkillSet, Candidate candidate) {
		
		
		
		 Iterator<String> uniqueSkillSetIterator = uniqueSkillSet.iterator();
		
		while (uniqueSkillSetIterator.hasNext()) {
			 String collapsedStringfromUser= null;
			 String collapsedStringFromSystem = null;
			 String lowercasedStringFromUser = null;
			 String lowerCasedStringFromSystem = null;
			String skillFromUser = uniqueSkillSetIterator.next();
			log.debug("Processing {},",skillFromUser);
			List<Skills> skills = skillsRepository.getAllMatchingSkills(skillFromUser.substring(0, 3) + "%");
			if(!skills.isEmpty()) {
				for (int i = 0; i < skills.size(); i++) {
	
					log.debug("Skills from DB is {}", skills.get(i));
					Skills skill = skills.get(i);
					String[] splitableFromSystem = skill.getSkill().split("\\s+");	 
					 if(splitableFromSystem.length>1) {
						 collapsedStringFromSystem = createCollapsedStringLowerCase(splitableFromSystem);
					 } else {
						 lowerCasedStringFromSystem = skill.getSkill().toLowerCase();
					 }
					String[] splitableFromUser = skillFromUser.split("\\s+");
					if (splitableFromUser.length > 1) {
						collapsedStringfromUser = createCollapsedStringLowerCase(splitableFromUser);
						log.debug("Collapsed String is {}", collapsedStringfromUser);
					} else {
						lowercasedStringFromUser = skillFromUser.toLowerCase();
					}
					 Boolean collapsedStringCase = collapsedStringFromSystem != null && collapsedStringfromUser!=null && !collapsedStringFromSystem.equals(collapsedStringfromUser);
					 Boolean singleStringCase = lowercasedStringFromUser != null && lowerCasedStringFromSystem!=null && !lowercasedStringFromUser.equals(lowerCasedStringFromSystem);
					 //log.debug("{},{},{},{}",lowerCasedStringFromSystem,collapsedStringfromUser,collapsedStringFromSystem,lowercasedStringFromUser);
					 Boolean crossMatchCase = !StringUtils.equals(lowerCasedStringFromSystem, collapsedStringfromUser) && StringUtils.equals(collapsedStringFromSystem, lowercasedStringFromUser);
					 log.debug("collapsedStringCase is {}, singleStringCase is {}, crossMatchCase {}",collapsedStringCase,singleStringCase,crossMatchCase);
					if ((collapsedStringCase || singleStringCase ||crossMatchCase)) {
						Skills newSkill = new Skills();
						newSkill.setSkill(converter.convertToCamelCase(skillFromUser));
						updateSkillIndex(skillsRepository.save(newSkill));
						updateCandidateSkillFromCapturedList(candidate, newSkill);
					} else
						updateCandidateSkillFromCapturedList(candidate, skill);
	
				}
			} else {
				Skills newSkill = new Skills();
				newSkill.setSkill(converter.convertToCamelCase(skillFromUser));
				updateSkillIndex(skillsRepository.save(newSkill));
				updateCandidateSkillFromCapturedList(candidate, newSkill);
			}
			

		}
		 
	 }
	 
	private void updateCandidateSkillFromCapturedList(Candidate candidate, Skills skill) {
		if (candidate.getCandidateSkills() != null && !candidate.getCandidateSkills().stream().anyMatch(
				cS -> cS.getSkills() != null ? cS.getSkills().getSkill().equalsIgnoreCase(skill.getSkill()) : false)) {
			
			CandidateSkills cSkills = new CandidateSkills();
			cSkills.setSkills(skill);
			cSkills.candidate(candidate);
			candidate.addCandidateSkill(cSkills);

		}
	}
	 
	 private String createCollapsedStringLowerCase(String[] splitable) {
		 StringBuilder sb = new StringBuilder();
		 for(int i =0; i < splitable.length; i++) {
			 sb.append(splitable[i]);
		 }
		 return sb.toString().toLowerCase();
	 }
	 
	 
	private void injestSkillsInformation2(CandidateSkills candidateSkillObject, Candidate candidate) {
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
					if(uniqueSkillSet.contains(capturedSkills[i].replaceAll("\\s+", "").toLowerCase())) {
						log.debug("Chekcing on skill {}",capturedSkills[i].replaceAll("\\s+", "").toLowerCase());
						continue;
					}
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
						String convertedSkill = converter.convertToCamelCase(skills.get(i).trim());
						if(!convertedSkill.isEmpty()) {
							newSkill.setSkill(convertedSkill);
							skillToAdd.add(newSkill);
							cSkill.skills(newSkill);
							candidateSkills.add(cSkill);
						}
					} else {
						if(!candidateSkills.stream().anyMatch(cS-> cS.getSkills().getSkill().equalsIgnoreCase(skill.getSkill()))) {
							cSkill.skills(skill);
							candidateSkills.add(cSkill);
						}
						
					}
					
					cSkill.candidate(candidateSkillObject.getCandidate());
				}
				if(skillToAdd.size()>0) {
					List<Skills> addedSkills = skillsRepository.saveAll(skillToAdd);
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
