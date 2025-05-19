package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Visual representation of the discard pile ("fire-pit").
 * Renders a small stack of cards fanned towards the left.
 */
public class FirePitView extends StackPane {

    private static final double CARD_W   = 100;
    private static final double CARD_H   = 140;
    private static final double OFFSET   = 2;

    private final IntegerProperty cardCount = new SimpleIntegerProperty();

    public FirePitView(int initialCount) {
        setAlignment(Pos.CENTER_RIGHT);
        setId("firePitView");
        setCardCount(initialCount);
        cardCount.addListener((obs, o, n) -> refresh());
    }

    public int getCardCount() { return cardCount.get(); }
    public void setCardCount(int newCount) { cardCount.set(newCount); }
    public IntegerProperty cardCountProperty() { return cardCount; }

    private void refresh() {
        getChildren().clear();
        int n = getCardCount();
        if (n <= 0) return;

        for (int i = 0; i < n; i++) {
            Rectangle back = new Rectangle(CARD_W, CARD_H, Color.DIMGREY);
            back.setArcWidth(12);
            back.setArcHeight(12);
            back.setStroke(Color.GRAY);
            back.setStrokeWidth(1.1);

            back.setTranslateX(-i * OFFSET);
            back.setTranslateY(-i * OFFSET);
            getChildren().add(back);
        }

        double w = CARD_W + OFFSET * (n - 1);
        double h = CARD_H + OFFSET * (n - 1);
        setPrefSize(w, h);
    }
}
