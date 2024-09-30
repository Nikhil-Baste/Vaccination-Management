package VaccinationManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class AppointmentReport extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;

    public AppointmentReport() {
        setSize(1273, 801);
        setBackground(new Color(82, 184, 252));
        setLayout(null);

        JLabel lblNewLabel = new JLabel("Report");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 1273, 59);
        add(lblNewLabel);

        // Search Field
        searchField = new JTextField();
        searchField.setFont(new Font("Times New Roman", Font.BOLD, 20));
        searchField.setBounds(50, 168, 200, 37);
        add(searchField);
        searchField.setToolTipText("Search by Patient Name");

        // Status Filter ComboBox
        statusFilter = new JComboBox<>(new String[]{"All", "Completed", "Pending"});
        statusFilter.setFont(new Font("Times New Roman", Font.BOLD, 20));
        statusFilter.setBounds(270, 168, 150, 37);
        add(statusFilter);

        // Button to filter records
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        searchButton.setBounds(440, 168, 100, 37);
        searchButton.setBackground(new Color(128, 255, 255));
        add(searchButton);
        searchButton.addActionListener(e -> loadUpdatedRecordData());

        // Table setup for updated vaccination records
        appointmentTableModel = new DefaultTableModel(new String[]{"Patient Name", "Vaccine Name", "Date Administered", "Status", "Worker Name"}, 0);
        appointmentTable = new JTable(appointmentTableModel);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBounds(50, 230, 1173, 500);
        add(scrollPane);

        loadUpdatedRecordData();  // Initial data load
    }

    private void loadUpdatedRecordData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");

            String patientSearch = searchField.getText().trim();
            String statusSearch = statusFilter.getSelectedItem().toString();

            // Query to fetch updated records data, including worker's name
            String query = "SELECT p.first_name, p.last_name, v.vaccine_name, ur.date_administered, ur.status, " +
                           "w.first_name AS worker_first_name, w.last_name AS worker_last_name " +
                           "FROM UpdatedRecords ur " +
                           "JOIN Patients p ON ur.patient_id = p.patient_id " +
                           "JOIN Vaccines v ON ur.vaccine_id = v.vaccine_id " +
                           "JOIN HealthcareWorkers w ON ur.worker_id = w.worker_id " +
                           "WHERE (p.first_name LIKE ? OR p.last_name LIKE ?)";

            if (!statusSearch.equals("All")) {
                query += " AND ur.status = ?";
            }

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%" + patientSearch + "%");
            pstmt.setString(2, "%" + patientSearch + "%");

            if (!statusSearch.equals("All")) {
                pstmt.setString(3, statusSearch);
            }

            ResultSet rs = pstmt.executeQuery();

            // Clear any existing rows from the table model
            appointmentTableModel.setRowCount(0);

            // Add fetched data to the table model
            while (rs.next()) {
                String patientName = rs.getString("first_name") + " " + rs.getString("last_name");
                String vaccineName = rs.getString("vaccine_name");
                String dateAdministered = rs.getString("date_administered");
                String status = rs.getString("status");
                String workerName = rs.getString("worker_first_name") + " " + rs.getString("worker_last_name");

                // Add row to table model
                appointmentTableModel.addRow(new Object[]{patientName, vaccineName, dateAdministered, status, workerName});
            }

            // Notify the JTable of data changes
            appointmentTableModel.fireTableDataChanged();

            // Clean up resources
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

