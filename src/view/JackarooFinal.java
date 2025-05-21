package view;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import engine.Game;
import engine.GameManager;
import engine.board.Board;
import engine.board.Cell;
import engine.board.SafeZone;
import exception.GameException;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.card.Marble;
import model.card.standard.Standard;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.player.CPU;
import model.player.Player;
import view.WinnerViewController;

/**
 * JackarooFinal - A consolidated UI class that combines functionality from
 * CardView, DeckView, CardController, DeckController, BoardView, and
 * BoardController into a single class.
 * This simplifies the architecture by eliminating the separation between views
 * and controllers.
 */
public class JackarooFinal extends BorderPane {

    // Constants
    private static final String CARD_DIR = "file:/Users/youssef/Code/jackaroo/resources/images/cards/";
    private static final String BG_MUSIC_PATH = "/audio/bgmusic.mp3";
    private static final String DECK_IMAGE_PATH = "/images/deck_back.png";
    private static final double CARD_WIDTH = 71;
    private static final double CARD_HEIGHT = 95;
    private static final double DECK_WIDTH = 60;
    private static final double DECK_HEIGHT = 90;
    private static final int DRAW_SIZE = 4;
    private static final double DEFAULT_WINDOW_SIZE = 800;
    private static final double CELL_SIZE_FOR_PANELS = 60;

    // Game state references
    private final Game game;
    private final Player humanPlayer;

    // UI components for player hand
    private final HBox cardsContainer;
    private final StackPane deckPane;
    private final Label deckCountLabel;
    private final ImageView deckBackView;
    private final DropShadow baseShadow;
    private final DropShadow hoverShadow;
    private final DropShadow selectionGlow;

    // Board UI components
    private final Pane centerPane;
    private final Map<Cell, Point2D> cellPositionMap = new HashMap<>();
    private final List<Integer> quadrantOrder;
    private double windowSize = DEFAULT_WINDOW_SIZE;
    private double ringRadius = 300;
    private double tileRadius = 20;
    private double calculatedCenterX;
    private double calculatedCenterY;
    private double verticalShift = -80;

    // Player panel components
    private final String[] defaultNames = { "Mr.Red", "Mr.Green", "Mr.Blue", "Mr.Yellow" };
    private final String[] defaultCssColors = { "red", "green", "blue", "goldenrod" };
    private final Colour[] playerColours = { Colour.RED, Colour.GREEN, Colour.BLUE, Colour.YELLOW };
    private final String[] iconPaths = {
            "/images/player_red.png",
            "/images/player_green.png",
            "/images/player_blue.png",
            "/images/player_yellow.png"
    };
    private Image[] playerIcons;

    // Firedeck components
    private FiredeckView firedeckView;
    
    // Background music
    private MediaPlayer mediaPlayer;
    
    // Turn indicator components
    private Label currentPlayerLabel;
    private Label nextPlayerLabel;
    private HBox turnIndicatorBox;

