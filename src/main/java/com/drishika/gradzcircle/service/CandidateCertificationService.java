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

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateCertificationDTO;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;


/**
 * @author abhinav
 *
 */
@Transactional
@Service
public class CandidateCertificationService {
	
	private final Logger log = LoggerFactory.getLogger(CandidateCertificationService.class);
	
	private final CandidateCertificationRepository candidateCertificationRepository;
	
	private static final String ENTITY_NAME = "candidateCertification";
	
	private final CandidateRepository candidateRepository;
	
	private final ProfileScoreCalculator profileScoreCalculator;
	
	private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;
	
	private DTOConverters dtoConverters;
	
	public CandidateCertificationService(CandidateCertificationRepository candidateCertificationRepository, CandidateRepository candidateRepository
			, DTOConverters dtoConverters, ProfileScoreCalculator profileScoreCalculator,CandidateCertificationSearchRepository candidateCertificationSearchRepository) {
		this.candidateCertificationRepository = candidateCertificationRepository;
		this.candidateRepository = candidateRepository;
		this.dtoConverters = dtoConverters;
		this.profileScoreCalculator = profileScoreCalculator;
		this.candidateCertificationSearchRepository = candidateCertificationSearchRepository;
	}
	
	public List<CandidateCertificationDTO> getCertificationByCandidate(Long id) {
		log.info("Getting Candidate Certifiction for candidate {}",id);
		List<CandidateCertificationDTO> certificationDTOs ;
		Candidate candidate = null;
		List<CandidateCertification> candidateCertifications = candidateCertificationRepository
				.findCertificationsByCandidateId(id);
		if(candidateCertifications.size() > 0) {
			candidate = candidateCertifications.get(0).getCandidate();
		}
		certificationDTOs = dtoConverters.convertCandidateCertifications(candidateCertifications, true,candidate);
		return certificationDTOs;
	}
	
	public CandidateCertification createCandidateCertification (CandidateCertification candidateCertification) {
		
		Optional<Candidate> opstionCandidate = candidateRepository.findById(candidateCertification.getCandidate().getId());
		if(!opstionCandidate.isPresent())
			throw new BadRequestAlertException("No Candidate present to link Certification", ENTITY_NAME,"");
		Candidate candidate = opstionCandidate.get();
		if(candidate.getCertifications().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_CERTIFICATION_PROFILE, false);
		}
		candidate = candidate.addCertification(candidateCertification);
		candidate = candidateRepository.save(candidate);
		log.debug("Candidate Certification post save are {}",candidate.getCertifications());
		return candidate.getCertifications().stream()
				.filter(cert->cert.getCertificationTitle().equals(candidateCertification.getCertificationTitle()))
					.findFirst().orElse(candidateCertification);
		
		
	}
	
	public CandidateCertification updateCandidateCertification (CandidateCertification candidateCertification) {
		return candidateCertificationRepository.save(candidateCertification);
	}
	
	public void deleteCertification (Long id) {
		Optional<CandidateCertification> candidateCertification = candidateCertificationRepository.findById(id);
		if(!candidateCertification.isPresent())
			return;
		Candidate candidate = candidateCertification.get().getCandidate();
		log.debug("REST request to delete CandidateCertification for candidate   : {} , {}", id,candidate.getId());
		log.debug("CANDIDATE CERTIFICATIONS ARE BEFORE REMOVE FROM LIST {}",candidate.getCertifications());
		candidate = candidate.removeCertification(candidateCertification.get());
		log.debug("Candidate post emoval of certs is {} {}",candidate,candidate.getCertifications());
		if(candidate.getCertifications().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_CERTIFICATION_PROFILE, true);
		candidate = candidateRepository.save(candidate);
		log.debug("After saving Candidate {}",candidate.getCertifications());
	}
	
	public List<CandidateCertification> getAllCandidateCertifications() {
		return candidateCertificationRepository.findAll();
	}
	
	public Optional<CandidateCertification> findById(Long id) {
		return candidateCertificationRepository.findById(id);
	}

	public List<CandidateCertification>searchCandidateCertifications(String query) {
		return StreamSupport
		.stream(candidateCertificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
		.collect(Collectors.toList());
	}
}
