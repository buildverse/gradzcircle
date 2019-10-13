package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidate")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Candidate.class)
public class Candidate implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "facebook")
	private String facebook;

	@Column(name = "linked_in")
	private String linkedIn;

	@Column(name = "twitter")
	private String twitter;

	@Column(name = "about_me")
	private String aboutMe;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "phone_code")
	private String phoneCode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "differently_abled")
	private Boolean differentlyAbled;

	@Column(name = "available_for_hiring")
	private Boolean availableForHiring;

	@Column(name = "open_to_relocate")
	private Boolean openToRelocate;

	@Column(name = "match_eligible")
	private Boolean matchEligible;
	
	@Column(name="profile_score")
	private Double profileScore;
	

	/*
	 * @Transient
	 * 
	 * @JsonProperty private Job job;
	 * 
	 * @Transient
	 * 
	 * @JsonProperty private Corporate corporate;
	 */
	@OneToOne
	@JoinColumn(unique = true)
	private User login;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
//	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	// @JsonManagedReference
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	// @JsonManagedReference
	private Set<CandidateEducation> educations = new HashSet<>();

	@OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
//	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateNonAcademicWork> nonAcademics = new HashSet<>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateCertification> certifications = new HashSet<>();

	@OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateEmployment> employments = new HashSet<>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<>();
	
    @OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateSkills> candidateSkills = new HashSet<>();

	@ManyToOne
	private Nationality nationality;

	@ManyToOne
	private Gender gender;

	@ManyToOne
	private MaritalStatus maritalStatus;

	@ManyToMany
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinTable(name = "candidate_job_category", joinColumns = @JoinColumn(name = "candidates_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "job_categories_id", referencedColumnName = "id"))
	private Set<JobCategory> jobCategories = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinTable(name = "candidate_applied_jobs", joinColumns = @JoinColumn(name = "candidate_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"))
	private Set<Job> appliedJobs = new HashSet<>();

	/*
	 * @ManyToMany
	 * 
	 * @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	 * 
	 * @JoinTable(name = "candidate_job", joinColumns
	 * = @JoinColumn(name="candidates_id", referencedColumnName="id"),
	 * inverseJoinColumns = @JoinColumn(name="jobs_id", referencedColumnName="id"))
	 * private Set<Job> jobs = new HashSet<>();
	 */

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private Set<CandidateJob> candidateJobs = new HashSet<CandidateJob>();

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonBackReference
	private Set<CorporateCandidate> shortlistedByCorporates = new HashSet<>();
	
	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
    private Set<CandidateProfileScore> profileScores = new HashSet<>();

	@ManyToOne
	private VisaType visaType;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public Candidate firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Candidate lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public Candidate middleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFacebook() {
		return facebook;
	}

	public Candidate facebook(String facebook) {
		this.facebook = facebook;
		return this;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getLinkedIn() {
		return linkedIn;
	}

	public Candidate linkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
		return this;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	public String getTwitter() {
		return twitter;
	}

	public Candidate twitter(String twitter) {
		this.twitter = twitter;
		return this;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public Candidate aboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
		return this;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Candidate dateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public Candidate phoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
		return this;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Candidate phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean isDifferentlyAbled() {
		return differentlyAbled;
	}

	public Candidate differentlyAbled(Boolean differentlyAbled) {
		this.differentlyAbled = differentlyAbled;
		return this;
	}

	public void setDifferentlyAbled(Boolean differentlyAbled) {
		this.differentlyAbled = differentlyAbled;
	}

	public Boolean isAvailableForHiring() {
		return availableForHiring;
	}

	public Candidate availableForHiring(Boolean availableForHiring) {
		this.availableForHiring = availableForHiring;
		return this;
	}

	public void setAvailableForHiring(Boolean availableForHiring) {
		this.availableForHiring = availableForHiring;
	}

	public Boolean isOpenToRelocate() {
		return openToRelocate;
	}

	public Candidate openToRelocate(Boolean openToRelocate) {
		this.openToRelocate = openToRelocate;
		return this;
	}

	public void setOpenToRelocate(Boolean openToRelocate) {
		this.openToRelocate = openToRelocate;
	}

	public Boolean isMatchEligible() {
		return matchEligible;
	}

	public Boolean getMatchEligible() {
		return matchEligible;
	}

	public Candidate matchEligible(Boolean matchEligible) {
		this.matchEligible = matchEligible;
		return this;
	}

	public void setMatchEligible(Boolean matchEligible) {
		this.matchEligible = matchEligible;
	}

	public Candidate profileScore(Double profileScore) {
		this.profileScore = profileScore;
		return this;
	}
	
	/**
	 * @return the profileScore
	 */
	public Double getProfileScore() {
		return profileScore;
	}

	/**
	 * @param profileScore the profileScore to set
	 */
	public void setProfileScore(Double profileScore) {
		this.profileScore = profileScore;
	}

	public User getLogin() {
		return login;
	}

	public Candidate login(User user) {
		this.login = user;
		return this;
	}

	public void setLogin(User user) {
		this.login = user;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public Candidate addresses(Set<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public Candidate addAddress(Address address) {
		this.addresses.add(address);
		address.setCandidate(this);
		return this;
	}

	public Candidate removeAddress(Address address) {
		this.addresses.remove(address);
		address.setCandidate(null);
		return this;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<CandidateEducation> getEducations() {
		return educations;
	}

	public Candidate educations(Set<CandidateEducation> candidateEducations) {
		this.educations = candidateEducations;
		return this;
	}

	public Candidate addEducation(CandidateEducation candidateEducation) {
		this.educations.add(candidateEducation);
		candidateEducation.setCandidate(this);
		return this;
	}

	public Candidate removeEducation(CandidateEducation candidateEducation) {
		this.educations.remove(candidateEducation);
		candidateEducation.setCandidate(null);
		return this;
	}

	public void setEducations(Set<CandidateEducation> candidateEducations) {
		this.educations = candidateEducations;
	}

	public Set<CandidateNonAcademicWork> getNonAcademics() {
		return nonAcademics;
	}

	public Candidate nonAcademics(Set<CandidateNonAcademicWork> candidateNonAcademicWorks) {
		this.nonAcademics = candidateNonAcademicWorks;
		return this;
	}

	public Candidate addNonAcademic(CandidateNonAcademicWork candidateNonAcademicWork) {
		this.nonAcademics.add(candidateNonAcademicWork);
		candidateNonAcademicWork.setCandidate(this);
		return this;
	}

	public Candidate removeNonAcademic(CandidateNonAcademicWork candidateNonAcademicWork) {
		this.nonAcademics.remove(candidateNonAcademicWork);
		candidateNonAcademicWork.setCandidate(null);
		return this;
	}

	public void setNonAcademics(Set<CandidateNonAcademicWork> candidateNonAcademicWorks) {
		this.nonAcademics = candidateNonAcademicWorks;
	}

	public Set<CandidateCertification> getCertifications() {
		return certifications;
	}

	public Candidate certifications(Set<CandidateCertification> candidateCertifications) {
		this.certifications = candidateCertifications;
		return this;
	}

	public Candidate addCertification(CandidateCertification candidateCertification) {
		this.certifications.add(candidateCertification);
		candidateCertification.setCandidate(this);
		return this;
	}

	public Candidate removeCertification(CandidateCertification candidateCertification) {
		this.certifications.remove(candidateCertification);
		candidateCertification.setCandidate(null);
		return this;
	}

	public void setCertifications(Set<CandidateCertification> candidateCertifications) {
		this.certifications = candidateCertifications;
	}

	public Set<CandidateEmployment> getEmployments() {
		return employments;
	}

	public Candidate employments(Set<CandidateEmployment> candidateEmployments) {
		this.employments = candidateEmployments;
		return this;
	}

	public Candidate addEmployment(CandidateEmployment candidateEmployment) {
		this.employments.add(candidateEmployment);
		candidateEmployment.setCandidate(this);
		return this;
	}

	public Candidate removeEmployment(CandidateEmployment candidateEmployment) {
		this.employments.remove(candidateEmployment);
		candidateEmployment.setCandidate(null);
		return this;
	}

	public void setEmployments(Set<CandidateEmployment> candidateEmployments) {
		this.employments = candidateEmployments;
	}

	public Set<CandidateLanguageProficiency> getCandidateLanguageProficiencies() {
		return candidateLanguageProficiencies;
	}

	public Candidate candidateLanguageProficiencies(Set<CandidateLanguageProficiency> candidateLanguageProficiencies) {
		this.candidateLanguageProficiencies = candidateLanguageProficiencies;
		return this;
	}

	public Candidate addCandidateLanguageProficiency(CandidateLanguageProficiency candidateLanguageProficiency) {
		this.candidateLanguageProficiencies.add(candidateLanguageProficiency);
		candidateLanguageProficiency.setCandidate(this);
		return this;
	}

	public Candidate removeCandidateLanguageProficiency(CandidateLanguageProficiency candidateLanguageProficiency) {
		this.candidateLanguageProficiencies.remove(candidateLanguageProficiency);
		candidateLanguageProficiency.setCandidate(null);
		return this;
	}

	public void setCandidateLanguageProficiencies(Set<CandidateLanguageProficiency> candidateLanguageProficiencies) {
		this.candidateLanguageProficiencies = candidateLanguageProficiencies;
	}

	public Nationality getNationality() {
		return nationality;
	}

	public Candidate nationality(Nationality nationality) {
		this.nationality = nationality;
		return this;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public Gender getGender() {
		return gender;
	}

	public Candidate gender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public Candidate maritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
		return this;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Set<JobCategory> getJobCategories() {
		return jobCategories;
	}

	public Candidate jobCategories(Set<JobCategory> jobCategories) {
		this.jobCategories = jobCategories;
		return this;
	}

	public Candidate addJobCategory(JobCategory jobCategory) {
		this.jobCategories.add(jobCategory);
		jobCategory.getCandidates().add(this);
		return this;
	}

	public Candidate removeJobCategory(JobCategory jobCategory) {
		this.jobCategories.remove(jobCategory);
		jobCategory.getCandidates().remove(this);
		return this;
	}

	public void setJobCategories(Set<JobCategory> jobCategories) {
		this.jobCategories = jobCategories;
	}

	public VisaType getVisaType() {
		return visaType;
	}

	public Candidate visaType(VisaType visaType) {
		this.visaType = visaType;
		return this;
	}

	public void setVisaType(VisaType visaType) {
		this.visaType = visaType;
	}

	public Candidate addCandidateJob(CandidateJob candidateJob) {
		this.candidateJobs.add(candidateJob);
		candidateJob.setCandidate(this);
		// job.getCandidateJobs().add(candidateJob);
		return this;
	}

	/*
	 * public Candidate removeJob(Job job) { for (Iterator<CandidateJob> iterator =
	 * jobs.iterator(); iterator.hasNext(); ) { CandidateJob candidateJob =
	 * iterator.next(); if(candidateJob.getCandidate().equals(this) &&
	 * candidateJob.getJob().getId().equals(job.getId())) { iterator.remove();
	 * candidateJob.getJob().getCandidates().remove(candidateJob);
	 * candidateJob.setCandidate(null); candidateJob.setJob(null); } } return this;
	 * }
	 */

	/**
	 * @return the appliedJobs
	 */
	public Set<Job> getAppliedJobs() {
		return appliedJobs;
	}

	/**
	 * @param appliedJobs
	 *            the appliedJobs to set
	 */
	public void setAppliedJobs(Set<Job> appliedJobs) {
		this.appliedJobs = appliedJobs;
	}

	public Candidate addAppliedJob(Job job) {
		this.appliedJobs.add(job);
		job.getAppliedCandidates().add(this);
		return this;
	}

	public Candidate removeAppliedJob(Job job) {
		this.appliedJobs.remove(job);
		job.getAppliedCandidates().remove(this);
		return this;
	}

	/**
	 * @return the candidateJobs
	 */
	public Set<CandidateJob> getCandidateJobs() {
		return candidateJobs;
	}

	/**
	 * @param candidateJobs
	 */
	public void setCandidateJobs(Set<CandidateJob> candidatejobs) {
		this.candidateJobs = candidatejobs;
	}
	

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	/**
	 * @return the shortlistedByCorporates
	 */
	public Set<CorporateCandidate> getShortlistedByCorporates() {
		return shortlistedByCorporates;
	}

	/**
	 * @param shortlistedByCorporates
	 *            the shortlistedByCorporates to set
	 */
	public void setShortlistedByCorporates(Set<CorporateCandidate> shortlistedByCorporates) {
		this.shortlistedByCorporates = shortlistedByCorporates;
	}

	public Candidate addCorporateCandidate(CorporateCandidate corporateCandidate) {
		this.shortlistedByCorporates.add(corporateCandidate);
		corporateCandidate.setCandidate(this);
		return this;
	}
	
	public Candidate addCandidateProfileScore(CandidateProfileScore profileScore) {
		this.profileScores.add(profileScore);
		profileScore.setCandidate(this);
		return this;
	}

	/**
	 * @return the profileScores
	 */
	public Set<CandidateProfileScore> getProfileScores() {
		return profileScores;
	}

	/**
	 * @param profileScores the profileScores to set
	 */
	public void setProfileScores(Set<CandidateProfileScore> profileScores) {
		this.profileScores = profileScores;
	}

    public Set<CandidateSkills> getCandidateSkills() {
        return candidateSkills;
    }

    public Candidate candidateSkills(Set<CandidateSkills> candidateSkills) {
        this.candidateSkills = candidateSkills;
        return this;
    }


    public Candidate addCandidateSkill(CandidateSkills candidateSkills) {
        this.candidateSkills.add(candidateSkills);
        candidateSkills.setCandidate(this);
        return this;
    }

    public Candidate removeCandidateSkill(CandidateSkills candidateSkills) {
        this.candidateSkills.remove(candidateSkills);
        candidateSkills.setCandidate(null);
        return this;
    }

    public void setCandidateSkills(Set<CandidateSkills> candidateSkills) {
        this.candidateSkills = candidateSkills;
    }

    
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Candidate candidate = (Candidate) o;
		if (candidate.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), candidate.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Candidate [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
				+ middleName + ", facebook=" + facebook + ", linkedIn=" + linkedIn + ", twitter=" + twitter
				+ ", aboutMe=" + aboutMe + ", dateOfBirth=" + dateOfBirth + ", phoneCode=" + phoneCode
				+ ", phoneNumber=" + phoneNumber + ", differentlyAbled=" + differentlyAbled + ", availableForHiring="
				+ availableForHiring + ", openToRelocate=" + openToRelocate + ", matchEligible=" + matchEligible
				+ ", profileScore=" + profileScore + "]";
	}

	

}
