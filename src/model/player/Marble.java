package model.player;

import model.Colour;

/**
 * Represents a marble in the Jackaroo game. Each marble is associated with a specific colour.
 */
public class Marble {
    private final Colour colour;

    /**
     * Constructs a Marble with a specified colour.
     * 
     * @param colour The colour of the marble (RED, GREEN, BLUE, YELLOW).
     */
    public Marble(Colour colour) {
        this.colour = colour;
    }

    /**
     * Retrieves the colour of the marble.
     * 
     * @return The Colour associated with this marble.
     */
    public Colour getColour() {
        return this.colour;
    }
}
