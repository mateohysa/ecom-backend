# E-commerce Backend API

A robust e-commerce backend system built with Spring Boot, providing secure user authentication, product management, and order processing capabilities.

## Technologies

### Core
- Java 21
- Spring Boot 3.3.5
- Spring Security
- Spring Data JPA
- PostgreSQL

### Security & Authentication
- JWT (JSON Web Tokens)
- BCrypt Password Encryption
- Role-based Access Control

### Email Services
- Spring Mail
- Email Verification System
- Password Reset Functionality

### Validation & Error Handling
- Spring Validation
- Custom Exception Handling
- HTTP Status Code Management

## Features

### User Management
- User Registration with Email Verification
- Secure Authentication using JWT
- Password Reset Functionality
- User Profile Management

### Address Management
- Multiple Addresses per User
- CRUD Operations for Addresses
- Address Validation

### Product Management
- Product Listing
- Inventory Tracking
- Product Categories

### Security Features
- Token-based Authentication
- Password Encryption
- Protected API Endpoints
- CORS Configuration

## API Endpoints

### Registration and Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /auth/verify` - Email verification
- `GET /auth/me` - Get current user
- `POST /auth/forgot` - Initiate password reset
- `POST /auth/reset` - Reset password

### Address Management
- `GET /user/{user_id}/address` - Get user addresses
- `PUT /user/{user_id}/address` - Add new address
- `PATCH /user/{user_id}/address/{addressId}` - Update address


## Setup and Installation

1. Prerequisites:
   - Java 21
   - PostgreSQL
   - Maven

2. Clone the repository:
```bash
git clone [https://github.com/mateohysa/ecom-backend.git]
