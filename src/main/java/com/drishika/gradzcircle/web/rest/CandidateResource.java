package com.drishika.gradzcircle.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.service.CandidateService;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Candidate.
 */
@RestController
@RequestMapping("/api")
public class CandidateResource {

	private final Logger log = LoggerFactory.getLogger(CandidateResource.class);

	private static final String ENTITY_NAME = "candidate";

	private final CandidateService candidateService;

	public CandidateResource(CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	/**
	 * POST /candidates : Create a new candidate.
	 *
	 * @param candidate
	 *            the candidate to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         candidate, or with status 400 (Bad Request) if the candidate has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
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
		Candidate result = candidateService.createCandidate(candidate);
		return ResponseEntity.created(new URI("/api/candidates/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /candidates : Updates an existing candidate.
	 *
	 * @param candidate
	 *            the candidate to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         candidate, or with status 400 (Bad Request) if the candidate is not
	 *         valid, or with status 500 (Internal Server Error) if the candidate
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/candidates")
	@Timed
	public ResponseEntity<Candidate> updateCandidate(@RequestBody Candidate candidate) throws URISyntaxException {
		log.debug("REST request to update Candidate : {}", candidate);
		if (candidate.getId() == null) {
			return createCandidate(candidate);
		}
		log.debug("Saving {} with addres {}", candidate, candidate.getAddresses());

		Candidate result = candidateService.updateCandidate(candidate);

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidate.getId().toString())).body(result);
	}

	/**
	 * GET /candidates : get all the candidates.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of candidates in
	 *         body
	 */
	@GetMapping("/candidates")
	@Timed
	public List<Candidate> getAllCandidates() {
		log.debug("REST request to get all Candidates");
		return candidateService.getAllCandidates();
	}

	/**
	 * GET /candidates/:id : get the "id" candidate.
	 *
	 * @param id
	 *            the id of the candidate to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the candidate,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/candidates/{id}")
	@Timed
	public ResponseEntity<Candidate> getCandidate(@PathVariable Long id) {
		log.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateService.getCandidate(id);
		if(candidate!= null)
			trimCandidateAddressData(candidate.getAddresses());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
	}

	/**
	 * GET /candidates/:id : get the "id" candidate.
	 *
	 * @param id
	 *            the id of the candidate to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the candidate,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/candidateByLogin/{id}")
	@Timed
	public ResponseEntity<Candidate> getCandidateByLoginId(@PathVariable Long id) {
		log.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateService.getCandidateByLoginId(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
	}

	/**
	 * DELETE /candidates/:id : delete the "id" candidate.
	 *
	 * @param id
	 *            the id of the candidate to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/candidates/{id}")
	@Timed
	public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
		log.debug("REST request to delete Candidate : {}", id);
		candidateService.deleteCandidate(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/candidates?query=:query : search for the candidate
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the candidate search
	 * @return the result of the search
	 */
	@GetMapping("/_search/candidates")
	@Timed
	public List<Candidate> searchCandidates(@RequestParam String query) {
		log.debug("REST request to search Candidates for query {}", query);
		return candidateService.searchCandidates(query);
	}
	
	@GetMapping("/candidatePublicProfile/{id}")
	@Timed
	public ResponseEntity<Candidate>getCandidatePubliProfile(@PathVariable Long id) {
		log.debug("REquest to get Candidate Public Profile non ElasticSearch");
		Candidate candidate = candidateService.getCandidatePublicProfile(id);
		trimCandidateAddressData(candidate.getAddresses());
		trimCandidateEducationData(candidate.getEducations());
		trimCandidateEmploymentData(candidate.getEmployments());
		trimCandidateCertifications(candidate.getCertifications());
		trimCandidateLanguageProficienies(candidate.getCandidateLanguageProficiencies());
		trimCandidateNonAcademics(candidate.getNonAcademics());
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));
		
	}

	/**
	 * SEARCH /_search/candidates?query=:query : API to get consolidate candidate
	 * Profile f
	 *
	 * @param query
	 *            the query of the candidate search
	 * @return the result of the search
	 */
	@GetMapping("/candidates/public-profile")
	@Timed
	public ResponseEntity<Candidate> retrieveCandidatePublicProfile(@RequestParam String query) {
		log.debug("REST request to get Candidate public profile for query {}", query);
		Candidate candidate = candidateService.retrieveCandidatePublicProfile(query);
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
					address.setCandidate(null);
				}
			});
		}

	}

}
