/**
 * The {@code view} package manages the user interface (UI) components of the Jackaroo game.
 * It is responsible for displaying the game board, player information, and interactions through a graphical or console-based interface.
 *
 * <p>
 * The view package facilitates:
 * <ul>
 *   <li>Rendering the game board and marble positions</li>
 *   <li>Displaying player hands and cards</li>
 *   <li>Showing game messages, including errors and turn updates</li>
 *   <li>Handling user inputs for playing cards and selecting marbles</li>
 * </ul>
 * </p>
 *
 * <p>
 * This package acts as a bridge between the game engine (model) and the player, ensuring smooth and interactive gameplay.
 * It is designed to be modular, allowing for potential UI upgrades (e.g., from console to GUI) without altering the core game logic.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * View view = new ConsoleView();
 * view.displayBoard(board);
 * view.showMessage("Player 1, it's your turn!");
 * }</pre>
 */
package view;
