package model.player;

import java.util.ArrayList;

import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import exception.ActionException;
import model.card.Card;

@SuppressWarnings("unused")
public class Player {
    private final String name;
    private final Colour colour;
    private ArrayList<Card> hand;
    private final ArrayList<Marble> marbles;
    private Card selectedCard;
	private final ArrayList<Marble> selectedMarbles;

    public Player(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        this.hand = new ArrayList<>();
        this.selectedMarbles = new ArrayList<>();
        this.marbles = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            this.marbles.add(new Marble(colour));
        }
        
        //default value
        this.selectedCard = null;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    
    public ArrayList<Marble> getMarbles() {
		return marbles;
	}
    
    public Card getSelectedCard() {
        return selectedCard;
   }
      
    public void regainMarble(Marble marble) {
        if (marble != null && marble.getColour() == this.colour && !marbles.contains(marble)) {
            marbles.add(marble);
        }
    }
    
    public Marble getOneMarble() {
        if (!marbles.isEmpty()) {
            return marbles.get(0);
        }
        return null;
    }

    public void selectCard(Card card) throws InvalidCardException {
        if (card == null || !hand.contains(card)) {
            throw new InvalidCardException("Selected card is not in player's hand");
        }
        this.selectedCard = card;
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        // if they've already selected this exact marble, do nothing
        if (selectedMarbles.contains(marble)) {
            return;
        }
        // only allow up to 2 distinct marbles
        if (selectedMarbles.size() >= 2) {
            throw new InvalidMarbleException("Cannot select more than two marbles");
        }
        // (any other validation you already have here)
        selectedMarbles.add(marble);
    }
	
    public void deselectAll() {
        selectedCard = null;
        selectedMarbles.clear();
    }
    public void play() throws GameException {
        if (selectedCard == null) {
            throw new InvalidCardException("No card has been selected.");
        }

        ArrayList<Marble> marblesToActOn = new ArrayList<>(selectedMarbles);

        if (!selectedCard.validateMarbleSize(marblesToActOn)) {
            throw new InvalidMarbleException("Invalid number of selected marbles for this card.");
        }

        if (!selectedCard.validateMarbleColours(marblesToActOn)) {
            throw new InvalidMarbleException("Invalid marble colours for the selected card.");
        }

        selectedCard.act(marblesToActOn);
    }



    
    

}
