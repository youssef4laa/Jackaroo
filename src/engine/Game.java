package engine;

import model.Colour;
import model.card.*;
import model.player.Player;
import model.player.CPU;
import engine.board.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The {@code Game} class represents the main engine of the Jackaroo game. it
 * manages game setup, player turns, board interactions, and game state
 * progression.
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

	/**
	 * The current turn count, incremented each time all players have taken a turn.
	 */
	private int turn;

	/**
	 * Constructs a new Game instance, initializing the board, players, and game
	 * state.
	 *
	 * @param playerName The name of the human player participating in the game.
	 * @throws IOException If an error occurs while loading the card pool.
	 */
	public Game(String playerName) throws IOException {
		// Initialize the board with shuffled player colours
		ArrayList<Colour> colours = new ArrayList<>();
		colours.add(Colour.BLUE);
		colours.add(Colour.GREEN);
		colours.add(Colour.RED);
		colours.add(Colour.YELLOW);
		Collections.shuffle(colours);
		this.board = new Board(colours, this);
		Deck.loadCardPool(board, this);
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


	public Board getBoard() {
		return board;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Card> getFirePit() {
		return firePit;
	}

}
