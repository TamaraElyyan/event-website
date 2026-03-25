package event.website.controller;

import event.website.dto.OrganizationDTO;
import event.website.model.Instructor;
import event.website.model.Organization;
import event.website.model.Role;
import event.website.model.User;
import event.website.service.FileService;
import event.website.service.OrganizationService;
import event.website.service.PictureService;
import event.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/organization")

public class OrganizationController {

    private final OrganizationService organizationService;
    private final UserService userService;
    private  final FileService fileService;
  private  final PictureService pictureService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OrganizationController(OrganizationService organizationService, UserService userService, FileService fileService, PictureService pictureService, PasswordEncoder passwordEncoder) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.fileService = fileService;
        this.pictureService = pictureService;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all organizations
    @GetMapping("/organizationList")
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    // Get organization by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Integer id) {
        Optional<Organization> organization = organizationService.getOrganizationById(id);
        if (organization.isEmpty()) {
            throw new RuntimeException("Organization with ID " + id + " not found.");
        }
        return organization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //create organization for Admin
    @PostMapping("/addNewOrganization")
    public ResponseEntity<Organization> createOrganization(@RequestPart OrganizationDTO organizationDTO,@RequestPart("pictureFile") MultipartFile pictureFile, @RequestPart("payload") MultipartFile file) throws IOException {
        Organization savedOrganization = null;
        User savedUser = null;

        try {
            // Create and save the user from the DTO
            try {
                User user = new User();
                user.setUsername(organizationDTO.getUsername());
                user.setEmail(organizationDTO.getContactEmail());
                user.setFirstName(organizationDTO.getFirstName());
                user.setLastName(organizationDTO.getLastName());
                user.setPhoneNumber(organizationDTO.getPhoneNumber());
               // user.setProfilePictureUrl(organizationDTO.getProfilePictureUrl());
                user.setRole(Role.valueOf(organizationDTO.getRole()));  // Assuming the role is passed as a String
//                user.setPassword(organizationDTO.getPassword());

                String encodedPassword = passwordEncoder.encode(organizationDTO.getPassword());
                user.setPassword(encodedPassword);

                savedUser = userService.save(user); // Save user using the service
                if (!pictureFile.isEmpty()) {
                    String location = pictureService.savePicture(file, savedUser.getId());
                    savedUser.setProfilePictureUrl(location);
                }

            } catch (Exception e) {
                // Handle user-specific exceptions (e.g., constraint violations, validation errors)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Respond with error (you can customize this)
            }

            // Once the user is saved, set the user to the organization
            Organization organization = new Organization();
            organization.setOrganizationName(organizationDTO.getOrganizationName());
            organization.setContactEmail(organizationDTO.getContactEmail());
            organization.setPhoneNumber(organizationDTO.getPhoneNumber());
            organization.setWebsiteUrl(organizationDTO.getWebsiteUrl());
           // organization.setDescriptionPicture(organizationDTO.getDescriptionPicture());
            organization.setDescription(organizationDTO.getDescription());
            organization.setUser(savedUser);


            // Now, save the organization with file
            savedOrganization = organizationService.saveOrganizationWithFile(organization, file);

        } catch (IOException e) {
            // Catch any other IO errors during file processing or organization saving
            throw new RuntimeException("Error saving organization with file", e);
        } catch (Exception e) {
            // Catch other unexpected errors
            throw new RuntimeException("Unexpected error occurred while saving organization and user", e);
        }

        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED); // Respond with created organization
    }


    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.loadAsResource(filename);

        // Check for file type (you might want to check if it's an image)
        String contentType = "image/jpeg";  // Or dynamically set the type based on file extension (e.g., png, jpg)
        try {
            contentType = Files.probeContentType(file.getFile().toPath()); // Or set it based on the file type
        } catch (IOException e) {
            // Default to image type in case of an error
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }



    @PostMapping("/updateOrganizationProfile/{username}")
    public ResponseEntity<Organization> createOrganizationProfile(@PathVariable String username, @RequestBody Organization organization) {
        User userDetails = (User) userService.loadUserByUsername(username);

        if (userDetails == null) {
            return ResponseEntity.notFound().build();
        }
        organization.setUser(userDetails);
        if (userDetails.getRole() == Role.ORGANIZATION) {
            organization.setOrganizationName(userDetails.getFirstName() + " " + userDetails.getLastName());
            Organization savedOrganization = organizationService.saveOrganization(organization);
            return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    // Update organization
    @PutMapping("updateOrganization/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Integer id, @RequestBody Organization organizationDetails) {
        Organization existingOrganization = organizationService.findById(id);

        if (existingOrganization == null) {
            return ResponseEntity.notFound().build();
        }

        existingOrganization.setOrganizationName(organizationDetails.getOrganizationName() != null ? organizationDetails.getOrganizationName() : existingOrganization.getOrganizationName());
        existingOrganization.setContactEmail(organizationDetails.getContactEmail() != null ? organizationDetails.getContactEmail() : existingOrganization.getContactEmail());
        existingOrganization.setPhoneNumber(organizationDetails.getPhoneNumber() != null ? organizationDetails.getPhoneNumber() : existingOrganization.getPhoneNumber());
        existingOrganization.setWebsiteUrl(organizationDetails.getWebsiteUrl() != null ? organizationDetails.getWebsiteUrl() : existingOrganization.getWebsiteUrl());

        if (organizationDetails.getUser() != null) {
            existingOrganization.setUser(organizationDetails.getUser());
        }

        Organization updatedOrganization = organizationService.saveOrganization(existingOrganization);
        return ResponseEntity.ok(updatedOrganization);
    }

    // Soft delete organization
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Integer id) {
        organizationService.softDeleteOrganization(id);
        return ResponseEntity.ok("Organization with ID " + id + " successfully deleted.");
    }
}
