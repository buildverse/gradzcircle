/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class GenderDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String gender;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	

}
