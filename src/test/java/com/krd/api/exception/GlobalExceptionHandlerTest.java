package com.krd.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krd.api.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Validation errors return 400 with standardized ErrorResponse structure")
    void validationErrors_Return400WithStandardizedStructure() throws Exception {
        // Missing required fields and invalid password
        String invalidJson = """
                {
                    "email": "invalid-email",
                    "password": "weak"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed for one or more fields"))
                .andExpect(jsonPath("$.path").value("/users"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItems("password", "email")));
    }

    @Test
    @DisplayName("Invalid email format returns validation error with field details")
    void invalidEmail_ReturnsValidationError() throws Exception {
        String invalidJson = """
                {
                    "email": "not-an-email",
                    "password": "ValidPass123!",
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field=='email')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field=='email')].message").exists());
    }

    @Test
    @DisplayName("Missing required fields returns validation errors")
    void missingRequiredFields_ReturnsValidationErrors() throws Exception {
        String incompleteJson = """
                {
                    "firstName": "Test"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItems("email", "password")));
    }

    @Test
    @DisplayName("Malformed JSON returns 400 with standardized error response")
    void malformedJson_Returns400() throws Exception {
        String malformedJson = "{ invalid json }";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request or invalid request body"))
                .andExpect(jsonPath("$.path").value("/users"))
                .andExpect(jsonPath("$.errors").doesNotExist()); // No field errors for malformed JSON
    }

    @Test
    @DisplayName("Empty JSON body returns validation errors")
    void emptyJsonBody_ReturnsValidationErrors() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItems("email", "password")));
    }

    @Test
    @DisplayName("Invalid Content-Type returns appropriate error")
    void invalidContentType_ReturnsError() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("some text"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Password validation errors contain specific messages")
    void passwordValidationErrors_ContainSpecificMessages() throws Exception {
        String weakPasswordJson = """
                {
                    "email": "test@example.com",
                    "password": "short",
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(weakPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field=='password')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field=='password')].message", hasItem(containsString("at least"))));
    }

    @Test
    @DisplayName("Multiple validation errors are all returned in errors array")
    void multipleValidationErrors_AllReturned() throws Exception {
        String multipleErrorsJson = """
                {
                    "email": "not-valid-email",
                    "password": "weak",
                    "firstName": "",
                    "lastName": ""
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(multipleErrorsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(1))));
    }

    @Test
    @DisplayName("Null values in required fields return validation errors")
    void nullRequiredFields_ReturnsValidationErrors() throws Exception {
        String nullFieldsJson = """
                {
                    "email": null,
                    "password": null,
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullFieldsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field", hasItems("email", "password")));
    }

    @Test
    @DisplayName("Extra unknown fields are ignored")
    void extraUnknownFields_AreIgnored() throws Exception {
        String extraFieldsJson = """
                {
                    "email": "test@example.com",
                    "password": "ValidPass123!",
                    "firstName": "Test",
                    "lastName": "User",
                    "unknownField": "should be ignored"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(extraFieldsJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Error response includes rejected values for debugging")
    void errorResponse_IncludesRejectedValues() throws Exception {
        String invalidJson = """
                {
                    "email": "invalid-email",
                    "password": "weak",
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field=='email')].rejectedValue").value("invalid-email"))
                .andExpect(jsonPath("$.errors[?(@.field=='password')].rejectedValue").value("weak"));
    }
}
