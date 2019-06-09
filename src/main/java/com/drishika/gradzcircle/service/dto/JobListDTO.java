/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class JobListDTO implements Serializable {
	
	private String jobTitle;
	private Double matchScore;
	
	
	
	
	/**
	 * @param jobTitle
	 * @param matchScore
	 */
	public JobListDTO(String jobTitle, Double matchScore) {
		this.jobTitle = jobTitle;
		this.matchScore = matchScore;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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
	
	

}
