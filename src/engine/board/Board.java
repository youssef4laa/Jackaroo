package engine.board;

import java.util.ArrayList;
import java.util.Random;
import engine.GameManager;
import model.Colour;

/**
 * Represents the game board for Jackaroo.
 * Initializes track cells, safe zones, and manages game state.
 */
public class Board implements BoardManager {
    private final GameManager gameManager;
    private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones;
    private int splitDistance;
    private static final int TOTAL_CELLS = 52;
    private static final int TRAP_COUNT = 8;
    private static final int[] BASE_POSITIONS = {0, 13, 26, 39};
    private static final int[] ENTRY_POSITIONS = {1, 14, 27, 40};
    private final Random random = new Random();

    /**
     * Constructs a Board object, initializing the game board.
     *
     * @param colourOrder   The order of colours for the safe zones.
     * @param gameManager   The GameManager instance.
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
     * Initializes the track with the appropriate cell types.
     */
    private void initializeTrack() {
        for (int i = 0; i < TOTAL_CELLS; i++) {
            CellType cellType = determineCellType(i);
            track.add(new Cell(null, cellType, false));
        }
    }

    /**
     * Determines the cell type based on its position.
     *
     * @param index The index of the cell on the track.
     * @return The CellType for the specified index.
     */
    private CellType determineCellType(int index) {
        if (contains(BASE_POSITIONS, index)) {
            return CellType.BASE;
        } else if (contains(ENTRY_POSITIONS, index)) {
            return CellType.ENTRY;
        } else {
            return CellType.NORMAL;
        }
    }

    /**
     * Helper method to check if an array contains a specific value.
     *
     * @param array The array to search.
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
     * Randomly assigns trap cells within the normal track cells.
     *
     * @param count The number of trap cells to assign.
     */
    private void assignTrapCells(int count) {
        for (int i = 0; i < count; i++) {
            assignTrapCell();
        }
    }

    /**
     * Randomizes a cell position on the track and flags it as a trap cell.
     * The position must be a NORMAL cell and not already flagged as a trap.
     */
    private void assignTrapCell() {
        int index;
        do {
            index = random.nextInt(TOTAL_CELLS);
        } while (track.get(index).getCellType() != CellType.NORMAL || track.get(index).isTrap());
        track.get(index).setTrap(true);
    }

    /**
     * Creates SafeZones based on the provided colour order.
     *
     * @param colourOrder The order of colours to assign to safe zones.
     */
    private void createSafeZones(ArrayList<Colour> colourOrder) {
        for (Colour colour : colourOrder) {
            safeZones.add(new SafeZone(colour));
        }
    }

    public int getSplitDistance() {
        return splitDistance;
    }

    public void setSplitDistance(int splitDistance) {
        this.splitDistance = splitDistance;
    }

    public ArrayList<Cell> getTrack() {
        return track;
    }

    public ArrayList<SafeZone> getSafeZones() {
        return safeZones;
    }

}
