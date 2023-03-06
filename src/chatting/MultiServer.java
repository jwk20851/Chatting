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
		li = Collections.synchronizedList(new ArrayList<String>());		//���� Ŭ���̾�Ʈ �����
		System.out.println("[���� ����]");
	}
	
	public static void main(String[] args) {
		MultiServer multiServer = new MultiServer();
		multiServer.start();
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {	//.accept() ����ó��
			serverSocket = new ServerSocket(1234);
			while (true) {
				System.out.println("[Ŭ���̾�Ʈ ���� �����]");
				socket = serverSocket.accept();
				
				//Ŭ���̾�Ʈ�� ������ ������ ���ο� ������ ����
				ReceiveThread receiveThread = new ReceiveThread(socket);
				receiveThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {	//.close() ����ó��
					serverSocket.close();
					System.out.println("[���� ����]");
				} catch (IOException e) {	
					e.printStackTrace();
					System.out.println("[�������� ��ſ���]");
				}
			}
		}
	}

	public synchronized static void addClient(String name) {
		li.add(name);
		System.out.println("[" + name + " ����. ���� �ο� " + li.size() + "��]");
	}
	
	public synchronized static void removeClient(String name) {
		li.remove(name);
		System.out.println("[" + name + " ����. ���� �ο� " + li.size() + "��]");
	}
}