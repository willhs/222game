package game.ui.window.menus;

import game.ui.window.GameWindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * @author Nicky van Hulst
 * */
public class MenuUtil {

	public static final Color WHITE_TRAN = new Color(1f,1f,1f,0.5f);
	public static final Color BLACK_TRAN = new Color(0f,0.5f,0.5f,0.5f);


	/**
	 * Draws the buttons on the graphics object on the screen vertically
	 * */
	public static void drawButtons(Graphics g, int selectedButton, Rectangle[] buttons, String[] buttonNames){
		Graphics2D g2d = (Graphics2D)g;

		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);

		for(int i = 0; i < buttons.length; i++){
			g2d.setColor(WHITE_TRAN);
			g2d.fill(buttons[i]);
			g2d.setColor(Color.black);
			g2d.draw(buttons[i]);

			if(selectedButton == i){
				//g2d.setColor(new Color(0f,0f,0f,0.5f));
				g.setColor(Color.blue);

				g2d.fill(buttons[i]);
			}
			//draws the string in the center of the current button
			g.setColor(Color.white);
			g2d.drawString(buttonNames[i], buttons[i].x + ((buttons[i].width/2) - g.getFontMetrics(myFont).stringWidth(buttonNames[i])/2), (int) ((buttons[i].y + buttons[i].getHeight() - (g.getFontMetrics(myFont).getHeight()/2))));
		}
	}


	/**
	 *figure out which button should be selected return the int representation of
	 *the button
	 * */
	public static int moveButtonSelectionUp(int selectedButton, int numbOfButtons){
		if(selectedButton == -1 || selectedButton == 0 ){
			return numbOfButtons-1;
		}
		else{
			return selectedButton-1;
		}
	}


	/**
	 *figure out which button should be selected return the int representation of
	 *the button
	 * */
	public static int moveButtonSelectionDown(int selectedButton, int numbOfButtons){
		if(selectedButton == -1 || selectedButton == numbOfButtons-1 ){
			return 0;
		}
		else{
			return selectedButton+1;
		}
	}


	/**
	 * Places all of the rectangles of the buttons array just outside
	 * of the screen
	 * */
	public static Rectangle[] setUpAnimation(Rectangle[] buttons){
		for(int i = 0; i < buttons.length; i++){
			buttons[i].x = (int) ((buttons[i].getX()) + (GameWindow.FRAME_WIDTH - buttons[i].getX()) + buttons[i].getX());//set the button to be outside the scrren
		}
		return buttons;
	}


	/**
	 *Decreases the location of all the buttons in the array and returns whether or not
	 *the buttons are now of the screen
	 * */
	public static boolean animateIn(int buttonStartX,Rectangle[] buttons, int speed){
		boolean isDone = false;

		for(int i = 0; i < buttons.length; i++){
			buttons[i].x -=speed;//decrease the x value of all the buttons

			//check if all buttons outside of the screen
			if(buttons[0].x <=  buttonStartX){
				isDone = true;
			}
		}
		return isDone;
	}


	/**
	 *Decreases the location of all the buttons in the array and returns whether or not
	 *the buttons are now of the screen
	 * */
	public static boolean animateOut(Rectangle[] buttons, int speed){
		boolean isDone = true;

		for(int i = 0; i < buttons.length; i++){
			buttons[i].x -=speed;//decrease the x value of all the buttons

			//check if all buttons outside of the screen
			if((buttons[i].x + buttons[i].getWidth()) >  0){
				isDone = false;
			}
		}
		return isDone;
	}

	/**
	 * Takes a buffered Image and return a buffered Image with the size
	 * width and height
	 * */
	public static BufferedImage scale(BufferedImage bufImage, int width, int height){
		BufferedImage returnImage = bufImage;

		//inventory test images
		Image tempImage = bufImage.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
		returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D bGr1 = returnImage.createGraphics();
	    bGr1.drawImage(tempImage, 0, 0, null);
	    bGr1.dispose();
		return returnImage;
	}
}
