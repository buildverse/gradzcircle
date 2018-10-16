package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CaptureUniversity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the CaptureUniversity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaptureUniversityRepository extends JpaRepository<CaptureUniversity, Long> {

}
