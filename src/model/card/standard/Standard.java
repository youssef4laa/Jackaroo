package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

/**
 * Represents a standard playing card with a specific rank and suit.
 * Inherits common card properties and methods from the abstract Card class.
 */
public class Standard extends Card {
    private final int rank; // Rank of the card (Ace, King, Queen, Jack, Four, Five, Seven, Ten)
    private final Suit suit; // Suit of the card (HEART, DIAMOND, SPADE, CLUB)

    /**
     * Constructs a standard playing card.
     *
     * @param name The name of the card (inherited from Card)
     * @param description A brief description of the card (inherited from Card)
     * @param rank The rank of the card
     * @param suit The suit of the card
     * @param boardManager The board manager (inherited from Card)
     * @param gameManager The game manager (inherited from Card)
     */
    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager,
                    GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Retrieves the rank of the card.
     *
     * @return The rank as an integer
     */
    public int getRank() {
        return rank;
    }

    /**
     * Retrieves the suit of the card.
     *
     * @return The suit of the card as a Suit enum
     */
    public Suit getSuit() {
        return suit;
    }
}

