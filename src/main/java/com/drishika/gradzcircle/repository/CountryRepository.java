package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Country;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {

    /**
    *
    * Use query Annotation to get enabled countris for Business
    *
    */
    @Query("Select  c from Country c where enabled=true")
    public List<Country> findEnabledCountries();

    /**
     * Reduced payload for meta data
     */
    @Query("Select c.id as id, c.countryNiceName as countryNiceName, c.phoneCode as phoneCode from Country c")
    public List<Country> findCountries();

}
