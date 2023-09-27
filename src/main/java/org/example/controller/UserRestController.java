package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.dto.BirthdayFilterDto;
import org.example.dto.UserDto;
import org.example.exception.ValidationException;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.example.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    @ResponseBody
    public List<UserDto> getAllUsers() {
        return userService.getAll().stream().map(userMapper::fromObjectToDto).toList();
    }

    @GetMapping("/email/{email}")
    @ResponseBody
    public UserDto getUser(@PathVariable String email) {
        return userMapper.fromObjectToDto(userService.getUserByEmail(email));
    }

    @GetMapping("/birthday")
    @ResponseBody
    public List<UserDto> getUsersByBirthday(@RequestBody @Valid BirthdayFilterDto birthdayFilterDto,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ValidationUtil.getErrors(bindingResult);
            throw new ValidationException("Failed to find users by birthday", errors);
        }

        List<User> users = userService.getUsersByBirthdayRange(
                birthdayFilterDto.dateRange().from(),
                birthdayFilterDto.dateRange().to()
        );

        return users.stream()
                .map(userMapper::fromObjectToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ValidationUtil.getErrors(bindingResult);
            throw new ValidationException("Failed to create new user", errors);
        }

        User user = userService.createUser(userMapper.fromDtoToObject(userDto));
        return userMapper.fromObjectToDto(user);
    }

    @PutMapping
    @ResponseBody
    public UserDto updateUser(@RequestBody @Valid UserDto userDto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ValidationUtil.getErrors(bindingResult);
            throw new ValidationException("Failed to update user", errors);
        }

        User user = userService.updateUserByEmail(userMapper.fromDtoToObject(userDto));
        return userMapper.fromObjectToDto(user);
    }

    @PutMapping("/email/{email}")
    @ResponseBody
    public UserDto updateUserAllFields(@RequestBody @Valid UserDto userDto,
                                       @PathVariable String email,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ValidationUtil.getErrors(bindingResult);
            throw new ValidationException("Failed to update user", errors);
        }

        User user = userService.updateWholeUser(userMapper.fromDtoToObject(userDto), email);
        return userMapper.fromObjectToDto(user);
    }

    @DeleteMapping("/email/{email}")
    @ResponseBody
    public boolean deleteUser(@PathVariable String email) {
        return userService.deleteUserByEmail(email);
    }
}
