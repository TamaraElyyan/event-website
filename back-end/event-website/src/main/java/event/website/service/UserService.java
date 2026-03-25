package event.website.service;

import event.website.model.User;
import event.website.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        try {
           return userRepository.save(user);
        } catch (ConstraintViolationException violationException) {
            throw violationException;
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User saving Failed"+e.getMessage());
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //forced to send execption
       return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public  void deleteById(int id) {
        userRepository.deleteById((long) id);
    }




}
