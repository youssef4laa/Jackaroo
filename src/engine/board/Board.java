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
            
            else if ((i+2) % 25 == 0) 
                this.track.get(i).setCellType(CellType.ENTRY);
        }

        for(int i = 0; i < 8; i++)
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

    private ArrayList<Cell> getSafeZone(Colour colour) {
        for (SafeZone safeZone : safeZones) {
            if (safeZone.getColour() == colour) {
                return safeZone.getCells();
            }
        }
        return null;
    }

    public void setSplitDistance(int splitDistance) {
        this.splitDistance = splitDistance;
    }
   
    private void assignTrapCell() {
        int randIndex = -1;
        
        do
            randIndex = (int)(Math.random() * 100); 
        while(this.track.get(randIndex).getCellType() != CellType.NORMAL || this.track.get(randIndex).isTrap());
        
        this.track.get(randIndex).setTrap(true);
    }

    private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalMovementException {
        if (marble == null || fullPath == null || fullPath.isEmpty()) {
            throw new IllegalMovementException("Invalid marble or path");
        }
    
        Colour currentPlayerColour = gameManager.getActivePlayerColour();
        boolean isOpponentMarble = marble.getColour() != currentPlayerColour;
    
        // Check if the starting cell is in a safe zone
        SafeZone currentSafeZone = getSafeZone(fullPath.get(0));
        boolean isInSafeZone = (currentSafeZone != null);
    
        Cell previousCell = fullPath.get(0);
        for (int i = 1; i < fullPath.size(); i++) {
            Cell currentCell = fullPath.get(i);
    
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
    
    // Helper method to get the safe zone of a given cell
    private SafeZone getSafeZone(Cell cell) {
        for (SafeZone safeZone : safeZones) {
            if (safeZone.getCells().contains(cell)) {
                return safeZone;
            }
        }
        return null;
    }
    
    // Helper method to validate marble interaction in the path
    private void validateMarbleInteraction(Cell currentCell, boolean destroy, int index, int pathSize) throws IllegalMovementException {
        Marble targetMarble = currentCell.getMarble();
        if (targetMarble != null) {
            Colour currentPlayerColour = gameManager.getActivePlayerColour();
            // Cannot bypass or land on own marbles
            if (targetMarble.getColour() == currentPlayerColour) {
                throw new IllegalMovementException("Cannot bypass or land on own marbles");
            }
    
            // Check Safe Zone protection
            if (isCellInSafeZone(currentCell)) {
                throw new IllegalMovementException("Cannot interact with marbles in Safe Zone");
            }
    
            // Handle non-King card interactions
            if (!destroy && index < pathSize - 1) {
                throw new IllegalMovementException("Cannot bypass opponent's marble without King card");
            }
        }
    }
    
    // Helper method to validate safe zone entry
    private void validateSafeZoneEntry(Marble marble, boolean isOpponentMarble, Cell currentCell) throws IllegalMovementException {
        if (isOpponentMarble) {
            throw new IllegalMovementException("Cannot move opponent's marble into Safe Zone");
        }
    
        boolean validEntry = false;
        for (SafeZone safeZone : safeZones) {
            if (safeZone.getColour() == marble.getColour()) {
                validEntry = true;
                break;
            }
        }
        if (!validEntry) {
            throw new IllegalMovementException("Invalid Safe Zone entry point");
        }
    }
    
    // Helper method to check if a cell is in any Safe Zone
    private boolean isCellInSafeZone(Cell cell) {
        for (SafeZone safeZone : safeZones) {
            if (safeZone.getCells().contains(cell)) {
                return true;
            }
        }
        return false;
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
            if (safeZones.get(i).getColour() == colour)
                return (i * 25) + 23;
        }
        
        return -1;
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

    @Override
    public ArrayList<Marble> getActionableMarbles() {
        ArrayList<Marble> actionableMarbles = new ArrayList<>();
        Colour currentPlayerColour = gameManager.getActivePlayerColour();

        // Check marbles on the main track
        for (Cell cell : track) {
            Marble marble = cell.getMarble();
            if (marble != null && marble.getColour() == currentPlayerColour) {
                actionableMarbles.add(marble);
            }
        }

        // Check marbles in the current player's safe zone
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
    private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
        // Check if the position on track is valid
        if (positionOnTrack < 0 || positionOnTrack >= track.size()) {
            throw new InvalidMarbleException("Invalid position on track: " + positionOnTrack);
        }
    
        Cell trackCell = track.get(positionOnTrack);
        Marble marble = trackCell.getMarble();
    
        // Check if there is a marble at the specified position
        if (marble == null) {
            throw new InvalidMarbleException("No marble at the specified position on track: " + positionOnTrack);
        }
    
        // Check if marble is already in any safe zone
        for (SafeZone safeZone : safeZones) {
            if (safeZone.getCells().stream().anyMatch(cell -> cell.getMarble() == marble)) {
                throw new InvalidMarbleException("Marble is already in a safe zone");
            }
        }
    
        // Validate position in safe zone
        if (positionInSafeZone < 0 || positionInSafeZone >= getSafeZoneSize()) {
            throw new InvalidMarbleException("Invalid position in safe zone: " + positionInSafeZone);
        }
    }
    
    // Helper method to get the size of the safe zone (assuming uniform size for all safe zones)
    private int getSafeZoneSize() {
        return safeZones.isEmpty() ? 0 : safeZones.get(0).getCells().size();
    }
    
    
    private void validateSwap(Marble marble1, Marble marble2) throws IllegalSwapException {
        if (marble1 == null || marble2 == null) {
            throw new IllegalSwapException("Cannot swap with null marble");
        }
    
        // Check if both marbles are on the main track
        int pos1 = getPositionInPath(track, marble1);
        int pos2 = getPositionInPath(track, marble2);
    
        if (pos1 == -1 || pos2 == -1) {
            throw new IllegalSwapException("Both marbles must be on the track");
        }
    
        // Check if the opponent's marble is in a safe base cell
        if (isOpponentMarbleSafeInBase(marble1) || isOpponentMarbleSafeInBase(marble2)) {
            throw new IllegalSwapException("Cannot swap with an opponent's marble that is safe in its base cell");
        }
    }
    
    // Helper method to check if a marble is an opponent's safe marble in a base cell
    private boolean isOpponentMarbleSafeInBase(Marble marble) {
        return marble.isOpponentMarble() && marble.isInBase() && marble.isSafe();
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
        
        // Check if the marble is in a base cell and is safe
        if (cell.getCellType() == CellType.BASE && isOpponentMarbleSafeInBase(marble)) {
            throw new IllegalDestroyException("Cannot destroy a marble that is safe in its base cell");
        }
    }

    public void destroyMarble(Marble marble) throws IllegalDestroyException {
        if (marble == null) {
            throw new IllegalDestroyException("Cannot destroy null marble");
        }

        // Find the marble's position on the track
        int position = -1;
        for (int i = 0; i < track.size(); i++) {
            if (track.get(i).getMarble() == marble) {
                position = i;
                break;
            }
        }

        // If marble is not on track, check safe zones
        if (position == -1) {
            for (SafeZone safeZone : safeZones) {
                for (Cell cell : safeZone.getCells()) {
                    if (cell.getMarble() == marble) {
                        // Remove marble from safe zone
                        cell.setMarble(null);
                        return;
                    }
                }
            }
            throw new IllegalDestroyException("Marble not found on board");
        }

        // For opponent's marble, validate the destruction
        if (marble.getColour() != gameManager.getActivePlayerColour()) {
            validateDestroy(position);
        }

        // Remove the marble from its current position
        track.get(position).setMarble(null);
    }
    
    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        if (marble == null || steps == 0) {
            throw new IllegalMovementException("Invalid marble or steps");
        }

        ArrayList<Cell> path = new ArrayList<>();
        ArrayList<Cell> currentPath = null;
        int currentPosition = getPositionInPath(track, marble);

        // Check if marble is on main track
        if (currentPosition != -1) {
            currentPath = track;
            // Add the starting position to the path
            path.add(track.get(currentPosition));
        } else {
            // Check if marble is in its corresponding safe zone
            boolean foundInSafeZone = false;
            for (SafeZone safeZone : safeZones) {
                if (safeZone.getColour() == marble.getColour()) {
                    currentPosition = getPositionInPath(safeZone.getCells(), marble);
                    if (currentPosition != -1) {
                        currentPath = safeZone.getCells();
                        foundInSafeZone = true;
                        // Add the starting position to the path
                        path.add(safeZone.getCells().get(currentPosition));
                        if (steps < 0) {
                            throw new IllegalMovementException("Cannot move backwards in safe zone");
                        }
                        // Check if the move would exceed the safe zone's length
                        if (currentPosition + steps >= safeZone.getCells().size()) {
                            throw new IllegalMovementException("Card rank too high for remaining safe zone distance");
                        }
                        break;
                    }
                }
            }
            
            if (!foundInSafeZone && currentPosition == -1) {
                throw new IllegalMovementException("Marble is not in a valid position on the board");
            }
        }

        if (currentPosition == -1 || currentPath == null) {
            throw new IllegalMovementException("Marble not found on board");
        }

        Colour myColour = gameManager.getCurrentPlayer().getColour();
        boolean isOpponentMarble = marble.getColour() != myColour;

        int targetPosition = currentPosition;
        int remainingSteps = Math.abs(steps);

        // Calculate maximum allowed steps based on current position
        if (currentPath == track) {
            int distanceToEnd = track.size() - currentPosition;
            if (steps > 0) {
                // Check if movement would exceed track length
                if (steps > track.size()) {
                    throw new IllegalMovementException("Movement exceeds track length");
                }

                // Calculate distance to safe zone entry
                int entryPos = getEntryPosition(marble.getColour());
                if (entryPos != -1 && !isOpponentMarble) {
                    int stepsToEntry;
                    if (entryPos > currentPosition) {
                        stepsToEntry = entryPos - currentPosition;
                    } else {
                        stepsToEntry = (track.size() - currentPosition) + entryPos;
                    }

                    // Check if marble can enter safe zone
                    if (steps >= stepsToEntry) {
                        // Add path to entry point
                        for (int i = 1; i < stepsToEntry; i++) {
                            targetPosition++;
                            if (targetPosition >= track.size()) {
                                targetPosition = 0;
                            }
                            path.add(track.get(targetPosition));
                        }

                        // Add the entry point
                        targetPosition++;
                        if (targetPosition >= track.size()) {
                            targetPosition = 0;
                        }
                        path.add(track.get(targetPosition));

                        // Transition to safe zone
                        ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
                        if (safeZone != null) {
                            int remainingStepsInSafeZone = steps - stepsToEntry;
                            if (remainingStepsInSafeZone >= safeZone.size()) {
                                throw new IllegalMovementException("Movement exceeds safe zone length");
                            }
                            for (int i = 0; i < remainingStepsInSafeZone; i++) {
                                path.add(safeZone.get(i));
                            }
                            return path;
                        }
                    }
                }

                // Regular forward movement on track
                for (int i = 1; i <= remainingSteps; i++) {
                    targetPosition++;
                    if (targetPosition >= track.size()) {
                        targetPosition = 0;
                    }
                    
                    // Validate opponent marble movement
                    if (isOpponentMarble) {
                        Cell targetCell = track.get(targetPosition);
                        if (targetCell.getMarble() != null && 
                            targetCell.getMarble().getColour() == myColour) {
                            throw new IllegalMovementException("Cannot bypass or land on own marbles when moving opponent's marble");
                        }
                    }
                    
                    path.add(track.get(targetPosition));
                }
            } else { // Backward movement
                for (int i = 1; i <= remainingSteps; i++) {
                    targetPosition--;
                    if (targetPosition < 0) {
                        targetPosition = track.size() - 1;
                    }
                    path.add(track.get(targetPosition));
                }
            }
        } else { // Movement in safe zone
            if (steps < 0) {
                throw new IllegalMovementException("Cannot move backwards in safe zone");
            }
            if (steps > currentPath.size() - currentPosition - 1) {
                throw new IllegalMovementException("Card rank too high for remaining safe zone distance");
            }
            for (int i = 1; i <= steps; i++) {
                targetPosition++;
                path.add(currentPath.get(targetPosition));
            }
        }

        return path;
    }
    
    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
        if (occupiedBaseCell == null || occupiedBaseCell.getCellType() != CellType.BASE) {
            throw new CannotFieldException("Invalid base cell");
        }

        Marble existingMarble = occupiedBaseCell.getMarble();
        if (existingMarble != null && existingMarble.getColour() == gameManager.getCurrentPlayer().getColour()) {
            throw new CannotFieldException("Base cell already occupied by a marble of the same color");
        }
    }



