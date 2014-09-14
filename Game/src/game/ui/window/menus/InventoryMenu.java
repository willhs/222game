package game.ui.window.menus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
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
	
	
	int selectedGrid = -1;
	
	
	public InventoryMenu(BlankPanel panel, GameScreen game){
		this.game = game;
		this.backColor = new Color(0f,0f,0f,0.5f);

		this.panel = panel;
		this.startX = (GameWindow.FRAME_WIDTH/2) - (width/2);

		this.frame = new Rectangle(startX, startY, width, GameWindow.FRAME_HEIGHT - (startY*2));

		this.gridX = startX + 20;
		this.gridY = (int) (startY + frame.height/2);//start the grid half way down the frame
		this.gridSize = (int) ((frame.getWidth() - (gap*2))/numbCol);//creates a grid that fits within the frame
	}
	

	/**
	 * draws the grid on the screen representing the inventory
	 * */
	public void drawInventoryGrid(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));
		
		int x = gridX;
		int y = gridY;
		
		int curGrid = 0;
		
		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){
				
				if(curGrid == selectedGrid){
					g.setColor(new Color(0f,0f,0f,0.5f));

				}
				else{
					g.setColor(new Color(1f,1f,1f,0.5f));
				}
				g.fillRect(x, y, gridSize, gridSize);
				g.setColor(Color.black);
				g.drawRect(x, y, gridSize, gridSize);
				x +=gridSize;
				curGrid++;
			}
			x = gridX;//reset x
			y += gridSize;
		}
		
		g2d.setStroke(oldStroke);
	}
	
	public void drawCharactrInventory(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));
		
		int width = 200;
		int x = (GameWindow.FRAME_WIDTH/2) - (width/2);
		int y = startY+20;
		int height = gridY - 20 - y;
		
		g.drawRect(x, y, width, height);
		g.setColor(new Color(1f,1f,1f,0.5f));
		g.fillRect(x, y, width, height);
		
		g2d.setStroke(oldStroke);
	}
	
	public void drawFrame(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));

		g2d.setColor(backColor);
		g2d.fill(frame);
		
		g2d.setColor(Color.black);
		g2d.draw(frame);
		
		g2d.setStroke(oldStroke);
	}
	
	
	
	/**
	 * Returns the number of the square in the 
	 * grid of the inventory screen returns -1 if not on the grid
	 * */
	public int getGridClicked(int x, int y){
		int xStart = gridX;
		int yStart = gridY;
		
		int numbGrid = 0;
		
		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){
				Rectangle gridRec = new Rectangle(xStart,yStart,gridSize,gridSize);
				if(gridRec.contains(x,y)){
					return numbGrid;
				}
				xStart += gridSize;
				numbGrid++;
			}
			xStart = gridX;
			yStart+=gridSize;
		}
		
		return -1;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		drawFrame(g);
		drawCharactrInventory(g);
		g.setColor(Color.black);
		//draws the grid on the screen 
		drawInventoryGrid(g);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseMoved(MouseEvent e) {
		selectedGrid = getGridClicked(e.getX(), e.getY());
		
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
