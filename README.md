Phenikaa Course Management API (PPEC API)
Ask DeepWiki

This repository contains the backend API for the Phenikaa Course Management system, a comprehensive platform for managing educational content, users, and administrative tasks. It is built with Java and the Spring Boot framework.

Features
This API provides a wide range of features to support a complete educational ecosystem:

User Management & Authentication
JWT-based Security: Secure authentication and authorization using JSON Web Tokens.
Role-Based Access Control: Granular permissions for various user roles (ADMIN, STUDENT, LECTURER, STAFF, CENTER, etc.).
User Lifecycle: Full support for user registration, login, logout, and token refresh.
Secure Password Reset: Functionality for users to reset their passwords via email verification.
Profile Management: Endpoints for users to view and update their personal profiles.
Course & Academic Management
Course Administration: Full CRUD operations for both online and offline courses.
Quizzes & Exams: Create and manage quizzes within lessons, handle submissions, and schedule exams.
Student Enrollment: Manage student registration for both online and offline courses.
Scheduling: Set up schedules for offline course sessions.
Certificates & Skills: Define skills and manage the lifecycle of certificates.
Content Management: Create and manage lectures, lessons, and associated study programs.
Partner & Staff Management
Partner/Center Management: Onboard and manage partner organizations like training centers and schools.
Staff Roles: Manage various staff types, including Lecturers, Teaching Assistants, and Service Staff, associating them with partners.
Asset Tracking: A system for creating, assigning, and tracking educational assets.
Communication & Financials
Real-time Chat: Private messaging between users implemented with Socket.IO.
News & Notifications: A system for posting news (with an approval workflow) and sending role-based notifications.
Payment Integration: Process payments for course enrollments through the PayPal API.
Financial Records: Manage withdrawal and payment transaction records.
Technology Stack
Backend: Java 17+, Spring Boot
Database: MySQL with Spring Data JPA & Hibernate
Security: Spring Security, JSON Web Tokens (JWT)
Build Tool: Gradle
API Documentation: OpenAPI 3 (Swagger UI)
Real-time Communication: Socket.IO
Third-Party Integrations:
Cloudinary: For cloud-based image and file storage.
PayPal: For payment processing.
Spring Mail: For sending emails (e.g., password resets).
Getting Started
To get the application running locally, follow these steps.

Prerequisites
Java (JDK 17 or higher)
Gradle
A running MySQL instance
Configuration & Setup
Clone the repository:

git clone https://github.com/ming3210/ppec_api.git
cd ppec_api
Configure the application:

Open the src/main/resources/application.yml file.
Update the spring.datasource properties to connect to your local or remote MySQL database.
Configure the jwt.secret with a secure, unique key.
Provide your credentials for paypal, cloudinary, and spring.mail to enable those integrations.
Run the application: Use the Gradle wrapper to build and start the Spring Boot application.

./gradlew bootRun
The API server will start on http://localhost:8080. The Socket.IO server will start on the port specified in application.yml (default 8085).

API Documentation
Once the application is running, comprehensive API documentation is available via Swagger UI. You can access it in your browser at:

http://localhost:8080/swagger-ui.html

This interface allows you to explore all available endpoints, view their request/response models, and test them directly.

User Roles
The system is designed with a multi-tiered role hierarchy to ensure proper access control:

ADMIN: Full administrative privileges over the entire system.
STUDENT: Enrolls in courses, takes quizzes, and manages their profile.
LECTURER: Manages course content, lectures, and academic materials.
ASSISTANT: Teaching assistant, supports lecturers.
SERVICE_STAFF: Staff member associated with a partner center.
SCHOOL_ADMIN: Administrator for a specific partner school.
STAFF: A general staff role.
CENTER: Represents a partner center account.
