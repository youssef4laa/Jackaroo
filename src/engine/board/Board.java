package engine.board;

import java.util.ArrayList;
import exception.*;
import engine.GameManager;
import model.Colour;
import model.player.Marble;

public class Board implements BoardManager {
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private final GameManager gameManager;
	private int splitDistance;

	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.track = new ArrayList<>();
		this.safeZones = new ArrayList<>();
		this.gameManager = gameManager;

		for (int i = 0; i < 100; i++) {
			this.track.add(new Cell(CellType.NORMAL));

			if (i % 25 == 0)
				this.track.get(i).setCellType(CellType.BASE);

			else if ((i + 2) % 25 == 0)
				this.track.get(i).setCellType(CellType.ENTRY);
		}

		for (int i = 0; i < 8; i++)
			this.assignTrapCell();

		for (int i = 0; i < 4; i++)
			this.safeZones.add(new SafeZone(colourOrder.get(i)));

		splitDistance = 3;
	}

	public ArrayList<Cell> getTrack() {
		return this.track;
	}

	public ArrayList<SafeZone> getSafeZones() {
		return this.safeZones;
	}

	@Override
	public int getSplitDistance() {
		return this.splitDistance;
	}

	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}

	private ArrayList<Cell> getSafeZone(Colour colour) {
		for (SafeZone safeZone : safeZones) {
			if (safeZone.getColour() == colour) {
				return safeZone.getCells();
			}
		}
		return null;
	}

	private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
		if (path == null || marble == null)
			return -1;

		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getMarble() == marble)
				return i;
		}

		return -1;
	}

	private int getBasePosition(Colour colour) {
		if (colour == null)
			return -1;

		for (int i = 0; i < safeZones.size(); i++) {
			if (safeZones.get(i).getColour() == colour)
				return i * 25;
		}

		return -1;
	}

	private int getEntryPosition(Colour colour) {
		if (colour == null)
			return -1;

		for (int i = 0; i < safeZones.size(); i++) {
			if (safeZones.get(i).getColour() == colour) {
				int entry = (i * 25) - 2;
				if (entry < 0) entry += track.size();
				return entry;
			}
		}

		return -1;
	}

	private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
		if (marble == null || steps == 0)
			throw new IllegalMovementException("Invalid marble or steps");

		ArrayList<Cell> path = new ArrayList<>();
		ArrayList<Cell> currentPath = null;
		int pos = getPositionInPath(track, marble);

		if (pos != -1) {
			currentPath = track;
		} else {
			for (SafeZone sz : safeZones) {
				if (sz.getColour() == marble.getColour()) {
					pos = getPositionInPath(sz.getCells(), marble);
					if (pos != -1) {
						currentPath = sz.getCells();
						if (steps < 0)
							throw new IllegalMovementException("Cannot move backwards in safe zone");
						if (pos + steps >= sz.getCells().size())
							throw new IllegalMovementException("Card rank too high for remaining safe zone distance");
						path.addAll(sz.getCells().subList(pos, pos + steps + 1));
						return path;
					}
				}
			}
			throw new IllegalMovementException("Marble is not in a valid position on the board");
		}

		Colour myColour = gameManager.getActivePlayerColour();
		boolean isOpponent = marble.getColour() != myColour;
		path.add(currentPath.get(pos));
		int current = pos;

		if (currentPath == track) {
			int entry = getEntryPosition(marble.getColour());
			if (!isOpponent && entry != -1) {
				int stepsToEntry = entry >= current ? entry - current : track.size() - current + entry;
				if (steps >= stepsToEntry) {
					for (int i = 1; i <= stepsToEntry; i++) {
						current = (current + 1) % track.size();
						path.add(track.get(current));
						if (isOpponent && track.get(current).getMarble() != null
								&& track.get(current).getMarble().getColour() == myColour)
							throw new IllegalMovementException("Cannot bypass or land on own marbles with opponent");
					}
					ArrayList<Cell> safe = getSafeZone(marble.getColour());
                    int stepsInSafeZone = steps - stepsToEntry;
                    // Check if the required steps within the safe zone exceed its size.
                    if (safe == null || stepsInSafeZone > safe.size())
                        throw new IllegalMovementException("Movement exceeds safe zone length");
                    
                    // Add the required number of safe zone cells to the path.
                    // We need to add cells from index 0 up to stepsInSafeZone-1 (inclusive)
                    for (int i = 0; i < stepsInSafeZone; i++) {
                        path.add(safe.get(i));
                    }
                    
                    // Validate the path has at least the starting position and ending position
                    if (path.size() < 2) {
                        throw new IllegalMovementException("Invalid path construction");
                    }
					return path;
				}
			}

			for (int i = 1; i <= Math.abs(steps); i++) {
				current = steps > 0 ? (current + 1) % track.size() : (current - 1 + track.size()) % track.size();
				Cell target = track.get(current);
				if (isOpponent && target.getMarble() != null && target.getMarble().getColour() == myColour)
					throw new IllegalMovementException("Cannot bypass or land on own marbles with opponent");
				path.add(target);
			}
		} else {
			if (steps < 0)
				throw new IllegalMovementException("Cannot move backwards in safe zone");
			if (pos + steps >= currentPath.size())
				throw new IllegalMovementException("Card rank too high for remaining safe zone distance");
			for (int i = 1; i <= steps; i++)
				path.add(currentPath.get(pos + i));
		}

		return path;
	}

	private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy)
			throws IllegalMovementException {
		if (marble == null || fullPath == null || fullPath.isEmpty()) {
			throw new IllegalMovementException("Invalid marble or path");
		}

		Colour currentPlayerColour = gameManager.getActivePlayerColour();
		boolean isOpponentMarble = marble.getColour() != currentPlayerColour;
		SafeZone currentSafeZone = getSafeZone(fullPath.get(0));
		boolean isInSafeZone = (currentSafeZone != null);
		int blockerCount = 0;

		for (int i = 1; i < fullPath.size() - 1; i++) {
			Cell cell = fullPath.get(i);
			Marble m = cell.getMarble();
			if (m != null) {
				blockerCount++;
				if (m.getColour() == currentPlayerColour) {
					throw new IllegalMovementException("Path blocked by own marble");
				}
			}
		}

		if (blockerCount >= 2) {
			throw new IllegalMovementException("Path is blocked by two or more marbles");
		}

		Cell targetCell = fullPath.get(fullPath.size() - 1);
		Marble targetMarble = targetCell.getMarble();
		// only block landing on own marble when NOT using a King card (destroy == false)
		if (!destroy && targetMarble != null && targetMarble.getColour() == currentPlayerColour) {
		    throw new IllegalMovementException("Target cell occupied by own marble");
		}


		int entryPos = getEntryPosition(marble.getColour());
		if (entryPos != -1) {
			Cell entryCell = track.get(entryPos);
			Marble entryMarble = entryCell.getMarble();
			if (entryMarble != null && entryMarble != marble) {
				throw new IllegalMovementException("Safe Zone Entry is blocked");
			}
		}

		Cell previousCell = fullPath.get(0);
		for (int i = 1; i < fullPath.size(); i++) {
			Cell currentCell = fullPath.get(i);

			if (currentCell.getCellType() == CellType.BASE && currentCell.getMarble() != null
					&& currentCell.getMarble().getColour() != currentPlayerColour) {
				throw new IllegalMovementException("Path is blocked by opponent's marble in Base Cell");
			}

			validateMarbleInteraction(currentCell, destroy, i, fullPath.size());

			if (currentCell.getCellType() == CellType.ENTRY) {
				validateSafeZoneEntry(marble, isOpponentMarble, currentCell);
			}

			if (isInSafeZone && !currentSafeZone.getCells().contains(currentCell)) {
				throw new IllegalMovementException("Cannot exit Safe Zone");
			}

			previousCell = currentCell;
		}
	}

	private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
	    if (marble == null || fullPath == null || fullPath.isEmpty())
	        return;

	    // 1) Remove marble from its current cell
	    Cell currentCell = findCurrentCell(marble);
	    if (currentCell != null) {
	        currentCell.setMarble(null);
	    }

	    // 2) Handle any collision at the target (King vs normal)
	    Cell targetCell = fullPath.get(fullPath.size() - 1);
	    Marble occupant = targetCell.getMarble();

	    if (occupant != null) {
	        if (destroy) {
	            int pos = track.indexOf(targetCell);
	            if (pos != -1) validateDestroy(pos);
	            targetCell.setMarble(null);
	        } else {
	            throw new IllegalDestroyException("Cannot move to occupied cell without King card");
	        }
	    }

	    // 3) Place our marble on the target
	    targetCell.setMarble(marble);

	    // 4) If it's a trap: first assign the NEW trap (old trap still active),
	    //    then deactivate this one, then destroy the marble
	    if (targetCell.isTrap()) {
	        assignTrapCell();          // picks a Normal non‑trap cell (old trap still true)
	        targetCell.setTrap(false); // now retire the old trap
	        destroyMarble(marble);     // send marble home
	    }
	}


	private void validateSwap(Marble marble1, Marble marble2) throws IllegalSwapException {
		if (marble1 == null || marble2 == null) {
			throw new IllegalSwapException("Cannot swap with null marble");
		}

		int pos1 = getPositionInPath(track, marble1);
		int pos2 = getPositionInPath(track, marble2);

		if (pos1 == -1 || pos2 == -1) {
			throw new IllegalSwapException("Both marbles must be on the track");
		}

		if (isOpponentMarbleSafeInBase(marble1) || isOpponentMarbleSafeInBase(marble2)) {
			throw new IllegalSwapException("Cannot swap with an opponent's marble that is safe in its base cell");
		}
	}

	private void validateDestroy(int positionInPath) throws IllegalDestroyException {
		if (positionInPath < 0 || positionInPath >= track.size()) {
			throw new IllegalDestroyException("Invalid position on track");
		}

		Cell cell = track.get(positionInPath);
		Marble marble = cell.getMarble();

		if (marble == null) {
			throw new IllegalDestroyException("No marble at the specified position");
		}

		if (cell.getCellType() == CellType.BASE && isOpponentMarbleSafeInBase(marble)) {
			throw new IllegalDestroyException("Cannot destroy a marble that is safe in its base cell");
		}
	}

	private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
		if (occupiedBaseCell == null || occupiedBaseCell.getCellType() != CellType.BASE) {
			throw new CannotFieldException("Invalid base cell");
		}

		Marble existingMarble = occupiedBaseCell.getMarble();
		if (existingMarble != null && existingMarble.getColour() == gameManager.getActivePlayerColour()) {
			throw new CannotFieldException("Base cell already occupied by a marble of the same color");
		}
	}

	private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
		if (positionOnTrack < 0 || positionOnTrack >= track.size()) {
			throw new InvalidMarbleException("Invalid position on track: " + positionOnTrack);
		}

		Cell trackCell = track.get(positionOnTrack);
		Marble marble = trackCell.getMarble();

		if (marble == null || marble.getColour() != gameManager.getActivePlayerColour()) {
			throw new InvalidMarbleException("Invalid marble at the specified position");
		}

		for (SafeZone safeZone : safeZones) {
			if (safeZone.getCells().stream().anyMatch(cell -> cell.getMarble() == marble)) {
				throw new InvalidMarbleException("Marble is already in a safe zone");
			}
		}

		if (positionInSafeZone < 0 || positionInSafeZone >= getSafeZoneSize()) {
			throw new InvalidMarbleException("Invalid position in safe zone: " + positionInSafeZone);
		}
	}

	public void moveBy(Marble marble, int steps, boolean destroy)
			throws IllegalMovementException, IllegalDestroyException {
		if (marble == null) {
			throw new IllegalMovementException("Cannot move null marble");
		}

		ArrayList<Cell> path = validateSteps(marble, steps);
		validatePath(marble, path, destroy);
		move(marble, path, destroy);
	}

	public void swap(Marble marble1, Marble marble2) throws IllegalSwapException {
		validateSwap(marble1, marble2);

		int pos1 = getPositionInPath(track, marble1);
		int pos2 = getPositionInPath(track, marble2);

		if (pos1 == -1 || pos2 == -1) {
			throw new IllegalSwapException("Marbles must be on the track");
		}

		Cell cell1 = track.get(pos1);
		Cell cell2 = track.get(pos2);

		cell1.setMarble(marble2);
		cell2.setMarble(marble1);
	}

	public void destroyMarble(Marble marble) throws IllegalDestroyException {
		if (marble == null) {
			throw new IllegalDestroyException("Cannot destroy null marble");
		}

		int position = -1;
		for (int i = 0; i < track.size(); i++) {
			if (track.get(i).getMarble() == marble) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			for (SafeZone safeZone : safeZones) {
				for (Cell cell : safeZone.getCells()) {
					if (cell.getMarble() == marble) {
						cell.setMarble(null);
						gameManager.sendHome(marble);
						return;
					}
				}
			}
			throw new IllegalDestroyException("Marble not found on board");
		}

		if (marble.getColour() != gameManager.getActivePlayerColour()) {
			validateDestroy(position);
		}

		track.get(position).setMarble(null);
		gameManager.sendHome(marble);
	}

	public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
		if (marble == null) {
			throw new CannotFieldException("Cannot field null marble");
		}

		int basePosition = getBasePosition(marble.getColour());
		if (basePosition == -1) {
			throw new CannotFieldException("Invalid marble color");
		}

		Cell baseCell = track.get(basePosition);

		if (baseCell.getMarble() != null) {
			validateFielding(baseCell);
			destroyMarble(baseCell.getMarble());
		}

		baseCell.setMarble(marble);
	}

	public void sendToSafe(Marble marble) throws InvalidMarbleException {
		if (marble == null) {
			throw new InvalidMarbleException("Cannot send null marble to safe zone");
		}

		int positionOnTrack = getPositionInPath(track, marble);
		if (positionOnTrack == -1 || marble.getColour() != gameManager.getActivePlayerColour()) {
			throw new InvalidMarbleException("Marble not found or does not belong to active player");
		}

		ArrayList<Cell> targetSafeZone = null;
		for (SafeZone safeZone : safeZones) {
			if (safeZone.getColour() == marble.getColour()) {
				targetSafeZone = safeZone.getCells();
				break;
			}
		}

		if (targetSafeZone == null) {
			throw new InvalidMarbleException("No matching safe zone found for marble");
		}

		ArrayList<Integer> unoccupiedPositions = new ArrayList<>();
		for (int i = 0; i < targetSafeZone.size(); i++) {
			if (targetSafeZone.get(i).getMarble() == null) {
				unoccupiedPositions.add(i);
			}
		}

		if (unoccupiedPositions.isEmpty()) {
			throw new InvalidMarbleException("No available positions in safe zone");
		}

		int randomIndex = (int) (Math.random() * unoccupiedPositions.size());
		int targetPosition = unoccupiedPositions.get(randomIndex);

		validateSaving(targetPosition, positionOnTrack);

		track.get(positionOnTrack).setMarble(null);
		targetSafeZone.get(targetPosition).setMarble(marble);
	}

	private void assignTrapCell() {
	    int randIndex;
	    do {
	        randIndex = (int)(Math.random()*track.size());
	    } while (track.get(randIndex).getCellType()!=CellType.NORMAL
	          || track.get(randIndex).isTrap());
	    track.get(randIndex).setTrap(true);
	}


	private SafeZone getSafeZone(Cell cell) {
		for (SafeZone safeZone : safeZones) {
			if (safeZone.getCells().contains(cell)) {
				return safeZone;
			}
		}
		return null;
	}

	public ArrayList<Marble> getActionableMarbles() {
		ArrayList<Marble> actionableMarbles = new ArrayList<>();
		Colour currentPlayerColour = gameManager.getActivePlayerColour();

		for (Cell cell : track) {
			Marble marble = cell.getMarble();
			if (marble != null && marble.getColour() == currentPlayerColour) {
				actionableMarbles.add(marble);
			}
		}

		for (SafeZone safeZone : safeZones) {
			if (safeZone.getColour() == currentPlayerColour) {
				for (Cell cell : safeZone.getCells()) {
					Marble marble = cell.getMarble();
					if (marble != null) {
						actionableMarbles.add(marble);
					}
				}
				break;
			}
		}

		return actionableMarbles;
	}

	private boolean isOpponentMarbleSafeInBase(Marble marble) {
		Colour activeColour = gameManager.getActivePlayerColour();
		if (marble.getColour() == activeColour)
			return false;

		int basePosition = getBasePosition(marble.getColour());
		if (basePosition == -1)
			return false;

		Cell baseCell = track.get(basePosition);
		return baseCell.getMarble() == marble;
	}

	private void validateMarbleInteraction(Cell currentCell, boolean destroy, int index, int pathSize)
			throws IllegalMovementException {
		Marble targetMarble = currentCell.getMarble();
		if (targetMarble != null) {
			Colour currentPlayerColour = gameManager.getActivePlayerColour();
			if (targetMarble.getColour() == currentPlayerColour) {
				throw new IllegalMovementException("Cannot bypass or land on own marbles");
			}

			if (isCellInSafeZone(currentCell)) {
				throw new IllegalMovementException("Cannot interact with marbles in Safe Zone");
			}

			if (!destroy && index < pathSize - 1) {
				throw new IllegalMovementException("Cannot bypass opponent's marble without King card");
			}
		}
	}

	private void validateSafeZoneEntry(Marble marble, boolean isOpponentMarble, Cell currentCell)
			throws IllegalMovementException {
		if (isOpponentMarble) {
			throw new IllegalMovementException("Cannot move opponent's marble into Safe Zone");
		}

		Colour marbleColour = marble.getColour();
		int entryPosition = getEntryPosition(marbleColour);
		if (entryPosition == -1) {
			throw new IllegalMovementException("Invalid Safe Zone entry position");
		}

		Cell entryCell = track.get(entryPosition);
		Marble blockingMarble = entryCell.getMarble();
		if (blockingMarble != null && blockingMarble != marble) {
			throw new IllegalMovementException("Safe Zone Entry is blocked");
		}

		boolean hasMatchingSafeZone = false;
		for (SafeZone safeZone : safeZones) {
			if (safeZone.getColour() == marbleColour) {
				hasMatchingSafeZone = true;
				break;
			}
		}

		if (!hasMatchingSafeZone) {
			throw new IllegalMovementException("Invalid Safe Zone entry point");
		}
	}

	private boolean isCellInSafeZone(Cell cell) {
		for (SafeZone safeZone : safeZones) {
			if (safeZone.getCells().contains(cell)) {
				return true;
			}
		}
		return false;
	}

	private int getSafeZoneSize() {
		return safeZones.isEmpty() ? 0 : safeZones.get(0).getCells().size();
	}

	private Cell findCurrentCell(Marble marble) {
		for (Cell cell : track) {
			if (cell.getMarble() == marble)
				return cell;
		}
		for (SafeZone safeZone : safeZones) {
			for (Cell cell : safeZone.getCells()) {
				if (cell.getMarble() == marble)
					return cell;
			}
		}
		return null;
	}

}
