package com.drishika.gradzcircle.service;

import java.util.Optional;

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
		corporate.setLogin(user);
		corporateRepository.save(corporate);
		logger.debug("Information for created Corporate {} ", corporate);
	}
	
	public Corporate updateCorporate(Corporate corporate) {
		Optional<Corporate> corporateFromRepo = corporateRepository.findById(corporate.getId());
		if(!corporateFromRepo.isPresent())
			return null;
		corporate.setShortlistedCandidates(corporateFromRepo.get().getShortlistedCandidates());
		injestCountryDetails(corporate);
		corporate.setCity(convertToCamelCase(corporate.getCity()));
		Corporate result = corporateRepository.save(corporate);
		return result;
	}
	
	
	private String convertToCamelCase(String string) {
		if(string == null)
			return "";
		char[] charArray = string.toLowerCase().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        for(int i=1 ; i<charArray.length ;i++ )
        		charArray[i] = Character.toLowerCase(charArray[i]);
        return new String(charArray);
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
		corporateRepository.deleteById(id);
	//	corporateSearchRepository.delete(id);

	}

	public Page<CandidateProfileListDTO> getLinkedCandidates(Pageable pageable, Long corporateId) {
		Page<Candidate> candidatePage = corporateRepository.findLinkedCandidates(corporateId, pageable);
		final Page<CandidateProfileListDTO> page = candidatePage.map(
				candidate -> converter.convertToCandidateProfileListingDTO(candidate));
		return page;
	}
	
	public Long getLinkedCandidatesCount(Long corporateId) {
		return corporateRepository.findAllLinkedCandidates(corporateId);
	}

}