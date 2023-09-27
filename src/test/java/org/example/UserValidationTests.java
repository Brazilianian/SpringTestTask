package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.UserRestController;
import org.example.domain.User;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
public class UserValidationTests {
    private static final String ENDPOINT_PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    public void testCreateUserShouldReturn422BecauseWrongEmail() throws Exception {
        UserDto userDto = new UserDto("wrongemail", "name", "lastname", LocalDate.of(2000, 1, 1), null, null);

        String requestBody = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Email must be valid")));
    }

    @Test
    public void testCreateUserShouldReturn422BecauseWrongBirthday() throws Exception {
        UserDto userDto = new UserDto("email@email.com", "name", "lastname", LocalDate.of(2024, 1, 1), null, null);

        String requestBody = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Date of birthday must be valid")));
    }
}
