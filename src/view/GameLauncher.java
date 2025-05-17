package view;

import engine.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX8 launcher for Milestone 3: prompts for player name and initializes the game.
 */
public class GameLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Prompt label
        Label promptLabel = new Label("Enter your name:");

        // Text field for player name input
        TextField nameField = new TextField();
        nameField.setPromptText("Player Name");

        // Start button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(event -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                // Show warning if name is empty
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Invalid Name");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid name.");
                alert.showAndWait();
                return;
            }

            try {
                // Initialize the game engine with the provided name
                Game game = new Game(playerName);
                // TODO: proceed to launch the main game UI, e.g., pass 'game' to a controller/view

                // For now, confirm startup
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Initialized");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + playerName + "! The game has started.");
                alert.showAndWait();

                // Optionally, close the launcher window
                primaryStage.close();

            } catch (IOException e) {
                e.printStackTrace();
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Initialization Error");
                error.setHeaderText(null);
                error.setContentText("Failed to start the game: " + e.getMessage());
                error.showAndWait();
            }
        });

        // Layout for input and button
        HBox inputBox = new HBox(10, promptLabel, nameField);
        inputBox.setPadding(new Insets(20));

        VBox root = new VBox(10, inputBox, startButton);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jackaroo - Enter Player Name");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
