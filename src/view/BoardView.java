package view;

import engine.board.Board;

import java.util.ArrayList;

import java.util.Optional;

import engine.board.Cell;

import engine.board.SafeZone;

import javafx.geometry.Insets;

import javafx.geometry.Point2D;

import javafx.geometry.Pos;

import javafx.scene.Node;
import model.card.Card;
import javafx.scene.control.Label;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.*;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;

import model.Colour; // Import Colour enum

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.Arrays;
import model.player.*;
import java.util.Collections;

public class BoardView {

	private BorderPane rootPane;

	private Pane centerPane; // The main area for the track, safe zones, etc.

// Made non-static, specific to this view instance

	private final Map<Cell, Point2D> cellPositionMap = new HashMap<>();

	private static final double DEFAULT_WINDOW_SIZE = 800; // Used for default calculations

	private static final double CELL_SIZE_FOR_PANELS = 60; // For player panels

// Configuration for drawing, can be adjusted

	private double windowSize = DEFAULT_WINDOW_SIZE;

	private double ringRadius = 300;

	private double tileRadius = 80;

	private double safeSpacingMultiplier = 2.2;

	private double verticalShift = -80; // To shift the center drawing up

	private double calculatedCenterX;

	private double calculatedCenterY;

	private final List<Integer> quadrantOrder;

	public BoardView() {

		this.rootPane = new BorderPane();

		this.centerPane = new Pane();

		this.rootPane.setCenter(centerPane);

		this.rootPane.setPadding(new Insets(0, 0, 20, 0)); // Default padding

// Calculate center points based on window size and shift

		this.calculatedCenterX = windowSize / 2;

		this.calculatedCenterY = windowSize / 2 + verticalShift;

		quadrantOrder = new ArrayList<>(Arrays.asList(0, 1, 2, 3));

		Collections.shuffle(quadrantOrder);

// Default background, can be overridden by controller

		this.rootPane.setStyle("-fx-background-image: url('/images/tile.png');"

				+ "-fx-background-repeat: repeat repeat;" + "-fx-background-position: center center;");

	}

	public BorderPane getRootPane() {

		return rootPane;

	}

	public Pane getCenterPane() {

		return centerPane;

	}

	public Map<Cell, Point2D> getCellPositionMap() {

		return cellPositionMap;

	}

	/**
	 * * Automatically lay out player panels according to the shuffled quadrant order.
	 * */

	private void layoutPlayerPanels(Map<Integer, Pane> panelPanes) {

		for (Map.Entry<Integer, Pane> entry : panelPanes.entrySet()) {

			int panelIdx = entry.getKey();

			Pane panel = entry.getValue();

// Determine which quadrant this panel should occupy

			int quad = quadrantOrder.get(panelIdx);

			PanelPosition pos = mapQuadToPosition(quad);

			setPlayerPanel(panel, pos);

		}

	}

	/**
	 * * Map shuffled quadrant index (0-3) to a PanelPosition enum.
	 * */

	private PanelPosition mapQuadToPosition(int quad) {

		switch (quad) {

		case 0:

			return PanelPosition.BOTTOM;

		case 1:

			return PanelPosition.LEFT;

		case 2:

			return PanelPosition.TOP;

		case 3:

			return PanelPosition.RIGHT;

		default:

			throw new IllegalArgumentException("Invalid quadrant: " + quad);

		}

	}

