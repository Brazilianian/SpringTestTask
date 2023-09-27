package org.example.service;

import org.example.domain.User;
import org.example.exception.ValidationException;
import org.example.exception.alreadyexists.UserAlreadyExistsException;
import org.example.exception.nocontent.UserNoContentException;
import org.example.exception.wasnotfound.UserWasNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>() {
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

        if (findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("Failed to create new user. User with email %s already exists", user.getEmail()));
        }

        users.add(user);
        return user;
    }

    /**
     * Update user info by email
     */
    public User updateUserByEmail(User user) {
        User userToUpdate = findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNoContentException(String.format("Can't update user. User with email %s was not found", user.getEmail())));

        return updateUser(userToUpdate, user);
    }

    /**
     * Update all fields of user by email (including old email)
     */
    public User updateWholeUser(User user, String email) {
        if (findByEmail(user.getEmail()).isEmpty()) {
            throw new UserNoContentException(String.format("Can't update user. User with email %s was not found", user.getEmail()));
        }

        // If new email already exists
        if (!user.getEmail().equals(email) && findByEmail(email).isPresent()) {
            throw new ValidationException(String.format("User with email %s already exists", user.getEmail()));
        }

        User userToUpdate = findByEmail(email).get();

        return updateUser(userToUpdate, user);
    }

    public User getUserByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new UserWasNotFoundException(String.format("User with email %s was not found", email)));
    }

    public boolean deleteUserByEmail(String email) {
        User user = findByEmail(email)
                .orElseThrow(() -> new UserNoContentException(String.format("Failed to delete user. User with email %s was not found", email)));

        return users.remove(user);
    }

    public List<User> getUsersByBirthdayRange(LocalDate from, LocalDate to) {
        return users.stream()
                .filter(u -> u.getBirthday().isAfter(from) && u.getBirthday().isBefore(to))
                .collect(Collectors.toList());
    }

    public List<User> getAll() {
        return users;
    }

    private Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    private User updateUser(User userToUpdate, User user) {
        if (isAgeInvalid(user)) {
            throw new ValidationException(String.format("Failed to update user. User age can't be less than %d", userSmallestAge));
        }

        users.removeIf(u -> u.getEmail().equals(userToUpdate.getEmail()));
        users.add(user);

        return user;
    }

    private boolean isAgeInvalid(User user) {
        return Period.between(user.getBirthday(), LocalDate.now()).getYears() <= userSmallestAge;
    }
}
