package view;

import engine.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final double CELL_SIZE = 60;

    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu(primaryStage);
        startMenu.show(playerName -> {
            try {
                buildGameUI(primaryStage, playerName);
            } catch (IOException e) {
                showErrorAndExit("Failed to start game: " + e.getMessage());
            }
        });
    }

    private void buildGameUI(Stage stage, String playerName) throws IOException {
        BorderPane root = BoardView.create(playerName); // <-- Use the static factory method
        Scene scene = new Scene(root);                  // <-- Use root directly
        stage.setScene(scene);
        // stage.sizeToScene(); // Removing sizeToScene might allow the window to be smaller than the preferred size, potentially showing clipping
        stage.setTitle("Jackaroo — Welcome, " + playerName);
        stage.setResizable(false); // Keeping resizable false for now, but flexible layout is key for resizable
        stage.show();
    }



  
    private void showErrorAndExit(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Initialization Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}