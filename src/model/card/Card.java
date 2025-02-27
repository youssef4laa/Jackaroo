package model.card;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents an abstract in-game card used in the Jackaroo game.
 * Cards encapsulate actions and effects that influence the board and game state.
 * This class is intended to be extended by specific card types.
 */
public abstract class Card {
    private final String name;         // The name of the card
    private final String description;  // A brief description of the card's effect
    protected BoardManager boardManager; // Interface to manage board interactions
    protected GameManager gameManager;   // Interface to manage game state

    /**
     * Constructs a new Card with a specified name, description, and game interfaces.
     * 
     * @param name The name of the card.
     * @param description A brief description of the card.
     * @param boardManager An interface for interacting with the game board.
     * @param gameManager An interface for managing game state and player turns.
     */
    public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
        this.name = name;
        this.description = description;
        this.boardManager = boardManager;
        this.gameManager = gameManager;
    }

    /**
     * Retrieves the description of the card.
     * 
     * @return The description of the card.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the name of the card.
     * 
     * @return The name of the card.
     */
    public String getName() {
        return name;
    }
}
