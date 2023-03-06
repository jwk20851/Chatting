package chatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
	public static void main(String[] args) {
		MultiClient multiClient = new MultiClient();
		multiClient.start();
	}
	
	public void start() {
		Socket socket = null;
		BufferedReader br = null;
		Scanner sc = new Scanner(System.in);
		try {	//Socket, .getInputStream(), .readLine() 예외처리
			socket = new Socket("localhost", 1234);
			System.out.println("[서버와 연결되었습니다. \"종료\" 입력 시 연결이 종료됩니다.]");
			System.out.print("사용할 닉네임을 입력하세요: ");
			
			String name = sc.nextLine();
			Thread sendThread = new SendThread(socket, name);
			sendThread.start();

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(br != null) {
				String inputMsg = br.readLine();
				if(("[" + name + "]님이 퇴장하였습니다.").equals(inputMsg))
					break;
				System.out.println(inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[서버와의 연결이 끊어졌습니다.]");
		} finally {
			try {	//.close() 예외처리
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[서버와의 연결이 종료되었습니다.]");
		}
	}
}
