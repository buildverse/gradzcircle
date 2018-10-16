package com.drishika.gradzcircle.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;

/**
 * Spring Data JPA repository for the JobFilter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobFilterRepository extends JpaRepository<JobFilter, Long> {

	JobFilter findByJob(Job job);

}
