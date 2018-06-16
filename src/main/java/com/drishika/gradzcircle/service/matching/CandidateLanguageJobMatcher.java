/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;

/**
 * @author abhinav
 *
 */
@Service
@Qualifier("CandidateLaguageMatcher")
public class CandidateLanguageJobMatcher implements Matcher<CandidateLanguageProficiency> {

	/* (non-Javadoc)
	 * @see com.drishika.gradzcircle.service.matching.Matcher#match(java.lang.Object)
	 */
	@Override
	public void match(CandidateLanguageProficiency candidateLanguageProficiency) {
		// TODO Auto-generated method stub
		
	}

}
