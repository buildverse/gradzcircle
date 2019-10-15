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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * A Skills.
 */
@Entity
@Table(name = "skills")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Skills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "skill")
    private String skill;
    
    @Transient
    @JsonProperty
    private String value;
    
    @Transient
    @JsonProperty
    private String display;
    
    @OneToMany(mappedBy = "skills")
  //  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<CandidateSkills> candidateSkills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public Skills skill(String skill) {
        this.skill = skill;
        return this;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Set<CandidateSkills> getCandidateSkills() {
        return candidateSkills;
    }

    public Skills candidateSkills(Set<CandidateSkills> candidateSkills) {
        this.candidateSkills = candidateSkills;
        return this;
    }

    public Skills addCandidateSkill(CandidateSkills candidateSkills) {
        this.candidateSkills.add(candidateSkills);
        candidateSkills.setSkills(this);
        return this;
    }

    public Skills removeCandidateSkill(CandidateSkills candidateSkills) {
        this.candidateSkills.remove(candidateSkills);
        candidateSkills.setSkills(null);
        return this;
    }

    public void setCandidateSkills(Set<CandidateSkills> candidateSkills) {
        this.candidateSkills = candidateSkills;
    }
    
    /**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
        Skills skills = (Skills) o;
        if (skills.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), skills.getId());
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
		return "Skills [id=" + id + ", skill=" + skill;
	}
}
