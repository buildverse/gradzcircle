package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CaptureQualification.
 */
@Entity
@Table(name = "capture_qualification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "capturequalification")
public class CaptureQualification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "qualification_name")
	private String qualificationName;

	@OneToOne
	@JoinColumn(unique = true)
	private CandidateEducation candidateEducation;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQualificationName() {
		return qualificationName;
	}

	public CaptureQualification qualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
		return this;
	}

	public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
	}

	public CandidateEducation getCandidateEducation() {
		return candidateEducation;
	}

	public CaptureQualification candidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducation = candidateEducation;
		return this;
	}

	public void setCandidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducation = candidateEducation;
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
		CaptureQualification captureQualification = (CaptureQualification) o;
		if (captureQualification.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), captureQualification.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CaptureQualification{" + "id=" + getId() + ", qualificationName='" + getQualificationName() + "'" + "}";
	}
}
