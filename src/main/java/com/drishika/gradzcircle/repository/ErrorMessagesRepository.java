package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.ErrorMessages;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ErrorMessages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ErrorMessagesRepository extends JpaRepository<ErrorMessages, Long> {

}
