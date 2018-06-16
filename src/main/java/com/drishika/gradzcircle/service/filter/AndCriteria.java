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
public class AndCriteria<T> implements Criteria<T> {

	private Criteria<T> criteria;
	private Criteria<T> otherCriteria;

	public AndCriteria(Criteria<T> criteria, Criteria<T> otherCriteria) {
		this.criteria = criteria;
		this.otherCriteria = otherCriteria;
	}

	@Override
	public List<T> meetCriteria(List<T> t) {
		List<T> firstCriteriaPersons = criteria.meetCriteria(t);
		return otherCriteria.meetCriteria(firstCriteriaPersons);
	}

}
