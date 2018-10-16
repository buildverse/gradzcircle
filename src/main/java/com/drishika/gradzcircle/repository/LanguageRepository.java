package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Language;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

	public Language findByLanguage(String language);

}
