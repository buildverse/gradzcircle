package com.drishika.gradzcircle.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.exception.FileRemoveException;
import com.drishika.gradzcircle.exception.FileRetrieveException;
import com.drishika.gradzcircle.exception.FileUploadException;
import com.drishika.gradzcircle.repository.UserRepository;
import com.drishika.gradzcircle.service.UserService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Upload a file to an Amazon S3 bucket.
 *
 * This code expects that you have AWS credentials set up per:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
@Service
@Transactional
public class FileServiceS3Impl implements FileServiceS3 {
	private UserRepository userRepository;
	private UserService userService;
	private AmazonS3 amazonS3Client;

	private static final Logger logger = LoggerFactory.getLogger(FileServiceS3.class);

	private void init() {
		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setConnectionTimeout(5000);
		configuration.setMaxConnections(1000);
		amazonS3Client = AmazonS3ClientBuilder.standard().withClientConfiguration(configuration)
				.withRegion(Constants.AWS_REGION).build();
	}

	public FileServiceS3Impl(UserRepository userRepository, UserService userService) {
		init();
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Override
	public List listObjects(String bucketName) {
		List objectList = amazonS3Client.listObjects(bucketName).getObjectSummaries();
		logger.info("Listing objects {}", amazonS3Client.listObjects(bucketName));
		return objectList;
	}

	@Override
	public void uploadObject(String bucketName, String key, MultipartFile file) throws FileUploadException {
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(file.getContentType());
			PutObjectResult result = amazonS3Client
					.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), objectMetadata)
							.withCannedAcl(CannedAccessControlList.PublicRead));
			User user = userRepository.getOne(Long.parseLong(key));
			user.setImageUrl(result.getETag());
			userService.updateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getLangKey(),
					user.getImageUrl());
			logger.info("Uploaded file .. All Good!!");
		} catch (AmazonServiceException asex) {
			logger.error("Error occuured during upload...{}", asex);
			throw new FileUploadException(asex.getErrorMessage());
		} catch (Exception e) {
			logger.error("Error occuured during upload...{}", e);
			throw new FileUploadException(e.getMessage());
		}

	}

	@Override
	public void deleteObject(String bucketName, String key) throws FileRemoveException {
		try {
			amazonS3Client.deleteObject(bucketName, key);
			User user = userRepository.getOne(Long.parseLong(key));
			user.setImageUrl(null);
			userService.updateUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getLangKey(),
					user.getImageUrl());
			logger.info("Deleted file .. All Good!!");
		} catch (AmazonServiceException asex) {
			logger.error("Error occuured during delete...{ }", asex);
			throw new FileRemoveException(asex.getErrorMessage());
		}

	}

	@Override
	public Resource getObject(String bucketName, String key) throws FileRetrieveException {
		Resource file = null;
			try (S3Object s3Object = amazonS3Client.getObject(bucketName, key)) {
			file = new Resource(s3Object,
					new Link(String.format("https://s3-%s.amazonaws.com/%s/%s",
							amazonS3Client.getBucketLocation(bucketName), s3Object.getBucketName(), s3Object.getKey()))
									.withRel("url"));
			logger.debug("Resource details are {}", file.getLinks());
			
		
		} catch (Exception stex) {
			logger.error("Error while retreiving file Resource File From Amazon for Bucket {} and Key {}",bucketName,key);
			//throw new FileRetrieveException("Unable to find resource file from Amazon for Buckte and Key",bucketName,key);
		}
			return file;	
	}
}