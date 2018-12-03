package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Nationality.
 */
@Entity
@Table(name = "nationality")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class Nationality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "nationality")
	private String nationality;
	
	@Transient
	@JsonProperty
	private String value;

	@Transient
	@JsonProperty
	private String display;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNationality() {
		return nationality;
	}

	public Nationality nationality(String nationality) {
		this.nationality = nationality;
		return this;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
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
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
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
		Nationality nationality = (Nationality) o;
		if (nationality.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), nationality.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Nationality{" + "id=" + getId() + ", nationality='" + getNationality() + "'" + "}";
	}
}
