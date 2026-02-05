package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import database.DBLogin;
import database.DBQuizAttempt;
import models.QuizAttempt;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * ViewScores - Shows the current user's quiz attempt history
 */
public class ViewScores extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable scoresTable;
    private DefaultTableModel tableModel;
    private JLabel lblStats;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewScores frame = new ViewScores();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ViewScores() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 550);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        panel.setBounds(30, 32, 630, 460);
        contentPane.add(panel);
        
        JLabel lblYourScores = new JLabel("Your Quiz History");
        lblYourScores.setHorizontalAlignment(SwingConstants.CENTER);
        lblYourScores.setFont(new Font("Courier New", Font.BOLD, 20));
        lblYourScores.setBounds(200, 11, 250, 32);
        panel.add(lblYourScores);
        
        // User info label
        String username = DBLogin.currentUser != null ? DBLogin.currentUser.getUsername() : "User";
        JLabel lblUser = new JLabel("Player: " + username);
        lblUser.setFont(new Font("Courier New", Font.BOLD, 14));
        lblUser.setBounds(20, 54, 300, 20);
        panel.add(lblUser);
        
        // Create scores table
        String[] columnNames = {"#", "Difficulty", "Score", "Total", "Percentage", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        scoresTable = new JTable(tableModel);
        scoresTable.setFont(new Font("Courier New", Font.PLAIN, 12));
        scoresTable.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));
        scoresTable.setRowHeight(25);
        
        // Set column widths
        scoresTable.getColumnModel().getColumn(0).setPreferredWidth(40);   // #
        scoresTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Difficulty
        scoresTable.getColumnModel().getColumn(2).setPreferredWidth(60);   // Score
        scoresTable.getColumnModel().getColumn(3).setPreferredWidth(60);   // Total
        scoresTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Percentage
        scoresTable.getColumnModel().getColumn(5).setPreferredWidth(150);  // Date
        
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.setBounds(20, 80, 590, 280);
        panel.add(scrollPane);
        
        // Statistics label
        lblStats = new JLabel("Total Attempts: 0 | Best Score: 0 | Average: 0.0");
        lblStats.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblStats.setBounds(20, 370, 590, 25);
        panel.add(lblStats);
        
        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadScores();
            }
        });
        btnRefresh.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnRefresh.setBackground(Color.LIGHT_GRAY);
        btnRefresh.setBounds(430, 405, 120, 33);
        panel.add(btnRefresh);
        
        // Back button
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDashboard ud = new UserDashboard();
                ud.setVisible(true);
                dispose();
            }
        });
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnBack.setBackground(new Color(169, 169, 169));
        btnBack.setBounds(32, 405, 178, 33);
        panel.add(btnBack);
        
        // Load scores on startup
        loadScores();
    }
    
    /**
     * Load user's scores from database
     */
    private void loadScores() {
        if (DBLogin.currentUser == null) {
            JOptionPane.showMessageDialog(this, "No user logged in!");
            return;
        }
        
        // Clear table
        tableModel.setRowCount(0);
        
        int userId = DBLogin.currentUser.getUserId();
        
        // Get all attempts
        List<QuizAttempt> attempts = DBQuizAttempt.getAttemptsByUser(userId);
        
        if (attempts.isEmpty()) {
            Object[] emptyRow = {"--", "No quiz attempts yet", "--", "--", "--", "--"};
            tableModel.addRow(emptyRow);
            lblStats.setText("Total Attempts: 0 | Best Score: 0 | Average: 0.0");
        } else {
            // Add each attempt to table
            for (int i = 0; i < attempts.size(); i++) {
                QuizAttempt attempt = attempts.get(i);
                
                Object[] rowData = {
                    i + 1,
                    attempt.getDifficultyLevel(),
                    attempt.getScore(),
                    attempt.getTotalQuestions(),
                    String.format("%.1f%%", attempt.getPercentage()),
                    attempt.getAttemptedAt().toString()
                };
                
                tableModel.addRow(rowData);
            }
            
            // Calculate and display statistics
            int totalAttempts = attempts.size();
            double avgScore = DBQuizAttempt.getUserAverageScore(userId);
            
            // Find best score across all difficulties
            int bestScore = 0;
            for (QuizAttempt attempt : attempts) {
                if (attempt.getScore() > bestScore) {
                    bestScore = attempt.getScore();
                }
            }
            
            lblStats.setText(String.format(
                "Total Attempts: %d | Best Score: %d | Average Score: %.1f",
                totalAttempts, bestScore, avgScore
            ));
        }
    }
}