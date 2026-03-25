package event.website.controller;

import event.website.model.Role;
import event.website.model.Student;
import event.website.model.User;
import event.website.service.StudentService;
import event.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173/")

public class StudentController {

    @Autowired
    private StudentService studentService;
    private UserService userService;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    public StudentController(UserService userService,StudentService studentService, StringHttpMessageConverter stringHttpMessageConverter) {
        this.userService=userService;
        this.studentService = studentService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }


    // Get all students
    @GetMapping("studentList")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isEmpty()) {
            throw new RuntimeException("student with ID " + id + " not found.");

        }
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create or update student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @PostMapping("/updateStudentProfile/{username}")
    public ResponseEntity<Student> createStudentProfile(@PathVariable String username,@RequestBody Student student) {
        User userDetails = (User) userService.loadUserByUsername(username);

        if (userDetails == null) {
            return ResponseEntity.notFound().build();
        }
        student.setUser(userDetails);
        if (userDetails.getRole() == Role.STUDENT) {
            student.setName(userDetails.getFirstName() + " " + userDetails.getLastName());
            Student savedStudent = studentService.saveStudent(student);
            return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudentProfile(@PathVariable Integer id, @RequestBody Student updatedStudentDetails) {
        // Find the existing student by ID
        Student existingStudent = studentService.findById(id);

        if (existingStudent == null) {
            // Return 404 Not Found if the student doesn't exist
            return ResponseEntity.notFound().build();
        }

        // Update fields if the provided details are not null
        existingStudent.setName(updatedStudentDetails.getName() != null ? updatedStudentDetails.getName() : existingStudent.getName());
        existingStudent.setEmail(updatedStudentDetails.getEmail() != null ? updatedStudentDetails.getEmail() : existingStudent.getEmail());
        existingStudent.setCity(updatedStudentDetails.getCity() != null ? updatedStudentDetails.getCity() : existingStudent.getCity());
        existingStudent.setJobTitle(updatedStudentDetails.getJobTitle() != null ? updatedStudentDetails.getJobTitle() : existingStudent.getJobTitle());
        existingStudent.setPhoneNumber(updatedStudentDetails.getPhoneNumber() != null ? updatedStudentDetails.getPhoneNumber() : existingStudent.getPhoneNumber());
        existingStudent.setSpecialization(updatedStudentDetails.getSpecialization() != null ? updatedStudentDetails.getSpecialization() : existingStudent.getSpecialization());

        // User association is mandatory, so ensure it remains unchanged
        if (updatedStudentDetails.getUser() != null) {
            existingStudent.setUser(updatedStudentDetails.getUser());
        }

        // Save the updated student
        Student updatedStudent = studentService.saveStudent(existingStudent);

        // Return the updated student details with 200 OK
        return ResponseEntity.ok(updatedStudent);
    }




    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        if (studentService.getStudentById(id).isPresent()) {
            student.setId(id); // Ensure we update the existing student
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete student
//    @DeleteMapping("delete/{id}")
//    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
//        if (studentService.getStudentById(id).isPresent()) {
//            studentService.softDeleteStudent(id);
//
//            return ResponseEntity.noContent().build();
//        } else {
//            throw new RuntimeException("student with ID " + id + " not found.");
//
//           // return ResponseEntity.notFound().build();
//        }
//    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String > deleteStudent(@PathVariable Integer id) {
        studentService.softDeleteStudent(id);
        return ResponseEntity.ok("Student with ID " + id + " successfully deleted.");
        //return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<?> getStudentByUsername(@PathVariable String username) {
        Optional<Student> student = studentService.getStudentByUsername(username);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
