package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.card.Deck;

/**
 * A JavaFX view component that shows the deck as a stack of cards
 * with the remaining count overlaid.
 */
public class DeckView extends StackPane {

    private final ImageView deckBackView;
    private final Label countLabel;

    /**
     * Constructs the DeckView. Loads the card-back image, sets up sizing,
     * and initializes the count label.
     */
    public DeckView() {
        // Load a card-back image (place deck_back.png on your classpath)
        deckBackView = new ImageView(new Image(getClass().getResourceAsStream("/images/deck_back.png")));
        deckBackView.setFitWidth(60);
        deckBackView.setFitHeight(90);

        // Label to show remaining cards
        countLabel = new Label();
        countLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        countLabel.setTranslateY(30);  // position at bottom of card

        // Stack image and count label
        this.getChildren().addAll(deckBackView, countLabel);
        this.setAlignment(Pos.CENTER);

        // Initial count
        updateCount();
    }

    /**
     * Call this after any change to the deck to refresh the displayed count.
     */
    public void updateCount() {
        // Deck.getPoolSize() returns the current number of cards left :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
        countLabel.setText(String.valueOf(Deck.getPoolSize()));
    }
}
