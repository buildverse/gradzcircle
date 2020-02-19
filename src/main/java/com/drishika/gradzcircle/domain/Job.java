package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.drishika.gradzcircle.domain.enumeration.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
// property = "id")
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "job_title")
	private String jobTitle;

	@Size(max = 10000)
	@Column(name = "job_description", length = 10000)
	private String jobDescription;

	@Column(name = "no_of_applicants")
	private Integer noOfApplicants;

	@Column(name = "salary")
	private Double salary;

	@Column(name = "job_status")
	private Integer jobStatus;

	@Column(name = "create_date")
	private ZonedDateTime createDate;

	@Column(name = "original_job_cost")
	private Double originalJobCost;

	@Column(name = "job_cost")
	private Double jobCost;

	@Column(name = "amount_paid")
	private Double amountPaid;

	@Column(name = "total_amount_paid")
	private Double totalAmountPaid;

	@Column(name = "no_of_applicants_bought")
	private Integer noOfApplicantsBought;

	@Column(name = "removed_filter_amount")
	private Double removedFilterAmount;

	@Column(name = "additional_filter_amount")
	private Double additionalFilterAmount;

	@Column(name = "admin_charge")
	private Double adminCharge;

	@Column(name = "admin_charge_rate")
	private Double adminChargeRate;

	@Column(name = "upfront_discount_rate")
	private Double upfrontDiscountRate;

	@Column(name = "upfront_discount_amount")
	private Double upfrontDiscountAmount;

	@Column(name = "escrow_amount_used")
	private Double escrowAmountUsed;

	@Column(name = "escrow_amount_added")
	private Double escrowAmountAdded;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Column(name = "has_been_edited")
	private Boolean hasBeenEdited;

	@Column(name = "ever_active")
	private Boolean everActive;

	@Column(name = "can_edit")
	private Boolean canEdit;

	@Column(name = "update_date")
	private ZonedDateTime updateDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;
	
    @Column(name = "no_of_applicant_left")
    private Long noOfApplicantLeft;

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonManagedReference
	private Set<JobFilter> jobFilters = new HashSet<>();

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JsonIgnore
//	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<JobHistory> histories = new HashSet<>();

	@ManyToOne
	private JobType jobType;

	@ManyToOne
	private EmploymentType employmentType;

	@ManyToOne
	private Corporate corporate;

	@ManyToMany(mappedBy = "appliedJobs",fetch = FetchType.LAZY)
	@JsonIgnore
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Candidate> appliedCandidates = new HashSet<>();

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonManagedReference(value = "jobToCandidate")
	private Set<CandidateJob> candidateJobs = new HashSet<>();

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public Job jobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
		return this;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public Job jobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
		return this;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Integer getNoOfApplicants() {
		return noOfApplicants;
	}

	public Job noOfApplicants(Integer noOfApplicants) {
		this.noOfApplicants = noOfApplicants;
		return this;
	}

	public void setNoOfApplicants(Integer noOfApplicants) {
		this.noOfApplicants = noOfApplicants;
	}

	public Double getSalary() {
		return salary == null ? new Double(0) : salary;
	}

	public Job salary(Double salary) {
		this.salary = salary;
		return this;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Integer getJobStatus() {
		return jobStatus;
	}

	public Job jobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
		return this;
	}

	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	public ZonedDateTime getCreateDate() {
		return createDate;
	}

	public Job createDate(ZonedDateTime createDate) {
		this.createDate = createDate;
		return this;
	}

	public void setCreateDate(ZonedDateTime createDate) {
		this.createDate = createDate;
	}

	public Double getOriginalJobCost() {
		return originalJobCost;
	}

	public Job originalJobCost(Double originalJobCost) {
		this.originalJobCost = originalJobCost;
		return this;
	}

	public void setOriginalJobCost(Double originalJobCost) {
		this.originalJobCost = originalJobCost;
	}

	public Double getJobCost() {
		return jobCost;
	}

	public Job jobCost(Double jobCost) {
		this.jobCost = jobCost;
		return this;
	}

	public void setJobCost(Double jobCost) {
		this.jobCost = jobCost;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public Job amountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
		return this;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getTotalAmountPaid() {
		return totalAmountPaid;
	}

	public Job totalAmountPaid(Double totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
		return this;
	}

	public void setTotalAmountPaid(Double totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}

	public Integer getNoOfApplicantsBought() {
		return noOfApplicantsBought;
	}

	public Job noOfApplicantsBought(Integer noOfApplicantsBought) {
		this.noOfApplicantsBought = noOfApplicantsBought;
		return this;
	}

	public void setNoOfApplicantsBought(Integer noOfApplicantsBought) {
		this.noOfApplicantsBought = noOfApplicantsBought;
	}

	public Double getRemovedFilterAmount() {
		return removedFilterAmount;
	}

	public Job removedFilterAmount(Double removedFilterAmount) {
		this.removedFilterAmount = removedFilterAmount;
		return this;
	}

	public void setRemovedFilterAmount(Double removedFilterAmount) {
		this.removedFilterAmount = removedFilterAmount;
	}

	public Double getAdditionalFilterAmount() {
		return additionalFilterAmount;
	}

	public Job additionalFilterAmount(Double additionalFilterAmount) {
		this.additionalFilterAmount = additionalFilterAmount;
		return this;
	}

	public void setAdditionalFilterAmount(Double additionalFilterAmount) {
		this.additionalFilterAmount = additionalFilterAmount;
	}

	public Double getAdminCharge() {
		return adminCharge;
	}

	public Job adminCharge(Double adminCharge) {
		this.adminCharge = adminCharge;
		return this;
	}

	public void setAdminCharge(Double adminCharge) {
		this.adminCharge = adminCharge;
	}

	public Double getAdminChargeRate() {
		return adminChargeRate;
	}

	public Job adminChargeRate(Double adminChargeRate) {
		this.adminChargeRate = adminChargeRate;
		return this;
	}

	public void setAdminChargeRate(Double adminChargeRate) {
		this.adminChargeRate = adminChargeRate;
	}

	public Double getUpfrontDiscountRate() {
		return upfrontDiscountRate;
	}

	public Job upfrontDiscountRate(Double upfrontDiscountRate) {
		this.upfrontDiscountRate = upfrontDiscountRate;
		return this;
	}

	public void setUpfrontDiscountRate(Double upfrontDiscountRate) {
		this.upfrontDiscountRate = upfrontDiscountRate;
	}

	public Double getUpfrontDiscountAmount() {
		return upfrontDiscountAmount;
	}

	public Job upfrontDiscountAmount(Double upfrontDiscountAmount) {
		this.upfrontDiscountAmount = upfrontDiscountAmount;
		return this;
	}

	public void setUpfrontDiscountAmount(Double upfrontDiscountAmount) {
		this.upfrontDiscountAmount = upfrontDiscountAmount;
	}

	public Double getEscrowAmountUsed() {
		return escrowAmountUsed;
	}

	public Job escrowAmountUsed(Double escrowAmountUsed) {
		this.escrowAmountUsed = escrowAmountUsed;
		return this;
	}

	public void setEscrowAmountUsed(Double escrowAmountUsed) {
		this.escrowAmountUsed = escrowAmountUsed;
	}

	public Double getEscrowAmountAdded() {
		return escrowAmountAdded;
	}

	public Job escrowAmountAdded(Double escrowAmountAdded) {
		this.escrowAmountAdded = escrowAmountAdded;
		return this;
	}

	public void setEscrowAmountAdded(Double escrowAmountAdded) {
		this.escrowAmountAdded = escrowAmountAdded;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public Job paymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
		return this;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Boolean isHasBeenEdited() {
		return hasBeenEdited;
	}

	public Boolean getHasBeenEdited() {
		return hasBeenEdited;
	}

	public Job hasBeenEdited(Boolean hasBeenEdited) {
		this.hasBeenEdited = hasBeenEdited;
		return this;
	}

	public void setHasBeenEdited(Boolean hasBeenEdited) {
		this.hasBeenEdited = hasBeenEdited;
	}

	public Boolean isEverActive() {
		return everActive;
	}

	public Boolean getEverActive() {
		return everActive;
	}

	public Job everActive(Boolean everActive) {
		this.everActive = everActive;
		return this;
	}

	public void setEverActive(Boolean everActive) {
		this.everActive = everActive;
	}

	public Boolean isCanEdit() {
		return canEdit;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public Job canEdit(Boolean canEdit) {
		this.canEdit = canEdit;
		return this;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public ZonedDateTime getUpdateDate() {
		return updateDate;
	}

	public Job updateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public Job createdBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public Job updatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Job noOfApplicantsLeft(Long noOfApplicantLeft) {
		this.noOfApplicantLeft = noOfApplicantLeft;
		return this;
	}

	/**
	 * @return the noOfApplicantLeft
	 */
	public Long getNoOfApplicantLeft() {
		return noOfApplicantLeft;
	}

	/**
	 * @param noOfApplicantLeft the noOfApplicantLeft to set
	 */
	public void setNoOfApplicantLeft(Long noOfApplicantLeft) {
		this.noOfApplicantLeft = noOfApplicantLeft;
	}

	public Set<JobFilter> getJobFilters() {
		return jobFilters;
	}

	public Job jobFilters(Set<JobFilter> jobFilters) {
		this.jobFilters = jobFilters;
		return this;
	}

	public Job addJobFilter(JobFilter jobFilter) {
		this.jobFilters.add(jobFilter);
		jobFilter.setJob(this);
		return this;
	}

	public Job removeJobFilter(JobFilter jobFilter) {
		this.jobFilters.remove(jobFilter);
		jobFilter.setJob(null);
		return this;
	}

	public void setJobFilters(Set<JobFilter> jobFilters) {
		this.jobFilters = jobFilters;
	}

	public Set<JobHistory> getHistories() {
		return histories;
	}

	public Job histories(Set<JobHistory> jobHistories) {
		this.histories = jobHistories;
		return this;
	}

	public Job addHistory(JobHistory jobHistory) {
		this.histories.add(jobHistory);
		jobHistory.setJob(this);
		return this;
	}

	public Job removeHistory(JobHistory jobHistory) {
		this.histories.remove(jobHistory);
		jobHistory.setJob(null);
		return this;
	}

	public void setHistories(Set<JobHistory> jobHistories) {
		this.histories = jobHistories;
	}

	public JobType getJobType() {
		return jobType;
	}

	public Job jobType(JobType jobType) {
		this.jobType = jobType;
		return this;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public Job employmentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
		return this;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public Corporate getCorporate() {
		return corporate;
	}

	public Job corporate(Corporate corporate) {
		this.corporate = corporate;
		return this;
	}

	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	public Set<Candidate> getAppliedCandidates() {
		return appliedCandidates;
	}

	public Job appliedCandidates(Set<Candidate> appliedCandidates) {
		this.appliedCandidates = appliedCandidates;
		return this;
	}

	public Job addAppliedCandidate(Candidate candidate) {
		this.appliedCandidates.add(candidate);
		candidate.getAppliedJobs().add(this);
		return this;
	}

	public Job removeAppliedCandidate(Candidate candidate) {
		this.appliedCandidates.remove(candidate);
		candidate.getAppliedJobs().remove(this);
		return this;
	}

	public void setAppliedCandidates(Set<Candidate> candidates) {
		this.appliedCandidates = candidates;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	/**
	 * @return the candidates
	 */
	public Set<CandidateJob> getCandidateJobs() {
		return candidateJobs;
	}

	/**
	 * @param candidates
	 *            the candidates to set
	 */
	public void setCandidateJobs(Set<CandidateJob> candidateJobs) {
		this.candidateJobs = candidateJobs;
	}

	public Job addCandidateJob(CandidateJob candidateJob) {
		this.candidateJobs.add(candidateJob);
		candidateJob.setJob(this);
		return this;
	}

	public Job removeCandidateJob(CandidateJob candidateJob) {
		this.candidateJobs.remove(candidateJob);
		candidateJob.setJob(null);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Job job = (Job) o;
		if (job.getId() == null || getId() == null) {
			return false;
		} 
		return Objects.equals(getId(), job.getId());

	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Job [id=" + id + ", jobTitle=" + jobTitle + ", jobDescription=" + jobDescription + ", noOfApplicants="
				+ noOfApplicants + ", salary=" + salary + ", jobStatus=" + jobStatus + ", createDate=" + createDate
				+ ", originalJobCost=" + originalJobCost + ", jobCost=" + jobCost + ", amountPaid=" + amountPaid
				+ ", totalAmountPaid=" + totalAmountPaid + ", noOfApplicantsBought=" + noOfApplicantsBought
				+ ", removedFilterAmount=" + removedFilterAmount + ", additionalFilterAmount=" + additionalFilterAmount
				+ ", adminCharge=" + adminCharge + ", adminChargeRate=" + adminChargeRate + ", upfrontDiscountRate="
				+ upfrontDiscountRate + ", upfrontDiscountAmount=" + upfrontDiscountAmount + ", escrowAmountUsed="
				+ escrowAmountUsed + ", escrowAmountAdded=" + escrowAmountAdded + ", paymentType=" + paymentType
				+ ", hasBeenEdited=" + hasBeenEdited + ", everActive=" + everActive + ", canEdit=" + canEdit
				+ ", updateDate=" + updateDate + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", noOfApplicantLeft=" + noOfApplicantLeft + "]";
	}

	
	
}
