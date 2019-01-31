package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A JobFilterHistory.
 */
@Entity
@Table(name = "job_filter_history")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobfilterhistory")
public class JobFilterHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Size(max = 10000)
	@Column(name = "filter_description", length = 10000)
	private String filterDescription;

	@ManyToOne
	private JobFilter jobFilter;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilterDescription() {
		return filterDescription;
	}

	public JobFilterHistory filterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
		return this;
	}

	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}

	public JobFilter getJobFilter() {
		return jobFilter;
	}

	public JobFilterHistory jobFilter(JobFilter jobFilter) {
		this.jobFilter = jobFilter;
		return this;
	}

	public void setJobFilter(JobFilter jobFilter) {
		this.jobFilter = jobFilter;
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
		JobFilterHistory jobFilterHistory = (JobFilterHistory) o;
		if (jobFilterHistory.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), jobFilterHistory.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "JobFilterHistory{" + "id=" + getId() + ", filterDescription='" + getFilterDescription() + "'" + "}";
	}
}
