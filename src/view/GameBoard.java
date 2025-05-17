package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameBoard extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final double CELL_SIZE = 50;

    @Override
    public void start(Stage primaryStage) {
        // Root layout
        BorderPane root = new BorderPane();

        // Create and configure the board grid
        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setStyle("-fx-padding: 10");

        // Populate grid with plain cells
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                // simple styling: border + background
                cell.setStyle(
                    "-fx-border-color: black; " +
                    "-fx-background-color: lightgray;"
                );
                boardGrid.add(cell, col, row);
            }
        }

        // Put the grid in the center of the BorderPane
        root.setCenter(boardGrid);

        // Show the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Board Display");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
