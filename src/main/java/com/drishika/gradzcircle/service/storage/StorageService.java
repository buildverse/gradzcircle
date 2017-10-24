package com.drishika.gradzcircle.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file,Long id);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename) throws StorageFileNotFoundException;

    void deleteAll();

    void delete(Long filname);

}

