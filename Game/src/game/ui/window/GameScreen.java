package game.ui.window;

import game.ui.window.menus.InventoryMenu;
import game.ui.window.menus.MainMenu;
import game.ui.window.menus.PauseMenu;

import java.awt.Graphics;
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


	public GameScreen(BlankPanel panel){
		this.panel = panel;
		this.keyQue = GameWindow.getKeyQueue();
		loadImages();
		releventQueKeypress = createKeylist();

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
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		if(currentMenu != null){
			currentMenu.handleMouseMoved(e);//pass the mouse movement onto the current menu
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

	@Override
	public void animate() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isAnimating() {
		// TODO Auto-generated method stub
		return false;
	}
}
