package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.card.Deck;
import javafx.scene.input.MouseButton;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * A JavaFX component displaying the deck as a card-back image
 * with an overlaid count of remaining cards.  Clicks on the top
 * of the deck invoke a user-supplied handler.
 * 
 * Now with a white outline on hover!
 */
public class DeckView extends StackPane {
    private static final String IMAGE_PATH = "/images/deck_back.png";
    private static final double CARD_WIDTH  = 60;
    private static final double CARD_HEIGHT = 90;

    private final ImageView deckBackView;
    private final Label    countLabel;
    private Consumer<MouseEvent> onClickHandler;

    // store the original drop-shadow so we can revert to it
    private final DropShadow baseShadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.4));
    // create a white outline effect for hover
    private final DropShadow hoverShadow = new DropShadow(8, Color.WHITE);

  public DeckView() {
    // Align content and add base drop shadow
    setAlignment(Pos.CENTER);
    setPadding(new Insets(5));
    setEffect(baseShadow);

    // Load card-back image
    InputStream imgStream = getClass().getResourceAsStream(IMAGE_PATH);
    if (imgStream == null) {
        throw new IllegalStateException("Missing resource: " + IMAGE_PATH);
    }
    Image deckImage = new Image(imgStream);

    // Setup ImageView
    deckBackView = new ImageView(deckImage);
    deckBackView.setFitWidth(CARD_WIDTH);
    deckBackView.setFitHeight(CARD_HEIGHT);
    deckBackView.setPickOnBounds(true); // make transparent pixels clickable

    // Setup count label
    countLabel = new Label();
    countLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    countLabel.setTranslateY(CARD_HEIGHT / 2 - 12);

    // Add children to the StackPane
    getChildren().addAll(deckBackView, countLabel);

    // Click handling on both the image and the full pane (outline area)
    deckBackView.setOnMouseClicked(this::handleClick);
    this.setOnMouseClicked(this::handleClick);

    // Hover effects: white outline on enter, revert on exit
    this.setOnMouseEntered(e -> setEffect(hoverShadow));
    this.setOnMouseExited(e -> setEffect(baseShadow));

    // Initial count display
    updateCount();
}

  private void handleClick(MouseEvent event) {
	  if (onClickHandler != null 
	      && event.getButton() == MouseButton.PRIMARY
	      && event.getClickCount() > 0) {
	    onClickHandler.accept(event);
	  }
	}
    

    /**
     * Refreshes the displayed count of cards. Safe to call from any thread.
     */
    public void updateCount() {
        Runnable updater = () -> countLabel.setText(String.valueOf(Deck.getPoolSize()));
        if (Platform.isFxApplicationThread()) updater.run();
        else Platform.runLater(updater);
    }

    /**
     * Sets the click handler invoked when the deck is clicked.
     */
    public void setOnDeckClicked(Consumer<MouseEvent> handler) {
        this.onClickHandler = handler;
    }
}
