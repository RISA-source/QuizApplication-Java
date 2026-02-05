package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import database.DBQuestion;
import database.DBQuizAttempt;
import database.DBUser;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewStatistics extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewStatistics frame = new ViewStatistics();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewStatistics() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 550);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(35, 31, 520, 460);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblTitle = new JLabel("System Statistics");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Courier New", Font.BOLD, 20));
        lblTitle.setBounds(120, 11, 280, 32);
        panel.add(lblTitle);
        
        // Get statistics from database
        int totalUsers = DBUser.getTotalUserCount();
        int totalQuestions = DBQuestion.getTotalQuestionCount();
        int totalAttempts = DBQuizAttempt.getTotalAttempts();
        
        int beginnerQs = DBQuestion.getQuestionCountByDifficulty("Beginner");
        int intermediateQs = DBQuestion.getQuestionCountByDifficulty("Intermediate");
        int advancedQs = DBQuestion.getQuestionCountByDifficulty("Advanced");
        
        int highestBeginner = DBQuizAttempt.getHighestScoreForDifficulty("Beginner");
        int highestIntermediate = DBQuizAttempt.getHighestScoreForDifficulty("Intermediate");
        int highestAdvanced = DBQuizAttempt.getHighestScoreForDifficulty("Advanced");
        
        // User Statistics
        JLabel lblUserStats = new JLabel("User Statistics");
        lblUserStats.setFont(new Font("Courier New", Font.BOLD, 16));
        lblUserStats.setBounds(20, 54, 200, 25);
        panel.add(lblUserStats);
        
        JLabel lblTotalUsers = new JLabel("Total Registered Users: " + totalUsers);
        lblTotalUsers.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblTotalUsers.setBounds(30, 85, 400, 20);
        panel.add(lblTotalUsers);
        
        JLabel lblTotalAttempts = new JLabel("Total Quiz Attempts: " + totalAttempts);
        lblTotalAttempts.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblTotalAttempts.setBounds(30, 110, 400, 20);
        panel.add(lblTotalAttempts);
        
        // Question Statistics
        JLabel lblQuestionStats = new JLabel("Question Statistics");
        lblQuestionStats.setFont(new Font("Courier New", Font.BOLD, 16));
        lblQuestionStats.setBounds(20, 150, 250, 25);
        panel.add(lblQuestionStats);
        
        JLabel lblTotalQuestions = new JLabel("Total Questions: " + totalQuestions);
        lblTotalQuestions.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblTotalQuestions.setBounds(30, 181, 400, 20);
        panel.add(lblTotalQuestions);
        
        JLabel lblBeginner = new JLabel("Beginner Level: " + beginnerQs);
        lblBeginner.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblBeginner.setBounds(40, 206, 300, 20);
        panel.add(lblBeginner);
        
        JLabel lblIntermediate = new JLabel("Intermediate Level: " + intermediateQs);
        lblIntermediate.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblIntermediate.setBounds(40, 231, 300, 20);
        panel.add(lblIntermediate);
        
        JLabel lblAdvanced = new JLabel("Advanced Level: " + advancedQs);
        lblAdvanced.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblAdvanced.setBounds(40, 256, 300, 20);
        panel.add(lblAdvanced);
        
        // High Score Statistics
        JLabel lblHighScores = new JLabel("Highest Scores by Difficulty");
        lblHighScores.setFont(new Font("Courier New", Font.BOLD, 16));
        lblHighScores.setBounds(20, 296, 350, 25);
        panel.add(lblHighScores);
        
        JLabel lblHighestBeginner = new JLabel("Beginner: " + highestBeginner);
        lblHighestBeginner.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblHighestBeginner.setBounds(30, 327, 300, 20);
        panel.add(lblHighestBeginner);
        
        JLabel lblHighestIntermediate = new JLabel("Intermediate: " + highestIntermediate);
        lblHighestIntermediate.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblHighestIntermediate.setBounds(30, 352, 300, 20);
        panel.add(lblHighestIntermediate);
        
        JLabel lblHighestAdvanced = new JLabel("Advanced: " + highestAdvanced);
        lblHighestAdvanced.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblHighestAdvanced.setBounds(30, 377, 300, 20);
        panel.add(lblHighestAdvanced);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ViewStatistics vs = new ViewStatistics();
                vs.setVisible(true);
            }
        });
        btnRefresh.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnRefresh.setBackground(Color.LIGHT_GRAY);
        btnRefresh.setBounds(320, 410, 120, 33);
        panel.add(btnRefresh);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad = new AdminDashboard();
                ad.setVisible(true);
                dispose();
            }
        });
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnBack.setBackground(new Color(169, 169, 169));
        btnBack.setBounds(80, 410, 120, 33);
        panel.add(btnBack);
    }
}