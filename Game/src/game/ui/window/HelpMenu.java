package game.ui.window;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class HelpMenu implements Menu {

	public void render(Graphics g){
		g.drawOval(50, 50, 100, 100);
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
