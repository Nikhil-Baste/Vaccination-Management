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

public class CenterManagementFrame extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField centerNameField;
    private JTextField locationField;
    private JTextField contactNumberField;

    public CenterManagementFrame() {
        setSize(1273, 801);
        setBackground(new Color(82, 184, 252));
        setLayout(null);

        JLabel centerNameLabel = new JLabel("Center Name");
        centerNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        centerNameLabel.setBounds(423, 88, 150, 25);
        add(centerNameLabel);

        JLabel locationLabel = new JLabel("Location");
        locationLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        locationLabel.setBounds(423, 138, 150, 25);
        add(locationLabel);

        JLabel contactNumberLabel = new JLabel("Contact No.");
        contactNumberLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        contactNumberLabel.setBounds(423, 188, 150, 25);
        add(contactNumberLabel);

        centerNameField = new JTextField();
        centerNameField.setBounds(555, 88, 200, 28);
        add(centerNameField);
        centerNameField.setColumns(10);

        locationField = new JTextField();
        locationField.setBounds(555, 139, 200, 28);
        add(locationField);
        locationField.setColumns(10);

        contactNumberField = new JTextField();
        contactNumberField.setBounds(555, 189, 200, 28);
        add(contactNumberField);
        contactNumberField.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("Center Management");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 1273, 59);
        add(lblNewLabel);

        // Table model setup
        tableModel = new DefaultTableModel(new String[]{"Center ID", "Center Name", "Location", "Contact Number"}, 0);

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
                int centerId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                String centerName = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
                String location = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
                String contactNumber = (String) tableModel.getValueAt(table.getSelectedRow(), 3);

                centerNameField.setText(centerName);
                locationField.setText(location);
                contactNumberField.setText(contactNumber);
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
                if (centerNameField.getText().isEmpty() || locationField.getText().isEmpty() || contactNumberField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                String sql = "INSERT INTO HealthCenters (center_name, location, contact_number) VALUES (?, ?, ?)";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {
                     
                    pstmt.setString(1, centerNameField.getText());
                    pstmt.setString(2, locationField.getText());
                    pstmt.setString(3, contactNumberField.getText());

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Health center added successfully!");
                        loadHealthCenterData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Insertion failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
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
            public void actionPerformed(ActionEvent e) {
                if (centerNameField.getText().isEmpty() || locationField.getText().isEmpty() || contactNumberField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                int centerId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                String sql = "UPDATE HealthCenters SET center_name = ?, location = ?, contact_number = ? WHERE center_id = ?";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {
                     
                    pstmt.setString(1, centerNameField.getText());
                    pstmt.setString(2, locationField.getText());
                    pstmt.setString(3, contactNumberField.getText());
                    pstmt.setInt(4, centerId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Health center modified successfully!");
                        loadHealthCenterData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Modification failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
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
                int centerId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                String sql = "DELETE FROM HealthCenters WHERE center_id = ?";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                     PreparedStatement pstmt = con.prepareStatement(sql)) {
                     
                    pstmt.setInt(1, centerId);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Health center deleted successfully!");
                        loadHealthCenterData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Deletion failed. Please try again.");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e1.getMessage());
                }
            }
        });

        // Load health centers on startup
        loadHealthCenterData();
    }

    private void loadHealthCenterData() {
        tableModel.setRowCount(0);

        String sql = "SELECT * FROM HealthCenters";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int centerId = rs.getInt("center_id");
                String centerName = rs.getString("center_name");
                String location = rs.getString("location");
                String contactNumber = rs.getString("contact_number");

                tableModel.addRow(new Object[]{centerId, centerName, location, contactNumber});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }
}
