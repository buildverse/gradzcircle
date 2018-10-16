package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.EmploymentType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the EmploymentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, Long> {

}
