package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Audit.
 */
@Entity
@Table(name = "audit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "audit")
public class Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_time")
	private ZonedDateTime createdTime;

	@Column(name = "updated_time")
	private ZonedDateTime updatedTime;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Audit createdBy(Integer createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Audit updatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public ZonedDateTime getCreatedTime() {
		return createdTime;
	}

	public Audit createdTime(ZonedDateTime createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public void setCreatedTime(ZonedDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public ZonedDateTime getUpdatedTime() {
		return updatedTime;
	}

	public Audit updatedTime(ZonedDateTime updatedTime) {
		this.updatedTime = updatedTime;
		return this;
	}

	public void setUpdatedTime(ZonedDateTime updatedTime) {
		this.updatedTime = updatedTime;
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
		Audit audit = (Audit) o;
		if (audit.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), audit.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Audit{" + "id=" + getId() + ", createdBy='" + getCreatedBy() + "'" + ", updatedBy='" + getUpdatedBy()
				+ "'" + ", createdTime='" + getCreatedTime() + "'" + ", updatedTime='" + getUpdatedTime() + "'" + "}";
	}
}
