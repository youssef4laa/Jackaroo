package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import exception.*; // Import necessary exceptions

public class Seven extends Standard {

    public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 7, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && (marbles.size() == 1 || marbles.size() == 2);
    }


    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null || marbles.isEmpty()) {
            throw new InvalidMarbleException("At least one marble must be selected.");
        }

        if (marbles.size() == 1) {
            Marble marble = marbles.get(0);
            try {
                // try to move 7 spaces; if blocked, swallow the exception
                boardManager.moveBy(marble, 7, false);
            } catch (IllegalMovementException e) {
                // path blocked by opponent – do nothing (don’t destroy any marbles)
            }
        } else {  // size == 2
            int splitDistance = boardManager.getSplitDistance();
            Marble marble1 = marbles.get(0);
            Marble marble2 = marbles.get(1);

            // you may similarly choose to catch here if your rules require,
            // but leaving as-is unless you have tests for split-block behavior
            boardManager.moveBy(marble1, splitDistance, false);
            boardManager.moveBy(marble2, 7 - splitDistance, false);
        }
    }

}






