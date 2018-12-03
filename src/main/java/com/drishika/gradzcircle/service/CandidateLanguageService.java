package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.LanguageRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateLanguageProficiencyDTO;
import com.drishika.gradzcircle.service.dto.LanguageDTO;
import com.drishika.gradzcircle.service.matching.Matcher;

@Service
@Transactional
public class CandidateLanguageService {

	private final Logger log = LoggerFactory.getLogger(CandidateLanguageService.class);

	private final Matcher<Candidate> matcher;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private final CandidateRepository candidateRepository;
	
	private final LanguageRepository languageRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	public CandidateLanguageService(CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository, LanguageRepository languageRepository,
			@Qualifier("CandidateLanguageMatcher") Matcher<Candidate> matcher,
			CandidateRepository candidateRepository) {
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		this.matcher = matcher;
		this.candidateRepository = candidateRepository;
		this.languageRepository = languageRepository;
	}

	public CandidateLanguageProficiency createCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		injestLanguageInformation(candidateLanguageProficiency);
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository
				.save(candidateLanguageProficiency);
		Candidate candidate = candidateRepository.findOne(result.getCandidate().getId());
		log.debug("I have these pre in candidate {}", candidate.getCandidateLanguageProficiencies());
		candidate.addCandidateLanguageProficiency(result);
		matcher.match(candidate);
		return result;
	}

	public CandidateLanguageProficiency updateCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		injestLanguageInformation(candidateLanguageProficiency);
		CandidateLanguageProficiency fromRepo = candidateLanguageProficiencyRepository.findOne(candidateLanguageProficiency.getId());
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency.candidate(fromRepo.getCandidate()));
		// Candidate candidate =
		// candidateRepository.findOne(result.getCandidate().getId());
		// //candidate.addCandidateLanguageProficiency(result);
		// matcher.match(candidate);
		// log.debug("The result post update language proficicency{}", result);
		return result;

	}

	public List<CandidateLanguageProficiency> getAllCandidateLanguageProficiencies() {
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
				.findAll();
		return candidateLanguageProficiencies;
	}

	public CandidateLanguageProficiencyDTO getCandidateLanguageProficiency(Long id) {
		log.debug("REST request to get CandidateLanguageProficiency : {}", id);
		CandidateLanguageProficiency candidateLanguageProficiency = candidateLanguageProficiencyRepository.findOne(id);
		return convertCandidateLanguageProficienciesToDTO(candidateLanguageProficiency);
	}
	
	private CandidateLanguageProficiencyDTO convertCandidateLanguageProficienciesToDTO(CandidateLanguageProficiency candidateLanguageProficiency) {
		
		CandidateLanguageProficiencyDTO candidateLanguageProficiencyDTO = new CandidateLanguageProficiencyDTO();
		LanguageDTO languageDTO = new LanguageDTO();
		candidateLanguageProficiencyDTO.setId(candidateLanguageProficiency.getId());
		languageDTO.setLanguage(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setId(candidateLanguageProficiency.getLanguage().getId());
		languageDTO.setValue(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setDisplay(candidateLanguageProficiency.getLanguage().getLanguage());
		candidateLanguageProficiencyDTO.setLanguage(languageDTO);
		candidateLanguageProficiencyDTO.setProficiency(candidateLanguageProficiency.getProficiency());
		return candidateLanguageProficiencyDTO;
	}

	public List<CandidateLanguageProficiency> getCandidateLanguageProficiencyByCandidate(Long id) {
		log.debug("REST request to get CandidateLanguageProficiency : {}", id);
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
				.findCandidateLanguageProficienciesByCandidateId(id);
		return candidateLanguageProficiencies;
	}

	public Stream<CandidateLanguageProficiency> getAllLanguageProfienciesForActiveCandidates() {
		return candidateLanguageProficiencyRepository.findCandidateLanguageProficienciesForActiveCandidates();
	}

	public void deleteCandidateLanguageProficiency(Long id) {
		log.debug("REST request to delete CandidateLanguageProficiency : {}", id);
		CandidateLanguageProficiency candidateLanguageProficiency = candidateLanguageProficiencyRepository.findOne(id);
		Candidate candidate = candidateLanguageProficiency.getCandidate();
		candidate.removeCandidateLanguageProficiency(candidateLanguageProficiency);
		matcher.match(candidate);
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
