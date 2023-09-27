package org.example.service;

import org.example.domain.User;
import org.example.exception.ValidationException;
import org.example.exception.alreadyexists.UserAlreadyExistsException;
import org.example.exception.wasnotfound.UserWasNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private List<User> users = new ArrayList<>() {
        {
            add(new User.Builder("email1@gmail.com", "first name 1", "last name 2", LocalDate.of(2000, 1, 1))
                    .phoneNumber("11111111")
                    .address("address 1")
                    .build());
        }

        {
            add(new User.Builder("email2@gmail.com", "first name 2", "last name 2", LocalDate.of(2002, 1, 1))
                    .phoneNumber("222222")
                    .address("address 2")
                    .build());
        }

        {
            add(new User.Builder("email3@gmail.com", "first name 3", "last name 3", LocalDate.of(2004, 1, 1))
                    .phoneNumber("33333")
                    .address("address 3")
                    .build());
        }
    };

    @Value("${user.smallest-age}")
    private int userSmallestAge;

    public User createUser(User user) {
        if (isAgeInvalid(user)) {
            throw new ValidationException("Failed to create new user", new HashMap<>() {{
                put("birthday", "User age can't be less than " + userSmallestAge);
            }});
        }

        users.add(user);
        return user;
    }

    /**
     * Update user info by email
     */
    public User updateUserByEmail(User user) {
        User userToUpdate = getUserByEmail(user.getEmail());

        return updateUser(userToUpdate, user);
    }

    /**
     * Update all fields of user by email (including old email)
     */
    public User updateWholeUser(User user, String email) {
        if (!user.getEmail().equals(email) && findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists", user.getEmail()));
        }

        User userToUpdate = getUserByEmail(email);

        return updateUser(userToUpdate, user);
    }

    private User findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    public boolean deleteUserByEmail(String email) {
        users = users.stream()
                .filter(u -> !u.getEmail().equals(email))
                .toList();

        return true;
    }

    public User getUserByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserWasNotFoundException(String.format("User with email %s was not found", email)));
    }

    private User updateUser(User userToUpdate, User user) {
        if (isAgeInvalid(user)) {
            throw new ValidationException(String.format("Failed to create new user. User age can't be less than %d", userSmallestAge));
        }

        users.removeIf(u -> u.getEmail().equals(userToUpdate.getEmail()));
        users.add(user);

        return user;
    }

    private boolean isAgeInvalid(User user) {
        return Period.between(user.getBirthday(), LocalDate.now()).getYears() <= userSmallestAge;
    }

    public List<User> getUsersByBirthdayRange(LocalDate from, LocalDate to) {
        return users.stream()
                .filter(u -> u.getBirthday().isAfter(from) && u.getBirthday().isBefore(to))
                .collect(Collectors.toList());
    }

    public List<User> getAll() {
        return users;
    }
}