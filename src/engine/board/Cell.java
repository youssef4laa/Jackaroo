package engine.board;

import model.player.Marble;

/**
 * Represents a cell on the Jackaroo board. A cell can hold a marble, have a
 * specific type, and may be designated as a trap.
 */
public class Cell {

	/** The marble occupying this cell, or null if the cell is empty. */
	private Marble marble;

	/** The type of this cell (e.g., NORMAL, SAFE, BASE, ENTRY). */
	private CellType cellType;

	/** Indicates whether this cell is a trap cell. */
	private boolean trap;

	/**
	 * Constructs a Cell with a specified marble, cell type, and trap status.
	 *
	 * @param marble   The marble occupying this cell, or null if the cell is empty.
	 * @param cellType The type of this cell (NORMAL, SAFE, BASE, ENTRY).
	 * @param trap     True if this cell is a trap, false otherwise.
	 */
	public Cell(Marble marble, CellType cellType, boolean trap) {
		this.marble = marble;
		this.cellType = cellType;
		this.trap = trap;
	}

	/**
	 * Retrieves the marble in this cell.
	 *
	 * @return The marble occupying this cell, or null if the cell is empty.
	 */
	public Marble getMarble() {
		return marble;
	}

	/**
	 * Sets a marble to this cell.
	 *
	 * @param marble The marble to place in this cell. Can be null to empty the
	 *               cell.
	 */
	public void setMarble(Marble marble) {
		this.marble = marble;
	}

	/**
	 * Retrieves the type of this cell.
	 *
	 * @return The cell type (e.g., NORMAL, SAFE, BASE, ENTRY).
	 */
	public CellType getCellType() {
		return cellType;
	}

	/**
	 * Sets the type of this cell.
	 *
	 * @param cellType The type to set for this cell.
	 */
	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	/**
	 * Checks if this cell is a trap cell.
	 *
	 * @return True if this cell is a trap, false otherwise.
	 */
	public boolean isTrap() {
		return trap;
	}

	/**
	 * Sets the trap status of this cell.
	 *
	 * @param trap True to mark this cell as a trap, false otherwise.
	 */
	public void setTrap(boolean trap) {
		this.trap = trap;
	}
}
