/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.domain.User;

/**
 * @author abhinav
 *
 */
public class CandidateDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String firstName;

	private String lastName;

	private String middleName;

	private String facebook;

	private String linkedIn;

	private String twitter;

	private String aboutMe;

	private LocalDate dateOfBirth;

	private String phoneCode;

	private String phoneNumber;

	private Boolean differentlyAbled;

	private Boolean availableForHiring;

	private Boolean openToRelocate;
	
	private Nationality nationality;

	private User login;
	
	private List<JobCategoryDTO> jobCategories = new ArrayList<>();
	
	private List<AddressDTO> addresses = new ArrayList<>();
	
	private GenderDTO gender;
	
	private MaritalStatusDTO maritalStatus;
	
	private Double profileScore;
	
	private Boolean hasEducation;
	
	private Boolean hasCertification;
	
	private Boolean hasEmployment;
	
	private Boolean hasNonAcademic;
	
	private Boolean hasLanguages;

	private Boolean hasSkills;
	
	private String imageUrl;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the facebook
	 */
	public String getFacebook() {
		return facebook;
	}

	/**
	 * @param facebook
	 *            the facebook to set
	 */
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	/**
	 * @return the linkedIn
	 */
	public String getLinkedIn() {
		return linkedIn;
	}

	/**
	 * @param linkedIn
	 *            the linkedIn to set
	 */
	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	/**
	 * @return the twitter
	 */
	public String getTwitter() {
		return twitter;
	}

	/**
	 * @param twitter
	 *            the twitter to set
	 */
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	/**
	 * @return the aboutMe
	 */
	public String getAboutMe() {
		return aboutMe;
	}

	/**
	 * @param aboutMe
	 *            the aboutMe to set
	 */
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	/**
	 * @return the dateOfBirth
	 */
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the phoneCode
	 */
	public String getPhoneCode() {
		return phoneCode;
	}

	/**
	 * @param phoneCode
	 *            the phoneCode to set
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the differentlyAbled
	 */
	public Boolean getDifferentlyAbled() {
		return differentlyAbled;
	}

	/**
	 * @param differentlyAbled
	 *            the differentlyAbled to set
	 */
	public void setDifferentlyAbled(Boolean differentlyAbled) {
		this.differentlyAbled = differentlyAbled;
	}

	/**
	 * @return the availableForHiring
	 */
	public Boolean getAvailableForHiring() {
		return availableForHiring;
	}

	/**
	 * @param availableForHiring
	 *            the availableForHiring to set
	 */
	public void setAvailableForHiring(Boolean availableForHiring) {
		this.availableForHiring = availableForHiring;
	}

	/**
	 * @return the openToRelocate
	 */
	public Boolean getOpenToRelocate() {
		return openToRelocate;
	}

	/**
	 * @param openToRelocate
	 *            the openToRelocate to set
	 */
	public void setOpenToRelocate(Boolean openToRelocate) {
		this.openToRelocate = openToRelocate;
	}

	/**
	 * @return the login
	 */
	public User getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(User login) {
		this.login = login;
	}

	/**
	 * @return the addresses
	 */
	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the jobCategories
	 */
	public List<JobCategoryDTO> getJobCategories() {
		return jobCategories;
	}

	/**
	 * @param jobCategories the jobCategories to set
	 */
	public void setJobCategories(List<JobCategoryDTO> jobCategories) {
		this.jobCategories = jobCategories;
	}

	/**
	 * @return the nationality
	 */
	public Nationality getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the genderDTO
	 */
	public GenderDTO getGender() {
		return gender;
	}

	/**
	 * @param genderDTO the genderDTO to set
	 */
	public void setGender(GenderDTO gender) {
		this.gender = gender;
	}

	/**
	 * @return the maritalStatus
	 */
	public MaritalStatusDTO getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(MaritalStatusDTO maritalStatus) {
		this.maritalStatus = maritalStatus;
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

	/**
	 * @return the hasEducation
	 */
	public Boolean getHasEducation() {
		return hasEducation;
	}

	/**
	 * @param hasEducation the hasEducation to set
	 */
	public void setHasEducation(Boolean hasEducation) {
		this.hasEducation = hasEducation;
	}

	/**
	 * @return the hasCertification
	 */
	public Boolean getHasCertification() {
		return hasCertification;
	}

	/**
	 * @param hasCertification the hasCertification to set
	 */
	public void setHasCertification(Boolean hasCertification) {
		this.hasCertification = hasCertification;
	}

	/**
	 * @return the hasEmployment
	 */
	public Boolean getHasEmployment() {
		return hasEmployment;
	}

	/**
	 * @param hasEmployment the hasEmployment to set
	 */
	public void setHasEmployment(Boolean hasEmployment) {
		this.hasEmployment = hasEmployment;
	}

	/**
	 * @return the hasNonAcademic
	 */
	public Boolean getHasNonAcademic() {
		return hasNonAcademic;
	}

	/**
	 * @param hasNonAcademic the hasNonAcademic to set
	 */
	public void setHasNonAcademic(Boolean hasNonAcademic) {
		this.hasNonAcademic = hasNonAcademic;
	}

	/**
	 * @return the hasLanguages
	 */
	public Boolean getHasLanguages() {
		return hasLanguages;
	}

	/**
	 * @param hasLanguages the hasLanguages to set
	 */
	public void setHasLanguages(Boolean hasLanguages) {
		this.hasLanguages = hasLanguages;
	}

	/**
	 * @return the hasSkills
	 */
	public Boolean getHasSkills() {
		return hasSkills;
	}

	/**
	 * @param hasSkills the hasSkills to set
	 */
	public void setHasSkills(Boolean hasSkills) {
		this.hasSkills = hasSkills;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	

}
