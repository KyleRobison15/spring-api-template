package com.krd.api.validation;

import com.krd.starter.user.dto.RegisterUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Password Validation Tests")
class PasswordValidationTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("Valid password with all requirements met")
    void validatePassword_WithValidPassword_PassesValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("ValidPass123!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Password too short - fails validation")
    void validatePassword_TooShort_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("Sh0rt!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("at least 8 characters")
        );
    }

    @Test
    @DisplayName("Password too long - fails validation")
    void validatePassword_TooLong_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        // Create password longer than 128 characters
        String longPassword = "A".repeat(129) + "a1!";

        request.setPassword(longPassword);

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("must not exceed 128 characters")
        );
    }

    @Test
    @DisplayName("Password without uppercase - fails validation")
    void validatePassword_NoUppercase_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("lowercase123!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("uppercase letter")
        );
    }

    @Test
    @DisplayName("Password without lowercase - fails validation")
    void validatePassword_NoLowercase_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("UPPERCASE123!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("lowercase letter")
        );
    }

    @Test
    @DisplayName("Password without digit - fails validation")
    void validatePassword_NoDigit_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("NoDigitsHere!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("number")
        );
    }

    @Test
    @DisplayName("Password without special character - fails validation")
    void validatePassword_NoSpecialChar_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("NoSpecialChar123");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("special character")
        );
    }

    @Test
    @DisplayName("Password with all special characters accepted")
    void validatePassword_WithAllSpecialChars_PassesValidation() {
        String[] validPasswords = {
                "Test123@", "Test123$", "Test123!", "Test123%",
                "Test123*", "Test123?", "Test123&", "Test123#",
                "Test123^", "Test123(", "Test123)", "Test123-",
                "Test123_", "Test123=", "Test123+", "Test123[",
                "Test123]", "Test123{", "Test123}", "Test123|",
                "Test123;", "Test123:", "Test123,", "Test123.",
                "Test123<", "Test123>"
        };

        for (String password : validPasswords) {
            RegisterUserRequest request = createValidRequest();
            request.setPassword(password);

            Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .as("Password with special char '%s' should be valid", password.substring(password.length() - 1))
                    .filteredOn(v -> v.getPropertyPath().toString().equals("password"))
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Null password - fails validation")
    void validatePassword_Null_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword(null);

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password")
        );
    }

    @Test
    @DisplayName("Empty password - fails validation")
    void validatePassword_Empty_FailsValidation() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v ->
                v.getPropertyPath().toString().equals("password")
        );
    }

    @Test
    @DisplayName("Password with spaces - passes if meets requirements")
    void validatePassword_WithSpaces_PassesIfValid() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("Valid Pass 123!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations)
                .filteredOn(v -> v.getPropertyPath().toString().equals("password"))
                .isEmpty();
    }

    @Test
    @DisplayName("Password with unicode characters - passes if meets requirements")
    void validatePassword_WithUnicode_PassesIfValid() {
        RegisterUserRequest request = createValidRequest();
        request.setPassword("Válid123!");

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);

        assertThat(violations)
                .filteredOn(v -> v.getPropertyPath().toString().equals("password"))
                .isEmpty();
    }

    private RegisterUserRequest createValidRequest() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("ValidPass123!");
        request.setFirstName("Test");
        request.setLastName("User");
        return request;
    }
}
