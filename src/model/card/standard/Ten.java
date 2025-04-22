package model.card.standard;

import model.Colour;
import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Ten extends Standard {

    public Ten(String name, String description, Suit suit,
               BoardManager boardManager, GameManager gameManager) {
        super(name, description, 10, suit, boardManager, gameManager);
    }

    // Allow zero (discard) or exactly one marble (move)
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles == null
            || marbles.isEmpty()
            || marbles.size() == 1;
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        // ---- exactly one marble: move it 10 steps ----
        if (marbles != null && marbles.size() == 1) {
            Marble m = marbles.get(0);
            // Milestone 2 § 2.2.1–12: use BoardManager.moveBy(marble, steps, destroy)
            // destroy=false because Ten is not the King card
            boardManager.moveBy(m, 10, false);
            return;
        }

        // ---- zero marbles: discard to next player ----
        if (marbles == null || marbles.isEmpty()) {
            Colour next = gameManager.getNextPlayerColour();
            gameManager.discardCard(next);
            return;
        }

        // ---- any other case (more than one marble) is invalid ----
        throw new InvalidMarbleException(
            "Exactly one marble may be selected for the Ten card.");
    }
}
