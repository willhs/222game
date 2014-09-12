package game.ui.window;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class GameScreen implements GraphicsPane  {

	//the menu drawn ontop on the game screen
	private GraphicsPane currentMenu;

	public GameScreen(){

	}

	public void render(Graphics g){
		//renderer.render(g); //TODO wait for will to implement this method

		if(currentMenu != null){
			currentMenu.render(g);
		}
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		// TODO pass this info to client

	}

	@Override
	public void keyPressed(String keyEvent) {
		if(currentMenu != null){
			currentMenu.keyPressed(keyEvent);
			return;//no need to do anything with the game as the menu is the puase menu
		}
		//TODO pass this information onto the client
	}

	public void setMenu(GraphicsPane menu){
		//if null just means no need to draw any exra windows on the game screen
		this.currentMenu = menu;
	}
}
