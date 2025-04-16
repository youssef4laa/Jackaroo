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

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 2;
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
            throws ActionException, InvalidMarbleException, IllegalSwapException {

        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Exactly two marbles must be selected for the Jack card.");
        }

        boardManager.swap(marbles.get(0), marbles.get(1));
    }

}
