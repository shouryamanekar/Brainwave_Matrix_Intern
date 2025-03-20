
# ATM APP

## Overview
The ATM App is a backend application built using Spring Boot. It provides functionalities for managing bank accounts, including authentication, deposits, withdrawals, fund transfers, and account management. The application uses PostgreSQL as the database and implements JWT-based authentication for secure access.

## Features
- **Account Management**: Create, retrieve, update, and delete accounts.
- **Authentication**: Secure login using ATM card number and PIN with JWT tokens.
- **Transactions**: Deposit, withdraw, and transfer funds between accounts.
- **PIN Management**: Change PIN securely.
- **Global Exception Handling**: Handles errors gracefully with meaningful responses.
- **CORS Configuration**: Supports cross-origin requests for frontend integration.

## Technologies Used
- **Spring Boot**: Backend framework.
- **Spring Data JPA**: Database interaction.
- **Spring Security**: Authentication and authorization.
- **PostgreSQL**: Relational database.
- **JWT**: Token-based authentication.
- **Dotenv**: Environment variable management.

## Prerequisites
- Java 21 or higher
- Maven 3.9.9 or higher
- PostgreSQL database

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/shouryamanekar/Brainwave_Matrix_Intern/Task1
   cd atm-app
   ```

2. Configure the database:
   - Update the `application.properties` file in `src/main/resources` with your PostgreSQL credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/atm_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. Set up environment variables:
   - Create a `.env` file in the root directory with the following content:
     ```env
     JWT_SECRET=your_jwt_secret
     JWT_EXPIRATION=300000
     ALLOWEDORIGINS=http://localhost:3000
     ALLOWEDMETHODS=GET,POST,PUT,DELETE,OPTIONS
     ALLOWEDHEADERS=Authorization,Content-Type
     ALLOWCREDENTIALS=true
     ```

4. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Access the application:
   - The server will run on `http://localhost:5000`.

## API Endpoints
### Authentication
- **POST** `/api/accounts/authenticate`: Authenticate using ATM card number and PIN.

### Account Management
- **POST** `/api/accounts`: Create a new account.
- **GET** `/api/accounts/details`: Get details of the authenticated account.
- **DELETE** `/api/accounts/delete`: Delete the authenticated account.
- **GET** `/api/accounts`: Get a list of all accounts (admin use).

### Transactions
- **PUT** `/api/accounts/deposit`: Deposit funds into the authenticated account.
- **PUT** `/api/accounts/withdraw`: Withdraw funds from the authenticated account.
- **PUT** `/api/accounts/transfer`: Transfer funds to another account.

### PIN Management
- **PUT** `/api/accounts/change-pin`: Change the PIN of the authenticated account.

## Project Structure
- **`config`**: Configuration files (CORS, security, dotenv).
- **`controller`**: REST controllers for handling API requests.
- **`dto`**: Data Transfer Objects for request/response payloads.
- **`entity`**: JPA entities representing database tables.
- **`exception`**: Custom exceptions and global exception handling.
- **`mapper`**: Utility for mapping between entities and DTOs.
- **`repository`**: JPA repositories for database operations.
- **`service`**: Business logic implementation.
- **`util`**: Utility classes (JWT, hashing).

## Contributors
[Shourya Manekar](mailto:shouryaamanekar@gmail.com)

## Acknowledgments
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT Documentation](https://jwt.io/)
