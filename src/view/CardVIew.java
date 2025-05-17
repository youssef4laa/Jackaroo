package view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.card.Card;
import model.card.standard.Standard;
import model.card.wild.Wild;

/**
 * A JavaFX component that shows a single card face.
 */
public class CardView extends StackPane {

    private Card card;
    private final ImageView imageView;

    /**
     * Constructs a CardView for the given card.
     * Assumes you have PNGs under /images/cards/ named like "7_club.png", "ace_spade.png", "burner.png", etc.
     */
    public CardView(Card card) {
        this.card = card;
        this.imageView = new ImageView(loadCardImage(card));
        imageView.setFitWidth(60);
        imageView.setFitHeight(90);

        getChildren().add(imageView);
        setAlignment(Pos.CENTER);
        getStyleClass().add("card-view"); // for CSS borders, hover effects, etc.
    }

    /**
     * Updates this view to show a different card.
     */
    public void setCard(Card card) {
        this.card = card;
        imageView.setImage(loadCardImage(card));
    }

    public Card getCard() {
        return card;
    }

    /**
     * Chooses the correct image resource for the card.
     */
    private Image loadCardImage(Card card) {
        String filename;

        if (card instanceof Standard) {
            Standard s = (Standard) card;
            // e.g. "/images/cards/7_club.png"
            filename = String.format(
                "/images/cards/%d_%s.png",
                s.getRank(),
                s.getSuit().name().toLowerCase()
            );
        } else if (card instanceof Wild) {
            // e.g. "/images/cards/burner.png" or "saver.png"
            filename = "/images/cards/" +
                       card.getClass().getSimpleName().toLowerCase() +
                       ".png";
        } else {
            // fallback to back-of-card
            filename = "/images/cards/card_back.png";
        }

        return new Image(getClass().getResourceAsStream(filename));
    }
}
