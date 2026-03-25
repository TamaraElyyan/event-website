package event.website.controller;

import event.website.config.TrainingSavingException;
import event.website.dto.RegistrationStudentDTO;
import event.website.model.Training;
import event.website.model.RegistrationStudent;
import event.website.model.Student;
import event.website.repository.RegistrationStudentRepository;
import event.website.repository.StudentRepository;
import event.website.repository.TrainingRepository;
import event.website.service.EmailService;
import event.website.service.TrainingService;
import event.website.service.RegistrationStudentService;
import event.website.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173/")

@RestController
@RequestMapping("/registration")
public class RegistrationStudentController {
    private final RegistrationStudentService registrationStudentService;
    private final StudentService studentService;
    private final TrainingService trainingService;
    private final EmailService emailService;
    private final StudentRepository studentRepository;
    private final RegistrationStudentRepository registrationStudentRepository;


    public RegistrationStudentController(RegistrationStudentService registrationStudentService, StudentService studentService, TrainingService trainingService, EmailService emailService, StudentRepository studentRepository, RegistrationStudentRepository registrationStudentRepository, TrainingRepository trainingRepository) {
        this.registrationStudentService = registrationStudentService;
        this.studentService = studentService;
        this.trainingService = trainingService;
        this.emailService = emailService;
        this.studentRepository = studentRepository;
        this.registrationStudentRepository = registrationStudentRepository;
    }

    // Get all registrations
    @GetMapping("registrationList")
    public ResponseEntity<List<RegistrationStudent>> getAllRegistrations() {
        List<RegistrationStudent> registrations = registrationStudentService.getAllRegistrations();
        if (registrations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(registrations);
    }


    // Get a registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationStudent> getRegistrationById(@PathVariable("id") Integer id) {
        RegistrationStudent registration = registrationStudentService.getRegistrationById(id);
        if (registration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registration);
    }

    // Create a new registration
//    @PostMapping("createRegistration")
//    public ResponseEntity<?> createRegistration(@RequestBody @Valid RegistrationStudentDTO registration) {
//        try {
//            // Fetch the event and handle the Optional
//            Optional<Event> optionalEvent = eventService.getTrainingById(registration.getTrainingId());
//            if (optionalEvent.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Event with ID " + registration.getTrainingId() + " not found.");
//            }
//            Event event = optionalEvent.get();
//
//            // Fetch the student and handle the Optional
//            Optional<Student> optionalStudent = studentService.getStudentById(registration.getStudentId());
//            if (optionalStudent.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Student with ID " + registration.getStudentId() + " not found.");
//            }
//            Student student = optionalStudent.get();
//
//            // Map the DTO to the entity
//            RegistrationStudent registrationStudent = new RegistrationStudent();
//            registrationStudent.setEvent(event);
//            registrationStudent.setStudent(student);
//            registrationStudent.setNotes(registration.getNotes());
//            registrationStudent.setEnrolled(registration.getStatus().equalsIgnoreCase("Registered"));
//
//            // Save the registration
//            RegistrationStudent savedRegistration = registrationStudentService.saveRegistration(registrationStudent);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegistration);
//
//        } catch (DataIntegrityViolationException ex) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("Duplicate entry: A registration with the given details already exists.");
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while creating the registration.");
//        }
//    }

    @PostMapping("/createRegistration")
    public ResponseEntity<?> createRegistration(@RequestBody @Valid RegistrationStudentDTO registration) {
        try {
            // Check if Event exists
            Optional<Training> optionalEvent = trainingService.getTrainingById(registration.getTrainingId());
            if (optionalEvent.isEmpty()) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "event", "Event with ID " + registration.getTrainingId() + " not found.");
            }
            Training training = optionalEvent.get();

