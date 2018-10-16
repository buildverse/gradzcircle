package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Employability;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Employability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployabilityRepository extends JpaRepository<Employability, Long> {

}
