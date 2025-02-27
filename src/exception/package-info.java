/**
 * The {@code exception} package contains custom exceptions for the Jackaroo game.
 * These exceptions provide specific error handling for invalid actions and selections 
 * during gameplay, enhancing the robustness and reliability of the game engine.
 *
 * <p>
 * Key exception classes in this package:
 * <ul>
 *   <li>{@link exception.GameException} - The base class for all custom exceptions in the game.</li>
 *   <li>{@link exception.InvalidSelectionException} - Abstract class for exceptions related to invalid selections.</li>
 *   <li>{@link exception.InvalidCardException} - Thrown when a selected card is invalid for the current game state.</li>
 *   <li>{@link exception.InvalidMarbleException} - Indicates that a selected marble is not allowed for the intended action.</li>
 *   <li>{@link exception.SplitOutOfRangeException} - Raised when a split distance selection is outside the valid range.</li>
 *   <li>{@link exception.ActionException} - Abstract class for exceptions related to illegal gameplay actions.</li>
 *   <li>{@link exception.IllegalMovementException} - Thrown when an invalid marble movement is attempted.</li>
 *   <li>{@link exception.IllegalSwapException} - Indicates an invalid attempt to swap marbles on the board.</li>
 *   <li>{@link exception.IllegalDestroyException} - Raised when an action would wrongly return a marble to the Home Zone.</li>
 *   <li>{@link exception.CannotFieldException} - Thrown when a marble cannot be legally placed on the board.</li>
 *   <li>{@link exception.CannotDiscardException} - Thrown when a card cannot be discarded under current game rules.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try {
 *     gameManager.makeMove(player, move);
 * } catch (InvalidMoveException e) {
 *     System.out.println("Invalid move: " + e.getMessage());
 * } catch (InvalidCardException e) {
 *     System.out.println("Invalid card selection: " + e.getMessage());
 * }
 * }</pre>
 *
 * <p>
 * By using custom exceptions, the Jackaroo game achieves improved 
 * error tracking, debugging, and robust input validation.
 * </p>
 */
package exception;
