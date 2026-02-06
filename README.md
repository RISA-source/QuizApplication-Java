# Quiz Application

A Java Swing-based quiz application with multi-round gameplay, user authentication, and administrative features.

## Overview

This application allows users to take quizzes across three difficulty levels (Beginner, Intermediate, Advanced) in a 5-round format with 5 questions per round. It includes user registration/login, score tracking, leaderboards, and an admin panel for managing questions and viewing statistics.

## Features

### User Features
- User registration and authentication
- Quiz gameplay with 3 difficulty levels
- 5 rounds per game, 5 questions per round
- Score tracking and history
- Leaderboard for each difficulty level
- Search and view other players' stats

### Admin Features
- Question management (Add, Edit, Delete)
- User data viewing
- System statistics
- Player search

## Technologies Used

- Java SE
- Swing (GUI)
- JDBC (Database connectivity)
- MySQL (Database)
- JUnit (Testing)

## Database Setup

1. Create a MySQL database named `quiz_db`
2. Run the following SQL schema:

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL
);

CREATE TABLE quiz_attempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

3. Update database credentials in `src/database/DBConnection.java`:
```java
String url = "jdbc:mysql://localhost:3306/quiz_db";
String username = "root";
String password = "your_password";
```

## Project Structure

```
src/
├── database/          # Database connection and operations
├── models/            # Data models (User, Question, QuizAttempt)
├── logic/             # Business logic (QuizEngine)
├── ui/                # Swing GUI components
└── tests/             # JUnit test cases
```

## Running the Application

1. Ensure MySQL is running and the database is set up
2. Import the project into your Java IDE (Eclipse recommended)
3. Add MySQL Connector JAR to build path
4. Run `LoginFrame.java` to start the application

### Default Admin Access
- Username: `admin`
- Password: `admin`

## Requirements

- Java 8 or higher
- MySQL 5.7 or higher
- MySQL Connector/J (JDBC driver)
- JUnit 5 (for testing)

## Testing

The project includes JUnit test cases for:
- Database connection
- User authentication
- Question management
- Quiz attempts
- Quiz engine logic

Run tests using your IDE's test runner or build tool.

## Notes

- Passwords are stored in plain text (not recommended for production)
- The quiz requires at least 25 questions per difficulty level for proper gameplay
- Each game session saves one entry to the database with the total score across all rounds

## License

This project is open source and available for educational purposes.
