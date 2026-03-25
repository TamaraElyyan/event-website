package event.website.controller;

import event.website.dto.InstructorDTO;
import event.website.dto.OrganizationDTO;
import event.website.model.Instructor;
import event.website.model.Organization;
import event.website.model.Role;
import event.website.model.User;
import event.website.service.InstructorService;
import event.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/instructor")
@CrossOrigin(origins = "http://localhost:5173/")
public class InstructorController {

    private final InstructorService instructorService;
    private final UserService userService;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    @Autowired
    public InstructorController(InstructorService instructorService, UserService userService,
                                StringHttpMessageConverter stringHttpMessageConverter) {
        this.instructorService = instructorService;
        this.userService = userService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }
    @CrossOrigin(origins = "http://localhost:5173/")

    // Get all instructors
    @GetMapping("/instructorList")
    public List<Instructor> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    // Get instructor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Integer id) {
        Optional<Instructor> instructor = instructorService.getInstructorById(id);
        if (instructor.isEmpty()) {
            throw new RuntimeException("Instructor with ID " + id + " not found.");
        }
        return instructor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //create organization for Admin
    @PostMapping("/addNewInstructor")
    public ResponseEntity<Instructor> createOrganization(@RequestBody InstructorDTO instructorDTO) {
        Instructor savedInstructor = null;
        User savedUser = null;

        try {
            // Create and save the user from the DTO
            try {
                User user = new User();
                user.setUsername(instructorDTO.getUsername());
                user.setEmail(instructorDTO.getEmail());
                user.setFirstName(instructorDTO.getFirstName());
                user.setLastName(instructorDTO.getLastName());
                user.setPhoneNumber(instructorDTO.getPhoneNumber());
                user.setProfilePictureUrl(instructorDTO.getProfilePictureUrl());
                user.setRole(Role.valueOf(instructorDTO.getRole()));  // Assuming the role is passed as a String
                user.setPassword(instructorDTO.getPassword());

                savedUser = userService.save(user); // Save user using the service

            } catch (Exception e) {
                // Handle user-specific exceptions (e.g., constraint violations, validation errors)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Respond with error (you can customize this)
            }

            // Once the user is saved, set the user to the organization
            Instructor instructor = new Instructor();
            instructor.setName(instructorDTO.getName());
            instructor.setEmail(instructorDTO.getEmail());
           // instructor.set(organizationDTO.getPhoneNumber());
            instructor.setJobTitle(instructorDTO.getJobTitle());

            instructor.setUser(savedUser);

            // Now, save the organization with file
            savedInstructor = instructorService.saveInstructor(instructor);

        
        } catch (Exception e) {
            // Catch other unexpected errors
            throw new RuntimeException("Unexpected error occurred while saving organization and user", e);
        }

        return new ResponseEntity<>(savedInstructor, HttpStatus.CREATED); // Respond with created organization
    }



    // Create or update instructor
    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) {
        Instructor savedInstructor = instructorService.saveInstructor(instructor);
        return new ResponseEntity<>(savedInstructor, HttpStatus.CREATED);
    }

    @PostMapping("/updateInstructorProfile/{username}")
    public ResponseEntity<Instructor> createInstructorProfile(@PathVariable String username, @RequestBody Instructor instructor) {
        User userDetails = (User) userService.loadUserByUsername(username);

        if (userDetails == null) {
            return ResponseEntity.notFound().build();
        }
        instructor.setUser(userDetails);
        if (userDetails.getRole() == Role.INSTRUCTOR) {
            instructor.setName(userDetails.getFirstName() + " " + userDetails.getLastName());
            Instructor savedInstructor = instructorService.saveInstructor(instructor);
            return new ResponseEntity<>(savedInstructor, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Update instructor
    @PutMapping("updateInstructor/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable Integer id, @RequestBody Instructor instructorDetails) {
        Instructor existingInstructor = instructorService.findById(id);

        if (existingInstructor == null) {
            return ResponseEntity.notFound().build();
        }

        existingInstructor.setName(instructorDetails.getName() != null ? instructorDetails.getName() : existingInstructor.getName());
        existingInstructor.setEmail(instructorDetails.getEmail() != null ? instructorDetails.getEmail() : existingInstructor.getEmail());
        existingInstructor.setJobTitle(instructorDetails.getJobTitle() != null ? instructorDetails.getJobTitle() : existingInstructor.getJobTitle());

        if (instructorDetails.getUser() != null) {
            existingInstructor.setUser(instructorDetails.getUser());
        }

        Instructor updatedInstructor = instructorService.saveInstructor(existingInstructor);
        return ResponseEntity.ok(updatedInstructor);
    }

    // Soft delete instructor
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteInstructor(@PathVariable Integer id) {
        instructorService.softDeleteInstructor(id);
        return ResponseEntity.ok("Instructor with ID " + id + " successfully deleted.");
    }
}
