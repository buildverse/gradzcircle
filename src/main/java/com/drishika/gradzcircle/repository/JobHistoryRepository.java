package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobHistory;


/**
 * Spring Data JPA repository for the JobHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
	
	List<JobHistory> findByJob(Job job);
	JobHistory findTopByOrderByIdDesc();
	List<JobHistory> findByJobOrderByIdDesc(Job job);
	List<JobHistory> findByJobIdOrderByIdDesc(Long id);

}
