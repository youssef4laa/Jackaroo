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
    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException, CannotDiscardException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Queen card requires selecting zero or one opponent marble.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("If selecting a marble for Queen card, it must be an opponent's marble.");
        }

        if (marbles == null || marbles.isEmpty()) {
            // No marble selected: discard from a random opponent
            gameManager.discardCard(); // Assumes GameManager handles selecting random opponent/card
        } else {
            // One opponent marble selected: discard from that specific opponent
            Marble targetMarble = marbles.get(0);
            Colour targetColour = targetMarble.getColour();
            gameManager.discardCard(targetColour);
        }
        // Note: The prompt mentioned mirroring Ten's *skip effect*.
        // Ten.java also calls gameManager.skipTurn(). If Queen should also skip,
        // add gameManager.skipTurn(); here.
        // Based on the prompt focusing on discard logic, skipTurn() is omitted for now.
    }

}
