package engine.board;
import java.util.ArrayList;
import java.util.List;
import model.card.Marble;

public class Firepit {

    private List<Marble> capturedMarbles;

    public Firepit() {
        this.capturedMarbles = new ArrayList<>();
    }

    /**
     * Adds a piece to the firepit (i.e., when it's captured).
     *
     * @param piece the piece to be added
     */
    public void addPiece(Marble marble) {
        if (marble != null && !capturedMarbles.contains(marble)) {
            capturedMarbles.add(marble);
            marble.setInFirepit(true); // Optional: if Piece has this flag
        }
    }

    /**
     * Removes a piece from the firepit (e.g., when it's re-entering the game).
     *
     * @param piece the piece to remove
     * @return true if the piece was in the firepit and was removed
     */
    public boolean removePiece(Marble marble) {
        if (marble != null && capturedMarbles.contains(marble)) {
            capturedMarbles.remove(marble);
            marble.setInFirepit(false); // Optional: if Piece has this flag
            return true;
        }
        return false;
    }

    /**
     * Gets the list of all pieces currently in the firepit.
     *
     * @return list of captured pieces
     */
    public List<Marble> getCapturedPieces() {
        return new ArrayList<>(capturedMarbles); // Return a copy for safety
    }

    /**
     * Clears the firepit (useful for resetting the game).
     */
    public void clear() {
        for (Marble marble : capturedMarbles) {
            marble.setInFirepit(false); // Reset status
        }
        capturedMarbles.clear();
    }

    /**
     * Checks if the firepit is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return capturedMarbles.isEmpty();
    }

    /**
     * Gets the number of pieces in the firepit.
     *
     * @return size of the firepit
     */
    public int size() {
        return capturedMarbles.size();
    }
}