	/**
	 * * Draw the game board and auto-layout player panels.
	 * */

public void drawGameBoard(Board board,
                          Map<Integer, PlayerPanelInfo> playerPanelInfoMap,
                          Map<Integer, Player> playerMap,
                          FiredeckView firedeckView,
                          DeckView deckView) {
    // 1) clear out any previous drawings
    centerPane.getChildren().clear();
    cellPositionMap.clear();

    // 2) build and position each player's panel using the new createPlayerPanelUI(...)
    Map<Integer, Pane> panelPanes = new HashMap<>();
    for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
        int idx = e.getKey();
        PlayerPanelInfo info = e.getValue();
        Player player = playerMap.get(idx);

        // figure out this panel's quadrant and orientation
        int quad = quadrantOrder.get(idx);
        PanelPosition pos = mapQuadToPosition(quad);
        boolean horizontal = (pos == PanelPosition.TOP || pos == PanelPosition.BOTTOM);

        // now call your updated factory
        Pane panel = createPlayerPanelUI(
            info.icon,
            horizontal,
            info.name,
            info.cssColor,
            player
        );
        panelPanes.put(idx, panel);
    }
    layoutPlayerPanels(panelPanes);

    // 3) draw the main circular track with automatic tile‐sizing
    List<Cell> trackCells = board.getTrack();
    Point2D[] trackPts = drawTrack(trackCells, trackCells.size());

    // 4) connect them with lines
    drawLinks(trackPts);

    // 5) draw each player's safe corridor and home corner
    drawSafeZones(board, trackPts, playerPanelInfoMap);
    drawHomeZones(board, playerPanelInfoMap);

    // Calculate card dimensions (consistent with createPlayerPanelUI)
    double cardH_for_decks = CELL_SIZE_FOR_PANELS * 1.5; // Increased height for better visibility
    double cardW_for_decks = cardH_for_decks * (2.5 / 3.5);

    // Calculate positions to center both components with reduced spacing
    double spacing = 30; // Reduced spacing between components
    double totalWidth = (cardW_for_decks * 2) + spacing; // Width of both cards plus spacing
    double startX = calculatedCenterX - (totalWidth / 2); // Start position to center both components

    // 6) position the FiredeckView if present
    if (firedeckView != null) {
        firedeckView.setPrefSize(cardW_for_decks, cardH_for_decks);
        firedeckView.relocate(
            startX,
            calculatedCenterY - cardH_for_decks / 2
        );
        centerPane.getChildren().add(firedeckView);
        firedeckView.toFront();
    }
    
    // 7) position the DeckView next to the firepit if present
    if (deckView != null) {
        // Position the deck to the right of the firepit with reduced spacing
        deckView.setPrefSize(cardW_for_decks, cardH_for_decks);
        deckView.relocate(
            startX + cardW_for_decks + spacing,
            calculatedCenterY - cardH_for_decks / 2
        );
        centerPane.getChildren().add(deckView);
        deckView.toFront();
    }
}

private Point2D[] drawTrack(List<Cell> trackCells, int totalTrackCells) {
    // gap you want between circles
    double spacing = 10;
    // significantly larger desired tile radius
    double desiredTileRadius = 20;  // Increased from original value

    // fixed board radius - use a larger radius to accommodate larger tiles
    double R = ringRadius;
    // straight-line distance (chord) between two adjacent centers on your ring
    double chord = 2 * R * Math.sin(Math.PI / totalTrackCells);
    // the maximum radius that still leaves 'spacing' px between tiles:
    double maxFittingRadius = (chord - spacing) / 2;

    // pick the smaller of your desired size or the fitting size,
    // but never let them shrink below a visible minimum (e.g. 5px)
    double actualTileRadius = Math.max(10, Math.min(desiredTileRadius, maxFittingRadius));
    
    // Save this radius for consistent tile sizes across track and safe zones
    tileRadius = actualTileRadius;

    Point2D[] trackPts = new Point2D[totalTrackCells];
    for (int i = 0; i < totalTrackCells; i++) {
        double angle = 2 * Math.PI * i / totalTrackCells; 
        double x = calculatedCenterX + R * Math.cos(angle);
        double y = calculatedCenterY + R * Math.sin(angle);

        Circle tile = new Circle(x, y, actualTileRadius, Color.BEIGE);
        tile.setStroke(Color.GRAY);
        centerPane.getChildren().add(tile);

        trackPts[i] = new Point2D(x, y);
        cellPositionMap.put(trackCells.get(i), trackPts[i]);
    }
    return trackPts;
}

