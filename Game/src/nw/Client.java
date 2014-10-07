package nw;

import game.ui.window.GameWindow;
import game.world.model.*;
import game.world.logic.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Component;

public class Client extends Thread{
	private static Server server;//This client may instantiate a server if single player mode is chosen
	private static ClientWorld world = null;
	private static boolean singlePlayerMode = false;

	//The two client IO streams
	private static InputStream inStream;
	private static OutputStream outStream;

	private static Component frame;//The frame to call repaint() on
	private static Player player;//The player to enter the game, given by the caller

	//Keep the last command so we can ignore duplicate commands
	private static String lastCommand = "";

	//Outgoing command queue.  Key events are not actually accepted by the server, they have to be translated.
	//We translate them, then add them to this queue which is polled and added to by the main loop.
	private static Queue<String> commandQueue = new LinkedList<String>();

	//Flag for printing debug messages
	private static boolean printing = false;

	//The number of remaining seconds in the game.  Updated by server.
	private static int remainingSecondsFromServer = 0;
	private static int lastTimeUpdate = 0;

	/*
	 * Instantiate Client in multiplayer mode, join a remote game
	 * @param player The player joining the game
	 * @param host The hostname of the server
	 * @param port Port of game on server
	 * @param frame Frame to call repaint() on within gameloop
	 */
	public Client(String host, int port, Component frame){
		this.frame = frame;
		this.player = player;
		//This is multiplayer mode, so we assume a server is already active at hort/port
		try{
			//Make socket
			Socket sock = new Socket(host, port);

			//Get the streams
			inStream = sock.getInputStream();
			outStream = sock.getOutputStream();

			start();
			waitForConnection();
		}catch(IOException e){
			System.err.println(e);
		}
	}

	/*
	 * Instantiate the client in single player mode, creating a faux server
	 * @param player Player to put in the game when created
	 * @param frame Frame to call repaint() on within gameloop
	 */
	public Client(Component frame){
		this.frame = frame;
		this.player = player;
		singlePlayerMode = true;
		//This is single player, so we instantiate a server here then use local io streams
		//for communication between us and it.
		try{
			//Server's io streams
			PipedInputStream sIn = new PipedInputStream();
			PipedOutputStream sOut = new PipedOutputStream();

			//Client's io streams, with links to the servers (other side of the links are created automatically)
			inStream = new PipedInputStream(sOut);
			outStream = new PipedOutputStream(sIn);

			//Create new server with the server's streams
			server = new Server(sIn, sOut, 0);
			server.initialiseWorld();//Initialise the static world before starting
			server.start();

			start();
			waitForConnection();
		}catch(IOException e){
			System.err.println(e);
		}
	}

	/*
	 * Move a local player by adding the move (left, right etc) to the
	 * key code queue to be processed and sent to the server
	 * @param move The move to be sent
	 */
	public static void makeMove(String move, float y){
		if (player != null){
			String cmd = world.getCommand(move, y);
			if(!cmd.equals(lastCommand)){
				lastCommand = cmd;
				commandQueue.add(cmd);
			}
		}
	}

	/*
	 * Wait until connection has been established and world has been received.
	 */
	public static void waitForConnection(){
		while(world==null){
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){System.err.println(e);}
		}
	}

	/*
	 * Create the command string for adding a player to the server and send it.
	 * Raise an error if the world says there's already a player with that name.
	 * @param p Player to add to the world
	 */
	public boolean addPlayerToWorld(Player p){
		String cmd = world.getSetClientPlayer(p);
		if(cmd.equals("")){
			return false;
		}else{
			commandQueue.add(cmd);
			player = p;
		}

		return true;
	}

	/*
	 * Tell the Server and the Client thread to close the connection
	 */
	public void quit(){
		commandQueue.add("Quit");
		while(world!=null){
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){System.err.println(e);}
		}
	}

	public static void print(String msg){
		if(printing){
			System.out.println(msg);
		}
	}

	private static int getSecondsRemaining(){
		return remainingSecondsFromServer - ((((int)System.currentTimeMillis())/1000)-lastTimeUpdate);
	}

	/*
	 * The full client loop.  Creates connections to server, then runs the main loop which
	 * reads one item from the incoming queue and processes it, then sends one item
	 * from the outgoing queue, then re-renders the game.
	 */
	public void run(){
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try{
			//Wrap the output and input streams in a ObjectOutputStreams so we can send and receive objects
			out = new ObjectOutputStream(outStream);
			out.flush();

			//Use buffering for the input stream so we don't block the
			//thread while waiting for the server to send.
			BufferedInputStream bis = new BufferedInputStream(inStream);
			in = new ObjectInputStream(bis);

			Object received =  null;//The last object polled from the incoming queue
			Room currentRoom = null;//The current room to be rendered
			String cmd;

			while(true){
				if(bis.available() != 0){//If there is an object ready to be read
					received = in.readObject();//Read it in

					if(received instanceof String){//If it's a string, it's a command
						print("[Client] Got: " + (String)received);
						world.applyCommand((String)received);//So run it on the world
						GameWindow.setRoom((Room)world.getCurrentPlace());
					}
					else if(received instanceof World){//If we got a world, then
						print("[Client] Got world!: " + received);
						world = (World)received;//Save it

						//Set the room as the player's current place
						Room cp = (Room)world.getCurrentPlace();
						GameWindow.setRoom((Room)(cp != null ? cp : world.getPlaces().next()));
					}
					else if(received instanceof Integer){//If it's an integer, the server is telling us the remaining time
						remainingSecondsFromServer = ((Integer)received).intValue();
						lastTimeUpdate = (int)System.currentTimeMillis() / 1000;
					}else{
						print("[Client] No idea what this is: " + received);
					}
				}

				if(commandQueue.size() != 0){
					cmd = commandQueue.poll();
					out.writeObject(cmd);
					if(cmd.equals("Quit")){
						throw new IOException();
					}
					print("[Client]: " + getSecondsRemaining());
				}

				frame.repaint();//Repaint the game
				Thread.sleep(10);
			}

		}catch(ClassNotFoundException e){
			System.err.println("Client CNF" + e);
		}catch(IOException e){
			System.err.println("Client IO" + e);
		}catch(InterruptedException e){
			System.err.println("Client IE" + e);
		}finally{
			world=null;
			try{
				out.close();
				in.close();
			}catch(IOException e){System.err.println("Client close" + e);}
		}
	}

	public static void main(String[] args){
		GameWindow gw = new GameWindow();
		if(args.length == 3){
			new Client(args[0], Integer.parseInt(args[1]), gw).start();
		}else if(args.length == 0){
			new Client(gw).start();
		}else{
			System.err.println("Usage: java Client [host] [port] playername    [or nothing for single player]");
			System.exit(1);
		}
	}
}
