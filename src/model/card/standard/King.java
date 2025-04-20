package model.card.standard;

import engine.GameManager;
import exception.*;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
public class King extends Standard {

    public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 13, suit, boardManager, gameManager);
    }

   
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        int s = marbles == null ? 0 : marbles.size();
        return s == 0 || s == 1;
    }
    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        // No marbles selected → field a new one
        if (marbles == null || marbles.isEmpty()) {
            gameManager.fieldMarble();
            return;
        }

        // Take the one marble and move 13 steps, destroying along the way
        Marble m = marbles.get(0);
        boardManager.moveBy(m, 13, /* destroy = */ true);
    }

}