public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
    if (marble == null) {
        throw new IllegalMovementException("Cannot move null marble");
    }

    // Step 1: Validate movement and get the path
    ArrayList<Cell> path = validateSteps(marble, steps);
    validatePath(marble, path, destroy);

    // Step 2: Perform the movement logic
    move(marble, path, destroy);
}

// New move(...) method separated for clarity and modularity
private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
    if (marble == null || fullPath == null || fullPath.isEmpty()) {
        return;
    }

    // Locate and remove from current position
    Cell currentCell = findCurrentCell(marble);
    if (currentCell != null) {
        currentCell.setMarble(null);
    }

    // Get target cell
    Cell targetCell = fullPath.get(fullPath.size() - 1);
    Marble targetMarble = targetCell.getMarble();

    if (targetMarble != null) {
        if (destroy) {
            int trackPos = track.indexOf(targetCell);
            if (trackPos != -1) {
                validateDestroy(trackPos);
            }
            targetCell.setMarble(null);
        } else {
            throw new IllegalMovementException("Cannot move to occupied cell without King card");
        }
    }

    // Place marble in new cell
    targetCell.setMarble(marble);

    // Handle trap if any
    if (targetCell.isTrap()) {
        targetCell.setTrap(false);
        destroyMarble(marble);
        assignTrapCell();
    }
}

