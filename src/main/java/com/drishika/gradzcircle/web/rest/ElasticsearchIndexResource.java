package com.drishika.gradzcircle.web.rest;

import java.net.URISyntaxException;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.drishika.gradzcircle.security.AuthoritiesConstants;
import com.drishika.gradzcircle.security.SecurityUtils;
import com.drishika.gradzcircle.service.ElasticsearchIndexService;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Elasticsearch index.
 */
@RestController
@RequestMapping("/api")
public class ElasticsearchIndexResource {

	private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexResource.class);

	private final ElasticsearchIndexService elasticsearchIndexService;

	public ElasticsearchIndexResource(ElasticsearchIndexService elasticsearchIndexService) {
		this.elasticsearchIndexService = elasticsearchIndexService;
	}

	/**
	 * POST /elasticsearch/index -> Reindex all Elasticsearch documents
	 */
	@RequestMapping(value = "/elasticsearch/index/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
	public ResponseEntity<Void> reindex() throws URISyntaxException {
		log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
		elasticsearchIndexService.reindexAll();
		return ResponseEntity.accepted().headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
				.build();
	}
	
	/**
	 * POST /elasticsearch/index -> Reindex all Elasticsearch documents
	 * 
	 */
	//TODO fix to pass correct message back in case of exception 
	@RequestMapping(value = "/elasticsearch/indexByEntity/{entityName}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
	public ResponseEntity<Void> reindexByEntity(@PathVariable String entityName) throws URISyntaxException {
		log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
		try {
			elasticsearchIndexService.reindexByEntityName(entityName);
			return ResponseEntity.accepted().headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
					.build();
		} catch(EntityNotFoundException enf) {
			log.error(enf.getMessage());
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(entityName, enf.getLocalizedMessage(),enf.getMessage()))
					.build();
		}
		
		
	}
	
	@RequestMapping(value = "/elasticsearch/removeByEntity/{entityName}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
	public ResponseEntity<Void> removeindexByEntity(@PathVariable String entityName) throws URISyntaxException {
		log.info("REST request to remove {} index Elasticsearch by user : {}", entityName,SecurityUtils.getCurrentUserLogin());
		try {
			elasticsearchIndexService.removeIndex(entityName);
			return ResponseEntity.accepted().headers(HeaderUtil.createAlert("elasticsearch.index.remove.accepted", null))
					.build();
		} catch(EntityNotFoundException enf) {
			log.error(enf.getMessage());
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(entityName, enf.getLocalizedMessage(),enf.getMessage()))
					.build();
		}
		
		
	}
}
