/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class JobEconomicsDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean payAsYouGo;
	
	private Double filterCost;
	
	private Double jobCost;
	
	private Double corporateEscrowAmount;
	
	private Long id;
	

	/**
	 * @return the payAsYouGo
	 */
	public Boolean getPayAsYouGo() {
		return payAsYouGo;
	}

	/**
	 * @param payAsYouGo the payAsYouGo to set
	 */
	public void setPayAsYouGo(Boolean payAsYouGo) {
		this.payAsYouGo = payAsYouGo;
	}

	/**
	 * @return the filterCost
	 */
	public Double getFilterCost() {
		return filterCost;
	}

	/**
	 * @param filterCost the filterCost to set
	 */
	public void setFilterCost(Double filterCost) {
		this.filterCost = filterCost;
	}

	/**
	 * @return the jobCost
	 */
	public Double getJobCost() {
		return jobCost;
	}

	/**
	 * @param jobCost the jobCost to set
	 */
	public void setJobCost(Double jobCost) {
		this.jobCost = jobCost;
	}

	/**
	 * @return the corporateEscrowAmount
	 */
	public Double getCorporateEscrowAmount() {
		return corporateEscrowAmount;
	}

	/**
	 * @param corporateEscrowAmount the corporateEscrowAmount to set
	 */
	public void setCorporateEscrowAmount(Double corporateEscrowAmount) {
		this.corporateEscrowAmount = corporateEscrowAmount;
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
	
	
	

}