// Helper method to find the current cell of a marble
private Cell findCurrentCell(Marble marble) {
    for (Cell cell : track) {
        if (cell.getMarble() == marble) return cell;
    }
    for (SafeZone safeZone : safeZones) {
        for (Cell cell : safeZone.getCells()) {
            if (cell.getMarble() == marble) return cell;
        }
    }
    return null;
}

// [Rest of your Board class remains unchanged...]
public void swap(Marble marble1, Marble marble2) throws IllegalSwapException {
    // Validate the swap using the helper method
    validateSwap(marble1, marble2);

    // Find the positions of the marbles on the track
    int pos1 = getPositionInPath(track, marble1);
    int pos2 = getPositionInPath(track, marble2);

    // Swap marbles on the track
    if (pos1 != -1 && pos2 != -1) {
        Cell cell1 = track.get(pos1);
        Cell cell2 = track.get(pos2);

        // Swap the marbles
        cell1.setMarble(marble2);
        cell2.setMarble(marble1);
    } else {
        // Check safe zones for marble1
        for (SafeZone safeZone : safeZones) {
            int safePos1 = getPositionInPath(safeZone.getCells(), marble1);
            int safePos2 = getPositionInPath(safeZone.getCells(), marble2);

            if (safePos1 != -1 && safePos2 != -1) {
                Cell cell1 = safeZone.getCells().get(safePos1);
                Cell cell2 = safeZone.getCells().get(safePos2);

                // Swap the marbles in the safe zone
                cell1.setMarble(marble2);
                cell2.setMarble(marble1);
                return;
            }
        }
        throw new IllegalSwapException("Marbles are not in a valid position to swap");
    }
}




