/**
 * The {@code engine.board} package contains the core classes responsible for 
 * managing the game board of Jackaroo. This includes the representation of 
 * individual board cells, safe zones for each player, and the board itself.
 *
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li>{@link engine.board.Board} - Manages the overall game board, including track initialization and trap cell assignment.</li>
 *   <li>{@link engine.board.Cell} - Represents individual cells on the board with different types and behaviors.</li>
 *   <li>{@link engine.board.SafeZone} - Defines the safe zones associated with player colors, offering marble protection.</li>
 *   <li>{@link engine.board.CellType} - Enumerates the possible types of cells (NORMAL, SAFE, BASE, ENTRY).</li>
 *   <li>{@link engine.board.BoardManager} - Interface defining essential methods for board management.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The package is designed with modularity and scalability in mind, supporting 
 * future enhancements such as custom board layouts or advanced cell behaviors.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * GameManager gameManager = new GameManager();
 * ArrayList<Colour> colourOrder = new ArrayList<>(Arrays.asList(Colour.RED, Colour.BLUE, Colour.GREEN, Colour.YELLOW));
 * Board board = new Board(colourOrder, gameManager);
 *
 * Cell cell = board.getTrack().get(0);
 * if (cell.isTrap()) {
 *     System.out.println("This cell is a trap!");
 * }
 * }</pre>
 *
 * @see engine.board.Board
 * @see engine.board.Cell
 * @see engine.board.SafeZone
 * @see engine.board.CellType
 * @see engine.board.BoardManager
 */
package engine.board;
