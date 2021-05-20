
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;

public class JavaObjClientView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	// private JTextArea textArea;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private ImageIcon emptybox = new ImageIcon("image/emptybox.jpg");
	private ImageIcon goldbox = new ImageIcon("image/goldbox.jpg");
	private ImageIcon krakenbox = new ImageIcon("image/krakenbox.jpg");
	private ImageIcon cardback = new ImageIcon("src/backcard.jpg");


	private JPanel U1_CardSet;
	private JPanel U1_C1;
	private JPanel U1_C2;
	private JPanel U1_C3;
	private JPanel U1_C4;
	private JPanel U1_C5;
	private JLabel U1_C1_img;
	private JLabel U1_C2_img;
	private JLabel U1_C3_img;
	private JLabel U1_C4_img;
	private JLabel U1_C5_img;
	
	private JPanel U2_CardSet;
	private JPanel U2_C1;
	private JPanel U2_C2;
	private JPanel U2_C3;
	private JPanel U2_C4;
	private JPanel U2_C5;
	private JLabel U2_C1_img;
	private JLabel U2_C2_img;
	private JLabel U2_C3_img;
	private JLabel U2_C4_img;
	private JLabel U2_C5_img;
	
	private JPanel U3_CardSet;
	private JPanel U3_C1;
	private JPanel U3_C2;
	private JPanel U3_C3;
	private JPanel U3_C4;
	private JPanel U3_C5;
	private JLabel U3_C1_img;
	private JLabel U3_C2_img;
	private JLabel U3_C3_img;
	private JLabel U3_C4_img;
	private JLabel U3_C5_img;
	
	private JPanel U4_CardSet;
	private JPanel U4_C1;
	private JPanel U4_C2;
	private JPanel U4_C3;
	private JPanel U4_C4;
	private JPanel U4_C5;
	private JLabel U4_C1_img;
	private JLabel U4_C2_img;
	private JLabel U4_C3_img;
	private JLabel U4_C4_img;
	private JLabel U4_C5_img;
	
	private JLabel lbUser1Name;
	private JLabel lbUser2Name;
	private JLabel lbUser3Name;
	private JLabel lbUser4Name;
	
	private List<JLabel> AllCard = new ArrayList<JLabel>(20);
	
	private JButton btnStart;	
	private String thisturn;
	
	private JLabel lbfindGoldNum;
	
	private String myId=null;
	
	private int findgold=0;
	
	/**
	 * Create the frame.
	 */
	public JavaObjClientView(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1096, 662);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(173, 216, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(803, 60, 267, 420);
		contentPane.add(scrollPane);
		
				textArea = new JTextPane();
				scrollPane.setViewportView(textArea);
				textArea.setEditable(true);
				textArea.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 11));

		txtInput = new JTextField();
		txtInput.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 12));
		txtInput.setBounds(803, 490, 187, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBackground(new Color(240, 255, 255));
		btnSend.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 14));
		btnSend.setBounds(1001, 490, 69, 40);
		contentPane.add(btnSend);

		lblUserName = new JLabel("Name");
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("나눔스퀘어라운드 Bold", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(803, 540, 88, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		lblUserName.setText(username);
		
		btnStart = new JButton("Start");
		btnStart.setBackground(new Color(240, 255, 255));
		btnStart.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 12));
		btnStart.setBounds(899, 540, 91, 40);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "600", "GameStart");
				SendObject(msg);
			}
		});
		contentPane.add(btnStart);
		
		JButton btnNewButton = new JButton("종 료");
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setBackground(new Color(240, 255, 255));
		btnNewButton.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(1001, 540, 69, 40);
		contentPane.add(btnNewButton);
		
		U1_CardSet = new JPanel();
		U1_CardSet.setBackground(Color.WHITE);
		U1_CardSet.setBounds(12, 10, 377, 262);
		contentPane.add(U1_CardSet);
		U1_CardSet.setLayout(null);
		
		U1_C1 = new JPanel();
		U1_C1.setBackground(new Color(255, 255, 255));
		U1_C1.setBounds(12, 10, 84, 117);
		U1_CardSet.add(U1_C1);	
		
		U1_C1_img = new JLabel("<cardImg>");
		U1_C1_img.setForeground(Color.LIGHT_GRAY);
		U1_C1_img.setFont(U1_C1_img.getFont().deriveFont(U1_C1_img.getFont().getSize() - 12f));
		U1_C1_img.setText("1");
		U1_C1_img.setIcon(cardback);
		U1_C1.add(U1_C1_img);
		U1_C1_img.addMouseListener(new CardTouchListener());
		AllCard.add(U1_C1_img);
		
		U1_C2 = new JPanel();
		U1_C2.setBackground(new Color(255, 255, 255));
		U1_C2.setBounds(141, 10, 84, 117);
		U1_CardSet.add(U1_C2);
		
		U1_C2_img = new JLabel("<cardImg>");
		U1_C2_img.setFont(U1_C2_img.getFont().deriveFont(U1_C2_img.getFont().getSize() - 12f));
		U1_C2_img.setText("2");
		U1_C2_img.setIcon(cardback);
		U1_C2.add(U1_C2_img);
		U1_C2_img.addMouseListener(new CardTouchListener());
		AllCard.add(U1_C2_img);
		
		
		U1_C3 = new JPanel();
		U1_C3.setBackground(new Color(255, 255, 255));
		U1_C3.setBounds(281, 10, 84, 117);
		U1_CardSet.add(U1_C3);
		
		U1_C3_img = new JLabel("<cardImg>");
		U1_C3_img.setFont(U1_C3_img.getFont().deriveFont(U1_C3_img.getFont().getSize() - 12f));
		U1_C3_img.setText("3");
		U1_C3_img.setIcon(cardback);
		U1_C3.add(U1_C3_img);
		U1_C3_img.addMouseListener(new CardTouchListener());
		AllCard.add(U1_C3_img);
		
		U1_C4 = new JPanel();
		U1_C4.setBackground(new Color(255, 255, 255));
		U1_C4.setBounds(70, 135, 84, 117);
		U1_CardSet.add(U1_C4);
		
		U1_C4_img = new JLabel("<cardImg>");
		U1_C4_img.setFont(U1_C4_img.getFont().deriveFont(U1_C4_img.getFont().getSize() - 12f));
		U1_C4_img.setText("4");
		U1_C4_img.setIcon(cardback);
		U1_C4.add(U1_C4_img);
		U1_C4_img.addMouseListener(new CardTouchListener());
		AllCard.add(U1_C4_img);
		
		U1_C5 = new JPanel();
		U1_C5.setBackground(new Color(255, 255, 255));
		U1_C5.setBounds(213, 135, 84, 117);
		U1_CardSet.add(U1_C5);
		
		U1_C5_img = new JLabel("<cardImg>");
		U1_C5_img.setFont(U1_C5_img.getFont().deriveFont(U1_C5_img.getFont().getSize() - 12f));
		U1_C5_img.setText("5");
		U1_C5_img.setIcon(cardback);
		U1_C5.add(U1_C5_img);
		U1_C5_img.addMouseListener(new CardTouchListener());
		AllCard.add(U1_C5_img);
		
		U2_CardSet = new JPanel();
		U2_CardSet.setBackground(Color.WHITE);
		U2_CardSet.setBounds(414, 10, 377, 262);
		contentPane.add(U2_CardSet);
		U2_CardSet.setLayout(null);
		
		U2_C1 = new JPanel();
		U2_C1.setBackground(new Color(255, 255, 255));
		U2_C1.setBounds(12, 10, 84, 117);
		U2_CardSet.add(U2_C1);
		
		U2_C1_img = new JLabel("<cardimg>");
		U2_C1_img.setFont(U2_C1_img.getFont().deriveFont(U2_C1_img.getFont().getSize() - 12f));
		U2_C1_img.setText("6");
		U2_C1_img.setIcon(cardback);
		U2_C1.add(U2_C1_img);
		U2_C1_img.addMouseListener(new CardTouchListener());
		AllCard.add(U2_C1_img);
		
		U2_C2 = new JPanel();
		U2_C2.setBackground(new Color(255, 255, 255));
		U2_C2.setBounds(146, 10, 84, 117);
		U2_CardSet.add(U2_C2);
		
		U2_C2_img = new JLabel("<cardimg>");
		U2_C2_img.setFont(U2_C2_img.getFont().deriveFont(U2_C2_img.getFont().getSize() - 12f));
		U2_C2_img.setText("7");
		U2_C2_img.setIcon(cardback);
		U2_C2.add(U2_C2_img);
		U2_C2_img.addMouseListener(new CardTouchListener());
		AllCard.add(U2_C2_img);
		
		U2_C3 = new JPanel();
		U2_C3.setBackground(new Color(255, 255, 255));
		U2_C3.setBounds(281, 10, 84, 117);
		U2_CardSet.add(U2_C3);
		
		U2_C3_img = new JLabel("<cardimg>");
		U2_C3_img.setFont(U2_C3_img.getFont().deriveFont(U2_C3_img.getFont().getSize() - 12f));
		U2_C3_img.setText("8");
		U2_C3_img.setIcon(cardback);
		U2_C3.add(U2_C3_img);
		U2_C3_img.addMouseListener(new CardTouchListener());
		AllCard.add(U2_C3_img);
		
		U2_C4 = new JPanel();
		U2_C4.setBackground(new Color(255, 255, 255));
		U2_C4.setBounds(81, 135, 84, 117);
		U2_CardSet.add(U2_C4);
		
		U2_C4_img = new JLabel("<cardimg>");
		U2_C4_img.setFont(U2_C4_img.getFont().deriveFont(U2_C4_img.getFont().getSize() - 12f));
		U2_C4_img.setText("9");
		U2_C4_img.setIcon(cardback);
		U2_C4.add(U2_C4_img);
		U2_C4_img.addMouseListener(new CardTouchListener());
		AllCard.add(U2_C4_img);
		
		U2_C5 = new JPanel();
		U2_C5.setBackground(new Color(255, 255, 255));
		U2_C5.setBounds(223, 137, 84, 117);
		U2_CardSet.add(U2_C5);
		
		U2_C5_img = new JLabel("<cardimg>");
		U2_C5_img.setFont(U2_C5_img.getFont().deriveFont(U2_C5_img.getFont().getSize() - 12f));
		U2_C5_img.setText("10");
		U2_C5_img.setIcon(cardback);
		U2_C5.add(U2_C5_img);
		U2_C5_img.addMouseListener(new CardTouchListener());
		AllCard.add(U2_C5_img);
		
		U3_CardSet = new JPanel();
		U3_CardSet.setBackground(Color.WHITE);
		U3_CardSet.setBounds(12, 318, 377, 262);
		contentPane.add(U3_CardSet);
		U3_CardSet.setLayout(null);
		
		U3_C1 = new JPanel();
		U3_C1.setBackground(new Color(255, 255, 255));
		U3_C1.setBounds(12, 10, 84, 117);
		U3_CardSet.add(U3_C1);
		
		U3_C1_img = new JLabel("<cardimg>");
		U3_C1_img.setFont(U3_C1_img.getFont().deriveFont(U3_C1_img.getFont().getSize() - 12f));
		U3_C1_img.setText("11");
		U3_C1_img.setIcon(cardback);
		U3_C1.add(U3_C1_img);
		U3_C1_img.addMouseListener(new CardTouchListener());
		AllCard.add(U3_C1_img);
		
		U3_C2 = new JPanel();
		U3_C2.setBackground(new Color(255, 255, 255));
		U3_C2.setBounds(147, 10, 84, 117);
		U3_CardSet.add(U3_C2);
		
		U3_C2_img = new JLabel("<cardimg>");
		U3_C2_img.setFont(U3_C2_img.getFont().deriveFont(U3_C2_img.getFont().getSize() - 12f));
		U3_C2_img.setText("12");
		U3_C2_img.setIcon(cardback);
		U3_C2.add(U3_C2_img);
		U3_C2_img.addMouseListener(new CardTouchListener());
		AllCard.add(U3_C2_img);
		
		U3_C3 = new JPanel();
		U3_C3.setBackground(new Color(255, 255, 255));
		U3_C3.setBounds(281, 10, 84, 117);
		U3_CardSet.add(U3_C3);
		
		U3_C3_img = new JLabel("<cardimg>");
		U3_C3_img.setFont(U3_C3_img.getFont().deriveFont(U3_C3_img.getFont().getSize() - 12f));
		U3_C3_img.setText("13");
		U3_C3_img.setIcon(cardback);
		U3_C3.add(U3_C3_img);
		U3_C3_img.addMouseListener(new CardTouchListener());
		AllCard.add(U3_C3_img);
		
		U3_C4 = new JPanel();
		U3_C4.setBackground(new Color(255, 255, 255));
		U3_C4.setBounds(79, 135, 84, 117);
		U3_CardSet.add(U3_C4);
		
		U3_C4_img = new JLabel("<cardimg>");
		U3_C4_img.setFont(U3_C4_img.getFont().deriveFont(U3_C4_img.getFont().getSize() - 12f));
		U3_C4_img.setText("14");
		U3_C4_img.setIcon(cardback);
		U3_C4.add(U3_C4_img);
		U3_C4_img.addMouseListener(new CardTouchListener());
		AllCard.add(U3_C4_img);
		
		U3_C5 = new JPanel();
		U3_C5.setBackground(new Color(255, 255, 255));
		U3_C5.setBounds(226, 135, 84, 117);
		U3_CardSet.add(U3_C5);
		
		U3_C5_img = new JLabel("<cardimg>");
		U3_C5_img.setFont(U3_C5_img.getFont().deriveFont(U3_C5_img.getFont().getSize() - 12f));
		U3_C5_img.setText("15");
		U3_C5_img.setIcon(cardback);
		U3_C5.add(U3_C5_img);
		U3_C5_img.addMouseListener(new CardTouchListener());
		AllCard.add(U3_C5_img);
		
		U4_CardSet = new JPanel();
		U4_CardSet.setBackground(Color.WHITE);
		U4_CardSet.setBounds(414, 318, 377, 262);
		contentPane.add(U4_CardSet);
		U4_CardSet.setLayout(null);
		
		U4_C1 = new JPanel();
		U4_C1.setBackground(new Color(255, 255, 255));
		U4_C1.setBounds(12, 10, 84, 117);
		U4_CardSet.add(U4_C1);
		
		U4_C1_img = new JLabel("<cardimg>");
		U4_C1_img.setFont(U4_C1_img.getFont().deriveFont(U4_C1_img.getFont().getSize() - 12f));
		U4_C1_img.setText("16");
		U4_C1_img.setIcon(cardback);
		U4_C1.add(U4_C1_img);
		U4_C1_img.addMouseListener(new CardTouchListener());
		AllCard.add(U4_C1_img);
		
		U4_C2 = new JPanel();
		U4_C2.setBackground(new Color(255, 255, 255));
		U4_C2.setBounds(148, 10, 84, 117);
		U4_CardSet.add(U4_C2);
		
		U4_C2_img = new JLabel("<cardimg>");
		U4_C2_img.setFont(U4_C2_img.getFont().deriveFont(U4_C2_img.getFont().getSize() - 12f));
		U4_C2_img.setText("17");
		U4_C2_img.setIcon(cardback);
		U4_C2.add(U4_C2_img);
		U4_C2_img.addMouseListener(new CardTouchListener());
		AllCard.add(U4_C2_img);
		
		U4_C3 = new JPanel();
		U4_C3.setBackground(new Color(255, 255, 255));
		U4_C3.setBounds(281, 10, 84, 117);
		U4_CardSet.add(U4_C3);
		
		U4_C3_img = new JLabel("<cardimg>");
		U4_C3_img.setFont(U4_C3_img.getFont().deriveFont(U4_C3_img.getFont().getSize() - 12f));
		U4_C3_img.setText("18");
		U4_C3_img.setIcon(cardback);
		U4_C3.add(U4_C3_img);
		U4_C3_img.addMouseListener(new CardTouchListener());
		AllCard.add(U4_C3_img);
		
		U4_C4 = new JPanel();
		U4_C4.setBackground(new Color(255, 255, 255));
		U4_C4.setBounds(78, 137, 84, 117);
		U4_CardSet.add(U4_C4);
		
		U4_C4_img = new JLabel("<cardimg>");
		U4_C4_img.setFont(U4_C4_img.getFont().deriveFont(U4_C4_img.getFont().getSize() - 12f));
		U4_C4_img.setText("19");
		U4_C4_img.setIcon(cardback);
		U4_C4.add(U4_C4_img);
		U4_C4_img.addMouseListener(new CardTouchListener());
		AllCard.add(U4_C4_img);
		
		U4_C5 = new JPanel();
		U4_C5.setBackground(new Color(255, 255, 255));
		U4_C5.setBounds(216, 137, 84, 117);
		U4_CardSet.add(U4_C5);
		
		U4_C5_img = new JLabel("<cardimg>");
		U4_C5_img.setFont(U4_C5_img.getFont().deriveFont(U4_C5_img.getFont().getSize() - 12f));
		U4_C5_img.setText("20");
		U4_C5_img.setIcon(cardback);
		U4_C5.add(U4_C5_img);
		U4_C5_img.addMouseListener(new CardTouchListener());
		AllCard.add(U4_C5_img);
		

		
		lbUser1Name = new JLabel("<User1>");
		lbUser1Name.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 14));
		lbUser1Name.setBounds(12, 282, 170, 26);
		contentPane.add(lbUser1Name);
		
		lbUser2Name = new JLabel("<User2>");
		lbUser2Name.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 14));
		lbUser2Name.setBounds(414, 282, 170, 26);
		contentPane.add(lbUser2Name);
		
		lbUser3Name = new JLabel("<User3>");
		lbUser3Name.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 14));
		lbUser3Name.setBounds(12, 590, 170, 26);
		contentPane.add(lbUser3Name);
		
		lbUser4Name = new JLabel("<User4>");
		lbUser4Name.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 14));
		lbUser4Name.setBounds(414, 590, 170, 26);
		contentPane.add(lbUser4Name);
		
		JLabel lbfindGoldTitle = new JLabel("\uBC1C\uACAC\uD55C \uBCF4\uBB3C \uC0C1\uC790  :");
		lbfindGoldTitle.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 20));
		lbfindGoldTitle.setBounds(843, 24, 165, 26);
		contentPane.add(lbfindGoldTitle);
		
		lbfindGoldNum = new JLabel("0");
		lbfindGoldNum.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 22));
		lbfindGoldNum.setBounds(1011, 24, 42, 26);
		contentPane.add(lbfindGoldNum);
		


		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			//SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					// String msg = dis.readUTF();
