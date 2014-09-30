package game.ui.window.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import game.ui.window.BlankPanel;
import game.ui.window.GraphicsPane;
import game.ui.window.keyInputManagment;

public class CharacterSelectionMenu implements GraphicsPane{

	private Rectangle nameBox;

	private Rectangle character1;
	private Rectangle character2;
	private Rectangle character3;

	int characterSelected = -1;
	
	private boolean nameBoxSelected;
	private String name = "";
	
	
	/**
	 * Constructor for the character selection menu 
	 * */
	public CharacterSelectionMenu(BlankPanel panel){
		setUpCharacterBoxes();
	}
	
	public void setUpCharacterBoxes(){
		nameBox = new Rectangle(100,100,200,500);
	}

	@Override
	public void render(Graphics g) {
		drawBoxes(g);// TODO Auto-generated method stub

	}
	
	public void drawBoxes(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.white);
		g2d.draw(nameBox);
		g2d.setColor(Color.black);
		g2d.draw(nameBox);
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
//		if(character1.contains(x,y)){
//			characterSelected = 1;
//		}
//		else if(character2.contains(x,y)){
//			characterSelected = 2;
//		}
//		else if(character3.contains(x,y)){
//			characterSelected = 3;
//		}
		 if(nameBox.contains(x,y)){
			 System.out.println("Selected");
			nameBoxSelected = true;
			return;
		}
		nameBoxSelected = false;
	
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(nameBoxSelected){
			if(keyEvent.equals("backspace")){
				name = name.substring(0, name.length()-1);//take one char of the string
				return;
			}
			KeyEvent e = keyInputManagment.getLastKeyEvent();
			String key = KeyEvent.getKeyText(e.getExtendedKeyCode());//TODO may need to fix
			
			//should make it a number or letter i think
			if(key.length() == 1){
				System.out.println("Concatening");
				name = name.concat(key);//add to the end of the string
			}
			System.out.println(key.length() + " " + key);
		}
		
		System.out.println(name);
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
