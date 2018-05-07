package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Job;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	
	@Query(" select j from Job j where j.jobStatus>=0")
	List<Job> findByJobStatus();
}
