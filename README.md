# Library Management System

## Overview

This is a RESTful API for a simple Library Management System. It provides endpoints to manage authors and books, with full CRUD (Create, Read, Update, Delete) functionality, including search and pagination. The application is built using the Dropwizard framework, with JDBI for database access and MySQL as the database.

## Features

* **Author Management**:
    * Create, retrieve, update, and delete authors.
    * Paginated listing of all authors.
    * Search for authors by name.
* **Book Management**:
    * Create, retrieve, update, and delete books.
    * Paginated listing of all books.
    * Search for books by title.
* **Robustness**:
    * Centralized configuration using YAML.
    * Secure handling of database credentials using environment variables.
    * Unit tests for core service logic using JUnit and Mockito.

## Prerequisites

Before running the application, ensure you have the following software installed:

* **Java Development Kit (JDK)**: Version 17 or higher.
* **Maven**: Version 3.8.1 or higher.
* **MySQL**: Version 8.0 or higher.

## Setup Instructions

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/kanwarmamta/Library-Management-System.git
    cd Library-Management-System
    ```

2.  **Configure the Database**:
    * Create a MySQL database named `lms`.
    * Use the following SQL to create the `authors` and `books` tables, and to add a foreign key constraint and an index for efficient querying:
        ```sql
        CREATE TABLE `authors` (
          `id` INT NOT NULL AUTO_INCREMENT,
          `authorName` VARCHAR(255) NOT NULL,
          `birthDate` DATE DEFAULT NULL,
          `nationality` VARCHAR(255) DEFAULT NULL,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

        CREATE INDEX idx_authorName ON authors(authorName);

        CREATE TABLE `books` (
          `id` INT NOT NULL AUTO_INCREMENT,
          `title` VARCHAR(255) NOT NULL,
          `authorId` INT NOT NULL,
          `publishedDate` DATE DEFAULT NULL,
          `isbn` VARCHAR(255) DEFAULT NULL,
          PRIMARY KEY (`id`),
          CONSTRAINT `books_ibfk_1` FOREIGN KEY (`authorId`) REFERENCES `authors` (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

        CREATE INDEX idx_title ON books(title);
        ```

3.  **Set Environment Variables**:
    Set your database credentials and URL as environment variables in your terminal.
    
    * **Mac/Linux**:
    ```bash
    export DB_USER="root"
    export DB_PASSWORD="your_password"
    export DB_URL="jdbc:mysql://localhost:3306/lms?useSSL=false&serverTimezone=UTC"
    ```

4.  **Build the Project**:
    ```bash
    mvn clean install
    ```

5.  **Run the Application**:
    ```bash
    java -jar target/lms-1.0-SNAPSHOT.jar server src/main/resources/config.yml
    ```
    The application will start on `http://localhost:8080`.

## API Documentation

The API follows a RESTful design. All endpoints accept and return JSON.

### Authors

* **`POST /authors`**
    * **Purpose**: Create a new author.
    * **Body**: `{"authorName": "...", "birthDate": "YYYY-MM-DD", "nationality": "..."}`
    * **Response**: `201 Created`
* **`GET /authors`**
    * **Purpose**: Get a paginated list of all authors.
    * **Query Params**: `limit` (default: 10), `offset` (default: 0)
    * **Response**: `200 OK`
* **`GET /authors/{id}`**
    * **Purpose**: Get a single author by ID.
    * **Response**: `200 OK` or `404 Not Found`
* **`GET /authors/search?authorName=...`**
    * **Purpose**: Search for authors by name.
    * **Response**: `200 OK`
* **`PUT /authors/{id}`**
    * **Purpose**: Update an existing author.
    * **Body**: `{"authorName": "...", "birthDate": "YYYY-MM-DD", "nationality": "..."}`
    * **Response**: `200 OK` or `404 Not Found`
* **`DELETE /authors/{id}`**
    * **Purpose**: Delete an author. Note: This will fail if the author has associated books (due to a foreign key constraint).
    * **Response**: `204 No Content` or `404 Not Found`

### Books

* **`POST /books`**
    * **Purpose**: Create a new book.
    * **Body**: `{"title": "...", "authorId": ..., "publishedDate": "YYYY-MM-DD", "isbn": "..."}`
    * **Response**: `201 Created`
* **`GET /books`**
    * **Purpose**: Get a paginated list of all books.
    * **Query Params**: `limit` (default: 10), `offset` (default: 0)
    * **Response**: `200 OK`
* **`GET /books/{id}`**
    * **Purpose**: Get a single book by ID.
    * **Response**: `200 OK` or `404 Not Found`
* **`GET /books/search?title=...`**
    * **Purpose**: Search for books by title.
    * **Response**: `200 OK`
* **`PUT /books/{id}`**
    * **Purpose**: Update an existing book.
    * **Body**: `{"title": "...", "authorId": ..., "publishedDate": "YYYY-MM-DD", "isbn": "..."}`
    * **Response**: `200 OK` or `404 Not Found`
* **`DELETE /books/{id}`**
    * **Purpose**: Delete a book.
    * **Response**: `204 No Content` or `404 Not Found`

## Assumptions Made

* The MySQL database is running on `localhost:3306`.
* The `lms` database and the `authors` and `books` tables exist with the specified schemas.
* An existing author ID is required to create a new book.
* The `Book` and `Author` beans use standard getters and setters that align with the JSON property names.
