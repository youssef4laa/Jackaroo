package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents a King card, a subclass of Standard with a rank of 13.
 * Inherits from the Standard class and sets the rank to 13 for King cards.
 */
public class King extends Standard {

    /**
     * Constructs a King card with a rank of 13.
     * This constructor initializes a King card by calling the parent constructor
     * of the Standard class, passing the name, description, suit, boardManager,
     * and gameManager, while explicitly setting the rank to 13.
     *
     * @param name The name of the card (inherited from Card)
     * @param description A brief description of the card (inherited from Card)
     * @param suit The suit of the card (HEART, DIAMOND, SPADE, CLUB)
     * @param boardManager The board manager (inherited from Card)
     * @param gameManager The game manager (inherited from Card)
     */
    public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        // Call the parent class constructor (Standard) with rank set to 13
        super(name, description, 13, suit, boardManager, gameManager);
    }
}
