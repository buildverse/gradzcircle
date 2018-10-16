package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.AppConfig;

import com.drishika.gradzcircle.repository.AppConfigRepository;
import com.drishika.gradzcircle.repository.search.AppConfigSearchRepository;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AppConfig.
 */
@RestController
@RequestMapping("/api")
public class AppConfigResource {

	private final Logger log = LoggerFactory.getLogger(AppConfigResource.class);

	private static final String ENTITY_NAME = "appConfig";

	private final AppConfigRepository appConfigRepository;

	private final AppConfigSearchRepository appConfigSearchRepository;

	public AppConfigResource(AppConfigRepository appConfigRepository,
			AppConfigSearchRepository appConfigSearchRepository) {
		this.appConfigRepository = appConfigRepository;
		this.appConfigSearchRepository = appConfigSearchRepository;
	}

	/**
	 * POST /app-configs : Create a new appConfig.
	 *
	 * @param appConfig
	 *            the appConfig to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         appConfig, or with status 400 (Bad Request) if the appConfig has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/app-configs")
	@Timed
	public ResponseEntity<AppConfig> createAppConfig(@RequestBody AppConfig appConfig) throws URISyntaxException {
		log.debug("REST request to save AppConfig : {}", appConfig);
		if (appConfig.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appConfig cannot already have an ID"))
					.body(null);
		}
		AppConfig result = appConfigRepository.save(appConfig);
		appConfigSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/app-configs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /app-configs : Updates an existing appConfig.
	 *
	 * @param appConfig
	 *            the appConfig to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         appConfig, or with status 400 (Bad Request) if the appConfig is not
	 *         valid, or with status 500 (Internal Server Error) if the appConfig
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/app-configs")
	@Timed
	public ResponseEntity<AppConfig> updateAppConfig(@RequestBody AppConfig appConfig) throws URISyntaxException {
		log.debug("REST request to update AppConfig : {}", appConfig);
		if (appConfig.getId() == null) {
			return createAppConfig(appConfig);
		}
		AppConfig result = appConfigRepository.save(appConfig);
		appConfigSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appConfig.getId().toString())).body(result);
	}

	/**
	 * GET /app-configs : get all the appConfigs.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of appConfigs in
	 *         body
	 */
	@GetMapping("/app-configs")
	@Timed
	public List<AppConfig> getAllAppConfigs() {
		log.debug("REST request to get all AppConfigs");
		return appConfigRepository.findAll();
	}

	/**
	 * GET /app-configs/:id : get the "id" appConfig.
	 *
	 * @param id
	 *            the id of the appConfig to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the appConfig,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/app-configs/{id}")
	@Timed
	public ResponseEntity<AppConfig> getAppConfig(@PathVariable Long id) {
		log.debug("REST request to get AppConfig : {}", id);
		AppConfig appConfig = appConfigRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appConfig));
	}

	/**
	 * DELETE /app-configs/:id : delete the "id" appConfig.
	 *
	 * @param id
	 *            the id of the appConfig to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/app-configs/{id}")
	@Timed
	public ResponseEntity<Void> deleteAppConfig(@PathVariable Long id) {
		log.debug("REST request to delete AppConfig : {}", id);
		appConfigRepository.delete(id);
		appConfigSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/app-configs?query=:query : search for the appConfig
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the appConfig search
	 * @return the result of the search
	 */
	@GetMapping("/_search/app-configs")
	@Timed
	public List<AppConfig> searchAppConfigs(@RequestParam String query) {
		log.debug("REST request to search AppConfigs for query {}", query);
		return StreamSupport.stream(appConfigSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}
