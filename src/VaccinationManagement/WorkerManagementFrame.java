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

public class WorkerManagementFrame extends JPanel {

    private JTable workerTable;
    private DefaultTableModel tableModel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField contactField;
    private JTextField emailField;
    private JTextField centerField;
    private JTextField usernameField;
    private JTextField passwordField;

    public WorkerManagementFrame() {
        setSize(1273, 801);
        setBackground(new Color(82, 184, 252));
        setLayout(null);

        // Labels
        JLabel nameLabel = new JLabel("First Name");
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        nameLabel.setBounds(424, 88, 150, 25);
        add(nameLabel);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lastNameLabel.setBounds(424, 138, 150, 25);
        add(lastNameLabel);

        JLabel contactLabel = new JLabel("Contact No.");
        contactLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        contactLabel.setBounds(424, 188, 150, 25);
        add(contactLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        emailLabel.setBounds(424, 238, 150, 25);
        add(emailLabel);

        JLabel centerLabel = new JLabel("Center ID");
        centerLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        centerLabel.setBounds(424, 288, 150, 25);
        add(centerLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        usernameLabel.setBounds(424, 338, 150, 25);
        add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        passwordLabel.setBounds(424, 388, 150, 25);
        add(passwordLabel);

        // Text fields
        firstNameField = new JTextField();
        firstNameField.setBounds(555, 88, 200, 30);
        add(firstNameField);

        lastNameField = new JTextField();
        lastNameField.setBounds(555, 138, 200, 30);
        add(lastNameField);

        contactField = new JTextField();
        contactField.setBounds(555, 188, 200, 30);
        add(contactField);

        emailField = new JTextField();
        emailField.setBounds(555, 238, 200, 30);
        add(emailField);

        centerField = new JTextField();
        centerField.setBounds(555, 288, 200, 30);
        add(centerField);

        usernameField = new JTextField();
        usernameField.setBounds(555, 338, 200, 30);
        add(usernameField);

        passwordField = new JTextField();
        passwordField.setBounds(555, 388, 200, 30);
        add(passwordField);

        JLabel lblNewLabel = new JLabel("Worker Management");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 1273, 59);
        add(lblNewLabel);

        // Table model and table
        tableModel = new DefaultTableModel(new String[] { "Worker ID", "First Name", "Last Name", "Contact", "Email", "Center ID" }, 0);
        workerTable = new JTable(tableModel) {
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

        JScrollPane scrollPane = new JScrollPane(workerTable);
        scrollPane.setBounds(0, 534, 1273, 267);
        add(scrollPane);

        // Load existing worker data
        loadWorkerData();

        // Add new worker button
        JButton addButton = new JButton("Add Worker");
        addButton.setBackground(new Color(128, 255, 128));
        addButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        addButton.setBounds(310, 444, 149, 49);
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewWorker();
            }
        });

