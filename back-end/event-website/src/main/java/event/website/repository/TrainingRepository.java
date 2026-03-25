package event.website.repository;

import event.website.model.Training;
import event.website.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
    Training findByTrainingName(String name);

//    Event findByType(@NotNull Type type);
    List<Training> findByType(Type type);

}
