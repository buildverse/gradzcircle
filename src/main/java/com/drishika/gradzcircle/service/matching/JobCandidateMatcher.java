package com.drishika.gradzcircle.service.matching;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.domain.Job;

/**
 * @author abhinav
 *
 */
@Service
@Qualifier("JobCandidateMatcher")
public class JobCandidateMatcher implements Matcher<Job> {

	@Override
	public void match(Job job) {
		// TODO Auto-generated method stub

	}

}
