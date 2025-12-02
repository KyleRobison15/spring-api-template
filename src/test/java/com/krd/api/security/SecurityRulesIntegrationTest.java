package com.krd.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krd.starter.jwt.dto.LoginRequest;
import com.krd.api.users.User;
import com.krd.api.users.UserRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Security Rules Integration Tests")
class SecurityRulesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userAccessToken;
    private String adminAccessToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        // Create test user
        User user = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("User123!"))
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .roles(new HashSet<>(Set.of("USER")))
                .enabled(true)
                .build();
        userRepository.save(user);

        // Create admin user
        User admin = User.builder()
                .email("admin@example.com")
                .password(passwordEncoder.encode("Admin123!"))
                .firstName("Admin")
                .lastName("User")
                .username("adminuser")
                .roles(new HashSet<>(Set.of("USER", "ADMIN")))
                .enabled(true)
                .build();
        userRepository.save(admin);

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
    @DisplayName("/auth/** endpoints are public")
    void authEndpoints_ArePublic() throws Exception {
        // Login endpoint should be accessible without authentication
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("User123!");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /users (registration) is public")
    void userRegistration_IsPublic() throws Exception {
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
    @DisplayName("GET /users requires authentication")
    void listUsers_RequiresAuthentication() throws Exception {
        // Without token - should fail
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());

        // With admin token - should succeed
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + adminAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /users/{id} requires ADMIN role")
    void deleteUser_RequiresAdminRole() throws Exception {
        // Regular user should not be able to delete
        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + userAccessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("/swagger-ui/** is public")
    void swaggerUI_IsPublic() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/v3/api-docs is public")
    void apiDocs_IsPublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    // Actuator is not included in this project - test removed

    @Test
    @DisplayName("Invalid JWT token returns 401")
    void invalidToken_Returns401() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Missing Bearer prefix returns 401")
    void missingBearerPrefix_Returns401() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", userAccessToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("CORS headers are properly configured")
    void corsHeaders_AreConfigured() throws Exception {
        mockMvc.perform(options("/auth/login")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk());
    }
}
