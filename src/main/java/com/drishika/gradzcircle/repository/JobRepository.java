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
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.service.dto.JobStatistics;

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
	
	Long countByJobStatus(Integer status);
	
	@Query(" select j from Job j where j.jobStatus=1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)")
	Page<Job> findAllActiveJobsForCandidatesThatCandidateHasNotAppliedFor(Pageable pageable, Long candidateId);

	
	@Query(" select j from Job j where j.jobStatus=1")
	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "1000"))
	Stream<Job> findAllActiveJobsForMatchingAsStream();

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?2 and ?3 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidateWithMatchScore(Long candidateId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?3 and ?4 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilterByEmploymentTypeForCandidateWithMatchScore(Long candidateId, Long employmentTypeId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and j.jobType.id =?3 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?4 and ?5 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndJobTypeForCandidateWithMatchScore(Long candidateId, Long employmentTypeId,Long jobTypeId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and (j.jobType.id =?3 or j.jobType.id=?4) and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?5 and ?6 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndTwoJobTypeForCandidateWithMatchScore(Long candidateId, Long employmentTypeId,Long jobTypeId1, Long jobTypeId2, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and j.jobType.id != ?3 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?4 and ?5 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndEmploymentTypeAndJobTypeNotForCandidateWithMatchScore(Long candidateId, Long employmentTypeId, Long jobTypeId, Double matchScoreFrom, Double matchScoreTo,Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.jobType.id = ?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?3 and ?4 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndJobTypeForCandidateWithMatchScore(Long candidateId, Long jobTypId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and (j.jobType.id = ?2 or j.jobType.id = ?3) and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?4 and ?5 order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndTwoJobTypesForCandidateWithMatchScore(Long candidateId, Long jobTypId1, Long jobTypeId2 ,Double matchScoreFrom, Double matchScoreTo, Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.jobType.id != ?2  and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) and cJ.matchScore between ?3 and ?4 order by cJ.matchScore desc")
	Page<Job>findByJobStatusAndJobTypeNotForCandidateWithMatchScore(Long candidateId, Long jobTypeId, Double matchScoreFrom, Double matchScoreTo, Pageable pageable);
	
// Without Match Score
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidate(Long candidateId, Pageable pageable);
	
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)  order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilterByEmploymentTypeForCandidate(Long candidateId, Long employmentTypeId, Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and j.jobType.id =?3 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndJobTypeForCandidate(Long candidateId, Long employmentTypeId,Long jobTypeId, Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and (j.jobType.id =?3 or j.jobType.id=?4) and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)  order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedFilteredByEmploymentTypeAndTwoJobTypeForCandidate(Long candidateId, Long employmentTypeId,Long jobTypeId1, Long jobTypeId2,  Pageable pageable);

	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and j.jobType.id != ?3 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)  order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndEmploymentTypeAndJobTypeNotForCandidate(Long candidateId, Long employmentTypeId, Long jobTypeId, Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.jobType.id = ?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)  order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndJobTypeForCandidate(Long candidateId, Long jobTypId, Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and (j.jobType.id = ?2 or j.jobType.id = ?3) and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)  order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndTwoJobTypesForCandidate(Long candidateId, Long jobTypId1, Long jobTypeId2 , Pageable pageable);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.jobType.id != ?2  and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job>findByJobStatusAndJobTypeNotForCandidate(Long candidateId, Long jobTypeId,  Pageable pageable);
	//-------
	
	
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
	
	@Query("select j from Job j, CorporateCandidate cc where j.id=cc.id.jobId and cc.id.candidateId=?1")
	Page<Job> getJobListShortListedForCandidate(Long candidateId, Pageable pageable);	
	
	@Query(" select j from Job j where j.jobStatus=1 and j.employmentType.id=?2 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)")
	Page<Job> findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByEmploymentType(Pageable pageable, Long candidateId, Long employementTypeId);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.employmentType.id=?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidateByEmploymentType(Long candidateId, Pageable pageable,Long employementTypeId);
	
	@Query(" select j from Job j where j.jobStatus=1 and j.jobType.id=?2 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)")
	Page<Job> findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByJobType(Pageable pageable, Long candidateId,Long jobTypeId);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus=1 and j.jobType.id=?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidateByJobType(Long candidateId, Pageable pageable,Long jobTypeId);
	
	@Query(" select j from Job j where j.jobStatus=1 and j.jobType.id=?3 and j.employmentType.id=?2 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1)")
	Page<Job> findAllActiveJobsForCandidatesThatCandidateHasNotAppliedForByEmploymentAndJobType(Pageable pageable, Long candidateId,Long employmentTypeId, Long jobTypeId);
	
	@Query("select j from Job j, CandidateJob cJ where j.id=cJ.job.id and j.jobStatus = 1 and j.jobType.id=?3 and j.employmentType.id=?2 and cJ.candidate.id=?1 and j.id not in (select cJA.id.jobId from CandidateAppliedJobs cJA where cJA.id.candidateId =?1) order by cJ.matchScore desc")
	Page<Job> findByJobStatusAndMatchAndNotAppliedForCandidateByEmploymentAndJobType(Long candidateId, Pageable pageable,Long employmentTypeId, Long jobTypeId);
	
	/*When user selects both employment type , all job types or nothing*/
	Page<Job> findByJobStatus(Integer active, Pageable pageable);

	/*When user selects both employment type , all job types or nothing*/
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType))"
			+ " FROM  Job j where j.jobStatus = 1 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentType();
	
	/*When user selects both employment type , all job types or nothing*/
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobType ();

	/*when user selects only Employment Type */
	Page<Job> findByJobStatusAndEmploymentType(Integer jobStatus, EmploymentType empType, Pageable pageable);
	
	/*when user selects only Employment Type */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenEmploymentTypeSelected(Long employmentTypeId);
	
	/*when user selects only Employment Type */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenEmployementTypeSelected (Long employmentTypeId);
	
	/*when user selects one Job Type ( or both EmploymentType and One job Type )*/
	
	Page<Job> findByJobStatusAndJobType(Integer jobStatus, JobType jobType, Pageable pageable);
	
	/*when user selects one Job Type ( or both EmploymentType and One job Type )*/
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id=?1 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenOneJobTypeSelected(Long jobTypeId);
	
	
	/*when user selects one Job Type ( or both EmploymentType and One job Type ) */
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id=?1 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenOneJobTypeSelected (Long jobTypeId);
	
	/*when user selects Two Job Types */
	
	@Query("Select j from Job j where j.jobStatus =?1 and (j.jobType.id = ?2 or j.jobType.id = ?3)")
	Page<Job> findByJobStatusAndTwoJobTypes(Integer jobStatus, Long jobTypeId1,Long jobTypeId2, Pageable pageable);
	
	/*when user selects Two Job Types */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and (j.jobType.id = ?1 or j.jobType.id = ?2) group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenTwoJobTypeSelected(Long jobTypeId1,Long jobTypeId2);
	
	
	/*when user selects Two Job Types */
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and (j.jobType.id = ?1 or j.jobType.id = ?2) group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenTwoJobTypeSelected (Long jobTypeId1, Long jobTypeId2);
	
	/*when user selects Three Job Types (Do not equla to for the one not asked for )*/
	
	Page<Job> findByJobStatusAndJobTypeNot(Integer jobStatus, JobType jobType, Pageable pageable);
	
	/*when user selects Three Job Types ( do not equal to the one not selected ) */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id != ?1 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenThreeJobTypeSelected(Long jobTypeId);
	
	/*when user selects Three Job Types  ( do not equal to the one not selected ) */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id != ?1 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenThreeJobTypeSelected (Long jobTypeId);
	
	/*when user selects One Employment Type and One Job Type */
	
	Page<Job> findByJobStatusAndJobTypeAndEmploymentType(Integer jobStatus, JobType jobType, EmploymentType employmentType,Pageable pageable);
	
	/*when user selects One Employment Type and One Job Type */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id = ?1 and j.employmentType.id= ?2 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenOneEmploymentTypeAndOneJobTypeSelected(Long jobTypeId, Long employmentTypeId);
	
	/*when user selects One Employment Type and One Job Type */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.jobType.id = ?1 and j.employmentType.id= ?2 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenOneEmploymentTypeAndOneJobTypeSelected (Long jobTypeId, Long employmentTypeId);
	
	/*when user selects one employment Type and Two Job Types */
	
	@Query("Select j from Job j where j.jobStatus = ?1 and j.employmentType.id=?2 and (j.jobType.id = ?3 or j.jobType.id = ?4)")
	Page<Job> findByJobStatusAndOneEmploymentTypeAndTwoJobTypes(Integer jobStatus, Long employmentTypeId, Long jobTypeId1,Long jobTypeId2, Pageable pageable);
	
	/*when user selects one employment Type and Two Job Types */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 and (j.jobType.id = ?2 or j.jobType.id = ?3) group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenOneEmploymentTypeAndTwoJobTypeSelected(Long employmentTypeId, Long jobTypeId1,Long jobTypeId2);
	
	/*when user selects one employment Type and Two Job Types */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 and (j.jobType.id = ?2 or j.jobType.id = ?3) group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenOneEmploymentTypeAndTwoJobTypeSelected (Long employmentTypeId, Long jobTypeId1, Long jobTypeId2);
	
	/*when user selects one employment Type and Three Job Types */
	
	Page<Job> findByJobStatusAndEmploymentTypeAndJobTypeNot(Integer jobStatus, EmploymentType employmentType, JobType jobType, Pageable pageable);
	 
	/*when user selects one employment Type and Three Job Types */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.employmentType.employmentType, COUNT(employmentType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 and j.jobType.id != ?2 group by j.employmentType.employmentType")
	List<JobStatistics> findStatisticsCountByEmploymentTypeWhenOneEmployementTypeAndThreeJobTypeSelected(Long employmentTypeId, Long jobTypeId);
	
	/*when user selects one employment Type and Three Job Types */
	
	@Query("SELECT "
			+ "    new com.drishika.gradzcircle.service.dto.JobStatistics (j.jobType.jobType, COUNT(jobType)) "
			+ " FROM  Job j where j.jobStatus = 1 and j.employmentType.id=?1 and j.jobType.id != ?2 group by j.jobType.jobType")
	List<JobStatistics> findStatisticsCountByJobTypeWhenOneEmployementTypeAndThreeJobTypeSelected (Long employmentTypeId, Long jobTypeId);
	
	
}
