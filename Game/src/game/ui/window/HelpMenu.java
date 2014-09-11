package game.ui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class HelpMenu implements Menu {
	private BlankPanel panel;
	private int numbOfButtons;

	private Rectangle[] buttons;
	private String[] buttonNames;

	private int selectedButton;

	public HelpMenu(BlankPanel panel){
		this.panel = panel;
		this.numbOfButtons = 1;
		this.selectedButton = Integer.MAX_VALUE;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		setupButtons();
	}

	/**
	 * Sets up the buttons for the option menu
	 * */
	private void setupButtons(){
		buttons[0] = new Rectangle(50,50,200,50);

		buttonNames[0] = "Back";

	}


	@Override
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(new Color(1f,0f,0f,0.1f ));

		Font myFont = new Font("arial",0,20);
		g.setFont(myFont);



		for(int i = 0; i < buttons.length; i++){
			g2d.setColor(new Color(1f,0f,0f,0.1f ));
			g2d.fill(buttons[i]);
			g2d.setColor(Color.black);
			g2d.draw(buttons[i]);

			if(selectedButton == i){
				g.setColor(Color.black);
				g2d.fill(buttons[i]);
			}
			g.setColor(Color.white);
			g.drawString(buttonNames[i], buttons[i].x + 20, buttons[i].y + 25);
		}

		g.drawString("HELP", GameWindow.FRAME_WIDTH/2, GameWindow.FRAME_HEIGHT/2);
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

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape")){
			panel.setMenu(new MainMenu(panel));
		}
	}
}
