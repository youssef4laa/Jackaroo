package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

import java.util.ArrayList;
import exception.*;
import model.card.Marble;

public class Six extends Standard {

    public Six(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 6, suit, boardManager, gameManager);
    }
    
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }
    
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (marbles == null || marbles.isEmpty()) {
            throw new InvalidMarbleException("A marble must be selected");
        }

        Marble m = marbles.get(0);
        boardManager.moveBy(m, 6, false);
    }
}