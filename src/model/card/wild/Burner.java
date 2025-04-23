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

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException, IllegalDestroyException {

        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Burner card requires selecting exactly one opponent marble.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Burner card requires selecting an opponent's marble."
            		+marbles.get(0).getColour()+" "+ gameManager.getActivePlayerColour());
        }

        Marble selectedMarble = marbles.get(0);

        boardManager.destroyMarble(selectedMarble);
    }

}
