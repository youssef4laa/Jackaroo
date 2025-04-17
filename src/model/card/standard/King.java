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
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (marbles == null || marbles.isEmpty()) {
            gameManager.fieldMarble();
            return;
        }

        Marble m = marbles.get(0);
        boardManager.moveBy(m, 13, true); 
    }

}
