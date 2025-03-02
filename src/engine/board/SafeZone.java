package engine.board;

import java.util.ArrayList;
import model.Colour;

/**
 * Represents a SafeZone on the Jackaroo game board.
 * SafeZones are associated with a specific player colour and contain four safe cells 
 * where marbles are protected from capture.
 */
public class SafeZone {

    /** The colour associated with this SafeZone, representing the player it belongs to. */
    private final Colour colour;

    /** A list of four cells in the SafeZone, all of which are of {@link CellType#SAFE}. */
    private final ArrayList<Cell> cells;

    /**
     * Constructs a SafeZone with the specified player colour.
     * Initializes the SafeZone with four SAFE-type cells.
     *
     * @param colour The {@link Colour} associated with this SafeZone.
     */
    public SafeZone(Colour colour) {
        this.colour = colour;
        this.cells = new ArrayList<>();

        // Initialize 4 SAFE cells with no marbles and not set as traps.
        for (int i = 0; i < 4; i++) {
            this.cells.add(new Cell(CellType.SAFE));
        }
    }

    /**
     * Retrieves the colour associated with this SafeZone.
     *
     * @return The {@link Colour} of the SafeZone.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Retrieves a copy of the list of safe cells in this SafeZone.
     * This ensures immutability by returning a new {@link ArrayList}.
     *
     * @return A list of {@link Cell} objects representing the safe cells.
     */
    public ArrayList<Cell> getCells() {
        return new ArrayList<>(cells);
    }
}
