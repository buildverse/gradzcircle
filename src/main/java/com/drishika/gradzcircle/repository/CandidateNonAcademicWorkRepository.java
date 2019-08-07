package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;

/**
 * Spring Data JPA repository for the CandidateNonAcademicWork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateNonAcademicWorkRepository extends JpaRepository<CandidateNonAcademicWork, Long> {
	@Query("select candidateNonAcademicWork from CandidateNonAcademicWork candidateNonAcademicWork where candidateNonAcademicWork.candidate.id=:id order by candidateNonAcademicWork.nonAcademicWorkEndDate desc")
	List<CandidateNonAcademicWork> findNonAcademicWorkByCandidateId(@Param("id") Long id);

}
