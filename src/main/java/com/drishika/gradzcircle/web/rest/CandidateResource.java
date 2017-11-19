package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.search.AddressSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Candidate.
 */
@RestController
@RequestMapping("/api")
public class CandidateResource {

    private final Logger log = LoggerFactory.getLogger(CandidateResource.class);

    private static final String ENTITY_NAME = "candidate";

    private final CandidateRepository candidateRepository;

    private final CandidateSearchRepository candidateSearchRepository;

    private final CandidateEducationSearchRepository candidateEducationSearchRepository;

    private final CandidateProjectSearchRepository candidateProjectSearchRepository;

    private final CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

    private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;

    private final CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;

    private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

    private final AddressSearchRepository addressSearchRepository;

    private final AddressRepository addressRepository;

    public CandidateResource(CandidateRepository candidateRepository,
            CandidateSearchRepository candidateSearchRepository,
            CandidateEducationSearchRepository candidateEducationSearchRepository,
            CandidateProjectSearchRepository candidateProjectSearchRepository,
            CandidateEmploymentSearchRepository candidateEmploymentSearchRepository,
            CandidateCertificationSearchRepository candidateCertificationSearchRepository,
            CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository,
            CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository,
            AddressRepository addressRepository, AddressSearchRepository addressSearchRepository) {
        this.candidateRepository = candidateRepository;
        this.candidateSearchRepository = candidateSearchRepository;
        this.addressRepository = addressRepository;
        this.addressSearchRepository = addressSearchRepository;
        this.candidateCertificationSearchRepository = candidateCertificationSearchRepository;
        this.candidateEducationSearchRepository = candidateEducationSearchRepository;
        this.candidateEmploymentSearchRepository = candidateEmploymentSearchRepository;
        this.candidateProjectSearchRepository = candidateProjectSearchRepository;
        this.candidateNonAcademicWorkSearchRepository = candidateNonAcademicWorkSearchRepository;
        this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
    }

