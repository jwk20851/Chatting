package chatting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class MultiServer {
	static List<String> li = null;
	
	public MultiServer() {
		li = Collections.synchronizedList(new ArrayList<String>());		//연결 클라이언트 저장용
		System.out.println("[서버 시작]");
	}
	
	public static void main(String[] args) {
		MultiServer multiServer = new MultiServer();
		multiServer.start();
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {	//.accept() 예외처리
			serverSocket = new ServerSocket(1234);
			while (true) {
				System.out.println("[클라이언트 연결 대기중]");
				socket = serverSocket.accept();
				
				//클라이언트가 접속할 때마다 새로운 스레드 생성
				ReceiveThread receiveThread = new ReceiveThread(socket);
				receiveThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {	//.close() 예외처리
					serverSocket.close();
					System.out.println("[서버 종료]");
				} catch (IOException e) {	
					e.printStackTrace();
					System.out.println("[서버소켓 통신에러]");
				}
			}
		}
	}

	public synchronized static void addClient(String name) {
		li.add(name);
		System.out.println("[" + name + " 입장. 현재 인원 " + li.size() + "명]");
	}
	
	public synchronized static void removeClient(String name) {
		li.remove(name);
		System.out.println("[" + name + " 퇴장. 현재 인원 " + li.size() + "명]");
	}
}