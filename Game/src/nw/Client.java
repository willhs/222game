package nw;
import java.net.*;
import java.io.*;
import java.util.*;
import game.ui.window.*;
import game.world.model.*;

public class Client{
	public static void main(String[] args) throws IOException{
		if(args.length != 2){
			System.err.println("Usage: java Client host port");
			System.exit(1);
		}

		LinkedList<Integer> keyCodeQueue = new LinkedList<Integer>();
		GameWindow gw = new GameWindow(keyCodeQueue);

		Socket sock = new Socket(args[0], Integer.parseInt(args[1]));

		ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
		out.flush();

		BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
		ObjectInputStream in = new ObjectInputStream(bis);

		Object received;
		String input = "";

		BufferedReader cis = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(">>> ");

		try{
			while(true){
				if(cis.ready()){
					out.writeObject(cis.readLine());
					System.out.println("sent");
					System.out.println(">>> ");
				}
				if(bis.available() != 0){
					received = in.readObject();
					if(received instanceof String){
						System.out.println("Got: " + (String)received);
					}else if(received instanceof Room){
						System.out.println("Got room!: " + received);
					}
				}
				if(keyCodeQueue.size() != 0){
					out.writeObject("bro could you send me that room thing?");
					keyCodeQueue.poll();
				}
				Thread.sleep(500);
				System.out.println("loop");
			}
	

		}catch(ClassNotFoundException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}catch(InterruptedException e){
			System.err.println(e);
		}finally{
			out.close();
			in.close();
		}
	}
}
