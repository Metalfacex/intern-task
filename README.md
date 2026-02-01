# Ticket Booking System - Debug Challenge

## Background

This is a ticket booking service for events. Users can book tickets, and the system tracks availability.

## Problem

QA has reported intermittent issues under load:
- Some customers received booking confirmations when tickets should have been sold out
- Database shows inconsistent ticket counts
- The bug is not reproducible with manual testing

**Your task:** Find the bug in `BookingService.java`, explain the root cause, and fix it.


## Project Structure

```
src/main/java/task/example/demo/
├── entity/           # JPA entities
├── repository/       # Data access
├── service/          # Business logic (bug is here)
├── controller/       # REST API
└── config/           # Security config
```

## API

- `POST /api/events` - Create event
- `GET /api/events` - List events
- `POST /api/book` - Book tickets `{eventId, username, quantity}`
- `GET /api/events/{id}/bookings` - List bookings

## Hints

1. Single requests work fine
2. Look at what happens between checking and updating

## Deliverables

1. Identify the bug location and type
2. Explain why it occurs
3. Implement a fix
4. (Bonus) What other solutions exist?

---

## Task 2: Authentication

The API is currently open to everyone. Implement proper authentication:

1. Create user registration endpoint (`POST /api/auth/register`)
2. Create login endpoint (`POST /api/auth/login`) that returns a JWT token
3. Protect all `/api/*` endpoints - require valid token in `Authorization: Bearer <token>` header
4. Token should contain user info and expire after reasonable time

**Requirements:**
- Use JWT (JSON Web Token)
- Store users in database with hashed passwords
- Return proper error responses for invalid/expired tokens

---

## Task 3: Swagger Documentation

Add API documentation using Swagger/OpenAPI:

1. Integrate Springdoc OpenAPI into the project
2. Document all endpoints with descriptions
3. Include request/response examples
4. Swagger UI should be accessible at `/swagger-ui.html`

**Requirements:**
- All endpoints must be documented
- Group endpoints by controller
- Include authentication info in docs (how to use Bearer token)
