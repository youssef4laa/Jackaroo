package engine.board;

/**
 * Defines the contract for managing communication with the Board class.
 * Implementations of this interface handle board-related operations.
 */
public interface BoardManager {

    /**
     * Calculates the distance for a split move on the board.
     *
     * @return The split distance as an integer.
     */
    int getSplitDistance();
}
