package event.website.controller;

import event.website.dto.TrainingCreationDTO;
import event.website.model.Training;
import event.website.model.Picture;

import event.website.repository.TrainingRepository;
import event.website.service.TrainingService;
import event.website.service.OrganizationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/training")
@CrossOrigin(origins = "http://localhost:5173/")

public class TrainingController {
    @Autowired
    private TrainingService trainingService;

    @Autowired
    private OrganizationService organizationService;
    private final StringHttpMessageConverter stringHttpMessageConverter;
     private final TrainingRepository trainingRepository;

    public TrainingController(TrainingService trainingService, StringHttpMessageConverter stringHttpMessageConverter, TrainingRepository trainingRepository) {
        this.trainingService = trainingService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
        this.trainingRepository = trainingRepository;
    }

    // Get all events
    @GetMapping("/courseList")
    public List<Training> getAllCourses() {
        return trainingService.getAllCourses();
    }


    // Get all events
    @GetMapping("/eventList")
    public List<Training> getAllEvents() {
        return trainingService.getAllEvents();
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Training> getTrainingById(@PathVariable Integer id) {
        Optional<Training> event = trainingService.getTrainingById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create or update event
    @PostMapping
    public ResponseEntity<Training> createTraining(@RequestBody Training training) {
        Training savedTraining = trainingService.saveEvent(training);
        return new ResponseEntity<>(savedTraining, HttpStatus.CREATED);
    }
//    @PostMapping("/adding")
//    public ResponseEntity<Event> createTrainingNew(@RequestBody Event event) {
//        Event savedEvent = eventService.createTraining(event);
//        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
//    }

    //without file
     @PostMapping("/addingNew")
    public ResponseEntity<Training> createNewTraining(@RequestBody  @Valid TrainingCreationDTO trainingCreationDTO) {
        // Create and save the training using the service
        Training savedTraining = trainingService.createNewTraining(
                trainingCreationDTO.getTraining(),        // The Event object
                trainingCreationDTO.getOrganizationIds(),                   // Organization IDs
                trainingCreationDTO.getInstructorIds() // Instructor IDs
        );

        return new ResponseEntity<>(savedTraining, HttpStatus.CREATED);
    }

//withFile
    @PostMapping("/adding")
    public ResponseEntity<Training> createTrainingNew(
            @RequestPart("trainingData") @Valid TrainingCreationDTO trainingCreationDTO,
            @RequestPart("file") MultipartFile [] files) throws IOException {
        // Create and save the training using the service
        if (files.length==0) {
            return new ResponseEntity("File must not be empty", HttpStatus.BAD_REQUEST);
        }
        Training savedTraining = trainingService.createTraining(
                trainingCreationDTO.getTraining(),        // The Event object
                trainingCreationDTO.getOrganizationIds(),                   // Organization IDs
                trainingCreationDTO.getInstructorIds(),
                files// Instructor IDs
        );

        return new ResponseEntity<>(savedTraining, HttpStatus.CREATED);
    }


    @PutMapping("/updateWithFile/{id}")
    public ResponseEntity<Training> updateWithFileTraining(
            @PathVariable Integer id,
            @RequestPart @Valid TrainingCreationDTO trainingCreationDTO,
            @RequestPart("file") MultipartFile [] files) throws IOException {

        // Fetch the existing event by ID
        Training existingTraining = trainingService.getTrainingById(id)
                .orElseThrow(() -> new RuntimeException("Event with ID " + id + " not found."));

        // Update the event using the service
        Training updatedTraining = trainingService.updateWithFileTraining(
                existingTraining,                        // Existing Event object
                trainingCreationDTO.getTraining(),          // Updated Event object
                trainingCreationDTO.getOrganizationIds(), // Organization IDs
                trainingCreationDTO.getInstructorIds(),   // Instructor IDs
                files
        );

        // Return the updated event
        return ResponseEntity.ok(updatedTraining);
    }




    //update without file
    @PutMapping("/update/{id}")
    public ResponseEntity<Training> updateTraining(
            @PathVariable Integer id,
            @RequestBody @Valid TrainingCreationDTO trainingCreationDTO) {
        // Fetch the existing event by ID
        Training existingTraining = trainingService.getTrainingById(id)
                .orElseThrow(() -> new RuntimeException("Event with ID " + id + " not found."));

        // Update the event using the service
        Training updatedTraining = trainingService.updateTraining(
                existingTraining,                        // Existing Event object
                trainingCreationDTO.getTraining(),          // Updated Event object
                trainingCreationDTO.getOrganizationIds(), // Organization IDs
                trainingCreationDTO.getInstructorIds()   // Instructor IDs
        );

        // Return the updated event
        return ResponseEntity.ok(updatedTraining);
    }



    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(@PathVariable Integer id, @RequestBody Training training) {
        if (trainingService.getTrainingById(id).isPresent()) {
            training.setId(id); // Ensure we update the existing event
            Training updatedTraining = trainingService.saveEvent(training);
            return ResponseEntity.ok(updatedTraining);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Transactional
    @DeleteMapping("/deleteTraining/{id}")
    public ResponseEntity<String> softdeleteTrainingById(@PathVariable Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Event ID must not be null");
        }

        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with ID " + id + " not found."));

        training.setDeleted(true);
        training.getTrainingInstructors().forEach(instructor -> instructor.setDeleted(true));
        training.getTrainingOrganizations().forEach(org -> org.setDeleted(true));
        training.getRegistrationStudents().forEach(student -> student.setDeleted(true));

        trainingRepository.save(training);

        // Return success message
        return ResponseEntity.ok("Event with ID " + id + " has been successfully deleted.");
    }


    @GetMapping("/{trainingId}/pictures")
    public List<String> getTrainingPictures(@PathVariable Integer trainingId) {
        // Retrieve the training by ID
        Optional<Training> optionalTraining = trainingService.getTrainingById(trainingId);

        // Check if training is present
        if (optionalTraining.isPresent()) {
            Training training = optionalTraining.get();

            // If there are pictures, return a list of image URLs
//            return training.getPictures().stream()
//                    .map(Picture::getUrl)  // Assuming Picture has an imageUrl property
//                    .collect(Collectors.toList());
            return training.getPictures().stream()
                    .filter(picture -> !picture.getIsUser())  // Filter pictures where isUser is false
                    .map(Picture::getUrl)  // Get the URL of the filtered picture
                    .collect(Collectors.toList());


        }

        // Return an empty list if training doesn't exist or no pictures found
        return List.of();
    }


    // Delete event
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Integer id) {
        if (trainingService.getTrainingById(id).isPresent()) {
            trainingService.deleteTraining(id);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
