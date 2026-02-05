package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import database.DBQuizAttempt;
import database.DBUser;
import models.QuizAttempt;
import models.User;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class SearchPlayer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchField;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel lblPlayerInfo;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SearchPlayer frame = new SearchPlayer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SearchPlayer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 550);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(35, 31, 666, 460);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblTitle = new JLabel("Search Player");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Courier New", Font.BOLD, 20));
        lblTitle.setBounds(200, 11, 220, 32);
        panel.add(lblTitle);
        
        JLabel lblSearch = new JLabel("Enter Username or Email:");
        lblSearch.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblSearch.setBounds(20, 54, 250, 20);
        panel.add(lblSearch);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Courier New", Font.PLAIN, 12));
        searchField.setBounds(20, 77, 400, 30);
        panel.add(searchField);
        searchField.setColumns(10);
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        btnSearch.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnSearch.setBackground(Color.LIGHT_GRAY);
        btnSearch.setBounds(430, 77, 100, 30);
        panel.add(btnSearch);
        
        // Player info label
        lblPlayerInfo = new JLabel("Search for a player to view their stats");
        lblPlayerInfo.setFont(new Font("Courier New", Font.BOLD, 13));
        lblPlayerInfo.setBounds(20, 118, 623, 25);
        panel.add(lblPlayerInfo);
        
        // Table for quiz history
        String[] columnNames = {"Attempt #", "Difficulty", "Score", "Percentage", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Courier New", Font.PLAIN, 11));
        resultsTable.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 11));
        resultsTable.setRowHeight(22);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBounds(20, 154, 623, 240);
        panel.add(scrollPane);
        
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
        btnBack.setBounds(20, 405, 120, 33);
        panel.add(btnBack);
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                tableModel.setRowCount(0);
                lblPlayerInfo.setText("Search for a player to view their stats");
            }
        });
        btnClear.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnClear.setBackground(Color.LIGHT_GRAY);
        btnClear.setBounds(523, 405, 120, 33);
        panel.add(btnClear);
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username or email!");
            return;
        }
        
        // Clear previous results
        tableModel.setRowCount(0);
        
        // Search for users
        List<User> users = DBUser.searchUsers(searchTerm);
        
        if (users.isEmpty()) {
            lblPlayerInfo.setText("No players found matching: " + searchTerm);
            JOptionPane.showMessageDialog(this, "No users found!");
            return;
        }
        
        if (users.size() > 1) {
            // Multiple users found - let user select
            String[] usernames = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                usernames[i] = users.get(i).getUsername() + " (" + users.get(i).getEmail() + ")";
            }
            
            String selected = (String) JOptionPane.showInputDialog(
                this,
                "Multiple users found. Select one:",
                "Select User",
                JOptionPane.QUESTION_MESSAGE,
                null,
                usernames,
                usernames[0]
            );
            
            if (selected == null) return;
            
            // Find selected user
            int selectedIndex = 0;
            for (int i = 0; i < usernames.length; i++) {
                if (usernames[i].equals(selected)) {
                    selectedIndex = i;
                    break;
                }
            }
            
            displayPlayerStats(users.get(selectedIndex));
        } else {
            // Single user found
            displayPlayerStats(users.get(0));
        }
    }
    
    private void displayPlayerStats(User user) {
        // Update info label
        int totalAttempts = DBQuizAttempt.getUserTotalAttempts(user.getUserId());
        double avgScore = DBQuizAttempt.getUserAverageScore(user.getUserId());
        
        lblPlayerInfo.setText(String.format(
            "Player: %s | Email: %s | Total Games: %d | Avg Score: %.1f",
            user.getUsername(),
            user.getEmail(),
            totalAttempts,
            avgScore
        ));
        
        // Load quiz history
        List<QuizAttempt> attempts = DBQuizAttempt.getAttemptsByUser(user.getUserId());
        
        if (attempts.isEmpty()) {
            Object[] rowData = {"No quiz attempts yet", "", "", "", ""};
            tableModel.addRow(rowData);
        } else {
            for (int i = 0; i < attempts.size(); i++) {
                QuizAttempt attempt = attempts.get(i);
                
                Object[] rowData = {
                    i + 1,
                    attempt.getDifficultyLevel(),
                    attempt.getScore() + "/" + attempt.getTotalQuestions(),
                    String.format("%.1f%%", attempt.getPercentage()),
                    attempt.getAttemptedAt()
                };
                
                tableModel.addRow(rowData);
            }
        }
    }
}