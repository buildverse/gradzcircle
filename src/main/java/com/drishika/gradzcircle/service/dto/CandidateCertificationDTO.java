/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author abhinav
 *
 */
public class CandidateCertificationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String certificationTitle;
	
	private Long id;

	private LocalDate certificationDate;

	private String certificationDetails;
	
	private CandidateDTO candidate;

	/**
	 * @return the certificationTitle
	 */
	public String getCertificationTitle() {
		return certificationTitle;
	}

	/**
	 * @param certificationTitle
	 *            the certificationTitle to set
	 */
	public void setCertificationTitle(String certificationTitle) {
		this.certificationTitle = certificationTitle;
	}

	/**
	 * @return the certificationDate
	 */
	public LocalDate getCertificationDate() {
		return certificationDate;
	}

	/**
	 * @param certificationDate
	 *            the certificationDate to set
	 */
	public void setCertificationDate(LocalDate certificationDate) {
		this.certificationDate = certificationDate;
	}

	/**
	 * @return the certificationDetails
	 */
	public String getCertificationDetails() {
		return certificationDetails;
	}

	/**
	 * @param certificationDetails
	 *            the certificationDetails to set
	 */
	public void setCertificationDetails(String certificationDetails) {
		this.certificationDetails = certificationDetails;
	}

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
	 * @return the candidate
	 */
	public CandidateDTO getCandidate() {
		return candidate;
	}

	/**
	 * @param candidate the candidate to set
	 */
	public void setCandidate(CandidateDTO candidate) {
		this.candidate = candidate;
	}
	
	

}
