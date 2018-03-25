package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.drishika.gradzcircle.domain.enumeration.PaymentType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
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

	@Column(name = "job_cost")
	private Double jobCost;

	@Column(name = "amount_paid")
	private Double amountPaid;

	@Column(name = "additional_amount_charge")
	private Double additionalAmountCharge;

	@Column(name = "escrow_amount_used")
	private Double escrowAmountUsed;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Column(name = "can_edit")
	private Boolean canEdit;

	@Column(name = "has_been_edited")
	private Boolean hasBeenEdited;

	@Column(name = "ever_active")
	private Boolean everActive;

	@Column(name = "update_date")
	private ZonedDateTime updateDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	@OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<JobFilter> jobFilters = new HashSet<>();

	@OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<JobHistory> histories = new HashSet<>();

	@ManyToOne
	private JobType jobType;

	@ManyToOne
	private EmploymentType employmentType;

	@ManyToOne
	private Corporate corporate;

	@ManyToMany(mappedBy = "jobs")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Candidate> candidates = new HashSet<>();

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

	public Double getAdditionalAmountCharge() {
		return additionalAmountCharge;
	}

	public Job additionalAmountCharge(Double additionalAmountCharge) {
		this.additionalAmountCharge = additionalAmountCharge;
		return this;
	}

	public void setAdditionalAmountCharge(Double additionalAmountCharge) {
		this.additionalAmountCharge = additionalAmountCharge;
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

	public Boolean isCanEdit() {
		return canEdit;
	}

	public Job canEdit(Boolean canEdit) {
		this.canEdit = canEdit;
		return this;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Boolean isHasBeenEdited() {
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

	public Job everActive(Boolean everActive) {
		this.everActive = everActive;
		return this;
	}

	public void setEverActive(Boolean everActive) {
		this.everActive = everActive;
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

	public Set<Candidate> getCandidates() {
		return candidates;
	}

	public Job candidates(Set<Candidate> candidates) {
		this.candidates = candidates;
		return this;
	}

	public Job addCandidate(Candidate candidate) {
		this.candidates.add(candidate);
		candidate.getJobs().add(this);
		return this;
	}

	public Job removeCandidate(Candidate candidate) {
		this.candidates.remove(candidate);
		candidate.getJobs().remove(this);
		return this;
	}

	public void setCandidates(Set<Candidate> candidates) {
		this.candidates = candidates;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

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
		} else if (getId().equals(job.getId()) && getJobTitle().equals(job.getJobTitle())
				&& getJobDescription().equals(job.getJobDescription()) && getSalary().equals(job.getSalary())
				&& getJobStatus().equals(job.getJobStatus()))
			return true;
		return false;

	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Job{" + "id=" + getId() + ", jobTitle='" + getJobTitle() + "'" + ", jobDescription='"
				+ getJobDescription() + "'" + ", noOfApplicants='" + getNoOfApplicants() + "'" + ", salary='"
				+ getSalary() + "'" + ", jobStatus='" + getJobStatus() + "'" + ", createDate='" + getCreateDate() + "'"
				+ ", jobCost='" + getJobCost() + "'" + ", amountPaid='" + getAmountPaid() + "'"
				+ ", additionalAmountCharge='" + getAdditionalAmountCharge() + "'" + ", escrowAmountUsed='"
				+ getEscrowAmountUsed() + "'" + ", paymentType='" + getPaymentType() + "'" + ", hasBeenEdited='"
				+ isHasBeenEdited() + "'" + ", everActive='" + isEverActive() + "'" + ", canEdit='" + isCanEdit() + "'"
				+ ", updateDate='" + getUpdateDate() + "'" + ", createdBy='" + getCreatedBy() + "'" + ", updatedBy='"
				+ getUpdatedBy() + "'" + "}";
	}

}
