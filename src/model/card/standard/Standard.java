package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

public class Standard extends Card {
	private final int rank; // Rank of the card ( Ace, King, Queen, Jack, Four, Five,Seven, Ten)
	private final Suit suit; // Suit of the card (HEART, DIAMOND, SPADE, CLUB)
	/**
	 * Constructs a standard playing card.
	 * 
	 * @param name         The name of the card (inherited from Card)
	 * @param description  A brief description of the card (inherited from Card)
	 * @param rank         The rank of the card
	 * @param suit         The suit of the card 
	 * @param boardManager The board manager (inherited from Card)
	 * @param gameManager  The game manager (inherited from Card)
	 */
	public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager,
			GameManager gameManager) {
		super(name, description, boardManager, gameManager);
		this.rank = rank;
		this.suit = suit;
	}

	public int getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

}
