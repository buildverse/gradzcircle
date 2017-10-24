package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CaptureQualification;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CaptureQualification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaptureQualificationRepository extends JpaRepository<CaptureQualification, Long> {

}
