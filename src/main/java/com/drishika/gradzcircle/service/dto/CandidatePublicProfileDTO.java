/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.util.HashSet;
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

	Set<CandidateEducationDTO> educations = new HashSet<>();

	Set<CandidateCertificationDTO> certifications = new HashSet<>();

	Set<CandidateEmploymentDTO> employments = new HashSet<>();

	Set<CandidateNonAcademicWorkDTO> nonAcademics = new HashSet<>();

	Set<CandidateLanguageProficiencyDTO> candidateLanguageProficiencies = new HashSet<>();

	Set<AddressDTO> addresses = new HashSet<>();
	
	

	

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
	public Set<CandidateEducationDTO> getEducations() {
		return educations;
	}

	/**
	 * @param educations
	 *            the educations to set
	 */
	public void setEducations(Set<CandidateEducationDTO> educations) {
		this.educations = educations;
	}

	/**
	 * @return the certifications
	 */
	public Set<CandidateCertificationDTO> getCertifications() {
		return certifications;
	}

	/**
	 * @param certifications
	 *            the certifications to set
	 */
	public void setCertifications(Set<CandidateCertificationDTO> certifications) {
		this.certifications = certifications;
	}

	/**
	 * @return the employments
	 */
	public Set<CandidateEmploymentDTO> getEmployments() {
		return employments;
	}

	/**
	 * @param employments
	 *            the employments to set
	 */
	public void setEmployments(Set<CandidateEmploymentDTO> employments) {
		this.employments = employments;
	}

	/**
	 * @return the nonAcademics
	 */
	public Set<CandidateNonAcademicWorkDTO> getNonAcademics() {
		return nonAcademics;
	}

	/**
	 * @param nonAcademics
	 *            the nonAcademics to set
	 */
	public void setNonAcademics(Set<CandidateNonAcademicWorkDTO> nonAcademics) {
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

	
	
	

}
