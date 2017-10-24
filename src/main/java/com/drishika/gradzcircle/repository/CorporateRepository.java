package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Corporate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {

    Corporate findByLoginId(Long id);

}
