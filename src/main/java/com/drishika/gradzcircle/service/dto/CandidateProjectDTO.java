/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.drishika.gradzcircle.domain.enumeration.ProjectType;

/**
 * @author abhinav
 *
 */
public class CandidateProjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectTitle;

	private LocalDate projectStartDate;

	private LocalDate projectEndDate;

	private String projectDescription;

	private Integer projectDuration;

	private String contributionInProject;

	private Boolean isCurrentProject;

	private ProjectType projectType;

	/**
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}

	/**
	 * @param projectTitle
	 *            the projectTitle to set
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	/**
	 * @return the projectStartDate
	 */
	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}

	/**
	 * @param projectStartDate
	 *            the projectStartDate to set
	 */
	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	/**
	 * @return the projectEndDate
	 */
	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}

	/**
	 * @param projectEndDate
	 *            the projectEndDate to set
	 */
	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription
	 *            the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @return the projectDuration
	 */
	public Integer getProjectDuration() {
		return projectDuration;
	}

	/**
	 * @param projectDuration
	 *            the projectDuration to set
	 */
	public void setProjectDuration(Integer projectDuration) {
		this.projectDuration = projectDuration;
	}

	/**
	 * @return the contributionInProject
	 */
	public String getContributionInProject() {
		return contributionInProject;
	}

	/**
	 * @param contributionInProject
	 *            the contributionInProject to set
	 */
	public void setContributionInProject(String contributionInProject) {
		this.contributionInProject = contributionInProject;
	}

	/**
	 * @return the isCurrentProject
	 */
	public Boolean getIsCurrentProject() {
		return isCurrentProject;
	}

	/**
	 * @param isCurrentProject
	 *            the isCurrentProject to set
	 */
	public void setIsCurrentProject(Boolean isCurrentProject) {
		this.isCurrentProject = isCurrentProject;
	}

	/**
	 * @return the projectType
	 */
	public ProjectType getProjectType() {
		return projectType;
	}

	/**
	 * @param projectType
	 *            the projectType to set
	 */
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

}
