package game.ui.window;

import game.ui.render.Renderer;
import game.ui.render.Res;
import game.ui.window.menus.InventoryMenu;
import game.ui.window.menus.MainMenu;
import game.ui.window.menus.MenuUtil;
import game.ui.window.menus.PauseMenu;
import game.world.dimensions.Vector3D;
import game.world.model.Item;
import game.world.model.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import javax.imageio.ImageIO;

import test.world.util.SimpleServerInterface;
import nw.Client;

/**
 * @author Nicky van Hulst
 * */
public class GameScreen implements GraphicsPane  {

	//the menu drawn on top on the game screen
	private GraphicsPane currentMenu;
	private BlankPanel panel;
	
	//list of key presses to pass on to the client
	private ArrayList<String> releventQueKeypress;

	//inventory bar fields
	private int numbofButtons = 6;
	private Rectangle[] inventoryButtons;
	private String[] names;
	private int boxSize = 80;
	
	//selected button
	private int selectedButton = -1;

	//inventory grid images
	//private BufferedImage[] inventoryImages;
	private Item items[];
	//private BufferedImage selectedImage;

	//world fields
	private Player player;
	private Vector3D rotateVector;
	private Client client;
	
	//the animation of stars
	private StarMation starMation;
	
	private InventoryMenu popUpInventory;
	
	/**
	 * Constructor for the game screen
	 * */
	public GameScreen(BlankPanel panel, Client client ,Player player){
		this.starMation = MainMenu.getStarMation();
		this.client = client;
		this.player = player;
		this.inventoryButtons = new Rectangle[numbofButtons];
		this.names = new String[numbofButtons];
	//	this.inventoryImages = new BufferedImage[6];
		setUpInventoryBarButtons();
		this.panel = panel;

		this.items = new Item[6];
		this.popUpInventory = new InventoryMenu(panel, this, player);
		popUpInventory.updateInventory();
		releventQueKeypress = createKeylist();
		rotateVector = new Vector3D(0f, 0f, 0f);
	}
	
	public Client getClient(){
		return client;
	}

	public void setUpInventoryBarButtons(){
		//modify the stroke size to 4
		int numbOfBoxes = numbofButtons;
		int width = boxSize*numbOfBoxes;
		int gap = 40;

		int xstart = (GameWindow.FRAME_WIDTH/2) - width/2;
		int ystart =  GameWindow.FRAME_HEIGHT-gap-boxSize;

		int x = xstart;
		int y = ystart;

		for(int i = 0; i < numbOfBoxes; i++){
			inventoryButtons[i] = new Rectangle(x,y,boxSize,boxSize);
			names[i] = "";
			x+= boxSize;
		}
	}
	
	
	/**
	 * Creates a list of the relevant key presses the client needs to 
	 * know about
	 * */
	private ArrayList<String> createKeylist(){
		ArrayList<String> keyList = new ArrayList<String>();

		keyList.add("Up");
		keyList.add("Down");
		keyList.add("Right");
		keyList.add("Left");
		keyList.add("Interact");

		return keyList;
	}

