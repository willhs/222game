package game.ui.window.menus;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.*;

import javax.swing.JFrame;

import com.sun.org.apache.bcel.internal.generic.Select;

/**
 * @author Nicky van Hulst 300294657
 * */
public class OptionMenu implements GraphicsPane, Animated {

	//panel to draw on
	private BlankPanel panel;

	//number of buttons to be created
	private int numbOfButtons;

	//arrays for buttons and button names
	private Rectangle[] buttons;
	private String[] buttonNames;

	//the currently selected button
	private int selectedButton;

	//the current menu the user is in
	private GraphicsPane currentMenu;

	//animation fields
	private boolean animating;
	private boolean animatingIn;
	private GraphicsPane nextMenu;
	private boolean setUp;
	private int buttonStartX;
	private int aniSpeed = 1;
	private int speedIncrease = 1;
	private StarMation starMation;

	//radio button fields
	Ellipse2D.Double radio1;
	Ellipse2D.Double radio2;
	Ellipse2D.Double radio3;

	String radio1Label;
	String radio2Label;
	String radio3Label;
	private int radioSelected = 0;


	/**
	 * Constructor for the optionMenu
	 * */
	public OptionMenu(BlankPanel panel){
		this.starMation = MainMenu.getStarMation();
		this.panel = panel;
		this.numbOfButtons = 2;
		this.selectedButton = -1;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];

		setupButtons();
		setUpResolution();
	}


	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		int buttonGap = 20;
		int x = 50;
		int y = 50;
		int width = 200;
		int height = 50;

		buttons[0] = new Rectangle(x,y,width,height);
		y += height + buttonGap;
		buttons[1] = new Rectangle(x,y,width,height);

		buttonNames[0] = "Back";
		buttonNames[1] = "Key Bindings";
	}

	public void setUpResolution(){
		int x = GameWindow.FRAME_WIDTH/2;
		int y = 100;

		int w = 15;
		int h = 15;

		radio1 = new Ellipse2D.Double(x, y, w, h);
		radio2 = new Ellipse2D.Double(x, y+w+5, w, h);
		radio3 = new Ellipse2D.Double(x, y+w+5+w+5, w, h);


		//word out string metrics
		radio1Label = "1360 x 765 ";
		radio2Label = "1024 x 768 ";
		radio3Label = "1440 x 810 ";
	}


	@Override
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		updateRes();
		//set my font


		starMation.render(g);
		if(currentMenu != null){
			currentMenu.render(g);
			return;
		}

		if(nextMenu!=null && nextMenu instanceof Animated){
			if(((Animated) nextMenu).isAnimating()){
				((Animated) nextMenu).animate();
				nextMenu.render(g);
			}
		}

		Font oldFont = g.getFont();
		Font myFont = new Font("Tunga",0,20);
		g.setFont(myFont);

		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);

		if(animating || animatingIn)return;

		//radio button
		int stringWidth = g.getFontMetrics(myFont).stringWidth(radio1Label);
		int stringX =  (GameWindow.FRAME_WIDTH/2)- (stringWidth/2);

		int r1y = 85;
		int r2y = 115;
		int r3y = 145;

		g.setColor(Color.white);
		g.drawString(radio1Label, stringX, 100);
		g.drawString(radio2Label, stringX, 130);
		g.drawString(radio3Label, stringX, 160);

		radio1.x = stringX+stringWidth + 10;
		radio2.x = stringX+stringWidth + 10;
		radio3.x = stringX+stringWidth + 10;

		radio1.y = r1y;
		radio2.y = r2y;
		radio3.y = r3y;

		g.setColor(Color.white);
		g2d.draw(radio1);
		g2d.draw(radio2);
		g2d.draw(radio3);

		g.setColor(Color.red);
		if(radioSelected == 1){
			g2d.fill(radio1);
		}
		else if(radioSelected == 2){
			g2d.fill(radio2);
		}
		else if(radioSelected == 3){
			g2d.fill(radio3);
		}

		g.setFont(oldFont);
	}

	public void updateRes(){
		if(radioSelected == 1){
			panel.setPreferredSize(new Dimension(1360, 765));//projector 1
		}
		else if(radioSelected == 2){
			panel.setPreferredSize(new Dimension(1024, 768));//projector 2
		}
		else if(radioSelected == 3){
			panel.setPreferredSize(new Dimension(1440, 810));//projector 2
		}
	}

	@Override
	public void handleMouseMoved(MouseEvent e){
		if(currentMenu != null){//this menu is not the menu in focus so pass the mouse movement on
			currentMenu.handleMouseMoved(e);
			return;
		}

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
		if(currentMenu != null){//this menu is not the menu in focus so pass the mouse event on
			currentMenu.handleMouseReleased(e);
			return;
		}
		if(selectedButton == 0){//back button
			animating = true;
			animatingIn = false;
			nextMenu = new MainMenu(panel);
		}
		else if(selectedButton == 1){//key binding menu button
			currentMenu = new KeyOptionScreen(panel);
		}
		else if(radio1.contains(e.getX(),e.getY())){
			radioSelected = 1;
		}
		else if(radio2.contains(e.getX(),e.getY())){
			radioSelected = 2;
		}
		else if(radio3.contains(e.getX(),e.getY())){
			radioSelected = 3;
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(currentMenu != null){
			currentMenu.keyPressed(keyEvent);
			return;
		}
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			animating = true;
			animatingIn = false;
			nextMenu = new MainMenu(panel);
		}
		else if(keyEvent.equals("enter")){
			handleMouseReleased(null);//TODO change to a proper method button pressed like main menu
		}
		else if(keyEvent.equals("down") || keyEvent.equals("move down")){
			selectedButton = MenuUtil.moveButtonSelectionDown(selectedButton, buttons.length);
		}
		else if(keyEvent.equals("up") || keyEvent.equals("move up")){
			selectedButton = MenuUtil.moveButtonSelectionUp(selectedButton, buttons.length);
		}
	}

	@Override
	public void animate() {
		if(animatingIn){
			if(!setUp){
				buttonStartX = (int) buttons[0].getX();
				MenuUtil.setUpAnimation(buttons);//moved the buttons to the end of the screen
				setUp = true;
			}
			if(MenuUtil.animateIn(buttonStartX,buttons, aniSpeed+=speedIncrease)){
				panel.setMenu(new OptionMenu(panel));//create the new main menu
			}
		}else{
			if(MenuUtil.animateOut(buttons,aniSpeed+=speedIncrease)){
				((MainMenu) nextMenu).setAnimating(true);
				((Animated) nextMenu).animate();
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
