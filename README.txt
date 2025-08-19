BOOKS MANAGEMENT SYSTEM
========================

A comprehensive Spring Boot application for managing personal book collections with role-based access control and file management capabilities.

PROJECT OVERVIEW
---------------
This is a modular Spring Boot application that provides a complete solution for managing personal book collections. It includes user authentication, role-based authorization, book organization through bookshelves, and file management for various book formats.

TECHNICAL STACK
--------------
- Java 21
- Spring Boot 3.3.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL Database
- Spring Cloud (Kafka integration)
- Swagger/OpenAPI 3.0
- Maven (Multi-module project)
- Lombok
- ModelMapper

PROJECT STRUCTURE
----------------
```
books-management/
├── application/          # Main application module
│   ├── src/main/java/   # Application configuration and startup
│   └── src/main/resources/ # Application properties and SQL scripts
├── domain/              # Core business logic module
│   ├── user/           # User management entities and services
│   ├── book/           # Book and bookshelf entities
│   └── security/       # Security domain entities
├── security/           # Authentication and authorization module
├── database/           # Database configuration and utilities
├── utility/            # Common utilities and helpers
├── search/             # Search functionality module
└── pom.xml            # Parent Maven configuration
```

CORE FEATURES
-------------
1. User Management
   - User registration and authentication
   - Role-based access control
   - Session management
   - Account lockout protection

2. Book Management
   - Upload and store books in various formats (PDF, EPUB, MOBI, etc.)
   - Organize books into bookshelves
   - Book metadata management
   - File download capabilities

3. Security Features
   - JWT-based authentication
   - Fine-grained permission system
   - Role-based authorization
   - Secure file access

4. API Endpoints
   - Public endpoints (/pub/*) - Registration, authentication
   - Member endpoints (/member/*) - Book and bookshelf management
   - Admin endpoints (/admin/*) - User and role management

DATABASE SCHEMA
--------------
The application uses PostgreSQL with the following main tables:
- USERS - User accounts and authentication data
- BOOK - Book metadata and file references
- BOOK_SHELF - Book organization containers
- SECURITY_ROLE - Role definitions
- SECURITY_PERMISSION - Permission definitions
- SECURITY_REST - API endpoint permissions
- SECURITY_REALM - Security realms
- SECURITY_USER_ROLE_REALM - User-role-realm mappings
- USER_SESSION - User session tracking

API DOCUMENTATION
-----------------
The application includes Swagger/OpenAPI documentation available at:
- Swagger UI: http://localhost:8087/api/swagger-ui/
- API Docs: http://localhost:8087/api/v3/api-docs/

MAIN ENDPOINTS
--------------
Public Endpoints:
- POST /api/pub/account/register - User registration
- POST /api/pub/account/login - User authentication
- POST /api/pub/account/refresh - Token refresh

Member Endpoints:
- GET /api/member/book-shelves - List bookshelves
- POST /api/member/book-shelves - Create bookshelf
- PUT /api/member/book-shelves/{id} - Update bookshelf
- DELETE /api/member/book-shelves/{id} - Delete bookshelf
- GET /api/member/books - List books
- POST /api/member/books - Upload book
- GET /api/member/books/{id}/download - Download book file
- PUT /api/member/books/{id}/file - Update book file

Admin Endpoints:
- GET /api/admin/users - List users
- PUT /api/admin/users/{id} - Update user
- PATCH /api/admin/users/{id}/role - Assign roles
- GET /api/admin/roles - List roles
- POST /api/admin/roles - Create role
- GET /api/admin/permissions - List permissions

CONFIGURATION
-------------
The application uses environment-specific configuration:
- Development: application-dev.properties
- Production: application-prod.properties

Key configuration properties:
- Database connection settings
- JWT security keys and expiration times
- File upload paths and size limits
- Kafka configuration (for distributed features)

SECURITY CONFIGURATION
---------------------
- JWT tokens for stateless authentication
- Role-based access control (RBAC)
- Permission-based authorization
- Account lockout after failed attempts
- Secure file access controls

FILE MANAGEMENT
--------------
Supported book formats:
- PDF (.pdf)
- EPUB (.epub)
- MOBI (.mobi)
- AZW3 (.azw3)
- IBA (.iba)
- BOOK (.book)

File storage configuration:
- Base path: /opt/books-management/files/
- Temporary path: /opt/books-management/files/temp/
- Maximum file size: 300MB

SETUP AND INSTALLATION
---------------------
1. Prerequisites:
   - Java 21
   - Maven 3.6+
   - PostgreSQL 12+
   - Kafka (optional, for distributed features)

2. Database Setup:
   - Create PostgreSQL database: books_management
   - Run SQL scripts from application/src/main/resources/database/
   - Initialize with default admin user

3. Application Configuration:
   - Update application-dev.properties with database credentials
   - Configure file storage paths
   - Set JWT security keys

4. Build and Run:
   ```bash
   mvn clean install
   mvn spring-boot:run -pl application
   ```

5. Access the application:
   - Application: http://localhost:8087/api
   - Swagger UI: http://localhost:8087/api/swagger-ui/

DEFAULT ADMIN ACCOUNT
--------------------
- Username: ادمین اصلی
- Phone: +989129999999
- Email: book.local@gmail.com
- Password: (pre-configured hashed password)

DEVELOPMENT GUIDELINES
---------------------
- Follow Spring Boot best practices
- Use proper exception handling
- Implement comprehensive logging
- Write unit tests for business logic
- Follow REST API conventions
- Use proper security annotations

MODULE DEPENDENCIES
------------------
- application depends on: search, domain, security, database, utility
- domain depends on: security
- All modules inherit from parent pom.xml

BUILD AND DEPLOYMENT
-------------------
1. Build the entire project:
   ```bash
   mvn clean install
   ```

2. Run specific module:
   ```bash
   mvn spring-boot:run -pl application
   ```

3. Create executable JAR:
   ```bash
   mvn package -pl application
   ```

MONITORING AND LOGGING
---------------------
- Application logs available in console and configured log files
- Hibernate SQL logging enabled in development
- Kafka integration for distributed logging (optional)
- Health check endpoints available

TROUBLESHOOTING
--------------
Common issues:
1. Database connection: Check PostgreSQL service and credentials
2. File upload: Verify file storage paths and permissions
3. JWT tokens: Check security key configuration
4. CORS issues: Verify CORS configuration for frontend integration

SUPPORT AND CONTRIBUTION
-----------------------
For issues and contributions:
- Check existing documentation
- Review API documentation
- Test with provided endpoints
- Follow coding standards

VERSION INFORMATION
------------------
- Version: 1.0.0
- Spring Boot: 3.3.0
- Java: 21
- Database: PostgreSQL
- Last Updated: [Current Date]

LICENSE
-------
[Add appropriate license information]

CONTACT
-------
[Add contact information for project maintainers] 