            // Check if Student exists
            Optional<Student> optionalStudent = studentService.getStudentById(registration.getStudentId());
            if (optionalStudent.isEmpty()) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "user", "Student with ID " + registration.getStudentId() + " not found.");
            }
            Student student = optionalStudent.get();

            // Create Registration
            RegistrationStudent registrationStudent = new RegistrationStudent();
            registrationStudent.setTraining(training);
            registrationStudent.setStudent(student);
            registrationStudent.setNotes(registration.getNotes());
            registrationStudent.setEnrolled(registration.getEnrolled());

            // Save Registration
            RegistrationStudent savedRegistration = registrationStudentService.saveRegistration(registrationStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRegistration);

        } catch (DataIntegrityViolationException ex) {
            return buildErrorResponse(HttpStatus.CONFLICT, "constraint", "A registration already exists for the given student and event.");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "general", "An error occurred while creating the registration.");
        }
    }

    @PutMapping("/accept/{registrationStudentId}")
    public ResponseEntity<?> acceptStudent(@PathVariable Integer registrationStudentId) {
        try {
            // Fetch the registration
            RegistrationStudent registrationStudent = registrationStudentService.getRegistrationById(registrationStudentId);

            if (registrationStudent == null) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Server", "Registration with ID " + registrationStudentId + " not found.");
            }

            // Check if the registration is already accepted
            if (registrationStudent.isEnrolled()) {
                // Return response indicating the registration is already accepted
                return buildErrorResponse(HttpStatus.CONFLICT, "already_accepted", "The student is already enrolled in this event.");
            }

            // Update the student enrollment and event's number of enrolled students
            registrationStudentService.acceptStudentAndUpdateTraining(registrationStudentId);
            // Send email
            emailService.sendStudentAcceptanceEmail(
                    registrationStudent.getStudent().getEmail(),
                    registrationStudent.getStudent().getName(),
                    registrationStudent.getTraining().getTrainingName()
            );
            return ResponseEntity.ok("Student accepted and event enrollment updated");
        } catch (TrainingSavingException e) {
            // Handle the exception for training saving failure (capacity exceeded)
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "training_capacity_exceeded", e.getMessage());

        }catch (Exception e) {
            // Handle any other exceptions with a general server error
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "server", "An error occurred while accepting the student: " + e.getMessage());
        }
    }

    @PutMapping("/reject/{registrationStudentId}")
    public ResponseEntity<?> rejectStudent(@PathVariable Integer registrationStudentId) {
        try {
            // Fetch the registration
            RegistrationStudent registrationStudent = registrationStudentService.getRegistrationById(registrationStudentId);

            if (registrationStudent == null) {
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Server", "Registration with ID " + registrationStudentId + " not found.");
            }

            // Check if the registration is already accepted
            if (!registrationStudent.isEnrolled()) {
                // Return response indicating the registration is already accepted
                return buildErrorResponse(HttpStatus.CONFLICT, "already_rejected", "The student is already rejected in this event.");
            }

            // Update the student enrollment and event's number of enrolled students
            registrationStudentService.rejectStudent(registrationStudentId);
            // Send email

            return ResponseEntity.ok("Student rejected and event enrollment updated");

        } catch (Exception e) {
            // Handle any other exceptions with a general server error
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "server", "An error occurred while rejected the student: " + e.getMessage());
        }
    }



    // Update an existing registration
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegistration(
            @PathVariable("id") Integer id,
            @RequestBody @Valid RegistrationStudent registration) {
        RegistrationStudent existingRegistration = registrationStudentService.getRegistrationById(id);
        if (existingRegistration == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Registration with ID " + id + " not found.");
        }
        registration.setId(id); // Ensure the correct ID is set for updating
        try {
            RegistrationStudent updatedRegistration = registrationStudentService.saveRegistration(registration);
            return ResponseEntity.ok(updatedRegistration);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the registration.");
        }
    }


    @GetMapping("/studentListRegistraion/{studentId}")
    public ResponseEntity<List<RegistrationStudent>> getEnrolledRegistrationsByStudent(@PathVariable("studentId") Integer studentId) {
        List<RegistrationStudent> registrations = registrationStudentService.getEnrolledRegistrationsByStudent(studentId);

        if (registrations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/studentList/{studentId}")
    public ResponseEntity<List<RegistrationStudentDTO>> getEnrolledRegistrationsByStudentId(@PathVariable("studentId") Integer studentId) {
        List<RegistrationStudentDTO> registrations = registrationStudentService.getEnrolledRegistrationsByStudentId(studentId);

        if (registrations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(registrations);
    }

    //Soft Delete
    @Transactional
    @DeleteMapping("/deleteRegistrationStudent/{id}")
    public ResponseEntity<String> softdeleteRegistrationById(@PathVariable Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Event ID must not be null");
        }

        RegistrationStudent Regstudent = registrationStudentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RegistrationStudent with ID " + id + " not found."));
        Regstudent.setDeleted(true);
        registrationStudentRepository.save(Regstudent);

        // Return success message
        return ResponseEntity.ok("RegistrationStudent with ID " + id + " has been successfully deleted.");
    }



    // Delete a registration
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable("id") Integer id) {
        RegistrationStudent existingRegistration = registrationStudentService.getRegistrationById(id);
        if (existingRegistration == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Registration with ID " + id + " not found.");
        }
        registrationStudentService.deleteRegistration(id);
        return ResponseEntity.ok("Registration " + id + " was deleted.");
    }


//    @GetMapping("/studentEnrolled/{studentId}/")
//    public ResponseEntity<List<RegistrationStudent>> getEnrolledRegistrationsByStudentId(@PathVariable("studentId") Integer studentId) {
//        List<RegistrationStudent> registrations = registrationStudentService.getEnrolledRegistrationsByStudentId(studentId);
//
//        if (registrations.isEmpty()) {
//            return ResponseEntity.noContent().build(); // No enrolled registrations found for the given student ID
//        }
//        return ResponseEntity.ok(registrations); // Return the list of enrolled registrations
//    }


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String type, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error_code", status.value());
        response.put("type", type);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }}

