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
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.service.matching.Matcher;



@Service
@Transactional
public class CandidateLanguageService  {

	private final Logger log = LoggerFactory.getLogger(CandidateLanguageService.class);
	
	private final Matcher<Candidate> matcher;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;
	
	private final CandidateRepository candidateRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	public CandidateLanguageService(CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository, @Qualifier("CandidateLanguageMatcher")Matcher<Candidate> matcher,
			CandidateRepository candidateRepository) {
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		this.matcher = matcher;
		this.candidateRepository = candidateRepository;
	}

	public CandidateLanguageProficiency createCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.saveAndFlush(candidateLanguageProficiency);
		Candidate candidate = candidateRepository.findOne(result.getCandidate().getId());
		log.debug("I have these pre in candidate {}",candidate.getCandidateLanguageProficiencies());
		candidate.addCandidateLanguageProficiency(result);
		matcher.match(candidate);
		return result;
	}

	public CandidateLanguageProficiency updateCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency);
		//Candidate candidate = candidateRepository.findOne(result.getCandidate().getId());
	//	//candidate.addCandidateLanguageProficiency(result);
		//matcher.match(candidate);
		//log.debug("The result post update language proficicency{}", result);
		return result;

	}

	public List<CandidateLanguageProficiency> getAllCandidateLanguageProficiencies() {
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
				.findAll();
		return candidateLanguageProficiencies;
	}

	public CandidateLanguageProficiency getCandidateLanguageProficiency(Long id) {
		log.debug("REST request to get CandidateLanguageProficiency : {}", id);
		CandidateLanguageProficiency candidateLanguageProficiency = candidateLanguageProficiencyRepository.findOne(id);
		return candidateLanguageProficiency;
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
		CandidateLanguageProficiency candidateLanguageProficiency =  candidateLanguageProficiencyRepository.findOne(id);
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

	
}
