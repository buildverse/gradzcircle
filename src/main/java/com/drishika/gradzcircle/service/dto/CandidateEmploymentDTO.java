/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author abhinav
 *
 */
public class CandidateEmploymentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer location;
	
	private Long id;

	private String jobTitle;

	private String employerName;

	private LocalDate employmentStartDate;

	private LocalDate employmentEndDate;

	private Integer employmentDuration;

	private Boolean isCurrentEmployment;

	private String jobDescription;

	private String employmentType;

	private String jobType;
	
	private CandidateDTO candidate;

	private List<CandidateProjectDTO> projects = new ArrayList<>();

	/**
	 * @return the location
	 */
	public Integer getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Integer location) {
		this.location = location;
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
	 * @return the employerName
	 */
	public String getEmployerName() {
		return employerName;
	}

	/**
	 * @param employerName
	 *            the employerName to set
	 */
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	/**
	 * @return the employmentStartDate
	 */
	public LocalDate getEmploymentStartDate() {
		return employmentStartDate;
	}

	/**
	 * @param employmentStartDate
	 *            the employmentStartDate to set
	 */
	public void setEmploymentStartDate(LocalDate employmentStartDate) {
		this.employmentStartDate = employmentStartDate;
	}

	/**
	 * @return the employmentEndDate
	 */
	public LocalDate getEmploymentEndDate() {
		return employmentEndDate;
	}

	/**
	 * @param employmentEndDate
	 *            the employmentEndDate to set
	 */
	public void setEmploymentEndDate(LocalDate employmentEndDate) {
		this.employmentEndDate = employmentEndDate;
	}

	/**
	 * @return the employmentDuration
	 */
	public Integer getEmploymentDuration() {
		return employmentDuration;
	}

	/**
	 * @param employmentDuration
	 *            the employmentDuration to set
	 */
	public void setEmploymentDuration(Integer employmentDuration) {
		this.employmentDuration = employmentDuration;
	}

	/**
	 * @return the isCurrentEmployment
	 */
	public Boolean getIsCurrentEmployment() {
		return isCurrentEmployment;
	}

	/**
	 * @param isCurrentEmployment
	 *            the isCurrentEmployment to set
	 */
	public void setIsCurrentEmployment(Boolean isCurrentEmployment) {
		this.isCurrentEmployment = isCurrentEmployment;
	}

	/**
	 * @return the jobDescription
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/**
	 * @param jobDescription
	 *            the jobDescription to set
	 */
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	/**
	 * @return the employmentType
	 */
	public String getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType
	 *            the employmentType to set
	 */
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * @return the jobType
	 */
	public String getJobType() {
		return jobType;
	}

	/**
	 * @param jobType
	 *            the jobType to set
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	

	/**
	 * @return the projects
	 */
	public List<CandidateProjectDTO> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<CandidateProjectDTO> projects) {
		this.projects = projects;
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
	
	

}
