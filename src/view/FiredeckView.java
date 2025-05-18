package view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.card.Card; // Assuming Card is in model.card
import model.card.standard.Standard; // Assuming Standard is in model.card.standard
// Import other card types if specific handling is needed, or rely on getName()

public class FiredeckView extends StackPane {

    private ImageView topCardImageView;
    private static final double CARD_WIDTH = 80; // Adjust as needed
    private static final double CARD_HEIGHT = 120; // Adjust as needed
    private Image cardBackImage;

    public FiredeckView() {
        // Load card back image
        try {
            cardBackImage = new Image(getClass().getResourceAsStream("/images/cards/card_back.png"));
        } catch (NullPointerException e) {
            System.err.println("Error loading card back image. Make sure '/images/cards/card_back.png' exists.");
            cardBackImage = null; // Handle missing image gracefully if necessary
        }

        topCardImageView = new ImageView();
        topCardImageView.setFitWidth(CARD_WIDTH);
        topCardImageView.setFitHeight(CARD_HEIGHT);
        topCardImageView.setPreserveRatio(true);

        this.getChildren().add(topCardImageView);
        this.setAlignment(Pos.CENTER);
        // Set a preferred size for the FiredeckView itself, can be useful for layout
        this.setPrefSize(CARD_WIDTH + 20, CARD_HEIGHT + 20); // Adding some padding
        this.setStyle("-fx-border-color: darkgray; -fx-border-style: dashed; -fx-background-color: #00000033;"); // Visual cue for the area

        showEmptyState(); // Initially show card back or empty state
    }

    public void updateView(Card topCard) {
        if (topCard == null) {
            showEmptyState();
            return;
        }

        String imagePath = getCardImagePath(topCard);
        try {
            Image cardImage = new Image(getClass().getResourceAsStream(imagePath));
            topCardImageView.setImage(cardImage);
        } catch (NullPointerException e) {
            System.err.println("Error loading card image: " + imagePath);
            // Fallback to card back or a placeholder if specific card image is missing
            if (cardBackImage != null) {
                topCardImageView.setImage(cardBackImage);
            } else {
                topCardImageView.setImage(null); // Or a placeholder image
            }
        }
    }

    public void showEmptyState() {
        if (cardBackImage != null) {
            topCardImageView.setImage(cardBackImage); // Show card back when empty
        } else {
            topCardImageView.setImage(null); // Or a placeholder for empty
        }
    }

    private String getCardImagePath(Card card) {
        String cardName = card.getName().toLowerCase().replace(" ", "_");
        String rank = "";
        String suit = "";

        if (card instanceof Standard) {
            Standard standardCard = (Standard) card;
            // Assuming rank is int for 2-10, and specific names for face cards
            // Adjust based on your Standard.getRank() and Standard.getSuit() return types and values
            int rankValue = standardCard.getRank();
            switch (rankValue) {
                case 1: rank = "ace"; break; // Or 1 if your images are named '1_spade.png'
                case 11: rank = "jack"; break;
                case 12: rank = "queen"; break;
                case 13: rank = "king"; break;
                default: rank = String.valueOf(rankValue); break;
            }
            suit = standardCard.getSuit().toString().toLowerCase(); // e.g., "spade", "heart"
            return String.format("/images/cards/%s_%s.png", rank, suit);
        } else {
            // For Wild cards or other special cards, use their name
            // Assuming getName() returns "Burner", "Saver", etc.
            return String.format("/images/cards/%s.png", cardName);
        }
        // Example: "/images/cards/ace_spade.png", "/images/cards/burner.png"
    }
}