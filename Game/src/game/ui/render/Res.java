package game.ui.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * @author hardwiwill
 * Contains references to all resources (e.g. images)
 */
public class Res {

	public static final char SEP = File.separatorChar;
	public static final String RES_PATH = "src" + SEP + "game" + SEP + "ui" + SEP + "render" + SEP + "res" + SEP;
	public static final String TEST_PATH = RES_PATH + "test" + SEP;
	public static final String IMG_PATH = RES_PATH + "img" + SEP;
	public static final String ROOM_PATH = IMG_PATH + "room" + SEP;

	public static final String UNKNOWN_IMAGE_PATH = RES_PATH + "unknown_image.png";
	//public static final String tableName = "table.png";

	/**
	 * @param name
	 * @return whether there is an image associated with this name
	 */
	public static boolean isImage(String name){
		return true; // TODO: not this
	}


	/**
	 * if no image found, return 'unknown image' image
	 * if unknown image isn't found, throw error
	 * @param name
	 * @return an image represented by name
	 */
	public static BufferedImage getImageFromName(String name){
		String filePath = TEST_PATH + name + ".png";
		File f = new File(filePath);
		if (!f.exists()){
			filePath = TEST_PATH + name + ".gif";

		}
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			//e.printStackTrace();
			try {
				return ImageIO.read(new File(UNKNOWN_IMAGE_PATH));
			} catch (IOException e1) {
				throw new Error("Can't find image '"+filePath+"'");
			}
		}
	}

}
