# Expense Tracker

A full-stack expense tracking application built using Spring Boot and React. The application allows users to create an account, securely log in, manage income and expenses, organize expenses using categories, and view their financial summary through a dashboard.

## Features

- User registration
- User login and logout
- JWT-based authentication
- BCrypt password encryption
- User-specific data
- Add income
- Add expenses
- Edit income and expenses
- Delete income and expenses
- Expense categories
- Automatic default categories for new users
- Dashboard with total income, expenses, and balance
- Monthly financial summary
- Recent transactions
- Income vs Expense visualization

## Technologies Used

### Backend

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- Lombok
- Bean Validation

### Frontend

- React
- JavaScript
- Vite
- HTML
- CSS

## Project Structure

```text
expense-tracker
│
├── src
│   └── main
│       ├── java
│       │   └── com.prasad.expensetracker
│       │       ├── config
│       │       ├── controller
│       │       ├── dto
│       │       ├── entity
│       │       ├── enums
│       │       ├── exception
│       │       ├── repository
│       │       ├── security
│       │       ├── service
│       │       └── util
│       │
│       └── resources
│
├── expense-tracker-frontend
│   ├── src
│   │   ├── App.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Login.jsx
│   │   ├── App.css
│   │   └── index.css
│   ├── public
│   ├── package.json
│   └── vite.config.js
│
├── pom.xml
└── README.md

Backend Modules
Authentication

The authentication module provides:

User registration
User login
JWT token generation
Password encryption using BCrypt
Protected API endpoints
Income Management

Users can:

Add income
View income
Update income
Delete income
Expense Management

Users can:

Add expenses
View expenses
Update expenses
Delete expenses
Assign categories to expenses
Categories

Each new user automatically receives default expense categories:

Food
Groceries
Transport
Shopping
Bills
Entertainment
Health
Education
Travel
Other

Categories are associated with individual users.

Dashboard

The dashboard provides:

Total income
Total expenses
Current balance
Monthly summary
Recent transactions
Income vs Expense comparison
Authentication

The application uses JWT-based authentication.

After successful login, the backend generates a JWT token. The frontend stores the token and sends it with protected API requests using the Authorization header.

Example:

Authorization: Bearer <JWT_TOKEN>

Passwords are encrypted using BCrypt before being stored in the database.

API Endpoints
Authentication
POST /api/auth/register
POST /api/auth/login
Income
POST   /api/incomes
GET    /api/incomes
GET    /api/incomes/{id}
PUT    /api/incomes/{id}
DELETE /api/incomes/{id}
Expenses
POST   /api/expenses
GET    /api/expenses
GET    /api/expenses/{id}
PUT    /api/expenses/{id}
DELETE /api/expenses/{id}
Categories
POST   /api/categories
GET    /api/categories
GET    /api/categories/{id}
PUT    /api/categories/{id}
DELETE /api/categories/{id}
Dashboard
GET /api/dashboard
GET /api/dashboard/monthly
GET /api/dashboard/categories
GET /api/dashboard/transactions
Running the Backend

Open the project in IntelliJ IDEA.

Make sure PostgreSQL is running and the database configuration is correctly set in:

src/main/resources/application.properties

Run:

ExpenseTrackerApplication.java

The backend runs on:

http://localhost:8080
Running the Frontend

Open a terminal inside:

expense-tracker-frontend

Install dependencies if required:

npm install

Start the development server:

npm run dev

The frontend runs on:

http://localhost:5173
Application Flow
User
 │
 ├── Register
 │
 ├── Login
 │      │
 │      └── JWT Token
 │
 └── Dashboard
        │
        ├── Add Income
        ├── Add Expense
        ├── Edit Transaction
        ├── Delete Transaction
        ├── View Categories
        ├── View Monthly Summary
        └── View Balance
Security

The application uses:

JWT authentication
BCrypt password hashing
Stateless sessions
Protected API endpoints
User-specific data access

Users can only access their own financial records.