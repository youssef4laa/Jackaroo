package engine.board;

import java.util.ArrayList;
import java.util.Random;
import engine.GameManager;
import model.Colour;

/**
 * Represents the game board for the Jackaroo game.
 * Manages the track cells, safe zones, and game state initialization.
 * Implements {@link BoardManager} to handle board-specific operations.
 */
public class Board implements BoardManager {
    /** Manages the overarching game logic and player interactions. */
    private final GameManager gameManager;

    /** The main track of the board, consisting of a list of {@link Cell} objects. */
    private final ArrayList<Cell> track;

    /** The safe zones for each player, stored as a list of {@link SafeZone} objects. */
    private final ArrayList<SafeZone> safeZones;

    /** The default split distance used in game mechanics. */
    private int splitDistance;

    /** The total number of cells on the board track. */
    private static final int TOTAL_CELLS = 100; // Changed from 52 to 100

    /** The number of trap cells to randomly assign on the board. */
    private static final int TRAP_COUNT = 8;

    /** Positions designated as BASE cells on the board. */
    private static final int[] BASE_POSITIONS = {0, 13, 26, 39};

    /** Positions designated as ENTRY cells on the board. */
    private static final int[] ENTRY_POSITIONS = {1, 14, 27, 40};

    /** A random number generator for assigning trap cells. */
    private final Random random = new Random();

    /**
     * Constructs a Board object, initializing the game board with cells and safe zones.
     *
     * @param colourOrder The order of player colours for assigning safe zones.
     * @param gameManager The {@link GameManager} instance to manage game state.
     */
    public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
        this.gameManager = gameManager;
        this.track = new ArrayList<>();
        this.safeZones = new ArrayList<>();
        this.splitDistance = 3;

        initializeTrack();
        assignTrapCells(TRAP_COUNT);
        createSafeZones(colourOrder);
    }

    /**
     * Initializes the track cells, setting their types based on position.
     */
    /**
     * Initializes the track cells in an anti-clockwise order.
     */
/**
 * Initializes the track cells in an anti-clockwise order.
 */
    private void initializeTrack() {
        track.clear(); // Clear existing track data before initializing
        for (int i = 0; i < 4; i++) {
            track.add(new Cell(CellType.BASE));
            for (int j = 0; j < 22; j++)
                track.add(new Cell(CellType.NORMAL));
            track.add(new Cell(CellType.ENTRY));
            track.add(new Cell(CellType.NORMAL));
        }
    }


/**
 * Determines the {@link CellType} based on the cell's index on the track.
 *
 * @param index The index of the cell on the track.
 * @return The determined {@link CellType} (NORMAL, SAFE, BASE, ENTRY).
 */
private CellType determineCellType(int index) {
    int adjustedIndex = TOTAL_CELLS - 1 - index; // Adjust to match anti-clockwise order

    if (contains(BASE_POSITIONS, adjustedIndex)) {
        return CellType.BASE;
    } else if (contains(ENTRY_POSITIONS, adjustedIndex)) {
        return CellType.ENTRY;
    } else {
        return CellType.NORMAL;
    }
}

/**
 * Checks if a given array contains a specific value.
 *
 * @param array The array to search through.
 * @param value The value to find.
 * @return True if the array contains the value, false otherwise.
 */
private boolean contains(int[] array, int value) {
    for (int i : array) {
        if (i == value) return true;
    }
    return false;
}


    /**
     * Randomly assigns the specified number of trap cells on the board.
     *
     * @param count The number of trap cells to assign.
     */
    private void assignTrapCells(int count) {
        for (int i = 0; i < count; i++) {
            assignTrapCell();
        }
    }

    /**
     * Randomizes a cell index and flags the cell as a trap if it is a NORMAL cell
     * and not already flagged as a trap.
     */
    private void assignTrapCell() {
        int index;
        do {
            index = random.nextInt(TOTAL_CELLS);
        } while (track.get(index).getCellType() != CellType.NORMAL || track.get(index).isTrap());
        track.get(index).setTrap(true);
    }

    /**
     * Creates safe zones for each player based on the provided colour order.
     *
     * @param colourOrder The order of colours to assign to each {@link SafeZone}.
     */
    private void createSafeZones(ArrayList<Colour> colourOrder) {
        for (Colour colour : colourOrder) {
            safeZones.add(new SafeZone(colour));
        }
    }

    /**
     * Retrieves the split distance used for game mechanics.
     *
     * @return The current split distance as an integer.
     */
    @Override
    public int getSplitDistance() {
        return splitDistance;
    }

    /**
     * Sets the split distance for game mechanics.
     *
     * @param splitDistance The split distance to set.
     */
    public void setSplitDistance(int splitDistance) {
        this.splitDistance = splitDistance;
    }

    /**
     * Retrieves the track of the board.
     *
     * @return A list of {@link Cell} objects representing the board's track.
     */
    public ArrayList<Cell> getTrack() {
        return track;
    }
    public static BoardManager createBoardManager(ArrayList<Colour> colourOrder, GameManager gameManager) {
        return new Board(colourOrder, gameManager);
    }


    /**
     * Retrieves the safe zones on the board.
     *
     * @return A list of {@link SafeZone} objects representing the safe zones.
     */
    public ArrayList<SafeZone> getSafeZones() {
        return safeZones;
    }
}