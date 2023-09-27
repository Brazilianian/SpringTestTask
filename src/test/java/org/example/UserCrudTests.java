package org.example;

import org.example.domain.User;
import org.example.dto.DateRangeDto;
import org.example.exception.wasnotfound.UserWasNotFoundException;
import org.example.handler.WasNotFoundExceptionHandler;
import org.example.service.UserService;
import org.hibernate.validator.cfg.defs.RangeDef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public class UserCrudTests {

    @Test
    @DisplayName("Test should create new user")
    public void shouldCreateNewUser() {
        UserService userService = new UserService();

        User user = new User
                .Builder("email@email.com", "firstName", "lastName", LocalDate.of(2000, 1, 1))
                .build();

        Assertions.assertEquals(user, userService.createUser(user));
    }

    @Test
    @DisplayName("Test should update user by email")
    public void shouldUpdateUserByEmail() {
        UserService userService = new UserService();

        User user = new User
                .Builder("email1@gmail.com", "firstName", "lastName", LocalDate.of(2000, 1, 1))
                .build();

        Assertions.assertEquals(user, userService.updateUserByEmail(user));
    }

    @Test
    @DisplayName("Test should update all fields of user")
    public void shouldUpdateUserAllFields() {
        UserService userService = new UserService();

        User user = new User
                .Builder("email@email.com", "firstName", "lastName", LocalDate.of(2000, 1, 1))
                .build();

        String userEmailToUpdate = "email1@gmail.com";

        User updatedUser = userService.updateWholeUser(user, userEmailToUpdate);

        Assertions.assertEquals(user, updatedUser);
        Assertions.assertThrowsExactly(UserWasNotFoundException.class, () -> userService.getUserByEmail(userEmailToUpdate));
    }

    @Test
    @DisplayName("Should delete user by email")
    public void shouldDeleteUser() {
        UserService userService = new UserService();

        String email = "email1@gmail.com";

        Assertions.assertEquals(userService.getUserByEmail(email).getEmail(), email);

        userService.deleteUserByEmail(email);

        Assertions.assertThrowsExactly(UserWasNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    @DisplayName("Should return list of users by birthday date range")
    public void shouldReturnListOfUsersByBirthdayRange() {
        UserService userService = new UserService();

        DateRangeDto dateRange = new DateRangeDto(
                LocalDate.of(2001, 1, 1),
                LocalDate.of(2004, 1, 1)
        );

        List<User> users = userService.getUsersByBirthdayRange(dateRange.from(), dateRange.to());

        for (User user : users) {
            Assertions.assertTrue(user.getBirthday().isAfter(dateRange.from()) && user.getBirthday().isBefore(dateRange.to()));
        }
    }
}
