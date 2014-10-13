package game.ui.window.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;
	
/**
 * @author Nicky van Hulst 3000294657
 * */
public class BasicHelpMenu extends HelpMenu implements GraphicsPane{
	
	//the game screen 
	private GameScreen game;
	
	//the panel to draw on 
	private BlankPanel panel;
	
	//the pause menu this menu came from
	private PauseMenu menu;
	
	
	/**
	 * Constructor for the basic help menu
	 * */
	public BasicHelpMenu(BlankPanel panel, StarMation starMation, GameScreen game, PauseMenu menu){
		super(panel,starMation);

		this.game = game;
		this.panel = panel;
		this.menu = menu;
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")  ){
			game.setMenu(new PauseMenu(panel, game, starMation));//change back to puase menu
		}
	}

	@Override
	public void render(Graphics g){
		g.setColor(new Color(0,0,0,0.5f));
		g.fillRect(buttons[1].x, buttons[1].y, (int)buttons[1].getWidth(), (int)buttons[1].getHeight());
		g.setFont(helpTextFont);
		g.setColor(Color.white);

		buttons[0] = new Rectangle(0,0,0,0);
		buttonNames[0] = "";

		MenuUtil.drawButtons(g, -1, buttons, buttonNames);
		drawString(g,helpText, (int) (buttons[1].getX()+20),(int)( textBox.getY() + 20));//draws the string within the text box
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(e == null)return;
		if(!(buttons[1].contains(e.getX(),e.getY())))menu.setMenu(null);//if the click is outside the screen close the menu
	}
}
