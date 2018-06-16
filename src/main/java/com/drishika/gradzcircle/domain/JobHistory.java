package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.drishika.gradzcircle.domain.enumeration.PaymentType;

/**
 * A JobHistory.
 */
@Entity
@Table(name = "job_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jobhistory")
public class JobHistory implements Serializable {

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

    @ManyToOne
    private Job job;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public JobHistory jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public JobHistory jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Integer getNoOfApplicants() {
        return noOfApplicants;
    }

    public JobHistory noOfApplicants(Integer noOfApplicants) {
        this.noOfApplicants = noOfApplicants;
        return this;
    }

    public void setNoOfApplicants(Integer noOfApplicants) {
        this.noOfApplicants = noOfApplicants;
    }

    public Double getSalary() {
        return salary;
    }

    public JobHistory salary(Double salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public JobHistory jobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public JobHistory createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Double getOriginalJobCost() {
        return originalJobCost;
    }

    public JobHistory originalJobCost(Double originalJobCost) {
        this.originalJobCost = originalJobCost;
        return this;
    }

    public void setOriginalJobCost(Double originalJobCost) {
        this.originalJobCost = originalJobCost;
    }

    public Double getJobCost() {
        return jobCost;
    }

    public JobHistory jobCost(Double jobCost) {
        this.jobCost = jobCost;
        return this;
    }

    public void setJobCost(Double jobCost) {
        this.jobCost = jobCost;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public JobHistory amountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
        return this;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public JobHistory totalAmountPaid(Double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
        return this;
    }

    public void setTotalAmountPaid(Double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public Integer getNoOfApplicantsBought() {
        return noOfApplicantsBought;
    }

    public JobHistory noOfApplicantsBought(Integer noOfApplicantsBought) {
        this.noOfApplicantsBought = noOfApplicantsBought;
        return this;
    }

    public void setNoOfApplicantsBought(Integer noOfApplicantsBought) {
        this.noOfApplicantsBought = noOfApplicantsBought;
    }

    public Double getRemovedFilterAmount() {
        return removedFilterAmount;
    }

    public JobHistory removedFilterAmount(Double removedFilterAmount) {
        this.removedFilterAmount = removedFilterAmount;
        return this;
    }

    public void setRemovedFilterAmount(Double removedFilterAmount) {
        this.removedFilterAmount = removedFilterAmount;
    }

    public Double getAdditionalFilterAmount() {
        return additionalFilterAmount;
    }

    public JobHistory additionalFilterAmount(Double additionalFilterAmount) {
        this.additionalFilterAmount = additionalFilterAmount;
        return this;
    }

    public void setAdditionalFilterAmount(Double additionalFilterAmount) {
        this.additionalFilterAmount = additionalFilterAmount;
    }

    public Double getAdminCharge() {
        return adminCharge;
    }

    public JobHistory adminCharge(Double adminCharge) {
        this.adminCharge = adminCharge;
        return this;
    }

    public void setAdminCharge(Double adminCharge) {
        this.adminCharge = adminCharge;
    }

    public Double getAdminChargeRate() {
        return adminChargeRate;
    }

    public JobHistory adminChargeRate(Double adminChargeRate) {
        this.adminChargeRate = adminChargeRate;
        return this;
    }

    public void setAdminChargeRate(Double adminChargeRate) {
        this.adminChargeRate = adminChargeRate;
    }

    public Double getUpfrontDiscountRate() {
        return upfrontDiscountRate;
    }

    public JobHistory upfrontDiscountRate(Double upfrontDiscountRate) {
        this.upfrontDiscountRate = upfrontDiscountRate;
        return this;
    }

    public void setUpfrontDiscountRate(Double upfrontDiscountRate) {
        this.upfrontDiscountRate = upfrontDiscountRate;
    }

    public Double getUpfrontDiscountAmount() {
        return upfrontDiscountAmount;
    }

    public JobHistory upfrontDiscountAmount(Double upfrontDiscountAmount) {
        this.upfrontDiscountAmount = upfrontDiscountAmount;
        return this;
    }

    public void setUpfrontDiscountAmount(Double upfrontDiscountAmount) {
        this.upfrontDiscountAmount = upfrontDiscountAmount;
    }

    public Double getEscrowAmountUsed() {
        return escrowAmountUsed;
    }

    public JobHistory escrowAmountUsed(Double escrowAmountUsed) {
        this.escrowAmountUsed = escrowAmountUsed;
        return this;
    }

    public void setEscrowAmountUsed(Double escrowAmountUsed) {
        this.escrowAmountUsed = escrowAmountUsed;
    }

    public Double getEscrowAmountAdded() {
        return escrowAmountAdded;
    }

    public JobHistory escrowAmountAdded(Double escrowAmountAdded) {
        this.escrowAmountAdded = escrowAmountAdded;
        return this;
    }

    public void setEscrowAmountAdded(Double escrowAmountAdded) {
        this.escrowAmountAdded = escrowAmountAdded;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public JobHistory paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean isHasBeenEdited() {
        return hasBeenEdited;
    }

    public JobHistory hasBeenEdited(Boolean hasBeenEdited) {
        this.hasBeenEdited = hasBeenEdited;
        return this;
    }

    public void setHasBeenEdited(Boolean hasBeenEdited) {
        this.hasBeenEdited = hasBeenEdited;
    }

    public Boolean isEverActive() {
        return everActive;
    }

    public JobHistory everActive(Boolean everActive) {
        this.everActive = everActive;
        return this;
    }

    public void setEverActive(Boolean everActive) {
        this.everActive = everActive;
    }

    public Boolean isCanEdit() {
        return canEdit;
    }

    public JobHistory canEdit(Boolean canEdit) {
        this.canEdit = canEdit;
        return this;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    
    /**
	 * @return the hasBeenEdited
	 */
	public Boolean getHasBeenEdited() {
		return hasBeenEdited;
	}

	/**
	 * @return the everActive
	 */
	public Boolean getEverActive() {
		return everActive;
	}

	/**
	 * @return the canEdit
	 */
	public Boolean getCanEdit() {
		return canEdit;
	}

	public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public JobHistory updateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public JobHistory createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public JobHistory updatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Job getJob() {
        return job;
    }

    public JobHistory job(Job job) {
        this.job = job;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobHistory jobHistory = (JobHistory) o;
        if (jobHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jobHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JobHistory{" +
            "id=" + getId() +
            ", jobTitle='" + getJobTitle() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", noOfApplicants='" + getNoOfApplicants() + "'" +
            ", salary='" + getSalary() + "'" +
            ", jobStatus='" + getJobStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", originalJobCost='" + getOriginalJobCost() + "'" +
            ", jobCost='" + getJobCost() + "'" +
            ", amountPaid='" + getAmountPaid() + "'" +
            ", totalAmountPaid='" + getTotalAmountPaid() + "'" +
            ", noOfApplicantsBought='" + getNoOfApplicantsBought() + "'" +
            ", removedFilterAmount='" + getRemovedFilterAmount() + "'" +
            ", additionalFilterAmount='" + getAdditionalFilterAmount() + "'" +
            ", adminCharge='" + getAdminCharge() + "'" +
            ", adminChargeRate='" + getAdminChargeRate() + "'" +
            ", upfrontDiscountRate='" + getUpfrontDiscountRate() + "'" +
            ", upfrontDiscountAmount='" + getUpfrontDiscountAmount() + "'" +
            ", escrowAmountUsed='" + getEscrowAmountUsed() + "'" +
            ", escrowAmountAdded='" + getEscrowAmountAdded() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", hasBeenEdited='" + isHasBeenEdited() + "'" +
            ", everActive='" + isEverActive() + "'" +
            ", canEdit='" + isCanEdit() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
