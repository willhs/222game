package game.ui.window;

import game.ui.render.Renderer;
import game.ui.render.ImageStorage;
import game.ui.window.menus.InventoryMenu;
import game.ui.window.menus.MainMenu;
import game.ui.window.menus.MenuUtil;
import game.ui.window.menus.PauseMenu;
import game.world.dimensions.Vector3D;
import game.world.model.Item;
import game.world.model.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

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
	private Item items[];

	//world fields
	private Vector3D rotateVector;
	private Client client;

	//the animation of stars
	private StarMation starMation;

	//the inventory menu
	private InventoryMenu popUpInventory;

	private Player player;

	private boolean gameOver;


	/**
	 * Constructor for the game screen
	 * */
	public GameScreen(BlankPanel panel, Client client ,Player player){
		this.starMation = MainMenu.getStarMation();
		this.client = client;
		this.inventoryButtons = new Rectangle[numbofButtons];
		this.names = new String[numbofButtons];
		this.panel = panel;
		this.items = new Item[6];
		this.player = player;
		this.popUpInventory = new InventoryMenu(panel, this, player);

		popUpInventory.updateInventory();
		releventQueKeypress = createKeylist();
		rotateVector = new Vector3D(0f, 0f, 0f);
		setUpInventoryBarButtons();
	}


	/**
	 *sets up the inventory on the game screen
	 * */
	private void setUpInventoryBarButtons(){
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

		//add relevent key presses
		keyList.add("Up");
		keyList.add("Down");
		keyList.add("Right");
		keyList.add("Left");
		keyList.add("Interact");
		keyList.add("Drop");
		keyList.add("PickUp");

		return keyList;
	}

	@Override
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		popUpInventory.updateInventory();

		//render the star animation
		starMation.render(g);
		removeItems();

		//render the game
		if(GameWindow.currentRoom != null){
			Renderer.renderPlace(g,GameWindow.currentRoom,rotateVector, player);
		}

		//draws the selected inventory bar
		drawInventorybar(g);
		drawInventory(g);

		//render the current menu over top of the game
		if(currentMenu != null){
			currentMenu.render(g);
		}

		//draws the payers remaining oxygen
		drawOxygen(g2d);

		//seconds stuff
		//drawTime(g2d);
	}

	public void drawOxygen(Graphics2D g){
		int airLevel = player.getAirLevel();

		g.setColor(new Color(0,191,255));
		g.fillRect(100, 50, airLevel*2, 50);

		g.setColor(Color.red);
		g.fill(new Arc2D.Double(75, 50, 50, 50, 90, 90, Arc2D.PIE));
		Arc2D arc =  new Arc2D.Double(75, 50, 50, 50, 180, 90, Arc2D.PIE);

		g.fill(new Arc2D.Double(200+75, 50, 50, 50, 0, 90, Arc2D.PIE));
		g.fill(new Arc2D.Double(200+75, 50, 50, 50, 270, 90, Arc2D.PIE));
		g.fill(arc);

		g.setColor(Color.red);
		g.drawRect(100, 50, 200, 50);

		Font myFont = new Font("Tunga",0,20);
		g.setFont(myFont);
		g.setColor(Color.white);

		g.drawString(airLevel+"", 100 + 100 - g.getFontMetrics(myFont).stringWidth(""+airLevel)/2, (int) ((50 + 50 - (g.getFontMetrics(myFont).getHeight()/2)))-3);

		if(airLevel >=190)gameOver(g);
	}

	public void gameOver(Graphics g){
			Graphics2D g2d = (Graphics2D)g;

			Font myFont = new Font("Tunga",0,30);
			g.setFont(myFont);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GameWindow.FRAME_WIDTH, GameWindow.FRAME_WIDTH);
			g.setColor(Color.white);
			g2d.drawString("Game Over", 0 + ((GameWindow.FRAME_WIDTH/2) - g.getFontMetrics(myFont).stringWidth("Game Over")/2), (int) (((GameWindow.FRAME_HEIGHT/2) - (g.getFontMetrics(myFont).getHeight()/2))));
			gameOver = true;

	}

	public void drawTime(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		int seconds = Client.getSecondsRemaining();
		Font myFont = new Font("Tunga",0,50);
		g.setFont(myFont);
		g.setColor(Color.white);
		g.drawString(seconds+"", 50, 100);
		if(seconds >= 100){

			myFont = new Font("Tunga",0,30);
			g.setFont(myFont);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GameWindow.FRAME_WIDTH, GameWindow.FRAME_WIDTH);
			g.setColor(Color.white);
			g2d.drawString("Game Over", 0 + ((GameWindow.FRAME_WIDTH/2) - g.getFontMetrics(myFont).stringWidth("Game Over")/2), (int) (((GameWindow.FRAME_HEIGHT/2) - (g.getFontMetrics(myFont).getHeight()/2))));
			gameOver = true;
		}
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

	@SuppressWarnings("static-access")
	@Override
	public void keyPressed(String keyEvent) {
		if(currentMenu != null){
			currentMenu.keyPressed(keyEvent);
			return;//no need to do anything with the game as the current menu is the pause menu
		}
		else if(gameOver){
			client.quit();
			panel.setMenu(new MainMenu(panel));
		}
		else if(keyEvent.equals("inventory")){
			removeItems();
			popUpInventory.updateInventory();
			currentMenu = popUpInventory;
		}
		else if(keyEvent.equals("escape")){
			currentMenu = new PauseMenu(panel, this, starMation);
		}
		else if(releventQueKeypress.contains(keyEvent)){
			System.out.println("Event" + keyEvent);
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

		//set the item selected
		updateSelectedItem();
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
		g2d.setStroke(oldStroke);//reset the bar line widht
	}


	@Override
	public void handleMousePressed(MouseEvent e) {
		if(currentMenu != null ){
			currentMenu.handleMousePressed(e);//pass control to the menu in focus
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
		return -1;//not on grid
	}


	/**
	 * Returns whether or not the there is an item at this location
	 * */
	public boolean containsItem(int gridNumb){
		if(gridNumb < 1 || gridNumb >= items.length)return false;
		if(items[gridNumb] != null)return true;
		return false;
	}


	/**
	 *Places the item on the grid at the selected location
	 * */
	public void placeItemOnGrid(int gridNumb, Item item){
		if(gridNumb < 0 || gridNumb >= items.length)return;//error
		if(items[gridNumb] == null){
			items[gridNumb] = item;
		}
	}


	/**
	 * Returns the item at a given location
	 * null if it does not removes it from this inventory
	 * */
	public Item grabItemFromIventory(int gridLocation){
		Item tempItem = items[gridLocation];
		items[gridLocation] = null;
		return  tempItem;//could still be null
	}


	/**
	 * Returns if the item is in the bar inventory
	 * on the game screen
	 * */
	public boolean inInventory(Item item){
		for(Item i : items){
			if(i!=null){
				if(i.getName().equals(item.getName()))return true;//in the inventory
			}
		}
		return false;//not in the inventory
	}

	/**
	 * Removes any items from the inventory that have been dropped
	 * */
	public void removeItems(){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				if(!(player.getInventory().isIn(items[i]))){
					items[i] = null;
				}
			}

		}
	}


	/**
	 * Sets the selected item to be selected
	 * */
	public void updateSelectedItem(){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null && selectedButton !=-1 && selectedButton == i){
				items[selectedButton].setSelected(true);
			}
			else if(items[i] != null){
				items[i].setSelected(false);
			}
		}
	}


	/**
	 *Draws the images of the inventory in the bar
	 * */
	public void drawInventory(Graphics g){
		for(int i = 0; i < items.length; i++){
			if(items[i] != null){
				g.drawImage(ImageStorage.getImageFromName(items[i].getImageName()), (int)inventoryButtons[i].getX(), (int)inventoryButtons[i].getY(),boxSize-5,boxSize-5, panel);
			}
		}
	}


	/**
	 * Returns the grid size
	 * */
	public int getGridSize(){return this.boxSize;}


	/**
	 * Returns the client
	 * */
	public Client getClient(){return client;}


	/**
	 * Set the current menu in focus on the screen
	 * */
	public void setMenu(GraphicsPane menu){this.currentMenu = menu;}

	/**
	 * Returns the selectted inventory button
	 * */
	public int getSelectedButton(){return this.selectedButton;}
}
