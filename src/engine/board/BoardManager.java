package engine.board;

import exception.IllegalMovementException;
import exception.IllegalDestroyException;
import exception.IllegalSwapException;
import exception.CannotFieldException;
import exception.InvalidMarbleException;
import model.player.Marble;
import java.util.ArrayList;

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

    /**
     * Moves a marble by the specified number of steps and optionally destroys it.
     *
     * @param marble The marble to move
     * @param steps The number of steps to move the marble
     * @param destroy Whether to destroy the marble after moving
     * @throws IllegalMovementException If the movement is not valid
     * @throws IllegalDestroyException If the marble cannot be destroyed
     */
    void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException;

    /**
     * Swaps the positions of two marbles on the board.
     *
     * @param marble1 The first marble to swap
     * @param marble2 The second marble to swap
     * @throws IllegalSwapException If the swap operation is not valid
     */
    void swap(Marble marble1, Marble marble2) throws IllegalSwapException;

    /**
     * Destroys a marble, removing it from the board.
     *
     * @param marble The marble to destroy
     * @throws IllegalDestroyException If the marble cannot be destroyed
     */
    void destroyMarble(Marble marble) throws IllegalDestroyException;

    /**
     * Sends a marble back to its base position.
     *
     * @param marble The marble to send to base
     * @throws CannotFieldException If the marble cannot be sent to base
     * @throws IllegalDestroyException If the marble cannot be destroyed during the process
     */
    void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException;

    /**
     * Sends a marble to a safe zone.
     *
     * @param marble The marble to send to safe zone
     * @throws InvalidMarbleException If the marble is not valid for safe zone placement
     */
    void sendToSafe(Marble marble) throws InvalidMarbleException;

    /**
     * Retrieves a list of marbles that can be acted upon in the current game state.
     *
     * @return An ArrayList of marbles that are available for actions
     */
    ArrayList<Marble> getActionableMarbles();
}
