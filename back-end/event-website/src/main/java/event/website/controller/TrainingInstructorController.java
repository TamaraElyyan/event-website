package event.website.controller;

import event.website.model.TrainingInstructor;
import event.website.service.TrainingInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainingInstructors")
@CrossOrigin(origins = "http://localhost:5173/")

public class TrainingInstructorController {

    @Autowired
    private TrainingInstructorService trainingInstructorService;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    public TrainingInstructorController(TrainingInstructorService trainingInstructorService, StringHttpMessageConverter stringHttpMessageConverter) {
        this.trainingInstructorService = trainingInstructorService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }

    // Get all training instructors
    @GetMapping
    public List<TrainingInstructor> getAllTrainingistructors() {
        return trainingInstructorService.getAllTrainingInstructors();
    }

    // Get training instructor by ID
    @GetMapping("/{id}")
    public ResponseEntity<TrainingInstructor> getTrainingInstructorById(@PathVariable Integer id) {
        Optional<TrainingInstructor> trainingInstructor = trainingInstructorService.getTrainingInstructorById(id);
        return trainingInstructor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create or update training instructor
    @PostMapping
    public ResponseEntity<TrainingInstructor> createTrainingInstructor(@RequestBody TrainingInstructor trainingInstructor) {
        TrainingInstructor savedTrainingInstructor = trainingInstructorService.saveTrainingInstructor(trainingInstructor);
        return new ResponseEntity<>(savedTrainingInstructor, HttpStatus.CREATED);
    }

    // Update training instructor
    @PutMapping("/{id}")
    public ResponseEntity<TrainingInstructor> updateTrainingInstructor(@PathVariable Integer id, @RequestBody TrainingInstructor trainingInstructor) {
        if (trainingInstructorService.getTrainingInstructorById(id).isPresent()) {
            trainingInstructor.setId(id); // Ensure we update the existing event instructor
            TrainingInstructor updatedTrainingInstructor = trainingInstructorService.saveTrainingInstructor(trainingInstructor);
            return ResponseEntity.ok(updatedTrainingInstructor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete training instructor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingInstructor(@PathVariable Integer id) {
        if (trainingInstructorService.getTrainingInstructorById(id).isPresent()) {
            trainingInstructorService.deleteTrainingInstructor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
