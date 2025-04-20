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
        // Must be an opponent’s marble
        return validateMarbleSize(marbles)
            && marbles.get(0).getColour() != gameManager.getActivePlayerColour();
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Exactly one marble must be selected");
        }
        Marble m = marbles.get(0);
        if (m.getColour() == gameManager.getActivePlayerColour()) {
            throw new InvalidMarbleException("You must select an opponent's marble");
        }
        // move that marble forward by 5 steps, never destroying its own
        boardManager.moveBy(m, getRank(), false);
    }
}
