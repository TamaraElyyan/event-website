package event.website.service;

import event.website.model.Organization;
import event.website.model.Picture;
import event.website.model.User;
import event.website.repository.OrganizationRepository;
import event.website.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final FileService fileService;
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository, PictureService pictureService, FileService fileService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.fileService = fileService;
    }

    // Create or update organization
    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }
    public Organization saveOrganizationWithFile(Organization organization, MultipartFile file ) throws IOException {
        // First, save the organization to generate its ID
        Organization savedOrg = organizationRepository.save(organization);

//        if (file != null) {
//            try {
//                // Store the new picture
//                String location = fileService.store(file);
//                if (location == null) {
//                    throw new IOException("File storage failed, location is null.");
//                }
//                savedOrg.setDescriptionPicture(location);  // Update the organization with the picture location
//
//                // Create a new picture object for the new profile picture
//                Picture picture = new Picture();
//                picture.setListing(savedOrg.getId());  // Use the saved organization's ID
//                picture.setUrl(location);
//                picture.setUploadDate(Instant.now());
//                picture.setDeleted(false);  // Mark the new picture as not deleted
//
//                pictureService.save(picture);  // Save the new picture
//            } catch (IOException e) {
//                throw new RuntimeException("Error saving picture: " + e.getMessage(), e);
//            }
//        }
        if (!file.isEmpty()) {
            String location = pictureService.savePicture(file, savedOrg.getId());
            savedOrg.setDescriptionPicture(location);
            organizationRepository.save(savedOrg);

        }
        // Return the saved organization
        return savedOrg;
    }


    // Get all organizations
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    // Get organization by ID
    public Optional<Organization> getOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    // Soft delete organization by ID
    @Transactional
    public void softDeleteOrganization(Integer id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization with ID " + id + " not found."));

        if (organization.isDeleted()) {
            throw new RuntimeException("Organization with ID " + id + " is already deleted.");
        }

        organization.setDeleted(true);

        User user = organization.getUser();
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
        }

        organizationRepository.save(organization);
    }

    public Organization findById(Integer id) {
        return organizationRepository.findById(id).orElse(null);
    }
}
