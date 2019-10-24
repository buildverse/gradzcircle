package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.CandidateSkills;


/**
 * Spring Data JPA repository for the CandidateSkills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateSkillsRepository extends JpaRepository<CandidateSkills, Long> {
	
	@Query("Select skill from CandidateSkills skill where skill.candidate.id=?1")
	public List<CandidateSkills> findSkillsForCandidate(Long id);
	
	@Query("Select sk from CandidateSkills sk where sk.candidate.id=?1 and sk.skills.skill=?2")
	public CandidateSkills findCandidateSkillPresent(Long id, String skill);

}
