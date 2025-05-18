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
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


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
        board = game.getBoard();

        Image[] icons = new Image[] {
            new Image(BoardView.class.getResourceAsStream("/images/player_red.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_green.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_blue.png")),
            new Image(BoardView.class.getResourceAsStream("/images/player_yellow.png"))
        };
        String[] names  = { "Mr. Red", "Mr. Green", "Mr. Blue", "Mr. Yellow" };
        String[] colors = { "red", "green", "blue", "goldenrod" };

        // randomize panel positions
        List<Integer> order = Arrays.asList(0,1,2,3);
        Collections.shuffle(order);
        Random rnd = new Random();
        int humanIdx = rnd.nextInt(4);
        names[humanIdx] = playerName;

        int leftIdx   = order.get(0);
        int topIdx    = order.get(1);
        int rightIdx  = order.get(2);
        int bottomIdx = order.get(3);

        // Assign colors to their respective positions
        // Create a map to link color ordinals to their index in the panel order
        Map<Integer, Integer> colorToPositionMap = new HashMap<>();
        colorToPositionMap.put(leftIdx, 2);   // LEFT = 2
        colorToPositionMap.put(topIdx, 3);    // TOP = 3 (was incorrectly 1)
        colorToPositionMap.put(rightIdx, 0);  // RIGHT = 0
        colorToPositionMap.put(bottomIdx, 1); // BOTTOM = 1 (was incorrectly 3)

        final double WINDOW_SIZE  = 800;
        final int    TRACK_CELLS  = board.getTrack().size();
        final double RING_RADIUS  = 300;
        final double TILE_RADIUS  = 12;
        final double SAFE_SPACING = TILE_RADIUS * 2.2;

        // --- Center pane with ring + safe + home ---
        Pane center = new Pane();
        center.setPrefSize(WINDOW_SIZE, WINDOW_SIZE);
        double cx = WINDOW_SIZE/2, cy = WINDOW_SIZE/2;

        // 1) Draw track tiles
        Point2D[] trackPts = new Point2D[TRACK_CELLS];
        for (int i = 0; i < TRACK_CELLS; i++) {
            double angle = 2 * Math.PI * i / TRACK_CELLS;
            double x = cx + RING_RADIUS * Math.cos(angle);
            double y = cy + RING_RADIUS * Math.sin(angle);
            trackPts[i] = new Point2D(x, y);

            Circle tile = new Circle(x, y, TILE_RADIUS, Color.BEIGE);
            tile.setStroke(Color.GRAY);
            center.getChildren().add(tile);

            cellPositionMap.put(board.getTrack().get(i), new int[]{ (int)x, (int)y });
        }

        // 2) Draw links between tiles
        for (int i = 0; i < TRACK_CELLS; i++) {
            Point2D p1 = trackPts[i];
            Point2D p2 = trackPts[(i+1)%TRACK_CELLS];
            Line link = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            link.setStroke(Color.DARKGRAY);
            center.getChildren().add(link);
        }

        // 3) Draw safe-zones in the right quadrant order
        for (SafeZone zone : board.getSafeZones()) {
            int colour = zone.getColour().ordinal();
            
            // Use the mapping to get the correct quadrant for this color
            Integer positionIndex = colorToPositionMap.get(colour);
            if (positionIndex == null) {
                // Fallback just in case
                positionIndex = colour;
            }
            
            int idx = positionIndex * (TRACK_CELLS / 4);
            Point2D base = trackPts[idx];

            // direction inward toward center
            Point2D dir = new Point2D(cx - base.getX(), cy - base.getY()).normalize();
            Point2D prev = base;
            String cssColor = colors[colour];

            // base circle
            Circle baseTile = new Circle(base.getX(), base.getY(), TILE_RADIUS);
            baseTile.setStyle(String.format("-fx-fill: %s99; -fx-stroke: black;", cssColor));
            center.getChildren().add(baseTile);

            // each safe spot
            List<Cell> safeCells = zone.getCells();
            for (int s = 0; s < safeCells.size(); s++) {
                Point2D pt = base.add(dir.multiply((s+1)*SAFE_SPACING));
                Circle safe = new Circle(pt.getX(), pt.getY(), TILE_RADIUS);
                safe.setStyle(String.format("-fx-fill: %s66; -fx-stroke: %s;", cssColor, cssColor));
                center.getChildren().add(safe);

                Line spoke = new Line(prev.getX(), prev.getY(), pt.getX(), pt.getY());
                spoke.setStyle(String.format("-fx-stroke: %s99;", cssColor));
                center.getChildren().add(spoke);

                cellPositionMap.put(safeCells.get(s), new int[]{ (int)pt.getX(), (int)pt.getY() });
                prev = pt;
            }
        }

        // 4) Draw home-zones in matching quadrants
        double homeSize = 100;
        double offset   = RING_RADIUS - (homeSize / 2);
        double[][] corners = {
            { cx + offset,            cy - offset - homeSize }, // RIGHT
            { cx - offset - homeSize, cy - offset - homeSize }, // TOP
            { cx - offset - homeSize, cy + offset },            // LEFT
            { cx + offset,            cy + offset }             // BOTTOM
        };
        for (model.Colour col : model.Colour.values()) {
            int colour = col.ordinal();
            
            // Use the mapping to get the correct quadrant for this color
            Integer positionIndex = colorToPositionMap.get(colour);
            if (positionIndex == null) {
                // Fallback just in case
                positionIndex = colour;
            }
            
            Rectangle rect = new Rectangle(
                corners[positionIndex][0],
                corners[positionIndex][1],
                homeSize, homeSize
            );
            rect.setStyle(String.format("-fx-fill: %s33; -fx-stroke: %s;", 
                                       colors[colour], colors[colour]));
            rect.setStrokeWidth(2);
            center.getChildren().add(rect);
        }

        // --- Assemble BorderPane with centered panels ---
        BorderPane root = new BorderPane();
        root.setCenter(center);

        // Left
        Pane leftPane = createSidePanel(icons[leftIdx], false, names[leftIdx], colors[leftIdx]);
        leftPane.setPrefHeight(WINDOW_SIZE);
        BorderPane.setAlignment(leftPane, Pos.CENTER);
        root.setLeft(leftPane);

        // Right
        Pane rightPane = createSidePanel(icons[rightIdx], false, names[rightIdx], colors[rightIdx]);
        rightPane.setPrefHeight(WINDOW_SIZE);
        BorderPane.setAlignment(rightPane, Pos.CENTER);
        root.setRight(rightPane);

        // Top
        Pane topPane = createSidePanel(icons[topIdx], true, names[topIdx], colors[topIdx]);
        topPane.setPrefWidth(WINDOW_SIZE);
        BorderPane.setAlignment(topPane, Pos.CENTER);
        root.setTop(topPane);

        // Bottom
        Pane bottomPane = createSidePanel(icons[bottomIdx], true, names[bottomIdx], colors[bottomIdx]);
        bottomPane.setPrefWidth(WINDOW_SIZE);
        BorderPane.setAlignment(bottomPane, Pos.CENTER);
        root.setBottom(bottomPane);

        // Background
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
