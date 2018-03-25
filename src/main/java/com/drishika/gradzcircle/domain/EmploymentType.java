package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EmploymentType.
 */
@Entity
@Table(name = "employment_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employmenttype")
public class EmploymentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "employment_type_cost")
    private Double employmentTypeCost;

    @OneToMany(mappedBy = "employmentType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateEmployment> candidateEmployments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public EmploymentType employmentType(String employmentType) {
        this.employmentType = employmentType;
        return this;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Double getEmploymentTypeCost() {
        return employmentTypeCost;
    }

    public EmploymentType employmentTypeCost(Double employmentTypeCost) {
        this.employmentTypeCost = employmentTypeCost;
        return this;
    }

    public void setEmploymentTypeCost(Double employmentTypeCost) {
        this.employmentTypeCost = employmentTypeCost;
    }

    public Set<CandidateEmployment> getCandidateEmployments() {
        return candidateEmployments;
    }

    public EmploymentType candidateEmployments(Set<CandidateEmployment> candidateEmployments) {
        this.candidateEmployments = candidateEmployments;
        return this;
    }

    public EmploymentType addCandidateEmployment(CandidateEmployment candidateEmployment) {
        this.candidateEmployments.add(candidateEmployment);
        candidateEmployment.setEmploymentType(this);
        return this;
    }

    public EmploymentType removeCandidateEmployment(CandidateEmployment candidateEmployment) {
        this.candidateEmployments.remove(candidateEmployment);
        candidateEmployment.setEmploymentType(null);
        return this;
    }

    public void setCandidateEmployments(Set<CandidateEmployment> candidateEmployments) {
        this.candidateEmployments = candidateEmployments;
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
        EmploymentType employmentType = (EmploymentType) o;
        if (employmentType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employmentType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmploymentType{" +
            "id=" + getId() +
            ", employmentType='" + getEmploymentType() + "'" +
            ", employmentTypeCost='" + getEmploymentTypeCost() + "'" +
            "}";
    }
}
