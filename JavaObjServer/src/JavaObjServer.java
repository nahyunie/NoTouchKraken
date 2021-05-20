
//JavaObjServer.java ObjectStream 기반 채팅 Server

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

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private Vector UserRoleVec = new Vector();

	private Vector[] cardVec = { new Vector(), new Vector(), new Vector(), new Vector() };

	private int cardnum = 20;
	
	private int turnUser;
	private int turn=0;
	private int round=0;
	
	private int checkEmptybox=0;
	private int checkGoldbox=0;
	private int checkKrakenbox=0;
	

	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

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
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("id = " + msg.getId() + "\n");
		textArea.append("data = " + msg.getData() + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
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
			// 매개변수로 넘어온 자료 저장
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
//				UserStatus = "O"; // Online 상태
//				Login();
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			WriteOne("Welcome to Java chat server\n");
			WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
			WriteOthers(msg); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
		}

		public void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
			}
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
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
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
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
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
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
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
				WriteAll("참가자가 부족하여 게임을 시작할 수 없습니다.");
			else {
				int vl = (int) Math.random() * 4 + 1;
				UserRoleVec.set(vl, "Villain");
				for(int i=0;i<UserRoleVec.size();i++) {
					if(UserRoleVec.get(i).equals("Villain")) {
						UserService Vl_user = (UserService) user_vc.elementAt(i);
						if (Vl_user.UserStatus == "O") {
							Vl_user.WriteOne("당신은 스켈레톤입니다.");
						}
					}
					else {
						UserService user = (UserService) user_vc.elementAt(i);
						if (user.UserStatus == "O")
							user.WriteOne("당신은 탐험대입니다.");
					}		
				}
				for(int i=0;i<user_vc.size();i++) {
					UserService user = (UserService) user_vc.elementAt(i);
					userlist.add(user.UserName);
				}
				ChatMsg m = new ChatMsg("Server","604","사용자 리스트");
				m.userList = userlist;
				WriteAllObject(m);
				CardShare();
			}
		}

		public void CardShare() {
			// 0-> 문어, 1~5 보물, 6~19 빈상자
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
					String msg = "당신은 크라켄 카드 " + kraken + "장, 보물상자 카드 " + gold + "장, 빈 상자 카드 " + empty + "장을 가지고 있습니다.";
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
			WriteAll(firstuser.UserName+"이(가) 첫번째 차례입니다.");
		}
		
		public void CardReShare() {
			turn=0; // turn 원 위치
			cardnum-=4; //카드 뽑힌만큼 빼줌
			round++; //라운드 횟수 올림
			
			for(int i=0;i<cardVec.length;i++) //카드 초기화
				cardVec[i].clear(); 
			
			int[] nums = new int[cardnum]; //랜덤 카드 번호 저장할 배열
			int insertNum=0;
			int kraken=0;
			int gold = 0;
			int empty = 0;
			
			int leftKraken = 1-checkKrakenbox;
			int leftGold = 4-checkGoldbox;
			int leftEmpty = 15-checkEmptybox;
			
			Random random = new Random();
			
			WriteAll("5초 뒤에 카드가 재분배됩니다.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			} //5초동안 해당 라운드에 뽑힌 카드들 보여주기 위함
			

			if(round==1) { //2라운드
				ChatMsg msg = new ChatMsg("Server", "701", "2라운드 카드 원위치");
				WriteAllObject(msg);
				
			}
			else if(round==2) { //3라운드
				ChatMsg msg = new ChatMsg("Server", "702", "3라운드 카드 원위치");
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
					String msg = "당신은 크라켄 카드 " + kraken + "장, 보물상자 카드 " + gold + "장, 빈 상자 카드 " + empty + "장을 가지고 있습니다.";
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
			WriteAll("크라켄 카드를 발견했습니다. 스켈레톤의 승리입니다!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			WriteAll("게임을 종료하여주십시오.");
			ChatMsg msg = new ChatMsg("Server", "801", "스켈레톤 승리");
			WriteAllObject(msg);
		}
		
		public void userAllTurn() {
			WriteAll("모든 라운드가 끝났습니다. 스켈레톤의 승리입니다!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
			WriteAll("게임을 종료하여주십시오.");
			ChatMsg msg = new ChatMsg("Server", "801", "스켈레톤 승리");
			WriteAllObject(msg);
		}
		
		public void findAllGold() {
			WriteAll("모든 보물을 찾았습니다. 탐험대의 승리입니다!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			WriteAll("게임을 종료하여주십시오.");
			ChatMsg msg = new ChatMsg("Server", "802", "탐험대 승리");
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
				WriteAll(user.UserName+"의 차례입니다.");
				WriteAllObject(m);
				
				whatIsCard = (Integer)cardVec[0].get(realCardNum-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			} 
			else if(realCardNum>5 && realCardNum<11) {
				UserService user = (UserService) user_vc.elementAt(1);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"의 차례입니다.");
				WriteAllObject(m);
				
				if(realCardNum==10)
					whatIsCard = (Integer)cardVec[1].get(cardVec[1].size()-1);
				else
					whatIsCard = (Integer)cardVec[1].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			}
			else if(realCardNum>10 && realCardNum<16) {
				UserService user = (UserService) user_vc.elementAt(2);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"의 차례입니다.");
				WriteAllObject(m);
				
				if(realCardNum==15)
					whatIsCard = (Integer)cardVec[2].get(cardVec[2].size()-1);
				else
					whatIsCard = (Integer)cardVec[2].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkEmptybox++;
				} 
			}
			else if(realCardNum>15 && realCardNum<21) {
				UserService user = (UserService) user_vc.elementAt(3);
				ChatMsg m = new ChatMsg("SERVER", "603", user.UserName);
				WriteAll(user.UserName+"의 차례입니다.");
				WriteAllObject(m);
				
				if(realCardNum==20)
					whatIsCard = (Integer)cardVec[3].get(cardVec[3].size()-1);
				else
					whatIsCard = (Integer)cardVec[3].get((realCardNum%5)-1);
				if(whatIsCard==0) {
					msg = ClickedCardNum + " kraken";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkKrakenbox++;
					findKraken();
					return;
				} else if(whatIsCard>0 && whatIsCard<leftGold+1) {
					msg = ClickedCardNum + " gold";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
					WriteAllObject(obcm);
					checkGoldbox++;
				} else {
					msg = ClickedCardNum + " empty";
					ChatMsg obcm = new ChatMsg("카드 정보", "501", msg);
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
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
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
//						} // catch문 끝
//					}
//					String msg = new String(b, "euc-kr");
//					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
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
						UserStatus = "O"; // Online 상태
						Login();
					} else if (cm.getCode().matches("200")) {
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
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
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// 실제 message 부분
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to 빼고.. [귓속말] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									// user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						} else { // 일반 채팅 메시지
							UserStatus = "O";
							// WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
						}
					} else if (cm.getCode().matches("400")) { // logout message 처리
						Logout();
						break;
					} else if (cm.getCode().matches("300")) {
						WriteAllObject(cm);
					} else if (cm.getCode().matches("500")){
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
						AppendText(msg); // server 화면에 출력
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
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}

}
