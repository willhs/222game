package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/**
 * @author Nicky van Hulst 300295657
 * */
public class PauseMenu implements GraphicsPane {
	private GameScreen game;
	private Rectangle[] buttons;
	private String[] buttonNames;
	private BlankPanel panel;

	//numb of buttons to be created
	private int numbOfButtons;

	//currently selected button by the mouse
	private int selectedButton;

	private GraphicsPane currentMenu;
	private StarMation starmation ;


	/**
	 * The constructor for the pause menu sets up the pause menu to be rendered
	 * on the screen.
	 * */
	public PauseMenu(BlankPanel panel, GameScreen game, StarMation starmation){
		this.game = game;
		this.starmation = starmation;
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
	private void setUpButtons(){
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
		buttonNames[1] = "Help";
		buttonNames[2] = "Main Menu";
		buttonNames[3] = "Quit";
	}


	@Override
	public void render(Graphics g) {
		if(currentMenu != null){
			currentMenu.render(g);
			return;
		}

		//draws the outside of the menu
		drawFrame(g);

		//drawButtons(g);
		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
	}


	/**
	 * Draws the frame around the pause menu
	 * */
	private void drawFrame(Graphics g){
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
		if(currentMenu !=null){
			currentMenu.handleMouseReleased(e);
			return;
		}
		if(selectedButton == 0){//resume game button
			game.setMenu(null);//set to null so no menu shows up on the game screen
		}
		else if(selectedButton == 1){//options button
			currentMenu = new BasicHelpMenu(panel, starmation, game, this);
		}
		else if(selectedButton == 2){//main menu button
			game.getClient().quit();
			panel.setMenu(new MainMenu(panel));
		}
		else if(selectedButton == 3){//quit button
			game.getClient().quit();
			System.exit(0);//exit the game
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape")){
			game.setMenu(null);
		}
		else if(keyEvent.equals("enter")){
			handleMouseReleased(null);
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			selectedButton = MenuUtil.moveButtonSelectionDown(selectedButton, buttons.length);
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			selectedButton = MenuUtil.moveButtonSelectionUp(selectedButton, buttons.length);
		}
	}
	
	
	/**
	 * Sets the current Menu
	 * */
	public void setMenu(GraphicsPane menu){
		this.currentMenu = menu;
	}


	@Override
	public void handleMousePressed(MouseEvent e) {}
}
