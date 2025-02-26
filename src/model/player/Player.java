package model.player;

import java.util.ArrayList;
import model.Colour;
import model.card.Card;

public class Player {
	private final String name;
	private final Colour colour;
	private ArrayList<Card> hand;
	private ArrayList<Marble> marbles;
	private Card selectedCard;
	private ArrayList<Marble> selectedMarbles;

	/**
	 * 
	 * @param name            represents name of the player
	 * @param colour          represents the player's safe zones & marbles
	 * @param hand            represents the hand each player has
	 * @param marbles         represents the marbles in each player's Home Zone
	 * @param selectedCard    represents the card to be played
	 * @param selectedMarbles marbles to be played
	 */
	public Player(String name, Colour colour) {
		super();
		this.name = name;
		this.colour = colour;
		selectedCard = null;
		hand = new ArrayList<>();
		selectedMarbles = new ArrayList<>();
		marbles = new ArrayList<>();
		for (int i = 0; i < 4; i++)
			marbles.add(new Marble(colour));

	}

//getters & setters 
	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public String getName() {
		return name;
	}

	public Colour getColour() {
		return colour;
	}

	public ArrayList<Marble> getMarbles() {
		return marbles;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

}
