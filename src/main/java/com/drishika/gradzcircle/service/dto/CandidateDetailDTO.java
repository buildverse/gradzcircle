/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;

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

	private User login;

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

}