public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
    if (marble == null) {
        throw new CannotFieldException("Cannot field null marble");
    }

    // Get the base position for the marble's color
    int basePosition = getBasePosition(marble.getColour());
    if (basePosition == -1) {
        throw new CannotFieldException("Invalid marble color");
    }

    Cell baseCell = track.get(basePosition);
    
    // Validate fielding if base is occupied
    if (baseCell.getMarble() != null) {
        validateFielding(baseCell);
        // Destroy the opponent's marble if present
        destroyMarble(baseCell.getMarble());
    }

    // Place the marble in its base cell
    baseCell.setMarble(marble);
}



public void sendToSafe(Marble marble) throws InvalidMarbleException {
    if (marble == null) {
        throw new InvalidMarbleException("Cannot send null marble to safe zone");
    }

    // Find marble's current position on track
    int positionOnTrack = getPositionInPath(track, marble);
    if (positionOnTrack == -1) {
        throw new InvalidMarbleException("Marble not found on track");
    }

    // Find corresponding safe zone
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

    // Find unoccupied cells in safe zone
    ArrayList<Integer> unoccupiedPositions = new ArrayList<>();
    for (int i = 0; i < targetSafeZone.size(); i++) {
        if (targetSafeZone.get(i).getMarble() == null) {
            unoccupiedPositions.add(i);
        }
    }

    if (unoccupiedPositions.isEmpty()) {
        throw new InvalidMarbleException("No available positions in safe zone");
    }

    // Select random unoccupied position
    int randomIndex = (int)(Math.random() * unoccupiedPositions.size());
    int targetPosition = unoccupiedPositions.get(randomIndex);

    // Validate the saving action
    validateSaving(targetPosition, positionOnTrack);

    // Move marble to safe zone
    track.get(positionOnTrack).setMarble(null);
    targetSafeZone.get(targetPosition).setMarble(marble);
}
}

