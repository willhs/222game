package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 * @author Nicky van HUlst
 * */


public class MainMenu implements GraphicsPane{
	private final int numbOfButtons = 5;
	private BlankPanel panel;
	private GameWindow window;

	private int selectedButton;
	private BufferedImage backgroundImage;


	private Rectangle[] buttons;
	 private String[] buttonNames;


	public MainMenu(BlankPanel panel){
		this.panel = panel;
		buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.selectedButton = -1;
		loadImages();
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
		g.drawImage(backgroundImage, 0, 0,panel);
		//drawBackGroundImage(g);
		drawButtons(g);
	}

	public void drawButtons(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);

		for(int i = 0; i < buttons.length; i++){
			g2d.setColor(new Color(1f,1f,1f,0.1f));
			g2d.fill(buttons[i]);
			g2d.setColor(Color.black);
			g2d.draw(buttons[i]);

			if(selectedButton == i){
				g2d.setColor(new Color(0f,0f,0f,0.5f));
				g2d.fill(buttons[i]);
			}
			//draws the string in the centre of the current button
			g.setColor(Color.white);
			g2d.drawString(buttonNames[i], buttons[i].x + ((buttons[i].width/2) - g.getFontMetrics(myFont).stringWidth(buttonNames[i])/2), (int) ((buttons[i].y + buttons[i].getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
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
			selectedButton = -1;//no button is selected
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent e){
		buttonPressed();

	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("enter")){
			buttonPressed();
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			moveSelectionDown();
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			moveSelectionUp();
		}
	}

	public void moveSelectionDown(){
		if(selectedButton == -1 || selectedButton == 0 ){
			selectedButton = buttons.length-1;
		}
		else{
			selectedButton--;
		}
	}

	public void moveSelectionUp(){
		if(selectedButton == -1 || selectedButton == buttons.length-1 ){
			selectedButton = 0;
		}
		else{
			selectedButton++;
		}
	}

	private void buttonPressed(){
		switch(selectedButton){
		case 0: panel.setMenu(new GameScreen(panel));
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
