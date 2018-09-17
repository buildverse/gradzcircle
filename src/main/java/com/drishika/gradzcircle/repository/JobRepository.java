package com.drishika.gradzcircle.repository;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Job;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	
	List<Job> findByJobStatusGreaterThanAndCorporateId(int jobStatus, Long corporateId);
	
	@Query(" select j from Job j where j.jobStatus=1")
	List<Job> findAllActiveJobs();
	
	@Query(" select j from Job j where j.jobStatus=1")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<Job> findAllActiveJobsForMatchingAsStream();
	
}
