package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateEmployment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the CandidateEmployment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateEmploymentRepository extends JpaRepository<CandidateEmployment, Long> {
	@Query("select candidateEmployment from CandidateEmployment candidateEmployment where candidateEmployment.candidate.id=:id order By candidateEmployment.employmentEndDate desc")
	List<CandidateEmployment> findByCandidateId(@Param("id") Long id);

}
