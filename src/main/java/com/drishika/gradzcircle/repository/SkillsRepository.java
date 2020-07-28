package com.drishika.gradzcircle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drishika.gradzcircle.domain.Skills;


/**
 * Spring Data JPA repository for the Skills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {
	
	public Skills findBySkillIgnoreCase(String skill);
	
	@Query("Select s from Skills s where UPPER(s.skill) like UPPER(?1)")
	public List<Skills> getAllMatchingSkills(String skill);

}
