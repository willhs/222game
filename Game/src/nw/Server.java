import java.net.*;
import java.io.*;

public class Server extends Thread{
	private Socket clientSocket;

	public Server(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	public void run(){
		BufferedReader in = null;
		PrintWriter out = null;

		String received = "";

		try{	

			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			while((received = in.readLine()) != null){
				System.out.println("Got: " + received);
	
				received = received.toUpperCase();
	
				System.out.println("Returning: " + received);
				out.println(received);
			}
	
			in.close();
			out.close();

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
