package game.ui.window;

import game.ui.render.Res;
import game.ui.window.menus.MenuUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StarMation {
	ArrayList<Star> stars;
	
	private int numbofStars = 300;
	private int maxWidth;
	private int maxHeight;
	
	public StarMation(){
		this.theta = 45; 
		this.stars = new ArrayList<Star>();
		this.maxHeight = GameWindow.FRAME_HEIGHT;
		this.maxWidth = GameWindow.FRAME_WIDTH;
		
		setUpStarMation();
	}
	
	public void setUpStarMation(){
		
		for(int i = 0; i < numbofStars; i++){
			stars.add(createStar(false));
		}
	}
	
	public Star createStar(boolean outside){
		int  randomX = (int) (Math.random()*maxWidth);
		//if(!outside)randomX = (int) (Math.random()*maxWidth);
		
		int randomY = (int) (Math.random()*maxHeight);
		//if(outside)randomY = (int) maxHeight + 20;
		
		
		int halfChance = (int)(Math.random()*10);
		
		if(outside){
		if(halfChance > 5){
			randomY = (int) maxHeight + 20;
		}
		else{
			randomX = -20;
		}
		}

		
		int randomSize = (int)(Math.random()*60);
		if(randomSize < 1)randomSize = 1;
		BufferedImage starImage =  MenuUtil.scale(Res.getImageFromName("Star"), randomSize, randomSize) ;
		
		return new Star(starImage,randomX,randomY);
	}
	
	private int origX = maxWidth/2;
	private int origY = maxHeight;
	
	double a = 600;
	double b = 800;
	double r = 500;
	double theta =  45; //TODO fix 
	
	public void render(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, maxWidth, maxHeight);
		
		if(stars.size() < numbofStars)stars.add(createStar(true));
		
		for(int i = 0; i < stars.size(); i++){
			
			g.drawImage(stars.get(i).getImage(), stars.get(i).getX(),stars.get(i).getY() ,null);
			
			stars.get(i).setX(stars.get(i).getX()+stars.get(i).speed);
			stars.get(i).setY(stars.get(i).getY()-1);

			
			if(stars.get(i).getX() > maxWidth)stars.remove(stars.get(i));
//			System.out.println("Y :" +stars.get(i).getY() + " X : " +  stars.get(i).getY() );
//			
//			theta = theta + Math.toRadians(10);
//			System.out.println("theta : "+theta );
//			
//			// try somethign 
//			if(stars.get(i).getX() < maxWidth){
//				stars.get(i).setX((int) (a +r*Math.cos(theta)));
//			}else{
//				stars.get(i).setX(0);
//			}
//			if(stars.get(i).getY() < maxHeight){
//				stars.get(i).setY((int) (b +r*Math.sin(theta)));
//			}else{
//				stars.get(i).setX(0);
//			}	
		}
	}
	
	 public class Star{
		private int x;
		private int y;
		public int speed;
		private BufferedImage image;
		 
		 public Star(BufferedImage image, int x, int y){
			 this.x  = x;
			 this.y = y;
			 this.image = image;
			 this.speed = (int)(Math.random()*6);
			 if(speed < 1)speed = 1;
		 }
		 
		 public BufferedImage getImage(){
			 return this.image;
		 }
		 
		 public int getX(){ return this.x;}
		 public int getY(){return this.y;}
		 
		 public void setY(int y){
			 this.y = y;
		 }
		 

		 public void setX(int x){
			 this.x = x;
		 }
		 
		 
	}
}
