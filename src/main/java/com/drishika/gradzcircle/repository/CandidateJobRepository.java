/**
 * 
 */
package com.drishika.gradzcircle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CandidateJob;

/**
 * @author abhinav
 *
 */
@Repository
public interface CandidateJobRepository extends JpaRepository<CandidateJob, CandidateJob.CandidateJobId> {

	@Query("select cJ from CandidateJob cJ where cJ.candidate.id=?1")
	Page<CandidateJob> findByCandidateId(Long candidateId, Pageable pageable);

	@Query("select cJ from CandidateJob cJ where cJ.job.id=?1")
	Page<CandidateJob> findByJobId(Long jobId, Pageable pageable);

	@Query("select cJ from CandidateJob cJ where cJ.job.id=?1 and cJ.reviewed=true")
	Page<CandidateJob> findCandidateReviewedForJob(Long jobId, Pageable pageable);

}
