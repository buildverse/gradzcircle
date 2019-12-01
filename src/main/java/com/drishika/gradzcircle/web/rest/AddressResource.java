package com.drishika.gradzcircle.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.search.AddressSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Address.
 */
@RestController
@RequestMapping("/api")
public class AddressResource {

	private final Logger log = LoggerFactory.getLogger(AddressResource.class);

	private static final String ENTITY_NAME = "address";

	private final AddressRepository addressRepository;

	private final AddressSearchRepository addressSearchRepository;

	public AddressResource(AddressRepository addressRepository, AddressSearchRepository addressSearchRepository) {
		this.addressRepository = addressRepository;
		this.addressSearchRepository = addressSearchRepository;
	}

	/**
	 * POST /addresses : Create a new address.
	 *
	 * @param address
	 *            the address to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         address, or with status 400 (Bad Request) if the address has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/addresses")
	@Timed
	public ResponseEntity<Address> createAddress(@RequestBody Address address) throws URISyntaxException {
		log.debug("REST request to save Address : {}", address);
		if (address.getId() != null) {
			 throw new BadRequestAlertException("A new address cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Address result = addressRepository.save(address);
		addressSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /addresses : Updates an existing address.
	 *
	 * @param address
	 *            the address to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         address, or with status 400 (Bad Request) if the address is not
	 *         valid, or with status 500 (Internal Server Error) if the address
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/addresses")
	@Timed
	public ResponseEntity<Address> updateAddress(@RequestBody Address address) throws URISyntaxException {
		log.debug("REST request to update Address : {}", address);
		if (address.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
		Address result = addressRepository.save(address);
		addressSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, address.getId().toString()))
				.body(result);
	}

	/**
	 * GET /addresses : get all the addresses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of addresses in
	 *         body
	 */
	@GetMapping("/addresses")
	@Timed
	public List<Address> getAllAddresses() {
		log.debug("REST request to get all Addresses");
		return addressRepository.findAll();
	}

	/**
	 * GET /addresses/:id : get the "id" address.
	 *
	 * @param id
	 *            the id of the address to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the address, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/addresses/{id}")
	@Timed
	public ResponseEntity<Address> getAddress(@PathVariable Long id) {
		log.debug("REST request to get Address : {}", id);
		Optional<Address> address = addressRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(address);
	}

	/**
	 * DELETE /addresses/:id : delete the "id" address.
	 *
	 * @param id
	 *            the id of the address to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/addresses/{id}")
	@Timed
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		log.debug("REST request to delete Address : {}", id);
		addressRepository.deleteById(id);
		addressSearchRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/addresses?query=:query : search for the address corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the address search
	 * @return the result of the search
	 */
	@GetMapping("/_search/addresses")
	@Timed
	public List<Address> searchAddresses(@RequestParam String query) {
		log.debug("REST request to search Addresses for query {}", query);
		return StreamSupport.stream(addressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
