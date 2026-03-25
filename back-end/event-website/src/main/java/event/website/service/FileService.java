package event.website.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private final String staticPicturePath = "/static/pictures";
    private final Path parentDirectory;

    // Constructor to initialize the storage directory
    public FileService() {
        this.parentDirectory = Paths.get(System.getProperty("user.dir") + "/src/main/resources" + staticPicturePath);

        // Ensure the directory exists
        try {
            if (!Files.exists(parentDirectory)) {
                Files.createDirectories(parentDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    /**
     * Store an uploaded file and return its relative path.
     *
     * @param file The file to store.
     * @return The relative path to access the file.
     * @throws IOException If an error occurs during file storage.
     */
    public String store(MultipartFile file) throws IOException {
        // Generate a unique filename
        String uniqueId = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());

        // Define the full path where the file will be stored
        Path filePath = parentDirectory.resolve(uniqueId);

        // Copy the file to the storage location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to access the image
        return staticPicturePath + "/" + uniqueId;
    }

    /**
     * Load a file as a resource.
     *
     * @param filePath The relative path to the file.
     * @return The file as a resource.
     */
//    public Resource loadAsResource(String filePath) {
//        try {
//            // Remove leading slashes from the relative path (if any)
//            String normalizedPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
//
//            // Resolve the full path of the file
//            Path file = parentDirectory.getParent().resolve(normalizedPath).normalize();
//
//            // Ensure the file exists and is readable
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() && resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("File not found or not readable: " + filePath);
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Malformed file path: " + filePath, e);
//        }
//    }

    public Resource loadAsResource(String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/pictures").resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    /**
     * Delete a file by its relative path.
     *
     * @param filePath The relative path to the file.
     */
    public void delete(String filePath) {
        try {
            // Resolve the full path of the file to delete
            Path file = parentDirectory.resolve(Paths.get(filePath).getFileName());

            // Delete the file if it exists
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filePath, e);
        }
    }

    /**
     * Get the file extension from a filename.
     *
     * @param filename The filename to process.
     * @return The file extension, including the dot (e.g., ".jpg").
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex == -1 ? "" : filename.substring(dotIndex);
    }
}
