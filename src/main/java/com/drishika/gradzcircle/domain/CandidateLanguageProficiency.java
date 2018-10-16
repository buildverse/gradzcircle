package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CandidateLanguageProficiency.
 */
@Entity
@Table(name = "candidate_language_proficiency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidatelanguageproficiency")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CandidateLanguageProficiency implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "proficiency")
	private String proficiency;

	@ManyToOne
	private Candidate candidate;

	@ManyToOne
	private Language language;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProficiency() {
		return proficiency;
	}

	public CandidateLanguageProficiency proficiency(String proficiency) {
		this.proficiency = proficiency;
		return this;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public CandidateLanguageProficiency candidate(Candidate candidate) {
		this.candidate = candidate;
		return this;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Language getLanguage() {
		return language;
	}

	public CandidateLanguageProficiency language(Language language) {
		this.language = language;
		return this;
	}

	public void setLanguage(Language language) {
		this.language = language;
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
		CandidateLanguageProficiency candidateLanguageProficiency = (CandidateLanguageProficiency) o;
		if (candidateLanguageProficiency.getId() == null || getId() == null) {
			return false;
		}

		return Objects.equals(getId(), candidateLanguageProficiency.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CandidateLanguageProficiency{" + "id=" + getId() + ", proficiency='" + getProficiency() + "'" + "}";
	}
}
