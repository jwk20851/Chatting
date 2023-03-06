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
			Collections.synchronizedList(new ArrayList<PrintWriter>());	//채팅용, 멀티 스레드 환경에서 데이터 안전성을 보장
	
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
		
		try {	//.readLine() 예외처리
			// 최초1회는 client이름을 수신
			name = br.readLine();
			sendAll("[" + name + "]님이 들어오셨습니다.");
			MultiServer.addClient(name);	//입장 유저 확인
			while (br != null) {
				String inputMsg = br.readLine();
				if("종료".equals(inputMsg))
					break;
				sendAll(name + ">> " + inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[" + name + " 접속 끊김]");
		} finally {
			sendAll("[" + name + "]님이 퇴장하였습니다.");
			list.remove(pw);
			try {	//.close() 예외처리
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MultiServer.removeClient(name);	//퇴장 유저 확인
	}
	
	private void sendAll(String str) {
		for(PrintWriter pw: list) {
			pw.println(str);
			pw.flush();
		}
	}
}