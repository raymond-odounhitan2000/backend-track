package com.it_num.crud_api.integration;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.it_num.crud_api.TestcontainersConfiguration;
import com.it_num.crud_api.dto.request.create.UserCreateRequest;
import com.it_num.crud_api.dto.request.update.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("User Controller Integration Tests")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateRequest validUserRequest;

    @BeforeEach
    void setUp() {
        validUserRequest = new UserCreateRequest(
                "Raymond",
                "IT-Num",
                "raymond-test",
                "raymond@test.com",
                "Test bio",
                "https://example.com/avatar.png"
        );
    }

    // ============== CREATE ==============

    @Test
    @DisplayName("POST /users should create a user and return 201")
    void shouldCreateUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Raymond"))
                .andExpect(jsonPath("$.lastName").value("IT-Num"))
                .andExpect(jsonPath("$.fullName").value("Raymond IT-Num"))
                .andExpect(jsonPath("$.username").value("raymond-test"))
                .andExpect(jsonPath("$.email").value("raymond@test.com"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("POST /users with invalid data should return 400")
    void shouldReturn400WhenCreatingInvalidUser() throws Exception {
        UserCreateRequest invalid = new UserCreateRequest(
                "",
                "X",
                "ab",
                "not-an-email",
                null,
                null
        );

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists())
                .andExpect(jsonPath("$.fieldErrors.username").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    @DisplayName("POST /users with duplicate email should return 400")
    void shouldReturn400WhenEmailAlreadyExists() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isCreated());

        UserCreateRequest duplicate = new UserCreateRequest(
                "Other", "User", "other-username",
                "raymond@test.com",
                null, null
        );

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("Email already in use")));
    }

    // ============== READ ==============

    @Test
    @DisplayName("GET /users/{id} should return user")
    void shouldGetUserById() throws Exception {
        String publicId = createUserAndGetPublicId();

        mockMvc.perform(get("/users/" + publicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(publicId))
                .andExpect(jsonPath("$.email").value("raymond@test.com"));
    }

    @Test
    @DisplayName("GET /users/{id} with unknown id should return 404")
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("GET /users should return list of users")
    void shouldGetAllUsers() throws Exception {
        createUserAndGetPublicId();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ============== UPDATE ==============

    @Test
    @DisplayName("PUT /users/{id} should update user")
    void shouldUpdateUser() throws Exception {
        String publicId = createUserAndGetPublicId();

        UserUpdateRequest update = new UserUpdateRequest(
                "Updated",
                "Name",
                "raymond-test",
                "raymond@test.com",
                "Updated bio",
                null
        );

        mockMvc.perform(put("/users/" + publicId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.bio").value("Updated bio"));
    }

    @Test
    @DisplayName("PUT /users/{id} with unknown id should return 404")
    void shouldReturn404WhenUpdatingUnknownUser() throws Exception {
        UserUpdateRequest update = new UserUpdateRequest(
                "Updated",
                "Name",
                "valid-username",
                "valid@email.com",
                null,
                null
        );

        mockMvc.perform(put("/users/00000000-0000-0000-0000-000000000000")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    // ============== DELETE ==============

    @Test
    @DisplayName("DELETE /users/{id} should return 204")
    void shouldDeleteUser() throws Exception {
        String publicId = createUserAndGetPublicId();

        mockMvc.perform(delete("/users/" + publicId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + publicId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/{id} with unknown id should return 404")
    void shouldReturn404WhenDeletingUnknownUser() throws Exception {
        mockMvc.perform(delete("/users/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    // ============== Helpers ==============

    private String createUserAndGetPublicId() throws Exception {
        String response = mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("id").asString();
    }
}