package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents the Burner card in the wild card category.
 * This class extends the {@code Wild} class and initializes a Burner card 
 * with the specified attributes.
 */
public class Burner extends Wild {

    /**
     * Constructs a Burner card with the specified attributes.
     *
     * @param name        The name of the card.
     * @param description A brief description of the card.
     * @param boardManager The {@code BoardManager} instance that manages the game board.
     * @param gameManager The {@code GameManager} instance that controls the game flow.
     */
    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
}
