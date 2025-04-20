package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import model.Colour;
import exception.*;

public class Jack extends Standard {

    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 11, suit, boardManager, gameManager);
    }

    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        int n = (marbles == null ? 0 : marbles.size());
        return n == 1 || n == 2;
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.size() != 2) return false;
        Colour me = gameManager.getActivePlayerColour();
        boolean own = false, opp = false;
        for (Marble m : marbles) {
            if (m == null) return false;
            if (m.getColour() == me) {
                if (own) return false;
                own = true;
            } else {
                if (opp) return false;
                opp = true;
            }
        }
        return own && opp;
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException, IllegalMovementException, IllegalSwapException {

        // Reject 0 or >2 marbles
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException(
                "Jack requires exactly one marble to move or two to swap."
            );
        }

        if (marbles.size() == 1) {
            // Move that one marble 11 steps, no destroy
            boardManager.moveBy(marbles.get(0), 11, /* destroy= */ false);
        } else {
            // Swap the two marbles
            boardManager.swap(marbles.get(0), marbles.get(1));
        }
    }

}
