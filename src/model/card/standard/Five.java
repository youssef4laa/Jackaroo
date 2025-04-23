package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.card.standard.Suit;
import model.player.Marble;

import java.util.ArrayList;

public class Five extends Standard {

    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException {
        boardManager.moveBy(marbles.get(0), getRank(), false);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return true; 
    }
}