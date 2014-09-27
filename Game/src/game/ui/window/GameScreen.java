package game.ui.window;

import game.ui.window.menus.InventoryMenu;
import game.ui.window.menus.MainMenu;
import game.ui.window.menus.MenuUtil;
import game.ui.window.menus.PauseMenu;
import game.world.util.Drawable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.sound.sampled.ReverbType;

public class GameScreen implements GraphicsPane  {

	//the menu drawn ontop on the game screen
	private GraphicsPane currentMenu;
	private BlankPanel panel;
	private Queue<String> keyQue;

	private ArrayList<String> releventQueKeypress;

	private BufferedImage testBackGroundImage;


	//inventory bar fields
	private int numbofButtons = 6;
	private Rectangle[] inventoryButtons;
	private String[] names;
	private int boxSize = 80;

	private int selectedButton = -1;

	public GameScreen(BlankPanel panel){
		this.inventoryButtons = new Rectangle[numbofButtons];
		this.names = new String[numbofButtons];
		setUpInventoryBarButtons();
		this.panel = panel;
		this.keyQue = GameWindow.getKeyQueue();
		loadImages();
		releventQueKeypress = createKeylist();
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

	public void render(Graphics g){
		//renderer.render(g); //TODO wait for will to implement this method
		g.drawImage(testBackGroundImage, 0, 0, panel);
		if(currentMenu != null){
			currentMenu.render(g);
		}

		drawInventorybar(g);
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

}
