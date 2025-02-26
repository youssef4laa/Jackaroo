package engine.board;

/**
 * Represents the possible types of cells on the Jackaroo game board.
 * Each cell type influences game mechanics, such as movement and safety.
 */
public enum CellType {

    /**
     * A standard cell on the board. 
     * Marbles can pass through or land on this cell without any special effect.
     */
    NORMAL,

    /**
     * A safe cell where marbles cannot be captured by opponents.
     * Provides a strategic advantage for players.
     */
    SAFE,

    /**
     * The base cell where a player's marbles start the game.
     * Marbles must be moved out of this cell to enter the main track.
     */
    BASE,

    /**
     * The entry cell where marbles enter the board from the base.
     * Typically the first cell marbles move to during gameplay.
     */
    ENTRY
}
