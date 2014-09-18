import java.net.*;
import java.io.*;

public class Client{
	public static void main(String[] args) throws IOException{
		if(args.length != 2){
			System.err.println("Usage: java Client host port");
			System.exit(1);
		}

		Socket sock = new Socket(args[0], Integer.parseInt(args[1]));

		PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out.flush();

		String received = "";
		String input = "";

		try{
		
			while(!input.equals("quit")){
				System.out.print(">>> ");
				input = System.console().readLine();
				out.println(input);
		
				received = in.readLine();
		
				System.out.println("Got: " + received);
			}
	
			out.close();
			in.close();

		}catch(IOException e){
			System.err.println(e);
		}
	}
}
