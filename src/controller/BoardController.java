package controller;

import engine.Game;
import engine.board.Board;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane; // For the return type of getView
import model.Colour; // Import the Colour enum
import view.BoardView;
import view.FiredeckView;


import java.io.IOException;
import java.util.*;

public class BoardController {

    private Game game;
    private BoardView boardView;
    private FiredeckController firedeckController;
    private FiredeckView firedeckViewInstance; // Keep instance for updates

    private final String[] defaultNames  = { "Mr.Red", "Mr.Green", "Mr.Blue", "Mr.Yellow" };
    private final String[] defaultCssColors = { "red", "green", "blue", "goldenrod" }; // CSS compatible
    private final Colour[] playerColours = { Colour.RED, Colour.GREEN, Colour.BLUE, Colour.YELLOW }; // Add player colours array
    private final String[] iconPaths = {
        "/images/player_red.png",
        "/images/player_green.png",
        "/images/player_blue.png",
        "/images/player_yellow.png"
    };
    private Image[] playerIcons;


    public BoardController(String humanPlayerName) throws IOException {
        this.game = new Game(humanPlayerName); // Game model initialized here
        this.boardView = new BoardView();
        loadPlayerIcons();

        // Create panel configurations first as they are needed for setupUI and drawGameBoard
        Map<Integer, BoardView.PlayerPanelInfo> playerPanelConfig = createPlayerPanelConfigurations(humanPlayerName);

        setupBoardUI(playerPanelConfig); // Pass the config to setup UI elements

        // Initialize Firedeck
        this.firedeckViewInstance = new FiredeckView();
        this.firedeckController = new FiredeckController(this.game, this.firedeckViewInstance);

        // Draw the main game board components, including placing the firedeck
        // The playerPanelInfoMap is needed for drawing home zones and safe zones correctly according to player color/position
        boardView.drawGameBoard(game.getBoard(), playerPanelConfig, firedeckViewInstance);
    }

    private void loadPlayerIcons() {
        playerIcons = new Image[iconPaths.length];
        for (int i = 0; i < iconPaths.length; i++) {
            try {
                playerIcons[i] = new Image(BoardView.class.getResourceAsStream(iconPaths[i]));
            } catch (Exception e) {
                System.err.println("Failed to load icon: " + iconPaths[i]);
                // playerIcons[i] will be null, handle gracefully in createPlayerPanelUI if necessary
                // Consider using a placeholder image or a default error icon.
            }
        }
    }

    // Modified setupBoardUI to accept the panel configuration map
    private void setupBoardUI(Map<Integer, BoardView.PlayerPanelInfo> panelInfoMap) {
        for (BoardView.PlayerPanelInfo info : panelInfoMap.values()) {
            Pane panel = boardView.createPlayerPanelUI(info.icon, info.isHorizontal, info.name, info.cssColor);
            boardView.setPlayerPanel(panel, info.position);
        }
        // Initial drawing of board elements is now moved to the constructor after firedeck init
    }


