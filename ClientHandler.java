import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// ClientHandler class
class ClientHandler extends Thread
{
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	String identity;
	String email;
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;


	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
	{
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run()
	{
		String received;
		String toreturn;

		// Read IDENTITY
		try {
			received = dis.readUTF();
			System.out.println("Received = " + received);
			if (received.substring(0, 4).equals("NAME"))
			{
				identity = received.substring(5);
				System.out.println("Name="+identity);
				
				
				/*dos.writeUTF("Hello " + identity +"!");
				File ff = new File("D:\\Eclipse 2\\Networking\\src\\ChatClientList");
				Scanner scan = new Scanner(ff);
				FileWriter  fileWriter = new FileWriter("D:\\Eclipse 2\\Networking\\src\\ChatClientList", true);
				PrintWriter output = new PrintWriter(fileWriter);
				while (scan.hasNext()) {
					System.out.println();
				}
				
				output.println(identity);
				fileWriter.close();
				output.close();*/
			}
			received = dis.readUTF();
			if (received.substring(0, 5).equals("EMAIL"))
			{
				email = received.substring(6);
				System.out.println("E-mail="+email);
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/chatdb?"+"user=chatuser&password=chatuserpw");
			statement = connect.createStatement();
			String qry = "insert into chatdb.users values (default, " + "'"+identity +"'"+"," +"'"+email+"'"+ ")";
			System.out.println("QUERY: "+qry);
			statement.execute(qry);
			
			// Send back directory information to client
			
			String qry1 = "select * from chatdb.users";
			resultSet = statement.executeQuery(qry1);
			String userList = "";
			while(resultSet.next()) 
			{
				userList = userList + resultSet.getString("MYUSER")+",";
			}
			resultSet.close();
			statement.close();
			connect.close();
			dos.writeUTF(userList);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while (true)
		{
			try {

				// Ask user what he wants
				dos.writeUTF("What do you want?[Date | Time | Chat]..\n"+
						"Type Exit to terminate connection.");

				// receive the answer from client
				received = dis.readUTF();

				if(received.equals("Exit"))
				{
					System.out.println("Client " + this.s + " sends exit...");
					System.out.println("Closing this connection.");
					this.s.close();
					System.out.println("Connection closed");
					break;
				}

				// creating Date object
				Date date = new Date();

				// write on output stream based on the
				// answer from the client
				switch (received) {

				case "Date" :
					toreturn = fordate.format(date);
					dos.writeUTF(toreturn);
					break;

				case "Time" :
					toreturn = fortime.format(date);
					dos.writeUTF(toreturn);
					break;
				case "Chat" :
					dos.writeUTF("Who would you like to chat with?");
					break;
				default:
					dos.writeUTF("Invalid input");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try
		{
			// closing resources
			this.dis.close();
			this.dos.close();

		}catch(IOException e){
			e.printStackTrace();
		}
	}
}