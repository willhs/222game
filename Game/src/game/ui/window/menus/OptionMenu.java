package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * @author Nicky van Hulst
 * */
public class OptionMenu implements GraphicsPane, Animated {
	
	//panel to draw on 
	private BlankPanel panel;
	
	//number of buttons to be created
	private int numbOfButtons;
	
	//arrays for buttons and button names
	private Rectangle[] buttons;
	private String[] buttonNames;

	//the currently selected button
	private int selectedButton;

	//the current menu the user is in
	private GraphicsPane currentMenu;

	//animation fields
	private boolean animating;
	private boolean animatingIn;
	private GraphicsPane nextMenu;
	private boolean setUp;
	private int buttonStartX;
	private int aniSpeed = 1;
	private int speedIncrease = 1;
	private StarMation starMation;

	
	/**
	 * Constructor for the optionMenu
	 * */
	public OptionMenu(BlankPanel panel){
		this.starMation = MainMenu.getStarMation();
		this.panel = panel;
		this.numbOfButtons = 2;
		this.selectedButton = -1;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];

		setupButtons();
	}


	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		int buttonGap = 20;
		int x = 50;
		int y = 50;
		int width = 200;
		int height = 50;

		buttons[0] = new Rectangle(x,y,width,height);
		y += height + buttonGap;
		buttons[1] = new Rectangle(x,y,width,height);

		buttonNames[0] = "Back";
		buttonNames[1] = "Key Bindings";
	}


	@Override
	public void render(Graphics g){
		//g.drawImage(backgroundImage, 0, 0,panel);
		starMation.render(g);
		if(currentMenu != null){
			currentMenu.render(g);
			return;
		}

		if(nextMenu!=null && nextMenu instanceof Animated){
			if(((Animated) nextMenu).isAnimating()){
				System.out.println("Next Menu animating");
				((Animated) nextMenu).animate();
				nextMenu.render(g);
			}
		}

		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
		g.drawString("OPTIONS", GameWindow.FRAME_WIDTH/2, GameWindow.FRAME_HEIGHT/2);
	}

	@Override
	public void handleMouseMoved(MouseEvent e){
		if(currentMenu != null){//this menu is not the menu in focus so pass the mouse movement on
			currentMenu.handleMouseMoved(e);
			return;
		}

		//set selected button
		for(int i = 0; i < buttons.length; i++){
			if(buttons[i].contains(e.getX(), e.getY())){
				selectedButton = i;//set selected button
				return;
			}
			selectedButton = Integer.MAX_VALUE;//no button is selected
		}
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(currentMenu != null){//this menu is not the menu in focus so pass the mouse event on
			currentMenu.handleMouseReleased(e);
			return;
		}
		if(selectedButton == 0){//back button
			animating = true;
			animatingIn = false;
			nextMenu = new MainMenu(panel);
		}
		else if(selectedButton == 1){//key binding menu button
			currentMenu = new KeyOptionScreen(panel);
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(currentMenu != null){
			currentMenu.keyPressed(keyEvent);
			return;
		}
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			animating = true;
			animatingIn = false;
			nextMenu = new MainMenu(panel);
		}
		else if(keyEvent.equals("enter")){
			handleMouseReleased(null);//TODO change to a proper method button pressed like main menu
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			selectedButton = MenuUtil.moveButtonSelectionDown(selectedButton, buttons.length);
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			selectedButton = MenuUtil.moveButtonSelectionUp(selectedButton, buttons.length);
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
				panel.setMenu(new OptionMenu(panel));//create the new main menu
			}
		}else{
			if(MenuUtil.animateOut(buttons,aniSpeed+=speedIncrease)){
				((MainMenu) nextMenu).setAnimating(true);
				((Animated) nextMenu).animate();
			}
		}
	}

	@Override
	public boolean isAnimating() {
		return animating;
	}

	@Override
	public void setAnimating(boolean in){
		animatingIn = in;
		animating = true;
	}

	@Override
	public void handleMousePressed(MouseEvent e) {}
}
