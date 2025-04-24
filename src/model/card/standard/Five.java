package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Five extends Standard {

    public Five(String name, String description, Suit suit,
                BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }


    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        // Must be an opponent’s marble
        // Ensure list is valid, has one non-null marble, and it's an opponent's
        return validateMarbleSize(marbles) // Checks marbles != null && marbles.size() == 1
            && marbles.get(0) != null // Explicitly check the marble is not null
            && marbles.get(0).getColour() != gameManager.getActivePlayerColour();
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {
        // Validate size and non-null marble first
        if (!validateMarbleSize(marbles) || marbles.get(0) == null) {
            throw new InvalidMarbleException("Exactly one valid marble must be selected");
        }
        
        Marble m = marbles.get(0);
        
        // Validate color (opponent's marble)
        if (m.getColour() == gameManager.getActivePlayerColour()) {
            throw new InvalidMarbleException("You must select an opponent's marble");
        }
        
        // move that marble forward by 5 steps, never destroying its own
        boardManager.moveBy(m, getRank(), false);
    }
}
