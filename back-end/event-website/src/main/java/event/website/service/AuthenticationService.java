package event.website.service;

import event.website.model.Role;
import event.website.model.User;
import event.website.request.AuthenticationRequest;
import event.website.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.sql.DriverManager.println;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(SignupRequest signupRequest) {
        println(String.valueOf(signupRequest));
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        if(signupRequest.getRole() != null) {
            user.setRole(signupRequest.getRole());
        }
        else {
            user.setRole(Role.STUDENT);
        }
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        user.setPassword(encodedPassword);

        return userService.save(user);
    }



    public Authentication authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        println(authenticationToken.getName());
        return authenticationManager.authenticate(authenticationToken);
    }
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the list of granted authorities (roles)
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Return the role from the first authority
            if (authorities != null && !authorities.isEmpty()) {
                return authorities.iterator().next().getAuthority();  // Return role like "ROLE_SUPER_ADMIN"
            }
        }

        return "ROLE_USER";  // Default role if no authorities found
    }

}
