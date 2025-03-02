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
 * The {@code Game} class represents the main engine of the Jackaroo game.
 * It manages game setup, player turns, board interactions, and game state progression.
 */
public class Game implements GameManager {

    /** The game board that manages cell and marble interactions. */
    private final Board board;

    /** The list of all players in the game, including human and CPU players. */
    private final ArrayList<Player> players;

    /** The collection of cards discarded or played, known as the fire pit. */
    private final ArrayList<Card> firePit;

    /** The index of the player whose turn is currently active. */
    private int currentPlayerIndex;

    /** The current turn count, incremented each time all players have taken a turn. */
    private int turn;

    /**
     * Constructs a new Game instance, initializing the board, players, and game state.
     *
     * @param playerName The name of the human player participating in the game.
     * @throws IOException If an error occurs while loading the card pool.
     */
    public Game(String playerName) throws IOException {
        // Initialize the board with shuffled player colours
        ArrayList<Colour> colours = new ArrayList<>(List.of(Colour.values()));
        Collections.shuffle(colours);
        this.board = new Board(colours, this);

        // Load the card pool using the Deck class, passing the board and game manager
        Deck.loadCardPool(board, this);

        // Initialize the list of players, adding the human player and three CPU players
        this.players = new ArrayList<>();
        players.add(new Player(playerName, colours.get(0)));
        for (int i = 1; i <= 3; i++) {
            players.add(new CPU("CPU " + i, colours.get(i), board));
        }

        // Draw initial hands for all players
        for (Player player : players) {
            ArrayList<Card> drawnHand = Deck.drawCards();
            player.setHand(drawnHand);
        }

        // Initialize the game state
        this.currentPlayerIndex = 0;
        this.turn = 0;
        this.firePit = new ArrayList<>();
    }

    /** Standard getter for board */
    public Board getBoard() {
        return board;
    }

    /** Standard getter for players list */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /** Standard getter for firePit */
    public ArrayList<Card> getFirePit() {
        return firePit;
    }

    /**
     * Advances the game to the next player's turn.
     * Increments the turn counter when all players have completed a round.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (currentPlayerIndex == 0) {
            turn++;
        }
    }

    /**
     * Adds a card to the fire pit (discard pile) if the card is not null.
     *
     * @param card The {@link Card} to add to the fire pit.
     */
    public void addToFirePit(Card card) {
        if (card != null) {
            firePit.add(card);
        }
    }

    /**
     * Checks if the game is over by evaluating if any player has no remaining marbles.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getMarbles().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the player whose turn is currently active.
     *
     * @return The current {@link Player}.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}