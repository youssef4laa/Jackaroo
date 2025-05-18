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
        stage.sizeToScene();
        stage.setTitle("Jackaroo — Welcome, " + playerName);
        stage.setResizable(false);
        stage.show();
    }



    /**
     * Creates a side panel containing the player's icon, colored name below at double size, and a placeholder.
     * @param icon      The player icon
     * @param horizontal Whether panel is horizontal (top/bottom) or vertical (left/right)
     * @param name      The display name, e.g. "Mr. Red"
     * @param color     The CSS color for the name text
     */
    private Pane createSidePanel(Image icon, boolean horizontal, String name, String color) {
        double w = 4 * CELL_SIZE, h = CELL_SIZE;
        Region placeholder = new Region();
        placeholder.setPrefSize(w, h);
        placeholder.setMinSize(w, h);
        placeholder.setMaxSize(w, h);
        placeholder.setStyle("-fx-border-color: gray; -fx-border-style: dashed;");

        ImageView iv = new ImageView(icon);
        iv.setFitWidth(CELL_SIZE * 2);
        iv.setFitHeight(CELL_SIZE * 2);
        iv.setPreserveRatio(true);

        Label nameLabel = new Label(name);
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(CELL_SIZE * 2);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle("-fx-font-size: 2em; -fx-text-fill: " + color + ";");

        if (horizontal) {
            VBox iconBox = new VBox(5, iv, nameLabel);
            iconBox.setAlignment(Pos.CENTER);
            HBox box = new HBox(10, iconBox, placeholder);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(10));
            HBox.setHgrow(placeholder, Priority.NEVER);
            return box;
        } else {
            VBox box = new VBox(5, iv, nameLabel, placeholder);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(10));
            return box;
        }
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
