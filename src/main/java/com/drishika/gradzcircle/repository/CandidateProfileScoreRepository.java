/**
 * 
 */
package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.drishika.gradzcircle.domain.CandidateProfileScore;
import com.drishika.gradzcircle.domain.CandidateProfileScore.CandidateProfileScoreId;

/**
 * @author abhinav
 *
 */
public interface CandidateProfileScoreRepository extends JpaRepository <CandidateProfileScore,CandidateProfileScoreId>{
	
	@Modifying
	@Query("Delete from CandidateProfileScore cps where cps.candidate.id=?1 and cps.profileCategory.id=?2")
	void deleteByCandidateAndProfileId(Long candidateId, Long profileId);

}
