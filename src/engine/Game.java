package engine;

import model.Colour;
import model.card.*;
import model.player.Player;
import model.player.CPU;
import engine.board.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Game class represents the main engine of the Jackaroo game, implementing GameManager.
 * It manages game setup, player turns, and board interactions.
 *
 * @author youssef4laa
 * @version 2025-02-26 19:57:10
 */
public class Game implements GameManager {
    private final Board board; // Read-only board object
    private final List<Player> players; // Read-only list of players
    private final List<Card> firePit; // Read-only list for discarded/played cards
    private int currentPlayerIndex; // Index of the current player
    private int turn; // Current turn count

    /**
     * Initializes a new Game instance.
     *
     * @param playerName The name of the human player.
     * @throws IOException If there is an issue loading the card pool.
     */
    public Game(String playerName) throws IOException {
        // Initialize the board with shuffled colours
        ArrayList<Colour> colours = new ArrayList<>(List.of(Colour.values()));
        Collections.shuffle(colours);
        this.board = new Board(colours, this);
        
        // Load card pool using existing method (passing BoardManager and GameManager)
        Deck.loadCardPool(board, this);

        // Initialize players list without modifying external methods
        this.players = new ArrayList<>();
        players.add(new Player(playerName, colours.get(0)));
        for (int i = 1; i <= 3; i++) {
            players.add(new CPU("CPU " + i, colours.get(i), board));
        }
        
        // Draw initial hands for each player and use a new ArrayList to avoid returning the entire cardsPool
        for (Player player : players) {
            ArrayList<Card> drawnHand = Deck.drawCards();
            player.setHand(drawnHand);
        }

        // Initialize game state
        this.currentPlayerIndex = 0;
        this.turn = 0;
        this.firePit = new ArrayList<>();
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    @Override
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (currentPlayerIndex == 0) {
            turn++;
        }
    }

    @Override
    public void addToFirePit(Card card) {
        if (card != null) {
            firePit.add(card);
        }
    }

    @Override
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getMarbles().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}