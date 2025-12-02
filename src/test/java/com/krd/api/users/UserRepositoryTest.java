package com.krd.api.users;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser1 = User.builder()
                .email("test1@example.com")
                .password("password1")
                .firstName("Test1")
                .lastName("User1")
                .username("testuser1")
                .roles(new HashSet<>(Set.of("USER")))
                .enabled(true)
                .build();

        testUser2 = User.builder()
                .email("test2@example.com")
                .password("password2")
                .firstName("Test2")
                .lastName("User2")
                .username("testuser2")
                .roles(new HashSet<>(Set.of("USER", "ADMIN")))
                .enabled(true)
                .build();

        testUser1 = entityManager.persist(testUser1);
        testUser2 = entityManager.persist(testUser2);
        entityManager.flush();
    }

    @Test
    @DisplayName("Find user by email - Success")
    void findByEmail_WithExistingEmail_ReturnsUser() {
        Optional<User> found = userRepository.findByEmail("test1@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test1@example.com");
        assertThat(found.get().getFirstName()).isEqualTo("Test1");
        assertThat(found.get().getLastName()).isEqualTo("User1");
    }

    @Test
    @DisplayName("Find user by email - Not found")
    void findByEmail_WithNonExistentEmail_ReturnsEmpty() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Check email exists - Returns true for existing email")
    void existsByEmail_WithExistingEmail_ReturnsTrue() {
        boolean exists = userRepository.existsByEmail("test1@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Check email exists - Returns false for non-existent email")
    void existsByEmail_WithNonExistentEmail_ReturnsFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Check username exists - Returns true for existing username")
    void existsByUsername_WithExistingUsername_ReturnsTrue() {
        boolean exists = userRepository.existsByUsername("testuser1");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Check username exists - Returns false for non-existent username")
    void existsByUsername_WithNonExistentUsername_ReturnsFalse() {
        boolean exists = userRepository.existsByUsername("nonexistent");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Save user with all fields")
    void saveUser_WithAllFields_Persists() {
        User newUser = User.builder()
                .email("newuser@example.com")
                .password("newpassword")
                .firstName("New")
                .lastName("User")
                .username("newuser")
                .roles(new HashSet<>(Set.of("USER")))
                .enabled(true)
                .build();

        User saved = userRepository.save(newUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("newuser@example.com");
        assertThat(saved.getPassword()).isEqualTo("newpassword");
        assertThat(saved.getFirstName()).isEqualTo("New");
        assertThat(saved.getLastName()).isEqualTo("User");
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getRoles()).containsExactly("USER");
        assertThat(saved.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Save user with only required fields")
    void saveUser_WithOnlyRequiredFields_Persists() {
        User minimalUser = User.builder()
                .email("minimal@example.com")
                .password("password")
                .roles(new HashSet<>())
                .enabled(true)
                .build();

        User saved = userRepository.save(minimalUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("minimal@example.com");
        assertThat(saved.getPassword()).isEqualTo("password");
        assertThat(saved.getFirstName()).isNull();
        assertThat(saved.getLastName()).isNull();
        assertThat(saved.getUsername()).isNull();
        assertThat(saved.getRoles()).isEmpty();
        assertThat(saved.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Update user fields")
    void updateUser_ModifiesFields() {
        testUser1.setFirstName("Updated");
        testUser1.setLastName("Name");
        testUser1.setEnabled(false);

        User updated = userRepository.save(testUser1);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        assertThat(updated.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Delete user by ID")
    void deleteUser_RemovesFromDatabase() {
        Long userId = testUser1.getId();

        userRepository.deleteById(userId);
        entityManager.flush();

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Find all users")
    void findAll_ReturnsAllUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("test1@example.com", "test2@example.com");
    }

    @Test
    @DisplayName("User roles are persisted and retrieved")
    void userRoles_ArePersisted() {
        Optional<User> found = userRepository.findById(testUser2.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    @DisplayName("Add role to user")
    void addRoleToUser_Persists() {
        testUser1.getRoles().add("ADMIN");
        userRepository.save(testUser1);
        entityManager.flush();

        Optional<User> found = userRepository.findById(testUser1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    @DisplayName("Remove role from user")
    void removeRoleFromUser_Persists() {
        testUser2.getRoles().remove("ADMIN");
        userRepository.save(testUser2);
        entityManager.flush();

        Optional<User> found = userRepository.findById(testUser2.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).containsExactly("USER");
    }

    @Test
    @DisplayName("Email uniqueness is enforced")
    void saveUser_WithDuplicateEmail_ThrowsException() {
        User duplicateEmailUser = User.builder()
                .email("test1@example.com") // Duplicate email
                .password("password3")
                .roles(new HashSet<>())
                .enabled(true)
                .build();

        try {
            userRepository.save(duplicateEmailUser);
            entityManager.flush();
            assertThat(false).as("Should have thrown exception for duplicate email").isTrue();
        } catch (Exception e) {
            // Expected - constraint violation
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("Username uniqueness is enforced")
    void saveUser_WithDuplicateUsername_ThrowsException() {
        User duplicateUsernameUser = User.builder()
                .email("unique@example.com")
                .password("password3")
                .username("testuser1") // Duplicate username
                .roles(new HashSet<>())
                .enabled(true)
                .build();

        try {
            userRepository.save(duplicateUsernameUser);
            entityManager.flush();
            assertThat(false).as("Should have thrown exception for duplicate username").isTrue();
        } catch (Exception e) {
            // Expected - constraint violation
            assertThat(e).isNotNull();
        }
    }
}
