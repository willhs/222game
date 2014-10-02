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

	private Color backColor;
	private BufferedImage characterImage;
	private Rectangle characterFrame;
	int selectedGrid = -1;

	//inventory images fields
	private int gridNumb = 15;
	private BufferedImage[] inventoryImages;
	private BufferedImage selectedImage;
	private int selectImageGrid = -1;

	//mouse selection fields
	private int lastXPress;
	private int lastYPress;

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
		this.backColor = MenuUtil.BLACK_TRAN;

		this.startX = (GameWindow.FRAME_WIDTH/2) - (width/2);//places the frame in the middle of the screen
		this.frame = new Rectangle(startX, startY, width, GameWindow.FRAME_HEIGHT - (startY*2));//creates the frame as a rectangle

		this.gridX = startX + 20;//set the start of the grid
		this.gridY = (int) (startY + frame.height/2);//start the grid half way down the frame
		this.gridSize = (int) ((frame.getWidth() - (gap*2))/numbCol);//creates a grid that fits within the frame
		this.inventoryImages = new BufferedImage[gridNumb];

		setCharacterInventory();
		loadImages();
		setUpInventoryFromPlayer();

	}

	/**
	 *Gets the inventory from the player and places it in the inventory
	 *grid
	 * */
	public void setUpInventoryFromPlayer(){
		int i = 0;
		for(Item item : player.getInventory()){
			if(i >=  inventoryImages.length)return;//TODO check with someone
			System.out.println("inventory");
			inventoryImages[i] = Res.getImageFromName(item.getImageName());
			inventoryImages[i] = MenuUtil.scale(inventoryImages[i], gridSize, gridSize);
			i++;
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
		g2d.setStroke(new BasicStroke(4));

		int x = gridX;
		int y = gridY;

		int curGrid = 0;//the number value of the grid square that is currently being drawn

		for(int i = 0; i < numbRow; i++ ){
			for(int j = 0; j < numbCol; j++){

				//if the current grid square is selected change the color
				if(curGrid == selectedGrid){
					g.setColor(Color.BLUE);
				}
				else{
					g.setColor(MenuUtil.BLACK_TRAN);
				}
				//draw the current grid square
				g.fillRect(x, y, gridSize, gridSize);
				g.setColor(Color.black);
				g.drawRect(x, y, gridSize, gridSize);

				//draw the image for the inventory
				if(inventoryImages[curGrid] != null ){
					if(selectImageGrid != curGrid)g.drawImage(inventoryImages[curGrid], x, y, panel);

				}


				x +=gridSize;
				curGrid++;
			}
			x = gridX;//reset x
			y += gridSize;
		}

		if(selectedImage !=null){
			g.drawImage(selectedImage, currMouseX, currMouseY,panel);
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

		g2d.draw(characterFrame);
		g.setColor(new Color(1f,1f,1f,0.5f));
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
			if(inventoryImages[selectedGrid] != null){
				displayMenu(g);
				displayingInfo = true;
			}
		}

	}

	public void displayMenu(Graphics g){
		if(selectedGrid != -1){
			g.setColor(Color.black);
			g.fillRect(currMouseX, currMouseY, 100, 100);
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
		if(selectedGrid != -1 && selectedImage !=null){
			inventoryImages[selectedGrid] = selectedImage;
			//selectedImage = null;
		}
	}


	@Override
	public void keyPressed(String keyEvent) {
		if(keyEvent.equals("escape") || keyEvent.equals("inventory")){
			game.setMenu(null);//inventory menu no longer to be required to render so set to null in the gameScreen
		}
	}

	public void loadImages(){
		java.net.URL imagefile = InventoryMenu.class.getResource("resources/characterAlph.jpg");
		java.net.URL imagefileInvetorytest = InventoryMenu.class.getResource("resources/Player.gif");


		//load background image
		try {
			this.characterImage = ImageIO.read(imagefile);


			//inventory test images
			this.inventoryImages[0] = ImageIO.read(imagefileInvetorytest );
			Image tempCharImage = inventoryImages[0].getScaledInstance(gridSize, gridSize, BufferedImage.SCALE_DEFAULT);
			inventoryImages[0] = new BufferedImage(tempCharImage.getWidth(null), tempCharImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		    Graphics2D bGr1 = inventoryImages[0].createGraphics();
		    bGr1.drawImage(tempCharImage, 0, 0, null);
		    bGr1.dispose();

			Image tempImage =  characterImage.getScaledInstance((int)characterFrame.getWidth(),(int)characterFrame.getHeight(), BufferedImage.SCALE_DEFAULT);//TODO change to the selected image by the user
			characterImage = new BufferedImage(tempImage.getWidth(null), tempImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

			//writes the temp image onto the bufferedimage
		    Graphics2D bGr = characterImage.createGraphics();
		    bGr.drawImage(tempImage, 0, 0, null);
		    bGr.dispose();

		} catch (IOException e) {
			System.out.println("failed reading image");
			e.printStackTrace();
		}
	}


	@Override
	public void handleMousePressed(MouseEvent e) {
		System.out.println("Mouse pressed");
		lastXPress = e.getX();
		lastYPress = e.getY();

		//TODO place all of this stuff into its own method some time dont be lazy

		//first check if theres is a selected image
		if(selectedImage != null){

			//check the image is to be placed on a valid location
			if(selectedGrid != -1 ){

				//set the image array to the new image
				inventoryImages[selectedGrid] = selectedImage;

				//check the selected grid is not the same as the one the image came from
				if(selectedGrid != selectImageGrid){
					inventoryImages[selectImageGrid] = null;//clear the image from the array
				}

				//reset the selected the image and return
				selectedImage = null;
				return;
			}

			//check if the mouse click is on the grid of the game screen inventory bar
			if(game.isOnGrid((int)e.getX(),(int)e.getY()) != -1){

				//place the item on the game screen at the appropriate grid location
				game.placeItemOnGrid(game.isOnGrid((int)e.getX(),(int)e.getY()), MenuUtil.scale(selectedImage,game.getGridSize()-10,game.getGridSize()-10));
				selectedImage = null;
				//inventoryImages[selectImageGrid] = null;
			}
		}

		//at this point if a valid grid is clicked it selected the image
		if(selectedGrid != -1){
			if(inventoryImages[selectedGrid] != null){
				selectedImage = inventoryImages[selectedGrid];
				selectImageGrid = selectedGrid;
			}
		}
	}
}
