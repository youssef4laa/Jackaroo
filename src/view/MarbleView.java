package view;


import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.card.Marble;

public class MarbleView extends Group {

    private final Marble marble;
    private final Circle circle;

    public MarbleView(Marble marble, double centerX, double centerY, double radius, String cssColor) {
        this.marble = marble;
        this.circle = new Circle(centerX, centerY, radius);
        circle.setFill(Paint.valueOf(cssColor));
        circle.setStroke(Paint.valueOf("#000000"));      // default black stroke
        circle.setStrokeWidth(2);
        this.getChildren().add(circle);
    }

    public Marble getMarble() {
        return marble;
    }

    public void setPosition(double centerX, double centerY) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
    }

    public double getCenterX() {
        return circle.getCenterX();
    }

    public double getCenterY() {
        return circle.getCenterY();
    }

    public void highlight(boolean active) {
        if (active) {
            circle.setStroke(Paint.valueOf("#FFD700")); // gold highlight
            circle.setStrokeWidth(4);
        } else {
            circle.setStroke(Paint.valueOf("#000000"));
            circle.setStrokeWidth(2);
        }
    }
}