package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A College.
 */
@Entity
@Table(name = "college")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class College implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "college_name")
    private String collegeName;

    @Column(name = "domain_name")
    private String domainName;

    /**
     * need to make this unique
     */
    @ApiModelProperty(value = "need to make this unique")
    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateEducation> candidateEducations = new HashSet<>();
    
    @ManyToOne
    private University university;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public College collegeName(String collegeName) {
        this.collegeName = collegeName;
        return this;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDomainName() {
        return domainName;
    }

    public College domainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getStatus() {
        return status;
    }

    public College status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<CandidateEducation> getCandidateEducations() {
        return candidateEducations;
    }

    public College candidateEducations(Set<CandidateEducation> candidateEducations) {
        this.candidateEducations = candidateEducations;
        return this;
    }

    public College addCandidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducations.add(candidateEducation);
        candidateEducation.setCollege(this);
        return this;
    }

    public College removeCandidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducations.remove(candidateEducation);
        candidateEducation.setCollege(null);
        return this;
    }

    public void setCandidateEducations(Set<CandidateEducation> candidateEducations) {
        this.candidateEducations = candidateEducations;
    }

    public University getUniversity() {
        return university;
    }

    public College university(University university) {
        this.university = university;
        return this;
    }

    public void setUniversity(University university) {
        this.university = university;
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
        College college = (College) o;
        if (college.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), college.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "College{" +
            "id=" + getId() +
            ", collegeName='" + getCollegeName() + "'" +
            ", domainName='" + getDomainName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
