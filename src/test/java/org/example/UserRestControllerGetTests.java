package org.example;

import org.example.domain.DateRange;
import org.example.dto.BirthdayFilterDto;
import org.example.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerGetTests extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    private static final String ENDPOINT_PATH = "/api/v1/users";

    @Test
    @DisplayName("Should get all users")
    public void shouldGetAllUsers() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        List<UserDto> userDtoList = fromJsonToList(mvcResult.getResponse().getContentAsString(), UserDto.class);
        Assertions.assertEquals(userDtoList.size(), 3);
    }

    @Test
    @DisplayName("Should successfully find user by email")
    public void shouldFindUserByEmail() throws Exception {
        String emailOfUserToFind = "email2@gmail.com";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH + "/email/" + emailOfUserToFind)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDto userDto = fromJsonToObject(mvcResult.getResponse().getContentAsString(), UserDto.class);

        Assertions.assertEquals(userDto.email(), emailOfUserToFind);
    }

    @Test
    @DisplayName("Should dont find user by email when email does not exist")
    public void shouldDontFindUserByEmailWhenEmailDoesNotExist() throws Exception {
        String emailOfUserToFind = "email@gmail.com";
        mvc.perform(MockMvcRequestBuilders
                        .get(ENDPOINT_PATH + "/email/" + emailOfUserToFind)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find users by birthday range")
    public void shouldFindUsersByBirthdayRange() throws Exception {
        BirthdayFilterDto birthdayFilterDto = new BirthdayFilterDto(new DateRange(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2005, 1, 1)
        ));

        String json = super.mapToJson(birthdayFilterDto);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH + "/birthday")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDto> userDtoList = fromJsonToList(mvcResult.getResponse().getContentAsString(), UserDto.class);
        for (UserDto userDto : userDtoList) {
            Assertions.assertTrue(
                    userDto.birthday().isAfter(birthdayFilterDto.dateRange().from())
                            && userDto.birthday().isBefore(birthdayFilterDto.dateRange().to())
            );
        }
    }

    @Test
    @DisplayName("Should fail to find users by birthday range when range is wrong")
    public void shouldFailToFindUsersByBirthdayRangeWhenRangeIsWrong() throws Exception {
        BirthdayFilterDto birthdayFilterDto = new BirthdayFilterDto(new DateRange(
                LocalDate.of(2005, 1, 1),
                LocalDate.of(2000, 1, 1)
        ));

        String json = super.mapToJson(birthdayFilterDto);

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_PATH + "/birthday")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Invalid range of date. From must be earlier than To")));
    }
}
