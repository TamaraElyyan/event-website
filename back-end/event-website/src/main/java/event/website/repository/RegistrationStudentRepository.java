package event.website.repository;

import event.website.model.RegistrationStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationStudentRepository extends JpaRepository<RegistrationStudent, Integer> {

    Optional<RegistrationStudent> findById(Integer id);
    List<RegistrationStudent> findByStudentIdAndEnrolledTrue(Integer studentId);
    List<RegistrationStudent> findByTrainingId(Integer TrainingId);

}
