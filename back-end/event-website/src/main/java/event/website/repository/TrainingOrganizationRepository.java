package event.website.repository;

import event.website.model.TrainingOrganization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingOrganizationRepository extends JpaRepository<TrainingOrganization, Integer> {

    Optional<TrainingOrganization> findById(Integer id);
    @Modifying
    @Transactional
    @Query("DELETE FROM TrainingOrganization eo WHERE eo.training.id = :trainingId")
    void deleteBytrainingId(@Param("trainingId") Integer trainingId);
}
