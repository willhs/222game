package nw;
import java.net.*;
import java.io.*;
import game.ui.window.*;
import game.world.model.*;
import java.util.ArrayList;
import java.awt.Polygon;

public class Server extends Thread{
	private Socket clientSocket;

	public Server(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	public void run(){
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		Object received;
		String recStr = "";

		try{

			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());

			while((received = in.readObject()) != null){
	
				if(received instanceof String){
					recStr = ((String)received);
					System.out.println("Got: " + recStr);
					if(recStr.equals("bro could you send me that room thing?")){
						out.writeObject(new Room(new ArrayList<Exit>(), new ArrayList<Item>(), new Polygon()));
					}
					else{
						recStr = recStr.toUpperCase();
	
						System.out.println("Returning: " + recStr);
						out.writeObject(recStr);
					}
				}else{
					System.out.println("No idea what this is: " + received);
				}
			}
	
			in.close();
			out.close();

		}catch(ClassNotFoundException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public static void main(String[] args) throws IOException{
		if(args.length != 1){
			System.err.println("Usage: java Server [port]");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try{
			ServerSocket serverSocket = new ServerSocket(portNumber);
			while(true){
				new Server(serverSocket.accept()).start();
			}
		}catch(IOException e){}
	}
}
