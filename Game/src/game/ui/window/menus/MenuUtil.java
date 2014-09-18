package game.ui.window.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MenuUtil {
	
	/**
	 * Draws the buttons on the graphics object on the screen vertically 
	 * */
	public static void drawButtons(Graphics g, int selectedButton, Rectangle[] buttons, String[] buttonNames){
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
			return numbOfButtons;
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
}
