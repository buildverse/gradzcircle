package com.drishika.gradzcircle.repository;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Job;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

	@Query("select j from Job j where j.jobStatus>-1 and j.corporate.id=?1")
	Page<Job> findByActiveJobAndCorporateId(Long corporateId, Pageable pageable);

	@Query(" select j from Job j where j.jobStatus=1")
	List<Job> findAllActiveJobs();

	@Query(" select j from Job j where j.jobStatus=1")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<Job> findAllActiveJobsForMatchingAsStream();

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus>-1 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidate(Long candidateId, Pageable pageable);

	@Query("select appliedJob from Job j, CandidateAppliedJobs appliedJob, CandidateJob cJ where j.id=appliedJob.id.jobId and cJ.job.id=appliedJob.id.jobId and j.id=?1 and cJ.candidate.id=appliedJob.id.candidateId order by cJ.matchScore desc")
	Page<CandidateAppliedJobs> findByAppliedCandidates(Long jobId, Pageable pageable);

	@Query("select cJ from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus>-1 and j.id=?1 and concat(cast(cJ.job.id as text),cast(cJ.candidate.id as text)) not in (select concat(cast(cc.id.jobId as text),cast(cc.candidate.id as text)) from CorporateCandidate cc) order by cJ.matchScore desc")
	Page<CandidateJob> findMatchedCandidatesForJob(Long jobId, Pageable pageable);

	@Query("select j from Job j, CandidateAppliedJobs cJA where cJA.id.jobId = j.id and cJA.id.candidateId=?1")
	Page<Job> findAppliedJobByCandidate(Long candidateId, Pageable pageable);
	
	@Query("select count(j) from Job j where j.corporate.id=?1")
	Long countByCorporate(Long corporateId);
	
	@Query("select count(j) from Job j where j.corporate.id=?1 and j.createDate between ?2 and ?3")
	Long numberOfJobsPostedAcrossDates(Long corporateId, ZonedDateTime fromDate, ZonedDateTime toDate);

}
