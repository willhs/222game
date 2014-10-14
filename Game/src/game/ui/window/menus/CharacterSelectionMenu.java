package game.ui.window.menus;

import game.ui.render.Res;
import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;
import game.ui.window.keyInputManagment;
import game.world.model.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import nw.Client;
/**
 * @author Nicky van Hulst 300294657
 * */
public class CharacterSelectionMenu implements GraphicsPane{

	//button fields
	private int selectedButton;

	//buttons fields
	protected Rectangle[] buttons;
	private String[] buttonNames;
	private int numbOfButtons = 2;

	//fields for the name text box
	private Rectangle nameBox;
	private String label = "Enter Name : ";
	protected boolean nameBoxSelected;
	protected String name = "";
	private boolean nextCap;
	private int maxNameLength = 30;

	//the character selection rectangles
	private Rectangle character1;
	private Rectangle character2;
	private Rectangle character3;

	//the character that is selected
	int characterSelected = -1;

	private int startX = 20;
	private int startY = 20;
	private int boxWidth = GameWindow.FRAME_WIDTH-startX*2;
	private int gap = 40;
	private int height = 150;

	//error message for the user
	protected String error = " ";

	//panel to draw on
	protected BlankPanel panel;

	//the background animation
	private StarMation starMation;


	/**
	 * Constructor for the character selection menu
	 * */
	public CharacterSelectionMenu(BlankPanel panel){
		this.starMation = MainMenu.getStarMation();
		this.panel = panel;
		this.buttonNames = new String[numbOfButtons];
		this.buttons = new Rectangle[numbOfButtons];
		this.selectedButton = -1;

		setUpButtons();
		setUpCharacterBoxes();
	}


	/**
	 * Sets up the buttons for the menu
	 * */
	public void setUpButtons(){
		int width = 200;
		int height = 50;

		int y = startY+ 100 + ((height+gap)*6);

		buttons[0] = new Rectangle(startX,y,width,height);
		buttons[1] = new Rectangle(startX+width+20,y,width,height);

		buttonNames[0] = "Back";
		buttonNames[1] = "Ok";
	}


	/**
	 *Sets up the characters selection boxes
	 * */
	public void setUpCharacterBoxes(){
		int y = startY;
		nameBox = new Rectangle(startX,y,boxWidth-120,40);

		y+=gap+50;
		character1 = new Rectangle(startX,y,boxWidth,height);
		y+=gap+height;
		character2 = new Rectangle(startX,y,boxWidth,height);
		y+=gap+height;
		character3 = new Rectangle(startX,y,boxWidth,height);
	}

	@Override
	public void render(Graphics g) {
		starMation.render(g);

		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
		drawBoxes(g);

		drawErrorString(g);
	}

	/**
	 *Draws the String on the screen telling the user about the current
	 *error
	 * */
	private void drawErrorString(Graphics g){
		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);
		g.setColor(Color.red);
		g.drawString(error, (int) ((int) (buttons[1].getX()+5) + buttons[1].getWidth()), (int) ((buttons[1].y + buttons[1].getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g.setColor(Color.white);
	}


	/**
	 * Draws the character selection boxes on the screen
	 * */
	public void drawBoxes(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//set the font
		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);

		nameBox.x = g.getFontMetrics(myFont).stringWidth(label) + startX;
		g2d.setColor(Color.white);
		if(nameBoxSelected)g2d.setColor(Color.blue);
		g2d.draw(nameBox);
		g2d.setColor(Color.white);

		g2d.drawString(label, startX, (int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));

		//Displays the name being typed on the screen
		g2d.drawString(name, startX + g.getFontMetrics(myFont).stringWidth(label) + 5,(int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		drawCharacter(1, character1, g2d);
		drawCharacter(2, character2, g2d);
		drawCharacter(3, character3, g2d);
	}


	/**
	 * Draws one of the character selection boxes on the screen
	 * */
	public void drawCharacter(int charNum, Rectangle character,Graphics  g){
		Graphics2D g2d = (Graphics2D)g;

		if(charNum == characterSelected)g.setColor(Color.blue);
		g2d.draw(character);
		g2d.drawRect((int)character.getX()+20, (int)character.getY()+10, (int)character.getHeight()-20, (int)character.getHeight()-20);
		g.drawImage(MenuUtil.scale(Res.getImageFromName("Char"+charNum),(int)character.getHeight()-20,(int)character.getHeight()-20),(int)character.getX()+20,(int)character.getY()+5, panel);
		g2d.setColor(Color.white);
	}


	@Override
	public void handleMouseMoved(MouseEvent e) {
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
	public void handleMouseReleased(MouseEvent e) {
		buttonPressed();

		int x = e.getX();
		int y = e.getY();

		//set the selected character or name box
		if(character1.contains(x,y)){
			characterSelected = 1;
		}
		else if(character2.contains(x,y)){
			characterSelected = 2;
		}
		else if(character3.contains(x,y)){
			characterSelected = 3;
		}
		 if(nameBox.contains(x,y)){
			nameBoxSelected = true;
			return;
		}
		nameBoxSelected = false;
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("enter")){
			//buttonPressed();
			okPressed();
		}
		if(nameBoxSelected){
			handleNameBoxKeyPress(keyEvent);
		}
		else if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			panel.setMenu(new MainMenu(panel));
		}
		else if(keyInputManagment.getLastKeyEvent().getKeyCode()  == KeyEvent.VK_TAB && !nameBoxSelected){
			nameBoxSelected = true;
		}
	}


	/**
	 * Performs action based on the key that is pressed
	 * */
	public void handleNameBoxKeyPress(String keyEvent){
		if(keyEvent.equals("backspace")){
			if(name.length() == 0)return;//make sure we dont try and shortan an empty string
			name = name.substring(0, name.length()-1);//take one char of the string
			return;
		}

		if(name.length() > maxNameLength)return;//max size of name

		//get the last key event
		KeyEvent e = keyInputManagment.getLastKeyEvent();
		String key = KeyEvent.getKeyText(e.getExtendedKeyCode());

		//check if its a number or letter
		if(key.length() == 1 || keyEvent.equals("space")){
			if(keyEvent.equals("space")){
				name = name.concat(" ");
				nextCap = true;
				return;
			}

			//start with a capitol letter
			if(name.length() == 0 )nextCap = true;

			//change the key to be a lower case
			if(!nextCap)key = key.toLowerCase();

			//add to the end of the string
			name = name.concat(key);
			nextCap = false;
		}
	}


	/**
	 *Decides what should happen when a button is pressed
	 * */
	private void buttonPressed(){
		switch(selectedButton){
		case 0: panel.setMenu(new MainMenu(panel));
			return;
		case 1: okPressed();
			return;
		}
	}


	/**
	 * The OK button is pressed check if user has entered required fields
	 * and create player and client
	 * */
	public void okPressed(){
		//check with the server that the name is ok

		//make sure a name is entered
		if(name.length() == 0){
			this.error = "No name name entered please enter a name!";
			return;
		}
		if(characterSelected == -1){
			this.error = "No character selected please select a character!";
			return;
		}

		Player player = new Player(name);
		String imageName = "Char"+characterSelected;
		player.setImageName(imageName);

		Client client = new Client(panel);
		client.addPlayerToWorld(player);
		panel.setMenu(new GameScreen(panel, client,player));
	}

	@Override
	public void handleMousePressed(MouseEvent e) {}
}
