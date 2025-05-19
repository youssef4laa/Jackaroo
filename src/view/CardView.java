package view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.card.Card;
import model.card.standard.Standard;

public class CardView extends StackPane {

    private static final String CARD_DIR =
        "file:/Users/youssef/Code/jackaroo/resources/images/cards/";

    private final ObjectProperty<Card> cardProperty = new SimpleObjectProperty<>();
    private final Rectangle frame             = new Rectangle(71, 95);
    private final ImageView cardImageView     = new ImageView();
    private boolean faceUp = true;

    public CardView(Card card) {
        // set view size & alignment
        setPrefSize(frame.getWidth(), frame.getHeight());
        setAlignment(Pos.CENTER);

        // frame styling
        frame.setArcWidth(8);
        frame.setArcHeight(8);
        frame.setStroke(Color.DARKGRAY);
        frame.setStrokeWidth(1.1);
        frame.setFill(Color.WHITE);

        // image sizing
        cardImageView.setFitWidth(71);
        cardImageView.setFitHeight(95);
        cardImageView.setSmooth(true);
        cardImageView.setPreserveRatio(false);

        // stacking order: white background, then card pic
        getChildren().addAll(frame, cardImageView);
        setCard(card);

        // hover cursor
        addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> setCursor(Cursor.HAND));
        addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> setCursor(Cursor.DEFAULT));
    }

    /** Change which card this view should show. */
    public void setCard(Card card) {
        cardProperty.set(card);
        refresh();
    }

    public ReadOnlyObjectProperty<Card> cardProperty() {
        return cardProperty;
    }

    /** Flip face-up ⇄ face-down. */
    public void setFaceUp(boolean faceUp) {
        if (this.faceUp != faceUp) {
            this.faceUp = faceUp;
            refresh();
        }
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    /** Redraws based on current cardProperty and faceUp. */
    private void refresh() {
        Card c = cardProperty.get();

        // face-down or no card: show back
        if (c == null || !faceUp) {
            cardImageView.setImage(
                new Image(CARD_DIR + "card_back.png", 71, 95, true, true)
            );
            return;
        }

        // face-up + Standard card: map to tile###
        if (c instanceof Standard) {
            Standard std = (Standard) c;
            int rank = std.getRank();  // 1..13
            int base;
            switch (std.getSuit()) {
                case HEART:   base =  0; break; // tile000–012
                case CLUB:    base = 13; break; // tile013–025
                case DIAMOND: base = 26; break; // tile026–038
                case SPADE:   base = 39; break; // tile039–051
                default:      base =  0; break;
            }
            int idx = base + (rank - 1);
            String filename = String.format("tile%03d.png", idx);
            cardImageView.setImage(
                new Image(CARD_DIR + filename, 71, 95, true, true)
            );
        } else {
            // non-standard wild: fallback to back (or provide your own)
            cardImageView.setImage(
                new Image(CARD_DIR + "card_back.png", 71, 95, true, true)
            );
        }
    }
}
