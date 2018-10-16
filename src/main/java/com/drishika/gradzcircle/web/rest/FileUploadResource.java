package com.drishika.gradzcircle.web.rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import com.amazonaws.services.s3.model.S3Object;
import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.drishika.gradzcircle.service.storage.StorageService;
import com.drishika.gradzcircle.web.rest.util.HeaderUtil;
import java.io.File;
import java.util.List;
import javax.persistence.Transient;
import com.drishika.gradzcircle.service.storage.FileServiceS3;
import com.drishika.gradzcircle.service.storage.StorageFileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")

public class FileUploadResource {

	private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

	private final StorageService storageService;

	private final FileServiceS3 fileServiceS3;

	@Transient
	private File file;

	@Autowired
	public FileUploadResource(StorageService storageService, FileServiceS3 fileServiceS3) {
		this.fileServiceS3 = fileServiceS3;
		this.storageService = storageService;
	}

	@GetMapping("/fileList/{bucket}")
	@Timed
	public List listObjects(@PathVariable String bucket) {
		return fileServiceS3.listObjects(bucket);

	}

	@GetMapping("/files/{id}")
	@ResponseBody
	@Timed
	public ResponseEntity<List<Link>> serveFile(@PathVariable String id) {
		Resource resource = null;
		try {
			resource = fileServiceS3.getObject("gradzcircle-assets", id);
			log.debug("Resource is {}", resource);
		} catch (Exception e) {
			log.error("There was an error getting image {}", e);
		}
		if (resource != null)
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.hasLinks() + "\"")
					.body(resource.getLinks());
		else
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "File not Found").body(null);

	}

	@PostMapping("/upload/{id}")
	@ResponseBody
	@Timed
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile multiPartFile, @PathVariable Long id) {
		log.info("User id passesd is {} ", id);
		// storageService.store(file, id);
		try {
			fileServiceS3.uploadObject("gradzcircle-assets", id.toString(), multiPartFile);
		} catch (Exception e) {
			log.error("Error while uploading file { }", e);
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createAlert("Problem uploading image ", id.toString())).build();
		}
		return ResponseEntity.ok().headers(HeaderUtil.createAlert("Uploaded Image for ", id.toString())).build();
	}

	@DeleteMapping("/remove/{id}")
	@Timed
	public ResponseEntity removeImage(@PathVariable Long id) {
		log.info("User id passesd for removing image is {} ", id);
		// storageService.delete(id);
		try {
			fileServiceS3.deleteObject("gradzcircle-assets", id.toString());
		} catch (Exception e) {
			log.error("Error while removing file { }", e);
			return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("Problem Deleting image ", id.toString()))
					.build();
		}
		return ResponseEntity.ok().headers(HeaderUtil.createAlert("Removed Image for ", id.toString())).build();
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
