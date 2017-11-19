package com.drishika.gradzcircle.repository;

import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    public Set<Address> findAddressByCandidate(Candidate candiate);

}
