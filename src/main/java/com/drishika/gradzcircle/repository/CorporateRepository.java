package com.drishika.gradzcircle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;

/**
 * Spring Data JPA repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {

	Corporate findByLoginId(Long id);

	@Query("select corporateCandidate from CorporateCandidate corporateCandidate where corporateCandidate.corporate.id=?1")
	Page<CorporateCandidate> findLinkedCandidates(Long corporateId, Pageable pageable);

}
