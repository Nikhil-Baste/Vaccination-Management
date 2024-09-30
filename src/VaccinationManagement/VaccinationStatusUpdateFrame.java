package VaccinationManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class VaccinationStatusUpdateFrame extends JPanel {

    private JTextField patientNameField, vaccineNameField, searchField;
    private JComboBox<Integer> doseNumberComboBox;
    private JComboBox<String> statusComboBox;  // Combo box for status
    private JTable updatedRecordsTable;
    private DefaultTableModel updatedRecordsTableModel;
    private int workerId;
    private int fetchedPatientId, fetchedVaccineId;  // Store fetched patient and vaccine IDs
    private JDateChooser dateAdministeredChooser;  // JDateChooser for date administered

    public VaccinationStatusUpdateFrame(int workerId) {
        this.workerId = workerId;
        setSize(1273, 801);
        setBackground(new Color(224, 255, 255));
        setLayout(null);

        JLabel titleLabel = new JLabel("Update Vaccination Status");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        titleLabel.setBounds(465, 10, 380, 75);
        add(titleLabel);

        // Search section
        JLabel searchLabel = new JLabel("Search Patient (ID/Name):");
        searchLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        searchLabel.setBounds(45, 142, 166, 25);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(221, 142, 200, 25);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(441, 143, 100, 25);
        add(searchButton);

        // Patient and vaccine details fields
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        patientNameLabel.setBounds(45, 192, 100, 25);
        add(patientNameLabel);

        patientNameField = new JTextField();
        patientNameField.setBounds(221, 192, 200, 25);
        patientNameField.setEditable(false);  // Non-editable
        add(patientNameField);

        JLabel vaccineNameLabel = new JLabel("Vaccine Name:");
        vaccineNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        vaccineNameLabel.setBounds(45, 242, 100, 25);
        add(vaccineNameLabel);

        vaccineNameField = new JTextField();
        vaccineNameField.setBounds(221, 242, 200, 25);
        vaccineNameField.setEditable(false);  // Non-editable
        add(vaccineNameField);

        JLabel doseNumberLabel = new JLabel("Dose Number:");
        doseNumberLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        doseNumberLabel.setBounds(45, 292, 100, 25);
        add(doseNumberLabel);

        doseNumberComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4});
        doseNumberComboBox.setBounds(221, 292, 200, 25);
        add(doseNumberComboBox);

        JLabel dateAdministeredLabel = new JLabel("Date Administered:");
        dateAdministeredLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        dateAdministeredLabel.setBounds(45, 342, 150, 25);
        add(dateAdministeredLabel);

        dateAdministeredChooser = new JDateChooser();
        dateAdministeredChooser.setDateFormatString("yyyy-MM-dd");  // Setting the date format
        dateAdministeredChooser.setBounds(221, 342, 200, 25);
        add(dateAdministeredChooser);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        statusLabel.setBounds(45, 392, 100, 25);
        add(statusLabel);

        // Combo box for status
        String[] statuses = {"Scheduled", "Completed", "Cancelled"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setBounds(221, 392, 200, 25);
        add(statusComboBox);

        JButton updateButton = new JButton("Update");
        updateButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        updateButton.setBounds(129, 503, 149, 49);
        updateButton.setBackground(new Color(128, 255, 255));
        add(updateButton);

        // Table for updated records
        updatedRecordsTableModel = new DefaultTableModel(new String[]{"Patient Name", "Vaccine Name", "Dose Number", "Date Administered", "Status"}, 0);
        updatedRecordsTable = new JTable(updatedRecordsTableModel);
        JScrollPane scrollPane = new JScrollPane(updatedRecordsTable);
        scrollPane.setBounds(465, 204, 765, 504);
        add(scrollPane);

        // Search button action
        searchButton.addActionListener(e -> searchPatient());

        // Update button action
        updateButton.addActionListener(e -> updateVaccinationStatus());

        loadUpdatedRecords();  // Load existing records into the table
    }

    private void searchPatient() {
        String searchQuery = searchField.getText();
        
        if (searchQuery.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter the patient ID or name.");
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");

                // Query to fetch patient and vaccine details only if the patient exists in the Appointments table
                String query = "SELECT p.patient_id, v.vaccine_id, p.first_name, p.last_name, v.vaccine_name, " +
                               "IFNULL(ur.dose_number, 0) AS dose_number, a.appointment_date, a.status " +
                               "FROM Patients p " +
                               "JOIN Appointments a ON p.patient_id = a.patient_id " +
                               "JOIN Vaccines v ON a.vaccine_id = v.vaccine_id " +
                               "LEFT JOIN UpdatedRecords ur ON p.patient_id = ur.patient_id AND v.vaccine_id = ur.vaccine_id " +
                               "WHERE a.patient_id = ?";

                PreparedStatement pstmt = con.prepareStatement(query);
                
                try {
                    int patientId = Integer.parseInt(searchQuery); // If search query is a number, assume it's patient ID
                    pstmt.setInt(1, patientId);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid patient ID. Please enter a valid numeric patient ID.");
                    return;
                }

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Fetch patient and related details only if the patient exists in Appointments
                    fetchedPatientId = rs.getInt("patient_id");  // Store patient ID
                    fetchedVaccineId = rs.getInt("vaccine_id");  // Store vaccine ID
                    patientNameField.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                    vaccineNameField.setText(rs.getString("vaccine_name"));
                    doseNumberComboBox.setSelectedItem(rs.getInt("dose_number"));  // Populate combo box with dose number
                    dateAdministeredChooser.setDate(rs.getDate("appointment_date"));  // Set the date chooser with the appointment date
                    statusComboBox.setSelectedItem(rs.getString("status")); // Set the status combo box
                } else {
                    JOptionPane.showMessageDialog(null, "No matching patient found in Appointments.");
                }

                rs.close();
                pstmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateVaccinationStatus() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");

            // Format the selected date from JDateChooser
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateAdministered = sdf.format(dateAdministeredChooser.getDate());

            // Insert or update the record in the UpdatedRecords table
            String query = "INSERT INTO UpdatedRecords (patient_id, vaccine_id, worker_id, dose_number, date_administered, status) " +
                           "VALUES (?, ?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE dose_number = ?, date_administered = ?, status = ?";

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, fetchedPatientId);  // Use fetched patient_id
            pstmt.setInt(2, fetchedVaccineId);  // Use fetched vaccine_id
            pstmt.setInt(3, workerId);  // Worker ID passed to the panel
            pstmt.setInt(4, (Integer) doseNumberComboBox.getSelectedItem());
            pstmt.setString(5, dateAdministered);
            pstmt.setString(6, (String) statusComboBox.getSelectedItem()); // Get status from combo box

            // Set for updating existing record
            pstmt.setInt(7, (Integer) doseNumberComboBox.getSelectedItem());
            pstmt.setString(8, dateAdministered);
            pstmt.setString(9, (String) statusComboBox.getSelectedItem()); // Update status from combo box

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update the status in the Appointments table
                updateAppointmentStatus(con);
                JOptionPane.showMessageDialog(this, "Vaccination status updated successfully!");
            }

            pstmt.close();
            con.close();

            loadUpdatedRecords();  // Reload updated records after update
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAppointmentStatus(Connection con) throws SQLException {
        // Update the status in the Appointments table
        String appointmentQuery = "UPDATE Appointments SET status = ?, appointment_date = ? " +
                                   "WHERE patient_id = ? AND vaccine_id = ?";

        PreparedStatement appointmentStmt = con.prepareStatement(appointmentQuery);
        appointmentStmt.setString(1, (String) statusComboBox.getSelectedItem()); // Set new status
        appointmentStmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(dateAdministeredChooser.getDate())); // Set new appointment date
        appointmentStmt.setInt(3, fetchedPatientId); // Use fetched patient_id
        appointmentStmt.setInt(4, fetchedVaccineId); // Use fetched vaccine_id

        appointmentStmt.executeUpdate(); // Execute update
        appointmentStmt.close(); // Close the statement
    }

    private void loadUpdatedRecords() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");

            String query = "SELECT p.first_name, p.last_name, v.vaccine_name, ur.dose_number, ur.date_administered, ur.status " +
                    "FROM UpdatedRecords ur " +
                    "JOIN Patients p ON ur.patient_id = p.patient_id " +
                    "JOIN Vaccines v ON ur.vaccine_id = v.vaccine_id " +
                    "WHERE ur.worker_id = ?";  // Fetch records for the specific worker

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, workerId);  // Set worker ID
            ResultSet rs = pstmt.executeQuery();

            updatedRecordsTableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("vaccine_name"),
                        rs.getInt("dose_number"),
                        rs.getString("date_administered"),
                        rs.getString("status")
                };
                updatedRecordsTableModel.addRow(row);
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

