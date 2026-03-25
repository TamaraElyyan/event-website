package event.website.repository;

import event.website.model.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {
    City findByNameEn(String nameEn);

    City getCityByid(Integer id);
    //City findByNameEn(String name);
}

