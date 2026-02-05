package ui;

import database.DBLogin;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * UserDashboard - Main dashboard for logged-in users
 */
public class UserDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserDashboard frame = new UserDashboard();
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
    public UserDashboard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 509);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        panel.setBounds(49, 24, 494, 417);
        contentPane.add(panel);
        
        JLabel lblUserRamKapoor = new JLabel("QuizRizz");
        lblUserRamKapoor.setHorizontalAlignment(SwingConstants.CENTER);
        lblUserRamKapoor.setFont(new Font("Courier New", Font.BOLD, 20));
        lblUserRamKapoor.setBounds(151, 30, 193, 23);
        panel.add(lblUserRamKapoor);
        
        // Get username from logged-in user
        String user = "";
        if (DBLogin.currentUser != null) {
            user = DBLogin.currentUser.getUsername();
        }
        
        JLabel lblWelcomeUser = new JLabel("Welcome " + user + ",");
        lblWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcomeUser.setFont(new Font("Courier New", Font.PLAIN, 13));
        lblWelcomeUser.setBounds(53, 76, 399, 14);
        panel.add(lblWelcomeUser);
        
        JLabel lblQuizSettings = new JLabel("Quiz Setting");
        lblQuizSettings.setHorizontalAlignment(SwingConstants.LEFT);
        lblQuizSettings.setFont(new Font("Courier New", Font.BOLD, 16));
        lblQuizSettings.setBounds(53, 115, 229, 23);
        panel.add(lblQuizSettings);
        
        // Logout button
        JButton btnLogOut = new JButton("Log Out");
        btnLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBLogin.logout();
                LoginFrame lf = new LoginFrame();
                lf.setVisible(true);
                dispose();
            }
        });
        btnLogOut.setForeground(Color.WHITE);
        btnLogOut.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnLogOut.setBackground(Color.RED);
        btnLogOut.setBounds(208, 373, 87, 33);
        panel.add(btnLogOut);
        
        // Leaderboard button
        JButton btnLeaderboard = new JButton("Leaderboard");
        btnLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leaderboard l = new Leaderboard();
                l.setVisible(true);
                dispose();
            }
        });
        btnLeaderboard.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnLeaderboard.setBackground(Color.LIGHT_GRAY);
        btnLeaderboard.setBounds(53, 256, 196, 33);
        panel.add(btnLeaderboard);
        
        // Difficulty selection combo box
        comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Courier New", Font.PLAIN, 11));
        comboBox.setBackground(Color.LIGHT_GRAY);
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Beginner", "Intermediate", "Advanced"}));
        comboBox.setBounds(228, 154, 224, 33);
        panel.add(comboBox);
        
        JLabel lblChooseDifficulty = new JLabel("Choose Difficulty");
        lblChooseDifficulty.setHorizontalAlignment(SwingConstants.LEFT);
        lblChooseDifficulty.setFont(new Font("Courier New", Font.PLAIN, 15));
        lblChooseDifficulty.setBounds(53, 156, 165, 23);
        panel.add(lblChooseDifficulty);
        
        // Search Player button
        JButton btnSearchPlayer = new JButton("Search Player");
        btnSearchPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchPlayerUser spu = new SearchPlayerUser();
                spu.setVisible(true);
                dispose();
            }
        });
        btnSearchPlayer.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnSearchPlayer.setBackground(Color.LIGHT_GRAY);
        btnSearchPlayer.setBounds(259, 256, 193, 33);
        panel.add(btnSearchPlayer);
        
        // Play button - passes selected difficulty to QuizPlay
        JButton btnPlay = new JButton("Play");
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty = (String) comboBox.getSelectedItem();
                QuizPlay qp = new QuizPlay(selectedDifficulty);
                qp.setVisible(true);
                dispose();
            }
        });
        btnPlay.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnPlay.setBackground(Color.LIGHT_GRAY);
        btnPlay.setBounds(53, 300, 399, 33);
        panel.add(btnPlay);
        
        // View Scores button
        JButton btnViewYourScores = new JButton("View Your Scores");
        btnViewYourScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewScores vs = new ViewScores();
                vs.setVisible(true);
                dispose();
            }
        });
        btnViewYourScores.setFont(new Font("Courier New", Font.PLAIN, 11));
        btnViewYourScores.setBackground(Color.LIGHT_GRAY);
        btnViewYourScores.setBounds(53, 212, 399, 33);
        panel.add(btnViewYourScores);
    }
}