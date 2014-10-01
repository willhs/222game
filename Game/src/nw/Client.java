package nw;

import game.ui.window.GameWindow;
import game.world.model.*;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Client{
	public static void main(String[] args) throws IOException{
		String playerName = "";
		InputStream inStream = null;
		OutputStream outStream = null;

		if(args.length == 3){
			playerName = args[2];
			Socket sock = new Socket(args[0], Integer.parseInt(args[1]));
			inStream = sock.getInputStream();
			outStream = sock.getOutputStream();
		}else if(args.length == 1){
			playerName = args[0];

			PipedInputStream sIn = new PipedInputStream();
			PipedOutputStream sOut = new PipedOutputStream();
			inStream = new PipedInputStream(sOut);
			outStream = new PipedOutputStream(sIn);

			Server server = new Server(sIn, sOut);
			server.initialiseWorld();
			server.start();
		}else{
			System.err.println("Usage: java Client [host] [port] playername");
			System.exit(1);
		}

		GameWindow gw = new GameWindow();
		Queue<String> keyCodeQueue = gw.getKeyQueue();
		ClientWorld world = null;

		Player player = new Player(playerName);

		ObjectOutputStream out = new ObjectOutputStream(outStream);
		out.flush();

		BufferedInputStream bis = new BufferedInputStream(inStream);
		ObjectInputStream in = new ObjectInputStream(bis);

		Object received =  null;
		Room currentRoom = null;

		try{

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
			out.close();
			in.close();
		}
	}
}
