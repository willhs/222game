package nw;

import game.ui.window.GameWindow;
import game.world.model.*;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Component;

public class Client extends Thread{
	private static Server server;
	private static InputStream inStream;
	private static OutputStream outStream;
	private static Component frame;
	private static Player player;
	private static Queue<String> keyCodeQueue = new LinkedList<String>();

	public Client(Player player, String host, int port, Component frame){
		this.frame = frame;
		this.player = player;
		try{
			Socket sock = new Socket(host, port);

			inStream = sock.getInputStream();
			outStream = sock.getOutputStream();
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public Client(Player player, Component frame){
		this.frame = frame;
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

	public static void makeMove(String move){
		keyCodeQueue.add(move);
	}

	public void run(){
		ClientWorld world = null;

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
						System.out.println("[Client] Got: " + (String)received);
						world.applyCommand((String)received);
					}
					else if(received instanceof Room){
						System.out.println("[Client] Got room!: " + received);
						currentRoom = (Room)received;
						GameWindow.setRoom(currentRoom);

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
						System.out.println("[Client] No idea what this is: " + received);
					}
				}

				if(keyCodeQueue.size() != 0 && world != null){
					String action  = keyCodeQueue.poll();
					String cmd = world.getCommand(action);
					System.out.println("Got action " + action + " and cmd " + cmd);
					out.writeObject(cmd);
				}

				frame.repaint();
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
