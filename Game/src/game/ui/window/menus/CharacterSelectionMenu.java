package game.ui.window.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import nw.Client;
import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.keyInputManagment;
import game.world.dimensions.Point3D;
import game.world.model.Key;
import game.world.model.Player;

public class CharacterSelectionMenu implements GraphicsPane{
	private BufferedImage backgroundImage;


	//button fields
	private int selectedButton;

	protected Rectangle[] buttons;
	private String[] buttonNames;
	private int numbOfButtons = 2;


	private Rectangle nameBox;

	private Rectangle character1;
	private Rectangle character2;
	private Rectangle character3;

	int characterSelected = -1;

	//
	private int startX = 20;
	private int startY = 20;
	private int boxWidth = GameWindow.FRAME_WIDTH-startX*2;
	private int gap = 40;
	private int height = 150;

	private String label = "Enter Name : ";
	protected boolean nameBoxSelected;
	protected String name = "";
	private boolean nextCap;
	private int maxNameLength = 30;

	protected String error = " ";

	protected BlankPanel panel;

	/**
	 * Constructor for the character selection menu
	 * */
	public CharacterSelectionMenu(BlankPanel panel){
		this.panel = panel;
		this.buttonNames = new String[numbOfButtons];
		this.buttons = new Rectangle[numbOfButtons];
		this.selectedButton = -1;

		setUpButtons();
		setUpCharacterBoxes();
		loadImages();
	}

	public void setUpButtons(){
		int width = 200;
		int height = 50;

		int y = startY+ 100 + ((height+gap)*6);

		buttons[0] = new Rectangle(startX,y,width,height);
		buttons[1] = new Rectangle(startX+width+20,y,width,height);

		buttonNames[0] = "Back";
		buttonNames[1] = "Ok";
	}

	public void setUpCharacterBoxes(){
		int y = startY;
		nameBox = new Rectangle(startX,y,boxWidth,40);

		y+=gap+50;

		character1 = new Rectangle(startX,y,boxWidth,height);
		y+=gap+height;
		character2 = new Rectangle(startX,y,boxWidth,height);
		y+=gap+height;
		character3 = new Rectangle(startX,y,boxWidth,height);


	}

	@Override
	public void render(Graphics g) {
		g.drawImage(backgroundImage, 0, 0,panel);

		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
		drawBoxes(g);// TODO Auto-generated method stub

	}

	public void drawBoxes(Graphics g){
		Graphics2D g2d = (Graphics2D)g;


		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);

		nameBox.x = g.getFontMetrics(myFont).stringWidth(label) + startX;
		g2d.setColor(Color.white);
		if(nameBoxSelected)g2d.setColor(Color.blue);
		g2d.draw(nameBox);
		g2d.setColor(Color.white);

		//g2d.drawString(label, nameBox.x + ((nameBox.width/2) - g.getFontMetrics(myFont).stringWidth(label)/2), (int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g2d.drawString(label, startX, (int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));

		//displayes the name being typed on the screen
		g2d.drawString(name, startX + g.getFontMetrics(myFont).stringWidth(label) + 5,(int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		if(characterSelected == 1)g2d.setColor(Color.blue);
		g2d.draw(character1);
		g2d.setColor(Color.white);
		if(characterSelected == 2)g2d.setColor(Color.blue);
		g2d.draw(character2);
		g2d.setColor(Color.white);
		if(characterSelected == 3)g2d.setColor(Color.blue);
		g2d.draw(character3);
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
			 System.out.println("Selected");
			nameBoxSelected = true;
			return;
		}
		nameBoxSelected = false;

	}

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("enter")){
			buttonPressed();
		}
		if(nameBoxSelected){
			handleNameBoxKeyPress(keyEvent);
		}
		else if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			panel.setMenu(new MainMenu(panel));
		}
	}

	public void handleNameBoxKeyPress(String keyEvent){
		if(keyEvent.equals("backspace")){
			if(name.length() == 0)return;//make sure we dont try and shortan an empty string

			name = name.substring(0, name.length()-1);//take one char of the string
			return;
		}
		if(name.length() > maxNameLength)return;//max size of name
		KeyEvent e = keyInputManagment.getLastKeyEvent();
		String key = KeyEvent.getKeyText(e.getExtendedKeyCode());//TODO may need to fix

		//should make it a number or letter i think
		if(key.length() == 1 || keyEvent.equals("space")){
			if(keyEvent.equals("space")){
				name = name.concat(" ");
				nextCap = true;
				return;
			}

			if(name.length() == 0 )nextCap = true;

			if(!nextCap)key = key.toLowerCase();
			name = name.concat(key);//add to the end of the string
			nextCap = false;
		}
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 *Decides what should happen when a button is pressed
	 * */
	private void buttonPressed(){
		switch(selectedButton){
		case 0: panel.setMenu(new MainMenu(panel));
			return;
		case 1: okPressed();;
			return;
		}
	}

	/**
	 * The ok button is pressed check if user has entered required fields
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
		//player.setImage("char1");
		player.addItem(new Key("Epic key of Awesome", new Point3D(0, 0, 0)));
		player.addItem(new Key("Epic key of Awesome 2", new Point3D(0, 0, 0)));

		Client client = new Client(player,panel);
		panel.setMenu(new GameScreen(panel, client,player));
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

}
