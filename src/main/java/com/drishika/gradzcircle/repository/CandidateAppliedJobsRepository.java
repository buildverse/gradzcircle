/**
 * 
 */
package com.drishika.gradzcircle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateJob;

/**
 * @author abhinav
 *
 */
public interface CandidateAppliedJobsRepository
		extends JpaRepository<CandidateAppliedJobs, CandidateAppliedJobs.CandidateJobId> {

	@Query("select cJ from CandidateAppliedJobs cJ where cJ.id.candidateId.id=?1")
	Page<CandidateAppliedJobs> findByCandidateId(Long candidateId, Pageable pageable);

	@Query("select cJ from CandidateAppliedJobs cJ where cJ.id.jobId.id=?1")
	Page<CandidateAppliedJobs> findByJobId(Long jobId, Pageable pageable);

	@Query("select count(cJ) from CandidateAppliedJobs cJ, Job j where cJ.id.jobId=j.id and j.corporate.id=?1")
	Long findAppliedCandidatesForAllJobsByCorporate(Long corporateId);
}
