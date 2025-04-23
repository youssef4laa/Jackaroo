package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Five extends Standard {

    public Five(String name, String description, Suit suit,
                BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return validateMarbleSize(marbles)
            && marbles.get(0) != null;
    }



    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles) || marbles.get(0) == null) {
            throw new InvalidMarbleException("Exactly one valid marble must be selected");
        }

        Marble m = marbles.get(0);

        // Validate it belongs to the active player
        if (m.getColour() != gameManager.getActivePlayerColour()) {
            throw new InvalidMarbleException("You must select your own marble");
        }

        // Move forward by 5 steps without destroying
        boardManager.moveBy(m, getRank(), false);
    }
}

