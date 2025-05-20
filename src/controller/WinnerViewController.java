package controller;
import view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Colour;   // your enum

public class WinnerViewController {

    @FXML private StackPane root;
    @FXML private Label winnerLabel;

    /**
     * Must be called right after FXMLLoader.load()
     */
    public void setWinner(Colour winner) {
        winnerLabel.setText(winner.name() + " wins!");
        // tint text to match the player colour
        winnerLabel.setTextFill(Color.valueOf(winner.name()));
    }

    @FXML
    private void onOk() {
        // remove the overlay by closing its window, or if you added to a parent pane, just hide it:
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
