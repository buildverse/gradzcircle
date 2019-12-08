package com.drishika.gradzcircle.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.drishika.gradzcircle.service.CandidateService;
import com.drishika.gradzcircle.service.dto.CandidateDTO;
import com.drishika.gradzcircle.service.dto.CandidateDetailDTO;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CandidatePublicProfileDTO;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import com.drishika.gradzcircle.web.rest.util.PaginationUtil;

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
		if(result == null)
			throw new BadRequestAlertException("Unable to update Candidate as none exists", ENTITY_NAME, "idexists");
		candidateService.updateCountryAndNationalityDataForDisplay(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidate.getId().toString())).body(result);
	}

	/**
	 * PUT /candidates_apply : Candidate Applies to Job
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
	@PutMapping("/candidate-apply")
	@Timed
	public ResponseEntity<Candidate> applyJob(@RequestBody Candidate candidate) throws URISyntaxException {
		log.debug("REST request to apply for job {} for Candidate : {}", candidate.getAppliedJobs(), candidate);
		if (candidate.getAppliedJobs() == null) {
			return null;
		}
		Candidate result = candidateService.applyJob(candidate);

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, candidate.getId().toString())).body(result);
	}

	/**
	 * PUT /candidates_shortlist : Corporate shortlists Candidate for a Job
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
	@GetMapping("/candidate-corporate-link/{candidateId}/{jobId}/{corporateId}")
	@Timed
	public ResponseEntity<Candidate> shortListCandidateForJob(@PathVariable("candidateId") Long candidateId,
			@PathVariable("jobId") Long jobId, @PathVariable("corporateId") Long corporateId)
			throws URISyntaxException {
		log.debug("REST request to shortlist candidate {} for job {} by corporate : {}", candidateId, jobId,
				corporateId);
		Candidate result = candidateService.shortListCandidateForJob(candidateId, jobId, corporateId);
		if(result == null)
			throw new BadRequestAlertException("No match exists to shortlist",ENTITY_NAME,"");
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
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
	 * GET /candidatesPreview : get all the candidates.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of candidates in
	 *         body
	 */
	@GetMapping("/candidatesPreview")
	@Timed
	public ResponseEntity<List<CandidateProfileListDTO>> getAllCandidatesForPreview(Pageable pageable) {
		log.debug("REST request to get  Candidates for Preview");
		final Page<CandidateProfileListDTO> page = candidateService.getAllCandidatesForPreview(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/candidatesPreview");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		
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
		if (candidate != null)
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
	public ResponseEntity<CandidateDetailDTO> getCandidateByLoginId(@PathVariable Long id) {
		log.debug("REST request to get Candidate by Login id for : {}", id);
		Candidate candidate = candidateService.getCandidateByLoginId(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateService.setCandidateDetails(candidate)));
	}
	
	/**
	 * GET /candidates/:id : get the "id" candidate.
	 *
	 * @param id
	 *            the id of the candidate to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the candidate,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/candidateById/{id}")
	@Timed
	public ResponseEntity<CandidateDetailDTO> getCandidateDetailsById(@PathVariable Long id) {
		log.debug("REST request to get Candidate by Id for  : {}", id);
		Candidate candidate = candidateService.getCandidate(id);
		candidateService.updateCountryAndNationalityDataForDisplay(candidate);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateService.convertToCandidateDetailDTO(candidate)));
	}


	/**
	 * GET /candidateDetails/:id : get the "id" candidate.
	 *
	 * @param id
	 *            the id of the candidate to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the candidate,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/candidateDetails/{id}")
	@Timed
	public ResponseEntity<CandidateDetailDTO> getCandidateDetails(@PathVariable Long id) {
		log.info("REST request to get Candidate Details for profile page ");
		
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidateService.getCandidateDetails(id)));
		
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

	@GetMapping("/candidates/candidatePublicProfile/{candidateId}/{jobId}/{corporateId}")
	@Timed
	public ResponseEntity<CandidatePublicProfileDTO> getCandidatePubliProfile(@PathVariable Long candidateId,
			@PathVariable Long jobId, @PathVariable Long corporateId) {
		log.debug("REquest to get Candidate Public Profile non ElasticSearch");
		CandidatePublicProfileDTO candidate = candidateService.getCandidatePublicProfile(candidateId, jobId,
				corporateId);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(candidate));

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
