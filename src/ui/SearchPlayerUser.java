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
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * SearchPlayerUser - Search for other players and view their stats
 */
public class SearchPlayerUser extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchField;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel lblPlayerInfo;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SearchPlayerUser frame = new SearchPlayerUser();
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
    public SearchPlayerUser() {
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
        panel.setBounds(33, 28, 620, 510);
        contentPane.add(panel);
        
        JLabel lblSearchPlayer = new JLabel("Search Player");
        lblSearchPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        lblSearchPlayer.setFont(new Font("Courier New", Font.BOLD, 20));
        lblSearchPlayer.setBounds(200, 11, 220, 32);
        panel.add(lblSearchPlayer);
        
        JLabel lblInstruction = new JLabel("Enter Username or Email:");
        lblInstruction.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblInstruction.setBounds(20, 54, 250, 20);
        panel.add(lblInstruction);
        
        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Courier New", Font.PLAIN, 12));
        searchField.setBounds(20, 77, 400, 30);
        panel.add(searchField);
        searchField.setColumns(10);
        
        // Search button
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
        
        // Clear button
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                tableModel.setRowCount(0);
                lblPlayerInfo.setText("Search for a player to view their quiz history");
            }
        });
        btnClear.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnClear.setBackground(Color.LIGHT_GRAY);
        btnClear.setBounds(540, 77, 70, 30);
        panel.add(btnClear);
        
        // Player info label
        lblPlayerInfo = new JLabel("Search for a player to view their quiz history");
        lblPlayerInfo.setFont(new Font("Courier New", Font.BOLD, 13));
        lblPlayerInfo.setBounds(20, 120, 580, 25);
        panel.add(lblPlayerInfo);
        
        // Create table for quiz history
        String[] columnNames = {"#", "Difficulty", "Score", "Total", "Percentage", "Date"};
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
        
        // Set column widths
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(40);   // #
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // Difficulty
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(60);   // Score
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(60);   // Total
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(90);   // Percentage
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(150);  // Date
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBounds(20, 156, 580, 290);
        panel.add(scrollPane);
        
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
        btnBack.setBounds(25, 460, 178, 33);
        panel.add(btnBack);
    }
    
    /**
     * Perform player search
     */
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
            // Multiple users found - let user select one
            String[] usernames = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                usernames[i] = users.get(i).getUsername() + " (" + users.get(i).getEmail() + ")";
            }
            
            String selected = (String) JOptionPane.showInputDialog(
                this,
                "Multiple players found. Select one:",
                "Select Player",
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
    
    /**
     * Display player's statistics and quiz history
     */
    private void displayPlayerStats(User user) {
        // Get player statistics
        int totalAttempts = DBQuizAttempt.getUserTotalAttempts(user.getUserId());
        double avgScore = DBQuizAttempt.getUserAverageScore(user.getUserId());
        
        // Best scores for each difficulty
        int bestBeginner = DBQuizAttempt.getUserBestScore(user.getUserId(), "Beginner");
        int bestIntermediate = DBQuizAttempt.getUserBestScore(user.getUserId(), "Intermediate");
        int bestAdvanced = DBQuizAttempt.getUserBestScore(user.getUserId(), "Advanced");
        
        // Update info label
        lblPlayerInfo.setText(String.format(
            "Player: %s | Total Games: %d | Avg Score: %.1f | Best: B-%d I-%d A-%d",
            user.getUsername(),
            totalAttempts,
            avgScore,
            bestBeginner,
            bestIntermediate,
            bestAdvanced
        ));
        
        // Load quiz history
        List<QuizAttempt> attempts = DBQuizAttempt.getAttemptsByUser(user.getUserId());
        
        if (attempts.isEmpty()) {
            Object[] rowData = {"--", "No quiz attempts yet", "--", "--", "--", "--"};
            tableModel.addRow(rowData);
        } else {
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
        }
    }
}