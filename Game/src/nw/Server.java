package nw;
import game.world.dimensions.*;
import game.world.model.*;

import java.awt.Polygon;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
	private InputStream inStream;
	private OutputStream outStream;

	private static List<ObjectOutputStream> outStreams = new ArrayList<ObjectOutputStream>();
	private static ServerWorld world;

	public Server(InputStream inStream, OutputStream outStream){
		this.inStream = inStream;
		this.outStream = outStream;
	}

	public static void initialiseWorld(){
		int[] xpoints = new int[]{200,400,400,200};
		int[] ypoints = new int[]{200,200,400,400};

		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		//items.add(new Table("Table1", new Point3D(250, 0, 250), new Rectangle3D(50, 50, 50)));
		Room room = new Room(items, p, "Room1");
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);
		world = new World(rooms);
	}

	public static void send(Object o) throws IOException{
		for(ObjectOutputStream os : outStreams){
			os.writeObject(o);
		}
	}

	public void run(){
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		Object received = null;
		String recStr = "";

		try{

			out = new ObjectOutputStream(outStream);
			out.flush();
			outStreams.add(out);

			BufferedInputStream bis = new BufferedInputStream(inStream);
			in = new ObjectInputStream(bis);

			long time = System.currentTimeMillis();

			synchronized(world){
				out.writeObject(world.getPlaces().next());
			}
			while(true){
	
				if(bis.available() != 0){
					received = in.readObject();
					if(received instanceof String){
						recStr = ((String)received);
						System.out.println("[Server] Got: " + recStr);
						
						for(String cmd : world.applyCommand((String)received)){
							System.out.println("[Server] Returning: " + cmd);	
							Server.send(cmd);
						}
					}else{
						System.out.println("[Server] No idea what this is: " + received);
					}
				}

				if((System.currentTimeMillis()-time)/1000 >= 2){
					time = System.currentTimeMillis();
					//out.writeObject(world.getPlaces().next());
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
			Server.initialiseWorld();
			while(true){
				Socket clientSock = serverSocket.accept();
				new Server(clientSock.getInputStream(), clientSock.getOutputStream()).start();
			}
		}catch(IOException e){
			System.err.println(e);
		}
	}
}
