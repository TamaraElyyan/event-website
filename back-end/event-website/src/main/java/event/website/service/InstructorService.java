package event.website.service;

import event.website.model.Instructor;
import event.website.model.User;
import event.website.repository.InstructorRepository;
import event.website.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserRepository userRepository) {
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
    }

    // Create or update instructor
    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    // Get all instructors
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    // Get instructor by ID
    public Optional<Instructor> getInstructorById(Integer id) {
        return instructorRepository.findById(id);
    }

    // Delete instructor by ID
    @Transactional
    public void softDeleteInstructor(Integer id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instructor with ID " + id + " not found."));

        if (instructor.isDeleted()) {
            throw new RuntimeException("Instructor with ID " + id + " is already deleted.");
        }

        instructor.setDeleted(true);

        User user = instructor.getUser();
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
        }

        instructorRepository.save(instructor);
    }

    public Instructor findById(Integer id) {
        return instructorRepository.findById(id).orElse(null);
    }
}
