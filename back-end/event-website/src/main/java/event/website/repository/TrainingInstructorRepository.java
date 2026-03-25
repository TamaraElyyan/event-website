package event.website.repository;

import event.website.model.TrainingInstructor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingInstructorRepository extends JpaRepository<TrainingInstructor, Integer> {

    Optional<TrainingInstructor> findById(Integer id);
    @Modifying
    @Transactional
    @Query("DELETE FROM TrainingInstructor ei WHERE ei.training.id = :trainingId")
    void deleteByEventId(@Param("trainingId") Integer trainingId);
}
