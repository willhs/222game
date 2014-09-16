package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

import java.awt.Canvas;
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
	private Font helpTextFont;

	private Rectangle textBox;

	public HelpMenu(BlankPanel panel){
		this.panel = panel;
		this.numbOfButtons = 1;
		this.selectedButton = Integer.MAX_VALUE;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.helpTextFont = new Font("arial",Font.BOLD,15);

		loadImages();
		setupButtons();
		setUpTextBox();
		readHelpText();
	}


	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		buttons[0] = new Rectangle(50,50,200,50);

		buttonNames[0] = "Back";

	}

	private void setUpTextBox(){
		int gap = 50;//the gap between button and text box
		int x = buttons[0].x + buttons[0].width + gap;
		int y = buttons[0].y;

		int width = GameWindow.FRAME_WIDTH - (x*2);
		int height = GameWindow.FRAME_HEIGHT - (y*2);

		textBox = new Rectangle(x,y,width,height);

	}


	@Override
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.drawImage(backgroundImage, 0, 0,panel);
		drawButtons(g);

		g2d.draw(textBox);
		g2d.setColor(new Color(0f,0f,0f,0.6f));
		g2d.fill(textBox);


		g.setColor(Color.white);
		g.setFont(helpTextFont);
		drawString(g,helpText, (int)(textBox.getX() + 20),(int)( textBox.getY() + 20));//draws the string within the text box
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
	/**
	 * Draws the string on the graphics object splitting the string when a \n
	 * pattern is reached
	 * */
	void drawString(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
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
	 * Reads in the help text for the menu from a file checks to make sure the text
	 * is within bounds of the text box
	 * */
	public void readHelpText(){
		this.helpText = "";
		Canvas canvas = new Canvas();

		InputStreamReader helpTextFile = new InputStreamReader(HelpMenu.class.getResourceAsStream("resources/helpText.txt"));
		BufferedReader textReader = new BufferedReader(helpTextFile);

		int curLineWidth = 0;
		try {

			int i = textReader.read();//reads a single character
			char c = (char)i;

			while(i != -1){//not the end of input stream

				if(curLineWidth >= (textBox.width - 40)){//check if we should place a new line character
					helpText = helpText.concat("\n");
					curLineWidth = 0;//reset the line width
				}

				helpText = helpText.concat(""+c);//adds the char at the end of the string


				i = textReader.read();
				c = (char)i;

				if(c == '\n'){
					curLineWidth = 0;
				}

				int charWidth = canvas.getFontMetrics(helpTextFont).stringWidth(""+c);
				curLineWidth += charWidth;// TODO use graphics object to get  font metrics
			}
		} catch (IOException e) {
			System.out.println("Failed at reading HelpText File");
			e.printStackTrace();
		}


	}
}
