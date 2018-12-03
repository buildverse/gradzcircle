/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

import com.drishika.gradzcircle.domain.User;

/**
 * @author abhinav
 *
 */
public class CandidateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private String aboutMe;
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
	 * @return the user
	 */
	public User getLogin() {
		return login;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setLogin(User login) {
		this.login = login;
	}

	/**
	 * @return the aboutMe
	 */
	public String getAboutMe() {
		return aboutMe;
	}

	/**
	 * @param aboutMe the aboutMe to set
	 */
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	
	

}
