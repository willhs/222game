package nw;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Room;
import game.world.model.ServerWorld;
import game.world.model.Table;
import game.world.model.World;

import java.awt.Polygon;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
	private Socket clientSocket;

	public Server(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	public ServerWorld getDefaultWorld(){
		int[] xpoints = new int[]{200,400,400,200};
		int[] ypoints = new int[]{200,200,400,400};

		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		items.add(new Table("Table1", new Point3D(250, 0, 250), new Rectangle3D(50, 50, 50)));
		Room room = new Room(items, p, "Room1");
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);
		return new World(rooms);
	}

	public void run(){
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		Object received = null;
		String recStr = "";

		ServerWorld world = getDefaultWorld();

		try{

			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();

			BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
			in = new ObjectInputStream(bis);

			//Player player = world.getPlayers().get(world.getPlayers().size-1);
			//System.out.println("Got player: " + player.getName());

			long time = System.currentTimeMillis();

			while(true){
	
				if(bis.available() != 0){
					received = in.readObject();
					if(received instanceof String){
						recStr = ((String)received);
						System.out.println("Got: " + recStr);
						
						for(String cmd : world.applyCommand((String)received)){
							System.out.println("Returning: " + cmd);	
							out.writeObject(cmd);
						}
					}else{
						System.out.println("No idea what this is: " + received);
					}
				}

				if((System.currentTimeMillis()-time)/1000 >= 2){
					out.writeObject(world.getPlaces().next());
					time = System.currentTimeMillis();
				}
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
				in.close();
				out.close();
			}catch(Exception e){}
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
