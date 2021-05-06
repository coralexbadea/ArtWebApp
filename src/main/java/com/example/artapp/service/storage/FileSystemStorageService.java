package com.example.artapp.service.storage;


import com.example.artapp.domain.Post;
import com.example.artapp.domain.User;
import com.example.artapp.imageprocessing.ImageProcessing;
import com.example.artapp.service.PostService;
import com.example.artapp.service.UserService;
import com.example.artapp.service.typecheck.ImageCheckStrategy;
import com.example.artapp.service.typecheck.PytorchCheckStrategy;
import com.example.artapp.service.typecheck.TypeCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;


@Service("storageService")
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	private final Path modelLocation;
	private final Path loadLocation;
	private final String logLocation;
	private TypeCheckContext typeCheckContext;

	@Autowired
	ImageProcessing imageProcessing;
	@Autowired
	UserService userService;
	@Autowired
	PostService postService;
	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.modelLocation = Paths.get(properties.getModelLocation());
		this.loadLocation = Paths.get(properties.getLoadLocation());
		this.logLocation = properties.getLogLocation();
		this.typeCheckContext = new TypeCheckContext();
	}

    public FileSystemStorageService() {
        this.rootLocation = Paths.get("");
        this.modelLocation = Paths.get("");
        this.loadLocation = Paths.get("");
		this.logLocation = "";
		this.typeCheckContext = new TypeCheckContext();
    }

    @Override
	public String store(MultipartFile file, String model) throws StorageException, IOException {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
			checkSecurity(filename);
			checkType(filename);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);

				imageProcessing.processImage(this.rootLocation.resolve(filename).toString(), model);
			}


		return filename;
	}

	@Override
	public String store(MultipartFile file) throws StorageException, IOException {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());

		Path folder = this.rootLocation;

		this.typeCheckContext.setStrategy(new PytorchCheckStrategy());
		if(this.typeCheckContext.check(filename)) {
			folder = this.modelLocation;
		}

			checkSecurity(filename);
			checkType(filename);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, folder.resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
			}
		return filename;
	}

	@Override
	public String storePersonal(MultipartFile file, Long pid) throws StorageException, IOException {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		Path folder = Paths.get(loadLocation.getFileName()+"/"+pid);

		checkSecurity(filename);
		checkType(filename);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, folder.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
		}
		return filename;
	}

	public String checkType(String filename) throws StorageException {
		boolean checked  = false;
		this.typeCheckContext.setStrategy(new ImageCheckStrategy());
		if(typeCheckContext.check(filename)){
			checked = true;
		}
		this.typeCheckContext.setStrategy(new PytorchCheckStrategy());
		if(typeCheckContext.check(filename)){
			checked = true;
		}
		if(checked){
			return "Ok";
		}

		throw new StorageException(
				"Unknown format for file:  "
						+ filename);
	}


	public void checkSecurity(String filename) throws StorageException{
		if (filename.equals("")) {
			throw new StorageException("Failed to store empty file " + filename);
		}
		if (filename.contains("..")) {
			// This is a security check
			throw new StorageException(
					"Cannot store file with relative path outside current directory "
							+ filename);
		}

	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {

		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		FileSystemUtils.deleteRecursively(loadLocation.toFile());
		FileSystemUtils.deleteRecursively(modelLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			Files.createDirectories(modelLocation);
			Files.createDirectories(loadLocation);
			createLogFile();

		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	private void createLogFile() throws IOException {
		File tmpDir = new File("/var/tmp");
		boolean exists = tmpDir.exists();
		if(!exists) {
			Path dir = Files.createDirectories(Paths.get(logLocation.split("/")[0]));
			Path logFile = dir.resolve(logLocation.split("/")[1]);
			Files.createFile(logFile);
		}
	}

	public void initPersonalFolder(Long id) {
		try {
			String sid = Long.toString(id);
			Path personalLocation = Paths.get(loadLocation.getFileName()+"/"+sid);
			Files.createDirectories(personalLocation);

		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public Resource loadAsResourcePersonal(String filename, Long pid) {
		try {
			Path file = loadPersonal(filename, pid);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Path loadPersonal(String filename, Long pid) {
		Post post = postService.getPostById(pid);
		User user = userService.getUserByPost(post);
		Long uid = user.getUserId();
		return loadLocation.resolve(uid+"/"+filename);
	}

	@Override
	public Resource loadAsResourcePersonal(String filename) {
		try {
			Path file = loadPersonal(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Path loadPersonal(String filename) {
		Long uid = userService.getCurrentUser().getUserId();
		return loadLocation.resolve(uid+"/"+filename);
	}




	@Override
	public void deleteFolder(Long id){
		Path personalLocation = Paths.get(loadLocation.getFileName()+"/"+id);
		FileSystemUtils.deleteRecursively(personalLocation.toFile());
	}


	@Override
	public void deleteFile(String imagePath, Long pid){
		Path personalLocation = Paths.get(loadLocation.getFileName()+"/"+pid+"/"+imagePath);
		FileSystemUtils.deleteRecursively(personalLocation.toFile());
	}

	@Override
	public void writeToLog(String ptitle) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		String str = formatter.format(date) + " new post created: "+ptitle+"\n";
		try {
			FileWriter myWriter = new FileWriter(logLocation, true);
			myWriter.write(str);
			myWriter.close();
		}catch (IOException e){
			System.out.println("Error happened during log update" + e.getMessage());
		}
	}

	@Override
	public void deleteFile(String imagePath, String modelPath){
		Path personalLocation = Paths.get(rootLocation.getFileName()+"/"+imagePath);
		FileSystemUtils.deleteRecursively(personalLocation.toFile());
		personalLocation = Paths.get(modelLocation.getFileName()+"/"+modelPath);
		FileSystemUtils.deleteRecursively(personalLocation.toFile());
	}
}
