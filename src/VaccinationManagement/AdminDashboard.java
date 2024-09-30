package VaccinationManagement;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;

public class AdminDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JPanel panel;

    public static void main(String[] args) {
        AdminDashboard frame = new AdminDashboard();
    }

    public AdminDashboard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1920, 1080);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        

        panel = new JPanel();
        panel.setBounds(267, 44, 1273, 801);
        panel.setBackground(new Color(82, 184, 252));
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel panelBackgroundImage = new JLabel("");
        panelBackgroundImage.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\admin_dashboard2.jpg"));
        panelBackgroundImage.setBounds(-150, 0, 1658, 1012);
        panel.add(panelBackgroundImage);
        
        JPanel sideBackground = new JPanel();
        sideBackground.setBounds(0, 0, 267, 845);
        contentPane.add(sideBackground);
        sideBackground.setLayout(null);
                
                JLabel lblNewLabel = new JLabel("Admin");
                lblNewLabel.setForeground(new Color(255, 0, 0));
                lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 60));
                lblNewLabel.setBounds(0, 36, 267, 92);
                sideBackground.add(lblNewLabel);
                
                JButton logoutButton = new JButton("LOG OUT");
//                contentPane.add(logoutButton);
                logoutButton.setBounds(25, 622, 213, 39);
                sideBackground.add(logoutButton);
                logoutButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		new Login();
                		dispose();
                	}
                });
                logoutButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
                
                        JButton viewappointmentButton = new JButton("Appointments");
                        viewappointmentButton.setBounds(25, 477, 213, 39);
                        sideBackground.add(viewappointmentButton);
                        viewappointmentButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppointmentManagementFrame appointmentPane = new AppointmentManagementFrame();
                                panel.removeAll();

                               
                                panel.add(appointmentPane);

                                // Refresh the panel 
                                panel.revalidate();
                                panel.repaint();
							
			
			}
                        
		});
                        viewappointmentButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        
                                JButton managecentersButton = new JButton("Centers");
                                managecentersButton.setBounds(25, 333, 213, 39);
                                sideBackground.add(managecentersButton);
                                managecentersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CenterManagementFrame centerPane = new CenterManagementFrame();
                                        panel.removeAll();

                                       
                                        panel.add(centerPane);

                                        // Refresh the panel 
                                        panel.revalidate();
                                        panel.repaint();
				
			}
		});
                                managecentersButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                
                                        JButton manageworkersButton = new JButton("Workers");
                                        manageworkersButton.setBounds(25, 406, 213, 39);
                                        sideBackground.add(manageworkersButton);
                                        manageworkersButton.addActionListener(new ActionListener() {
                                        	public void actionPerformed(ActionEvent e) {
                                        		WorkerManagementFrame workerPane = new WorkerManagementFrame();
                                                panel.removeAll();

                                               
                                                panel.add(workerPane);

                                                // Refresh the panel 
                                                panel.revalidate();
                                                panel.repaint();
                                        		
                                        	}
                                        });
                                        manageworkersButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                        
                                                JButton managepatientsButton = new JButton("Patients");
                                                managepatientsButton.setBounds(25, 255, 213, 39);
                                                sideBackground.add(managepatientsButton);
                                                managepatientsButton.addActionListener(new ActionListener() {
                                                	public void actionPerformed(ActionEvent e) {
                                                		loadPatientsManagementPanel();

                                                	}
                                                });
                                                managepatientsButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                                
                                                        JButton managevaccineButton = new JButton("Vaccines");
                                                        managevaccineButton.setBounds(25, 178, 213, 39);
                                                        sideBackground.add(managevaccineButton);
                                                        managevaccineButton.addActionListener(new ActionListener() {
                                                            public void actionPerformed(ActionEvent e) {
                                                                loadVaccineManagementPanel();
                                                                
                                                            }
                                                        });
                                                        managevaccineButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                                        
                                                                JButton viewreportButton = new JButton("Reports");
                                                                viewreportButton.setBounds(25, 550, 213, 39);
                                                                sideBackground.add(viewreportButton);
                                                                viewreportButton.addActionListener(new ActionListener() {
                                                                	public void actionPerformed(ActionEvent e) {
                                                                		AppointmentReport appointmentPane = new AppointmentReport();
                                                                        panel.removeAll();

                                                                       
                                                                        panel.add(appointmentPane);

                                                                        // Refresh the panel 
                                                                        panel.revalidate();
                                                                        panel.repaint();
                                                                		
                                                                	}
                                                                });
                                                                viewreportButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
                                                                
                                                                        JLabel sidebarImage = new JLabel("");
                                                                        sidebarImage.setBounds(0, -25, 1540, 870);
                                                                        sidebarImage.setBackground(new Color(57, 173, 251));
                                                                        sidebarImage.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\login solid.png"));
                                                                        sideBackground.add(sidebarImage);
                                                                
                                                                JLabel lblNewLabel_1 = new JLabel("");
                                                                lblNewLabel_1.setBounds(36, -52, 1980, 897);
                                                                contentPane.add(lblNewLabel_1);
                                                                lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\login solid.png"));
                                                                lblNewLabel_1.setBackground(new Color(57, 173, 251));

        setVisible(true);
    }

    private void loadVaccineManagementPanel() {
        // Create an instance of VaccineManagementFrame2 (which extends JPanel)
        VaccineManagementFrame vaccinePane = new VaccineManagementFrame();
        vaccinePane.setBounds(0, 0, panel.getWidth(), panel.getHeight()); // Set bounds to fill the main panel
        vaccinePane.setVisible(true); // Ensure the panel is visible

        // Remove the existing content from the panel
        panel.removeAll();

        // Add the new panel
        panel.add(vaccinePane);

        // Refresh the panel to display the new content
        panel.revalidate();
        panel.repaint();
    }
    
    
    private void loadPatientsManagementPanel() {
    	
    	
    	
    	
        // Create an instance of VaccineManagementFrame2 (which extends JPanel)
        PatientsManagementFrame vaccinePane = new PatientsManagementFrame();
        vaccinePane.setBounds(0, 0, 1273, 801); // Set bounds to fill the main panel
        vaccinePane.setVisible(true); // Ensure the panel is visible

        // Remove the existing content from the panel
        panel.removeAll();

        // Add the new panel
        panel.add(vaccinePane);

        // Refresh the panel to display the new content
        panel.revalidate();
        panel.repaint();
    }
}




