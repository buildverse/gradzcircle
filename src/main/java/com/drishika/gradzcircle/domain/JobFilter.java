package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.drishika.gradzcircle.web.rest.util.CustomJobFilterDeserializer;
import com.drishika.gradzcircle.web.rest.util.CustomeJobFilterSerialize;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A JobFilter.
 */
@Entity
@Table(name = "job_filter")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobfilter")
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
// property = "id")
public class JobFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Size(max = 10000)
	@Column(name = "filter_description", length = 10000)
	@JsonDeserialize(using = CustomJobFilterDeserializer.class)
	@JsonSerialize(using = CustomeJobFilterSerialize.class)
	private String filterDescription;

	@OneToMany(mappedBy = "jobFilter", cascade = CascadeType.ALL)
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<JobFilterHistory> histories = new HashSet<>();

	@ManyToOne
	@JsonBackReference
	private Job job;

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

	public JobFilter filterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
		return this;
	}

	public void setFilterDescription(String filterDescription) {
		this.filterDescription = filterDescription;
	}

	public Set<JobFilterHistory> getHistories() {
		return histories;
	}

	public JobFilter histories(Set<JobFilterHistory> jobFilterHistories) {
		this.histories = jobFilterHistories;
		return this;
	}

	public JobFilter addHistory(JobFilterHistory jobFilterHistory) {
		this.histories.add(jobFilterHistory);
		jobFilterHistory.setJobFilter(this);
		return this;
	}

	public JobFilter removeHistory(JobFilterHistory jobFilterHistory) {
		this.histories.remove(jobFilterHistory);
		jobFilterHistory.setJobFilter(null);
		return this;
	}

	public void setHistories(Set<JobFilterHistory> jobFilterHistories) {
		this.histories = jobFilterHistories;
	}

	public Job getJob() {
		return job;
	}

	public JobFilter job(Job job) {
		this.job = job;
		return this;
	}

	public void setJob(Job job) {
		this.job = job;
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
		JobFilter jobFilter = (JobFilter) o;
		if (jobFilter.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), jobFilter.getId())
				&& Objects.equals(getFilterDescription(), jobFilter.getFilterDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "JobFilter{" + "id=" + getId() + ", filterDescription='" + getFilterDescription() + "'" + "} and job is "
				+ getJob();
	}
}
