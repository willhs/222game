package game.ui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;


/**
 * @author Nicky van HUlst
 * */


public class MainMenu implements Menu{
	private final int numbOfButtons = 5;
	private BlankPanel panel;

	private int selectedButton;

	Rectangle[] buttons;

	public MainMenu(BlankPanel panel){
		this.panel = panel;
		this.buttons = new Rectangle[numbOfButtons];
		this.selectedButton = Integer.MAX_VALUE;
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

		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new Rectangle(x,y,recWidth,recHeight);
			y += recHeight + buttonGap;

		}
	}

	/**
	 *
	 * */
	public void render(Graphics g){
		drawButtons(g);
	}

	public void drawButtons(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.red);

		for(int i = 0; i < buttons.length; i++){
			g2d.fill(buttons[i]);
			if(selectedButton == i){
				g.setColor(Color.black);
				g2d.fill(buttons[i]);
				g.setColor(Color.red);
			}
		}
	}



	/**
	 *
	 * */
	public void handleMouseMoved(MouseEvent e){

		//set selected button
		for(int i = 0; i < buttons.length; i++){
			if(buttons[i].contains(e.getX(), e.getY())){
				selectedButton = i;//set selected button
				return;
			}
			selectedButton = Integer.MAX_VALUE;//no button is selected
		}
	}

	/**
	 * This method is called when the mouse is released in the main menu
	 * */
	public void handleMouseReleased(MouseEvent e){
		System.out.println("Mouse Released");

		switch(selectedButton){
			case 0: System.out.println("Single Player");
				return;
			case 1: System.out.println("Multiplayer");
				return;
			case 2 : panel.setMenu(new OptionMenu(panel));
				return;
			case 3 : System.out.println("Help");
				return;
			case 4 : System.exit(0);
				return;
		}
	}


}
