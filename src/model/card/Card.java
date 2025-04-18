package model.card;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;
import java.util.ArrayList;
import model.Colour;
import exception.*;
public abstract class Card {
	private final String name;
    private final String description;
    protected BoardManager boardManager;
    protected GameManager gameManager;

    public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
        this.name = name;
        this.description = description;
        this.boardManager = boardManager;
        this.gameManager = gameManager;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
  
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null) return false;
        Colour playerColour = gameManager.getActivePlayerColour();
        for (Marble marble : marbles) {
            if (marble == null || marble.getColour() != playerColour) {
                return false;
            }
        }
        return true;
    }
    
    public abstract void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;

    
}
