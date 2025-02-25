package engine.board;

//Class representing the Cells available on the board.
import model.player.Marble;

public class Cell {
	private Marble marble;
	private CellType cellType;
	private boolean trap;
/**
 * 
 * @param marble null or occupied by a marble.
 * @param cellType represents which type of cell this is (NORMAL, SAFE, BASE, ENTRY)
 * @param trap determines whether a cell is a trap cell or not.
 */
	public Cell(Marble marble, CellType cellType, boolean trap) {
		super();
		this.marble = marble;
		this.cellType = cellType;
		this.trap = trap;
	}

	public Marble getMarble() {
		return marble;
	}

	public void setMarble(Marble marble) {
		this.marble = marble;
	}

	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public boolean isTrap() {
		return trap;
	}

	public void setTrap(boolean trap) {
		this.trap = trap;
	}

}
