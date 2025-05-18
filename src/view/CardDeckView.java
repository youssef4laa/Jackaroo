/* ────────────────────────────────────────────────────────────────────────────
 *  CardDeckView.java   (package view)
 *  A tiny Java 8‑compatible control that renders a face‑down deck of cards
 *  stacked with a slight offset.  Designed to sit next to the Fire‑deck in
 *  BoardView.  You can update the deck size at runtime via setCardCount().
 *  ──────────────────────────────────────────────────────────────────────────── */
package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Visual representation of a (face‑down) deck of cards.  Nothing here knows
 * about the *contents* of the deck – it only draws a neat stack whose height
 * reflects the current card‑count.
 */
public class CardDeckView extends StackPane {

    /* basic card metrics – match CardView so the backs look identical */
    private static final double CARD_W = 100;
    private static final double CARD_H = 140;
    private static final double OFFSET  = 2;     // pixel translation per layer

    private final IntegerProperty cardCount = new SimpleIntegerProperty();

    public CardDeckView(int initialCount) {
        setAlignment(Pos.CENTER_LEFT);           // left‑align origin for x‑offsets
        setCardCount(initialCount);

        /* Whenever the count changes, re‑draw the stack. */
        cardCount.addListener((obs, o, n) -> refresh());
    }

    // ── API ────────────────────────────────────────────────────────────────
    public int getCardCount()                 { return cardCount.get(); }
    public void setCardCount(int newCount)    { cardCount.set(newCount); }
    public IntegerProperty cardCountProperty(){ return cardCount; }

    // ── internals ─────────────────────────────────────────────────────────
    private void refresh() {
        getChildren().clear();
        int n = getCardCount();
        if (n <= 0) return;                    // nothing to draw

        for (int i = 0; i < n; i++) {
            Rectangle back = new Rectangle(CARD_W, CARD_H, Color.DARKRED);
            back.setArcWidth(12);
            back.setArcHeight(12);
            back.setStroke(Color.DARKGRAY);
            back.setStrokeWidth(1.1);

            /* Translate each successive card slightly up‑and‑right so you get
               a visible "fan" indicating how many remain. */
            back.setTranslateX(i * OFFSET);
            back.setTranslateY(-i * OFFSET);
            getChildren().add(back);
        }

        /* Resize the control so it encloses the entire stack. */
        double w = CARD_W + OFFSET * (n - 1);
        double h = CARD_H + OFFSET * (n - 1);
        setPrefSize(w, h);
    }
}
