package com.example.artapp.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();


	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

    String store(MultipartFile image) throws StorageException, IOException;
	String store(MultipartFile image, String model) throws StorageException, IOException;
	void initPersonalFolder(Long id) ;

	Resource loadAsResourcePersonal(String filename, Long pid);
	Path loadPersonal(String filename, Long pid) ;

	Resource loadAsResourcePersonal(String filename);
	Path loadPersonal(String filename) ;


	void deleteFolder(Long id);

    String storePersonal(MultipartFile image, Long pid) throws StorageException, IOException;

    void deleteFile(String imagePath, String modelPath);

	void deleteFile(String imagePath, Long pid);
}
