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

	private Long id ;
	
	private String proficiency;

	private LanguageDTO language;

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
	public LanguageDTO getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(LanguageDTO language) {
		this.language = language;
	}

	
	

}
