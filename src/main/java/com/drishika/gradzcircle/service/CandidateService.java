package com.drishika.gradzcircle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;

/**
 * Service to manage Candidate
 */

@Service
@Transactional

public class CandidateService {
    
    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    private CandidateRepository candidateRepository;

    private CandidateSearchRepository candidateSearchRepository;


    public CandidateService (CandidateRepository candidateRepository, CandidateSearchRepository candidateSearchRepository){
        this.candidateRepository = candidateRepository;
        this.candidateSearchRepository = candidateSearchRepository;
    }

    public void createCandidate (User user){
        Candidate candidate = new Candidate ();
        candidate.setLogin(user);
        candidateRepository.save(candidate);
        candidateSearchRepository.save(candidate);
        logger.debug("Created Information for candidate: {}", candidate);
    }
}