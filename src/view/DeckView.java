package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import model.card.Deck;

/**
 * A JavaFX component displaying the deck as a card-back image
 * with an overlaid count of remaining cards.
 */
public class DeckView extends StackPane {
    private static final String IMAGE_PATH = "/images/deck_back.png";
    private final ImageView deckBackView;
    private final Label countLabel;

    public DeckView() {
        // Load card-back image, fail fast if missing
        InputStream imgStream = getClass().getResourceAsStream(IMAGE_PATH);
        if (imgStream == null) {
            throw new IllegalStateException("Missing resource: " + IMAGE_PATH);
        }
        Image deckImage = new Image(imgStream);

        deckBackView = new ImageView(deckImage);
        deckBackView.setFitWidth(60);
        deckBackView.setFitHeight(90);

        countLabel = new Label();
        countLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        countLabel.setTranslateY(30);  // Position at bottom of the card

        // Compose the view
        getChildren().addAll(deckBackView, countLabel);
        setAlignment(Pos.CENTER);

        // Initial count
        updateCount();
    }

    /**
     * Refreshes the displayed count of cards. Safe to call from any thread.
     */
    public void updateCount() {
        Runnable updater = () -> {
            int size;
            try {
                size = Deck.getPoolSize();
            } catch (NullPointerException e) {
                System.err.println("⚠ Deck not initialized: " + e.getMessage());
                size = 0;
            }
            countLabel.setText(String.valueOf(size));
        };

        if (Platform.isFxApplicationThread()) {
            updater.run();
        } else {
            Platform.runLater(updater);
        }
    }
}
