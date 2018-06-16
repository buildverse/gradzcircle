/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.domain.CandidateEducation;

/**
 * @author abhinav
 *
 */
@Service
@Qualifier ("CandidateEducationMatcher")
public class CandidateEducationJobMatcher implements Matcher<CandidateEducation> {

	/* (non-Javadoc)
	 * @see com.drishika.gradzcircle.service.matching.Matcher#match(java.lang.Object)
	 */
	@Override
	public void match(CandidateEducation candidateEducation) {
		// TODO Auto-generated method stub
		
	}

}
