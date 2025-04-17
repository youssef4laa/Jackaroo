package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import model.Colour;
import exception.*;

public class Queen extends Standard {

    public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 12, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles == null || marbles.isEmpty() || marbles.size() == 1;
    }
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.isEmpty()) {
            return true; // No marbles selected is valid
        }
        if (marbles.size() == 1) {
            Marble selectedMarble = marbles.get(0);
            if (selectedMarble == null) return false;
            // Selected marble must belong to an opponent
            return selectedMarble.getColour() != gameManager.getActivePlayerColour();
        }
        return false;
    }
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException, CannotDiscardException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Queen card requires selecting zero or one opponent marble.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("If selecting a marble for Queen card, it must be an opponent's marble.");
        }

        if (marbles == null || marbles.isEmpty()) {

            gameManager.discardCard(); 
        } else {
            Marble targetMarble = marbles.get(0);
            Colour targetColour = targetMarble.getColour();
            gameManager.discardCard(targetColour);
        }
    }

}
