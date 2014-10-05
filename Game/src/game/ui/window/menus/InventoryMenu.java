package game.ui.window.menus;

import game.ui.render.Res;
import game.ui.window.BlankPanel;
import game.ui.window.GameScreen;
import game.ui.window.GameWindow;
import game.ui.window.GraphicsPane;
import game.world.model.Item;
import game.world.model.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.ItemSelectable;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

/**
 * @author Nicky van Hulst
 * */
public class InventoryMenu implements GraphicsPane {
	private GameScreen game;
	private BlankPanel panel;

	//the draw able space for the inventory menu
	private Rectangle frame;
	private int width = 500;
	private int startX;
	private int startY = 20;

	//inventory grid
	private int gridX;
	private int gridY;
	private int gridSize;
	private int gap = 20;
	private int numbCol = 5;
	private int numbRow = 3;
	
	//character fields
	private Color backColor;
	private BufferedImage characterImage;
	private Rectangle characterFrame;
	int selectedGrid = -1;

	//inventory images fields
	private int gridNumb = 15;
	//private BufferedImage[] inventoryImages;
	
	private Item[] items;
	
//	private BufferedImage selectedImage;
	
	private Item selectedItem;
	
	private int selectImageGrid = -1;

	private int currMouseY;
	private int currMouseX;
	
	private Player player;
	
	private long prevTime;
	private boolean displayingInfo;


	/**
	 *The constructor for the InventoryMenu
	 * */
	public InventoryMenu(BlankPanel panel, GameScreen game, Player player){
		this.panel = panel;
		this.game = game;
		this.player = player;
		this.backColor = MenuUtil.WHITE_TRAN;

		this.startX = (GameWindow.FRAME_WIDTH/2) - (width/2);//places the frame in the middle of the screen
		this.frame = new Rectangle(startX, startY, width, GameWindow.FRAME_HEIGHT - (startY*2));//creates the frame as a rectangle

		this.gridX = startX + 20;//set the start of the grid
		this.gridY = (int) (startY + frame.height/2);//start the grid half way down the frame
		this.gridSize = (int) ((frame.getWidth() - (gap*2))/numbCol);//creates a grid that fits within the frame
				
		this.items = new Item[gridNumb];

		setCharacterInventory();
		setUpInventoryFromPlayer();
	}

	/**
	 *Gets the inventory from the player and places it in the inventory
	 *grid
	 * */
	public void setUpInventoryFromPlayer(){
		int i = 0;
		for(Item item : player.getInventory()){
			if(i >=  items.length)return;//TODO check with someone
			System.out.println("inventory");
			//1inventoryImages[i] = Res.getImageFromName(item.getImageName());
			//1inventoryImages[i] = MenuUtil.scale(inventoryImages[i], gridSize, gridSize);
			items[i] = item;
			i++;
		}
	}
	
	//TODO test
	/**
	 * Updates the inventory, if there is a new item it will place it in the first
	 * empty location it comes across in the grid
	 * */
	public void updateInventory(){
		//first check if item already in inventory
		boolean in = false;
		
		//iterate through the players inventory 
		for(Item item : player.getInventory()){
			
			//iterate through the current items already in the inventory
			for(int i = 0; i < items.length; i++){
				if(items[i] != null){//check if there is an item at this location 
					if(items[i].getName().equals(item.getName()) || game.inInventory(item)){
						
						in = true;//the item is already in  the item array
					} 
				}
				if( game.inInventory(item))in = true;//becuase if all empty needs to check the other inventory 
			}
			if(!in){//the item is not in the items array so add it in
				for(int j = 0; j < items.length; j++){
					if(items[j] == null){
						items[j] = item;//nothing in this location so add the new item
						return;
					}
				}
			}
			in = false;//reset for the next item
		}
	}


	/**
	 * draws the grid on the screen representing the inventory
	 * changes the grid color if one of the squares is selected
	 * */
	public void drawInventoryGrid(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//modify the stroke size to 4
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(2));

		int x = gridX;
		int y = gridY;

