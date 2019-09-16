/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Job;

/**
 * @author abhinav
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class CandidateJobUnitTest {

	private CandidateJob candidateJob;
	private Candidate candidate;
	private Job job;
	private CandidateJob.CandidateJobId candidateJobId;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		candidate = new Candidate();
		job = new Job();
		//candidateJobId = new CandidateJob.CandidateJobId();
		candidateJob = new CandidateJob();
	}

	@Test
	public void testCandidateJobEquality() throws Exception {

	}

}
