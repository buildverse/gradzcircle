/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.JobType;

/**
 * @author abhinav
 *
 */
public class CandidateJobDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String jobTitle;

	private Integer jobStatus;

	private EmploymentType employmentType;

	private JobType jobType;

	private Double matchScore;

	private Boolean hasCandidateApplied;

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
	 * @return the hasCandidateApplied
	 */
	public Boolean getHasCandidateApplied() {
		return hasCandidateApplied;
	}

	/**
	 * @param hasCandidateApplied
	 *            the hasCandidateApplied to set
	 */
	public void setHasCandidateApplied(Boolean hasCandidateApplied) {
		this.hasCandidateApplied = hasCandidateApplied;
	}

}
