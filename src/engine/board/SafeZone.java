package engine.board; // Defines the package where this class belongs
import java.util.ArrayList; // Imports the ArrayList class to use dynamic lists
import model.Colour; // Imports the Colour class, which associates the safe zone with a player
import engine.board.Cell;   // Imports the Cell class, representing individual board cells
//This is a class that represents any Safe Zone on the board.
public class SafeZone { // Defines the SafeZone class
    // Attributes (Read-Only)
    private final Colour colour; // Stores the colour associated with this SafeZone, immutable
    private final ArrayList<Cell> cells; // Stores the list of four safe cells, immutable

    // Constructor
    public SafeZone(Colour colour) { // Initializes the SafeZone with a given colour
        this.colour = colour; // Assigns the provided colour to this SafeZone
        this.cells = new ArrayList<>(); // Initializes an empty list to store safe cells
        
        // Initialize 4 SAFE cells
        for (int i = 0; i < 4; i++) { // Loops four times to create 4 cells
        	  this.cells.add(new Cell(null, CellType.SAFE, false));  // Adds a new SAFE cell with no marbles that isn't a trap to the list.
        }
    }

    // Getter methods
    public Colour getColour() { // Provides access to the colour attribute
        return colour; // Returns the colour of this SafeZone
    }

    public ArrayList<Cell> getCells() { // Provides access to the list of safe cells
        return new ArrayList<>(cells); // Returns a copy of the cells list to maintain immutability
    }
}
