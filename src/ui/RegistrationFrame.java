package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.DBRegistration;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegistrationFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userField;
	private JTextField mailField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationFrame frame = new RegistrationFrame();
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
	public RegistrationFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 508);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(79, 24, 431, 417);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblRegisterHere = new JLabel("Register Here");
		lblRegisterHere.setBounds(124, 21, 173, 23);
		lblRegisterHere.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegisterHere.setFont(new Font("Courier New", Font.BOLD, 20));
		panel.add(lblRegisterHere);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(46, 190, 96, 26);
		panel.add(lblNewLabel_1);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblConfirmPassword.setBounds(46, 253, 192, 26);
		panel.add(lblConfirmPassword);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = userField.getText();
				String email = mailField.getText();
				char[] pass_chars = passwordField.getPassword();
				char[] con_pass_chars = passwordField_1.getPassword();
				
                String pass = new String(pass_chars);
                String con_pass = new String(con_pass_chars);
                
                if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || con_pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!");
                    return;
                }

                if (user.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Username must be at least 4 characters long");
                    return;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(null, "Invalid email address");
                    return;
                }

                if (pass.length() < 6 || !pass.matches(".*\\d.*")) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 6 characters and contain a number");
                    return;
                }

                if (!pass.equals(con_pass)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }

//                JOptionPane.showMessageDialog(null, "Registration successful!");
                
                boolean success = DBRegistration.registerUser(user, email, pass);

                if (success) {
                	final JOptionPane optionPane = new JOptionPane(
                            "Registration successful!\nRedirecting to login in 3...",
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new Object[]{},   // no buttons
                            null
                    );

                    final JDialog dialog = optionPane.createDialog("Success");
                    dialog.setModal(false);
                    dialog.setVisible(true);

                    final int[] seconds = {3};

                    Timer timer = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            seconds[0]--;

                            if (seconds[0] > 0) {
                                optionPane.setMessage(
                                    "Registration successful!\nRedirecting to login in " + seconds[0] + "..."
                                );
                            } else {
                                ((Timer) e.getSource()).stop();
                                dialog.dispose();

                                LoginFrame lf = new LoginFrame();
                                lf.setVisible(true);
                                dispose();
                            }
                        }
                    });
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Username or Email already exists!",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

			}
		});
		btnRegister.setFont(new Font("Courier New", Font.PLAIN, 11));
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(46, 334, 338, 33);
		panel.add(btnRegister);
		
		JLabel lblNewLabel_1_1 = new JLabel("Username");
		lblNewLabel_1_1.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblNewLabel_1_1.setBounds(46, 65, 96, 26);
		panel.add(lblNewLabel_1_1);
		
		userField = new JTextField();
		userField.setFont(new Font("Courier New", Font.PLAIN, 11));
		userField.setColumns(10);
		userField.setBounds(46, 91, 338, 26);
		panel.add(userField);
		
		JLabel lblEmail = new JLabel("E-Mail");
		lblEmail.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblEmail.setBounds(46, 128, 96, 26);
		panel.add(lblEmail);
		
		mailField = new JTextField();
		mailField.setFont(new Font("Courier New", Font.PLAIN, 11));
		mailField.setColumns(10);
		mailField.setBounds(46, 153, 338, 26);
		panel.add(mailField);
		
		JLabel lblNewLabel = new JLabel("Already have an account?");
		lblNewLabel.setFont(new Font("Courier New", Font.PLAIN, 9));
		lblNewLabel.setBounds(155, 378, 142, 14);
		panel.add(lblNewLabel);
		
		JLabel lbllogin = new JLabel("<html><u>Login</u></html>");
		lbllogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				LoginFrame lf = new LoginFrame();
				lf.setVisible(true);
			}			
			@Override
			public void mouseEntered(MouseEvent e) {
				lbllogin.setForeground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lbllogin.setForeground(Color.BLACK);
			}
		});
		lbllogin.setFont(new Font("Courier New", Font.PLAIN, 9));
		lbllogin.setBounds(197, 392, 40, 14);
		panel.add(lbllogin);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(46, 215, 338, 26);
		panel.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(46, 278, 338, 26);
		panel.add(passwordField_1);

	}
}
