package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents the Five card in a standard deck.
 * This class extends the {@code Standard} class and initializes a Five card 
 * with a predefined rank value of 5.
 */
public class Five extends Standard {

    /**
     * Constructs a Five card with a rank of 5.
     *
     * @param name        The name of the card.
     * @param description A brief description of the card.
     * @param suit        The suit of the card (e.g., Hearts, Diamonds, Clubs, Spades).
     * @param boardManager The {@code BoardManager} instance that manages the game board.
     * @param gameManager The {@code GameManager} instance that controls the game flow.
     */
    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }
}

