package com.krd.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krd.starter.jwt.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("User Controller Integration Tests")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User regularUser;
    private User adminUser;
    private String userAccessToken;
    private String adminAccessToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        // Create regular user
        regularUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("User123!"))
                .firstName("Regular")
                .lastName("User")
                .username("regularuser")
                .roles(new HashSet<>(Set.of("USER")))
                .enabled(true)
                .build();
        regularUser = userRepository.save(regularUser);

        // Create admin user
        adminUser = User.builder()
                .email("admin@example.com")
                .password(passwordEncoder.encode("Admin123!"))
                .firstName("Admin")
                .lastName("User")
                .username("adminuser")
                .roles(new HashSet<>(Set.of("USER", "ADMIN")))
                .enabled(true)
                .build();
        adminUser = userRepository.save(adminUser);

        // Get access tokens
        userAccessToken = getAccessToken("user@example.com", "User123!");
        adminAccessToken = getAccessToken("admin@example.com", "Admin123!");
    }

    private String getAccessToken(String email, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @DisplayName("POST /users - Success - Register new user")
    void registerUser_WithValidData_ReturnsCreated() throws Exception {
        String registerJson = """
                {
                    "email": "newuser@example.com",
                    "password": "NewUser123!",
                    "firstName": "New",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /users - Fail - Weak password")
    void registerUser_WithWeakPassword_ReturnsBadRequest() throws Exception {
        String registerJson = """
                {
                    "email": "newuser@example.com",
                    "password": "weak",
                    "firstName": "New",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users - Fail - Duplicate email")
    void registerUser_WithDuplicateEmail_ReturnsConflict() throws Exception {
        String registerJson = """
                {
                    "email": "user@example.com",
                    "password": "ValidPass123!",
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /users - Success - List all users")
    void listUsers_WithAuthentication_ReturnsOk() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /users/{id} - Success - Get user by ID")
    void getUserById_WithValidId_ReturnsOk() throws Exception {
        mockMvc.perform(get("/users/" + regularUser.getId())
                        .header("Authorization", "Bearer " + userAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @DisplayName("GET /users/{id} - Fail - User not found")
    void getUserById_WithInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/users/99999")
                        .header("Authorization", "Bearer " + userAccessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/{id} - Success - User updates own profile")
    void updateUser_UserUpdatesOwnProfile_ReturnsOk() throws Exception {
        String updateJson = """
                {
                    "firstName": "Updated"
                }
                """;

        mockMvc.perform(put("/users/" + regularUser.getId())
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /users/{id} - Fail - User updates another user's profile")
    void updateUser_UserUpdatesAnotherProfile_ReturnsForbidden() throws Exception {
        String updateJson = """
                {
                    "firstName": "Hacker"
                }
                """;

        mockMvc.perform(put("/users/" + adminUser.getId())
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /users/{id} - Success - Admin updates any user")
    void updateUser_AdminUpdatesAnyUser_ReturnsOk() throws Exception {
        String updateJson = """
                {
                    "firstName": "AdminUpdated"
                }
                """;

        mockMvc.perform(put("/users/" + regularUser.getId())
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{id} - Success - Admin soft deletes user")
    void deleteUser_AdminDeletesUser_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/users/" + regularUser.getId())
                        .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /users/{id} - Fail - Regular user cannot delete")
    void deleteUser_RegularUserCannotDelete_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/users/" + regularUser.getId())
                        .header("Authorization", "Bearer " + userAccessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /users/{id}/roles - Success - Admin adds role")
    void addRole_AdminAddsRole_ReturnsOk() throws Exception {
        String addRoleJson = """
                {
                    "role": "ADMIN"
                }
                """;

        mockMvc.perform(post("/users/" + regularUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRoleJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /users/{id}/roles - Fail - Regular user cannot add roles")
    void addRole_RegularUserCannotAddRoles_ReturnsForbidden() throws Exception {
        String addRoleJson = """
                {
                    "role": "ADMIN"
                }
                """;

        mockMvc.perform(post("/users/" + regularUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRoleJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /users/{id}/roles - Success - Admin removes role")
    void removeRole_AdminRemovesRole_ReturnsOk() throws Exception {
        // First add ADMIN role to regularUser
        String addRoleJson = """
                {
                    "role": "ADMIN"
                }
                """;
        mockMvc.perform(post("/users/" + regularUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRoleJson))
                .andExpect(status().isOk());

        // Now remove ADMIN role from regularUser
        String deleteRoleJson = """
                {
                    "role": "ADMIN"
                }
                """;

        mockMvc.perform(delete("/users/" + regularUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRoleJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{id}/roles - Fail - Regular user cannot remove roles")
    void removeRole_RegularUserCannotRemoveRoles_ReturnsForbidden() throws Exception {
        String deleteRoleJson = """
                {
                    "role": "USER"
                }
                """;

        mockMvc.perform(delete("/users/" + regularUser.getId() + "/roles")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRoleJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /users/{id}/change-password - Success - User changes own password")
    void changePassword_UserChangesOwnPassword_ReturnsOk() throws Exception {
        String changePasswordJson = """
                {
                    "oldPassword": "User123!",
                    "newPassword": "NewPassword123!",
                    "confirmPassword": "NewPassword123!"
                }
                """;

        mockMvc.perform(post("/users/" + regularUser.getId() + "/change-password")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /users/{id}/change-password - Fail - Wrong old password")
    void changePassword_WrongOldPassword_ReturnsBadRequest() throws Exception {
        String changePasswordJson = """
                {
                    "oldPassword": "WrongPassword123!",
                    "newPassword": "NewPassword123!",
                    "confirmPassword": "NewPassword123!"
                }
                """;

        mockMvc.perform(post("/users/" + regularUser.getId() + "/change-password")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users/{id}/change-password - Fail - Weak new password")
    void changePassword_WeakNewPassword_ReturnsBadRequest() throws Exception {
        String changePasswordJson = """
                {
                    "oldPassword": "User123!",
                    "newPassword": "weak"
                }
                """;

        mockMvc.perform(post("/users/" + regularUser.getId() + "/change-password")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users/{id}/change-password - Fail - User changes another user's password")
    void changePassword_UserChangesAnotherPassword_ReturnsForbidden() throws Exception {
        String changePasswordJson = """
                {
                    "oldPassword": "Admin123!",
                    "newPassword": "NewPassword123!",
                    "confirmPassword": "NewPassword123!"
                }
                """;

        mockMvc.perform(post("/users/" + adminUser.getId() + "/change-password")
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changePasswordJson))
                .andExpect(status().isForbidden());
    }
}
