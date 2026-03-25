package event.website.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.website.model.Picture;
import event.website.model.Student;
import event.website.model.User;
import event.website.repository.UserRepository;
import event.website.service.FileService;
import event.website.service.PictureService;
import event.website.service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173/")

public class UserController {
    private final UserService userService;
    private final StringHttpMessageConverter stringHttpMessageConverter;
    private final FileService fileService;
    private final PictureService   pictureService;
    private final UserRepository userRepository;

    public UserController(UserService userService, StringHttpMessageConverter stringHttpMessageConverter, FileService fileService, PictureService pictureService, UserRepository userRepository) {
        this.userService = userService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
        this.fileService = fileService;
        this.pictureService = pictureService;
        this.userRepository = userRepository;
    }

//    @GetMapping("{username}")
//    public User getUserByUsername(@PathVariable("username") String username) {
//
//        return (User) userService.loadUserByUsername(username);
//    }




//    @GetMapping("{username}")
//    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable("username") String username) {
//        User user = (User) userService.loadUserByUsername(username);
//        Map<String, Object> map = new HashMap<>();
//        map.put("username", user.getUsername());
//        map.put("Email", user.getEmail());
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User userDetails = (User) userService.loadUserByUsername(username);
        if (userDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetails);

    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User savedUser = (User) userService.loadUserByUsername(username);
        if (savedUser == null) {
            return ResponseEntity.notFound().build();
        }

        savedUser.setUsername(user.getUsername());
        savedUser.setEmail(user.getEmail());
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setPhoneNumber(user.getPhoneNumber());
        String password = user.getPassword();

//        if (password != null) {
//            savedUser.setPassword(passwordEncoder.encode(password));
//        }
        ZoneId palestineTimeZone = ZoneId.of("Asia/Jerusalem");
        Instant nowInPalestine = ZonedDateTime.now(palestineTimeZone).toInstant();

        savedUser.setUpdatedAt(nowInPalestine);

        userService.save(savedUser);

        return ResponseEntity.ok(savedUser);
    }





    @DeleteMapping("/{username}")
    public  void deleteUser(@PathVariable("username") String username){
        User userDetails =(User)userService.loadUserByUsername(username);
        userService.deleteById(userDetails.getId());
    }


    @PostMapping("/pictures")
    public ResponseEntity<Picture> savePicture(){

        return ResponseEntity.ok().build();
    }

    @PostMapping("profilePicture/{username}")
    public ResponseEntity<Picture> addUserPicture(@PathVariable("username") String username, @RequestParam("payload") MultipartFile file) {
        try {
            // Load user
            User user = (User) userService.loadUserByUsername(username);

            // Check if the user already has a profile picture
            if (user.getProfilePictureUrl() != null) {
                // If the user has an existing profile picture, soft delete the old one
                Picture existingPicture = pictureService.findByListingAndIsDeletedFalse(user.getId());
                if (existingPicture != null) {
                    existingPicture.setDeleted(true);  // Mark the existing picture as deleted
                    pictureService.save(existingPicture);  // Save the updated picture (soft delete)
                }
            }

            // Store the new picture
            String location = fileService.store(file);
            user.setProfilePictureUrl(location);
            userService.save(user);  // Save the user with the new profile picture URL

            // Create a new picture object for the new profile picture
            Picture picture = new Picture();
            picture.setListing(user.getId());
            picture.setUrl(location);
            picture.setUploadDate(Instant.now());
            picture.setDeleted(false);  // Mark the new picture as not deleted
            pictureService.save(picture);  // Save the new picture

            return ResponseEntity.ok(picture);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("getProfilePicture/{username}")
    public ResponseEntity<Resource> getUserProfilePicture(@PathVariable("username") String username) {
        try {
            User user = (User) userService.loadUserByUsername(username);

            if (user.getProfilePictureUrl() == null) {
                return ResponseEntity.notFound().build();
            }

            Resource file = fileService.loadAsResource(user.getProfilePictureUrl());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
//    @GetMapping("files/{filename}")
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        try {
//            Resource file = fileService.loadAsResource("/static/pictures/" + filename);
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"")
//                    .body(file);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

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

//    public ResponseEntity<Picture> getUserProfilePicture(@PathVariable("username") String username) {
//        try {
//            // Load user
//            User user = (User) userService.loadUserByUsername(username);
//
//            // Check if the user has a profile picture URL
//            if (user.getProfilePictureUrl() == null) {
//                return ResponseEntity.notFound().build(); // No profile picture found
//            }
//
//            // Retrieve the picture using the user's ID
//            Picture picture = pictureService.findByListingAndIsDeletedFalse(user.getId());
//            if (picture == null) {
//                return ResponseEntity.notFound().build(); // No active picture found
//            }
//
//            return ResponseEntity.ok(picture); // Return the picture
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build(); // Handle any errors
//        }
//    }


    @PostMapping("/{username}/pictureUpdate")
    public ResponseEntity<User>  pictureUpdate(@PathVariable("username") String username,@RequestParam("payload") MultipartFile file) {
        //upload picture store on somewhere
        try {
            String location = fileService.store(file);

            User user = (User) userService.loadUserByUsername(username);

            user.setProfilePictureUrl(location);
            userService.save(user);

            return ResponseEntity.ok(user);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }


    }





    @Transactional
    public void softDeleteStudent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        if (user.isDeleted()) {
            throw new RuntimeException("user with ID " + id + " is already deleted.");
        }

        // Mark the student as deleted
        user.setDeleted(true);

        // Save the updated student record
        userRepository.save(user);
    }


}

