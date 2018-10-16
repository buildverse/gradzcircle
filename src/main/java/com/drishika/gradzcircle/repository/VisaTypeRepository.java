package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.VisaType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the VisaType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisaTypeRepository extends JpaRepository<VisaType, Long> {

}
