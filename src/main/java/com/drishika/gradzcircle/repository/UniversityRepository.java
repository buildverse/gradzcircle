package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.University;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the University entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
	
	public University findByUniversityName(String universityName);

}