//					byte[] b = new byte[BUF_LEN];
//					int ret;
//					ret = dis.read(b);
//					if (ret < 0) {
//						AppendText("dis.read() < 0 error");
//						try {
//							dos.close();
//							dis.close();
//							socket.close();
//							break;
//						} catch (Exception ee) {
//							break;
//						}// catch문 끝
//					}
//					String	msg = new String(b, "euc-kr");
//					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					switch (cm.getCode()) {
					case "200": // chat message
						AppendText(msg);
						break;
					case "300": // Image 첨부
						AppendText("[" + cm.getId() + "]");
						AppendImage(cm.img);
						break;
					case "501":
						String[] args = cm.getData().split(" ");
						CardReverse(args[0],args[1]);
						break;
					case "601":
						myId = cm.getData();
						btnStart.setEnabled(false);
						break;
					case "602":
						thisturn = cm.getData();
						break;
					case "603":
						thisturn = cm.getData();
						break;
					case "604":
						lbUser1Name.setText(cm.getUserList().get(0));
						lbUser2Name.setText(cm.getUserList().get(1));
						lbUser3Name.setText(cm.getUserList().get(2));
						lbUser4Name.setText(cm.getUserList().get(3));
						break;
					case "701":
						for(int i=0;i<AllCard.size();i++) {
							if(i%5==4)
								AllCard.get(i).setIcon(null);
							else
								AllCard.get(i).setIcon(cardback);
						}
						break;
					case "702":
						for(int i=0;i<AllCard.size();i++) {
							if(i%5==4 || i%5==3)
								AllCard.get(i).setIcon(null);
							else
								AllCard.get(i).setIcon(cardback);
						}
						break;
					case "801":
						for(int i=0;i<AllCard.size();i++)
							AllCard.get(i).setIcon(null);
					case "802":
						for(int i=0;i<AllCard.size();i++)
							AllCard.get(i).setIcon(null);
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}

		private void CardReverse(String cardNum, String cardInfo) {
			int realCardNum = Integer.parseInt(cardNum)-1;
			System.out.println(realCardNum);
			switch(cardInfo) {
			case "empty":
				AllCard.get(realCardNum).setIcon(emptybox);
				break;
			case "gold":
				AllCard.get(realCardNum).setIcon(goldbox);
				findgold++;
				lbfindGoldNum.setText(Integer.toString(findgold));
				break;
			case "kraken":
				AllCard.get(realCardNum).setIcon(krakenbox);
				break;
			}	
		}
	}

	class CardTouchListener extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e) {
			JLabel jl = (JLabel)e.getSource();
			int cn = Integer.parseInt(jl.getText());
			int mn = Integer.parseInt(myId);
			if(!UserName.equals(thisturn))
				return;
			if(mn==0 && cn>0 && cn<6)
				return;
			else if(mn==1 && cn<5 && cn>11)
				return;
			else if(mn==2 && cn>10 && cn<16)
				return;
			else if(mn==3 && cn>15 && cn<21)
				return;

			ChatMsg obcm = new ChatMsg(UserName, "500", jl.getText());
			SendObject(obcm);
			System.out.println(jl.getText()+" 카드누름");
		}
	}
	
	
	
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		//AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.replaceSelection(msg + "\n");
	}

	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else
			textArea.insertIcon(ori_icon);
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
