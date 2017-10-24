package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Nationality;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Nationality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Long> {

}
