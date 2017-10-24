package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.JobCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

}
