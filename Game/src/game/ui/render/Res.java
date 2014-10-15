package game.ui.render;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @author hardwiwill
 *
 *         Contains references to all resources (images and worlds)
 */
public class Res {

	public static final char SEP = File.separatorChar;
	public static final String RES_PATH = "res" + SEP;
	public static final String IMG_PATH = RES_PATH + "img" + SEP;
	public static final String WORLD_PATH = RES_PATH + "world" + SEP;
	public static final String FLOOR_PATH = WORLD_PATH + "floor" + SEP;
	public static final String WORLDS_PATH = WORLD_PATH + "worlds" + SEP;

	public static final String UNKNOWN_IMAGE_PATH = RES_PATH
			+ "unknown_image.png";

	public static final Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public static final String[] SUPPORTED_IMAGE_TYPES = new String[] { "png",
	"gif" };

	/**
	 * Reads, from files, all images which are commonly used in the game
	 * @author nicky van hulst
	 */
	public static void readInAllCommonImages(){
		//------------------- willls stuff----------------
		//		addImagesFromDir("character");
		//		addImagesFromDir("label");
		//		addImagesFromDir("crystal");
		//		addImagesFromDir("teleporter");
		//		addImagesFromDir("vine");
		//		addImagesFromDir("tree");
		//		addImagesFromDir("plant");

		//------------------- end of willls stuff----------------


		addImage("Air_tank");
		addImage("Char1");
		addImage("Char1_Multi");
		addImage("Char2");
		addImage("Char3");
		addImage("Char_Multi");
		addImage("Chest");
		addImage("Crystal");
		addImage("Door");
		addImage("Help");
		addImage("Key");
		addImage("Multi_player");
		addImage("OpenChest");
		addImage("Options");
		addImage("Plant");
		addImage("Player");
		addImage("Quit");
		addImage("Quit_Sel");
		addImage("Single_Player");
		addImage("Star");
		addImage("Star1");
		addImage("Star1");
		addImage("Table");
		addImage("Tree");
		addImage("Trixel");
		addImage("alien_plant");
		addImage("alien_plant_1");
		addImage("alien_vine_0");
		addImage("alien_vine_1");
		addImage("alien_vine_10");
		addImage("alien_vine_2");
		addImage("alien_vine_3");
		addImage("alien_vine_4");
		addImage("alien_vine_5");
		addImage("alien_vine_6");
		addImage("alien_vine_7");
		addImage("alien_vine_8");
		addImage("alien_vine_9");
		addImage("background_0");
		addImage("background_1");
		addImage("background_10");
		addImage("background_11");
		addImage("background_12");
		addImage("background_13");
		addImage("background_14");
		addImage("background_15");
		addImage("background_16");
		addImage("background_17");
		addImage("background_18");
		addImage("background_19");
		addImage("background_2");
		addImage("background_3");
		addImage("background_4");
		addImage("background_5");
		addImage("background_6");
		addImage("background_7");
		addImage("background_8");
		addImage("background_9");
		addImage("cabbage");
		addImage("crystal_large_black");
		addImage("crystal_large_blue");
		addImage("crystal_large_green");
		addImage("crystal_large_purple");
		addImage("crystal_large_red");
		addImage("crystal_large_white");
		addImage("flax");
		addImage("flower");
		addImage("flower_tree");
		addImage("flower_tree_2");
		addImage("long_pinecone");
		addImage("many_stars");
		addImage("oxygen_tank_labelled");
		addImage("oxygen_tank_plain");
		addImage("punga");
		addImage("roots_tree");
		addImage("space_ship");
		addImage("teleport_off");
		addImage("teleporter_on");
		addImage("tentacle");
		addImage("unknown_image");
		addImage("LockedPortal");
	}

	/**
	 * Used as a way to get all the image names from a directory to save time
	 * for the method above
	 * @author nicky van hulst
	 * */
	public static ArrayList<String> getAllImageNames() {
		ArrayList<String> imageNames = new ArrayList<String>();

		File file = new File("src/game/ui/Images");
		if (!file.isDirectory()) {
			throw new IllegalArgumentException(
					"imagePath should be a directory");
		}

		for (File imageFile : file.listFiles()) {
			String fullImageName = imageFile.getName();
			String shortImageName = fullImageName.substring(0,
					fullImageName.length() - 4); // omitting file extension.
			imageNames.add(shortImageName);
			System.out.println(shortImageName);
		}
		return imageNames;
	}


	/**
	 * Adds an image to the local image storage if an image isn't found with
	 * that name, an image of a question mark will be added instead
	 *
	 * @param name
	 */
	public static void addImage(String name) {

		java.net.URL resource = null;
		java.net.URL unknown = null;

		BufferedImage image = null;

		// try to find image from the name (test name with all supported file
		// extensions)
		for (String supportedImageType : SUPPORTED_IMAGE_TYPES) {
			try {
				resource = Res.class.getResource("Images" + "/" + name + "."
						+ supportedImageType);
				unknown = ClassLoader.getSystemResource(UNKNOWN_IMAGE_PATH);

				if (resource == null){
					continue;
				}
				// image = readImage(IMG_PATH + name + "." +
				// supportedImageType);
				image = readImage(resource);
			} catch (IOException e) {
				continue; // try another image type
			}
			break; // found the image
		}
		// if image isn't found
		if (image == null) {
			try {
				image = readImage(unknown, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		images.put(name, image);
	}



	/**
	 * if no image found, return 'unknown image' image if unknown image isn't
	 * found, throw error
	 *
	 * @param fileName
	 * @return
	 * @deprecated
	 */
	private static BufferedImage readImage(java.net.URL resource,
			String fileName) throws IOException {
		return readImage(resource);
	}

	private static BufferedImage readImage(java.net.URL resource)
			throws IOException {

		BufferedImage image = ImageIO.read(resource);

		if (image == null) {
			// file wasn't an image.
			throw new IOException();
		}
		image = createCompatibleImage(image);
		return image;
	}

	/**
	 * I believe this does: Converts a bufferedimage into a format which can be
	 * read faster on the current machine Found here:
	 * http://stackoverflow.com/questions
	 * /6319465/fast-loading-and-drawing-of-rgb-data-in-bufferedimage But that
	 * was originally from another question (which I couldn't find).
	 *
	 * @param image
	 * @return a better bufferedimage
	 */
	private static BufferedImage createCompatibleImage(BufferedImage image) {

		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		BufferedImage newImage = gc.createCompatibleImage(image.getWidth(),
				image.getHeight(), Transparency.TRANSLUCENT);

		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return newImage;
	}

	/**
	 * @param name
	 * @return whether there is an image associated with this name
	 */
	public static boolean isImage(String name) {
		return images.containsKey(name);
	}

	/**
	 * Gets an image for the name OR returns the unknown image (question mark)
	 *
	 * @param name
	 * @return an image represented by name
	 */
	public static BufferedImage getImageFromName(String name) {
		return images.get(name);
	}

}
