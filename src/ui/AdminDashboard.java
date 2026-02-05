package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminDashboard frame = new AdminDashboard();
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
	public AdminDashboard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 510);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.GRAY);
		panel.setBounds(47, 27, 494, 417);
		contentPane.add(panel);
		
		JLabel lblAdminPanel = new JLabel("ADMIN Panel");
		lblAdminPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminPanel.setFont(new Font("Courier New", Font.BOLD, 20));
		lblAdminPanel.setBounds(155, 21, 173, 23);
		panel.add(lblAdminPanel);
		
		JLabel lblWelcomeAdmin = new JLabel("Welcome ADMIN,");
		lblWelcomeAdmin.setFont(new Font("Courier New", Font.PLAIN, 12));
		lblWelcomeAdmin.setBounds(53, 76, 142, 14);
		panel.add(lblWelcomeAdmin);
		
		JLabel lblQuestionManagement = new JLabel("Question Management");
		lblQuestionManagement.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuestionManagement.setFont(new Font("Courier New", Font.BOLD, 16));
		lblQuestionManagement.setBounds(133, 115, 229, 23);
		panel.add(lblQuestionManagement);
		
		JButton btnDelete = new JButton("Manage Questions");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionManagement qm = new QuestionManagement();
				qm.setVisible(true);
				dispose();
			}
		});
		btnDelete.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnDelete.setBackground(Color.LIGHT_GRAY);
		btnDelete.setBounds(53, 162, 399, 33);
		panel.add(btnDelete);
		
		JLabel lblUserManagement = new JLabel("User Management");
		lblUserManagement.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserManagement.setFont(new Font("Courier New", Font.BOLD, 16));
		lblUserManagement.setBounds(133, 230, 229, 23);
		panel.add(lblUserManagement);
		
		JButton btnViewStatistics = new JButton("View Statistics");
		btnViewStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewStatistics vs = new ViewStatistics();
				vs.setVisible(true);
				dispose();
			}
		});
		btnViewStatistics.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnViewStatistics.setBackground(Color.LIGHT_GRAY);
		btnViewStatistics.setBounds(319, 273, 142, 33);
		panel.add(btnViewStatistics);
		
		JButton btnSearchPlayer = new JButton("Search Player");
		btnSearchPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchPlayer sp = new SearchPlayer();
				sp.setVisible(true);
				dispose();
			}
		});
		btnSearchPlayer.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnSearchPlayer.setBackground(Color.LIGHT_GRAY);
		btnSearchPlayer.setBounds(53, 273, 137, 33);
		panel.add(btnSearchPlayer);
		
		JButton btnUserData = new JButton("User Data");
		btnUserData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserDataView udv = new UserDataView();
				udv.setVisible(true);
				dispose();
			}
		});
		btnUserData.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnUserData.setBackground(Color.LIGHT_GRAY);
		btnUserData.setBounds(200, 273, 109, 33);
		panel.add(btnUserData);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginFrame lf = new LoginFrame();
				lf.setVisible(true);
			}
		});
		btnLogOut.setForeground(Color.WHITE);
		btnLogOut.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnLogOut.setBackground(Color.RED);
		btnLogOut.setBounds(208, 355, 87, 33);
		panel.add(btnLogOut);
		
		JLabel lblIeViewAdd = new JLabel("i.e. Add, Edit or Delete questions ");
		lblIeViewAdd.setFont(new Font("Courier New", Font.PLAIN, 10));
		lblIeViewAdd.setBounds(143, 137, 262, 14);
		panel.add(lblIeViewAdd);

	}
}
