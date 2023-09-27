package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.example.domain.User;

import java.time.LocalDate;

public record UserDto(@Email(message = "Email must be valid")
                      @NotBlank(message = "Email can't be empty")
                      String email,

                      @NotBlank(message = "First name can't be empty")
                      String firstName,

                      @NotBlank(message = "Last name can't be empty")
                      String lastName,
                      @Past(message = "Date of birthday must be valid")
                      @NotNull(message = "Date must be valid") LocalDate birthday,
                      String address,
                      String phoneNumber) {
    public static UserDto from(User user) {
        return new UserDto(user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthday(),
                user.getAddress(),
                user.getPhoneNumber());
    }
}