		int curGrid = 0;//the number value of the grid square that is currently being drawn

		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){

				//if the current grid square is selected change the color
				if(curGrid == selectedGrid){
					//g.setColor(Color.BLUE);
				}
				else{
					g.setColor(backColor);
				}
				
				//draw the current grid square
				g.fillRect(x, y, gridSize, gridSize);
				g.setColor(Color.black);
				g.drawRect(x, y, gridSize, gridSize);
				
				if(items[curGrid] != null ){
					if(selectImageGrid != curGrid)g.drawImage(Res.getImageFromName(items[curGrid].getImageName()), x, y,gridSize,gridSize, panel);
				}
				
				x +=gridSize;
				curGrid++;
			}
			x = gridX;//reset x
			y += gridSize;
		}

		if(selectedItem !=null){
			g.drawImage(Res.getImageFromName(selectedItem.getImageName()), currMouseX, currMouseY,gridSize,gridSize,panel);
		}

		g2d.setStroke(oldStroke);
	}


	/**
	 * Draws the character information part of the inventory screen
	 * on top of the game screen
	 * */
	private void drawCharactrInventory(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//set the border size to 4
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));
		
		//gets the players image for Resources and scales it to the size of the frame
		g.drawImage(MenuUtil.scale(Res.getImageFromName(player.getImageName()),(int)characterFrame.getWidth(),(int)characterFrame.getHeight()), characterFrame.x,characterFrame.y,panel);
		g2d.draw(characterFrame);
		
		g.setColor(backColor);
		g2d.fill(characterFrame);

		//reset the border size
		g2d.setStroke(oldStroke);
	}


	/**
	 *Draws the outside of the inventory menu on the game screen
	 * */
	public void drawFrame(Graphics g){
		Graphics2D g2d = (Graphics2D)g;

		//increase the border size to 4
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(4));

		//draw the inside of the frame
		g2d.setColor(backColor);
		g2d.fill(frame);

		//draw the border of the frame
		g2d.setColor(Color.black);
		g2d.draw(frame);

		//reset the border size
		g2d.setStroke(oldStroke);
	}

	public void setCharacterInventory(){
		int width = 200;
		int x = (GameWindow.FRAME_WIDTH/2) - (width/2);
		int y = startY+20;
		int height = gridY - 20 - y;

		characterFrame = new Rectangle(x, y, width, height);
	}


	/**
	 * Returns the number of the square in the
	 * grid of the inventory screen returns -1 if not on the grid
	 * */
	public int getGridClicked(int x, int y){
		int xStart = gridX;
		int yStart = gridY;

		int numbGrid = 0;//the number of the grid across then down

		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){
				Rectangle gridRec = new Rectangle(xStart,yStart,gridSize,gridSize);
				if(gridRec.contains(x,y)){//checks if the mouse press is inside the current grid square
					return numbGrid;
				}
				xStart += gridSize;
				numbGrid++;
			}
			xStart = gridX;
			yStart+=gridSize;
		}
		return -1;//no grid square selected
	}


	@Override
	public void render(Graphics g) {
		if(prevTime - System.currentTimeMillis() < -500 || displayingInfo){
			displayMenu(g);
			displayingInfo = true;
		}

		drawFrame(g);//draws the outside of the inventory
		drawCharactrInventory(g);//draws the character part of the inventory
		g.drawImage(characterImage, (int)characterFrame.getX(),(int)characterFrame.getY(),panel);

		g.setColor(Color.black);
		drawInventoryGrid(g);//draws the grid on the screen


		//black square over inventory check
		if( selectedGrid != -1 && prevTime - System.currentTimeMillis() < -2000 || displayingInfo &&  selectedGrid != -1 ){
			if(items[selectedGrid] != null){
				displayMenu(g);
				displayingInfo = true;
			}
		}

	}

	public void displayMenu(Graphics g){
		if(selectedGrid != -1 && items[selectedGrid] != null){
			g.setColor(Color.black);
			//g.fillRect(currMouseX, currMouseY, 100, 100);
			g.setColor(Color.white);
			g.drawString(items[selectedGrid].getName(), currMouseX+5,currMouseY+10);
			g.setColor(Color.white);
		}
	}


	@Override
	public void handleMouseMoved(MouseEvent e) {
		prevTime = System.currentTimeMillis();
		displayingInfo  = false;
		//sets the selected grid square -1 if none selected
		selectedGrid = getGridClicked(e.getX(), e.getY());
		currMouseX = e.getX();
		currMouseY = e.getY();
		//TODO check the rest of the menu
	}


	@Override
	public void handleMouseReleased(MouseEvent e) {
		if(selectedGrid != -1 && selectedItem !=null && items[selectedGrid] == null){
			//inventoryImages[selectedGrid] = selectedImage;
			items[selectedGrid] = selectedItem;
			//selectedImage = null;
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("inventory")){
			game.setMenu(null);//inventory menu no longer to be required to render so set to null in the gameScreen
		}
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		//TODO place all of this stuff into its own method some time dont be lazy

		//first check if theres is a selected image
		if(selectedItem != null){

			//check the image is to be placed on a valid location
			if(selectedGrid != -1 ){

				//set the image array to the new image
				if(items[selectedGrid] == null && selectedGrid != selectImageGrid){
					items[selectedGrid] = selectedItem;
					if(selectImageGrid!=-1)items[selectImageGrid] = null;
					selectedItem = null; 
					return;
				}				

				//check the selected grid is not the same as the one the image came from
				if(selectedGrid == selectImageGrid){
					selectImageGrid = -1;
					selectedItem = null; 
					return;
				}
			}

			//check if the mouse click is on the grid of the game screen inventory bar
			int gridOnGameBar = game.isOnGrid((int)e.getX(),(int)e.getY());
			if(gridOnGameBar != -1 && (!(game.containsItem(gridOnGameBar)))){

				//place the item on the game screen at the appropriate grid location
				game.placeItemOnGrid(gridOnGameBar, selectedItem);
				selectedItem = null;
				items[selectImageGrid] = null;
				return;
			}
		}
		else{
			//at this point if a valid grid is clicked it selected the image
			if(selectedGrid != -1){
				if(items[selectedGrid] != null){
					selectedItem = items[selectedGrid];
					selectImageGrid = selectedGrid;
				}
			}
			
			if(game.isOnGrid((int)e.getX(),(int)e.getY()) != -1){
				Item item = game.grabItemFromIventory(game.isOnGrid((int)e.getX(),(int)e.getY()));
				if(item!=null){
					selectedItem = item;
					selectImageGrid = -1;//TODO fix 
					
				}	
			}
		}
		
	}
}
