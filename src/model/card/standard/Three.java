package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Three extends Standard {

    public Three(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 3, suit, boardManager, gameManager);
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
        boardManager.moveBy(m, 3, false);
    }
}