        // Edit worker button
        JButton editButton = new JButton("Edit Worker");
        editButton.setBackground(new Color(128, 255, 255));
        editButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        editButton.setBounds(517, 444, 149, 49);
        add(editButton);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editWorker();
            }
        });

        // Delete worker button
        JButton deleteButton = new JButton("Delete Worker");
        deleteButton.setBackground(new Color(255, 128, 128));
        deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        deleteButton.setBounds(721, 444, 149, 49);
        add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteWorker();
            }
        });

        // Table selection
        workerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = workerTable.getSelectedRow();
                firstNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                lastNameField.setText((String) tableModel.getValueAt(selectedRow, 2));
                contactField.setText((String) tableModel.getValueAt(selectedRow, 3));
                emailField.setText((String) tableModel.getValueAt(selectedRow, 4));
                centerField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });
    }

    // Method to load existing worker data from the database
    private void loadWorkerData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM HealthcareWorkers")) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getInt("worker_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("contact_number"),
                    rs.getString("email"), rs.getInt("center_id")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to validate fields
    private boolean validateWorkerFields() {
        if (firstNameField.getText().isEmpty() || !firstNameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "First name is required and should contain only letters.");
            return false;
        }
        if (lastNameField.getText().isEmpty() || !lastNameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Last name is required and should contain only letters.");
            return false;
        }
        if (contactField.getText().length() != 10 || !contactField.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Contact number must be exactly 10 digits.");
            return false;
        }
        if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
            return false;
        }
        return true;
    }

    // Method to add a new worker to the database
    private void addNewWorker() {
        if (!validateWorkerFields()) {
            return; // Stop the process if validation fails
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                PreparedStatement psWorker = con.prepareStatement("INSERT INTO HealthcareWorkers (first_name, last_name, contact_number, email, center_id, user_id) VALUES (?, ?, ?, ?, ?, ?)");
                PreparedStatement psUser = con.prepareStatement("INSERT INTO HealthcareWorkerUsers (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            // Insert login credentials into HealthcareWorkerUsers only after validation
            psUser.setString(1, usernameField.getText());
            psUser.setString(2, passwordField.getText());
            psUser.executeUpdate();
        
            ResultSet generatedKeys = psUser.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);  // Get the generated user_id from HealthcareWorkerUsers
            }

            // Insert healthcare worker details into HealthcareWorkers
            psWorker.setString(1, firstNameField.getText());
            psWorker.setString(2, lastNameField.getText());
            psWorker.setString(3, contactField.getText());
            psWorker.setString(4, emailField.getText());
            psWorker.setInt(5, Integer.parseInt(centerField.getText()));  // center_id
            psWorker.setInt(6, userId);  // user_id linked to login credentials

            psWorker.executeUpdate();

            JOptionPane.showMessageDialog(null, "Healthcare Worker added successfully!");

            // Refresh the table data
            loadWorkerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding healthcare worker.");
        }
    }

    // Method to edit a selected worker's details
    private void editWorker() {
        int selectedRow = workerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a worker to edit.");
            return;
        }

        int workerId = (int) tableModel.getValueAt(selectedRow, 0);  // Get worker_id from the selected row

        if (!validateWorkerFields()) {
            return; // Stop the process if validation fails
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                PreparedStatement psWorker = con.prepareStatement("UPDATE HealthcareWorkers SET first_name = ?, last_name = ?, contact_number = ?, email = ?, center_id = ? WHERE worker_id = ?")) {

            psWorker.setString(1, firstNameField.getText());
            psWorker.setString(2, lastNameField.getText());
            psWorker.setString(3, contactField.getText());
            psWorker.setString(4, emailField.getText());
            psWorker.setInt(5, Integer.parseInt(centerField.getText()));  // Update center_id
            psWorker.setInt(6, workerId);  // Use worker_id to identify the row to update

            psWorker.executeUpdate();
            JOptionPane.showMessageDialog(null, "Healthcare Worker updated successfully!");

            // Refresh the table data
            loadWorkerData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating healthcare worker.");
        }
    }

    // Method to delete a selected worker from the database
    private void deleteWorker() {
        int selectedRow = workerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a worker to delete.");
            return;
        }

        int workerId = (int) tableModel.getValueAt(selectedRow, 0);  // Get worker_id from the selected row
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this worker?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                    PreparedStatement psWorker = con.prepareStatement("DELETE FROM HealthcareWorkers WHERE worker_id = ?");
                    PreparedStatement psUser = con.prepareStatement("DELETE FROM HealthcareWorkerUsers WHERE user_id = (SELECT user_id FROM HealthcareWorkers WHERE worker_id = ?)")) {

                psUser.setInt(1, workerId);  // Get the associated user_id to delete the user credentials
                psUser.executeUpdate();

                psWorker.setInt(1, workerId);  // Delete the healthcare worker from HealthcareWorkers
                psWorker.executeUpdate();

                JOptionPane.showMessageDialog(null, "Healthcare Worker deleted successfully!");

                // Refresh the table data
                loadWorkerData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting healthcare worker.");
            }
        }
    }
}


