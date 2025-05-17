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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class Main extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final double CELL_SIZE = 60;

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
        root.setStyle(
        	    "-fx-background-image: url(\"/images/tile.png\");" +
        	    "-fx-background-repeat: repeat repeat;" +
        	    "-fx-background-position: center center;"
        	);

                // auto-size the window to exactly fit the top panel + grid + bottom panel
               Scene scene = new Scene(root);
                stage.setScene(scene);
               stage.sizeToScene();              // shrink-wrap to content
               stage.setTitle("Jackaroo — Welcome, " + playerName);
              stage.setResizable(false);
               stage.show();
    }
private Pane createSidePanel(Image icon, boolean horizontal) {
    // 1) use a Region or Pane with a fixed pref, min and max size
    double w = 4 * CELL_SIZE, h = CELL_SIZE;
    Region placeholder = new Region();  
    placeholder.setPrefSize(w, h);
    placeholder.setMinSize(w, h);
    placeholder.setMaxSize(w, h);
    placeholder.setStyle("-fx-border-color: gray; -fx-border-style: dashed;");

    ImageView iv = new ImageView(icon);
    iv.setFitWidth(CELL_SIZE*2);
    iv.setFitHeight(CELL_SIZE*2);
    iv.setPreserveRatio(true);

    if (horizontal) {
        HBox box = new HBox(10, iv, placeholder);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        // 2) turn off horizontal grow on the placeholder
        HBox.setHgrow(placeholder, Priority.NEVER);
        return box;
    } else {
        VBox box = new VBox(10, iv, placeholder);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        // if you ever see vertical stretching you can similarly:
        // VBox.setVgrow(placeholder, Priority.NEVER);
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
