package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.College;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the College entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    public College findByCollegeName(String CollegeName);
}
