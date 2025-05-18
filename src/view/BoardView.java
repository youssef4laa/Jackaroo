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

	private double tileRadius = 12;

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
	 * 
	 * Automatically lay out player panels according to the shuffled quadrant order.
	 * 
	 */

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
	 * 
	 * Map shuffled quadrant index (0-3) to a PanelPosition enum.
	 * 
	 */

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
	 * 
	 * Draw the game board and auto-layout player panels.
	 * 
	 */

	public void drawGameBoard(Board board, Map<Integer, PlayerPanelInfo> playerPanelInfoMap,

			FiredeckView firedeckView) {

		centerPane.getChildren().clear(); // Clear previous drawings if any

		cellPositionMap.clear();

// First, create and layout player panels based on shuffled quadrants

		Map<Integer, Pane> panelPanes = new HashMap<>();

		for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {

			PlayerPanelInfo info = e.getValue();

// Orientation: top/bottom panels horizontal, left/right vertical

// But we can derive orientation from the target position

			int quad = quadrantOrder.get(e.getKey());

			PanelPosition pos = mapQuadToPosition(quad);

			boolean horizontal = (pos == PanelPosition.TOP || pos == PanelPosition.BOTTOM);

			Pane panel = createPlayerPanelUI(info.icon, horizontal, info.name, info.cssColor);

			panelPanes.put(e.getKey(), panel);

		}

		layoutPlayerPanels(panelPanes);

// Draw board elements

		final int TRACK_CELLS = board.getTrack().size();

		Point2D[] trackPts = drawTrack(board.getTrack(), TRACK_CELLS);

		drawLinks(trackPts);

		drawSafeZones(board, trackPts, playerPanelInfoMap);

		drawHomeZones(board, playerPanelInfoMap);

// Add FiredeckView to the center

		if (firedeckView != null) {

			double firedeckWidth = firedeckView.getPrefWidth();

			double firedeckHeight = firedeckView.getPrefHeight();

			if (firedeckWidth <= 0 || firedeckHeight <= 0) {

				firedeckWidth = 150;

				firedeckHeight = 100;

				firedeckView.setPrefSize(firedeckWidth, firedeckHeight);

			}

			double firedeckX = calculatedCenterX - firedeckWidth / 2;

			double firedeckY = calculatedCenterY - firedeckHeight / 2;

			firedeckView.setLayoutX(firedeckX);

			firedeckView.setLayoutY(firedeckY);

			centerPane.getChildren().add(firedeckView);

			firedeckView.toFront();

		}

	}

	private Point2D[] drawTrack(List<Cell> trackCells, int totalTrackCells) {

		Point2D[] trackPts = new Point2D[totalTrackCells];

		for (int i = 0; i < totalTrackCells; i++) {

			double angle = 2 * Math.PI * i / totalTrackCells;

			double x = calculatedCenterX + ringRadius * Math.cos(angle);

			double y = calculatedCenterY + ringRadius * Math.sin(angle);

			trackPts[i] = new Point2D(x, y);

			Circle tile = new Circle(x, y, tileRadius, Color.BEIGE);

			tile.setStroke(Color.GRAY);

			centerPane.getChildren().add(tile);

			cellPositionMap.put(trackCells.get(i), new Point2D(x, y));

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

	public Pane createPlayerPanelUI(Image icon, boolean horizontalLayout, String name, String cssColor) {

		double placeholderWidth = 4 * CELL_SIZE_FOR_PANELS;

		double placeholderHeight = CELL_SIZE_FOR_PANELS;

		Region placeholder = new Region();

		placeholder.setPrefSize(placeholderWidth, placeholderHeight);

		placeholder.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		placeholder.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		placeholder.setStyle("-fx-border-color: gray; -fx-border-style: dashed; -fx-background-color: #FFFFFF1A;");

		ImageView iv = new ImageView(icon);

		double iconSize = CELL_SIZE_FOR_PANELS * 1.5;

		iv.setFitWidth(iconSize);

		iv.setFitHeight(iconSize);

		iv.setPreserveRatio(true);

		Label nameLabel = new Label(name);

		nameLabel.setWrapText(true);

		nameLabel.setMaxWidth(CELL_SIZE_FOR_PANELS * 2);

		nameLabel.setAlignment(Pos.CENTER);

		nameLabel.setStyle("-fx-font-size: 1.5em; -fx-text-fill: " + cssColor + "; -fx-font-weight: bold;");

		nameLabel.setMinHeight(Region.USE_PREF_SIZE);

		StackPane iconPane = new StackPane(iv);

		iconPane.setPrefSize(iconSize, iconSize);

		iconPane.setAlignment(Pos.CENTER);

		Pane layoutPane;

		if (horizontalLayout) {

			VBox nameAndIconBox = new VBox(5, iconPane, nameLabel);

			nameAndIconBox.setAlignment(Pos.CENTER);

			HBox box = new HBox(10, nameAndIconBox, placeholder);

			box.setAlignment(Pos.CENTER);

			box.setPadding(new Insets(5));

			HBox.setHgrow(placeholder, Priority.ALWAYS);

			HBox.setHgrow(nameAndIconBox, Priority.NEVER);

			box.setPrefHeight(iconSize + nameLabel.getFont().getSize() + 20);

			layoutPane = box;

		} else {

			VBox box = new VBox(5, iconPane, nameLabel, placeholder);

			box.setAlignment(Pos.CENTER);

			box.setPadding(new Insets(5));

			VBox.setVgrow(placeholder, Priority.ALWAYS);

			box.setPrefWidth(iconSize + 20);

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
	 * 
	 * Draw each player’s safe zone by:
	 * 
	 * 1) looking up the shuffled quadrant for that player
	 * 
	 * 2) mapping it to a PanelPosition (TOP/BOTTOM/LEFT/RIGHT)
	 * 
	 * 3) picking the corresponding track‐cell as the “base”
	 * 
	 * 4) stamping circles inward toward center
	 * 
	 */

	public void drawSafeZones(Board board, Point2D[] trackPts, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {

		final int total = trackPts.length;

		final double SAFESP = tileRadius * safeSpacingMultiplier;

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

// draw the “base” circle

			Circle baseTile = new Circle(base.getX(), base.getY(), tileRadius);

			baseTile.setStyle(String.format("-fx-fill: %s99; -fx-stroke: black;", info.cssColor));

			centerPane.getChildren().add(baseTile);

// 4: draw the inward “spokes” of safe‐cells

			Point2D dir = new Point2D(calculatedCenterX - base.getX(),

					calculatedCenterY - base.getY()).normalize();

			Point2D prev = base;

			Optional<SafeZone> optZ = board.getSafeZones().stream()

					.filter(z -> z.getColour() == info.playerColor)

					.findFirst();

			if (!optZ.isPresent())
				continue;

			for (int i = 0; i < optZ.get().getCells().size(); i++) {

				Point2D p = base.add(dir.multiply((i + 1) * SAFESP));

				Circle c = new Circle(p.getX(), p.getY(), tileRadius);

				c.setStyle(String.format("-fx-fill: %s66; -fx-stroke: %s;", info.cssColor, info.cssColor));

				centerPane.getChildren().add(c);

				Line spoke = new Line(prev.getX(), prev.getY(), p.getX(), p.getY());

				spoke.setStyle(String.format("-fx-stroke: %s99;", info.cssColor));

				centerPane.getChildren().add(spoke);

				spoke.toBack();

				cellPositionMap.put(optZ.get().getCells().get(i), p);

				prev = p;

			}

		}

	}

