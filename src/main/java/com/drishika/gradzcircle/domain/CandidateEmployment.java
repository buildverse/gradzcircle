package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CandidateEmployment.
 */
@Entity
@Table(name = "candidate_employment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidateemployment")
public class CandidateEmployment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "location")
    private Integer location;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "employer_name")
    private String employerName;

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Column(name = "employment_end_date")
    private LocalDate employmentEndDate;

    @Column(name = "employment_duration")
    private Integer employmentDuration;

    @Column(name = "is_current_employment")
    private Boolean isCurrentEmployment;

    @Column(name = "job_description")
    private String jobDescription;

    @ManyToOne
    private Candidate candidate;

    @OneToMany(mappedBy = "employment", cascade = CascadeType.REMOVE)

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateProject> projects = new HashSet<>();

    @ManyToOne
    private EmploymentType employmentType;

    @ManyToOne
    private Country country;

    @ManyToOne
    private JobType jobType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLocation() {
        return location;
    }

    public CandidateEmployment location(Integer location) {
        this.location = location;
        return this;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public CandidateEmployment jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmployerName() {
        return employerName;
    }

    public CandidateEmployment employerName(String employerName) {
        this.employerName = employerName;
        return this;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public CandidateEmployment employmentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
        return this;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public CandidateEmployment employmentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
        return this;
    }

    public void setEmploymentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public Integer getEmploymentDuration() {
        return employmentDuration;
    }

    public CandidateEmployment employmentDuration(Integer employmentDuration) {
        this.employmentDuration = employmentDuration;
        return this;
    }

    public void setEmploymentDuration(Integer employmentDuration) {
        this.employmentDuration = employmentDuration;
    }

    public Boolean isIsCurrentEmployment() {
        return isCurrentEmployment;
    }

    public CandidateEmployment isCurrentEmployment(Boolean isCurrentEmployment) {
        this.isCurrentEmployment = isCurrentEmployment;
        return this;
    }

    public void setIsCurrentEmployment(Boolean isCurrentEmployment) {
        this.isCurrentEmployment = isCurrentEmployment;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public CandidateEmployment jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public CandidateEmployment candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Set<CandidateProject> getProjects() {
        return projects;
    }

    public CandidateEmployment projects(Set<CandidateProject> candidateProjects) {
        this.projects = candidateProjects;
        return this;
    }

    public CandidateEmployment addProjects(CandidateProject candidateProject) {
        this.projects.add(candidateProject);
        candidateProject.setEmployment(this);
        return this;
    }

    public CandidateEmployment removeProjects(CandidateProject candidateProject) {
        this.projects.remove(candidateProject);
        candidateProject.setEmployment(null);
        return this;
    }

    public void setProjects(Set<CandidateProject> candidateProjects) {
        this.projects = candidateProjects;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public CandidateEmployment employmentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
        return this;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Country getCountry() {
        return country;
    }

    public CandidateEmployment country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public JobType getJobType() {
        return jobType;
    }

    public CandidateEmployment jobType(JobType jobType) {
        this.jobType = jobType;
        return this;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CandidateEmployment candidateEmployment = (CandidateEmployment) o;
        if (candidateEmployment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidateEmployment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CandidateEmployment{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", employerName='" + getEmployerName() + "'" +
            ", employmentStartDate='" + getEmploymentStartDate() + "'" +
            ", employmentEndDate='" + getEmploymentEndDate() + "'" +
            ", employmentDuration='" + getEmploymentDuration() + "'" +
            ", isCurrentEmployment='" + isIsCurrentEmployment() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            "}";
    }
}
