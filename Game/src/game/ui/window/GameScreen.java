package game.ui.window;

import game.ui.render.Renderer;
import game.ui.window.menus.InventoryMenu;
import game.ui.window.menus.MainMenu;
import game.ui.window.menus.MenuUtil;
import game.ui.window.menus.PauseMenu;
import game.world.dimensions.Vector3D;
import game.world.model.Player;
import game.world.model.Room;
import game.world.dimensions.Vector3D;

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

public class GameScreen implements GraphicsPane  {

	//the menu drawn ontop on the game screen
	private GraphicsPane currentMenu;
	private BlankPanel panel;
	private Queue<String> keyQue;

	private ArrayList<String> releventQueKeypress;

	private BufferedImage testBackGroundImage;


	private int lastXPress;
	private int lastYPress;

	//inventory bar fields
	private int numbofButtons = 6;
	private Rectangle[] inventoryButtons;
	private String[] names;
	private int boxSize = 80;

	private int selectedButton = -1;


	//inventory grid images
	private BufferedImage[] inventoryImages;
	//private BufferedImage selectedImage;
	
	
	//world fields 
	private Room currentRoom;
	private Player player;
	
	private Vector3D rotateVector;

	public GameScreen(BlankPanel panel){
		this.player = player;
		//this.currentRoom = room;
		this.inventoryButtons = new Rectangle[numbofButtons];
		this.names = new String[numbofButtons];
		this.inventoryImages = new BufferedImage[6];
		setUpInventoryBarButtons();
		this.panel = panel;
		this.keyQue = GameWindow.getKeyQueue();
		loadImages();
		releventQueKeypress = createKeylist();

		rotateVector = new Vector3D(0f, 0f, 0f);
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

	private ArrayList<String> createKeylist(){
		ArrayList<String> keyList = new ArrayList<String>();

		keyList.add("move up");
		keyList.add("move down");
		keyList.add("move right");
		keyList.add("move left");

		return keyList;
	}

	@Override
	public void render(Graphics g){
		//g.drawImage(testBackGroundImage, 0, 0, panel);
		Renderer.render(g,rotateVector); //TODO wait for will to implement this method


		if(currentMenu != null){
			currentMenu.render(g);
		}


		//at this point draw my own overlay over the game

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
			currentMenu = new InventoryMenu(panel,this);
		}
		else if(keyEvent.equals("escape")){
			currentMenu = new PauseMenu(panel, this);
		}
		else if(releventQueKeypress.contains(keyEvent)){
			keyQue.offer(keyEvent);
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
			rotateVector =  rotateVector.plus( new Vector3D(0.05f , 0f , 0f));
		}
		else if(keyEvent.equals("rotate left")){
			rotateVector =  rotateVector.plus( new Vector3D(-0.05f , 0f , 0f));
		}



		
	}

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

	public void setMenu(GraphicsPane menu){
		//if null just means no need to draw any exra windows on the game screen
		this.currentMenu = menu;
	}

	public void placeInInventory(int x, int y, BufferedImage image){
		int selectedGrid = -1;

		for(int i = 0; i < inventoryButtons.length; i++){
			if(inventoryButtons[i].contains(x, y)){
				selectedButton = i;//set selected button
			}
		}
	}




	/**
	 *loads all of the images required for the game screen
	 *scaled the images to the size of the screen
	 * */
	public void loadImages(){
		java.net.URL imagefile = MainMenu.class.getResource("resources/test-Game.jpg");

		//TODO accualy do scaling this current does nothing it needs to be assigned
		//load background image
		try {
			this.testBackGroundImage = ImageIO.read(imagefile);
			testBackGroundImage.getScaledInstance(GameWindow.FRAME_WIDTH, GameWindow.FRAME_HEIGHT, BufferedImage.SCALE_DEFAULT);
		} catch (IOException e) {
			System.out.println("failed reading imagge");
			e.printStackTrace();
		}
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		if(currentMenu != null ){
			currentMenu.handleMousePressed(e);
			return;
		}

		lastXPress = e.getX();
		lastYPress = e.getY();
	}

	/**
	 * Returns whether or not a point is on the inventory grid
	 * returns -1 if not on the grid
	 * */
	public int isOnGrid(int x, int y){
		for(int i = 0; i < inventoryButtons.length; i++){
			if(inventoryButtons[i].contains(x,y))return i;
		}
		return -1;
	}

	public void placeItemOnGrid(int gridNumb, BufferedImage image){//TODO will most likely change to an item instead of an image but just to test for now
		if(inventoryImages[gridNumb] == null){
			inventoryImages[gridNumb] = image;
		}
	}

	public void drawInventory(Graphics g){
		for(int i = 0; i < inventoryImages.length; i++){
			if(inventoryImages != null){
				g.drawImage(inventoryImages[i], (int)inventoryButtons[i].getX(), (int)inventoryButtons[i].getY(), panel);
			}
		}
	}

	public int getGridSize(){
		return this.boxSize;
	}
}
