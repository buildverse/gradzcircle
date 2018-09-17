/**
 * 
 */
package com.drishika.gradzcircle.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import com.drishika.gradzcircle.domain.CandidateJob;

/**
 * @author abhinav
 *
 */
@Repository
public interface CandidateJobRepository extends JpaRepository <CandidateJob,CandidateJob.CandidateJobId>{

}
