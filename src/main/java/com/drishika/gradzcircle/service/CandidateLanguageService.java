package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.service.matching.CandidateMatcher;
import com.drishika.gradzcircle.service.matching.Matcher;



@Service
@Transactional
public class CandidateLanguageService  {

	private final Logger log = LoggerFactory.getLogger(CandidateLanguageService.class);
	
	//private final Matcher<Candidate> jobMatcher;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	public CandidateLanguageService(CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository) {
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		//this.jobMatcher = jobMatcher;
	}

	public CandidateLanguageProficiency createCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency);
		candidateLanguageProficiencySearchRepository.save(result);
		
		return result;
	}

	public CandidateLanguageProficiency updateCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {
		CandidateLanguageProficiency result = candidateLanguageProficiencyRepository.save(candidateLanguageProficiency);
		log.debug("The result post update language proficicency{}", result);
		candidateLanguageProficiencySearchRepository.save(result);
		
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
		candidateLanguageProficiencies
				.forEach(candidateLanguageProficiency -> candidateLanguageProficiency.setCandidate(null));
		return candidateLanguageProficiencies;
	}

	public void deleteCandidateLanguageProficiency(Long id) {
		log.debug("REST request to delete CandidateLanguageProficiency : {}", id);
		candidateLanguageProficiencyRepository.delete(id);
		candidateLanguageProficiencySearchRepository.delete(id);
		

	}

	public List<CandidateLanguageProficiency> searchCandidateLanguageProficiencies(String query) {
		log.debug("REST request to search CandidateLanguageProficiencies for query {}", query);
		List<CandidateLanguageProficiency> candidateLanguageProficiencies = StreamSupport
				.stream(candidateLanguageProficiencySearchRepository.search(queryStringQuery(query)).spliterator(),
						false)
				.collect(Collectors.toList());
		candidateLanguageProficiencies
				.forEach(candidateLanguageProficiency -> candidateLanguageProficiency.setCandidate(null));
		return candidateLanguageProficiencies;
	}

	
}
