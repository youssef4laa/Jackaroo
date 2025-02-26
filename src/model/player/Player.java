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
 * @param name
 * @param colour
 * @param hand
 * @param marbles
 * @param selectedCard
 * @param selectedMarbles
 */
public Player(String name, Colour colour) {
	super();
	this.name = name;
	this.colour = colour;
	selectedCard = null;
	hand = new ArrayList<>();
	selectedMarbles =  new ArrayList<>();
	marbles = new ArrayList<>();

}
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
