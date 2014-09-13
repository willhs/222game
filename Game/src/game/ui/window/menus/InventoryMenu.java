package game.ui.window.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;

public class InventoryMenu implements GraphicsPane {
	private BlankPanel panel;
	private GameScreen game;

	
	//the draw able space for the inventory menu
	private Rectangle frame;
	private int width = 500;
	private int startX;
	private int startY = GameWindow.FRAME_HEIGHT/9;
	
	//inventory grid 
	private int gridX;
	private int gridY;
	private int gridSize;
	private int gap = 20;
	private int numbCol = 5;
	private int numbRow = 3;
	
	private Color backColor;
	
	
	public InventoryMenu(BlankPanel panel, GameScreen game){
		this.game = game;
		this.backColor = new Color(0f,0f,0f,0.5f);

		this.panel = panel;
		this.startX = (GameWindow.FRAME_WIDTH/2) - (width/2);

		this.frame = new Rectangle(startX, startY, width, GameWindow.FRAME_HEIGHT - (startY*2));
		System.out.println("Starty + " + startY + " startX + " + startX + " frame width : " + frame.getWidth() + " frame Height : " + frame.getHeight());

		this.gridX = startX + 20;
		this.gridY = (int) (startY + frame.height/2);//start the grid half way down the frame
		this.gridSize = (int) ((frame.getWidth() - (gap*2))/numbCol);//creates a grid that fits within the frame
	}
	

	/**
	 * draws the grid on the screen representing the inventory
	 * */
	public void drawInventoryGrid(Graphics g){
		int x = gridX;
		int y = gridY;
		
		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){
				g.drawRect(x, y, gridSize, gridSize);
				x +=gridSize;
			}
			x = gridX;//reset x
			y += gridSize;
		}
		
		
	}
	
	public void drawFrame(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		//need to fix the bug where it bases the color on the background but when the mouse move it re renders the scree n
		//and therefore changes the color and turn it to complete black

		g2d.setColor(backColor);
		g2d.fill(frame);
		
		g2d.setColor(Color.black);
		g2d.draw(frame);
	}
	
	
	
	/**
	 * Returns the number of the square in the 
	 * grid of the inventory screen
	 * */
	public int getGridClicked(){
		return -1;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		drawFrame(g);
		
		g.setColor(Color.black);
		//draws the grid on the screen 
		drawInventoryGrid(g);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("inventory")){
			game.setMenu(null);
		}
		
	}

}
