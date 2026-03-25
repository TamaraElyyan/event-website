package event.website.service;

import event.website.config.AlreadyDeletedException;
import event.website.model.Student;
import event.website.model.User;
import event.website.repository.StudentRepository;
import event.website.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {


    private StudentRepository studentRepository;
    private UserRepository userRepository;

    @Autowired
    public StudentService(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }



    // Create or update student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    // Delete student by ID
    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

//    @Transactional
//    public void softDeleteStudent(Integer id) {
//        // Fetch student by ID and handle not found
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found."));
//
//        // Check if the student is already deleted
//        if (student.isDeleted()) {
//            throw new AlreadyDeletedException("Student with ID " + id + " is already deleted.");
//        }
//
//        // Mark the student as deleted
//        student.setDeleted(true);
//
//        // Mark the associated user as deleted
//        User user = student.getUser(); // Assuming the relationship is eagerly loaded
//        if (user != null) {
//            user.setDeleted(true);
//            userRepository.save(user);
//        }
//
//        // Save the updated student record
//        studentRepository.save(student);
//    }

    @Transactional
    public void softDeleteStudent(Integer id) {
        // Fetch student by ID and handle not found
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found."));

        // Check if the student is already deleted
        if (student.isDeleted()) {
            throw new AlreadyDeletedException("Student with ID " + id + " is already deleted.");
        }

        // Mark the student as deleted
        student.setDeleted(true);

        // Mark the associated user as deleted
        User user = student.getUser(); // Assuming the relationship is eagerly loaded
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user); // Save the user if it exists
        }

        // Save the updated student record
        studentRepository.save(student);
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id).orElse(null);

    }

    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
}
