package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CaptureUniversity.
 */
@Entity
@Table(name = "capture_university")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "captureuniversity")
public class CaptureUniversity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "university_name")
	private String universityName;

	@OneToOne
	@JoinColumn(unique = true)
	private CaptureCollege capturecollege;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUniversityName() {
		return universityName;
	}

	public CaptureUniversity universityName(String universityName) {
		this.universityName = universityName;
		return this;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public CaptureCollege getCapturecollege() {
		return capturecollege;
	}

	public CaptureUniversity capturecollege(CaptureCollege captureCollege) {
		this.capturecollege = captureCollege;
		return this;
	}

	public void setCapturecollege(CaptureCollege captureCollege) {
		this.capturecollege = captureCollege;
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
		CaptureUniversity captureUniversity = (CaptureUniversity) o;
		if (captureUniversity.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), captureUniversity.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CaptureUniversity{" + "id=" + getId() + ", universityName='" + getUniversityName() + "'" + "}";
	}
}
