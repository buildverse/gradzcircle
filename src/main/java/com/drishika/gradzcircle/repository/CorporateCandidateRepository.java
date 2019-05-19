/**
 * 
 */
package com.drishika.gradzcircle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.CorporateCandidate.CorporateCandidateJobId;

/**
 * @author abhinav
 *
 */
/*@Repository
public interface CorporateCandidateRepository extends JpaRepository<CorporateCandidate, CorporateCandidateJobId> {
	
	Page<Candidate> findDistinctCandidateByCorporate(Long corporateId, Pageable pageable);

}*/