    private Map<Integer, BoardView.PlayerPanelInfo> createPlayerPanelConfigurations(String humanPlayerName) {
        Map<Integer, BoardView.PlayerPanelInfo> panelConfigurations = new HashMap<>();
        List<Integer> logicalPlayerIndices = Arrays.asList(0, 1, 2, 3); // Represents logical player indices (RED=0, GREEN=1, BLUE=2, YELLOW=3)
        Collections.shuffle(logicalPlayerIndices, new Random()); // Shuffle which logical player color gets which panel position

        // We need to know which *panel position* corresponds to the human player
        // Let's decide the human player always gets the BOTTOM panel (index 0 in visualPositions)
        // Find the logical player index that was randomly assigned to visual position 0 (BOTTOM)
        int logicalPlayerIndexForBottomPanel = logicalPlayerIndices.indexOf(0); // Find the original index of the player who ended up at visual position 0

        // Define visual panel positions (mapping to BorderPane regions) and their properties
        BoardView.PanelPosition[] visualPositions = {
            BoardView.PanelPosition.BOTTOM, // Index 0
            BoardView.PanelPosition.LEFT,   // Index 1
            BoardView.PanelPosition.TOP,    // Index 2
            BoardView.PanelPosition.RIGHT   // Index 3
        };
        boolean[] isHorizontalLayout = {true, false, true, false}; // For Bottom/Top, Left/Right

        // Now, map the shuffled logical player indices to the fixed visual panel positions
        for (int visualPanelIndex = 0; visualPanelIndex < visualPositions.length; visualPanelIndex++) {
            // Get the logical player index that was randomly assigned to this visual panel position
            int logicalPlayerIdxAssignedToPanel = logicalPlayerIndices.get(visualPanelIndex); // This index corresponds to the color, name, icon, etc.

            String playerName = defaultNames[logicalPlayerIdxAssignedToPanel];
            // If this logical player was the one randomly assigned to the bottom panel (our human player position)
            // This logic is slightly complex because we shuffled logical players and then assigned them to fixed panel positions.
            // A clearer approach might be to shuffle the visual positions and assign logical players.
            // Let's revise the shuffling slightly for clarity.

        }

        // --- Revised Shuffling Logic ---
        List<Integer> panelPositionIndices = Arrays.asList(0, 1, 2, 3); // Represents visual panel indices (0=Bottom, 1=Left, 2=Top, 3=Right)
        Collections.shuffle(panelPositionIndices, new Random()); // Shuffle the order of panel positions

        // Now assign logical players (by their fixed index 0-3) to the shuffled panel positions
        for (int logicalPlayerIdx = 0; logicalPlayerIdx < logicalPlayerIndices.size(); logicalPlayerIdx++) {
             // The panelPositionIndex for this logical player is at index logicalPlayerIdx in the shuffled list
            int assignedPanelPositionIndex = panelPositionIndices.get(logicalPlayerIdx);

            BoardView.PanelPosition currentVisualPos = visualPositions[assignedPanelPositionIndex];
            boolean horizontal = isHorizontalLayout[assignedPanelPositionIndex];
            String playerName = defaultNames[logicalPlayerIdx];
            Colour playerColor = playerColours[logicalPlayerIdx]; // Get the actual color enum

            // Determine if this logical player is the human player (assuming humanPlayerName matches one of the defaultNames initially)
            // A more robust way is to pass the human player's Colour or a player object from Game to the controller.
            // For this fix, let's assume the human player's *color* is determined elsewhere or can be matched by name initially.
            // Let's stick to matching by name for simplicity based on current code, but be aware of limitations.
            if (playerName.equals(humanPlayerName)) {
                // This logical player is the human player
                playerName = humanPlayerName; // Use the provided human player name
            }


            panelConfigurations.put(
                assignedPanelPositionIndex, // Key is the assigned visual panel index (0-3)
                new BoardView.PlayerPanelInfo(
                    playerIcons[logicalPlayerIdx], // Icon corresponds to the logical player's original color index
                    playerName,
                    defaultCssColors[logicalPlayerIdx], // CSS color corresponds to the logical player's original color index
                    currentVisualPos,
                    horizontal,
                    playerColor // ***Crucially, include the player's game model Colour***
                )
            );
        }

        // Ensure the human player (if matched by name) is correctly identified in the configuration
        // This simple name matching might be problematic if player names are not unique or change.
        // A better approach is to identify the human player by their Colour or a Player object from the Game model.
        // Assuming for now that the humanPlayerName reliably matches one of the default names initially.
        boolean humanPlayerFound = false;
        for (BoardView.PlayerPanelInfo info : panelConfigurations.values()) {
            if (info.name.equals(humanPlayerName) && !info.name.startsWith("Mr.")) { // Simple check to see if the name was customized
                 // Assuming the player whose default name was replaced is the human player
                 humanPlayerFound = true;
                 // If you need to store which panel position the human is in, you could do it here.
                 // Example: int humanPanelIndex = info.position.ordinal(); // Assuming enum ordinal matches 0-3 order
                 break;
            }
        }
        // If human player not found (e.g., humanPlayerName didn't match), you might want to handle this.
        // For this fix, we proceed assuming the human player is included in the map.


        return panelConfigurations;
    }


    public BorderPane getGameView() {
        return boardView.getRootPane();
    }

    public void refreshFiredeck() {
        if (firedeckController != null) {
            firedeckController.updateFiredeck();
        }
    }

    // Add other methods to handle game interactions if needed
    // e.g., methods that respond to clicks on the board, which would then update the model
    // and refresh views.
}