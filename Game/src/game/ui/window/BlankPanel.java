package game.ui.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class BlankPanel extends JPanel{


	private Menu currentMenu;


	public BlankPanel(){

		setSize(new Dimension(GameWindow.FRAME_WIDTH,GameWindow.FRAME_HEIGHT));
		setBackground(Color.black);
		setUpMouseListner();
		currentMenu = new MainMenu(this);

	}

	public void paint(Graphics g){

		g.setColor(Color.blue);
		g.fillRect(0, 0, GameWindow.FRAME_WIDTH, GameWindow.FRAME_HEIGHT);
		currentMenu.render(g);
	}



	public void setUpMouseListner(){
		this.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseMoved(MouseEvent e){
				currentMenu.handleMouseMoved(e);
				repaint();
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e){
				currentMenu.handleMouseReleased(e);
				repaint();
			}
		});
	}

	public void setMenu(Menu menu){
		this.currentMenu = menu;
	}
}
