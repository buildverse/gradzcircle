package com.drishika.gradzcircle.service.storage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import com.drishika.gradzcircle.config.ApplicationProperties;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.UserRepository;
import com.drishika.gradzcircle.service.UserService;
import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

/**
 * @TODO: 
 * 1. Check dir crreation security. Currently i can delete data within it. 
 */



@Service
@Transactional
public class FileSystemStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);
    private final Path rootLocation;
     private final ApplicationProperties applicationProperties;
    private final UserRepository userRepository;
    private final UserService userService;


    public FileSystemStorageService(ApplicationProperties applicationProperties, UserRepository userRepository,UserService userService) {
        this.userService = userService;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
        this.rootLocation = Paths.get(System.getProperty("user.home"),applicationProperties.storage);
        setDirectory(this.rootLocation);
        
    }

    private void setDirectory(Path path){
        if (Files.notExists(path)){
            try{
            Files.createDirectory(path);
            logger.info("Created upload directory at ", path.toString());
            } catch (Exception ex){
                logger.error("Error creating directory {}",ex.getMessage());
            }
        }
    }

    @Override
    public void store(MultipartFile file,Long userId) {
        String [] spiltName = file.getOriginalFilename().split("\\.");
        String fileName = spiltName[0];
        String fileExtension = spiltName[1];
        
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            Path start = this.rootLocation;
            logger.info("Start location si {}",start.toString());

            String joined = null;
            try (Stream<Path> stream = Files.walk(start, 1)){
                  joined = stream.map(String::valueOf).filter(path -> (path.substring(path.lastIndexOf("/") + 1, path.length()).split("\\.")[0]).equals(userId.toString()))
                    .sorted().collect(Collectors.joining(";"));
            }
            logger.info("Joined is {}", joined);

            if(joined != null | joined.indexOf("")>0){
                File oldPic = new File(joined);
                oldPic.delete();
            }
            
            File userPic = new File(this.rootLocation.resolve(userId.toString()+"."+fileExtension).toString());
            
            logger.info("User Pic is {}", userPic);
            file.transferTo(userPic);
           // Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            //Files.move(this.rootLocation.resolve(file.getOriginalFilename()),this.rootLocation.resolve(userId.toString()));
            User user  = userRepository.findOne(userId);
            user.setImageUrl(userPic.getPath());
          //  userRepository.save(user);
          userService.updateUser(user.getFirstName(),user.getLastName(),user.getEmail(),user.getLangKey(),user.getImageUrl());
            
            
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        logger.debug("Filename to serve is {} ", filename);
        try {
            User user  = userRepository.findOne(Long.parseLong(filename));
            filename = user.getImageUrl();
            if(filename == null){
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
            logger.debug("Filename retrieved from DB is {}", filename);
            Path file = load(filename);
            logger.debug("Path is {}", filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(Long filename){
        User user  = userRepository.findOne(filename);
        String filePath = null;
        if(user != null)
             filePath = user.getImageUrl()!=null ?user.getImageUrl():null;
        if(filePath == null){
            throw new StorageFileNotFoundException("Could not read file: " + filePath);
        }
        Path file = load(filePath);
        file.toFile().delete();
        user.setImageUrl(null);
        userService.updateUser(user.getFirstName(),user.getLastName(),user.getEmail(),user.getLangKey(),user.getImageUrl());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}