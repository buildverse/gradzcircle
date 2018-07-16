package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Qualification.
 */
@Entity
@Table(name = "qualification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class Qualification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "qualification")
	private String qualification;

	@Transient
	@JsonProperty
	private String value;

	@Transient
	@JsonProperty
	private String display;

	@OneToMany(mappedBy = "qualification")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateEducation> candidateEducations = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQualification() {
		return qualification;
	}

	public Qualification qualification(String qualification) {
		this.qualification = qualification;
		return this;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Set<CandidateEducation> getCandidateEducations() {
		return candidateEducations;
	}

	public Qualification candidateEducations(Set<CandidateEducation> candidateEducations) {
		this.candidateEducations = candidateEducations;
		return this;
	}

	public Qualification addCandidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducations.add(candidateEducation);
		candidateEducation.setQualification(this);
		return this;
	}

	public Qualification removeCandidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducations.remove(candidateEducation);
		candidateEducation.setQualification(null);
		return this;
	}

	public void setCandidateEducations(Set<CandidateEducation> candidateEducations) {
		this.candidateEducations = candidateEducations;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public Qualification value(String value) {
		this.value = value;
		return this;
	}

	/**
	 * @param value
	 *            the value to set
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

	public Qualification display(String display) {
		this.display = display;
		return this;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
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
		Qualification qualification = (Qualification) o;
		if (qualification.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), qualification.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Qualification [id=" + id + ", qualification=" + qualification + ", value=" + value + ", display="
				+ display + "]";
	}
}