    /**
     * Creates a new JackarooFinal UI component that includes the game board, player
     * panels, and deck.
     * 
     * @param humanPlayerName The name of the human player
     * @throws IOException If there's an error loading resources
     */
    public JackarooFinal(String humanPlayerName) throws IOException {
        // Initialize game
        this.game = new Game(humanPlayerName);
        this.humanPlayer = game.getPlayers().get(0); // Human player is always first

        // Setup shadow effects
    baseShadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.4));
    hoverShadow = new DropShadow(8, Color.WHITE);
    selectionGlow = new DropShadow(20, Color.rgb(0, 255, 0, 0.8));

        // Initialize center pane for board
        centerPane = new Pane();
        setCenter(centerPane);
        setPadding(new Insets(0, 0, 20, 0));

        // Calculate center points for board
        calculatedCenterX = windowSize / 2;
        calculatedCenterY = windowSize / 2 + verticalShift;

        // Initialize quadrant order and shuffle it
        quadrantOrder = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(quadrantOrder);

        // Set background
        setStyle("-fx-background-image: url('/images/tile.png');"
                + "-fx-background-repeat: repeat repeat;"
                + "-fx-background-position: center center;");

        // Load player icons
        loadPlayerIcons();

        // Create player panels
        Map<Integer, PlayerPanelInfo> playerPanelConfig = createPlayerPanelConfigurations(humanPlayerName);

        // Initialize firedeck view
        firedeckView = new FiredeckView();
        
        // Initialize turn indicator components
        currentPlayerLabel = new Label();
        nextPlayerLabel = new Label();
        currentPlayerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nextPlayerLabel.setStyle("-fx-font-size: 14px;");
        
        turnIndicatorBox = new HBox(10);
        turnIndicatorBox.setAlignment(Pos.CENTER);
        turnIndicatorBox.setPadding(new Insets(10));
        turnIndicatorBox.getChildren().addAll(
            new Label("Current Turn: "), currentPlayerLabel,
            new Label("Next Turn: "), nextPlayerLabel
        );
        
        // Initial update of turn indicators
        updateTurnIndicators();

        // Create deck view components
        deckPane = new StackPane();
        deckPane.setAlignment(Pos.CENTER);
        deckPane.setPadding(new Insets(5));
        deckPane.setEffect(baseShadow);

        // Load deck image
        Image deckImage = new Image(getClass().getResourceAsStream(DECK_IMAGE_PATH));
        deckBackView = new ImageView(deckImage);
        deckBackView.setFitWidth(DECK_WIDTH);
        deckBackView.setFitHeight(DECK_HEIGHT);
        deckBackView.setPickOnBounds(true);

        // Setup count label
        deckCountLabel = new Label();
        deckCountLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        deckCountLabel.setTranslateY(DECK_HEIGHT / 2 - 12);

        // Add components to deck pane
        deckPane.getChildren().addAll(deckBackView, deckCountLabel);

        // Setup deck hover effects
        deckPane.setOnMouseEntered(e -> deckPane.setEffect(hoverShadow));
        deckPane.setOnMouseExited(e -> deckPane.setEffect(baseShadow));

        // Setup deck click handler
        deckPane.setOnMouseClicked(this::handleDeckClick);

        // Create cards container for human player's hand
        cardsContainer = new HBox(10);
        cardsContainer.setAlignment(Pos.CENTER);

        // Build a map of index -> Player model
        Map<Integer, Player> playerMap = new HashMap<>();
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            playerMap.put(i, players.get(i));
        }

        // Draw the game board with all components
        drawGameBoard(game.getBoard(), playerPanelConfig, playerMap);
        
        // Initialize background music
        initializeBackgroundMusic();

        // Initialize the deck count
        updateDeckCount();
        
        // Turn indicator will be placed in the left panel by layoutPlayerPanels
    }

    /**
     * Initializes and plays background music.
     */
    private void initializeBackgroundMusic() {
        try {
            URL musicUrl = getClass().getResource(BG_MUSIC_PATH);
            if (musicUrl == null) {
                throw new IOException("Audio file not found at: " + BG_MUSIC_PATH);
            }
            Media sound = new Media(musicUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.3);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
            // Fallback to system beep if audio fails
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Updates the displayed count of cards in the deck.
     */
    public void updateDeckCount() {
        Runnable updater = () -> deckCountLabel.setText(String.valueOf(Deck.getPoolSize()));
        if (Platform.isFxApplicationThread())
            updater.run();
        else
            Platform.runLater(updater);
    }

    /**
     * Handles clicks on the deck - draws cards and adds them to the player's hand.
     */
    private void handleDeckClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        if (Deck.getPoolSize() < DRAW_SIZE) {
            showUserError("Not enough cards left in the deck. Please refill first.");
            return;
        }

        try {
            List<Card> drawnCards = Deck.drawCards();
            addCardsToHand(drawnCards);
            updateDeckCount();
            
            // Update turn indicators after drawing cards
            updateTurnIndicators();

            // Check for winner after drawing cards
            Colour winner = game.checkWin();
            if (winner != null) {
                showWinnerDialog(winner);
            }
        } catch (Exception ex) {
            showUserError("Unexpected draw failure: " + ex.getMessage());
        }
    }

    /**
     * Adds cards to the player's hand and creates UI components for them.
     */
    private void addCardsToHand(List<Card> cards) {
        for (Card card : cards) {
            CardPane cardPane = new CardPane(card, humanPlayer);
            cardsContainer.getChildren().add(cardPane);
        }

        // Update the firedeck view if needed
        updateFiredeckView();
    }

    /**
     * Updates the firedeck view with the top card from the fire pit.
     */
    private void updateFiredeckView() {
        List<Card> firePit = game.getFirePit();
        if (firePit.isEmpty()) {
            firedeckView.showEmptyState();
        } else {
            Card topCard = firePit.get(firePit.size() - 1);
            firedeckView.updateView(topCard);
        }
    }
    
    /**
     * Updates the turn indicator labels with current and next player colors.
     */
    private void updateTurnIndicators() {
        Colour currentPlayerColour = game.getActivePlayerColour();
        Colour nextPlayerColour = game.getNextPlayerColour();
        
        // Update the labels with player colors
        currentPlayerLabel.setText(currentPlayerColour.toString());
        nextPlayerLabel.setText(nextPlayerColour.toString());
        
        // Set the text color to match the player color
        String currentColorStyle = getColorStyle(currentPlayerColour);
        String nextColorStyle = getColorStyle(nextPlayerColour);
        
        currentPlayerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + currentColorStyle + ";");
        nextPlayerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + nextColorStyle + ";");
    }
    
    /**
     * Converts a Colour enum to a CSS color string.
     */
    private String getColorStyle(Colour colour) {
        switch (colour) {
            case RED: return "red";
            case GREEN: return "green";
            case BLUE: return "blue";
            case YELLOW: return "goldenrod";
            default: return "black";
        }
    }

    /**
     * Refills the deck from a list of cards (e.g., discard pile).
     */
    public void refillDeckFrom(List<Card> cards) {
        Deck.refillPool(new ArrayList<>(cards));
        updateDeckCount();
    }

    /**
     * Displays an error message to the user.
     */
    private void showUserError(String message) {
        System.err.println("⚠ " + message);
        // Could be replaced with a proper dialog in a real implementation
    }

    /**
     * Loads player icons from resources.
     */
    private void loadPlayerIcons() {
        playerIcons = new Image[iconPaths.length];
        for (int i = 0; i < iconPaths.length; i++) {
            try {
                playerIcons[i] = new Image(getClass().getResourceAsStream(iconPaths[i]));
            } catch (Exception e) {
                System.err.println("Failed to load icon: " + iconPaths[i]);
            }
        }
    }

    /**
     * Creates player panel configurations for all players.
     */
    private Map<Integer, PlayerPanelInfo> createPlayerPanelConfigurations(String humanPlayerName) {
        Map<Integer, PlayerPanelInfo> panelConfigurations = new HashMap<>();

        // Fixed visual positions and orientations
        PanelPosition[] visualPositions = {
                PanelPosition.BOTTOM,
                PanelPosition.LEFT,
                PanelPosition.TOP,
                PanelPosition.RIGHT
        };
        boolean[] isHorizontalLayout = { true, false, true, false };

        // Shuffle panel positions
        List<Integer> panelPositionIndices = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(panelPositionIndices, new Random());

        for (int logicalIdx = 0; logicalIdx < playerColours.length; logicalIdx++) {
            int visualIdx = panelPositionIndices.get(logicalIdx);
            PanelPosition pos = visualPositions[visualIdx];
            boolean horiz = isHorizontalLayout[visualIdx];

            String name = (logicalIdx == 0) ? humanPlayerName : defaultNames[logicalIdx];

            panelConfigurations.put(
                    visualIdx,
                    new PlayerPanelInfo(
                            playerIcons[logicalIdx],
                            name,
                            defaultCssColors[logicalIdx],
                            pos,
                            horiz,
                            playerColours[logicalIdx]));
        }

        return panelConfigurations;
    }

    /**
     * Shows a dialog when a player wins the game.
     */
    private void showWinnerDialog(Colour winner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WinnerView.fxml"));
            Parent root = loader.load();
            WinnerViewController ctrl = loader.getController();
            ctrl.setWinner(winner);

            Stage dialog = new Stage();
            dialog.initOwner(getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Game Over!");
            dialog.showAndWait();
        } catch (IOException e) {
            // If FXML fails to load, at least show a popup
            ExceptionPopup.showException(e);
        }
    }

    /**
     * Draws the complete game board with all components.
     */
    private void drawGameBoard(Board board, Map<Integer, PlayerPanelInfo> playerPanelInfoMap,
            Map<Integer, Player> playerMap) {
        // 1) Clear out any previous drawings
        centerPane.getChildren().clear();
        cellPositionMap.clear();

        // 2) Build and position each player's panel
        Map<Integer, Pane> panelPanes = new HashMap<>();
        for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
            int idx = e.getKey();
            PlayerPanelInfo info = e.getValue();
            Player player = playerMap.get(idx);

            // Figure out this panel's quadrant and orientation
            int quad = quadrantOrder.get(idx);
            PanelPosition pos = mapQuadToPosition(quad);
            boolean horizontal = (pos == PanelPosition.TOP || pos == PanelPosition.BOTTOM);

            // Create the player panel
            Pane panel = createPlayerPanelUI(
                    info.icon,
                    horizontal,
                    info.name,
                    info.cssColor,
                    player);
            panelPanes.put(idx, panel);
        }
        layoutPlayerPanels(panelPanes);

        // 3) Draw the main circular track with automatic tile-sizing
        List<Cell> trackCells = board.getTrack();
        Point2D[] trackPts = drawTrack(trackCells, trackCells.size());

        // 4) Connect them with lines
        drawLinks(trackPts);

        // 5) Draw each player's safe corridor and home corner
        drawSafeZones(board, trackPts, playerPanelInfoMap);
        drawHomeZones(board, playerPanelInfoMap);

        // Calculate card dimensions
        double cardH_for_decks = CELL_SIZE_FOR_PANELS * 1.5;
        double cardW_for_decks = cardH_for_decks * (2.5 / 3.5);

        // Calculate positions to center both components with reduced spacing
        double spacing = 30;
        double totalWidth = (cardW_for_decks * 2) + spacing;
        double startX = calculatedCenterX - (totalWidth / 2);

        // 6) Position the FiredeckView
        firedeckView.setPrefSize(cardW_for_decks, cardH_for_decks);
        firedeckView.relocate(
                startX,
                calculatedCenterY - cardH_for_decks / 2);
        centerPane.getChildren().add(firedeckView);
        firedeckView.toFront();

        // 7) Position the deck pane next to the firepit
        deckPane.setPrefSize(cardW_for_decks, cardH_for_decks);
        deckPane.relocate(
                startX + cardW_for_decks + spacing,
                calculatedCenterY - cardH_for_decks / 2);
        centerPane.getChildren().add(deckPane);
        deckPane.toFront();
    }

    /**
     * Automatically lay out player panels according to the shuffled quadrant order.
     * Also places the turn indicator on the left side.
     */
    private void layoutPlayerPanels(Map<Integer, Pane> panelPanes) {
        // Create a container for the left side that will hold both the turn indicator and player panel
        VBox leftSideContainer = new VBox(20);
        leftSideContainer.setAlignment(Pos.CENTER);
        
        // Add the turn indicator to the top of the left container
        VBox turnIndicatorContainer = new VBox(10);
        turnIndicatorContainer.setAlignment(Pos.CENTER);
        turnIndicatorContainer.setPadding(new Insets(10));
        turnIndicatorContainer.getChildren().add(turnIndicatorBox);
        leftSideContainer.getChildren().add(turnIndicatorContainer);
        
        // Track if we've found the left panel
        Pane leftPanel = null;
        
        // First pass to identify the left panel and place other panels
        for (Map.Entry<Integer, Pane> entry : panelPanes.entrySet()) {
            int panelIdx = entry.getKey();
            Pane panel = entry.getValue();

            // Determine which quadrant this panel should occupy
            int quad = quadrantOrder.get(panelIdx);
            PanelPosition pos = mapQuadToPosition(quad);
            
            if (pos == PanelPosition.LEFT) {
                // Save the left panel to add it to our container
                leftPanel = panel;
            } else {
                // Place other panels directly
                setPlayerPanel(panel, pos);
            }
        }
        
        // If we found a left panel, add it to our container
        if (leftPanel != null) {
            leftSideContainer.getChildren().add(leftPanel);
        }
        
        // Set the combined container to the left position
        setLeft(leftSideContainer);
    }

    /**
     * Map shuffled quadrant index (0-3) to a PanelPosition enum.
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
     * Sets a player panel to the specified position in the BorderPane.
     */
    private void setPlayerPanel(Pane panel, PanelPosition position) {
        switch (position) {
            case BOTTOM:
                BorderPane.setAlignment(panel, Pos.CENTER);
                setBottom(panel);
                break;
            case LEFT:
                BorderPane.setAlignment(panel, Pos.CENTER);
                setLeft(panel);
                break;
            case RIGHT:
                BorderPane.setAlignment(panel, Pos.CENTER);
                setRight(panel);
                break;
            case TOP:
                BorderPane.setAlignment(panel, Pos.CENTER);
                setTop(panel);
                break;
        }
    }

    /**
     * Draws the main circular track with automatic tile-sizing.
     */
    private Point2D[] drawTrack(List<Cell> trackCells, int totalTrackCells) {
        // Gap between circles
        double spacing = 10;
        // Desired tile radius
        double desiredTileRadius = 20;

        // Fixed board radius
        double R = ringRadius;
        // Straight-line distance (chord) between two adjacent centers on the ring
        double chord = 2 * R * Math.sin(Math.PI / totalTrackCells);
        // The maximum radius that still leaves 'spacing' px between tiles
        double maxFittingRadius = (chord - spacing) / 2;

        // Pick the smaller of desired size or fitting size, but never below a minimum
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

    /**
     * Draws connecting lines between track points.
     */
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

    /**
     * Draws each player's safe zone with exactly 4 tiles per safe zone.
     */
    private void drawSafeZones(Board board, Point2D[] trackPts, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {
        final int total = trackPts.length;
        // Use a consistent spacing between safe zone tiles
        final double SAFESP = tileRadius * 2.2;

        for (Map.Entry<Integer, PlayerPanelInfo> e : playerPanelInfoMap.entrySet()) {
            int panelIdx = e.getKey();
            PlayerPanelInfo info = e.getValue();

            // Get shuffled quadrant and map to a compass position
            PanelPosition pos = mapQuadToPosition(quadrantOrder.get(panelIdx));

            // Pick the track-cell index that corresponds to that compass point
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

            // Always draw exactly 4 safe zone tiles (limiting to 4 regardless of actual
            // size)
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
     * Draws each player's 2×2 home square in the corner next to their panel.
     */
    private void drawHomeZones(Board board, Map<Integer, PlayerPanelInfo> playerPanelInfoMap) {
        double homeSize = 80;
        double margin = 20;
        Image homeZoneImage = new Image(getClass().getResourceAsStream("/images/homezone.png"));
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

            // Background pane
            Rectangle rect = new Rectangle(x0, y0, homeSize, homeSize);
            BackgroundImage bgImage = new BackgroundImage(
                    homeZoneImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(homeSize, homeSize, false, false, false, false));
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

            // Compute cell size & marble size
            double cellSize = homeSize / 2;
            double pieceRadius = (cellSize / 2) * marbleToCellRadiusRatio;
            String marbleImageName;
            switch (info.playerColor) {
                case RED:
                    marbleImageName = "/images/redMarble.png";
                    break;
                case BLUE:
                    marbleImageName = "/images/blueMarble.png";
                    break;
                case YELLOW:
                    marbleImageName = "/images/yellowMarble.png";
                    break;
                case GREEN:
                    marbleImageName = "/images/greenMarble.png";
                    break;
                default:
                    marbleImageName = "/images/greyMarble.png";
                    break;
            }
            Image marbleImg = new Image(getClass().getResourceAsStream(marbleImageName));

            // Place 2×2 marbles
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    double cellCenterX = x0 + c * cellSize + cellSize / 2;
                    double cellCenterY = y0 + r * cellSize + cellSize / 2;

                    // Optional shadow behind
                    Circle shadow = new Circle(
                            cellCenterX + pieceRadius * 0.1,
                            cellCenterY + pieceRadius * 0.1,
                            pieceRadius);
                    shadow.setStyle("-fx-fill: #00000055; -fx-stroke: transparent;");
                    centerPane.getChildren().add(shadow);

                    // Image-based marble
                    ImageView marbleView = new ImageView(marbleImg);
                    double diameter = pieceRadius * 2;
                    marbleView.setFitWidth(diameter);
                    marbleView.setFitHeight(diameter);
                    marbleView.setPreserveRatio(true);
                    // Center the image
                    marbleView.setLayoutX(cellCenterX - pieceRadius);
                    marbleView.setLayoutY(cellCenterY - pieceRadius);
                    centerPane.getChildren().add(marbleView);
                }
            }
        }
    }

    /**
     * Creates a player panel UI with cards, name, and icon.
     */
    private Pane createPlayerPanelUI(Image icon, boolean horizontalLayout, String name, String cssColor,
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
                        "-fx-font-weight: bold;");
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        // 3) Card area: one CardPane per card in hand
        HBox cardDisplayArea = new HBox(5);
        cardDisplayArea.setAlignment(Pos.CENTER);
        double cardH = CELL_SIZE_FOR_PANELS * 0.90;
        double cardW = cardH * (2.5 / 3.5);
        List<Card> hand = player.getHand();
        cardDisplayArea.setPrefSize(
                cardW * hand.size() + 5 * (hand.size() - 1),
                cardH);

        // Determine if this is a human player (non-CPU)
        boolean isHuman = !(player instanceof CPU);

        for (Card card : hand) {
            CardPane cv = new CardPane(card, player);
            // Only face up for human player
            cv.setFaceUp(isHuman);
            cv.setPrefSize(cardW, cardH);
            cv.setMaxSize(cardW, cardH);
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

    /**
     * Panel position enum for player panels.
     */
    private enum PanelPosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    /**
     * Player panel information class.
     */
    private static class PlayerPanelInfo {
        public final Image icon;
        public final String name;
        public final String cssColor;
        public final PanelPosition position;
        public final boolean isHorizontal;
        public final Colour playerColor;

        public PlayerPanelInfo(Image icon, String name, String cssColor, PanelPosition position,
                boolean isHorizontal, Colour playerColor) {
            this.icon = icon;
            this.name = name;
            this.cssColor = cssColor;
            this.position = position;
            this.isHorizontal = isHorizontal;
            this.playerColor = playerColor;
        }
    }

    /**
     * Inner class that combines CardView and CardController functionality.
     */
    private class CardPane extends StackPane {
        private final ObjectProperty<Card> cardProperty = new SimpleObjectProperty<>();
        private final Rectangle frame = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        private final ImageView cardImageView = new ImageView();
        private boolean faceUp = true;
        private final Card card;
        private final Player owner;

        public CardPane(Card card, Player owner) {
            this.card = card;
            this.owner = owner;

            // Set view size & alignment
            setPrefSize(frame.getWidth(), frame.getHeight());
            setAlignment(Pos.CENTER);

            // Frame styling
            frame.setArcWidth(8);
            frame.setArcHeight(8);
            frame.setStroke(Color.DARKGRAY);
            frame.setStrokeWidth(1.1);
            frame.setFill(Color.WHITE);

            // Image sizing
            cardImageView.setFitWidth(CARD_WIDTH);
            cardImageView.setFitHeight(CARD_HEIGHT);
            cardImageView.setSmooth(true);
            cardImageView.setPreserveRatio(false);

            // Stacking order: white background, then card pic
            getChildren().addAll(frame, cardImageView);
            setCard(card);

            // Hover cursor
            addEventHandler(MouseEvent.MOUSE_ENTERED, e -> setCursor(Cursor.HAND));
            addEventHandler(MouseEvent.MOUSE_EXITED, e -> setCursor(Cursor.DEFAULT));

            // Click handler
            setOnMouseClicked(this::handleCardClick);
        }

        /**
         * Handles clicks on a card - selects it for the player.
         */
        private void handleCardClick(MouseEvent event) {
            if (event.getButton() != MouseButton.PRIMARY)
                return;

            // Toggle selection state
            if (getEffect() == selectionGlow) {
                setEffect(baseShadow);  // Deselect if already selected
            } else {
                try {
                    // Deselect any previously selected card
                    cardsContainer.getChildren().forEach(node -> {
                        if (node instanceof CardPane) {
                            node.setEffect(baseShadow);
                        }
                    });
                    owner.selectCard(card);
                    highlight(true);
                    // Refresh the firedeck view
                    updateFiredeckView();
                    // Update turn indicators after playing a card
                    updateTurnIndicators();
                } catch (GameException ex) {
                    highlight(false);
                    showUserError(ex.getMessage());
                }
            }
        }

        /**
         * Changes which card this view should show.
         */
        public void setCard(Card card) {
            cardProperty.set(card);
            refresh();
        }

        /**
         * Returns the card property for binding.
         */
        public ReadOnlyObjectProperty<Card> cardProperty() {
            return cardProperty;
        }

        /**
         * Toggles whether the card is face-up or face-down.
         */
        public void setFaceUp(boolean faceUp) {
            if (this.faceUp != faceUp) {
                this.faceUp = faceUp;
                refresh();
            }
        }

        /**
         * Returns whether the card is currently face-up.
         */
        public boolean isFaceUp() {
            return faceUp;
        }

        /**
         * Highlights or unhighlights the card.
         */
        private void highlight(boolean active) {
            setEffect(active ? selectionGlow : null);
        }

        /**
         * Redraws the card based on current card property and face-up state.
         */
        private void refresh() {
            Card c = cardProperty.get();

            // Face-down or no card: show back
            if (c == null || !faceUp) {
                cardImageView.setImage(
                        new Image(CARD_DIR + "card_back.png", CARD_WIDTH, CARD_HEIGHT, true, true));
                return;
            }

            // Face-up + Standard card: map to tile###
            if (c instanceof Standard) {
                Standard std = (Standard) c;
                int rank = std.getRank(); // 1..13
                int base;
                switch (std.getSuit()) {
                    case HEART:
                        base = 0;
                        break; // tile000–012
                    case CLUB:
                        base = 13;
                        break; // tile013–025
                    case DIAMOND:
                        base = 26;
                        break; // tile026–038
                    case SPADE:
                        base = 39;
                        break; // tile039–051
                    default:
                        base = 0;
                        break;
                }
                int idx = base + (rank - 1);
                String filename = String.format("tile%03d.png", idx);
                cardImageView.setImage(
                        new Image(CARD_DIR + filename, CARD_WIDTH, CARD_HEIGHT, true, true));
            } else if (c instanceof Burner) {
                cardImageView.setImage(
                        new Image(CARD_DIR + "tile052.png", CARD_WIDTH, CARD_HEIGHT, true, true));
            } else if (c instanceof Saver) {
                cardImageView.setImage(
                        new Image(CARD_DIR + "tile053.png", CARD_WIDTH, CARD_HEIGHT, true, true));
            } else {
                // Non-standard wild: fallback to back
                cardImageView.setImage(
                        new Image(CARD_DIR + "card_back.png", CARD_WIDTH, CARD_HEIGHT, true, true));
            }
        }
    }
}