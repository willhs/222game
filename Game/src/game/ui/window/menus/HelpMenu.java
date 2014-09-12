package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class HelpMenu implements GraphicsPane {
	private BlankPanel panel;
	private int numbOfButtons;

	private Rectangle[] buttons;
	private String[] buttonNames;

	private int selectedButton;
	private BufferedImage backgroundImage;

	private String helpText;

	public HelpMenu(BlankPanel panel){
		this.panel = panel;
		this.numbOfButtons = 1;
		this.selectedButton = Integer.MAX_VALUE;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];

		loadImages();
		setupButtons();
		readHelpText();

	}


	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		buttons[0] = new Rectangle(50,50,200,50);

		buttonNames[0] = "Back";

	}


	@Override
	public void render(Graphics g){
		g.drawImage(backgroundImage, 0, 0,panel);
		drawButtons(g);
		g.drawString("HELP", GameWindow.FRAME_WIDTH/2, GameWindow.FRAME_HEIGHT/2);
	}

	private void drawButtons(Graphics g){
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
			selectedButton = Integer.MAX_VALUE;//no button is selected
		}
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedButton == 0){
			panel.setMenu(new MainMenu(panel));
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape")){
			panel.setMenu(new MainMenu(panel));
		}
	}

	public void loadImages(){
		java.net.URL imagefile = MainMenu.class.getResource("resources/bocks.jpg");


		//load background image
		try {
			this.backgroundImage = ImageIO.read(imagefile);
			backgroundImage.getScaledInstance(GameWindow.FRAME_WIDTH, GameWindow.FRAME_HEIGHT, BufferedImage.SCALE_DEFAULT);
		} catch (IOException e) {
			System.out.println("failed reading image");
			e.printStackTrace();
		}
	}

	/**
	 * Reads in the help text for the menu from a file
	 * */
	public void readHelpText(){
		helpText = "";

		InputStreamReader helpTextFile = new InputStreamReader(HelpMenu.class.getResourceAsStream("HelpText.txt"));

		BufferedReader textReader = new BufferedReader(helpTextFile);

		try {
			while(textReader.read()  == -1){

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
