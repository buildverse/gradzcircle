/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author abhinav
 *
 */
public class CandidatePublicProfileDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private CandidateDetailDTO candidateDetails;

	private Boolean isShortListed;
	
	private Double matchScore;
	
	//private Boolean shortListedForCurrentJob;
	
	private Boolean reviewed;

	List<CandidateEducationDTO> educations = new ArrayList<>();

	List<CandidateCertificationDTO> certifications = new ArrayList<>();

	List<CandidateEmploymentDTO> employments = new ArrayList<>();

	List<CandidateNonAcademicWorkDTO> nonAcademics = new ArrayList<>();

	Set<CandidateLanguageProficiencyDTO> candidateLanguageProficiencies = new HashSet<>();

	Set<AddressDTO> addresses = new HashSet<>();
	
	Boolean canBeShortListed;

	

	/**
	 * @return the isShortListed
	 */
	public Boolean getIsShortListed() {
		return isShortListed;
	}

	/**
	 * @param isShortListed the isShortListed to set
	 */
	public void setIsShortListed(Boolean isShortListed) {
		this.isShortListed = isShortListed;
	}

	/**
	 * @return the candidateDetails
	 */
	public CandidateDetailDTO getCandidateDetails() {
		return candidateDetails;
	}

	/**
	 * @param candidateDetails
	 *            the candidateDetails to set
	 */
	public void setCandidateDetails(CandidateDetailDTO candidateDetails) {
		this.candidateDetails = candidateDetails;
	}

	
	
	/**
	 * @return the educations
	 */
	public List<CandidateEducationDTO> getEducations() {
		return educations;
	}

	/**
	 * @param educations the educations to set
	 */
	public void setEducations(List<CandidateEducationDTO> educations) {
		this.educations = educations;
	}

	/**
	 * @return the certifications
	 */
	public List<CandidateCertificationDTO> getCertifications() {
		return certifications;
	}

	/**
	 * @param certifications the certifications to set
	 */
	public void setCertifications(List<CandidateCertificationDTO> certifications) {
		this.certifications = certifications;
	}

	/**
	 * @return the employments
	 */
	public List<CandidateEmploymentDTO> getEmployments() {
		return employments;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<CandidateEmploymentDTO> employments) {
		this.employments = employments;
	}

	/**
	 * @return the nonAcademics
	 */
	public List<CandidateNonAcademicWorkDTO> getNonAcademics() {
		return nonAcademics;
	}

	/**
	 * @param nonAcademics the nonAcademics to set
	 */
	public void setNonAcademics(List<CandidateNonAcademicWorkDTO> nonAcademics) {
		this.nonAcademics = nonAcademics;
	}

	/**
	 * @return the candidateLanguageProficiencies
	 */
	public Set<CandidateLanguageProficiencyDTO> getCandidateLanguageProficiencies() {
		return candidateLanguageProficiencies;
	}

	/**
	 * @param candidateLanguageProficiencies
	 *            the candidateLanguageProficiencies to set
	 */
	public void setCandidateLanguageProficiencies(Set<CandidateLanguageProficiencyDTO> candidateLanguageProficiencies) {
		this.candidateLanguageProficiencies = candidateLanguageProficiencies;
	}

	/**
	 * @return the addresses
	 */
	public Set<AddressDTO> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses
	 *            the addresses to set
	 */
	public void setAddresses(Set<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the matchScore
	 */
	public Double getMatchScore() {
		return matchScore;
	}

	/**
	 * @param matchScore the matchScore to set
	 */
	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}

	/**
	 * @return the reviewed
	 */
	public Boolean getReviewed() {
		return reviewed;
	}

	/**
	 * @param reviewed the reviewed to set
	 */
	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

	/**
	 * @return the canBeShortListed
	 */
	public Boolean getCanBeShortListed() {
		return canBeShortListed;
	}

	/**
	 * @param canBeShortListed the canBeShortListed to set
	 */
	public void setCanBeShortListed(Boolean canBeShortListed) {
		this.canBeShortListed = canBeShortListed;
	}

	
	
	

}
