/**
 * Matcher implemnetation - Concrete Observer
 */
package com.drishika.gradzcircle.service.matching;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.service.CandidateEducationService;
import com.drishika.gradzcircle.service.JobService;

/**
 * @author abhinav
 *
 */
@Service
@Qualifier("CandidateCoreMatcher")
public class CandidateMatcher implements Matcher<Candidate> {
	
	private final JobService jobService;
	
	//private final CandidateEducationService candidateEducationService;
	
	private final List<JobFilter> jobFilters;

	/**
	 * 
	 */
	public CandidateMatcher(JobService jobService) {
		this.jobService = jobService;
		//this.candidateEducationService = candidateEducationService;
		this.jobFilters = new ArrayList<JobFilter>();
	}
	
	/* (non-Javadoc)
	 * @see com.drishika.gradzcircle.service.matching.Matcher#match()
	 */
	@Override
	public void match(Candidate candidate) {
		/*List<CandidateEducation> candidateEducations = candidateEducationService.getEducationByCandidateId(candidate.getId());
		if(candidateEducations == null ) {
			return;
		}*/
		
		List<Job> jobs = jobService.getAllActiveJobs();
		jobs.forEach(job->createFilterObject(job));
		
	}
	
	
	private void createFilterObject(Job job) {
		
	}

}
