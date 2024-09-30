package VaccinationManagement;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	public static void main(String[] args) {
		
					Login frame = new Login(); 
					
				
	}


	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Nikhil\\Pictures\\Images\\vaccination-image.png"));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0,  1920, 1080);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(207, 116, 218));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Vaccination Management");
		lblNewLabel.setBackground(UIManager.getColor("Button.background"));
		lblNewLabel.setForeground(new Color(128, 0, 64));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 65));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(156, 66, 773, 135);
		contentPane.add(lblNewLabel);
		
		JButton adminButton = new JButton("Admin");
		adminButton.setBackground(SystemColor.activeCaption);
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Admin();
				dispose();
			}
		});
		adminButton.setFont(new Font("Times New Roman", Font.BOLD, 50));
		adminButton.setBounds(381, 284, 300, 80);
		contentPane.add(adminButton);
		
		JButton userButton = new JButton("Worker");
		userButton.setBackground(SystemColor.activeCaption);
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new User();
				dispose();
			}
		});
		userButton.setFont(new Font("Times New Roman", Font.BOLD, 50));
		userButton.setBounds(381, 483, 300, 80);
		contentPane.add(userButton);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBackground(SystemColor.activeCaption);
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Nikhil\\Pictures\\Images\\vaccination-image.png"));
		lblNewLabel_1.setBounds(0, 0, 1540, 845);
		contentPane.add(lblNewLabel_1);
		
		
		setVisible(true);
		
	}
}
