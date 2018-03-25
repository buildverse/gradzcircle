package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.JobFilterHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobFilterHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobFilterHistoryRepository extends JpaRepository<JobFilterHistory, Long> {

}
