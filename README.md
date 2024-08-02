# PUMIS: Personalized University Management Information System

## Overview

PUMIS is a Java-based Student Management System that manages student records using a MySQL database. It offers functionalities to add, display, search, update, and delete student records.

## Features

- **Add Student**: Add a new student record.
- **Display All Students**: View all student records.
- **Search Student by ID**: Find a student record by its unique ID.
- **Update Student Information**: Modify existing student details.
- **Delete Student by ID**: Remove a student record from the database.

## Database Schema

The system uses a MySQL database with a table named `students`. The schema for the table is as follows:

```sql
CREATE TABLE students (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT,
    gender VARCHAR(255),
    email VARCHAR(255),
    course VARCHAR(255),
    year_of_study INT
);
```
## Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL server
- MySQL JDBC driver

## Setup
1.**Clone the repository**:
 ```sh
git clone https://github.com/yourusername/PUMIS.git
cd PUMIS
 ```

2.**Configure the MySQL database**:

- Create a new MySQL database:
```sh
CREATE DATABASE pranshu;
```
- Update the database credentials in the initializeDatabase method of PUMIS.java:
```sh
String url = "jdbc:mysql://localhost:3306/pranshu";
String user = "root";
String password = "yourpassword";
```
3.**Compile and run the program**:
```sh
javac PUMIS.java
java PUMIS
```

## Usage
Upon running the program, a menu will be displayed with options to manage student records:
```sh
Student Management System Menu:
1. Add Student
2. Display All Students
3. Search Student by ID
4. Update Student Information
5. Delete Student by ID
6. Exit
Enter your choice:
```
Follow the on-screen prompts to perform the desired operations.

## Input Validation
- Name: Must contain only alphabetic characters and spaces.
- Email: Must follow a valid email format.

## Error Handling
The system includes basic error handling for invalid inputs and SQL exceptions. Appropriate messages are displayed to guide the user.

## Closing
When exiting the application, the database connection is safely closed to prevent resource leaks.

## Contributing
Contributions are welcome! Please fork this repository and submit a pull request for any improvements or bug fixes.
