package view;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading and caching card images.
 * Provides methods to retrieve card images with fallback to a default image.
 */
public class CardImageLoader {
    private static final String CARDS_DIRECTORY = "/resources/cards/";
    private static final String DEFAULT_IMAGE = "default.png";
    
    // Cache to store loaded images
    private final Map<String, Image> imageCache = new HashMap<>();
    
    // Default image to use when a specific card image is not found
    private Image defaultImage;
    
    /**
     * Initializes the CardImageLoader and loads the default image.
     */
    public CardImageLoader() {
        try {
            // Load the default image
            String defaultPath = CARDS_DIRECTORY + DEFAULT_IMAGE;
            defaultImage = loadImageFromPath(defaultPath);
            
            // If default image couldn't be loaded, create a simple placeholder
            if (defaultImage == null) {
                // Create a 1x1 pixel empty image as absolute fallback
                defaultImage = new Image(1, 1, true, true);
            }
        } catch (Exception e) {
            System.err.println("Error loading default card image: " + e.getMessage());
            // Create a 1x1 pixel empty image as absolute fallback
            defaultImage = new Image(1, 1, true, true);
        }
    }
    
    /**
     * Gets the image for a specific card.
     * 
     * @param cardName the name of the card (e.g., "king_heart", "burner")
     * @return the card image, or the default image if the specific card image is not found
     */
    public Image getCardImage(String cardName) {
        // Check if the image is already in the cache
        if (imageCache.containsKey(cardName)) {
            return imageCache.get(cardName);
        }
        
        // Try to load the image
        String imagePath = CARDS_DIRECTORY + cardName + ".png";
        Image cardImage = loadImageFromPath(imagePath);
        
        // If the image couldn't be loaded, use the default image
        if (cardImage == null) {
            System.out.println("Card image not found: " + imagePath + ". Using default image.");
            cardImage = defaultImage;
        }
        
        // Cache the image
        imageCache.put(cardName, cardImage);
        
        return cardImage;
    }
    
    /**
     * Loads an image from the specified path.
     * 
     * @param imagePath the path to the image
     * @return the loaded image, or null if the image couldn't be loaded
     */
    private Image loadImageFromPath(String imagePath) {
        try {
            File file = new File(System.getProperty("user.dir") + imagePath);
            return new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.err.println("Image not found: " + imagePath);
            return null;
        } catch (Exception e) {
            System.err.println("Error loading image " + imagePath + ": " + e.getMessage());
            return null;
        }
    }
}