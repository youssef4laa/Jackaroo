package view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * A simple helper for showing error popups.
 * Any closed alert will NOT terminate the game—only closing the main window will.
 */
public class ExceptionPopup {

    private ExceptionPopup() {
        // prevent instantiation
    }

    /**
     * Displays an error alert with the given header and message.
     *
     * @param header  short description (e.g. "Invalid Action")
     * @param message detailed reason (e.getMessage())
     */
    public static void showError(String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(message);
            // showAndWait() will block only the dialog, not the whole game
            alert.showAndWait();
        });
    }

    /**
     * Convenience to show any thrown GameException.
     *
     * @param ex the exception to display
     */
    public static void showException(Exception ex) {
        showError("Invalid Action", ex.getMessage());
    }
}
