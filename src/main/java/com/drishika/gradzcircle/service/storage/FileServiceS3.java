package com.drishika.gradzcircle.service.storage;
import com.drishika.gradzcircle.exception.FileRemoveException;
import com.drishika.gradzcircle.exception.FileRetrieveException;
import com.drishika.gradzcircle.exception.FileUploadException;
import java.util.List;
import org.springframework.hateoas.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceS3 {

    public List listObjects(String bucketName);

    public void uploadObject (String bucketName,String key, MultipartFile file) throws FileUploadException;

    public void deleteObject (String bucketName, String key) throws FileRemoveException;

    public Resource getObject (String bucketName, String key) throws FileRetrieveException;

}