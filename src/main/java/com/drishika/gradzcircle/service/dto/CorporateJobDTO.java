/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.JobType;

/**
 * @author abhinav
 *
 */
public class CorporateJobDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String jobTitle;

	private Integer jobStatus;

	private EmploymentType employmentType;

	private JobType jobType;

	private Integer noOfMatchedCandidates;

	private Integer noOfCandidatesApplied;

	private Double matchScore;
	
	private Long totalNumberOfJobs;
	
	private Long jobsLastMonth;
	
	private Long newApplicants;
	
	private Long totalLinkedCandidates;
	
	private Boolean hasBeenEdited;

	private Boolean everActive;

	private Boolean canEdit;
	
	private Long noOfShortListedCandidate;
	
	private Long numberOfCandidatesRemaining;
	
	private Double corporateEscrowAmount;
	
	private Integer noOfApplicants;
	



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the jobStatus
	 */
	public Integer getJobStatus() {
		return jobStatus;
	}

	/**
	 * @param jobStatus
	 *            the jobStatus to set
	 */
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * @return the employmentType
	 */
	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType
	 *            the employmentType to set
	 */
	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * @return the jobType
	 */
	public JobType getJobType() {
		return jobType;
	}

	/**
	 * @param jobType
	 *            the jobType to set
	 */
	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	/**
	 * @return the matchScore
	 */
	public Double getMatchScore() {
		return matchScore;
	}

	/**
	 * @param matchScore
	 *            the matchScore to set
	 */
	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}

	/**
	 * @return the noOfMatchedCandidates
	 */
	public Integer getNoOfMatchedCandidates() {
		return noOfMatchedCandidates;
	}

	/**
	 * @param noOfMatchedCandidates
	 *            the noOfMatchedCandidates to set
	 */
	public void setNoOfMatchedCandidates(Integer noOfMatchedCandidates) {
		this.noOfMatchedCandidates = noOfMatchedCandidates;
	}

	/**
	 * @return the noOfCandidatesApplied
	 */
	public Integer getNoOfCandidatesApplied() {
		return noOfCandidatesApplied;
	}

	/**
	 * @param noOfCandidatesApplied
	 *            the noOfCandidatesApplied to set
	 */
	public void setNoOfCandidatesApplied(Integer noOfCandidatesApplied) {
		this.noOfCandidatesApplied = noOfCandidatesApplied;
	}

	/**
	 * @return the totalNumberOfJobs
	 */
	public Long getTotalNumberOfJobs() {
		return totalNumberOfJobs;
	}

	/**
	 * @param totalNumberOfJobs the totalNumberOfJobs to set
	 */
	public void setTotalNumberOfJobs(Long totalNumberOfJobs) {
		this.totalNumberOfJobs = totalNumberOfJobs;
	}

	/**
	 * @return the jobsLastMonth
	 */
	public Long getJobsLastMonth() {
		return jobsLastMonth;
	}

	/**
	 * @param jobsLastMonth the jobsLastMonth to set
	 */
	public void setJobsLastMonth(Long jobsLastMonth) {
		this.jobsLastMonth = jobsLastMonth;
	}

	/**
	 * @return the newApplicants
	 */
	public Long getNewApplicants() {
		return newApplicants;
	}

	/**
	 * @param newApplicants the newApplicants to set
	 */
	public void setNewApplicants(Long newApplicants) {
		this.newApplicants = newApplicants;
	}

	/**
	 * @return the totalLinkedCandidates
	 */
	public Long getTotalLinkedCandidates() {
		return totalLinkedCandidates;
	}

	/**
	 * @param totalLinkedCandidates the totalLinkedCandidates to set
	 */
	public void setTotalLinkedCandidates(Long totalLinkedCandidates) {
		this.totalLinkedCandidates = totalLinkedCandidates;
	}

	/**
	 * @return the hasBeenEdited
	 */
	public Boolean getHasBeenEdited() {
		return hasBeenEdited;
	}

	/**
	 * @param hasBeenEdited the hasBeenEdited to set
	 */
	public void setHasBeenEdited(Boolean hasBeenEdited) {
		this.hasBeenEdited = hasBeenEdited;
	}

	/**
	 * @return the everActive
	 */
	public Boolean getEverActive() {
		return everActive;
	}

	/**
	 * @param everActive the everActive to set
	 */
	public void setEverActive(Boolean everActive) {
		this.everActive = everActive;
	}

	/**
	 * @return the canEdit
	 */
	public Boolean getCanEdit() {
		return canEdit;
	}

	/**
	 * @param canEdit the canEdit to set
	 */
	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 * @return the noOfShortListedCandidate
	 */
	public Long getNoOfShortListedCandidate() {
		return noOfShortListedCandidate;
	}

	/**
	 * @param noOfShortListedCandidate the noOfShortListedCandidate to set
	 */
	public void setNoOfShortListedCandidate(Long noOfShortListedCandidate) {
		this.noOfShortListedCandidate = noOfShortListedCandidate;
	}

	/**
	 * @return the numberOfCandidatesRemaining
	 */
	public Long getNumberOfCandidatesRemaining() {
		return numberOfCandidatesRemaining;
	}

	/**
	 * @param numberOfCandidatesRemaining the numberOfCandidatesRemaining to set
	 */
	public void setNumberOfCandidatesRemaining(Long numberOfCandidatesRemaining) {
		this.numberOfCandidatesRemaining = numberOfCandidatesRemaining;
	}

	/**
	 * @return the corporateEscrowAmount
	 */
	public Double getCorporateEscrowAmount() {
		return corporateEscrowAmount;
	}

	/**
	 * @param corporateEscrowAmount the corporateEscrowAmount to set
	 */
	public void setCorporateEscrowAmount(Double corporateEscrowAmount) {
		this.corporateEscrowAmount = corporateEscrowAmount;
	}

	/**
	 * @return the noOfApplicants
	 */
	public Integer getNoOfApplicants() {
		return noOfApplicants;
	}

	/**
	 * @param noOfApplicants the noOfApplicants to set
	 */
	public void setNoOfApplicants(Integer noOfApplicants) {
		this.noOfApplicants = noOfApplicants;
	}




	
	

}
