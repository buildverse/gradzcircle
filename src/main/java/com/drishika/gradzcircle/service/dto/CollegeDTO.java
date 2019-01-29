/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.drishika.gradzcircle.domain.University;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author abhinav
 *
 */
public class CollegeDTO implements Serializable {

	private Long id;

	private String collegeName;

	private String value;

	private String display;

	private UniversityDTO university;
	
	public CollegeDTO() {
		university = new UniversityDTO();
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
	 * @return the collegeName
	 */
	public String getCollegeName() {
		return collegeName;
	}

	/**
	 * @param collegeName
	 *            the collegeName to set
	 */
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
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
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return the universityDto
	 */
	public UniversityDTO getUniversity() {
		return university;
	}

	/**
	 * @param universityDto
	 *            the universityDto to set
	 */
	public void setUniversity(UniversityDTO universityDto) {
		this.university = universityDto;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollegeDTO [id=" + id + ", collegeName=" + collegeName + ", value=" + value + ", display=" + display
				+ ", universityDto=" + university + "]";
	}
	
	

}
