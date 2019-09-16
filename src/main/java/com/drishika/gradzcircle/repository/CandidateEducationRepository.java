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

	@Query("select cE from CandidateEducation cE where cE.candidate.matchEligible=true and cE.highestQualification=true and cE.educationToDate >= ?1")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateAfterAndHighestQualification(LocalDate date);

	@Query("select cE from CandidateEducation cE  where cE.candidate.matchEligible=true and cE.highestQualification=true and cE.educationToDate <= ?1")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateBeforeAndHighestQualification(LocalDate date);

	@Query("select cE from CandidateEducation cE  where cE.candidate.matchEligible=true and cE.highestQualification=true and cE.educationToDate >= ?1 and cE.educationToDate <= ?2")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateBetweenAndHighestQualification(LocalDate fromDate, LocalDate toDate);

	@Query("select cE from CandidateEducation cE  where cE.candidate.matchEligible=true and cE.highestQualification=true")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findAllHighestCandidateEducationForMatchEligilbeCandidates();
	
	@Query("select cE from CandidateEducation cE where cE.candidate.matchEligible=true and cE.highestQualification=true and (cE.educationToDate >= ?1 or cE.educationToDate is null)")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<CandidateEducation> findByEducationToDateFutureAndHighestQualification(LocalDate date);
}
