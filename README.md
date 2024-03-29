# Bank-API

## Overview

The Bank API is a Spring Boot application that provides RESTful services for handling bank operations. It leverages Spring Data MongoDB for data persistence, Lombok for simplifying code, and Hazelcast for caching to enhance performance.

## Features

- **Account Management:**
  - CRUD operations for managing bank accounts.
  - Account details include owner information, balance, and other relevant data.

- **Transaction Operations:**
  - Deposit: Add funds to an account.
  - Withdrawal: Deduct funds from an account.
  - Transfer: Transfer funds from one account to another.

## Technologies Used

- **Spring Boot:** Provides a robust and flexible framework for building Java-based applications.
- **Spring Data MongoDB:** Simplifies data access and manipulation for MongoDB.
- **Lombok:** Reduces boilerplate code by providing annotations for common tasks.
- **Hazelcast:** Enables caching to improve application performance through distributed in-memory storage.


## Getting Started

### Prerequisites

Before running the project, make sure you have the following installed:

- Java JDK (11 or higher)
- MongoDB

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/bank-api.git
    cd bank-api
    ```

2. **Build and run the project:**

    ```bash
    ./mvnw spring-boot:run
    ```

The application should be accessible at `http://localhost:8080`.

## Usage

To use the API, refer to the [Endpoints](#endpoints) section below.

## Endpoints

1. **CRUD Operations for Accounts:**

   - **GET /V1/accounts:** Get all accounts
   - **GET /V1/accounts/{id}:** Get account by ID
   - **GET /V1/accounts?accountNumber={accountNumber}:** Get account by accountNumber
   - **POST /V1/accounts:** Create a new account
   - **PUT /V1/accounts/{id}:** Update an existing account
   - **DELETE /V1/accounts/{id}:** Delete an account

2. **Bank Operations:**

   - **GET /V1/accounts/withdraw?amount={amount}&accountNumber={accountNumber}:** Withdraw funds from an account.
   - **GET /V1/accounts/deposit?amount={amount}&accountNumber={accountNumber}:** Deposit funds into an account.
   - **GET /V1/accounts/transfer?amount={amount}&from={accountNumber}&to={accountNumber}** Transfer funds from one account to another.

## Database

This project uses MongoDB as the database. Make sure MongoDB is running locally, or update the database configuration in `application.properties` accordingly.

#### Feel free to further customize it based on your project's specific details and requirements.

# 🐱‍🏍Happy coding
