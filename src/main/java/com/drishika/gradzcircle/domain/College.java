package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

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
    
    @Transient
    @JsonProperty
    private String value;
    
    @Transient
    @JsonProperty
    private String display;

    @Column(name = "affiliation")
    private String affiliation;

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

    public String getAffiliation() {
        return affiliation;
    }

    public College affiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
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

    /**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	public College value(String value) {
        this.value = value;
        return this;
    }
	

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
    public College display(String display) {
        this.display = display;
        return this;
    }


	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
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

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "College{" +
            "id=" + getId() +
            ", collegeName='" + getCollegeName() + "'" +
            ", domainName='" + getDomainName() + "'" +
            ", affiliation='" + getAffiliation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
