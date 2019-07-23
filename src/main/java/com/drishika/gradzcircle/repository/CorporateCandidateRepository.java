/**
 * 
 */
package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.CorporateCandidate.CorporateCandidateJobId;

/**
 * @author abhinav
 *
 */
@Repository
public interface CorporateCandidateRepository extends JpaRepository<CorporateCandidate, CorporateCandidateJobId> {
	
	@Query("Select cc from CorporateCandidate cc where cc.corporate.id=?1 and cc.candidate.id=?2")
	List<CorporateCandidate> findJobThatCorporateShortListedCandidateFor(Long corporateId, Long candidateId);

}
