package event.website.controller;

import event.website.model.City;
import event.website.model.Contact;
import event.website.repository.CityRepository;
import event.website.service.CityService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/city")
@CrossOrigin(origins = "http://localhost:5173/")

public class CityController {
    private final CityService cityService;
    private final StringHttpMessageConverter stringHttpMessageConverter;
    private final CityRepository cityRepository;

    public CityController(CityService cityService, StringHttpMessageConverter stringHttpMessageConverter, CityRepository cityRepository) {
        this.cityService = cityService;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
        this.cityRepository = cityRepository;
    }
    @GetMapping("/cityList")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

//    @GetMapping("{username}")
//    public User getUserByUsername(@PathVariable("username") String username) {
//
//        return (User) userService.loadUserByUsername(username);
//    }
//    @GetMapping("{cityname}")
//    public ResponseEntity<City> getUserByUsername(@PathVariable("cityname") String nameEn) {
//        City city = cityService.loadUserByCityName(nameEn);
//
//        if (city == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Return 200 OK with the city object
//        return ResponseEntity.ok(city);
//    }

    @GetMapping("{id}")
    public ResponseEntity<City> getCity(@PathVariable Integer id , @Valid String nameEn) {
        try {
            // Attempt to load the city by the given name
                City city = cityService.findCityById(id);

            // If city is not found, return 404 not found
            if (city == null) {
                return ResponseEntity.notFound().build();
            }

            // If city is found, return 200 OK with the city object
            return ResponseEntity.ok(city);
        } catch (Exception e) {
            // If an unexpected exception occurs, it will be caught here
            // This will be handled by the global exception handler
            throw new RuntimeException("An error occurred while fetching the city: " + e.getMessage());
        }
    }


    @PostMapping("/newCity")
    public ResponseEntity<City> newCity(@RequestBody City city) {
        City cityNew = cityService.saveCity(city);
        if (cityNew == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(cityNew);
    }

@PostMapping("/addCity")
public ResponseEntity<?> addCity(@RequestBody @Valid City city) throws SQLIntegrityConstraintViolationException {
    try {
        City cityNew = cityService.saveCity(city);
        return ResponseEntity.ok(cityNew);
    }
//    catch (DataIntegrityViolationException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body("Duplicate entry: A city with the name '" + city.getNameEn() + "' already exists.");
//    } catch (Exception ex) {
//        // Handle other exceptions
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("An error occurred while saving the city.");
//    }

   catch (DataIntegrityViolationException ex) {
        throw new SQLIntegrityConstraintViolationException("Duplicate entry: A city with the name '" + city.getNameEn() + "' already exists.");
    } catch (Exception ex) {
        throw new RuntimeException("An error occurred while saving the city.", ex);
    }
}


    @PutMapping("/updateCity/{id}")
    public City updateCity(@PathVariable Integer id ,@RequestBody @Valid City city) {
            City cityObject=cityRepository.getCityByid(id);
            if (cityObject == null) {
                throw new RuntimeException("City with ID " + id + " not found.");
            }
            cityObject.setNameEn(city.getNameEn());
            cityObject.setNameAr(city.getNameAr());
            return cityService.saveCity(cityObject);
    }

    @DeleteMapping("/{id}")
    public String deleteCity(@PathVariable("id")  Integer id) {
        City city=cityService.findCityById(id);
        if (city == null) {
            throw new RuntimeException("City with ID " + id + " not found.");

        }
        cityService.deleteCity(id);
        return "City " + id + " was deleted.";
    }
    //soft delete
    @DeleteMapping("deleteCity/{id}")
    public ResponseEntity<String> deleteSoftCity(@PathVariable("id") Integer id) {
        City city = cityService.findCityById(id);
        if (city == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("City with ID " + id + " not found.");
        }

        // Perform soft delete by setting the deleted flag to true
        cityService.softDeleteCity(id);

        return ResponseEntity.ok("City " + id + " was successfully soft deleted.");
    }


}
