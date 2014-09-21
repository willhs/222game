package game.ui.window.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import game.ui.window.BlankPanel;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.ui.window.keyInputManagment;
/**
 * @author Nicky van Hulst
 * */
public class KeyOptionScreen implements GraphicsPane {
	private HashMap<String,Integer> keyMap;
	private BlankPanel panel;

	//buttons fields
	private Rectangle[] buttons;
	private String[] buttonNames;
	private int numbOfButtons = 1;
	private int selectedButton;


	//zise of the table
	private int tableWidth  = 500;
	private int colWidth = tableWidth/2;
	private int colHeight = 20; //could do calculation baesd on size of screen and size of key map
	private int startX = (GameWindow.FRAME_WIDTH/2)- (tableWidth/2);
	private int startY = 50;

	private int mouseOnRow = -1;
	private boolean selectedRow;//if the user has clicked on a valid row
	private String keySelected;

	private String errorMessege;

	public KeyOptionScreen(BlankPanel panel){
		keyMap = keyInputManagment.getKeyMap();//set  the key map
		this.numbOfButtons = 1;
		this.selectedButton = Integer.MAX_VALUE;
		this.buttons = new Rectangle[numbOfButtons];
		this.buttonNames = new String[numbOfButtons];
		this.panel = panel;
		this.errorMessege = "";
		setupButtons();

	}


	private void setupButtons(){
		int buttonGap = 20;
		int x = 50;
		int y = 50;
		int width = 200;
		int height = 50;

		buttons[0] = new Rectangle(x,y,width,height);

		buttonNames[0] = "Back";
	}

	@Override
	public void render(Graphics g) {
		drawMesseges(g);
		drawKeyTable(g);
		MenuUtil.drawButtons(g, selectedButton, buttons, buttonNames);
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		//sets the currently select row by the mouse
		if(!selectedRow){
			mouseOnRow = getSelectedRow(e.getX(), e.getY());
		}

		//set selected button if the mouse is on a button
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
		//the mouse is currently on a row
		if(mouseOnRow != -1){
			selectedRow = true;//indicate a row is currently selected
		}
		if(selectedButton == 0){
			panel.setMenu(new OptionMenu(panel));//change the menu back to the option menu
		}
	}

	@Override
	public void keyPressed(String keyEvent) {
		//check if escape is pressed
		if(keyEvent.equals("escape") || keyEvent.equals("backspace")){
			if(selectedRow){
				selectedRow = false;//a row is selected currently so unselect it
			}
			else{
				panel.setMenu(new OptionMenu(panel));//no row is selected but escape is pressed so return to option menu
			}
		}

		//check if a row is selected
		if(selectedRow){
			KeyEvent e = keyInputManagment.getLastKeyEvent();//need to real key event grabs the last one

			//check is key already bound
			for(int keyValue : keyMap.values()){
				if(e.getKeyCode() == keyValue){
					this.errorMessege = "The Key is Already Bound to another action!";
					return;//the key is bound so return
				}
			}

			this.errorMessege = "";//the user can rebind so reset error messege
			keyMap.put(keySelected, e.getKeyCode());//update the map with the new key binding
			selectedRow = false;//unselect the row as the user has selected the new key
		}
	}

	/**
	 * Draws the key table onto the graphics object
	 * @return
	 * */
	private void drawKeyTable(Graphics g){
		int x = startX;
		int y = startY;
		int row = 0;

		//draw the outer frame of the table
		g.setColor(Color.red);

		for(String key : keyMap.keySet()){

			if(row == mouseOnRow){
				g.setColor(Color.blue);
				g.fillRect(x, y, colWidth*2, colHeight);
				keySelected = key;//TODO fix this terrible design
			}

			g.setColor(Color.white);
			g.drawRect(x, y, colWidth, colHeight);
			g.drawString(key, x + 20, y + 15);
			g.drawRect(x+colWidth, y, colWidth, colHeight);

			//if no key is selected to be changed draw the key
			if(!(selectedRow && row == mouseOnRow)){
				g.drawString(KeyEvent.getKeyText(keyMap.get(key)), x + 20 + colWidth, y + 15);
			}
			else{
				g.drawString("_", x + 20 + colWidth, y + 15);
				keySelected = key;
				System.out.println(key);

			}
			y+=colHeight;
			row++;
		}
	}


	/**
	 * Draws all of the messages to display to the user
	 * */
	private void drawMesseges(Graphics g){
		g.setColor(Color.red);
		g.drawString(errorMessege, startX, startY-10);//error messege to show the user
	}


	/**
	 * Returns the number of the selected row
	 * */
	private int getSelectedRow(int xMouse, int yMouse){
		int y = startY;

		for(int i = 0; i < keyMap.size(); i++){
			//checks if the mouse click is within the current row
			if(new Rectangle(startX,y,colWidth*2,colHeight).contains(xMouse,yMouse)){
				return i;
			}
			y += colHeight;
		}
		return -1;//return this if not on a row in the table
	}

}
