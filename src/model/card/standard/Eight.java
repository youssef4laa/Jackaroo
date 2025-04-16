package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Eight extends Standard {

    public Eight(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 8, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }

    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (!validateMarbleSize(marbles)) {
             throw new InvalidMarbleException("Exactly one marble must be selected for the Eight card.");
        }

        Marble m = marbles.get(0);
        boardManager.moveBy(m, 8, false);
    }
}