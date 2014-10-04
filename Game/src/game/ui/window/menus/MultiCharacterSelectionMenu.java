package game.ui.window.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.net.ssl.HostnameVerifier;

import nw.Client;
import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.keyInputManagment;
import game.world.model.Player;

/**
 * @author Nicky van Hulst
 * */
public class MultiCharacterSelectionMenu extends CharacterSelectionMenu {

	private Rectangle textFieldPort;
	private boolean portSelected;
	private Rectangle textFieldHostName;
	private boolean hostSelected;

	private String host = "Host Name :";
	private String port = "Port Number :";

	private textBoxWrapper hostString = new textBoxWrapper("");
	private textBoxWrapper portString = new textBoxWrapper("");

	private int startX = 20;
	private int startY = 70;
	private int maxTextLenght = 30;


	public MultiCharacterSelectionMenu(BlankPanel panel) {
		super(panel);

		textFieldHostName = new Rectangle(startX,startY,200,30);
		textFieldPort = new Rectangle(startX+ 200 + 20,startY,200,30);
	}

	@Override
	public void render(Graphics g){
		super.render(g);

		//draws the texts fields the user can enter information into
		drawTextFields(g);

		//draw the string if there is an error
		drawErrorString(g);
	}


	/**
	 *Draws the String on the screen telling the user about the current
	 *error
	 * */
	public void drawErrorString(Graphics g){
		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);
		g.setColor(Color.red);
		g.drawString(error, (int) ((int) (buttons[1].getX()+5) + buttons[1].getWidth()), (int) ((buttons[1].y + buttons[1].getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g.setColor(Color.white);
	}


	/**
	 *Draws the text fields for the port number and the hostname
	 *on the graphics object
	 * */
	public void drawTextFields(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//set the font
		Font myFont = new Font("arial",0,15);
		g.setFont(myFont);

		//set the x value for the recatangle based on the size of the label infront of it and draw the label
		textFieldHostName.x = g.getFontMetrics(myFont).stringWidth(host) + startX;
		g.drawString(host, startX, (int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));

		//if the host textbox is sellected make it blue and draw it or just draw it
		if(hostSelected)g.setColor(Color.blue);
		g2d.draw(textFieldHostName);
		g.setColor(Color.white);

		//draw the string that the user is typing in the box  centred on the rectangle
		g2d.drawString(hostString.s, startX + g.getFontMetrics(myFont).stringWidth(host) + 5,(int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		//work out where the port string x should be drawn
		int portStartX = (int) (textFieldHostName.getX() + textFieldHostName.getWidth() + 5);

		//set the rectangle x value for the port textbox
		textFieldPort.x = g.getFontMetrics(myFont).stringWidth(port) + startX + 200 + g.getFontMetrics(myFont).stringWidth(host) + 5 ;

		//draw the port text box label and the string the user has typed
		g.drawString(port, portStartX, (int) ((textFieldPort.y + textFieldPort.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g2d.drawString(portString.s, (int) (startX + g.getFontMetrics(myFont).stringWidth(port) + textFieldHostName.getWidth() + g.getFontMetrics(myFont).stringWidth(host) + 10) ,(int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		//if the port textbox is sellected make it blue and draw it or just draw it
		if(portSelected)g.setColor(Color.blue);
		g2d.draw(textFieldPort);
		g.setColor(Color.white);
	}

	@Override
	public void okPressed(){
		//check with the server that the name is ok

		//make sure a all the fields have something entered
		if(portString.s.length() == 0 || hostString.s.length() == 0 || super.name.length() == 0 ){
			super.error = "Required fields are not filled in!";
			return;
		}

		//check a character has been selected
		if(super.characterSelected == -1){
			this.error = "No character selected please select a character!";
			return;
		}


		int portNumb = -1;
		//check the port number is a number
		try {
			 port = port.trim();
			 portNumb = Integer.parseInt(portString.s);
		} catch (NumberFormatException e) {
			error = "Port number is incorrect format ";
			System.out.println("Error converting to int");
			return;
		}


		//no errors at this point so create the gane player and client and change menu
		Player player = new Player(super.name);
		Client client = new Client(hostString.s,portNumb,panel);
		if(!player.addPlayerToWorld(player)){
			//Player name was taken.
		}
		System.out.println("Creating : " + "Name :"+ name + " Host :" + hostString.s + " Port :" + portNumb );
		panel.setMenu(new GameScreen(panel, client,player));
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		//reset the fields that are selected
		portSelected = false;
		hostSelected = false;
		super.handleMouseReleased(e);

		//check if the host text box is being selected
		if(textFieldHostName.contains(e.getX(),e.getY())){
			hostSelected = true;
			portSelected = false;
		}

		//check if the port text box is being selected
		if(textFieldPort.contains(e.getX(),e.getY())){
			portSelected = true;
			hostSelected = false;
		}
	}

	
	@Override
	public void keyPressed(String keyEvent) {
		KeyEvent e = keyInputManagment.getLastKeyEvent();

		if(e.getKeyCode() == KeyEvent.VK_TAB){
			handleTabPress();
		}
		if(super.nameBoxSelected){
			handleNameBoxKeyPress(keyEvent);
		}
		else if(hostSelected){
			handleTextBoxKeyPress(keyEvent, hostString);
		}
		else if(portSelected){
			handleTextBoxKeyPress(keyEvent, portString);
		}
		else if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			panel.setMenu(new MainMenu(panel));
		}
	}


	/**
	 *Handles the text being entered from a user into a text box
	 * */
	public void handleTextBoxKeyPress(String keyEvent, textBoxWrapper text){
		KeyEvent e = keyInputManagment.getLastKeyEvent();
		String textBox = text.s;

		//grabs the last key event that occoured
		String key = KeyEvent.getKeyText(e.getExtendedKeyCode());//TODO may need to fix
		
		//should make it a number or letter i think
		if(key.length() == 1 ){
			textBox = textBox.concat(key).toLowerCase();//add to the end of the string
			text.s = textBox;
		}
		else{
			handleSpecialKeyPress(e,text);//the key pressed is not a character
		}
	}


	/**
	 * Switches which text box is selected depending on which one is currently selected
	 * */
	public void handleTabPress(){
		if(nameBoxSelected){
			hostSelected = true;
			nameBoxSelected = false;
		}
		else if(hostSelected){
			portSelected = true;
			hostSelected = false;
		}
		else if(portSelected){
			nameBoxSelected = true;
			portSelected = false;
		}
	}


	/**
	 * checks which special key is pressed an performs the appropriate action
	 * */
	public void handleSpecialKeyPress(KeyEvent e, textBoxWrapper text){
		String textBox = text.s;

		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(textBox.length() == 0)return;//make sure we dont try and shorten an empty string
			textBox = textBox.substring(0, textBox.length()-1);//take one char of the string
			text.s = textBox;
			return;
		}

		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			textBox = textBox.concat(" ").toLowerCase();
			text.s = textBox;
			return;
		}

		 if(e.getKeyCode() == KeyEvent.VK_MINUS){
			System.out.println("MINUS");
			textBox = textBox.concat("-");
			text.s = textBox;
			return;
		}

		 if(e.getKeyCode() == KeyEvent.VK_PERIOD){
			System.out.println("PERIOD");
			textBox = textBox.concat(".");
			text.s = textBox;
			return;
		}

		 if(e.getKeyCode() == KeyEvent.VK_SLASH){
			System.out.println("Slash");
			textBox = textBox.concat("/");
			text.s = textBox;
			return;
		}
	}

	//class the wrap a string to send by reference
	class textBoxWrapper{
		public String s;
		public textBoxWrapper(String s ){
			this.s = s;
		}
	}
}
