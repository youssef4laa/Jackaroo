package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents an Ace card, a subclass of Standard with a rank of 1.
 * Inherits from the Standard class and sets the rank to 1 for Ace cards.
 */
public class Ace extends Standard {

    /**
     * Constructs an Ace card with a rank of 1.
     *
     * @param name The name of the card (inherited from Card)
     * @param description A brief description of the card (inherited from Card)
     * @param suit The suit of the card (HEART, DIAMOND, SPADE, CLUB)
     * @param boardManager The board manager (inherited from Card)
     * @param gameManager The game manager (inherited from Card)
     */
    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        // Call the parent class constructor (Standard) with rank set to 1
        super(name, description, 1, suit, boardManager, gameManager);
    }
}

