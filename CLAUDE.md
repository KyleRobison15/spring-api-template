# Using This Template with Claude Code

This guide shows you how to effectively use Claude Code to customize and extend this Spring API Template.

## 🚀 Quick Start

### Initial Setup with Claude

Once you've cloned/created your project from this template:

```
Hey Claude! I just cloned the Spring API Template. Can you help me customize it for my project?

My project details:
- Name: TaskFlow API
- Organization: com.taskflow
- Database: taskflow_db
- Purpose: Task management application with teams and projects
```

Claude will help you:
1. Rename the project and packages
2. Update all configuration files
3. Set up your database
4. Customize security settings

---

## 📋 Following the Customization Checklist

The README.md contains a comprehensive checklist of all TODOs. Ask Claude to help you work through it:

```
Claude, can you help me work through the customization checklist in the README?
Let's start with the project identity section.
```

Claude can:
- Search for all TODO comments across the codebase
- Update files systematically
- Explain why each change is needed
- Validate your configuration

---

## 🎯 Common Customization Tasks

### Renaming the Project

```
Claude, please rename this project from "spring-api-template" to "taskflow-api" and update the package from "com.krd.api" to "com.taskflow.api". Make sure to update all references including settings.gradle, build.gradle, application.yaml, and all Java files.
```

### Adding Custom User Fields

```
Claude, I need to add the following fields to the User entity:
- phoneNumber (String, max 20 characters)
- department (String)
- hireDate (LocalDate)

Please:
1. Update the User entity
2. Update the UserDto
3. Create a Flyway migration
4. Update the STARTER_REFERENCE.md if needed
```

### Creating New Endpoints

```
Claude, I need to create a Task management feature with:
- Task entity (id, title, description, status, dueDate, assignedTo)
- CRUD endpoints
- Only authenticated users can create tasks
- Only admins can delete tasks
- Anyone can view their own tasks

Please create the entity, repository, service, controller, and Flyway migration.
```

### Configuring CORS

```
Claude, please update the CORS configuration to allow:
- http://localhost:3000 (dev)
- https://app.taskflow.com (prod)
- https://staging.taskflow.com (staging)

Update both application.yaml and application-prod.yaml appropriately.
```

---

## 🔍 Understanding the Starter

The template uses the KRD Spring API Starter. Ask Claude about it:

```
Claude, can you explain what the BaseUserService provides and show me how to override template methods for custom behavior?
```

```
Claude, I see BaseUserRepository has soft-delete support. Can you explain how that works and show me an example query?
```

```
Claude, what JWT configuration options are available from the starter? Show me how to adjust token expiration times.
```

Claude can reference the [Spring API Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter) for detailed information about what the starter provides.

---

## 🛠️ Advanced Customization

### Adding a New Starter Dependency

```
Claude, I want to add the payment-gateway-starter to this project. Can you:
1. Add the dependency to build.gradle
2. Show me how to create an Order entity that implements OrderInfo
3. Create a checkout endpoint
4. Configure Stripe settings
```

### Implementing Custom Security Rules

```
Claude, I need to create a webhook endpoint at /webhooks/github that should be publicly accessible. Please update the SecurityConfig to allow this, and explain how the SecurityRules pattern works.
```

### Customizing Password Validation

```
Claude, my organization requires:
- Minimum 12 character passwords
- No special character requirement
- Must not contain user's email or name

Please update the password policy configuration and explain how the validation works.
```

### Working with Exception Handling

This template uses a **two-tier exception handling architecture**:
1. **Automatic** - Common exceptions handled by exception-handling-starter (validation, auth, etc.)
2. **Domain-specific** - Your custom exceptions in `ApiExceptionHandler.java`

```
Claude, I need to add error handling for my new ProductNotFoundException. Please:
1. Show me the current ApiExceptionHandler structure
2. Add a handler for ProductNotFoundException that returns 404
3. Explain the two-tier exception handling architecture
4. Show an example error response
```

```
Claude, can you explain the exception handling architecture in this template? Specifically:
- What's the difference between automatic and domain-specific exception handling?
- Which exceptions are handled automatically by the exception-handling-starter?
- Why do we use @Order(HIGHEST_PRECEDENCE) in our ApiExceptionHandler?
- When should I add a new exception handler vs. rely on the automatic ones?
```

```
Claude, I'm getting validation errors but they're not showing the field-level details. Can you:
1. Explain how validation errors are handled automatically
2. Show me what a validation error response looks like
3. Help me understand which exceptions I need to handle vs. which are automatic
```

---

## 🧪 Testing with Claude

### Writing Tests

```
Claude, please write integration tests for the User registration flow including:
- Successful registration
- Duplicate email validation
- Password policy validation
- Auto-reactivation of soft-deleted account
```

