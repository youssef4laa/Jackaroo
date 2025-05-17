package view;

import engine.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final double CELL_SIZE = 60;
    private static final double MARGIN = 200;

    @Override
    public void start(Stage primaryStage) {
        // First show the start menu. When done, build the game UI.
        StartMenu startMenu = new StartMenu(primaryStage);
        startMenu.show(playerName -> {
            try {
                buildGameUI(primaryStage, playerName);
            } catch (IOException e) {
                showErrorAndExit("Failed to start game: " + e.getMessage());
            }
        });
    }

    /**
     * Constructs the main game UI—board, side panels, etc.—and shows it on stage.
     */
    private void buildGameUI(Stage stage, String playerName) throws IOException {
        // initialize game engine
        new Game(playerName);

        // load player icons
        Image[] icons = new Image[] {
            new Image(getClass().getResourceAsStream("/images/player_red.png")),
            new Image(getClass().getResourceAsStream("/images/player_green.png")),
            new Image(getClass().getResourceAsStream("/images/player_blue.png")),
            new Image(getClass().getResourceAsStream("/images/player_yellow.png"))
        };

        // build board grid
        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setPadding(new Insets(10));
        boardGrid.setStyle("-fx-background-color: lightgray;");
        boardGrid.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Pane cell = new Pane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                boardGrid.add(cell, c, r);
            }
        }

        // root layout
        BorderPane root = new BorderPane();
        root.setCenter(boardGrid);
        root.setLeft(createSidePanel(icons[0], false));   // red
        root.setTop(createSidePanel(icons[1], true));     // green
        root.setRight(createSidePanel(icons[2], false));  // blue
        root.setBottom(createSidePanel(icons[3], true));  // yellow

        Scene scene = new Scene(root, COLS * CELL_SIZE + MARGIN, COLS * CELL_SIZE + MARGIN);
        stage.setScene(scene);
        stage.setTitle("Jackaroo — Welcome, " + playerName);
        stage.setResizable(true);
        stage.show();
    }

    private Pane createSidePanel(Image icon, boolean horizontal) {
        Pane placeholder = new Pane();
        placeholder.setPrefSize(4 * CELL_SIZE, CELL_SIZE);
        placeholder.setStyle("-fx-border-color: gray; -fx-border-style: dashed;");

        ImageView iv = new ImageView(icon);
        iv.setFitWidth(CELL_SIZE);
        iv.setFitHeight(CELL_SIZE);
        iv.setPreserveRatio(true);

        if (horizontal) {
            HBox box = new HBox(10, iv, placeholder);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(10));
            return box;
        } else {
            VBox box = new VBox(10, iv, placeholder);
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
