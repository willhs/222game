package nw;

import game.ui.window.GameWindow;
import game.world.model.*;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import javax.swing.JFrame;

public class Client extends Thread{
	private static Server server;
	private static InputStream inStream;
	private static OutputStream outStream;
	private static GameWindow gw;
	private static Player player;

	public Client(Player player, String host, int port, GameWindow gw){
		this.gw = gw;
		this.player = player;
		try{
			Socket sock = new Socket(host, port);

			inStream = sock.getInputStream();
			outStream = sock.getOutputStream();
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public Client(Player player, GameWindow gw){ 
		this.gw = gw;
		this.player = player;
		try{
			PipedInputStream sIn = new PipedInputStream();
			PipedOutputStream sOut = new PipedOutputStream();
			inStream = new PipedInputStream(sOut);
			outStream = new PipedOutputStream(sIn);

			server = new Server(sIn, sOut);
			server.initialiseWorld();
			server.start();
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public void run(){
		ClientWorld world = null;
		Queue<String> keyCodeQueue = gw.getKeyQueue();

		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try{
			out = new ObjectOutputStream(outStream);
			out.flush();

			BufferedInputStream bis = new BufferedInputStream(inStream);
			in = new ObjectInputStream(bis);

			Object received =  null;
			Room currentRoom = null;

			while(true){
				if(bis.available() != 0){
					received = in.readObject();
					if(received instanceof String){
						System.out.println("Got: " + (String)received);
						world.applyCommand((String)received);
					}
					else if(received instanceof Room){
						System.out.println("Got room!: " + received);
						currentRoom = (Room)received;
						gw.setRoom(currentRoom);

						if(world==null){
							List<Place> placeList = new ArrayList<Place>();
							placeList.add(currentRoom);
							world = new World(placeList);
							out.writeObject(world.getSetClientPlayer(player));
						}else{
							world.replaceCurrentPlace(currentRoom);
						}
					}
					else{
						System.out.println("No idea what this is: " + received);
					}
				}

				if(keyCodeQueue.size() != 0){
					out.writeObject(keyCodeQueue.poll());
				}

				gw.repaint();
				Thread.sleep(50);
			}

		}catch(ClassNotFoundException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}catch(InterruptedException e){
			System.err.println(e);
		}finally{
			try{
				out.close();
				in.close();
			}catch(IOException e){System.err.println(e);}
		}
	}
	
	public static void main(String[] args){
		GameWindow gw = new GameWindow();
		if(args.length == 3){
			new Client(new Player(args[2]), args[0], Integer.parseInt(args[1]), gw).start();
		}else if(args.length == 0){
			new Client(new Player("TestPlayer"), gw).start();
		}else{
			System.err.println("Usage: java Client [host] [port] playername    [or nothing for single player]");
			System.exit(1);
		}
	}
}
