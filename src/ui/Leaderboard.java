package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import database.DBQuizAttempt;
import models.QuizAttempt;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * Leaderboard - Shows top scores for each difficulty level
 */
public class Leaderboard extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable beginnerTable;
    private JTable intermediateTable;
    private JTable advancedTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Leaderboard frame = new Leaderboard();
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
    public Leaderboard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        panel.setBounds(26, 31, 640, 510);
        contentPane.add(panel);
        
        JLabel lblLeaderboard = new JLabel("🏆 Leaderboard 🏆");
        lblLeaderboard.setHorizontalAlignment(SwingConstants.CENTER);
        lblLeaderboard.setFont(new Font("Courier New", Font.BOLD, 20));
        lblLeaderboard.setBounds(180, 11, 280, 32);
        panel.add(lblLeaderboard);
        
        JLabel lblInfo = new JLabel("Top 10 Scores by Difficulty Level");
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setFont(new Font("Courier New", Font.PLAIN, 12));
        lblInfo.setBounds(180, 45, 280, 20);
        panel.add(lblInfo);
        
        // Create tabbed pane for different difficulties
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Courier New", Font.BOLD, 12));
        tabbedPane.setBounds(20, 75, 600, 370);
        panel.add(tabbedPane);
        
        // Beginner tab
        JPanel beginnerPanel = new JPanel();
        beginnerPanel.setBackground(Color.LIGHT_GRAY);
        beginnerPanel.setLayout(null);
        tabbedPane.addTab("Beginner", null, beginnerPanel, null);
        
        beginnerTable = createLeaderboardTable();
        JScrollPane beginnerScroll = new JScrollPane(beginnerTable);
        beginnerScroll.setBounds(10, 10, 570, 310);
        beginnerPanel.add(beginnerScroll);
        
        // Intermediate tab
        JPanel intermediatePanel = new JPanel();
        intermediatePanel.setBackground(Color.LIGHT_GRAY);
        intermediatePanel.setLayout(null);
        tabbedPane.addTab("Intermediate", null, intermediatePanel, null);
        
        intermediateTable = createLeaderboardTable();
        JScrollPane intermediateScroll = new JScrollPane(intermediateTable);
        intermediateScroll.setBounds(10, 10, 570, 310);
        intermediatePanel.add(intermediateScroll);
        
        // Advanced tab
        JPanel advancedPanel = new JPanel();
        advancedPanel.setBackground(Color.LIGHT_GRAY);
        advancedPanel.setLayout(null);
        tabbedPane.addTab("Advanced", null, advancedPanel, null);
        
        advancedTable = createLeaderboardTable();
        JScrollPane advancedScroll = new JScrollPane(advancedTable);
        advancedScroll.setBounds(10, 10, 570, 310);
        advancedPanel.add(advancedScroll);
        
        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadLeaderboards();
            }
        });
        btnRefresh.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnRefresh.setBackground(Color.LIGHT_GRAY);
        btnRefresh.setBounds(440, 455, 120, 33);
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
        btnBack.setBounds(32, 455, 178, 33);
        panel.add(btnBack);
        
        // Load leaderboard data
        loadLeaderboards();
    }
    
    /**
     * Create a table for leaderboard display
     */
    private JTable createLeaderboardTable() {
        String[] columnNames = {"Rank", "Player", "Score", "Total", "Percentage", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Courier New", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));
        table.setRowHeight(25);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // Rank
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Player
        table.getColumnModel().getColumn(2).setPreferredWidth(60);   // Score
        table.getColumnModel().getColumn(3).setPreferredWidth(60);   // Total
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Percentage
        table.getColumnModel().getColumn(5).setPreferredWidth(150);  // Date
        
        return table;
    }
    
    /**
     * Load leaderboard data for all difficulties
     */
    private void loadLeaderboards() {
        loadLeaderboardForDifficulty("Beginner", beginnerTable);
        loadLeaderboardForDifficulty("Intermediate", intermediateTable);
        loadLeaderboardForDifficulty("Advanced", advancedTable);
    }
    
    /**
     * Load leaderboard for specific difficulty
     */
    private void loadLeaderboardForDifficulty(String difficulty, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        // Get top 10 scores for this difficulty
        List<QuizAttempt> topScores = DBQuizAttempt.getLeaderboard(difficulty, 10);
        
        if (topScores.isEmpty()) {
            Object[] emptyRow = {"--", "No scores yet", "--", "--", "--", "--"};
            model.addRow(emptyRow);
        } else {
            int rank = 1;
            for (QuizAttempt attempt : topScores) {
                // Add medal emoji for top 3
                String rankDisplay;
                if (rank == 1) rankDisplay = "1st";
                else if (rank == 2) rankDisplay = "2nd";
                else if (rank == 3) rankDisplay = "3rd";
                else rankDisplay = String.valueOf(rank);
                
                Object[] rowData = {
                    rankDisplay,
                    attempt.getUsername(),
                    attempt.getScore(),
                    attempt.getTotalQuestions(),
                    String.format("%.1f%%", attempt.getPercentage()),
                    attempt.getAttemptedAt().toString()
                };
                
                model.addRow(rowData);
                rank++;
            }
        }
    }
}