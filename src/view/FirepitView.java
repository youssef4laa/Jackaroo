package view;


import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.card.Marble;

import java.util.List;

/**
 * A JavaFX component for displaying the marbles currently in the firepit.
 */
public class FirepitView extends HBox {

    private static final int MARBLE_RADIUS = 15;

    public FirepitView() {
        this.setSpacing(10);
        this.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: black;");
    }

    /**
     * Updates the firepit view with the current marbles.
     *
     * @param marbles list of marbles currently in the firepit
     */
    public void update(List<Marble> marbles) {
        this.getChildren().clear();

        for (Marble marble : marbles) {
            Circle marbleCircle = new Circle(MARBLE_RADIUS);
            marbleCircle.setFill(getJavaFXColor(marble));
            marbleCircle.setStroke(Color.BLACK);
            this.getChildren().add(marbleCircle);
        }
    }

    /**
     * Maps your model's Colour to JavaFX Color.
     * You can expand this to match your Colour enum or class.
     */
    private Color getJavaFXColor(Marble marble) {
        switch (marble.getColour().toString().toUpperCase()) {
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            case "GREEN":
                return Color.GREEN;
            case "YELLOW":
                return Color.GOLD;
            default:
                return Color.GRAY;
        }
    }
}