### Testing the API

```
Claude, please help me test the authentication flow:
1. Register a new user
2. Login and get tokens
3. Use the access token to get current user
4. Test token refresh

Show me the curl commands or how to use Swagger UI.
```

---

## 📝 Documentation Tasks

### Updating Documentation

```
Claude, I added a Projects feature. Please update the README.md to mention it in the "What's Included" section, and create a section in GETTING_STARTED.md showing how to work with projects.
```

### Adding API Documentation

```
Claude, please add Swagger annotations to my TaskController to document:
- Endpoint descriptions
- Request/response examples
- Security requirements
- Possible error responses
```

---

## 🔄 Refactoring with Claude

### Extracting Common Logic

```
Claude, I notice I'm repeating authorization logic in multiple services. Can you help me extract this into a reusable component?
```

### Improving Code Organization

```
Claude, my controllers are getting large. Can you suggest a better organization pattern and help me refactor the UserController?
```

---

## 🐛 Debugging with Claude

### Understanding Errors

```
Claude, I'm getting this error when starting the application:
[paste error stack trace]

Can you explain what's wrong and how to fix it?
```

### Database Issues

```
Claude, Flyway is failing with "Table 'users' already exists". How do I resolve this? Should I drop the table or skip the migration?
```

### Security Issues

```
Claude, I'm getting 401 Unauthorized even though I'm sending the JWT token. Can you help me debug this?
```

---

## 💡 Best Practices

### Ask Claude to Review

```
Claude, please review my SecurityConfig and let me know if there are any security issues or best practices I should follow.
```

### Performance Optimization

```
Claude, I have an N+1 query problem with Tasks and their assigned Users. Can you help me optimize this?
```

### Configuration Review

```
Claude, please review all my TODO comments and let me know what still needs to be configured before deploying to production.
```

---

## 📦 Deployment Help

### Environment Setup

```
Claude, I'm deploying to Railway. Can you help me:
1. Set up the environment variables
2. Configure the production database
3. Update CORS for my frontend domain
4. Verify the production configuration is secure
```

### Docker Configuration

```
Claude, please create a Dockerfile for this application with:
- Multi-stage build
- Java 21 runtime
- Proper security settings
- Health check endpoint
```

---

## 🎓 Learning with Claude

### Understanding Patterns

```
Claude, can you explain the MapStruct mapper pattern used in this template? Show me examples of custom mappings I might need.
```

```
Claude, explain how the soft-delete pattern works with auto-reactivation. Show me the repository queries and service logic involved.
```

### Spring Security Deep Dive

```
Claude, walk me through the authentication flow step by step:
1. User sends credentials to /auth/login
2. [... fill in the rest]

Reference the actual code in this template.
```

---

## 🔗 Useful Claude Commands

### Finding Code

```
Claude, where is the JWT token generation logic?
```

```
Claude, show me all the places where user roles are checked.
```

### Code Search

```
Claude, find all usages of BaseUserService in the codebase.
```

```
Claude, are there any TODO comments I haven't addressed yet?
```

---

## 💬 Tips for Working with Claude

1. **Be Specific**: Instead of "update the user", say "add a phoneNumber field to the User entity"

2. **Provide Context**: Mention your project name, domain, and requirements upfront

3. **Reference Documentation**: Ask Claude to check the [Spring API Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter) for starter details

4. **Review Changes**: Always review what Claude suggests before applying

5. **Iterative Approach**: Break large tasks into smaller steps

6. **Ask for Explanations**: If you don't understand something, ask Claude to explain

7. **Use Examples**: Ask Claude to show examples from the existing codebase

---

## 🎯 Example Workflow

Here's a complete workflow for adding a new feature:

```
1. Me: "Claude, I need to add a Projects feature where users can create projects and invite team members."

2. Claude: [Explains the approach, suggests entity structure]

3. Me: "That sounds good. Let's start with the Project entity. Include: id, name, description, owner (User), members (List<User>), createdAt."

4. Claude: [Creates Project entity]

5. Me: "Now create the repository, service, and controller. Only project owners should be able to update or delete projects."

6. Claude: [Creates components with authorization]

7. Me: "Create the Flyway migration for the projects table and project_members join table."

8. Claude: [Creates migration]

9. Me: "Add Swagger documentation to the controller."

10. Claude: [Adds OpenAPI annotations]

11. Me: "Write integration tests for the project creation flow."

12. Claude: [Creates tests]

13. Me: "Update the README to mention the Projects feature."

14. Claude: [Updates documentation]
```

---

**Happy coding with Claude!** 🎉

For more information:
- [README.md](README.md) - Complete setup, customization, and feature guide
- [Spring API Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter) - Starter feature documentation
