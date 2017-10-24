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
 * A JobType.
 */
@Entity
@Table(name = "job_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobtype")
public class JobType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "job_type")
    private String jobType;

    @OneToMany(mappedBy = "jobType")
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

    public String getJobType() {
        return jobType;
    }

    public JobType jobType(String jobType) {
        this.jobType = jobType;
        return this;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Set<CandidateEmployment> getCandidateEmployments() {
        return candidateEmployments;
    }

    public JobType candidateEmployments(Set<CandidateEmployment> candidateEmployments) {
        this.candidateEmployments = candidateEmployments;
        return this;
    }

    public JobType addCandidateEmployment(CandidateEmployment candidateEmployment) {
        this.candidateEmployments.add(candidateEmployment);
        candidateEmployment.setJobType(this);
        return this;
    }

    public JobType removeCandidateEmployment(CandidateEmployment candidateEmployment) {
        this.candidateEmployments.remove(candidateEmployment);
        candidateEmployment.setJobType(null);
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
        JobType jobType = (JobType) o;
        if (jobType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobType{" +
            "id=" + getId() +
            ", jobType='" + getJobType() + "'" +
            "}";
    }
}
