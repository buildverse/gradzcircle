/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.JobType;

/**
 * @author abhinav
 *
 */
public class CandidateJobDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String jobTitle;

	private Integer jobStatus;

	private EmploymentType employmentType;
	
	private String jobDescription;
	
	private Double salary;
	
	private ZonedDateTime updateDate;

	private JobType jobType;

	private Double matchScore;

	private Boolean hasCandidateApplied;
	
	private Long totalNumberOfJobs;
	
	private Long countOfPermanentEmployment;
	
	private Long countOfContractEmployment;
	
	private Long countOfFullTimeJob;
	
	private Long countOfPartTimeJob;
	
	private Long countOfInternJob;
	
	private Long countOfSummerJob;
	
	private String corporateName;
	
	private String city;
	
	private String corporateUrl;
	
	private Long corporateLoginId;
	

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
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the jobStatus
	 */
	public Integer getJobStatus() {
		return jobStatus;
	}

	/**
	 * @param jobStatus
	 *            the jobStatus to set
	 */
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * @return the employmentType
	 */
	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType
	 *            the employmentType to set
	 */
	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * @return the jobType
	 */
	public JobType getJobType() {
		return jobType;
	}

	/**
	 * @param jobType
	 *            the jobType to set
	 */
	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	/**
	 * @return the matchScore
	 */
	public Double getMatchScore() {
		return matchScore;
	}

	/**
	 * @param matchScore
	 *            the matchScore to set
	 */
	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}

	/**
	 * @return the hasCandidateApplied
	 */
	public Boolean getHasCandidateApplied() {
		return hasCandidateApplied;
	}

	/**
	 * @param hasCandidateApplied
	 *            the hasCandidateApplied to set
	 */
	public void setHasCandidateApplied(Boolean hasCandidateApplied) {
		this.hasCandidateApplied = hasCandidateApplied;
	}

	/**
	 * @return the totalNumberOfJobs
	 */
	public Long getTotalNumberOfJobs() {
		return totalNumberOfJobs;
	}

	/**
	 * @param totalNumberOfJobs the totalNumberOfJobs to set
	 */
	public void setTotalNumberOfJobs(Long totalNumberOfJobs) {
		this.totalNumberOfJobs = totalNumberOfJobs;
	}

	/**
	 * @return the countOfPermanentEmployment
	 */
	public Long getCountOfPermanentEmployment() {
		return countOfPermanentEmployment;
	}

	/**
	 * @param countOfPermanentEmployment the countOfPermanentEmployment to set
	 */
	public void setCountOfPermanentEmployment(Long countOfPermanentEmployment) {
		this.countOfPermanentEmployment = countOfPermanentEmployment;
	}

	/**
	 * @return the countOfContractEmployment
	 */
	public Long getCountOfContractEmployment() {
		return countOfContractEmployment;
	}

	/**
	 * @param countOfContractEmployment the countOfContractEmployment to set
	 */
	public void setCountOfContractEmployment(Long countOfContractEmployment) {
		this.countOfContractEmployment = countOfContractEmployment;
	}

	/**
	 * @return the countOfFullTimeJob
	 */
	public Long getCountOfFullTimeJob() {
		return countOfFullTimeJob;
	}

	/**
	 * @param countOfFullTimeJob the countOfFullTimeJob to set
	 */
	public void setCountOfFullTimeJob(Long countOfFullTimeJob) {
		this.countOfFullTimeJob = countOfFullTimeJob;
	}

	/**
	 * @return the countOfPartTimeJob
	 */
	public Long getCountOfPartTimeJob() {
		return countOfPartTimeJob;
	}

	/**
	 * @param countOfPartTimeJob the countOfPartTimeJob to set
	 */
	public void setCountOfPartTimeJob(Long countOfPartTimeJob) {
		this.countOfPartTimeJob = countOfPartTimeJob;
	}

	/**
	 * @return the countOfInternJob
	 */
	public Long getCountOfInternJob() {
		return countOfInternJob;
	}

	/**
	 * @param countOfInternJob the countOfInternJob to set
	 */
	public void setCountOfInternJob(Long countOfInternJob) {
		this.countOfInternJob = countOfInternJob;
	}

	/**
	 * @return the countOfSummerJob
	 */
	public Long getCountOfSummerJob() {
		return countOfSummerJob;
	}

	/**
	 * @param countOfSummerJob the countOfSummerJob to set
	 */
	public void setCountOfSummerJob(Long countOfSummerJob) {
		this.countOfSummerJob = countOfSummerJob;
	}

	/**
	 * @return the jobDescription
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/**
	 * @param jobDescription the jobDescription to set
	 */
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	/**
	 * @return the salary
	 */
	public Double getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(Double salary) {
		this.salary = salary;
	}

	/**
	 * @return the updateDate
	 */
	public ZonedDateTime getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the corporateName
	 */
	public String getCorporateName() {
		return corporateName;
	}

	/**
	 * @param corporateName the corporateName to set
	 */
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the corporateUrl
	 */
	public String getCorporateUrl() {
		return corporateUrl;
	}

	/**
	 * @param corporateUrl the corporateUrl to set
	 */
	public void setCorporateUrl(String corporateUrl) {
		this.corporateUrl = corporateUrl;
	}

	/**
	 * @return the corporateLoginId
	 */
	public Long getCorporateLoginId() {
		return corporateLoginId;
	}

	/**
	 * @param corporateLoginId the corporateLoginId to set
	 */
	public void setCorporateLoginId(Long corporateLoginId) {
		this.corporateLoginId = corporateLoginId;
	}

	
}
