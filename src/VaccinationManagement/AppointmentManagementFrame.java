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
import javax.swing.JComboBox;
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

public class AppointmentManagementFrame extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField patientIdField;
    private JTextField vaccineIdField;
    private JDateChooser dateChooser;
    private JComboBox<String> statusComboBox;

    public AppointmentManagementFrame() {
        setSize(1273, 801);
        setBackground(new Color(82, 184, 252));
        setLayout(null);
        setVisible(true);

        // Initialize Labels
        JLabel patientIdLabel = new JLabel("Patient ID");
        patientIdLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        patientIdLabel.setBounds(423, 91, 101, 25);
        add(patientIdLabel);

        JLabel vaccineIdLabel = new JLabel("Vaccine ID");
        vaccineIdLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        vaccineIdLabel.setBounds(423, 132, 124, 25);
        add(vaccineIdLabel);

        JLabel dateLabel = new JLabel("Apoint. Date");
        dateLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        dateLabel.setBounds(423, 170, 200, 25);
        add(dateLabel);

        JLabel statusLabel = new JLabel("Status");
        statusLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        statusLabel.setBounds(423, 212, 101, 25);
        add(statusLabel);

        // Initialize Fields
        patientIdField = new JTextField();
        patientIdField.setBounds(555, 88, 200, 28);
        add(patientIdField);
        patientIdField.setColumns(10);

        vaccineIdField = new JTextField();
        vaccineIdField.setColumns(10);
        vaccineIdField.setBounds(555, 133, 200, 28);
        add(vaccineIdField);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setBounds(555, 170, 200, 28);
        add(dateChooser);

        // Status JComboBox
        String[] statuses = { "Scheduled", "Completed", "Cancelled" };
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setBounds(555, 209, 200, 28);
        add(statusComboBox);
        
        JLabel lblNewLabel = new JLabel("Appointment Management");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 1273, 59);
        add(lblNewLabel);

        // Table model setup
        tableModel = new DefaultTableModel(new String[]{"Appointment ID", "Patient ID", "Vaccine ID", "Appointment Date", "Status"}, 0);

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
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Fetching data from the selected row
                    int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
                    int patientId = (int) tableModel.getValueAt(selectedRow, 1);
                    int vaccineId = (int) tableModel.getValueAt(selectedRow, 2);
                    String appointmentDate = (String) tableModel.getValueAt(selectedRow, 3);
                    String status = (String) tableModel.getValueAt(selectedRow, 4);

                    // Setting the fields with the fetched data
                    patientIdField.setText(String.valueOf(patientId));
                    vaccineIdField.setText(String.valueOf(vaccineId));
                    statusComboBox.setSelectedItem(status);  // Updated to use statusComboBox

                    // Set the appointment date in the date chooser
                    try {
                        java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(appointmentDate);
                        dateChooser.setDate(date);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

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
                if (patientIdField.getText().isEmpty() || 
                    vaccineIdField.getText().isEmpty() || 
                    dateChooser.getDate() == null || 
                    statusComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                String sql = "INSERT INTO Appointments (patient_id, vaccine_id, appointment_date, status) VALUES (?, ?, ?, ?)";
                java.util.Date appointmentDate = dateChooser.getDate();
                java.sql.Date sqlDate = new java.sql.Date(appointmentDate.getTime());

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {

                    pstmt.setInt(1, Integer.parseInt(patientIdField.getText()));
                    pstmt.setInt(2, Integer.parseInt(vaccineIdField.getText()));
                    pstmt.setDate(3, sqlDate);
                    pstmt.setString(4, (String) statusComboBox.getSelectedItem());

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment added successfully!");
                        loadAppointmentData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Insertion failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
                }
            }
        });

        // Modify Button
        JButton modifyButton = new JButton("Modify");
        modifyButton.setBackground(new Color(128, 255, 255));
        modifyButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        modifyButton.setBounds(517, 444, 149, 49);
        add(modifyButton);

        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (patientIdField.getText().isEmpty() || 
                    vaccineIdField.getText().isEmpty() || 
                    dateChooser.getDate() == null || 
                    statusComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                int appointmentId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                String sql = "UPDATE Appointments SET patient_id = ?, vaccine_id = ?, appointment_date = ?, status = ? WHERE appointment_id = ?";
                java.util.Date appointmentDate = dateChooser.getDate();
                java.sql.Date sqlDate = new java.sql.Date(appointmentDate.getTime());

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {

                    pstmt.setInt(1, Integer.parseInt(patientIdField.getText()));
                    pstmt.setInt(2, Integer.parseInt(vaccineIdField.getText()));
                    pstmt.setDate(3, sqlDate);
                    pstmt.setString(4, (String) statusComboBox.getSelectedItem());
                    pstmt.setInt(5, appointmentId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment updated successfully!");
                        loadAppointmentData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
                }
            }
        });

        // Delete Button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 128, 128));
        deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        deleteButton.setBounds(721, 444, 149, 49);
        add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an appointment to delete.");
                    return;
                }

                int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
                String sql = "DELETE FROM Appointments WHERE appointment_id = ?";
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {

                    pstmt.setInt(1, appointmentId);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment deleted successfully!");
                        loadAppointmentData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Delete failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
                }
            }
        });

        // Load existing appointment data
        loadAppointmentData();
    }

    // Method to load appointment data from the database into the table
    private void loadAppointmentData() {
        // Clear previous data
        tableModel.setRowCount(0);

        // Query to fetch appointment data
        String sql = "SELECT appointment_id, patient_id, vaccine_id, appointment_date, status FROM Appointments";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                int patientId = rs.getInt("patient_id");
                int vaccineId = rs.getInt("vaccine_id");
                String appointmentDate = rs.getString("appointment_date");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{appointmentId, patientId, vaccineId, appointmentDate, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

