package com.drishika.gradzcircle.repository;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;

/**
 * Spring Data JPA repository for the CandidateEducation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Long> {

	@Query("select candidateEducation from CandidateEducation candidateEducation where candidateEducation.candidate.id=:id order By candidateEducation.educationToDate desc")
	List<CandidateEducation> findByCandidateId(@Param("id") Long id);

	CandidateEducation findByCandidateAndHighestQualification(Candidate candidate, Boolean highestQualification);

	List<CandidateEducation> findByOrderByEducationToDateDesc();

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateAfterAndHighestQualification(LocalDate date,
			Boolean highestEducation);

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateBeforeAndHighestQualification(LocalDate date,
			Boolean highestEducation);

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateBetweenAndHighestQualification(LocalDate fromDate, LocalDate toDate,
			Boolean highestEducation);

	@Query("select cE from CandidateEducation cE inner join cE.candidate c where c.matchEligible=true and cE.highestQualification=true")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findAllHighestCandidateEducationForMatchEligilbeCandidates();

}
