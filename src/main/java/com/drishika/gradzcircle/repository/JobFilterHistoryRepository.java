package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;

/**
 * Spring Data JPA repository for the JobFilterHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobFilterHistoryRepository extends JpaRepository<JobFilterHistory, Long> {

	JobFilterHistory findByJobFilter(JobFilter jobFilter);

	List<JobFilterHistory> findByJobFilterOrderByIdDesc(JobFilter jobFilter);

}
