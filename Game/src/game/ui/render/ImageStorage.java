package game.ui.render;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @author hardwiwill
 *
 * Contains references to all resources (e.g. images)
 */
public class ImageStorage {

	public static final char SEP = File.separatorChar;
	public static final String RES_PATH = "res"+SEP;
	public static final String IMG_PATH = RES_PATH + "img" + SEP;
	public static final String LEVEL_PATH = RES_PATH + "level" + SEP;
	public static final String FLOOR_PATH = LEVEL_PATH + "floor" + SEP;
	public static final String LEVELS_PATH = LEVEL_PATH + "levels" + SEP;

	public static final String UNKNOWN_IMAGE_PATH = RES_PATH + "unknown_image.png";

	public static final Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public static final String[] SUPPORTED_IMAGE_TYPES = new String[]{"png", "gif"};

	/**
	 * Reads, from files, all images which are commonly used in the game
	 */
	public static void readInAllCommonImages(){

		addImage("space_ship");
		addImage("Char3");
		addImage("Char2");
		addImage("Char1");
		addImage("Player");
		addImage("Tree");
		addImage("Table");
		addImage("Key");
		addImage("Star1");
		addImage("Door");
		addImage("Chest");
		addImage("OpenChest");
		addImage("Tree");
		addImage("oxygen_tank_labelled");
		addImage("Trixel");

		addImagesFromDir("crystal");
		addImagesFromDir("teleporter");
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
				image = readImage(IMG_PATH + name + "." + supportedImageType);
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
	 * Adds all images at the given directory path
	 * If an image file can't be found, replace that image with a question-mark image
	 * If an image is incompatible, don't add to the map
	 * @param imagePath
	 */
	private static void addImagesFromDir(String imagePath) {

		File file = new File(IMG_PATH+imagePath);
		if (!file.isDirectory()){
			throw new IllegalArgumentException("imagePath should be a directory");
		}
		// goes through all files in dir, if there is a problem reading a file
		// input a UNKNOWN_IMAGE in place of the intended image.
		for (File imageFile : file.listFiles()){
			String fullImageName = imageFile.getName();
			String shortImageName = fullImageName.substring(0, fullImageName.length()-4); // omitting file extension.
			try {
				BufferedImage image = readImage(imageFile);
				images.put(shortImageName, image); // will only be put in if no IOExceptions thrown reading the file
			} catch (FileNotFoundException e) {
				try {
					images.put(shortImageName, readImage(UNKNOWN_IMAGE_PATH));
				} catch (IOException io) {
					System.err.println("Can't find question mark image");
				}
			} catch (IOException e1) {
				//incompatible image (such as .directory file). Don't make issue
				//System.err.println("Failed reading image \""+fullImageName+"\" from dir");
			}
		}
	}

	/**
	 * if no image found, return 'unknown image' image
	 * if unknown image isn't found, throw error
	 * @param fileName
	 * @return
	 */
	private static BufferedImage readImage(String fileName) throws IOException {
		return readImage(new File(fileName));
	}

	private static BufferedImage readImage(File imageFile) throws IOException {
		BufferedImage image = ImageIO.read(imageFile);
		if (image == null){
			// file wasn't an image.
			throw new IOException();
		}
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
	private static BufferedImage createCompatibleImage(BufferedImage image){

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
		return images.get(name);
	}

}
