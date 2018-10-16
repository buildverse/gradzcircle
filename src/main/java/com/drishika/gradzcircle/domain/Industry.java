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
 * A Industry.
 */
@Entity
@Table(name = "industry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "industry")
public class Industry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "industry_name")
	private String industryName;

	@OneToMany(mappedBy = "industry")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Corporate> corporates = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndustryName() {
		return industryName;
	}

	public Industry industryName(String industryName) {
		this.industryName = industryName;
		return this;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Set<Corporate> getCorporates() {
		return corporates;
	}

	public Industry corporates(Set<Corporate> corporates) {
		this.corporates = corporates;
		return this;
	}

	public Industry addCorporate(Corporate corporate) {
		this.corporates.add(corporate);
		corporate.setIndustry(this);
		return this;
	}

	public Industry removeCorporate(Corporate corporate) {
		this.corporates.remove(corporate);
		corporate.setIndustry(null);
		return this;
	}

	public void setCorporates(Set<Corporate> corporates) {
		this.corporates = corporates;
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
		Industry industry = (Industry) o;
		if (industry.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), industry.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Industry{" + "id=" + getId() + ", industryName='" + getIndustryName() + "'" + "}";
	}
}
