package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Colour;

/**
 * Controller for the winner dialog that appears when a player wins the game.
 */
public class WinnerViewController {

    @FXML private StackPane root;
    @FXML private Label winnerLabel;

    /**
     * Must be called right after FXMLLoader.load()
     * Sets the winner text and color in the dialog.
     * 
     * @param winner The color of the winning player
     */
    public void setWinner(Colour winner) {
        winnerLabel.setText(winner.name() + " wins!");
        // tint text to match the player colour
        winnerLabel.setTextFill(Color.valueOf(winner.name()));
    }

    /**
     * Handles the OK button click to close the dialog.
     */
    @FXML
    private void onOk() {
        // Close the dialog window
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}