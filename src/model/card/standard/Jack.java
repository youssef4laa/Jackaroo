package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents the Jack card in a standard deck.
 * This class extends the {@code Standard} class and initializes a Jack card 
 * with a predefined rank value of 11.
 */
public class Jack extends Standard {

    /**
     * Constructs a Jack card with a rank of 11.
     *
     * @param name        The name of the card.
     * @param description A brief description of the card.
     * @param suit        The suit of the card (e.g., Hearts, Diamonds, Clubs, Spades).
     * @param boardManager The {@code BoardManager} instance that manages the game board.
     * @param gameManager The {@code GameManager} instance that controls the game flow.
     */
    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 11, suit, boardManager, gameManager);
    }
}

