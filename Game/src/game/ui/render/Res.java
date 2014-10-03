package game.ui.render;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	public static Map<String, BufferedImage> images;

	public static final String[] SUPPORTED_IMAGE_TYPES = new String[]{"png", "gif"};

	/**
	 * Reads, from files, all images which are commonly used in the game
	 */
	public static void readInAllCommonImages(){
		images = new HashMap<String, BufferedImage>();

		addImage("Char3");
		addImage("Char2");
		addImage("Char1");
		addImage("Player");
		addImage("Tree");
		addImage("Table");
		addImage("Key");
		addImage("Star1");
	}

	/**
	 * Adds an image to the local image storage
	 * if an image isn't found with that name, an image of a question mark will be added instead
	 * @param name
	 */
	public static void addImage(String name){

		BufferedImage image = null;

		// try to find image from the name (test name with all supported file extensions)
		for (String supportedImageType : SUPPORTED_IMAGE_TYPES){
			try {
				image = readImage(TEST_PATH + name + "." + supportedImageType);
			} catch (IOException e) {
				continue; //try another image type
			}
			break; // found the image
		}
		// if image isn't found
		if (image == null){
			try {
				image = readImage(UNKNOWN_IMAGE_PATH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		images.put(name, image);
	}

	/**
	 * if no image found, return 'unknown image' image
	 * if unknown image isn't found, throw error
	 * @param fileName
	 * @return
	 */
	public static BufferedImage readImage(String fileName) throws IOException{

		BufferedImage image = ImageIO.read(new File(fileName));
		image = createCompatibleImage(image);
		return image;
	}

	/**
	 * I believe this does:
	 * Converts a bufferedimage into a format which can be read faster on the current machine
	 * Found here:
	 * http://stackoverflow.com/questions/6319465/fast-loading-and-drawing-of-rgb-data-in-bufferedimage
	 * But that was originally from another question (which I couldn't find).
	 * @param image
	 * @return a better bufferedimage
	 */
	public static BufferedImage createCompatibleImage(BufferedImage image){

	    GraphicsConfiguration gc = GraphicsEnvironment.
	        getLocalGraphicsEnvironment().
	        getDefaultScreenDevice().
	        getDefaultConfiguration();

	    BufferedImage newImage = gc.createCompatibleImage(
	        image.getWidth(),
	        image.getHeight(),
	        Transparency.TRANSLUCENT);

	    Graphics2D g = newImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return newImage;
	}

	/**
	 * @param name
	 * @return whether there is an image associated with this name
	 */
	public static boolean isImage(String name){
		return images.containsKey(name);
	}


	/**
	 * Gets an image for the name
	 * OR returns the unknown image (question mark)
	 * @param name
	 * @return an image represented by name
	 */
	public static BufferedImage getImageFromName(String name){
		if (images == null){
			images = new HashMap<String, BufferedImage>();
		}
		return images.get(name);
	}

}
