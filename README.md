PostApp is a RESTful API application built with Spring Boot. It provides a secure platform where users can register, activate their accounts via email, authenticate using JWT tokens, create posts, and view posts by themselves or others. The application combines robust user management with role-based access control, token-based authentication, and detailed API documentation through Swagger.

Key Features
- User Registration & Activation:
Users can register with thorough validation. Upon registration, an activation code is sent to their email, which must be used within a limited timeframe to activate the account.

- JWT Authentication:
Implements secure login using JSON Web Tokens (JWT). JWT tokens protect API endpoints and handle session management in a stateless manner, eliminating the need for server-side sessions.

- Role-Based Authorization:
Supports multiple roles such as Admin and User. Access to API endpoints is controlled based on user roles, ensuring secure and appropriate resource access.

- Post Management:
Registered users can create posts and retrieve all posts or filter to see only their own. This allows for personal and public post management.

- Security:
Integrates Spring Security to enforce authentication and authorization. Includes a custom JWT token filter to intercept requests and validate tokens.

- Email Service:
Sends activation codes via email using Spring Mail, ensuring users verify their email addresses during registration.

- API Documentation:
Provides an interactive Swagger UI (via SpringDoc OpenAPI) for easy API exploration, testing, and integration.

Technologies Used
- Java 21
- Spring Boot 3.4.5
- Spring Security (for authentication and authorization)
- JWT (JSON Web Tokens) with Auth0 library (for stateless token-based authentication)
- Spring Data JPA with PostgreSQL (for data persistence)
- MapStruct (for automatic DTO and entity mapping)
- Spring Mail (for sending activation emails)
- Swagger UI (SpringDoc OpenAPI) (for API documentation)
- Lombok (to minimize boilerplate code)

Overview:
The application flow begins with user registration, where the user provides necessary details. After registering, an activation email with a unique code is sent. Once the user activates the account, they can log in to receive a JWT token for subsequent requests. The token grants access to endpoints based on the user's role.
Users can create posts and view posts globally or their own posts. All sensitive endpoints are secured and require valid JWT tokens for access.

