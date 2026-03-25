package event.website.service;

import event.website.model.Picture;
import event.website.repository.PictureRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class PictureService {
private  final PictureRepository pictureRepository;
private  final FileService fileService;

public PictureService(PictureRepository pictureRepository, FileService fileService) {

    this.pictureRepository = pictureRepository;
    this.fileService = fileService;
}
public Picture save(Picture picture) {
     return pictureRepository.save(picture);
}

    public Picture findByListingAndIsDeletedFalse(Integer listing) {
        return pictureRepository.findByListingAndDeletedFalse(Long.valueOf(listing))
                .orElse(null);
    }

    public List<Picture> findByListingId(Integer listingId) {
        return pictureRepository.findByListing(listingId);
    }


    public String savePicture(MultipartFile file, Integer listingId) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }

        try {
            // Store the file and get its location
            String location = fileService.store(file);
            if (location == null) {
                throw new IOException("File storage failed, location is null.");
            }

            // Create and save the picture
            Picture picture = new Picture();
            picture.setListing(listingId);
            picture.setUrl(location);
            picture.setUploadDate(Instant.now());
            picture.setDeleted(false);

            save(picture);

            return location;  // Return the saved file location
        } catch (IOException e) {
            throw new RuntimeException("Error saving picture: " + e.getMessage(), e);
        }
    }


//    public void deletePictureByListingId(int listingId) {
//        // Find the picture by listingId (which could represent the user)
//        Picture picture = pictureRepository.findByListing(listingId);
//
//        if (picture != null) {
//            // Delete the picture record from the database
//            pictureRepository.delete(picture);
//        }
//    }
}

