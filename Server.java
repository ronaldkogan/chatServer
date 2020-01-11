import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;


public class Server
{

	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner(System.in);

		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(5056);
		System.out.println("Starting server at port 5056");
		// running infinite loop for getting
		// client request
		System.out.println("Type Exit to close the server. ");
		while (true)
		{
			/*if (scan.hasNext())
if(scan.nextLine().equals("Exit")) {
break;
}
			 */
			Socket s = null;

			try
			{
				// socket object to receive incoming client requests
				s = ss.accept();

				System.out.println("A new client is connected : " + s);

				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				Thread t = new ClientHandler(s, dis, dos);

				// Invoking the start() method
				t.start();

			}
			catch (Exception e){
				s.close();
				e.printStackTrace();
			}
		}
	}
}