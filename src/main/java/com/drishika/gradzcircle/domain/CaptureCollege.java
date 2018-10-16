package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CaptureCollege.
 */
@Entity
@Table(name = "capture_college")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "capturecollege")
public class CaptureCollege implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "college_name")
	private String collegeName;

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

	public String getCollegeName() {
		return collegeName;
	}

	public CaptureCollege collegeName(String collegeName) {
		this.collegeName = collegeName;
		return this;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public CandidateEducation getCandidateEducation() {
		return candidateEducation;
	}

	public CaptureCollege candidateEducation(CandidateEducation candidateEducation) {
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
		CaptureCollege captureCollege = (CaptureCollege) o;
		if (captureCollege.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), captureCollege.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CaptureCollege{" + "id=" + getId() + ", collegeName='" + getCollegeName() + "'" + "}";
	}
}
