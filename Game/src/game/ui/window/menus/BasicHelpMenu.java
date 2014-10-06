package game.ui.window.menus;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GraphicsPane;
import game.ui.window.StarMation;

public class BasicHelpMenu extends HelpMenu implements GraphicsPane{
	private GameScreen game;
	private BlankPanel panel;

	public BasicHelpMenu(BlankPanel panel, StarMation starMation, GameScreen game){
		super(panel,starMation);

		this.game = game;
		this.panel = panel;
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")  ){
			game.setMenu(new PauseMenu(panel, game, starMation));
		}
	}

	@Override
	public void render(Graphics g){
		buttons[0] = new Rectangle(0,0,0,0);
		buttonNames[0] = "";
		MenuUtil.drawButtons(g, -1, buttons, buttonNames);
		g.setFont(helpTextFont);
		drawString(g,helpText, (int) (buttons[1].getX()+20),(int)( textBox.getY() + 20));//draws the string within the text box
	}
}
