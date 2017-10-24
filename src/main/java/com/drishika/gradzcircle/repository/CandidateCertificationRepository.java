package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CandidateCertification;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CandidateCertification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateCertificationRepository extends JpaRepository<CandidateCertification,Long> {

    @Query("select candidateCertification from CandidateCertification candidateCertification where candidateCertification.candidate.id=:id order By candidateCertification.certificationDate desc")
    List<CandidateCertification> findCertificationsByCandidateId(@Param("id") Long id);

}
