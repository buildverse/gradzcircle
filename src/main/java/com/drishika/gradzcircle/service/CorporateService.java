package com.drishika.gradzcircle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.search.CorporateSearchRepository;


/**
 * Service class for managing users.
 */
@Service
@Transactional

public class CorporateService {

    private final Logger logger = LoggerFactory.getLogger(CorporateService.class);

    private CorporateRepository corporateRepository;

    private CorporateSearchRepository corporateSearchRepository;

    public CorporateService (CorporateRepository corporateRepository, CorporateSearchRepository corporateSearchRepository){
        this.corporateRepository = corporateRepository;
        this.corporateSearchRepository = corporateSearchRepository;
    }

    public void createCorporate (String corporateName,String phoneNumber,String country,User user ){
        Corporate corporate = new Corporate();
        corporate.setCorporateName(corporateName);
        //corporate.setCorporatePhone(phoneNumber);
        //corporate.setCorporateCountry (country);
        corporate.setLogin(user);
        corporateRepository.save(corporate);
        corporateSearchRepository.save(corporate);
        logger.debug("Information for created Corporate {} ",corporate);
    }


}