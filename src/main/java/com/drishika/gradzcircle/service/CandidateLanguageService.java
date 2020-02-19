package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateLanguageProficiencyDTO;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

@Service
@Transactional
public class CandidateLanguageService {

	private final Logger log = LoggerFactory.getLogger(CandidateLanguageService.class);

	private final Matcher<Long> matcher;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private final CandidateRepository candidateRepository;
	
	private final LanguageRepository languageRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;
	
    private final ProfileScoreCalculator profileScoreCalculator;
    
    private final DTOConverters converter;
	

	public CandidateLanguageService(CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository, LanguageRepository languageRepository,
			@Qualifier("CandidateLanguageMatcher") Matcher<Long> matcher,
			CandidateRepository candidateRepository,ProfileScoreCalculator profileScoreCalculator,DTOConverters converter) {
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		this.matcher = matcher;
		this.candidateRepository = candidateRepository;
		this.languageRepository = languageRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.converter = converter;
	}

	public CandidateLanguageProficiency createCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		injestLanguageInformation(candidateLanguageProficiency);
		Candidate candidate = null;
		Optional<Candidate> optional = candidateRepository.findById(candidateLanguageProficiency.getCandidate().getId());
		if(optional.isPresent())
			candidate = optional.get();
		else 
			return null;
		if(candidate.getCandidateLanguageProficiencies().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_LANGUAGE_PROFILE, false);
		}
		candidate.addCandidateLanguageProficiency(candidateLanguageProficiency);
		candidate = candidateRepository.save(candidate);
		//CandidateLanguageProficiency result = candidateLanguageProficiencyRepository
			//	.save(candidateLanguageProficiency);
		//Candidate candidate = candidateRepository.findOne(result.getCandidate().getId());
		log.debug("I have these pre in candidate {}", candidate.getCandidateLanguageProficiencies());
	//	candidate.addCandidateLanguageProficiency(result);
		matcher.match(candidate.getId());
		return candidate.getCandidateLanguageProficiencies().stream().
	 	 filter(langProf->langProf.getLanguage().getLanguage()
	 			.equals(candidateLanguageProficiency.getLanguage().getLanguage())).findFirst().orElse(candidateLanguageProficiency);
		 
	}

	public CandidateLanguageProficiency updateCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		injestLanguageInformation(candidateLanguageProficiency);
		CandidateLanguageProficiency fromRepo = null;
		Optional<CandidateLanguageProficiency> optionalFromRepo = candidateLanguageProficiencyRepository.findById(candidateLanguageProficiency.getId());
		if(optionalFromRepo.isPresent())
			fromRepo = optionalFromRepo.get();
		else 
			return null;
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency.candidate(fromRepo.getCandidate()));
		return result;

	}

	public List<CandidateLanguageProficiency> getAllCandidateLanguageProficiencies() {
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
				.findAll();
		return candidateLanguageProficiencies;
	}

	public CandidateLanguageProficiencyDTO getCandidateLanguageProficiency(Long id) {
		log.debug("REST request to get CandidateLanguageProficiency : {}", id);
		Optional<CandidateLanguageProficiency> candidateLanguageProficiency = candidateLanguageProficiencyRepository.findById(id);
		if(candidateLanguageProficiency.isPresent())
			return converter.convertCandidateLanguageProficiency(candidateLanguageProficiency.get());
		else 
			return null;
	}
	
	/*private CandidateLanguageProficiencyDTO convertCandidateLanguageProficienciesToDTO(CandidateLanguageProficiency candidateLanguageProficiency) {
		if(candidateLanguageProficiency == null)
			return null;
		CandidateLanguageProficiencyDTO candidateLanguageProficiencyDTO = new CandidateLanguageProficiencyDTO();
		LanguageDTO languageDTO = new LanguageDTO();
		CandidateDTO candidateDTO = new CandidateDTO();
		candidateLanguageProficiencyDTO.setId(candidateLanguageProficiency.getId());
		languageDTO.setLanguage(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setId(candidateLanguageProficiency.getLanguage().getId());
		languageDTO.setValue(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setDisplay(candidateLanguageProficiency.getLanguage().getLanguage());
		candidateLanguageProficiencyDTO.setLanguage(languageDTO);
		candidateLanguageProficiencyDTO.setProficiency(candidateLanguageProficiency.getProficiency());
		candidateDTO.setProfileScore(candidateLanguageProficiency.getCandidate().getProfileScore()!=null?candidateLanguageProficiency.getCandidate().getProfileScore():0d);
		candidateLanguageProficiencyDTO.setCandidate(candidateDTO);
		return candidateLanguageProficiencyDTO;
	}
*/
	public List<CandidateLanguageProficiencyDTO> getCandidateLanguageProficiencyByCandidate(Long id) {
		log.debug("REST request to get CandidateLanguageProficiency : {}", id);
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
				.findCandidateLanguageProficienciesByCandidateId(id);
		Candidate candidate = null;
		Optional<Candidate> candidateOptional = candidateRepository.findById(id);
		if(candidateOptional.isPresent())
			candidate = candidateOptional.get();
		return converter.convertCandidateLanguageProficiencies(candidateLanguageProficiencies, true, candidate);
		
	}

	public Stream<CandidateLanguageProficiency> getAllLanguageProfienciesForActiveCandidates() {
		return candidateLanguageProficiencyRepository.findCandidateLanguageProficienciesForActiveCandidates();
	}

	public void deleteCandidateLanguageProficiency(Long id) {
		log.debug("REST request to delete CandidateLanguageProficiency : {}", id);
		Optional<CandidateLanguageProficiency> candidateLanguageProficiency = candidateLanguageProficiencyRepository.findById(id);
		Candidate candidate = null;
		if(candidateLanguageProficiency.isPresent())
			candidate = candidateLanguageProficiency.get().getCandidate();
		else 
			return;
		candidate.removeCandidateLanguageProficiency(candidateLanguageProficiency.get());
		if(candidate.getCandidateLanguageProficiencies().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_LANGUAGE_PROFILE, true);
		matcher.match(candidate.getId());
	}

	public List<CandidateLanguageProficiency> searchCandidateLanguageProficiencies(String query) {
		log.debug("REST request to search CandidateLanguageProficiencies for query {}", query);
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = StreamSupport
				.stream(candidateLanguageProficiencySearchRepository.search(queryStringQuery(query)).spliterator(),
						false)
				.collect(Collectors.toList());
		return candidateLanguageProficiencies;
	}
	
	private void injestLanguageInformation(CandidateLanguageProficiency candidateLanguageProficiency) {
		if(candidateLanguageProficiency.getLanguage() != null) {
			Language languageFromRepo = languageRepository.findByLanguage(candidateLanguageProficiency.getLanguage().getLanguage());
			candidateLanguageProficiency.setLanguage(languageFromRepo);
		}
	}

}
