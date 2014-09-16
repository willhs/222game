package game.ui.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author hardwiwill
 * Contains references to all resources (e.g. images)
 */
public class Res {

	public static String RES_PATH = "src" + File.separator + "game" + File.separator + "render" + File.separator + "res" + File.separator;
	public static String UNKNOWN_IMAGE_PATH = RES_PATH + "unknown";

	/**
	 * @param drawableName
	 * @return whether there is an image associated with this name
	 */
	public static boolean isImage(String drawableName){
		return true; // TODO: not this
	}


	/**
	 * @param name
	 * @return an image represented by name
	 * if no image found, return 'unknown image' image
	 * if unknown image isn't found, throw error
	 */
	public static BufferedImage getImageFromName(String name){
		String filePath = RES_PATH + name;
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				return ImageIO.read(new File(UNKNOWN_IMAGE_PATH));
			} catch (IOException e1) {
				throw new Error("Can't find image '"+name+"'");
			}
		}
	}

}
