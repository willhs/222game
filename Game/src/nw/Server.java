package nw;
import game.world.dimensions.*;
import game.world.model.*;
import game.world.logic.*;

import java.awt.Polygon;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Server extends Thread{
	//This server is instantiated once for each connection, so static fields are shared across connections,
	//while non-static fields are duplicated for each connection.

	//One set of IO streams for each connection
	private InputStream inStream;
	private OutputStream outStream;
	private int id;

	//One hashmap of output streams shared by all connections.  Every time something needs to be sent out
	//to all clients (almost everything), it is sent out on each stream in this hashmap.
	//Neds to be a hashmap so we can remove the right stream on exit.
	private static HashMap<Integer, ObjectOutputStream> outStreams = new HashMap<Integer, ObjectOutputStream>();

	//The single server world.  Should probably synchronize this, lest wrath we face.
	private static ServerWorld world;

	public Server(InputStream inStream, OutputStream outStream, int id){
		this.inStream = inStream;
		this.outStream = outStream;
		this.id = id;
	}

	/*
	 * On the first day, God called this function.
	 */
	public static void initialiseWorld(){
		int[] xpoints = new int[] { 200, 800, 800, 200 };
		int[] ypoints = new int[] { 200, 200, 800, 800 };
		Polygon firstFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> firstItems = new ArrayList<Item>();
		Room firstRoom = new Room(firstItems, new ArrayList<Enviroment>(), firstFloor, "Room1");
		Polygon secontFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> secondItems = new ArrayList<Item>();
		Room secondRoom = new Room(secondItems, new ArrayList<Enviroment>(), secontFloor, "Room2");
		
		List<Place> places = new ArrayList<Place>();
		places.add(firstRoom);
		places.add(secondRoom);
		
		Exit exit = new Door("Door1", firstRoom, new Point3D(400, 0, 400),
		                secondRoom, new Point3D(400, 0, 500));
		firstRoom.addExit(exit);
		secondRoom.addExit(exit);

		world = new World(places);
		world.addExit(exit);
	}

	/*
	 * Broadcast an object to all clients.
	 * @param o Object to send to all clients.
	 */
	public static void send(Object o) throws IOException{
		for(ObjectOutputStream os : outStreams.values()){
			os.writeObject(o);
		}
	}

	/*
	 * The main loop.  This function is running once for each player in the game concurrently.
	 * It:
	 *   1. Wraps the streams to add buffering and object serialization
	 *   2. Sends the world to the current client
	 *   3. Loops forever, processing any objects in the incoming queue when one is ready
	 */
	public void run(){
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		Object received = null;
		String recStr = "";

		try{

			//Wrap the output stream in an ObjectOutputStream, for sending whole objects
			out = new ObjectOutputStream(outStream);
			out.flush();
			System.out.println("adding broadcast id " + id);
			outStreams.put(id, out);//Add the output stream to the list of broadcast streams

			//Wrap the input stream in a buffer, so we don't block waiting for the client to send
			BufferedInputStream bis = new BufferedInputStream(inStream);
			in = new ObjectInputStream(bis);//And also in an ObjectInputStream as above

			long time = System.currentTimeMillis();//Save the time for interval broadcasts

			synchronized(world){
				out.writeObject(world);//Send the whole world to the client
			}
			while(true){//Forever:
	
				if(bis.available() != 0){//If there are any objects incoming
					received = in.readObject();//Get one
					if(received instanceof String){//If it's a string, it's a command, let's process it
						recStr = ((String)received);
						System.out.println("[Server] Got: " + recStr);
						if(recStr.equals("Quit")){
							throw new IOException();
						}
						
						for(String cmd : world.applyCommand((String)received)){//Apply the command and
							System.out.println("[Server] Returning: " + cmd);	
							Server.send(cmd);//Send each resulting command to all clients.
						}
					}else{
						System.out.println("[Server] No idea what this is: " + received);
					}
				}

				//We might choose to intermittently send the current room to deal with losses, but not at the moment.
				if((System.currentTimeMillis()-time)/1000 >= 2){
					time = System.currentTimeMillis();
					//out.writeObject(world.getPlaces().next());
				}
				Thread.sleep(50);
			}
		}catch(ClassNotFoundException e){
			System.err.println("Server CNF " + e);
		}catch(IOException e){
			System.err.println("Server IO " + e);
		}catch(InterruptedException e){
			System.err.println("Server IE " + e);
		}finally{
			outStreams.remove(id);

			System.out.print("Player left.  Remaining IDs: ");
			for(Integer cid : outStreams.keySet()){
				System.out.print(cid + ", ");
			}
			System.out.println();

			try{
				in.close();
				out.close();
			}catch(Exception e){System.err.println("Server close");}
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
			Server.initialiseWorld();
			int nextID = 0;
			while(true){
				Socket clientSock = serverSocket.accept();
				new Server(clientSock.getInputStream(), clientSock.getOutputStream(), nextID).start();
				nextID++;
			}
		}catch(IOException e){
			System.err.println(e);
		}
	}
}
