package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

import java.util.ArrayList;
import exception.*;
import model.card.Marble;

public class Two extends Standard {

    public Two(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 2, suit, boardManager, gameManager);
    }
    
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return validateMarbleSize(marbles)
            && marbles.get(0) != null
            && marbles.get(0).getColour() == gameManager.getActivePlayerColour();
    }
    
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (marbles == null || marbles.isEmpty()) {
            throw new InvalidMarbleException("A marble must be selected");
        }

        Marble m = marbles.get(0);
        boardManager.moveBy(m, 2, false);
    }
}