    /**
     * POST  /candidates : Create a new candidate.
     *
     * @param candidate the candidate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidate, or with status 400 (Bad Request) if the candidate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/candidates")
    @Timed
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) throws URISyntaxException {
        log.debug("REST request to save Candidate : {}", candidate);
        if (candidate.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new candidate cannot already have an ID"))
                    .body(null);
        }
        Candidate result = candidateRepository.save(candidate);
        candidateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/candidates/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /candidates : Updates an existing candidate.
     *
     * @param candidate the candidate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidate,
     * or with status 400 (Bad Request) if the candidate is not valid,
     * or with status 500 (Internal Server Error) if the candidate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/candidates")
    @Timed
    public ResponseEntity<Candidate> updateCandidate(@RequestBody Candidate candidate) throws URISyntaxException {
        log.debug("REST request to update Candidate : {}", candidate);
        if (candidate.getId() == null) {
            return createCandidate(candidate);
        }
        //Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
        //Set<Address> candidateAddresses = candidate.getAddresses();
        /*log.debug("Incoming address from user is {}",candidateAddresses);
        if(addresses==null && candidateAddresses!=null){
            log.debug("no address stored earlier {}",candidateAddresses);
            candidateAddresses.forEach(candidateAddress-> candidate.addAddress(candidateAddress));
        }else if (addresses != null && candidateAddresses == null){
            log.debug("address stored earlier, but no new coming in {}",candidateAddresses);
            addresses.forEach(address -> candidate.removeAddress(address));
        }else if(addresses!=null && candidateAddresses != null && !addresses.containsAll(candidateAddresses)) {
            log.debug("When have both {}",candidateAddresses);
            candidate.getAddresses().clear();    
            candidateAddresses.forEach(candidateAddress -> candidate.addAddress(candidateAddress));
            log.debug("Savig {} with addres {}",candidate,candidate.getAddresses());
        }*/
        log.debug("Saving {} with addres {}",candidate,candidate.getAddresses());
        candidate.getAddresses().forEach(candidateAddress -> candidateAddress.setCandidate(candidate));
        Candidate result = candidateRepository.save(candidate);
        candidateSearchRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidate.getId().toString())).body(result);
    }

    /**
     * GET  /candidates : get all the candidates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of candidates in body
     */
    @GetMapping("/candidates")
    @Timed
    public List<Candidate> getAllCandidates() {
        log.debug("REST request to get all Candidates");
        return candidateRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /candidates/:id : get the "id" candidate.
     *
     * @param id the id of the candidate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidate, or with status 404 (Not Found)
     */
    @GetMapping("/candidates/{id}")
    @Timed
    public ResponseEntity<Candidate> getCandidate(@PathVariable Long id) {
        log.debug("REST request to get Candidate : {}", id);
        Candidate candidate = candidateRepository.findOneWithEagerRelationships(id);
        Set <Address> addresses = addressRepository.findAddressByCandidate(candidate);
        addresses.forEach(candidateAddress->{
            candidateAddress.setCandidate(null);
        });
        candidate.setAddresses(addresses);
        log.debug("Retruning candidate {}",candidate.getAddresses());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
    }

    /**
     * GET  /candidates/:id : get the "id" candidate.
     *
     * @param id the id of the candidate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidate, or with status 404 (Not Found)
     */
    @GetMapping("/candidateByLogin/{id}")
    @Timed
    public ResponseEntity<Candidate> getCandidateByLoginId(@PathVariable Long id) {
        log.debug("REST request to get Candidate : {}", id);
        Candidate candidate = candidateRepository.findByLoginId(new Long(id).longValue());
        Set <Address> addresses = addressRepository.findAddressByCandidate(candidate);
        candidate.setAddresses(addresses);
        log.debug("Retruning candidate {}",candidate.getAddresses());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
    }

    /**
     * DELETE  /candidates/:id : delete the "id" candidate.
     *
     * @param id the id of the candidate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/candidates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        log.debug("REST request to delete Candidate : {}", id);
        candidateRepository.delete(id);
        candidateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/candidates?query=:query : search for the candidate corresponding
     * to the query.
     *
     * @param query the query of the candidate search
     * @return the result of the search
     */
    @GetMapping("/_search/candidates")
    @Timed
    public List<Candidate> searchCandidates(@RequestParam String query) {
        log.debug("REST request to search Candidates for query {}", query);
        return StreamSupport.stream(candidateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * SEARCH  /_search/candidates?query=:query : API to get consolidate candidate Profile f
     *
     * @param query the query of the candidate search
     * @return the result of the search
     */
    @GetMapping("/candidates/public-profile")
    @Timed
    public ResponseEntity<Candidate> retrieveCandidatePublicProfile(@RequestParam String query) {
        log.debug("REST request to get Candidate public profile for query {}", query);
        Candidate candidate = candidateSearchRepository.findOne(Long.parseLong(query));
        if (candidate == null)
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
        Set<Address>addresses = addressRepository.findAddressByCandidate(candidate);
        Set<CandidateEducation> candidateEducations = StreamSupport
                .stream(candidateEducationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toSet());
        Set<CandidateEmployment> candidateEmployments = StreamSupport
                .stream(candidateEmploymentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toSet());
        
        if (candidateEducations != null & candidateEducations.size() > 0) {
            candidateEducations.forEach(candidateEducation -> {
                Set<CandidateProject> candidateEducationProjects = StreamSupport
                        .stream(candidateProjectSearchRepository
                                .search(queryStringQuery(candidateEducation.getId().toString())).spliterator(), false)
                        .collect(Collectors.toSet());
                candidateEducation.setProjects(candidateEducationProjects);
            });
        }
        if (candidateEmployments != null & candidateEmployments.size() > 0) {
            candidateEmployments.forEach(candidateEmployment -> {
                Set<CandidateProject> candidateEmploymentProjects = StreamSupport
                        .stream(candidateProjectSearchRepository
                                .search(queryStringQuery(candidateEmployment.getId().toString())).spliterator(), false)
                        .collect(Collectors.toSet());
                candidateEmployment.setProjects(candidateEmploymentProjects);
            });
        }

        Set<CandidateCertification> candidateCertifications = StreamSupport
                .stream(candidateCertificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toSet());
        Set<CandidateNonAcademicWork> candidateNonAcademicWorks = StreamSupport
                .stream(candidateNonAcademicWorkSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toSet());
        Set<CandidateLanguageProficiency> candidateLanguageProficiencies = StreamSupport
                .stream(candidateLanguageProficiencySearchRepository.search(queryStringQuery(query)).spliterator(),
                        false)
                .collect(Collectors.toSet());
        //log.debug("Education, employments, Projects, certs and extra are {},{},{},{},{}",candidateEducations,candidateEmployments,candidateCertifications,candidateNonAcademicWorks);
        trimCandidateAddressData(addresses);
        trimCandidateEducationData(candidateEducations);
        trimCandidateEmploymentData(candidateEmployments);
        trimCandidateCertifications(candidateCertifications);
        trimCandidateLanguageProficienies(candidateLanguageProficiencies);
        trimCandidateNonAcademics(candidateNonAcademicWorks);
        candidate.setAddresses(addresses);
        candidate.setEducations(candidateEducations);
        candidate.setEmployments(candidateEmployments);
        candidate.setCertifications(candidateCertifications);
        candidate.setNonAcademics(candidateNonAcademicWorks);
        candidate.setCandidateLanguageProficiencies(candidateLanguageProficiencies);
        //log.debug("Education, employments, Projects, certs and extra are {},{},{},{},{}",candidateEducations,candidateEmployments,candidateCertifications,candidateNonAcademicWorks);
        //log.debug("Candidate details are {}",candidate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));

    }

    private void trimCandidateCertifications(Set<CandidateCertification> certifications) {
        certifications.forEach(certification -> {
            certification.setCandidate(null);
        });
    }

    private void trimCandidateNonAcademics(Set<CandidateNonAcademicWork> nonAcademicWorks) {
        nonAcademicWorks.forEach(nonAcademicWork -> {
            nonAcademicWork.setCandidate(null);
        });
    }

    private void trimCandidateLanguageProficienies(Set<CandidateLanguageProficiency> languageProficiencies) {
        languageProficiencies.forEach(languageProficiency -> {
            languageProficiency.setCandidate(null);
        });
    }

    private void trimCandidateEmploymentData(Set<CandidateEmployment> candidateEmployments) {
        candidateEmployments.forEach(candidateEmployment -> {
            candidateEmployment.setCandidate(null);
            if (candidateEmployment.getProjects() != null) {
                candidateEmployment.getProjects().forEach(candidateProject -> {
                    candidateProject.setEmployment(null);
                });
            }
        });
    }

    private void trimCandidateEducationData(Set<CandidateEducation> candidateEducations) {
        candidateEducations.forEach(candidateEducation -> {
            candidateEducation.setCandidate(null);
            if (candidateEducation.getProjects() != null) {
                candidateEducation.getProjects().forEach(candidateProject -> {
                    candidateProject.setEducation(null);
                });
            }
        });
    }

    private void trimCandidateAddressData(Set<Address> addresses) {

        if (addresses != null) {
            addresses.forEach(address -> {
                if (address.getCountry() != null) {
                    address.getCountry().setCorporates(null);
                    address.getCountry().setVisas(null);
                    address.getCountry().setNationality(null);
                    address.getCountry().setAddresses(null);
                }
            });
        }

    }

}
