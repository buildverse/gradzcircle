package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Candidate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Candidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("select distinct candidate from Candidate candidate left join fetch candidate.jobCategories left join fetch candidate.jobs")
    List<Candidate> findAllWithEagerRelationships();

    @Query("select candidate from Candidate candidate left join fetch candidate.jobCategories left join fetch candidate.jobs where candidate.id =:id")
    Candidate findOneWithEagerRelationships(@Param("id") Long id);

    Candidate findByLoginId(@Param("uid") Long uid);


}
