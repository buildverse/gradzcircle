/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.util.Set;

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

}
