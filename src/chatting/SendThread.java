package chatting;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread {
	Socket socket = null;
	String name = "";

	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);

 		try {	//.getOutputStream() ����ó��
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(name);	//���� 1ȸ�� Ŭ���̾�Ʈ �̸��� ������ ����
			pw.flush();
			
			while (true) {
				String outputMsg = sc.nextLine();
				pw.println(outputMsg);
				pw.flush();
				if("����".equals(outputMsg))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
