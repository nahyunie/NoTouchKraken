// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client

import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class JavaObjClientMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjClientMain frame = new JavaObjClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaObjClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 908, 553);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(666, 302, 82, 33);
		contentPane.add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setBounds(755, 302, 116, 33);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setForeground(Color.WHITE);
		lblIpAddress.setBounds(666, 357, 82, 33);
		contentPane.add(lblIpAddress);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtIpAddress.setText("127.0.0.1");
		txtIpAddress.setColumns(10);
		txtIpAddress.setBounds(755, 357, 116, 33);
		contentPane.add(txtIpAddress);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setForeground(Color.WHITE);
		lblPortNumber.setBounds(666, 412, 82, 33);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField();
		txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setColumns(10);
		txtPortNumber.setBounds(755, 412, 116, 33);
		contentPane.add(txtPortNumber);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(666, 455, 205, 38);
		contentPane.add(btnConnect);
		
		/*ImageIcon logo = new ImageIcon("image/krakenlogo.png");
		JLabel logoLabel = new JLabel(logo);
		logoLabel.setBounds(0,0,logo.getIconWidth(),logo.getIconHeight());
		contentPane.add(logoLabel);*/
		
		ImageIcon background = new ImageIcon("image/kraken1-1.png");
		JLabel backLabel = new JLabel(background, SwingConstants.CENTER);
		backLabel.setBounds(0,0,background.getIconWidth(),background.getIconHeight());
		contentPane.add(backLabel);

		

		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		txtIpAddress.addActionListener(action);
		txtPortNumber.addActionListener(action);
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String ip_addr = txtIpAddress.getText().trim();
			String port_no = txtPortNumber.getText().trim();
			JavaObjClientView view = new JavaObjClientView(username, ip_addr, port_no);
			setVisible(false);
		}
	}
}


