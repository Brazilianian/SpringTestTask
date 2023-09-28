package org.example;


import org.example.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerPostTests extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    private static final String ENDPOINT_PATH = "/api/v1/users";

    @Test
    @DisplayName("Should create user")
    public void shouldCreateUser() throws Exception {
        UserDto userDto = new UserDto(
                "email@email.com", "name", "lastName", LocalDate.of(2000, 1, 1),
                null, null);
        String inputJson = super.mapToJson(userDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andReturn();

        UserDto createdUserDto = fromJsonToObject(mvcResult.getResponse().getContentAsString(), UserDto.class);
        Assertions.assertEquals(createdUserDto.email(), userDto.email());
    }

    @Test
    @DisplayName("Should fail to create user when email is wrong")
    public void shouldFailToCreateUserWhenEmailIsWrong() throws Exception {
        UserDto userDto = new UserDto(
                "wrongEmail", "name", "lastName", LocalDate.of(2000, 1, 1),
                null, null);
        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Email must be valid")));
    }

    @Test
    @DisplayName("Should fail to create new user when email already exists")
    public void shouldFailToCreateNewUserWhenEmailAlreadyExists() throws Exception {
        UserDto userDto = new UserDto(
                "email1@gmail.com", "name", "lastName", LocalDate.of(2000, 1, 1),
                null, null);
        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("User with email email1@gmail.com already exists")));
    }

    @Test
    @DisplayName("Should fail to create new user when first name or last name or birthday are blank")
    public void shouldFailToCreateNewUserWhenFirstNameOrLastNameOrBirthdayAreBlank() throws Exception {
        UserDto userDto = new UserDto(
                "email@gmail.com", "", null, null,
                null, null);
        String inputJson = super.mapToJson(userDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(
                contentAsString.contains("First name can't be empty")
                        && contentAsString.contains("Last name can't be empty")
                        && contentAsString.contains("Date must be valid"));
    }

    @Test
    @DisplayName("Should fail to create user when birthday later than current date")
    public void shouldFailToCreateUserWhenBirthdayLaterThanCurrentDate() throws Exception {
        UserDto userDto = new UserDto(
                "email@gmail.com", "name", "lastName", LocalDate.of(2025, 1, 1),
                null, null);

        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Date of birthday must be valid")));
    }

    @Test
    @DisplayName("Should fail to create user when age less than")
    public void shouldFailToCreateUserWhenAgeLessThan() throws Exception {
        UserDto userDto = new UserDto(
                "email@gmail.com", "name", "lastName", LocalDate.of(2018, 1, 1),
                null, null);

        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("User age can't be less than")));
    }
}
