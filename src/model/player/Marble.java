package model.player;

import model.Colour;

//Class representing the Marbles in the game.
public class Marble {
	private final Colour colour;
/**
 * 
 * @param colour enum representing colour of the marble (RED, GREEN, BLUE, YELLOW).
 */
	public Marble(Colour colour) {
		super();
		this.colour = colour;
	}
	public Colour getColour() {
		return this.colour;
	}
}
