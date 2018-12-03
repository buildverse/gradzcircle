/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author abhinav
 *
 */
public class CountryDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String countryNiceName;
	
	private String value;

	private String display;

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
	 * @return the countryNiceName
	 */
	public String getCountryNiceName() {
		return countryNiceName;
	}

	/**
	 * @param countryNiceName the countryNiceName to set
	 */
	public void setCountryNiceName(String countryNiceName) {
		this.countryNiceName = countryNiceName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
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
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
	
	

}
