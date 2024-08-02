import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Student Management System (PUMIS: Personalized University Management
 * Information System)
 * This program manages student records using a MySQL database.
 */
public class PUMIS {
    private static int idcounter; // Counter to generate unique student IDs
    private static Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Initialize MySQL connection
            initializeDatabase();

            // Initialize idcounter from database or start from 1001 if table is empty
            initializeIDCounter();

            boolean exit = false;

            while (!exit) {
                // Display menu options
                System.out.println("\nStudent Management System Menu:");
                System.out.println("1. Add Student");
                System.out.println("2. Display All Students");
                System.out.println("3. Search Student by ID");
                System.out.println("4. Update Student Information");
                System.out.println("5. Delete Student by ID");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = getIntInput(); // Read user choice

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        displayAllStudents();
                        break;
                    case 3:
                        searchStudentByID();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deleteStudentByID();
                        break;
                    case 6:
                        exit = true;
                        System.out.println("Exiting... Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner object
            closeDatabaseConnection(); // Close the database connection
        }
    }

    // Utility method to get integer input from user
    private static int getIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume invalid input
            } finally {
                scanner.nextLine(); // Consume newline character
            }
        }
    }

    // Initialize MySQL database connection
    private static void initializeDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/[Database_name]";
        String user = "root";
        String password = "[Password]";
        connection = DriverManager.getConnection(url, user, password);

        try (Statement stmt = connection.createStatement()) {
            // Create students table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                    + "id INT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "age INT, "
                    + "Gender VARCHAR(255),"
                    + "email VARCHAR(255), "
                    + "course VARCHAR(255), "
                    + "year_of_study INT)";
            stmt.execute(createTableSQL);
        }
    }

    // Initialize idcounter from database or start from 1001 if table is empty
    private static void initializeIDCounter() throws SQLException {
        String query = "SELECT MAX(id) FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next() && rs.getInt(1) > 0) {
                idcounter = rs.getInt(1) + 1;
            } else {
                idcounter = 1001; // Default starting ID
            }
        }
    }

    // Method to add a new student
    private static void addStudent() throws SQLException {
        int id = idcounter++;

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim(); // Read and trim input

        System.out.print("Enter student age: ");
        int age = getIntInput(); // Get student age from user input

        System.out.print("Enter student gender: ");
        String gender = scanner.nextLine().trim(); // Get student gender from user input

        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim(); // Get student email from user input

        System.out.print("Enter student course: ");
        String course = scanner.nextLine().trim(); // Get student course from user input

        System.out.print("Enter year of study: ");
        int yearOfStudy = getIntInput(); // Get year of study from user input

        // Validate the input
        if (isValidName(name) && isValidEmail(email)) {
            String insertSQL = "INSERT INTO students (id, name, age,gender, email, course, year_of_study) VALUES (?, ?, ?, ?, ?, ?,?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setInt(3, age);
                pstmt.setString(4,gender);
                pstmt.setString(5, email);
                pstmt.setString(6, course);
                pstmt.setInt(7, yearOfStudy);
                pstmt.executeUpdate();
                System.out.println("Student added successfully.");
            } catch (SQLException e) {
                System.err.println("Error inserting into database: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid input. Please enter valid information.");
        }
    }

    // Method to validate if a name contains only alphabetic characters
    private static boolean isValidName(String name) {
        // Allow alphabetic characters and spaces
        return name.matches("[a-zA-Z ]+");
    }

    // Method to validate email
    private static boolean isValidEmail(String email) {
        // Simple regex for email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Method to display all students
    private static void displayAllStudents() throws SQLException {
        String query = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String course = rs.getString("course");
                int yearOfStudy = rs.getInt("year_of_study");
                System.out.printf("ID: %-5d | Name: %-10s | Age: %-3d | Gender: %-6s | Email: %-25s | Course: %-100s | Year of Study: %-2d%n", 
    id, name, age, gender, email, course, yearOfStudy);
            }
        }
    }

    // Method to search for a student by ID
    private static void searchStudentByID() throws SQLException {
        System.out.print("Enter student ID to search: ");
        int id = getIntInput(); // Get student ID from user input

        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String course = rs.getString("course");
                int yearOfStudy = rs.getInt("year_of_study");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ",Gender:"+ gender+", Email: " + email + ", Course: " + course + ", Year of Study: " + yearOfStudy);

                } else {
                    System.out.println("Student not found with ID: " + id);
                }
            }
        }
    }

    // Method to update student information
    private static void updateStudent() throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = getIntInput(); // Get student ID from user input

        System.out.print("Enter new name for student: ");
        String newName = scanner.nextLine().trim(); // Get new name from user input

        System.out.print("Enter new age for student: ");
        int newAge = getIntInput(); // Get new age from user input

        System.out.print("Enter new gender for student: ");
        String newGender = scanner.nextLine().trim(); // Get new gender from user input

        System.out.print("Enter new email for student: ");
        String newEmail = scanner.nextLine().trim(); // Get new email from user input

        System.out.print("Enter new course for student: ");
        String newCourse = scanner.nextLine().trim(); // Get new course from user input

        System.out.print("Enter new year of study for student: ");
        int newYearOfStudy = getIntInput(); // Get new year of study from user input

        // Validate the input
        if (isValidName(newName) && isValidEmail(newEmail)) {
            String updateSQL = "UPDATE students SET name = ?, age = ?, gender = ?, email = ?, course = ?, year_of_study = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setString(1, newName);
                pstmt.setInt(2, newAge);
                pstmt.setString(3, newGender);
                pstmt.setString(4, newEmail);
                pstmt.setString(5, newCourse);
                pstmt.setInt(6, newYearOfStudy);
                pstmt.setInt(7, id);

                // Update student information
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student information updated successfully.");
                } else {
                    System.out.println("Student not found with ID: " + id);
                }
            } catch (SQLException e) {
                System.err.println("Error updating student: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid input. Please enter valid information.");
        }
    }

    // Method to delete a student by ID
    private static void deleteStudentByID() throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = getIntInput(); // Get student ID from user input

        String deleteSQL = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    // Close the database connection
    private static void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}

