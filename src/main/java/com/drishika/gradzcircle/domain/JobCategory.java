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
 * A JobCategory.
 */
@Entity
@Table(name = "job_category")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class JobCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "job_category")
	private String jobCategory;

	@ManyToMany(mappedBy = "jobCategories")
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Candidate> candidates = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public JobCategory jobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
		return this;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public Set<Candidate> getCandidates() {
		return candidates;
	}

	public JobCategory candidates(Set<Candidate> candidates) {
		this.candidates = candidates;
		return this;
	}

	public JobCategory addCandidate(Candidate candidate) {
		this.candidates.add(candidate);
		candidate.getJobCategories().add(this);
		return this;
	}

	public JobCategory removeCandidate(Candidate candidate) {
		this.candidates.remove(candidate);
		candidate.getJobCategories().remove(this);
		return this;
	}

	public void setCandidates(Set<Candidate> candidates) {
		this.candidates = candidates;
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
		JobCategory jobCategory = (JobCategory) o;
		if (jobCategory.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), jobCategory.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "JobCategory{" + "id=" + getId() + ", jobCategory='" + getJobCategory() + "'" + "}";
	}
}
