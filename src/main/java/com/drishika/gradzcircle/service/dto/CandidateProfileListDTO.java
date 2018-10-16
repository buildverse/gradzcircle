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
public class CandidateProfileListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String tagLine;
	private String qualificationWithHighestCourse;
	private User login;
	private Long id;
	private Boolean reviewed;

	/**
	 * @return the reviewed
	 */
	public Boolean getReviewed() {
		return reviewed;
	}

	/**
	 * @param reviewed
	 *            the reviewed to set
	 */
	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

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
	 * @return the tagLine
	 */
	public String getTagLine() {
		return tagLine;
	}

	/**
	 * @param tagLine
	 *            the tagLine to set
	 */
	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	/**
	 * @return the qualificationWithHighestCourse
	 */
	public String getQualificationWithHighestCourse() {
		return qualificationWithHighestCourse;
	}

	/**
	 * @param qualificationWithHighestCourse
	 *            the qualificationWithHighestCourse to set
	 */
	public void setQualificationWithHighestCourse(String qualificationWithHighestCourse) {
		this.qualificationWithHighestCourse = qualificationWithHighestCourse;
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