	@Override
	public void render(Graphics g){
		//render the star animation 
		starMation.render(g);
		
		
		if(GameWindow.currentRoom != null){
			Renderer.renderPlace(g,GameWindow.currentRoom,rotateVector); 
		}
		
		//render the current menu over top of the game
		if(currentMenu != null){
			currentMenu.render(g);
		}
		
		//draws the selected inventory bar
		drawInventorybar(g);
		drawInventory(g);
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		if(currentMenu != null){
			currentMenu.handleMouseMoved(e);//pass the mouse movement onto the current menu
			return;
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(currentMenu != null){
			currentMenu.handleMouseReleased(e);//pass the mouse movement onto the current menu
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(currentMenu != null){
			currentMenu.keyPressed(keyEvent);
			return;//no need to do anything with the game as the current menu is the pause menu
		}
		else if(keyEvent.equals("inventory")){
			popUpInventory.updateInventory();
			currentMenu = popUpInventory;
		}
		else if(keyEvent.equals("escape")){
			currentMenu = new PauseMenu(panel, this);
		}
		else if(releventQueKeypress.contains(keyEvent)){
			client.makeMove(keyEvent, rotateVector.getY());
		}
		else if(keyEvent.equals("1")){
			selectedButton = 0;
		}
		else if(keyEvent.equals("2")){
			selectedButton = 1;
		}
		else if(keyEvent.equals("3")){
			selectedButton = 2;
		}
		else if(keyEvent.equals("4")){
			selectedButton = 3;
		}
		else if(keyEvent.equals("5")){
			selectedButton = 4;
		}
		else if(keyEvent.equals("6")){
			selectedButton = 5;
		}
		else if(keyEvent.equals("rotate right")){
			rotateVector =  rotateVector.plus( new Vector3D(0f , 0.05f , 0f));
		}
		else if(keyEvent.equals("rotate left")){
			rotateVector =  rotateVector.plus( new Vector3D(0f , -0.05f , 0f));
		}
	}
	
	
	/**
	 * Draws the inventory bar at the bottom of the game screen
	 * */
	public void drawInventorybar(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//modify the stroke size to 4
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));

		MenuUtil.drawButtons(g, -1, inventoryButtons, names);

		if(selectedButton >= 0 && selectedButton < numbofButtons){
			g.setColor(Color.blue);
			g.drawRect((int)inventoryButtons[selectedButton].getX(), (int)inventoryButtons[selectedButton].getY(), boxSize, boxSize);
		}
		g2d.setStroke(oldStroke);
	}
	
	/**
	 * Set the current menu in focus on the screen 
	 * */
	public void setMenu(GraphicsPane menu){
		this.currentMenu = menu;
	}
		
	
	/**
	 *places an item in the inventory bar at the bottom of the screen 
	 * */
	//TODO change to boolean and check for duplicate items 
	public void placeInInventory(int x, int y, BufferedImage image){
		int selectedGrid = -1;

		for(int i = 0; i < inventoryButtons.length; i++){
			if(inventoryButtons[i].contains(x, y)){
				selectedButton = i;//set selected button
			}
		}
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		if(currentMenu != null ){
			currentMenu.handleMousePressed(e);
			return;
		}
	}

	/**
	 * Returns whether or not a point is on the inventory grid
	 * returns -1 if not on the grid
	 * */
	public int isOnGrid(int x, int y){
		for(int i = 0; i < inventoryButtons.length; i++){
			if(inventoryButtons[i].contains(x,y))return i;//also checks if there is an item in the location
		}
		return -1;
	}
	
	public boolean containsItem(int gridNumb){
		if(items[gridNumb] != null)return true;
		return false;
	}
	
	//TODO check if used and how to make better
	/**
	 * 
	 * */
	public void placeItemOnGrid(int gridNumb, Item item){//TODO will most likely change to an item instead of an image but just to test for now
//		if(inventoryImages[gridNumb] == null){
//			inventoryImages[gridNumb] = image;
//		}
		if(gridNumb < 0 || gridNumb >= items.length)return;//error
		if(items[gridNumb] == null){
			items[gridNumb] = item;
		}
	}
	
	/**
	 * Returns the item at a given location 
	 * null if it does not exsist 
	 * */
	public Item grabItemFromIventory(int gridLocation){
		Item tempItem = items[gridLocation];
		items[gridLocation] = null;
		return  tempItem;//could still be null
	}
	
	public boolean inInventory( Item item){
		System.out.println("In Inventory");
		System.out.println(items.length);
		for(Item i : items){
			System.out.println("Iterating");
			if(i!=null){
				System.out.println("Not null in ininventory");
				if(i.getName().equals(item.getName()))return true;
			}
		}
		return false;
	}
	
	
	/**
	 *Draws the images of the inventory in the bar
	 * */
	public void drawInventory(Graphics g){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				g.drawImage(Res.getImageFromName(items[i].getImageName()), (int)inventoryButtons[i].getX(), (int)inventoryButtons[i].getY(),boxSize-5,boxSize-5, panel);
			}
		}
	}

	public int getGridSize(){
		return this.boxSize;
	}
}
