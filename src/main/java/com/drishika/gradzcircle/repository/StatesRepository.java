package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.States;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the States entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatesRepository extends JpaRepository<States, Long> {

}
