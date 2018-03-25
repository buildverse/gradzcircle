package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidate")
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

    @OneToOne
    @JoinColumn(unique = true)
    private User login;

    @OneToMany(mappedBy = "candidate", cascade=CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateEducation> educations = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateNonAcademicWork> nonAcademics = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateCertification> certifications = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateEmployment> employments = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<>();

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private Gender gender;

    @ManyToOne
    private MaritalStatus maritalStatus;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "candidate_job_category",
               joinColumns = @JoinColumn(name="candidates_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="job_categories_id", referencedColumnName="id"))
    private Set<JobCategory> jobCategories = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "candidate_job",
               joinColumns = @JoinColumn(name="candidates_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="jobs_id", referencedColumnName="id"))
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    private VisaType visaType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Set<Job> getJobs() {
        return jobs;
    }

    public Candidate jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public Candidate addJob(Job job) {
        this.jobs.add(job);
        job.getCandidates().add(this);
        return this;
    }

    public Candidate removeJob(Job job) {
        this.jobs.remove(job);
        job.getCandidates().remove(this);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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

    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", linkedIn='" + getLinkedIn() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", aboutMe='" + getAboutMe() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", differentlyAbled='" + isDifferentlyAbled() + "'" +
            ", availableForHiring='" + isAvailableForHiring() + "'" +
            ", openToRelocate='" + isOpenToRelocate() + "'" +
            "}";
    }
}
