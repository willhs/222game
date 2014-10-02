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

		drawTextFields(g);
	}

	public void drawTextFields(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//set the font
		Font myFont = new Font("arial",0,15);
		g.setFont(myFont);

		textFieldHostName.x = g.getFontMetrics(myFont).stringWidth(host) + startX;
		g.drawString(host, startX, (int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));


		if(hostSelected)g.setColor(Color.blue);
		g2d.draw(textFieldHostName);
		g.setColor(Color.white);

		g2d.drawString(hostString.s, startX + g.getFontMetrics(myFont).stringWidth(host) + 5,(int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		int portStartX = (int) (textFieldHostName.getX() + textFieldHostName.getWidth() + 5);

		textFieldPort.x = g.getFontMetrics(myFont).stringWidth(port) + startX + 200 + g.getFontMetrics(myFont).stringWidth(host) + 5 ;
		g.drawString(port, portStartX, (int) ((textFieldPort.y + textFieldPort.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g2d.drawString(portString.s, (int) (startX + g.getFontMetrics(myFont).stringWidth(port) + textFieldHostName.getWidth() + g.getFontMetrics(myFont).stringWidth(host) + 10) ,(int) ((textFieldHostName.y + textFieldHostName.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));

		if(portSelected)g.setColor(Color.blue);
		g2d.draw(textFieldPort);
		g.setColor(Color.white);
	}

	@Override
	public void okPressed(){
		//check with the server that the name is ok

		//make sure a name is entered
		if(portString.s.length() == 0 || hostString.s.length() == 0 || super.name.length() == 0 ){
			super.error = "No name name entered please enter a name!";
			return;
		}
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

		Player player = new Player(super.name);
		Client client = new Client(player,hostString.s,portNumb,panel);
		System.out.println("Creating : " + "Name :"+ name + " Host :" + hostString.s + " Port :" + portNumb );
		panel.setMenu(new GameScreen(panel, client,player));
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		portSelected = false;
		hostSelected = false;
		super.handleMouseReleased(e);

		if(textFieldHostName.contains(e.getX(),e.getY())){
			hostSelected = true;
			portSelected = false;
		}
		if(textFieldPort.contains(e.getX(),e.getY())){
			portSelected = true;
			hostSelected = false;
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(super.nameBoxSelected){
			handleNameBoxKeyPress(keyEvent);
		}
		else if(hostSelected){
			handleTextBoxKeyPress(keyEvent, hostString);
		}
		else if(portSelected){
			handleTextBoxKeyPress(keyEvent, portString);
		}
	}
	
	
	/**
	 * 
	 * */
	public void handleTextBoxKeyPress(String keyEvent, textBoxWrapper text){
		String textBox = text.s;
		if(keyEvent.equals("backspace")){
			if(textBox.length() == 0)return;//make sure we dont try and shortan an empty string

			textBox = textBox.substring(0, textBox.length()-1);//take one char of the string
			text.s = textBox;
			return;
		}
		if(textBox.length() >  maxTextLenght)return;//max size of name
		KeyEvent e = keyInputManagment.getLastKeyEvent();
		String key = KeyEvent.getKeyText(e.getExtendedKeyCode());//TODO may need to fix

		//should make it a number or letter i think
		if(key.length() == 1 || keyEvent.equals("space")){
			if(keyEvent.equals("space")){
				textBox = textBox.concat(" ").toLowerCase();
				text.s = textBox;
				return;
			}
			textBox = textBox.concat(key).toLowerCase();//add to the end of the string
			text.s = textBox;
		}
	}

	class textBoxWrapper{
		public String s;
		public textBoxWrapper(String s ){
			this.s = s;
		}
	}
}
