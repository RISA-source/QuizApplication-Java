package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.DBLogin;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
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
	public LoginFrame() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login Here");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD, 20));
		lblNewLabel.setBounds(183, 37, 209, 43);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("E-Mail");
		lblNewLabel_1.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(124, 96, 96, 26);
		contentPane.add(lblNewLabel_1);
		
		JLabel password = new JLabel("Password");
		password.setFont(new Font("Courier New", Font.PLAIN, 13));
		password.setBounds(124, 160, 96, 26);
		contentPane.add(password);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = textField.getText();
                char[] pwdChars = passwordField.getPassword();
                String pass = new String(pwdChars);
				
				if (email.equals("admin") && pass.equals("admin")) {
					AdminDashboard ad = new AdminDashboard();
					ad.setVisible(true);
					dispose();
					return;
				}

				// basic validation
				if (email.isEmpty() || pass.isEmpty()) {
				    JOptionPane.showMessageDialog(null, "Email and password required!");
				    return;
				}

				// DB login check
				boolean isValidUser = DBLogin.loginUser(email, pass);

				if (isValidUser) {
				    UserDashboard ud = new UserDashboard();
				    ud.setVisible(true);
				    dispose();
				} else {
				    JOptionPane.showMessageDialog(
				        null,
				        "Invalid email or password!",
				        "Login Failed",
				        JOptionPane.ERROR_MESSAGE
				    );
				}

			}
		});
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnNewButton.setBackground(new Color(192, 192, 192));
		btnNewButton.setBounds(124, 242, 338, 33);
		contentPane.add(btnNewButton);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 0, 5, 0));
		panel.setBackground(Color.GRAY);
		panel.setBounds(81, 25, 422, 329);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("If you donot have an account,");
		lblNewLabel_2.setFont(new Font("Courier New", Font.PLAIN, 9));
		lblNewLabel_2.setBounds(139, 276, 164, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("<html><u>Register Here</u></html>");
		lblNewLabel_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				RegistrationFrame rf = new RegistrationFrame();
				rf.setVisible(true);
			}			
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewLabel_2_1.setForeground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNewLabel_2_1.setForeground(Color.BLACK);
			}
		});
		lblNewLabel_2_1.setFont(new Font("Courier New", Font.PLAIN, 9));
		lblNewLabel_2_1.setBounds(172, 287, 83, 14);
		panel.add(lblNewLabel_2_1);
		
		textField = new JTextField();
		textField.setFont(new Font("Courier New", Font.PLAIN, 11));
		textField.setColumns(10);
		textField.setBounds(43, 97, 338, 26);
		panel.add(textField);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(43, 161, 338, 26);
		panel.add(passwordField);

	}
}
