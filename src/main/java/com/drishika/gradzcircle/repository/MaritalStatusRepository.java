package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.MaritalStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MaritalStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

}
