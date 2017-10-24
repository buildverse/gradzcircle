package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the CandidateLanguageProficiency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateLanguageProficiencyRepository extends JpaRepository<CandidateLanguageProficiency,Long> {

    @Query("select candidateLanguageProficiency from CandidateLanguageProficiency candidateLanguageProficiency where candidateLanguageProficiency.candidate.id=:id")
    List<CandidateLanguageProficiency> findCandidateLanguageProficienciesByCandidateId (@Param("id") Long id);
}
