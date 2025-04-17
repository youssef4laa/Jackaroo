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


    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null || marbles.isEmpty()) {
            throw new InvalidMarbleException("At least one marble must be selected.");
        }

        if (marbles.size() == 1) {
            Marble marble = marbles.get(0);
            boardManager.moveBy(marble, 7, false);
        } else if (marbles.size() == 2) {
            int splitDistance = boardManager.getSplitDistance();
            Marble marble1 = marbles.get(0);
            Marble marble2 = marbles.get(1);

            boardManager.moveBy(marble1, splitDistance, false);
            boardManager.moveBy(marble2, 7 - splitDistance, false);
        } else {
            throw new InvalidMarbleException("Invalid number of marbles selected for the Seven card.");
        }
    }





}
