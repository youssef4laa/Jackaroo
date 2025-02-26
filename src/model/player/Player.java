package model.player;

import java.util.ArrayList;
import model.Colour;
import model.card.Card;

/**
 * Represents a player in the Jackaroo game, holding a hand of cards, marbles,
 * and managing the selected card and marbles for play.
 */
public class Player {
    private final String name;
    private final Colour colour;
    private ArrayList<Card> hand;
    private ArrayList<Marble> marbles;
    private Card selectedCard;
    private ArrayList<Marble> selectedMarbles;

    /**
     * Constructs a Player with a specified name and associated colour.
     * Initializes the player's hand, marbles, and other game-related properties.
     * 
     * @param name   The name of the player.
     * @param colour The player's associated colour, defining their safe zones and marbles.
     */
    public Player(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        this.selectedCard = null;
        this.hand = new ArrayList<>();
        this.selectedMarbles = new ArrayList<>();
        this.marbles = new ArrayList<>();
        
        // Initialize 4 marbles with the player's colour
        for (int i = 0; i < 4; i++) {
            marbles.add(new Marble(colour));
        }
    }

    /**
     * Retrieves the player's current hand of cards.
     * 
     * @return The ArrayList of cards in the player's hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Sets the player's hand to the provided list of cards.
     * 
     * @param hand The ArrayList of cards to assign as the player's hand.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * Retrieves the player's name.
     * 
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the player's associated colour.
     * 
     * @return The Colour representing the player's safe zones and marbles.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Retrieves the marbles owned by the player.
     * 
     * @return An ArrayList of the player's marbles.
     */
    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    /**
     * Retrieves the currently selected card for play.
     * 
     * @return The Card that the player has selected.
     */
    public Card getSelectedCard() {
        return selectedCard;
    }
}
