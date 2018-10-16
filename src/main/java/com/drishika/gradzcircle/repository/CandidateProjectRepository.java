package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateProject;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;

import org.springframework.data.jpa.repository.*;
import java.util.Set;

/**
 * Spring Data JPA repository for the CandidateProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateProjectRepository extends JpaRepository<CandidateProject, Long> {
	Set<CandidateProject> findByEducation(CandidateEducation education);

	Set<CandidateProject> findByEmployment(CandidateEmployment employment);

}
