package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.CaptureCourse;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CaptureCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaptureCourseRepository extends JpaRepository<CaptureCourse, Long> {

}
