package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class PauseMenu implements GraphicsPane {

	private Rectangle[] buttons;
	private String[] buttonNames;
	private BlankPanel panel;

	private int numbOfButtons;
	private int selectedButton;
	private GameScreen game;


	public PauseMenu(BlankPanel panel, GameScreen game){
		this.game = game;
		this.numbOfButtons = 4;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.panel = panel;
		setUpButtons();

	}

	/**
	 * Sets up all of the button locations for the main menu
	 * */
	public void setUpButtons(){

		int y = GameWindow.FRAME_HEIGHT/numbOfButtons;

		int buttonGap = 20;

		int recHeight = 50;
		int recWidth = 200;

		int x = (GameWindow.FRAME_WIDTH/2)- (recWidth/2);

		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new Rectangle(x,y,recWidth,recHeight);
			y += recHeight + buttonGap;
		}

		//name the buttons
		buttonNames[0] = "Resume";
		buttonNames[1] = "Options";
		buttonNames[2] = "Main Menu";
		buttonNames[3] = "Quit";
	}

	@Override
	public void render(Graphics g) {
		//does not clear the screen as the game is running in the background

		int borderGap = 20;
		//draw
		int x = buttons[0].x - borderGap;
		int y = buttons[0].y - borderGap;

		int width = (int)( buttons[buttons.length-1].getWidth() + borderGap*2);
		int height = (int)(buttons[buttons.length-1].getMaxY() - y + borderGap);



		g.setColor(new Color(0.5f,0.5f,0.5f,0.1f));
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);

		drawButtons(g);

		//the pauze game menu will only show up when the player is in game


	}

	public void drawButtons(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);


		for(int i = 0; i < buttons.length; i++){
			g2d.setColor(new Color(1f,1f,1f,0.1f ));
			g2d.fill(buttons[i]);
			g2d.setColor(Color.black);
			g2d.draw(buttons[i]);

			if(selectedButton == i){
				g.setColor( new Color(0f,0f,0f,0.5f));
				g2d.fill(buttons[i]);
			}
			g.setColor(Color.white);
			g.drawString(buttonNames[i], buttons[i].x + 20, buttons[i].y + 25);
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
		}
		selectedButton = Integer.MAX_VALUE;//no button is selected
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedButton == 0){
			game.setMenu(null);//so no menu shows up on the game screen
		}
		else if(selectedButton == 1){
			System.out.println("options");
		}
		else if(selectedButton == 2){
			panel.setMenu(new MainMenu(panel));
		}
		else if(selectedButton == 3){
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape")){
			System.out.println("Escape");
			game.setMenu(null);
		}
	}
}
