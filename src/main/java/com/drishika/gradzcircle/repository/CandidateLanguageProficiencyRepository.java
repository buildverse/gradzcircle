package com.drishika.gradzcircle.repository;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;


/**
 * Spring Data JPA repository for the CandidateLanguageProficiency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateLanguageProficiencyRepository extends JpaRepository<CandidateLanguageProficiency,Long> {

    @Query("select candidateLanguageProficiency from CandidateLanguageProficiency candidateLanguageProficiency where candidateLanguageProficiency.candidate.id=:id")
    List<CandidateLanguageProficiency> findCandidateLanguageProficienciesByCandidateId (@Param("id") Long id);
    
    @Query("select cLP from CandidateLanguageProficiency cLP inner join cLP.candidate c where c.matchEligible=true ")
    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
    Stream<CandidateLanguageProficiency> findCandidateLanguageProficienciesForActiveCandidates ();
}
