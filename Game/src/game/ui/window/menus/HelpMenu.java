package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Nicky van Hulst
 * */
public class HelpMenu implements GraphicsPane, Animated{

	//panel to draw on
	private BlankPanel panel;

	//button fields
	protected Rectangle[] buttons;
	private String[] buttonNames;
	private int numbOfButtons;
	private int selectedButton;

	//help text fields
	protected String helpText;
	private Font helpTextFont;
	protected Rectangle textBox;

	//animation fields
	private boolean animating;
	private boolean animatingIn;
	private boolean setUp;
	private int buttonStartX;
	private int aniSpeed = 1;
	private float speedMultiplyer = 1f;

	//the menu to be drawn after the animation
	private GraphicsPane nextMenu;
	protected StarMation starMation;


	/**
	 * The constructor for the help menu
	 * */
	public HelpMenu(BlankPanel panel, StarMation starMation){
		this.starMation = starMation;
		this.panel = panel;
		this.numbOfButtons = 2;
		this.selectedButton = -1;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.helpTextFont = new Font("arial",Font.BOLD,15);

		setupButtons();
		setUpTextBox();
		readHelpText();
	}


	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		buttons[0] = new Rectangle(50,50,200,50);
		setUpTextBox();
		buttons[1] = textBox;
		buttonNames[0] = "Back";
		buttonNames[1] = "";
	}


	/**
	 * Sets up the text box to display the help text
	 * */
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
		//draws the background image
		//g.drawImage(backgroundImage, 0, 0,panel);
		starMation.render(g);

		//if there is a menu to render in the animation render it
		if(nextMenu!=null){
			if(((Animated) nextMenu).isAnimating()){
				((Animated) nextMenu).animate();
				nextMenu.render(g);
			}
		}

		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
		g.setColor(Color.white);
		g.setFont(helpTextFont);
		drawString(g,helpText, (int) (buttons[1].getX()+20),(int)( textBox.getY() + 20));//draws the string within the text box
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
			if(buttons[0].contains(e.getX(), e.getY())){//TODO currently zero beuase there is only one button
				selectedButton = i;//set selected button
				return;
			}
			selectedButton = -1;//no button is selected
		}
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedButton == 0){
			animating = true;
			nextMenu = new MainMenu(panel);
			((MainMenu)nextMenu).setAnimating(true);
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")  ){
			animating = true;
			nextMenu = new MainMenu(panel);
			((MainMenu)nextMenu).setAnimating(true);
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
				curLineWidth += charWidth;
			}
		} catch (IOException e) {
			System.out.println("Failed at reading HelpText File");
			e.printStackTrace();
		}
	}


	@Override
	public void animate() {
		if(animatingIn){
			if(!setUp){//if the starting x co-ordinate has not been saved
				buttonStartX = (int) buttons[0].getX();
				MenuUtil.setUpAnimation(buttons);//moved the buttons to the end of the screen
				setUp = true;
			}
			if(MenuUtil.animateIn(buttonStartX,buttons, aniSpeed += speedMultiplyer)){
				panel.setMenu(new HelpMenu(panel,starMation));//create the new help menu
			}
		}
		else{
			if(MenuUtil.animateOut(buttons, aniSpeed+= speedMultiplyer)){
				((MainMenu) nextMenu).setAnimating(true);
				((MainMenu) nextMenu).animate();
			}
		}
	}


	@Override
	public boolean isAnimating() {
		return animating;
	}


	@Override
	public void setAnimating(boolean in){
		animatingIn = in;
		animating = true;
	}

	@Override
	public void handleMousePressed(MouseEvent e) {}
}
