package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

/**
 * Represents an abstract wild card in the Jackaroo game.
 * Wild cards introduce unique mechanics and effects that can alter the game state,
 * providing strategic opportunities for players.
 */
public abstract class Wild extends Card {

    /**
     * Constructs a wild card with the specified properties.
     * 
     * @param name         The name of the card (inherited from {@link Card}).
     * @param description  A brief description of the card's effect (inherited from {@link Card}).
     * @param boardManager The board manager for board interactions (inherited from {@link Card}).
     * @param gameManager  The game manager for managing game state (inherited from {@link Card}).
     */
    public Wild(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
}
