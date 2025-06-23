FriendshipApp is a Spring Boot-based RESTful API designed to facilitate social connections through messaging. It enables users to register, update profiles, and communicate with other users via private messages. The application emphasizes clean architecture, secure user management, and rich querying capabilities, while providing clear API documentation through Swagger UI.

Key Features
- User Management:
Users can register, update their profiles, and retrieve user information. Profile data includes username, email, gender, age, and profile picture. The system supports active/inactive status tracking.

- Messaging System:
Users can send messages to one another, view received and sent messages, mark messages as read/unread, and retrieve message summaries. Messages can be queried by sender, receiver, date ranges, and read status.

- Advanced Queries:
Extensive search options allow filtering users by username patterns, gender, age ranges, and profile picture availability. Messages can be filtered by various criteria for flexible retrieval.

- Security & Validation:
Input data is validated using Spring Validation. Although JWT authentication is not implemented in the current version, the structure supports extension for secure authentication.

- DTO Mapping with MapStruct:
Data Transfer Objects (DTOs) are mapped from entities via MapStruct, enabling clean separation of API contracts from internal database models.

- API Documentation:
Integrated Swagger UI via Springdoc OpenAPI provides interactive documentation and testing for all endpoints.

Technologies Used
- Java 21
- Spring Boot 3.4.5
- Spring Data JPA with Hibernate
- PostgreSQL as the relational database
- MapStruct for entity-to-DTO mapping
- Spring Validation for request validation
- Springdoc OpenAPI (Swagger UI) for API documentation


