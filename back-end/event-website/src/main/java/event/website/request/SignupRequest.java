package event.website.request;

import event.website.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignupRequest {
    private String username;
    private String password;
    @NotBlank
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    @NotBlank
    @Email
    private String email;


    public SignupRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }



    public @NotNull @Email String getEmail() {
        return email;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
