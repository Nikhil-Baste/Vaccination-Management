package VaccinationManagement;



import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;


public class HealthcareWorkerDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JPanel panel;
	int workerId;
	
	public HealthcareWorkerDashboard(int workerId) {
		
		this.workerId = workerId;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
	        
	        
	        
	        
	        JButton logoutButton = new JButton("LOG OUT");
	        logoutButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		new Login();
	        		dispose();
	        	}
	        });
	        
	        JLabel lblNewLabel_2 = new JLabel("Worker");
	        lblNewLabel_2.setForeground(new Color(0, 0, 0));
	        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
	        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 60));
	        lblNewLabel_2.setBounds(10, 51, 257, 107);
	        contentPane.add(lblNewLabel_2);
	        logoutButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
	        logoutButton.setBounds(27, 563, 200, 50);
	        contentPane.add(logoutButton);
		
	        

	        panel = new JPanel();
	        panel.setBounds(267, 44, 1273, 801);
	        panel.setBackground(new Color(82, 184, 252));
	        contentPane.add(panel);
	        panel.setLayout(null);
	        
	        JLabel panelBackgroundImage = new JLabel("");
	        panelBackgroundImage.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\worker.jpg"));
	        panelBackgroundImage.setBounds(0, 0, 1658, 1012);
	        panel.add(panelBackgroundImage);


	        JButton statusButton = new JButton("Status");
	        statusButton.setBounds(27, 290, 200, 50);
	        statusButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		loadVaccinationStatusUpdateFrame(workerId);

	        	}
	        });
	        contentPane.add(statusButton);
	        statusButton.setFont(new Font("Times New Roman", Font.BOLD, 20));



	        setVisible(true);
	    }

	    
	    private void loadVaccinationStatusUpdateFrame(int worker_id) {
	    	
	    	
	    	
	    	VaccinationStatusUpdateFrame vaccinePane = new VaccinationStatusUpdateFrame(worker_id);
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
	    
	    public static void main(String[] args) {
	    	int workerId = 1;
			
			
			HealthcareWorkerDashboard frame1 = new HealthcareWorkerDashboard(workerId);
			
		
}
}
		

	

