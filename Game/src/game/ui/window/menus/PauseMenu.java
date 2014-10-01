package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

public class PauseMenu implements GraphicsPane {
	private GameScreen game;
	private Rectangle[] buttons;
	private String[] buttonNames;
	private BlankPanel panel;

	//numb of buttons to be created
	private int numbOfButtons;

	//currently selected button by the mouse
	private int selectedButton;


	/**
	 * The constructor for the pause menu sets up the pause menu to be rendered
	 * on the screen.
	 * */
	public PauseMenu(BlankPanel panel, GameScreen game){
		this.game = game;
		this.numbOfButtons = 4;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.panel = panel;
		this.selectedButton = -1;
		setUpButtons();
	}


	/**
	 * Sets up all of the button locations for the pause menu
	 * */
	public void setUpButtons(){
		int y = GameWindow.FRAME_HEIGHT/numbOfButtons;
		int buttonGap = 20;
		int recHeight = 50;
		int recWidth = 200;
		int x = (GameWindow.FRAME_WIDTH/2)- (recWidth/2);//Center the buttons

		//create buttons represented by rectangles
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
		//draws the outside of the menu
		drawFrame(g);

		//drawButtons(g);
		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
	}

	public void drawFrame(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		int borderGap = 20;
		int x = buttons[0].x - borderGap;
		int y = buttons[0].y - borderGap;

		int width = (int)( buttons[buttons.length-1].getWidth() + borderGap*2);
		int height = (int)(buttons[buttons.length-1].getMaxY() - y + borderGap);

		//draws the rectangle around the button
		g.setColor(new Color(0f,0f,0f,0.5f));
		g.fillRect(x, y, width, height);

		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));

		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		g2d.setStroke(oldStroke);//reset the border size
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
		selectedButton = -1;//no button is selected
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedButton == 0){//resume game button
			game.setMenu(null);//set to null so no menu shows up on the game screen
		}
		else if(selectedButton == 1){//options button
			System.out.println("options"); //TODO
		}
		else if(selectedButton == 2){//main menu button
			panel.setMenu(new MainMenu(panel));
		}
		else if(selectedButton == 3){//quit button
			System.exit(0);//exit the game
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape")){
			System.out.println("Escape");
			game.setMenu(null);
		}
		else if(keyEvent.equals("enter")){
			handleMouseReleased(null);//TODO buttonPressed();
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			selectedButton = MenuUtil.moveButtonSelectionDown(selectedButton, buttons.length);
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			selectedButton = MenuUtil.moveButtonSelectionUp(selectedButton, buttons.length);
		}
	}


	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
