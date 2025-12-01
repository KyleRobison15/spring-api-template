// TODO: Rename this file and class to match your project name (e.g., YourProjectNameApplication.java)
package com.krd.api; // TODO: Replace 'com.krd.api' with your company/organization package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class for the Spring API Template.
 *
 * This template uses the KRD Spring API Starter which provides:
 * - JWT authentication (dual-token system)
 * - User management with soft-delete
 * - Role-based access control
 * - Password validation
 * - Base controllers and services
 *
 * To use this template:
 * 1. Rename the project, package, and this class
 * 2. Extend the User entity with your custom fields
 * 3. Configure application.yaml with your settings
 * 4. Add your business logic and endpoints
 *
 * IMPORTANT: The @EnableJpaRepositories and @EntityScan annotations are required to scan for
 * repositories and entities in both your application package and the starter package.
 * When you rename your package, update "com.krd.api" to match your new package name.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.krd.api", "com.krd.starter.user"}) // TODO: Update "com.krd.api" when you rename your package
@EntityScan(basePackages = {"com.krd.api", "com.krd.starter.user"}) // TODO: Update "com.krd.api" when you rename your package
public class SpringApiTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiTemplateApplication.class, args);
	}

}