private void drawLinks(Point2D[] trackPts) {

		for (int i = 0; i < trackPts.length; i++) {

			Point2D p1 = trackPts[i];

			Point2D p2 = trackPts[(i + 1) % trackPts.length];

			Line link = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());

			link.setStroke(Color.DARKGRAY);

			centerPane.getChildren().add(link);

			link.toBack(); // Send links behind tiles

		}

	}

// Modified drawSafeZones ... [rest unchanged]

// drawSafeZones and drawHomeZones methods remain as before, using quadrantOrder

// for placement.

	// Add imports if not already present at the top of your file:
// import javafx.scene.layout.HBox; // Already present
// import javafx.scene.image.Image; // Already present
// import javafx.scene.image.ImageView; // Already present
// import javafx.scene.shape.Rectangle; // Already present
// import javafx.scene.Node; // Already present
// import javafx.scene.paint.Color; // Already present
// import javafx.geometry.Pos; // Already present
public Pane createPlayerPanelUI(Image icon,
                                    boolean horizontalLayout,
                                    String name,
                                    String cssColor,
                                    Player player) {
        // 1) Icon
        ImageView iv = new ImageView(icon);
        double iconSize = CELL_SIZE_FOR_PANELS * 1.5;
        iv.setFitWidth(iconSize);
        iv.setFitHeight(iconSize);
        iv.setPreserveRatio(true);
        StackPane iconPane = new StackPane(iv);
        iconPane.setPrefSize(iconSize, iconSize);
        iconPane.setAlignment(Pos.CENTER);

        // 2) Name
        Label nameLabel = new Label(name);
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(CELL_SIZE_FOR_PANELS * 2);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle(
            "-fx-font-size: 1.5em;" +
            "-fx-text-fill: " + cssColor + ";" +
            "-fx-font-weight: bold;"
        );
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        // 3) Card area: one CardView per card in hand
        HBox cardDisplayArea = new HBox(5);
        cardDisplayArea.setAlignment(Pos.CENTER);
        double cardH = CELL_SIZE_FOR_PANELS * 0.90;
        double cardW = cardH * (2.5/3.5);
        List<Card> hand = player.getHand();
        // size the HBox so it can fit all cards + spacing
        cardDisplayArea.setPrefSize(
            cardW * hand.size() + 5 * (hand.size() - 1),
            cardH
        );

        for (Card card : hand) {
            CardView cv = new CardView(card);
            cv.setFaceUp(true);
            // **instead of setFitWidth/Height**, use pref size
            cv.setPrefSize(cardW, cardH);
            // ensure it cannot grow beyond that
            cv.setMaxSize(cardW, cardH);
            // click handler
            cv.setOnMouseClicked(e -> {
                try {
                    player.selectCard(card);
                    // TODO: add visual selection highlight
                } catch (Exception ex) {
                    // handle invalid selection
                }
            });
            cardDisplayArea.getChildren().add(cv);
        }

        // 4) Combine icon + name
        VBox nameAndIconBox = new VBox(5, iconPane, nameLabel);
        nameAndIconBox.setAlignment(Pos.CENTER);

        // 5) Final layout
        Pane layoutPane;
        if (horizontalLayout) {
            HBox box = new HBox(10, nameAndIconBox, cardDisplayArea);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(5));
            HBox.setHgrow(cardDisplayArea, Priority.NEVER);
            HBox.setHgrow(nameAndIconBox, Priority.NEVER);
            layoutPane = box;
        } else {
            VBox box = new VBox(5, iconPane, nameLabel, cardDisplayArea);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(5));
            VBox.setVgrow(cardDisplayArea, Priority.NEVER);
            layoutPane = box;
        }

        return layoutPane;
    }
public enum PanelPosition {

