package VaccinationManagement;

import java.awt.EventQueue;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.SystemColor;

public class User extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public User() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login();
			}
		});
		btnNewButton.setBackground(new Color(240, 248, 255));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.setBounds(763, 486, 85, 57);
		contentPane.add(btnNewButton);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setForeground(Color.PINK);
		usernameLabel.setBackground(Color.PINK);
		usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		usernameLabel.setBounds(218, 257, 300, 50);
		contentPane.add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(Color.PINK);
		passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
		passwordLabel.setBounds(218, 335, 300, 50);
		contentPane.add(passwordLabel); 
		
		JTextField usernameField = new JTextField();
		usernameField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		usernameField.setBackground(SystemColor.inactiveCaption);
		usernameField.setBounds(528, 258, 300, 50);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setFont(new Font("Times New Roman", Font.BOLD, 20));
		passwordField.setBackground(SystemColor.inactiveCaption);
		passwordField.setBounds(528, 335, 300, 50);
		contentPane.add(passwordField);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBackground(new Color(128, 255, 128));
		loginButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				try {
					
					String username = usernameField.getText();
					String password = new String(passwordField.getPassword());
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine","root","root");
					
					Statement stmt = con.createStatement();
					
					String sql = "select * from healthcareworkerusers where username = '"+username+"' and password = '"+password+"'";
					
					ResultSet rs = stmt.executeQuery(sql);
					
					if(rs.next())
					{
						int workerId = rs.getInt("user_id");
						JOptionPane.showMessageDialog(null, "Login Succesfull");
						
						new HealthcareWorkerDashboard(workerId);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "invalid username or password");
					}
					
					
					
					con.close();
				}catch(Exception e1)
				{
					System.out.println(e1);
				}
				
				
			}
		});
		loginButton.setBounds(359, 486, 246, 57);
		contentPane.add(loginButton);
		
		JLabel lblNewLabel = new JLabel("Worker Login");
		lblNewLabel.setForeground(new Color(255, 128, 64));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 70));
		lblNewLabel.setBounds(148, 29, 628, 204);
		contentPane.add(lblNewLabel);
		setVisible(true);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\vaccination-image.png"));
		lblNewLabel_1.setBounds(0, 0, 1540, 845);
		contentPane.add(lblNewLabel_1);
		
		
	}
	
	
	public static void main(String[] args) {
		
		User frame = new User();
		
}
}
