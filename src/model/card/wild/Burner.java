package model.card.wild;

import engine.GameManager;
import model.Colour;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Burner extends Wild {

    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return marbles.get(0) != null
            && marbles.get(0).getColour() != gameManager.getActivePlayerColour();
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        gameManager.sendHome(marbles.get(0));
    }

}
