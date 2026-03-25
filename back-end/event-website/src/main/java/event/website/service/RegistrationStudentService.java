package event.website.service;

import event.website.config.TrainingSavingException;
import event.website.dto.RegistrationStudentDTO;
import event.website.model.Role;
import event.website.model.Training;
import event.website.model.RegistrationStudent;
import event.website.repository.TrainingRepository;
import event.website.repository.RegistrationStudentRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.sql.DriverManager.println;

@Service
public class RegistrationStudentService {
    private final RegistrationStudentRepository registrationStudentRepository;
    private final TrainingRepository trainingRepository;
    private final AuthenticationService authenticationService;
    public RegistrationStudentService(RegistrationStudentRepository registrationStudentRepository, TrainingRepository trainingRepository, AuthenticationService authenticationService) {
        this.registrationStudentRepository = registrationStudentRepository;
        this.trainingRepository = trainingRepository;
        this.authenticationService = authenticationService;
    }

    public long countEnrolledStudentsForTraining(Long trainingId) {
        // Fetch the list of registration students for the given training
        List<RegistrationStudent> registrationStudents = registrationStudentRepository.findByTrainingId(Math.toIntExact(trainingId));

        // Count how many students are enrolled
        return registrationStudents.stream()
                .filter(RegistrationStudent::isEnrolled) // Assuming isEnrolled() returns the enrolled status
                .count();
    }

    public void acceptStudentAndUpdateTraining(Integer registrationStudentId) {
        // Fetch the registration student by ID
        RegistrationStudent registrationStudent = registrationStudentRepository.findById(registrationStudentId)
                .orElseThrow(() -> new RuntimeException("RegistrationStudent not found"));



        // Fetch the event related to this registration
        Training training = registrationStudent.getTraining(); // Assuming there's an Event object in RegistrationStudent
        String currentUserRole = authenticationService.getCurrentUserRole();
        println(currentUserRole);

        if(training.getMaxNumberOfStudents()>training.getNumberOfStudentsEnrolled() ||currentUserRole.equals("SUPER_ADMIN") )
        {
            // Update the enrollment status to true
            registrationStudent.setEnrolled(true);
            registrationStudentRepository.save(registrationStudent);
        // Increment the number of enrolled students for the event
        training.setNumberOfStudentsEnrolled(training.getNumberOfStudentsEnrolled() + 1);
        trainingRepository.save(training);
        }else{
            //throws exception

            throw new TrainingSavingException("Training capacity has been exceeded. No more participants can be added.");

        }

    }

    public void rejectStudent(Integer registrationStudentId) {
        // Fetch the registration student by ID
        RegistrationStudent registrationStudent = registrationStudentRepository.findById(registrationStudentId)
                .orElseThrow(() -> new RuntimeException("RegistrationStudent not found"));



        // Fetch the event related to this registration
        Training training = registrationStudent.getTraining(); // Assuming there's an Event object in RegistrationStudent
        String currentUserRole = authenticationService.getCurrentUserRole();
        println(currentUserRole);

        if(currentUserRole.equals("ADMIN")||currentUserRole.equals("SUPER_ADMIN") )
        {
            // Update the enrollment status to true
            registrationStudent.setEnrolled(false);
            registrationStudentRepository.save(registrationStudent);
            // Increment the number of enrolled students for the event
            training.setNumberOfStudentsEnrolled(training.getNumberOfStudentsEnrolled() - 1);
            trainingRepository.save(training);
        }else{
            //throws exception

            throw new TrainingSavingException("Training capacity has been exceeded. No more participants can be added.");

        }

    }

    // Get all registrations
    public List<RegistrationStudent> getAllRegistrations() {
        return registrationStudentRepository.findAll();
    }

    // Get a specific registration by ID
    public RegistrationStudent getRegistrationById(Integer id) {
        Optional<RegistrationStudent> registration = registrationStudentRepository.findById(id);
        return registration.orElse(null);
    }

    // Save a new or updated registration
    public RegistrationStudent saveRegistration(@Valid RegistrationStudent registration) {
        return registrationStudentRepository.save(registration);
    }

    // Check if a registration exists by ID
    public boolean existsById(Integer id) {
        return registrationStudentRepository.existsById(id);
    }

    // Delete a registration by ID
    public void deleteRegistration(Integer id) {
        registrationStudentRepository.deleteById(id);
    }

    public List<RegistrationStudent> getEnrolledRegistrationsByStudent(Integer studentId) {
        return registrationStudentRepository.findByStudentIdAndEnrolledTrue(studentId);

    }

    public List<RegistrationStudentDTO> getEnrolledRegistrationsByStudentId(Integer studentId) {
        List<RegistrationStudent> registrations = registrationStudentRepository.findByStudentIdAndEnrolledTrue(studentId);

        return registrations.stream().map(registration -> {
            RegistrationStudentDTO dto = new RegistrationStudentDTO();
            dto.setStudentId(registration.getStudent().getId()); // Assuming Student has an 'id' field
            dto.setTrainingId(registration.getTraining().getId()); // Assuming Event has an 'id' field
            dto.setEnrolled(registration.getEnrolled());
            dto.setNotes(registration.getNotes());
            dto.setId(registration.getId());
            return dto;
        }).collect(Collectors.toList());
    }

}
