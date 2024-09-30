package VaccinationManagement;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class Admin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;

	
	
	public Admin() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		usernameLabel.setBounds(314, 268, 300, 50);
		contentPane.add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		passwordLabel.setBounds(314, 386, 300, 50);
		contentPane.add(passwordLabel);
		
		usernameField = new JTextField();
		usernameField.setBounds(624, 268, 300, 50);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(624, 386, 300, 50);
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login();
			}
		});
		btnNewButton.setBackground(new Color(240, 248, 255));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.setBounds(922, 564, 85, 50);
		contentPane.add(btnNewButton);
		
		
		
		JButton loginButton = new JButton("Login");
		loginButton.setBackground(new Color(152, 251, 152));
		loginButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String (passwordField.getPassword());
				
				if(username.equals("admin") && password.equals("admin123"))
				{
					new AdminDashboard();
					dispose();
				}
				else
				{
					 JOptionPane.showMessageDialog(Admin.this, "Invalid username or password.","Login Failed", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		loginButton.setBounds(486, 557, 250, 50);
		contentPane.add(loginButton);
		
		JLabel lblNewLabel = new JLabel("Admin Login");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 65));
		lblNewLabel.setBounds(347, 80, 571, 137);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\login.png"));
		lblNewLabel_1.setBounds(0, 0, 1540, 845);
		contentPane.add(lblNewLabel_1);
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {

					Admin frame = new Admin();
				
	}


}
