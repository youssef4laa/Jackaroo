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
        // Allow selecting 1 or 2 marbles
        return marbles != null && (marbles.size() == 1 || marbles.size() == 2);
    }

    public void act(ArrayList<Marble> marbles)
            throws ActionException, InvalidMarbleException, SplitOutOfRangeException {

        if (marbles == null || marbles.isEmpty()) {
            throw new InvalidMarbleException("At least one marble must be selected");
        }

        if (marbles.size() == 1) {

            Marble m = marbles.get(0);
            boardManager.moveBy(m, 7, false);
        } else if (marbles.size() == 2) {

            int splitDistance = gameManager.editSplitDistance(7);

            Marble m1 = marbles.get(0);
            Marble m2 = marbles.get(1);


            boardManager.moveBy(m1, splitDistance, false);

            boardManager.moveBy(m2, 7 - splitDistance, false);
        } else {

            throw new InvalidMarbleException("Invalid number of marbles selected for Seven card: " + marbles.size());
        }
    }
}
