package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Gender;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Gender entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

	Gender findByGender(String gender);

}
