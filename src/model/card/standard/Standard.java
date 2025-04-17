package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.player.Marble;
import java.util.ArrayList;
import exception.*;
public class Standard extends Card {
    private final int rank;
    private final Suit suit;

    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null || marbles.size() != 1) {
            throw new InvalidMarbleException("Exactly one marble must be selected for Standard cards.");
        }

        boardManager.moveBy(marbles.get(0), this.getRank(), false);
    }



}
