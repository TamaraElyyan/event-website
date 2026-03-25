package event.website.controller;

import event.website.model.Role;
import event.website.model.User;
import event.website.repository.UserRepository;
import event.website.request.AuthenticationRequest;
import event.website.request.SignupRequest;
import event.website.service.AuthenticationService;
import event.website.service.JWTService;
import event.website.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "http://localhost:5173/")

public class AuthenticationController {
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(JWTService jwtService, AuthenticationService authenticationService, UserService userService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest requestBody) {
//        if(requestBody.getRole() == Role.ADMIN ) {
//          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        User user=authenticationService.signup(requestBody);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);

        User user = authenticationService.signup(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/add-admin")
    public ResponseEntity<User> addAdmin(@RequestBody SignupRequest requestBody) {
//        if(requestBody.getRole() != Role.ADMIN ) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
        User user=authenticationService.signup(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }


//    @PostMapping("/login")
//    public String login(@RequestBody AuthenticationRequest requestBody) {
//        Authentication authenticate = authenticationService.authenticate(requestBody);
//        if (authenticate.isAuthenticated()) {
//            return jwtService.generateToken(authenticate);
//        } else {
//            throw new BadCredentialsException("Bad credentials");
//        }
//    }
@CrossOrigin(origins = "http://localhost:5173/") // Adjust domain if needed
@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody AuthenticationRequest requestBody) {
    //log.info("Login attempt with username: {}", requestBody.getUsername());
    try {
        Authentication authenticate = authenticationService.authenticate(requestBody);
        User user = (User) authenticate.getPrincipal();

        if(user!=null) {
            ZoneId palestineTimeZone = ZoneId.of("Asia/Jerusalem");
            Instant nowInPalestine = ZonedDateTime.now(palestineTimeZone).toInstant();

            user.setLastLogin(nowInPalestine);
            userService.save(user);
           // user.setLastLogin(Instant.now());
            userService.save(user);
        }

        if (authenticate.isAuthenticated()) {
            String jwtToken = jwtService.generateToken(authenticate);
            return ResponseEntity.ok(jwtToken);
        } else {
           // log.error("Authentication failed for user: {}", requestBody.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
    } catch (BadCredentialsException ex) {
       // log.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
    }
}




    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            // Get the JWT from the request
            String jwt = getJwtFromRequest(request);
            if (jwt != null) {
                // Optionally, add the JWT to a blacklist (e.g., Redis or in-memory storage)
                jwtService.addToBlacklist(jwt); // Implement this method to add to a blacklist
            }

            // Client-side: Simply delete the JWT token from the client (front-end handles it)
            return ResponseEntity.ok("Successfully logged out");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // Extract the token from the request headers (assuming it's in the Authorization header)
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract JWT token from the header
        }
        return null;
    }


}
