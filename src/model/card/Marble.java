package model.card;
import model.Colour;

public class Marble {

/**
 * Represents a marble in the Jackaroo game.
 * Each marble is owned by a player and has a state (e.g., in firepit, in home, on board).
 */


    private final Colour colour; // e.g., "Red", "Blue", "Green", "Yellow"
    private final int currentPlayerIndex;       // Unique identifier per player
    private boolean inFirepit;
    public Marble(Colour colour,int currentPlayerIndex) {
        this.colour = colour;
        this.currentPlayerIndex= currentPlayerIndex;
        this.inFirepit=false;

    /**
     * Constructs a marble with a specific color and ID.
     *
     * @param color the color of the marble
     * @param id    the ID of the marble (e.g., 0–3 if 4 per player)
    /**
     * Sets whether this marble is currently in the firepit.
     *
     * @param inFirepit true if in firepit, false otherwise
     */
    }
    public void setInFirepit(boolean inFirepit) {
        this.inFirepit = inFirepit;
    }

    /**
     * Checks whether the marble is in the firepit.
     *
     * @return true if in firepit, false otherwise
     */
    public boolean isInFirepit() {
        return inFirepit;
    }


    public int getId() {
        return currentPlayerIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Marble)) return false;
        Marble other = (Marble) obj;
        return this.currentPlayerIndex== other.currentPlayerIndex&& this.colour.equals(other.colour);
    }

    @Override
    public int hashCode() {
        return colour.hashCode() * 31 + currentPlayerIndex;
    }

    @Override
    public String toString() {
        return colour + " Marble #" + currentPlayerIndex+ (inFirepit ? " (in firepit)" : "");
    }




    
    
    

    public Colour getColour() {
        return this.colour;
    }
}
