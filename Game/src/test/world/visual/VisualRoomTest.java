package test.world.visual;

import game.world.dimensions.*;

import java.awt.Graphics;

import javax.swing.*;

public class VisualRoomTest {
	
	public static void main(String arg[]){
		JFrame frame =  new JFrame("Test");
		JPanel panel = new JPanel(){
			public void paint(Graphics g){
				Dimension d = new Complement(new Rectangle(0,0,200,200), new Rectangle(0,0,20,20));
				for (int i = (int) d.getX(); i < d.getWidth(); i++){
					for (int j = (int) d.getY(); j < d.getHeight(); j++){
						if (d.contains(i, j)){
							g.fillRect(20+i, 20+j, 1, 1);
						}
					}
				}
			}
		};
		frame.add(panel);
		frame.setSize(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
}
