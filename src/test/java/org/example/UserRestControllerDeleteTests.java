package org.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerDeleteTests extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    private static final String ENDPOINT_PATH = "/api/v1/users";

    @Test
    @DisplayName("Should delete user")
    public void shouldDeleteUser() throws Exception {
        String emailOfUserToDelete = "email1@gmail.com";

        mvc.perform(MockMvcRequestBuilders
                        .delete(ENDPOINT_PATH + "/email/" + emailOfUserToDelete))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                        .delete(ENDPOINT_PATH + "/email/" + emailOfUserToDelete))
                .andExpect(status().isNoContent());
    }
}
