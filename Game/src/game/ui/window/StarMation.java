package game.ui.window;

import game.ui.render.ImageStorage;
import game.ui.window.menus.MenuUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StarMation {
	ArrayList<Star> stars;

	private int numbofStars = 200;
	private int maxWidth;
	private int maxHeight;
	private int size = 60;


	/**
	 * The constructor for starMation
	 * */
	public StarMation(){
		this.stars = new ArrayList<Star>();
		this.maxHeight = GameWindow.FRAME_HEIGHT;
		this.maxWidth = GameWindow.FRAME_WIDTH;

		setUpStarMation();
	}


	/**
	 * Creates the number of stars based on the number of stars variable
	 * */
	private void setUpStarMation(){
		for(int i = 0; i < numbofStars; i++){
			stars.add(createStar(false));
		}
	}


	/***
	 * Creates a star at a random location
	 * if outside is true it creates it outside the screen
	 * */
	private Star createStar(boolean outside){

		//set random variable based on screen size
		int randomX = (int) (Math.random()*maxWidth);
		int randomY = (int) (Math.random()*maxHeight+20);
		int halfChance = (int)(Math.random()*10);

		//if the star is outside the screen there should be 50/50 change to spawn outside x axis or y axis
		if(outside){
		if(halfChance > 5){
			randomY = maxHeight - 50;
		}
		else{
			randomX = -20;
			}
		}

		//set the star to a random size
		int randomSize = (int)(Math.random()*size);

		//make sure the size is at least 1
		if(randomSize < 1)randomSize = 1;

		return new Star( MenuUtil.scale(ImageStorage.getImageFromName("Star1"), randomSize, randomSize),randomX,randomY);
	}


	/**
	 * Renders all of the stars on the graphics object
	 * */
	public void render(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, maxWidth, maxHeight);

		//place down more stars if less than a certain count
		if(stars.size() < numbofStars)stars.add(createStar(true));

		for(int i = 0; i < stars.size(); i++){
			//draw the star at its location
			g.drawImage(stars.get(i).getImage(), stars.get(i).getX(),stars.get(i).getY() ,null);

			//increment its location
			stars.get(i).setX(stars.get(i).getX()+stars.get(i).speed);
			stars.get(i).setY(stars.get(i).getY()-1);

			//remove the star once it leaves the screen
			if(stars.get(i).getX() > maxWidth || stars.get(i).getY() > maxHeight )stars.remove(stars.get(i));
		}
	}

	 public class Star{
		private int x;
		private int y;
		public int speed;
		private int speedRange = 6;

		private BufferedImage image;

		public Star(BufferedImage image, int x, int y){
			 this.x = x;
			 this.y = y;
			 this.image = image;
			 this.speed = (int)(Math.random()*speedRange);
			 if(speed < 1)speed = 1;
		}

		 public BufferedImage getImage(){
			 return this.image;
		 }

		 public int getX(){ return this.x;}
		 public int getY(){return this.y;}

		 public void setY(int y){this.y = y;}
		 public void setX(int x){this.x = x;}
	}
}
