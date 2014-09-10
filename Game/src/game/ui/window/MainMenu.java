package game.ui.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MainMenu extends JPanel {
	private final int numbOfButtons = 5;

	private int selectedButton;

	Rectangle[] buttons;

	public MainMenu(){
		setSize(new Dimension(GameWindow.FRAME_WIDTH,GameWindow.FRAME_HEIGHT));
		this.buttons = new Rectangle[numbOfButtons];
		this.selectedButton = Integer.MAX_VALUE;
		setBackground(Color.black);
		setUpButtons();
		setUpMouseListnet();
	}


	/**
	 * Sets up all of the button locations for the main menu
	 * */
	public void setUpButtons(){
		int width = GameWindow.FRAME_WIDTH;
		int height = GameWindow.FRAME_HEIGHT;


		int y = height/numbOfButtons;

		int buttonGap = 60;

		int recHeight = 50;
		int recWidth = 200;

		int x = (GameWindow.FRAME_WIDTH/2)- (recWidth/2);

		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new Rectangle(x,y,recWidth,recHeight);
			y += recHeight + buttonGap;

		}
	}

	/**
	 *
	 * */
	public void paint(Graphics g){
		drawButtons(g);
	}

	public void drawButtons(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.red);

		for(int i = 0; i < buttons.length; i++){
			g2d.fill(buttons[i]);
			if(selectedButton == i){
				g.setColor(Color.black);
				g2d.draw(buttons[i]);
				g.setColor(Color.red);
			}
		}
	}

	public void setUpMouseListnet(){
		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e){
				handleMouseMoved(e);
				repaint();
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e){
				handleMouseReleased(e);
			}
		});
	}

	/**
	 *
	 * */
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

	public void handleMouseReleased(MouseEvent e){
		System.out.println("Mouse Released");

		switch(selectedButton){
			case 0: System.out.println("Single Player");
				return;
			case 1: System.out.println("Multiplayer");
				return;
		}
	}





}
