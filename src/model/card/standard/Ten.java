package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;

public class Ten extends Standard {

    public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 10, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        // Ten card does not require selecting marbles
        return marbles == null || marbles.isEmpty();
    }

    @Override
    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException {

        if (marbles != null && !marbles.isEmpty()) {
            throw new InvalidMarbleException("No marbles should be selected for the Ten card.");
        }

        gameManager.discardCard();
        gameManager.skipTurn();
    }

}
