# API Documentation

## Base URL
The base URL for all API endpoints is:  
`http://localhost:5000`

---

## Authentication Endpoints

### 1. Authenticate User
**POST** `/api/accounts/authenticate`

- **Description**: Authenticate a user using their ATM card number and PIN.
- **Request Body**:
  ```json
  {
    "atmCardNumber": "1234567890123456",
    "pin": "1234"
  }
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "token": "jwt_token_here"
    }
    ```
  - **401 Unauthorized**: Invalid credentials.

---

## Account Management Endpoints

### 2. Create Account
**POST** `/api/accounts`

- **Description**: Create a new account.
- **Request Body**:
  ```json
  {
    "accountHolderName": "John Doe",
    "pin": "1234",
    "balance": 1000
  }
  ```
- **Response**:
  - **201 Created**:
    ```json
    {
      "id": 1,
      "accountHolderName": "John Doe",
      "balance": 1000.0,
      "atmCardNumber": "4467225009911002"
    }
    ```

---

### 3. Get Account Details
**GET** `/api/accounts/details`

- **Description**: Retrieve details of the authenticated account.
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "id": 1,
      "name": "John Doe",
      "balance": 1000.0,
      "atmCardNumber": "4467225009911002"
    }
    ```

---

### 4. Delete Account
**DELETE** `/api/accounts/delete`

- **Description**: Delete the authenticated account.
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "message": "Account deleted successfully"
    }
    ```

---

### 5. Get All Accounts (Admin Only)
**GET** `/api/accounts`

- **Description**: Retrieve a list of all accounts (admin use only).
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    [
      {
        "accountId": 1,
        "name": "John Doe",
        "balance": 1000
      },
      {
        "accountId": 2,
        "name": "Jane Smith",
        "balance": 2000
      }
    ]
    ```

---

## Transaction Endpoints

### 6. Deposit Funds
**PUT** `/api/accounts/deposit`

- **Description**: Deposit funds into the authenticated account.
- **Request Body**:
  ```json
  {
    "amount": 500
  }
  ```
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
  - **Response**:
    - **200 OK**:
      ```json
      {
        "id": 1,
        "accountHolderName": "John Doe",
        "balance": 1500.0,
        "atmCardNumber": "4467225009911002"
      }
      ```

---

### 7. Withdraw Funds
**PUT** `/api/accounts/withdraw`

- **Description**: Withdraw funds from the authenticated account.
- **Request Body**:
  ```json
  {
    "amount": 300
  }
  ```
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "id": 1,
      "accountHolderName": "John Doe",
      "balance": 1000.0,
      "atmCardNumber": "4467225009911002"
    }
    ```
  - **400 Bad Request**: Insufficient funds.

---

### 8. Transfer Funds
**PUT** `/api/accounts/transfer`

- **Description**: Transfer funds to another account.
- **Request Body**:
  ```json
  {
    "toCardNumber": "4467225009911002",
    "amount": 500.0
  }
  ```
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "id": 1,
      "accountHolderName": "John Doe",
      "balance": 500.0,
      "atmCardNumber": "4467225009911002"
    }
    ```

---

## PIN Management Endpoints

### 9. Change PIN
**PUT** `/api/accounts/change-pin`

- **Description**: Change the PIN of the authenticated account.
- **Request Body**:
  ```json
  {
    "oldPin": "1234",
    "newPin": "5678"
  }
  ```
- **Headers**:
  ```http
  Authorization: Bearer jwt_token_here
  ```
- **Response**:
  - **200 OK**:
    ```json
    {
      "message": "PIN changed successfully"
    }
    ```
  - **400 Bad Request**: Old PIN is incorrect.

---

## Error Handling
All endpoints return meaningful error messages with appropriate HTTP status codes:
- **400 Bad Request**: Invalid input or request parameters.
- **401 Unauthorized**: Authentication failure.
- **403 Forbidden**: Access denied.
- **404 Not Found**: Resource not found.
- **500 Internal Server Error**: Unexpected server error.

---
