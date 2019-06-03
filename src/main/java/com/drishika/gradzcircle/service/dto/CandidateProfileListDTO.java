/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

import com.drishika.gradzcircle.domain.User;

/**
 * @author abhinav
 *
 */
public class CandidateProfileListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String tagLine;
	private String qualificationWithHighestCourse;
	private User login;
	private Long id;
	private Boolean reviewed;
	private Long jobId;
	private Long corporateId;
	private Double matchScore;

	/**
	 * @return the reviewed
	 */
	public Boolean getReviewed() {
		return reviewed;
	}

	/**
	 * @param reviewed
	 *            the reviewed to set
	 */
	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the tagLine
	 */
	public String getTagLine() {
		return tagLine;
	}

	/**
	 * @param tagLine
	 *            the tagLine to set
	 */
	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	/**
	 * @return the qualificationWithHighestCourse
	 */
	public String getQualificationWithHighestCourse() {
		return qualificationWithHighestCourse;
	}

	/**
	 * @param qualificationWithHighestCourse
	 *            the qualificationWithHighestCourse to set
	 */
	public void setQualificationWithHighestCourse(String qualificationWithHighestCourse) {
		this.qualificationWithHighestCourse = qualificationWithHighestCourse;
	}

	/**
	 * @return the login
	 */
	public User getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(User login) {
		this.login = login;
	}

	/**
	 * @return the jobId
	 */
	public Long getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the corporateId
	 */
	public Long getCorporateId() {
		return corporateId;
	}

	/**
	 * @param corporateId the corporateId to set
	 */
	public void setCorporateId(Long corporateId) {
		this.corporateId = corporateId;
	}
	
	

	/**
	 * @return the matchScore
	 */
	public Double getMatchScore() {
		return matchScore;
	}

	/**
	 * @param matchScore the matchScore to set
	 */
	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CandidateProfileListDTO [firstName=" + firstName + ", lastName=" + lastName + ", tagLine=" + tagLine
				+ ", qualificationWithHighestCourse=" + qualificationWithHighestCourse + ", login=" + login + ", id="
				+ id + ", reviewed=" + reviewed + ", jobId=" + jobId + ", corporateId=" + corporateId + "]";
	}

	
}
