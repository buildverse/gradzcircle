package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.drishika.gradzcircle.domain.enumeration.ProjectType;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * A CandidateProject.
 */
@Entity
@Table(name = "candidate_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidateproject")
public class CandidateProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "project_title")
	private String projectTitle;

	@Column(name = "project_start_date")
	private LocalDate projectStartDate;

	@Column(name = "project_end_date")
	private LocalDate projectEndDate;

	@Size(max = 10000)
	@Column(name = "project_description", length = 10000)
	private String projectDescription;

	@Column(name = "project_duration")
	private Integer projectDuration;

	@Column(name = "contribution_in_project")
	private String contributionInProject;

	@Column(name = "is_current_project")
	private Boolean isCurrentProject;

	@Enumerated(EnumType.STRING)
	@Column(name = "project_type")
	private ProjectType projectType;

	@ManyToOne
	private CandidateEducation education;

	@ManyToOne
	@JsonBackReference
	private CandidateEmployment employment;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public CandidateProject projectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
		return this;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}

	public CandidateProject projectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
		return this;
	}

	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}

	public CandidateProject projectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
		return this;
	}

	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public CandidateProject projectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
		return this;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public Integer getProjectDuration() {
		return projectDuration;
	}

	public CandidateProject projectDuration(Integer projectDuration) {
		this.projectDuration = projectDuration;
		return this;
	}

	public void setProjectDuration(Integer projectDuration) {
		this.projectDuration = projectDuration;
	}

	public String getContributionInProject() {
		return contributionInProject;
	}

	public CandidateProject contributionInProject(String contributionInProject) {
		this.contributionInProject = contributionInProject;
		return this;
	}

	public void setContributionInProject(String contributionInProject) {
		this.contributionInProject = contributionInProject;
	}

	public Boolean isIsCurrentProject() {
		return isCurrentProject;
	}

	public CandidateProject isCurrentProject(Boolean isCurrentProject) {
		this.isCurrentProject = isCurrentProject;
		return this;
	}

	public void setIsCurrentProject(Boolean isCurrentProject) {
		this.isCurrentProject = isCurrentProject;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public CandidateProject projectType(ProjectType projectType) {
		this.projectType = projectType;
		return this;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public CandidateEducation getEducation() {
		return education;
	}

	public CandidateProject education(CandidateEducation candidateEducation) {
		this.education = candidateEducation;
		return this;
	}

	public void setEducation(CandidateEducation candidateEducation) {
		this.education = candidateEducation;
	}

	public CandidateEmployment getEmployment() {
		return employment;
	}

	public CandidateProject employment(CandidateEmployment candidateEmployment) {
		this.employment = candidateEmployment;
		return this;
	}

	public void setEmployment(CandidateEmployment candidateEmployment) {
		this.employment = candidateEmployment;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CandidateProject candidateProject = (CandidateProject) o;
		if (candidateProject.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), candidateProject.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CandidateProject{" + "id=" + getId() + ", projectTitle='" + getProjectTitle() + "'"
				+ ", projectStartDate='" + getProjectStartDate() + "'" + ", projectEndDate='" + getProjectEndDate()
				+ "'" + ", projectDescription='" + getProjectDescription() + "'" + ", projectDuration='"
				+ getProjectDuration() + "'" + ", contributionInProject='" + getContributionInProject() + "'"
				+ ", isCurrentProject='" + isIsCurrentProject() + "'" + ", projectType='" + getProjectType() + "'"
				+ "}";
	}
}
