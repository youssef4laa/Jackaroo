package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents the Ten card in a standard deck.
 * This class extends the {@code Standard} class and initializes a Ten card 
 * with a predefined rank value of 10.
 */
public class Ten extends Standard {

    /**
     * Constructs a Ten card with the specified attributes.
     *
     * @param name        The name of the card.
     * @param description A brief description of the card.
     * @param suit        The suit of the card (e.g., Hearts, Diamonds, Clubs, Spades).
     * @param boardManager The {@code BoardManager} instance that manages the game board.
     * @param gameManager The {@code GameManager} instance that controls the game flow.
     */
    public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 10, suit, boardManager, gameManager);
    }
}
