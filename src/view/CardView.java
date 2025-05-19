/*  ────────────────────────────────────────────────────────────────────────────
 *  CardView.java  (package view)
 *  JavaFX-8-compatible version
 *  ──────────────────────────────────────────────────────────────────────────── */
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.card.Card;
import model.card.standard.Standard;

public class CardView extends StackPane {

    /* ── public read-only property so the controller can bind if desired ── */
    private final ObjectProperty<Card> cardProperty = new SimpleObjectProperty<>();

    private final Rectangle frame       = new Rectangle(100, 140); // constant size
    private final VBox      faceContainer = new VBox(2);          // rank / suit / name
    private final ImageView suitIcon      = new ImageView();
    private       boolean   faceUp        = true;

    public CardView(Card card) {
        setPrefSize(frame.getWidth(), frame.getHeight());
        setAlignment(Pos.CENTER);

        frame.setArcWidth(12);
        frame.setArcHeight(12);
        frame.setStroke(Color.DARKGRAY);
        frame.setStrokeWidth(1.1);

        faceContainer.setAlignment(Pos.CENTER);

        getChildren().addAll(frame, faceContainer);               // order matters
        setCard(card);

        /* simple hover cue so users know the card is clickable */
        addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> setCursor(Cursor.HAND));
        addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> setCursor(Cursor.DEFAULT));
    }

    /** Changes which card is rendered and refreshes the graphic. */
    public void setCard(Card card) {
        cardProperty.set(card);
        refresh();
    }

    public ReadOnlyObjectProperty<Card> cardProperty() {
        return cardProperty;
    }

    /** Flips the card visually (face-up ⇄ face-down). */
    public void setFaceUp(boolean faceUp) {
        if (this.faceUp != faceUp) {
            this.faceUp = faceUp;
            refresh();
        }
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    /* ── private helpers ──────────────────────────────────────────────── */
    private void refresh() {
        Card c = cardProperty.get();
        faceContainer.getChildren().clear();

        if (c == null || !faceUp) {
            frame.setFill(Color.DARKRED);          // back of the card
            suitIcon.setImage(null);
            return;
        }

        frame.setFill(Color.WHITE);

        Text rankText = new Text();
        Text nameText = new Text(c.getName());
        /* “Montserrat” may not exist on every PC; JavaFX will silently fall back */
        nameText.setFont(Font.font("Montserrat", FontWeight.SEMI_BOLD, 10));

        /* If the engine card is a Standard card we can show rank & suit */
        if (c instanceof Standard) {               // Java 8-style instanceof
            Standard std = (Standard) c;           // explicit cast required
            rankText.setText(String.valueOf(std.getRank()));
            rankText.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));

            /* Lookup an image according to suit, e.g. “/icons/heart.png” –
               adjust the path to match your resource layout. */
            String iconPath = "/icons/" + std.getSuit().name().toLowerCase() + ".png";
            suitIcon.setImage(new Image(iconPath, 18, 18, true, true));
        } else {
            rankText.setText("");                  // wild cards don’t need a rank here
            suitIcon.setImage(null);
        }

        faceContainer.getChildren().addAll(rankText, suitIcon, nameText);
    }
}
