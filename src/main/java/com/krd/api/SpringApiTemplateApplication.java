// TODO: Rename this file and class to match your project name (e.g., YourProjectNameApplication.java)
package com.krd.api; // TODO: Replace 'com.krd.api' with your company/organization package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 */
@SpringBootApplication
public class SpringApiTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringApiTemplateApplication.class, args);
	}

}
