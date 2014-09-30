package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 * @author Nicky van Hulst
 * */
public class MainMenu implements GraphicsPane, Animated{
	private final int numbOfButtons = 5;
	private BlankPanel panel;
	private BufferedImage backgroundImage;

	//the currently selected button
	private int selectedButton;

	//arrays for the button rectangles and names
	private Rectangle[] buttons;
	private String[] buttonNames;

	//animation fields
	private boolean animating;
	private boolean animatingIn;
	private boolean setUp;
	private int buttonStartX;
	private int  aniSpeed = 1;
	private float speedIncrease = 1f;

	//the menu to be drawn after the animation
	private GraphicsPane nextMenu;

	/**
	 * Constructor for the MainMenu
	 * */
	public MainMenu(BlankPanel panel){
		this.panel = panel;
		buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.selectedButton = -1;
		loadImages();
		setUpButtons();
	}


	/**
	 * Sets up all of the button locations for the main menu
	 * */
	public void setUpButtons(){
		int height = GameWindow.FRAME_HEIGHT;
		int y = height/numbOfButtons;
		int buttonGap = 20;
		int recHeight = 50;
		int recWidth = 200;
		int x = (GameWindow.FRAME_WIDTH/2)- (recWidth/2);

		//create buttons
		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new Rectangle(x,y,recWidth,recHeight);
			y += recHeight + buttonGap;
		}

		//name the buttons
		buttonNames[0] = "Single Player";
		buttonNames[1] = "Multiplayer";
		buttonNames[2] = "Options";
		buttonNames[3] = "Help";
		buttonNames[4] = "Quit";
	}

	/**
	 *Draws all of the elements of the menu on the screen
	 * */
	public void render(Graphics g){
		g.drawImage(backgroundImage, 0, 0,panel);
		//drawBackGroundImage(g);
		MenuUtil.drawButtons(g,selectedButton,buttons,buttonNames);

		if(nextMenu!=null && nextMenu instanceof Animated){
			if(((Animated) nextMenu).isAnimating()){
				System.out.println("Next Menu animating");
				((Animated) nextMenu).animate();
				nextMenu.render(g);
			}
		}
	}


	@Override
	public void handleMouseMoved(MouseEvent e){
		//set selected button
		for(int i = 0; i < buttons.length; i++){
			if(buttons[i].contains(e.getX(), e.getY())){
				selectedButton = i;//set selected button
				return;
			}
			selectedButton = -1;//no button is selected
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent e){
		buttonPressed();
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("enter")){
			buttonPressed();
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			selectedButton = MenuUtil.moveButtonSelectionDown(selectedButton, buttons.length);
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			selectedButton = MenuUtil.moveButtonSelectionUp(selectedButton, buttons.length);
		}
	}


	/**
	 *Decides what should happen when a button is pressed
	 * */
	private void buttonPressed(){
		switch(selectedButton){
		case 0: panel.setMenu(new GameScreen(panel));
			return;
		case 1: panel.setMenu(new CharacterSelectionMenu(panel));
			return;
		case 2 :
			nextMenu = new OptionMenu(panel);
			animating = true;
			return;
		case 3 :
			nextMenu = new HelpMenu(panel);
			animating = true;
			return;
		case 4 : System.exit(0);
			return;
		}
	}


	@Override
	public void animate() {
		if(animatingIn){
			if(!setUp){
				buttonStartX = (int) buttons[0].getX();
				MenuUtil.setUpAnimation(buttons);//moved the buttons to the end of the screen
				setUp = true;
			}
			if(MenuUtil.animateIn(buttonStartX,buttons, aniSpeed+=speedIncrease)){
				panel.setMenu(new MainMenu(panel));//create the new main menu
			}
		}
		else{
			if(MenuUtil.animateOut(buttons, aniSpeed+=speedIncrease)){
				if(nextMenu instanceof OptionMenu){
					((OptionMenu) nextMenu).setAnimating(true);
				}
				else{
					((HelpMenu)nextMenu).setAnimating(true);
				}

				((Animated) nextMenu).animate();
			}
		}
	}

	/**
	 * Sets whether the menu is animating in or out
	 * */
	public void setAnimating(boolean in){
		animatingIn = in;
		animating = true;
	}

	/**
	 * Loads the images for the menu
	 * */
	public void loadImages(){
		java.net.URL imagefile = MainMenu.class.getResource("resources/bocks.jpg");


		//load background image
		try {
			this.backgroundImage = ImageIO.read(imagefile);
			backgroundImage.getScaledInstance(GameWindow.FRAME_WIDTH, GameWindow.FRAME_HEIGHT, BufferedImage.SCALE_DEFAULT);
			System.out.println(backgroundImage.getWidth());
		} catch (IOException e) {
			System.out.println("failed reading imagge");
			e.printStackTrace();
		}
	}


	/**
	 * Returns whether or not the menu is currently animating
	 * */
	@Override
	public boolean isAnimating(){
		return animating;
	}


	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
