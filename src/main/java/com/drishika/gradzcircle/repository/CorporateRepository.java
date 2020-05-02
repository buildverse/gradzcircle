package com.drishika.gradzcircle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;

/**
 * Spring Data JPA repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {

	Corporate findByLoginId(Long id);
	
	//FIXME - Need to change this to join this will fuck itself
	@Query("select candidate from Candidate candidate where candidate.id in (select distinct cc.id.candidateId from CorporateCandidate cc where cc.corporate.id=?1)")
	Page<Candidate> findLinkedCandidates(Long corporateId, Pageable pageable);
	
	@Query("select  count (distinct candidate) from CorporateCandidate corporateCandidate, Candidate candidate where candidate.id = corporateCandidate.id.candidateId and corporateCandidate.corporate.id=?1")
	Long findAllLinkedCandidates(Long corporateId);
	
	@Query("select count(corporateCandidate) from CorporateCandidate corporateCandidate where corporateCandidate.id.corporateId=?1 and corporateCandidate.id.jobId=?2")
	Long findCountOfCandidatesShortlistedByJob(Long corporateId,Long jobId);
	
	@Query("select corporateCandidate from CorporateCandidate corporateCandidate where corporateCandidate.id.jobId=?1")
	Page<CorporateCandidate> findLinkedCandidatesByJob(Long jobId, Pageable pageable);
	

}
