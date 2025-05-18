package view;

import controller.BoardController; // Import the BoardController
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    // These constants seem related to an older grid-based approach,
    // they might not be needed with the new BoardView/Controller structure
    // private static final int ROWS = 10;
    // private static final int COLS = 10;
    // private static final double CELL_SIZE = 60;

    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu(primaryStage);
        startMenu.show(playerName -> {
            try {
                // Call buildGameUI with the stage and player name
                buildGameUI(primaryStage, playerName);
            } catch (IOException e) {
                // Handle potential IO errors during game setup (e.g., loading resources)
                showErrorAndExit("Failed to start game: " + e.getMessage());
            } catch (Exception e) {
                // Catch any other unexpected exceptions during UI building
                showErrorAndExit("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        });
    }

    private void buildGameUI(Stage stage, String playerName) throws IOException {
        // Instantiate the BoardController, which sets up the game model and view
        BoardController boardController = new BoardController(playerName);

        // Get the root JavaFX node (the BorderPane) from the controller
        BorderPane root = boardController.getGameView();

        // Create a new scene with the root node provided by the controller
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        stage.setScene(scene);

        // Set the stage title
        stage.setTitle("Jackaroo — Welcome, " + playerName);

        // You can adjust resizable based on whether your layout handles resizing well
        // stage.setResizable(false); // Keeping resizable false for now

        // Display the stage
        stage.show();
    }

    /**
     * Displays an error alert and exits the application.
     * @param msg The error message to display.
     */
    private void showErrorAndExit(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText("Fatal Error");
        alert.setContentText(msg);
        alert.showAndWait();
        Platform.exit(); // Exit the JavaFX application
        System.exit(1); // Ensure the process terminates
    }

    public static void main(String[] args) {
        launch(args);
    }
}
