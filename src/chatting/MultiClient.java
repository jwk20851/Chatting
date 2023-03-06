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
		try {	//Socket, .getInputStream(), .readLine() ����ó��
			socket = new Socket("localhost", 1234);
			System.out.println("[������ ����Ǿ����ϴ�. \"����\" �Է� �� ������ ����˴ϴ�.]");
			System.out.print("����� �г����� �Է��ϼ���: ");
			
			String name = sc.nextLine();
			Thread sendThread = new SendThread(socket, name);
			sendThread.start();

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(br != null) {
				String inputMsg = br.readLine();
				if(("[" + name + "]���� �����Ͽ����ϴ�.").equals(inputMsg))
					break;
				System.out.println(inputMsg);
			}
		} catch (IOException e) {
			System.out.println("[�������� ������ ���������ϴ�.]");
		} finally {
			try {	//.close() ����ó��
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[�������� ������ ����Ǿ����ϴ�.]");
		}
	}
}
