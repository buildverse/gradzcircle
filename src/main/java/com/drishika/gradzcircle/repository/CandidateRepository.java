package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.Job;

/**
 * Spring Data JPA repository for the Candidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	// @Query("select distinct candidate from Candidate candidate left join fetch
	// candidate.jobCategories left join fetch candidate.jobs")
	@Query("select distinct candidate from Candidate candidate left join fetch candidate.jobCategories")
	List<Candidate> findAllWithEagerRelationships();

	// @Query("select candidate from Candidate candidate left join fetch
	// candidate.jobCategories left join fetch candidate.jobs where candidate.id
	// =:id")
	@Query("select candidate from Candidate candidate left join fetch candidate.jobCategories where candidate.id =:id")
	Candidate findOneWithEagerRelationships(@Param("id") Long id);

	Candidate findByLoginId(Long uid);

	/*
	 * @Query("select c from Candidate c left outer join fetch c.appliedJobs where job.id=:id "
	 * ) Page<Candidate> findAppliedCandidatesByJob (Long jobId,Pageable pageable);
	 * 
	 * @Query("select c from Candidate c left outer join fetch c.candidateJobs where job.id=:id "
	 * ) Page<Candidate> findMatchedCandidatesForJobId ( Long jobId, Pageable
	 * pageable);
	 */
	/*
	 * @Query("select distinct candidate from Candidate candidate join candidate.jobs where candidate = :candidate"
	 * ) List<Job> findAllMatchedJobsByCandidate(Candidate candidate);
	 * 
	 * @Query("select distinct candidate from Candidate candidate join candidate.jobs "
	 * ) List<Job> findAllMatchedJobsByAllCandidate();
	 */
}
