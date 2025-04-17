package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;


public class Ace extends Standard {

    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 1, suit, boardManager, gameManager);
    }

    @Override
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
        boardManager.moveBy(m, 1, false);
    }




}