// drawHomeZones logic remains largely correct based on panel position index

	/**
	 * 
	 * Draw each player's 2×2 home square in the corner next to their panel
	 * 
	 */
	public void drawHomeZones(Board board, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {
		double homeSize = 100;
		double margin = 20; // gap between window edge and home-zone
		
		// Load the home zone background image
		Image homeZoneImage = new Image("/images/homezone.png");

		for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
			int panelIdx = e.getKey();
			PlayerPanelInfo info = e.getValue();

			// figure out which corner to place the home zone based on panel position
			PanelPosition pos = mapQuadToPosition(quadrantOrder.get(panelIdx));
			double x0 = 0, y0 = 0;

			switch (pos) {
			case TOP:
				// Top-left corner
				x0 = margin;
				y0 = margin;
				break;
			case BOTTOM:
				// Bottom-right corner
				x0 = windowSize - margin - homeSize;
				y0 = windowSize - margin - homeSize;
				break;
			case LEFT:
				// Bottom-left corner
				x0 = margin;
				y0 = windowSize - margin - homeSize;
				break;
			case RIGHT:
				// Top-right corner
				x0 = windowSize - margin - homeSize;
				y0 = margin;
				break;
			}

			// Create a rectangle with the image background
			Rectangle rect = new Rectangle(x0, y0, homeSize, homeSize);
			
			// Create background image pattern
			BackgroundImage bgImage = new BackgroundImage(
				homeZoneImage,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(homeSize, homeSize, false, false, false, false)
			);
			
			// Apply the background to the rectangle using CSS
			rect.setFill(Color.TRANSPARENT);
			rect.setStyle(String.format("-fx-stroke: %s;", info.cssColor));
			rect.setStrokeWidth(2);
			
			// Create a pane to hold the rectangle with the background image
			StackPane homeZonePane = new StackPane();
			homeZonePane.setBackground(new Background(bgImage));
			homeZonePane.getChildren().add(rect);
			homeZonePane.setLayoutX(x0);
			homeZonePane.setLayoutY(y0);
			homeZonePane.setPrefSize(homeSize, homeSize);
			
			centerPane.getChildren().add(homeZonePane);

			// populate the 2×2 starting pieces inside
			double cell = homeSize / 2;
			for (int r = 0; r < 2; r++) {
				for (int c = 0; c < 2; c++) {
					double cx = x0 + c * cell + cell / 2;
					double cy = y0 + r * cell + cell / 2;
					double pr = (cell / 2) * 0.8;

					Circle shadow = new Circle(cx + 2, cy + 2, pr);
					shadow.setStyle("-fx-fill: #00000055; -fx-stroke: transparent;");

					Circle piece = new Circle(cx, cy, pr);
					piece.setStyle(String.format(
							"-fx-fill: %s; -fx-stroke: black; -fx-stroke-width: 1;",
							info.cssColor));

					centerPane.getChildren().addAll(shadow, piece);
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