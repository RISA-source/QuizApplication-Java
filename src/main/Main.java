package main;

import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;

import database.*;
import models.*;
import ui.LoginFrame;

/**
 * Main entry point for the Quiz Application
 * Provides options to run in CLI mode (for reports) or GUI mode (full application)
 * 
 * @author Quiz Application Team
 * @version 1.0
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main method - Application entry point
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    QUIZ APPLICATION - MAIN MENU");
        System.out.println("========================================\n");
        
        while (true) {
            System.out.println("Please select a mode:");
            System.out.println("1. GUI Mode (Graphical User Interface)");
            System.out.println("2. CLI Mode (Command Line Reports)");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice (1-3): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    launchGUI();
                    return; // Exit after launching GUI
                    
                case "2":
                    runCLI();
                    break;
                    
                case "3":
                    System.out.println("\nThank you for using Quiz Application!");
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("\nInvalid choice! Please enter 1, 2, or 3.\n");
            }
        }
    }
    
    /**
     * Launch GUI mode - Opens the login window
     */
    private static void launchGUI() {
        System.out.println("\nLaunching GUI mode...");
        System.out.println("Opening Login window...\n");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
    
    /**
     * Run CLI mode - Display reports and statistics
     */
    private static void runCLI() {
        boolean cliRunning = true;
        
        while (cliRunning) {
            System.out.println("\n========================================");
            System.out.println("       CLI MODE - REPORTS MENU");
            System.out.println("========================================");
            System.out.println("1. Show All Users Report");
            System.out.println("2. Show All Questions Report");
            System.out.println("3. Show All Quiz Attempts Report");
            System.out.println("4. Show Leaderboard Report");
            System.out.println("5. Show Summary Statistics");
            System.out.println("6. Show Comprehensive Report (All Data)");
            System.out.println("7. Back to Main Menu");
            System.out.print("\nEnter your choice (1-7): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    showAllUsersReport();
                    break;
                case "2":
                    showAllQuestionsReport();
                    break;
                case "3":
                    showAllQuizAttemptsReport();
                    break;
                case "4":
                    showLeaderboardReport();
                    break;
                case "5":
                    showSummaryStatistics();
                    break;
                case "6":
                    showComprehensiveReport();
                    break;
                case "7":
                    cliRunning = false;
                    System.out.println("\nReturning to main menu...\n");
                    break;
                default:
                    System.out.println("\n❌ Invalid choice! Please enter 1-7.\n");
            }
            
            if (cliRunning && !choice.equals("7")) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Display all registered users
     */
    private static void showAllUsersReport() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           ALL USERS REPORT");
        System.out.println("=".repeat(80));
        
        List<User> users = DBUser.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found in the database.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-30s %-20s%n", 
            "User ID", "Username", "Email", "Registered");
        System.out.println("-".repeat(80));
        
        for (User user : users) {
            System.out.printf("%-10d %-20s %-30s %-20s%n",
                user.getUserId(),
                truncate(user.getUsername(), 20),
                truncate(user.getEmail(), 30),
                user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 19) : "N/A"
            );
        }
        
        System.out.println("-".repeat(80));
        System.out.println("Total Users: " + users.size());
    }
    
    /**
     * Display all questions in the database
     */
    private static void showAllQuestionsReport() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                              ALL QUESTIONS REPORT");
        System.out.println("=".repeat(100));
        
        List<Question> questions = DBQuestion.getAllQuestions();
        
        if (questions.isEmpty()) {
            System.out.println("No questions found in the database.");
            return;
        }
        
        System.out.printf("%-5s %-50s %-15s %-10s%n", 
            "ID", "Question", "Difficulty", "Correct");
        System.out.println("-".repeat(100));
        
        for (Question q : questions) {
            System.out.printf("%-5d %-50s %-15s %-10c%n",
                q.getQuestionId(),
                truncate(q.getQuestionText(), 50),
                q.getDifficultyLevel(),
                q.getCorrectOption()
            );
        }
        
        System.out.println("-".repeat(100));
        System.out.println("Total Questions: " + questions.size());
        
        // Show breakdown by difficulty
        int beginner = DBQuestion.getQuestionCountByDifficulty("Beginner");
        int intermediate = DBQuestion.getQuestionCountByDifficulty("Intermediate");
        int advanced = DBQuestion.getQuestionCountByDifficulty("Advanced");
        
        System.out.println("\nBreakdown by Difficulty:");
        System.out.println("  Beginner:     " + beginner);
        System.out.println("  Intermediate: " + intermediate);
        System.out.println("  Advanced:     " + advanced);
    }
    
    /**
     * Display all quiz attempts
     */
    private static void showAllQuizAttemptsReport() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                          ALL QUIZ ATTEMPTS REPORT");
        System.out.println("=".repeat(100));
        
        List<QuizAttempt> attempts = DBQuizAttempt.getRecentAttempts(1000); // Get all
        
        if (attempts.isEmpty()) {
            System.out.println("No quiz attempts found in the database.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-10s %-12s %-20s%n", 
            "Attempt ID", "Username", "Difficulty", "Score", "Percentage", "Date");
        System.out.println("-".repeat(100));
        
        for (QuizAttempt attempt : attempts) {
            System.out.printf("%-10d %-20s %-15s %-10s %-12s %-20s%n",
                attempt.getAttemptId(),
                truncate(attempt.getUsername(), 20),
                attempt.getDifficultyLevel(),
                attempt.getScore() + "/" + attempt.getTotalQuestions(),
                String.format("%.1f%%", attempt.getPercentage()),
                attempt.getAttemptedAt() != null ? attempt.getAttemptedAt().toString().substring(0, 19) : "N/A"
            );
        }
        
        System.out.println("-".repeat(100));
        System.out.println("Total Attempts: " + attempts.size());
    }
    
    /**
     * Display leaderboard for all difficulty levels
     */
    private static void showLeaderboardReport() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                         LEADERBOARD REPORT (Top 10)");
        System.out.println("=".repeat(90));
        
        String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
        
        for (String difficulty : difficulties) {
            System.out.println("\n" + difficulty.toUpperCase() + " LEVEL");
            System.out.println("-".repeat(90));
            
            List<QuizAttempt> leaderboard = DBQuizAttempt.getLeaderboard(difficulty, 10);
            
            if (leaderboard.isEmpty()) {
                System.out.println("No attempts for this difficulty level yet.");
                continue;
            }
            
            System.out.printf("%-6s %-20s %-10s %-12s %-20s%n", 
                "Rank", "Username", "Score", "Percentage", "Date");
            System.out.println("-".repeat(90));
            
            int rank = 1;
            for (QuizAttempt attempt : leaderboard) {
                String rankDisplay;
                if (rank == 1) rankDisplay = "1st";
                else if (rank == 2) rankDisplay = "2nd";
                else if (rank == 3) rankDisplay = "3rd";
                else rankDisplay = rank + "th";
                
                System.out.printf("%-6s %-20s %-10s %-12s %-20s%n",
                    rankDisplay,
                    truncate(attempt.getUsername(), 20),
                    attempt.getScore() + "/" + attempt.getTotalQuestions(),
                    String.format("%.1f%%", attempt.getPercentage()),
                    attempt.getAttemptedAt() != null ? attempt.getAttemptedAt().toString().substring(0, 19) : "N/A"
                );
                rank++;
            }
        }
    }
    
    /**
     * Display summary statistics
     */
    private static void showSummaryStatistics() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                        SUMMARY STATISTICS");
        System.out.println("=".repeat(80));
        
        // User Statistics
        int totalUsers = DBUser.getTotalUserCount();
        System.out.println("\nUSER STATISTICS");
        System.out.println("-".repeat(80));
        System.out.println("Total Registered Users: " + totalUsers);
        
        // Question Statistics
        int totalQuestions = DBQuestion.getTotalQuestionCount();
        int beginnerQs = DBQuestion.getQuestionCountByDifficulty("Beginner");
        int intermediateQs = DBQuestion.getQuestionCountByDifficulty("Intermediate");
        int advancedQs = DBQuestion.getQuestionCountByDifficulty("Advanced");
        
        System.out.println("\nQUESTION STATISTICS");
        System.out.println("-".repeat(80));
        System.out.println("Total Questions:       " + totalQuestions);
        System.out.println("  Beginner Level:      " + beginnerQs);
        System.out.println("  Intermediate Level:  " + intermediateQs);
        System.out.println("  Advanced Level:      " + advancedQs);
        
        // Quiz Attempt Statistics
        int totalAttempts = DBQuizAttempt.getTotalAttempts();
        
        System.out.println("\nQUIZ ATTEMPT STATISTICS");
        System.out.println("-".repeat(80));
        System.out.println("Total Quiz Attempts:   " + totalAttempts);
        
        if (totalAttempts > 0) {
            System.out.println("Average Attempts per User: " + 
                String.format("%.2f", (double) totalAttempts / Math.max(totalUsers, 1)));
        }
        
        // High Score Statistics
        int highestBeginner = DBQuizAttempt.getHighestScoreForDifficulty("Beginner");
        int highestIntermediate = DBQuizAttempt.getHighestScoreForDifficulty("Intermediate");
        int highestAdvanced = DBQuizAttempt.getHighestScoreForDifficulty("Advanced");
        
        System.out.println("\nHIGHEST SCORES BY DIFFICULTY");
        System.out.println("-".repeat(80));
        System.out.println("Beginner:      " + highestBeginner + "/25");
        System.out.println("Intermediate:  " + highestIntermediate + "/25");
        System.out.println("Advanced:      " + highestAdvanced + "/25");
    }
    
    /**
     * Display comprehensive report with all data
     */
    private static void showComprehensiveReport() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                          COMPREHENSIVE SYSTEM REPORT");
        System.out.println("=".repeat(100));
        System.out.println("Generated: " + new java.util.Date());
        System.out.println("=".repeat(100));
        
        // Summary Statistics Section
        showSummaryStatistics();
        
        // Top Performers Section
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                             TOP PERFORMERS");
        System.out.println("=".repeat(100));
        
        String[] difficulties = {"Beginner", "Intermediate", "Advanced"};
        
        for (String difficulty : difficulties) {
            List<QuizAttempt> topPerformers = DBQuizAttempt.getLeaderboard(difficulty, 3);
            
            if (!topPerformers.isEmpty()) {
                System.out.println("\n🌟 Top Performer - " + difficulty + " Level:");
                QuizAttempt top = topPerformers.get(0);
                System.out.println("   Player: " + top.getUsername());
                System.out.println("   Score:  " + top.getScore() + "/" + top.getTotalQuestions() + 
                    " (" + String.format("%.1f%%", top.getPercentage()) + ")");
                System.out.println("   Date:   " + (top.getAttemptedAt() != null ? 
                    top.getAttemptedAt().toString().substring(0, 19) : "N/A"));
            }
        }
        
        // Recent Activity Section
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           RECENT ACTIVITY (Last 10)");
        System.out.println("=".repeat(100));
        
        List<QuizAttempt> recentAttempts = DBQuizAttempt.getRecentAttempts(10);
        
        if (!recentAttempts.isEmpty()) {
            System.out.printf("\n%-20s %-15s %-10s %-12s %-20s%n", 
                "Username", "Difficulty", "Score", "Percentage", "Date");
            System.out.println("-".repeat(100));
            
            for (QuizAttempt attempt : recentAttempts) {
                System.out.printf("%-20s %-15s %-10s %-12s %-20s%n",
                    truncate(attempt.getUsername(), 20),
                    attempt.getDifficultyLevel(),
                    attempt.getScore() + "/" + attempt.getTotalQuestions(),
                    String.format("%.1f%%", attempt.getPercentage()),
                    attempt.getAttemptedAt() != null ? attempt.getAttemptedAt().toString().substring(0, 19) : "N/A"
                );
            }
        } else {
            System.out.println("\nNo recent activity.");
        }
        
        // Active Users Section
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           MOST ACTIVE USERS (Top 5)");
        System.out.println("=".repeat(100));
        
        List<User> allUsers = DBUser.getAllUsers();
        
        if (!allUsers.isEmpty()) {
            // Create a list to store users with their attempt counts
            java.util.List<UserActivity> userActivities = new java.util.ArrayList<>();
            
            for (User user : allUsers) {
                int attempts = DBQuizAttempt.getUserTotalAttempts(user.getUserId());
                if (attempts > 0) {
                    double avgScore = DBQuizAttempt.getUserAverageScore(user.getUserId());
                    userActivities.add(new UserActivity(user.getUsername(), attempts, avgScore));
                }
            }
            
            // Sort by attempt count (descending)
            userActivities.sort((a, b) -> Integer.compare(b.attempts, a.attempts));
            
            if (!userActivities.isEmpty()) {
                System.out.printf("\n%-20s %-15s %-15s%n", 
                    "Username", "Total Attempts", "Avg Score");
                System.out.println("-".repeat(100));
                
                int count = 0;
                for (UserActivity ua : userActivities) {
                    if (count >= 5) break;
                    System.out.printf("%-20s %-15d %-15s%n",
                        truncate(ua.username, 20),
                        ua.attempts,
                        String.format("%.2f", ua.avgScore)
                    );
                    count++;
                }
            } else {
                System.out.println("\nNo user activity yet.");
            }
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                          END OF COMPREHENSIVE REPORT");
        System.out.println("=".repeat(100));
    }
    
    /**
     * Helper method to truncate strings for display
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string with "..." if needed
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Helper class to store user activity data
     */
    private static class UserActivity {
        String username;
        int attempts;
        double avgScore;
        
        UserActivity(String username, int attempts, double avgScore) {
            this.username = username;
            this.attempts = attempts;
            this.avgScore = avgScore;
        }
    }
}