package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.domain.ProfileCategory;

import com.drishika.gradzcircle.repository.ProfileCategoryRepository;
import com.drishika.gradzcircle.repository.search.ProfileCategorySearchRepository;
import com.drishika.gradzcircle.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing ProfileCategory.
 */
@RestController
@RequestMapping("/api")
public class ProfileCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProfileCategoryResource.class);

    private static final String ENTITY_NAME = "profileCategory";

    private final ProfileCategoryRepository profileCategoryRepository;

    private final ProfileCategorySearchRepository profileCategorySearchRepository;

    public ProfileCategoryResource(ProfileCategoryRepository profileCategoryRepository, ProfileCategorySearchRepository profileCategorySearchRepository) {
        this.profileCategoryRepository = profileCategoryRepository;
        this.profileCategorySearchRepository = profileCategorySearchRepository;
    }

    /**
     * POST  /profile-categories : Create a new profileCategory.
     *
     * @param profileCategory the profileCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profileCategory, or with status 400 (Bad Request) if the profileCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profile-categories")
    @Timed
    public ResponseEntity<ProfileCategory> createProfileCategory(@RequestBody ProfileCategory profileCategory) throws URISyntaxException {
        log.debug("REST request to save ProfileCategory : {}", profileCategory);
        if (profileCategory.getId() != null) {
            throw new BadRequestAlertException("A new profileCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfileCategory result = profileCategoryRepository.save(profileCategory);
        profileCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profile-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profile-categories : Updates an existing profileCategory.
     *
     * @param profileCategory the profileCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profileCategory,
     * or with status 400 (Bad Request) if the profileCategory is not valid,
     * or with status 500 (Internal Server Error) if the profileCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profile-categories")
    @Timed
    public ResponseEntity<ProfileCategory> updateProfileCategory(@RequestBody ProfileCategory profileCategory) throws URISyntaxException {
        log.debug("REST request to update ProfileCategory : {}", profileCategory);
        if (profileCategory.getId() == null) {
            return createProfileCategory(profileCategory);
        }
        ProfileCategory result = profileCategoryRepository.save(profileCategory);
        profileCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profileCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profile-categories : get all the profileCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profileCategories in body
     */
    @GetMapping("/profile-categories")
    @Timed
    public List<ProfileCategory> getAllProfileCategories() {
        log.debug("REST request to get all ProfileCategories");
        return profileCategoryRepository.findAll();
        }

    /**
     * GET  /profile-categories/:id : get the "id" profileCategory.
     *
     * @param id the id of the profileCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profileCategory, or with status 404 (Not Found)
     */
    @GetMapping("/profile-categories/{id}")
    @Timed
    public ResponseEntity<ProfileCategory> getProfileCategory(@PathVariable Long id) {
        log.debug("REST request to get ProfileCategory : {}", id);
        ProfileCategory profileCategory = profileCategoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profileCategory));
    }

    /**
     * DELETE  /profile-categories/:id : delete the "id" profileCategory.
     *
     * @param id the id of the profileCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profile-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfileCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProfileCategory : {}", id);
        profileCategoryRepository.delete(id);
        profileCategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profile-categories?query=:query : search for the profileCategory corresponding
     * to the query.
     *
     * @param query the query of the profileCategory search
     * @return the result of the search
     */
    @GetMapping("/_search/profile-categories")
    @Timed
    public List<ProfileCategory> searchProfileCategories(@RequestParam String query) {
        log.debug("REST request to search ProfileCategories for query {}", query);
        return StreamSupport
            .stream(profileCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
