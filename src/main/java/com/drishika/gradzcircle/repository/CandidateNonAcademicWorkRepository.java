package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the CandidateNonAcademicWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateNonAcademicWorkRepository extends JpaRepository<CandidateNonAcademicWork, Long> {
	@Query("select candidateNonAcademicWork from CandidateNonAcademicWork candidateNonAcademicWork where candidateNonAcademicWork.candidate.id=:id order by candidateNonAcademicWork.nonAcademicWorkEndDate desc")
	List<CandidateNonAcademicWork> findNonAcademicWorkByCandidateId(@Param("id") Long id);

}
