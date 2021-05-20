
//JavaObjServer.java ObjectStream ��� ä�� Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaObjServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private Vector UserRoleVec = new Vector();

	private Vector[] cardVec = { new Vector(), new Vector(), new Vector(), new Vector() };

	private int cardnum = 20;
	
	private int turnUser;
	private int turn=0;
	private int round=0;
	
	private int checkEmptybox=0;
	private int checkGoldbox=0;
	private int checkKrakenbox=0;
	

	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjServer frame = new JavaObjServer();
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
	public JavaObjServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
//				is = client_socket.getInputStream();
//				dis = new DataInputStream(is);
//				os = client_socket.getOutputStream();
//				dos = new DataOutputStream(os);

				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

				// line1 = dis.readUTF();
				// /login user1 ==> msg[0] msg[1]
//				byte[] b = new byte[BUF_LEN];
//				dis.read(b);		
//				String line1 = new String(b);
//
//				//String[] msg = line1.split(" ");
//				//UserName = msg[1].trim();
//				UserStatus = "O"; // Online ����
//				Login();
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("���ο� ������ " + UserName + " ����.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			WriteOthers(msg); // ���� user_vc�� ���� ������ user�� ���Ե��� �ʾҴ�.
		}

		public void Logout() {
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			WriteAll(msg); // ���� ������ �ٸ� User�鿡�� ����
			AppendText("����� " + "[" + UserName + "] ����. ���� ������ �� " + UserVec.size());
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
		public void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		// �ӼӸ� ����
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
			}
		}

		public void WriteOneObject(Object ob) {
			try {
				oos.writeObject(ob);
			} catch (IOException e) {
				AppendText("oos.writeObject(ob) error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}

		public void RoleShare() {
			List<String> userlist = new ArrayList<String>();
			if (user_vc.size() < 4)
				WriteAll("�����ڰ� �����Ͽ� ������ ������ �� �����ϴ�.");
			else {
				int vl = (int) Math.random() * 4 + 1;
				UserRoleVec.set(vl, "Villain");
				for(int i=0;i<UserRoleVec.size();i++) {
					if(UserRoleVec.get(i).equals("Villain")) {
						UserService Vl_user = (UserService) user_vc.elementAt(i);
						if (Vl_user.UserStatus == "O") {
							Vl_user.WriteOne("����� ���̷����Դϴ�.");
						}
					}
					else {
						UserService user = (UserService) user_vc.elementAt(i);
						if (user.UserStatus == "O")
							user.WriteOne("����� Ž����Դϴ�.");
					}		
				}
				for(int i=0;i<user_vc.size();i++) {
					UserService user = (UserService) user_vc.elementAt(i);
					userlist.add(user.UserName);
				}
				ChatMsg m = new ChatMsg("Server","604","����� ����Ʈ");
				m.userList = userlist;
				WriteAllObject(m);
				CardShare();
			}
		}

		public void CardShare() {
			// 0-> ����, 1~5 ����, 6~19 �����
			int kraken = 0;
			int gold = 0;
			int empty = 0;
			int[] nums = new int[cardnum];
			int insertNum=0;
			Random random = new Random();
			for(int i=0;i<cardnum;i++) {
				nums[i] = random.nextInt(cardnum);
				for(int j=0;j<i;j++) {
					if(nums[i]==nums[j]) {
						i--;
						break;
					}
				}
			}
			for(int k=0;k<cardnum;k++)
				System.out.print(nums[k]+" ");
			System.out.println(" ");
			for(int i=0;i<4;i++) {
				for(int j=0;j<(cardnum/4);j++) {
					int cardNum = nums[insertNum++];
					cardVec[i].add(cardNum);
					if(cardNum==0)
						kraken++;
					else if(cardNum>0 && cardNum<5)
						gold++;
					else {
						empty++;
					}
				}
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O") {
					String msg = "����� ũ���� ī�� " + kraken + "��, �������� ī�� " + gold + "��, �� ���� ī�� " + empty + "���� ������ �ֽ��ϴ�.";
					user.WriteOne(msg);
					ChatMsg m = new ChatMsg(user.UserName, "601", Integer.toString(i));
					user.WriteOneObject(m);
				}
				kraken = gold = empty = 0;
			}
			
			turnUser = random.nextInt(4);
			UserService firstuser = (UserService) user_vc.elementAt(turnUser);
			ChatMsg msg = new ChatMsg("Server", "602", firstuser.UserName);
			WriteAllObject(msg);
			WriteAll(firstuser.UserName+"��(��) ù��° �����Դϴ�.");
		}
		
		public void CardReShare() {
			turn=0; // turn �� ��ġ
			cardnum-=4; //ī�� ������ŭ ����
			round++; //���� Ƚ�� �ø�
			
			for(int i=0;i<cardVec.length;i++) //ī�� �ʱ�ȭ
				cardVec[i].clear(); 
			
			int[] nums = new int[cardnum]; //���� ī�� ��ȣ ������ �迭
			int insertNum=0;
			int kraken=0;
			int gold = 0;
			int empty = 0;
			
			int leftKraken = 1-checkKrakenbox;
			int leftGold = 4-checkGoldbox;
			int leftEmpty = 15-checkEmptybox;
			
			Random random = new Random();
			
			WriteAll("5�� �ڿ� ī�尡 ��й�˴ϴ�.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			} //5�ʵ��� �ش� ���忡 ���� ī��� �����ֱ� ����
			

			if(round==1) { //2����
				ChatMsg msg = new ChatMsg("Server", "701", "2���� ī�� ����ġ");
				WriteAllObject(msg);
				
			}
			else if(round==2) { //3����
				ChatMsg msg = new ChatMsg("Server", "702", "3���� ī�� ����ġ");
				WriteAllObject(msg);
			}
			else if(round==3) {
				userAllTurn();
				return;
			}
			

			for(int i=0;i<cardnum;i++) {
				nums[i] = random.nextInt(cardnum);
				for(int j=0;j<i;j++) {
					if(nums[i]==nums[j]) {
						i--;
						break;
					}
				}
			}
			
			for(int k=0;k<cardnum;k++)
				System.out.print(nums[k]+" ");
			System.out.println(" ");
			
			for(int i=0;i<4;i++) {
				for(int j=0;j<(cardnum/4);j++) {
					int cardNum = nums[insertNum++];
					cardVec[i].add(cardNum);
					if(cardNum==0)
						kraken++;
					else if(cardNum>0 && cardNum<(leftGold+1))
						gold++;
					else {
						empty++;
					}
				}
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O") {
					String msg = "����� ũ���� ī�� " + kraken + "��, �������� ī�� " + gold + "��, �� ���� ī�� " + empty + "���� ������ �ֽ��ϴ�.";
					user.WriteOne(msg);
					ChatMsg m = new ChatMsg(user.UserName, "601", Integer.toString(i));
					user.WriteOneObject(m);
				}
				kraken = gold = empty = 0;
			}
			for(int i=0;i<cardVec.length;i++) {
				for(int j=0;j<cardVec[i].size();j++)
					System.out.print(cardVec[i].get(j)+" ");
				System.out.println("");
			}
			
		}
		
		public void findKraken() {
			WriteAll("ũ���� ī�带 �߰��߽��ϴ�. ���̷����� �¸��Դϴ�!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			WriteAll("������ �����Ͽ��ֽʽÿ�.");
			ChatMsg msg = new ChatMsg("Server", "801", "���̷��� �¸�");
			WriteAllObject(msg);
		}
		
		public void userAllTurn() {
			WriteAll("��� ���尡 �������ϴ�. ���̷����� �¸��Դϴ�!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
			WriteAll("������ �����Ͽ��ֽʽÿ�.");
			ChatMsg msg = new ChatMsg("Server", "801", "���̷��� �¸�");
			WriteAllObject(msg);
		}
		
		public void findAllGold() {
			WriteAll("��� ������ ã�ҽ��ϴ�. Ž����� �¸��Դϴ�!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			WriteAll("������ �����Ͽ��ֽʽÿ�.");
			ChatMsg msg = new ChatMsg("Server", "802", "Ž��� �¸�");
			WriteAllObject(msg);
		}
		
		public void cardNumInfo(String ClickedCardNum) {
			int leftGold = 4-checkGoldbox;
			int realCardNum = Integer.parseInt(ClickedCardNum);
			String msg=null;
			int whatIsCard;
			if(realCardNum>0 && realCardNum<6) {
				UserService user = (UserService) user_vc.elementAt(0);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"�� �����Դϴ�.");
				WriteAllObject(m);
				
				whatIsCard = (Integer)cardVec[0].get(realCardNum-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			} 
			else if(realCardNum>5 && realCardNum<11) {
				UserService user = (UserService) user_vc.elementAt(1);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"�� �����Դϴ�.");
				WriteAllObject(m);
				
				if(realCardNum==10)
					whatIsCard = (Integer)cardVec[1].get(cardVec[1].size()-1);
				else
					whatIsCard = (Integer)cardVec[1].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			}
			else if(realCardNum>10 && realCardNum<16) {
				UserService user = (UserService) user_vc.elementAt(2);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"�� �����Դϴ�.");
				WriteAllObject(m);
				
				if(realCardNum==15)
					whatIsCard = (Integer)cardVec[2].get(cardVec[2].size()-1);
				else
					whatIsCard = (Integer)cardVec[2].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			}
			else if(realCardNum>15 && realCardNum<21) {
				UserService user = (UserService) user_vc.elementAt(3);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"�� �����Դϴ�.");
				WriteAllObject(m);
				
				if(realCardNum==20)
					whatIsCard = (Integer)cardVec[3].get(cardVec[3].size()-1);
				else
					whatIsCard = (Integer)cardVec[3].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("ī�� ����", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			}
			if(checkGoldbox==4)
				findAllGold();
			turn++;
			if(turn==4)
				CardReShare();
		}

		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
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
//							client_socket.close();
//							Logout();
//							break;
//						} catch (Exception ee) {
//							break;
//						} // catch�� ��
//					}
//					String msg = new String(b, "euc-kr");
//					msg = msg.trim(); // �յ� blank NULL, \n ��� ����
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if (cm.getCode().matches("100")) {
						UserName = cm.getId();
						UserStatus = "O"; // Online ����
						Login();
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
						if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
							}
							WriteOne("-----------------------------\n");
						} else if (args[1].matches("/sleep")) {
							UserStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							UserStatus = "O";
						} else if (args[1].matches("/to")) { // �ӼӸ�
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// ���� message �κ�
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to ����.. [�ӼӸ�] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // �Ϲ� ä�� �޽���
							UserStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.getCode().matches("400")) { // logout message ó��
						Logout();
						break;
					} else if (cm.getCode().matches("300")) {
						WriteAllObject(cm);
					} else if (cm.getCode().matches("500")){
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server ȭ�鿡 ���
						String[] args = msg.split(" ");
						cardNumInfo(args[1]);
						
					} else if (cm.getCode().matches("600")) {
						UserRoleVec.clear();
						for(int i=0;i<4;i++) {
							UserRoleVec.add(i, "Explorer");
						}
						RoleShare();
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // �������� ���� ��ü�� ���Ϳ��� �����
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}

}
