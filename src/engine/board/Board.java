package engine.board;
import java.util.ArrayList;
import engine.GameManager;
public class Board {
	private final GameManager gameManager;
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private int splitDistance;
	
	Board(ArrayList<Colour> colourOrder, GameManager gameManager){
		this.gameManager = gameManager;
		track = new ArrayList<>();
		safeZones = new ArrayList<>();
		splitDistance = 3;
		//constructor stuck at 4.
	}
	private void assignTrapCell() {
		/* void assignTrapCell() should randomize a cell position on the track and flag it as a trap cell.
		The position must be a NORMAL cell and not flagged as trap already. */
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
