package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Gender.
 */
@Entity
@Table(name = "gender")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gender implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "gender")
	private String gender;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public Gender gender(String gender) {
		this.gender = gender;
		return this;
	}

	public void setGender(String gender) {
		this.gender = gender;
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
		Gender gender = (Gender) o;
		if (gender.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), gender.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Gender{" + "id=" + getId() + ", gender='" + getGender() + "'" + "}";
	}
}
