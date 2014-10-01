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

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.keyInputManagment;

public class CharacterSelectionMenu implements GraphicsPane{
	private BufferedImage backgroundImage;
	
	
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
	private boolean nameBoxSelected;
	private String name = "";
	
	private BlankPanel panel;
	/**
	 * Constructor for the character selection menu 
	 * */
	public CharacterSelectionMenu(BlankPanel panel){
		this.panel = panel;
		setUpCharacterBoxes();
		loadImages();
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
		drawBoxes(g);// TODO Auto-generated method stub
		
	}
	
	public void drawBoxes(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(backgroundImage, 0, 0,panel);

		
		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);
		
		nameBox.x = g.getFontMetrics(myFont).stringWidth(label) + startX;
		g2d.setColor(Color.white);
		g2d.draw(nameBox);
	//	g2d.setColor(Color.black);
		g2d.draw(nameBox);
		
		//g2d.drawString(label, nameBox.x + ((nameBox.width/2) - g.getFontMetrics(myFont).stringWidth(label)/2), (int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		g2d.drawString(label, startX, (int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		
		//displayes the name being typed on the screen
		g2d.drawString(name, startX + g.getFontMetrics(myFont).stringWidth(label) + 5,(int) ((nameBox.y + nameBox.getHeight() - (g.getFontMetrics(myFont).getHeight()/2)) ));
		
		g2d.draw(character1);
		g2d.draw(character2);
		g2d.draw(character3);

		
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
//		if(character1.contains(x,y)){
//			characterSelected = 1;
//		}
//		else if(character2.contains(x,y)){
//			characterSelected = 2;
//		}
//		else if(character3.contains(x,y)){
//			characterSelected = 3;
//		}
		 if(nameBox.contains(x,y)){
			 System.out.println("Selected");
			nameBoxSelected = true;
			return;
		}
		nameBoxSelected = false;
	
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(nameBoxSelected){
			if(keyEvent.equals("backspace")){
				name = name.substring(0, name.length()-1);//take one char of the string
				return;
			}
			KeyEvent e = keyInputManagment.getLastKeyEvent();
			String key = KeyEvent.getKeyText(e.getExtendedKeyCode());//TODO may need to fix
			
			//should make it a number or letter i think
			if(key.length() == 1){
				System.out.println("Concatening");
				name = name.concat(key);//add to the end of the string
			}
			System.out.println(key.length() + " " + key);
		}
		
		System.out.println(name);
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

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
