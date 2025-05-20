package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A single marble (with drop-shadow) at a given position.
 */
public class MarbleView extends Group {
    private final Circle shadow;
    private final Circle marble;

    /**
     * @param centerX   x-coordinate of marble center
     * @param centerY   y-coordinate of marble center
     * @param radius    radius of the marble (shadow uses same radius)
     * @param cssColor  fill color, e.g. "#ff0000"
     */
    public MarbleView(double centerX, double centerY, double radius, String cssColor) {
        // shadow, slight offset
        double offset = radius * 0.1;
        shadow = new Circle(centerX + offset, centerY + offset, radius);
        shadow.setFill(Color.web(cssColor, 0.33));
        shadow.setStroke(null);

        // main marble
        marble = new Circle(centerX, centerY, radius);
        marble.setFill(Color.web(cssColor));
        marble.setStroke(Color.BLACK);
        marble.setStrokeWidth(1);

        getChildren().addAll(shadow, marble);
    }

    /** Expose the marble node for event wiring. */
    public Circle getMarbleNode() {
        return marble;
    }
}
