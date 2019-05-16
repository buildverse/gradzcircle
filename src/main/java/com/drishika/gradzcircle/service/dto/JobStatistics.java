/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;

/**
 * @author abhinav
 *
 */
public class JobStatistics implements Serializable {
	
	private String type;
	private Long count;

	
	public JobStatistics(String type,Long count) {
		this.type = type;
		this.count = count;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}


	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JobStatistics [type=" + type + ", count=" + count + "]";
	}
	
	
	
	
	

}
