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
 * A VisaType.
 */
@Entity
@Table(name = "visa_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "visatype")
public class VisaType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "visa")
	private String visa;

	@OneToMany(mappedBy = "visaType")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Candidate> candidates = new HashSet<>();

	@ManyToOne
	private Country country;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVisa() {
		return visa;
	}

	public VisaType visa(String visa) {
		this.visa = visa;
		return this;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public Set<Candidate> getCandidates() {
		return candidates;
	}

	public VisaType candidates(Set<Candidate> candidates) {
		this.candidates = candidates;
		return this;
	}

	public VisaType addCandidate(Candidate candidate) {
		this.candidates.add(candidate);
		candidate.setVisaType(this);
		return this;
	}

	public VisaType removeCandidate(Candidate candidate) {
		this.candidates.remove(candidate);
		candidate.setVisaType(null);
		return this;
	}

	public void setCandidates(Set<Candidate> candidates) {
		this.candidates = candidates;
	}

	public Country getCountry() {
		return country;
	}

	public VisaType country(Country country) {
		this.country = country;
		return this;
	}

	public void setCountry(Country country) {
		this.country = country;
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
		VisaType visaType = (VisaType) o;
		if (visaType.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), visaType.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "VisaType{" + "id=" + getId() + ", visa='" + getVisa() + "'" + "}";
	}
}
