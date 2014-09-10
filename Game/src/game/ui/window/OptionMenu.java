package game.ui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class OptionMenu implements Menu {
	private BlankPanel panel;
	private int numbOfButtons;

	private Rectangle[] buttons;

	private int selectedButton;

	public OptionMenu(BlankPanel panel){
		this.panel = panel;
		this.numbOfButtons = 1;
		this.selectedButton = Integer.MAX_VALUE;
		this.buttons = new Rectangle[numbOfButtons];
		setupButtons();
	}

	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		buttons[0] = new Rectangle(50,50,200,50);
	}


	@Override
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.red);


		for(int i = 0; i < buttons.length; i++){
			g2d.fill(buttons[i]);
			if(selectedButton == i){
				g.setColor(Color.black);
				g2d.fill(buttons[i]);
				g.setColor(Color.red);
			}
		}
	}

	@Override
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

	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedButton == 0){
			panel.setMenu(new MainMenu(panel));
		}
	}

}
