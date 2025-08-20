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
- Spring Cloud
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
│       ├── database/    # Database schema and migration scripts
│       └── security/    # Security initialization scripts
├── domain/              # Core business logic module
│   ├── user/           # User management entities and services
│   │   ├── account/    # Account management services
│   │   ├── user/       # User entity and CRUD operations
│   │   ├── session/    # Session management
│   │   └── in/rolerealm/ # User-role-realm relationships
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
   - Role-based access control (Super Admin, Admin, Member)
   - Session management and tracking
   - Account lockout protection after failed attempts
   - Automatic role assignment (MEMBER role for new users)
   - Password change functionality
   - User profile management

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
- POST /api/pub/account/register - User registration (auto-assigns MEMBER role)
- POST /api/pub/account/login - User authentication
- POST /api/pub/account/refresh - Token refresh
- POST /api/pub/account/logout - User logout
- PUT /api/pub/account/password - Change password
- PUT /api/pub/account/profile - Update user profile

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


SECURITY CONFIGURATION
---------------------
- JWT tokens for stateless authentication
- Role-based access control (RBAC) with three-tier system:
  * Super Admin: Full system access
  * Admin: User management capabilities
  * Member: Book management capabilities
- Permission-based authorization with hierarchical structure
- Account lockout after failed attempts (configurable threshold)
- Secure file access controls
- Session tracking with IP, OS, and user agent information

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
- Base path: ./books-management/files/ (relative to application directory)
- Temporary path: ./books-management/files/temp/ (relative to application directory)
- Maximum file size: 300MB
- Cross-platform compatible (Windows, Linux, macOS)

SETUP AND INSTALLATION
---------------------
1. Prerequisites:
   - Java 21
   - Maven 3.6+
   - PostgreSQL 12+
   

2. Database Setup:
   - Create PostgreSQL database: books_management
   - Create schema: book
   - Execute only the changes.sql file from application/src/main/resources/database/ (first time only)
   - The script will create all necessary tables and initial data in the book schema

3. Application Configuration:
   - Update application-dev.properties with database credentials
   - Configure file storage paths (directories will be created automatically on startup)
   - Set JWT security keys

4. Build and Run:
   ```bash
   mvn clean install
   mvn spring-boot:run -pl application
   ```

5. Access the application:
   - Application: http://localhost:8087/api
   - Swagger UI: http://localhost:8087/api/swagger-ui/

DEFAULT ACCOUNTS
----------------
Super Admin Account:
- Username: ادمین اصلی
- Phone: +989129999999
- Email: book.local@gmail.com
- Password: (pre-configured hashed password)
- Role: Super Admin (full system access)

Default Roles:
- ADMIN (-2): Administrative users with user management capabilities
- MEMBER (-3): Regular users with book management capabilities

Note: New users are automatically assigned the MEMBER role upon registration

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