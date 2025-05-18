package view;

import engine.Game;
import engine.GameManager;
import engine.board.Board;
import engine.board.SafeZone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Colour;
import engine.board.Cell;


import java.io.IOException;
import java.util.*;

public class BoardView {
	private static Board board;
	private static final Map<Cell, int[]> cellPositionMap = new HashMap<>();
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final double CELL_SIZE = 60;

    public static BorderPane create(String playerName) throws IOException {
    	Game game = new Game(playerName);
    	board = game.getBoard(); // store board instance for reuse

        Image[] icons = new Image[] {
            new Image(BoardView.class.getResourceAsStream("/images/player_red.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_green.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_blue.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_yellow.png"))
        };

        String[] names  = { "Mr. Red", "Mr. Green", "Mr. Blue", "Mr. Yellow" };
        String[] colors = { "red",      "green",      "blue",      "goldenrod" };

        List<Integer> order = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(order);
        Random rnd = new Random();
        int playerCharIndex = rnd.nextInt(icons.length);
        names[playerCharIndex] = playerName;

        int leftIdx   = order.get(0);
        int topIdx    = order.get(1);
        int rightIdx  = order.get(2);
        int bottomIdx = order.get(3);

        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        boardGrid.setPadding(new Insets(10));
        boardGrid.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        ArrayList<Cell> track = board.getTrack();
        ArrayList<SafeZone> safeZones = board.getSafeZones();
        int cellIndex = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Pane cell = new Pane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);

                // TEMP: pick a cell from track (if within range)
                Cell currentCell = null;
                if (cellIndex < track.size()) {
                    currentCell = track.get(cellIndex++);
                    cellPositionMap.put(currentCell, new int[]{r, c});
                }

                // Add SafeZone cells to the map at corners
                for (SafeZone zone : safeZones) {
                    for (Cell safeCell : zone.getCells()) {
                        if (!cellPositionMap.containsKey(safeCell)) {
                        	String zoneColour = zone.getColour().toString();

                        	if (
                        	    (zoneColour.equals("RED")    && r >= 7 && r <= 8 && c >= 4 && c <= 5) ||
                        	    (zoneColour.equals("GREEN")  && r >= 4 && r <= 5 && c >= 1 && c <= 2) ||
                        	    (zoneColour.equals("BLUE")   && r >= 1 && r <= 2 && c >= 4 && c <= 5) ||
                        	    (zoneColour.equals("YELLOW") && r >= 4 && r <= 5 && c >= 7 && c <= 8)
                        	)
 {
                                cellPositionMap.put(safeCell, new int[]{r, c});
                                currentCell = safeCell;
                            }
                        }
                    }
                }

                boolean isSafe = currentCell != null && currentCell.getCellType() == engine.board.CellType.SAFE;
                cell.setStyle("-fx-border-color: black; -fx-background-color: " + (isSafe ? "green;" : "lightgray;"));

                boardGrid.add(cell, c, r);
            }
        }


        BorderPane root = new BorderPane();
        root.setCenter(boardGrid);
        root.setLeft(  createSidePanel(icons[leftIdx], false, names[leftIdx], colors[leftIdx]));
        root.setTop(   createSidePanel(icons[topIdx],  true,  names[topIdx],  colors[topIdx]));
        root.setRight( createSidePanel(icons[rightIdx], false, names[rightIdx], colors[rightIdx]));
        root.setBottom(createSidePanel(icons[bottomIdx], true, names[bottomIdx], colors[bottomIdx]));

        root.setStyle(
            "-fx-background-image: url('/images/tile.png');" +
            "-fx-background-repeat: repeat repeat;" +
            "-fx-background-position: center center;"
        );

        return root;
    }

    private static Pane createSidePanel(Image icon, boolean horizontal, String name, String color) {
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
}
