package VaccinationManagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;
import javax.swing.JTextArea;

public class PatientsManagementFrame extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField nameField;
	private JTextField surnameField;
	private JTextField contactField;
	private JTextField emailField;
	private JTextArea addressArea;
	private JDateChooser dateChooser; // Added for date selection

	public PatientsManagementFrame() {
		setSize(1273, 801);
		setBackground(new Color(82, 184, 252));
		setLayout(null);

		JLabel addressLabel = new JLabel("Address");
		addressLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		addressLabel.setBounds(423, 323, 101, 25);
		add(addressLabel);

		JLabel emailLabel = new JLabel("Email");
		emailLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		emailLabel.setBounds(423, 255, 101, 25);
		add(emailLabel);

		addressArea = new JTextArea();
		addressArea.setBounds(555, 301, 200, 78);
		add(addressArea);

		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBounds(555, 256, 200, 28);
		add(emailField);

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(555, 170, 200, 28);
		add(dateChooser);

		JLabel nameLabel = new JLabel("Name");
		nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		nameLabel.setBounds(423, 88, 101, 25);
		add(nameLabel);

		JLabel surnameLabel = new JLabel("Surname");
		surnameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		surnameLabel.setBounds(423, 132, 124, 25);
		add(surnameLabel);

		JLabel dobLabel = new JLabel("DOB");
		dobLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		dobLabel.setBounds(423, 170, 101, 25);
		add(dobLabel);

		JLabel contactLabel = new JLabel("Contact No.");
		contactLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		contactLabel.setBounds(423, 212, 101, 25);
		add(contactLabel);

		nameField = new JTextField();
		nameField.setBounds(555, 89, 200, 28);
		add(nameField);
		nameField.setColumns(10);

		surnameField = new JTextField();
		surnameField.setColumns(10);
		surnameField.setBounds(555, 133, 200, 28);
		add(surnameField);

		contactField = new JTextField();
		contactField.setColumns(10);
		contactField.setBounds(555, 209, 200, 28);
		add(contactField);

		// Table model setup
		tableModel = new DefaultTableModel(new String[]{"Patient ID", "First Name", "Last Name", "DOB", "Contact", "Email", "Address"}, 0);

		// Table setup
		table = new JTable(tableModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					c.setBackground(row % 2 == 0 ? new Color(220, 220, 220) : Color.WHITE); 
				} else {
					c.setBackground(new Color(184, 207, 229)); 
				}
				return c;
			}
		};

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String firstName = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
				String lastName = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
				String dob = (String) tableModel.getValueAt(table.getSelectedRow(), 3);
				String contact = (String) tableModel.getValueAt(table.getSelectedRow(), 4);
				String email = (String) tableModel.getValueAt(table.getSelectedRow(), 5);
				String address = (String) tableModel.getValueAt(table.getSelectedRow(), 6);

				nameField.setText(firstName);
				surnameField.setText(lastName);
				contactField.setText(contact);
				emailField.setText(email);
				addressArea.setText(address);

				// Parse date from table and set it to JDateChooser
				try {
					java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dob);
					dateChooser.setDate(date);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		// Add JScrollPane to make table scrollable
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 534, 1273, 267);
		add(scrollPane);

		// Add button
		JButton addButton = new JButton("Add");
		addButton.setBackground(new Color(128, 255, 128));
		addButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		addButton.setBounds(310, 444, 149, 49);
		add(addButton);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Check for empty fields
					if (nameField.getText().isEmpty() || 
						surnameField.getText().isEmpty() || 
						dateChooser.getDate() == null || 
						contactField.getText().isEmpty() || 
						emailField.getText().isEmpty() || 
						addressArea.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please fill in all fields.");
						return;
					}

					// Validate name fields
					if (!nameField.getText().matches("[a-zA-Z]+") || !surnameField.getText().matches("[a-zA-Z]+")) {
						JOptionPane.showMessageDialog(null, "Name and Surname must contain only letters.");
						return;
					}

					// Validate contact number
					String contactNumber = contactField.getText();
					if (contactNumber.length() != 10 || !contactNumber.matches("\\d{10}")) {
						JOptionPane.showMessageDialog(null, "Contact number must be exactly 10 digits.");
						return;
					}

					// Validate email format
					String email = emailField.getText();
					if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
						JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
						return;
					}

					// Insert record into the database
					insertPatientRecord();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}

			private void insertPatientRecord() {
				String sql = "INSERT INTO Patients(first_name, last_name, dob, contact_number, email, address) VALUES (?, ?, ?, ?, ?, ?)";
				java.util.Date dob = dateChooser.getDate();
				java.sql.Date sqlDate = new java.sql.Date(dob.getTime());

				try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
					 PreparedStatement pstmt = con.prepareStatement(sql)) {

					pstmt.setString(1, nameField.getText());
					pstmt.setString(2, surnameField.getText());
					pstmt.setDate(3, sqlDate);
					pstmt.setString(4, contactField.getText());
					pstmt.setString(5, emailField.getText());
					pstmt.setString(6, addressArea.getText());

					int rowsAffected = pstmt.executeUpdate();

					// Check if the update was successful
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Record inserted successfully!");
						loadPatientData();
					} else {
						JOptionPane.showMessageDialog(null, "Insertion failed. Please try again.");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
				}
			}
		});

		// Modify button
		JButton modifyButton = new JButton("Modify");
		modifyButton.setBackground(new Color(128, 255, 255));
		modifyButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		modifyButton.setBounds(517, 444, 149, 49);
		add(modifyButton);

		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameField.getText().isEmpty() || 
					surnameField.getText().isEmpty() || 
					dateChooser.getDate() == null || 
					contactField.getText().isEmpty() || 
					emailField.getText().isEmpty() || 
					addressArea.getText().isEmpty()) {
					
					JOptionPane.showMessageDialog(null, "Please fill in all fields.");
				} else {
					try {
						// Validate name fields
						if (!nameField.getText().matches("[a-zA-Z]+") || !surnameField.getText().matches("[a-zA-Z]+")) {
							JOptionPane.showMessageDialog(null, "Name and Surname must contain only letters.");
							return;
						}

						// Validate contact number
						String contactNumber = contactField.getText();
						if (contactNumber.length() != 10 || !contactNumber.matches("\\d{10}")) {
							JOptionPane.showMessageDialog(null, "Contact number must be exactly 10 digits.");
							return;
						}

						// Validate email format
						String email = emailField.getText();
						if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
							JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
							return;
						}

						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
						Statement stmt = con.createStatement();

						int patientId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
						java.util.Date dob = dateChooser.getDate();
						java.sql.Date sqlDate = new java.sql.Date(dob.getTime());

						String sql = "UPDATE Patients SET first_name= '" + nameField.getText() +
								"', last_name='" + surnameField.getText() + 
								"', dob ='" + sqlDate +
								"', contact_number='" + contactNumber +
								"', email='" + email +
								"', address='" + addressArea.getText() +
								"' WHERE patient_id = " + patientId;

						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog(null, "Record modified successfully."); // Success message

						con.close();
					} catch (Exception e1) {
						e1.printStackTrace(); // Print stack trace for debugging
						JOptionPane.showMessageDialog(null, "Failed to modify record."); // Failure message
					}

					// Clear fields after operation
					nameField.setText("");
					surnameField.setText("");
					dateChooser.setDate(null);
					contactField.setText("");
					emailField.setText("");
					addressArea.setText("");
					loadPatientData();
				}
			}
		});

		// Delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.setBackground(new Color(255, 128, 128));
		deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		deleteButton.setBounds(721, 444, 149, 49);
		add(deleteButton);

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
					Statement stmt = con.createStatement();

					int patientId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
					String sql = "DELETE FROM Patients WHERE patient_id = " + patientId;
					int rowsAffected = stmt.executeUpdate(sql);

					con.close();

					// Check if a record was deleted
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Record deleted successfully."); // Success message
					} else {
						JOptionPane.showMessageDialog(null, "No record found to delete."); // Inform user if no record was deleted
					}
				} catch (Exception e1) {
					e1.printStackTrace(); // Print stack trace for debugging
					JOptionPane.showMessageDialog(null, "Failed to delete record."); // Failure message
				}

				// Clear fields after operation
				nameField.setText("");
				surnameField.setText("");
				dateChooser.setDate(null);
				contactField.setText("");
				emailField.setText("");
				addressArea.setText("");
				loadPatientData();
			}
		});

		JLabel lblNewLabel = new JLabel("Patient Management");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 1273, 59);
		add(lblNewLabel);

		// Load data from the database
		loadPatientData();
	}

	private void loadPatientData() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
			Statement stmt = con.createStatement();
			String query = "SELECT patient_id, first_name, last_name, dob, contact_number, email, address FROM Patients";
			ResultSet rs = stmt.executeQuery(query);

			// Clear existing rows
			tableModel.setRowCount(0);
			
			// Fetch data from ResultSet and add it to the table model
			while (rs.next()) {
				int patientId = rs.getInt("patient_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String dob = rs.getString("dob");
				String contactNumber = rs.getString("contact_number");
				String email = rs.getString("email");
				String address = rs.getString("address");

				// Add row to the table model
				tableModel.addRow(new Object[]{patientId, firstName, lastName, dob, contactNumber, email, address});
			}

			// Close the ResultSet, Statement, and Connection
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
