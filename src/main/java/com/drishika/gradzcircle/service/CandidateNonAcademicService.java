/**
 * 
 */
package com.drishika.gradzcircle.service;

import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.repository.CandidateNonAcademicWorkRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateNonAcademicWorkDTO;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;

/**
 * @author abhinav
 *
 */
@Transactional
@Service
public class CandidateNonAcademicService {
	
	private final Logger log = LoggerFactory.getLogger(CandidateNonAcademicService.class);
	
	private final CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository;
	private final CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;
	private final CandidateRepository candidateRepository;
	private final ProfileScoreCalculator profileScoreCalculator;
	private DTOConverters dtoConverters;
	private static final String ENTITY_NAME = "candidateNonAcademicWork";
	
	public CandidateNonAcademicService(CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository,
			 DTOConverters dtoConverters, CandidateRepository candidateRepository, ProfileScoreCalculator profileScoreCalculator,
			CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository) {
		this.candidateNonAcademicWorkRepository = candidateNonAcademicWorkRepository;
		this.dtoConverters = dtoConverters;
		this.candidateRepository = candidateRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.candidateNonAcademicWorkSearchRepository = candidateNonAcademicWorkSearchRepository;
	}

	
	public CandidateNonAcademicWork createCandidateNonAcademicWork(CandidateNonAcademicWork candidateNonAcademicWork ) {
		Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateNonAcademicWork.getCandidate().getId());
		if(!optionalCandidate.isPresent())
			throw new BadRequestAlertException("No Candidate Fount to link Non Acad", ENTITY_NAME,
					"");
		Candidate candidate = optionalCandidate.get();
		if (candidate.getNonAcademics().size() < 1) {
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_NON_ACADEMIC_PROFILE, false);
		}
		candidate.addNonAcademic(candidateNonAcademicWork);
		candidate = candidateRepository.save(candidate);
		return candidate.getNonAcademics().stream()
				.filter(nonAcad->nonAcad.getNonAcademicInitiativeTitle().equals(candidateNonAcademicWork.getNonAcademicInitiativeTitle()))
				.findFirst().orElse(candidateNonAcademicWork);
	}
	
	public CandidateNonAcademicWork updateCandidateNonAcademicWork(CandidateNonAcademicWork candidateNonAcademicWork ) {
		return candidateNonAcademicWorkRepository.save(candidateNonAcademicWork);
	}
	
	public void deleteCandidateNonAcademicWork(Long id) {
		Optional<CandidateNonAcademicWork> candidateNonAcademicWorkOptional = candidateNonAcademicWorkRepository.findById(id);
		if(!candidateNonAcademicWorkOptional.isPresent())
			return;
		CandidateNonAcademicWork candidateNonAcademicWork = candidateNonAcademicWorkOptional.get();
		Candidate candidate = candidateNonAcademicWorkOptional.get().getCandidate();
		log.debug("REST request to delete CandidateNonAcademicWork for candidate   : {} , {}", id,candidate.getId());
		candidate = candidate.removeNonAcademic(candidateNonAcademicWork);
		log.debug("Candidate post emoval of certs is {} {}",candidate,candidate.getNonAcademics());
		if(candidate.getNonAcademics().isEmpty())
			profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_NON_ACADEMIC_PROFILE, true);
		candidateRepository.save(candidate);
	}
	
	public List<CandidateNonAcademicWorkDTO> getNonAcadsByCandidate(Long id) {
		List<CandidateNonAcademicWork> candidateNonAcademicWorks = candidateNonAcademicWorkRepository
				.findNonAcademicWorkByCandidateId(id);
		Candidate candidate = candidateRepository.findById(id).get();
		log.debug("Canddiate Non Acad work is {}",candidateNonAcademicWorks);
		return dtoConverters.convertCandidateNonAcademicWork(candidateNonAcademicWorks, true,candidate);
	}
	
	public List<CandidateNonAcademicWork> getAllCandidateNonAcademicWorks() {
		return candidateNonAcademicWorkRepository.findAll();
	}
	
	public Optional<CandidateNonAcademicWork> getCandidateNonAcademicWork(Long id){
		return candidateNonAcademicWorkRepository.findById(id);
	}
	
	public List<CandidateNonAcademicWork> searchCandidateNonAcademicWorks( String query) {
		return StreamSupport
				.stream(candidateNonAcademicWorkSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}
	
	
}
