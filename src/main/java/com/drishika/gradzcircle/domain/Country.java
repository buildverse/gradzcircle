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
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "country")
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "short_code")
	private String shortCode;

	@Column(name = "short_code_three_char")
	private String shortCodeThreeChar;

	@Column(name = "country_nice_name")
	private String countryNiceName;

	@Column(name = "num_code")
	private Integer numCode;

	@Column(name = "phone_code")
	private Integer phoneCode;

	@Column(name = "enabled")
	private Boolean enabled;

	@OneToOne
	@JoinColumn(unique = true)
	private Nationality nationality;

	@OneToMany(mappedBy = "country")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "country")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<University> universities = new HashSet<>();

	@OneToMany(mappedBy = "country")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateEmployment> candidateEmployments = new HashSet<>();

	@OneToMany(mappedBy = "country")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<VisaType> visas = new HashSet<>();

	@OneToMany(mappedBy = "country")
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

	public String getCountryName() {
		return countryName;
	}

	public Country countryName(String countryName) {
		this.countryName = countryName;
		return this;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getShortCode() {
		return shortCode;
	}

	public Country shortCode(String shortCode) {
		this.shortCode = shortCode;
		return this;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getShortCodeThreeChar() {
		return shortCodeThreeChar;
	}

	public Country shortCodeThreeChar(String shortCodeThreeChar) {
		this.shortCodeThreeChar = shortCodeThreeChar;
		return this;
	}

	public void setShortCodeThreeChar(String shortCodeThreeChar) {
		this.shortCodeThreeChar = shortCodeThreeChar;
	}

	public String getCountryNiceName() {
		return countryNiceName;
	}

	public Country countryNiceName(String countryNiceName) {
		this.countryNiceName = countryNiceName;
		return this;
	}

	public void setCountryNiceName(String countryNiceName) {
		this.countryNiceName = countryNiceName;
	}

	public Integer getNumCode() {
		return numCode;
	}

	public Country numCode(Integer numCode) {
		this.numCode = numCode;
		return this;
	}

	public void setNumCode(Integer numCode) {
		this.numCode = numCode;
	}

	public Integer getPhoneCode() {
		return phoneCode;
	}

	public Country phoneCode(Integer phoneCode) {
		this.phoneCode = phoneCode;
		return this;
	}

	public void setPhoneCode(Integer phoneCode) {
		this.phoneCode = phoneCode;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public Country enabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Nationality getNationality() {
		return nationality;
	}

	public Country nationality(Nationality nationality) {
		this.nationality = nationality;
		return this;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public Country addresses(Set<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public Country addAddress(Address address) {
		this.addresses.add(address);
		address.setCountry(this);
		return this;
	}

	public Country removeAddress(Address address) {
		this.addresses.remove(address);
		address.setCountry(null);
		return this;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<University> getUniversities() {
		return universities;
	}

	public Country universities(Set<University> universities) {
		this.universities = universities;
		return this;
	}

	public Country addUniversity(University university) {
		this.universities.add(university);
		university.setCountry(this);
		return this;
	}

	public Country removeUniversity(University university) {
		this.universities.remove(university);
		university.setCountry(null);
		return this;
	}

	public void setUniversities(Set<University> universities) {
		this.universities = universities;
	}

	public Set<CandidateEmployment> getCandidateEmployments() {
		return candidateEmployments;
	}

	public Country candidateEmployments(Set<CandidateEmployment> candidateEmployments) {
		this.candidateEmployments = candidateEmployments;
		return this;
	}

	public Country addCandidateEmployment(CandidateEmployment candidateEmployment) {
		this.candidateEmployments.add(candidateEmployment);
		candidateEmployment.setCountry(this);
		return this;
	}

	public Country removeCandidateEmployment(CandidateEmployment candidateEmployment) {
		this.candidateEmployments.remove(candidateEmployment);
		candidateEmployment.setCountry(null);
		return this;
	}

	public void setCandidateEmployments(Set<CandidateEmployment> candidateEmployments) {
		this.candidateEmployments = candidateEmployments;
	}

	public Set<VisaType> getVisas() {
		return visas;
	}

	public Country visas(Set<VisaType> visaTypes) {
		this.visas = visaTypes;
		return this;
	}

	public Country addVisa(VisaType visaType) {
		this.visas.add(visaType);
		visaType.setCountry(this);
		return this;
	}

	public Country removeVisa(VisaType visaType) {
		this.visas.remove(visaType);
		visaType.setCountry(null);
		return this;
	}

	public void setVisas(Set<VisaType> visaTypes) {
		this.visas = visaTypes;
	}

	public Set<Corporate> getCorporates() {
		return corporates;
	}

	public Country corporates(Set<Corporate> corporates) {
		this.corporates = corporates;
		return this;
	}

	public Country addCorporate(Corporate corporate) {
		this.corporates.add(corporate);
		corporate.setCountry(this);
		return this;
	}

	public Country removeCorporate(Corporate corporate) {
		this.corporates.remove(corporate);
		corporate.setCountry(null);
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
		Country country = (Country) o;
		if (country.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), country.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Country{" + "id=" + getId() + ", countryName='" + getCountryName() + "'" + ", shortCode='"
				+ getShortCode() + "'" + ", shortCodeThreeChar='" + getShortCodeThreeChar() + "'"
				+ ", countryNiceName='" + getCountryNiceName() + "'" + ", numCode='" + getNumCode() + "'"
				+ ", phoneCode='" + getPhoneCode() + "'" + ", enabled='" + isEnabled() + "'" + "}";
	}
}
