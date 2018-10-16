package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Corporate.
 */
@Entity
@Table(name = "corporate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "corporate")
public class Corporate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "city")
	private String city;

	@Column(name = "established_since")
	private LocalDate establishedSince;

	@Column(name = "email")
	private String email;

	@Size(max = 10000)
	@Column(name = "overview", length = 10000)
	private String overview;

	@Size(max = 10000)
	@Column(name = "benefits", length = 10000)
	private String benefits;

	@Column(name = "website")
	private String website;

	@Column(name = "facebook")
	private String facebook;

	@Column(name = "twitter")
	private String twitter;

	@Column(name = "instagram")
	private String instagram;

	@Column(name = "linked_in")
	private String linkedIn;

	@Column(name = "culture")
	private String culture;

	@Column(name = "contact_person")
	private String contactPerson;

	@Column(name = "phone")
	private String phone;

	@Column(name = "phone_code")
	private String phoneCode;

	@Column(name = "person_designation")
	private String personDesignation;

	@Column(name = "tag_line")
	private String tagLine;

	@Column(name = "escrow_amount")
	private Double escrowAmount;

	@ManyToOne
	private Country country;

	@ManyToOne
	private Industry industry;

	@OneToOne
	@JoinColumn(unique = true)
	private User login;

	@OneToMany(mappedBy = "corporate", cascade = CascadeType.ALL)
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Job> jobs = new HashSet<>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "candidateToCorporate")
	private Set<CorporateCandidate> shortlistedCandidates = new HashSet<CorporateCandidate>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Corporate name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public Corporate address(String address) {
		this.address = address;
		return this;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public Corporate city(String city) {
		this.city = city;
		return this;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getEstablishedSince() {
		return establishedSince;
	}

	public Corporate establishedSince(LocalDate establishedSince) {
		this.establishedSince = establishedSince;
		return this;
	}

	public void setEstablishedSince(LocalDate establishedSince) {
		this.establishedSince = establishedSince;
	}

	public String getEmail() {
		return email;
	}

	public Corporate email(String email) {
		this.email = email;
		return this;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOverview() {
		return overview;
	}

	public Corporate overview(String overview) {
		this.overview = overview;
		return this;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getBenefits() {
		return benefits;
	}

	public Corporate benefits(String benefits) {
		this.benefits = benefits;
		return this;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	public String getWebsite() {
		return website;
	}

	public Corporate website(String website) {
		this.website = website;
		return this;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFacebook() {
		return facebook;
	}

	public Corporate facebook(String facebook) {
		this.facebook = facebook;
		return this;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public Corporate twitter(String twitter) {
		this.twitter = twitter;
		return this;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getInstagram() {
		return instagram;
	}

	public Corporate instagram(String instagram) {
		this.instagram = instagram;
		return this;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getLinkedIn() {
		return linkedIn;
	}

	public Corporate linkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
		return this;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	public String getCulture() {
		return culture;
	}

	public Corporate culture(String culture) {
		this.culture = culture;
		return this;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public Corporate contactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
		return this;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhone() {
		return phone;
	}

	public Corporate phone(String phone) {
		this.phone = phone;
		return this;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public Corporate phoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
		return this;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPersonDesignation() {
		return personDesignation;
	}

	public Corporate personDesignation(String personDesignation) {
		this.personDesignation = personDesignation;
		return this;
	}

	public void setPersonDesignation(String personDesignation) {
		this.personDesignation = personDesignation;
	}

	public String getTagLine() {
		return tagLine;
	}

	public Corporate tagLine(String tagLine) {
		this.tagLine = tagLine;
		return this;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public Double getEscrowAmount() {
		return escrowAmount;
	}

	public Corporate escrowAmount(Double escrowAmount) {
		this.escrowAmount = escrowAmount;
		return this;
	}

	public void setEscrowAmount(Double escrowAmount) {
		this.escrowAmount = escrowAmount;
	}

	public Country getCountry() {
		return country;
	}

	public Corporate country(Country country) {
		this.country = country;
		return this;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Industry getIndustry() {
		return industry;
	}

	public Corporate industry(Industry industry) {
		this.industry = industry;
		return this;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	public User getLogin() {
		return login;
	}

	public Corporate login(User user) {
		this.login = user;
		return this;
	}

	public void setLogin(User user) {
		this.login = user;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public Corporate jobs(Set<Job> jobs) {
		this.jobs = jobs;
		return this;
	}

	public Corporate addJob(Job job) {
		this.jobs.add(job);
		job.setCorporate(this);
		return this;
	}

	public Corporate removeJob(Job job) {
		this.jobs.remove(job);
		job.setCorporate(null);
		return this;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	/**
	 * @return the shortlistedCandidates
	 */
	public Set<CorporateCandidate> getShortlistedCandidates() {
		return shortlistedCandidates;
	}

	/**
	 * @param shortlistedCandidates
	 *            the shortlistedCandidates to set
	 */
	public void setShortlistedCandidates(Set<CorporateCandidate> shortlistedCandidates) {
		this.shortlistedCandidates = shortlistedCandidates;
	}

	public Corporate addCorporateCandidate(CorporateCandidate corporateCandidate) {
		this.shortlistedCandidates.add(corporateCandidate);
		corporateCandidate.setCorporate(this);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Corporate corporate = (Corporate) o;
		if (corporate.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), corporate.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Corporate{" + "id=" + getId() + ", name='" + getName() + "'" + ", address='" + getAddress() + "'"
				+ ", city='" + getCity() + "'" + ", establishedSince='" + getEstablishedSince() + "'" + ", email='"
				+ getEmail() + "'" + ", overview='" + getOverview() + "'" + ", benefits='" + getBenefits() + "'"
				+ ", website='" + getWebsite() + "'" + ", facebook='" + getFacebook() + "'" + ", twitter='"
				+ getTwitter() + "'" + ", instagram='" + getInstagram() + "'" + ", linkedIn='" + getLinkedIn() + "'"
				+ ", culture='" + getCulture() + "'" + ", contactPerson='" + getContactPerson() + "'" + ", phone='"
				+ getPhone() + "'" + ", phoneCode='" + getPhoneCode() + "'" + ", personDesignation='"
				+ getPersonDesignation() + "'" + ", tagLine='" + getTagLine() + "'" + ", escrowAmount='"
				+ getEscrowAmount() + "'" + "}";
	}
}
