package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;
public class Saver extends Wild {

    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return marbles != null
            && marbles.size() == 1
            && marbles.get(0) != null
            && marbles.get(0).getColour() == gameManager.getActivePlayerColour();
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Saver card requires selecting exactly one of your own marbles.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Saver card requires selecting one of your own marbles.");
        }
        Marble selectedMarble = marbles.get(0);
        boardManager.sendToSafe(selectedMarble);
    }

}
