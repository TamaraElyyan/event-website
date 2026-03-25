package event.website.service;

import event.website.model.City;
import event.website.model.Contact;
import event.website.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService  {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    public List<City> getAllCities() {
        return (List<City>) cityRepository.findAll();
    }

    public City loadByCityName(String nameEn) throws UsernameNotFoundException {
        nameEn = nameEn.trim();

        City city = cityRepository.findByNameEn(nameEn);
        if (city == null) {
            throw new UsernameNotFoundException("City not found");
        }
        return city;
    }
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public void deleteCity(Integer id) {
        cityRepository.deleteById(Long.valueOf(id));
    }
    public City findCityById(Integer id) {
        return cityRepository.findById(Long.valueOf(id)).orElse(null);
    }
    public void softDeleteCity(Integer id) {
        City city = cityRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("City with ID " + id + " not found."));

        // Mark the city as deleted by setting the deleted flag to true
        city.setDeleted(true);

        // Save the updated city record
        cityRepository.save(city);
    }


}
