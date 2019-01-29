package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.ProfileCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProfileCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileCategoryRepository extends JpaRepository<ProfileCategory, Long> {

}
