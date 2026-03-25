package event.website.service;

import event.website.model.*;
import event.website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainingService {


    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private TrainingInstructorRepository trainingInstructorRepository;

    @Autowired
    private TrainingOrganizationRepository trainingOrganizationRepository;

    @Autowired
    private  FileService fileService;

    @Autowired
    private  PictureService pictureService;

  @Autowired
  private  PictureRepository pictureRepository;

    //with file
    public Training createTraining(Training trainingObject, Set<Integer> organizationIds, Set<Integer> instructorIds, MultipartFile[] files) throws IOException {
        // Create and save the Training
        Training training = new Training();
        training.setTrainingName(trainingObject.getTrainingName());
        training.setTrainingDescription(trainingObject.getTrainingDescription());
        training.setNumberOfStudentsEnrolled(0); // Set default enrolled students to 0
        training.setEndRegistration(trainingObject.getEndRegistration());
        training.setType(trainingObject.getType());
        training.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        training = trainingRepository.save(training);

        // Fetch Organizations by IDs using OrganizationService and create TrainingOrganization association
        for (Integer organizationId : organizationIds) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + organizationId));

            TrainingOrganization trainingOrganization = new TrainingOrganization();
            trainingOrganization.setTraining(training);
            trainingOrganization.setOrganization(organization);

            // Save the TrainingOrganization
            trainingOrganizationRepository.save(trainingOrganization);

            // Optionally, add to the event's set of Organization to manage the relationship
            training.getTrainingOrganizations().add(trainingOrganization);
        }

        // Fetch Instructors by IDs and create EventInstructor association
        for (Integer instructorId : instructorIds) {
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

            TrainingInstructor trainingInstructor = new TrainingInstructor();
            trainingInstructor.setTraining(training);
            trainingInstructor.setInstructor(instructor);

            // Save the EventInstructor
            trainingInstructorRepository.save(trainingInstructor);

            // Optionally, add to the event's set of instructors to manage the relationship
            training.getTrainingInstructors().add(trainingInstructor);
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // String location = fileService.store(file);
                String location = fileService.store(file); // Assume store() handles file storage
                Picture picture = new Picture();
                picture.setListing(training.getId()); // Associate with the Training ID
                picture.setUrl(location);
                picture.setUploadDate(Instant.now());
                picture.setDeleted(false); // Mark as active
                picture.setIsUser(false);
                pictureService.save(picture);

                // Add the picture to the DTO's picture list
                trainingObject.getPictures().add(picture);
            }}
                // Return the saved event with all associated data
                return training;

    }


    //without file
    public Training createNewTraining(Training trainingObject, Set<Integer> organizationIds, Set<Integer> instructorIds) {
        // Create and save the Training
        Training training = new Training();
        training.setTrainingName(trainingObject.getTrainingName());
        training.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        training.setTrainingDescription(trainingObject.getTrainingDescription());
        training.setNumberOfStudentsEnrolled(0); // Set default enrolled students to 0
        training.setEndRegistration(trainingObject.getEndRegistration());
        training.setType(trainingObject.getType());
        training.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        training = trainingRepository.save(training);

        // Fetch Organizations by IDs using OrganizationService and create TrainingOrganization association
        for (Integer organizationId : organizationIds) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + organizationId));

            TrainingOrganization trainingOrganization = new TrainingOrganization();
            trainingOrganization.setTraining(training);
            trainingOrganization.setOrganization(organization);

            // Save the TrainingOrganization
            trainingOrganizationRepository.save(trainingOrganization);

            // Optionally, add to the event's set of Organization to manage the relationship
            training.getTrainingOrganizations().add(trainingOrganization);
        }

        // Fetch Instructors by IDs and create EventInstructor association
        for (Integer instructorId : instructorIds) {
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

            TrainingInstructor trainingInstructor = new TrainingInstructor();
            trainingInstructor.setTraining(training);
            trainingInstructor.setInstructor(instructor);

            // Save the EventInstructor
            trainingInstructorRepository.save(trainingInstructor);

            // Optionally, add to the event's set of instructors to manage the relationship
            training.getTrainingInstructors().add(trainingInstructor);
        }

        // Return the saved event with all associated data
        return training;
    }
    public Training updateTraining(Training existingTraining, Training trainingObject, Set<Integer> organizationIds, Set<Integer> instructorIds) {
        // Update the event's basic details
        existingTraining.setTrainingName(trainingObject.getTrainingName());
        existingTraining.setTrainingDescription(trainingObject.getTrainingDescription());
        existingTraining.setNumberOfStudentsEnrolled(trainingObject.getNumberOfStudentsEnrolled());
        existingTraining.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        existingTraining.setEndRegistration(trainingObject.getEndRegistration());
        existingTraining.setTrainingDescription(trainingObject.getTrainingDescription());
        existingTraining.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        existingTraining.setEndDate(trainingObject.getEndDate());
        existingTraining.setType(trainingObject.getType());
        existingTraining.setEndDate(trainingObject.getEndDate());
        existingTraining.setStartDate(trainingObject.getStartDate());
        existingTraining.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());

        // Save the updated event to the database
        Training updatedTraining = trainingRepository.save(existingTraining);

        // Clear existing associations for organizations and instructors
        trainingOrganizationRepository.deleteBytrainingId(updatedTraining.getId());
        trainingInstructorRepository.deleteByEventId(updatedTraining.getId());

        // Add new associations for organizations
        for (Integer organizationId : organizationIds) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + organizationId));

            TrainingOrganization trainingOrganization = new TrainingOrganization();
            trainingOrganization.setTraining(updatedTraining);
            trainingOrganization.setOrganization(organization);
            trainingOrganizationRepository.save(trainingOrganization);
        }

        // Add new associations for instructors
        for (Integer instructorId : instructorIds) {
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

            TrainingInstructor trainingInstructor = new TrainingInstructor();
            trainingInstructor.setTraining(updatedTraining);
            trainingInstructor.setInstructor(instructor);
            trainingInstructorRepository.save(trainingInstructor);
        }

        return updatedTraining;
    }




    public Training updateWithFileTraining(
            Training existingTraining,
            Training trainingObject,
            Set<Integer> organizationIds,
            Set<Integer> instructorIds,
            MultipartFile[] files) throws IOException {

        // Update the basic details of the training
        existingTraining.setTrainingName(trainingObject.getTrainingName());
        existingTraining.setTrainingDescription(trainingObject.getTrainingDescription());
        existingTraining.setNumberOfStudentsEnrolled(trainingObject.getNumberOfStudentsEnrolled());
        existingTraining.setEndRegistration(trainingObject.getEndRegistration());
        existingTraining.setMaxNumberOfStudents(trainingObject.getMaxNumberOfStudents());
        existingTraining.setEndDate(trainingObject.getEndDate());
        existingTraining.setStartDate(trainingObject.getStartDate());
        existingTraining.setType(trainingObject.getType());

        // Save the updated training object
        Training updatedTraining = trainingRepository.save(existingTraining);

        // Clear existing associations for organizations and instructors
        trainingOrganizationRepository.deleteBytrainingId(updatedTraining.getId());
        trainingInstructorRepository.deleteByEventId(updatedTraining.getId());

        // Update organizations
        for (Integer organizationId : organizationIds) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + organizationId));

            TrainingOrganization trainingOrganization = new TrainingOrganization();
            trainingOrganization.setTraining(updatedTraining);
            trainingOrganization.setOrganization(organization);
            trainingOrganizationRepository.save(trainingOrganization);
        }

        // Update instructors
        for (Integer instructorId : instructorIds) {
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

            TrainingInstructor trainingInstructor = new TrainingInstructor();
            trainingInstructor.setTraining(updatedTraining);
            trainingInstructor.setInstructor(instructor);
            trainingInstructorRepository.save(trainingInstructor);
        }

        // Handle file uploads
        if (files != null && files.length > 0) {
            // Mark existing pictures for this training as deleted
            List<Picture> existingPictures = pictureService.findByListingId(existingTraining.getId());
            for (Picture existingPicture : existingPictures) {
                existingPicture.setDeleted(true);
                pictureRepository.save(existingPicture); // Update the picture as deleted
            }

            // Add new pictures from the uploaded files
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String location = fileService.store(file); // Store the file and get its location
                    Picture picture = new Picture();
                    picture.setListing(existingTraining.getId()); // Associate the picture with the training ID
                    picture.setUrl(location);
                    picture.setUploadDate(Instant.now());
                    picture.setDeleted(false); // Mark as active
                    pictureService.save(picture);

                    // Optionally, add to the training object's picture list
                    trainingObject.getPictures().add(picture);
                }
            }
        }

        return updatedTraining;
    }


    // Create or update event
    public Training saveEvent(Training training) {
        return trainingRepository.save(training);
    }

    public List<Training> getAllCourses() {
        return trainingRepository.findByType(Type.TRAINING_COURSE);
    }


    // Get all events
//    public List<Event> getAllEvents() {
//        return trainingRepository.findAll();
//    }
    public List<Training> getAllEvents() {
        return trainingRepository.findByType(Type.EVENT);
    }



    // Get event by ID
    public Optional<Training> getTrainingById(Integer id) {
        return trainingRepository.findById(id);
    }

    // Delete event by ID
    public void deleteTraining(Integer id) {
        trainingRepository.deleteById(id);
    }
}
