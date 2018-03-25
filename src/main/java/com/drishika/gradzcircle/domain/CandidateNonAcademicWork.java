package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CandidateNonAcademicWork.
 */
@Entity
@Table(name = "candidate_non_academic_work")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidatenonacademicwork")
public class CandidateNonAcademicWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "non_academic_initiative_title")
    private String nonAcademicInitiativeTitle;

    @Size(max = 10000)
    @Column(name = "non_academic_initiative_description", length = 10000)
    private String nonAcademicInitiativeDescription;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_current_activity")
    private Boolean isCurrentActivity;

    @Column(name = "role_in_initiative")
    private String roleInInitiative;

    @Column(name = "non_academic_work_start_date")
    private LocalDate nonAcademicWorkStartDate;

    @Column(name = "non_academic_work_end_date")
    private LocalDate nonAcademicWorkEndDate;

    @ManyToOne
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNonAcademicInitiativeTitle() {
        return nonAcademicInitiativeTitle;
    }

    public CandidateNonAcademicWork nonAcademicInitiativeTitle(String nonAcademicInitiativeTitle) {
        this.nonAcademicInitiativeTitle = nonAcademicInitiativeTitle;
        return this;
    }

    public void setNonAcademicInitiativeTitle(String nonAcademicInitiativeTitle) {
        this.nonAcademicInitiativeTitle = nonAcademicInitiativeTitle;
    }

    public String getNonAcademicInitiativeDescription() {
        return nonAcademicInitiativeDescription;
    }

    public CandidateNonAcademicWork nonAcademicInitiativeDescription(String nonAcademicInitiativeDescription) {
        this.nonAcademicInitiativeDescription = nonAcademicInitiativeDescription;
        return this;
    }

    public void setNonAcademicInitiativeDescription(String nonAcademicInitiativeDescription) {
        this.nonAcademicInitiativeDescription = nonAcademicInitiativeDescription;
    }

    public Integer getDuration() {
        return duration;
    }

    public CandidateNonAcademicWork duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean isIsCurrentActivity() {
        return isCurrentActivity;
    }

    public CandidateNonAcademicWork isCurrentActivity(Boolean isCurrentActivity) {
        this.isCurrentActivity = isCurrentActivity;
        return this;
    }

    public void setIsCurrentActivity(Boolean isCurrentActivity) {
        this.isCurrentActivity = isCurrentActivity;
    }

    public String getRoleInInitiative() {
        return roleInInitiative;
    }

    public CandidateNonAcademicWork roleInInitiative(String roleInInitiative) {
        this.roleInInitiative = roleInInitiative;
        return this;
    }

    public void setRoleInInitiative(String roleInInitiative) {
        this.roleInInitiative = roleInInitiative;
    }

    public LocalDate getNonAcademicWorkStartDate() {
        return nonAcademicWorkStartDate;
    }

    public CandidateNonAcademicWork nonAcademicWorkStartDate(LocalDate nonAcademicWorkStartDate) {
        this.nonAcademicWorkStartDate = nonAcademicWorkStartDate;
        return this;
    }

    public void setNonAcademicWorkStartDate(LocalDate nonAcademicWorkStartDate) {
        this.nonAcademicWorkStartDate = nonAcademicWorkStartDate;
    }

    public LocalDate getNonAcademicWorkEndDate() {
        return nonAcademicWorkEndDate;
    }

    public CandidateNonAcademicWork nonAcademicWorkEndDate(LocalDate nonAcademicWorkEndDate) {
        this.nonAcademicWorkEndDate = nonAcademicWorkEndDate;
        return this;
    }

    public void setNonAcademicWorkEndDate(LocalDate nonAcademicWorkEndDate) {
        this.nonAcademicWorkEndDate = nonAcademicWorkEndDate;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public CandidateNonAcademicWork candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
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
        CandidateNonAcademicWork candidateNonAcademicWork = (CandidateNonAcademicWork) o;
        if (candidateNonAcademicWork.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidateNonAcademicWork.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CandidateNonAcademicWork{" +
            "id=" + getId() +
            ", nonAcademicInitiativeTitle='" + getNonAcademicInitiativeTitle() + "'" +
            ", nonAcademicInitiativeDescription='" + getNonAcademicInitiativeDescription() + "'" +
            ", duration='" + getDuration() + "'" +
            ", isCurrentActivity='" + isIsCurrentActivity() + "'" +
            ", roleInInitiative='" + getRoleInInitiative() + "'" +
            ", nonAcademicWorkStartDate='" + getNonAcademicWorkStartDate() + "'" +
            ", nonAcademicWorkEndDate='" + getNonAcademicWorkEndDate() + "'" +
            "}";
    }
}
