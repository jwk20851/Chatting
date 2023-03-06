package chatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReceiveThread extends Thread{
	static List<PrintWriter> list = 
			Collections.synchronizedList(new ArrayList<PrintWriter>());	//ä�ÿ�, ��Ƽ ������ ȯ�濡�� ������ �������� ����
	
	Socket socket = null;
	BufferedReader br = null;
	PrintWriter pw = null;
			
	public ReceiveThread (Socket socket) {
		this.socket = socket;
		try {
			pw = new PrintWriter(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			list.add(pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void run() {
		String name = "";
		
		try {	//.readLine() ����ó��
			// ����1ȸ�� client�̸��� ����
			name = br.readLine();
			sendAll("[" + name + "]���� �����̽��ϴ�.");
			MultiServer.addClient(name);	//���� ���� Ȯ��
			while (br != null) {
				String inputMsg = br.readLine();
				if("����".equals(inputMsg))
					break;
				sendAll(name + ">> " + inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[" + name + " ���� ����]");
		} finally {
			sendAll("[" + name + "]���� �����Ͽ����ϴ�.");
			list.remove(pw);
			try {	//.close() ����ó��
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MultiServer.removeClient(name);	//���� ���� Ȯ��
	}
	
	private void sendAll(String str) {
		for(PrintWriter pw: list) {
			pw.println(str);
			pw.flush();
		}
	}
}