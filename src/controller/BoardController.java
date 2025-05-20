package controller;


import engine.Game;
import engine.board.Board;
import javafx.fxml.FXMLLoader;          // ← new
import javafx.scene.Parent;             // ← new
import javafx.scene.Scene;              // ← new
import javafx.scene.image.Image;
import javafx.stage.Modality;           // ← new
import javafx.stage.Stage;              // ← new
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.Colour;
import view.BoardView;
import view.ExceptionPopup;             // ← to show any IO errors loading the FXML      // ← your custom winner‐dialog controller
import view.FiredeckView;
import  view.*;
import java.io.IOException;
import java.util.*;
import model.player.Player;
public class BoardController {
    private DeckView deckViewInstance;
    private DeckController deckController;
    private Game game;
    private BoardView boardView;
    private FiredeckController firedeckController;
    private FiredeckView firedeckViewInstance;

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

    public BoardController(String humanPlayerName) throws IOException {
        this.game = new Game(humanPlayerName);
        this.boardView = new BoardView();
        loadPlayerIcons();
        Map<Integer, BoardView.PlayerPanelInfo> playerPanelConfig =
            createPlayerPanelConfigurations(humanPlayerName);
        setupBoardUI(playerPanelConfig);

        // Initialize firedeck
        this.firedeckViewInstance = new FiredeckView();
        this.firedeckController = new FiredeckController(this.game, this.firedeckViewInstance);
        
        // Initialize deck view
        this.deckViewInstance = new DeckView();
        this.deckController = new DeckController(this.deckViewInstance, cards -> {
            // Handle drawn cards here if needed
            Colour winner = game.checkWin();
            if (winner != null) {
                showWinnerDialog(winner);
            }
        });

        // Build a map of index -> Player model
        Map<Integer, Player> playerMap = new HashMap<>();
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            playerMap.put(i, players.get(i));
        }

        // Draw the full game board with track, safe zones, home zones, panels, firedeck and deck
        boardView.drawGameBoard(
            game.getBoard(),
            playerPanelConfig,
            playerMap,
            firedeckViewInstance,
            deckViewInstance
        );
    }

    private void loadPlayerIcons() {
        playerIcons = new Image[iconPaths.length];
        for (int i = 0; i < iconPaths.length; i++) {
            try {
                playerIcons[i] = new Image(
                    BoardView.class.getResourceAsStream(iconPaths[i])
                );
            } catch (Exception e) {
                System.err.println("Failed to load icon: " + iconPaths[i]);
            }
        }
    }

    private void setupBoardUI(Map<Integer, BoardView.PlayerPanelInfo> panelInfoMap) {
        for (BoardView.PlayerPanelInfo info : panelInfoMap.values()) {
            Player modelPlayer = game.getPlayers().stream()
                .filter(p -> p.getColour() == info.playerColor)
                .findFirst()
                .orElseThrow(() ->
                    new IllegalStateException("No model player for colour " + info.playerColor)
                );

            Pane panel = boardView.createPlayerPanelUI(
                info.icon,
                info.isHorizontal,
                info.name,
                info.cssColor,
                modelPlayer
            );
            boardView.setPlayerPanel(panel, info.position);
        }
    }

    private Map<Integer, BoardView.PlayerPanelInfo> createPlayerPanelConfigurations(String humanPlayerName) {
        Map<Integer, BoardView.PlayerPanelInfo> panelConfigurations = new HashMap<>();

        // Fixed visual positions and orientations
        BoardView.PanelPosition[] visualPositions = {
            BoardView.PanelPosition.BOTTOM,
            BoardView.PanelPosition.LEFT,
            BoardView.PanelPosition.TOP,
            BoardView.PanelPosition.RIGHT
        };
        boolean[] isHorizontalLayout = { true, false, true, false };

        // Shuffle panel positions
        List<Integer> panelPositionIndices = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(panelPositionIndices, new Random());

        for (int logicalIdx = 0; logicalIdx < playerColours.length; logicalIdx++) {
            int visualIdx = panelPositionIndices.get(logicalIdx);
            BoardView.PanelPosition pos = visualPositions[visualIdx];
            boolean horiz = isHorizontalLayout[visualIdx];

            String name = (logicalIdx == panelPositionIndices.get(0))
                          ? humanPlayerName
                          : defaultNames[logicalIdx];

            panelConfigurations.put(
                visualIdx,
                new BoardView.PlayerPanelInfo(
                    playerIcons[logicalIdx],
                    name,
                    defaultCssColors[logicalIdx],
                    pos,
                    horiz,
                    playerColours[logicalIdx]
                )
            );
        }

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
    private void showWinnerDialog(Colour winner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/WinnerView.fxml")
            );
            Parent root = loader.load();
            WinnerViewController ctrl = loader.getController();
            ctrl.setWinner(winner);

            Stage dialog = new Stage();
            // owner is your main window:
            dialog.initOwner(boardView.getRootPane().getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Game Over!");
            dialog.showAndWait();
        } catch (IOException e) {
            // if FXML fails to load, at least show a popup
            ExceptionPopup.showException(e);
        }
    }
    private Map<Integer, Player> toPlayerMap(List<Player> players) {
        Map<Integer, Player> m = new HashMap<>();
        for (int i = 0; i < players.size(); i++) m.put(i, players.get(i));
        return m;
    }
}
