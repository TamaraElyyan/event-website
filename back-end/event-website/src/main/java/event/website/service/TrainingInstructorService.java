package event.website.service;

import event.website.model.TrainingInstructor;
import event.website.repository.TrainingInstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingInstructorService {

    @Autowired
    private TrainingInstructorRepository trainingInstructorRepository;

    // Create or update an event instructor
    public TrainingInstructor saveTrainingInstructor(TrainingInstructor trainingInstructor) {
        return trainingInstructorRepository.save(trainingInstructor);
    }

    // Get all event instructors
    public List<TrainingInstructor> getAllTrainingInstructors() {
        return trainingInstructorRepository.findAll();
    }

    // Get an event instructor by ID
    public Optional<TrainingInstructor> getTrainingInstructorById(Integer id) {
        return trainingInstructorRepository.findById(id);
    }

    // Delete an event instructor by ID
    public void deleteTrainingInstructor(Integer id) {
        trainingInstructorRepository.deleteById(id);
    }
}
