/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author abhinav
 *
 */
public class CandidateNonAcademicWorkDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nonAcademicInitiativeTitle;

	private String nonAcademicInitiativeDescription;

	private Integer duration;

	private Boolean isCurrentActivity;

	private String roleInInitiative;

	private LocalDate nonAcademicWorkStartDate;

	private LocalDate nonAcademicWorkEndDate;

	/**
	 * @return the nonAcademicInitiativeTitle
	 */
	public String getNonAcademicInitiativeTitle() {
		return nonAcademicInitiativeTitle;
	}

	/**
	 * @param nonAcademicInitiativeTitle
	 *            the nonAcademicInitiativeTitle to set
	 */
	public void setNonAcademicInitiativeTitle(String nonAcademicInitiativeTitle) {
		this.nonAcademicInitiativeTitle = nonAcademicInitiativeTitle;
	}

	/**
	 * @return the nonAcademicInitiativeDescription
	 */
	public String getNonAcademicInitiativeDescription() {
		return nonAcademicInitiativeDescription;
	}

	/**
	 * @param nonAcademicInitiativeDescription
	 *            the nonAcademicInitiativeDescription to set
	 */
	public void setNonAcademicInitiativeDescription(String nonAcademicInitiativeDescription) {
		this.nonAcademicInitiativeDescription = nonAcademicInitiativeDescription;
	}

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the isCurrentActivity
	 */
	public Boolean getIsCurrentActivity() {
		return isCurrentActivity;
	}

	/**
	 * @param isCurrentActivity
	 *            the isCurrentActivity to set
	 */
	public void setIsCurrentActivity(Boolean isCurrentActivity) {
		this.isCurrentActivity = isCurrentActivity;
	}

	/**
	 * @return the roleInInitiative
	 */
	public String getRoleInInitiative() {
		return roleInInitiative;
	}

	/**
	 * @param roleInInitiative
	 *            the roleInInitiative to set
	 */
	public void setRoleInInitiative(String roleInInitiative) {
		this.roleInInitiative = roleInInitiative;
	}

	/**
	 * @return the nonAcademicWorkStartDate
	 */
	public LocalDate getNonAcademicWorkStartDate() {
		return nonAcademicWorkStartDate;
	}

	/**
	 * @param nonAcademicWorkStartDate
	 *            the nonAcademicWorkStartDate to set
	 */
	public void setNonAcademicWorkStartDate(LocalDate nonAcademicWorkStartDate) {
		this.nonAcademicWorkStartDate = nonAcademicWorkStartDate;
	}

	/**
	 * @return the nonAcademicWorkEndDate
	 */
	public LocalDate getNonAcademicWorkEndDate() {
		return nonAcademicWorkEndDate;
	}

	/**
	 * @param nonAcademicWorkEndDate
	 *            the nonAcademicWorkEndDate to set
	 */
	public void setNonAcademicWorkEndDate(LocalDate nonAcademicWorkEndDate) {
		this.nonAcademicWorkEndDate = nonAcademicWorkEndDate;
	}

}
