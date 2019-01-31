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
 * A Course.
 */
@Entity
@Table(name = "course")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "course")
	private String course;

	@Transient
	@JsonProperty
	private String value;

	@Transient
	@JsonProperty
	private String display;

	@OneToMany(mappedBy = "course")
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateEducation> candidateEducations = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourse() {
		return course;
	}

	public Course course(String course) {
		this.course = course;
		return this;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Set<CandidateEducation> getCandidateEducations() {
		return candidateEducations;
	}

	public Course candidateEducations(Set<CandidateEducation> candidateEducations) {
		this.candidateEducations = candidateEducations;
		return this;
	}

	public Course addCandidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducations.add(candidateEducation);
		candidateEducation.setCourse(this);
		return this;
	}

	public Course removeCandidateEducation(CandidateEducation candidateEducation) {
		this.candidateEducations.remove(candidateEducation);
		candidateEducation.setCourse(null);
		return this;
	}

	public void setCandidateEducations(Set<CandidateEducation> candidateEducations) {
		this.candidateEducations = candidateEducations;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public Course display(String display) {
		this.display = display;
		return this;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	public Course value(String value) {
		this.value = value;
		return this;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Course course = (Course) o;
		if (course.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), course.getId());
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
		return "Course [id=" + id + ", course=" + course + ", value=" + value + ", display=" + display + "]";
	}
}
