package org.example.mapper;

import org.example.domain.User;
import org.example.dto.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserMapper {

    public UserDto fromObjectToDto(User user) {
        return UserDto.from(user);
    }

    public User fromDtoToObject(UserDto userDto) {
        return new User.Builder(userDto.email(), userDto.firstName(), userDto.lastName(), userDto.birthday())
                .address(userDto.address())
                .phoneNumber(userDto.phoneNumber())
                .build();
    }
}
