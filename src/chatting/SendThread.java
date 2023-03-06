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

 		try {	//.getOutputStream() 예외처리
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(name);	//최초 1회는 클라이언트 이름을 서버에 전송
			pw.flush();
			
			while (true) {
				String outputMsg = sc.nextLine();
				pw.println(outputMsg);
				pw.flush();
				if("종료".equals(outputMsg))
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
