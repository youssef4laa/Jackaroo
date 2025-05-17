package view;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import exception.IllegalMovementException;
import exception.InvalidCardException;

/**
 * Represents a card in the Jackaroo game.
 * Each card has a type, suit (if applicable), and a visual representation.
 */
public class Card {
    private String type;
    private String suit;
    private Button cardButton;
    private static CardImageLoader imageLoader = new CardImageLoader();
    
    /**
     * Creates a new card with the specified type and suit.
     * 
     * @param type the type of card (e.g., "king", "queen", "burner")
     * @param suit the suit of the card (e.g., "heart", "spade"), or null for special cards
     */
    public Card(String type, String suit) {
        this.type = type;
        this.suit = suit;
        
        // Create the card button with the appropriate image
        createCardButton();
    }
    
    /**
     * Creates a button with the card image.
     */
    private void createCardButton() {
        cardButton = new Button();
        
        // Determine the image name based on the card type and suit
        String imageName;
        if (suit != null) {
            imageName = type + "_" + suit;
        } else {
            imageName = type;
        }
        
        // Create an ImageView with the card image
        ImageView imageView = new ImageView(imageLoader.getCardImage(imageName));
        
        // Set the image size to fit within the button
        imageView.setFitWidth(100);  // Adjust as needed
        imageView.setFitHeight(150); // Adjust as needed
        imageView.setPreserveRatio(true);
        
        // Set the image as the button's graphic
        cardButton.setGraphic(imageView);
        
        // Remove the button's background and border for a cleaner look
        cardButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Add default action handler with exception handling
        cardButton.setOnAction(event -> {
            try {
                // This would be replaced with actual game engine calls
                // For example: gameEngine.playCard(this);
                System.out.println("Card played: " + this);
            } catch (InvalidCardException e) {
                showErrorAlert("Invalid Card", e.getMessage());
            } catch (IllegalMovementException e) {
                showErrorAlert("Illegal Movement", e.getMessage());
            } catch (Exception e) {
                showErrorAlert("Error", "An unexpected error occurred: " + e.getMessage());
            }
        });
    }
    
    /**
     * Shows an error alert dialog with the specified title and message.
     * 
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Gets the button representing this card.
     * 
     * @return the card button
     */
    public Button getCardButton() {
        return cardButton;
    }
    
    /**
     * Gets the type of this card.
     * 
     * @return the card type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the suit of this card.
     * 
     * @return the card suit, or null for special cards
     */
    public String getSuit() {
        return suit;
    }
    
    @Override
    public String toString() {
        if (suit != null) {
            return type + " of " + suit;
        } else {
            return type;
        }
    }
}