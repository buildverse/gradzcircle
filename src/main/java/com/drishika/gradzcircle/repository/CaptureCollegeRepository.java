package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CaptureCollege;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the CaptureCollege entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaptureCollegeRepository extends JpaRepository<CaptureCollege, Long> {

}
