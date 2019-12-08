package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A CandidateSkills.
 */
@Entity
@Table(name = "candidate_skills")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidateskills")
public class CandidateSkills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private Candidate candidate;

    @ManyToOne
    private Skills skills;
    
    @Transient
    @JsonProperty
    private List<Skills> skillsList = new ArrayList<>();
 
	@Transient
	@JsonProperty
	private String capturedSkills;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public CandidateSkills candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Skills getSkills() {
        return skills;
    }

    public CandidateSkills skills(Skills skills) {
        this.skills = skills;
        return this;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }
    
    
    
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


	/**
	 * @return the skillsList
	 */
	public List<Skills> getSkillsList() {
		return skillsList;
	}

	/**
	 * @param skillsList the skillsList to set
	 */
	public void setSkillsList(List<Skills> skillsList) {
		this.skillsList = skillsList;
	}

	/**
	 * @return the capturedSkills
	 */
	public String getCapturedSkills() {
		return capturedSkills;
	}
	
	 public CandidateSkills capturedSkills(String capturedSkills) {
	        this.capturedSkills = capturedSkills;
	        return this;
	    }

	/**
	 * @param capturedSkills the capturedSkills to set
	 */
	public void setCapturedSkills(String capturedSkills) {
		this.capturedSkills = capturedSkills;
	}
	
	

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CandidateSkills candidateSkills = (CandidateSkills) o;
        if (candidateSkills.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidateSkills.getId());
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
		return "CandidateSkills [id=" + id + ", skills=" + skills + ", skillsList=" + skillsList
				+ ", capturedSkills=" + capturedSkills + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	

   
}
