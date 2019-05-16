package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.JobType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {
	
	public JobType findByJobType(String jobType);
	
	@Query("Select j from JobType j where j.jobType not in (?1,?2,?3)")
	public JobType findRemainingJobType(String id1, String id2, String id3);

}
