/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class CandidateLanguageProficiencyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String proficiency;

	private String language;

	/**
	 * @return the proficiency
	 */
	public String getProficiency() {
		return proficiency;
	}

	/**
	 * @param proficiency
	 *            the proficiency to set
	 */
	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
