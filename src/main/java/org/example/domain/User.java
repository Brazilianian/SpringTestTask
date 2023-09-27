package org.example.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @NonNull
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private LocalDate birthday;

    private String address;
    private String phoneNumber;

    public static class Builder {
        private final String email;
        private final String firstName;
        private final String lastName;
        private final LocalDate birthday;

        public Builder(String email, String firstName, String lastName, LocalDate birthday) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = birthday;
        }

        private String address;
        private String phoneNumber;

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public User build() {
            return new User(email, firstName, lastName, birthday, address, phoneNumber);
        }
    }
}
