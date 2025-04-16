package model.card.standard;

import engine.GameManager;
import model.Colour;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.ActionException;
import exception.InvalidMarbleException;
public class Five extends Standard {

    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return marbles != null
            && marbles.size() == 1
            && marbles.get(0) != null
            && marbles.get(0).getColour() != gameManager.getActivePlayerColour();
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
        // Ensure it's an opponent's marble (already validated by validateMarbleColours)
        if (m.getColour() == gameManager.getActivePlayerColour()) {
             throw new InvalidMarbleException("You must select an opponent's marble");
        }

        boardManager.moveBy(m, 5, false);
    }
}
