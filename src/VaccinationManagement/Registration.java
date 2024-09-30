package VaccinationManagement;
import java.awt.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import javax.swing.SwingConstants;

public class Registration extends JFrame {

	  private JTextField usernameField;
	    private JPasswordField passwordField, confirmPasswordField;
	    private JComboBox<String> roleComboBox;
	    private JButton registerButton, loginButton;
	    private JLabel errorMessageLabel;


	public Registration() {
	
		
		
		
		
		 setTitle("Vaccination Management System - Register");
	        setSize(400, 300);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        // Creating the panel to hold components
	        JPanel panel = new JPanel();
	        add(panel);
	        placeComponents(panel);
	}
	
	private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Username Label and TextField
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 30, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 30, 160, 25);
        panel.add(usernameField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 70, 160, 25);
        panel.add(passwordField);

        // Confirm Password Label and PasswordField
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 110, 120, 25);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(150, 110, 160, 25);
        panel.add(confirmPasswordField);

        // Role Label and ComboBox
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 150, 80, 25);
        panel.add(roleLabel);

        String[] roles = {"admin", "healthcare_worker", "patient"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 150, 160, 25);
        panel.add(roleComboBox);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(50, 190, 120, 25);
        panel.add(registerButton);

        // Login Button (Redirect to Login Frame)
        loginButton = new JButton("Login");
        loginButton.setBounds(190, 190, 120, 25);
        panel.add(loginButton);

        // Error Message Label
        errorMessageLabel = new JLabel("");
        errorMessageLabel.setBounds(50, 220, 300, 25);
        panel.add(errorMessageLabel);

        // Register Button ActionListener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                registerUser(username, password, confirmPassword, role);
            }

        });

        // Login Button ActionListener (to redirect to login frame)
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login loginFrame = new Login(); // Redirect to LoginFrame
                loginFrame.setVisible(true);
                dispose(); // Close Registration Frame
            }
        });
    }

    // Method to handle registration logic
    private void registerUser(String username, String password, String confirmPassword, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Username and Password cannot be empty");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabel.setText("Passwords do not match");
            return;
        }

        // Check if the username is already taken (in the Users table)
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vaccination_system", "root", "password");

            String checkQuery = "SELECT * FROM Users WHERE username=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                errorMessageLabel.setText("Username already taken");
                return;
            }

            // Insert new user into Users table
            String insertQuery = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);  // Consider hashing the password for better security
            insertStmt.setString(3, role);

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please login.");
                // Redirect to Login Frame after successful registration
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
                dispose();  // Close the registration frame
            } else {
                errorMessageLabel.setText("Registration failed. Please try again.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            errorMessageLabel.setText("Database error");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

	
	public static void main(String[] args) {
		Registration registrationFrame = new Registration();
        registrationFrame.setVisible(true);
	}


	

}
