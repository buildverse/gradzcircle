package com.drishika.gradzcircle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.util.DTOConverters;

/**
 * Service class for managing users.
 */
@Service
@Transactional

public class CorporateService {

	private final Logger logger = LoggerFactory.getLogger(CorporateService.class);

	private final CorporateRepository corporateRepository;

	private final CorporateSearchRepository corporateSearchRepository;
	
	private final CountryRepository countryRepository;

	private final DTOConverters converter;

	public CorporateService(CorporateRepository corporateRepository,
			CorporateSearchRepository corporateSearchRepository, DTOConverters converter,CountryRepository countryRepository) {
		this.corporateRepository = corporateRepository;
		this.corporateSearchRepository = corporateSearchRepository;
		this.converter = converter;
		this.countryRepository = countryRepository;
	}

	public void createCorporate(String corporateName, String phoneNumber, String country, User user) {
		Corporate corporate = new Corporate();
		corporate.setName(corporateName);
		// corporate.setCorporatePhone(phoneNumber);
		// corporate.setCorporateCountry (country);
		corporate.setLogin(user);
		corporateRepository.save(corporate);
		corporateSearchRepository.save(corporate);
		logger.debug("Information for created Corporate {} ", corporate);
	}
	
	public Corporate updateCorporate(Corporate corporate) {
		Corporate corporateFromRepo = corporateRepository.findOne(corporate.getId());
		corporate.setShortlistedCandidates(corporateFromRepo.getShortlistedCandidates());
		injestCountryDetails(corporate);
		Corporate result = corporateRepository.save(corporate);
		return result;
	}

	private void injestCountryDetails(Corporate corporate) {
		Country country  = countryRepository.findByCountryNiceName(corporate.getCountry().getCountryNiceName());
		corporate.setCountry(country);
	}

	
	/**
	 * @param id
	 * @return
	 */
	public Corporate getCorporateByLoginId(Long id) {
		// TODO Auto-generated method stub
		return corporateRepository.findByLoginId(id);
	}

	/**
	 * @param id
	 */
	public void deleteCorporate(Long id) {
		corporateRepository.delete(id);
		corporateSearchRepository.delete(id);

	}

	public Page<CandidateProfileListDTO> getLinkedCandidates(Pageable pageable, Long corporateId) {
		Page<CorporateCandidate> candidatePage = corporateRepository.findLinkedCandidates(corporateId, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage.map(
				corporateCandidate -> converter.convertToCandidateProfileListingDTO(corporateCandidate.getCandidate()));
		return page;
	}
	
	public Long getAllLinkedCandidates(Long corporateId) {
		return corporateRepository.findAllLinkedCandidates(corporateId);
	}

}