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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerPutTests extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    private static final String ENDPOINT_PATH = "/api/v1/users";

    @Test
    @DisplayName("Should to update user info")
    public void shouldToUpdateUser() throws Exception {
        UserDto userDto = new UserDto("email1@gmail.com", "newFirstName", "newLastName", LocalDate.of(2000, 1, 1),
                null, null);

        String inputJson = super.mapToJson(userDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        UserDto updatedUserDto = fromJsonToObject(mvcResult.getResponse().getContentAsString(), UserDto.class);
        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH + "/email/" + updatedUserDto.email()))
                .andReturn();

        UserDto updatedUserGottenFromServer = fromJsonToObject(mvcResult2.getResponse().getContentAsString(), UserDto.class);
        Assertions.assertEquals(updatedUserGottenFromServer, updatedUserDto);
    }

    @Test
    @DisplayName("Should to fail to update user because wrong email")
    public void shouldToFailToUpdateUserBecauseWrongEmail() throws Exception {
        UserDto userDto = new UserDto("emailDoesNotExist@gmail.com", "newFirstName", "newLastName", LocalDate.of(2000, 1, 1),
                null, null);

        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders
                        .put(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should to update all user info (including email)")
    public void shouldToUpdateUserAllFields() throws Exception {
        UserDto userDto = new UserDto("email@gmail.com", "newFirstName", "newLastName", LocalDate.of(2000, 1, 1),
                null, null);
        String emailOfUserToUpdate = "email1@gmail.com";

        String inputJson = super.mapToJson(userDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .put(ENDPOINT_PATH + "/email/" + emailOfUserToUpdate)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        UserDto updatedUserDto = fromJsonToObject(mvcResult.getResponse().getContentAsString(), UserDto.class);
        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH + "/email/" + userDto.email()))
                .andReturn();

        UserDto updatedUserGottenFromServer = fromJsonToObject(mvcResult2.getResponse().getContentAsString(), UserDto.class);
        Assertions.assertEquals(updatedUserGottenFromServer, updatedUserDto);

        mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH + "/email/" + emailOfUserToUpdate))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should to fail update all user info because user age to less than")
    public void shouldToFailUpdateUserAllFieldsBecauseAge() throws Exception {
        UserDto userDto = new UserDto("email@gmail.com", "newFirstName", "newLastName", LocalDate.of(2020, 1, 1),
                null, null);
        String emailOfUserToUpdate = "email1@gmail.com";

        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders
                        .put(ENDPOINT_PATH + "/email/" + emailOfUserToUpdate)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("User age can't be less than")));
    }

    @Test
    @DisplayName("Should to fail update all user info because user was not found")
    public void shouldToFailUpdateUserAllFieldsBecauseWrongEmail() throws Exception {
        UserDto userDto = new UserDto("email@gmail.com", "newFirstName", "newLastName", LocalDate.of(2020, 1, 1),
                null, null);
        String emailOfUserToUpdate = "emailDoesNotExist@gmail.com";

        String inputJson = super.mapToJson(userDto);
        mvc.perform(MockMvcRequestBuilders
                        .put(ENDPOINT_PATH + "/email/" + emailOfUserToUpdate)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString("User with email emailDoesNotExist@gmail.com was not found")));
    }
}