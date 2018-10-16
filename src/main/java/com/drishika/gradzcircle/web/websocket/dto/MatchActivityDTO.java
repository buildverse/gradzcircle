/**
 * 
 */
package com.drishika.gradzcircle.web.websocket.dto;

import java.time.Instant;

/**
 * @author abhinav
 *
 */
public class MatchActivityDTO {

	private Long corporateId;
	private Long jobId;
	private Double matchScore;
	private Long candidateId;

	/**
	 * @return the corporateId
	 */
	public Long getCorporateId() {
		return corporateId;
	}

	/**
	 * @param corporateId
	 *            the corporateId to set
	 */
	public void setCorporateId(Long corporateId) {
		this.corporateId = corporateId;
	}

	/**
	 * @return the jobId
	 */
	public Long getJobId() {
		return jobId;
	}

	/**
	 * @param jobId
	 *            the jobId to set
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
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
	 * @return the candidateId
	 */
	public Long getCandidateId() {
		return candidateId;
	}

	/**
	 * @param candidateId
	 *            the candidateId to set
	 */
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MatchActivityDTO [corporateId=" + corporateId + ", jobId=" + jobId + ", matchScore=" + matchScore
				+ ", candidateId=" + candidateId + "]";
	}

}
