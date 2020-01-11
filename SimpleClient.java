import java.net.*;
import java.util.Scanner;
import java.io.*;
public class SimpleClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scn = new Scanner(System.in);
		System.out.println("What would you like to be called?");
		String name = scn.nextLine();
		System.out.println("What is your e-mail address?");
		String email = scn.nextLine();
		//String name = scn.nextLine();
		//Socket s1 = new Socket("localhost", 1254);
		Socket s1 = new Socket("10.1.10.74",5056);
		InputStream s1In = s1.getInputStream();
		DataInputStream dis = new DataInputStream(s1In);
		DataOutputStream  dos = new DataOutputStream(s1.getOutputStream());
		dos.writeUTF("NAME:"+name); //send identity
		dos.writeUTF("EMAIL: "+email);
		String st = new String(dis.readUTF());
		System.out.println(st);
		
		while (true) 
		{
			
			String st1 = new String(dis.readUTF());
			System.out.println(st1);
			String tosend = scn.nextLine();
			dos.writeUTF(tosend);
			if(tosend.equals("Exit")) {
				System.out.println("Closing this connection : "+s1);
				s1.close();
				System.out.println("Connection closed");
				break;
			}
			String received = dis.readUTF();
			System.out.println(received);
		}
		
		System.out.println("Exit");
		scn.close();
		dis.close();
		dos.close();
		
		

	}

}
