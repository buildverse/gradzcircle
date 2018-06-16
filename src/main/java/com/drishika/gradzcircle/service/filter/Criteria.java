/**
 * 
 */
package com.drishika.gradzcircle.service.filter;

import java.util.List;
import java.util.Map;

/**
 * @author abhinav
 *
 */
public interface Criteria<T> {
	public List<T> meetCriteria(List<T> t);
}
