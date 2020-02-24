/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateEmploymentDTO;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;

/**
 * @author abhinav
 *
 */
@Transactional
@Service
public class CandidateEmploymentService {
	
	private final Logger log = LoggerFactory.getLogger(CandidateEmploymentService.class); 
	
	private final CandidateProjectRepository candidateProjectRepository;
	
	private static final String ENTITY_NAME = "candidateEmployment";
	
	private final ProfileScoreCalculator profileScoreCalculator;
	
	private final CandidateRepository candidateRepository;
	
	private final CandidateEmploymentRepository candidateEmploymentRepository;

	private final CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;
	
	
	private final DTOConverters converter;

	
	public CandidateEmploymentService(CandidateProjectRepository candidateProjectRepository,ProfileScoreCalculator profileScoreCalculator,
			CandidateRepository candidateRepository,DTOConverters converter,CandidateEmploymentRepository candidateEmploymentRepository,
			CandidateEmploymentSearchRepository candidateEmploymentSearchRepository) {
		this.candidateProjectRepository = candidateProjectRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.candidateRepository = candidateRepository;
		this.converter = converter;
		this.candidateEmploymentRepository = candidateEmploymentRepository;
		this.candidateEmploymentSearchRepository = candidateEmploymentSearchRepository;
	}
	
	public CandidateEmployment createCandidateEmployment(CandidateEmployment candidateEmployment) {
		Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateEmployment.getCandidate().getId());
		if(!optionalCandidate.isPresent())
			throw new BadRequestAlertException("A No candidate available to link employment", ENTITY_NAME, "");
		Candidate candidate = optionalCandidate.get();
		if(candidate.getEmployments().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_EXPERIENCE_PROFILE, false);
		}
		candidate = candidateRepository.save(candidate.addEmployment(candidateEmployment));
		log.debug("Candidate Employment is {}",candidate.getEmployments());
		return candidate.getEmployments().stream()
				.filter(emp->emp.getId()!=null).filter(emp->emp.getJobTitle().equals(candidateEmployment.getJobTitle()))
					.findFirst().orElse(candidateEmployment);
	}
	
	public CandidateEmployment updateCandidateEmployment(CandidateEmployment candidateEmployment) {
		return candidateEmploymentRepository.save(candidateEmployment);
	}
	
	public List<CandidateEmployment> findAll() {
		return candidateEmploymentRepository.findAll();
	}
	
	public List<CandidateEmploymentDTO> getEmploymentsForCandidate(Long id) {
		List<CandidateEmploymentDTO> candidateEmploymentDTOs;
		Optional<Candidate> candidate = candidateRepository.findById(id);
		List<CandidateEmployment> candidateEmployments = candidateEmploymentRepository.findByCandidateId(id);
		candidateEmploymentDTOs =  converter.convertCandidateEmployments(candidateEmployments, true,candidate.get());
		return candidateEmploymentDTOs;
	}
	
	public Optional<CandidateEmployment> getCandidateEmployment(Long id) {
		return candidateEmploymentRepository.findById(id);
	}
	
	public void deleteCandidateEmployment (Long id) {
		Optional<CandidateEmployment> candidateEmployment = candidateEmploymentRepository.findById(id);
		if(!candidateEmployment.isPresent())
			return;
		Candidate candidate = candidateEmployment.get().getCandidate();
		log.debug("REST request to delete CandidateEmployment for candidate   : {} , {}", id,candidate.getId());
		candidate.removeEmployment(candidateEmployment.get());
		
		if(candidate.getEmployments().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_EXPERIENCE_PROFILE, true);
		candidateRepository.save(candidate);
	}
	
	public List<CandidateEmployment> searchCandidateEmployments(String query) {
		return StreamSupport
				.stream(candidateEmploymentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}
}
