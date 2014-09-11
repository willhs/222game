package game.ui.window;

import java.awt.Color;
import java.awt.Font;
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
	String[] buttonNames;


	public MainMenu(BlankPanel panel){
		this.panel = panel;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
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

		//name the buttons
		buttonNames[0] = "Single Player";
		buttonNames[1] = "Multiplayer";
		buttonNames[2] = "Options";
		buttonNames[3] = "Help";
		buttonNames[4] = "Quit";
	}

	/**
	 *
	 * */
	public void render(Graphics g){
		drawButtons(g);
	}

	public void drawButtons(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);

		for(int i = 0; i < buttons.length; i++){
			g2d.setColor(new Color(1f,0f,0f,0.1f ));
			g2d.fill(buttons[i]);
			g2d.setColor(Color.black);
			g2d.draw(buttons[i]);

			if(selectedButton == i){
				g.setColor(Color.black);
				g2d.fill(buttons[i]);
				g.setColor(Color.red);
			}
			g.setColor(Color.white);
			g2d.drawString(buttonNames[i], buttons[i].x + 20, buttons[i].y + 25);
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
			selectedButton = Integer.MAX_VALUE;//no button is selected
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent e){

		switch(selectedButton){
			case 0: System.out.println("Single Player");
				return;
			case 1: System.out.println("Multiplayer");
				return;
			case 2 : panel.setMenu(new OptionMenu(panel));
				return;
			case 3 : panel.setMenu(new HelpMenu(panel));
				return;
			case 4 : System.exit(0);
				return;
		}
	}


	@Override
	public void keyPressed(String keyEvent) {

	}


}
