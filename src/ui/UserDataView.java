package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import database.DBQuizAttempt;
import database.DBUser;
import models.User;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * UserDataView - Admin panel to view all registered users
 * Displays users in a table with their statistics
 */
public class UserDataView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable usersTable;
    private DefaultTableModel tableModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserDataView frame = new UserDataView();
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
    public UserDataView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 550);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(35, 31, 720, 460);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblUserDetails = new JLabel("All Registered Users");
        lblUserDetails.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserDetails.setFont(new Font("Courier New", Font.BOLD, 20));
        lblUserDetails.setBounds(200, 11, 320, 32);
        panel.add(lblUserDetails);
        
        // Create table with column headers
        String[] columnNames = {
            "ID", 
            "Username", 
            "Email", 
            "Total Attempts", 
            "Avg Score", 
            "Registered"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        usersTable = new JTable(tableModel);
        usersTable.setFont(new Font("Courier New", Font.PLAIN, 12));
        usersTable.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));
        usersTable.setRowHeight(25);
        
        // Set column widths
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Username
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Email
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Total Attempts
        usersTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Avg Score
        usersTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Registered
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBounds(20, 54, 680, 280);
        panel.add(scrollPane);
        
        // Statistics label
        JLabel lblStats = new JLabel("Total Users: 0");
        lblStats.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblStats.setBounds(20, 345, 400, 20);
        panel.add(lblStats);
        
        // Refresh button
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUserData();
                int totalUsers = DBUser.getTotalUserCount();
                lblStats.setText("Total Users: " + totalUsers);
            }
        });
        btnRefresh.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnRefresh.setBackground(Color.LIGHT_GRAY);
        btnRefresh.setBounds(520, 375, 180, 33);
        panel.add(btnRefresh);
        
        // Back button
        JButton btnBack = new JButton("Back to Dashboard");
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
        btnBack.setBounds(20, 375, 180, 33);
        panel.add(btnBack);
        
        // Load initial data
        loadUserData();
        int totalUsers = DBUser.getTotalUserCount();
        lblStats.setText("Total Users: " + totalUsers);
    }
    
    /**
     * Load all users from database and display in table
     */
    private void loadUserData() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Get all users from database
        List<User> users = DBUser.getAllUsers();
        
        // Add each user to table
        for (User user : users) {
            // Get user statistics
            int totalAttempts = DBQuizAttempt.getUserTotalAttempts(user.getUserId());
            double avgScore = DBQuizAttempt.getUserAverageScore(user.getUserId());
            
            // Format average score
            String avgScoreStr = totalAttempts > 0 ? 
                String.format("%.1f", avgScore) : "N/A";
            
            // Format timestamp
            String registeredDate = user.getCreatedAt() != null ? 
                user.getCreatedAt().toString() : "N/A";
            
            // Add row to table
            Object[] rowData = {
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                totalAttempts,
                avgScoreStr,
                registeredDate
            };
            
            tableModel.addRow(rowData);
        }
    }
}