		TOP, BOTTOM, LEFT, RIGHT

	}

	public static class PlayerPanelInfo {

		public final Image icon;

		public final String name;

		public final String cssColor;

		public final PanelPosition position;

		public final boolean isHorizontal;

		public final Colour playerColor;

		public PlayerPanelInfo(Image icon,

				String name,

				String cssColor,

				PanelPosition position,

				boolean isHorizontal,

				Colour playerColor) {

			this.icon = icon;

			this.name = name;

			this.cssColor = cssColor;

			this.position = position;

			this.isHorizontal = isHorizontal;

			this.playerColor = playerColor;

		}

	}

	/**
	 * * Draw each player’s safe zone by:
	 * * 1) looking up the shuffled quadrant for that player
	 * * 2) mapping it to a PanelPosition (TOP/BOTTOM/LEFT/RIGHT)
	 * * 3) picking the corresponding track‐cell as the “base”
	 * * 4) stamping circles inward toward center
	 * */

/**
 * Draw each player's safe zone with exactly 4 tiles per safe zone
 * using consistent tile sizes as the main track
 */
public void drawSafeZones(Board board, Point2D[] trackPts, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {
    final int total = trackPts.length;
    // Use a consistent spacing between safe zone tiles
    final double SAFESP = tileRadius * 2.2;

    for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
        int panelIdx = e.getKey();
        PlayerPanelInfo info = e.getValue();

        // 1/2: get shuffled quadrant and map to a compass position
        PanelPosition pos = mapQuadToPosition(quadrantOrder.get(panelIdx));

        // 3: pick the track‐cell index that corresponds to that compass point
        int baseIdx;
        switch (pos) {
        case RIGHT:
            baseIdx = 0;
            break;
        case BOTTOM:
            baseIdx = total / 4;
            break;
        case LEFT:
            baseIdx = total / 2;
            break;
        case TOP:
            baseIdx = 3 * total / 4;
            break;
        default:
            throw new IllegalStateException();
        }

        Point2D base = trackPts[baseIdx];

        // Use colored circle for the base track tile to indicate connection point
        // No more pure black tile in the safe zone
        Circle baseTile = new Circle(base.getX(), base.getY(), tileRadius);
        baseTile.setStyle(String.format("-fx-fill: %s99; -fx-stroke: black;", info.cssColor));
        centerPane.getChildren().add(baseTile);

        // Calculate direction vector pointing inward toward center
        Point2D dir = new Point2D(calculatedCenterX - base.getX(),
                calculatedCenterY - base.getY()).normalize();

        Point2D prev = base;

        Optional<SafeZone> optZ = board.getSafeZones().stream()
                .filter(z -> z.getColour() == info.playerColor)
                .findFirst();

        if (!optZ.isPresent())
            continue;

        // Always draw exactly 4 safe zone tiles (limiting to 4 regardless of actual size)
        // This will handle any size of safe zone in the model, but only display 4 visually
        int safeTileCount = Math.min(4, optZ.get().getCells().size());
        
        for (int i = 0; i < safeTileCount; i++) {
            Point2D p = base.add(dir.multiply((i + 1) * SAFESP));

            // Create the safe zone tile with the same radius as track tiles
            Circle c = new Circle(p.getX(), p.getY(), tileRadius);
            c.setStyle(String.format("-fx-fill: %s66; -fx-stroke: %s;", info.cssColor, info.cssColor));
            centerPane.getChildren().add(c);

            // Create the connecting line
            Line spoke = new Line(prev.getX(), prev.getY(), p.getX(), p.getY());
            spoke.setStyle(String.format("-fx-stroke: %s99;", info.cssColor));
            centerPane.getChildren().add(spoke);
            spoke.toBack();

            // Only map cells that actually exist in the model
            if (i < optZ.get().getCells().size()) {
                cellPositionMap.put(optZ.get().getCells().get(i), p);
            }

            prev = p;
        }
    }
}

	/**
	 * * Draw each player's 2×2 home square in the corner next to their panel.
	 * Tiles (cells) are made larger and marbles (pieces) smaller.
	 * */
	public void drawHomeZones(Board board, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {
    double homeSize = 80;
    double margin = 20;
    Image homeZoneImage = new Image("/images/homezone.png");
    double marbleToCellRadiusRatio = 0.4;

    for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
        int panelIdx = e.getKey();
        PlayerPanelInfo info = e.getValue();

        PanelPosition pos = mapQuadToPosition(quadrantOrder.get(panelIdx));
        double x0 = 0, y0 = 0;
        switch (pos) {
            case TOP:
                x0 = margin;
                y0 = margin;
                break;
            case BOTTOM:
                x0 = windowSize - margin - homeSize;
                y0 = windowSize - margin - homeSize;
                break;
            case LEFT:
                x0 = margin;
                y0 = windowSize - margin - homeSize;
                break;
            case RIGHT:
                x0 = windowSize - margin - homeSize;
                y0 = margin;
                break;
        }

        // background pane
        Rectangle rect = new Rectangle(x0, y0, homeSize, homeSize);
        BackgroundImage bgImage = new BackgroundImage(
            homeZoneImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(homeSize, homeSize, false, false, false, false)
        );
        rect.setFill(Color.TRANSPARENT);
        rect.setStyle(String.format("-fx-stroke: %s;", info.cssColor));
        rect.setStrokeWidth(2);
        StackPane homeZonePane = new StackPane();
        homeZonePane.setBackground(new Background(bgImage));
        homeZonePane.getChildren().add(rect);
        homeZonePane.setLayoutX(x0);
        homeZonePane.setLayoutY(y0);
        homeZonePane.setPrefSize(homeSize, homeSize);
        centerPane.getChildren().add(homeZonePane);

        // compute cell size & marble size
        double cellSize = homeSize / 2;
        double pieceRadius = (cellSize / 2) * marbleToCellRadiusRatio;
        String marbleImageName;
        switch (info.playerColor) {
            case RED:    marbleImageName = "/images/redMarble.png";    break;
            case BLUE:   marbleImageName = "/images/blueMarble.png";   break;
            case YELLOW: marbleImageName = "/images/yellowMarble.png"; break;
            case GREEN:  marbleImageName = "/images/greenMarble.png";  break;
            default:     marbleImageName = "/images/greyMarble.png";   break;
        }
        Image marbleImg = new Image(marbleImageName);

        // place 2×2
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                double cellCenterX = x0 + c * cellSize + cellSize / 2;
                double cellCenterY = y0 + r * cellSize + cellSize / 2;

                // optional: keep a subtle shadow behind
                Circle shadow = new Circle(
                    cellCenterX + pieceRadius * 0.1,
                    cellCenterY + pieceRadius * 0.1,
                    pieceRadius
                );
                shadow.setStyle("-fx-fill: #00000055; -fx-stroke: transparent;");
                centerPane.getChildren().add(shadow);

                // image-based marble
                ImageView marbleView = new ImageView(marbleImg);
                double diameter = pieceRadius * 2;
                marbleView.setFitWidth(diameter);
                marbleView.setFitHeight(diameter);
                marbleView.setPreserveRatio(true);
                // center the image
                marbleView.setLayoutX(cellCenterX - pieceRadius);
                marbleView.setLayoutY(cellCenterY - pieceRadius);
                centerPane.getChildren().add(marbleView);
            }
        }
    }
}

	public void setPlayerPanel(Pane panel, PanelPosition position) {

		switch (position) {

		case BOTTOM:

			BorderPane.setAlignment(panel, Pos.CENTER);

			rootPane.setBottom(panel);

			break;

		case LEFT:

			BorderPane.setAlignment(panel, Pos.CENTER);

			rootPane.setLeft(panel);

			break;

		case RIGHT:

			BorderPane.setAlignment(panel, Pos.CENTER);

			rootPane.setRight(panel);

			break;

		case TOP:

			BorderPane.setAlignment(panel, Pos.CENTER);

			rootPane.setTop(panel);

			break;

		}

	}

}