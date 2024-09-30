package VaccinationManagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;

public class VaccineManagementFrame extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField manufacturerField;
    private JTextField dosageField;
    private JDateChooser expiryDateChooser;  // Replacing expiryField with JDateChooser

    public VaccineManagementFrame() {
        setSize(1273, 801);
        setBackground(new Color(82, 184, 252));
        setLayout(null);
        
        expiryDateChooser = new JDateChooser();  // Set up JDateChooser for expiry date
        expiryDateChooser.setDateFormatString("yyyy-MM-dd");
        expiryDateChooser.setBounds(555, 256, 200, 28);
        add(expiryDateChooser);
       
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        nameLabel.setBounds(423, 88, 101, 25);
        add(nameLabel);

        JLabel lblManufacturer = new JLabel("Manufacturer");
        lblManufacturer.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblManufacturer.setBounds(423, 143, 124, 25);
        add(lblManufacturer);

        JLabel lblDosage = new JLabel("Dosage");
        lblDosage.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblDosage.setBounds(423, 194, 101, 25);
        add(lblDosage);

        JLabel lblExpiry = new JLabel("Expiry");
        lblExpiry.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblExpiry.setBounds(423, 253, 101, 25);
        add(lblExpiry);

        nameField = new JTextField();
        nameField.setBounds(555, 89, 200, 28);
        add(nameField);
        nameField.setColumns(10);

        manufacturerField = new JTextField();
        manufacturerField.setColumns(10);
        manufacturerField.setBounds(555, 143, 200, 28);
        add(manufacturerField);

        dosageField = new JTextField();
        dosageField.setColumns(10);
        dosageField.setBounds(555, 194, 200, 28);
        add(dosageField);

        // Table model setup
        tableModel = new DefaultTableModel(new String[]{"Vaccine ID", "Vaccine Name", "Manufacturer", "Dosage", "Expiry Date"}, 0);

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
                String vaccinename = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
                String manufacturer = (String) tableModel.getValueAt(table.getSelectedRow(), 2);
                String dosage = (String) tableModel.getValueAt(table.getSelectedRow(), 3);
                String expiry = (String) tableModel.getValueAt(table.getSelectedRow(), 4);

                nameField.setText(vaccinename);
                manufacturerField.setText(manufacturer);
                dosageField.setText(dosage);
                
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(expiry);
                    expiryDateChooser.setDate(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 534, 1273, 267);
        add(scrollPane);

        // Add Vaccine button
        JButton addButton = new JButton("Add ");
        addButton.setBackground(new Color(128, 255, 128));
        addButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        addButton.setBounds(312, 444, 149, 49);
        add(addButton);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
                    Statement stmt = con.createStatement();

                    String expiryDate = new SimpleDateFormat("yyyy-MM-dd").format(expiryDateChooser.getDate());
                    String sql = "INSERT INTO Vaccines(vaccine_name, manufacturer, dosage, expiry_date) " +
                            "VALUES ('" + nameField.getText() + "','" + manufacturerField.getText() + "','" +
                            dosageField.getText() + "','" + expiryDate + "')";
                    stmt.executeUpdate(sql);

                    con.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                nameField.setText("");
                manufacturerField.setText("");
                dosageField.setText("");
                expiryDateChooser.setDate(null);
                loadVaccineData();
            }
        });

        JButton deleteButton = new JButton("Delete ");
        deleteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		deleteVaccineData();
        	}
        });
        deleteButton.setBackground(new Color(255, 128, 128));
        deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		deleteButton.setBounds(721, 444, 149, 49);
        add(deleteButton);
        
        JButton btnModify = new JButton("Modify");
        btnModify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				  try {
	                    Class.forName("com.mysql.cj.jdbc.Driver");
	                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
	                    Statement stmt = con.createStatement();

	                    int vaccineId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
	                    String expiryDate = new SimpleDateFormat("yyyy-MM-dd").format(expiryDateChooser.getDate());

	                    String sql = "UPDATE Vaccines SET vaccine_name= '" + nameField.getText() + 
	                                 "', manufacturer='" + manufacturerField.getText() + 
	                                 "', dosage ='" + dosageField.getText() + 
	                                 "', expiry_date='" + expiryDate + "' WHERE vaccine_id = " + vaccineId;
	                    stmt.executeUpdate(sql);

	                    con.close();
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }

	                nameField.setText("");
	                manufacturerField.setText("");
	                dosageField.setText("");
	                expiryDateChooser.setDate(null);
	                loadVaccineData();
	            }
		});
        btnModify.setBackground(new Color(128, 255, 255));
        btnModify.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnModify.setBounds(517, 444, 149, 49);
        add(btnModify);
        
        JLabel lblNewLabel = new JLabel("Vaccine Management");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 1273, 59);
        add(lblNewLabel);
        
        loadVaccineData();
    }

    private void loadVaccineData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
            Statement stmt = con.createStatement();
            String query = "SELECT vaccine_id, vaccine_name, manufacturer, dosage, expiry_date FROM Vaccines";
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);

            while (rs.next()) {
                int vaccineId = rs.getInt("vaccine_id");
                String vaccineName = rs.getString("vaccine_name");
                String manufacturer = rs.getString("manufacturer");
                String dosage = rs.getString("dosage");
                String expiryDate = rs.getString("expiry_date");
                tableModel.addRow(new Object[]{vaccineId, vaccineName, manufacturer, dosage, expiryDate});
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteVaccineData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demovaccine", "root", "root");
            Statement stmt = con.createStatement();

            int vaccineId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
            String sql = "DELETE FROM Vaccines WHERE vaccine_id = " + vaccineId;
            stmt.executeUpdate(sql);

            con.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        nameField.setText("");
        manufacturerField.setText("");
        dosageField.setText("");
        expiryDateChooser.setDate(null);  // Clear the expiry date chooser
        loadVaccineData();
    }
}








