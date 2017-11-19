package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateEducation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import com.drishika.gradzcircle.domain.Candidate;
import org.springframework.data.repository.query.Param;
import java.util.List;



/**
 * Spring Data JPA repository for the CandidateEducation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Long> {

    @Query("select candidateEducation from CandidateEducation candidateEducation where candidateEducation.candidate.id=:id order By candidateEducation.educationToDate desc")
    List<CandidateEducation> findByCandidateId(@Param("id") Long id);

    CandidateEducation findByCandidateAndHighestQualification(Candidate candidate,Boolean highestQualification);

}
