/**
 * 
 */
package com.drishika.gradzcircle.service.filter;

import java.util.List;

/**
 * @author abhinav
 *
 */
public class OrCriteria<T> implements Criteria<T> {

	private Criteria<T> criteria;
	private Criteria<T> otherCriteria;

	public OrCriteria(Criteria<T> criteria, Criteria<T> otherCriteria) {
		this.criteria = criteria;
		this.otherCriteria = otherCriteria;
	}

	@Override
	public List<T> meetCriteria(List<T> t) {
		List<T> firstCriteriaItems = criteria.meetCriteria(t);
		List<T> otherCriteriaItems = otherCriteria.meetCriteria(t);

		for (T k : otherCriteriaItems) {
			if (!firstCriteriaItems.contains(k)) {
				firstCriteriaItems.add(k);
			}
		}
		return firstCriteriaItems;
	}

}
