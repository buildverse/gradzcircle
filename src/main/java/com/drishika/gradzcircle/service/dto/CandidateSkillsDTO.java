/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class CandidateSkillsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String skillName;

	private CandidateDTO candidate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	

	/**
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName;
	}

	/**
	 * @param skillName the skillName to set
	 */
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	/**
	 * @return the candidate
	 */
	public CandidateDTO getCandidate() {
		return candidate;
	}

	/**
	 * @param candidate the candidate to set
	 */
	public void setCandidate(CandidateDTO candidate) {
		this.candidate = candidate;
	}

	

}
