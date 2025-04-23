package engine.board;

import model.player.Marble;

public class Cell {
    private Marble marble;
    private CellType cellType;
    private boolean trap;

    public Cell(CellType cellType) {
        this.cellType = cellType;
        this.marble = null;
        this.trap = false;
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
	public boolean isBaseCell() {
	    return cellType == CellType.BASE;
	}

	public boolean isSafeZone() {
	    return cellType == CellType.SAFE;
	}

	public boolean isSafeZoneEntry() {
	    return cellType == CellType.ENTRY;
	}

	public boolean isOccupied() {
	    return marble != null;
	}

	public boolean hasSamePlayerMarble(Marble marble) {
	    return this.marble != null && this.marble.getColour().equals(marble.getColour());
	}

	public boolean hasOpponentMarble(Marble marble) {
	    return this.marble != null && !this.marble.getColour().equals(marble.getColour());
	}

	

}
