package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Skills;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Skills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

}
