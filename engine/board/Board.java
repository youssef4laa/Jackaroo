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

		// Modified check: Only block Base cell movement when not using a King card
		if (currentCell.getCellType() == CellType.BASE 
			&& currentCell.getMarble() != null
			&& currentCell.getMarble().getColour() != currentPlayerColour 
			&& !destroy) {
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