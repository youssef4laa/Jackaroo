package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Nine extends Standard {

    public Nine(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 9, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }

    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (!validateMarbleSize(marbles)) {
             throw new InvalidMarbleException("Exactly one marble must be selected for the Nine card.");
        }

        Marble m = marbles.get(0);
        boardManager.moveBy(m, 9, false);
    }
}